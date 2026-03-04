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
package objectos.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

abstract class Fetch {

  static final JsString HI = JsString.raw("HI"); // history (on-off)
  static final JsString HU = JsString.raw("HU"); // history (url)
  static final JsString RH = JsString.raw("RH"); // request header

  private record ReqHeader(JsString name, JsString value) {
    @Override
    public final String toString() {
      return "[" + RH + "," + name + "," + value + "]";
    }
  }

  JsArray history;

  List<ReqHeader> reqHeaders;

  /// Adds the specified header field to the HTTP request.
  ///
  /// @param name the header name
  /// @param value the header value
  public final void header(String name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    final JsString $value;
    $value = JsString.of(value);

    header0(name, $value);
  }

  /// Adds the specified header field to the HTTP request.
  ///
  /// @param name the header name
  /// @param value the header value
  public final void header(String name, JsString value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    header0(name, value);
  }

  private void header0(String name, JsString value) {
    final JsString $name;
    $name = JsString.raw(name);

    final ReqHeader pojo;
    pojo = new ReqHeader($name, value);

    if (reqHeaders == null) {
      reqHeaders = new ArrayList<>();
    }

    reqHeaders.add(pojo);
  }

  /// Configures whether the browser's history is updated on a successful
  /// response.
  ///
  /// @param value `true` if the browser's history should be updated;
  ///        `false` otherwise
  public final void history(boolean value) {
    if (value) {
      history = null;
    } else {
      history = JsArray.raw(HI, "false");
    }
  }

  /// Adds an entry to the browser's history stack on a successful response.
  ///
  /// @param url the new history entry's URL
  public final void history(String url) {
    final JsString $url;
    $url = JsString.of(url);

    history($url);
  }

  /// Adds an entry to the browser's history stack on a successful response.
  ///
  /// @param url the new history entry's URL
  public final void history(JsString url) {
    Objects.requireNonNull(url, "url == null");

    history = JsArray.raw(HU, url);
  }

  final String addIf(List<?> list) {
    if (list != null) {
      return list.stream().map(Object::toString).collect(Collectors.joining(",", ",", ""));
    } else {
      return "";
    }
  }

}
