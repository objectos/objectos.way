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
import objectos.fs.Posix;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.ToString;

/**
 * A Git tree entry resulting from a <em>read tree</em> operation.
 *
 * @since 1
 */
public final class Entry extends MutableTreeEntry {

  final EntryMode mode;

  final String name;

  final ObjectId object;

  MutableTree parent;

  Entry(EntryMode mode, String name, ObjectId object) {
    this.mode = mode;
    this.name = name;
    this.object = object;
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.formatStart(sb, this);

    formatToStringImpl(null, sb, depth);

    sb.append('\n');

    ToString.formatEnd(sb, depth);
  }

  /**
   * Returns the file mode of this tree entry.
   *
   * @return the file mode of this tree entry
   */
  @Override
  public final EntryMode getMode() {
    return mode;
  }

  /**
   * Returns the name of this tree entry.
   *
   * @return the name of this tree entry
   */
  @Override
  public final String getName() {
    return name;
  }

  /**
   * Returns an object representing the hash value that uniquely identifies the
   * git object referenced by this tree entry.
   *
   * @return an object representing the hash value
   */
  public final ObjectId getObjectId() {
    return object;
  }

  /**
   * Returns {@code true} if this tree entry references a blob object.
   *
   * @return {@code true} if this tree entry references a blob object
   */
  public final boolean isBlob() {
    return mode.isBlob();
  }

  /**
   * Returns {@code true} if this tree entry references a tree object.
   *
   * @return {@code true} if this tree entry references a tree object
   */
  public final boolean isTree() {
    return mode.isTree();
  }

  /**
   * Returns a string representation of the form
   * {@code octal blob|tree hash name}.
   *
   * @return a string representation
   */
  public final String print() {
    StringBuilder out;
    out = new StringBuilder();

    print0(out);

    out.append(name);

    return out.toString();
  }

  @Override
  final ObjectId computeObjectId(GitInjector injector, Charset charset) {
    return getObjectId();
  }

  final RegularFile createRegularFile(ResolvedPath notFound) throws IOException {
    switch (mode) {
      case EXECUTABLE_FILE:
        return notFound.createRegularFile(
            Posix.ownerReadable(),
            Posix.ownerWritable(),
            Posix.ownerExecutable()
        );
      case REGULAR_FILE:
        return notFound.createRegularFile();
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  final void executeWriteTree(WriteTree wt) {
    wt.writeImmutable(object);
  }

  @Override
  final void formatToStringImpl(String prefix, StringBuilder sb, int depth) {
    sb.append('\n');

    ToString.appendIndentation(sb, depth);

    print0(sb);

    if (prefix != null) {
      sb.append(prefix);

      sb.append('/');
    }

    sb.append(name);
  }

  private void print0(StringBuilder out) {
    out.append(mode.printMode());

    out.append(Git.SP);

    out.append(mode.printType());

    out.append(Git.SP);

    out.append(object.print());

    out.append("    ");
  }

}