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

import objectos.html.ElementName;
import objectos.html.markup.Fragment0;
import objectos.html.markup.Fragment1;
import objectos.html.markup.Fragment2;
import objectos.html.markup.Fragment3;
import objectos.html.markup.Fragment4;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class RecorderTest {

  private Recorder create(HtmlSink sink) {
    return Recorder.of(sink);
  }

  @Test(description = "<!DOCTYPE html>")
  public void doctype01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    subject.doctype();

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.DOCTYPE);
        })
    );
  }

  @SuppressWarnings("unused")
  @Test(description = "empty document")
  public void record00() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    assertEquals(sink, HtmlSinkY.create(_ -> {}));
  }

  @Test(description = """
  <html></html>
  """)
  public void record01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    final ElementNamePojo name;
    name = ElementNamePojo.HTML;

    subject.element(name);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(name.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(name.index());
        })
    );
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void record02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    subject.element(
        ElementName.HTML,
        subject.attribute(AttributeNamePojo.LANG, "pt-BR")
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.LANG.index());
          opts.addInt8(opts.addObject("pt-BR"));
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  <html autofocus></html>
  """)
  public void record03() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    subject.element(
        ElementName.HTML,
        subject.attribute(AttributeNamePojo.AUTOFOCUS)
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xBOOLEAN8);
          opts.addInt8(AttributeNamePojo.AUTOFOCUS.index());
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  <html><head></head></html>
  """)
  public void record04() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    subject.element(
        ElementNamePojo.HTML,
        subject.element(ElementNamePojo.HEAD)
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xSTARTTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  <head><meta charset="UTF-8"></head>
  """)
  public void record05() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    subject.element(
        ElementNamePojo.HEAD,
        subject.element(
            ElementNamePojo.META,
            subject.attribute(AttributeNamePojo.CHARSET, "UTF-8")
        )
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.CHARSET.index());
          opts.addInt8(opts.addObject("UTF-8"));
          opts.addByte(HtmlBytes.xSTARTTAG8);
          opts.addInt8(ElementNamePojo.META.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.VOIDELEM);
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
        })
    );
  }

  @Test(description = """
  <html lang="pt-BR">
    <head></head>
  </html>
  """)
  public void record06() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    subject.element(
        ElementNamePojo.HTML,
        subject.element(ElementNamePojo.HEAD),
        subject.attribute(AttributeNamePojo.LANG, "pt-BR")
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xSTARTTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.LANG.index());
          opts.addInt8(opts.addObject("pt-BR"));
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addInt8(6);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  <!DOCTYPE html>
  <html></html>
  """)
  public void record07() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    subject.doctype();

    subject.element(
        ElementNamePojo.HTML
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.DOCTYPE);
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

  @Test(description = """
  <span>ipsum</span>
  """)
  public void record08() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    subject.element(
        ElementNamePojo.SPAN,
        subject.text("ipsum")
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(opts.addObject("ipsum"));
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.SPAN.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.SPAN.index());
        })
    );
  }

  @Test(description = """
  <div class="c1 c2"></html>
  """)
  public void record09() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    subject.element(
        ElementName.DIV,
        subject.attribute(AttributeNamePojo.CLASS, "c1"),
        subject.attribute(AttributeNamePojo.CLASS, "c2")
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.CLASS.index());
          opts.addInt8(opts.addObject("c1"));
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.CLASS.index());
          opts.addInt8(opts.addObject("c2"));
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addInt8(3);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.DIV.index());
        })
    );
  }

  @Test(description = """
  flatten instruction
  """)
  public void flatten01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    final ElementPointer res;
    res = subject.element(
        ElementName.UL,
        subject.flatten(
            subject.element(ElementName.LI),
            subject.element(ElementName.LI)
        )
    );

    assertEquals(res.value(), 17);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          int li0 = opts.addByte(HtmlBytes.xSTARTTAG8);
          opts.addInt8(ElementNamePojo.LI.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.LI.index());

          int li1 = opts.addByte(HtmlBytes.xSTARTTAG8);
          opts.addInt8(ElementNamePojo.LI.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.LI.index());

          int flat = opts.addByte(HtmlBytes.xSTARTFLAT);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(2);
          opts.addInt8(li0);
          opts.addInt8(li1);

          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.UL.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(flat);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.UL.index());
        })
    );
  }

  @Test(description = "fragment")
  public void fragment01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    final Fragment0 fragment;
    fragment = () -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject("foo"));
    };

    final FragmentHtml res;
    res = subject.fragment(fragment);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

  @Test(description = "fragment1")
  public void fragment02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    final Fragment1<String> fragment;
    fragment = value -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value));
    };

    final FragmentHtml res;
    res = subject.fragment(fragment, "foo");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

  @Test(description = "fragment2")
  public void fragment03() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    final Fragment2<String, String> fragment;
    fragment = (value1, value2) -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value1));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value2));
    };

    final FragmentHtml res;
    res = subject.fragment(fragment, "foo", "bar");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("bar"));

          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

  @Test(description = "fragment3")
  public void fragment04() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    final Fragment3<String, String, String> fragment;
    fragment = (value1, value2, value3) -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value1));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value2));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value3));
    };

    final FragmentHtml res;
    res = subject.fragment(fragment, "foo", "bar", "baz");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("bar"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("baz"));

          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

  @Test(description = "fragment4")
  public void fragment05() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    final Fragment4<String, String, String, String> fragment;
    fragment = (value1, value2, value3, value4) -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value1));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value2));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value3));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value4));
    };

    final FragmentHtml res;
    res = subject.fragment(fragment, "foo", "bar", "baz", "qux");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("bar"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("baz"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("qux"));

          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

}
