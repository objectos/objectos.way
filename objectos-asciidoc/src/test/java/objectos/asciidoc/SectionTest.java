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

  '''
  = doc

  pream

  == L1

  parag
  '''
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

  '''
  = doc

  pream

  == L1

  sect1

  === 2

  sect2
  '''
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
      <title>L1</title>
      <p>sect1</p>

      <section level="2">
      <title>2</title>
      <p>sect2</p>
      </section>
      </section>
      </document>
      """
    );
  }

}