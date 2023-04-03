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

final class DocumentAttributeTest extends AbstractAsciiDocTest {

  DocumentAttributeTest(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  Objectos Docs v0002/index doc header

  0123
  456789
  012345
  6
  78
  '''
  = a
  :b: c
  :d: e

  f
  '''

  P0: H1-0,2 B2,3 LF
      AN5,6 AV8,9 LF
      AN11,12 AV14,15 LF
      LF
      B17,18 LF
      EOF
  """)
  public void testCase01() {
    test(
      """
      = a
      :b: c
      :d: e

      f
      """,

      p0(
        Token.HEADING, 1, 0, 2, Token.BLOB, 2, 3, Token.LF,
        Token.DOCATTR, 5, 6, Token.BLOB, 8, 9, Token.LF,
        Token.DOCATTR, 11, 12, Token.BLOB, 14, 15, Token.LF,
        Token.LF,
        Token.BLOB, 17, 18, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.HEADING_START, 1,
        Code.TOKENS, 4, 7,
        Code.HEADING_END, 1,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.TOKENS, 23, 26,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(
        "b", "c",
        "d", "e"
      ),

      p2(
        t(Text.REGULAR, 2, 3),
        t(Text.REGULAR, 17, 18)
      ),

      """
      <body>
      <div id="header">
      <h1>a</h1>
      </div>
      <div id="content">
      <div class="paragraph">
      <p>f</p>
      </div>
      </div>
      </body>
      """
    );
  }

}