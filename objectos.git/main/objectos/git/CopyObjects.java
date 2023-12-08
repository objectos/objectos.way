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
import java.util.Set;
import java.util.zip.Deflater;
import objectos.concurrent.Concurrent;
import objectos.core.io.Charsets;
import objectos.fs.RegularFile;
import objectos.lang.object.Check;
import objectos.util.set.UnmodifiableSet;

final class CopyObjects extends AbstractGitEngineTask {

  static final byte _FILTER_NON_EXISTING = 1;

  static final byte IO_WRITE = 1;

  private ByteArrayWriter data;

  private Repository destination;

  private FilterNonExisting filterNonExisting;

  private UnmodifiableSet<ObjectId> nonExisting;

  private ObjectId objectId;

  private ObjectReader objectReader;

  private final ThisObjectReaderAdapter objectReaderAdapter = new ThisObjectReaderAdapter();

  private Repository repository;

  private Set<ObjectId> set;

  CopyObjects(GitInjector injector) {
    super(injector);
  }

  public final void setInput(Repository src, Set<ObjectId> objects, Repository dest) {
    checkSetInput();

    repository = Check.notNull(src, "src == null");

    set = Check.notNull(objects, "objects == null");

    destination = Check.notNull(dest, "dest == null");
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _FILTER_NON_EXISTING:
        return executeFilterNonExisting();
      case _START:
        return executeStart();
      case _STOP:
        return executeStop();
      default:
        return super.execute(state);
    }
  }

  @Override
  final byte executeFinally() {
    data = null;

    destination = null;

    filterNonExisting = injector.putFilterNonExisting(filterNonExisting);

    nonExisting = null;

    objectId = null;

    objectReader = injector.putObjectReader(objectReader);

    objectReaderAdapter.executeFinally(injector);

    repository = null;

    set = null;

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

    filterNonExisting = injector.getFilterNonExisting();

    filterNonExisting.set(destination, set);

    objectReader = injector.getObjectReader();

    objectReader.set(filterNonExisting);

    objectReaderAdapter.executeStart(injector);

    return toSubTask(objectReader, _FILTER_NON_EXISTING);
  }

  @Override
  final byte executeStop() {
    if (objectReader != null) {
      try {
        Concurrent.exhaust(objectReader);
      } catch (Throwable e) {
        catchThrowable(e);
      }
    }

    try {
      executeFinally();
    } catch (Throwable e) {
      catchThrowable(e);
    }

    return super.executeStop();
  }

  final void toWriteLooseObject(ObjectId oid, ByteArrayWriter byteArrayWriter) {
    objectId = oid;

    data = byteArrayWriter;

    state = toIo(IO_WRITE, _SUB_TASK, _FINALLY);
  }

  private byte executeFilterNonExisting() {
    UnmodifiableSet<ObjectId> result;
    result = getSubTaskResult();

    nonExisting = result;

    objectReader.set(objectReaderAdapter);

    return toSubTask(objectReader, _FINALLY);
  }

  private void ioWrite() throws IOException {
    RegularFile file;
    file = destination.createRegularFile(objectId);

    data.writeTo(file);

    data.clear();
  }

  private class ThisObjectReaderAdapter implements ObjectReaderAdapter {

    private ByteArrayWriter bytes;

    private ByteArrayWriter contents;

    private Deflater deflater;

    private ObjectReaderHandle handle;

    private ObjectId objectId;

    private final CopyObjects outer = CopyObjects.this;

    @Override
    public final void executeFinally() {
      handle.setResult(set);

      outer.setResult(set);

      handle = null;

      objectId = null;
    }

    @Override
    public final void executeObjectBodyFull(byte[] array, int length, ByteBuffer buffer) {
      contents.write(array, 0, length);
    }

    @Override
    public final void executeObjectBodyPart(ByteBuffer buffer) {
      contents.write(buffer);
    }

    @Override
    public final void executeObjectFinish() {
      contents.deflate(deflater, bytes);

      contents.clear();

      outer.toWriteLooseObject(objectId, bytes);
    }

    @Override
    public final void executeObjectHeader(ObjectKind kind, long length) {
      byte[] prefix;
      prefix = kind.headerPrefix;

      contents.write(prefix);

      String objectSizeString;
      objectSizeString = Long.toString(length, 10);

      byte[] objectSizeBytes;
      objectSizeBytes = objectSizeString.getBytes(Charsets.utf8());

      contents.write(objectSizeBytes);

      contents.write(Git.NULL);
    }

    @Override
    public final void executeObjectNotFound(ObjectId objectId) {
      handle.catchThrowable(
          new ObjectNotFoundException(objectId)
      );
    }

    @Override
    public final void executeObjectStart(ObjectId objectId) {
      Check.state(contents.isEmtpy(), "previous object not written");

      this.objectId = objectId;
    }

    @Override
    public final void executeStart(ObjectReaderHandle handle) {
      handle.setInputMany(ObjectReaderMode.READ_OBJECT, nonExisting);

      this.handle = handle;
    }

    @Override
    public final GitRepository getRepository() {
      return repository;
    }

    final void executeFinally(GitInjector injector) {
      bytes = injector.putByteArrayWriter(bytes);

      contents = injector.putByteArrayWriter(contents);

      deflater = injector.putDeflater(deflater);
    }

    final void executeStart(GitInjector injector) {
      bytes = injector.getByteArrayWriter();

      contents = injector.getByteArrayWriter();

      deflater = injector.getDeflater();
    }

  }

}