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
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.zip.Deflater;
import objectos.fs.Directory;
import objectos.fs.NotRegularFileException;
import objectos.fs.PathNameVisitor;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.notes.Note2;
import objectos.util.list.UnmodifiableList;

final class WriteTree extends AbstractGitEngineTask {

  static final Note2<GitCommand<?>, MutableTree> ESTART;

  static final Note2<GitCommand<?>, ObjectId> ESUCCESS;

  static {
    Class<?> source;
    source = WriteTree.class;

    ESTART = Note2.debug(source, "Start");

    ESUCCESS = Note2.debug(source, "Success");
  }

  private static final byte _ASSEMBLE = 1;

  private static final byte _RESULT = 2;

  private static final byte IO_WRITE = 1;

  private static final byte REND = 1;

  private static final byte RIMMUTABLE = 2;

  private static final byte RMUTABLE = 3;

  private ByteBuffer byteBuffer;

  private Charset charset;

  private IntStack dataStack;

  private ByteArrayWriter dataTape;

  private Deflater deflater;

  private MessageDigest messageDigest;

  private ByteArrayWriter objectContents;

  private ObjectId objectId;

  private final byte[] objectIdBytes = new byte[ObjectId.BYTE_LENGTH];

  private int objectSize;

  private Repository repository;

  private StringBuilder stringBuilder;

  private MutableTree tree;

  private ByteArrayWriter treeBody;

  WriteTree(GitInjector injector) {
    super(injector);
  }

  public final void setInput(GitCommand<?> command, Repository repository, MutableTree tree) {
    checkSetInput();

    this.command = command;

    this.repository = repository;

    this.tree = tree;
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _ASSEMBLE:
        return executeAssemble();
      case _RESULT:
        return executeResult();
      default:
        return super.execute(state);
    }
  }

  @Override
  final byte executeFinally() {
    byteBuffer = null;

    charset = null;

    dataStack = injector.putIntStack(dataStack);

    dataTape = injector.putByteArrayWriter(dataTape);

    deflater = injector.putDeflater(deflater);

    messageDigest = injector.putMessageDigest(messageDigest);

    objectContents = injector.putByteArrayWriter(objectContents);

    objectId = null;

    Arrays.fill(objectIdBytes, (byte) 0);

    objectSize = 0;

    repository = null;

    stringBuilder = injector.putStringBuilder(stringBuilder);

    tree = null;

    treeBody = injector.putByteArrayWriter(treeBody);

    return super.executeFinally();
  }

  @Override
  final void executeIo(byte task) throws IOException {
    switch (task) {
      case IO_WRITE:
        ioWrite();
        break;
      default:
        super.executeIo(task);
        break;
    }
  }

  @Override
  final byte executeStart() {
    super.executeStart();

    stringBuilder = injector.getStringBuilder();

    IllegalArgumentException maybe;
    maybe = tree.validate(stringBuilder);

    if (maybe != null) {
      return toError(maybe);
    } else {
      charset = repository.getCharset();

      dataTape = injector.getByteArrayWriter();

      dataStack = injector.getIntStack();

      deflater = injector.getDeflater();

      messageDigest = injector.getMessageDigest(Git.SHA1);

      objectContents = injector.getByteArrayWriter();

      treeBody = injector.getByteArrayWriter();

      send(ESTART, command, tree);

      return _ASSEMBLE;
    }
  }

  final void skipObject() {
    byteBuffer.position(
        byteBuffer.position() + objectSize
    );
  }

  final void write(ResolvedPath notFound) throws IOException {
    notFound.createParents();

    try (FileChannel channel = notFound.openWriteChannel()) {
      int limit;
      limit = byteBuffer.limit();

      byteBuffer.limit(byteBuffer.position() + objectSize);

      while (byteBuffer.hasRemaining()) {
        channel.write(byteBuffer);
      }

      byteBuffer.limit(limit);
    }
  }

  final void writeImmutable(ObjectId object) {
    dataTape.write(RIMMUTABLE);

    dataStack.push(dataTape.size());

    dataTape.write(object.getBytes());
  }

  final void writeMutableBlob(byte[] contents) {
    writeMutable0(ObjectKind.BLOB, contents.length);

    objectContents.write(contents);

    writeMutable1();
  }

  final void writeMutableTree(UnmodifiableList<MutableTreeEntry> sortedEntries) {
    treeBody.clear();

    for (int i = 0, size = sortedEntries.size(); i < size; i++) {
      MutableTreeEntry entry;
      entry = sortedEntries.get(i);

      EntryMode mode;
      mode = entry.getMode();

      String name;
      name = entry.getName();

      treeBody.write(mode.getBytes());

      treeBody.write(Git.UTF8__space);

      byte[] nameBytes;
      nameBytes = name.getBytes(charset);

      treeBody.write(nameBytes);

      treeBody.write(Git.NULL);

      int offset;
      offset = dataStack.pop();

      treeBody.write(dataTape, offset, ObjectId.BYTE_LENGTH);
    }

    writeMutable0(ObjectKind.TREE, treeBody.size());

    objectContents.write(treeBody);

    writeMutable1();
  }

  private byte executeAssemble() {
    tree.executeWriteTree(this);

    dataTape.write(REND);

    byteBuffer = dataTape.asByteBuffer();

    return toIo(IO_WRITE, _RESULT, _FINALLY);
  }

  private byte executeResult() {
    setResult(objectId);

    send(ESUCCESS, command, objectId);

    return _FINALLY;
  }

  private void ioWrite() throws IOException {
    while (byteBuffer.hasRemaining()) {
      byte code;
      code = byteBuffer.get();

      switch (code) {
        case REND:
          // noop
          break;
        case RIMMUTABLE:
          ioWriteImmutable();
          break;
        case RMUTABLE:
          ioWriteMutable();
          break;
        default:
          throw new AssertionError("Unexpected record type: code=" + code);
      }
    }
  }

  private void ioWriteImmutable() {
    byteBuffer.position(
        byteBuffer.position() + ObjectId.BYTE_LENGTH
    );
  }

  private void ioWriteMutable() throws IOException {
    byteBuffer.get(objectIdBytes);

    objectSize = byteBuffer.getInt();

    objectId = ObjectId.copyOf(objectIdBytes);

    ResolvedPath loose;
    loose = repository.resolveLooseObject(objectId);

    loose.acceptPathNameVisitor(IoWriteMutable.INSTANCE, this);
  }

  private void writeMutable0(ObjectKind kind, int size) {
    objectContents.clear();

    byte[] prefix;
    prefix = kind.headerPrefix;

    objectContents.write(prefix);

    String bodySize;
    bodySize = Integer.toString(size, 10);

    byte[] objectSizeBytes;
    objectSizeBytes = bodySize.getBytes(charset);

    objectContents.write(objectSizeBytes);

    objectContents.write(Git.NULL);
  }

  private void writeMutable1() {
    messageDigest.reset();

    objectContents.update(messageDigest);

    byte[] hash;
    hash = messageDigest.digest();

    dataTape.write(RMUTABLE);

    dataStack.push(dataTape.size());

    dataTape.write(hash);

    int offset;
    offset = dataTape.size();

    byte zero;
    zero = 0;

    dataTape.write(zero);
    dataTape.write(zero);
    dataTape.write(zero);
    dataTape.write(zero);

    int size;
    size = dataTape.size();

    objectContents.deflate(deflater, dataTape);

    size = dataTape.size() - size;

    dataTape.putInt(offset, size);
  }

  private enum IoWriteMutable implements PathNameVisitor<Void, WriteTree> {
    INSTANCE;

    @Override
    public final Void visitDirectory(Directory directory, WriteTree p) throws IOException {
      throw new NotRegularFileException(directory);
    }

    @Override
    public final Void visitNotFound(ResolvedPath notFound, WriteTree p) throws IOException {
      p.write(notFound);

      return null;
    }

    @Override
    public final Void visitRegularFile(RegularFile file, WriteTree p) throws IOException {
      p.skipObject();

      return null;
    }
  }

}