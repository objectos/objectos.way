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
 * An {@link IoWorker} that immediately executes the submitted task.
 *
 * @since 2
 */
public final class DirectIoWorker implements IoWorker {

  private static final DirectIoWorker INSTANCE = new DirectIoWorker();

  private DirectIoWorker() {}

  /**
   * Returns the only instance of this class.
   *
   * @return the only instance of this class
   */
  public static DirectIoWorker get() {
    return INSTANCE;
  }

  /**
   * Does not do anything.
   *
   * @return {@code false} always
   */
  @Override
  public final boolean cancelOrInterrupt(IoTask task) {
    return false;
  }

  /**
   * Immediately executes the specified task.
   *
   * @param task
   *        the I/O task to execute
   */
  @Override
  public final void submit(IoTask task) {
    task.executeIo();
  }

}