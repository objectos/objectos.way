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

final class SectionTest extends AbstractAsciiDocTest {

  SectionTest(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  section

  - level 1
  - single paragraph

  012345
  6
  789012
  3
  456789
  0
  123456
  '''
  = doc

  pream

  == L1

  parag
  '''

  P0: ^ H1-0,2 B2,5 $ LF
      ^ $ LF
      ^ B7,12 $ LF
      ^ $ LF
      ^ H2-14,17 B17,19 $ LF
      ^ $ LF
      ^ B21,26 $ LF
      ^ $ EOF

  P1: DOC_START
      HEADING_START 1
      P0 start end
      HEADING_END
      PREAMBLE_START
      PARAGRAPH P0 start end
      PREAMBLE_END
      SECTION_START 1
      HEADING_START 2
      P0 start end
      HEADING_END
      PARAGRAPH P0 start end
      SECTION_END
      DOC_END
  """)
  public final void testCase01() {
    test(
      """
      = doc

      pream

      == L1

      parag
      """,

      p0(
        Token.HEADING, 1, 0, 2,
        Token.BLOB, 2, 5,
        Token.LF,
        Token.LF,
        Token.BLOB, 7, 12,
        Token.LF,
        Token.LF,
        Token.HEADING, 2, 14, 17,
        Token.BLOB, 17, 19,
        Token.LF,
        Token.LF,
        Token.BLOB, 21, 26,
        Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.HEADING_START, 1, Code.TOKENS, 4, 7, Code.HEADING_END, 1,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START, Code.TOKENS, 9, 12, Code.PARAGRAPH_END,
        Code.PREAMBLE_END,

        Code.SECTION_START, 1,
        Code.HEADING_START, 2, Code.TOKENS, 18, 21, Code.HEADING_END, 2,
        Code.PARAGRAPH_START, Code.TOKENS, 23, 26, Code.PARAGRAPH_END,
        Code.SECTION_END,

        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 2, 5),
        t(Text.REGULAR, 7, 12),
        t(Text.REGULAR, 17, 19),
        t(Text.REGULAR, 21, 26)
      ),

      """
      <div id="header">
      <h1>doc</h1>
      </div>
      <div id="content">
      <div id="preamble">
      <div class="sectionbody">
      <div class="paragraph">
      <p>pream</p>
      </div>
      </div>
      </div>
      <div class="sect1">
      <h2 id="_l1">L1</h2>
      <div class="sectionbody">
      <div class="paragraph">
      <p>parag</p>
      </div>
      </div>
      </div>
      </div>
      """
    );
  }

  @Test(description = //
  """
  section

  - level 1
  - level 2
  - single paragraph in each

  012345
  6
  789012
  3
  456789
  0
  123456
  7
  890123
  4
  567890
  '''
  = doc

  pream

  == L1

  sect1

  === 2

  sect2
  '''

  P0: ^ H1-0,2 B2,5 $ LF
      ^ $ LF
      ^ B7,12 $ LF
      ^ $ LF
      ^ H2-14,17 B17,19 $ LF
      ^ $ LF
      ^ B21,26 $ LF
      ^ $ LF
      ^ H3-28,32 B32,33 $ LF
      ^ $ LF
      ^ B35,40 $ LF
      ^ $ EOF

  P1: DOC_START
      HEADING_START 1
      P0 start end
      HEADING_END
      PREAMBLE_START
      PARAGRAPH P0 start end
      PREAMBLE_END
      SECTION_START 1
      HEADING_START 2
      P0 start end
      HEADING_END
      PARAGRAPH P0 start end
      SECTION_END
      SECTION_START 2
      HEADING_START 3
      P0 start end
      HEADING_END
      PARAGRAPH P0 start end
      SECTION_END
      DOC_END
  """)
  public final void testCase02() {
    test(
      """
      = doc

      pream

      == L1

      sect1

      === 2

      sect2
      """,

      p0(
        Token.HEADING, 1, 0, 2,
        Token.BLOB, 2, 5,
        Token.LF,
        Token.LF,
        Token.BLOB, 7, 12,
        Token.LF,
        Token.LF,

        Token.HEADING, 2, 14, 17,
        Token.BLOB, 17, 19,
        Token.LF,
        Token.LF,

        Token.BLOB, 21, 26,
        Token.LF,
        Token.LF,

        Token.HEADING, 3, 28, 32,
        Token.BLOB, 32, 33,
        Token.LF,
        Token.LF,

        Token.BLOB, 35, 40,
        Token.LF,

        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.HEADING_START, 1, Code.TOKENS, 4, 7, Code.HEADING_END, 1,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START, Code.TOKENS, 9, 12, Code.PARAGRAPH_END,
        Code.PREAMBLE_END,

        Code.SECTION_START, 1,
        Code.HEADING_START, 2, Code.TOKENS, 18, 21, Code.HEADING_END, 2,
        Code.PARAGRAPH_START, Code.TOKENS, 23, 26, Code.PARAGRAPH_END,

        Code.SECTION_START, 2,
        Code.HEADING_START, 3, Code.TOKENS, 32, 35, Code.HEADING_END, 3,
        Code.PARAGRAPH_START, Code.TOKENS, 37, 40, Code.PARAGRAPH_END,

        Code.SECTION_END,
        Code.SECTION_END,

        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 2, 5),
        t(Text.REGULAR, 7, 12),
        t(Text.REGULAR, 17, 19),
        t(Text.REGULAR, 21, 26),
        t(Text.REGULAR, 32, 33),
        t(Text.REGULAR, 35, 40)
      ),

      """
      <div id="header">
      <h1>doc</h1>
      </div>
      <div id="content">
      <div id="preamble">
      <div class="sectionbody">
      <div class="paragraph">
      <p>pream</p>
      </div>
      </div>
      </div>
      <div class="sect1">
      <h2 id="_l1">L1</h2>
      <div class="sectionbody">
      <div class="paragraph">
      <p>sect1</p>
      </div>
      <div class="sect2">
      <h3 id="_2">2</h3>
      <div class="paragraph">
      <p>sect2</p>
      </div>
      </div>
      </div>
      </div>
      </div>
      """
    );
  }

  @Test(description = //
  """
  section

  - attribute list
  - level 1
  - no doctitle
  - single paragraph

  012345
  678901
  2
  345678
  '''
  [nam]
  == L1

  sect1
  '''

  P0: ^ [ B1,4 ] $ LF
      ^ H2-6,9 B9,11 $ LF
      ^ $ LF
      ^ B13,18 $ LF
      ^ $ EOF
  """)
  public final void testCase03() {
    test(
      """
      [nam]
      == L1

      sect1
      """,

      p0(
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.BLOB, 1, 4, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END,
        Token.LF,

        Token.HEADING, 2, 6, 9,
        Token.BLOB, 9, 11,
        Token.LF,
        Token.LF,

        Token.BLOB, 13, 18,
        Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,

        Code.ATTR_POSITIONAL, 1, 2, 5,
        Code.SECTION_START, 1,
        Code.HEADING_START, 2, Code.TOKENS, 12, 15, Code.HEADING_END, 2,
        Code.PARAGRAPH_START, Code.TOKENS, 17, 20, Code.PARAGRAPH_END,
        Code.SECTION_END,

        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 9, 11),
        t(Text.REGULAR, 13, 18)
      ),

      """
      <div id="header">
      </div>
      <div id="content">
      <div class="sect1">
      <h2 id="_l1">L1</h2>
      <div class="sectionbody">
      <div class="paragraph">
      <p>sect1</p>
      </div>
      </div>
      </div>
      </div>
      """
    );
  }

  @Test(description = //
  """
  section

  - level 1
  - starts after a UL

  012345
  6
  7890
  1234
  5
  678901
  2
  34
  '''
  == L1

  * a
  * b

  == L2

  c
  '''

  P0: H2-0,2 B3,5 LF
      LF
      * B9,10 LF
      * B13,14 LF
      LF
      H2-16,18 B19,21 LF
      LF
      B23,24 LF
      EOF
  """)
  public final void testCase04() {
    test(
      """
      == L1

      * a
      * b

      == L2

      c
      """,

      p0(
        Token.HEADING, 2, 0, 3, Token.BLOB, 3, 5, Token.LF,
        Token.LF,

        Token.ULIST_ASTERISK, 1, 7, 8, Token.BLOB, 9, 10, Token.LF,
        Token.ULIST_ASTERISK, 1, 11, 12, Token.BLOB, 13, 14, Token.LF,
        Token.LF,

        Token.HEADING, 2, 16, 19, Token.BLOB, 19, 21, Token.LF,
        Token.LF,
        Token.BLOB, 23, 24, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.SECTION_START, 1,
        Code.HEADING_START, 2, Code.TOKENS, 4, 7, Code.HEADING_END, 2,
        Code.ULIST_START,
        Code.LI_START, Code.TOKENS, 13, 16, Code.LI_END,
        Code.LI_START, Code.TOKENS, 21, 24, Code.LI_END,
        Code.ULIST_END,
        Code.SECTION_END,
        Code.SECTION_START, 1,
        Code.HEADING_START, 2, Code.TOKENS, 30, 33, Code.HEADING_END, 2,
        Code.PARAGRAPH_START, Code.TOKENS, 35, 38, Code.PARAGRAPH_END,
        Code.SECTION_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 3, 5),
        t(Text.REGULAR, 9, 10),
        t(Text.REGULAR, 13, 14),
        t(Text.REGULAR, 19, 21),
        t(Text.REGULAR, 23, 24)
      ),

      """
      <div id="header">
      </div>
      <div id="content">
      <div class="sect1">
       <h2 id="_l1">L1</h2>
       <div class="sectionbody">
        <div class="ulist">
         <ul>
          <li><p>a</p></li>
          <li><p>b</p></li>
         </ul>
        </div>
       </div>
      </div>
      <div class="sect1">
       <h2 id="_l2">L2</h2>
       <div class="sectionbody">
        <div class="paragraph">
         <p>c</p>
        </div>
       </div>
      </div>
      </div>
      """
    );
  }

  @Test(description = //
  """
  section level reduction

  012345
  6
  78
  9
  01234
  5
  67
  '''
  === A

  b

  == C

  d
  '''
  """)
  public final void testCase05() {
    test(
      """
      === A

      b

      == C

      d
      """,

      p0(
        Token.HEADING, 3, 0, 4, Token.BLOB, 4, 5, Token.LF,
        Token.LF,
        Token.BLOB, 7, 8, Token.LF,
        Token.LF,
        Token.HEADING, 2, 10, 13, Token.BLOB, 13, 14, Token.LF,
        Token.LF,
        Token.BLOB, 16, 17, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.SECTION_START, 2,
        Code.HEADING_START, 3, Code.TOKENS, 4, 7, Code.HEADING_END, 3,
        Code.PARAGRAPH_START, Code.TOKENS, 9, 12, Code.PARAGRAPH_END,
        Code.SECTION_END,
        Code.SECTION_START, 1,
        Code.HEADING_START, 2, Code.TOKENS, 18, 21, Code.HEADING_END, 2,
        Code.PARAGRAPH_START, Code.TOKENS, 23, 26, Code.PARAGRAPH_END,
        Code.SECTION_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 4, 5),
        t(Text.REGULAR, 7, 8),
        t(Text.REGULAR, 13, 14),
        t(Text.REGULAR, 16, 17)
      ),

      """
      <div id="header">
      </div>
      <div id="content">
      <div class="sect2">
       <h3 id="_a">A</h3>
       <div class="paragraph">
        <p>b</p>
       </div>
      </div>
      <div class="sect1">
       <h2 id="_c">C</h2>
       <div class="sectionbody">
        <div class="paragraph">
         <p>d</p>
        </div>
       </div>
      </div>
      </div>
      """
    );
  }

  @Test(description = //
  """
  title is constrained monospace

  0123456
  7
  89
  '''
  == `A`

  b
  '''
  """)
  public final void testCase06() {
    test(
      """
      == `A`

      b
      """,

      p0(
        Token.HEADING, 2, 0, 3, Token.MONO_START, 3, Token.BLOB, 4, 5, Token.MONO_END, 5, Token.LF,
        Token.LF,
        Token.BLOB, 8, 9, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.SECTION_START, 1,
        Code.HEADING_START, 2, Code.TOKENS, 4, 11, Code.HEADING_END, 2,
        Code.PARAGRAPH_START, Code.TOKENS, 13, 16, Code.PARAGRAPH_END,
        Code.SECTION_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.MONOSPACE_START, Text.REGULAR, 4, 5, Text.MONOSPACE_END),
        t(Text.REGULAR, 8, 9)
      ),

      """
      <div id="header">
      </div>
      <div id="content">
      <div class="sect1">
       <h2 id="_a"><code>A</code></h2>
       <div class="sectionbody">
        <div class="paragraph">
         <p>b</p>
        </div>
       </div>
      </div>
      </div>
      """
    );
  }

}