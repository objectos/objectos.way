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
import static org.testng.Assert.assertSame;
import java.util.function.Consumer;
import objectos.html.TestableName;
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

    assertSame(res, subject);

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(RenderDoctypeEvent.of(0));
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

    assertEquals(res, new BeginStartTagState(ctx, elementKind, nameSize));

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
        }), 4, "attribute 8/8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xATTRIBUTE816);
          opts.addInt8(AttributeName.CONTENTEDITABLE.index());
          opts.addInt16(opts.addObject("false"));
        }), 5, "attribute 8/16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xATTRIBUTE824);
          opts.addInt8(AttributeName.CONTENTEDITABLE.index());
          opts.addInt24(opts.addObject("false"));
        }), 6, "attribute 8/24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xBOOLEAN8);
          opts.addInt8(AttributeName.AUTOFOCUS.index());
        }), 3, "boolean 8"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xJSACTION88);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt8(opts.addObject(action));
        }), 4, "jsaction 8/8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xJSACTION816);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt16(opts.addObject(action));
        }), 5, "jsaction 8/16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xJSACTION824);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt24(opts.addObject(action));
        }), 6, "jsaction 8/24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(ElementName.SPAN.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }), 5, "consumed element regular"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xELEMVOID8);
          opts.addInt8(ElementName.HR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }), 5, "consumed element void"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(5);
          opts.addByte(HtmlBytes.FRAGEND);
        }), 6, "consumed fragment empty"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(8);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CONTENTEDITABLE.index());
          opts.addInt8(opts.addObject("false"));
          opts.addByte(HtmlBytes.FRAGEND);
        }), 9, "consumed fragment one child"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(opts.addObject("Ipsum"));
        }), 3, "text 8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xTEXT16);
          opts.addInt16(opts.addObject("Ipsum"));
        }), 4, "text 16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xTEXT24);
          opts.addInt24(opts.addObject("Ipsum"));
        }), 5, "text 24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xRAW8);
          opts.addInt8(opts.addObject("Ipsum"));
        }), 3, "raw 8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xRAW16);
          opts.addInt16(opts.addObject("Ipsum"));
        }), 4, "raw 16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xRAW24);
          opts.addInt24(opts.addObject("Ipsum"));
        }), 5, "raw 24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTEST8);
          opts.addInt8(opts.addObject("Ipsum"));
        }), 3, "test 8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xTEST16);
          opts.addInt16(opts.addObject("Ipsum"));
        }), 4, "test 16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xTEST24);
          opts.addInt24(opts.addObject("Ipsum"));
        }), 5, "test 24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTESTONLY8);
          opts.addInt8(opts.addObject("Ipsum"));
        }), 3, "test only 8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xTESTONLY16);
          opts.addInt16(opts.addObject("Ipsum"));
        }), 4, "test only 16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xTESTONLY24);
          opts.addInt24(opts.addObject("Ipsum"));
        }), 5, "test only 24"}
    };
  }

  @Test(dataProvider = "testCase03Provider", description = """
  Skip to element start
  """)
  public void testCase03(Consumer<CtxY> prefix, int expectedIndex, String description) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      prefix.accept(opts);

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
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

          opts.push(FrameY.create(f -> {
            f.index = expectedIndex;
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

    assertEquals(res, new RenderRawState(ctx, rawSize));

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
        {HtmlBytes.TEST8, IntSize.BIT8, "test 8"},
        {HtmlBytes.TEST16, IntSize.BIT16, "test 16"},
        {HtmlBytes.TEST24, IntSize.BIT24, "test 24"}
    };
  }

  @Test(dataProvider = "testCase05Provider", description = """
  Render testable node
  """)
  public void testCase05(byte proto, IntSize testSize, String description) {
    final int objectsIndex;
    objectsIndex = testSize == IntSize.BIT24 ? HtmlSink.MAX_INT16 + 1 : testSize == IntSize.BIT16 ? HtmlSink.MAX_INT8 + 1 : 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(objectsIndex);

      opts.addObject(TestableName.H1);

      final int valueIndex;
      valueIndex = opts.addObject("Ipsum");

      opts.addByte(proto);

      opts.addInt(testSize, valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableTextState(ctx, testSize));

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
  public Object[][] testCase06Provider() {
    return new Object[][] {
        {HtmlBytes.TESTONLY8, IntSize.BIT8, "test only 8"},
        {HtmlBytes.TESTONLY16, IntSize.BIT16, "test only 16"},
        {HtmlBytes.TESTONLY24, IntSize.BIT24, "test only 24"}
    };
  }

  @Test(dataProvider = "testCase06Provider", description = """
  Render testable-only node
  """)
  public void testCase06(byte proto, IntSize testSize, String description) {
    final int objectsIndex;
    objectsIndex = testSize == IntSize.BIT24 ? HtmlSink.MAX_INT16 + 1 : testSize == IntSize.BIT16 ? HtmlSink.MAX_INT8 + 1 : 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(objectsIndex);

      opts.addObject(TestableName.H1);

      final int valueIndex;
      valueIndex = opts.addObject("Ipsum");

      opts.addByte(proto);

      opts.addInt(testSize, valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableOnlyState(ctx, testSize));

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
