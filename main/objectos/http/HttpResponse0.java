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
import objectox.http.HeaderValueBuilder;
import objectox.http.RequestMethodEnum;
import objectox.http.Rfc;
import objectox.http.resp.StatusEnum;

final class HttpResponse0 implements HttpResponse {

  private final byte[] buffer;

  private final Clock clock;

  private boolean closeConnection;

  private final HttpErrorResponses errorResponses;

  private final boolean head;

  private final List<HttpResponse1Header> headers = new ArrayList<>();

  private final long id;

  private final Note.Sink noteSink;

  private final OutputStream outputStream;

  private boolean processed;

  private Status status = Status.OK;

  private final boolean testable;

  HttpResponse0(byte[] buffer, Clock clock, HttpErrorResponses errorResponses, boolean head, long id, Note.Sink noteSink, OutputStream outputStream, boolean testable) {
    this.buffer = buffer;

    this.clock = clock;

    this.errorResponses = errorResponses;

    this.head = head;

    this.id = id;

    this.noteSink = noteSink;

    this.outputStream = outputStream;

    this.testable = testable;
  }

  @Override
  public final String toString() {
    if (outputStream instanceof ByteArrayOutputStream impl) {
      final byte[] bytes;
      bytes = impl.toByteArray();

      return new String(bytes, StandardCharsets.UTF_8);
    } else {
      return "HttpExchange[]";
    }
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

  private void ok(Media.Bytes media) {
    status(Status.OK);

    header(HeaderName.DATE, now());

    send(media);
  }

  private void ok(Media.Stream media) {
    status(Status.OK);

    header(HeaderName.DATE, now());

    send(media);
  }

  private void ok(Media.Text media) {
    status(Status.OK);

    header(HeaderName.DATE, now());

    send(media);
  }

  // 3xx responses

  @Override
  public final void movedPermanently(String location) {
    location0(Status.MOVED_PERMANENTLY, location);
  }

  @Override
  public final void found(String location) {
    location0(Status.FOUND, location);
  }

  @Override
  public final void seeOther(String location) {
    location0(Status.SEE_OTHER, location);
  }

  private void location0(Status status, String location) {
    status(status);

    Objects.requireNonNull(location, "location == null");

    final String raw;
    raw = Rfc.urlEncode(location);

    header(HeaderName.DATE, now());

    header(HeaderName.CONTENT_LENGTH, 0L);

    header(HeaderName.LOCATION, raw);

    send();
  }

  // 4xx responses

  @Override
  public final void error(Status status) {
    final StatusEnum impl;
    impl = checkStatus(status);

    final Media media;
    media = errorResponses.get(impl);

    error0(impl, media);
  }

  @Override
  public final void error(Status status, String message) {
    final StatusEnum impl;
    impl = checkStatus(status);

    Objects.requireNonNull(message, "message == null");

    final Media media;
    media = errorResponses.get(impl, message);

    error0(impl, media);
  }

  @Override
  public final void error(Status status, Throwable cause) {
    final StatusEnum impl;
    impl = checkStatus(status);

    Objects.requireNonNull(cause, "cause == null");

    final Media media;
    media = errorResponses.get(impl, cause);

    error0(impl, media);
  }

  private StatusEnum checkStatus(Status status) {
    final StatusEnum impl;
    impl = (StatusEnum) status;

    if (!impl.isError()) {
      final String msg;
      msg = "HTTP status does not represent an error: " + status;

      throw new IllegalArgumentException(msg);
    }

    return impl;
  }

  private void error0(StatusEnum status, Media media) {
    status(status);

    header(HeaderName.DATE, now());

    header(HeaderName.CONNECTION, "close");

    send(media);
  }

  public final void allow(RequestMethodEnum... methods) {
    Objects.requireNonNull(methods, "methods == null");

    final String allow;
    allow = Arrays.stream(methods).map(RequestMethodEnum::name).collect(Collectors.joining(", "));

    status(Status.METHOD_NOT_ALLOWED);

    header(HeaderName.DATE, now());

    header(HeaderName.ALLOW, allow);

    header(HeaderName.CONTENT_LENGTH, 0L);

    send();
  }

  @Override
  public final void status(Status value) {
    checkProcessed();

    status = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void header(HeaderName name, long value) {
    checkProcessed();

    headers.add(
        new HttpResponse1Header(name, value)
    );
  }

  @Override
  public final void header(HeaderName name, String value) {
    checkProcessed();

    headers.add(
        new HttpResponse1Header(name, value)
    );
  }

  @Override
  public final void header(HeaderName name, Consumer<? super HeaderValueOptions> builder) {
    checkProcessed();

    Objects.requireNonNull(name, "name == null");

    final HeaderValueBuilder valueBuilder;
    valueBuilder = new HeaderValueBuilder();

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

  private void send(Media.Bytes media) {
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

    header(HeaderName.CONTENT_TYPE, contentType);

    header(HeaderName.CONTENT_LENGTH, length);

    try {
      final HttpResponse2Writer w;
      w = writer();

      w.send(bytes, 0, length);
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  private void send(Media.Text media) {
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

    header(HeaderName.CONTENT_TYPE, contentType);

    header(HeaderName.TRANSFER_ENCODING, "chunked");

    try {
      final HttpResponse2Writer w;
      w = writer();

      w.send(media);
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  private void send(Media.Stream media) {
    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Stream provided a null content-type");
    }

    header(HeaderName.CONTENT_TYPE, contentType);

    header(HeaderName.TRANSFER_ENCODING, "chunked");

    try {
      final HttpResponse2Writer w;
      w = writer();

      w.send(media);
    } catch (IOException e) {
      clientWriteError(e);
    }
  }

  @Override
  public final boolean processed() {
    return processed;
  }

  final long id() { return id; }

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
    writer = new HttpResponse2Writer(buffer, head, outputStream, testable);

    writer.status(status);

    for (var header : headers) {
      final HeaderName name;
      name = header.name();

      final String value;
      value = header.value();

      writer.header(name, value);

      if (name == HeaderName.CONNECTION) {
        closeConnection = "close".equalsIgnoreCase(value);
      }
    }

    writer.lineSeparator();

    return writer;
  }

}