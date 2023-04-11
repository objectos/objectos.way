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

public class ParagraphTest {

  Tester tester = Tester.objectos();

  public ParagraphTest() {}

  ParagraphTest(Tester tester) {
    this.tester = tester;
  }

  @Test(enabled = false, description = """
  two lines paragraph

  0123
  4567
  '''
  abc
  def
  '''
  """)
  public void testCase01() {
    tester.test(
      """
      abc
      def
      """,

      """
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p>abc
      def</p>
      </div>
      </div>
      """
    );
  }

}
