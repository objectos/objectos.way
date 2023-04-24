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

public class SectionTest {

  Tester tester = Tester.objectos();

  public SectionTest() {}

  SectionTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  section

  - level 1
  - single paragraph
  """)
  public void testCase01() {
    tester.test(
      """
      = doc

      pream

      == L1

      parag
      """,

      """
      <document>
      <title>doc</title>
      <p>pream</p>

      <section level="1">
      <style>null</style>
      <title>L1</title>
      <p>parag</p>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  section

  - level 1
  - level 2
  - single paragraph in each
  """)
  public void testCase02() {
    tester.test(
      """
      = doc

      pream

      == L1

      sect1

      === 2

      sect2
      """,

      """
      <document>
      <title>doc</title>
      <p>pream</p>

      <section level="1">
      <style>null</style>
      <title>L1</title>
      <p>sect1</p>

      <section level="2">
      <style>null</style>
      <title>2</title>
      <p>sect2</p>
      </section>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  section

  - attribute list
  - level 1
  - no doctitle
  - single paragraph
  """)
  public void testCase03() {
    tester.test(
      """
      [nam]
      == L1

      sect1
      """,

      """
      <document>
      <section level="1">
      <style>nam</style>
      <title>L1</title>
      <p>sect1</p>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  section

  - level 1
  - starts after a UL
  """)
  public void testCase04() {
    tester.test(
      """
      == L1

      * a
      * b

      == L2

      c
      """,

      """
      <document>
      <section level="1">
      <style>null</style>
      <title>L1</title>
      <unordered-list>
      <item>
      <text>a</text>
      </item>
      <item>
      <text>b</text>
      </item>
      </unordered-list>
      </section>

      <section level="1">
      <style>null</style>
      <title>L2</title>
      <p>c</p>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  section level reduction

  012345
  6
  78
  9
  01234
  5
  67
  '''
  === A

  b

  == C

  d
  '''
  """)
  public void testCase05() {
    tester.test(
      """
      === A

      b

      == C

      d
      """,

      """
      <document>
      <section level="2">
      <style>null</style>
      <title>A</title>
      <p>b</p>
      </section>

      <section level="1">
      <style>null</style>
      <title>C</title>
      <p>d</p>
      </section>
      </document>
      """
    );
  }

}