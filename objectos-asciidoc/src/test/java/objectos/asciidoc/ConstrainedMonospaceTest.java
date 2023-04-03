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

final class ConstrainedMonospaceTest extends AbstractAsciiDocTest {

  ConstrainedMonospaceTest(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  monospace

  - constrained
  - words only
  - well-formed

  0123456789012
  '''
  `a` `b`, `c`
  '''

  P0: ^ ` B1,2 ` B3,4 ` B5,6 ` B7,9 ` B10,11 ` $ LF
      ^ $ EOF

  P2: ^ <M R1,2 M> R3,4 <M R5,6 M> R7,9 <M R10,11 M> $ LF
      ^ $ EOF
  """)
  public void testCase01() {
    test(
      """
      `a` `b`, `c`
      """,

      p0(
        Token.MONO_START, 0,
        Token.BLOB, 1, 2,
        Token.MONO_END, 2,
        Token.BLOB, 3, 4,
        Token.MONO_START, 4,
        Token.BLOB, 5, 6,
        Token.MONO_END, 6,
        Token.BLOB, 7, 9,
        Token.MONO_START, 9,
        Token.BLOB, 10, 11,
        Token.MONO_END, 11,
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
          Text.MONOSPACE_START,
          Text.REGULAR, 1, 2,
          Text.MONOSPACE_END,
          Text.REGULAR, 3, 4,
          Text.MONOSPACE_START,
          Text.REGULAR, 5, 6,
          Text.MONOSPACE_END,
          Text.REGULAR, 7, 9,
          Text.MONOSPACE_START,
          Text.REGULAR, 10, 11,
          Text.MONOSPACE_END
        )
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p><code>a</code> <code>b</code>, <code>c</code></p>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  monospace

  - constrained
  - phrases
  - well-formed

            1
  012345678901234567
  '''
  `a b` `c d`, `e f`
  '''

  L0: ^ ` W1,2 SP W3,4 ` SP ` W7,8 SP W9,10 ` X11,12 SP ` W14,15 SP W16,17 ` $ LF
      ^ $ EOF

  L1: ^ <M R1,4 M> R4,6 <M R7,10 M> R11,13 <M R14,17 M> $ LF
      ^ $ EOF
  """)
  public void testCase02() {
    test(
      """
      `a b` `c d`, `e f`
      """,

      p0(
        Token.MONO_START, 0,
        Token.BLOB, 1, 4,
        Token.MONO_END, 4,
        Token.BLOB, 5, 6,
        Token.MONO_START, 6,
        Token.BLOB, 7, 10,
        Token.MONO_END, 10,
        Token.BLOB, 11, 13,
        Token.MONO_START, 13,
        Token.BLOB, 14, 17,
        Token.MONO_END, 17,
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
          Text.MONOSPACE_START,
          Text.REGULAR, 1, 4,
          Text.MONOSPACE_END,
          Text.REGULAR, 5, 6,
          Text.MONOSPACE_START,
          Text.REGULAR, 7, 10,
          Text.MONOSPACE_END,
          Text.REGULAR, 11, 13,
          Text.MONOSPACE_START,
          Text.REGULAR, 14, 17,
          Text.MONOSPACE_END
        )
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p><code>a b</code> <code>c d</code>, <code>e f</code></p>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  monospace

  - in the middle of paragraph

  01234567
  '''
  a `b` c
  '''
  """)
  public void testCase03() {
    test(
      """
      a `b` c
      """,

      p0(
        Token.BLOB, 0, 2,
        Token.MONO_START, 2, Token.BLOB, 3, 4, Token.MONO_END, 4,
        Token.BLOB, 5, 7, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.TOKENS, 0, 13,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 0, 2,
          Text.MONOSPACE_START, Text.REGULAR, 3, 4, Text.MONOSPACE_END,
          Text.REGULAR, 5, 7)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p>a <code>b</code> c</p>
      </div>
      </div>
      </body>
      """
    );
  }

}