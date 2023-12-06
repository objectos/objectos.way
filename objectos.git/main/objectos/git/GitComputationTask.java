/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import java.util.concurrent.ExecutionException;
import objectos.concurrent.Computation;
import objectos.concurrent.CpuTask;
import objectos.concurrent.CpuWorker;

final class GitComputationTask<V> implements Computation<V>, CpuTask {

  private static final byte _EXECUTE = 1;

  private static final byte _OFFER = 2;

  private static final byte _STOP = 0;

  private byte state = _OFFER;

  private final GitTask<V> task;

  private final CpuWorker worker;

  GitComputationTask(CpuWorker worker, GitTask<V> task) {
    this.worker = worker;

    this.task = task;
  }

  @Override
  public final void executeOne() {
    switch (state) {
      case _EXECUTE:
        if (!task.isActive()) {
          state = _STOP;
        }
        break;
      case _OFFER:
        if (worker.offer(task)) {
          state = _EXECUTE;
        }
        break;
      case _STOP:
        throw new IllegalStateException("stopped");
    }
  }

  @Override
  public final V getResult() throws IllegalStateException, ExecutionException {
    return task.getResult();
  }

  @Override
  public final boolean isActive() {
    return state != _STOP;
  }

}