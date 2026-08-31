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
package objectox.html.stream;

import static org.testng.Assert.assertEquals;

import objectos.html.stream.EndStartTagKind;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.IntSize;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.Test;

public class ScanRegularElementAttributeStateTest {

  @Test(description = """
  boolean attr:
  - count: 1
  - object
  - name (8 bit)
  - reg elem
  """)
  public void booleanAttribute01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeName.AUTOFOCUS.index());
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());

      opts.push(new ScanDocumentNodeFrame(6));
      opts.push(new ScanRegularElementAttributeFrame(6, IntSize.BIT8, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderBooleanAttributeState(ctx, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(6));
          opts.push(new ScanRegularElementAttributeFrame(7, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  js action attr:
  - count: 1
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void jsActionAttribute01() {
    final JsAction action;
    action = Js.noop();

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xJSACTION88);
      opts.addInt8(AttributeName.ONCLICK.index());
      opts.addInt8(opts.addObject(action));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanRegularElementAttributeFrame(7, IntSize.BIT8, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderJsActionAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(7));
          opts.push(new ScanRegularElementAttributeFrame(8, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  js action attr:
  - count: 1
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void jsActionAttribute01b() {
    final JsAction action;
    action = Js.noop();

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xJSACTION88);
      opts.addInt8(AttributeName.ONCLICK.index());
      opts.addInt8(opts.addObject(action));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanRegularElementAttributeFrame(8, IntSize.BIT8, 1, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
          opts.push(new ScanDocumentNodeFrame(8));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute01_08bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CONTENTEDITABLE.index());
      opts.addInt8(opts.addObject("false"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanRegularElementAttributeFrame(7, IntSize.BIT8, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(7));
          opts.push(new ScanRegularElementAttributeFrame(8, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (16 bit)
  - reg elem
  """)
  public void regularAttribute01_16bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE816);
      opts.addInt8(AttributeName.CONTENTEDITABLE.index());
      opts.addInt16(opts.addObject("false"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(8));
      opts.push(new ScanRegularElementAttributeFrame(8, IntSize.BIT8, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT16));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(8));
          opts.push(new ScanRegularElementAttributeFrame(9, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (24 bit)
  - reg elem
  """)
  public void regularAttribute01_24bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE824);
      opts.addInt8(AttributeName.CONTENTEDITABLE.index());
      opts.addInt24(opts.addObject("false"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(9));
      opts.push(new ScanRegularElementAttributeFrame(9, IntSize.BIT8, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT24));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(9));
          opts.push(new ScanRegularElementAttributeFrame(10, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute01b_08bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CONTENTEDITABLE.index());
      opts.addInt8(opts.addObject("false"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanRegularElementAttributeFrame(8, IntSize.BIT8, 1, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
          opts.push(new ScanDocumentNodeFrame(8));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (16 bit)
  - reg elem
  """)
  public void regularAttribute01b_16bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE816);
      opts.addInt8(AttributeName.CONTENTEDITABLE.index());
      opts.addInt16(opts.addObject("false"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(8));
      opts.push(new ScanRegularElementAttributeFrame(9, IntSize.BIT8, 1, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
          opts.push(new ScanDocumentNodeFrame(9));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (24 bit)
  - reg elem
  """)
  public void regularAttribute01b_24bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE824);
      opts.addInt8(AttributeName.CONTENTEDITABLE.index());
      opts.addInt24(opts.addObject("false"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(9));
      opts.push(new ScanRegularElementAttributeFrame(10, IntSize.BIT8, 1, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
          opts.push(new ScanDocumentNodeFrame(10));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - object
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute03() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanRegularElementAttributeFrame(7, IntSize.BIT8, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(7));
          opts.push(new ScanRegularElementAttributeFrame(8, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - object
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute03b() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanRegularElementAttributeFrame(8, IntSize.BIT8, 1, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
          opts.push(new ScanDocumentNodeFrame(8));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 2
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute05() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.LANG.index());
      opts.addInt8(opts.addObject("pt-BR"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.DIR.index());
      opts.addInt8(opts.addObject("auto"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(2);
      opts.addInt8(0);
      opts.addInt8(3);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.HTML.index());

      opts.push(new ScanDocumentNodeFrame(10));
      opts.push(new ScanRegularElementAttributeFrame(10, IntSize.BIT8, 2));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(10));
          opts.push(new ScanRegularElementAttributeFrame(11, IntSize.BIT8, 2, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 2
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute05b() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.LANG.index());
      opts.addInt8(opts.addObject("pt-BR"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.DIR.index());
      opts.addInt8(opts.addObject("auto"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(2);
      opts.addInt8(0);
      opts.addInt8(3);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.HTML.index());

      opts.push(new ScanDocumentNodeFrame(10));
      opts.push(new ScanRegularElementAttributeFrame(11, IntSize.BIT8, 2, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(10));
          opts.push(new ScanRegularElementAttributeFrame(12, IntSize.BIT8, 2, 2));
          opts.push(new JumpFrame(4));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 2
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute05c() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.LANG.index());
      opts.addInt8(opts.addObject("pt-BR"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.DIR.index());
      opts.addInt8(opts.addObject("auto"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(2);
      opts.addInt8(0);
      opts.addInt8(3);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.HTML.index());

      opts.push(new ScanDocumentNodeFrame(10));
      opts.push(new ScanRegularElementAttributeFrame(12, IntSize.BIT8, 2, 2));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
          opts.push(new ScanDocumentNodeFrame(12));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 3
  - object (class)
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute07() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c1"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c3"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c2"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(3);
      opts.addInt8(0);
      opts.addInt8(6);
      opts.addInt8(3);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());

      opts.push(new ScanDocumentNodeFrame(13));
      opts.push(new ScanRegularElementAttributeFrame(13, IntSize.BIT8, 3));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(13));
          opts.push(new ScanRegularElementAttributeFrame(14, IntSize.BIT8, 3, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 3
  - object (class)
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute07b() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c1"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c3"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c2"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(3);
      opts.addInt8(0);
      opts.addInt8(6);
      opts.addInt8(3);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());

      opts.push(new ScanDocumentNodeFrame(13));
      opts.push(new ScanRegularElementAttributeFrame(14, IntSize.BIT8, 3, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(13));
          opts.push(new ScanRegularElementAttributeFrame(15, IntSize.BIT8, 3, 2));
          opts.push(new JumpFrame(7));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 3
  - object (class)
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute07c() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c1"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c3"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c2"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(3);
      opts.addInt8(0);
      opts.addInt8(6);
      opts.addInt8(3);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());

      opts.push(new ScanDocumentNodeFrame(13));
      opts.push(new ScanRegularElementAttributeFrame(15, IntSize.BIT8, 3, 2));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(13));
          opts.push(new ScanRegularElementAttributeFrame(16, IntSize.BIT8, 3, 3));
          opts.push(new JumpFrame(4));
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 3
  - object (class)
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute07d() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c1"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c3"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c2"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(3);
      opts.addInt8(0);
      opts.addInt8(6);
      opts.addInt8(3);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());

      opts.push(new ScanDocumentNodeFrame(13));
      opts.push(new ScanRegularElementAttributeFrame(16, IntSize.BIT8, 3, 3));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
          opts.push(new ScanDocumentNodeFrame(16));
        })
    );
  }

  @Test(description = """
  regular elem:
  - no attrs
  - no children
  """)
  public void regularElement01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(4));
      opts.push(new ScanRegularElementAttributeFrame(4, IntSize.BIT8, 0));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
          opts.push(new ScanDocumentNodeFrame(4));
        })
    );
  }

  @Test(description = """
  regular elem:
  - no attrs
  - reg elem (no attrs / no children)
  """)
  public void regularElement02() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(10));
      opts.push(new ScanRegularElementAttributeFrame(10, IntSize.BIT8, 1));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new BeginStartTagState(ctx, ElementKind.REGULAR, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(EndStartTagEvent.of(EndStartTagKind.ELEMENT));
          opts.push(new ScanDocumentNodeFrame(10));
      opts.push(new ScanRegularElementNodeFrame(11, IntSize.BIT8, 1, 1));
      opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  regular elem:
  - no attrs
  - reg elem (no attrs / no children)
  """)
  public void regularElement02b() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(10));
      opts.push(new ScanRegularElementNodeFrame(11, IntSize.BIT8, 1, 1));
      opts.push(new JumpFrame(4));
      opts.push(new ScanRegularElementAttributeFrame(4, IntSize.BIT8, 0));
    });

    final ScanRegularElementAttributeState subject;
    subject = new ScanRegularElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
          opts.push(new ScanDocumentNodeFrame(10));
          opts.push(new ScanRegularElementNodeFrame(11, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(4));
        })
    );
  }

}