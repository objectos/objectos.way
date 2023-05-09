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

public class SourceCodeBlockTest {

  Tester tester = Tester.objectos();

  public SourceCodeBlockTest() {}

  SourceCodeBlockTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  source code block

  - implict source style
  - delimited
  - single line
  """)
  public void testCase01() {
    tester.test(
      """
      [,java]
      ----
      break;
      ----
      """,

      """
      <document>
      <listing>
      <style>source</style>
      <lang>java</lang>
      <pre>break;</pre>
      </listing>
      </document>
      """
    );
  }

  @Test(description = """
  source code block

  - explict source style
  - delimited
  - multiple lines
  """)
  public void testCase02() {
    tester.test(
      """
      [source,java]
      ----
      class A {

      }
      ----
      """,

      """
      <document>
      <listing>
      <style>source</style>
      <lang>java</lang>
      <pre>class A {

      }</pre>
      </listing>
      </document>
      """
    );
  }

  @Test(description = """
  source code block

  - implict source style
  - delimited
  - indented content
  """)
  public void testCase03() {
    tester.test(
      """
      [,a]
      ----
      b
          c
      ----
      """,

      """
      <document>
      <listing>
      <style>source</style>
      <lang>a</lang>
      <pre>b
          c</pre>
      </listing>
      </document>
      """
    );
  }

  @Test(description = """
  source code block

  - implict source style
  - delimited
  - contains Token.BOLD_END
  """)
  public void testCase04() {
    tester.test(
      """
      [,a]
      ----
      b*;
      ----
      """,

      """
      <document>
      <listing>
      <style>source</style>
      <lang>a</lang>
      <pre>b*;</pre>
      </listing>
      </document>
      """
    );
  }

  @Test(description = """
  source code block

  - trailing space at close marker
  - has paragraph after
  """)
  public void testCase05() {
    tester.test(
      """
      [,a]
      ----
      b*;
      ----\040\040

      foo
      """,

      """
      <document>
      <listing>
      <style>source</style>
      <lang>a</lang>
      <pre>b*;</pre>
      </listing>
      <p>foo</p>
      </document>
      """
    );
  }

}