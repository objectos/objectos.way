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

public class PreambleTest {

  Tester tester = Tester.objectos();

  public PreambleTest() {}

  PreambleTest(Tester tester) {
    this.tester = tester;
  }

  @Test(description = """
  paragraph + UL
  """)
  public void testCase01() {
    tester.test(
      """
      = A

      b

      * c
      * d
      """,

      """
      <document>
      <title>A</title>
      <p>b</p>
      <unordered-list>
      <item>
      <text>c</text>
      </item>
      <item>
      <text>d</text>
      </item>
      </unordered-list>
      </document>
      """
    );
  }

}