/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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
package objectox.html.format;

import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;

import objectos.html.format.Formatter;
import org.testng.annotations.Test;

public class FormatterTest {

  @Test(description = "testable returns a new TestableFormatter on each call")
  public void testable01() {
    final Formatter first;
    first = Formatter.testable();

    final Formatter second;
    second = Formatter.testable();

    assertTrue(first instanceof TestableFormatter);

    assertTrue(second instanceof TestableFormatter);

    assertNotSame(first, second);
  }

}
