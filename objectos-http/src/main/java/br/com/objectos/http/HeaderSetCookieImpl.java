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

final class HeaderSetCookieImpl
    extends AbstractHeader<Header.SetCookie> implements Header.SetCookie {

  private State state = State.COOKIE_NAME;

  private StringBuilder stringBuilder = new StringBuilder();

  private String cookieName;

  private String cookieValue;

  @Override
  public final void acceptResponseVisitor(ResponseVisitor visitor) {
    visitor.visitResponseHeader(this);
  }

  @Override
  public final void consume(char c) {
    state = execute(c);
  }

  @Override
  public final String getCookieName() {
    return cookieName;
  }

  @Override
  public final String getCookieValue() {
    return cookieValue;
  }

  @Override
  public final String getHeaderName() {
    return "Set-Cookie";
  }

  @Override
  public final boolean isMalformed() {
    return state == State.MALFORMED;
  }

  @Override
  public final boolean shouldConsume() {
    return state != null;
  }

  @Override
  final void clear() {
    state = null;

    stringBuilder = null;
  }

  @Override
  final void toStringValue(StringBuilder result) {
    result.append(cookieName);

    result.append('=');

    result.append(cookieValue);
  }

  private String buildString() {
    String result;
    result = stringBuilder.toString();

    stringBuilder.setLength(0);

    return result;
  }

  private State execute(char c) {
    switch (state) {
      case COOKIE_NAME:
        return executeCookieName(c);
      case COOKIE_VALUE:
        return executeCookieValue(c);
      case CR:
        return executeCr(c);
      default:
        throw new UnsupportedOperationException("Implement me @ " + state);
    }
  }

  private State executeCookieName(char c) {
    if (c == '=') {
      cookieName = buildString();

      return State.COOKIE_VALUE;
    }

    if (!Character.isLetterOrDigit(c)) {
      return State.MALFORMED;
    }

    stringBuilder.append(c);

    return state;
  }

  private State executeCookieValue(char c) {
    if (c == Http.CR) {
      cookieValue = buildString();

      return State.CR;
    }

    if (!Character.isLetterOrDigit(c)) {
      return State.MALFORMED;
    }

    stringBuilder.append(c);

    return state;
  }

  private State executeCr(char c) {
    if (c == Http.LF) {
      return toResult();
    }

    return State.MALFORMED;
  }

  private State toResult() {
    // stop, return null
    return null;
  }

  private enum State {

    COOKIE_NAME,

    COOKIE_VALUE,

    CR,

    MALFORMED;

  }

}
