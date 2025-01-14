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
package objectos.way;

import java.util.List;

final class AppShutdownHook implements App.ShutdownHook {

  record Notes(
      Note.Ref1<Object> registered,
      Note.Ref1<Object> ignored,

      Note.Ref0 started,
      Note.Ref1<Object> resourceStarted,
      Note.Ref2<Object, Throwable> resourceError,
      Note.Long1 totalTime
  ) implements App.ShutdownHook.Notes {

    static Notes get() {
      Class<?> s;
      s = App.ShutdownHook.class;

      return new Notes(
          Note.Ref1.create(s, "Registered", Note.INFO),
          Note.Ref1.create(s, "Ignored", Note.INFO),

          Note.Ref0.create(s, "Started", Note.INFO),
          Note.Ref1.create(s, "Resource started", Note.DEBUG),
          Note.Ref2.create(s, "Resource error", Note.WARN),
          Note.Long1.create(s, "Total time [ms]", Note.INFO)
      );
    }

  }

  private final Notes notes = Notes.get();

  private final List<Object> hooks = Util.createList();

  private final Job job;

  private final Note.Sink noteSink;

  AppShutdownHook(Note.Sink noteSink) {
    this.noteSink = noteSink;

    job = new Job();

    job.setDaemon(true);

    Runtime runtime;
    runtime = Runtime.getRuntime();

    runtime.addShutdownHook(job);
  }

  @Override
  public final void register(AutoCloseable closeable) {
    Check.notNull(closeable, "closeable == null");

    addHook(closeable);
  }

  @Override
  public final void registerIfPossible(Object resource) {
    Check.notNull(resource, "resource == null");

    if (resource instanceof AutoCloseable) {
      addHook(resource);
    } else if (resource instanceof Thread) {
      addHook(resource);
    } else {
      noteSink.send(notes.ignored, resource);
    }
  }

  @Override
  public final void registerThread(Thread thread) {
    Check.notNull(thread, "thread == null");

    addHook(thread);
  }

  private void addHook(Object hook) {
    noteSink.send(notes.registered, hook);

    hooks.add(hook);
  }

  // visible for testing
  final Thread startAndJoinThread() throws InterruptedException {
    job.start();

    job.join();

    return job;
  }

  private class Job extends Thread {
    Job() {
      super("ShutdownHook");
    }

    @Override
    public final void run() {
      long startTime;
      startTime = System.currentTimeMillis();

      noteSink.send(notes.started);

      if (hooks != null) {
        doHooks();
      }

      long totalTime;
      totalTime = System.currentTimeMillis() - startTime;

      noteSink.send(notes.totalTime, totalTime);
    }

    private void doHooks() {
      for (int i = 0, size = hooks.size(); i < size; i++) {
        Object hook;
        hook = hooks.get(i);

        noteSink.send(notes.resourceStarted, hook);

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
          noteSink.send(notes.resourceError, hook, t);
        }
      }
    }
  }

}