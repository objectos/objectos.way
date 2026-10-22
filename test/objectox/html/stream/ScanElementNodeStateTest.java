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
import java.util.function.Function;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ScanElementNodeStateTest {

  @Test(description = """
  element:
  - no values left
  """)
  public void compute01() {
    test(
        ScanElementNodeState::compute,

        opts -> {
          opts.push(f -> {
            f.index = 0;

            f.regular(ElementName.DIV);

            f.values8(1, 1);
          });
        },

        opts -> {
          opts.push(f -> {
            f.index = 0;

            f.regular(ElementName.DIV);

            f.values8(1, 1);
          });

          final RenderEndTagEvent event;
          event = new RenderEndTagEvent(0, ElementName.DIV);

          opts.result(event);
        },

        ctx -> new EndElementState(ctx)
    );
  }

  @DataProvider
  public Object[][] elementNodeProvider() {
    return new Object[][] {
        {HtmlBytes.xELEMSTR8, s(ctx -> new BeginStartTagState(ctx, ScanLevel.NESTED, ElementKind.REGULAR, IntSize.BIT8))},
        {HtmlBytes.xELEMVOID8, s(ctx -> new BeginStartTagState(ctx, ScanLevel.NESTED, ElementKind.VOID, IntSize.BIT8))},

        {HtmlBytes.xTEXT8, s(ctx -> new RenderTextState(ctx, ScanLevel.NESTED, IntSize.BIT8))},
        {HtmlBytes.xTEXT16, s(ctx -> new RenderTextState(ctx, ScanLevel.NESTED, IntSize.BIT16))},
        {HtmlBytes.xTEXT24, s(ctx -> new RenderTextState(ctx, ScanLevel.NESTED, IntSize.BIT24))},

        {HtmlBytes.xRAW8, s(ctx -> new RenderRawState(ctx, ScanLevel.NESTED, IntSize.BIT8))},
        {HtmlBytes.xRAW16, s(ctx -> new RenderRawState(ctx, ScanLevel.NESTED, IntSize.BIT16))},
        {HtmlBytes.xRAW24, s(ctx -> new RenderRawState(ctx, ScanLevel.NESTED, IntSize.BIT24))},

        {HtmlBytes.xTHEADING8, s(ctx -> new RenderTestableTextState(ctx, TestableKind.HEADING, IntSize.BIT8))},
        {HtmlBytes.xTHEADING16, s(ctx -> new RenderTestableTextState(ctx, TestableKind.HEADING, IntSize.BIT16))},
        {HtmlBytes.xTHEADING24, s(ctx -> new RenderTestableTextState(ctx, TestableKind.HEADING, IntSize.BIT24))},

        {HtmlBytes.xTCELL8, s(ctx -> new RenderTestableTextState(ctx, TestableKind.CELL, IntSize.BIT8))},
        {HtmlBytes.xTCELL16, s(ctx -> new RenderTestableTextState(ctx, TestableKind.CELL, IntSize.BIT16))},
        {HtmlBytes.xTCELL24, s(ctx -> new RenderTestableTextState(ctx, TestableKind.CELL, IntSize.BIT24))},

        {HtmlBytes.xTPROP8, s(ctx -> new RenderTestableTextState(ctx, TestableKind.PROP, IntSize.BIT8))},
        {HtmlBytes.xTPROP16, s(ctx -> new RenderTestableTextState(ctx, TestableKind.PROP, IntSize.BIT16))},
        {HtmlBytes.xTPROP24, s(ctx -> new RenderTestableTextState(ctx, TestableKind.PROP, IntSize.BIT24))},

        {HtmlBytes.xTPROPKEY8, s(ctx -> new RenderTestableTextState(ctx, TestableKind.PROP_KEY, IntSize.BIT8))},
        {HtmlBytes.xTPROPKEY16, s(ctx -> new RenderTestableTextState(ctx, TestableKind.PROP_KEY, IntSize.BIT16))},
        {HtmlBytes.xTPROPKEY24, s(ctx -> new RenderTestableTextState(ctx, TestableKind.PROP_KEY, IntSize.BIT24))},

        {HtmlBytes.xTPROPVAL8, s(ctx -> new RenderTestableTextState(ctx, TestableKind.PROP_VALUE, IntSize.BIT8))},
        {HtmlBytes.xTPROPVAL16, s(ctx -> new RenderTestableTextState(ctx, TestableKind.PROP_VALUE, IntSize.BIT16))},
        {HtmlBytes.xTPROPVAL24, s(ctx -> new RenderTestableTextState(ctx, TestableKind.PROP_VALUE, IntSize.BIT24))},

        {HtmlBytes.xTNEWLINE, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.NESTED, TestableKind.NEWLINE, IntSize.BIT8))},

        {HtmlBytes.THEADING8, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.HEADING, IntSize.BIT8))},
        {HtmlBytes.THEADING16, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.HEADING, IntSize.BIT16))},
        {HtmlBytes.THEADING24, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.HEADING, IntSize.BIT24))},

        {HtmlBytes.TCELL8, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.CELL, IntSize.BIT8))},
        {HtmlBytes.TCELL16, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.CELL, IntSize.BIT16))},
        {HtmlBytes.TCELL24, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.CELL, IntSize.BIT24))},

        {HtmlBytes.TPROP8, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.PROP, IntSize.BIT8))},
        {HtmlBytes.TPROP16, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.PROP, IntSize.BIT16))},
        {HtmlBytes.TPROP24, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.PROP, IntSize.BIT24))},

        {HtmlBytes.TPROPKEY8, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.PROP_KEY, IntSize.BIT8))},
        {HtmlBytes.TPROPKEY16, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.PROP_KEY, IntSize.BIT16))},
        {HtmlBytes.TPROPKEY24, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.PROP_KEY, IntSize.BIT24))},

        {HtmlBytes.TPROPVAL8, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.PROP_VALUE, IntSize.BIT8))},
        {HtmlBytes.TPROPVAL16, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.PROP_VALUE, IntSize.BIT16))},
        {HtmlBytes.TPROPVAL24, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.PROP_VALUE, IntSize.BIT24))},

        {HtmlBytes.TNEWLINE, s(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.NEWLINE, IntSize.BIT8))}
    };
  }

  @Test(dataProvider = "elementNodeProvider", description = """
  element:
  - values8
  - element node
  """)
  public void compute02(byte proto, Function<Ctx, State> state) {
    final int frameIndex = 1, valueIndex = 0;

    test(
        ScanElementNodeState::compute,

        opts -> {
          opts.addByte(proto);
          opts.addInt8(valueIndex);

          opts.push(f -> {
            f.index = frameIndex;

            f.regular(ElementName.SPAN);

            f.values8(0, 1);
          });
        },

        opts -> {
          opts.push(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.SPAN);

            f.values8(1, 1);
          });

          opts.push(f -> {
            f.index = valueIndex + 1;
          });
        },

        state
    );
  }

  @Test(dataProvider = "elementNodeProvider", description = """
  element:
  - values16
  - element node
  """)
  public void compute03(byte proto, Function<Ctx, State> state) {
    final int offset = HtmlSink.MAX_INT8 + 1;

    final int frameIndex = offset + 1, valueIndex = offset;

    test(
        ScanElementNodeState::compute,

        opts -> {
          opts.codeIndex(offset);
          opts.addByte(proto);
          opts.addInt16(valueIndex);

          opts.push(f -> {
            f.index = frameIndex;

            f.regular(ElementName.SPAN);

            f.values16(0, 1);
          });
        },

        opts -> {
          opts.push(f -> {
            f.index = frameIndex + 2;

            f.regular(ElementName.SPAN);

            f.values16(1, 1);
          });

          opts.push(f -> {
            f.index = valueIndex + 1;
          });
        },

        state
    );
  }

  @Test(dataProvider = "elementNodeProvider", description = """
  element:
  - values24
  - element node
  """)
  public void compute04(byte proto, Function<Ctx, State> state) {
    final int offset = HtmlSink.MAX_INT16 + 1;

    final int frameIndex = offset + 1, valueIndex = offset;

    test(
        ScanElementNodeState::compute,

        opts -> {
          opts.codeIndex(offset);
          opts.addByte(proto);
          opts.addInt24(valueIndex);

          opts.push(f -> {
            f.index = frameIndex;

            f.regular(ElementName.SPAN);

            f.values24(0, 1);
          });
        },

        opts -> {
          opts.push(f -> {
            f.index = frameIndex + 3;

            f.regular(ElementName.SPAN);

            f.values24(1, 1);
          });

          opts.push(f -> {
            f.index = valueIndex + 1;
          });
        },

        state
    );
  }

  @Test(description = """
  element:
  - no values left
  - level 1
  """)
  public void compute05() {
    test(
        ScanElementNodeState::compute,

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });

          opts.push(f -> {
            f.index = 0;

            f.regular(ElementName.DIV);

            f.values8(1, 1);
          });
        },

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });

          opts.push(f -> {
            f.index = 0;

            f.regular(ElementName.DIV);

            f.values8(1, 1);
          });

          final RenderEndTagEvent event;
          event = new RenderEndTagEvent(1, ElementName.DIV);

          opts.result(event);
        },

        ctx -> new EndElementState(ctx)
    );
  }

  @DataProvider
  public Object[][] illegalStateExceptionProvider() {
    return new Object[][] {
        {HtmlBytes.DOCTYPE},

        {HtmlBytes.ELEMSTR8},
        {HtmlBytes.ELEMVOID8},

        {HtmlBytes.FRAGSTR},
        {HtmlBytes.FRAGEND},

        {HtmlBytes.TEXT8},
        {HtmlBytes.TEXT16},
        {HtmlBytes.TEXT24},

        {HtmlBytes.RAW8},
        {HtmlBytes.RAW16},
        {HtmlBytes.RAW24},

        {HtmlBytes.xATTRIBUTE88},
        {HtmlBytes.xATTRIBUTE816},
        {HtmlBytes.xATTRIBUTE824},

        {HtmlBytes.xBOOLEAN8},

        {HtmlBytes.xJSACTION88},
        {HtmlBytes.xJSACTION816},
        {HtmlBytes.xJSACTION824},

        {HtmlBytes.xFRAGSTR}
    };
  }

  @Test(dataProvider = "illegalStateExceptionProvider", description = """
  element:
  - element
  - not an element node op
  """)
  public void compute06(byte proto) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(proto);
      opts.addInt8(0);

      opts.push(f -> {
        f.index = 1;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      });
    });

    final ScanElementNodeState subject;
    subject = new ScanElementNodeState(ctx);

    try {
      subject.compute();

      fail();
    } catch (IllegalStateException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Not an element node op");
    }
  }

  @Test(description = """
  element:
  - unknown op code
  """)
  public void compute07() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte((byte) 0x7F);
      opts.addInt8(0);

      opts.push(f -> {
        f.index = 1;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      });
    });

    final ScanElementNodeState subject;
    subject = new ScanElementNodeState(ctx);

    try {
      subject.compute();

      fail();
    } catch (IllegalArgumentException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Invalid compiled HTML code");
    }
  }

  private Function<Ctx, State> s(Function<Ctx, State> state) {
    return state;
  }

  private void test(
      Function<ScanElementNodeState, State> op,
      Consumer<? super CtxY> opts, Consumer<? super CtxY> expected, Function<Ctx, State> state) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final ScanElementNodeState subject;
    subject = new ScanElementNodeState(ctx);

    final State res;
    res = op.apply(subject);

    final State expectedState;
    expectedState = state.apply(ctx);

    assertEquals(res, expectedState);

    assertEquals(
        ctx,

        CtxY.create(expectedOpts -> {
          expectedOpts.sink(ctx);

          expected.accept(expectedOpts);
        })
    );
  }

}
