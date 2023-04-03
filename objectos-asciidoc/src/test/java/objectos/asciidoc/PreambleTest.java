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

final class PreambleTest extends AbstractAsciiDocTest {

  PreambleTest(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  paragraph + UL

  0123
  4
  56
  7
  8901
  2345
  '''
  = A

  b

  * c
  * d
  '''
  """)
  public void testCase01() {
    test(
      """
      = A

      b

      * c
      * d
      """,

      p0(
        Token.HEADING, 1, 0, 2,
        Token.BLOB, 2, 3, Token.LF,
        Token.LF,
        Token.BLOB, 5, 6, Token.LF,
        Token.LF,
        Token.ULIST_ASTERISK, 1, 8, 9, Token.BLOB, 10, 11, Token.LF,
        Token.ULIST_ASTERISK, 1, 12, 13, Token.BLOB, 14, 15, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.HEADING_START, 1, Code.TOKENS, 4, 7, Code.HEADING_END, 1,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START, Code.TOKENS, 9, 12, Code.PARAGRAPH_END,

        Code.ULIST_START,
        Code.LI_START, Code.TOKENS, 18, 21, Code.LI_END,

        Code.LI_START, Code.TOKENS, 26, 29, Code.LI_END,
        Code.ULIST_END,

        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 2, 3),
        t(Text.REGULAR, 5, 6),
        t(Text.REGULAR, 10, 11),
        t(Text.REGULAR, 14, 15)
      ),

      """
      <div id="header">
      <h1>A</h1>
      </div>
      <div id="content">
      <div class="paragraph">
       <p>b</p>
      </div>
      <div class="ulist">
       <ul>
        <li><p>c</p></li>
        <li><p>d</p></li>
       </ul>
      </div>
      </div>
      """
    );
  }

}