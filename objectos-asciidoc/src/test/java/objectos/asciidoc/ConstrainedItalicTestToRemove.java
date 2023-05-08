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

final class ConstrainedItalicTestToRemove extends AbstractAsciiDocTest {

  ConstrainedItalicTestToRemove(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  = italic

  - constrained
  - not an italic

  0123456
  '''
  a '_' b
  '''
  """)
  public void testCase03() {
    test(
      """
      a '_' b
      """,

      p0(
        Token.BLOB, 0, 3,
        Token.ITALIC_END, 3,
        Token.BLOB, 4, 7, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.TOKENS, 0, 8,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(
          Text.REGULAR, 0, 3,
          Text.REGULAR, 3, 4,
          Text.REGULAR, 4, 7
        )
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p>a '_' b</p>
      </div>
      </div>
      </body>
      """
    );
  }

}