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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;

import objectos.html.AttributeName;
import objectos.html.ElementName;
import objectos.html.Fragment0;
import objectos.html.Id;
import objectos.html.Markup;
import objectox.html.Ambiguous;
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.attr.AttributeNamePojo;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class RecorderTest {

  @Test(description = """
  <html></html>
  """)
  public void testCase00() {
    final Recorder html;
    html = Recorder.create();

    html.element(ElementName.HTML);

    test(
        html,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        ElementNamePojo.HTML.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL
    );
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void testCase01() {
    final Recorder html;
    html = Recorder.create();

    html.element(
        ElementName.HTML,
        html.attribute1(AttributeName.LANG, "pt-BR")
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
    final Recorder html;
    html = Recorder.create();

    html.element(
        ElementName.HTML,
        html.attribute1(AttributeName.CLASS, "no-js"),
        html.attribute1(AttributeName.LANG, "pt-BR")
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
    final Recorder html;
    html = Recorder.create();

    html.element(
        ElementName.HTML,
        html.element(ElementName.HEAD)
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
    final Recorder html;
    html = Recorder.create();

    html.doctype();
    html.element(ElementName.HTML);

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
    final Recorder html;
    html = Recorder.create();

    final Fragment0 fragment;
    fragment = () -> {
      html.element(ElementName.META, html.attribute1(AttributeName.CHARSET, "utf-8"));
    };

    html.element(
        ElementName.HTML,
        html.element(
            ElementName.HEAD,
            html.f(fragment)
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
    final Id foo;
    foo = Id.of("foo");

    final Id bar;
    bar = Id.of("bar");

    final Recorder html;
    html = Recorder.create();

    html.element(
        ElementName.HTML,
        foo,
        html.element(ElementName.BODY, bar)
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
    final Recorder html;
    html = Recorder.create();

    html.element(
        ElementName.HTML,
        html.element(
            ElementName.BODY,
            html.element(ElementName.P, html.text("o7html"))
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
    final Recorder html;
    html = Recorder.create();

    html.element(
        ElementName.HTML,
        html.element(
            ElementName.HEAD,
            html.ambiguous(Ambiguous.TITLE, "element")
        ),
        html.element(
            ElementName.BODY,
            html.ambiguous(Ambiguous.TITLE, "attribute")
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
    final Recorder html;
    html = Recorder.create();

    html.element(
        ElementName.BODY,
        html.element(ElementName.NAV)
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
  Html.CompilerTemplate TC31

  - email input
  """)
  public void testCase31() {
    final Recorder html;
    html = Recorder.create();

    html.element(
        ElementName.INPUT,
        html.attribute1(AttributeName.TYPE, "email"),
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
    final Recorder html;
    html = Recorder.create();

    html.element(
        ElementName.FORM,
        html.flatten(
            html.element(ElementName.LABEL),
            html.element(ElementName.INPUT)
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
  fragment at the root of the document
  """)
  public void testCase73() {
    final Recorder html;
    html = Recorder.create();

    final Fragment0 action;
    action = () -> {
      html.doctype();
      html.element(ElementName.HTML);
    };

    html.f(action);

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
    final Recorder html;
    html = Recorder.create();

    final Fragment0 action;
    action = () -> {};

    html.f(action);

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

  private void test(Recorder html, int... expected) {
    assertEquals(
        html.main(),

        ByteArray.of(expected)
    );
  }

}