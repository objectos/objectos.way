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
package br.com.objectos.http;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;
import objectos.lang.Check;

public final class RequestParser extends AbstractHttpParser<RequestHeader> {

  private Request result;

  private Method resultMethod;

  private final StringBuilder resultRequestTarget = new StringBuilder();

  private RequestParser(ByteBuffer byteBuffer,
                        CharBuffer charBuffer) {
    super(byteBuffer, charBuffer);
  }

  public static RequestParser create(ByteBuffer byteBuffer, CharBuffer charBuffer) {
    Check.notNull(byteBuffer, "byteBuffer == null");
    Check.notNull(charBuffer, "charBuffer == null");

    return new RequestParser(byteBuffer, charBuffer);
  }

  @Override
  public final Request getResult() throws ProtocolException {
    return getResultImpl(result);
  }

  @Override
  final HeaderParser<? extends RequestHeader>
      createHeaderParser(String lowerCaseName, String originalName) {
    switch (lowerCaseName) {
      case "accept":
        return new HeaderAcceptImpl();
      case "content-length":
        return resultContentLength = new HeaderContentLengthImpl();
      case "content-type":
        return resultContentType = new HeaderContentTypeImpl();
      case "cookie":
        return new HeaderCookieImpl();
      case "host":
        return new HeaderHostImpl();
      default:
        return new HeaderUnknownImpl(originalName);
    }
  }

  @Override
  final Action executeParse() {
    assert ioReadCount > 0;

    char c;
    c = nextChar();

    switch (c) {
      case 'G':
        return executeParseMethod(Method.GET);
      case 'P':
        return executeParseMethod(Method.POST);
      default:
        return toError400("No method starting with " + c);
    }
  }

  @Override
  final Action executeParseHttpVersionImpl() {
    if (nextCharSize() < 2) {
      return executeIoAndDecodeAgain();
    }

    char[] source;
    source = nextCharArray(2);

    boolean matches;
    matches = source[0] == '\r' && source[1] == '\n';

    if (!matches) {
      return toError400("Expected CRLF but found " + Arrays.toString(source));
    }

    return Action.PARSE_HEADER;
  }

  @Override
  final Action executeParseReasonPhrase() {
    throw new UnsupportedOperationException("Not a HttpResponseParser");
  }

  @Override
  final Action executeParseRequestTarget() {
    while (hasNextChar()) {
      char c;
      c = nextChar();

      if (c == HttpParser.SP) {
        return Action.PARSE_HTTP_VERSION;
      }

      resultRequestTarget.append(c);
    }

    return toError400("Invalid request target: " + resultRequestTarget.toString());
  }

  @Override
  final Action executeParseStatusCode() {
    throw new UnsupportedOperationException("Not a HttpResponseParser");
  }

  @Override
  final void executeResetImpl() {
    result = null;

    resultMethod = null;

    resultRequestTarget.setLength(0);
  }

  @Override
  final Action executeResult() {
    result = new Request(
      buildBody(),
      buildHeaders(),
      resultMethod,
      resultRequestTarget.toString(),
      resultVersion
    );

    return Action.END;
  }

  private Action executeParseMethod(Method candidate) {
    char[] suffix;
    suffix = candidate.parseSuffix;

    int suffixLength;
    suffixLength = suffix.length;

    // suffix + SP
    int minRequiredChars;
    minRequiredChars = suffixLength + 1;

    if (nextCharSize() < minRequiredChars) {
      return executeIoAndDecodeAgain();
    }

    for (int i = 0; i < suffix.length; i++) {
      char nextChar;
      nextChar = nextChar();

      char expected;
      expected = suffix[i];

      if (nextChar != expected) {
        return toError400("Bad request line");
      }
    }

    char nextChar;
    nextChar = nextChar();

    if (nextChar != HttpParser.SP) {
      return toError400("Bad request line");
    }

    resultMethod = candidate;

    return Action.PARSE_REQUEST_TARGET;
  }

}