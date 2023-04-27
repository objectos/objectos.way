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

final class UrlMacroTestToRemove extends AbstractAsciiDocTest {

  UrlMacroTestToRemove(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  - https
  - well-formed
  - comma in attrlist

  0123456789012345
  '''
  https://a[b, c]
  '''
  """)
  public void testCase03() {
    test(
      """
      https://a[b, c]
      """,

      p0(
        Token.INLINE_MACRO, 0, 5,
        Token.BLOB, 6, 9,
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.BLOB, 10, 11, Token.ATTR_VALUE_END,
        Token.SEPARATOR, 11, 13,
        Token.ATTR_VALUE_START, Token.BLOB, 13, 14, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END,
        Token.LF,

        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.PARAGRAPH_START,
        Code.URL_MACRO, 0, 9,
        Code.URL_TARGET_START,
        Code.TOKENS, 8, 11, Code.TOKENS, 12, 15, Code.TOKENS, 16, 19,
        Code.URL_TARGET_END,
        Code.TOKENS, 21, 21,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 10, 11),
        t(Text.REGULAR, 11, 13),
        t(Text.REGULAR, 13, 14),
        t()
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p><a href="https://a">b, c</a></p>
      </div>
      </div>
      </body>
      """
    );
  }

}