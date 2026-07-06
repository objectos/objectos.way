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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestableFormatterPojoTest {

  private final BiConsumer<TestableFormatterPojo, String> h1 = (subject, value) -> subject.h1(value);
  private final BiConsumer<TestableFormatterPojo, String> h2 = (subject, value) -> subject.h2(value);

  @DataProvider
  public Object[][] heading01Provider() {
    return new Object[][] {
        {h1, "# Title"},
        {h2, "## Title"}
    };
  }

  @Test(dataProvider = "heading01Provider", description = "reject null value")
  public void heading01(BiConsumer<TestableFormatterPojo, String> method, String expected) {
    final TestableFormatterPojo subject;
    subject = new TestableFormatterPojo();

    method.accept(subject, "Title");

    assertEquals(subject.items, List.of(expected));
  }

  @Test
  public void list01() {
    final TestableFormatterPojo subject;
    subject = new TestableFormatterPojo();

    subject.list(lf -> {
      lf.item("Foo");
      lf.item("Bar");
      lf.item("Baz");
    });

    assertEquals(subject.items, List.of("""
    - Foo
    - Bar
    - Baz\
    """));
  }

  @Test
  public void list02() {
    final TestableFormatterPojo subject;
    subject = new TestableFormatterPojo();

    subject.list(
        List.of("Foo", "Bar", "Baz"),
        (lf, v) -> lf.item(v)
    );

    assertEquals(subject.items, List.of("""
    - Foo
    - Bar
    - Baz\
    """));
  }

  @Test
  public void table01() {
    final TestableFormatterPojo subject;
    subject = new TestableFormatterPojo();

    subject.table(List.of(1, 2, 3), (rf, v) -> rf.cell(v, 1));

    assertEquals(subject.items, List.of("""
    | 1 |
    | 2 |
    | 3 |\
    """));
  }

  @Test
  public void toStringTest01() {
    final TestableFormatterPojo subject;
    subject = new TestableFormatterPojo();

    assertEquals(
        subject.toString(),

        """
        """
    );
  }

  @Test
  public void toStringTest02() {
    final TestableFormatterPojo subject;
    subject = new TestableFormatterPojo();

    subject.h1("First Heading");

    assertEquals(
        subject.toString(),

        """
        # First Heading
        """
    );
  }

  @Test
  public void toStringTest03() {
    final TestableFormatterPojo subject;
    subject = new TestableFormatterPojo();

    subject.h1("First Heading");

    subject.h2("Second Heading");

    assertEquals(
        subject.toString(),

        """
        # First Heading

        ## Second Heading
        """
    );
  }

  @Test
  public void toStringTest04() {
    final TestableFormatterPojo subject;
    subject = new TestableFormatterPojo();

    subject.h1("First Heading");

    subject.table(List.of(1, 2, 3), (rf, v) -> rf.cell(v, 1));

    subject.h2("Second Heading");

    assertEquals(
        subject.toString(),

        """
        # First Heading

        | 1 |
        | 2 |
        | 3 |

        ## Second Heading
        """
    );
  }

  @Test
  public void toStringTest05() {
    final TestableFormatterPojo subject;
    subject = new TestableFormatterPojo();

    subject.h1("First Heading");

    subject.table(List.<Integer> of(), (rf, v) -> rf.cell(v, 1));

    subject.h2("Second Heading");

    assertEquals(
        subject.toString(),

        """
        # First Heading

        ## Second Heading
        """
    );
  }

}
