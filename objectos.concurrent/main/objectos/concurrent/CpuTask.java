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
 * A CPU bound task that must run in a single thread.
 *
 * <p>
 * Instances of this interface are designed to run concurrently in a cooperative
 * manner in a single thread. To work correctly, implementations must:
 *
 * <ul>
 * <li>break down the total work of an individual task in units; and</li>
 * <li>delegate any I/O operation to an {@link IoWorker}.</li>
 * </ul>
 *
 * @since 2
 */
public interface CpuTask {

  /**
   * Executes one unit of this task's work.
   */
  void executeOne();

  /**
   * Returns {@code true} if there are more units of work to be completed by
   * this task.
   *
   * @return {@code true} if there are more units of work to be completed by
   *         this task
   */
  boolean isActive();

}