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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import objectos.way.Media;

final class ResponseBuilder implements Response.Options {

  private ResponseBody body = ResponseBody.OfEmpty.INSTANCE;

  private final List<Header> headers = new ArrayList<>();

  private HttpStatus status = HttpStatus.OK;

  public final Response build() {
    final HttpStatus0 $status;
    $status = (HttpStatus0) status;

    final List<Header> $headers;
    $headers = List.copyOf(headers);

    return new Response0($status, $headers, body);
  }

  @Override
  public final void status(HttpStatus value) {
    status = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void header(HttpHeaderName name, long value) {
    headers.add(
        Header.of(name, value)
    );
  }

  @Override
  public final void header(HttpHeaderName name, String value) {
    headers.add(
        Header.of(name, value)
    );
  }

  @Override
  public final void header(HttpHeaderName name, Consumer<? super HttpHeaderValueBuilder> builder) {
    headers.add(
        Header.of(name, builder)
    );
  }

  @Override
  public final void date() {
    headers.add(
        Header.DATE
    );
  }

  @Override
  public final void body() {
    body = ResponseBody.OfEmpty.INSTANCE;
  }

  @Override
  public final void body(byte[] bytes) {
    body = new ResponseBody.OfBytes(bytes, 0, bytes.length);
  }

  @Override
  public final void body(byte[] bytes, int offset, int length) {
    if (length < 0) {
      throw new IllegalArgumentException("length < 0");
    }

    if (offset < 0) {
      throw new IllegalArgumentException("offset < 0");
    }

    if (offset + length < bytes.length) {
      throw new IllegalArgumentException("offset + length < bytes.length");
    }

    body = new ResponseBody.OfBytes(bytes, 0, bytes.length);
  }

  @Override
  public final void body(Path file) {
    body = new ResponseBody.OfFile(file);
  }

  @Override
  public final void body(Media.Stream entity) {
    Objects.requireNonNull(entity, "entity == null");

    body = new ResponseBody.OfMediaStream(entity);
  }

  @Override
  public final void body(Media.Text entity) {
    Objects.requireNonNull(entity, "entity == null");

    body = new ResponseBody.OfMediaText(entity);
  }

}
