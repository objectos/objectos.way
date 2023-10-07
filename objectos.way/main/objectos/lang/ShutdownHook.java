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

import java.lang.System.Logger.Level;
import java.util.List;
import objectos.util.GrowableList;
import objectox.lang.Check;

/**
 * An utility for registering objects with the JVM shutdown hook facility.
 *
 * @see Runtime#addShutdownHook(Thread)
 */
public final class ShutdownHook {

  /**
   * An object that gets notified when the {@code ShutdownHook} is run.
   */
  public interface Listener {

    /**
     * Called once by the {@code ShutdownHook} on the JVM shutdown.
     *
     * @throws Exception
     *         if this hook cannot be executed normally
     */
    void onShutdownHook() throws Exception;

  }

  private System.Logger logger = System.getLogger(ShutdownHook.class.getName());

  private Job job;

  private List<Object> hooks;

  private ShutdownHook() {}

  /**
   * Returns the {@code ShutdownHook} singleton.
   *
   * @return the {@code ShutdownHook} singleton
   */
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

  /**
   * Closes the specified {@link AutoCloseable} instance when this shutdown
   * hook runs.
   *
   * <p>
   * In other words, the closeable {@link AutoCloseable#close()} method is
   * called by the shutdown hook when the latter runs.
   *
   * @param closeable
   *        the auto closeable instance to be closed
   */
  public final void addAutoCloseable(AutoCloseable closeable) {
    Check.notNull(closeable, "closeable == null");

    addHook(closeable);
  }

  /**
   * Notifies the specified {@link ShutdownHook.Listener} instance when this
   * shutdown hook runs.
   *
   * <p>
   * In other words, the listener {@link ShutdownHook.Listener#onShutdownHook()}
   * method is called by the shutdown hook when the latter runs.
   *
   * @param listener
   *        the listener instance to be notified
   */
  public final void addListener(ShutdownHook.Listener listener) {
    Check.notNull(listener, "listener == null");

    addHook(listener);
  }

  /**
   * Interrupts the specified {@link Thread} instance when this shutdown hook
   * runs.
   *
   * <p>
   * In other words, the thread {@link Thread#interrupt()} is called by the
   * shutdown hook when the latter runs.
   *
   * @param thread
   *        the thread instance to be interrupted
   */
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

  // visible for testing
  final void logger(System.Logger logger) {
    this.logger = Check.notNull(logger, "logger == null");
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

      logger.log(Level.INFO, "Started");

      if (hooks != null) {
        doHooks();
      }

      long totalTime;
      totalTime = System.currentTimeMillis() - startTime;

      logger.log(Level.INFO, "Finished in %d ms", totalTime);
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
          logger.log(Level.WARNING, () -> "Failed to run hook " + hook, t);
        }
      }
    }
  }

}
