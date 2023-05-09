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

final class ConstrainedBoldTestToRemove extends AbstractAsciiDocTest {

  ConstrainedBoldTestToRemove(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  bold

  - constrained
  - phrases
  - well-formed

            1
  012345678901234567
  '''
  *a b* *c d*, *e f*
  '''

  L0: ^ * W1,2 SP W3,4 * SP * W7,8 SP W9,10 * X11,12 SP * W14,15 SP W16,17 * $ LF
      ^ $ EOF

  L1: ^ <B R1,4 B> R4,6 <B R7,10 B> R11,13 <B R14,17 B> $ LF
      ^ $ EOF
  """)
  public void testCase02() {
    test(
      """
      *a b* *c d*, *e f*
      """,

      p0(
        Token.BOLD_START, 0,
        Token.BLOB, 1, 4,
        Token.BOLD_END, 4,
        Token.BLOB, 5, 6,
        Token.BOLD_START, 6,
        Token.BLOB, 7, 10,
        Token.BOLD_END, 10,
        Token.BLOB, 11, 13,
        Token.BOLD_START, 13,
        Token.BLOB, 14, 17,
        Token.BOLD_END, 17,
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
          Text.BOLD_START,
          Text.REGULAR, 1, 4,
          Text.BOLD_END,
          Text.REGULAR, 5, 6,
          Text.BOLD_START,
          Text.REGULAR, 7, 10,
          Text.BOLD_END,
          Text.REGULAR, 11, 13,
          Text.BOLD_START,
          Text.REGULAR, 14, 17,
          Text.BOLD_END
        )
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p><strong>a b</strong> <strong>c d</strong>, <strong>e f</strong></p>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  bold enclosing italic

  012345678
  '''
  a *_b_*.
  '''
  """)
  public void testCase03() {
    test(
      """
      a *_b_*.
      """,

      p0(
        Token.BLOB, 0, 2,
        Token.BOLD_START, 2,
        Token.ITALIC_START, 3,
        Token.BLOB, 4, 5,
        Token.ITALIC_END, 5,
        Token.BOLD_END, 6,
        Token.BLOB, 7, 8,
        Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.TOKENS, 0, 17,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(
          Text.REGULAR, 0, 2,
          Text.BOLD_START,
          Text.ITALIC_START,
          Text.REGULAR, 4, 5,
          Text.ITALIC_END,
          Text.BOLD_END,
          Text.REGULAR, 7, 8
        )
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p>a <strong><em>b</em></strong>.</p>
      </div>
      </div>
      </body>
      """
    );
  }

}