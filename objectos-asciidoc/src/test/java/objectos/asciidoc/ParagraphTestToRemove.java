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

final class ParagraphTestToRemove extends AbstractAsciiDocTest {

  ParagraphTestToRemove(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  multiline paragraph. Line ends in a imacro.

  012345678
  90
  '''
  a i:b[c]
  d
  '''
  """)
  public void testCase04() {
    test(
      """
      a i:b[c]
      d
      """,

      p0(
        Token.BLOB, 0, 2,
        Token.INLINE_MACRO, 2, 3,
        Token.BLOB, 4, 5,
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.BLOB, 6, 7, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END,
        Token.LF,
        Token.BLOB, 9, 10, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.TOKENS, 0, 3,
        Code.INLINE_MACRO, 2, 3,
        Code.MACRO_TARGET, 4, 5,
        Code.ATTR_POSITIONAL, 1, 11, 14,
        Code.TOKENS, 16, 20,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 0, 2),
        t(Text.REGULAR, 8, 10)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p>a <a href="b">c</a> d</p>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  imacro in the middle of a sentence

  01234567890
  '''
  a i:b[c] d
  '''
  """)
  public void testCase05() {
    test(
      """
      a i:b[c] d
      """,

      p0(
        Token.BLOB, 0, 2,
        Token.INLINE_MACRO, 2, 3,
        Token.BLOB, 4, 5,
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.BLOB, 6, 7, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END,
        Token.BLOB, 8, 10, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.TOKENS, 0, 3,
        Code.INLINE_MACRO, 2, 3,
        Code.MACRO_TARGET, 4, 5,
        Code.ATTR_POSITIONAL, 1, 11, 14,
        Code.TOKENS, 16, 19,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 0, 2),
        t(Text.REGULAR, 8, 10)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p>a <a href="b">c</a> d</p>
      </div>
      </div>
      </body>
      """
    );
  }

}
