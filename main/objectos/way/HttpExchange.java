/*
 * Copyright (C) 2016-2025 Objectos Software LTDA.
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
package objectos.way;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

final class HttpExchange extends HttpSupport implements Closeable {

  private record Notes(
      Note.Ref2<String, String> hexdump
  ) {

    static Notes get() {
      final Class<?> s;
      s = Http.Exchange.class;

      return new Notes(
          Note.Ref2.create(s, "HEX", Note.ERROR)
      );
    }

  }

  public enum ParseStatus {
    // keep going
    NORMAL,

    // SocketInput statuses
    EOF,

    UNEXPECTED_EOF,

    OVERFLOW,

    // 400 bad request
    INVALID_METHOD,

    INVALID_TARGET,

    INVALID_PROTOCOL,

    INVALID_REQUEST_LINE_TERMINATOR,

    INVALID_HEADER,

    INVALID_CONTENT_LENGTH,

    // 414 actually
    URI_TOO_LONG;

    public final boolean isError() {
      return this != NORMAL;
    }

    final boolean isNormal() {
      return this == NORMAL;
    }

    final boolean isBadRequest() {
      return compareTo(INVALID_METHOD) >= 0;
    }
  }

  static final class SendException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SendException(IOException cause) {
      super(cause);
    }

    @Override
    public final IOException getCause() {
      return (IOException) super.getCause();
    }
  }

  private enum RequestBodyKind {
    EMPTY,

    IN_BUFFER,

    FILE;
  }

  private static final Notes NOTES = Notes.get();

  private static final int _START = 0;
  private static final int _PARSE = 1;
  private static final int _REQUEST = 2;
  private static final int _RESPONSE = 3;
  private static final int _PROCESSED = 4;

  private static final int STATE_MASK = 0xF;
  private static final int BITS_MASK = ~STATE_MASK;

  private static final int KEEP_ALIVE = 1 << 4;
  private static final int CONTENT_LENGTH = 1 << 5;
  private static final int CHUNKED = 1 << 6;

  private Map<String, Object> attributes;

  private int bitset = 0;

  private final Note.Sink noteSink;

  ParseStatus parseStatus = ParseStatus.NORMAL;

  private final Closeable socket;

  // RequestBody

  private RequestBodyKind requestBodyKind = RequestBodyKind.EMPTY;

  private Path requestBodyDirectory;

  private Path requestBodyFile;

  // RequestHeaders

  HttpHeaderName headerName;

  Map<HttpHeaderName, HttpHeader> headers;

  // RequestLine

  private Http.Method method;

  private String path;

  int pathIndex;

  private int pathLimit;

  Map<String, String> pathParams;

  private Map<String, Object> queryParams;

  private boolean queryParamsReady;

  private int queryStart;

  private String rawValue;

  private Http.Version version;

  // SocketInput

  private static final int HARD_MAX_BUFFER_SIZE = 1 << 14;

  byte[] buffer;

  int bufferIndex = 0;

  int bufferLimit = 0;

  private final InputStream inputStream;

  int lineLimit = 0;

  private final int maxBufferSize;

  private final OutputStream outputStream;

  public HttpExchange(Socket socket, int bufferSizeInitial, int bufferSizeMax, Clock clock, Note.Sink noteSink) throws IOException {
    this(socket, socket.getInputStream(), socket.getOutputStream(), bufferSizeInitial, bufferSizeMax, clock, noteSink);
  }

  private HttpExchange(
      Closeable socket,
      InputStream inputStream,
      OutputStream outputStream,
      int bufferSizeInitial,
      int bufferSizeMax,
      Clock clock,
      Note.Sink noteSink
  ) {

    final int initialSize;
    initialSize = powerOfTwo(bufferSizeInitial);

    this.buffer = new byte[initialSize];

    this.clock = clock;

    this.inputStream = inputStream;

    this.maxBufferSize = powerOfTwo(bufferSizeMax);

    this.noteSink = noteSink;

    this.outputStream = outputStream;

    this.socket = socket;

  }

  HttpExchange(HttpExchangeConfig config) {

    attributes = config.attributes;

    final int initialSize;
    initialSize = powerOfTwo(config.bufferSizeInitial);

    buffer = new byte[initialSize];

    clock = config.clock;

    inputStream = config.inputStream();

    maxBufferSize = config.bufferSizeMax;

    noteSink = config.noteSink;

    outputStream = new ByteArrayOutputStream();

    socket = () -> {}; // noop closeable

  }

  /**
   * Parses the specified string into a new request-target instance.
   *
   * @param target
   *        the raw (undecoded) request-target value
   *
   * @return a new request target instance
   *
   * @throws IllegalArgumentException
   *         if the string represents an invalid request-target value
   */
  public static HttpExchange parseRequestTarget(String target) {
    Objects.requireNonNull(target, "target == null");

    // append a line terminator
    target = target + " \r\n";

    byte[] bytes;
    bytes = target.getBytes(StandardCharsets.UTF_8);

    // in-memory stream... no closing needed...
    InputStream inputStream;
    inputStream = new ByteArrayInputStream(bytes);

    HttpExchange requestLine;
    requestLine = new HttpExchange(null, inputStream, null, 1024, 4096, null, null);

    try {
      requestLine.parseLine();

      requestLine.parseRequestTarget();
    } catch (IOException e) {
      throw new AssertionError("In-memory stream does not throw IOException", e);
    }

    ParseStatus parseStatus;
    parseStatus = requestLine.parseStatus;

    if (parseStatus.isError()) {
      throw new IllegalArgumentException(parseStatus.name());
    }

    return requestLine;
  }

  static final int powerOfTwo(int size) {
    // maybe size is already power of 2
    int x;
    x = size - 1;

    int leading;
    leading = Integer.numberOfLeadingZeros(x);

    int n;
    n = -1 >>> leading;

    if (n < 0) {
      // should not happen as minimal buffer size is 128
      throw new IllegalArgumentException("Buffer size is too small");
    }

    if (n >= HARD_MAX_BUFFER_SIZE) {
      return HARD_MAX_BUFFER_SIZE;
    }

    return n + 1;
  }

  @Override
  public final void close() throws IOException {
    try {
      requestBodyClose();
    } finally {
      socket.close();
    }
  }

  @Override
  public final String toString() {
    final int state;
    state = bitset & STATE_MASK;

    return switch (state) {
      case _START -> "HttpExchange[START]";

      case _PARSE -> "HttpExchange[PARSE]";

      case _REQUEST -> new String(buffer, 0, bufferLimit, StandardCharsets.UTF_8);

      case _PROCESSED -> {
        if (outputStream instanceof ByteArrayOutputStream impl) {
          final byte[] bytes;
          bytes = impl.toByteArray();

          yield new String(bytes, StandardCharsets.UTF_8);
        } else {
          yield "HttpExchange[PROCESSED]";
        }
      }

      default -> "HttpExchange[" + state + "]";
    };
  }

  // ##################################################################
  // # BEGIN: HTTP/1.1 request parsing
  // ##################################################################

  public final ParseStatus parse() throws IOException, IllegalStateException {
    if (testState(_START)) {
      // noop
    }

    else if (testState(_PROCESSED)) {
      resetSocketInput();

      resetRequestLine();

      resetHeaders();

      resetRequestBody();

      resetServerLoop();
    }

    else {
      throw new IllegalStateException("""
      The parse() metod must only be called after:
      1) loop creation; or
      2) a successful commit operation
      """);
    }

    // force bits reset
    bitset = _PARSE;

    // request line

    parseRequestLine();

    if (parseStatus.isError()) {
      return parseStatus;
    }

    // request headers

    parseHeaders();

    if (parseStatus.isError()) {
      return parseStatus;
    }

    // request body

    parseRequestBody();

    if (parseStatus.isError()) {
      return parseStatus;
    }

    parseRequestEnd();

    return parseStatus;
  }

  private void resetSocketInput() {
    bufferLimit = 0;

    bufferIndex = 0;

    lineLimit = 0;

    parseStatus = ParseStatus.NORMAL;
  }

  private void resetRequestLine() {
    method = null;

    pathLimit = 0;

    if (pathParams != null) {
      pathParams.clear();
    }

    path = null;

    if (queryParams != null) {
      queryParams.clear();
    }

    queryParamsReady = false;

    queryStart = 0;

    rawValue = null;

    version = null;
  }

  private void resetHeaders() {
    headerName = null;

    if (headers != null) {
      headers.clear();
    }
  }

  private void resetRequestBody() {
    requestBodyKind = RequestBodyKind.EMPTY;

    requestBodyFile = null;
  }

  private void resetServerLoop() {
    if (attributes != null) {
      attributes.clear();
    }
  }

  // ##################################################################
  // # BEGIN: HTTP/1.1 request parsing || request line
  // ##################################################################

  final void parseRequestLine() throws IOException {
    parseLine();

    if (parseStatus == ParseStatus.UNEXPECTED_EOF) {
      if (bufferLimit == 0) {
        // buffer is empty, this is an expected EOF
        parseStatus = ParseStatus.EOF;
      }

      return;
    }

    parseMethod();

    if (method == null) {
      // parse method failed -> bad request
      parseStatus = ParseStatus.INVALID_METHOD;

      return;
    }

    parseRequestTarget();

    parseVersion();

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }

    if (!consumeIfEndOfLine()) {
      parseStatus = ParseStatus.INVALID_REQUEST_LINE_TERMINATOR;

      return;
    }
  }

  private static final byte[] _CONNECT = "CONNECT ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _DELETE = "DELETE ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _GET = "GET ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _HEAD = "HEAD ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _OPTIONS = "OPTIONS ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _POST = "POST ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _PUT = "PUT ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _PATCH = "PATCH ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _TRACE = "TRACE ".getBytes(StandardCharsets.UTF_8);

  private void parseMethod() throws IOException {
    if (bufferIndex >= lineLimit) {
      // empty line... nothing to do
      return;
    }

    byte first;
    first = buffer[bufferIndex];

    // based on the first char, we select out method candidate

    switch (first) {
      case 'C' -> parseMethod0(Http.Method.CONNECT, _CONNECT);

      case 'D' -> parseMethod0(Http.Method.DELETE, _DELETE);

      case 'G' -> parseMethod0(Http.Method.GET, _GET);

      case 'H' -> parseMethod0(Http.Method.HEAD, _HEAD);

      case 'O' -> parseMethod0(Http.Method.OPTIONS, _OPTIONS);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod0(Http.Method.TRACE, _TRACE);
    }
  }

  private void parseMethod0(Http.Method candidate, byte[] candidateBytes) throws IOException {
    if (matches(candidateBytes)) {
      method = candidate;
    }
  }

  private void parseMethodP() throws IOException {
    // method starts with a P. It might be:
    // - POST
    // - PUT
    // - PATCH

    // we'll try them in sequence

    parseMethod0(Http.Method.POST, _POST);

    if (method != null) {
      return;
    }

    parseMethod0(Http.Method.PUT, _PUT);

    if (method != null) {
      return;
    }

    parseMethod0(Http.Method.PATCH, _PATCH);

    if (method != null) {
      return;
    }
  }

  final void parseRequestTarget() throws IOException {
    int startIndex;
    startIndex = parsePathStart();

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }

    parsePathRest(startIndex);

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }
  }

  private int parsePathStart() throws IOException {
    // we will check if the request target starts with a '/' char

    int targetStart;
    targetStart = bufferIndex;

    if (bufferIndex >= lineLimit) {
      // reached EOL -> bad request
      parseStatus = ParseStatus.INVALID_TARGET;

      return 0;
    }

    byte b;
    b = buffer[bufferIndex++];

    if (b != Bytes.SOLIDUS) {
      // first char IS NOT '/' => BAD_REQUEST
      parseStatus = ParseStatus.INVALID_TARGET;

      return 0;
    }

    // mark request path start

    return targetStart;
  }

  private void parsePathRest(int startIndex) throws IOException {
    // we will look for the first:
    // - ? char
    // - SP char
    int index;
    index = indexOf(Bytes.QUESTION_MARK, Bytes.SP);

    if (index < 0) {
      // trailing char was not found
      parseStatus = ParseStatus.URI_TOO_LONG;

      return;
    }

    // index where path ends
    int pathEndIndex;
    pathEndIndex = index;

    // as of now target ends at the path
    int targetEndIndex;
    targetEndIndex = pathEndIndex;

    // as of now query starts and ends at path i.e. len = 0
    int queryStartIndex;
    queryStartIndex = pathEndIndex;

    // we'll continue at the '?' or SP char
    bufferIndex = index;

    byte b;
    b = buffer[bufferIndex++];

    if (b == Bytes.QUESTION_MARK) {
      queryStartIndex = bufferIndex;

      targetEndIndex = indexOf(Bytes.SP);

      if (targetEndIndex < 0) {
        // trailing char was not found
        parseStatus = ParseStatus.URI_TOO_LONG;

        return;
      }

      // we'll continue immediately after the SP
      bufferIndex = targetEndIndex + 1;
    }

    rawValue = bufferToString(startIndex, targetEndIndex);

    pathLimit = pathEndIndex - startIndex;

    queryStart = queryStartIndex - startIndex;
  }

  static final byte[] HTTP_VERSION_PREFIX = {'H', 'T', 'T', 'P', '/'};

  private void parseVersion() {
    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' = 8 bytes

    if (!matches(HTTP_VERSION_PREFIX)) {
      // buffer does not start with 'HTTP/'
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    // check if we  have '1' '.' '1' = 3 bytes

    int requiredIndex;
    requiredIndex = bufferIndex + 3 - 1;

    if (requiredIndex >= lineLimit) {
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeMajor;
    maybeMajor = buffer[bufferIndex++];

    if (!Http.isDigit(maybeMajor)) {
      // major version is not a digit => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeDot;
    maybeDot = buffer[bufferIndex++];

    if (maybeDot != '.') {
      // major version not followed by a DOT => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeMinor;
    maybeMinor = buffer[bufferIndex++];

    if (!Http.isDigit(maybeMinor)) {
      // minor version is not a digit => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    final int major;
    major = maybeMajor - 0x30;

    final int minor;
    minor = maybeMinor - 0x30;

    if (major == 1 && minor == 1) {
      version = Http.Version.HTTP_1_1;
    } else {
      version = Http.Version.HTTP_1_0;
    }
  }

  // ##################################################################
  // # END: HTTP/1.1 request parsing || request line
  // ##################################################################

  // ##################################################################
  // # BEGIN: HTTP/1.1 request parsing || headers
  // ##################################################################

  final void parseHeaders() throws IOException {
    parseLine();

    while (parseStatus.isNormal() && !consumeIfEmptyLine()) {
      parseHeaderName();

      if (parseStatus.isError()) {
        break;
      }

      parseHeaderValue();

      if (parseStatus.isError()) {
        break;
      }

      parseLine();
    }

    // clear last header name just in case
    headerName = null;
  }

  private void parseHeaderName() {
    // we reset any previous found header name

    headerName = null;

    // possible header name starts here
    int startIndex;
    startIndex = bufferIndex;

    int colonIndex;
    colonIndex = indexOf(Bytes.COLON);

    if (colonIndex < 0) {
      // no colon found
      parseStatus = ParseStatus.INVALID_HEADER;

      return;
    }

    final int length;
    length = colonIndex - startIndex;

    if (length == 0) {
      // empty header name
      parseStatus = ParseStatus.INVALID_HEADER;

      return;
    }

    // let's validate and lowercase the header name
    final byte[] bytes;
    bytes = new byte[length];

    for (int idx = 0; idx < length; idx++) {
      final int offset;
      offset = startIndex + idx;

      final byte b;
      b = buffer[offset];

      final byte mapped;
      mapped = HttpHeaderName.map(b);

      if (mapped < 0) {
        // header name contains an invalid character
        parseStatus = ParseStatus.INVALID_HEADER;

        return;
      }

      bytes[idx] = mapped;
    }

    final String lowerCase;
    lowerCase = new String(bytes, StandardCharsets.US_ASCII);

    final HttpHeaderName standard;
    standard = HttpHeaderName.byLowerCase(lowerCase);

    if (standard != null) {
      headerName = standard;
    } else {
      headerName = HttpHeaderName.ofLowerCase(lowerCase);
    }

    // resume immediately after the colon
    bufferIndex = colonIndex + 1;
  }

  private void parseHeaderValue() {
    int startIndex;
    startIndex = parseHeaderValueStart();

    int endIndex;
    endIndex = parseHeaderValueEnd(startIndex);

    if (startIndex > endIndex) {
      // value has negative length... is it possible?
      hexDump();

      throw new UnsupportedOperationException("Implement me");
    }

    final HttpHeader header;
    header = HttpHeader.create(startIndex, endIndex);

    if (headers == null) {
      headers = Util.createMap();
    }

    final HttpHeader existing;
    existing = headers.put(headerName, header);

    if (existing != null) {
      existing.add(header);

      headers.put(headerName, existing);
    }
  }

  final void hexDump() {
    HexFormat format;
    format = HexFormat.of();

    String bufferDump;
    bufferDump = format.formatHex(buffer, 0, bufferLimit);

    String args;
    args = "bufferIndex=" + bufferIndex + ";lineLimit=" + lineLimit;

    noteSink.send(NOTES.hexdump, bufferDump, args);
  }

  private int parseHeaderValueStart() {
    // consumes and discard a single leading OWS if present
    byte maybeOws;
    maybeOws = buffer[bufferIndex];

    if (Bytes.isOptionalWhitespace(maybeOws)) {
      // consume and discard leading OWS
      bufferIndex++;
    }

    return bufferIndex;
  }

  private int parseHeaderValueEnd(int startIndex) {
    int end;
    end = lineLimit;

    byte maybeCR;
    maybeCR = buffer[end - 1];

    if (maybeCR == Bytes.CR) {
      // value ends at the CR of the line end CRLF
      end = end - 1;
    }

    if (end != startIndex) {

      byte maybeOWS;
      maybeOWS = buffer[end - 1];

      if (Bytes.isOptionalWhitespace(maybeOWS)) {
        // value ends at the trailing OWS
        end = end - 1;
      }

    }

    // resume immediately after lineLimite
    bufferIndex = lineLimit + 1;

    return end;
  }

  // ##################################################################
  // # END: HTTP/1.1 request parsing || headers
  // ##################################################################

  // ##################################################################
  // # BEGIN: HTTP/1.1 request parsing || body
  // ##################################################################

  final void parseRequestBody() throws IOException {
    final HttpHeader contentLength;
    contentLength = headerUnchecked(HttpHeaderName.CONTENT_LENGTH);

    if (contentLength != null) {
      final long length;
      length = contentLength.unsignedLongValue(buffer);

      if (length < 0) {
        // TODO 413 Payload Too Large
        parseStatus = ParseStatus.INVALID_HEADER;
      }

      else if (canBuffer(length)) {
        int read;
        read = read(length);

        if (read < 0) {
          throw new EOFException();
        }

        requestBodyKind = RequestBodyKind.IN_BUFFER;
      }

      else {
        if (requestBodyDirectory == null) {
          requestBodyFile = Files.createTempFile("objectos-way-request-body-", ".tmp");
        } else {
          requestBodyFile = Files.createTempFile(requestBodyDirectory, "objectos-way-request-body-", ".tmp");
        }

        long read;
        read = read(requestBodyFile, length);

        if (read < 0) {
          parseStatus = ParseStatus.EOF;
        } else {
          requestBodyKind = RequestBodyKind.FILE;
        }
      }

      return;
    }

    final HttpHeader transferEncoding;
    transferEncoding = headerUnchecked(HttpHeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      // TODO 501 Not Implemented
      throw new UnsupportedOperationException("Implement me");
    }

    // TODO 411 Length Required
  }

  // ##################################################################
  // # END: HTTP/1.1 request parsing || body
  // ##################################################################

  // ##################################################################
  // # BEGIN: HTTP/1.1 request parsing || keep alive
  // ##################################################################

  final void parseRequestEnd() {
    // handle keep alive

    clearBit(KEEP_ALIVE);

    if (version == Http.Version.HTTP_1_1) {
      setBit(KEEP_ALIVE);
    }

    HttpHeader connection;
    connection = headerUnchecked(HttpHeaderName.CONNECTION);

    if (connection != null) {
      final String value;
      value = connection.get(buffer);

      if (value != null) {

        if (value.equalsIgnoreCase("keep-alive")) {
          setBit(KEEP_ALIVE);
        }

        else if (value.equalsIgnoreCase("close")) {
          clearBit(KEEP_ALIVE);
        }

      }

    }

    setState(_REQUEST);
  }

  // ##################################################################
  // # END: HTTP/1.1 request parsing || keep alive
  // ##################################################################

  private void checkRequest() {
    Check.state(
        !badRequest(),

        """
        This request method can only be invoked:
        - after a successful parse() operation; and
        - before any response related method invocation.
        """
    );
  }

  private boolean badRequest() {
    Check.state(testState(_REQUEST), "Http.Request methods can only be invoked after a parse() operation");

    return parseStatus.isBadRequest();
  }

  // ##################################################################
  // # BEGIN: Http.Exchange API || request line
  // ##################################################################

  @Override
  public final Http.Method method() {
    return method;
  }

  @Override
  public final Http.Version version() {
    return version;
  }

  // ##################################################################
  // # END: Http.Exchange API || request line
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || request target
  // ##################################################################

  @Override
  public final String path() {
    if (path == null) {
      String raw;
      raw = rawPath();

      path = decode(raw);
    }

    return path;
  }

  @Override
  public final String pathParam(String name) {
    Objects.requireNonNull(name, "name == null");

    String result;
    result = null;

    if (pathParams != null) {
      result = pathParams.get(name);
    }

    return result;
  }

  final void pathReset() {
    pathIndex = 0;

    if (pathParams != null) {
      pathParams.clear();
    }
  }

  final void pathParams(Map<String, String> value) {
    pathParams = value;
  }

  final boolean testPathExact(String exact) {
    final String path;
    path = path();

    final int thisLength;
    thisLength = path.length() - pathIndex;

    final int thatLength;
    thatLength = exact.length();

    if (thisLength == thatLength && path.regionMatches(pathIndex, exact, 0, thatLength)) {
      pathIndex += thatLength;

      return true;
    } else {
      return false;
    }
  }

  final boolean testPathParam(String name, char terminator) {
    final String path;
    path = path();

    final int terminatorIndex;
    terminatorIndex = path.indexOf(terminator, pathIndex);

    if (terminatorIndex < 0) {
      return false;
    }

    final String varValue;
    varValue = path.substring(pathIndex, terminatorIndex);

    // immediately after the terminator
    pathIndex = terminatorIndex + 1;

    if (pathParams == null) {
      pathParams = Util.createMap();
    }

    pathParams.put(name, varValue);

    return true;
  }

  final boolean testPathParamLast(String name) {
    final String path;
    path = path();

    final int solidus;
    solidus = path.indexOf('/', pathIndex);

    if (solidus < 0) {

      final String varValue;
      varValue = path.substring(pathIndex);

      pathIndex += varValue.length();

      if (pathParams == null) {
        pathParams = Util.createMap();
      }

      pathParams.put(name, varValue);

      return true;

    } else {

      return false;

    }
  }

  final boolean testPathRegion(String region) {
    final String path;
    path = path();

    if (path.regionMatches(pathIndex, region, 0, region.length())) {
      pathIndex += region.length();

      return true;
    } else {
      return false;
    }
  }

  final boolean testPathEnd() {
    final String path;
    path = path();

    return pathIndex == path.length();
  }

  @Override
  public final String queryParam(String name) {
    Check.notNull(name, "name == null");

    Map<String, Object> params;
    params = $queryParams();

    return Http.queryParamsGet(params, name);
  }

  @Override
  public final List<String> queryParamAll(String name) {
    Check.notNull(name, "name == null");

    Map<String, Object> params;
    params = $queryParams();

    return Http.queryParamsGetAll(params, name);
  }

  @Override
  public final Set<String> queryParamNames() {
    Map<String, Object> params;
    params = $queryParams();

    return params.keySet();
  }

  private Map<String, Object> $queryParams() {
    if (!queryParamsReady) {
      if (queryParams == null) {
        queryParams = Util.createMap();
      }

      makeQueryParams(queryParams, this::decode);

      queryParamsReady = true;
    }

    return queryParams;
  }

  @Override
  public final String rawPath() {
    return rawValue.substring(0, pathLimit);
  }

  @Override
  public final String rawQuery() {
    return queryStart == pathLimit ? null : rawValue.substring(queryStart);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final String rawQueryWith(String name, String value) {
    if (name.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }

    Objects.requireNonNull(value, "value == null");

    String encodedKey;
    encodedKey = encode(name);

    String encodedValue;
    encodedValue = encode(value);

    int queryLength;
    queryLength = rawValue.length() - queryStart;

    if (queryLength < 2) {
      return encodedKey + "=" + encodedValue;
    }

    Map<String, Object> params;
    params = Util.createSequencedMap();

    makeQueryParams(params, Function.identity());

    params.put(encodedKey, encodedValue);

    return Http.queryParamsToString(params, Function.identity());
  }

  public final String rawValue() {
    return rawValue;
  }

  private void makeQueryParams(Map<String, Object> map, Function<String, String> decoder) {
    int queryLength;
    queryLength = rawValue.length() - queryStart;

    if (queryLength < 2) {
      // query is empty: either "" or "?"
      return;
    }

    String source;
    source = rawQuery();

    StringBuilder sb;
    sb = new StringBuilder();

    String key;
    key = null;

    for (int i = 0, len = source.length(); i < len; i++) {
      char c;
      c = source.charAt(i);

      switch (c) {
        case '=' -> {
          if (key != null) {
            Http.queryParamsAdd(map, decoder, key, "");
          }

          key = sb.toString();

          sb.setLength(0);
        }

        case '&' -> {
          String value;
          value = sb.toString();

          sb.setLength(0);

          if (key == null) {
            key = value;

            continue;
          }

          Http.queryParamsAdd(map, decoder, key, value);

          key = null;
        }

        default -> sb.append(c);
      }
    }

    String value;
    value = sb.toString();

    if (key != null) {
      Http.queryParamsAdd(map, decoder, key, value);
    } else {
      Http.queryParamsAdd(map, decoder, value, "");
    }
  }

  // ##################################################################
  // # END: Http.Exchange API || request target
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || request headers
  // ##################################################################

  @Override
  public final String header(Http.HeaderName name) {
    Objects.requireNonNull(name, "name == null");

    final HttpHeader maybe;
    maybe = headerUnchecked(name);

    if (maybe == null) {
      return null;
    }

    return maybe.get(buffer);
  }

  public final int size() {
    return headers != null ? headers.size() : 0;
  }

  private HttpHeader headerUnchecked(Http.HeaderName name) {
    if (headers == null) {
      return null;
    } else {
      return headers.get(name);
    }
  }

  // ##################################################################
  // # END: Http.Exchange API || request headers
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || request body
  // ##################################################################

  public void requestBodyDirectory(java.nio.file.Path directory) {
    requestBodyDirectory = directory;
  }

  private void requestBodyClose() throws IOException {
    if (requestBodyFile != null) {
      Files.delete(requestBodyFile);
    }
  }

  @Override
  public final InputStream bodyInputStream() throws IOException {
    checkRequest();

    return switch (requestBodyKind) {
      case EMPTY -> InputStream.nullInputStream();

      case IN_BUFFER -> openStreamImpl();

      case FILE -> Files.newInputStream(requestBodyFile);
    };
  }

  private InputStream openStreamImpl() {
    int length;
    length = bufferLimit - bufferIndex;

    return new ByteArrayInputStream(buffer, bufferIndex, length);
  }

  // ##################################################################
  // # END: Http.Exchange API || request body
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || request attributes
  // ##################################################################

  @Override
  public final <T> void set(Class<T> key, T value) {
    String name;
    name = key.getName(); // implicit null check

    Check.notNull(value, "value == null");

    Map<String, Object> map;
    map = attributes();

    map.put(name, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T get(Class<T> key) {
    String name;
    name = key.getName(); // implicit null check

    Map<String, Object> map;
    map = attributes();

    return (T) map.get(name);
  }

  private Map<String, Object> attributes() {
    if (attributes == null) {
      attributes = Util.createMap();
    }

    return attributes;
  }

  // ##################################################################
  // # END: Http.Exchange API || request attributes
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || response
  // ##################################################################

  @Override
  public final void respond(Http.Status status, Media.Bytes object, Consumer<Http.ResponseHeaders> headers) {
    final byte[] bytes;
    bytes = respond0(status, object);

    headers.accept(this);

    send0(bytes);
  }

  private byte[] respond0(Http.Status status, Media.Bytes object) {
    Objects.requireNonNull(status, "status == null");

    // early object validation
    String contentType;
    contentType = object.contentType();

    if (contentType == null) {
      throw new NullPointerException("The specified Lang.MediaObject provided a null content-type");
    }

    byte[] bytes;
    bytes = object.toByteArray();

    if (bytes == null) {
      throw new NullPointerException("The specified Lang.MediaObject provided a null byte array");
    }

    status0(status);

    dateNow();

    header0(Http.HeaderName.CONTENT_TYPE, contentType);

    header0(Http.HeaderName.CONTENT_LENGTH, bytes.length);

    return bytes;
  }

  private static final byte[] CHUNKED_TRAILER = "0\r\n\r\n".getBytes(StandardCharsets.UTF_8);

  @Override
  public final void respond(Http.Status status, Lang.MediaWriter writer) {
    final Charset charset;
    charset = respond0(status, writer);

    send0(writer, charset);
  }

  @Override
  public final void respond(Http.Status status, Lang.MediaWriter writer, Consumer<Http.ResponseHeaders> headers) {
    final Charset charset;
    charset = respond0(status, writer);

    headers.accept(this);

    if (testBit(CONTENT_LENGTH)) {
      throw new IllegalStateException(
          "Content-Length must not be set with a Lang.MediaWriter response"
      );
    }

    if (!testBit(CHUNKED)) {
      throw new IllegalStateException(
          "Transfer-Encoding: chunked must be set with a Lang.MediaWriter response"
      );
    }

    send0(writer, charset);
  }

  private Charset respond0(Http.Status status, Lang.MediaWriter writer) {
    Objects.requireNonNull(status, "status == null");

    // early writer validation
    final String contentType;
    contentType = writer.contentType();

    if (contentType == null) {
      throw new NullPointerException("The specified Lang.MediaWriter provided a null content-type");
    }

    final Charset charset;
    charset = writer.mediaCharset();

    if (charset == null) {
      throw new NullPointerException("The specified Lang.MediaWriter provided a null charset");
    }

    status0(status);

    dateNow();

    header0(Http.HeaderName.CONTENT_TYPE, contentType);

    header0(Http.HeaderName.TRANSFER_ENCODING, "chunked");

    return charset;
  }

  private void send0(Lang.MediaWriter writer, final Charset charset) {
    if (method == Http.Method.HEAD) {

      send0();

    } else {

      try {
        sendStart();

        bufferIndex = 0;

        CharWritableAppendable out;
        out = new CharWritableAppendable(charset);

        writer.mediaTo(out);

        out.flush();

        outputStream.write(CHUNKED_TRAILER);
      } catch (IOException e) {
        throw new SendException(e);
      } finally {
        setState(_PROCESSED);
      }

    }
  }

  public final void iseIfPossible(Throwable t) {
    if (testState(_RESPONSE)) {
      return;
    }

    if (testState(_PROCESSED)) {
      return;
    }

    StringWriter sw;
    sw = new StringWriter();

    PrintWriter pw;
    pw = new PrintWriter(sw);

    t.printStackTrace(pw);

    String msg;
    msg = sw.toString();

    byte[] bytes;
    bytes = msg.getBytes();

    status0(Http.Status.INTERNAL_SERVER_ERROR);

    dateNow();

    header0(Http.HeaderName.CONTENT_LENGTH, bytes.length);

    header0(Http.HeaderName.CONTENT_TYPE, "text/plain");

    header0(Http.HeaderName.CONNECTION, "close");

    send0(bytes);
  }

  private void checkResponse() {
    if (testState(_REQUEST)) {
      bufferIndex = 0;

      setState(_RESPONSE);

      return;
    }

    throw new IllegalStateException(
        """
        Response methods can only be invoked:
        - after a successful parse() operation; and
        - before the commit() method invocation.
        """
    );
  }

  static final byte[][] STATUS_LINES;

  static {
    int size;
    size = HttpStatus.size();

    byte[][] map;
    map = new byte[size][];

    for (int index = 0; index < size; index++) {
      HttpStatus status;
      status = HttpStatus.get(index);

      String response;
      response = Integer.toString(status.code()) + " " + status.reasonPhrase() + "\r\n";

      map[index] = Http.utf8(response);
    }

    STATUS_LINES = map;
  }

  @Override
  final void status0(Http.Status status) {
    checkResponse();

    Http.Version version;
    version = Http.Version.HTTP_1_1;

    writeBytes(version.responseBytes);

    HttpStatus internal;
    internal = (HttpStatus) status;

    byte[] statusBytes;
    statusBytes = STATUS_LINES[internal.index];

    writeBytes(statusBytes);
  }

  @Override
  final void header0(Http.HeaderName name, String value) {
    // write our the name
    final HttpHeaderName impl;
    impl = (HttpHeaderName) name;

    final byte[] nameBytes;
    nameBytes = impl.getBytes(version);

    writeBytes(nameBytes);

    // write out the separator
    writeBytes(Bytes.COLONSP);

    // write out the value
    byte[] valueBytes;
    valueBytes = value.getBytes(StandardCharsets.UTF_8);

    writeBytes(valueBytes);

    writeBytes(Bytes.CRLF);

    // handle connection: close if necessary
    if (name == Http.HeaderName.CONNECTION && value.equalsIgnoreCase("close")) {
      clearBit(KEEP_ALIVE);
    }

    else if (name == Http.HeaderName.CONTENT_LENGTH) {
      setBit(CONTENT_LENGTH);
    }

    else if (name == Http.HeaderName.TRANSFER_ENCODING) {
      if (value.equalsIgnoreCase("chunked")) {
        setBit(CHUNKED);
      } else {
        clearBit(CHUNKED);
      }
    }
  }

  @Override
  final void body0(Object original, byte[] bytes) {
    send0(bytes);
  }

  @Override
  final void send0() {
    try {
      sendStart();
    } catch (IOException e) {
      throw new SendException(e);
    } finally {
      setState(_PROCESSED);
    }
  }

  @Override
  final void send0(byte[] body) {
    if (method == Http.Method.HEAD) {

      send0();

    } else {

      try {
        sendStart();

        outputStream.write(body, 0, body.length); // implicity body null-check
      } catch (IOException e) {
        throw new SendException(e);
      } finally {
        setState(_PROCESSED);
      }

    }
  }

  @Override
  final void send0(java.nio.file.Path file) {
    Objects.requireNonNull(file, "file == null");

    if (method == Http.Method.HEAD) {

      send0();

    } else {

      try {
        sendStart();

        try (InputStream in = Files.newInputStream(file)) {
          in.transferTo(outputStream);
        }
      } catch (IOException e) {
        throw new SendException(e);
      } finally {
        setState(_PROCESSED);
      }

    }
  }

  @Override
  final void endResponse() {
    if (testState(_PROCESSED)) {
      // expected, no action
    } else if (testState(_RESPONSE)) {
      // unfinished response?
      send0();
    }
  }

  private void sendStart() throws IOException {
    writeBytes(Bytes.CRLF);

    outputStream.write(buffer, 0, bufferIndex);
  }

  private class CharWritableAppendable implements Appendable {
    private final Charset charset;

    public CharWritableAppendable(Charset charset) {
      this.charset = charset;
    }

    @Override
    public Appendable append(char c) throws IOException {
      String s;
      s = Character.toString(c);

      return append(s);
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
      String s;
      s = csq.toString();

      byte[] bytes;
      bytes = s.getBytes(charset);

      buffer(bytes);

      return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
      CharSequence sub;
      sub = csq.subSequence(start, end);

      return append(sub);
    }

    private void buffer(byte[] bytes) throws IOException {
      int bytesIndex;
      bytesIndex = 0;

      int remaining;
      remaining = bytes.length - bytesIndex;

      while (remaining > 0) {
        int maxAvailable;
        maxAvailable = maxBufferSize - bufferIndex;

        if (maxAvailable == 0) {
          flush();

          maxAvailable = maxBufferSize - bufferIndex;
        }

        int bytesToCopy;
        bytesToCopy = Math.min(remaining, maxAvailable);

        int requiredIndex;
        requiredIndex = bufferIndex + bytesToCopy - 1;

        if (requiredIndex >= buffer.length) {
          int minSize;
          minSize = requiredIndex + 1;

          int newSize;
          newSize = powerOfTwo(minSize);

          if (newSize > maxBufferSize) {
            throw new UnsupportedOperationException("Implement me");
          }

          buffer = Arrays.copyOf(buffer, newSize);
        }

        System.arraycopy(bytes, bytesIndex, buffer, bufferIndex, bytesToCopy);

        bufferIndex += bytesToCopy;

        bytesIndex += bytesToCopy;

        remaining -= bytesToCopy;
      }
    }

    final void flush() throws IOException {
      int chunkLength;
      chunkLength = bufferIndex;

      String lengthDigits;
      lengthDigits = Integer.toHexString(chunkLength);

      byte[] lengthBytes;
      lengthBytes = (lengthDigits + "\r\n").getBytes(StandardCharsets.UTF_8);

      outputStream.write(lengthBytes, 0, lengthBytes.length);

      int bufferRemaining;
      bufferRemaining = buffer.length - bufferIndex;

      if (bufferRemaining >= 2) {
        buffer[bufferIndex++] = Bytes.CR;
        buffer[bufferIndex++] = Bytes.LF;

        outputStream.write(buffer, 0, bufferIndex);
      } else {
        outputStream.write(buffer, 0, bufferIndex);

        outputStream.write(Bytes.CRLF);
      }

      bufferIndex = 0;
    }
  }

  private void writeBytes(byte[] bytes) {
    int length;
    length = bytes.length;

    int requiredIndex;
    requiredIndex = bufferIndex + length - 1;

    if (requiredIndex >= buffer.length) {
      int minSize;
      minSize = requiredIndex + 1;

      int newSize;
      newSize = powerOfTwo(minSize);

      if (newSize > maxBufferSize) {
        throw new UnsupportedOperationException("Implement me");
      }

      buffer = Arrays.copyOf(buffer, newSize);
    }

    System.arraycopy(bytes, 0, buffer, bufferIndex, length);

    bufferIndex += length;
  }

  // ##################################################################
  // # END: Http.Exchange API || response
  // ##################################################################

  public final boolean keepAlive() {
    return testBit(KEEP_ALIVE);
  }

  @Override
  public final boolean processed() {
    return testState(_PROCESSED);
  }

  private void clearBit(int mask) {
    bitset &= ~mask;
  }

  private void setBit(int mask) {
    bitset |= mask;
  }

  private void setState(int value) {
    int bits;
    bits = bitset & BITS_MASK;

    bitset = bits | value;
  }

  private boolean testBit(int mask) {
    return (bitset & mask) != 0;
  }

  private boolean testState(int value) {
    int result;
    result = bitset & STATE_MASK;

    return result == value;
  }

  private String decode(String raw) {
    return URLDecoder.decode(raw, StandardCharsets.UTF_8);
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  final void parseLine() throws IOException {
    int startIndex;
    startIndex = bufferIndex;

    byte needle;
    needle = Bytes.LF;

    while (true) {
      for (int i = startIndex; i < bufferLimit; i++) {
        byte maybe;
        maybe = buffer[i];

        if (maybe == needle) {
          lineLimit = i;

          return;
        }
      }

      // not inside buffer
      // let's try to read more data
      startIndex = bufferLimit;

      int writableLength;
      writableLength = buffer.length - bufferLimit;

      if (writableLength == 0) {
        // buffer is full, try to increase

        if (buffer.length == maxBufferSize) {
          // cannot increase...
          parseStatus = ParseStatus.OVERFLOW;

          return;
        }

        int newLength;
        newLength = buffer.length << 1;

        buffer = Arrays.copyOf(buffer, newLength);

        writableLength = buffer.length - bufferLimit;
      }

      int bytesRead;
      bytesRead = inputStream.read(buffer, bufferLimit, writableLength);

      if (bytesRead < 0) {
        // EOF
        parseStatus = ParseStatus.UNEXPECTED_EOF;

        return;
      }

      bufferLimit += bytesRead;
    }
  }

  final boolean matches(byte[] bytes) {
    int length;
    length = bytes.length;

    int toIndex;
    toIndex = bufferIndex + length;

    if (toIndex >= lineLimit) {
      // outside of line...
      return false;
    }

    boolean matches;
    matches = Arrays.equals(
        buffer, bufferIndex, toIndex,
        bytes, 0, length
    );

    if (matches) {
      bufferIndex += length;

      return true;
    } else {
      return false;
    }
  }

  final int indexOf(byte needle) {
    for (int i = bufferIndex; i < bufferLimit; i++) {
      byte maybe;
      maybe = buffer[i];

      if (maybe == needle) {
        return i;
      }
    }

    return -1;
  }

  final int indexOf(byte needleA, byte needleB) {
    for (int i = bufferIndex; i < bufferLimit; i++) {
      byte maybe;
      maybe = buffer[i];

      if (maybe == needleA) {
        return i;
      }

      if (maybe == needleB) {
        return i;
      }
    }

    return -1;
  }

  final boolean consumeIfEndOfLine() {
    if (bufferIndex < lineLimit) {
      byte next;
      next = buffer[bufferIndex++];

      if (next != Bytes.CR) {
        return false;
      }
    }

    if (bufferIndex != lineLimit) {
      return false;
    }

    // index immediately after LF
    bufferIndex++;

    return true;
  }

  final String bufferToString(int start, int end) {
    int length;
    length = end - start;

    return new String(buffer, start, length, StandardCharsets.UTF_8);
  }

  final boolean consumeIfEmptyLine() {
    int length;
    length = lineLimit - bufferIndex;

    if (length == 0) {
      bufferIndex++;

      return true;
    }

    if (length == 1) {
      byte cr;
      cr = buffer[bufferIndex];

      if (cr == Bytes.CR) {
        bufferIndex += 2;

        return true;
      }
    }

    return false;
  }

  final byte get(int index) {
    return buffer[index];
  }

  final boolean canBuffer(long contentLength) {
    int maxAvailable;
    maxAvailable = maxBufferSize - bufferIndex;

    return maxAvailable >= contentLength;
  }

  final int read(long contentLength) throws IOException {
    // unread bytes in buffer
    int unread;
    unread = bufferLimit - bufferIndex;

    if (unread >= contentLength) {
      // everything is in the buffer already -> do not read

      return 0;
    }

    // we assume canBuffer was invoked before this method...
    // i.e. max buffer size can hold everything
    int length;
    length = (int) contentLength;

    int requiredBufferLength;
    requiredBufferLength = bufferIndex + length;

    // must we increase our buffer?

    if (requiredBufferLength > buffer.length) {
      int newLength;
      newLength = powerOfTwo(requiredBufferLength);

      buffer = Arrays.copyOf(buffer, newLength);
    }

    // how many bytes must we read
    int mustReadCount;
    mustReadCount = length - unread;

    while (mustReadCount > 0) {
      int read;
      read = inputStream.read(buffer, bufferLimit, mustReadCount);

      if (read < 0) {
        return -1;
      }

      bufferLimit += read;

      mustReadCount -= read;
    }

    return length;
  }

  final long read(Path file, long contentLength) throws IOException {
    // max out buffer if necessary
    if (buffer.length < maxBufferSize) {
      buffer = Arrays.copyOf(buffer, maxBufferSize);
    }

    // unread bytes in buffer
    int unread;
    unread = bufferLimit - bufferIndex;

    // how many bytes must we read
    long mustReadCount;
    mustReadCount = contentLength - unread;

    try (OutputStream out = Files.newOutputStream(file)) {
      while (mustReadCount > 0) {
        // this is guaranteed to be an int value
        long available;
        available = buffer.length - bufferLimit;

        // this is guaranteed to be an int value
        long iteration;
        iteration = Math.min(available, mustReadCount);

        int read;
        read = inputStream.read(buffer, bufferLimit, (int) iteration);

        if (read < 0) {
          return -1;
        }

        bufferLimit += read;

        out.write(buffer, bufferIndex, bufferLimit - bufferIndex);

        bufferLimit = bufferIndex;

        mustReadCount -= read;
      }
    }

    return contentLength;
  }

}