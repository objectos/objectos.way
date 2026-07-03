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
package objectox.dev;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.function.BiConsumer;
import objectos.dev.TestableRowFormatter;
import org.testng.annotations.Test;

public class TestableTableTest {

  private <T> String table(Iterable<? extends T> elements, BiConsumer<TestableRowFormatter, T> format) {
    final TestableTable<T> subject;
    subject = new TestableTable<>(elements, format);

    return subject.toString();
  }

  @Test(
      description = "reject null elements",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "elements == null")
  public void format01() {
    table(null, (_, _) -> {});
  }

  @Test(
      description = "reject null format",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "format == null")
  public void format02() {
    table(List.of(), null);
  }

  @Test(description = "empty => empty string")
  public void format03() {
    assertEquals(
        table(
            List.<Integer> of(),

            (r, v) -> {
              r.cell(v, 1);
            }
        ),

        ""
    );
  }

}
