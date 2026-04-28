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
package objectos.way.dev;

import module java.base;
import module objectos.way;

/// This class is not part of the Objectos Way JAR file. It is placed in the
/// main source tree to ease the development.
public final class DevModule implements Consumer<HttpRouting> {

  private final App.Injector injector;

  public DevModule(App.Injector injector) {
    this.injector = injector;
  }

  @Override
  public final void accept(HttpRouting routing) {
    final ScriptModule scripts;
    scripts = new ScriptModule();

    scripts.accept(routing);

    routing.path("/script.js", path -> path.GET(this::script));

    routing.path("/styles.css", path -> path.GET(this::styles));
  }

  private void script(HttpExchange http) {
    final JsLibrary library;
    library = JsLibrary.of();

    http.staticFile(library);
  }

  private void styles(HttpExchange http) {
    http.ok(StyleSheet.create(opts -> {
      final Note.Sink noteSink;
      noteSink = injector.getInstance(Note.Sink.class);

      opts.noteSink(noteSink);

      final Path classOutput;
      classOutput = Path.of("work", "main");

      opts.scanDirectory(classOutput);
    }));
  }

}
