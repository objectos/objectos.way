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

public class DocumentTitleTest {

  Tester tester = Tester.objectos();

  public DocumentTitleTest() {}

  DocumentTitleTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  doctitle + eof

  - happy path
  - title ends @ eof

  '''
  = The doctitle'''
  """)
  public void testCase01() {
    tester.test(
      """
      = The doctitle""",

      """
      <div id="header">
      <h1>The doctitle</h1>
      </div>
      <div id="content">

      </div>
      """
    );
  }

  @Test(description = """
  doctitle + NL

  - happy path
  - title ends @ NL

  '''
  = The doctitle
  '''
  """)
  public void testCase02() {
    tester.test(
      """
      = The doctitle
      """,

      """
      <div id="header">
      <h1>The doctitle</h1>
      </div>
      <div id="content">

      </div>
      """
    );
  }

}