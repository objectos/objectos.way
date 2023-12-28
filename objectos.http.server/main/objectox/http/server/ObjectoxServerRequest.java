/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.http.server;

import objectos.http.server.ServerRequest;
import objectos.http.server.UriPath;

public class ObjectoxServerRequest implements ServerRequest {

  private final ObjectoxHttpExchange exchange;

  public ObjectoxServerRequest(ObjectoxHttpExchange exchange) {
    this.exchange = exchange;
  }

  @Override
  public final boolean badRequest() {
    return false;
  }

  @Override
  public final boolean done() {
    return false;
  }

  @Override
  public final boolean serverRequest() {
    return true;
  }

  @Override
  public final UriPath path() {
    return exchange.requestPath;
  }

}