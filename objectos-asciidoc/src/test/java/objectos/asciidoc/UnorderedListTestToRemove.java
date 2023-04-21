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

final class UnorderedListTestToRemove extends AbstractAsciiDocTest {

  UnorderedListTestToRemove(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  unordered list

  - nested unordered list
  - asterisk

  0123
  45678
  90123
  4567
  '''
  * a
  ** b
  ** c
  * d
  '''

  P0: *0,1 B2,3 $ LF
      **4,6 B7,8 $ LF
      **9,11 B12,13 $ LF
      *14,15 B16,17 $ LF
      EOF
  """)
  public final void testCase04() {
    test(
      """
      * a
      ** b
      ** c
      * d
      """,

      p0(
        Token.ULIST_ASTERISK, 1, 0, 1, Token.BLOB, 2, 3, Token.LF,
        Token.ULIST_ASTERISK, 2, 4, 6, Token.BLOB, 7, 8, Token.LF,
        Token.ULIST_ASTERISK, 2, 9, 11, Token.BLOB, 12, 13, Token.LF,
        Token.ULIST_ASTERISK, 1, 14, 15, Token.BLOB, 16, 17, Token.LF,

        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.ULIST_START,

        Code.LI_START, Code.TOKENS, 4, 7,

        Code.ULIST_START,
        Code.LI_START, Code.TOKENS, 12, 15, Code.LI_END,

        Code.LI_START, Code.TOKENS, 20, 23, Code.LI_END,
        Code.ULIST_END,
        Code.LI_END,

        Code.LI_START, Code.TOKENS, 28, 31, Code.LI_END,

        Code.ULIST_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 2, 3),
        t(Text.REGULAR, 7, 8),
        t(Text.REGULAR, 12, 13),
        t(Text.REGULAR, 16, 17)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="ulist">
      <ul>
      <li><p>a</p>
      <div class="ulist">
      <ul>
      <li><p>b</p></li>
      <li><p>c</p></li>
      </ul>
      </div></li>
      <li><p>d</p></li>
      </ul>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  unordered list

  - indented nested unordered list
  - asterisk

  0123
  456789
  012345
  6789
  '''
  * a
   ** b
   ** c
  * d
  '''

  P0: * B2,3 LF
      ** B8,9 LF
      ** B14,15 LF
      * B18,19 LF
      EOF
  """)
  public final void testCase05() {
    test(
      """
      * a
       ** b
       ** c
      * d
      """,

      p0(
        Token.ULIST_ASTERISK, 1, 0, 1, Token.BLOB, 2, 3, Token.LF,
        Token.ULIST_ASTERISK, 2, 4, 7, Token.BLOB, 8, 9, Token.LF,
        Token.ULIST_ASTERISK, 2, 10, 13, Token.BLOB, 14, 15, Token.LF,
        Token.ULIST_ASTERISK, 1, 16, 17, Token.BLOB, 18, 19, Token.LF,

        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.ULIST_START,

        Code.LI_START, Code.TOKENS, 4, 7,

        Code.ULIST_START,
        Code.LI_START, Code.TOKENS, 12, 15, Code.LI_END,

        Code.LI_START, Code.TOKENS, 20, 23, Code.LI_END,
        Code.ULIST_END,
        Code.LI_END,

        Code.LI_START, Code.TOKENS, 28, 31, Code.LI_END,

        Code.ULIST_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 2, 3),
        t(Text.REGULAR, 8, 9),
        t(Text.REGULAR, 14, 15),
        t(Text.REGULAR, 18, 19)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="ulist">
      <ul>
      <li><p>a</p>
      <div class="ulist">
      <ul>
      <li><p>b</p></li>
      <li><p>c</p></li>
      </ul>
      </div></li>
      <li><p>d</p></li>
      </ul>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  unordered list

  - simple elements
  - asterisk
  - single level
  - section

  01234
  5
  67890
  1
  2345
  6789
  '''
  == A

  b c:

  * d
  * e
  '''
  """)
  public final void testCase06() {
    test(
      """
      == A

      b c:

      * d
      * e
      """,

      p0(
        Token.HEADING, 2, 0, 3, Token.BLOB, 3, 4, Token.LF,
        Token.LF,
        Token.BLOB, 6, 8, Token.BLOB, 8, 10, Token.LF,
        Token.LF,
        Token.ULIST_ASTERISK, 1, 12, 13, Token.BLOB, 14, 15, Token.LF,
        Token.ULIST_ASTERISK, 1, 16, 17, Token.BLOB, 18, 19, Token.LF,

        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.SECTION_START, 1,
        Code.HEADING_START, 2,
        Code.TOKENS, 4, 7,
        Code.HEADING_END, 2,
        Code.PARAGRAPH_START,
        Code.TOKENS, 9, 15,
        Code.PARAGRAPH_END,

        Code.ULIST_START,
        Code.LI_START, Code.TOKENS, 21, 24, Code.LI_END,

        Code.LI_START, Code.TOKENS, 29, 32, Code.LI_END,
        Code.ULIST_END,

        Code.SECTION_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 3, 4),
        t(Text.REGULAR, 6, 10),
        t(Text.REGULAR, 14, 15),
        t(Text.REGULAR, 18, 19)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
        <div class="sect1">
          <h2 id="_a">A</h2>
          <div class="sectionbody">
            <div class="paragraph">
              <p>b c:</p>
            </div>
            <div class="ulist">
              <ul>
              <li><p>d</p></li>
              <li><p>e</p></li>
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
  unordered list

  - text with monospace

  0123456789
  0123
  '''
  * a `b` c
  * d
  '''
  """)
  public final void testCase07() {
    test(
      """
      * a `b` c
      * d
      """,

      p0(
        Token.ULIST_ASTERISK, 1, 0, 1,
        Token.BLOB, 2, 4,
        Token.MONO_START, 4, Token.BLOB, 5, 6, Token.MONO_END, 6,
        Token.BLOB, 7, 9, Token.LF,

        Token.ULIST_ASTERISK, 1, 10, 11, Token.BLOB, 12, 13, Token.LF,

        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.ULIST_START,

        Code.LI_START, Code.TOKENS, 4, 17, Code.LI_END,

        Code.LI_START, Code.TOKENS, 22, 25, Code.LI_END,

        Code.ULIST_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 2, 4,
          Text.MONOSPACE_START, Text.REGULAR, 5, 6, Text.MONOSPACE_END,
          Text.REGULAR, 7, 9),
        t(Text.REGULAR, 12, 13)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="ulist">
      <ul>
      <li><p>a <code>b</code> c</p></li>
      <li><p>d</p></li>
      </ul>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  ul should end before the paragraph

  0123
  4567
  8
  90
  '''
  * a
  * b

  c
  '''
  """)
  public void testCase08() {
    test(
      """
      * a
      * b

      c
      """,

      p0(
        Token.ULIST_ASTERISK, 1, 0, 1, Token.BLOB, 2, 3, Token.LF,
        Token.ULIST_ASTERISK, 1, 4, 5, Token.BLOB, 6, 7, Token.LF,
        Token.LF,
        Token.BLOB, 9, 10, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.ULIST_START,
        Code.LI_START, Code.TOKENS, 4, 7, Code.LI_END,
        Code.LI_START, Code.TOKENS, 12, 15, Code.LI_END,
        Code.ULIST_END,

        Code.PARAGRAPH_START,
        Code.TOKENS, 17, 20,
        Code.PARAGRAPH_END,

        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 2, 3),
        t(Text.REGULAR, 6, 7),
        t(Text.REGULAR, 9, 10)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="ulist">
       <ul>
        <li><p>a</p></li>
        <li><p>b</p></li>
       </ul>
      </div>
      <div class="paragraph">
       <p>c</p>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  ul should end before section (ends with imacro)

  0123
  456789012
  3
  45678
  '''
  * a
  * i:b[c]

  == D
  '''
  """)
  public void testCase09() {
    test(
      """
      * a
      * i:b[c]

      == D
      """,

      p0(
        Token.ULIST_ASTERISK, 1, 0, 1, Token.BLOB, 2, 3, Token.LF,
        Token.ULIST_ASTERISK, 1, 4, 5,
        Token.INLINE_MACRO, 6, 7,
        Token.BLOB, 8, 9,
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.BLOB, 10, 11, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END, Token.LF,
        Token.LF,
        Token.HEADING, 2, 14, 17, Token.BLOB, 17, 18, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.ULIST_START,
        Code.LI_START, Code.TOKENS, 4, 7, Code.LI_END,

        Code.LI_START,
        Code.INLINE_MACRO, 6, 7,
        Code.MACRO_TARGET, 8, 9,
        Code.ATTR_POSITIONAL, 1, 20, 23,
        Code.LI_END,

        Code.ULIST_END,
        Code.PREAMBLE_END,

        Code.SECTION_START, 1,
        Code.HEADING_START, 2, Code.TOKENS, 31, 34, Code.HEADING_END, 2,
        Code.SECTION_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 2, 3),
        t(Text.REGULAR, 17, 18)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="ulist">
       <ul>
        <li><p>a</p></li>
        <li><p><a href="b">c</a></p></li>
       </ul>
      </div>
      <div class="sect1">
       <h2 id="_d">D</h2>
       <div class="sectionbody">
       </div>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  ul should end before block attr list

  0123
  4
  567
  8901
  '''
  * a

  []
  * b
  '''
  """)
  public void testCase10() {
    test(
      """
      * a

      []
      * b
      """,

      p0(
        Token.ULIST_ASTERISK, 1, 0, 1, Token.BLOB, 2, 3, Token.LF,
        Token.LF,
        Token.ATTR_LIST_START, Token.ATTR_LIST_END, Token.LF,
        Token.ULIST_ASTERISK, 1, 8, 9, Token.BLOB, 10, 11, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.ULIST_START,
        Code.LI_START, Code.TOKENS, 4, 7, Code.LI_END,
        Code.ULIST_END,

        Code.ULIST_START,
        Code.LI_START, Code.TOKENS, 16, 19, Code.LI_END,
        Code.ULIST_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 2, 3),
        t(Text.REGULAR, 10, 11)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="ulist">
       <ul>
        <li><p>a</p></li>
       </ul>
      </div>
      <div class="ulist">
       <ul>
        <li><p>b</p></li>
       </ul>
      </div>
      </div>
      </body>
      """
    );
  }

}