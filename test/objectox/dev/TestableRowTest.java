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

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.testng.annotations.Test;

public class TestableRowTest {

  @Test(description = "strings")
  public void format01() {
    final TestableRow row;
    row = new TestableRow("abcde", 5, "abcd", 5, "abc", 5, "ab", 5, "a", 5);

    assertEquals(row.format(), "abcde | abcd  | abc   | ab    | a");
  }

  @Test(description = "booleans")
  public void format02() {
    final TestableRow row;
    row = new TestableRow(true, false);

    assertEquals(row.format(), "true  | false");
  }

  @Test(description = "dates")
  public void format03() {
    final TestableRow row;
    row = new TestableRow(LocalDate.of(2025, 1, 1), LocalDateTime.of(2025, 1, 1, 13, 0));

    assertEquals(
        row.format(),

        "2025-01-01 | 2025-01-01 13:00:00"
    );
  }

  @Test(description = "numbers")
  public void format04() {
    final TestableRow row;
    row = new TestableRow(123, 5, 4567L, 7);

    assertEquals(
        row.format(),

        "00123 | 0004567"
    );
  }

}
