/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.git;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.zip.Deflater;
import objectos.core.io.Write;
import objectos.fs.Directory;
import objectos.fs.PathNameVisitor;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;
import objectos.lang.object.ToString;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

/**
 * A class for creating a Git tree hierarchy suitable for recursively writing to
 * a repository.
 *
 * @since 2
 */
public final class MutableTree extends MutableTreeEntry {

  private static final ThisSelector SELECTOR = new ThisSelector();

  private byte[] contents;

  private final GrowableList<MutableTreeEntry> entries = new GrowableList<>();

  private final String name;

  private ObjectId objectId;

  /**
   * Creates an unnamed {@code MutableTree} instance.
   *
   * @since 3
   */
  public MutableTree() {
    name = "";
  }

  /**
   * Creates a {@code MutableTree} instance having the specified name.
   *
   * @param name
   *        the name of this tree when serving as a tree entry
   *
   * @throws IllegalArgumentException
   *         if name is an empty string
   *
   * @since 3
   */
  public MutableTree(String name) {
    this.name = Check.notNull(name, "name == null");

    Check.argument(!this.name.isEmpty(), "name must not be empty");
  }

  /**
   * Adds the specified entry to this mutable tree.
   *
   * @param entry
   *        the entry to be added
   */
  public final void addEntry(Entry entry) {
    entries.addWithNullMessage(entry, "entry == null");
  }

  /**
   * Adds the specified blob to this mutable tree.
   *
   * @param blob
   *        the blob to be added
   */
  public final void addEntry(MutableBlob blob) {
    entries.addWithNullMessage(blob, "blob == null");
  }

  /**
   * Adds the specified tree to this mutable tree.
   *
   * @param tree
   *        the tree to be added
   */
  public final void addEntry(MutableTree tree) {
    Check.argument(tree.isNamed(), "MutableTree must define a name to serve as an entry");

    entries.addWithNullMessage(tree, "tree == null");
  }

  /**
   * Removes all of the entries from this tree.
   */
  public final void clear() {
    entries.clear();
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.formatStart(sb, this);

    formatToStringImpl(null, sb, depth);

    sb.append('\n');

    ToString.formatEnd(sb, depth);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final EntryMode getMode() {
    return EntryMode.TREE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getName() {
    return name;
  }

  final void addEntry(MutableTreeEntry entry) {
    entries.addWithNullMessage(entry, "entry == null");
  }

  final void computeContents(GitInjector injector, Charset charset) throws IOException {
    ByteArrayWriter body;
    body = injector.getByteArrayWriter();

    ByteArrayWriter contents;
    contents = injector.getByteArrayWriter();

    try {
      computeContents0(injector, charset, body, contents);
    } finally {
      injector.putByteArrayWriter(body);

      injector.putByteArrayWriter(contents);
    }
  }

  @Override
  final ObjectId computeObjectId(GitInjector injector, Charset charset) throws IOException {
    computeContents(injector, charset);

    return objectId;
  }

  @Override
  final void executeWriteTree(WriteTree wt) {
    UnmodifiableList<MutableTreeEntry> sorted;
    sorted = entries.toUnmodifiableList(ORDER);

    for (int i = sorted.size() - 1; i >= 0; i--) {
      MutableTreeEntry entry;
      entry = sorted.get(i);

      entry.executeWriteTree(wt);
    }

    wt.writeMutableTree(sorted);
  }

  @Override
  final void formatToStringImpl(String prefix, StringBuilder sb, int depth) {
    sb.append('\n');

    ToString.appendIndentation(sb, depth);

    EntryMode mode;
    mode = EntryMode.TREE;

    sb.append(mode.printMode());

    sb.append(Git.SP);

    sb.append(mode.printType());

    sb.append(Git.SP);

    sb.append("0000000000000000000000000000000000000000");

    sb.append("    ");

    String newPrefix;

    if (prefix != null) {
      newPrefix = prefix + '/' + name;
    } else {
      newPrefix = name;
    }

    sb.append(newPrefix);

    for (int i = 0, size = entries.size(); i < size; i++) {
      MutableTreeEntry e;
      e = entries.get(i);

      e.formatToStringImpl(newPrefix, sb, depth);
    }
  }

  final ObjectId getObjectId() {
    return objectId;
  }

  final boolean isNamed() {
    return !name.equals("");
  }

  final IllegalArgumentException validate(StringBuilder sb) {
    IllegalArgumentException result;
    result = null;

    if (isNamed()) {
      validate0(sb, "tree is named");
    }

    if (entries.isEmpty()) {
      validate0(sb, "tree has no entries");
    }

    if (sb.length() > 0) {
      String message;
      message = sb.toString();

      sb.setLength(0);

      result = new IllegalArgumentException(message);
    }

    return result;
  }

  @Override
  final void writeTree(
                       GitInjector injector, Repository repository) throws GitStubException, IOException {
    ResolvedPath resolved;
    resolved = repository.resolveLooseObject(objectId);

    ResolvedPath maybe;
    maybe = resolved.acceptPathNameVisitor(SELECTOR, null);

    if (maybe == null) {
      return;
    }

    Write.byteArray(maybe, contents);

    for (int i = 0, size = entries.size(); i < size; i++) {
      MutableTreeEntry entry;
      entry = entries.get(i);

      entry.writeTree(injector, repository);
    }
  }

  private void computeContents0(
                                GitInjector injector, Charset charset, ByteArrayWriter body, ByteArrayWriter contents)
                                                                                                                       throws IOException {
    entries.sort(MutableTreeEntry.ORDER);

    for (int i = 0, size = entries.size(); i < size; i++) {
      MutableTreeEntry entry;
      entry = entries.get(i);

      EntryMode mode;
      mode = entry.getMode();

      body.write(mode.getBytes());

      body.write(Git.UTF8__space);

      String name;
      name = entry.getName();

      byte[] nameBytes;
      nameBytes = name.getBytes(charset);

      body.write(nameBytes);

      body.write(Git.NULL);

      ObjectId id;
      id = entry.computeObjectId(injector, charset);

      body.write(id.getBytes());
    }

    byte[] prefix;
    prefix = ObjectKind.TREE.headerPrefix;

    contents.write(prefix);

    String bodySize;
    bodySize = Integer.toString(body.size(), 10);

    byte[] objectSizeBytes;
    objectSizeBytes = bodySize.getBytes(charset);

    contents.write(objectSizeBytes);

    contents.write(Git.NULL);

    contents.write(body);

    MessageDigest messageDigest;
    messageDigest = injector.getMessageDigest(Git.SHA1);

    objectId = contents.computeObjectId(messageDigest);

    Deflater deflater;
    deflater = injector.getDeflater();

    ByteArrayWriter compressed;
    compressed = body.clear();

    contents.deflate(deflater, compressed);

    injector.putDeflater(deflater);

    this.contents = compressed.toByteArray();
  }

  private void validate0(StringBuilder sb, String reason) {
    if (sb.length() == 0) {
      sb.append("Refuse to write tree for the following reason(s): ");
    } else {
      sb.append(", ");
    }

    sb.append(reason);
  }

  private static class ThisSelector implements PathNameVisitor<ResolvedPath, Void> {

    @Override
    public final ResolvedPath visitDirectory(Directory directory, Void p) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final ResolvedPath visitNotFound(ResolvedPath notFound, Void p) throws IOException {
      return notFound.createParents();
    }

    @Override
    public final ResolvedPath visitRegularFile(RegularFile file, Void p) {
      // do nothing, tree exists
      return null;
    }

  }

}