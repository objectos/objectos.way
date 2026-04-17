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

import java.util.Objects;
import objectos.way.Note;

final class HttpHost0 implements HttpHost {

  private HttpHandler handler = HttpHandler.notFound();

  String name;

  private Note.Sink noteSink;

  private HttpSessionLoader sessionLoader = (_, _) -> null;

  @Override
  public final void name(String value) {
    name = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void handler(HttpHandler value) {
    handler = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void noteSink(Note.Sink value) {
    noteSink = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void sessionStore(HttpSessionStore value) {
    sessionLoader = (HttpSessionLoader) Objects.requireNonNull(value, "value == null");
  }

  final HttpHost1 build(Note.Sink serverNoteSink) {
    return new HttpHost1(
        handler,

        noteSink != null ? noteSink : serverNoteSink,

        sessionLoader
    );
  }

  final String name(int port) {
    return name != null
        ? name
        : port == 80
            ? "localhost"
            : "localhost:" + port;
  }

}
