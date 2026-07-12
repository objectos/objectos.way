/*
 * Copyright (C) 2015-2026 Objectos Software LTDA.
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
package objectox.html;

import java.util.Arrays;
import objectos.html.ClassName;
import objectos.html.Id;
import objectos.html.Markup;
import objectos.way.Html;
import objectox.html.attr.AttributeNamePojo;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class HtmlMarkupTest01Recorder {

  @Test(description = """
  <html></html>
  """)
  public void testCase00() {
    Markup.OfHtml html;
    html = new Markup.OfHtml();

    html.html();

    test(
        html,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HTML.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void testCase01() {
    Markup.OfHtml html;
    html = new Markup.OfHtml();

    html.html(
        html.lang("pt-BR")
    );

    test(
        html,

        HtmlByteProto.MARKED5,
        (byte) AttributeNamePojo.LANG.index(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HTML.index(),
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
    Markup.OfHtml html;
    html = new Markup.OfHtml();

    html.html(
        html.className("no-js"),
        html.lang("pt-BR")
    );

    test(
        html,

        HtmlByteProto.MARKED5,
        (byte) AttributeNamePojo.CLASS.index(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.MARKED5,
        (byte) AttributeNamePojo.LANG.index(),
        HtmlBytes.encodeInt0(1),
        HtmlBytes.encodeInt1(1),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(9),
        HtmlBytes.encodeInt1(9),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HTML.index(),
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
    Markup.OfHtml html;
    html = new Markup.OfHtml();

    html.html(
        html.head()
    );

    test(
        html,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HEAD.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HTML.index(),
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
    Markup.OfHtml html;
    html = new Markup.OfHtml();

    html.doctype();
    html.html();

    test(
        html,

        HtmlByteProto.DOCTYPE,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HTML.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  fragment inclusion
  """)
  public void testCase10() {
    Markup.OfHtml html;
    html = new Markup.OfHtml();

    Html.Fragment.Of0 action;
    action = () -> {
      html.meta(html.charset("utf-8"));
    };

    int startIndex;
    startIndex = html.fragmentBegin();

    action.invoke();

    html.fragmentEnd(startIndex);

    html.html(
        html.head(
            HtmlInstruction.FRAGMENT
        )
    );

    test(
        html,

        HtmlByteProto.LENGTH3,
        HtmlBytes.encodeInt0(18),
        HtmlBytes.encodeInt1(18),
        HtmlBytes.encodeInt2(18),

        HtmlByteProto.MARKED5,
        (byte) AttributeNamePojo.CHARSET.index(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.META.index(),
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
        (byte) ElementNamePojo.HEAD.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(19),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(29),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HTML.index(),
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
    Id foo;
    foo = Id.of("foo");

    Id bar;
    bar = Id.of("bar");

    Markup.OfHtml html;
    html = new Markup.OfHtml();

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
        (byte) ElementNamePojo.BODY.index(),
        HtmlByteProto.ATTRIBUTE_EXT1,
        (byte) AttributeNamePojo.ID.index(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(9),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(11),
        HtmlBytes.encodeInt1(11),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HTML.index(),
        HtmlByteProto.ATTRIBUTE_EXT1,
        (byte) AttributeNamePojo.ID.index(),
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
    Markup.OfHtml html;
    html = new Markup.OfHtml();

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
        (byte) ElementNamePojo.P.index(),
        HtmlByteProto.TEXT,
        HtmlBytes.encodeInt0(10),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.BODY.index(),
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(16),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(21),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HTML.index(),
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
    Markup.OfHtml html;
    html = new Markup.OfHtml();

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
        (byte) Ambiguous.TITLE.code(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HEAD.index(),
        HtmlByteProto.AMBIGUOUS1,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(12),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.MARKED5,
        (byte) Ambiguous.TITLE.code(),
        HtmlBytes.encodeInt0(1),
        HtmlBytes.encodeInt1(1),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.BODY.index(),
        HtmlByteProto.AMBIGUOUS1,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(12),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(9),
        HtmlBytes.encodeInt1(9),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HTML.index(),
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
    Markup.OfHtml html;
    html = new Markup.OfHtml();

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
        (byte) ElementNamePojo.NAV.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.BODY.index(),
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
    Markup.OfHtml html;
    html = new Markup.OfHtml();

    html.style(
        "@font-face {font-family: 'Foo';}"
    );

    test(
        html,

        HtmlByteProto.AMBIGUOUS1,
        (byte) Ambiguous.STYLE.code(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5
    );
  }

  @Test(description = """
  Html.CompilerTemplate TC31

  - email input
  """)
  public void testCase31() {
    Markup.OfHtml html;
    html = new Markup.OfHtml();

    html.input(
        html.type("email"),
        Markup.required
    );

    test(
        html,

        HtmlByteProto.MARKED5,
        (byte) AttributeNamePojo.TYPE.index(),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt0(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(9),
        HtmlBytes.encodeInt1(9),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.INPUT.index(),
        HtmlByteProto.ATTRIBUTE1,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.ATTRIBUTE_EXT0,
        (byte) AttributeNamePojo.REQUIRED.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(14),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  Html.CompilerTemplate TC46

  - flatten instruction
  """)
  public void testCase46() {
    Markup.OfHtml html;
    html = new Markup.OfHtml();

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
        (byte) ElementNamePojo.LABEL.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL,

        HtmlByteProto.LENGTH2,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.INPUT.index(),
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
        (byte) ElementNamePojo.FORM.index(),
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
    grd = ClassName.of("grd");

    ClassName col;
    col = ClassName.of("col");

    MarkupPojo html;
    html = new Markup.OfHtml();

    html.textImpl("A");

    html.flattenBegin();
    html.elementValue(HtmlInstruction.ELEMENT);
    html.elementEnd();

    html.elementBegin(ElementNamePojo.DIV);
    html.elementValue(col);
    html.elementValue(HtmlInstruction.ELEMENT);
    html.elementEnd();

    html.textImpl("B");

    html.flattenBegin();
    html.elementValue(HtmlInstruction.ELEMENT);
    html.elementEnd();

    html.elementBegin(ElementNamePojo.DIV);
    html.elementValue(col);
    html.elementValue(HtmlInstruction.ELEMENT);
    html.elementEnd();

    html.flattenBegin();
    html.elementValue(HtmlInstruction.ELEMENT);
    html.elementValue(HtmlInstruction.ELEMENT);
    html.elementEnd();

    html.elementBegin(ElementNamePojo.DIV);
    html.elementValue(grd);
    html.elementValue(HtmlInstruction.ELEMENT);
    html.elementEnd();

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
        (byte) ElementNamePojo.DIV.index(),
        HtmlByteProto.ATTRIBUTE_EXT1,
        (byte) AttributeNamePojo.CLASS.index(),
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
        (byte) ElementNamePojo.DIV.index(),
        HtmlByteProto.ATTRIBUTE_EXT1,
        (byte) AttributeNamePojo.CLASS.index(),
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
        (byte) ElementNamePojo.DIV.index(),
        HtmlByteProto.ATTRIBUTE_EXT1,
        (byte) AttributeNamePojo.CLASS.index(),
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
  fragment at the root of the document
  """)
  public void testCase73() {
    Markup.OfHtml html;
    html = new Markup.OfHtml();

    Html.Fragment.Of0 action;
    action = () -> {
      html.doctype();
      html.html();
    };

    int startIndex;
    startIndex = html.fragmentBegin();

    action.invoke();

    html.fragmentEnd(startIndex);

    test(
        html,

        HtmlByteProto.FRAGMENT,
        HtmlBytes.encodeInt0(12),
        HtmlBytes.encodeInt1(12),
        HtmlBytes.encodeInt2(12),

        HtmlByteProto.DOCTYPE,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HTML.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL,

        // fragment end
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(13),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  fragment at the root of the document
  """)
  public void testCase77() {
    Markup.OfHtml html;
    html = new Markup.OfHtml();

    Html.Fragment.Of0 action;
    action = () -> {};

    int startIndex;
    startIndex = html.fragmentBegin();

    action.invoke();

    html.fragmentEnd(startIndex);

    test(
        html,

        HtmlByteProto.FRAGMENT,
        HtmlBytes.encodeInt0(3),
        HtmlBytes.encodeInt1(3),
        HtmlBytes.encodeInt2(3),

        // fragment end
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(4),
        HtmlByteProto.INTERNAL
    );
  }

  private void test(MarkupPojo html, byte... expected) {
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