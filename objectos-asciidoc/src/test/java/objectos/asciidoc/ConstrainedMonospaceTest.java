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

public class ConstrainedMonospaceTest {

  Tester tester = Tester.objectos();

  public ConstrainedMonospaceTest() {}

  ConstrainedMonospaceTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  monospace

  - constrained
  - words only
  - well-formed
  """)
  public void testCase01() {
    tester.test(
      """
      `a` `b`, `c`
      """,

      """
      <document>
      <p><code>a</code> <code>b</code>, <code>c</code></p>
      </document>
      """
    );
  }

  @Test(description = """
  monospace

  - constrained
  - phrases
  - well-formed
  """)
  public void testCase02() {
    tester.test(
      """
      `a b` `c d`, `e f`
      """,

      """
      <document>
      <p><code>a b</code> <code>c d</code>, <code>e f</code></p>
      </document>
      """
    );
  }

  @Test(description = """
  monospace

  - in the middle of paragraph
  """)
  public void testCase03() {
    tester.test(
      """
      a `b` c
      """,

      """
      <document>
      <p>a <code>b</code> c</p>
      </document>
      """
    );
  }

  @Test(description = """
  monospace

  - in the middle of paragraph
  """)
  public void testCase04() {
    tester.test(
      """
      a ` not ` c
      """,

      """
      <document>
      <p>a ` not ` c</p>
      </document>
      """
    );
  }

  @Test(description = """
  monospace

  - contents is a (not) inline macro
  """)
  public void testCase05() {
    tester.test(
      """
      a `Int::value` c
      """,

      """
      <document>
      <p>a <code>Int::value</code> c</p>
      </document>
      """
    );
  }

  @Test(description = """
  monospace

  - contents is a (not) emphasis
  - contents is a (not) strong
  """)
  public void testCase06() {
    tester.test(
      """
      a `_this()` c

      a `*what` c
      """,

      """
      <document>
      <p>a <code>_this()</code> c</p>
      <p>a <code>*what</code> c</p>
      </document>
      """
    );
  }

  @Test(description = """
  monospace

  - contents is a (not) emphasis
  - contents is a (not) strong
  """)
  public void testCase07() {
    tester.test(
      """
      foo `UTF_8` bar

      foo `UTF*8` bar
      """,

      """
      <document>
      <p>foo <code>UTF_8</code> bar</p>
      <p>foo <code>UTF*8</code> bar</p>
      </document>
      """
    );
  }

  @Test(description = """
  monospace

  - not a monospace
  """)
  public void testCase08() {
    tester.test(
      """
      foo`bar
      """,

      """
      <document>
      <p>foo`bar</p>
      </document>
      """
    );
  }

}