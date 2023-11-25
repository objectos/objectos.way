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

/**
 * An {@link CpuWorker} that immediately executes any offered task.
 *
 * @since 2
 */
public final class DirectCpuWorker implements CpuWorker {

  private static final DirectCpuWorker INSTANCE = new DirectCpuWorker();

  private DirectCpuWorker() {}

  /**
   * Returns the only instance of this class.
   *
   * @return the only instance of this class
   */
  public static DirectCpuWorker get() {
    return INSTANCE;
  }

  /**
   * Immediatly executes the specified task to its completion.
   *
   * @return {@code true} always
   */
  @Override
  public final boolean offer(CpuTask task) {
    Concurrent.exhaust(task);

    return true;
  }

}