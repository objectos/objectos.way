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
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import objectos.lang.object.Check;
import objectos.notes.Note1;
import objectos.notes.NoteSink;
import objectos.util.list.GrowableList;

final class ReadCommit implements ObjectReaderAdapter {

  private static final Note1<ObjectId> ESTART;

  private static final Note1<ObjectId> ESUCCESS;

  static {
    Class<?> source;
    source = ReadCommit.class;

    ESTART = Note1.debug(source, "Start");

    ESUCCESS = Note1.debug(source, "Success");
  }

  static final byte _BEFORE_IDENTIFICATION = 1;

  static final byte _BEFORE_MESSAGE = 2;

  static final byte _DECODE = 3;

  static final byte _PARSE_AUTHOR = 4;

  static final byte _PARSE_COMMITTER = 5;

  static final byte _PARSE_IDENTIFICATION = 6;

  static final byte _PARSE_MESSAGE = 7;

  static final byte _PARSE_OBJECT_ID = 8;

  static final byte _PARSE_PARENT = 9;

  static final byte _PARSE_PARENT_OR_AUTHOR = 10;

  static final byte _PARSE_PREFIX = 11;

  static final byte _PARSE_TREE = 12;

  static final byte _RESULT = 13;

  static final byte _START = 14;

  static final byte _STOP = 0;

  private static final char[] PAUTHOR = "uthor ".toCharArray();

  private static final char[] PCOMMITTER = "committer ".toCharArray();

  private static final char[] PPARENT = "arent ".toCharArray();

  private static final char[] PTREE = "tree ".toCharArray();

  byte state;

  private boolean active;

  private Identification author;

  private String badObjectMessage;

  private char[] charArray;

  private int charArrayIndex;

  private int charArrayLength;

  private Identification committer;

  private ByteBuffer data;

  private byte decodeAction;

  private CharsetDecoder decoder;

  private CharBuffer decoderBuffer;

  private ObjectReaderHandle handle;

  private final IdentificationBuilder identificationBuilder = new IdentificationBuilder();

  private final GitInjector injector;

  private String line;

  private NoteSink logger;

  private ObjectId objectId;

  private long objectLength;

  private long objectPosition;

  private GrowableList<ObjectId> parents;

  private byte parseIdentificationTarget;

  private byte parseObjectIdTarget;

  private char[] parsePrefix;

  private int parsePrefixIndex;

  private byte parsePrefixSource;

  private GitRepository repository;

  private StringBuilder stringBuilder;

  private ObjectId tree;

  ReadCommit(GitInjector injector) {
    this.injector = injector;
  }

  @Override
  public final void executeFinally() {
    active = false;

    author = null;

    badObjectMessage = null;

    charArray = null;

    charArrayIndex = 0;

    charArrayLength = 0;

    committer = null;

    data = null;

    decodeAction = 0;

    decoder = injector.putDecoder(decoder);

    decoderBuffer = injector.putCharBuffer(decoderBuffer);

    handle = null;

    identificationBuilder.reset();

    line = "";

    logger = null;

    objectId = null;

    objectLength = 0;

    objectPosition = 0;

    parents = injector.putGrowableList(parents);

    parseIdentificationTarget = 0;

    parseObjectIdTarget = 0;

    parsePrefix = null;

    parsePrefixIndex = 0;

    parsePrefixSource = 0;

    repository = null;

    state = _STOP;

    stringBuilder = injector.putStringBuilder(stringBuilder);

    tree = null;
  }

  @Override
  public final void executeObjectBodyFull(byte[] array, int length, ByteBuffer buffer) {
    int offset;
    offset = 0;

    buffer.clear();

    while (offset < length && state != _STOP) {
      if (buffer.position() > 0) {
        buffer.compact();
      }

      int bytesToPut;
      bytesToPut = length - offset;

      bytesToPut = Math.min(bytesToPut, buffer.remaining());

      buffer.put(array, offset, bytesToPut);

      offset += bytesToPut;

      buffer.flip();

      executeObjectBodyPart(buffer);
    }
  }

  @Override
  public final void executeObjectBodyPart(ByteBuffer buffer) {
    Check.state(!active, "already active");

    data = buffer;

    active = true;

    execute();
  }

  @Override
  public final void executeObjectFinish() {
    // noop
  }

  @Override
  public final void executeObjectHeader(ObjectKind kind, long length) {
    if (kind != ObjectKind.COMMIT) {
      handle.catchThrowable(
          new BadObjectException(objectId, "Not a commit object. Found " + kind)
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
    this.repository = repository;

    this.objectId = objectId;
  }

  final String getDecodedString() {
    return new String(charArray, charArrayIndex, charArrayLength - charArrayIndex);
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
      case _BEFORE_IDENTIFICATION:
        return executeBeforeIdentification();
      case _BEFORE_MESSAGE:
        return executeBeforeMessage();
      case _DECODE:
        return executeDecode();
      case _PARSE_AUTHOR:
        return executeParseAuthor();
      case _PARSE_COMMITTER:
        return executeParseCommitter();
      case _PARSE_OBJECT_ID:
        return executeParseObjectId();
      case _PARSE_IDENTIFICATION:
        return executeParseIdentification();
      case _PARSE_MESSAGE:
        return executeParseMessage();
      case _PARSE_PARENT:
        return executeParseParent();
      case _PARSE_PARENT_OR_AUTHOR:
        return executeParseParentOrAuthor();
      case _PARSE_PREFIX:
        return executeParsePrefix();
      case _PARSE_TREE:
        return executeParseTree();
      case _RESULT:
        return executeResult();
      case _START:
        return executeStart();
      default:
        throw new UnsupportedOperationException("Implement me: state=" + state);
    }
  }

  private byte executeBeforeIdentification() {
    boolean found;
    found = false;

    while (charArrayIndex < charArrayLength) {
      char c;
      c = charArray[charArrayIndex++];

      if (c == Git.LF) {
        found = true;

        break;
      }

      stringBuilder.append(c);
    }

    if (!found) {
      return toNeedsInput();
    }

    line = stringBuilder.toString();

    return _PARSE_IDENTIFICATION;
  }

  private byte executeBeforeMessage() {
    badObjectMessage = "Failed to parse commit message";

    if (!hasRemaining(1)) {
      return toNeedsInput();
    }

    char c;
    c = charArray[charArrayIndex];

    charArrayIndex++;

    if (c != Git.LF) {
      return toBadObject();
    }

    stringBuilder.setLength(0);

    return _PARSE_MESSAGE;
  }

  private byte executeDecode() {
    decoderBuffer.position(charArrayIndex);

    if (decoderBuffer.position() > 0) {
      decoderBuffer.compact();
    }

    int dataPosition;
    dataPosition = data.position();

    int dataLimit;
    dataLimit = data.limit();

    int preventOverflowLimit;
    preventOverflowLimit = dataPosition + decoderBuffer.remaining();

    preventOverflowLimit = Math.min(preventOverflowLimit, dataLimit);

    data.limit(preventOverflowLimit);

    long expectedAfterObjectPosition;
    expectedAfterObjectPosition = objectPosition + data.remaining();

    boolean endOfInput;
    endOfInput = expectedAfterObjectPosition >= objectLength;

    CoderResult coderResult;
    coderResult = decoder.decode(data, decoderBuffer, endOfInput);

    data.limit(dataLimit);

    if (!coderResult.isUnderflow()) {
      return toStubException("coderResult != underflow");
    }

    objectPosition += data.position() - dataPosition;

    if (objectPosition > objectLength) {
      return toStubException("objectPosition > objectLength");
    }

    decoderBuffer.flip();

    charArray = decoderBuffer.array();

    charArrayIndex = decoderBuffer.position();

    charArrayLength = decoderBuffer.limit();

    decoderBuffer.position(decoderBuffer.limit());

    return decodeAction;
  }

  private byte executeParseAuthor() {
    return parseAuthorOrCommitter(
        "Failed to parse commit author information",

        PAUTHOR,

        _PARSE_AUTHOR
    );
  }

  private byte executeParseCommitter() {
    return parseAuthorOrCommitter(
        "Failed to parse commit committer information",

        PCOMMITTER,

        _PARSE_COMMITTER
    );
  }

  private byte executeParseIdentification() {
    IdentificationBuilder builder;
    builder = identificationBuilder;

    builder.reset();

    // name
    char c;

    char[] array;
    array = line.toCharArray();

    int index;
    index = 0;

    char previous;
    previous = '\0';

    boolean found;
    found = false;

    stringBuilder.setLength(0);

    while (index < array.length) {
      c = array[index++];

      if (c == '<' && previous == ' ') {
        found = true;

        break;
      }

      stringBuilder.append(c);

      previous = c;
    }

    if (!found) {
      return toBadObject();
    }

    int length;
    length = stringBuilder.length();

    // remove trailing space
    stringBuilder.setLength(length - 1);

    builder.setName(stringBuilder.toString());

    // email
    found = false;

    stringBuilder.setLength(0);

    while (index < array.length) {
      c = array[index++];

      if (c == '>') {
        found = true;

        break;
      }

      stringBuilder.append(c);
    }

    if (!found) {
      return toBadObject();
    }

    if (index >= array.length) {
      return toBadObject();
    }

    c = array[index++];

    if (c != ' ') {
      return toBadObject();
    }

    if (stringBuilder.length() == 0) {
      return toBadObject();
    }

    builder.setEmail(stringBuilder.toString());

    // time
    found = false;

    long seconds;
    seconds = 0;

    while (index < array.length) {
      c = array[index++];

      if (c == ' ') {
        found = true;

        break;
      }

      int digit;
      digit = Git.parseInt(c);

      seconds *= 10;

      seconds += digit;
    }

    if (!found) {
      return toBadObject();
    }

    builder.setSeconds(seconds);

    // tz
    stringBuilder.setLength(0);

    while (index < array.length) {
      c = array[index++];

      stringBuilder.append(c);
    }

    builder.setGitTimeZone(stringBuilder.toString());

    builder.setValid();

    if (!builder.isValid()) {
      return toBadObject();
    }

    switch (parseIdentificationTarget) {
      case _PARSE_AUTHOR:
        author = builder.build();

        return _PARSE_COMMITTER;
      case _PARSE_COMMITTER:
        committer = builder.build();

        return _BEFORE_MESSAGE;
      default:
        throw new UnsupportedOperationException(
            "Implement me: target=" + parseIdentificationTarget);
    }
  }

  private byte executeParseMessage() {
    badObjectMessage = "Failed to parse commit message";

    int remaining;
    remaining = charArrayLength - charArrayIndex;

    stringBuilder.append(charArray, charArrayIndex, remaining);

    charArrayIndex = charArrayLength;

    if (hasRemaining()) {
      return toNeedsInput();
    } else {
      return _RESULT;
    }
  }

  private byte executeParseObjectId() {
    char c;
    c = '\0';

    while (charArrayIndex < charArrayLength && stringBuilder.length() < ObjectId.CHAR_LENGTH) {
      c = charArray[charArrayIndex++];

      stringBuilder.append(c);
    }

    if (stringBuilder.length() < ObjectId.CHAR_LENGTH) {
      return toNeedsInput();
    }

    if (!hasRemaining(1)) {
      return toNeedsInput();
    }

    c = charArray[charArrayIndex++];

    if (c != Git.LF) {
      return toBadObject();
    }

    try {
      ObjectId objectId;
      objectId = ObjectId.parse(stringBuilder, 0);

      switch (parseObjectIdTarget) {
        case _PARSE_PARENT:
          parents.add(objectId);

          return _PARSE_PARENT_OR_AUTHOR;
        case _PARSE_TREE:
          tree = objectId;

          return _PARSE_PARENT_OR_AUTHOR;
        default:
          throw new UnsupportedOperationException("Implement me: target=" + parseObjectIdTarget);
      }
    } catch (InvalidObjectIdFormatException e) {
      return toError(e);
    }
  }

  private byte executeParseParent() {
    badObjectMessage = "Failed to parse commit parent information";

    if (!hasRemaining(PPARENT)) {
      return toNeedsInput();
    }

    if (!matches(PPARENT)) {
      return toBadObject();
    }

    return toParseObjectId(_PARSE_PARENT);
  }

  private byte executeParseParentOrAuthor() {
    badObjectMessage = "Failed to parse commit parent/author information";

    if (!hasRemaining(1)) {
      return toNeedsInput();
    }

    char c;
    c = charArray[charArrayIndex];

    charArrayIndex++;

    switch (c) {
      case 'a':
        return _PARSE_AUTHOR;
      case 'p':
        return _PARSE_PARENT;
      default:
        return toStubException("Implement me @ char=" + c);
    }
  }

  private byte executeParsePrefix() {
    while (charArrayIndex < charArrayLength && parsePrefixIndex < parsePrefix.length) {
      char c;
      c = charArray[charArrayIndex++];

      char p;
      p = parsePrefix[parsePrefixIndex++];

      if (c != p) {
        return toBadObject();
      }
    }

    if (parsePrefixIndex == parsePrefix.length) {
      switch (parsePrefixSource) {
        case _PARSE_TREE:
          return toParseObjectId(parsePrefixSource);
        default:
          throw new UnsupportedOperationException(
              "Implement me: parsePrefixSource=" + parsePrefixSource);
      }
    }

    else {
      return toNeedsInput();
    }
  }

  private byte executeParseTree() {
    badObjectMessage = "Failed to parse commit tree information";

    return toParsePrefix(PTREE, _PARSE_TREE);
  }

  private byte executeResult() {
    String message;
    message = stringBuilder.toString();

    Commit result;
    result = new Commit(
        author,
        committer,
        message,
        objectId,
        parents.toUnmodifiableList(),
        tree
    );

    handle.setResult(result);

    logger.send(ESUCCESS, objectId);

    active = false;

    return state;
  }

  private byte executeStart() {
    Charset charset;
    charset = repository.getCharset();

    decoder = injector.getDecoder(charset);

    decoderBuffer = injector.getCharBuffer();

    logger = injector.getLogger();

    parents = injector.getGrowableList();

    stringBuilder = injector.getStringBuilder();

    logger.send(ESTART, objectId);

    return toDecode(_PARSE_TREE);
  }

  private boolean hasRemaining() {
    return objectPosition < objectLength;
  }

  private boolean hasRemaining(char[] prefix) {
    return hasRemaining(prefix.length);
  }

  private boolean hasRemaining(int expected) {
    int remaining;
    remaining = charArrayLength - charArrayIndex;

    return remaining >= expected;
  }

  private boolean matches(char[] prefix) {
    for (int i = 0, length = prefix.length; i < length && charArrayIndex < charArrayLength; i++) {
      char c;
      c = charArray[charArrayIndex++];

      if (c != prefix[i]) {
        return false;
      }
    }

    return true;
  }

  private byte parseAuthorOrCommitter(String message, char[] prefix, byte target) {
    badObjectMessage = message;

    if (!hasRemaining(prefix)) {
      return toNeedsInput(target);
    }

    if (!matches(prefix)) {
      return toBadObject();
    }

    return toParseIdentification(target);
  }

  private byte toBadObject() {
    return toError(
        new BadObjectException(objectId, badObjectMessage)
    );
  }

  private byte toDecode(byte onReady) {
    decodeAction = onReady;

    return _DECODE;
  }

  private byte toError(Throwable e) {
    handle.catchThrowable(e);

    active = false;

    return state;
  }

  private byte toNeedsInput() {
    return toNeedsInput(state);
  }

  private byte toNeedsInput(byte target) {
    if (hasRemaining()) {
      active = false;

      return toDecode(target);
    } else {
      return toBadObject();
    }
  }

  private byte toParseIdentification(byte target) {
    line = "";

    parseIdentificationTarget = target;

    stringBuilder.setLength(0);

    return _BEFORE_IDENTIFICATION;
  }

  private byte toParseObjectId(byte source) {
    parseObjectIdTarget = source;

    stringBuilder.setLength(0);

    return _PARSE_OBJECT_ID;
  }

  private byte toParsePrefix(char[] prefix, byte source) {
    parsePrefix = prefix;

    parsePrefixIndex = 0;

    parsePrefixSource = source;

    return _PARSE_PREFIX;
  }

  private byte toStubException(String message) {
    return toError(
        new GitStubException(message)
    );
  }

}