/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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

import java.util.Arrays;
import objectos.way.Html.ClassName;
import objectos.way.Html.FragmentLambda;
import objectos.way.Html.HtmlClassName;
import objectos.way.Html.HtmlId;
import org.testng.annotations.Test;

public class HtmlCompilerTestRecorder {

  @Test(description = """
  <html></html>
  """)
  public void testCase00() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.html();

    test(
        html,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HTML.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void testCase01() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.html(
        html.lang("pt-BR")
    );

    test(
        html,

        HtmlByteProto.MARKED5,
        (byte) HtmlAttributeName.LANG.index(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HTML.index(),
        HtmlByteProto.ATTRIBUTE1,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(12),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  <html class="no-js" lang="pt-BR"></html>
  """)
  public void testCase02() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.html(
        html.className("no-js"),
        html.lang("pt-BR")
    );

    test(
        html,

        HtmlByteProto.MARKED5,
        (byte) HtmlAttributeName.CLASS.index(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.MARKED5,
        (byte) HtmlAttributeName.LANG.index(),
        HtmlBytes.encodeInt0(1),
        HtmlBytes.encodeInt1(1),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(9),
        HtmlBytes.encodeInt1(9),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HTML.index(),
        HtmlByteProto.ATTRIBUTE1,
        HtmlBytes.encodeInt0(16),
        HtmlByteProto.ATTRIBUTE1,
        HtmlBytes.encodeInt0(13),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(19),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  <html><head></head></html>
  """)
  public void testCase03() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.html(
        html.head()
    );

    test(
        html,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HEAD.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HTML.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(14),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(15),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  <!DOCTYPE html>
  <html></html>
  """)
  public void testCase09() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.doctype();
    html.html();

    test(
        html,

        HtmlByteProto.DOCTYPE,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HTML.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  fragment inclusion
  """)
  public void testCase10() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    FragmentLambda action;
    action = () -> {
      html.meta(html.charset("utf-8"));
    };

    html.html(
        html.head(
            html.include(action)
        )
    );

    test(
        html,

        HtmlByteProto.LENGTH3,
        HtmlBytes.encodeInt0(18),
        HtmlBytes.encodeInt1(18),
        HtmlBytes.encodeInt2(18),

        HtmlByteProto.MARKED5,
        (byte) HtmlAttributeName.CHARSET.index(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.META.index(),
        HtmlByteProto.ATTRIBUTE1,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(12),
        HtmlByteProto.INTERNAL,

        // fragment end
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(19),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HEAD.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(19),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(29),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HTML.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(16),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(39),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  External id attributes
  """)
  public void testCase13() {
    HtmlId foo;
    foo = new HtmlId("foo");

    HtmlId bar;
    bar = new HtmlId("bar");

    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.html(
        foo,
        html.body(bar)
    );

    test(
        html,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(9),
        HtmlBytes.encodeInt1(9),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.BODY.index(),
        HtmlByteProto.ATTRIBUTE_EXT1,
        (byte) HtmlAttributeName.ID.index(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(9),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(11),
        HtmlBytes.encodeInt1(11),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HTML.index(),
        HtmlByteProto.ATTRIBUTE_EXT1,
        (byte) HtmlAttributeName.ID.index(),
        HtmlBytes.encodeInt0(1),
        HtmlBytes.encodeInt1(1),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(22),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(23),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  Text child element
  """)
  public void testCase14() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.html(
        html.body(
            html.p("o7html")
        )
    );

    test(
        html,

        HtmlByteProto.MARKED4,
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL4,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.P.index(),
        HtmlByteProto.TEXT,
        HtmlBytes.encodeInt0(10),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.BODY.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(16),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(21),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HTML.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(16),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(31),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  Ambiguous
  """)
  public void testCase16() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.html(
        html.head(
            html.title("element")
        ),
        html.body(
            html.title("attribute")
        )
    );

    test(
        html,

        HtmlByteProto.MARKED5,
        (byte) HtmlAmbiguous.TITLE.ordinal(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HEAD.index(),
        HtmlByteProto.AMBIGUOUS1,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(12),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.MARKED5,
        (byte) HtmlAmbiguous.TITLE.ordinal(),
        HtmlBytes.encodeInt0(1),
        HtmlBytes.encodeInt1(1),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.BODY.index(),
        HtmlByteProto.AMBIGUOUS1,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(12),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(9),
        HtmlBytes.encodeInt1(9),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.HTML.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(31),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(18),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(39),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  include template
  """)
  public void testCase20() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.body(
        html.nav()
    );

    /*
    // template begin
    html.elementBegin(HtmlElementName.NAV);
    html.elementEnd();
    // template end

    html.elementBegin(HtmlElementName.BODY);
    html.elementValue(BaseApi.FRAGMENT);
    html.elementEnd();

    html.compilationEnd();
    */

    test(
        html,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.NAV.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.BODY.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(14),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(15),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  style/script => raw
  """)
  public void testCase25() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.style(
        "@font-face {font-family: 'Foo';}"
    );

    test(
        html,

        HtmlByteProto.MARKED4,
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL4,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.STYLE.index(),
        HtmlByteProto.TEXT,
        HtmlBytes.encodeInt0(10),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  Html.CompilerTemplate TC31

  - email input
  """)
  public void testCase31() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.input(
        html.type("email"),
        html.required()
    );

    test(
        html,

        HtmlByteProto.MARKED5,
        (byte) HtmlAttributeName.TYPE.index(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt0(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.MARKED3,
        (byte) HtmlAttributeName.REQUIRED.index(),
        HtmlByteProto.INTERNAL3,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(9),
        HtmlBytes.encodeInt1(9),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.INPUT.index(),
        HtmlByteProto.ATTRIBUTE1,
        HtmlBytes.encodeInt0(14),
        HtmlByteProto.ATTRIBUTE0,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(17),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  Html.CompilerTemplate TC46

  - flatten instruction
  """)
  public void testCase46() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.form(
        html.flatten(
            html.label(),
            html.input()
        )
    );

    test(
        html,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.LABEL.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.INPUT.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(20),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(14),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(23),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(9),
        HtmlBytes.encodeInt1(9),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.FORM.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(32),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(26),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(35),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  Html.CompilerTemplate TC47

  - grid component
  """)
  public void testCase47() {
    ClassName grd;
    grd = new HtmlClassName("grd");

    ClassName col;
    col = new HtmlClassName("col");

    HtmlRecorder html;
    html = new HtmlRecorder();

    html.span("foo");
    html.text("bar");
    html.span("zaz");

    html.compilationBegin();

    html.textImpl("A");

    html.flattenBegin();
    html.elementValue(Html.ELEMENT);
    html.elementEnd();

    html.elementBegin(HtmlElementName.DIV);
    html.elementValue(col);
    html.elementValue(Html.ELEMENT);
    html.elementEnd();

    html.textImpl("B");

    html.flattenBegin();
    html.elementValue(Html.ELEMENT);
    html.elementEnd();

    html.elementBegin(HtmlElementName.DIV);
    html.elementValue(col);
    html.elementValue(Html.ELEMENT);
    html.elementEnd();

    html.flattenBegin();
    html.elementValue(Html.ELEMENT);
    html.elementValue(Html.ELEMENT);
    html.elementEnd();

    html.elementBegin(HtmlElementName.DIV);
    html.elementValue(grd);
    html.elementValue(Html.ELEMENT);
    html.elementEnd();

    html.compilationEnd();

    test(
        html,

        HtmlByteProto.MARKED4,
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL4,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.TEXT,
        HtmlBytes.encodeInt0(8),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(9),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(11),
        HtmlBytes.encodeInt1(11),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.DIV.index(),
        HtmlByteProto.ATTRIBUTE_EXT1,
        (byte) HtmlAttributeName.CLASS.index(),
        HtmlBytes.encodeInt0(1),
        HtmlBytes.encodeInt1(1),
        HtmlByteProto.TEXT,
        HtmlBytes.encodeInt0(22),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(23),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.MARKED4,
        HtmlBytes.encodeInt0(2),
        HtmlBytes.encodeInt1(2),
        HtmlByteProto.INTERNAL4,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.TEXT,
        HtmlBytes.encodeInt0(8),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(9),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(11),
        HtmlBytes.encodeInt1(11),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.DIV.index(),
        HtmlByteProto.ATTRIBUTE_EXT1,
        (byte) HtmlAttributeName.CLASS.index(),
        HtmlBytes.encodeInt0(3),
        HtmlBytes.encodeInt1(3),
        HtmlByteProto.TEXT,
        HtmlBytes.encodeInt0(22),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(23),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(44),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(20),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(59),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(13),
        HtmlBytes.encodeInt1(13),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.DIV.index(),
        HtmlByteProto.ATTRIBUTE_EXT1,
        (byte) HtmlAttributeName.CLASS.index(),
        HtmlBytes.encodeInt0(4),
        HtmlBytes.encodeInt1(4),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(60),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(36),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(75),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  The test-field pseudo-attribute
  """)
  public void testCase65() {
    HtmlRecorder html;
    html = new HtmlRecorder();

    html.compilationBegin();

    html.div(
        html.div(
            html.testable("x", "123")
        ),
        html.div(
            html.testable("y", "abc")
        )
    );

    test(
        html,

        HtmlByteProto.MARKED6,
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlBytes.encodeInt0(1),
        HtmlBytes.encodeInt1(1),
        HtmlByteProto.INTERNAL6,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.DIV.index(),
        HtmlByteProto.TESTABLE,
        HtmlBytes.encodeInt0(12),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(13),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.MARKED6,
        HtmlBytes.encodeInt0(2),
        HtmlBytes.encodeInt1(2),
        HtmlBytes.encodeInt0(3),
        HtmlBytes.encodeInt1(3),
        HtmlByteProto.INTERNAL6,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.DIV.index(),
        HtmlByteProto.TESTABLE,
        HtmlBytes.encodeInt0(12),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(13),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(9),
        HtmlBytes.encodeInt1(9),
        HtmlByteProto.STANDARD_NAME,
        (byte) HtmlElementName.DIV.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(32),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(18),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(41),
        HtmlByteProto.INTERNAL
    );
  }

  private void test(HtmlRecorder html, byte... expected) {
    byte[] result;
    result = Arrays.copyOf(html.main, html.mainIndex);

    if (result.length != expected.length) {
      throw new AssertionError(
          """
        Arrays don't have the same size.

        Actual  : %s
        Expected: %s
        """.formatted(Arrays.toString(result), Arrays.toString(expected))
      );
    }

    if (!Arrays.equals(result, expected)) {
      throw new AssertionError(
          """
        Arrays don't have the same content.

        Actual  : %s
        Expected: %s
        """.formatted(Arrays.toString(result), Arrays.toString(expected))
      );
    }
  }

}