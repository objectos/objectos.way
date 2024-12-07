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
package objectos.way.www;

import java.io.IOException;
import objectos.way.App;
import objectos.way.Http;
import objectos.way.Http.HandlerFactory;
import objectos.way.Note;
import objectos.way.Script;
import objectos.way.Web;

abstract class Start extends App.Bootstrap {

  public static final int DEVELOPMENT_HTTP_PORT = 8005;

  @Override
  protected final void bootstrap() {
    long startTime;
    startTime = System.currentTimeMillis();

    // Stage
    Stage stage;
    stage = stage();

    // NoteSink
    App.NoteSink noteSink;
    noteSink = noteSink();

    Note.Ref0 startNote;
    startNote = Note.Ref0.create(getClass(), "Start", Note.INFO);

    noteSink.send(startNote);

    // ShutdownHook
    App.ShutdownHook shutdownHook;
    shutdownHook = App.ShutdownHook.create(config -> config.noteSink(noteSink));

    shutdownHook.registerIfPossible(noteSink);

    // WebResources
    Web.Resources webResources;
    webResources = webResources(noteSink);

    shutdownHook.register(webResources);

    // Injector
    Injector injector;
    injector = new Injector(stage, noteSink, webResources);

    // HandlerFactory
    HandlerFactory handlerFactory;
    handlerFactory = handlerFactory(shutdownHook, injector);

    // Http.Server
    try {
      Http.Server httpServer;
      httpServer = Http.Server.create(config -> {
        config.handlerFactory(handlerFactory);

        config.bufferSize(1024, 4096);

        config.noteSink(noteSink);

        config.port(serverPort());
      });

      shutdownHook.register(httpServer);

      httpServer.start();
    } catch (IOException e) {
      throw App.serviceFailed("WebServer", e);
    }

    Note.Long1 totalTimeNote;
    totalTimeNote = Note.Long1.create(getClass(), "Total time [ms]", Note.INFO);

    long totalTime;
    totalTime = System.currentTimeMillis() - startTime;

    noteSink.send(totalTimeNote, totalTime);
  }

  abstract Stage stage();

  abstract App.NoteSink noteSink();

  private Web.Resources webResources(Note.Sink noteSink) {
    try {
      return Web.Resources.create(config -> {
        config.noteSink(noteSink);

        config.contentTypes("""
        .js: text/javascript; charset=utf-8
        """);

        config.serveFile("/ui/script.js", Script.getBytes());
      });
    } catch (IOException e) {
      throw App.serviceFailed("WebResources", e);
    }
  }

  abstract Http.HandlerFactory handlerFactory(App.ShutdownHook shutdownHook, Injector injector);

  abstract int serverPort();

}