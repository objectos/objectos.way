/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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

import java.util.function.Function;
import objectox.html.IntSize;
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CtxTest {

  @DataProvider
  public Object[][] nextScanOpProvider() {
    return new Object[][] {
        {HtmlBytes.DOCTYPE, op(ctx -> new RenderDoctypeState(ctx))},

        {HtmlBytes.ELEMSTR8, op(ctx -> new BeginStartTagState(ctx, ScanLevel.TOP_LEVEL, ElementKind.REGULAR, IntSize.BIT8))},
        {HtmlBytes.ELEMVOID8, op(ctx -> new BeginStartTagState(ctx, ScanLevel.TOP_LEVEL, ElementKind.VOID, IntSize.BIT8))},

        {HtmlBytes.FRAGSTR, op(ctx -> new SkipState(ctx, 3))},
        {HtmlBytes.FRAGEND, op(ctx -> new ScanDocumentNodeState(ctx))},

        {HtmlBytes.TEXT8, op(ctx -> new RenderTextState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.TEXT16, op(ctx -> new RenderTextState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.TEXT24, op(ctx -> new RenderTextState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.RAW8, op(ctx -> new RenderRawState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.RAW16, op(ctx -> new RenderRawState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.RAW24, op(ctx -> new RenderRawState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.THEADING8, op(ctx -> new TestableHeadingState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.THEADING16, op(ctx -> new TestableHeadingState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.THEADING24, op(ctx -> new TestableHeadingState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.TCELL8, op(ctx -> new TestableCellState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.TCELL16, op(ctx -> new TestableCellState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.TCELL24, op(ctx -> new TestableCellState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.TPROP8, op(ctx -> new TestablePropState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.TPROP16, op(ctx -> new TestablePropState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.TPROP24, op(ctx -> new TestablePropState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.TPROPKEY8, op(ctx -> new TestableEntryNameState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.TPROPKEY16, op(ctx -> new TestableEntryNameState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.TPROPKEY24, op(ctx -> new TestableEntryNameState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.TPROPVAL8, op(ctx -> new TestableEntryValueState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8))},
        {HtmlBytes.TPROPVAL16, op(ctx -> new TestableEntryValueState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT16))},
        {HtmlBytes.TPROPVAL24, op(ctx -> new TestableEntryValueState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT24))},

        {HtmlBytes.TNEWLINE, op(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.TOP_LEVEL, TestableKind.NEWLINE, IntSize.BIT8))},

        {HtmlBytes.xELEMSTR8, op(ctx -> new BeginStartTagState(ctx, ScanLevel.NESTED, ElementKind.REGULAR, IntSize.BIT8))},
        {HtmlBytes.xELEMVOID8, op(ctx -> new BeginStartTagState(ctx, ScanLevel.NESTED, ElementKind.VOID, IntSize.BIT8))},

        {HtmlBytes.xATTRIBUTE88, op(ctx -> new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8))},
        {HtmlBytes.xATTRIBUTE816, op(ctx -> new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT16))},
        {HtmlBytes.xATTRIBUTE824, op(ctx -> new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT24))},

        {HtmlBytes.xBOOLEAN8, op(ctx -> new RenderBooleanAttributeState(ctx, IntSize.BIT8))},

        {HtmlBytes.xJSACTION88, op(ctx -> new RenderJsActionAttributeState(ctx, IntSize.BIT8, IntSize.BIT8))},
        {HtmlBytes.xJSACTION816, op(ctx -> new RenderJsActionAttributeState(ctx, IntSize.BIT8, IntSize.BIT16))},
        {HtmlBytes.xJSACTION824, op(ctx -> new RenderJsActionAttributeState(ctx, IntSize.BIT8, IntSize.BIT24))},

        {HtmlBytes.xFRAGSTR, op(ctx -> new SkipFragmentState(ctx))},

        {HtmlBytes.xTEXT8, op(ctx -> new RenderTextState(ctx, ScanLevel.NESTED, IntSize.BIT8))},
        {HtmlBytes.xTEXT16, op(ctx -> new RenderTextState(ctx, ScanLevel.NESTED, IntSize.BIT16))},
        {HtmlBytes.xTEXT24, op(ctx -> new RenderTextState(ctx, ScanLevel.NESTED, IntSize.BIT24))},

        {HtmlBytes.xRAW8, op(ctx -> new RenderRawState(ctx, ScanLevel.NESTED, IntSize.BIT8))},
        {HtmlBytes.xRAW16, op(ctx -> new RenderRawState(ctx, ScanLevel.NESTED, IntSize.BIT16))},
        {HtmlBytes.xRAW24, op(ctx -> new RenderRawState(ctx, ScanLevel.NESTED, IntSize.BIT24))},

        {HtmlBytes.xTHEADING8, op(ctx -> new TestableHeadingState(ctx, TestableLevel.REGULAR, IntSize.BIT8))},
        {HtmlBytes.xTHEADING16, op(ctx -> new TestableHeadingState(ctx, TestableLevel.REGULAR, IntSize.BIT16))},
        {HtmlBytes.xTHEADING24, op(ctx -> new TestableHeadingState(ctx, TestableLevel.REGULAR, IntSize.BIT24))},

        {HtmlBytes.xTCELL8, op(ctx -> new TestableCellState(ctx, TestableLevel.REGULAR, IntSize.BIT8))},
        {HtmlBytes.xTCELL16, op(ctx -> new TestableCellState(ctx, TestableLevel.REGULAR, IntSize.BIT16))},
        {HtmlBytes.xTCELL24, op(ctx -> new TestableCellState(ctx, TestableLevel.REGULAR, IntSize.BIT24))},

        {HtmlBytes.xTPROP8, op(ctx -> new TestablePropState(ctx, TestableLevel.REGULAR, IntSize.BIT8))},
        {HtmlBytes.xTPROP16, op(ctx -> new TestablePropState(ctx, TestableLevel.REGULAR, IntSize.BIT16))},
        {HtmlBytes.xTPROP24, op(ctx -> new TestablePropState(ctx, TestableLevel.REGULAR, IntSize.BIT24))},

        {HtmlBytes.xTPROPKEY8, op(ctx -> new TestableEntryNameState(ctx, TestableLevel.REGULAR, IntSize.BIT8))},
        {HtmlBytes.xTPROPKEY16, op(ctx -> new TestableEntryNameState(ctx, TestableLevel.REGULAR, IntSize.BIT16))},
        {HtmlBytes.xTPROPKEY24, op(ctx -> new TestableEntryNameState(ctx, TestableLevel.REGULAR, IntSize.BIT24))},

        {HtmlBytes.xTPROPVAL8, op(ctx -> new TestableEntryValueState(ctx, TestableLevel.REGULAR, IntSize.BIT8))},
        {HtmlBytes.xTPROPVAL16, op(ctx -> new TestableEntryValueState(ctx, TestableLevel.REGULAR, IntSize.BIT16))},
        {HtmlBytes.xTPROPVAL24, op(ctx -> new TestableEntryValueState(ctx, TestableLevel.REGULAR, IntSize.BIT24))},

        {HtmlBytes.xTNEWLINE, op(ctx -> new RenderTestableOnlyState(ctx, ScanLevel.NESTED, TestableKind.NEWLINE, IntSize.BIT8))},

        {HtmlBytes.oTHEADING8, op(ctx -> new TestableHeadingState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT8))},
        {HtmlBytes.oTHEADING16, op(ctx -> new TestableHeadingState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT16))},
        {HtmlBytes.oTHEADING24, op(ctx -> new TestableHeadingState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT24))},

        {HtmlBytes.oTCELL8, op(ctx -> new TestableCellState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT8))},
        {HtmlBytes.oTCELL16, op(ctx -> new TestableCellState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT16))},
        {HtmlBytes.oTCELL24, op(ctx -> new TestableCellState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT24))},

        {HtmlBytes.oTPROP8, op(ctx -> new TestablePropState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT8))},
        {HtmlBytes.oTPROP16, op(ctx -> new TestablePropState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT16))},
        {HtmlBytes.oTPROP24, op(ctx -> new TestablePropState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT24))},

        {HtmlBytes.oTPROPKEY8, op(ctx -> new TestableEntryNameState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT8))},
        {HtmlBytes.oTPROPKEY16, op(ctx -> new TestableEntryNameState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT16))},
        {HtmlBytes.oTPROPKEY24, op(ctx -> new TestableEntryNameState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT24))},

        {HtmlBytes.oTPROPVAL8, op(ctx -> new TestableEntryValueState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT8))},
        {HtmlBytes.oTPROPVAL16, op(ctx -> new TestableEntryValueState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT16))},
        {HtmlBytes.oTPROPVAL24, op(ctx -> new TestableEntryValueState(ctx, TestableLevel.TEST_ONLY, IntSize.BIT24))}
    };
  }

  @Test(dataProvider = "nextScanOpProvider", description = """
  - reads the next code byte
  - returns the state corresponding to it
  """)
  public void nextScanOp(byte code, Function<Ctx, ScanOp> factory) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(code);

      opts.push(new Frame(0));
    });

    final ScanOp res;
    res = ctx.nextScanOp();

    final Ctx expectedCtx;
    expectedCtx = CtxY.create(opts -> {
      opts.addByte(code);

      opts.push(new Frame(1));
    });

    assertEquals(ctx, expectedCtx);

    assertEquals(res, factory.apply(expectedCtx));
  }

  @Test(description = """
  - unknown code byte
  - throws IllegalArgumentException
  """)
  public void nextScanOpMalformed() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.VALUES8);

      opts.push(new Frame(0));
    });

    try {
      ctx.nextScanOp();

      fail();
    } catch (IllegalArgumentException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Invalid compiled HTML code");
    }
  }

  private Function<Ctx, ScanOp> op(Function<Ctx, ScanOp> factory) {
    return factory;
  }

}
