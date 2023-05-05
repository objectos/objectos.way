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

final class ListingBlockTestToRemove extends AbstractAsciiDocTest {

  ListingBlockTestToRemove(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  listing block code with ']'

  01234
  56789
  01234
  '''
  ----
  foo]
  ----
  '''
  """)
  public void testCase02() {
    test(
      """
      ----
      foo]
      ----
      """,

      p0(
        Token.LISTING_BLOCK_DELIM, 4, Token.LF,
        Token.BLOB, 5, 9, Token.LF,
        Token.LISTING_BLOCK_DELIM, 4, Token.LF,

        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.LISTING_BLOCK_START,
        Code.VERBATIM, 3, 6,
        Code.LISTING_BLOCK_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="listingblock">
      <div class="content">
      <pre>foo]</pre>
      </div>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  listing block code with '[x]'

  01234
  5678901
  23456
  '''
  ----
  a: b[2]
  ----
  '''
  """)
  public void testCase03() {
    test(
      """
      ----
      a: b[2]
      ----
      """,

      p0(
        Token.LISTING_BLOCK_DELIM, 4, Token.LF,
        Token.INLINE_MACRO, 5, 6,
        Token.BLOB, 7, 9,
        Token.ATTR_LIST_START, Token.ATTR_VALUE_START,
        Token.BLOB, 10, 11,
        Token.ATTR_VALUE_END, Token.ATTR_LIST_END, Token.LF,
        Token.LISTING_BLOCK_DELIM, 4,
        Token.LF, Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.LISTING_BLOCK_START,
        Code.VERBATIM, 3, 16,
        Code.LISTING_BLOCK_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="listingblock">
      <div class="content">
      <pre>a: b[2]</pre>
      </div>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  listing block code with '_'

  ----
  foo _bar
  ----
  '''
  """)
  public void testCase04() {
    test(
      """
      ----
      foo _bar
      ----
      """,

      p0(
        Token.LISTING_BLOCK_DELIM, 4, Token.LF,
        Token.BLOB, 5, 9,
        Token.ITALIC_START, 9,
        Token.BLOB, 10, 13, Token.LF,
        Token.LISTING_BLOCK_DELIM, 4,
        Token.LF, Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.LISTING_BLOCK_START,
        Code.VERBATIM, 3, 11,
        Code.LISTING_BLOCK_END,
        Code.PREAMBLE_END,
        Code.DOCUMENT_END
      ),

      docAttr(),

      p2(),

      """
      <body>
      <div id="header">
      </div>
      <div id="content">
      <div class="listingblock">
      <div class="content">
      <pre>foo _bar</pre>
      </div>
      </div>
      </div>
      </body>
      """
    );
  }

}