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

import java.io.IOException;
import objectos.http.Method;
import objectos.http.server.ServerExchangeResult;
import objectos.http.server.ServerRequest;
import objectos.http.server.ServerRequestBody;
import objectos.http.server.ServerRequestHeaders;
import objectos.http.server.ServerResponse;
import objectos.http.server.UriPath;
import objectox.http.StandardHeaderName;

public final class ObjectoxServerRequest implements ServerRequest {

  private final SocketInput input;

  private ServerRequestBody body;

  private ObjectoxRequestLine requestLine;

  private ServerRequestHeaders requestHeaders;

  public ObjectoxServerRequest(SocketInput input) {
    this.input = input;
  }

  @Override
  public final Method method() {
    return requestLine.method;
  }

  @Override
  public final UriPath path() {
    return requestLine.path;
  }

  @Override
  public final ServerRequestHeaders headers() {
    return requestHeaders;
  }

  @Override
  public final ServerRequestBody body() {
    return body;
  }

  public final ServerExchangeResult get() throws IOException {
    ObjectoxRequestLine requestLine;
    requestLine = new ObjectoxRequestLine(input);

    requestLine.parse();

    if (requestLine.status != null) {
      return ObjectoxBadRequest.INSTANCE;
    }

    ObjectoxServerRequestHeaders requestHeaders;
    requestHeaders = new ObjectoxServerRequestHeaders(input);

    requestHeaders.parse();

    if (requestHeaders.status != null) {
      return ObjectoxBadRequest.INSTANCE;
    }

    ServerRequestBody body;
    body = NoServerRequestBody.INSTANCE;

    if (requestHeaders.contains(StandardHeaderName.CONTENT_LENGTH)) {
      throw new UnsupportedOperationException(
          "Implement me :: parse body"
      );
    }

    if (requestHeaders.contains(StandardHeaderName.TRANSFER_ENCODING)) {
      throw new UnsupportedOperationException(
          "Implement me :: maybe chunked?"
      );
    }

    this.body = body;

    this.requestLine = requestLine;

    this.requestHeaders = requestHeaders;

    return this;
  }

  @Override
  public final ServerResponse response() {
    throw new UnsupportedOperationException("Implement me");
  }

}