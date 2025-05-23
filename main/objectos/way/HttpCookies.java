/*
 * Copyright (C) 2025 Objectos Software LTDA.
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

import java.util.Map;

final class HttpCookies implements Http.Cookies {

  private final Map<String, HttpRequestCookiesValue> cookies;

  HttpCookies(Map<String, HttpRequestCookiesValue> cookies) {
    this.cookies = cookies;
  }

  @Override
  public final String get(String name) {
    String res = null;

    HttpRequestCookiesValue value;
    value = cookies.get(name);

    if (value != null) {
      res = value.get();
    }

    return res;
  }

}