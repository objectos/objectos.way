/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class SyntaxJavaTest {

  private final Syntax.Java hl = Syntax.Java.create(config -> {
    config.set(Syntax.Java.ANNOTATION, "annotation");
    config.set(Syntax.Java.COMMENT, "comment");
    config.set(Syntax.Java.KEYWORD, "keyword");
    config.set(Syntax.Java.STRING_LITERAL, "string");
  });

  @Test
  public void eolComment01() {
    test(
        """
        // foo""",

        """
        <div><span class="comment">// foo</span></div>
        """
    );
  }

  @Test
  public void eolComment02() {
    test(
        """
        // foo
        """,

        """
        <div><span class="comment">// foo</span></div>
        """
    );
  }

  @Test
  public void eolComment03() {
    test(
        """
        // one\r
        // two\r
        """,

        """
        <div><span class="comment">// one</span></div>
        <div><span class="comment">// two</span></div>
        """
    );
  }

  @Test
  public void eolComment04() {
    test(
        """
        /""",

        """
        <div><span>/</span></div>
        """
    );
  }

  @Test
  public void stringLiteral01() {
    test(
        """
        "abc\"""",

        """
        <div><span class="string">"abc"</span></div>
        """
    );
  }

  private void test(String java, String expected) {
    final Html.Component component;
    component = hl.highlight(java);

    assertEquals(component.toHtml(), expected);
  }

}