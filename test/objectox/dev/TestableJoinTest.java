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

import objectos.dev.Testable;
import objectos.dev.TestableFormatter;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestableJoinTest {

  private record Rec(String name, int count) implements Testable {
    @Override
    public void formatTestable(TestableFormatter formatter) {}

    @Override
    public final String toTestableText() {
      return Testable.asRow(name, 5, count, 5);
    }
  }

  @Test(description = "reject null array")
  public void format01() {
    try {
      final Testable[] values;
      values = null;

      new TestableJoin(values);

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "rows == null");
    }
  }

  @Test(description = "reject null value in array")
  public void format02() {
    final TestableJoin subject;
    subject = new TestableJoin(
        new Rec("foo", 4),
        null,
        new Rec("bar", 5)
    );

    try {
      subject.format();

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "rows[1] == null");
    }
  }

  @Test(description = "empty array -> empty string")
  public void format03() {
    final TestableJoin subject;
    subject = new TestableJoin();

    assertEquals(subject.format(), "");
  }

  @Test(description = "join by LF")
  public void format04() {
    final TestableJoin subject;
    subject = new TestableJoin(
        new Rec("foo", 4),
        new Rec("bar", 5)
    );

    assertEquals(
        subject.format(),

        """
        foo   | 00004
        bar   | 00005\
        """
    );
  }

}
