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

public class ConstrainedItalicTest {

  Tester tester = Tester.objectos();

  public ConstrainedItalicTest() {}

  ConstrainedItalicTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  = italic

  - constrained
  - words only
  - well-formed
  """)
  public void testCase01() {
    tester.test(
      """
      _a_ _b_, _c_
      """,

      """
      <document>
      <p><em>a</em> <em>b</em>, <em>c</em></p>
      </document>
      """
    );
  }

  @Test(description = """
  italic

  - constrained
  - phrases
  - well-formed
  """)
  public void testCase02() {
    tester.test(
      """
      _a b_ _c d_, _e f_
      """,

      """
      <document>
      <p><em>a b</em> <em>c d</em>, <em>e f</em></p>
      </document>
      """
    );
  }

  @Test(description = """
  = italic

  - constrained
  - not an italic
  """)
  public void testCase03() {
    tester.test(
      """
      a '_' b
      """,

      """
      <document>
      <p>a '_' b</p>
      </document>
      """
    );
  }

  @Test(description = """
  = italic

  - constrained
  - not an italic
  """)
  public void testCase04() {
    tester.test(
      """
      a '_' b
      foo

      bar
      """,

      """
      <document>
      <p>a '_' b
      foo</p>
      <p>bar</p>
      </document>
      """
    );
  }

}