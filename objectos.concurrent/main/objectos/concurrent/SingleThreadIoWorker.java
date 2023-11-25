/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import objectos.lang.object.Check;
import objectos.notes.Note1;
import objectos.notes.NoteSink;

/**
 * A service that uses a single thread for executing I/O tasks operating off an
 * unbounded queue.
 *
 * @since 2
 */
public final class SingleThreadIoWorker extends IoWorkerService {

  private static final Note1<Throwable> ETASK_ERROR;

  private static final Note1<IoTask> ETASK_INTERRUPTED;

  static {
    Class<?> source;
    source = SingleThreadIoWorker.class;

    ETASK_ERROR = Note1.error(source, "Task error");

    ETASK_INTERRUPTED = Note1.trace(source, "Task interrupted");
  }

  private final BlockingQueue<IoTask> queue;

  private final Worker worker;

  /**
   * Creates new single-threaded I/O executor service with the specified logger.
   *
   * @param logger
   *        a logger instance
   */
  public SingleThreadIoWorker(NoteSink logger) {
    this.logger = Check.notNull(logger, "logger == null");

    queue = new LinkedBlockingDeque<IoTask>();

    String workerName;
    workerName = Concurrent.nextIoName();

    worker = new Worker(workerName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean cancelOrInterrupt(IoTask task) {
    Check.notNull(task, "task == null");

    if (task == worker.currentTask) {
      worker.interrupt();

      return true;
    }

    if (queue.remove(task)) {
      return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void startService() {
    worker.start();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void stopService() {
    worker.shutdown();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void submit(IoTask task) {
    Check.notNull(task, "task == null");

    queue.add(task);
  }

  @Override
  final boolean isStarted() {
    return worker.isAlive();
  }

  private class Worker extends Thread {

    private IoTask currentTask;

    private boolean shutdown;

    Worker(String name) {
      super(name);
    }

    @Override
    public final void run() {
      while (true) {
        try {
          currentTask = queue.take();
        } catch (InterruptedException e) {
          return;
        }

        try {
          currentTask.executeIo();
        } catch (Throwable t) {
          logger.send(ETASK_ERROR, t);
        }

        boolean wasInterrupted;
        wasInterrupted = Thread.interrupted();

        if (wasInterrupted && shutdown) {
          return;
        }

        else if (wasInterrupted) {
          logger.send(ETASK_INTERRUPTED, currentTask);
        }

        currentTask = null;
      }
    }

    final void shutdown() {
      shutdown = true;

      interrupt();
    }

  }

}