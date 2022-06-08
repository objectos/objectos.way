/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.lang;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.io.IOException;
import org.testng.annotations.Test;

public class SuppressedTest {

  @Test
  public void addIfPossible() {
    Throwable rethrow = null;

    Throwable a = new IOException();

    Throwable b = new NullPointerException();

    Throwable c = new IllegalArgumentException();

    rethrow = Suppressed.addIfPossible(rethrow, a);
    var suppressed = rethrow.getSuppressed();

    assertSame(rethrow, a);
    assertEquals(suppressed.length, 0);

    rethrow = Suppressed.addIfPossible(rethrow, b);
    suppressed = rethrow.getSuppressed();

    assertSame(rethrow, a);
    assertEquals(suppressed.length, 1);
    assertSame(suppressed[0], b);

    rethrow = Suppressed.addIfPossible(rethrow, c);
    suppressed = rethrow.getSuppressed();

    assertSame(rethrow, a);
    assertEquals(suppressed.length, 2);
    assertSame(suppressed[0], b);
    assertSame(suppressed[1], c);
  }

}
