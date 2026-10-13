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
import java.util.function.Consumer;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.IntSize;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ScanDocumentNodeStateTest {

  @Test(description = """
  empty document
  """)
  public void testCase00() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new StopState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.push(new Frame(0));
        })
    );
  }

  @Test(description = """
  - doctype
  """)
  public void testCase01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      opts.addByte(HtmlBytes.DOCTYPE);
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderDoctypeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new Frame(1));
        })
    );
  }

  @DataProvider
  public Object[][] testCase02Provider() {
    return new Object[][] {
        {HtmlBytes.ELEMSTR8, ElementName.DIV, ElementKind.REGULAR, IntSize.BIT8},
        {HtmlBytes.ELEMVOID8, ElementName.HR, ElementKind.VOID, IntSize.BIT8}
    };
  }

  @Test(dataProvider = "testCase02Provider", description = """
  StartState -> BeginStartTagState
  """)
  public void testCase02(byte proto, ElementName elementName, ElementKind elementKind, IntSize nameSize) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      opts.addByte(proto);
      opts.addInt8(elementName.index());
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new BeginStartTagState(ctx, ScanLevel.TOP_LEVEL, elementKind, nameSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new Frame(1));
        })
    );
  }

  @DataProvider
  public Object[][] testCase03Provider() {
    final JsAction action;
    action = Js.noop();

    return new Object[][] {
        {ctx(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CONTENTEDITABLE.index());
          opts.addInt8(opts.addObject("false"));
        }), 2, "attribute 8/8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xATTRIBUTE816);
          opts.addInt8(AttributeName.CONTENTEDITABLE.index());
          opts.addInt16(opts.addObject("false"));
        }), 3, "attribute 8/16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xATTRIBUTE824);
          opts.addInt8(AttributeName.CONTENTEDITABLE.index());
          opts.addInt24(opts.addObject("false"));
        }), 4, "attribute 8/24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xBOOLEAN8);
          opts.addInt8(AttributeName.AUTOFOCUS.index());
        }), 1, "boolean 8"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xJSACTION88);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt8(opts.addObject(action));
        }), 2, "jsaction 8/8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xJSACTION816);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt16(opts.addObject(action));
        }), 3, "jsaction 8/16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xJSACTION824);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt24(opts.addObject(action));
        }), 4, "jsaction 8/24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(opts.addObject("Ipsum"));
        }), 1, "text 8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xTEXT16);
          opts.addInt16(opts.addObject("Ipsum"));
        }), 2, "text 16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xTEXT24);
          opts.addInt24(opts.addObject("Ipsum"));
        }), 3, "text 24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xRAW8);
          opts.addInt8(opts.addObject("Ipsum"));
        }), 1, "raw 8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xRAW16);
          opts.addInt16(opts.addObject("Ipsum"));
        }), 2, "raw 16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xRAW24);
          opts.addInt24(opts.addObject("Ipsum"));
        }), 3, "raw 24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTHEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("Ipsum"));
        }), 2, "testable heading 8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xTHEADING16);
          opts.addByte((byte) 1);
          opts.addInt16(opts.addObject("Ipsum"));
        }), 3, "testable heading 16"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTCELL8);
          opts.addInt8(3);
          opts.addInt8(opts.addObject("Ipsum"));
        }), 2, "testable cell 8"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTCELL16);
          opts.addInt16(300);
          opts.addInt16(opts.addObject("Ipsum"));
        }), 4, "testable cell 16"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTPROP8);
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(opts.addObject("bar"));
        }), 2, "testable prop 8"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTPROP16);
          opts.addInt16(opts.addObject("foo"));
          opts.addInt16(opts.addObject("bar"));
        }), 4, "testable prop 16"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTPROPKEY8);
          opts.addInt8(opts.addObject("foo"));
        }), 1, "testable prop key 8"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTPROPKEY16);
          opts.addInt16(opts.addObject("foo"));
        }), 2, "testable prop key 16"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTPROPVAL8);
          opts.addInt8(opts.addObject("bar"));
        }), 1, "testable prop value 8"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTPROPVAL16);
          opts.addInt16(opts.addObject("bar"));
        }), 2, "testable prop value 16"}
    };
  }

  @Test(dataProvider = "testCase03Provider", description = """
  Skip to next document node
  """)
  public void testCase03(Consumer<CtxY> prefix, int expectedCount, String description) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      prefix.accept(opts);
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new SkipState(ctx, expectedCount));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));
        })
    );
  }

  @Test(description = """
  Consumed regular element marker
  """)
  public void testCase07() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new SkipRegularElementState(ctx, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));
        })
    );
  }

  @Test(description = """
  Consumed void element marker
  """)
  public void testCase08() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      opts.addByte(HtmlBytes.xELEMVOID8);
      opts.addInt8(ElementName.HR.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new SkipVoidElementState(ctx, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));
        })
    );
  }

  @Test(description = """
  Consumed fragment marker
  """)
  public void testCase09() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      opts.addByte(HtmlBytes.xFRAGSTR);
      opts.addInt24(5);
      opts.addByte(HtmlBytes.FRAGEND);
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new SkipFragmentState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));
        })
    );
  }

  @DataProvider
  public Object[][] testCase04Provider() {
    return new Object[][] {
        {HtmlBytes.RAW8, IntSize.BIT8, "raw 8"},
        {HtmlBytes.RAW16, IntSize.BIT16, "raw 16"},
        {HtmlBytes.RAW24, IntSize.BIT24, "raw 24"}
    };
  }

  @Test(dataProvider = "testCase04Provider", description = """
  Render raw node
  """)
  public void testCase04(byte proto, IntSize rawSize, String description) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(proto);
      opts.addInt8(opts.addObject("Ipsum"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderRawState(ctx, ScanLevel.TOP_LEVEL, rawSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));
        })
    );
  }

  @DataProvider
  public Object[][] testCase05Provider() {
    return new Object[][] {
        {HtmlBytes.THEADING8, IntSize.BIT8, "testable heading 8"},
        {HtmlBytes.THEADING16, IntSize.BIT16, "testable heading 16"},
        {HtmlBytes.THEADING24, IntSize.BIT24, "testable heading 24"}
    };
  }

  @Test(dataProvider = "testCase05Provider", description = """
  Render testable node at the document root as testable-only
  """)
  public void testCase05(byte proto, IntSize testSize, String description) {
    final int objectsIndex;
    objectsIndex = testSize == IntSize.BIT24 ? HtmlSink.MAX_INT16 + 1 : testSize == IntSize.BIT16 ? HtmlSink.MAX_INT8 + 1 : 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(objectsIndex);

      final int valueIndex;
      valueIndex = opts.addObject("Ipsum");

      opts.addByte(proto);

      opts.addByte((byte) 1);

      opts.addInt(testSize, valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.HEADING, testSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));
        })
    );
  }

  @Test(description = """
  Consumed fragment end marker
  """)
  public void testCase10() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      opts.addByte(HtmlBytes.FRAGEND);
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));
        })
    );
  }

  @Test(description = """
  Consumed optional new line marker
  """)
  public void testCase11() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      opts.addByte(HtmlBytes.xTNEWLINE);
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));
        })
    );
  }

  @Test(description = """
  Unknown op code
  """)
  public void testCase12() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      opts.addByte((byte) 0x7F);
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

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
  factory:
  - pushes a scan document node frame
  """)
  public void of() {
    final Ctx ctx;
    ctx = CtxY.create(_ -> {});

    final ScanDocumentNodeState res;
    res = ScanDocumentNodeState.of(ctx);

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.push(new Frame(0));
        })
    );
  }

  private Consumer<? super CtxY> ctx(Consumer<? super CtxY> opts) {
    return opts;
  }

}
