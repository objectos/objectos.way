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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import org.testng.annotations.Test;

public class ReloadingWatcherTest {

  private static final class Adapter implements ReloadingWatcherAdapter {

    private boolean closed;

    private final boolean value;

    Adapter(boolean value) {
      this.value = value;
    }

    @Override
    public final void close() {
      closed = true;
    }

    @Override
    public final boolean changed() {
      return value;
    }

  }

  @Test
  public void getIf01() throws IOException {
    final Adapter adapter;
    adapter = new Adapter(true);

    try (var watcher = new ReloadingWatcher(adapter)) {
      assertEquals(watcher.getIf(() -> "supplier", "default"), "supplier");
    }

    assertEquals(adapter.closed, true);
  }

  @Test
  public void getIf02() throws IOException {
    final Adapter adapter;
    adapter = new Adapter(false);

    try (var watcher = new ReloadingWatcher(adapter)) {
      assertEquals(watcher.getIf(() -> "supplier", "default"), "default");
    }

    assertEquals(adapter.closed, true);
  }

}
