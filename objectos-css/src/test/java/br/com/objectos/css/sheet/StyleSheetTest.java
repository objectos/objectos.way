/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.css.sheet;

import static org.testng.Assert.assertEquals;

import br.com.objectos.css.Css;
import br.com.objectos.css.sheet.ex.BackgroundImageTestCase;
import br.com.objectos.css.sheet.ex.TestCase00;
import br.com.objectos.css.sheet.ex.TestCase01;
import br.com.objectos.css.sheet.ex.TestCase02;
import br.com.objectos.css.sheet.ex.TestCase03;
import br.com.objectos.css.sheet.ex.TestCase04;
import br.com.objectos.css.sheet.ex.TestCase05;
import br.com.objectos.css.sheet.ex.TestCase06;
import br.com.objectos.css.sheet.ex.TestCase07;
import br.com.objectos.css.sheet.ex.TestCase08;
import br.com.objectos.css.sheet.ex.TestCase09;
import br.com.objectos.css.sheet.ex.TestCase10;
import br.com.objectos.css.sheet.ex.TestCase11;
import br.com.objectos.css.sheet.ex.TestCase12;
import br.com.objectos.css.sheet.ex.TestCase13;
import br.com.objectos.css.sheet.ex.TestCase14;
import br.com.objectos.css.sheet.ex.TestCase15;
import br.com.objectos.css.sheet.ex.TestCase16;
import br.com.objectos.css.sheet.ex.TestCase17;
import br.com.objectos.css.sheet.ex.TestCase18;
import br.com.objectos.css.sheet.ex.TestCase19;
import br.com.objectos.css.sheet.ex.TestCase20;
import br.com.objectos.css.sheet.ex.TestCase21;
import br.com.objectos.css.sheet.ex.TestCase22;
import br.com.objectos.css.sheet.ex.TestCase23;
import br.com.objectos.css.sheet.ex.TestCase24;
import br.com.objectos.css.sheet.ex.TestCase25;
import br.com.objectos.css.sheet.ex.TestCase26;
import br.com.objectos.css.sheet.ex.TestCase27;
import br.com.objectos.css.sheet.ex.TestCase28;
import br.com.objectos.css.sheet.ex.TestCase29;
import br.com.objectos.css.sheet.ex.TestCase30;
import br.com.objectos.css.sheet.ex.TestCase31;
import br.com.objectos.css.sheet.ex.TestCase32;
import br.com.objectos.css.sheet.ex.TestCase33;
import br.com.objectos.css.sheet.ex.TestCase34;
import br.com.objectos.css.sheet.ex.TestCase35;
import br.com.objectos.css.sheet.ex.TransformTestCase;
import java.util.Set;
import objectos.util.UnmodifiableList;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StyleSheetTest {

  private StyleSheetWriter minified;

  private StyleSheetWriter pretty;

  @BeforeClass
  public void _beforeClass() {
    minified = StyleSheetWriter.ofMinified();

    pretty = StyleSheetWriter.ofPretty();
  }

  @BeforeMethod
  public void _beforeMethod() {
    minified.clear();

    pretty.clear();
  }

  @Test
  public void backgroundImage() {
    test(
      new BackgroundImageTestCase(),
      minified(
        "body{",
        "background-image:url(\"foo.jpg\")",
        "}"
      ),
      pretty(
        "body {",
        "  background-image: url(\"foo.jpg\");",
        "}"
      )
    );
  }

  @Test(description = //
  """
  # filterClassSelectorsByName test case #01

  - result will be non-empty
  - filtered out in the middle
  """)
  public void filterClassSelectorsByName01() {
    var css = new AbstractStyleSheet() {
      @Override
      protected final void definition() {
        style(cn("a"), border(zero()));
        style(b, zIndex(1));
        style(cn("b"), margin(zero()));
        style(Css.dot("c"), padding(zero()));
      }
    };

    var keep = Set.of("a", "c");

    minified.filterClassSelectorsByName(keep::contains);

    test(minified, css, ".a{border:0}b{z-index:1}.c{padding:0}");

    pretty.filterClassSelectorsByName(keep::contains);

    test(pretty, css,
      """
      .a {
        border: 0;
      }

      b {
        z-index: 1;
      }

      .c {
        padding: 0;
      }"""
    );
  }

  @Test(description = //
  """
  # filterClassSelectorsByName test case #02

  - result will be empty
  """)
  public void filterClassSelectorsByName02() {
    var css = new AbstractStyleSheet() {
      @Override
      protected final void definition() {
        style(cn("a"), border(zero()));
      }
    };

    var keep = Set.of("b", "c");

    minified.filterClassSelectorsByName(keep::contains);

    test(minified, css, "");

    pretty.filterClassSelectorsByName(keep::contains);

    test(pretty, css, "");
  }

  @Test(description = //
  """
  # filterClassSelectorsByName test case #03

  - media queries
  """)
  public void filterClassSelectorsByName03() {
    var css = new AbstractStyleSheet() {
      @Override
      protected final void definition() {
        style(
          cn("a"),
          zIndex(0)
        );

        media(
          screen, minWidth(px(800)),

          style(
            cn("b"),
            zIndex(1)
          ),

          style(
            cn("c"),
            zIndex(2)
          )
        );
      }
    };

    var keep = Set.of("a", "b");

    minified.filterClassSelectorsByName(keep::contains);

    test(minified, css, ".a{z-index:0}@media screen and (min-width:800px){.b{z-index:1}}");

    pretty.filterClassSelectorsByName(keep::contains);

    test(pretty, css,
      """
      .a {
        z-index: 0;
      }

      @media screen and (min-width: 800px) {
        .b {
          z-index: 1;
        }
      }""");
  }

  @Test(description = //
  """
  # filterClassSelectorsByName test case #04

  - hover state
  """)
  public void filterClassSelectorsByName04() {
    var css = new AbstractStyleSheet() {
      @Override
      protected final void definition() {
        style(
          cn("a"), HOVER,
          zIndex(0)
        );

        style(
          cn("b"),
          zIndex(1)
        );
      }
    };

    var keep = Set.of("a");

    minified.filterClassSelectorsByName(keep::contains);

    test(minified, css, ".a:hover{z-index:0}");

    pretty.filterClassSelectorsByName(keep::contains);

    test(pretty, css,
      """
      .a:hover {
        z-index: 0;
      }""");
  }

  @Test(description = //
  """
  # filterClassSelectorsByName test case #05

  - hover state (multiple)
  """)
  public void filterClassSelectorsByName05() {
    var css = new AbstractStyleSheet() {
      @Override
      protected final void definition() {
        style(
          cn("a"), HOVER,
          zIndex(0)
        );
        style(
          cn("b"), HOVER,
          zIndex(1)
        );
        style(
          cn("c"),
          zIndex(2)
        );
      }
    };

    var keep = Set.of("a", "c");

    minified.filterClassSelectorsByName(keep::contains);

    test(minified, css, ".a:hover{z-index:0}.c{z-index:2}");

    pretty.filterClassSelectorsByName(keep::contains);

    test(pretty, css,
      """
      .a:hover {
        z-index: 0;
      }

      .c {
        z-index: 2;
      }""");
  }

  @Test(description = //
  """
  # filterClassSelectorsByName test case #06

  - media queries: skip empty media query
  """)
  public void filterClassSelectorsByName06() {
    var css = new AbstractStyleSheet() {
      @Override
      protected final void definition() {
        media(
          screen, minWidth(px(800)),

          style(
            cn("a"),
            zIndex(0)
          )
        );
        media(
          screen, minWidth(px(1024)),

          style(
            cn("b"),
            zIndex(1)
          )
        );
      }
    };

    var keep = Set.of("b");

    minified.filterClassSelectorsByName(keep::contains);

    test(minified, css, "@media screen and (min-width:1024px){.b{z-index:1}}");

    pretty.filterClassSelectorsByName(keep::contains);

    test(pretty, css,
      """
      @media screen and (min-width: 1024px) {
        .b {
          z-index: 1;
        }
      }""");
  }

  @Test(description = //
  """
  # filterClassSelectorsByName test case #07

  - media queries
  - keep only non-query rules
  """)
  public void filterClassSelectorsByName07() {
    var css = new AbstractStyleSheet() {
      @Override
      protected final void definition() {
        style(
          cn("a"),
          zIndex(0)
        );
        media(
          screen, minWidth(px(800)),

          style(
            cn("b"),
            zIndex(1)
          )
        );
        media(
          screen, minWidth(px(1024)),

          style(
            cn("c"),
            zIndex(2)
          )
        );
      }
    };

    var keep = Set.of("a");

    minified.filterClassSelectorsByName(keep::contains);

    test(minified, css, ".a{z-index:0}");

    pretty.filterClassSelectorsByName(keep::contains);

    test(pretty, css,
      """
      .a {
        z-index: 0;
      }""");
  }

  @Test
  public void testCase00() {
    test(
      new TestCase00(),
      minified(
        "body{}"
      ),
      pretty(
        "body {}"
      )
    );
  }

  @Test
  public void testCase01() {
    test(
      new TestCase01(),
      minified(
        "#myid{}"
      ),
      pretty(
        "#myid {}"
      )
    );
  }

  @Test
  public void testCase02() {
    test(
      new TestCase02(),
      minified(
        "a#myid{}"
      ),
      pretty(
        "a#myid {}"
      )
    );
  }

  @Test
  public void testCase03() {
    test(
      new TestCase03(),
      minified(
        "a#myid{}"
      ),
      pretty(
        "a#myid {}"
      )
    );
  }

  @Test
  public void testCase04() {
    test(
      new TestCase04(),
      minified(
        "body.dsl.obj{}"
      ),
      pretty(
        "body.dsl.obj {}"
      )
    );
  }

  @Test
  public void testCase05() {
    test(
      new TestCase05(),
      minified(
        "ul li{}"
      ),
      pretty(
        "ul li {}"
      )
    );
  }

  @Test
  public void testCase06() {
    test(
      new TestCase06(),
      minified(
        "a::after,a:visited{}"
      ),
      pretty(
        "a::after, a:visited {}"
      )
    );
  }

  @Test
  public void testCase07() {
    test(
      new TestCase07(),
      minified(
        "[type],[type=input],[data-attr^=start]{}"
      ),
      pretty(
        "[type], [type=\"input\"], [data-attr^=\"start\"] {}"
      )
    );
  }

  @Test
  public void testCase08() {
    test(
      new TestCase08(),
      minified(
        "*{display:block}"
      ),
      pretty(
        "* {",
        "  display: block;",
        "}"
      )
    );
  }

  @Test
  public void testCase09() {
    test(
      new TestCase09(),
      minified(
        "*{z-index:-300}"
      ),
      pretty(
        "* {",
        "  z-index: -300;",
        "}"
      )
    );
  }

  @Test
  public void testCase10() {
    test(
      new TestCase10(),
      minified(
        "*{line-height:2.5}"
      ),
      pretty(
        "* {",
        "  line-height: 2.5;",
        "}"
      )
    );
  }

  @Test
  public void testCase11() {
    test(
      new TestCase11(),
      minified(
        "*{content:\"Chapter \"}"
      ),
      pretty(
        "* {",
        "  content: \"Chapter \";",
        "}"
      )
    );
  }

  @Test
  public void testCase12() {
    test(
      new TestCase12(),
      minified(
        "*{min-height:160px}"
      ),
      pretty(
        "* {",
        "  min-height: 160px;",
        "}"
      )
    );
  }

  @Test
  public void testCase13() {
    test(
      new TestCase13(),
      minified(
        "*{line-height:1.4pt}"
      ),
      pretty(
        "* {",
        "  line-height: 1.4pt;",
        "}"
      )
    );
  }

  @Test
  public void testCase14() {
    test(
      new TestCase14(),
      minified(
        "*{background-color:floralwhite}"
      ),
      pretty(
        "* {",
        "  background-color: floralwhite;",
        "}"
      )
    );
  }

  @Test
  public void testCase15() {
    test(
      new TestCase15(),
      minified(
        "*{color:#f09}"
      ),
      pretty(
        "* {",
        "  color: #ff0099;",
        "}"
      )
    );
  }

  @Test
  public void testCase16() {
    test(
      new TestCase16(),
      minified(
        "*{flex:2 2}"
      ),
      pretty(
        "* {",
        "  flex: 2 2;",
        "}"
      )
    );
  }

  @Test
  public void testCase17() {
    test(
      new TestCase17(),
      minified(
        "*{flex:2 2.5}"
      ),
      pretty(
        "* {",
        "  flex: 2 2.5;",
        "}"
      )
    );
  }

  @Test
  public void testCase18() {
    test(
      new TestCase18(),
      minified(
        "*{margin:0 auto}"
      ),
      pretty(
        "* {",
        "  margin: 0 auto;",
        "}"
      )
    );
  }

  @Test
  public void testCase19() {
    test(
      new TestCase19(),
      minified(
        "*{margin:0 1px}"
      ),
      pretty(
        "* {",
        "  margin: 0 1px;",
        "}"
      )
    );
  }

  @Test
  public void testCase20() {
    test(
      new TestCase20(),
      minified(
        "*{margin:0 .5em}"
      ),
      pretty(
        "* {",
        "  margin: 0 0.5em;",
        "}"
      )
    );
  }

  @Test
  public void testCase21() {
    test(
      new TestCase21(),
      minified(
        "*{border-bottom:1px blue}"
      ),
      pretty(
        "* {",
        "  border-bottom: 1px blue;",
        "}"
      )
    );
  }

  @Test
  public void testCase22() {
    test(
      new TestCase22(),
      minified(
        "*{border-top:1px #f0c927}"
      ),
      pretty(
        "* {",
        "  border-top: 1px #f0c927;",
        "}"
      )
    );
  }

  @Test
  public void testCase23() {
    test(
      new TestCase23(),
      minified(
        "*{",
        "margin:1px 2.3pt 4rem;",
        "padding:1px 0 3px 10px",
        "}"
      ),
      pretty(
        "* {",
        "  margin: 1px 2.3pt 4rem;",
        "  padding: 1px 0 3px 10px;",
        "}"
      )
    );
  }

  @Test
  public void testCase24() {
    test(
      new TestCase24(),
      minified(
        "a:hover{text-decoration:underline}",
        "p{line-height:1}"
      ),
      pretty(
        "a:hover {",
        "  text-decoration: underline;",
        "}",
        "",
        "p {",
        "  line-height: 1;",
        "}"
      )
    );
  }

  @Test
  public void testCase25() {
    test(
      new TestCase25(),
      minified(
        "*{font-family:sans-serif,Arial}"
      ),
      pretty(
        "* {",
        "  font-family: sans-serif, \"Arial\";",
        "}"
      )
    );
  }

  @Test
  public void testCase26() {
    test(
      new TestCase26(),
      minified(
        "*{margin:30.5% 70%}"
      ),
      pretty(
        "* {",
        "  margin: 30.5% 70%;",
        "}"
      )
    );
  }

  @Test
  public void testCase27() {
    test(
      new TestCase27(),
      minified(
        "@media screen{",
        "section{width:100%}",
        "}"
      ),
      pretty(
        "@media screen {",
        "  section {",
        "    width: 100%;",
        "  }",
        "}"
      )
    );
  }

  @Test
  public void testCase28() {
    test(
      new TestCase28(),
      minified(
        "@media screen and (min-width:800px){",
        "section{width:100%}",
        "}"
      ),
      pretty(
        "@media screen and (min-width: 800px) {",
        "  section {",
        "    width: 100%;",
        "  }",
        "}"
      )
    );
  }

  @Test
  public void testCase29() {
    test(
      new TestCase29(),
      minified(
        "button:focus{",
        "outline:1px dotted;",
        "outline:5px auto -webkit-focus-ring-color",
        "}"
      ),
      pretty(
        "button:focus {",
        "  outline: 1px dotted;",
        "  outline: 5px auto -webkit-focus-ring-color;",
        "}"
      )
    );
  }

  @Test
  public void testCase30() {
    test(
      new TestCase30(),
      minified(
        ".rgb{",
        "color:rgb(0,1,2);",
        "color:rgb(0,100.1,255);",
        "color:rgb(0,127,255,.5);",
        "color:rgb(.1,.2,.3,.4);",
        "color:rgba(0,127,255,.5);",
        "color:rgba(.1,.2,.3,.4)",
        "}"
      ),
      pretty(
        ".rgb {",
        "  color: rgb(0, 1, 2);",
        "  color: rgb(0, 100.1, 255);",
        "  color: rgb(0, 127, 255, 0.5);",
        "  color: rgb(0.1, 0.2, 0.3, 0.4);",
        "  color: rgba(0, 127, 255, 0.5);",
        "  color: rgba(0.1, 0.2, 0.3, 0.4);",
        "}"
      )
    );
  }

  @Test
  public void testCase31() {
    test(
      new TestCase31(),
      minified(
        ".string{content:\"string value\"}"
      ),
      pretty(
        ".string {",
        "  content: \"string value\";",
        "}"
      )
    );
  }

  @Test
  public void testCase32() {
    test(
      new TestCase32(),
      minified(
        ".zero{z-index:0}",
        ".one{z-index:1}",
        ".two{z-index:2}"
      ),
      pretty(
        ".zero {",
        "  z-index: 0;",
        "}",
        "",
        ".one {",
        "  z-index: 1;",
        "}",
        "",
        ".two {",
        "  z-index: 2;",
        "}"
      )
    );
  }

  @Test
  public void testCase33() {
    test(
      new TestCase33(),
      minified(
        ".zero{z-index:0}",
        "@media screen and (min-width:800px){.one{z-index:1}}",
        ".two{z-index:2}"
      ),
      pretty(
        ".zero {",
        "  z-index: 0;",
        "}",
        "",
        "@media screen and (min-width: 800px) {",
        "  .one {",
        "    z-index: 1;",
        "  }",
        "}",
        "",
        ".two {",
        "  z-index: 2;",
        "}"
      )
    );
  }

  @Test
  public void testCase34() {
    test(
      new TestCase34(),
      minified(
        ".zero{z-index:0}",
        ".one{font-family:\"Foo Light\"}",
        ".two{z-index:2}"
      ),
      pretty(
        ".zero {",
        "  z-index: 0;",
        "}",
        "",
        ".one {",
        "  font-family: \"Foo Light\";",
        "}",
        "",
        ".two {",
        "  z-index: 2;",
        "}"
      )
    );
  }

  @Test
  public void testCase35() {
    test(
      new TestCase35(),
      minified(
        "@media screen and (min-width:800px){",
        ".one{z-index:1}",
        ".two{z-index:2}",
        "}"
      ),
      pretty(
        "@media screen and (min-width: 800px) {",
        "  .one {",
        "    z-index: 1;",
        "  }",
        "",
        "  .two {",
        "    z-index: 2;",
        "  }",
        "}"
      )
    );
  }

  @Test
  public void transform() {
    test(
      new TransformTestCase(),
      minified(
        "#tc-01{",
        "transform:rotate(45deg)",
        "}"
      ),
      pretty(
        "#tc-01 {",
        "  transform: rotate(45deg);",
        "}"
      )
    );
  }

  private String minified(String... expected) {
    var parts = UnmodifiableList.copyOf(expected);

    return parts.join();
  }

  private String pretty(String... expected) {
    var lines = UnmodifiableList.copyOf(expected);

    return lines.join("\n");
  }

  private void test(StyleSheet sheet, String minifiedOutput, String prettyOutput) {
    assertEquals(minified.toString(sheet), minifiedOutput, "MinifiedStyleSheetWriter");

    assertEquals(pretty.toString(sheet), prettyOutput, "PrettyStyleSheetWriter");
  }

  private void test(StyleSheetWriter writer, StyleSheet sheet, String expected) {
    var actual = writer.toString(sheet);

    if (!actual.equals(expected)) {
      Assert.fail(
        """

        -------
        Actual:
        %s
        -------
        Expected:
        %s
        -------
        """.formatted(actual, expected)
      );
    }
  }

}
