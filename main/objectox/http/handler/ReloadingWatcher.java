/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.handler;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

final class ReloadingWatcher implements Closeable {

  private final ReloadingWatcherAdapter adapter;

  private final Lock lock = new ReentrantLock();

  ReloadingWatcher(ReloadingWatcherAdapter adapter) {
    this.adapter = adapter;
  }

  @Override
  public final void close() throws IOException {
    adapter.close();
  }

  public final <T> T getIf(Supplier<T> supplier, T defaultValue) {
    lock.lock();
    try {
      final boolean changed;
      changed = adapter.changed();

      return changed ? supplier.get() : defaultValue;
    } finally {
      lock.unlock();
    }
  }

}
