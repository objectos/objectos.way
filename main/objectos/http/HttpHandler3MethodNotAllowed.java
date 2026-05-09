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
package objectos.http;

import java.util.Set;
import java.util.stream.Collectors;

final class HttpHandler3MethodNotAllowed extends HttpHandler0Super {

  private final Set<HttpMethod> allowedMethods;

  HttpHandler3MethodNotAllowed(Set<HttpMethod> allowedMethods) {
    this.allowedMethods = allowedMethods;
  }

  @Override
  final void handleImpl(HttpExchange http) {
    final HttpMethod reqMethod;
    reqMethod = http.method();

    if (allowedMethods.contains(reqMethod)) {
      return;
    }

    http.status(HttpStatus.METHOD_NOT_ALLOWED);

    http.header(HttpHeaderName.DATE, http.now());

    http.header(HttpHeaderName.CONNECTION, "close");

    http.header(HttpHeaderName.CONTENT_LENGTH, 0L);

    final String allow;
    allow = allowedMethods.stream().map(HttpMethod::name).collect(Collectors.joining(", "));

    http.header(HttpHeaderName.ALLOW, allow);

    http.send();
  }

}
