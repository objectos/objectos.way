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
package objectox.http.resp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Clock;
import java.util.List;
import java.util.function.Consumer;
import objectos.http.HttpHeaderName;
import objectos.http.Response;
import objectox.http.Header;
import objectox.http.HttpStatus0;

public record ResponsePojo(

    HttpStatus0 status,

    List<Header> headers,

    ResponseEntity entity,

    boolean closeConnection

) implements Response {

  public static ResponsePojo create0(Consumer<? super Options> opts) {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    opts.accept(builder);

    return builder.build();
  }

  public final void setCookie(String value) {
    final HttpHeaderName name;
    name = HttpHeaderName.SET_COOKIE;

    final Header h;
    h = new Header(name, value);

    headers.add(h);
  }

  @Override
  public final String toString() {
    return toString(Clock.systemUTC(), false);
  }

  public final String toString(Clock clock, boolean head) {
    final byte[] buffer;
    buffer = new byte[1024];

    final ResponseDate date;
    date = new ResponseDate(clock);

    final ByteArrayOutputStream outputStream;
    outputStream = new ByteArrayOutputStream();

    final ResponseSender sender;
    sender = new ResponseSender(buffer, date, outputStream);

    try {
      if (head) {
        sender.head(this);
      } else {
        sender.send(this);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    final byte[] bytes;
    bytes = outputStream.toByteArray();

    return new String(bytes);
  }

}
