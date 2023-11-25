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
 * An array of {@link CpuWorker} instances.
 *
 * Implementations must ensure that instances contain at least one worker.
 *
 * @since 3
 */
public interface CpuArray {

  /**
   * Returns the worker at the specified array index.
   *
   * @param index
   *        the index of the worker to be returned
   *
   * @return the worker at the specified array index
   *
   * @throws ArrayIndexOutOfBoundsException
   *         if the index is invalid
   */
  CpuWorker get(int index);

  /**
   * Returns the number of workers in this array.
   *
   * @return the number of workers in this array; never smaller than one
   */
  int size();

}