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
package testing.site;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import objectos.way.App;
import objectos.way.Http;
import objectos.way.Note;
import testing.zite.TestingSiteInjector;

public final class TestingSiteDev extends TestingSite {

  @SuppressWarnings("unused")
  private final App.Option<Path> classOutputOption = option(
      "--class-output", ofPath(),
      //description("Where to look for class files in dev mode")
      withValidator(Files::isDirectory, "Path must be a directory")
  );

  private final App.Option<Path> testClassOutputOption = option(
      "--test-class-output", ofPath(),
      //description("Where to look for class files in dev mode")
      withValidator(Files::isDirectory, "Path must be a directory")
  );

  public static void main(String[] args) {
    new TestingSiteDev().start(args);
  }

  @Override
  final App.NoteSink noteSink() {
    return App.NoteSink.OfConsole.create(config -> {});
  }

  @Override
  final Http.HandlerFactory handlerFactory(Note.Sink noteSink, App.ShutdownHook shutdownHook, TestingSiteInjector injector) {
    FileSystem fileSystem;
    fileSystem = FileSystems.getDefault();

    WatchService watchService;

    try {
      watchService = fileSystem.newWatchService();
    } catch (IOException e) {
      throw App.serviceFailed("WatchService", e);
    }

    shutdownHook.register(watchService);

    App.Reloader reloader;

    try {
      reloader = App.Reloader.create(config -> {
        config.binaryName("testing.site.web.TestingHttpModule");

        config.watchService(watchService);

        config.noteSink(noteSink);

        config.directory(testClassOutputOption.get());
      });

      shutdownHook.register(reloader);
    } catch (IOException e) {
      throw App.serviceFailed("ClassReloader", e);
    }

    return App.createHandlerFactory(reloader, TestingSiteInjector.class, injector);
  }

}