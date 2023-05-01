/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc;

import org.testng.annotations.Test;

public class UnorderedListTest {

  Tester tester = Tester.objectos();

  public UnorderedListTest() {}

  UnorderedListTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  unordered list

  - simple elements
  - dash
  - single level
  """)
  public void testCase01() {
    tester.test(
      """
      - a
      -  b
      - c
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>a</text>
      </item>
      <item>
      <text>b</text>
      </item>
      <item>
      <text>c</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

  @Test(description = """
  unordered list

  - complex elements
  - dash
  - single level
  """)
  public void testCase02() {
    tester.test(
      """
      - a
      bcd
      - e
      fgh
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>a
      bcd</text>
      </item>
      <item>
      <text>e
      fgh</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

  @Test(description = """
  unordered list

  - simple elements
  - asterisk
  - single level
  """)
  public void testCase03() {
    tester.test(
      """
      * a
      * b
      * c
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>a</text>
      </item>
      <item>
      <text>b</text>
      </item>
      <item>
      <text>c</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

  @Test(description = """
  unordered list

  - nested unordered list
  - asterisk
  """)
  public void testCase04() {
    tester.test(
      """
      * a
      ** b
      ** c
      * d
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>a</text>
      <unordered-list>
      <item>
      <text>b</text>
      </item>
      <item>
      <text>c</text>
      </item>
      </unordered-list>
      </item>
      <item>
      <text>d</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

  @Test(description = """
  unordered list

  - indented nested unordered list
  - asterisk
  """)
  public void testCase05() {
    tester.test(
      """
      * a
       ** b
       ** c
      * d
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>a</text>
      <unordered-list>
      <item>
      <text>b</text>
      </item>
      <item>
      <text>c</text>
      </item>
      </unordered-list>
      </item>
      <item>
      <text>d</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

  @Test(description = """
  unordered list

  - simple elements
  - asterisk
  - single level
  - section
  """)
  public void testCase06() {
    tester.test(
      """
      == A

      b c:

      * d
      * e
      """,

      """
      <document>
      <section level="1">
      <style>null</style>
      <title>A</title>
      <p>b c:</p>

      <unordered-list>
      <item>
      <text>d</text>
      </item>
      <item>
      <text>e</text>
      </item>
      </unordered-list>
      </section>
      </document>
      """
    );
  }

}