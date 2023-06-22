/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import objectos.http.Header.ContentTypeVisitor;
import objectos.http.media.ApplicationType;
import objectos.http.media.ImageType;
import objectos.http.media.TextType;
import objectos.lang.Check;
import objectos.util.GrowableList;
import objectos.util.UnmodifiableList;

abstract class AbstractHttpParser<H extends Header> {

  private static final char[] HTTP_MAJORS = new char[] {'1', '2', '3'};

  private static final char[] HTTP_MINORS = new char[] {'0', '1'};

  private static final int HTTP_VERSION_MIN = "HTTP/0.0".length();

  Action action;

  Charset bodyCharset;

  long bodyLength;

  Exception exception;

  HeaderParser<? extends H> headerParser;

  int ioReadCount;

  HeaderContentLengthImpl resultContentLength;

  HeaderContentTypeImpl resultContentType;

  Version resultVersion;

  Status status;

  String statusMessage;

  private BodyKind bodyKind;

  private final ByteBuffer byteBuffer;

  private final CharBuffer charBuffer;

  private int charIndex;

  private final ThisContentTypeVisitor contentTypeVisitor = new ThisContentTypeVisitor();

  private Action decodeAction;

  private CharsetDecoder decoder;

  private final Map<Charset, CharsetDecoder> decoderMap = new HashMap<>(4);

  private ReadableByteChannel input;

  private volatile boolean ioRunning;

  private long ioStartTime;

  private char parseChar;

  private Action parseCharAction;

  private StringBuilder parseToEol;

  private Action parseToEolAction;

  private final GrowableList<H> resultHeaders = new GrowableList<>();

  private final StringBuilder stringBuilder = new StringBuilder();

  private final long timeout = 1 * 1000;

  public AbstractHttpParser(ByteBuffer byteBuffer,
                            CharBuffer charBuffer) {
    this.byteBuffer = byteBuffer;
    this.charBuffer = charBuffer;
  }

  public final void executeIo() {
    try {
      if (byteBuffer.position() != 0) {
        byteBuffer.compact();
      }

      ioReadCount = input.read(byteBuffer);
    } catch (IOException e) {
      exception = e;
    } finally {
      ioRunning = false;
    }
  }

  public final void executeOne() {
    action = execute0();
  }

  public abstract Object getResult() throws ProtocolException;

  public final boolean isActive() {
    switch (action) {
      case END:
      case ERROR:
        return false;
      default:
        return true;
    }
  }

  public final void setInput(ReadableByteChannel channel) {
    Check.notNull(channel, "channel == null");

    input = channel;

    action = Action.RESET;
  }

  final Body buildBody() {
    switch (bodyKind) {
      case IGNORED:
        return BodyIgnoredImpl.INSTANCE;
      case TEXT:
        return new BodyTextImpl(bodyCharset, stringBuilder.toString());
      default:
        throw new UnsupportedOperationException("Implement me @ " + bodyKind);
    }
  }

  final UnmodifiableList<H> buildHeaders() {
    return resultHeaders.toUnmodifiableList();
  }

  abstract HeaderParser<? extends H> createHeaderParser(String lowerCaseName, String originalName);

  final Action executeIoAndDecodeAgain() {
    decodeAction = action;

    return toIo();
  }

  abstract Action executeParse();

  abstract Action executeParseHttpVersionImpl();

  abstract Action executeParseReasonPhrase();

  abstract Action executeParseRequestTarget();

  abstract Action executeParseStatusCode();

  abstract void executeResetImpl();

  abstract Action executeResult();

  final <T> T getResultImpl(T result) throws ProtocolException {
    if (action == null) {
      throw new IllegalStateException("No input was processed.");
    }

    if (isActive()) {
      throw new IllegalStateException("Still more input to be processed.");
    }

    if (status != null) {
      throw ProtocolException.create(status, statusMessage, exception);
    }

    if (result == null) {
      throw new AssertionError("Expected to have a result but not result was found");
    }

    return result;
  }

  final boolean hasNextChar() {
    return charIndex < charBuffer.position();
  }

  final char nextChar() {
    return charBuffer.get(charIndex++);
  }

  final char[] nextCharArray(int length) {
    char[] result;
    result = new char[length];

    charBuffer.get(charIndex, result, 0, length);

    charIndex += length;

    return result;
  }

  final int nextCharSize() {
    return charBuffer.position() - charIndex;
  }

  final void setDecoder(Charset charset) {
    CharsetDecoder newDecoder;
    newDecoder = decoderMap.get(charset);

    if (newDecoder == null) {
      newDecoder = charset.newDecoder();

      decoderMap.put(charset, newDecoder);
    } else {
      newDecoder.reset();
    }

    decoder = newDecoder;
  }

  final Action toError(Status status) {
    return toError(status, null);
  }

  final Action toError(Status status, String message) {
    this.status = status;

    statusMessage = message;

    return Action.ERROR;
  }

  final Action toError400(String message) {
    return toError(Status.BAD_REQUEST, message);
  }

  final Action toParseChar(char expected, Action onSuccess) {
    parseChar = expected;

    parseCharAction = onSuccess;

    return Action.PARSE_CHAR;
  }

  final Action toParseToEol(StringBuilder buffer, Action onSuccess) {
    parseToEol = buffer;

    parseToEolAction = onSuccess;

    return Action.PARSE_TO_EOL;
  }

  private Action execute0() {
    switch (action) {
      case BODY:
        return executeBody();
      case BODY_TEXT:
        return executeBodyText();
      case BODY_TEXT_IN_MEMORY:
        return executeBodyTextInMemory();
      case DECODE:
        return executeDecode();
      case IO_READY:
        return executeIoReady();
      case IO_TIMEOUT:
        return executeIoTimeout();
      case IO_WAIT:
        return executeIoWait();
      case PARSE:
        return executeParse();
      case PARSE_CHAR:
        return executeParseChar();
      case PARSE_HEADER:
        return executeParseHeader();
      case PARSE_HEADER_NAME:
        return executeParseHeaderName();
      case PARSE_HEADER_VALUE:
        return executeParseHeaderValue();
      case PARSE_HEADER_VALUE_AFTER_OWS:
        return executeParseHeaderValueAfterOws();
      case PARSE_HTTP_VERSION:
        return executeParseHttpVersion();
      case PARSE_REASON_PHRASE:
        return executeParseReasonPhrase();
      case PARSE_REQUEST_TARGET:
        return executeParseRequestTarget();
      case PARSE_STATUS_CODE:
        return executeParseStatusCode();
      case PARSE_TO_EOL:
        return executeParseToEol();
      case RESET:
        return executeReset();
      case RESULT:
        return executeResult();
      default:
        throw new UnsupportedOperationException("Implement me @ " + action);
    }
  }

  private Action executeBody() {
    contentTypeVisitor.bodyAction = Action.RESULT;

    if (resultContentLength != null) {
      bodyLength = resultContentLength.getLength();
    } else {
      bodyLength = Long.MAX_VALUE;
    }

    if (resultContentType != null) {
      resultContentType.acceptContentTypeVisitor(contentTypeVisitor);
    }

    return contentTypeVisitor.bodyAction;
  }

  private Action executeBodyText() {
    bodyKind = BodyKind.TEXT;

    int overReadCount;
    overReadCount = nextCharSize();

    int oldPosition;
    oldPosition = byteBuffer.position();

    int newPosition;
    newPosition = oldPosition - overReadCount;

    if (newPosition < 0) {
      throw new UnsupportedOperationException("Implement me @ newPosition=" + newPosition);
    }

    byteBuffer.position(newPosition);

    setDecoder(bodyCharset);

    decodeAction = Action.BODY_TEXT_IN_MEMORY;

    stringBuilder.setLength(0);

    if (byteBuffer.hasRemaining()) {
      return Action.DECODE;
    } else {
      return toIo();
    }
  }

  private Action executeBodyTextInMemory() {
    if (!charBuffer.hasArray()) {
      throw new UnsupportedOperationException("Implement me");
    }

    char[] array;
    array = charBuffer.array();

    int charsToAppend;
    charsToAppend = charBuffer.position();

    int finalLength;
    finalLength = stringBuilder.length() + charsToAppend;

    if (finalLength > bodyLength) {
      charsToAppend = (int) (bodyLength - stringBuilder.length());
    }

    stringBuilder.append(array, 0, charsToAppend);

    if (stringBuilder.length() == bodyLength) {
      return Action.RESULT;
    }

    if (ioReadCount < 0) {
      return Action.RESULT;
    }

    return toIo();
  }

  private Action executeDecode() {
    charBuffer.clear();

    CoderResult result;
    result = decoder.decode(byteBuffer, charBuffer, false);

    if (!result.isUnderflow()) {
      throw new UnsupportedOperationException("Implement me. result=" + result);
    }

    return decodeAction;
  }

  private Action executeIoEof() {
    switch (decodeAction) {
      case BODY_TEXT_IN_MEMORY:
        return Action.RESULT;
      default:
        throw new UnsupportedOperationException("Implement me @ " + decodeAction.name());
    }
  }

  private Action executeIoReady() {
    if (ioReadCount < 0) {
      return executeIoEof();
    }

    if (ioReadCount == 0) {
      return toIo();
    }

    byteBuffer.flip();

    return Action.DECODE;
  }

  private Action executeIoTimeout() {
    return toError(Status.INTERNAL_SERVER_ERROR, "I/O timeout");
  }

  private Action executeIoWait() {
    if (ioRunning) {
      return Action.IO_WAIT;
    }

    else if (exception != null) {
      return toError(Status.INTERNAL_SERVER_ERROR);
    }

    else if (ioTimedOut()) {
      return Action.IO_TIMEOUT;
    }

    else {
      return Action.IO_READY;
    }
  }

  private Action executeParseChar() {
    if (!hasNextChar()) {
      return executeIoAndDecodeAgain();
    }

    char nextChar;
    nextChar = nextChar();

    if (nextChar != parseChar) {
      throw new UnsupportedOperationException("Implement me");
    }

    return parseCharAction;
  }

  private Action executeParseHeader() {
    char c;
    c = nextChar();

    if (Character.isAlphabetic(c)) {
      stringBuilder.setLength(0);

      stringBuilder.append(c);

      return Action.PARSE_HEADER_NAME;
    }

    if (c != '\r') {
      throw new UnsupportedOperationException("Implement me");
    }

    if (!hasNextChar()) {
      return executeIoAndDecodeAgain();
    }

    c = nextChar();

    if (c != '\n') {
      throw new UnsupportedOperationException("Implement me");
    }

    return Action.BODY;
  }

  private Action executeParseHeaderName() {
    while (hasNextChar()) {
      char c;
      c = nextChar();

      if (c == HttpParser.COLON) {
        return Action.PARSE_HEADER_VALUE;
      }

      stringBuilder.append(c);
    }

    // TODO needs a max. length check

    return executeIoAndDecodeAgain();
  }

  private Action executeParseHeaderValue() {
    String originalName;
    originalName = stringBuilder.toString();

    String lowerCaseName;
    lowerCaseName = originalName.toLowerCase();

    headerParser = createHeaderParser(lowerCaseName, originalName);

    if (!hasNextChar()) {
      throw new UnsupportedOperationException("Implement me");
    }

    char c;
    c = nextChar();

    if (c != HttpParser.SP) {
      headerParser.consume(c);
    }

    return Action.PARSE_HEADER_VALUE_AFTER_OWS;
  }

  private Action executeParseHeaderValueAfterOws() {
    while (headerParser.shouldConsume() && hasNextChar()) {
      char c;
      c = nextChar();

      headerParser.consume(c);
    }

    if (headerParser.shouldConsume()) {
      return executeIoAndDecodeAgain();
    }

    if (headerParser.isMalformed()) {
      return toError400("Malformed header: " + stringBuilder.toString());
    }

    H header;
    header = headerParser.build();

    resultHeaders.add(header);

    return Action.PARSE_HEADER;
  }

  private Action executeParseHttpVersion() {
    if (nextCharSize() < HTTP_VERSION_MIN) {
      return executeIoAndDecodeAgain();
    }

    char major;
    major = '\0';

    char minor;
    minor = '\0';

    char[] source;
    source = nextCharArray(HTTP_VERSION_MIN);

    int sourceIndex;
    sourceIndex = 0;

    boolean matches;
    matches = true
        && source[sourceIndex++] == 'H'
        && source[sourceIndex++] == 'T'
        && source[sourceIndex++] == 'T'
        && source[sourceIndex++] == 'P'
        && source[sourceIndex++] == '/'
        && isOneOf(major = source[sourceIndex++], HTTP_MAJORS)
        && source[sourceIndex++] == '.'
        && isOneOf(minor = source[sourceIndex++], HTTP_MINORS);

    if (!matches) {
      return toError400("Bad http version: " + new String(source));
    }

    if (major != '1') {
      String message;
      message = new String(source);

      return toError(Status.HTTP_VERSION_NOT_SUPPORTED, message.trim());
    }

    switch (minor) {
      case '0':
        resultVersion = Version.V1_0;
        break;
      case '1':
        resultVersion = Version.V1_1;
        break;
      default:
        return toError400("Bad http version: " + new String(source));
    }

    return executeParseHttpVersionImpl();
  }

  private Action executeParseToEol() {
    boolean cr;
    cr = false;

    while (hasNextChar()) {
      char nextChar;
      nextChar = nextChar();

      if (nextChar == Http.CR) {
        cr = true;
        break;
      }

      parseToEol.append(nextChar);
    }

    if (!cr) {
      return executeIoAndDecodeAgain();
    }

    return toParseChar(Http.LF, parseToEolAction);
  }

  private Action executeReset() {
    bodyCharset = null;

    bodyKind = BodyKind.IGNORED;

    bodyLength = 0;

    byteBuffer.clear();

    charBuffer.clear();

    charIndex = 0;

    setDecoder(StandardCharsets.ISO_8859_1);

    decodeAction = Action.PARSE;

    exception = null;

    resultContentLength = null;

    resultContentType = null;

    resultHeaders.clear();

    status = null;

    statusMessage = null;

    executeResetImpl();

    return toIo();
  }

  private boolean ioTimedOut() {
    long elapsed;
    elapsed = System.currentTimeMillis() - ioStartTime;

    return elapsed > timeout;
  }

  private boolean isOneOf(char nextChar, char[] options) {
    for (char option : options) {
      if (nextChar == option) {
        return true;
      }
    }

    return false;
  }

  private Action toIo() {
    ioReadCount = 0;

    ioStartTime = System.currentTimeMillis();

    ioRunning = true;

    executeIo();

    return Action.IO_WAIT;
  }

  enum Action {

    BODY,

    BODY_TEXT,

    BODY_TEXT_IN_MEMORY,

    DECODE,

    END,

    ERROR,

    IO_READY,

    IO_TIMEOUT,

    IO_WAIT,

    PARSE,

    PARSE_CHAR,

    PARSE_HEADER,

    PARSE_HEADER_NAME,

    PARSE_HEADER_VALUE,

    PARSE_HEADER_VALUE_AFTER_OWS,

    PARSE_HTTP_VERSION,

    PARSE_REASON_PHRASE,

    PARSE_REQUEST_TARGET,

    PARSE_STATUS_CODE,

    PARSE_TO_EOL,

    RESET,

    RESULT;

  }

  enum BodyKind {

    IGNORED,

    TEXT;

  }

  private class ThisContentTypeVisitor implements ContentTypeVisitor {

    Action bodyAction;

    @Override
    public final void visitApplicationType(ApplicationType type) {
      switch (type) {
        case JSON:
          bodyAction = Action.BODY_TEXT;

          bodyCharset = StandardCharsets.UTF_8;
          break;
        default:
          throw new UnsupportedOperationException("Implement me @ " + type);
      }
    }

    @Override
    public final void visitApplicationType(ApplicationType type, Charset charset) {
      switch (type) {
        case JSON:
          bodyAction = Action.BODY_TEXT;

          bodyCharset = charset;
          break;
        default:
          throw new UnsupportedOperationException("Implement me @ " + type);
      }
    }

    @Override
    public final void visitImageType(ImageType type) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final void visitTextType(TextType type) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final void visitTextType(TextType type, Charset charset) {
      bodyAction = Action.BODY_TEXT;

      bodyCharset = charset;
    }

  }

}
