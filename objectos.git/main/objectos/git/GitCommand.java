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

import objectos.concurrent.Computation;
import objectos.concurrent.CpuWorker;

/**
 * Represents a result producing high-level (porcelain) Git command. More
 * precisely, it acts as a factory of asynchronous git computations.
 *
 * <p>
 * Instances of this interface are intended to be executed by a
 * {@link GitService} instance.
 *
 * <p>
 * Programmers are encouraged to use one of the abstract classes provided by
 * this package instead of implementing this interface directly.
 *
 * @param <V>
 *        the type of the result of this command
 *
 * @since 3
 */
public interface GitCommand<V> {

  /**
   * Returns a possibly new and possibly asynchronous git computation
   * representing the pending result of this command.
   *
   * <p>
   * Please
   *
   * @param executor
   *        the executor providing low-level git tasks
   * @param cpuWorker
   *        the cpu worker to execute the command
   *
   * @return a computation object representing the pending result of this
   *         command
   */
  Computation<V> submitAsync(GitCommandExecutor executor, CpuWorker cpuWorker);

}