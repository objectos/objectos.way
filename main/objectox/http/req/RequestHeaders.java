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
package objectox.http.req;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import objectos.http.HeaderName;
import objectox.http.Rfc;

public record RequestHeaders(Map<HeaderName, Object> headers) {

  public final boolean closeConnection() {
    final String connection;
    connection = Rfc.queryParamsGet(headers, HeaderName.CONNECTION);

    return "close".equalsIgnoreCase(connection);
  }

  public final String header(HeaderName name) {
    Objects.requireNonNull(name, "name == null");

    return Rfc.queryParamsGet(headers, name);
  }

  public final List<String> headerAll(HeaderName name) {
    Objects.requireNonNull(name, "name == null");

    return Rfc.queryParamsGetAll(headers, name);
  }

}
