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
  section

  - level reduction
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

  @Test(description = """
  section

  - level reduction
  - L1 -> L2 -> L3 -> L2
  """)
  public void testCase06() {
    tester.test(
      """
      == A

      b

      === C

      d

      ==== E

      f

      === G

      h
      """,

      """
      <document>
      <section level="1">
      <style>null</style>
      <title>A</title>
      <p>b</p>
      <section level="2">
      <style>null</style>
      <title>C</title>
      <p>d</p>
      <section level="3">
      <style>null</style>
      <title>E</title>
      <p>f</p>
      </section>
      </section>
      <section level="2">
      <style>null</style>
      <title>G</title>
      <p>h</p>
      </section>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  section

  - level reduction
  - L1 -> L2 -> L3 -> L1
  """)
  public void testCase07() {
    tester.test(
      """
      == A

      b

      === C

      d

      ==== E

      f

      == G

      h
      """,

      """
      <document>
      <section level="1">
      <style>null</style>
      <title>A</title>
      <p>b</p>
      <section level="2">
      <style>null</style>
      <title>C</title>
      <p>d</p>
      <section level="3">
      <style>null</style>
      <title>E</title>
      <p>f</p>
      </section>
      </section>
      </section>
      <section level="1">
      <style>null</style>
      <title>G</title>
      <p>h</p>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  title is constrained monospace
  """)
  public void testCase08() {
    tester.test(
      """
      == `A`

      b
      """,

      """
      <document>
      <section level="1">
      <style>null</style>
      <title><code>A</code></title>
      <p>b</p>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  section has title only, i.e., no body
  """)
  public void testCase09() {
    tester.test(
      """
      intro

      == ABC
      """,

      """
      <document>
      <p>intro</p>
      <section level="1">
      <style>null</style>
      <title>ABC</title>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  section has source code block
  """)
  public void testCase10() {
    tester.test(
      """
      == ABC

      [,java]
      ----
      break;
      ----
      """,

      """
      <document>
      <section level="1">
      <style>null</style>
      <title>ABC</title>
      <listing>
      <style>source</style>
      <lang>java</lang>
      <pre>break;</pre>
      </listing>
      </section>
      </document>
      """
    );
  }

  @Test(description = """
  title has constrained italic/bold
  """)
  public void testCase11() {
    tester.test(
      """
      == A _B_ *C* D

      e
      """,

      """
      <document>
      <section level="1">
      <style>null</style>
      <title>A <em>B</em> <strong>C</strong> D</title>
      <p>e</p>
      </section>
      </document>
      """
    );
  }

}