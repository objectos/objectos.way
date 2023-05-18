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

public class ListingBlockTest {

  Tester tester = Tester.objectos();

  public ListingBlockTest() {}

  ListingBlockTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  listing block

  - delimited
  - single line
  """)
  public void testCase01() {
    tester.test(
      """
      ----
      code
      ----
      """,

      """
      <document>
      <listing>
      <style>listing</style>
      <pre>code</pre>
      </listing>
      </document>
      """
    );
  }

  @Test(description = """
  listing block code with ']'
  """)
  public void testCase02() {
    tester.test(
      """
      ----
      foo]
      ----
      """,

      """
      <document>
      <listing>
      <style>listing</style>
      <pre>foo]</pre>
      </listing>
      </document>
      """
    );
  }

  @Test(description = """
  listing block code with '[x]'
  """)
  public void testCase03() {
    tester.test(
      """
      ----
      a: b[2]
      ----
      """,

      """
      <document>
      <listing>
      <style>listing</style>
      <pre>a: b[2]</pre>
      </listing>
      </document>
      """
    );
  }

  @Test(description = """
  listing block code with '_'
  """)
  public void testCase04() {
    tester.test(
      """
      ----
      foo _bar
      ----
      """,

      """
      <document>
      <listing>
      <style>listing</style>
      <pre>foo _bar</pre>
      </listing>
      </document>
      """
    );
  }

  @Test(description = """
  listing block marker ends at EOF
  """)
  public void testCase05() {
    tester.test(
      """
      ----
      foo
      ----""",

      """
      <document>
      <listing>
      <style>listing</style>
      <pre>foo</pre>
      </listing>
      </document>
      """
    );
  }

}