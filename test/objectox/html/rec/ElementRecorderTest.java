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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;

import objectos.html.ElementName;
import objectox.html.Ambiguous;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlInstruction;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class ElementRecorderTest {

  @Test(description = """
  <html></html>
  """)
  public void record01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ElementRecorder subject;
    subject = new ElementRecorder(sink);

    final ElementNamePojo name;
    name = ElementNamePojo.HTML;

    final ElementInstruction res;
    res = subject.record(name);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(name.index());
          opts.addByte(HtmlBytes.VALUES0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(name.index());
        })
    );
  }

  @Test(description = """
  <html autofocus></html>
  """)
  public void record02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final int attrIndex;
    attrIndex = sink.addByte(HtmlBytes.BOOLEAN8);

    final AttributeInstruction attr;
    attr = new AttributeInstruction(attrIndex);

    final ElementRecorder subject;
    subject = new ElementRecorder(sink);

    final ElementNamePojo name;
    name = ElementNamePojo.HTML;

    final ElementInstruction res;
    res = subject.record(name, attr);

    assertEquals(res.value(), 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.XBOOLEAN8);
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(name.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(name.index());
        })
    );
  }

  @Test(description = "<html><head><title>x</title></head></html>")
  public void record03() {
    final ByteArray aux;
    aux = ByteArray.of(HtmlByteProto.INTERNAL);

    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.MARKED5,
        HtmlBytes.encodeInt0(Ambiguous.TITLE.ordinal()),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5,

        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(7),
        HtmlBytes.encodeInt1(7),
        HtmlByteProto.STANDARD_NAME,
        (byte) ElementNamePojo.HEAD.index(),
        HtmlByteProto.AMBIGUOUS1,
        HtmlBytes.encodeInt0(11),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(12),
        HtmlByteProto.INTERNAL
    );

    final ObjectArray objects;
    objects = ObjectArray.of("x");

    final ZZZElementRecorder subject;
    subject = ZZZElementRecorder.of(aux, main, objects);

    final ElementName name;
    name = ElementName.HTML;

    subject.record(name, HtmlInstruction.ELEMENT);

    assertEquals(
        aux,

        ByteArray.of(HtmlByteProto.INTERNAL, HtmlByteProto.INTERNAL)
    );

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.MARKED5,
            HtmlBytes.encodeInt0(Ambiguous.TITLE.ordinal()),
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

            HtmlByteProto.ELEMENT,
            HtmlBytes.encodeInt0(7),
            HtmlBytes.encodeInt1(7),
            HtmlByteProto.STANDARD_NAME,
            (byte) ElementNamePojo.HTML.index(),
            HtmlByteProto.ELEMENT,
            HtmlBytes.encodeInt0(16),
            HtmlByteProto.END,
            HtmlBytes.encodeInt0(22),
            HtmlByteProto.INTERNAL
        )
    );

    assertEquals(
        objects,

        ObjectArray.of("x")
    );
  }

}
