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