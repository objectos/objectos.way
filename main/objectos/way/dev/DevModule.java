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
package objectos.way.dev;

import static objectos.way.Http.Method.GET;

import module java.base;
import module objectos.way;

/// This class is not part of the Objectos Way JAR file.
/// It is placed in the main source tree to ease the development.
public final class DevModule implements Http.Routing.Module {

  private final App.Injector injector;

  public DevModule(App.Injector injector) {
    this.injector = injector;
  }

  @Override
  public final void configure(Http.Routing routing) {
    routing.install(new DevScript());

    routing.path("/styles.css", GET, this::styles);

    routing.handler(Http.Handler.notFound());
  }

  private void styles(Http.Exchange http) {
    http.ok(Css.StyleSheet.create(opts -> {
      final Note.Sink noteSink;
      noteSink = injector.getInstance(Note.Sink.class);

      opts.noteSink(noteSink);

      final Path classOutput;
      classOutput = Path.of("work", "main");

      opts.scanDirectory(classOutput);
    }));
  }

}
