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
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ScanDocumentNodeStateTest {

  @DataProvider
  public Object[][] compute01Provider() {
    return new Object[][] {
        {false},
        {true}
    };
  }

  @Test(dataProvider = "compute01Provider", description = """
  end of document
  """)
  public void compute01(boolean emitted) {
    test(
        ScanDocumentNodeState::compute,

        opts -> {
          opts.emitted = emitted;

          opts.push(f -> {
            f.index = 0;
          });
        },

        opts -> {
          opts.emitted = emitted;

          opts.push(f -> {
            f.index = 0;
          });
        },

        ctx -> new EndDocumentState(ctx, !emitted)
    );
  }

  @DataProvider
  public Object[][] compute02Provider() {
    return new Object[][] {
        {HtmlBytes.DOCTYPE, s(ctx -> new EmitDoctypeState(ctx))},

        {HtmlBytes.ELEMSTR8, s(ctx -> new BeginStartTagState(ctx, ScanLevel.TOP_LEVEL, ElementKind.REGULAR, IntSize.BIT8))},
        {HtmlBytes.ELEMVOID8, s(ctx -> new BeginStartTagState(ctx, ScanLevel.TOP_LEVEL, ElementKind.VOID, IntSize.BIT8))},

        {HtmlBytes.FRAGEND, s(ctx -> new ScanDocumentNodeState(ctx))},

        {HtmlBytes.THEADING8, s(ctx -> new EmitTestableHeadingState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.THEADING16, s(ctx -> new EmitTestableHeadingState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.THEADING24, s(ctx -> new EmitTestableHeadingState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.TCELL8, s(ctx -> new EmitTestableCellState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.TCELL16, s(ctx -> new EmitTestableCellState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.TCELL24, s(ctx -> new EmitTestableCellState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.TEXT8, s(ctx -> new EmitTextState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.TEXT16, s(ctx -> new EmitTextState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.TEXT24, s(ctx -> new EmitTextState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.RAW8, s(ctx -> new EmitRawState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.RAW16, s(ctx -> new EmitRawState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.RAW24, s(ctx -> new EmitRawState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.xELEMSTR8, s(ctx -> new SkipRegularElementState(ctx, IntSize.BIT8))},

        {HtmlBytes.xELEMVOID8, s(ctx -> new SkipVoidElementState(ctx, IntSize.BIT8))},

        {HtmlBytes.xFRAGSTR, s(ctx -> new SkipFragmentState(ctx))}
    };
  }

  @Test(dataProvider = "compute02Provider", description = """
  Dispatch to the next state
  """)
  public void compute02(byte proto, Function<Ctx, State> state) {
    test(
        ScanDocumentNodeState::compute,

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });

          opts.addByte(proto);
        },

        opts -> {
          opts.push(f -> {
            f.index = 1;
          });
        },

        state
    );
  }

  @DataProvider
  public Object[][] compute03Provider() {
    return new Object[][] {
        {HtmlBytes.FRAGSTR, 3},

        {HtmlBytes.xATTRIBUTE88, 2},
        {HtmlBytes.xATTRIBUTE816, 3},
        {HtmlBytes.xATTRIBUTE824, 4},

        {HtmlBytes.xBOOLEAN8, 1},

        {HtmlBytes.xJSACTION88, 2},
        {HtmlBytes.xJSACTION816, 3},
        {HtmlBytes.xJSACTION824, 4},

        {HtmlBytes.xTEXT8, 1},
        {HtmlBytes.xTEXT16, 2},
        {HtmlBytes.xTEXT24, 3},

        {HtmlBytes.xRAW8, 1},
        {HtmlBytes.xRAW16, 2},
        {HtmlBytes.xRAW24, 3},

        {HtmlBytes.xTHEADING8, 2},
        {HtmlBytes.xTHEADING16, 3},
        {HtmlBytes.xTHEADING24, 4},

        {HtmlBytes.xTCELL8, 2},
        {HtmlBytes.xTCELL16, 4},
        {HtmlBytes.xTCELL24, 6},

        {HtmlBytes.oTHEADING8, 2},
        {HtmlBytes.oTHEADING16, 3},
        {HtmlBytes.oTHEADING24, 4},

        {HtmlBytes.oTCELL8, 2},
        {HtmlBytes.oTCELL16, 4},
        {HtmlBytes.oTCELL24, 6}
    };
  }

  @Test(dataProvider = "compute03Provider", description = """
  Skip to next document node
  """)
  public void compute03(byte proto, int expectedCount) {
    test(
        ScanDocumentNodeState::compute,

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });

          opts.addByte(proto);
        },

        opts -> {
          opts.push(f -> {
            f.index = 1;
          });
        },

        ctx -> new SkipState(ctx, expectedCount)
    );
  }

  @DataProvider
  public Object[][] compute04Provider() {
    return new Object[][] {
        {HtmlBytes.ATTRIBUTE88},
        {HtmlBytes.ATTRIBUTE816},
        {HtmlBytes.ATTRIBUTE824},

        {HtmlBytes.BOOLEAN8},

        {HtmlBytes.JSACTION88},
        {HtmlBytes.JSACTION816},
        {HtmlBytes.JSACTION824},

        {HtmlBytes.VALUES8},
        {HtmlBytes.VALUES16},
        {HtmlBytes.VALUES24}
    };
  }

  @Test(dataProvider = "compute04Provider", description = """
  Unhandled op code
  """)
  public void compute04(byte proto) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(f -> {
        f.index = 0;
      });

      opts.addByte(proto);
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
  Unknown op code
  """)
  public void compute05() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(f -> {
        f.index = 0;
      });

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

  private Function<Ctx, State> s(Function<Ctx, State> state) {
    return state;
  }

  private void test(
      Function<ScanDocumentNodeState, State> op,
      Consumer<? super CtxY> opts, Consumer<? super CtxY> expected, Function<Ctx, State> state) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

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
