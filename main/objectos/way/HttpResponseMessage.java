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

import java.util.Arrays;
import java.util.stream.Collectors;
import objectos.way.Http.ResponseMessage;

final class HttpResponseMessage implements Http.ResponseMessage {

  enum Kind {

    FOUND,

    METHOD_NOT_ALLOWED,

    OK_MEDIA_BYTES;

  }

  private final Kind kind;

  private final Object value;

  public HttpResponseMessage(Kind kind, Object value) {
    this.kind = kind;
    this.value = value;
  }

  static ResponseMessage found(String location) {
    return new HttpResponseMessage(Kind.FOUND, location);
  }

  static ResponseMessage methodNotAllowed(Http.Method[] allowedMethods) {
    final String allow;
    allow = Arrays.stream(allowedMethods).map(Http.Method::name).collect(Collectors.joining(", "));

    return new HttpResponseMessage(Kind.METHOD_NOT_ALLOWED, allow);
  }

  static ResponseMessage ok(Media.Bytes object) {
    return new HttpResponseMessage(Kind.OK_MEDIA_BYTES, object);
  }

  final void accept(HttpSupport http) {
    switch (kind) {
      case FOUND -> {

        http.status0(Http.Status.FOUND);

        http.dateNow();

        http.header0(Http.HeaderName.CONTENT_LENGTH, 0L);

        http.header0(Http.HeaderName.LOCATION, valueAsString());

        http.send0();

      }

      case METHOD_NOT_ALLOWED -> {

        http.status0(Http.Status.METHOD_NOT_ALLOWED);

        http.dateNow();

        http.header0(Http.HeaderName.CONTENT_LENGTH, 0L);

        http.header0(Http.HeaderName.ALLOW, valueAsString());

        http.send0();

      }

      case OK_MEDIA_BYTES -> {

        final Media.Bytes object;
        object = (Media.Bytes) value;

        http.respond(Http.Status.OK, object);

      }
    }
  }

  private String valueAsString() {
    return (String) value;
  }

}