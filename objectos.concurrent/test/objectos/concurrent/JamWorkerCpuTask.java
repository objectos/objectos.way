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

final class JamWorkerCpuTask implements CpuTask {

  private boolean active = true;

  private volatile int counter;

  private boolean jam = false;

  @Override
  public final void executeOne() {
    if (jam) {
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      } finally {
        active = false;
      }
    }

    counter++;
  }

  public final int get() {
    return counter;
  }

  @Override
  public final boolean isActive() {
    return active;
  }

  public final void jam() {
    jam = true;
  }

  public final void reset() {
    active = true;

    jam = false;

    counter = 0;
  }

}