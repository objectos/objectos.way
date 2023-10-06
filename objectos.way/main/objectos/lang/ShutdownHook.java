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

  private List<AutoCloseable> autoCloseables;

  private List<Listener> listeners;

  private List<Thread> threads;

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

    if (autoCloseables == null) {
      synchronized (this) {
        if (autoCloseables == null) {
          autoCloseables = new GrowableList<>();
        }
      }
    }

    synchronized (autoCloseables) {
      autoCloseables.add(closeable);
    }
  }
  public final void addListener(ShutdownHook.Listener listener) {
    Check.notNull(listener, "listener == null");

    if (listeners == null) {
      synchronized (this) {
        if (listeners == null) {
          listeners = new GrowableList<>();
        }
      }
    }

    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  public final void addThread(Thread thread) {
    Check.notNull(thread, "thread == null");

    if (threads == null) {
      synchronized (this) {
        if (threads == null) {
          threads = new GrowableList<>();
        }
      }
    }

    synchronized (threads) {
      threads.add(thread);
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

      if (autoCloseables != null) {
        doCloseables();
      }

      if (listeners != null) {
        doListeners();
      }

      if (threads != null) {
        doThreads();
      }

      long totalTime;
      totalTime = System.currentTimeMillis() - startTime;

      noteSink.send(FINISHED, totalTime);
    }

    private void doCloseables() {
      for (int i = 0; i < autoCloseables.size(); i++) {
        AutoCloseable c;
        c = autoCloseables.get(i);

        try {
          c.close();
        } catch (Exception e) {
          log(e);
        }
      }
    }

    private void doListeners() {
      for (int i = listeners.size() - 1; i >= 0; i--) {
        ShutdownHook.Listener l;
        l = listeners.get(i);

        try {
          l.onShutdownHook();
        } catch (Exception e) {
          log(e);
        }
      }
    }

    private void doThreads() {
      for (int i = threads.size() - 1; i >= 0; i--) {
        Thread t;
        t = threads.get(i);

        try {
          t.interrupt();
        } catch (Exception e) {
          log(e);
        }
      }
    }
  }

}
