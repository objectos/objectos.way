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