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

import java.util.Iterator;
import java.util.List;

sealed abstract class HttpHandler0Super implements HttpHandler
    permits
    HttpHandler1Path,
    HttpHandler2Method,
    HttpHandler3MethodNotAllowed,
    HttpHandler4NotFound,
    HttpHandler5List {

  @Override
  public final void handle(HttpExchange http) {
    if (http.processed()) {
      return;
    }

    handleImpl(http);
  }

  abstract void handleImpl(HttpExchange http);

  final void handleFirst(HttpExchange http, List<HttpHandler> list) {
    final Iterator<HttpHandler> iter;
    iter = list.iterator();

    while (!http.processed() && iter.hasNext()) {
      final HttpHandler handler;
      handler = iter.next();

      handler.handle(http);
    }
  }

}
