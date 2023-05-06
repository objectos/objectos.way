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

final class SourceCodeBlockTestToRemove extends AbstractAsciiDocTest {

  SourceCodeBlockTestToRemove(AsciiDocTest outer) { super(outer); }

  @Test(description = //
  """
  source code block

  - implict source style
  - delimited
  - indented content

  01234
  56789
  01
  234567
  89012
  '''
  [,a]
  ----
  b
      c
  ----
  '''
  """)
  public void testCase03() {
    test(
      """
      [,a]
      ----
      b
          c
      ----
      """,

      p0(
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.ATTR_VALUE_END,
        Token.SEPARATOR, 1, 2,
        Token.ATTR_VALUE_START, Token.BLOB, 2, 3, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END, Token.LF,
        Token.LISTING_BLOCK_DELIM, 4, Token.LF,
        Token.BLOB, 10, 11, Token.LF,
        Token.LITERALI, 12, 16, Token.BLOB, 16, 17, Token.LF,
        Token.LISTING_BLOCK_DELIM, 4, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.ATTR_POSITIONAL, 1, 2, 2,
        Code.ATTR_POSITIONAL, 2, 7, 10,
        Code.LISTING_BLOCK_START,
        Code.VERBATIM, 16, 26,
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
      <pre class="highlight"><code class="language-a" data-lang="a">b
          c</code></pre>
      </div>
      </div>
      </div>
      </body>
      """
    );
  }

  @Test(description = //
  """
  source code block

  - implict source style
  - delimited
  - contains Token.BOLD_END

  01234
  56789
  0123
  45678
  '''
  [,a]
  ----
  b*;
  ----
  '''
  """)
  public void testCase04() {
    test(
      """
      [,a]
      ----
      b*;
      ----
      """,

      p0(
        Token.ATTR_LIST_START,
        Token.ATTR_VALUE_START, Token.ATTR_VALUE_END,
        Token.SEPARATOR, 1, 2,
        Token.ATTR_VALUE_START, Token.BLOB, 2, 3, Token.ATTR_VALUE_END,
        Token.ATTR_LIST_END, Token.LF,
        Token.LISTING_BLOCK_DELIM, 4, Token.LF,
        Token.BLOB, 10, 11, Token.BOLD_END, 11, Token.BLOB, 12, 13, Token.LF,
        Token.LISTING_BLOCK_DELIM, 4, Token.LF,
        Token.EOF
      ),

      p1(
        Code.DOCUMENT_START,
        Code.PREAMBLE_START,
        Code.ATTR_POSITIONAL, 1, 2, 2,
        Code.ATTR_POSITIONAL, 2, 7, 10,
        Code.LISTING_BLOCK_START,
        Code.VERBATIM, 16, 24,
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
      <pre class="highlight"><code class="language-a" data-lang="a">b*;</code></pre>
      </div>
      </div>
      </div>
      </body>
      """
    );
  }

}