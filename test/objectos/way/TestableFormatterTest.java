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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.testng.annotations.Test;

public class TestableFormatterTest {

  @Test(description = "cell(String, length)")
  public void cell01() {
    Testable.Formatter w;
    w = Testable.Formatter.create();

    w.heading1("String cells");

    // happy path
    w.cell("abcde", 5);
    w.cell("abcd", 5);
    w.cell("abc", 5);
    w.cell("ab", 5);
    w.cell("a", 5);
    w.newLine();

    // null value
    w.cell("foo", 5);
    w.cell("bar", 5);
    w.cell(null, 5);
    w.cell("maz", 5);
    w.cell("qux", 5);
    w.newLine();

    // first cell empty
    w.cell("", 5);
    w.cell("test", 5);
    w.cell("test", 5);
    w.cell("test", 5);
    w.cell("test", 5);
    w.newLine();

    assertEquals(w.toString(), """
    # String cells

    abcde | abcd  | abc   | ab    | a
    foo   | bar   | null  | maz   | qux
          | test  | test  | test  | test
    """);
  }

  @Test(description = "date cells")
  public void cell02() {
    Testable.Formatter w;
    w = Testable.Formatter.create();

    w.heading1("Date cells");

    // happy path
    w.cell(LocalDate.of(2025, 1, 1));
    w.cell(LocalDateTime.of(2025, 1, 1, 13, 0));
    w.newLine();

    // null value
    w.cell((LocalDate) null);
    w.cell((LocalDateTime) null);
    w.newLine();

    assertEquals(w.toString(), """
    # Date cells

    2025-01-01 | 2025-01-01 13:00:00
    ---------- | ---------- --------
    """);
  }

  @Test
  public void field01() {
    Testable.Formatter w;
    w = Testable.Formatter.create();

    w.heading1("Field");

    w.field("name", "value");

    w.fieldName("foo");
    w.fieldValue("bar");

    assertEquals(w.toString(), """
    # Field

    name: value
    foo: bar
    """);
  }

  @Test
  public void field02() {
    Testable.Formatter w;
    w = Testable.Formatter.create();

    w.heading1("Field");

    w.field("name", "");

    w.fieldName("foo");
    w.fieldValue("");

    assertEquals(w.toString(), """
    # Field

    name:
    foo:
    """);
  }

  @Test
  public void field03() {
    Testable.Formatter w;
    w = Testable.Formatter.create();

    w.heading1("Field");

    w.fieldValue("foo");

    assertEquals(w.toString(), """
    # Field

    foo
    """);
  }

  @Test
  public void heading01() {
    Testable.Formatter w;
    w = Testable.Formatter.create();

    w.heading1("First");
    w.row("test", 5);
    w.heading2("Second");
    w.row("test", 5);
    w.heading3("Third");
    w.row("test", 5);
    w.heading4("Fourth");
    w.row("test", 5);
    w.heading5("Fifth");
    w.row("test", 5);
    w.heading6("Sixth");
    w.row("test", 5);

    assertEquals(w.toString(), """
    # First

    test

    ## Second

    test

    ### Third

    test

    #### Fourth

    test

    ##### Fifth

    test

    ###### Sixth

    test
    """);
  }

  @Test
  public void row01() {
    Testable.Formatter w;
    w = Testable.Formatter.create();

    w.row("abcde", 5, "abcd", 5, "abc", 5, "ab", 5, "a", 5);

    assertEquals(w.toString(), """
    abcde | abcd  | abc   | ab    | a
    """);
  }

}