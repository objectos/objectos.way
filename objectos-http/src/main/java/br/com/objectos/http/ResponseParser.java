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

public final class ResponseParser extends AbstractHttpParser<ResponseHeader> {

  private Response result;

  private final StringBuilder resultReasonPhrase = new StringBuilder();

  private Status resultStatus;

  public ResponseParser(ByteBuffer byteBuffer, CharBuffer charBuffer) {
    super(byteBuffer, charBuffer);
  }

  @Override
  public final Response getResult() throws ProtocolException {
    return getResultImpl(result);
  }

  @Override
  final HeaderParser<? extends ResponseHeader>
      createHeaderParser(String lowerCaseName, String originalName) {
    switch (lowerCaseName) {
      case "content-length":
        return resultContentLength = new HeaderContentLengthImpl();
      case "content-type":
        return resultContentType = new HeaderContentTypeImpl();
      case "location":
        return new HeaderLocationImpl();
      case "server":
        return new HeaderServerImpl();
      case "set-cookie":
        return new HeaderSetCookieImpl();
      default:
        return new HeaderUnknownImpl(originalName);
    }
  }

  @Override
  final Action executeParse() {
    return Action.PARSE_HTTP_VERSION;
  }

  @Override
  final Action executeParseHttpVersionImpl() {
    return toParseChar(HttpParser.SP, Action.PARSE_STATUS_CODE);
  }

  @Override
  final Action executeParseReasonPhrase() {
    return toParseToEol(resultReasonPhrase, Action.PARSE_HEADER);
  }

  @Override
  final Action executeParseRequestTarget() {
    throw new UnsupportedOperationException("Not a HttpRequestParser");
  }

  @Override
  final Action executeParseStatusCode() {
    int size;
    size = 3;

    if (nextCharSize() < size) {
      return executeIoAndDecodeAgain();
    }

    char[] source;
    source = nextCharArray(size);

    int digit;
    digit = Http.toUsAsciiDigit(source[0]);

    if (digit > 5) {
      throw new UnsupportedOperationException("Implement me");
    }

    if (digit < 1) {
      throw new UnsupportedOperationException("Implement me");
    }

    int code;
    code = digit * 100;

    digit = Http.toUsAsciiDigit(source[1]);

    if (digit > 9) {
      throw new UnsupportedOperationException("Implement me");
    }

    if (digit < 0) {
      throw new UnsupportedOperationException("Implement me");
    }

    code += digit * 10;

    digit = Http.toUsAsciiDigit(source[2]);

    if (digit > 9) {
      throw new UnsupportedOperationException("Implement me");
    }

    if (digit < 0) {
      throw new UnsupportedOperationException("Implement me");
    }

    code += digit;

    resultStatus = Status.ofCode(code);

    return toParseChar(HttpParser.SP, Action.PARSE_REASON_PHRASE);
  }

  @Override
  final void executeResetImpl() {
    resultContentLength = null;

    resultContentType = null;

    result = null;

    resultReasonPhrase.setLength(0);

    resultStatus = null;
  }

  @Override
  final Action executeResult() {
    result = new Response(
      buildBody(),
      buildHeaders(),
      resultReasonPhrase.toString(),
      resultStatus,
      resultVersion
    );

    return Action.END;
  }

}
