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

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import objectos.lang.object.Check;
import objectos.notes.Note1;
import objectos.notes.NoteSink;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

final class ReadTree implements ObjectReaderAdapter {

  static final Note1<ObjectId> ESTART;

  static final Note1<ObjectId> ESUCCESS;

  static {
    Class<?> source;
    source = ReadTree.class;

    ESTART = Note1.debug(source, "Start");

    ESUCCESS = Note1.debug(source, "Success");
  }

  static final byte _NEXT_ENTRY = 1;

  static final byte _PARSE_MODE = 2;

  static final byte _PARSE_NAME = 3;

  static final byte _PARSE_OBJECT_ID = 4;

  static final byte _RESULT = 5;

  static final byte _START = 6;

  static final byte _STOP = 0;

  byte state;

  private boolean active;

  private String badObjectMessage;

  private ByteArrayWriter byteArrayWriter;

  private byte[] data;

  private int dataIndex;

  private int dataLength;

  private CharsetDecoder decoder;

  private GrowableList<Entry> entries;

  private ObjectReaderHandle handle;

  private final GitInjector injector;

  private NoteSink logger;

  private EntryMode mode;

  private int modeOctal;

  private String name;

  private ObjectId objectId;

  private long objectLength;

  private long objectPosition;

  private GitRepository repository;

  ReadTree(GitInjector injector) {
    this.injector = injector;
  }

  @Override
  public final void executeFinally() {
    active = false;

    byteArrayWriter = injector.putByteArrayWriter(byteArrayWriter);

    data = null;

    dataIndex = 0;

    dataLength = 0;

    decoder = injector.putDecoder(decoder);

    entries = injector.putGrowableList(entries);

    handle = null;

    logger = null;

    mode = null;

    modeOctal = 0;

    name = null;

    objectId = null;

    objectLength = 0;

    objectPosition = 0;

    repository = null;

    state = _STOP;
  }

  @Override
  public final void executeObjectBodyFull(byte[] array, int length, ByteBuffer buffer) {
    Check.state(!active, "already active");

    data = array;

    dataIndex = 0;

    dataLength = length;

    objectPosition = length;

    onDataReady();
  }

  @Override
  public final void executeObjectBodyPart(ByteBuffer buffer) {
    Check.state(!active, "already active");

    data = buffer.array();

    dataIndex = buffer.position();

    dataLength = buffer.limit();

    buffer.position(buffer.limit());

    objectPosition += dataLength - dataIndex;

    onDataReady();
  }

  @Override
  public final void executeObjectFinish() {
    // noop
  }

  @Override
  public final void executeObjectHeader(ObjectKind kind, long length) {
    if (kind != ObjectKind.TREE) {
      handle.catchThrowable(
          new BadObjectException(objectId, "Not a tree object. Found " + kind)
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

  private void execute() {
    try {
      while (active) {
        state = execute(state);
      }
    } catch (Throwable e) {
      toError(e);
    }
  }

  private byte execute(byte state) {
    switch (state) {
      case _NEXT_ENTRY:
        return executeNextEntry();
      case _PARSE_MODE:
        return executeParseMode();
      case _PARSE_NAME:
        return executeParseName();
      case _PARSE_OBJECT_ID:
        return executeParseObjectId();
      case _RESULT:
        return executeResult();
      case _START:
        return executeStart();
      default:
        throw new UnsupportedOperationException("Implement me: state=" + state);
    }
  }

  private byte executeNextEntry() {
    int remaining;
    remaining = dataLength - dataIndex;

    if (remaining > 0 || (remaining == 0 && hasRemaining())) {
      modeOctal = 0;

      byteArrayWriter.clear();

      return _PARSE_MODE;
    } else {
      return _RESULT;
    }
  }

  private byte executeParseMode() {
    badObjectMessage = "Failed to parse entry file mode";

    byte b;

    boolean found;
    found = false;

    while (dataIndex < dataLength) {
      b = data[dataIndex++];

      if (b == Git.UTF8__space) {
        found = true;

        break;
      }

      int digit;
      digit = Utf8.parseInt(b);

      modeOctal *= 8;

      modeOctal += digit;
    }

    if (!found) {
      return toNeedsInput();
    }

    switch (modeOctal) {
      case 0100644:
        mode = EntryMode.REGULAR_FILE;
        break;
      case 0100755:
        mode = EntryMode.EXECUTABLE_FILE;
        break;
      case 040000:
        mode = EntryMode.TREE;
        break;
      case 0160000:
        mode = EntryMode.COMMIT;
        break;
      default:
        return toError(
            new GitStubException("mode = " + Integer.toOctalString(modeOctal))
        );
    }

    return _PARSE_NAME;
  }

  private byte executeParseName() {
    badObjectMessage = "Failed to parse entry name";

    byte b;

    boolean found;
    found = false;

    while (dataIndex < dataLength) {
      b = data[dataIndex++];

      if (b == Git.NULL) {
        found = true;

        break;
      }

      byteArrayWriter.write(b);
    }

    if (!found) {
      return toNeedsInput();
    }

    try {
      name = byteArrayWriter.toString(decoder);

      byteArrayWriter.clear();

      return _PARSE_OBJECT_ID;
    } catch (CharacterCodingException e) {
      return toError(e);
    }
  }

  private byte executeParseObjectId() {
    badObjectMessage = "Failed to parse entry object id";

    while (dataIndex < dataLength && byteArrayWriter.size() < 20) {
      byte b;
      b = data[dataIndex++];

      byteArrayWriter.write(b);
    }

    if (byteArrayWriter.size() < 20) {
      return toNeedsInput();
    }

    byte[] bytes;
    bytes = byteArrayWriter.toByteArray();

    ObjectId id;
    id = ObjectId.fromByteArray(bytes);

    Entry entry;
    entry = new Entry(mode, name, id);

    entries.add(entry);

    return _NEXT_ENTRY;
  }

  private byte executeResult() {
    UnmodifiableList<Entry> list;
    list = entries.toUnmodifiableList();

    Tree result;
    result = new Tree(list, objectId);

    handle.setResult(result);

    active = false;

    logger.send(ESUCCESS, objectId);

    return state;
  }

  private byte executeStart() {
    byteArrayWriter = injector.getByteArrayWriter();

    Charset charset;
    charset = repository.getCharset();

    decoder = injector.getDecoder(charset);

    entries = injector.getGrowableList();

    logger = injector.getLogger();

    logger.send(ESTART, objectId);

    return _PARSE_MODE;
  }

  private boolean hasRemaining() {
    return objectPosition < objectLength;
  }

  private void onDataReady() {
    if (objectPosition > objectLength) {
      handle.catchThrowable(
          new BadObjectException(
              objectId,

              "Corrupt object: declared size=" + objectLength + " actual size=" + objectPosition)
      );
    } else {
      active = true;

      execute();
    }
  }

  private byte toBadObject() {
    return toError(
        new BadObjectException(objectId, badObjectMessage)
    );
  }

  private byte toError(Throwable e) {
    handle.catchThrowable(e);

    active = false;

    return state;
  }

  private byte toNeedsInput() {
    if (hasRemaining()) {
      active = false;

      return state;
    } else {
      return toBadObject();
    }
  }

}