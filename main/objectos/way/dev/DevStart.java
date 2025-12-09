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

import module java.base;
import module objectos.way;

/// This class is not part of the Objectos Way JAR file.
/// It is placed in the main source tree to ease the development.
public final class DevStart extends App.Bootstrap {

  public static final int TESTING_HTTP_PORT = 8003;

  public static void main(String[] args) {
    final DevStart start;
    start = new DevStart();

    start.start(args);
  }

  @Override
  protected final void bootstrap() {
    final long startTime;
    startTime = System.currentTimeMillis();

    final App.Injector injector;
    injector = App.Injector.create(this::injector);

    final Note.Sink noteSink;
    noteSink = injector.getInstance(Note.Sink.class);

    final App.ShutdownHook shutdownHook;
    shutdownHook = injector.getInstance(App.ShutdownHook.class);

    // Http.Handler
    final Http.Handler serverHandler;
    serverHandler = serverHandler(injector);

    shutdownHook.registerIfPossible(serverHandler);

    // Http.Server
    try {
      final Http.Server httpServer;
      httpServer = Http.Server.create(opts -> {
        opts.bufferSize(8192, 8192);

        opts.handler(serverHandler);

        opts.noteSink(noteSink);

        opts.port(TESTING_HTTP_PORT);
      });

      shutdownHook.register(httpServer);

      httpServer.start();
    } catch (IOException e) {
      throw App.serviceFailed("Http.Server", e);
    }

    // bootstrap end event
    final Note.Long1 totalTimeNote;
    totalTimeNote = Note.Long1.create(getClass(), "BT1", Note.INFO);

    final long totalTime;
    totalTime = System.currentTimeMillis() - startTime;

    noteSink.send(totalTimeNote, totalTime);
  }

  private void injector(App.Injector.Options ctx) {
    // Note.Sink
    final Note.Sink noteSink;
    noteSink = App.NoteSink.ofAppendable(System.out, opts -> {
      opts.filter(note -> {
        final String source;
        source = note.source();

        if (source.startsWith("objectos.way")) {
          return true;
        }

        return note.hasAny(Note.ERROR, Note.WARN, Note.INFO);
      });
    });

    ctx.putInstance(Note.Sink.class, noteSink);

    // bootstrap start event
    final Note.Ref0 startNote;
    startNote = Note.Ref0.create(getClass(), "BT0", Note.INFO);

    noteSink.send(startNote);

    // App.ShutdownHook
    final App.ShutdownHook shutdownHook;
    shutdownHook = App.ShutdownHook.create(opts -> {
      opts.noteSink(noteSink);
    });

    shutdownHook.registerIfPossible(noteSink);

    ctx.putInstance(App.ShutdownHook.class, shutdownHook);
  }

  private record Reloader(App.Injector injector) implements App.Reloader.HandlerFactory {
    @Override
    public final Http.Handler reload(ClassLoader loader) throws Exception {
      final Class<?> bootClass;
      bootClass = loader.loadClass("objectos.way.DevBoot");

      final Module reloaded;
      reloaded = bootClass.getModule();

      final Method method;
      method = bootClass.getMethod("boot", App.Injector.class, Module.class);

      final Class<? extends Reloader> self;
      self = getClass();

      final Module original;
      original = self.getModule();

      original.addReads(reloaded);

      original.addExports("objectos.way.dev", reloaded);

      final Object instance;
      instance = method.invoke(null, injector, original);

      return (Http.Handler) instance;
    }
  }

  private Http.Handler serverHandler(App.Injector injector) {
    try {
      return App.Reloader.create(opts -> {
        final Reloader reloader;
        reloader = new Reloader(injector);

        opts.handlerFactory(reloader);

        opts.moduleOf(DevStart.class);

        final Note.Sink noteSink;
        noteSink = injector.getInstance(Note.Sink.class);

        opts.noteSink(noteSink);
      });
    } catch (IOException e) {
      throw App.serviceFailed("App.Reloader", e);
    }
  }

}
