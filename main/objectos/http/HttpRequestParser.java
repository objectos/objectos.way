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
    final HttpRequestParser2Method methodParser;
    methodParser = new HttpRequestParser2Method(input);

    final HttpMethod method;
    method = methodParser.parse();

    final String path;

    try {
      path = parsePath();
    } catch (DecodePercException e) {
      throw HttpClientException.of(InvalidRequestLine.PATH_PERCENT, e);
    } catch (HttpSocketEof e) {
      throw HttpClientException.of(InvalidRequestLine.PATH_NEXT_CHAR, e);
    } catch (HttpSocketOverflow e) {
      throw HttpClientException.of(InvalidRequestLine.URI_TOO_LONG, e);
    }

    final Map<String, Object> queryParams;

    try {
      queryParams = parseQuery();
    } catch (DecodePercException e) {
      throw HttpClientException.of(InvalidRequestLine.QUERY_PERCENT);
    } catch (HttpSocketEof e) {
      throw HttpClientException.of(InvalidRequestLine.QUERY_CHAR, e);
    } catch (HttpSocketOverflow e) {
      throw HttpClientException.of(InvalidRequestLine.URI_TOO_LONG, e);
    }

    final HttpVersion version;

    try {
      version = parseVersion();
    } catch (HttpSocketEof | HttpSocketOverflow e) {
      throw HttpClientException.of(InvalidRequestLine.VERSION_CHAR, e);
    }

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
  // # BEGIN: Path
  // ##################################################################

  private static final byte[] PATH_TABLE;

  private static final byte SOLIDUS = '/';

  private static final byte PATH_VALID = 1;
  private static final byte PATH_PERCENT = 2;
  private static final byte PATH_SPACE = 3;
  private static final byte PATH_QUESTION = 4;
  private static final byte PATH_CRLF = 5;

  static {
    final byte[] table;
    table = new byte[128];

    // 0 = invalid
    // 1 = valid
    // 2 = %xx
    // 3 = ' ' -> version
    // 4 = '?' -> stop
    // 5 = '\r' -> 0.9
    // 5 = '\n' -> 0.9

    Ascii.fill(table, Http.unreserved(), PATH_VALID);

    Ascii.fill(table, Http.subDelims(), PATH_VALID);

    table[':'] = PATH_VALID;

    table['@'] = PATH_VALID;

    // solidus acts as segment separator
    table[SOLIDUS] = PATH_VALID;

    table['%'] = PATH_PERCENT;

    table[' '] = PATH_SPACE;

    table['?'] = PATH_QUESTION;

    table['\r'] = PATH_CRLF;

    table['\n'] = PATH_CRLF;

    PATH_TABLE = table;
  }

  private String parsePath() throws DecodePercException, IOException {
    // where our path begins
    final int startIndex;
    startIndex = input.bufferIndex();

    // first char must be a '/' (solidus)
    final byte first;
    first = input.readByte();

    final int firstCodePoint;

    final boolean firstPerc;

    if (first < 0) {
      throw HttpClientException.of(InvalidRequestLine.PATH_FIRST_CHAR);
    }

    else if (first != '%') {
      firstCodePoint = first;

      firstPerc = false;
    }

    else {
      firstCodePoint = decodePerc();

      firstPerc = true;
    }

    if (firstCodePoint != '/') {
      throw HttpClientException.of(InvalidRequestLine.PATH_FIRST_CHAR);
    }

    final String result;

    // remaining chars
    if (!firstPerc) {
      result = parsePath0(startIndex);
    } else {
      final StringBuilder path;
      path = new StringBuilder("/");

      result = parsePath1(path);
    }

    final int length;
    length = result.length();

    if (length >= 2) {
      final char second;
      second = result.charAt(1);

      if (second == '/') {
        throw HttpClientException.of(InvalidRequestLine.PATH_SEGMENT_NZ);
      }
    }

    return result;
  }

  private String parsePath0(int startIndex) throws DecodePercException, IOException {
    while (true) {
      final byte code;
      code = readTable(PATH_TABLE, InvalidRequestLine.PATH_NEXT_CHAR);

      switch (code) {
        case PATH_VALID -> {
          // noop
        }

        case PATH_PERCENT -> {
          final StringBuilder path;
          path = makeStrBuilder(startIndex);

          final int decoded;
          decoded = decodePerc();

          path.appendCodePoint(decoded);

          return parsePath1(path);
        }

        case PATH_SPACE, PATH_QUESTION -> {
          return makeStr(startIndex);
        }

        case PATH_CRLF -> {
          // assume version 0.9
          throw HttpClientException.of(InvalidRequestLine.HTTP_VERSION_NOT_SUPPORTED);
        }

        default -> throw HttpClientException.of(InvalidRequestLine.PATH_NEXT_CHAR);
      }
    }
  }

  private String parsePath1(StringBuilder path) throws DecodePercException, IOException {
    while (true) {
      final byte b;
      b = input.readByte();

      if (b < 0) {
        throw HttpClientException.of(InvalidRequestLine.PATH_NEXT_CHAR);
      }

      final byte code;
      code = PATH_TABLE[b];

      switch (code) {
        case PATH_VALID -> {
          path.append((char) b);
        }

        case PATH_PERCENT -> {
          final int decoded;
          decoded = decodePerc();

          path.appendCodePoint(decoded);
        }

        case PATH_SPACE, PATH_QUESTION -> {
          return path.toString();
        }

        case PATH_CRLF -> {
          // assume version 0.9
          throw HttpClientException.of(InvalidRequestLine.HTTP_VERSION_NOT_SUPPORTED);
        }

        default -> throw HttpClientException.of(InvalidRequestLine.PATH_NEXT_CHAR);
      }
    }
  }

  // ##################################################################
  // # END: Path
  // ##################################################################

  // ##################################################################
  // # BEGIN: Query
  // ##################################################################

  private static final byte[] QUERY_TABLE;

  private static final byte QUERY_VALID = 1;
  private static final byte QUERY_PERCENT = 2;
  private static final byte QUERY_PLUS = 3;
  private static final byte QUERY_EQUALS = 4;
  private static final byte QUERY_AMPERSAND = 5;
  private static final byte QUERY_SPACE = 6;
  private static final byte QUERY_CRLF = 7;

  static {
    final byte[] table;
    table = new byte[128];

    // 0 = invalid
    // 1 = valid
    // 2 = %xx
    // 3 = '+' -> SPACE
    // 4 = '=' -> key/value separator
    // 5 = '&' -> next key
    // 6 = ' ' -> space
    // 7 = '\r' -> 0.9
    // 7 = '\n' -> 0.9

    Ascii.fill(table, Http.unreserved(), QUERY_VALID);

    Ascii.fill(table, Http.subDelims(), QUERY_VALID);

    table[':'] = QUERY_VALID;

    table['@'] = QUERY_VALID;

    table['/'] = QUERY_VALID;

    table['?'] = QUERY_VALID;

    table['%'] = QUERY_PERCENT;

    table['+'] = QUERY_PLUS;

    table['='] = QUERY_EQUALS;

    table['&'] = QUERY_AMPERSAND;

    table[' '] = QUERY_SPACE;

    table['\r'] = QUERY_CRLF;

    table['\n'] = QUERY_CRLF;

    QUERY_TABLE = table;
  }

  private class Query {

    boolean emptyValue;

    Map<String, Object> params;

    HttpVersion version;

    final void add(String name) {
      if (!name.isEmpty()) {
        add(name, "");
      }
    }

    final void add(String name, String value) {
      if (params == null) {
        params = new HashMap<>();
      }

      Http.queryParamsAdd(params, Function.identity(), name, value);
    }

    final boolean done() {
      return version != null;
    }

  }

  private Map<String, Object> parseQuery() throws DecodePercException, IOException {
    final byte prev;
    prev = input.peekPrev();

    if (prev != '?') {
      return null;
    }

    final Query query;
    query = new Query();

    while (true) {
      final String name;
      name = parseQueryName(query);

      if (query.done()) {
        query.add(name);

        break;
      }

      if (query.emptyValue) {
        query.add(name, "");

        query.emptyValue = false;

        continue;
      }

      final String value;
      value = parseQueryValue(query);

      query.add(name, value);

      if (query.done()) {
        break;
      }
    }

    return query.params;
  }

  private String parseQueryName(Query query) throws DecodePercException, IOException {
    final int startIndex;
    startIndex = input.bufferIndex();

    while (true) {
      final byte code;
      code = readTable(QUERY_TABLE, InvalidRequestLine.QUERY_CHAR);

      switch (code) {
        case QUERY_VALID -> {
          // noop
        }

        case QUERY_PERCENT -> {
          final StringBuilder name;
          name = makeStrBuilder(startIndex);

          final int decoded;
          decoded = decodePerc();

          name.appendCodePoint(decoded);

          return parseQueryName1(query, name);
        }

        case QUERY_PLUS -> {
          final StringBuilder name;
          name = makeStrBuilder(startIndex);

          name.append(' ');

          return parseQueryName1(query, name);
        }

        case QUERY_EQUALS -> {
          return makeStr(startIndex);
        }

        case QUERY_SPACE -> {
          query.version = HttpVersion.HTTP_1_1;

          return makeStr(startIndex);
        }

        case QUERY_AMPERSAND -> {
          query.emptyValue = true;

          return makeStr(startIndex);
        }

        case QUERY_CRLF -> {
          // assume version 0.9
          throw HttpClientException.of(InvalidRequestLine.HTTP_VERSION_NOT_SUPPORTED);
        }

        default -> throw HttpClientException.of(InvalidRequestLine.QUERY_CHAR);
      }
    }
  }

  private String parseQueryName1(Query query, StringBuilder name) throws DecodePercException, IOException {
    while (true) {
      final byte b;
      b = input.readByte();

      if (b < 0) {
        throw HttpClientException.of(InvalidRequestLine.QUERY_CHAR);
      }

      final byte code;
      code = QUERY_TABLE[b];

      switch (code) {
        case QUERY_VALID -> {
          name.append((char) b);
        }

        case QUERY_PERCENT -> {
          final int decoded;
          decoded = decodePerc();

          name.appendCodePoint(decoded);
        }

        case QUERY_PLUS -> {
          name.append(' ');
        }

        case QUERY_EQUALS -> {
          return name.toString();
        }

        case QUERY_SPACE -> {
          query.version = HttpVersion.HTTP_1_1;

          return name.toString();
        }

        case QUERY_AMPERSAND -> {
          query.emptyValue = true;

          return name.toString();
        }

        case QUERY_CRLF -> {
          // assume version 0.9
          throw HttpClientException.of(InvalidRequestLine.HTTP_VERSION_NOT_SUPPORTED);
        }

        default -> throw HttpClientException.of(InvalidRequestLine.QUERY_CHAR);
      }
    }
  }

  private String parseQueryValue(Query query) throws DecodePercException, IOException {
    final int startIndex;
    startIndex = input.bufferIndex();

    while (true) {
      final byte code;
      code = readTable(QUERY_TABLE, InvalidRequestLine.QUERY_CHAR);

      switch (code) {
        case QUERY_VALID -> {
          // noop
        }

        case QUERY_PERCENT -> {
          final StringBuilder value;
          value = makeStrBuilder(startIndex);

          final int decoded;
          decoded = decodePerc();

          value.appendCodePoint(decoded);

          return parseQueryValue1(query, value);
        }

        case QUERY_PLUS -> {
          final StringBuilder value;
          value = makeStrBuilder(startIndex);

          value.append(' ');

          return parseQueryValue1(query, value);
        }

        case QUERY_AMPERSAND -> {
          return makeStr(startIndex);
        }

        case QUERY_SPACE -> {
          query.version = HttpVersion.HTTP_1_1;

          return makeStr(startIndex);
        }

        case QUERY_CRLF -> {
          // assume version 0.9
          throw HttpClientException.of(InvalidRequestLine.HTTP_VERSION_NOT_SUPPORTED);
        }

        default -> throw HttpClientException.of(InvalidRequestLine.QUERY_CHAR);
      }
    }
  }

  private String parseQueryValue1(Query query, StringBuilder value) throws DecodePercException, IOException {
    while (true) {
      final byte b;
      b = input.readByte();

      if (b < 0) {
        throw HttpClientException.of(InvalidRequestLine.QUERY_CHAR);
      }

      final byte code;
      code = QUERY_TABLE[b];

      switch (code) {
        case QUERY_VALID -> {
          value.append((char) b);
        }

        case QUERY_PERCENT -> {
          final int decoded;
          decoded = decodePerc();

          value.appendCodePoint(decoded);
        }

        case QUERY_PLUS -> {
          value.append(' ');
        }

        case QUERY_AMPERSAND -> {
          return value.toString();
        }

        case QUERY_SPACE -> {
          query.version = HttpVersion.HTTP_1_1;

          return value.toString();
        }

        case QUERY_CRLF -> {
          // assume version 0.9
          throw HttpClientException.of(InvalidRequestLine.HTTP_VERSION_NOT_SUPPORTED);
        }

        default -> throw HttpClientException.of(InvalidRequestLine.QUERY_CHAR);
      }
    }
  }

  // ##################################################################
  // # END: Target: Query
  // ##################################################################

  // ##################################################################
  // # END: Target
  // ##################################################################

  // ##################################################################
  // # BEGIN: Version
  // ##################################################################

  private static final byte[] HTTP_1_1_CRLF = "HTTP/1.1\r\n".getBytes(StandardCharsets.US_ASCII);

  private static final byte[] HTTP_1_1_LF = "HTTP/1.1\n".getBytes(StandardCharsets.US_ASCII);

  private static final byte[] HTTP_OTHERS = "HTTP/".getBytes(StandardCharsets.US_ASCII);

  private HttpVersion parseVersion() throws IOException {
    if (input.matches(HTTP_1_1_CRLF, 0)) {
      return HttpVersion.HTTP_1_1;
    }

    if (input.matches(HTTP_1_1_LF, 0)) {
      throw HttpClientException.of(InvalidLineTerminator.INSTANCE);
    }

    if (!input.matches(HTTP_OTHERS, 0)) {
      throw HttpClientException.of(InvalidRequestLine.VERSION_CHAR);
    }

    final boolean minor;
    minor = parseVersionMajor();

    if (minor) {
      parseVersionMinor();
    }

    throw HttpClientException.of(InvalidRequestLine.HTTP_VERSION_NOT_SUPPORTED);
  }

  private boolean parseVersionMajor() throws IOException {
    final byte first;
    first = input.readByte();

    if (!Http.isDigit(first)) {
      throw HttpClientException.of(InvalidRequestLine.VERSION_CHAR);
    }

    while (true) {
      final byte b;
      b = input.readByte();

      if (Http.isDigit(b)) {
        continue;
      }

      if (b == '.') {
        return true;
      }

      if (b == '\r') {
        final byte lf;
        lf = input.readByte();

        if (lf == '\n') {
          return false;
        }
      }

      throw HttpClientException.of(InvalidRequestLine.VERSION_CHAR);
    }
  }

  private void parseVersionMinor() throws IOException {
    final byte first;
    first = input.readByte();

    if (!Http.isDigit(first)) {
      throw HttpClientException.of(InvalidRequestLine.VERSION_CHAR);
    }

    while (true) {
      final byte b;
      b = input.readByte();

      if (Http.isDigit(b)) {
        continue;
      }

      if (b == '\r') {
        final byte lf;
        lf = input.readByte();

        if (lf == '\n') {
          return;
        }
      }

      throw HttpClientException.of(InvalidRequestLine.VERSION_CHAR);
    }
  }

  // ##################################################################
  // # END: Version
  // ##################################################################

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
  // # BEGIN: URL Decode
  // ##################################################################

  // ##################################################################
  // # BEGIN: URL Decode
  // ##################################################################

  @SuppressWarnings("serial")
  private final class DecodePercException extends Exception {}

  private int decodePerc() throws DecodePercException, IOException {
    final byte high1;
    high1 = readPerc();

    return switch (high1) {
      // 0yyyzzzz
      case 0b0000, 0b0001,
           0b0010, 0b0011,
           0b0100, 0b0101, 0b0110, 0b0111 -> decodePerc1(high1);

      // 110xxxyy 10yyzzzz
      case 0b1100, 0b1101 -> decodePerc2(high1);

      // 1110wwww 10xxxxyy 10yyzzzz
      case 0b1110 -> decodePerc3(high1);

      // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
      case 0b1111 -> decodePerc4(high1);

      default -> throw new DecodePercException();
    };
  }

  private int decodePerc1(byte high1) throws DecodePercException, IOException {
    final byte low1;
    low1 = readPerc();

    return decodePerc(high1, low1);
  }

  private int decodePerc2(byte high1) throws DecodePercException, IOException {
    final byte low1;
    low1 = readPerc();

    final int perc1;
    perc1 = decodePerc(high1, low1);

    final int perc2;
    perc2 = decodePercNext();

    final int c;
    c = (perc1 & 0b1_1111) << 6 | (perc2 & 0b11_1111);

    if (c < 0x80 || c > 0x7FF) {
      throw new DecodePercException();
    }

    return c;
  }

  private int decodePerc3(byte high1) throws DecodePercException, IOException {
    final byte low1;
    low1 = readPerc();

    final int perc1;
    perc1 = decodePerc(high1, low1);

    final int perc2;
    perc2 = decodePercNext();

    final int perc3;
    perc3 = decodePercNext();

    final int c;
    c = (perc1 & 0b1111) << 12 | (perc2 & 0b11_1111) << 6 | (perc3 & 0b11_1111);

    if (c < 0x800 || c > 0xFFFF || Character.isSurrogate((char) c)) {
      throw new DecodePercException();
    }

    return c;
  }

  private int decodePerc4(byte high1) throws DecodePercException, IOException {
    final byte low1;
    low1 = readPerc();

    final int perc1;
    perc1 = decodePerc(high1, low1);

    final int perc2;
    perc2 = decodePercNext();

    final int perc3;
    perc3 = decodePercNext();

    final int perc4;
    perc4 = decodePercNext();

    final int c;
    c = (perc1 & 0b111) << 18 | (perc2 & 0b11_1111) << 12 | (perc3 & 0b11_1111) << 6 | (perc4 & 0b11_1111);

    if (c < 0x1_0000 || !Character.isValidCodePoint(c)) {
      throw new DecodePercException();
    }

    return c;
  }

  private int decodePerc(byte high, byte low) {
    return (high << 4) | low;
  }

  private int decodePercNext() throws DecodePercException, IOException {
    readPercSep();

    final byte high;
    high = readPerc();

    final byte low;
    low = readPerc();

    final int perc;
    perc = decodePerc(high, low);

    if (!utf8Byte(perc)) {
      throw new DecodePercException();
    }

    return perc;
  }

  private byte readPerc() throws DecodePercException, IOException {
    final byte b;
    b = input.readByte();

    final byte perc;
    perc = Bytes.fromHexDigit(b);

    if (perc < 0) {
      throw new DecodePercException();
    }

    return perc;
  }

  private void readPercSep() throws DecodePercException, IOException {
    final byte b;
    b = input.readByte();

    if (b != '%') {
      throw new DecodePercException();
    }
  }

  private boolean utf8Byte(int utf8) {
    final int topTwoBits;
    topTwoBits = utf8 & 0b1100_0000;

    return topTwoBits == 0b1000_0000;
  }

  // ##################################################################
  // # END: URL Decode
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

  private String makeStr(int startIndex) {
    final int bufferIndex;
    bufferIndex = input.bufferIndex();

    return makeStr(startIndex, bufferIndex);
  }

  private String makeStr(int startIndex, int endIndex) {
    endIndex = endIndex - 1;

    return input.bufferToAscii(startIndex, endIndex);
  }

  private StringBuilder makeStrBuilder(int startIndex) {
    final String prefix;
    prefix = makeStr(startIndex);

    return new StringBuilder(prefix);
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
