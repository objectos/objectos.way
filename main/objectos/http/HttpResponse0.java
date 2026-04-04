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

import module java.base;
import module objectos.way;

final class HttpResponse0 implements HttpResponse {

  private final byte[] buffer;

  private final List<HttpResponse1Header> headers = new ArrayList<>();

  private final OutputStream outputStream;

  private HttpStatus status = HttpStatus.OK;

  private final HttpVersion version;

  HttpResponse0(byte[] buffer, OutputStream outputStream, HttpVersion version) {
    this.buffer = buffer;

    this.outputStream = outputStream;

    this.version = version;
  }

  // 2xx responses

  @Override
  public final void ok(Media.Bytes media) {
    status(HttpStatus.OK);

    media(media);
  }

  @Override
  public final void ok(Media.Stream media) {
    status(HttpStatus.OK);

    media(media);
  }

  @Override
  public final void ok(Media.Text media) {
    status(HttpStatus.OK);

    media(media);
  }

  // 3xx responses

  @Override
  public final void movedPermanently(String location) {
    location0(HttpStatus.MOVED_PERMANENTLY, location);
  }

  @Override
  public final void found(String location) {
    location0(HttpStatus.FOUND, location);
  }

  @Override
  public final void seeOther(String location) {
    location0(HttpStatus.SEE_OTHER, location);
  }

  private void location0(HttpStatus status, String location) {
    status(status);

    Objects.requireNonNull(location, "location == null");

    final String raw;
    raw = Http.raw(location);

    header(HttpHeaderName.DATE, now());

    header(HttpHeaderName.CONTENT_LENGTH, 0L);

    header(HttpHeaderName.LOCATION, raw);

    send();
  }

  // 4xx responses

  @Override
  public final void badRequest(Media media) {
    status(HttpStatus.BAD_REQUEST);

    send(media);
  }

  @Override
  public final void forbidden(Media media) {
    status(HttpStatus.FORBIDDEN);

    send(media);
  }

  @Override
  public final void notFound(Media media) {
    status(HttpStatus.NOT_FOUND);

    send(media);
  }

  @Override
  public final void allow(HttpMethod... methods) {
    Objects.requireNonNull(methods, "methods == null");

    final String allow;
    allow = Arrays.stream(methods).map(HttpMethod::name).collect(Collectors.joining(", "));

    status(HttpStatus.METHOD_NOT_ALLOWED);

    header(HttpHeaderName.DATE, now());

    header(HttpHeaderName.ALLOW, allow);

    header(HttpHeaderName.CONTENT_LENGTH, 0L);

    send();
  }

  // 5xx responses

  @Override
  public final void internalServerError(Media media, Throwable error) {
    status(HttpStatus.INTERNAL_SERVER_ERROR);

    send(media);

    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void status(HttpStatus value) {
    status = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void header(HttpHeaderName name, long value) {
    headers.add(
        new HttpResponse1Header(name, value)
    );
  }

  @Override
  public final void header(HttpHeaderName name, String value) {
    headers.add(
        new HttpResponse1Header(name, value)
    );
  }

  @Override
  public final void header(HttpHeaderName name, Consumer<? super HttpHeaderValueBuilder> builder) {
    Objects.requireNonNull(name, "name == null");

    final HttpHeaderValueBuilderImpl valueBuilder;
    valueBuilder = new HttpHeaderValueBuilderImpl();

    builder.accept(valueBuilder);

    final String value;
    value = valueBuilder.build();

    header(name, value);
  }

  @Override
  public final String now() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void send() {
    try {
      writeStart();
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  @Override
  public final void send(byte[] bytes, int offset, int length) {
    try {
      writeStart();

      // FIX writing mode depends on response headers
      outputStream.write(bytes, offset, length);
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  @Override
  public final void send(Path file) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void send(Media media) {
    switch (media) {
      case Media.Bytes bytes -> media(bytes);

      case Media.Text text -> media(text);

      case Media.Stream stream -> media(stream);
    }
  }

  public final void media(Media.Bytes media) {
    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Bytes provided a null content-type");
    }

    final byte[] bytes;
    bytes = media.toByteArray();

    if (bytes == null) {
      throw new IllegalArgumentException("The specified Media.Bytes provided a null byte array");
    }

    header(HttpHeaderName.DATE, now());

    header(HttpHeaderName.CONTENT_TYPE, contentType);

    header(HttpHeaderName.CONTENT_LENGTH, bytes.length);

    throw new UnsupportedOperationException("Implement me");
  }

  public final void media(Media.Text media) {
    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Text provided a null content-type");
    }

    final Charset charset;
    charset = media.charset();

    if (charset == null) {
      throw new IllegalArgumentException("The specified Media.Text provided a null charset");
    }

    header(HttpHeaderName.DATE, now());

    header(HttpHeaderName.CONTENT_TYPE, contentType);

    header(HttpHeaderName.TRANSFER_ENCODING, "chunked");

    throw new UnsupportedOperationException("Implement me");
  }

  public final void media(Media.Stream media) {
    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Stream provided a null content-type");
    }

    header(HttpHeaderName.DATE, now());

    header(HttpHeaderName.CONTENT_TYPE, contentType);

    header(HttpHeaderName.TRANSFER_ENCODING, "chunked");

    throw new UnsupportedOperationException("Implement me");
  }

  private void clientWriteError(IOException e) {
    throw new UnsupportedOperationException("Implement me");
  }

  private void writeStart() throws IOException {
    final HttpResponse2WriteStart writer;
    writer = new HttpResponse2WriteStart(buffer, headers, outputStream, status, version);

    writer.write();
  }

}