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

final class ConstrainedItalicTest extends AbstractAsciiDocTest {

  ConstrainedItalicTest(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  = italic

  - constrained
  - words only
  - well-formed

  0123456789012
  '''
  _a_ _b_, _c_
  '''

  P0: ^ _ B1,2 _ B3,4 _ B5,6 _ B7,9 _ B10,11 _ $ LF
      ^ $ EOF

  P2: ^ <I R1,2 I> R3,4 <I R5,6 I> R7,9 <I R10,11 I> $ LF
      ^ $ EOF
  """)
  public final void testCase01() {
    test(
      """
      _a_ _b_, _c_
      """,

      p0(
        Token.ITALIC_START, 0,
        Token.BLOB, 1, 2,
        Token.ITALIC_END, 2,
        Token.BLOB, 3, 4,
        Token.ITALIC_START, 4,
        Token.BLOB, 5, 6,
        Token.ITALIC_END, 6,
        Token.BLOB, 7, 9,
        Token.ITALIC_START, 9,
        Token.BLOB, 10, 11,
        Token.ITALIC_END, 11,
        Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.TOKENS, 0, 27,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(
          Text.ITALIC_START,
          Text.REGULAR, 1, 2,
          Text.ITALIC_END,
          Text.REGULAR, 3, 4,
          Text.ITALIC_START,
          Text.REGULAR, 5, 6,
          Text.ITALIC_END,
          Text.REGULAR, 7, 9,
          Text.ITALIC_START,
          Text.REGULAR, 10, 11,
          Text.ITALIC_END
        )
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p><em>a</em> <em>b</em>, <em>c</em></p>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  italic

  - constrained
  - phrases
  - well-formed

  012345678901234567
  '''
  _a b_ _c d_, _e f_
  '''

  L0: ^ _ W1,2 SP W3,4 _ SP _ W7,8 SP W9,10 _ X11,12 SP _ W14,15 SP W16,17 _ $ LF
      ^ $ EOF

  L1: ^ <I R1,4 I> R4,6 <I R7,10 I> R11,13 <I R14,17 I> $ LF
      ^ $ EOF
  """)
  public final void testCase02() {
    test(
      """
      _a b_ _c d_, _e f_
      """,

      p0(
        Token.ITALIC_START, 0,
        Token.BLOB, 1, 4,
        Token.ITALIC_END, 4,
        Token.BLOB, 5, 6,
        Token.ITALIC_START, 6,
        Token.BLOB, 7, 10,
        Token.ITALIC_END, 10,
        Token.BLOB, 11, 13,
        Token.ITALIC_START, 13,
        Token.BLOB, 14, 17,
        Token.ITALIC_END, 17,
        Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.TOKENS, 0, 27,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(
          Text.ITALIC_START,
          Text.REGULAR, 1, 4,
          Text.ITALIC_END,
          Text.REGULAR, 5, 6,
          Text.ITALIC_START,
          Text.REGULAR, 7, 10,
          Text.ITALIC_END,
          Text.REGULAR, 11, 13,
          Text.ITALIC_START,
          Text.REGULAR, 14, 17,
          Text.ITALIC_END
        )
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p><em>a b</em> <em>c d</em>, <em>e f</em></p>
      </div>
      </div>
      </body>
      """
    );
  }

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