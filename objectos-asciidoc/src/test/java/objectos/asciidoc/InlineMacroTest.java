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

public class InlineMacroTest {

  Tester tester = Tester.objectos();

  public InlineMacroTest() {}

  InlineMacroTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  Not an inline macro
  """)
  public void testCase01() {
    tester.test(
      """
      see this:

      foo
      """,

      """
      <document>
      <p>see this:</p>
      <p>foo</p>
      </document>
      """
    );
  }

  @Test(description = """
  inline macro

  - unordered list
  - section
  """)
  public void testCase02() {
    tester.test(
      """
      == A

      * i:b[c]
      * i:d[e]
      """,

      """
      <document>
      <section level="1">
      <style>null</style>
      <title>A</title>
      <unordered-list>
      <item>
      <text><a href="b">c</a></text>
      </item>
      <item>
      <text><a href="d">e</a></text>
      </item>
      </unordered-list>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  imacro ends the sentence
  """)
  public void testCase03() {
    tester.test(
      """
      a i:b[c].

      d
      """,

      """
      <document>
      <p>a <a href="b">c</a>.</p>
      <p>d</p>
      </document>
      """
    );
  }

  @Test(description = """
  not imacro: target with spaces
  """)
  public void testCase04() {
    tester.test(
      """
      i:b c[d]
      """,

      """
      <document>
      <p>i:b c[d]</p>
      </document>
      """
    );
  }

  @Test(description = """
  imacro + monospace
  """)
  public void testCase05() {
    tester.test(
      """
      i:b[`c`]
      """,

      """
      <document>
      <p><a href="b"><code>c</code></a></p>
      </document>
      """
    );
  }

  @Test(description = """
  imacro + spaces + monospace
  """)
  public void testCase06() {
    tester.test(
      """
      i:b[c `d` e]
      """,

      """
      <document>
      <p><a href="b">c <code>d</code> e</a></p>
      </document>
      """
    );
  }

  @Test(description = """
  not inline macro:

  - semicolon @ eol
  """)
  public void testCase07() {
    tester.test(
      """
      foo tmpl:\040

      bar
      """,

      """
      <document>
      <p>foo tmpl:</p>
      <p>bar</p>
      </document>
      """
    );
  }

  @Test(description = """
  not inline macro:

  - no attrlist at EOL
  - also list...
  """)
  public void testCase08() {
    tester.test(
      """
      * Service; and
      * Service.java:4.

      baz
      """,

      """
      <document>
      <unordered-list>
      <item>
      <text>Service; and</text>
      </item>
      <item>
      <text>Service.java:4.</text>
      </item>
      </unordered-list>
      <p>baz</p>
      </document>
      """
    );
  }

  @Test(description = """
  inline macro:

  - empty attrlist
  """)
  public void testCase09() {
    tester.test(
      """
      i:foo[]
      """,

      """
      <document>
      <p><a href="foo"></a></p>
      </document>
      """
    );
  }

}