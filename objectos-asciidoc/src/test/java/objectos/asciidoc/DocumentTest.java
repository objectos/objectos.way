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

public class DocumentTest {

  Tester tester = Tester.objectos();

  public DocumentTest() {}

  DocumentTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  doctitle + eof

  - happy path
  - title ends @ eof
  """)
  public void testCase01() {
    tester.test(
      """
      = The doctitle""",

      """
      <document>
      <title>The doctitle</title>
      </document>
      """
    );
  }

  @Test(description = """
  doctitle + NL

  - happy path
  - title ends @ NL
  """)
  public void testCase02() {
    tester.test(
      """
      = The doctitle
      """,

      """
      <document>
      <title>The doctitle</title>
      </document>
      """
    );
  }

  @Test(description = """
  doctitle (not a doctitle)

  - not a title (no space after symbol '=')
  """)
  public void testCase03() {
    tester.test(
      """
      =Not Title
      """,

      """
      <document>
      <p>=Not Title</p>
      </document>
      """
    );
  }

  @Test(description = """
  doctitle + paragraph
  """)
  public void testCase04() {
    tester.test(
      """
      = A

      b
      """,

      """
      <document>
      <title>A</title>
      <p>b</p>
      </document>
      """
    );
  }

  @Test(description = """
  document attributes

  - Objectos Docs v0002/index doc header
  """)
  public void testCase05() {
    tester.test(
      """
      = a
      :b: c
      :d: e

      {b}-{d}
      """,

      """
      <document>
      <title>a</title>
      <p>c-e</p>
      </document>
      """
    );
  }

}