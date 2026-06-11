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

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Consumer;

final class StaticFiles implements Handler, BiFunction<Request, Result, Result> {

  private final StaticFilesHandler staticFilesHandler;

  private final StaticFilesWriter staticFilesWriter;

  StaticFiles(
      StaticFilesHandler staticFilesHandler,

      StaticFilesWriter staticFilesWriter
  ) {
    this.staticFilesHandler = staticFilesHandler;

    this.staticFilesWriter = staticFilesWriter;
  }

  public static StaticFiles create(Consumer<? super StaticFilesBuilder> opts) throws IOException {
    final StaticFilesBuilder builder;
    builder = new StaticFilesBuilder();

    opts.accept(builder);

    return builder.build();
  }

  @Override
  public final Result apply(Request request, Result initial) {
    return switch (initial) {
      case Request r -> staticFilesHandler.handle(r);

      case StaticFileContent(Content c) -> staticFilesWriter.apply(request, c);

      default -> initial;
    };
  }

  @Override
  public final Result handle(Request request) {
    return staticFilesHandler.handle(request);
  }

}
