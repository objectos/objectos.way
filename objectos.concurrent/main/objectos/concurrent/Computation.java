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

import java.util.concurrent.ExecutionException;

/**
 * A non-blocking asynchronous computation.
 *
 * @param <V>
 *        the type of the result of this computation
 *
 * @since 3
 */
public interface Computation<V> {

  /**
   * Returns the result of this computation if it is available; throws an
   * exception otherwise. Implementations must never block if a result is not
   * available.
   *
   * <p>
   * As this method should never block, clients should call {@link #isActive()}
   * prior to invoking this method. In other words, this method should only be
   * called after {@link #isActive()} returns false.
   *
   * @return the computed result
   *
   * @throws IllegalStateException
   *         if this computation is still active
   * @throws ExecutionException
   *         if this computation completed exceptionally
   */
  V getResult() throws IllegalStateException, ExecutionException;

  /**
   * Returns {@code true} if this computation is still running and is not yet
   * complete.
   *
   * @return {@code true} if this computation is still running;
   *         {@code false} otherwise.
   */
  boolean isActive();

}