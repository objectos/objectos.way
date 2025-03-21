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

public class UrlMacroTest {

  Tester tester = Tester.objectos();

  public UrlMacroTest() {}

  UrlMacroTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  - https
  - well-formed
  """)
  public void testCase01() {
    tester.test(
      """
      https://example.com[Ex]
      """,

      """
      <document>
      <p><a href="https://example.com">Ex</a></p>
      </document>
      """
    );
  }

  @Test(description = """
  - https
  - well-formed
  - w/ text before
  """)
  public void testCase02() {
    tester.test(
      """
      foo https://example.com[Ex]
      """,

      """
      <document>
      <p>foo <a href="https://example.com">Ex</a></p>
      </document>
      """
    );
  }

  @Test(description = """
  - https
  - well-formed
  - comma in attrlist
  """)
  public void testCase03() {
    tester.test(
      """
      https://a[b, c]
      """,

      """
      <document>
      <p><a href="https://a">b, c</a></p>
      </document>
      """
    );
  }

}