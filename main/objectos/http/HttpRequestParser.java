/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import module java.base;
import objectos.internal.Ascii;
import objectos.internal.Bytes;

final class HttpRequestParser {

  private final HttpExchangeBodyFiles bodyFiles;

  private final int bodyMemoryMax;

  private final long bodySizeMax;

  private final long id;

  private final HttpRequestParser0Input input;

  HttpRequestParser(HttpExchangeBodyFiles bodyFiles, int bodyMemoryMax, long bodySizeMax, long id, HttpRequestParser0Input input) {
    this.bodyFiles = bodyFiles;

    this.bodyMemoryMax = bodyMemoryMax;

    this.bodySizeMax = bodySizeMax;

    this.id = id;

    this.input = input;
  }

  @SuppressWarnings("unused")
  public final HttpRequest parse() throws IOException {
    // method
    final HttpRequestParser2Method methodParser;
    methodParser = new HttpRequestParser2Method(input);

    final HttpMethod method;
    method = methodParser.parse();

    // path
    final HttpRequestParser3Path pathParser;
    pathParser = new HttpRequestParser3Path(input);

    final String path;
    path = pathParser.parse();

    // query
    final HttpRequestParser4Query queryParser;
    queryParser = new HttpRequestParser4Query(input);

    final Map<String, Object> queryParams;
    queryParams = queryParser.parse();

    // version
    final HttpRequestParser5Version versionParser;
    versionParser = new HttpRequestParser5Version(input);

    final HttpVersion version;
    version = versionParser.parse();

    // headers
    final Map<HttpHeaderName, Object> headers;

    try {
      headers = parseHeaders();
    } catch (HttpSocketEof e) {
      throw HttpClientException.of(InvalidRequestHeaders.EOF, e);
    } catch (HttpSocketOverflow e) {
      throw HttpClientException.of(InvalidRequestHeaders.REQUEST_HEADER_FIELDS_TOO_LARGE, e);
    }

    final HttpRequestBody body;

    try {
      body = parseBody(headers);
    } catch (HttpSocketEof e) {
      throw HttpClientException.of(InvalidRequestBody.EOF, e);
    }

    return new HttpRequestImpl(
        method,

        path,

        queryParams,

        version,

        headers,

        body
    );
  }

  // ##################################################################
  // # BEGIN: Headers
  // ##################################################################

  private static final class Headers {

    Map<HttpHeaderName, Object> map;

    final StringBuilder sb = new StringBuilder();

    final void appendChar(byte b) {
      sb.append((char) b);
    }

    final String makeString() {
      final String res;
      res = sb.toString();

      sb.setLength(0);

      return res;
    }

    final void put(HttpHeaderName name, String value) {
      if (map == null) {
        map = new HashMap<>();
      }

      Http.mapAdd(map, name, value);
    }

  }

  private Map<HttpHeaderName, Object> parseHeaders() throws IOException {
    final Headers headers;
    headers = new Headers();

    while (true) {
      if (parseHeadersTerminator()) {
        break;
      }

      final HttpHeaderName name;
      name = parseHeaderName(headers);

      final String value;
      value = parseHeaderValue(headers);

      headers.put(name, value);
    }

    return headers.map;
  }

  private boolean parseHeadersTerminator() throws IOException {
    final byte first;
    first = input.peekByte();

    return switch (first) {
      case Bytes.CR -> {
        input.skipByte();

        final byte second;
        second = input.readByte();

        if (second == Bytes.LF) {
          yield true;
        }

        throw HttpClientException.of(InvalidLineTerminator.INSTANCE);
      }

      case Bytes.LF -> {
        throw HttpClientException.of(InvalidLineTerminator.INSTANCE);
      }

      default -> false;
    };
  }

  private HttpHeaderName parseHeaderName(Headers headers) throws IOException {
    while (true) {
      final byte b;
      b = input.readByte();

      final byte mapped;
      mapped = HttpHeaderNameImpl.map(b);

      switch (mapped) {
        case HttpHeaderNameImpl.INVALID -> {
          throw HttpClientException.of(InvalidRequestHeaders.NAME_CHAR);
        }

        case HttpHeaderNameImpl.COLON -> {
          final String lowerCase;
          lowerCase = headers.makeString();

          final HttpHeaderNameImpl standard;
          standard = HttpHeaderNameImpl.byLowerCase(lowerCase);

          if (standard != null) {
            return standard;
          } else {
            return HttpHeaderNameImpl.ofLowerCase(lowerCase);
          }
        }

        default -> {
          headers.appendChar(mapped);
        }
      }
    }
  }

  private static final byte[] HEADER_VALUE_TABLE;

  private static final byte HEADER_VALUE_VALID = 1;

  private static final byte HEADER_VALUE_WS = 2;

  private static final byte HEADER_VALUE_CR = 3;

  private static final byte HEADER_VALUE_LF = 4;

  static {
    final byte[] table;
    table = new byte[128];

    for (int b = 0x21; b < 0x7F; b++) {
      // VCHAR are valid
      table[b] = HEADER_VALUE_VALID;
    }

    // valid under certain circustances
    table[' '] = HEADER_VALUE_WS;

    table['\t'] = HEADER_VALUE_WS;

    table['\r'] = HEADER_VALUE_CR;

    table['\n'] = HEADER_VALUE_LF;

    HEADER_VALUE_TABLE = table;
  }

  private String parseHeaderValue(Headers headers) throws IOException {
    int startIndex;

    // skip OWS
    loop: while (true) {
      startIndex = input.bufferIndex();

      final byte code;
      code = readTable(HEADER_VALUE_TABLE, InvalidRequestHeaders.VALUE_CHAR);

      switch (code) {
        case HEADER_VALUE_WS -> {
          // noop
        }

        case HEADER_VALUE_VALID -> {
          break loop;
        }

        case HEADER_VALUE_CR -> {
          final int endIndex;
          endIndex = startIndex + 1;

          return parseHeaderValueCR(startIndex, endIndex);
        }

        case HEADER_VALUE_LF -> {
          throw HttpClientException.of(InvalidLineTerminator.INSTANCE);
        }

        default -> {
          throw HttpClientException.of(InvalidRequestHeaders.VALUE_CHAR);
        }
      }
    }

    int validIndex;
    validIndex = input.bufferIndex();

    // value contents
    while (true) {
      final byte code;
      code = readTable(HEADER_VALUE_TABLE, InvalidRequestHeaders.VALUE_CHAR);

      switch (code) {
        case HEADER_VALUE_WS -> {
          // noop
        }

        case HEADER_VALUE_VALID -> {
          validIndex = input.bufferIndex();
        }

        case HEADER_VALUE_CR -> {
          final int endIndex;
          endIndex = validIndex + 1;

          return parseHeaderValueCR(startIndex, endIndex);
        }

        case HEADER_VALUE_LF -> {
          throw HttpClientException.of(InvalidLineTerminator.INSTANCE);
        }

        default -> {
          throw HttpClientException.of(InvalidRequestHeaders.VALUE_CHAR);
        }
      }
    }
  }

  private String parseHeaderValueCR(int startIndex, int endIndex) throws IOException {
    final byte lf;
    lf = input.readByte();

    if (lf != Bytes.LF) {
      throw HttpClientException.of(InvalidRequestHeaders.VALUE_CHAR);
    }

    return makeStr(startIndex, endIndex);
  }

  // ##################################################################
  // # END: Headers
  // ##################################################################

  // ##################################################################
  // # BEGIN: Body
  // ##################################################################

  private HttpRequestBody parseBody(Map<HttpHeaderName, Object> headers) throws IOException {
    final String contentLength;
    contentLength = Http.queryParamsGet(headers, HttpHeaderName.CONTENT_LENGTH);

    if (contentLength != null) {
      return parseBodyFixed(headers, contentLength);
    }

    final String transferEncoding;
    transferEncoding = Http.queryParamsGet(headers, HttpHeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      // TODO 501 Not Implemented
      throw new UnsupportedOperationException("Implement me");
    }

    final String contentType;
    contentType = Http.queryParamsGet(headers, HttpHeaderNameImpl.CONTENT_TYPE);

    if (contentType != null) {
      throw HttpClientException.of(InvalidRequestHeaders.LENGTH_REQUIRED);
    }

    return HttpRequestBodyImpl.ofNull();
  }

  private HttpRequestBody parseBodyFixed(Map<HttpHeaderName, Object> headers, String contentLength) throws IOException {
    final String transferEncoding;
    transferEncoding = Http.queryParamsGet(headers, HttpHeaderNameImpl.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      throw HttpClientException.of(InvalidRequestHeaders.BOTH_CL_TE);
    }

    final long length;
    length = parseBodyFixedLength(contentLength);

    if (length == 0) {
      return HttpRequestBodyImpl.ofNull();
    }

    if (length > bodySizeMax) {
      throw HttpClientException.of(InvalidRequestHeaders.CONTENT_TOO_LARGE);
    }

    final String contentType;
    contentType = Http.queryParamsGet(headers, HttpHeaderNameImpl.CONTENT_TYPE);

    final boolean parseForm;
    parseForm = contentType != null && contentType.equalsIgnoreCase("application/x-www-form-urlencoded");

    if (length <= bodyMemoryMax) {
      return parseBodyFixedMemory(length, parseForm);
    }

    else {
      return parseBodyFixedFile(length, parseForm);
    }
  }

  private long parseBodyFixedLength(String contentLength) throws HttpClientException {
    long length;
    length = 0;

    final long hardLimit;
    hardLimit = Long.MAX_VALUE;

    final long multLimit;
    multLimit = hardLimit / 10;

    boolean overflow;
    overflow = false;

    for (int i = 0, len = contentLength.length(); i < len; i++) {
      final char d;
      d = contentLength.charAt(i);

      if (!Ascii.isDigit(d)) {
        throw HttpClientException.of(InvalidRequestHeaders.INVALID_CONTENT_LENGTH);
      }

      if (overflow) {
        // already invalid...
        // just check if content-length is numeric
        continue;
      }

      if (length > multLimit) {
        overflow = true;

        continue;
      }

      length *= 10;

      final long digit;
      digit = (long) d & 0xF;

      if (length > hardLimit - digit) {
        overflow = true;

        continue;
      }

      length += digit;
    }

    if (overflow) {
      throw HttpClientException.of(InvalidRequestHeaders.CONTENT_TOO_LARGE);
    }

    return length;
  }

  private HttpRequestBody parseBodyFixedMemory(long length, boolean parseForm) throws IOException {
    // length is guaranteed to fit in an int
    // in any case we throw if length overflows...

    final int len;
    len = Math.toIntExact(length);

    final ByteArrayOutputStream outputStream;
    outputStream = new ByteArrayOutputStream(len);

    input.transferTo(outputStream, len);

    final byte[] bytes;
    bytes = outputStream.toByteArray();

    final Map<String, Object> formParams;
    formParams = parseForm ? parseForm(bytes) : null;

    return HttpRequestBodyImpl.of(bytes, formParams);
  }

  private HttpRequestBody parseBodyFixedFile(long length, boolean parseForm) throws IOException {
    final Path file;
    file = bodyFiles.file(id);

    try (OutputStream outputStream = bodyFiles.newOutputStream(file)) {
      input.transferTo(outputStream, length);
    }

    final Map<String, Object> formParams;
    formParams = parseForm ? parseForm(file) : null;

    return HttpRequestBodyImpl.of(file, formParams);
  }

  // ##################################################################
  // # END: Body
  // ##################################################################

  // ##################################################################
  // # BEGIN: Form
  // ##################################################################

  private Map<String, Object> parseForm(byte[] bytes) {
    throw new UnsupportedOperationException("Implement me");
  }

  private Map<String, Object> parseForm(Path file) {
    throw new UnsupportedOperationException("Implement me");
  }

  // ##################################################################
  // # END: Form
  // ##################################################################

  // ##################################################################
  // # BEGIN: Errors
  // ##################################################################

  enum InvalidLineTerminator implements HttpClientException.Kind {
    INSTANCE;

    private static final byte[] MESSAGE = "Invalid line terminator.\n".getBytes(StandardCharsets.US_ASCII);

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final HttpStatus status() {
      return HttpStatus.BAD_REQUEST;
    }
  }

  enum InvalidRequestLine implements HttpClientException.Kind {
    // do not reorder, do not rename

    // 414 URI Too Long
    URI_TOO_LONG(HttpStatus.URI_TOO_LONG),

    // path does not start with solidus
    PATH_FIRST_CHAR,

    // path starts with two consecutive '/'
    PATH_SEGMENT_NZ,

    // path has an invalid character
    PATH_NEXT_CHAR,

    // path has an invalid percent encoded sequence
    PATH_PERCENT,

    // query has an invalid character
    QUERY_CHAR,

    // query has an invalid percent encoded sequence
    QUERY_PERCENT,

    // invalid version
    VERSION_CHAR,

    // 505 HTTP Version Not Supported
    HTTP_VERSION_NOT_SUPPORTED(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);

    private static final byte[] MESSAGE = "Invalid request line.\n".getBytes(StandardCharsets.US_ASCII);

    private final HttpStatus status;

    private InvalidRequestLine() {
      this(HttpStatus.BAD_REQUEST);
    }

    private InvalidRequestLine(HttpStatus status) {
      this.status = status;
    }

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final HttpStatus status() {
      return status;
    }
  }

  enum InvalidRequestHeaders implements HttpClientException.Kind {
    // do not reorder, do not rename

    // header name has an invalid character
    NAME_CHAR,

    // header value has an invalid character
    VALUE_CHAR,

    // invalid header terminator, i.e., the last '\r\n'
    TERMINATOR,

    // invalid value, e.g., 'Content-Length: two hundred bytes'
    INVALID_CONTENT_LENGTH,

    // request include both Content-Length and Transfer-Enconding.
    BOTH_CL_TE,

    // Unexpected end of stream
    EOF,

    // 411 Length Required
    LENGTH_REQUIRED(HttpStatus.LENGTH_REQUIRED),

    // 413 Content Too Large
    CONTENT_TOO_LARGE(HttpStatus.CONTENT_TOO_LARGE),

    // 431 Request Header Fields Too Large
    REQUEST_HEADER_FIELDS_TOO_LARGE(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);

    private static final byte[] MESSAGE = "Invalid request headers.\n".getBytes(StandardCharsets.US_ASCII);

    private final HttpStatus status;

    private InvalidRequestHeaders() {
      this(HttpStatus.BAD_REQUEST);
    }

    private InvalidRequestHeaders(HttpStatus status) {
      this.status = status;
    }

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final HttpStatus status() {
      return status;
    }
  }

  enum InvalidRequestBody implements HttpClientException.Kind {
    // do not reorder, do not rename

    // Unexpected end of stream
    EOF;

    private static final byte[] MESSAGE = "Invalid request body.\n".getBytes(StandardCharsets.US_ASCII);

    private final HttpStatus status;

    private InvalidRequestBody() {
      this(HttpStatus.BAD_REQUEST);
    }

    private InvalidRequestBody(HttpStatus status) {
      this.status = status;
    }

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final HttpStatus status() {
      return status;
    }
  }

  // ##################################################################
  // # END: Errors
  // ##################################################################

  // ##################################################################
  // # BEGIN: Util
  // ##################################################################

  private String makeStr(int startIndex, int endIndex) {
    endIndex = endIndex - 1;

    return input.bufferToAscii(startIndex, endIndex);
  }

  private byte readTable(byte[] table, HttpClientException.Kind kind) throws IOException {
    final byte next;
    next = input.readByte();

    if (next < 0) {
      throw HttpClientException.of(kind);
    }

    return table[next];
  }

  // ##################################################################
  // # END: Util
  // ##################################################################

}
