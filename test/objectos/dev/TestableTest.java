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
package objectos.dev;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class TestableTest {

  record Row01(String c1, String c2, String c3, String c4, String c5) implements Testable {
    @Override
    public void formatTestable(TestableFormatter formatter) {
      // noop
    }

    @Override
    public String toTestableText() {
      return Testable.asRow(c1, 5, c2, 5, c3, 5, c4, 5, c5, 5);
    }
  }

  @Test
  public void asRow01() {
    final Row01 subject;
    subject = new Row01("abcde", "abcd", "abc", "ab", "a");

    assertEquals(subject.toTestableText(), "abcde | abcd  | abc   | ab    | a");
  }

  @Test
  public void join() {
    assertEquals(
        Testable.join(
            new Row01("abcde", "abcd", "abc", "ab", "a"),
            new Row01("x", "xy", "xyz", "xyz1", "xyz12")
        ),

        """
        abcde | abcd  | abc   | ab    | a
        x     | xy    | xyz   | xyz1  | xyz12\
        """
    );
  }

}
