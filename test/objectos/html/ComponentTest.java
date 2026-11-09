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
package objectos.html;

import static org.testng.Assert.assertEquals;

import objectos.html.format.Formatter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ComponentTest {

  @DataProvider
  public Object[][] formatProvider() {
    final Component empty;
    empty = _ -> {};

    final Component nested;
    nested = m -> m.div(m.span(m.text("Hello")));

    final Component testableHeading;
    testableHeading = m -> m.t().h1("Heading 1");

    final Component testableHeadingAfterRegular;
    testableHeadingAfterRegular = m -> {
      m.div(m.text("Hello"));
      m.t().h1("Heading 1");
    };

    final Component testableHeadingAndCell;
    testableHeadingAndCell = m -> {
      m.t().h1("Heading 1");
      m.t().cell(1, "x");
    };

    final Component testableListItems;
    testableListItems = m -> {
      m.t().item("Foo");
      m.t().item("Bar");
    };

    final Component testableProps;
    testableProps = m -> {
      m.t().prop("First name", "John");
      m.t().prop("Last name", "Smith");
    };

    return new Object[][] {
        {
            "document, empty",

            empty,

            "",

            ""
        },
        {
            "regular element, nested",

            nested,

            """
            <div>\
            <span>Hello</span>\
            </div>
            """,

            ""
        },
        {
            "testable text, top-level testable heading",

            testableHeading,

            "",

            """
            # Heading 1
            """
        },
        {
            "testable text, testable heading after a regular element",

            testableHeadingAfterRegular,

            """
            <div>Hello</div>
            """,

            """
            # Heading 1
            """
        },
        {
            "testable text, testable heading followed by testable cell",

            testableHeadingAndCell,

            "",

            """
            # Heading 1

            | x |
            """
        },
        {
            "testable text, testable list items",

            testableListItems,

            "",

            """
            - Foo
            - Bar
            """
        },
        {
            "testable text, testable props",

            testableProps,

            "",

            """
            - First name: John
            - Last name: Smith
            """
        }
    };
  }

  @Test(dataProvider = "formatProvider")
  public void format(
      String description,
      Component subject,
      String expectedStandard,
      String expectedTestable) {
    final String standard;
    standard = subject.format(Formatter.standard());

    assertEquals(standard, expectedStandard, description);

    final String testable;
    testable = subject.format(Formatter.testable());

    assertEquals(testable, expectedTestable, description);
  }

}
