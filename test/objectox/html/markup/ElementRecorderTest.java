/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.markup;

import static org.testng.Assert.assertEquals;

import objectos.html.Attribute;
import objectox.html.AmbiguousName;
import objectox.html.HtmlPojo;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class ElementRecorderTest {

  @Test(description = """
  ElementAttributesRecorder delegation
  """)
  public void attrs01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ElementRecorder subject;
    subject = create(sink);

    final ElementPointer res;
    res = subject.record(
        ElementNamePojo.HTML,

        Attribute.of(AttributeNamePojo.CHARSET, "UTF-8")
    );

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(11);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.CHARSET.index());
          opts.addInt8(opts.addObject("UTF-8"));
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  ElementAttributesRecorder before ElementsChildrenRecorder
  """)
  public void attrs02() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.B.index());
      opts.addByte(HtmlBytes.JMP24);
      opts.addInt24(8);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.B.index());
    });

    final ElementRecorder subject;
    subject = create(sink);

    final ElementPointer res;
    res = subject.record(
        ElementNamePojo.DIV,

        Attribute.of(AttributeNamePojo.ID, "x"),
        new ElementPointer(0)
    );

    assertEquals(res.value(), 8);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xSTARTTAG8);
          opts.addInt8(ElementNamePojo.B.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(8);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.B.index());
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.DIV.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(21);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("x"));
          opts.addByte(HtmlBytes.ELEM8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.DIV.index());
        })
    );
  }

  @Test(description = """
  ElementChildrenRecorder delegation
  """)
  public void children01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HEAD.index());
      opts.addByte(HtmlBytes.JMP24);
      opts.addInt24(8);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HEAD.index());
    });

    final ElementRecorder subject;
    subject = create(sink);

    final ElementPointer res;
    res = subject.record(
        ElementNamePojo.HTML,

        new ElementPointer(0)
    );

    assertEquals(res.value(), 8);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xSTARTTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(8);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(18);
          opts.addByte(HtmlBytes.ELEM8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  <html>Ipsum</html>
  """)
  public void children02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ElementRecorder subject;
    subject = create(sink);

    final String value;
    value = "Ipsum";

    final ElementPointer res;
    res = subject.record(ElementNamePojo.HTML, new RawNode(value));

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(10);
          opts.addByte(HtmlBytes.RAW8);
          opts.addInt8(opts.addObject(value));
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  <html>Ipsum</html>
  """)
  public void children03() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ElementRecorder subject;
    subject = create(sink);

    final String value;
    value = "Ipsum";

    final ElementPointer res;
    res = subject.record(ElementNamePojo.HTML, new TextNode(value));

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(10);
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject(value));
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  <head><title>foo</title></head>
  """)
  public void children04() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ElementRecorder subject;
    subject = create(sink);

    final String text;
    text = "foo";

    final AmbiguousPojo value;
    value = AmbiguousPojo.of0(AmbiguousName.TITLE, text);

    final ElementPointer res;
    res = subject.record(ElementNamePojo.HEAD, value);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(11);
          opts.addByte(HtmlBytes.ELTXT88);
          opts.addInt8(ElementNamePojo.TITLE.index());
          opts.addInt8(opts.addObject(text));
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
        })
    );
  }

  @Test(description = """
  <html><fragment></html>
  """)
  public void children05() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ElementRecorder subject;
    subject = create(sink);

    final HtmlPojo frag;
    frag = new HtmlPojo(new HtmlSink());

    final ElementPointer res;
    res = subject.record(ElementNamePojo.HTML, frag);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(10);
          opts.addByte(HtmlBytes.FRAG8);
          opts.addInt8(opts.addObject(frag));
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  STARTAG8, jump, ENDTAG8
  """)
  public void tags01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ElementRecorder subject;
    subject = create(sink);

    final ElementPointer res;
    res = subject.record(ElementNamePojo.HTML);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(8);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  STARTAG8, jump, VOIDELEM
  """)
  public void tags02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ElementRecorder subject;
    subject = create(sink);

    final ElementPointer res;
    res = subject.record(ElementNamePojo.BR);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.BR.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(7);
          opts.addByte(HtmlBytes.VOIDELEM);
        })
    );
  }

  private ElementRecorder create(HtmlSink sink) {
    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final ElementAttributesRecorder attributesRecorder;
    attributesRecorder = new ElementAttributesRecorder(attributeRecorder, booleanAttributeRecorder);

    final ChildAmbiguousRecorder childAmbiguousRecorder;
    childAmbiguousRecorder = new ChildAmbiguousRecorder(sink);

    final ChildElementMarker childElementMarker;
    childElementMarker = new ChildElementMarker(sink);

    final ChildElementRecorder childElementRecorder;
    childElementRecorder = new ChildElementRecorder(sink);

    final ChildFragmentRecorder childFragmentRecorder;
    childFragmentRecorder = new ChildFragmentRecorder(sink);

    final ChildRawRecorder childRawRecorder;
    childRawRecorder = new ChildRawRecorder(sink);

    final ChildTextRecorder childTextRecorder;
    childTextRecorder = new ChildTextRecorder(sink);

    final ElementChildrenRecorder childrenRecorder;
    childrenRecorder = new ElementChildrenRecorder(childAmbiguousRecorder, childElementMarker, childElementRecorder, childFragmentRecorder, childRawRecorder, childTextRecorder);

    return new ElementRecorder(sink, attributesRecorder, childrenRecorder);
  }

}
