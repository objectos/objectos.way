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

import objectos.notes.Note0;
import objectos.notes.NoteSink;

/**
 * A {@link CpuArray} implementation consisting of {@link FixedCpuWorker}
 * instances.
 *
 * @since 3
 */
public final class FixedCpuArray extends CpuArrayService {

  private static final Note0 STARTED;

  static {
    Class<?> source;
    source = FixedCpuArray.class;

    STARTED = Note0.info(source, "Started");
  }

  private final FixedCpuWorker[] workers;

  /**
   * Creates a new array instance with a size equal to the number of processors
   * available to the JVM.
   *
   * @param activeCount
   *        the maximum number of tasks that can be run concurrently in each
   *        worker
   * @param queueCapacity
   *        the bounded queue capacity for each worker
   * @param logger
   *        the logger to be used by this array and by each worker
   *
   * @see FixedCpuWorker#FixedCpuWorker(int, int, NoteSink)
   * @see Runtime#availableProcessors()
   */
  public FixedCpuArray(int activeCount, int queueCapacity, NoteSink logger) {
    Runtime runtime;
    runtime = Runtime.getRuntime();

    int availableProcessors;
    availableProcessors = runtime.availableProcessors();

    FixedCpuWorker[] a;
    a = new FixedCpuWorker[availableProcessors];

    for (int i = 0; i < a.length; i++) {
      a[i] = new FixedCpuWorker(activeCount, queueCapacity, logger);
    }

    workers = a;

    this.logger = logger;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final FixedCpuWorker get(int index) {
    return workers[index];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int size() {
    return workers.length;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void startService() {
    for (FixedCpuWorker worker : workers) {
      worker.startService();
    }

    logger.send(STARTED);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void stopService() {
    for (FixedCpuWorker worker : workers) {
      try {
        worker.stopService();
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  final boolean isStarted() {
    return workers[0].isStarted();
  }

}