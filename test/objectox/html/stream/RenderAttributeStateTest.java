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

import objectox.html.IntSize;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.Test;

public class RenderAttributeStateTest {

  @Test(description = """
  regular elem:
  - 1 reg attr (markup) (8bit)
  - no children
  """)
  public void regularElement02_08bit() {
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
      opts.push(new JumpFrame(1));
    });

    final RenderAttributeState subject;
    subject = new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanRegularElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(RenderAttributeEvent.of(AttributeName.CONTENTEDITABLE, "false"));
          opts.push(new ScanDocumentNodeFrame(7));
          opts.push(new ScanRegularElementAttributeFrame(8, IntSize.BIT8, 1, 1));
        })
    );
  }

  @Test(description = """
  regular elem:
  - 1 reg attr (markup) (16bit)
  - no children
  """)
  public void regularElement02_16bit() {
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
      opts.push(new JumpFrame(1));
    });

    final RenderAttributeState subject;
    subject = new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT16);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanRegularElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(RenderAttributeEvent.of(AttributeName.CONTENTEDITABLE, "false"));
          opts.push(new ScanDocumentNodeFrame(8));
          opts.push(new ScanRegularElementAttributeFrame(9, IntSize.BIT8, 1, 1));
        })
    );
  }

  @Test(description = """
  regular elem:
  - 1 reg attr (markup) (24bit)
  - no children
  """)
  public void regularElement02_24bit() {
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
      opts.push(new JumpFrame(1));
    });

    final RenderAttributeState subject;
    subject = new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT24);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanRegularElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(RenderAttributeEvent.of(AttributeName.CONTENTEDITABLE, "false"));
          opts.push(new ScanDocumentNodeFrame(9));
          opts.push(new ScanRegularElementAttributeFrame(10, IntSize.BIT8, 1, 1));
        })
    );
  }

  @Test(description = """
  regular elem:
  - 1 reg attr (object) (8bit)
  - no children
  """)
  public void regularElement04() {
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
      opts.push(new JumpFrame(1));
    });

    final RenderAttributeState subject;
    subject = new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanRegularElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(RenderAttributeEvent.of(AttributeName.ID, "foo"));
          opts.push(new ScanDocumentNodeFrame(7));
          opts.push(new ScanRegularElementAttributeFrame(8, IntSize.BIT8, 1, 1));
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
      opts.push(new ScanVoidElementAttributeFrame(8, IntSize.BIT8, 1, 1));
      opts.push(new JumpFrame(1));
    });

    final RenderAttributeState subject;
    subject = new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanVoidElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(RenderAttributeEvent.of(AttributeName.TYPE, "password"));
          opts.push(new ScanDocumentNodeFrame(7));
          opts.push(new ScanVoidElementAttributeFrame(8, IntSize.BIT8, 1, 1));
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
      opts.push(new ScanVoidElementAttributeFrame(9, IntSize.BIT8, 1, 1));
      opts.push(new JumpFrame(1));
    });

    final RenderAttributeState subject;
    subject = new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT16);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanVoidElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(RenderAttributeEvent.of(AttributeName.TYPE, "password"));
          opts.push(new ScanDocumentNodeFrame(8));
          opts.push(new ScanVoidElementAttributeFrame(9, IntSize.BIT8, 1, 1));
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
      opts.push(new ScanVoidElementAttributeFrame(10, IntSize.BIT8, 1, 1));
      opts.push(new JumpFrame(1));
    });

    final RenderAttributeState subject;
    subject = new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT24);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanVoidElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(RenderAttributeEvent.of(AttributeName.TYPE, "password"));
          opts.push(new ScanDocumentNodeFrame(9));
          opts.push(new ScanVoidElementAttributeFrame(10, IntSize.BIT8, 1, 1));
        })
    );
  }

}
