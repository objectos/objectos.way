/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;
import objectos.way.Http.ResponseMessage;

sealed abstract class HttpSupport implements Http.Exchange, Http.ResponseHeaders permits HttpExchange {

  Clock clock;

  // 2xx responses

  @Override
  public final void ok(Media.Bytes media) {
    respond(Http.Status.OK, media);
  }

  // 4xx responses

  @Override
  public final void badRequest(Media.Bytes media) {
    respond(Http.Status.BAD_REQUEST, media);
  }

  @Override
  public final void notFound(Media.Bytes media) {
    respond(Http.Status.NOT_FOUND, media);
  }

  @Override
  public final void respond(ResponseMessage message) {
    HttpResponseMessage impl;
    impl = (HttpResponseMessage) message;

    impl.accept(this);
  }

  public final void respond(Http.Status status, Media.Bytes media) {
    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new NullPointerException("The specified Media.Bytes provided a null content-type");
    }

    final byte[] bytes;
    bytes = media.toByteArray();

    if (bytes == null) {
      throw new NullPointerException("The specified Media.Bytes provided a null byte array");
    }

    status0(status);

    dateNow();

    header0(Http.HeaderName.CONTENT_TYPE, contentType);

    header0(Http.HeaderName.CONTENT_LENGTH, bytes.length);

    body0(media, bytes);
  }

  @Override
  public final void respond(Lang.MediaWriter writer) {
    respond(Http.Status.OK, writer);
  }

  @Override
  public final void header(Http.HeaderName name, long value) {
    Objects.requireNonNull(name, "name == null");

    header0(name, value);
  }

  @Override
  public final void header(Http.HeaderName name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    header0(name, value);
  }

  @Override
  public final void dateNow() {
    Clock theClock;
    theClock = clock;

    if (theClock == null) {
      theClock = Clock.systemUTC();
    }

    ZonedDateTime now;
    now = ZonedDateTime.now(theClock);

    String value;
    value = Http.formatDate(now);

    header0(Http.HeaderName.DATE, value);
  }

  abstract void endResponse();

  abstract void status0(Http.Status status);

  final void header0(Http.HeaderName name, long value) {
    final String s;
    s = Long.toString(value);

    header0(name, s);
  }

  abstract void header0(Http.HeaderName name, String value);

  abstract void body0(Object original, byte[] bytes);

  abstract void send0();

  abstract void send0(byte[] bytes);

  abstract void send0(Path file);

}