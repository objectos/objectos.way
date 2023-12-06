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

/**
 * Submits git commands to be executed asynchronously.
 *
 * @since 3
 */
public interface GitExecutor {

  /**
   * Submits the specified command to be executed asynchronously.
   *
   * @param <V>
   *        the type of the command result
   * @param command
   *        the command to be executed
   *
   * @return a {@code GitComputation} representing the possibly ongoing
   *         execution of the command
   */
  <V> Computation<V> submit(GitCommand<V> command);

}