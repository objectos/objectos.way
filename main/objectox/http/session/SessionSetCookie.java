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
package objectox.http.session;

import java.time.Duration;
import objectox.http.SameSite;

record SessionSetCookie(
    String domain,

    boolean httpOnly,

    Duration maxAge,

    String name,

    String path,

    SameSite sameSite,

    boolean secure
) {

  public final String forValid(String value) {
    final StringBuilder sb;
    sb = new StringBuilder();

    sb.append(name);

    sb.append("=");

    sb.append(value);

    if (domain != null) {
      sb.append("; Domain=");

      sb.append(domain);
    }

    /*
    if (cookieExpires != null) {
      sb.append("; Expires=");

      sb.append(Http.formatDate(cookieExpires));
    }
    */

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

  public final String forInvalid() {
    final StringBuilder sb;
    sb = new StringBuilder();

    sb.append(name);

    sb.append("=");

    if (domain != null) {
      sb.append("; Domain=");

      sb.append(domain);
    }

    if (httpOnly) {
      sb.append("; HttpOnly");
    }

    sb.append("; Max-Age=0");

    if (path != null) {
      sb.append("; Path=");

      sb.append(path);
    }

    if (secure) {
      sb.append("; Secure");
    }

    return sb.toString();
  }

}
