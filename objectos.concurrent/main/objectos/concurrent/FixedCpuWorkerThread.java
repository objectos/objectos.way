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

import objectos.util.array.IntArrays;

final class FixedCpuWorkerThread extends Thread {

  private final FixedCpuWorkerThreadAdapter adapter;

  private int[] slots = new int[10];

  private int slotsHead;

  private int slotsTail;

  FixedCpuWorkerThread(FixedCpuWorkerThreadAdapter adapter) {
    this.adapter = adapter;
  }

  @Override
  public final void run() {
    adapter.log(FixedCpuWorker.STARTED, getName());

    while (!adapter.interrupted()) {
      try {
        run0();
      } catch (InterruptedException e) {
        return;
      }
    }
  }

  final void run0() throws InterruptedException {
    CpuTask t;
    t = adapter.take();

    adapter.log(FixedCpuWorker.UNPARKED);

    adapter.assertNull(0);

    adapter.set(0, t);

    adapter.log(FixedCpuWorker.JOB_STARTED, t);

    int count = 1;

    while (count > 0) {
      int index = 0;

      int limit = count;

      slotsReset();

      while (index < limit) {
        t = adapter.get(index);

        boolean remove = true;

        if (t.isActive()) {
          try {
            t.executeOne();

            remove = false;

            if (hasSlot()) {
              int slot = getSlot();

              adapter.set(index, null);

              adapter.set(slot, t);

              addSlot(index);
            }
          } catch (Throwable e) {
            adapter.log(FixedCpuWorker.JOB_EXCEPTION, e);
          }
        } else {
          adapter.log(FixedCpuWorker.JOB_COMPLETED, t);
        }

        if (remove) {
          count--;

          adapter.set(index, null);

          addSlot(index);
        }

        index++;
      }

      while (adapter.hasTask() && adapter.hasSlot(count)) {
        t = adapter.poll();

        adapter.set(count, t);

        adapter.log(FixedCpuWorker.JOB_STARTED, t);

        count++;
      }

      boolean wasInterrupted;
      wasInterrupted = adapter.interrupted();

      if (wasInterrupted && adapter.shutdown()) {
        return;
      }

      else if (wasInterrupted) {
        adapter.log(FixedCpuWorker.INTERRUPTED, t);
      }
    }
  }

  private void addSlot(int value) {
    slots = IntArrays.growIfNecessary(slots, slotsTail);

    slots[slotsTail++] = value;
  }

  private int getSlot() {
    return slots[slotsHead++];
  }

  private boolean hasSlot() {
    return slotsTail - slotsHead > 0;
  }

  private void slotsReset() {
    slotsHead = 0;

    slotsTail = 0;
  }

}