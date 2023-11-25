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

import objectos.lang.object.ToString;

final class CountingCpuTask implements CpuTask, ToString.Formattable {

  private volatile boolean active;

  private volatile int counter;

  @Override
  public final void executeOne() {
    counter++;
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "active", Boolean.toString(active),
        "counter", Integer.toString(counter)
    );
  }

  public final int get() {
    return counter;
  }

  @Override
  public final boolean isActive() {
    return active;
  }

  public final void reset() {
    active = true;

    counter = 0;
  }

  public final void signalStop() {
    active = false;
  }

  @Override
  public final String toString() {
    return ToString.of(this);
  }

}