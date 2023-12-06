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
import java.util.Iterator;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import objectos.fs.Directory;
import objectos.fs.NotRegularFileException;
import objectos.fs.PathNameVisitor;
import objectos.fs.ReadableFileChannelSource;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.util.set.GrowableSet;
import objectos.util.set.UnmodifiableSet;

/**
 * A state machine for reading Git objects from a repository. Supports:
 *
 * <ul>
 * <li>loose objects</li>
 * <li>pack files v2 (if it contains a v2 index file)</li>
 * </ul>
 *
 * @since 3
 */
final class ObjectReader extends AbstractGitEngineTask implements ObjectReaderHandle {

  static final Note0 EIO_CLOSE;

  static final Note1<Long> EIO_OPEN;

  static final Note2<Integer, Long> EIO_READ;

  static final Note1<ObjectId> ENEXT_OBJECT;

  static {
    Class<?> source;
    source = ObjectReader.class;

    EIO_CLOSE = Note0.trace(source, "I/O close");

    EIO_OPEN = Note1.trace(source, "I/O open");

    EIO_READ = Note2.trace(source, "I/O read");

    ENEXT_OBJECT = Note1.debug(source, "Next object");
  }

  static final byte _BASE_OBJECT = 1;

  static final byte _CLOSE = 2;

  static final byte _DELTA_ARRAY = 3;

  static final byte _HEADER = 5;

  static final byte _INFLATE = 6;

  static final byte _IO_INDEX_FAN_OUT = 7;

  static final byte _NEXT_LOOSE_OBJECT = 8;

  static final byte _NEXT_PACK_FILE = 9;

  static final byte _NEXT_PACK_FILE_OBJECT = 10;

  static final byte _OBJECT_DATA = 11;

  static final byte _OBJECT_DATA_FULL = 12;

  static final byte _PARSE_INDEX_FAN_OUT = 13;

  static final byte _PARSE_INDEX_OBJECT_NAME = 14;

  static final byte _PARSE_INDEX_OFFSET = 15;

  static final byte _PARSE_PACK_HEADER = 16;

  static final byte _RECONSTRUCT_OBJECT = 17;

  static final byte IO_CLOSE = 1;

  static final byte IO_OPEN_LOOSE = 2;

  static final byte IO_READ_AUX = 3;

  static final byte IO_READ_MAIN = 4;

  /**
   * Offset to the fan-out table.
   */
  private static final int OFFSET_FAN_OUT = 4 + 4;

  /**
   * Offset to the object names table.
   */
  private static final int OFFSET_OBJECT_NAMES = 4 + 4 + (256 * 4);

  ByteArrayWriter baseObject;

  long channelPosition;

  long channelReadCount;

  long channelReadLimit;

  ByteArrayWriter deltaArray;

  IntStack deltaStack;

  Inflater inflater;

  ByteBuffer inflaterBuffer;

  long objectLength;

  long objectPosition;

  private ObjectReaderAdapter adapter;

  private final ThrowableMessageBuilder badObjectMessage = new ThrowableMessageBuilder();

  private FileChannel channelAux;

  private ReadableFileChannelSource channelAuxSource;

  private ByteBuffer channelBuffer;

  private FileChannel channelMain;

  private ReadableFileChannelSource channelMainSource;

  private int deltaLimit;

  private int deltaOffset;

  private byte inflateAction;

  private ObjectReaderMode mode;

  private ObjectId objectId;

  private ObjectKind objectKind;

  private Iterator<ObjectId> objects;

  private GrowableSet<ObjectId> objectsNotFound;

  private PackFile packFile;

  private int packFileIndex;

  private ByteArrayWriter reconstructedObject = new ByteArrayWriter();

  private GitRepository repository;

  ObjectReader(GitInjector injector) {
    super(injector);
  }

  public final void set(ObjectReaderAdapter adapter) {
    checkSetInput();

    this.adapter = Check.notNull(adapter, "adapter == null");
  }

  @Override
  public final void setInput(ObjectReaderMode mode, ObjectId single) {
    this.mode = Check.notNull(mode, "mode == null");

    objectId = Check.notNull(single, "single == null");

    objects = null;
  }

  @Override
  public final void setInputMany(ObjectReaderMode mode, Iterable<ObjectId> many) {
    this.mode = Check.notNull(mode, "mode == null");

    objects = many.iterator();

    objectId = null;
  }

  @Override
  final byte errorState() {
    return _CLOSE;
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _BASE_OBJECT:
        return executeBaseObject();
      case _CLOSE:
        return executeClose();
      case _DELTA_ARRAY:
        return executeDeltaArray();
      case _HEADER:
        return executeHeader();
      case _INFLATE:
        return executeInflate();
      case _IO_INDEX_FAN_OUT:
        return executeIoIndexFanOut();
      case _NEXT_LOOSE_OBJECT:
        return executeNextLooseObject();
      case _NEXT_PACK_FILE:
        return executeNextPackFile();
      case _NEXT_PACK_FILE_OBJECT:
        return executeNextPackFileObject();
      case _OBJECT_DATA:
        return executeObjectData();
      case _OBJECT_DATA_FULL:
        return executeObjectDataFull();
      case _PARSE_INDEX_FAN_OUT:
        return executeParseIndexFanOut();
      case _PARSE_INDEX_OBJECT_NAME:
        return executeParseIndexObjectName();
      case _PARSE_INDEX_OFFSET:
        return executeParseIndexOffset();
      case _PARSE_PACK_HEADER:
        return executeParsePackHeader();
      case _RECONSTRUCT_OBJECT:
        return executeReconstructObject();
      case _STOP:
        return executeStop();
      default:
        return super.execute(state);
    }
  }

  @Override
  final byte executeFinally() {
    try {
      badObjectMessage.clear();

      baseObject = injector.putByteArrayWriter(baseObject);

      channelAux = null;

      channelAuxSource = null;

      channelBuffer = injector.putByteBuffer(channelBuffer);

      channelMain = null;

      channelMainSource = null;

      channelPosition = 0;

      channelReadCount = 0;

      channelReadLimit = 0;

      deltaArray = injector.putByteArrayWriter(deltaArray);

      deltaLimit = 0;

      deltaOffset = 0;

      deltaStack = injector.putIntStack(deltaStack);

      inflater = injector.putInflater(inflater);

      inflateAction = 0;

      inflaterBuffer = injector.putByteBuffer(inflaterBuffer);

      mode = null;

      objectId = null;

      objectKind = null;

      objectLength = 0;

      objectPosition = 0;

      objects = null;

      objectsNotFound = injector.putGrowableSet(objectsNotFound);

      packFile = null;

      packFileIndex = 0;

      reconstructedObject = injector.putByteArrayWriter(reconstructedObject);

      repository = null;
    } catch (Throwable e) {
      catchThrowable(e);
    }

    try {
      adapter.executeFinally();
    } catch (Throwable e) {
      catchThrowable(e);
    }

    return super.executeFinally();
  }

  @Override
  final void executeIo(byte ioTask) throws GitStubException, IOException {
    switch (ioTask) {
      case IO_CLOSE:
        ioClose();
        break;
      case IO_OPEN_LOOSE:
        ioOpenLoose();
        break;
      case IO_READ_AUX:
        ioReadAux();
        break;
      case IO_READ_MAIN:
        ioReadMain();
        break;
      default:
        super.executeIo(ioTask);
        break;
    }
  }

  @Override
  final byte executeStart() {
    super.executeStart();

    baseObject = injector.getByteArrayWriter();

    channelBuffer = injector.getByteBuffer();

    deltaArray = injector.getByteArrayWriter();

    deltaStack = injector.getIntStack();

    inflater = injector.getInflater();

    inflaterBuffer = injector.getByteBuffer();

    objectsNotFound = injector.getGrowableSet();

    reconstructedObject = injector.getByteArrayWriter();

    repository = adapter.getRepository();

    adapter.executeStart(this);

    return _NEXT_PACK_FILE;
  }

  @Override
  final byte executeStop() {
    if (channelAux != null || channelMain != null) {
      try {
        ioClose();
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

  final void looseObjectFound(RegularFile file) throws IOException {
    adapter.executeObjectStart(objectId);

    switch (mode) {
      case EXISTS:
        ioReady(_NEXT_LOOSE_OBJECT);

        break;
      case READ_OBJECT:
        if (channelMain != null) {
          throw new IOException("channelMain != null");
        }

        channelMain = file.openReadChannel();

        long fileSize;
        fileSize = channelMain.size();

        channelPosition(0, fileSize);

        ioRead(channelMain);

        break;
      default:
        throw new AssertionError("Unexpected mode=" + mode);
    }
  }

  final void looseObjectNotFound() {
    adapter.executeObjectNotFound(objectId);

    objectId = null;

    ioReady(_NEXT_LOOSE_OBJECT);
  }

  private void channelPosition(long position, long limit) {
    channelBuffer.clear();

    channelPosition = position;

    channelReadCount = 0;

    channelReadLimit = limit;
  }

  private byte executeBaseObject() {
    baseObject.write(inflaterBuffer);

    objectPosition = baseObject.size();

    if (objectPosition < objectLength) {
      return toReadAndInflate(_BASE_OBJECT);
    } else {
      deltaLimit = deltaArray.size();

      deltaOffset = deltaStack.pop();

      return _RECONSTRUCT_OBJECT;
    }
  }

  private byte executeClose() {
    if (channelAux != null || channelMain != null) {
      return toIo(IO_CLOSE, _FINALLY, _FINALLY);
    } else {
      return _FINALLY;
    }
  }

  private byte executeDeltaArray() {
    objectPosition += inflaterBuffer.remaining();

    deltaArray.write(inflaterBuffer);

    if (objectPosition < objectLength) {
      return toReadAndInflate(_DELTA_ARRAY);
    } else {
      int arrayOffset;
      arrayOffset = deltaStack.peek();

      long position;
      position = deltaArray.getLong(arrayOffset);

      long fileSize;
      fileSize = packFile.getPackFileSize();

      channelPosition(position, fileSize - position);

      channelBuffer.clear();

      channelReadCount = 0;

      inflater.reset();

      inflaterBuffer.clear();

      return toIo(IO_READ_MAIN, _PARSE_PACK_HEADER);
    }
  }

  private byte executeHeader() {
    byte[] array;
    array = inflaterBuffer.array();

    int index;
    index = inflaterBuffer.position();

    int limit;
    limit = inflaterBuffer.limit();

    byte b;
    b = array[index++];

    ObjectKind candidate;

    switch (b) {
      default:
        badObjectMessage
            .clear()
            .append("Failed to parse a loose object header").nl()
            .nl()
            .append("GIT object type starts with an unexpected byte").nl()
            .nl()
            .keyValue("byte", Byte.toString(b)).nl()
            .nl()
            .keyValue("adapter", adapter).nl()
            .keyValue("objectId", objectId).nl()
            .nl();

        return toBadObject();
      case ObjectKind.BLOB_FIRST_BYTE:
        candidate = ObjectKind.BLOB;
        break;
      case ObjectKind.COMMIT_FIRST_BYTE:
        candidate = ObjectKind.COMMIT;
        break;
      case ObjectKind.TREE_FIRST_BYTE:
        candidate = ObjectKind.TREE;
        break;
    }

    byte[] headerPrefix;
    headerPrefix = candidate.headerPrefix;

    int remaining;
    remaining = limit - index;

    if (remaining < headerPrefix.length - 1) {
      badObjectMessage
          .clear()
          .append("Failed to parse a loose object header").nl()
          .nl()
          .append("GIT object type shorter than expected").nl()
          .nl()
          .keyValue("candidate", candidate).nl()
          .keyValue("remaining", remaining).nl()
          .nl()
          .keyValue("adapter", adapter).nl()
          .keyValue("objectId", objectId).nl()
          .nl();

      return toBadObject();
    }

    for (int i = 1; i < headerPrefix.length; i++) {
      b = array[index++];

      if (b != headerPrefix[i]) {
        badObjectMessage
            .clear()
            .append("Failed to parse a loose object header").nl()
            .nl()
            .append("Mismatch between expected and read GIT object type").nl()
            .nl()
            .keyValue("candidate", candidate).nl()
            .keyValue("expectedByte", headerPrefix[i]).nl()
            .keyValue("readByte", b).nl()
            .nl()
            .keyValue("adapter", adapter).nl()
            .keyValue("objectId", objectId).nl()
            .nl();

        return toBadObject();
      }
    }

    long length;
    length = 0;

    boolean endReached;
    endReached = false;

    while (index < limit) {
      b = array[index++];

      if (b == 0) {
        endReached = true;

        break;
      }

      int digit;
      digit = Utf8.parseInt(b);

      length *= 10;

      length += digit;
    }

    if (!endReached) {
      badObjectMessage
          .clear()
          .append("Failed to parse a loose object header").nl()
          .nl()
          .append("Could not find null byte marker.").nl()
          .nl()
          .keyValue("index", index).nl()
          .keyValue("length", length).nl()
          .keyValue("limit", limit).nl()
          .nl()
          .keyValue("adapter", adapter).nl()
          .keyValue("objectId", objectId).nl()
          .nl();

      return toBadObject();
    }

    objectPosition = 0;

    objectLength = length;

    adapter.executeObjectHeader(candidate, length);

    if (hasError()) {
      return _CLOSE;
    } else {
      inflaterBuffer.position(index);

      return execute(_OBJECT_DATA);
    }
  }

  private byte executeInflate() {
    if (inflaterBuffer.position() > 0) {
      inflaterBuffer.compact();
    }

    if (inflater.needsInput()) {
      inflater.setInput(
          channelBuffer.array(),

          channelBuffer.position(),

          channelBuffer.remaining()
      );
    }

    long byteReadBefore;
    byteReadBefore = inflater.getBytesRead();

    int inflatedCount;

    try {
      inflatedCount = inflater.inflate(
          inflaterBuffer.array(),

          inflaterBuffer.position(),

          inflaterBuffer.remaining()
      );
    } catch (DataFormatException e) {
      return toDataFormatException(e);
    }

    if (inflatedCount < 0) {
      return toStubException(
          "inflated returned < 0 for " + objectId + " and length " + objectLength);
    }

    long bytesRead;
    bytesRead = inflater.getBytesRead() - byteReadBefore;

    if (bytesRead >= Integer.MAX_VALUE) {
      return toStubException("bytesRead >= Integer.MAX_VALUE");
    }

    channelBuffer.position(
        channelBuffer.position() + (int) bytesRead
    );

    if (inflatedCount == 0 && objectLength != 0) {
      if (channelReadCount < channelReadLimit) {
        return toIo(IO_READ_MAIN, _INFLATE);
      } else {
        String state;
        state = inflateActionToString(inflateAction);

        badObjectMessage
            .clear()
            .append("Failed to inflate object data").nl()
            .nl()
            .append("Inflater wrote zero bytes and did not reach the end of the object.").nl()
            .nl()
            .keyValue("adapter", adapter).nl()
            .keyValue("objectId", objectId).nl()
            .keyValue("state", state).nl()
            .nl();

        return toBadObject();
      }
    } else {
      inflaterBuffer.position(
          inflaterBuffer.position() + inflatedCount
      );

      inflaterBuffer.flip();

      return inflateAction;
    }
  }

  private byte executeIoIndexFanOut() {
    int position;
    position = OFFSET_FAN_OUT;

    int limit;
    limit = 4;

    int fanOutIndex;
    fanOutIndex = objectId.getFanOutIndex();

    if (fanOutIndex > 0) {
      position += (fanOutIndex - 1) * 4;

      limit += 4;
    }

    channelPosition(position, limit);

    return toIo(IO_READ_AUX, _PARSE_INDEX_FAN_OUT);
  }

  private byte executeNextLooseObject() {
    if (hasObjects()) {
      nextObject();

      return toIoOpenLoose();
    } else {
      return _CLOSE;
    }
  }

  private byte executeNextPackFile() {
    int count;
    count = repository.getPackFileCount();

    if (packFileIndex < count) {
      packFile = repository.getPackFile(packFileIndex);

      packFileIndex++;

      channelMainSource = packFile.getPackFile();

      channelAuxSource = packFile.getIndexFile();

      return _NEXT_PACK_FILE_OBJECT;
    } else {
      return _NEXT_LOOSE_OBJECT;
    }
  }

  private byte executeNextPackFileObject() {
    if (hasObjects()) {
      nextObject();

      baseObject.clear();

      inflater.reset();

      inflaterBuffer.clear();

      reconstructedObject.clear();

      return _IO_INDEX_FAN_OUT;
    } else {
      UnmodifiableSet<ObjectId> copy;
      copy = objectsNotFound.toUnmodifiableSet();

      objects = copy.iterator();

      objectsNotFound.clear();

      return toIo(IO_CLOSE, _NEXT_PACK_FILE, _FINALLY);
    }
  }

  private byte executeObjectData() {
    int positionStart;
    positionStart = inflaterBuffer.position();

    adapter.executeObjectBodyPart(inflaterBuffer);

    if (hasError()) {
      return _CLOSE;
    }

    int positionDelta;
    positionDelta = inflaterBuffer.position() - positionStart;

    objectPosition += positionDelta;

    if (objectPosition > objectLength) {
      badObjectMessage
          .clear()
          .append("Failed to read object data").nl()
          .nl()
          .append("Read more bytes than the declared object length").nl()
          .nl()
          .keyValue("adapter", adapter).nl()
          .keyValue("objectId", objectId).nl()
          .keyValue("objectLength", objectLength).nl()
          .keyValue("objectPosition", objectPosition).nl()
          .nl();

      return toBadObject();
    }

    if (inflaterBuffer.hasRemaining()) {
      return _OBJECT_DATA;
    }

    boolean endOfObject;
    endOfObject = objectPosition == objectLength;

    if (!endOfObject) {
      return toReadAndInflate(_OBJECT_DATA);
    }

    adapter.executeObjectFinish();

    inflater.reset();

    inflaterBuffer.clear();

    if (channelAux != null) {
      return toNextPackFileObject();
    } else {
      objectId = null;

      return toIo(IO_CLOSE, _NEXT_LOOSE_OBJECT);
    }
  }

  private byte executeObjectDataFull() {
    int size;
    size = reconstructedObject.size();

    adapter.executeObjectHeader(objectKind, size);

    inflaterBuffer.clear();

    adapter.executeObjectBodyFull(
        reconstructedObject.array(),

        size,

        inflaterBuffer
    );

    adapter.executeObjectFinish();

    return toNextPackFileObject();
  }

  private byte executeParseIndexFanOut() {
    int count0;
    count0 = 0;

    int fanOutIndex;
    fanOutIndex = objectId.getFanOutIndex();

    if (fanOutIndex != 0) {
      count0 = channelBuffer.getInt();
    }

    int count1;
    count1 = channelBuffer.getInt();

    int bucketSize;
    bucketSize = count1 - count0;

    if (bucketSize == 0) {
      objectsNotFound.add(objectId);

      return toNextPackFileObject();
    }

    objectPosition = count0;

    objectLength = count1;

    long offset;
    offset = objectPosition * 20;

    channelPosition(
        OFFSET_OBJECT_NAMES + offset,

        bucketSize * 20
    );

    return toIo(IO_READ_AUX, _PARSE_INDEX_OBJECT_NAME);
  }

  private byte executeParseIndexObjectName() {
    byte[] objectIdBytes;
    objectIdBytes = new byte[20];

    for (; objectPosition < objectLength;) {
      int remaining;
      remaining = channelBuffer.remaining();

      if (remaining < 20) {
        return toIo(IO_READ_AUX, _PARSE_INDEX_OBJECT_NAME);
      }

      channelBuffer.get(objectIdBytes);

      ObjectId id;
      id = ObjectId.fromByteArray(objectIdBytes);

      if (!objectId.equals(id)) {
        objectPosition++;

        continue;
      }

      adapter.executeObjectStart(objectId);

      switch (mode) {
        case EXISTS:
          return toNextPackFileObject();
        case READ_OBJECT:
          int objectCount;
          objectCount = packFile.getObjectCount();

          int position;
          position = OFFSET_OBJECT_NAMES;

          // skip object names
          position += objectCount * 20;

          // skip crc32
          position += objectCount * 4;

          // go to offset
          position += objectPosition * 4;

          int limit;
          limit = 4;

          channelPosition(position, limit);

          return toIo(IO_READ_AUX, _PARSE_INDEX_OFFSET);
        default:
          throw new AssertionError("Unexpected mode=" + mode);
      }
    }

    objectsNotFound.add(objectId);

    return toNextPackFileObject();
  }

  private byte executeParseIndexOffset() {
    int offset;
    offset = channelBuffer.getInt();

    if (offset < 0) {
      return toStubException("8-byte offset entries not yet implemented");
    }

    long fileSize;
    fileSize = packFile.getPackFileSize();

    channelPosition(offset, fileSize - offset);

    return toIo(IO_READ_MAIN, _PARSE_PACK_HEADER);
  }

  private byte executeParsePackHeader() {
    long length;
    length = 0;

    int value;
    value = channelBuffer.get();

    boolean last;
    last = value >= 0;

    length = value & 0xF;

    value = value >>> 4;

    byte type;
    type = (byte) (value & 7);

    int shift = 4;

    while (!last) {
      value = channelBuffer.get();

      last = value >= 0;

      value = value & 0x7F;

      length |= ((long) value) << shift;

      shift += 7;
    }

    objectPosition = 0;

    objectLength = length;

    PackedObjectKind kind;
    kind = PackedObjectKind.ofByte(type);

    if (kind.isNonDeltified()) {
      objectKind = kind.toObjectKind();

      if (hasError()) {
        return _CLOSE;
      }

      if (deltaStack.isEmpty()) {
        adapter.executeObjectHeader(objectKind, length);

        return toInflate(_OBJECT_DATA);
      } else {
        return toInflate(_BASE_OBJECT);
      }
    }

    if (kind == PackedObjectKind.OFS_DELTA) {
      int size;
      size = deltaArray.size();

      deltaStack.push(size);

      long ofsDelta;
      ofsDelta = 0;

      value = channelBuffer.get();

      ofsDelta = value & 0x7F;

      while (value < 0) {
        ofsDelta = ofsDelta + 1;

        ofsDelta = ofsDelta << 7;

        value = channelBuffer.get();

        ofsDelta += value & 0x7F;
      }

      long position;
      position = channelPosition - ofsDelta;

      deltaArray.putLong(position);

      return toInflate(_DELTA_ARRAY);
    }

    return toStubException("Not yet implemented: kind=" + kind);
  }

  private byte executeReconstructObject() {
    byte[] array;
    array = deltaArray.array();

    int index;
    index = deltaOffset + 8;

    long baseSize;
    baseSize = 0;

    boolean last;
    last = false;

    int shift;
    shift = 0;

    while (index < deltaLimit && !last) {
      int value;
      value = array[index++];

      last = value >= 0;

      value = value & 0x7F;

      baseSize |= (long) value << shift;

      shift += 7;
    }

    if (baseSize != baseObject.size()) {
      badObjectMessage
          .clear()
          .append("Failed to reconstruct deltified object data").nl()
          .nl()
          .append("Delta base object size mismatch").nl()
          .nl()
          .keyValue("           adapter", adapter).nl()
          .keyValue("          objectId", objectId).nl()
          .keyValue(" declared baseSize", baseSize).nl()
          .keyValue("in-memory baseSize", baseObject.size()).nl()
          .keyValue("              pack", packFile.getObjectId()).nl()
          .nl();

      return toBadObject();
    }

    last = false;

    shift = 0;

    long reconstructedSize;
    reconstructedSize = 0;

    while (index < deltaLimit && !last) {
      int value;
      value = array[index++];

      last = value >= 0;

      value = value & 0x7F;

      reconstructedSize |= (long) value << shift;

      shift += 7;
    }

    for (; index < deltaLimit;) {
      byte instruction;
      instruction = array[index++];

      if ((instruction & 128) != 0) {
        int offset0 = 0;

        if ((instruction & 1) != 0) {
          offset0 = array[index++] & 0xFF;
        }

        int offset1 = 0;

        if ((instruction & 2) != 0) {
          offset1 = array[index++] & 0xFF;
        }

        int offset2 = 0;

        if ((instruction & 4) != 0) {
          offset2 = array[index++] & 0xFF;
        }

        int offset3 = 0;

        if ((instruction & 8) != 0) {
          offset3 = array[index++] & 0xFF;
        }

        int offset;
        offset = 0;

        offset |= offset0 << 0;

        offset |= offset1 << 8;

        offset |= offset2 << 16;

        offset |= offset3 << 24;

        int size0 = 0;

        if ((instruction & 16) != 0) {
          size0 = array[index++] & 0xFF;
        }

        int size1 = 0;

        if ((instruction & 32) != 0) {
          size1 = array[index++] & 0xFF;
        }

        int size2 = 0;

        if ((instruction & 64) != 0) {
          size2 = array[index++] & 0xFF;
        }

        int size;
        size = 0;

        size |= size0 << 0;

        size |= size1 << 8;

        size |= size2 << 16;

        if (size == 0) {
          size = 0x10000;
        }

        reconstructedObject.write(baseObject, offset, size);
      } else {
        int size;
        size = instruction & 127;

        int remaining;
        remaining = deltaLimit - index;

        if (remaining < size) {
          return toStubException("remaining < size: remaining=" + remaining + ";size=" + size);
        }

        reconstructedObject.write(array, index, size);

        index += size;
      }
    }

    if (reconstructedSize != reconstructedObject.size()) {
      badObjectMessage
          .clear()
          .append("Failed to reconstruct deltified object data").nl()
          .nl()
          .append("Reconstructed object size mismatch").nl()
          .nl()
          .keyValue("       adapter", adapter).nl()
          .keyValue("      objectId", objectId).nl()
          .keyValue(" declared size", reconstructedSize).nl()
          .keyValue("in-memory size", reconstructedObject.size()).nl()
          .keyValue("          pack", packFile.getObjectId()).nl()
          .nl();

      return toBadObject();
    }

    if (deltaStack.isEmpty()) {
      return _OBJECT_DATA_FULL;
    }

    deltaLimit = deltaOffset;

    deltaOffset = deltaStack.pop();

    ByteArrayWriter temp;
    temp = baseObject;

    baseObject = reconstructedObject;

    temp.clear();

    reconstructedObject = temp;

    return _RECONSTRUCT_OBJECT;
  }

  private boolean hasObjects() {
    if (objects != null) {
      return objects.hasNext();
    } else {
      return objectId != null;
    }
  }

  private String inflateActionToString(byte action) {
    String state;

    switch (action) {
      case _BASE_OBJECT:
        state = "_BASE_OBJECT";
        break;
      case _DELTA_ARRAY:
        state = "_DELTA_ARRAY";
        break;
      default:
        state = Byte.toString(action);
        break;
    }

    return state;
  }

  private void ioClose() throws IOException {
    close(channelAux);

    close(channelMain);

    channelAux = null;

    channelMain = null;

    send(EIO_CLOSE);
  }

  private void ioOpenLoose() throws IOException {
    ResolvedPath maybeLoose;
    maybeLoose = repository.resolveLooseObject(objectId);

    maybeLoose.acceptPathNameVisitor(ResolveLoose.INSTANCE, this);
  }

  private void ioRead(FileChannel channel) throws IOException {
    if (channelBuffer.position() > 0) {
      channelBuffer.compact();
    }

    long delta;
    delta = channelReadLimit - channelReadCount;

    long newLimit;
    newLimit = channelBuffer.position() + delta;

    newLimit = Math.min(newLimit, channelBuffer.capacity());

    // cast should be safe: limited by channelBuffer.capacity()
    channelBuffer.limit((int) newLimit);

    if (channelReadCount == 0) {
      channel.position(channelPosition);
    }

    int thisReadCount;
    thisReadCount = channel.read(channelBuffer);

    if (thisReadCount > 0) {
      channelReadCount += thisReadCount;
    }

    channelBuffer.flip();

    send(EIO_READ, thisReadCount, channelReadCount);
  }

  private void ioReadAux() throws IOException {
    if (channelAux == null) {
      channelAux = channelAuxSource.openReadChannel();

      send(EIO_OPEN, channelReadLimit);
    }

    ioRead(channelAux);
  }

  private void ioReadMain() throws IOException {
    if (channelMain == null) {
      channelMain = channelMainSource.openReadChannel();

      send(EIO_OPEN, channelReadLimit);
    }

    ioRead(channelMain);
  }

  private ObjectId nextObject() {
    ObjectId next;

    if (objects != null) {
      next = objectId = objects.next();
    } else {
      next = objectId;
    }

    send(ENEXT_OBJECT, next);

    return next;
  }

  private byte toBadObject() {
    String message;
    message = badObjectMessage.toString();

    return toError(
        new BadObjectException(objectId, message)
    );
  }

  private byte toDataFormatException(DataFormatException e) {
    String message;
    message = e.getMessage();

    if (message == null) {
      message = "Invalid ZLIB data format";
    }

    return toIoException(message);
  }

  private byte toInflate(byte onReady) {
    inflateAction = onReady;

    return _INFLATE;
  }

  private byte toIo(byte task, byte onReady) {
    return toIo(task, onReady, _CLOSE);
  }

  private byte toIoOpenLoose() {
    return toIo(IO_OPEN_LOOSE, toInflate(_HEADER));
  }

  private byte toNextPackFileObject() {
    objectId = null;

    return _NEXT_PACK_FILE_OBJECT;
  }

  private byte toReadAndInflate(byte onReady) {
    if (channelBuffer.hasRemaining()) {
      return toInflate(onReady);
    }

    if (channelReadCount < channelReadLimit) {
      return toIo(IO_READ_MAIN, toInflate(onReady));
    }

    String state;
    state = inflateActionToString(onReady);

    badObjectMessage
        .clear()
        .append("Failed to inflate object data").nl()
        .nl()
        .append("No more bytes left to read.").nl()
        .nl()
        .keyValue("         adapter", adapter).nl()
        .keyValue("channelReadCount", channelReadCount).nl()
        .keyValue("channelReadLimit", channelReadLimit).nl()
        .keyValue("        objectId", objectId).nl()
        .keyValue("           state", state).nl()
        .nl();

    return toBadObject();
  }

  private enum ResolveLoose implements PathNameVisitor<Void, ObjectReader> {

    INSTANCE;

    @Override
    public final Void visitDirectory(Directory directory, ObjectReader p) throws IOException {
      throw new NotRegularFileException(directory);
    }

    @Override
    public final Void visitNotFound(ResolvedPath notFound, ObjectReader p) throws IOException {
      p.looseObjectNotFound();

      return null;
    }

    @Override
    public final Void visitRegularFile(RegularFile file, ObjectReader p) throws IOException {
      p.looseObjectFound(file);

      return null;
    }

  }

}