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

import java.io.PrintWriter;
import java.io.StringWriter;
import objectos.way.Media;

final class HttpErrorResponses0 implements HttpErrorResponses {

  static final HttpErrorResponses STANDARD = new HttpErrorResponses0();

  @Override
  public final Media get(HttpStatus0 status) {
    return Media.Bytes.textPlain("" + status.code + " " + status.reasonPhrase + "\n");
  }

  @Override
  public final Media get(HttpStatus0 status, String message) {
    return Media.Bytes.textPlain("" + status.code + " " + status.reasonPhrase + "\n\n" + message + "\n");
  }

  @Override
  public final Media get(HttpStatus0 status, Throwable cause) {
    final StringWriter out;
    out = new StringWriter();

    final PrintWriter wrapper;
    wrapper = new PrintWriter(out);

    wrapper.print(status.code);

    wrapper.print(' ');

    wrapper.println(status.reasonPhrase);

    wrapper.println();

    cause.printStackTrace(wrapper);

    final String msg;
    msg = out.toString();

    return Media.Bytes.textPlain(msg);
  }

}
