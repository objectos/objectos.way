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

final class InlineMacroTestToRemove extends AbstractAsciiDocTest {

  InlineMacroTestToRemove(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  inline macro

  - unordered list
  - section

  01234
  5
  678901234
  567890123
  '''
  == A

  * i:b[c]
  * i:d[e]
  '''
  """)
  public void testCase03() {
    test(
      """
      == A

      * i:b[c]
      * i:d[e]
      """,

      p0(
        Token.HEADING, 2, 0, 3, Token.BLOB, 3, 4, Token.LF,
        Token.LF,

        Token.ULIST_ASTERISK, 1, 6, 7,
        Token.INLINE_MACRO, 8, 9, Token.BLOB, 10, 11,
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.BLOB, 12, 13, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END,
        Token.LF,

        Token.ULIST_ASTERISK, 1, 15, 16,
        Token.INLINE_MACRO, 17, 18, Token.BLOB, 19, 20,
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.BLOB, 21, 22, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END,
        Token.LF,

        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.SECTION_START, 1,
        Code.HEADING_START, 2,
        Code.TOKENS, 4, 7,
        Code.HEADING_END, 2,

        Code.ULIST_START,
        Code.LI_START,
        Code.INLINE_MACRO, 8, 9,
        Code.MACRO_TARGET, 10, 11,
        Code.ATTR_POSITIONAL, 1, 21, 24,
        Code.LI_END,

        Code.LI_START,
        Code.INLINE_MACRO, 17, 18,
        Code.MACRO_TARGET, 19, 20,
        Code.ATTR_POSITIONAL, 1, 39, 42,
        Code.LI_END,

        Code.ULIST_END,
        Code.SECTION_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 3, 4)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
        <div class="sect1">
          <h2 id="_a">A</h2>
          <div class="sectionbody">
            <div class="ulist">
              <ul>
                <li><p><a href="b">c</a></p></li>
                <li><p><a href="d">e</a></p></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  imacro ends the sentence

  0123456789
  0
  12
  '''
  a i:b[c].

  d
  '''
  """)
  public void testCase04() {
    test(
      """
      a i:b[c].

      d
      """,

      p0(
        Token.BLOB, 0, 2,
        Token.INLINE_MACRO, 2, 3,
        Token.BLOB, 4, 5,
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.BLOB, 6, 7, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END,
        Token.BLOB, 8, 9,
        Token.LF,
        Token.LF,
        Token.BLOB, 11, 12, Token.LF,

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
        Code.PARAGRAPH_START,
        Code.TOKENS, 21, 24,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 0, 2),
        t(Text.REGULAR, 8, 9),
        t(Text.REGULAR, 11, 12)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
       <p>a <a href="b">c</a>.</p>
      </div>
      <div class="paragraph">
       <p>d</p>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  not imacro: target with spaces

  012345678
  '''
  i:b c[d]
  '''
  """)
  public void testCase05() {
    test(
      """
      i:b c[d]
      """,

      p0(
        Token.BLOB, 0, 8,
        Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.TOKENS, 0, 3,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 0, 8)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
       <p>i:b c[d]</p>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  imacro + monospace

  012345678
  '''
  i:b[`c`]
  '''
  """)
  public void testCase06() {
    test(
      """
      i:b[`c`]
      """,

      p0(
        Token.INLINE_MACRO, 0, 1,
        Token.BLOB, 2, 3,
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START,
        Token.MONO_START, 4, Token.BLOB, 5, 6, Token.MONO_END, 6,
        Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END,
        Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.INLINE_MACRO, 0, 1,
        Code.MACRO_TARGET, 2, 3,
        Code.ATTR_POSITIONAL, 1, 8, 15,
        Code.TOKENS, 17, 17,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t()
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
       <p><a href="b"><code>c</code></a></p>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  imacro + spaces + monospace

  0123456789012
  '''
  i:b[c `d` e]
  '''
  """)
  public void testCase07() {
    test(
      """
      i:b[c `d` e]
      """,

      p0(
        Token.INLINE_MACRO, 0, 1,
        Token.BLOB, 2, 3,
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START,
        Token.BLOB, 4, 6,
        Token.MONO_START, 6, Token.BLOB, 7, 8, Token.MONO_END, 8,
        Token.BLOB, 9, 11,
        Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END,
        Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.INLINE_MACRO, 0, 1,
        Code.MACRO_TARGET, 2, 3,
        Code.ATTR_POSITIONAL, 1, 8, 21,
        Code.TOKENS, 23, 23,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t()
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
       <p><a href="b">c <code>d</code> e</a></p>
      </div>
      </div>
      </body>
      """
    );
  }

}