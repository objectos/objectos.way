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
import java.time.Clock;
import objectos.notes.NoteSink;
import objectos.way.App;
import objectos.way.App.ShutdownHook;
import objectos.way.Http;
import objectos.way.Script;
import objectos.way.Web;
import testing.zite.TestingSiteInjector;

abstract class TestingSite extends App.Bootstrap {

  final App.Option<Integer> portOption = option(
      "--port", ofInteger(),
      //description("The port to listen to")
      withValidator(this::checkPort, "Port value must between 1024 < port < 65536"),
      withValue(8003)
  );

  private boolean checkPort(Integer value) {
    int port;
    port = value.intValue();

    return 1024 < port && port < 65536;
  }

  @Override
  protected final void bootstrap() {
    // NoteSink
    App.NoteSink noteSink;
    noteSink = noteSink();

    // ShutdownHook
    App.ShutdownHook shutdownHook;
    shutdownHook = App.ShutdownHook.create(noteSink);

    shutdownHook.registerIfPossible(noteSink);

    // SessionStore
    Web.Store sessionStore;
    sessionStore = Web.Store.create(config -> {});

    // Web.Resources

    Web.Resources webResources;

    try {
      webResources = Web.createResources(
          Web.contentTypes("""
          .css: text/css; charset=utf-8
          .js: text/javascript; charset=utf-8
          """),

          Web.serveFile("/common/way.js", Script.getBytes()),
          Web.serveFile("/ui/script.js", Script.getBytes())
      );
    } catch (IOException e) {
      throw App.serviceFailed("WebResources", e);
    }

    shutdownHook.register(webResources);

    // Carbon
    Http.Handler carbonHandler;
    carbonHandler = carbonHandler(noteSink);

    // Injector
    TestingSiteInjector injector;
    injector = new TestingSiteInjector(noteSink, sessionStore, webResources, carbonHandler);

    // HandlerFactory
    Http.HandlerFactory handlerFactory;
    handlerFactory = handlerFactory(noteSink, shutdownHook, injector);

    // Clock

    Clock clock;
    clock = clock();

    // WebServer
    try {
      Http.Server httpServer;
      httpServer = Http.createServer(
          handlerFactory,

          Http.bufferSize(1024, 4096),

          Http.clock(clock),

          Http.noteSink(noteSink),

          Http.port(portOption.get())
      );

      shutdownHook.register(httpServer);

      httpServer.start();
    } catch (IOException e) {
      throw App.serviceFailed("WebServer", e);
    }
  }

  abstract App.NoteSink noteSink();

  abstract Http.Handler carbonHandler(NoteSink noteSink);

  abstract Http.HandlerFactory handlerFactory(NoteSink noteSink, ShutdownHook shutdownHook, TestingSiteInjector injector);

  Clock clock() {
    return Clock.systemUTC();
  }

}