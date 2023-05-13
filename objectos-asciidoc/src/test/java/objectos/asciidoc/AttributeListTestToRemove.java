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

final class AttributeListTestToRemove extends AbstractAsciiDocTest {

  AttributeListTestToRemove(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  Named attribute

  - well-formed

  012345678
  90
  '''
  [,a,b=c]
  d
  '''
  """)
  public void testCase02() {
    test(
      """
      [,a,b=c]
      d
      """,

      p0(
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.ATTR_VALUE_END,
        Token.SEPARATOR, 1, 2,
        Token.ATTR_VALUE_START, Token.BLOB, 2, 3, Token.ATTR_VALUE_END,
        Token.SEPARATOR, 3, 4,
        Token.ATTR_NAME, 4, 5,
        Token.ATTR_VALUE_START, Token.BLOB, 6, 7, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END, Token.LF,
        Token.BLOB, 9, 10, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.ATTR_POSITIONAL, 1, 2, 2,
        Code.ATTR_POSITIONAL, 2, 7, 10,
        Code.ATTR_NAMED, 4, 5, 18, 21,
        Code.PARAGRAPH_START,
        Code.TOKENS, 24, 27,
        Code.PARAGRAPH_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(
        t(Text.REGULAR, 9, 10)
      ),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="paragraph">
      <p>d</p>
      </div>
      </div>
      </body>
      """
    );
  }

}