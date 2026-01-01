/*
 * Copyright (C) 2021-2026 Objectos Software LTDA.
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

  @Test(description = """
  unordered list

  - text with monospace
  """)
  public void testCase07() {
    tester.test(
      """
      * a `b` c
      * d
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>a <code>b</code> c</text>
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

  - third item is NOT an item
  """)
  public void testCase08() {
    tester.test(
      """
      - a
      - b
      -
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>a</text>
      </item>
      <item>
      <text>b
      -</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

  @Test(description = """
  unordered list

  - ul should end before the paragraph
  """)
  public void testCase09() {
    tester.test(
      """
      * a
      * b

      c
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
      </unordered-list>
      <p>c</p>
      </document>
      """
    );
  }

  @Test(description = """
  ul should end before section (ends with imacro)
  """)
  public void testCase10() {
    tester.test(
      """
      * a
      * i:b[c]

      == D
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>a</text>
      </item>
      <item>
      <text><a href="b">c</a></text>
      </item>
      </unordered-list>
      <section level="1">
      <style>null</style>
      <title>D</title>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  items can be separated by new line
  """)
  public void testCase11() {
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
  ul should end before block attr list
  """)
  public void testCase12() {
    tester.test(
      """
      * a

      []
      * b
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>a</text>
      </item>
      </unordered-list>
      <unordered-list>
      <item>
      <text>b</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

  @Test(description = """
  ul at end of document
  """)
  public void testCase13() {
    tester.test(
      """
      * a
      * b
      cde

      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>a</text>
      </item>
      <item>
      <text>b
      cde</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

  @Test(description = """
  unordered list

  - text with monospace
  - text with strong
  - text with emphasis
  """)
  public void testCase14() {
    tester.test(
      """
      * `a`
      * _b_
      * *c* d
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text><code>a</code></text>
      </item>
      <item>
      <text><em>b</em></text>
      </item>
      <item>
      <text><strong>c</strong> d</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

  @Test(description = """
  unordered list

  - items separated by NL
  """)
  public void testCase15() {
    tester.test(
      """
      * item 1:
      more 1

      * item 2:
      more 2

      * item 3:
      more 3
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>item 1:
      more 1</text>
      </item>
      <item>
      <text>item 2:
      more 2</text>
      </item>
      <item>
      <text>item 3:
      more 3</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

}