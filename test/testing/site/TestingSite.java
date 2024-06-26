/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.time.Clock;
import objectos.args.CommandLine;
import objectos.args.CommandLineException;
import objectos.args.EnumOption;
import objectos.args.IntegerOption;
import objectos.args.PathOption;
import objectos.css.reset.Preflight;
import objectos.lang.WayShutdownHook;
import objectos.lang.classloader.ClassReloader;
import objectos.notes.Level;
import objectos.notes.Note2;
import objectos.notes.NoteSink;
import objectos.notes.impl.ConsoleNoteSink;
import objectos.way.AppSessionStore;
import objectos.way.HandlerFactory;
import objectos.way.Http;
import objectos.way.Script;
import objectos.web.BootstrapException;
import objectos.web.Stage;
import objectos.web.WayWebResources;
import testing.zite.TestingSiteInjector;

public class TestingSite {

  final EnumOption<Stage> stageOption;

  final IntegerOption portOption;

  final PathOption classOutputOption;

  TestingSite() {
    stageOption = new EnumOption<>(Stage.class, "--stage");
    stageOption.description("The stage to start this application: DEVELOPMENT, TESTING or PRODUCTION");
    stageOption.required();

    portOption = new IntegerOption("--port");
    portOption.description("The port to listen to");
    portOption.validator(this::checkPort, "Port value must between 1024 < port < 65536");
    portOption.set(8003);

    classOutputOption = new PathOption("--class-output");
    classOutputOption.description("Where to look for class files in dev mode");
    classOutputOption.activator(() -> stageOption.is(Stage.DEVELOPMENT));
    classOutputOption.validator(Files::isDirectory, "Path must be a directory");
  }

  private boolean checkPort(Integer value) {
    int port;
    port = value.intValue();

    return 1024 < port && port < 65536;
  }

  public static void main(String[] args) {
    TestingSite site;
    site = new TestingSite();

    site.parseArgs(args);

    site.start();
  }

  private void parseArgs(String[] args) {
    CommandLine cli;
    cli = new CommandLine("ui", stageOption, portOption, classOutputOption);

    try {
      cli.parse(args);
    } catch (CommandLineException e) {
      e.printMessage();

      System.exit(1);
    }
  }

  // visible for testing
  final void start() {
    try {
      bootstrap();
    } catch (BootstrapException e) {
      fail(e);
    }
  }

  private void bootstrap() throws BootstrapException {
    Stage mode;
    mode = stageOption.get();

    // NoteSink
    NoteSink noteSink;

    switch (mode) {
      case DEVELOPMENT, TESTING -> noteSink = new ConsoleNoteSink(Level.TRACE);

      default -> throw new UnsupportedOperationException("Implement me");
    }

    // ShutdownHook
    WayShutdownHook shutdownHook;
    shutdownHook = new WayShutdownHook();

    shutdownHook.noteSink(noteSink);

    if (noteSink instanceof AutoCloseable closeable) {
      shutdownHook.addAutoCloseable(closeable);
    }

    // SessionStore
    AppSessionStore sessionStore;
    sessionStore = new AppSessionStore();

    // WebResources

    WayWebResources webResources;

    try {
      webResources = new WayWebResources();

      shutdownHook.addAutoCloseable(webResources);

      webResources.contentType(".css", "text/css; charset=utf-8");

      webResources.contentType(".js", "text/javascript; charset=utf-8");

      Path common;
      common = Path.of("common");

      Preflight preflight;
      preflight = new Preflight();

      webResources.createNew(common.resolve("preflight.css"), preflight.toString().getBytes(StandardCharsets.UTF_8));

      webResources.createNew(common.resolve("way.js"), Script.getBytes());
    } catch (IOException e) {
      throw new BootstrapException("WebResources", e);
    }

    // WaySiteInjector
    TestingSiteInjector injector;
    injector = new TestingSiteInjector(noteSink, sessionStore, mode, webResources);

    // HandlerFactory
    HandlerFactory handlerFactory;

    switch (mode) {
      case DEVELOPMENT -> {
        FileSystem fileSystem;
        fileSystem = FileSystems.getDefault();

        WatchService watchService;

        try {
          watchService = fileSystem.newWatchService();
        } catch (IOException e) {
          throw new BootstrapException("WatchService", e);
        }

        shutdownHook.addAutoCloseable(watchService);

        ClassReloader.Builder classReloaderBuilder;
        classReloaderBuilder = ClassReloader.builder();

        classReloaderBuilder.noteSink(noteSink);

        classReloaderBuilder.watchService(watchService);

        Path classOutput;
        classOutput = classOutputOption.get();

        classReloaderBuilder.watch(classOutput, "testing.site");

        ClassReloader classReloader;

        try {
          classReloader = classReloaderBuilder.of("testing.site.web.TestingHttpModule");

          shutdownHook.addAutoCloseable(classReloader);
        } catch (IOException e) {
          throw new BootstrapException("ClassReloader", e);
        }

        handlerFactory = new DevHandlerFactory(classReloader, injector);
      }

      case TESTING -> handlerFactory = new ProdHandlerFactory(injector);

      default -> throw new UnsupportedOperationException("Implement me");
    }

    // Clock

    Clock clock;
    clock = switch (mode) {
      default -> Clock.systemUTC();

      case TESTING -> testingClock();
    };

    // WebServer
    try {
      Http.Server httpServer;
      httpServer = Http.createServer(
          handlerFactory,

          Http.bufferSize(1024, 4096),

          Http.clock(clock),

          Http.noteSink(noteSink),

          Http.port(portOption.get()),

          Http.sessionStore(sessionStore)
      );

      shutdownHook.addAutoCloseable(httpServer);

      httpServer.start();
    } catch (IOException e) {
      throw new BootstrapException("WebServer", e);
    }
  }

  Clock testingClock() {
    throw new UnsupportedOperationException();
  }

  private void fail(BootstrapException e) {
    NoteSink noteSink;
    noteSink = new ConsoleNoteSink(Level.ERROR);

    Note2<String, Throwable> note;
    note = Note2.error(TestingSite.class, "Bootstrap failed [service]");

    String service;
    service = e.getMessage();

    Throwable error;
    error = e.getCause();

    noteSink.send(note, service, error);

    System.exit(2);
  }

}