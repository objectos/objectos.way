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

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import objectos.core.service.Services;
import objectos.notes.NoOpNoteSink;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FixedCpuArrayTest {

  private FixedCpuArray array;

  @BeforeClass
  public void beforeClass() throws Exception {
    array = new FixedCpuArray(100, 100, NoOpNoteSink.of());

    Services.start(
        array
    );
  }

  @Test
  public void get() {
    int size;
    size = array.size();

    for (int i = 0; i < size; i++) {
      FixedCpuWorker w;
      w = array.get(i);

      assertNotNull(w);
    }
  }

  @Test
  public void size() {
    assertTrue(array.size() > 1);
  }

}