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

public class ScanDocumentNodeStateTest {

  @Test(description = """
  document:
  - empty
  """)
  public void document01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new ScanDocumentNodeFrame(0));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new StopState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(0));
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

      opts.push(new ScanDocumentNodeFrame(0));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new BeginStartTagState(ctx, ElementKind.REGULAR, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(1));
        })
    );
  }

  @Test(description = """
  regular elem:
  - no attrs
  - no children
  """)
  public void regularElement01b() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(6));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new StopState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.push(new ScanDocumentNodeFrame(6));
        })
    );
  }

  @Test(description = """
  regular elem:
  - 1 reg attr (markup) (8bit)
  - no children
  """)
  public void regularElement02_8bit() {
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

      opts.push(new ScanDocumentNodeFrame(3));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new BeginStartTagState(ctx, ElementKind.REGULAR, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(4));
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

      opts.push(new ScanDocumentNodeFrame(4));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new BeginStartTagState(ctx, ElementKind.REGULAR, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(5));
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

      opts.push(new ScanDocumentNodeFrame(5));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new BeginStartTagState(ctx, ElementKind.REGULAR, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(6));
        })
    );
  }

}
