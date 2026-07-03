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
import java.util.List;
import org.testng.annotations.Test;

public class TestableRowFormatterPojoTest {

  @Test
  public void cellBoolean() {
    final TestableRowFormatterPojo subject;
    subject = new TestableRowFormatterPojo();

    subject.cell(true);
    subject.cell(false);

    assertEquals(
        subject.columns,

        List.of("true ", "false")
    );
  }

  @Test
  public void cellLong() {
    final TestableRowFormatterPojo subject;
    subject = new TestableRowFormatterPojo();

    subject.cell(123, 5);
    subject.cell(456L, 3);

    assertEquals(
        subject.columns,

        List.of("00123", "456")
    );
  }

  @Test
  public void cellLocalDate() {
    final TestableRowFormatterPojo subject;
    subject = new TestableRowFormatterPojo();

    subject.cell(LocalDate.of(2026, 1, 1));

    assertEquals(
        subject.columns,

        List.of("2026-01-01")
    );
  }

  @Test
  public void cellLocalDateTime() {
    final TestableRowFormatterPojo subject;
    subject = new TestableRowFormatterPojo();

    subject.cell(LocalDateTime.of(2026, 1, 1, 13, 0));

    assertEquals(
        subject.columns,

        List.of("2026-01-01 13:00:00")
    );
  }

  @Test
  public void cellString() {
    final TestableRowFormatterPojo subject;
    subject = new TestableRowFormatterPojo();

    subject.cell("foo", 5);

    assertEquals(
        subject.columns,

        List.of("foo  ")
    );
  }

  @Test
  public void toStringTest01() {
    final TestableRowFormatterPojo subject;
    subject = new TestableRowFormatterPojo();

    subject.cell("foo", 5);

    assertEquals(
        subject.toString(),

        "| foo   |"
    );
  }

  @Test
  public void toStringTest02() {
    final TestableRowFormatterPojo subject;
    subject = new TestableRowFormatterPojo();

    subject.cell("foo", 5);
    subject.cell(true);

    assertEquals(
        subject.toString(),

        "| foo   | true  |"
    );
  }

}
