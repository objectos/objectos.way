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
/*
5 * Copyright (C) 2020-2021 Objectos Software LTDA.
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

import java.nio.ByteBuffer;
import java.util.Arrays;
import objectos.lang.object.Check;

final class ReadBlob implements ObjectReaderAdapter {

  static final byte _START = 0;

  static final byte _STOP = 0;

  byte state;

  private ByteArrayWriter blob;

  private ObjectReaderHandle handle;

  private final GitInjector injector;

  private ObjectId objectId;

  private long objectLength;

  private GitRepository repository;

  ReadBlob(GitInjector injector) {
    this.injector = injector;
  }

  @Override
  public final void executeFinally() {
    blob = injector.putByteArrayWriter(blob);

    handle = null;

    objectId = null;

    objectLength = 0;

    repository = null;

    state = _STOP;
  }

  @Override
  public final void executeObjectBodyFull(byte[] array, int length, ByteBuffer buffer) {
    if (objectLength != length) {
      handle.catchThrowable(
          new BadObjectException(
              objectId,

              "Corrupt object: declared size=" + objectLength + " actual size=" + length)
      );
    } else {
      byte[] contents;
      contents = Arrays.copyOfRange(array, 0, length);

      Blob result;
      result = new Blob(contents, objectId);

      handle.setResult(result);
    }
  }

  @Override
  public final void executeObjectBodyPart(ByteBuffer buffer) {
    if (blob == null) {
      blob = injector.getByteArrayWriter();
    }

    blob.write(buffer);

    int size;
    size = blob.size();

    if (size > objectLength) {
      handle.catchThrowable(
          new BadObjectException(
              objectId,

              "Corrupt object: declared size=" + objectLength + " actual size=" + size)
      );
    } else if (size == objectLength) {
      byte[] contents;
      contents = blob.toByteArray();

      Blob result;
      result = new Blob(contents, objectId);

      handle.setResult(result);
    }
  }

  @Override
  public final void executeObjectFinish() {
    // noop
  }

  @Override
  public final void executeObjectHeader(ObjectKind kind, long length) {
    if (kind != ObjectKind.BLOB) {
      handle.catchThrowable(
          new BadObjectException(objectId, "Not a blob object. Found " + kind)
      );
    } else {
      objectLength = length;

      state = _START;
    }
  }

  @Override
  public final void executeObjectNotFound(ObjectId objectId) {
    handle.catchThrowable(
        new ObjectNotFoundException(objectId)
    );
  }

  @Override
  public final void executeObjectStart(ObjectId id) {
    // noop
  }

  @Override
  public final void executeStart(ObjectReaderHandle handle) {
    Check.state(state == _STOP, "already started");

    handle.setInput(ObjectReaderMode.READ_OBJECT, objectId);

    this.handle = handle;
  }

  @Override
  public final GitRepository getRepository() {
    return repository;
  }

  public final void set(GitRepository repository, ObjectId objectId) {
    this.repository = Check.notNull(repository, "repository == null");

    this.objectId = Check.notNull(objectId, "objectId == null");
  }

}