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
package objectox.html.play;

import static org.testng.Assert.assertEquals;

import objectox.html.IntSize;
import org.testng.annotations.Test;

public class ValuesTest {

  @Test(description = "hasNext => false")
  public void hasNext01() {
    final Values values;
    values = Values.of(0, IntSize.BIT8, 0);

    assertEquals(values.hasNext(0), false);
  }

  @Test(description = "hasNext => true")
  public void hasNext02() {
    final Values values;
    values = Values.of(0, IntSize.BIT8, 2);

    assertEquals(values.hasNext(0), true);
    assertEquals(values.hasNext(1), true);
    assertEquals(values.hasNext(2), false);
  }

}
