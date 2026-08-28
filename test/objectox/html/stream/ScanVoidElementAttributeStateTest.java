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

public class ScanVoidElementAttributeStateTest {

  @Test(description = """
  void elem:
  - no attrs
  """)
  public void voidElement01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.HR.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(4));
      opts.push(new ScanVoidElementAttributeFrame(4, IntSize.BIT8, 0));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndElementState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(4));
          opts.result(EndStartTagEvent.of(EndStartTagKind.VOID_ELEMENT));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 reg attr (markup) (8bit)
  """)
  public void voidElement02_08bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt8(opts.addObject("password"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanVoidElementAttributeFrame(7, IntSize.BIT8, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(7));
          opts.push(new ScanVoidElementAttributeFrame(8, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 reg attr (markup) (16bit)
  """)
  public void voidElement02_16bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE816);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt16(opts.addObject("password"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(8));
      opts.push(new ScanVoidElementAttributeFrame(8, IntSize.BIT8, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT16));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(8));
          opts.push(new ScanVoidElementAttributeFrame(9, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 reg attr (markup) (24bit)
  """)
  public void voidElement02_24bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE824);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt24(opts.addObject("password"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(9));
      opts.push(new ScanVoidElementAttributeFrame(9, IntSize.BIT8, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT24));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(9));
          opts.push(new ScanVoidElementAttributeFrame(10, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 reg attr (markup) (8bit)
  """)
  public void voidElement02b_08bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt8(opts.addObject("password"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanVoidElementAttributeFrame(8, IntSize.BIT8, 1, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndElementState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(8));
          opts.result(EndStartTagEvent.of(EndStartTagKind.VOID_ELEMENT));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 reg attr (markup) (16bit)
  """)
  public void voidElement02b_16bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE816);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt16(opts.addObject("password"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(8));
      opts.push(new ScanVoidElementAttributeFrame(9, IntSize.BIT8, 1, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndElementState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(9));
          opts.result(EndStartTagEvent.of(EndStartTagKind.VOID_ELEMENT));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 reg attr (markup) (24bit)
  """)
  public void voidElement02b_24bit() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE824);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt24(opts.addObject("password"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(9));
      opts.push(new ScanVoidElementAttributeFrame(10, IntSize.BIT8, 1, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndElementState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(10));
          opts.result(EndStartTagEvent.of(EndStartTagKind.VOID_ELEMENT));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 boolean attr (object) (8bit)
  """)
  public void voidElement03() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeName.CHECKED.index());
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(6));
      opts.push(new ScanVoidElementAttributeFrame(6, IntSize.BIT8, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderBooleanAttributeState(ctx, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(6));
          opts.push(new ScanVoidElementAttributeFrame(7, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 boolean attr (object) (8bit)
  """)
  public void voidElement03b() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeName.CHECKED.index());
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(6));
      opts.push(new ScanVoidElementAttributeFrame(7, IntSize.BIT8, 1, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndElementState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(7));
          opts.result(EndStartTagEvent.of(EndStartTagKind.VOID_ELEMENT));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 reg attr (object) (8bit)
  """)
  public void voidElement04() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanVoidElementAttributeFrame(7, IntSize.BIT8, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(7));
          opts.push(new ScanVoidElementAttributeFrame(8, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 js action attr (object) (8bit)
  """)
  public void voidElement05() {
    final JsAction action;
    action = Js.noop();

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xJSACTION88);
      opts.addInt8(AttributeName.ONCLICK.index());
      opts.addInt8(opts.addObject(action));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanVoidElementAttributeFrame(7, IntSize.BIT8, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderJsActionAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(7));
          opts.push(new ScanVoidElementAttributeFrame(8, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 js action attr (object) (8bit)
  """)
  public void voidElement05b() {
    final JsAction action;
    action = Js.noop();

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xJSACTION88);
      opts.addInt8(AttributeName.ONCLICK.index());
      opts.addInt8(opts.addObject(action));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(7));
      opts.push(new ScanVoidElementAttributeFrame(8, IntSize.BIT8, 1, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndElementState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(8));
          opts.result(EndStartTagEvent.of(EndStartTagKind.VOID_ELEMENT));
        })
    );
  }

  @Test(description = """
  void elem:
  - 2 reg attrs (markup) (8bit)
  """)
  public void voidElement06() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt8(opts.addObject("text"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.SIZE.index());
      opts.addInt8(opts.addObject("30"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(2);
      opts.addInt8(0);
      opts.addInt8(3);

      opts.push(new ScanDocumentNodeFrame(10));
      opts.push(new ScanVoidElementAttributeFrame(10, IntSize.BIT8, 2));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(10));
          opts.push(new ScanVoidElementAttributeFrame(11, IntSize.BIT8, 2, 1));
          opts.push(new JumpFrame(1));
        })
    );
  }

  @Test(description = """
  void elem:
  - 2 reg attrs (markup) (8bit)
  """)
  public void voidElement06b() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt8(opts.addObject("text"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.SIZE.index());
      opts.addInt8(opts.addObject("30"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(2);
      opts.addInt8(0);
      opts.addInt8(3);

      opts.push(new ScanDocumentNodeFrame(10));
      opts.push(new ScanVoidElementAttributeFrame(11, IntSize.BIT8, 2, 1));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(10));
          opts.push(new ScanVoidElementAttributeFrame(12, IntSize.BIT8, 2, 2));
          opts.push(new JumpFrame(4));
        })
    );
  }

  @Test(description = """
  void elem:
  - 2 reg attrs (markup) (8bit)
  """)
  public void voidElement06c() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt8(opts.addObject("text"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.SIZE.index());
      opts.addInt8(opts.addObject("30"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(2);
      opts.addInt8(0);
      opts.addInt8(3);

      opts.push(new ScanDocumentNodeFrame(10));
      opts.push(new ScanVoidElementAttributeFrame(12, IntSize.BIT8, 2, 2));
    });

    final ScanVoidElementAttributeState subject;
    subject = new ScanVoidElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndElementState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(12));
          opts.result(EndStartTagEvent.of(EndStartTagKind.VOID_ELEMENT));
        })
    );
  }

}
