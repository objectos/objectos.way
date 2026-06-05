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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class ResponseBuilder implements Response.Options {

  private ResponseEntity entity = ResponseEntity.OfEmpty.INSTANCE;

  private final List<Header> headers = new ArrayList<>();

  private HttpStatus status = HttpStatus.OK;

  private boolean closeConnection;

  public final ResponsePojo build() {
    final HttpStatus0 $status;
    $status = (HttpStatus0) status;

    return new ResponsePojo($status, headers, entity, closeConnection);
  }

  @Override
  public final void status(HttpStatus value) {
    status = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void header(HttpHeaderName name, long value) {
    final HttpHeaderName n;
    n = Objects.requireNonNull(name, "name == null");

    final String v;
    v = Long.toString(value);

    header0(n, v);
  }

  @Override
  public final void header(HttpHeaderName name, String value) {
    final HttpHeaderName n;
    n = Objects.requireNonNull(name, "name == null");

    header0(n, value);
  }

  @Override
  public final void header(HttpHeaderName name, Consumer<? super HttpHeaderValueBuilder> builder) {
    final HttpHeaderName n;
    n = Objects.requireNonNull(name, "name == null");

    final HttpHeaderValueBuilderImpl valueBuilder;
    valueBuilder = new HttpHeaderValueBuilderImpl();

    builder.accept(valueBuilder);

    final String value;
    value = valueBuilder.build();

    header0(n, value);
  }

  private void header0(HttpHeaderName name, String value) {
    if (name == HttpHeaderName.CONNECTION) {
      closeConnection = "close".equalsIgnoreCase(value);
    }

    final Header h;
    h = Header.of(name, value);

    headers.add(h);
  }

  @Override
  public final void date() {
    headers.add(
        Header.DATE
    );
  }

  @Override
  public final void send(Path file) {
    if (!Files.isRegularFile(file)) {
      throw new IllegalArgumentException(file + " does not represent a regular file");
    }

    entity = new ResponseEntity.OfFile(file);
  }

  @Override
  public final void send(Content content) {
    final Content c;
    c = Objects.requireNonNull(content, "content == null");

    entity = new ResponseEntity.OfContent(c);
  }

}
