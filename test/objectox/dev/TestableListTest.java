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

import org.testng.annotations.Test;

public class TestableListTest {

  @Test(description = "empty -> empty")
  public void testCase01() {
    final TestableList subject;
    subject = new TestableList();

    assertEquals(subject.toString(), "");
  }

  @Test(description = "1 item")
  public void testCase02() {
    final TestableList subject;
    subject = new TestableList();

    subject.item("Java");

    assertEquals(
        subject.toString(),

        """
        - Java\
        """
    );
  }

  @Test(description = "multiple items")
  public void testCase03() {
    final TestableList subject;
    subject = new TestableList();

    subject.item("Foo");
    subject.item("Bar");
    subject.item("Baz");

    assertEquals(
        subject.toString(),

        """
        - Foo
        - Bar
        - Baz\
        """
    );
  }

  @Test(
      description = "reject null items",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void testCase04() {
    final TestableList subject;
    subject = new TestableList();

    subject.item(null);
  }

  @Test(description = "accept boolean values")
  public void testCase05() {
    final TestableList subject;
    subject = new TestableList();

    subject.item("Foo");
    subject.item(true);
    subject.item("Bar");
    subject.item(false);

    assertEquals(
        subject.toString(),

        """
        - Foo
        - true
        - Bar
        - false\
        """
    );
  }

  @Test(description = "handle empty string item")
  public void testCase06() {
    final TestableList subject;
    subject = new TestableList();

    subject.item("Foo");
    subject.item("");
    subject.item("Bar");

    assertEquals(
        subject.toString(),

        """
        - Foo
        -
        - Bar\
        """
    );
  }

}
