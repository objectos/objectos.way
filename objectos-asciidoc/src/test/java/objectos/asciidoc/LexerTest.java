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

final class LexerTest extends AbstractAsciiDocTest {

  LexerTest(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  apostrophe between word chars

  0123
  '''
  a'b
  '''
  """)
  public void testCase01() {
    test(
      """
      a'b
      """,

      p0(
        Token.BLOB, 0, 1, Token.APOSTROPHE, 1, Token.BLOB, 2, 3, Token.LF,
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
        t(Text.REGULAR, 0, 1, Text.CURVED_APOSTROPHE, Text.REGULAR, 2, 3)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p>a’b</p>
      </div>
      </div>
      </body>
      """
    );
  }

}