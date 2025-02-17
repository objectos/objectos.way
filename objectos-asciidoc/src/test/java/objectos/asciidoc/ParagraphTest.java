/*
 * Copyright (C) 2021-2025 Objectos Software LTDA.
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

public class ParagraphTest {

  Tester tester = Tester.objectos();

  public ParagraphTest() {}

  ParagraphTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  two lines paragraph
  """)
  public void testCase01() {
    tester.test(
      """
      abc
      def
      """,

      """
      <document>
      <p>abc
      def</p>
      </document>
      """
    );
  }

  @Test(description = """
  multiline paragraph

  - last line starts with url macro
  """)
  public void testCase02() {
    tester.test(
      """
      abc
      https://d[e].
      """,

      """
      <document>
      <p>abc
      <a href="https://d">e</a>.</p>
      </document>
      """
    );
  }

  @Test(description = """
  multiline paragraph

  - 3 lines
  - second line ends with cmonospace
  """)
  public void testCase03() {
    tester.test(
      """
      abc
      d `e`
      f
      """,

      """
      <document>
      <p>abc
      d <code>e</code>
      f</p>
      </document>
      """
    );
  }

  @Test(description = """
  multiline paragraph.

  - line ends in a imacro.
  """)
  public void testCase04() {
    tester.test(
      """
      a i:b[c]
      d
      """,

      """
      <document>
      <p>a <a href="b">c</a>
      d</p>
      </document>
      """
    );
  }

  @Test(description = """
  imacro in the middle of a sentence
  """)
  public void testCase05() {
    tester.test(
      """
      a i:b[c] d
      """,

      """
      <document>
      <p>a <a href="b">c</a> d</p>
      </document>
      """
    );
  }

  @Test(description = """
  next line of paragraph starts with monospace
  """)
  public void testCase06() {
    tester.test(
      """
      abc;
      `foo` bar
      """,

      """
      <document>
      <p>abc;
      <code>foo</code> bar</p>
      </document>
      """
    );
  }

  @Test(description = """
  paragraphs separated by non-empty line
  """)
  public void testCase07() {
    tester.test(
      """
      foo
      \040\040
      bar
      """,

      """
      <document>
      <p>foo</p>
      <p>bar</p>
      </document>
      """
    );
  }

  @Test(description = """
  paragraph

  - trim before EOF
  """)
  public void testCase08() {
    tester.test(
      """
      foo
      \040\040
      """,

      """
      <document>
      <p>foo</p>
      </document>
      """
    );
  }

  @Test(description = """
  paragraph

  - whole paragraph is emphasis
  - followed by another paragraph
  """)
  public void testCase09() {
    tester.test(
      """
      _foo_

      bar

      """,

      """
      <document>
      <p><em>foo</em></p>
      <p>bar</p>
      </document>
      """
    );
  }

}
