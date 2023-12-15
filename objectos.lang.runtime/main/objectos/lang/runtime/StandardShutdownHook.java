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
package objectos.lang.runtime;

import java.util.List;
import objectos.lang.object.Check;
import objectos.notes.LongNote;
import objectos.notes.NoOpNoteSink;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.NoteSink;
import objectos.util.list.GrowableList;

final class StandardShutdownHook implements ShutdownHook {

  static final Note1<Object> REGISTRATION;

  static {
    Class<?> source;
    source = ShutdownHook.class;

    REGISTRATION = Note1.info(source, "Registration [hook]");
  }

  private NoteSink noteSink = NoOpNoteSink.of();

  private Job job;

  private final List<Object> hooks = new GrowableList<>();

  StandardShutdownHook() {}

  @Override
  public final void addAutoCloseable(AutoCloseable closeable) {
    Check.notNull(closeable, "closeable == null");

    addHook(closeable);
  }

  @Override
  public final void addThread(Thread thread) {
    Check.notNull(thread, "thread == null");

    addHook(thread);
  }

  final void noteSink(NoteSink sink) {
    noteSink = Check.notNull(sink, "sink == null");
  }

  private void addHook(Object hook) {
    synchronized (hooks) {
      noteSink.send(REGISTRATION, hook);

      hooks.add(hook);
    }
  }

  // visible for testing
  final Thread startAndJoinThread() throws InterruptedException {
    job.start();

    job.join();

    return job;
  }

  final ShutdownHook register() {
    job = new Job();

    job.setDaemon(true);

    Runtime runtime;
    runtime = Runtime.getRuntime();

    runtime.addShutdownHook(job);

    return this;
  }

  /*
   * Note objects will be used only during JVM shutdown.
   * So we create them lazily during Job execution.
   */
  static class Events {

    static final Note0 START;

    static final Note1<Object> EXECUTION_START;

    static final Note2<Object, Throwable> EXECUTION_ERROR;

    static final LongNote FINISH;

    static {
      Class<?> s;
      s = StandardShutdownHook.class;

      START = Note0.info(s, "Start");

      EXECUTION_START = Note1.debug(s, "Execution start [hook]");

      EXECUTION_ERROR = Note2.warn(s, "Execution error [hook] [exception]");

      FINISH = LongNote.info(s, "Finish [Total time in ms]");
    }

  }

  private class Job extends Thread {
    Job() {
      super("ShutdownHook");
    }

    @Override
    public final void run() {
      long startTime;
      startTime = System.currentTimeMillis();

      noteSink.send(Events.START);

      if (hooks != null) {
        doHooks();
      }

      long totalTime;
      totalTime = System.currentTimeMillis() - startTime;

      noteSink.send(Events.FINISH, totalTime);
    }

    private void doHooks() {
      for (int i = 0, size = hooks.size(); i < size; i++) {
        Object hook;
        hook = hooks.get(i);

        noteSink.send(Events.EXECUTION_START, hook);

        try {

          if (hook instanceof AutoCloseable c) {
            c.close();
          }

          else if (hook instanceof Thread t) {
            t.interrupt();
          }

          else {
            throw new AssertionError("Unknown hook type=" + hook.getClass());
          }

        } catch (Throwable t) {
          noteSink.send(Events.EXECUTION_ERROR, hook, t);
        }
      }
    }
  }

}
