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
package objectos.http.internal;

import java.nio.charset.Charset;
import java.util.Objects;
import objectos.http.Http.Header.Name;
import objectos.http.Http.Status;
import objectos.http.io.CharWritable;
import objectos.http.server.Response;

final class HttpResponse implements Response {

  private final HttpExchange outer;

  HttpResponse(HttpExchange outer) {
    this.outer = outer;
  }

  @Override
  public final void header(Name name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    // TODO check state

    HttpResponseHeader header;
    header = new HttpResponseHeader(name, value);

    outer.responseHeaders.add(header);
  }

  @Override
  public final void send(byte[] data) {
    outer.responseBody = Objects.requireNonNull(data, "data == null");
  }

  @Override
  public final void send(CharWritable entity, Charset charset) {
    Objects.requireNonNull(entity, "entity == null");
    Objects.requireNonNull(charset, "charset == null");

    outer.responseBody = new HttpChunkedChars(outer, entity, charset);
  }

  @Override
  public final void status(Status status) {
    outer.status = Objects.requireNonNull(status, "status == null");
  }

}