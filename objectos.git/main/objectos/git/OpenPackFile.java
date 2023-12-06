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
import objectos.core.io.Charsets;
import objectos.fs.Directory;
import objectos.fs.NotRegularFileException;
import objectos.fs.PathNameVisitor;
import objectos.fs.ReadableFileChannelSource;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;

/**
 * Opens a Git packfile. Resolves the pathname and confirms (or not) that the
 * file exist. Validates the header and (maybe) the checksum. Opens the
 * associated index file (if one exists).
 *
 * <p>
 * Apart from validating the header and (maybe) the checksum, does not do any
 * further reading.
 *
 * @since 3
 */
final class OpenPackFile extends AbstractGitEngineTask {

  private static final byte _IDX_HEADER = 2;

  private static final byte _PACK_HEADER = 3;

  private static final int IDX_LENGTH = 4 + 4;

  private static final byte IO_IDX = 1;

  private static final byte IO_PACK = 2;

  private static final int PACK_IDX2_MAGIC = 0xff744f63;

  private static final int PACK_LENGTH = 4 + 4 + 4;

  private static final byte[] PACK_SIGNATURE = "PACK".getBytes(Charsets.usAscii());

  private ByteBuffer channelBuffer;

  private RegularFile indexFile;

  private int objectCount;

  private ObjectId objectId;

  private RegularFile packFile;

  private long packFileSize;

  private Directory repository;

  private int version;

  OpenPackFile(GitInjector injector) {
    super(injector);
  }

  public final void set(Directory objectsDirectory, ObjectId objectId) {
    checkSetInput();

    this.repository = Check.notNull(objectsDirectory, "objectsDirectory == null");

    this.objectId = Check.notNull(objectId, "objectId == null");
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _IDX_HEADER:
        return executeIdxHeader();
      case _PACK_HEADER:
        return executePackHeader();
      default:
        return super.execute(state);
    }
  }

  @Override
  final byte executeFinally() {
    channelBuffer = injector.putByteBuffer(channelBuffer);

    indexFile = null;

    objectCount = 0;

    objectId = null;

    packFile = null;

    packFileSize = 0;

    repository = null;

    version = 0;

    return super.executeFinally();
  }

  @Override
  final void executeIo(byte task) throws GitStubException, IOException {
    switch (task) {
      case IO_IDX:
        ioIdx();
        break;
      case IO_PACK:
        ioPack();
        break;
      default:
        super.executeIo(task);
        break;
    }
  }

  @Override
  final byte executeStart() {
    super.executeStart();

    channelBuffer = injector.getByteBuffer();

    channelBuffer.clear();

    channelBuffer.limit(PACK_LENGTH);

    return toIo(IO_PACK, _PACK_HEADER, _FINALLY);
  }

  final void openIdx(RegularFile file) throws IOException {
    indexFile = file;

    ioRead(indexFile);
  }

  private byte executeIdxHeader() {
    int magic;
    magic = channelBuffer.getInt();

    if (magic != PACK_IDX2_MAGIC) {
      return toBadObject("Invalid or corrupt index file: " + indexFile.getPath());
    }

    int idxVersion;
    idxVersion = channelBuffer.getInt();

    if (idxVersion != 2) {
      return toBadObject("Invalid or corrupt index file: " + indexFile.getPath());
    }

    setResult(
        new IndexedPackFile(indexFile, objectCount, objectId, packFile, packFileSize, version)
    );

    return _FINALLY;
  }

  private byte executePackHeader() {
    if (!Git.matches(channelBuffer, PACK_SIGNATURE)) {
      return toBadObject("PACK signature not found");
    }

    version = channelBuffer.getInt();

    objectCount = channelBuffer.getInt();

    channelBuffer.clear();

    channelBuffer.limit(IDX_LENGTH);

    return toIo(IO_IDX, _IDX_HEADER, _FINALLY);
  }

  private void ioIdx() throws IOException {
    String fileName;
    fileName = objectId.toString("pack-", ".idx");

    ResolvedPath maybeIdx;
    maybeIdx = repository.resolve("pack", fileName);

    maybeIdx.acceptPathNameVisitor(IoIdx.INSTANCE, this);
  }

  private void ioPack() throws IOException {
    String fileName;
    fileName = objectId.toString("pack-", ".pack");

    ResolvedPath maybePackFile;
    maybePackFile = repository.resolve("pack", fileName);

    // fail if not a regular file
    packFile = maybePackFile.toRegularFile();

    packFileSize = packFile.size();

    ioRead(packFile);
  }

  private void ioRead(ReadableFileChannelSource source) throws IOException {
    try (var channel = source.openReadChannel()) {
      channel.position(0);

      channel.read(channelBuffer);

      channelBuffer.flip();
    }
  }

  private byte toBadObject(String message) {
    return toError(
        new BadObjectException(objectId, message)
    );
  }

  private enum IoIdx implements PathNameVisitor<Void, OpenPackFile> {

    INSTANCE;

    @Override
    public final Void visitDirectory(Directory directory, OpenPackFile p) throws IOException {
      p.catchThrowable(
          new NotRegularFileException(directory)
      );

      return null;
    }

    @Override
    public final Void visitNotFound(ResolvedPath notFound, OpenPackFile p) throws IOException {
      p.ioReady(_FINALLY);

      return null;
    }

    @Override
    public final Void visitRegularFile(RegularFile file, OpenPackFile p) throws IOException {
      p.openIdx(file);

      return null;
    }

  }
}