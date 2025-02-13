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
  public void annotation01() {
    test(
        """
        @Target(ElementType.TYPE_USE)
        @interface X {}
        """,

        """
        <span data-line="1"><span data-high="annotation">@Target</span><span>(ElementType.TYPE_USE)</span></span>\
        <span data-line="2"><span data-high="annotation">@interface</span><span> X {}</span></span>
        """
    );
  }

  @Test
  public void annotation02() {
    test(
        """
        private @X String a;
        """,

        """
        <span data-line="1"><span data-high="keyword">private</span><span> </span><span data-high="annotation">@X</span><span> String a;</span></span>
        """
    );
  }

  @Test
  public void eolComment01() {
    test(
        """
        // foo""",

        """
        <span data-line="1"><span data-high="comment">// foo</span></span>
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
        <span data-line="1"><span data-high="comment">// foo</span></span>
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
        <span data-line="1"><span data-high="comment">// one</span></span>\
        <span data-line="2"><span data-high="comment">// two</span></span>
        """
    );
  }

  @Test
  public void eolComment04() {
    test(
        """
        /""",

        """
        <span data-line="1"><span>/</span></span>
        """
    );
  }

  @Test
  public void eolComment05() {
    test(
        """
        clazz = getClass(); // 1""",

        """
        <span data-line="1"><span>clazz = getClass(); </span><span data-high="comment">// 1</span></span>
        """
    );
  }

  @Test
  public void keyword01() {
    test(
        """
        public""",

        """
        <span data-line="1"><span data-high="keyword">public</span></span>
        """
    );
  }

  @Test
  public void stringLiteral01() {
    test(
        """
        "\"""",

        """
        <span data-line="1"><span data-high="string">""</span></span>
        """
    );
  }

  @Test
  public void stringLiteral02() {
    test(
        """
        "abc\"""",

        """
        <span data-line="1"><span data-high="string">"abc"</span></span>
        """
    );
  }

  @Test
  public void textBlock01() {
    test("""
        \"""
        abc\"\"\"""",

        """
        <span data-line="1"><span data-high="string">\"""</span></span>\
        <span data-line="2"><span data-high="string">abc\"""</span></span>
        """
    );
  }

  @Test
  public void textBlock02() {
    test("""
        \"""
        abc\"""
        """,

        """
        <span data-line="1"><span data-high="string">\"""</span></span>\
        <span data-line="2"><span data-high="string">abc\"""</span></span>
        """
    );
  }

  @Test
  public void textBlock03() {
    test("""
        \"""
        abc
        \"""
        """,

        """
        <span data-line="1"><span data-high="string">\"""</span></span>\
        <span data-line="2"><span data-high="string">abc</span></span>\
        <span data-line="3"><span data-high="string">\"""</span></span>
        """
    );
  }

  private void test(String java, String expected) {
    final Html.Component component;
    component = hl.highlight(java);

    assertEquals(component.toHtml(), expected);
  }

}