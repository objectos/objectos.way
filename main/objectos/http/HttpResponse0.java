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

  private final Clock clock;

  private boolean closeConnection;

  private final boolean head;

  private final List<HttpResponse1Header> headers = new ArrayList<>();

  private final long id;

  private final Note.Sink noteSink;

  private final OutputStream outputStream;

  private boolean processed;

  private HttpStatus status = HttpStatus.OK;

  HttpResponse0(byte[] buffer, Clock clock, boolean head, long id, Note.Sink noteSink, OutputStream outputStream) {
    this.buffer = buffer;

    this.clock = clock;

    this.head = head;

    this.id = id;

    this.noteSink = noteSink;

    this.outputStream = outputStream;
  }

  // 2xx responses

  @Override
  public final void ok(Media media) {
    switch (media) {
      case Media.Bytes b -> ok(b);

      case Media.Stream s -> ok(s);

      case Media.Text t -> ok(t);
    }
  }

  @Override
  public final void ok(Media.Bytes media) {
    status(HttpStatus.OK);

    header(HttpHeaderName.DATE, now());

    send(media);
  }

  @Override
  public final void ok(Media.Stream media) {
    status(HttpStatus.OK);

    header(HttpHeaderName.DATE, now());

    send(media);
  }

  @Override
  public final void ok(Media.Text media) {
    status(HttpStatus.OK);

    header(HttpHeaderName.DATE, now());

    send(media);
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

    header(HttpHeaderName.DATE, now());

    send(media);
  }

  @Override
  public final void forbidden(Media media) {
    status(HttpStatus.FORBIDDEN);

    header(HttpHeaderName.DATE, now());

    send(media);
  }

  @Override
  public final void notFound(Media media) {
    status(HttpStatus.NOT_FOUND);

    header(HttpHeaderName.DATE, now());

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

    header(HttpHeaderName.DATE, now());

    header(HttpHeaderName.CONNECTION, "close");

    send(media);

    noteSink.send(HttpServerTask.THROW, id, error);
  }

  @Override
  public final void status(HttpStatus value) {
    checkProcessed();

    status = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void header(HttpHeaderName name, long value) {
    checkProcessed();

    headers.add(
        new HttpResponse1Header(name, value)
    );
  }

  @Override
  public final void header(HttpHeaderName name, String value) {
    checkProcessed();

    headers.add(
        new HttpResponse1Header(name, value)
    );
  }

  @Override
  public final void header(HttpHeaderName name, Consumer<? super HttpHeaderValueBuilder> builder) {
    checkProcessed();

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
    final ZonedDateTime now;
    now = ZonedDateTime.now(clock);

    return Http.formatDate(now);
  }

  @Override
  public final void send() {
    try {
      final HttpResponse2Writer w;
      w = writer();

      w.send();
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  @Override
  public final void send(byte[] bytes) {
    try {
      final HttpResponse2Writer w;
      w = writer();

      w.send(bytes, 0, bytes.length);
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  @Override
  public final void send(byte[] bytes, int offset, int length) {
    try {
      final HttpResponse2Writer w;
      w = writer();

      w.send(bytes, offset, length);
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  @Override
  public final void send(Path file) {
    try {
      final HttpResponse2Writer w;
      w = writer();

      w.send(file);
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  @Override
  public final void send(Media media) {
    switch (media) {
      case Media.Bytes bytes -> send(bytes);

      case Media.Text text -> send(text);

      case Media.Stream stream -> send(stream);
    }
  }

  @Override
  public final void send(Media.Bytes media) {
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

    final int length;
    length = bytes.length;

    header(HttpHeaderName.CONTENT_TYPE, contentType);

    header(HttpHeaderName.CONTENT_LENGTH, length);

    try {
      final HttpResponse2Writer w;
      w = writer();

      w.send(bytes, 0, length);
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  @Override
  public final void send(Media.Text media) {
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

    header(HttpHeaderName.CONTENT_TYPE, contentType);

    header(HttpHeaderName.TRANSFER_ENCODING, "chunked");

    try {
      final HttpResponse2Writer w;
      w = writer();

      w.send(media);
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  @Override
  public final void send(Media.Stream media) {
    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Stream provided a null content-type");
    }

    header(HttpHeaderName.CONTENT_TYPE, contentType);

    header(HttpHeaderName.TRANSFER_ENCODING, "chunked");

    try {
      final HttpResponse2Writer w;
      w = writer();

      w.send(media);
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  public final boolean processed() {
    return processed;
  }

  final boolean closeConnection() {
    return closeConnection;
  }

  private void checkProcessed() {
    if (processed) {
      final String msg;
      msg = "A response has already been written out";

      throw new IllegalStateException(msg);
    }
  }

  private void clientWriteError(IOException e) {
    noteSink.send(HttpServerTask.THROW, id, e);
  }

  private HttpResponse2Writer writer() throws IOException {
    checkProcessed();

    processed = true;

    final HttpResponse2Writer writer;
    writer = new HttpResponse2Writer(buffer, head, outputStream);

    writer.status(status);

    for (var header : headers) {
      final HttpHeaderName name;
      name = header.name();

      final String value;
      value = header.value();

      writer.header(name, value);

      if (name == HttpHeaderName.CONNECTION) {
        closeConnection = "close".equalsIgnoreCase(value);
      }
    }

    writer.lineSeparator();

    return writer;
  }

}