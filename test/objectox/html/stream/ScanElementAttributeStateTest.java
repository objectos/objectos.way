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
import static org.testng.Assert.fail;

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
  public void testCase01() {
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

          opts.result(new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
        })
    );
  }

  @Test(description = """
  void element w/ 0 values
  """)
  public void testCase02() {
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

          opts.result(new EndStartTagEvent(0, EndStartTagKind.VOID_ELEMENT));
        })
    );
  }

  @DataProvider
  public Object[][] testCase03Provider() {
    return new Object[][] {
        {HtmlBytes.xATTRIBUTE88, IntSize.BIT8, IntSize.BIT8},
        {HtmlBytes.xATTRIBUTE816, IntSize.BIT8, IntSize.BIT16},
        {HtmlBytes.xATTRIBUTE824, IntSize.BIT8, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "testCase03Provider", description = """
  regular element:
  - values8
  - attribute
  """)
  public void testCase03(byte proto, IntSize nameSize, IntSize valueSize) {
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

  @Test(dataProvider = "testCase03Provider", description = """
  regular element:
  - values16
  - attribute
  """)
  public void testCase04(byte proto, IntSize nameSize, IntSize valueSize) {
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

  @Test(dataProvider = "testCase03Provider", description = """
  regular element:
  - values16
  - attribute
  """)
  public void testCase05(byte proto, IntSize nameSize, IntSize valueSize) {
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
  public void testCase06() {
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
  public Object[][] testCase07Provider() {
    return new Object[][] {
        {HtmlBytes.xJSACTION88, IntSize.BIT8, IntSize.BIT8},
        {HtmlBytes.xJSACTION816, IntSize.BIT8, IntSize.BIT16},
        {HtmlBytes.xJSACTION824, IntSize.BIT8, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "testCase07Provider", description = """
  regular element:
  - values8
  - js action attribute
  """)
  public void testCase07(byte proto, IntSize nameSize, IntSize valueSize) {
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
  public Object[][] testCase08Provider() {
    return new Object[][] {
        {HtmlBytes.xTEXT8, IntSize.BIT8},
        {HtmlBytes.xTEXT16, IntSize.BIT16},
        {HtmlBytes.xTEXT24, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "testCase08Provider", description = """
  regular element:
  - values8
  - text node
  """)
  public void testCase08(byte proto, IntSize textSize) {
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

    assertEquals(res, new RenderTextState(ctx, ScanLevel.NESTED, textSize));

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
          event = new EndStartTagEvent(0, EndStartTagKind.ELEMENT);

          opts.result(event);
        })
    );
  }

  @DataProvider
  public Object[][] rawCase08Provider() {
    return new Object[][] {
        {HtmlBytes.xRAW8, IntSize.BIT8},
        {HtmlBytes.xRAW16, IntSize.BIT16},
        {HtmlBytes.xRAW24, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "rawCase08Provider", description = """
  regular element:
  - values8
  - raw node
  """)
  public void rawCase08(byte proto, IntSize rawSize) {
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

    assertEquals(res, new RenderRawState(ctx, ScanLevel.NESTED, rawSize));

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
          event = new EndStartTagEvent(0, EndStartTagKind.ELEMENT);

          opts.result(event);
        })
    );
  }

  @DataProvider
  public Object[][] testCase09Provider() {
    return new Object[][] {
        {HtmlBytes.xTHEADING8, IntSize.BIT8},
        {HtmlBytes.xTHEADING16, IntSize.BIT16},
        {HtmlBytes.xTHEADING24, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "testCase09Provider", description = """
  regular element:
  - values8
  - testable node
  """)
  public void testCase09(byte proto, IntSize testSize) {
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

    assertEquals(res, new RenderTestableTextState(ctx, TestableKind.HEADING, testSize));

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
          event = new EndStartTagEvent(0, EndStartTagKind.ELEMENT);

          opts.result(event);
        })
    );
  }

  @DataProvider
  public Object[][] testCase10Provider() {
    return new Object[][] {
        {HtmlBytes.oTHEADING8, IntSize.BIT8},
        {HtmlBytes.oTHEADING16, IntSize.BIT16},
        {HtmlBytes.oTHEADING24, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "testCase10Provider", description = """
  regular element:
  - values8
  - testable-only node
  """)
  public void testCase10(byte proto, IntSize testSize) {
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

    assertEquals(res, new RenderTestableOnlyState(ctx, ScanLevel.NESTED, TestableKind.HEADING, testSize));

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
          event = new EndStartTagEvent(0, EndStartTagKind.ELEMENT);

          opts.result(event);
        })
    );
  }

  @Test(description = """
  void element:
  - values8
  - text node
  - malformed
  """)
  public void testCase11() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xTEXT8);
      opts.addInt8(opts.addObject("Ipsum"));

      opts.push(FrameY.create(f -> {
        f.index = 1;

        f.voidElem(ElementName.INPUT);

        f.values8(0, 1);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    try {
      subject.compute();

      fail();
    } catch (IllegalArgumentException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Invalid compiled HTML code");
    }
  }

  @Test(description = """
  regular element:
  - values8
  - document node op code
  - not an element attribute op
  """)
  public void testCase12() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.DOCTYPE);
      opts.addInt8(0);

      opts.push(FrameY.create(f -> {
        f.index = 1;

        f.regular(ElementName.SPAN);

        f.values8(1, 2);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    try {
      subject.compute();

      fail();
    } catch (IllegalStateException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Not an element attribute op");
    }
  }

  @Test(description = """
  regular element:
  - values8
  - unknown op code
  """)
  public void testCase13() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte((byte) 0x7F);
      opts.addInt8(0);

      opts.push(FrameY.create(f -> {
        f.index = 1;

        f.regular(ElementName.SPAN);

        f.values8(1, 2);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    try {
      subject.compute();

      fail();
    } catch (IllegalArgumentException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Invalid compiled HTML code");
    }
  }

}