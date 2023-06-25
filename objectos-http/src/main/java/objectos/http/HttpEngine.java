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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import objectos.lang.NoteSink;

/**
 * @since 4
 */
final class HttpEngine implements HttpResponseHandle, Runnable {

  static final byte _BODY = 1;

  static final byte _DECODE = 2;

  static final byte _FINALLY = 3;

  static final byte _PARSE = 4;

  static final byte _PARSE_HEADER = 5;

  static final byte _PARSE_HEADER_NAME = 6;

  static final byte _PARSE_HEADER_VALUE = 7;

  static final byte _PARSE_HTTP_VERSION = 8;

  static final byte _PARSE_REQUEST_TARGET = 9;

  static final byte _RESPONSE = 11;

  static final byte _SKIP_HEADER_VALUE_OWS = 12;

  static final byte _START = 13;

  static final byte _STOP = 0;

  static final byte _WAIT_IO = 14;

  static final byte IO_READ = 1;

  private static final char[] HTTP_1_0 = "HTTP/1.0".toCharArray();

  private static final char[] HTTP_1_1 = "HTTP/1.1".toCharArray();

  ByteBuffer byteBuffer;

  boolean channelEof;

  long channelReadTotal;

  CharBuffer charBuffer;

  byte decodeAction;

  Throwable error;

  volatile byte ioReady;

  byte ioTask;

  byte state;

  private long channelDecodePosition;

  private int channelReadCount;

  private char[] charArray;

  private int charArrayIndex;

  private int charArrayLength;

  private final DateFormat dateFormat = Http.createDateFormat();

  private final CharsetDecoder decoder;

  private final CharsetEncoder encoder;

  private String headerName;

  private int headerNameOffset;

  private byte ioExceptional;

  private boolean ioRunning;

  Method method;

  @SuppressWarnings("unused")
  private final NoteSink noteSink;

  private final HttpProcessor processor;

  private ReadableByteChannel readableByteChannel;

  private ResponseTask responseTask;

  private Socket socket;

  private final StringBuilder stringBuilder;

  private final StringDeduplicator stringDeduplicator;

  private Version version;

  HttpEngine(int bufferSize,
             NoteSink noteSink,
             HttpProcessor processor,
             StringDeduplicator stringDeduplicator) {
    byteBuffer = ByteBuffer.allocate(bufferSize);

    charBuffer = CharBuffer.allocate(bufferSize);

    Charset charset;
    charset = StandardCharsets.ISO_8859_1;

    decoder = charset.newDecoder();

    encoder = charset.newEncoder();

    this.noteSink = noteSink;

    this.processor = processor;

    stringBuilder = new StringBuilder(128);

    this.stringDeduplicator = stringDeduplicator;
  }

  public final void executeIo() {
    try {
      executeIo(ioTask);
    } catch (Throwable e) {
      error = e;
    } finally {
      ioRunning = false;
    }
  }

  public final void executeOne() {
    try {
      state = execute(state);
    } catch (Throwable e) {
      error = e;

      state = execute(_FINALLY);
    }
  }

  @Override
  public final String formatDate(Date date) {
    return dateFormat.format(date);
  }

  @Override
  public final String formatDate(long millis) {
    Date date;
    date = new Date(millis);

    return formatDate(date);
  }

  @Override
  public final ByteBuffer getByteBuffer() {
    byteBuffer.clear();

    return byteBuffer;
  }

  @Override
  public final WritableByteChannel getChannel() {
    try {
      OutputStream outputStream;
      outputStream = socket.getOutputStream();

      return Channels.newChannel(outputStream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public final CharBuffer getCharBuffer() {
    charBuffer.clear();

    return charBuffer;
  }

  @Override
  public final CharsetEncoder getEncoder(Charset charset) {
    return encoder;
  }

  @Override
  public final StringBuilder getStringBuilder() {
    stringBuilder.setLength(0);

    return stringBuilder;
  }

  public final boolean isActive() {
    return state != _STOP;
  }

  @Override
  public final void run() {
    while (isActive()) {
      executeOne();
    }
  }

  public final void setInput(Socket socket) {
    this.socket = socket;

    state = _START;
  }

  final String stringValue() {
    return new String(charArray, charArrayIndex, charArrayLength);
  }

  private byte execute(byte state) {
    switch (state) {
      case _BODY:
        return executeBody();
      case _DECODE:
        return executeDecode();
      case _FINALLY:
        return executeFinally();
      case _PARSE:
        return executeParse();
      case _PARSE_HEADER:
        return executeParseHeader();
      case _PARSE_HEADER_NAME:
        return executeParseHeaderName();
      case _PARSE_HEADER_VALUE:
        return executeParseHeaderValue();
      case _PARSE_HTTP_VERSION:
        return executeParseHttpVersion();
      case _PARSE_REQUEST_TARGET:
        return executeParseRequestTarget();
      case _RESPONSE:
        return executeResponse();
      case _SKIP_HEADER_VALUE_OWS:
        return executeSkipHeaderValueOWS();
      case _START:
        return executeStart();
      case _WAIT_IO:
        return executeWaitIo();
      default:
        throw new AssertionError("Unexpected state: state=" + state);
    }
  }

  private byte executeBody() {
    responseTask = processor.responseTask();

    return _RESPONSE;
  }

  private byte executeDecode() {
    charBuffer.position(charArrayIndex);

    if (charBuffer.position() > 0) {
      charBuffer.compact();

      headerNameOffset = 0;
    }

    int dataPosition;
    dataPosition = byteBuffer.position();

    int dataLimit;
    dataLimit = byteBuffer.limit();

    int preventOverflowLimit;
    preventOverflowLimit = dataPosition + charBuffer.remaining();

    preventOverflowLimit = Math.min(preventOverflowLimit, dataLimit);

    byteBuffer.limit(preventOverflowLimit);

    boolean endOfInput;
    endOfInput = false;

    if (channelEof) {
      long expectedAfterObjectPosition;
      expectedAfterObjectPosition = channelDecodePosition + byteBuffer.remaining();

      endOfInput = expectedAfterObjectPosition >= channelReadTotal;
    }

    CoderResult coderResult;
    coderResult = decoder.decode(byteBuffer, charBuffer, endOfInput);

    byteBuffer.limit(dataLimit);

    if (!coderResult.isUnderflow()) {
      return toStubException("coderResult != underflow");
    }

    channelDecodePosition += byteBuffer.position() - dataPosition;

    if (channelDecodePosition > channelReadTotal) {
      return toStubException("channelDecodePosition > channelReadCount");
    }

    charBuffer.flip();

    charArray = charBuffer.array();

    charArrayIndex = charBuffer.position();

    charArrayLength = charBuffer.limit();

    charBuffer.position(charBuffer.limit());

    return decodeAction;
  }

  private byte executeFinally() {
    try {
      socket.close();
    } catch (IOException e) {
      error = e;
    }

    byteBuffer.clear();

    channelDecodePosition = 0;

    channelEof = false;

    channelReadCount = 0;

    channelReadTotal = 0;

    charArray = null;

    charArrayIndex = 0;

    charArrayLength = 0;

    charBuffer.clear();

    decodeAction = 0;

    decoder.reset();

    encoder.reset();

    headerName = null;

    headerNameOffset = 0;

    ioRunning = false;

    ioTask = 0;

    method = null;

    stringBuilder.setLength(0);

    version = null;

    return _STOP;
  }

  private byte executeInline(byte target) {
    state = target;

    return execute(target);
  }

  private void executeIo(byte task) throws IOException {
    switch (task) {
      case IO_READ:
        executeIoRead();
        break;
      default:
        throw new AssertionError("Unexpected I/O task: task=" + task);
    }
  }

  private void executeIoRead() throws IOException {
    if (byteBuffer.position() > 0) {
      byteBuffer.compact();
    }

    if (readableByteChannel == null) {
      InputStream inputStream;
      inputStream = socket.getInputStream();

      readableByteChannel = Channels.newChannel(inputStream);
    }

    channelReadCount = readableByteChannel.read(byteBuffer);

    if (channelReadCount > 0) {
      channelReadTotal += channelReadCount;

      byteBuffer.flip();
    }
  }

  private byte executeParse() {
    char c;
    c = charArray[charArrayIndex++];

    Method candidate;

    switch (c) {
      case 'G':
        candidate = Method.GET;
        break;
      default:
        throw new UnsupportedOperationException("Implement me");
    }

    char[] suffix;
    suffix = candidate.parseSuffix;

    // suffix + SP
    int minRequiredChars;
    minRequiredChars = suffix.length + 1;

    if (!hasRemaining(minRequiredChars)) {
      return toNeedsInput();
    }

    for (int i = 0; i < suffix.length; i++) {
      c = charArray[charArrayIndex++];

      char s;
      s = suffix[i];

      if (c != s) {
        return toError(Status.BAD_REQUEST, "Bad request line");
      }
    }

    c = charArray[charArrayIndex++];

    if (c != HttpParser.SP) {
      return toError(Status.BAD_REQUEST, "Bad request line");
    }

    method = candidate;

    return executeInline(_PARSE_REQUEST_TARGET);
  }

  private byte executeParseHeader() {
    if (!hasRemaining(2)) {
      return toNeedsInput();
    }

    headerNameOffset = charArrayIndex;

    char c;
    c = charArray[charArrayIndex];

    if (Character.isAlphabetic(c)) {
      charArray[charArrayIndex++] = Character.toLowerCase(c);

      return executeInline(_PARSE_HEADER_NAME);
    }

    charArrayIndex++;

    if (c != Http.CR) {
      throw new UnsupportedOperationException("Implement me");
    }

    c = charArray[charArrayIndex++];

    if (c != Http.LF) {
      throw new UnsupportedOperationException("Implement me");
    }

    return executeInline(_BODY);
  }

  private byte executeParseHeaderName() {
    boolean colon;
    colon = false;

    while (charArrayIndex < charArrayLength) {
      char c;
      c = charArray[charArrayIndex];

      charArray[charArrayIndex++] = Character.toLowerCase(c);

      if (c == Http.COLON) {
        colon = true;

        break;
      }
    }

    if (!colon) {
      charArrayIndex = headerNameOffset;

      return toNeedsInput();
    }

    int len;
    len = charArrayIndex - headerNameOffset - 1;

    String name;
    name = new String(charArray, headerNameOffset, len);

    headerName = stringDeduplicator.dedup(name);

    stringBuilder.setLength(0);

    return executeInline(_SKIP_HEADER_VALUE_OWS);
  }

  private byte executeParseHeaderValue() {
    boolean found;
    found = false;

    while (charArrayIndex < charArrayLength) {
      char c;
      c = charArray[charArrayIndex++];

      if (c == Http.CR) {
        found = true;

        break;
      }

      stringBuilder.append(c);
    }

    if (!found) {
      return toNeedsInput();
    }

    char c;
    c = charArray[charArrayIndex++];

    if (c != Http.LF) {
      throw new UnsupportedOperationException("Implement me");
    }

    String value;
    value = stringBuilder.toString();

    processor.requestHeader(headerName, value);

    return executeInline(_PARSE_HEADER);
  }

  private byte executeParseHttpVersion() {
    int len;
    len = HTTP_1_0.length;

    // + 2 from CRLF
    if (!hasRemaining(len + 2)) {
      return toNeedsInput();
    }

    int mismatch;
    mismatch = Arrays.mismatch(
      charArray, charArrayIndex, charArrayIndex + len,
      HTTP_1_1, 0, len);

    if (mismatch != -1) {
      throw new UnsupportedOperationException("Implement me");
    }

    charArrayIndex += len;

    version = Version.V1_1;

    int offset;
    offset = charArrayIndex;

    boolean matches;
    matches = true
        && charArray[charArrayIndex++] == '\r'
        && charArray[charArrayIndex++] == '\n';

    if (!matches) {
      String s;
      s = new String(charArray, offset, charArrayIndex - offset);

      return toError(Status.BAD_REQUEST, "Expected CRLF but found " + s);
    }

    len = stringBuilder.length();

    char[] a;
    a = new char[len];

    stringBuilder.getChars(0, len, a, 0);

    RequestTargetImpl impl;
    impl = new RequestTargetImpl(a);

    processor.requestLine(method, impl, version);

    return executeInline(_PARSE_HEADER);
  }

  private byte executeParseRequestTarget() {
    boolean found;
    found = false;

    while (charArrayIndex < charArrayLength) {
      char c;
      c = charArray[charArrayIndex++];

      if (c == HttpParser.SP) {
        found = true;

        break;
      }

      stringBuilder.append(c);
    }

    if (!found) {
      return toNeedsInput();
    } else {
      return executeInline(_PARSE_HTTP_VERSION);
    }
  }

  private byte executeResponse() {
    if (responseTask.isActive()) {
      responseTask.executeOne();

      return _RESPONSE;
    } else {
      return _FINALLY;
    }
  }

  private byte executeSkipHeaderValueOWS() {
    boolean found;
    found = false;

    while (charArrayIndex < charArrayLength) {
      char c;
      c = charArray[charArrayIndex];

      if (c != Http.SP && c != Http.HTAB) {
        found = true;

        break;
      }

      charArrayIndex++;
    }

    if (found) {
      return executeInline(_PARSE_HEADER_VALUE);
    } else {
      return toNeedsInput();
    }
  }

  private byte executeStart() {
    processor.requestStart(this);

    return toIo(
      IO_READ, toDecode(_PARSE), _FINALLY
    );
  }

  private byte executeWaitIo() {
    if (ioRunning) {
      return _WAIT_IO;
    }

    else if (error != null) {
      return ioExceptional;
    }

    else if (channelReadCount == 0) {
      return toIo(ioTask, ioReady, ioExceptional);
    }

    else if (channelReadCount < 0) {
      channelEof = true;

      return ioReady;
    }

    else {
      return ioReady;
    }
  }

  private boolean hasRemaining(int expected) {
    int remaining;
    remaining = charArrayLength - charArrayIndex;

    return remaining >= expected;
  }

  private byte toDecode(byte onReady) {
    decodeAction = onReady;

    return _DECODE;
  }

  private byte toError(Status status, String message) {
    throw new UnsupportedOperationException("Implement me");
  }

  private byte toIo(byte task, byte onReady, byte onExceptional) {
    ioTask = task;

    ioReady = onReady;

    ioExceptional = onExceptional;

    ioRunning = true;

    executeIo();

    return _WAIT_IO;
  }

  private byte toNeedsInput() {
    if (channelEof) {
      throw new UnsupportedOperationException("Implement me");
    }

    return toIo(
      IO_READ, toDecode(state), _FINALLY
    );
  }

  private byte toStubException(String string) {
    throw new UnsupportedOperationException("Implement me: " + string);
  }

}