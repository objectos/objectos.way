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
import objectox.html.IntSize;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ScanElementAttributeStateTest {

  @Test(description = """
  element:
  """)
  public void regular01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;

        f.regular(ElementName.SPAN);

        f.values8(0, 0);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx, ElementName.SPAN));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 0;

            f.regular(ElementName.SPAN);

            f.values8(0, 0);
          }));

          opts.result(new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT));
        })
    );
  }

  @DataProvider
  public Object[][] regular02Provider() {
    return new Object[][] {
        {HtmlBytes.xATTRIBUTE88, IntSize.BIT8, IntSize.BIT8},
        {HtmlBytes.xATTRIBUTE816, IntSize.BIT8, IntSize.BIT16},
        {HtmlBytes.xATTRIBUTE824, IntSize.BIT8, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "regular02Provider", description = """
  regular element:
  - values8
  - attribute
  """)
  public void regular02(byte proto, IntSize nameSize, IntSize valueSize) {
    final int frameIndex = 1, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(proto);
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.SPAN);

        f.values8(0, 1);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, nameSize, valueSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.SPAN);

            f.values8(1, 1);
          }));

          opts.push(FrameY.create(f -> {
            f.index = valueIndex + 1;
          }));
        })
    );
  }

  @Test(dataProvider = "regular02Provider", description = """
  regular element:
  - values16
  - attribute
  """)
  public void regular03(byte proto, IntSize nameSize, IntSize valueSize) {
    final int offset = HtmlSink.MAX_INT8 + 1;

    final int frameIndex = offset + 1, valueIndex = offset;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.codeIndex(offset);
      opts.addByte(proto);
      opts.addInt16(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.SPAN);

        f.values16(0, 1);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, nameSize, valueSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 2;

            f.regular(ElementName.SPAN);

            f.values16(1, 1);
          }));

          opts.push(FrameY.create(f -> {
            f.index = valueIndex + 1;
          }));
        })
    );
  }

  @Test(dataProvider = "regular02Provider", description = """
  regular element:
  - values16
  - attribute
  """)
  public void regular04(byte proto, IntSize nameSize, IntSize valueSize) {
    final int offset = HtmlSink.MAX_INT16 + 1;

    final int frameIndex = offset + 1, valueIndex = offset;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.codeIndex(offset);
      opts.addByte(proto);
      opts.addInt24(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.SPAN);

        f.values24(0, 1);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAttributeState(ctx, nameSize, valueSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 3;

            f.regular(ElementName.SPAN);

            f.values24(1, 1);
          }));

          opts.push(FrameY.create(f -> {
            f.index = valueIndex + 1;
          }));
        })
    );
  }

  @Test(description = """
  regular element:
  - values8
  - boolean attribute
  """)
  public void regular05() {
    final int frameIndex = 1, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.SPAN);

        f.values8(0, 1);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderBooleanAttributeState(ctx, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.SPAN);

            f.values8(1, 1);
          }));

          opts.push(FrameY.create(f -> {
            f.index = valueIndex + 1;
          }));
        })
    );
  }

  @DataProvider
  public Object[][] regular06Provider() {
    return new Object[][] {
        {HtmlBytes.xJSACTION88, IntSize.BIT8, IntSize.BIT8},
        {HtmlBytes.xJSACTION816, IntSize.BIT8, IntSize.BIT16},
        {HtmlBytes.xJSACTION824, IntSize.BIT8, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "regular06Provider", description = """
  regular element:
  - values8
  - js action attribute
  """)
  public void regular06(byte proto, IntSize nameSize, IntSize valueSize) {
    final int frameIndex = 1, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(proto);
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.SPAN);

        f.values8(0, 1);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderJsActionAttributeState(ctx, nameSize, valueSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.SPAN);

            f.values8(1, 1);
          }));

          opts.push(FrameY.create(f -> {
            f.index = valueIndex + 1;
          }));
        })
    );
  }

  @DataProvider
  public Object[][] regular07Provider() {
    return new Object[][] {
        {HtmlBytes.xTEXT8, IntSize.BIT8},
        {HtmlBytes.xTEXT16, IntSize.BIT16},
        {HtmlBytes.xTEXT24, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "regular07Provider", description = """
  regular element:
  - values8
  - text node
  """)
  public void regular07(byte proto, IntSize textSize) {
    final int frameIndex = 1, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(proto);
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.SPAN);

        f.values8(0, 1);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTextState(ctx, textSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.SPAN);

            f.values8(1, 1);
          }));

          opts.push(FrameY.create(f -> {
            f.index = valueIndex + 1;
          }));

          final EndStartTagEvent event;
          event = new EndStartTagEvent(EndStartTagKind.ELEMENT);

          opts.result(event);
        })
    );
  }

  @Test(description = """
  element w/ 0 values
  """)
  public void void01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;

        f.voidElem(ElementName.INPUT);

        f.values8(0, 0);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndElementState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 0;

            f.voidElem(ElementName.INPUT);

            f.values8(0, 0);
          }));

          opts.result(new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT));
        })
    );
  }

}