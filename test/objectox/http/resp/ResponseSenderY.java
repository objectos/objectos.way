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

import java.io.IOException;
import java.io.OutputStream;
import java.time.Clock;
import java.util.function.Consumer;
import objectos.http.ResponseOptions;
import objectos.way.Y;

final class ResponseSenderY {

  int bufferSize;

  Clock clock = Y.clockFixed();

  OutputStream outputStream;

  private ResponseSenderY() {}

  public static ResponseSender create(Consumer<? super ResponseSenderY> opts) {
    final ResponseSenderY y;
    y = new ResponseSenderY();

    opts.accept(y);

    return y.build();
  }

  public static String send(Consumer<? super ResponseSenderY> opts, Consumer<? super ResponseOptions> resp) throws IOException {
    final ResponseSenderY y;
    y = new ResponseSenderY();

    opts.accept(y);

    final ResponseSender sender;
    sender = y.build();

    final ResponsePojo pojo;
    pojo = ResponsePojo.create0(resp);

    sender.send(pojo);

    return y.response();
  }

  private ResponseSender build() {
    final ResponseDate date;
    date = new ResponseDate(clock);

    return new ResponseSender(
        new byte[bufferSize],

        date,

        outputStream
    );
  }

  private String response() {
    return outputStream.toString();
  }

}
