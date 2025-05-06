/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;
import objectos.way.Http.SetCookie;

final class HttpSetCookieConfig implements SetCookie.Config {

  private String name;

  private String value;

  private boolean httpOnly;

  private boolean secure;

  private String path;

  private String domain;

  private Duration maxAge;

  private ZonedDateTime expires;

  private SetCookie.SameSite sameSite;

  @Override
  public final void name(String value) {
    name = Objects.requireNonNull(value, "value == null");
    checkNotBlank(name, "name");
  }

  @Override
  public final void value(String value) {
    this.value = Objects.requireNonNull(value, "value == null");
    checkNotBlank(this.value, "value");
  }

  @Override
  public final void httpOnly() {
    httpOnly = true;
  }

  @Override
  public final void secure() {
    secure = true;
  }

  @Override
  public final void path(String value) {
    path = Objects.requireNonNull(value, "value == null");
    checkNotBlank(path, "path");
  }

  @Override
  public final void domain(String value) {
    domain = Objects.requireNonNull(value, "value == null");
    checkNotBlank(domain, "domain");
  }

  @Override
  public final void maxAge(Duration value) {
    if (value.isNegative()) {
      throw new IllegalArgumentException("Max-Age cannot be negative");
    }

    maxAge = value;
  }

  @Override
  public final void expires(ZonedDateTime value) {
    expires = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void sameSite(Http.SetCookie.SameSite value) {
    sameSite = Objects.requireNonNull(value, "value == null");
  }

  final HttpSetCookie build() {
    if (name == null || value == null) {
      throw new IllegalArgumentException("Cookie name and value are required");
    }

    final StringBuilder sb;
    sb = new StringBuilder();

    sb.append(name);
    sb.append("=");
    sb.append(value);

    if (domain != null) {
      sb.append("; Domain=");

      sb.append(domain);
    }

    if (expires != null) {
      sb.append("; Expires=");

      sb.append(Http.formatDate(expires));
    }

    if (httpOnly) {
      sb.append("; HttpOnly");
    }

    if (maxAge != null) {
      sb.append("; Max-Age=");

      sb.append(maxAge.getSeconds());
    }

    if (path != null) {
      sb.append("; Path=");

      sb.append(path);
    }

    if (sameSite != null) {
      sb.append("; SameSite=");

      sb.append(sameSite.text);
    }

    if (secure) {
      sb.append("; Secure");
    }

    final String value;
    value = sb.toString();

    return new HttpSetCookie(value);
  }

  final String buildString() {
    if (name == null || value == null) {
      throw new IllegalArgumentException("Cookie name and value are required");
    }

    final StringBuilder sb;
    sb = new StringBuilder();

    sb.append(name);
    sb.append("=");
    sb.append(value);

    if (domain != null) {
      sb.append("; Domain=");

      sb.append(domain);
    }

    if (expires != null) {
      sb.append("; Expires=");

      sb.append(Http.formatDate(expires));
    }

    if (httpOnly) {
      sb.append("; HttpOnly");
    }

    if (maxAge != null) {
      sb.append("; Max-Age=");

      sb.append(maxAge.getSeconds());
    }

    if (path != null) {
      sb.append("; Path=");

      sb.append(path);
    }

    if (sameSite != null) {
      sb.append("; SameSite=");

      sb.append(sameSite.text);
    }

    if (secure) {
      sb.append("; Secure");
    }

    return sb.toString();
  }

  private void checkNotBlank(String value, String name) {
    if (value.isBlank()) {
      throw new IllegalArgumentException(name + " must not be blank");
    }
  }

}