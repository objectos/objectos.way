/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import objectos.lang.object.Check;
import objectos.way.HttpExchangeLoop.ParseStatus;

non-sealed class HttpRequestLine extends HttpSocketInput implements Http.Request.Target {

  // force Http class init
  static final byte HTTP_REQUEST_LINE = Http.GET;

  byte method;

  private String path;

  private int pathLimit;

  Map<String, String> pathParams;

  private Map<String, Object> queryParams;

  private boolean queryParamsReady;

  private int queryStart;

  private String rawValue;

  byte versionMajor;

  byte versionMinor;

  HttpRequestLine() {}

  @Override
  public final String path() {
    if (path == null) {
      String raw;
      raw = rawPath();

      path = decode(raw);
    }

    return path;
  }

  private String decode(String raw) {
    return URLDecoder.decode(raw, StandardCharsets.UTF_8);
  }

  @Override
  public final String pathParam(String name) {
    Check.notNull(name, "name == null");

    String result;
    result = null;

    if (pathParams != null) {
      result = pathParams.get(name);
    }

    return result;
  }

  @Override
  public final String queryParam(String name) {
    Check.notNull(name, "name == null");

    Map<String, Object> params;
    params = $queryParams();

    Object maybe;
    maybe = params.get(name);

    return switch (maybe) {
      case null -> null;

      case String s -> s;

      case List<?> l -> (String) l.get(0);

      default -> throw new AssertionError(
          "Type should not have been put into the map: " + maybe.getClass()
      );
    };
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
        queryParams = Util.createGrowableMap();
      }

      makeQueryParams(queryParams, this::decode);

      queryParamsReady = true;
    }

    return queryParams;
  }

  @Override
  public final int queryParamAsInt(String name, int defaultValue) {
    String maybe;
    maybe = queryParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  public final String rawPath() {
    return rawValue.substring(0, pathLimit);
  }

  @Override
  public final String rawQuery() {
    return queryStart == pathLimit ? null : rawValue.substring(queryStart);
  }

  private Map<String, Object> $rawQueryParams() {
    Map<String, Object> map;
    map = Util.createGrowableMap();

    makeQueryParams(map, Function.identity());

    return map;
  }

  public final String rawValue() {
    return rawValue;
  }

  public final String rawValue(String queryParamName, String queryParamValue) {
    Check.notNull(queryParamName, "queryParamName == null");
    Check.notNull(queryParamValue, "queryParamValue == null");

    StringBuilder builder;
    builder = new StringBuilder(rawPath());

    builder.append('?');

    Map<String, Object> params;
    params = $rawQueryParams();

    String rawName;
    rawName = encode(queryParamName);

    String rawValue;
    rawValue = encode(queryParamValue);

    params.put(rawName, rawValue);

    int count;
    count = 0;

    for (String key : params.keySet()) {
      if (count++ > 0) {
        builder.append('&');
      }

      builder.append(key);

      builder.append('=');

      Object value;
      value = params.get(key);

      if (value instanceof String s) {
        builder.append(s);
      }

      else {
        throw new UnsupportedOperationException("Implement me");
      }
    }

    return builder.toString();
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  final void resetRequestLine() {
    method = 0;

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

    versionMajor = versionMinor = 0;
  }

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

    if (method == 0) {
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
      case 'C' -> parseMethod0(Http.CONNECT, _CONNECT);

      case 'D' -> parseMethod0(Http.DELETE, _DELETE);

      case 'G' -> parseMethod0(Http.GET, _GET);

      case 'H' -> parseMethod0(Http.HEAD, _HEAD);

      case 'O' -> parseMethod0(Http.OPTIONS, _OPTIONS);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod0(Http.TRACE, _TRACE);
    }
  }

  private void parseMethod0(byte candidate, byte[] candidateBytes) throws IOException {
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

    parseMethod0(Http.POST, _POST);

    if (method != 0) {
      return;
    }

    parseMethod0(Http.PUT, _PUT);

    if (method != 0) {
      return;
    }

    parseMethod0(Http.PATCH, _PATCH);

    if (method != 0) {
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

    versionMajor = (byte) (maybeMajor - 0x30);

    versionMinor = (byte) (maybeMinor - 0x30);
  }

  // matcher methods

  private int matcherIndex;

  final void matcherReset() {
    matcherIndex = 0;

    if (pathParams != null) {
      pathParams.clear();
    }
  }

  final boolean atEnd() {
    return matcherIndex == pathLimit;
  }

  final boolean exact(String other) {
    String value;
    value = path();

    boolean result;
    result = value.equals(other);

    matcherIndex += value.length();

    return result;
  }

  final boolean namedVariable(String name) {
    String value;
    value = path();

    int solidus;
    solidus = value.indexOf('/', matcherIndex);

    String varValue;

    if (solidus < 0) {
      varValue = value.substring(matcherIndex);
    } else {
      varValue = value.substring(matcherIndex, solidus);
    }

    matcherIndex += varValue.length();

    variable(name, varValue);

    return true;
  }

  final boolean region(String region) {
    String value;
    value = path();

    boolean result;
    result = value.regionMatches(matcherIndex, region, 0, region.length());

    matcherIndex += region.length();

    return result;
  }

  final boolean startsWithMatcher(String prefix) {
    String value;
    value = path();

    boolean result;
    result = value.startsWith(prefix);

    matcherIndex += prefix.length();

    return result;
  }

  private void variable(String name, String value) {
    if (pathParams == null) {
      pathParams = Util.createGrowableMap();
    }

    pathParams.put(name, value);
  }

  // query param stuff

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
          key = sb.toString();

          sb.setLength(0);

          putQueryParams(map, decoder, key, "");
        }

        case '&' -> {
          String value;
          value = sb.toString();

          sb.setLength(0);

          if (key == null) {
            putQueryParams(map, decoder, value, "");

            continue;
          }

          putQueryParams(map, decoder, key, value);

          key = null;
        }

        default -> sb.append(c);
      }
    }

    String value;
    value = sb.toString();

    if (key != null) {
      putQueryParams(map, decoder, key, value);
    } else {
      putQueryParams(map, decoder, value, "");
    }
  }

  @SuppressWarnings("unchecked")
  private void putQueryParams(Map<String, Object> map, Function<String, String> decoder, String rawKey, String rawValue) {
    String key;
    key = decoder.apply(rawKey);

    String value;
    value = decoder.apply(rawValue);

    Object oldValue;
    oldValue = map.put(key, value);

    if (oldValue == null) {
      return;
    }

    if (oldValue.equals("")) {
      return;
    }

    if (oldValue instanceof String s) {

      if (value.equals("")) {
        map.put(key, s);
      } else {
        List<String> list;
        list = Util.createList();

        list.add(s);

        list.add(value);

        map.put(key, list);
      }

    }

    else {
      List<String> list;
      list = (List<String>) oldValue;

      list.add(value);

      map.put(key, list);
    }
  }

}