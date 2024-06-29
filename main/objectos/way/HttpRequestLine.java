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
import java.nio.charset.StandardCharsets;
import java.util.Map;
import objectos.lang.object.Check;
import objectos.util.map.GrowableMap;
import objectos.way.Http.Request;
import objectos.way.HttpExchangeLoop.ParseStatus;

non-sealed class HttpRequestLine extends HttpSocketInput implements Http.Request.Target {

  Http.Request.Method method;

  private String path;

  private int pathLimit;

  Map<String, String> pathParams;

  private int queryStart;

  private String rawPath;

  private String rawTarget;

  Http.Request.Target.Query query;

  byte versionMajor;

  byte versionMinor;

  HttpRequestLine() {}

  @Override
  public final String path() {
    if (path == null) {
      String raw;
      raw = rawPath();

      path = URLDecoder.decode(raw, StandardCharsets.UTF_8);
    }

    return path;
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
  public Request.Target.Query query() {
    if (query == null) {
      if (queryStart == pathLimit) {
        query = HttpRequestTargetQueryEmpty.INSTANCE;
      } else {
        HttpRequestTargetQuery impl;
        impl = new HttpRequestTargetQuery();

        String raw;
        raw = rawTarget.substring(queryStart);

        impl.set(raw);

        query = impl;
      }
    }

    return query;
  }

  @Override
  public final String rawPath() {
    if (rawPath == null) {
      rawPath = rawTarget.substring(0, pathLimit);
    }

    return rawPath;
  }

  final void resetRequestLine() {
    method = null;

    pathLimit = 0;

    if (pathParams != null) {
      pathParams.clear();
    }

    path = rawPath = rawTarget = null;

    query = null;

    queryStart = 0;

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

  private void parseMethod() throws IOException {
    if (bufferIndex >= lineLimit) {
      // empty line... nothing to do
      return;
    }

    byte first;
    first = buffer[bufferIndex];

    // based on the first char, we select out method candidate

    switch (first) {
      case 'C' -> parseMethod0(Http.CONNECT);

      case 'D' -> parseMethod0(Http.DELETE);

      case 'G' -> parseMethod0(GET);

      case 'H' -> parseMethod0(Http.HEAD);

      case 'O' -> parseMethod0(Http.OPTIONS);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod0(Http.TRACE);
    }
  }

  // force Http init
  static final Http.Request.Method GET = Http.GET;

  static final byte[][] STD_METHOD_BYTES;

  static {
    int size;
    size = HttpRequestMethod.size();

    byte[][] map;
    map = new byte[size][];

    for (int index = 0; index < size; index++) {
      HttpRequestMethod method;
      method = HttpRequestMethod.get(index);

      String nameAndSpace;
      nameAndSpace = method.text() + " ";

      map[index] = nameAndSpace.getBytes(StandardCharsets.UTF_8);
    }

    STD_METHOD_BYTES = map;
  }

  private void parseMethod0(Http.Request.Method candidate) throws IOException {
    int index;
    index = candidate.index();

    byte[] candidateBytes;
    candidateBytes = STD_METHOD_BYTES[index];

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

    parseMethod0(Http.POST);

    if (method != null) {
      return;
    }

    parseMethod0(Http.PUT);

    if (method != null) {
      return;
    }

    parseMethod0(Http.PATCH);

    if (method != null) {
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

    rawTarget = bufferToString(startIndex, targetEndIndex);

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
      pathParams = new GrowableMap<>();
    }

    pathParams.put(name, value);
  }

}