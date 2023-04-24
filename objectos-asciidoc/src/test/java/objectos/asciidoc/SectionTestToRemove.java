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

final class SectionTestToRemove extends AbstractAsciiDocTest {

  SectionTestToRemove(AsciiDocTest outer) { super(outer); }

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