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
package objectox.http.handler;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;
import objectos.http.Handler;
import objectos.http.ReloadingHandler;
import objectos.http.Request;
import objectos.http.Result;
import objectos.way.Note;

public final class ReloadingHandlerPojo implements ReloadingHandler {

  private static final Note.Ref1<Throwable> THROW = Note.Ref1.create(ReloadingHandlerPojo.class, "THR", Note.ERROR);

  private final Predicate<? super String> binaryNameFilter;

  private final Predicate<? super byte[]> classFileFilter;

  private volatile Handler handler;

  private final Note.Sink noteSink;

  private final Function<? super ClassLoader, ? extends Handler> reloadingFunction;

  private final ReloadingModule reloadingModule;

  private final ReloadingWatcher reloadingWatcher;

  ReloadingHandlerPojo(
      Predicate<? super String> binaryNameFilter,

      Predicate<? super byte[]> classFileFilter,

      Note.Sink noteSink,

      Function<? super ClassLoader, ? extends Handler> reloadingFunction,

      ReloadingModule reloadingModule,

      ReloadingWatcher reloadingWatcher
  ) {
    this.binaryNameFilter = binaryNameFilter;

    this.classFileFilter = classFileFilter;

    this.noteSink = noteSink;

    this.reloadingFunction = reloadingFunction;

    this.reloadingModule = reloadingModule;

    this.reloadingWatcher = reloadingWatcher;
  }

  @Override
  public final void close() throws IOException {
    reloadingWatcher.close();
  }

  @Override
  public final Result handle(Request req) {
    final Handler h;
    h = handler = createOrReload();

    return h.handle(req);
  }

  private Handler createOrReload() {
    final Handler h;
    h = handler;

    if (h == null) {
      return create();
    } else {
      return reloadingWatcher.getIf(this::create, h);
    }
  }

  private Handler create() {
    final ClassLoader loader;
    loader = reloadingModule.findLoader(binaryNameFilter, classFileFilter, noteSink);

    final Handler instance;

    try {
      instance = reloadingFunction.apply(loader);
    } catch (Throwable e) {
      noteSink.send(THROW, e);

      return HandlerNoop.INSTANCE;
    }

    if (instance == null) {
      Throwable t;
      t = new NullPointerException("Reloader returned a null handler");

      noteSink.send(THROW, t);

      return HandlerNoop.INSTANCE;
    }

    return instance;
  }

}