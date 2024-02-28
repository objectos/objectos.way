/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public final class TestingRandom {

  private TestingRandom() {}

  public static final class SequentialRandom extends Random {

    private static final long serialVersionUID = 1L;

    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public final void nextBytes(byte[] bytes) {
      Arrays.fill(bytes, (byte) 0);

      int value;
      value = counter.getAndIncrement();

      for (int i = 0; i < 4; i++) {
        int offset;
        offset = bytes.length - i - 1;

        if (offset < 0) {
          return;
        }

        bytes[offset] = (byte) value;

        value = value >>> 8;
      }
    }

    public final void reset() {
      counter.set(1);
    }

  }

}