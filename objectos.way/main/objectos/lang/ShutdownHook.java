/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.lang;

import java.util.List;
import objectos.util.GrowableList;
import objectox.lang.Check;
import objectox.lang.NoOpNoteSink;
import objectox.lang.Note0;
import objectox.lang.Note1;
import objectox.lang.NoteSink;

/**
 * An utility for registering objects with the JVM shutdown hook facility.
 */
public final class ShutdownHook {

  public interface Listener {

    void onShutdownHook() throws Exception;

  }

  private static final Note1<Throwable> CAUGHT_EXCEPTION = Note1.warn();

  private static final Note1<Long> FINISHED = Note1.info();

  private static final Note0 STARTED = Note0.info();

  private NoteSink noteSink = NoOpNoteSink.getInstance();

  private Job job;

  private List<Object> hooks;

  private ShutdownHook() {}

  public static ShutdownHook of() {
    return ShutdownHookLazy.INSTANCE;
  }

  private static class ShutdownHookLazy {
    static ShutdownHook INSTANCE = create();

    private static ShutdownHook create() {
      ShutdownHook shutdownHook;
      shutdownHook = new ShutdownHook();

      shutdownHook.register();

      return shutdownHook;
    }
  }

  public final void addAutoCloseable(AutoCloseable closeable) {
    Check.notNull(closeable, "closeable == null");

    addHook(closeable);
  }

  public final void addListener(ShutdownHook.Listener listener) {
    Check.notNull(listener, "listener == null");

    addHook(listener);
  }

  public final void addThread(Thread thread) {
    Check.notNull(thread, "thread == null");

    addHook(thread);
  }

  private void addHook(Object hook) {
    if (hooks == null) {
      synchronized (this) {
        if (hooks == null) {
          hooks = new GrowableList<>();
        }
      }
    }

    synchronized (hooks) {
      hooks.add(hook);
    }
  }

  public synchronized final void noteSink(NoteSink sink) {
    this.noteSink = Check.notNull(sink, "sink == null");
  }

  final void log(Throwable e) {
    noteSink.send(CAUGHT_EXCEPTION, e);
  }

  // visible for testing
  final Thread startAndJoinThread() throws InterruptedException {
    job.start();

    job.join();

    return job;
  }

  private void register() {
    job = new Job();

    job.setDaemon(true);

    Runtime runtime;
    runtime = Runtime.getRuntime();

    runtime.addShutdownHook(job);
  }

  private class Job extends Thread {
    Job() {
      super("ShutdownHook");
    }

    @Override
    public final void run() {
      long startTime;
      startTime = System.currentTimeMillis();

      noteSink.send(STARTED);

      if (hooks != null) {
        doHooks();
      }

      long totalTime;
      totalTime = System.currentTimeMillis() - startTime;

      noteSink.send(FINISHED, totalTime);
    }

    private void doHooks() {
      for (int i = 0, size = hooks.size(); i < size; i++) {
        Object hook;
        hook = hooks.get(i);

        try {

          if (hook instanceof AutoCloseable c) {
            c.close();
          }

          else if (hook instanceof ShutdownHook.Listener l) {
            l.onShutdownHook();
          }

          else if (hook instanceof Thread t) {
            t.interrupt();
          }

          else {
            throw new AssertionError("Unknown hook type=" + hook.getClass());
          }

        } catch (Throwable t) {
          log(t);
        }
      }
    }
  }

}
