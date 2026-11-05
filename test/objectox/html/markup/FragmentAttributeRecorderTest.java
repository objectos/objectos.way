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
package objectox.html.markup;

import static org.testng.Assert.assertEquals;

import java.util.function.Consumer;
import objectox.html.IntSize;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FragmentAttributeRecorderTest {

  @DataProvider
  public Object[][] testCase01Provider() {
    return new Object[][] {
        {HtmlBytes.ATTRIBUTE88, HtmlBytes.xATTRIBUTE88, 2, "ATTRIBUTE88"},
        {HtmlBytes.ATTRIBUTE816, HtmlBytes.xATTRIBUTE816, 3, "ATTRIBUTE816"},
        {HtmlBytes.ATTRIBUTE824, HtmlBytes.xATTRIBUTE824, 4, "ATTRIBUTE824"},
        {HtmlBytes.JSACTION816, HtmlBytes.xJSACTION816, 3, "JSACTION816"},
        {HtmlBytes.JSACTION824, HtmlBytes.xJSACTION824, 4, "JSACTION824"},
        {HtmlBytes.BOOLEAN8, HtmlBytes.xBOOLEAN8, 1, "BOOLEAN8"}
    };
  }

  @Test(dataProvider = "testCase01Provider", description = "ValueRecorder delegation (all widths)")
  public void testCase01(byte opcode, byte xOpcode, int body, String description) {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(opcode);

          for (int i = 0; i < body; i++) {
            opts.addInt8(0);
          }

          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(xOpcode);

          for (int i = 0; i < body; i++) {
            opts.addInt8(0);
          }

          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        }
    );
  }

  @DataProvider
  public Object[][] testCase02Provider() {
    return new Object[][] {
        {IntSize.BIT8},
        {IntSize.BIT16},
        {IntSize.BIT24}
    };
  }

  @Test(dataProvider = "testCase02Provider", description = "ValueRecorder delegation (all value sizes)")
  public void testCase02(IntSize size) {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        size, 3,
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);

          switch (size) {
            case BIT8 -> opts.addInt8(7);

            case BIT16 -> opts.addInt16(7);

            case BIT24 -> opts.addInt24(7);
          }
        }
    );
  }

  @Test(description = "FRAGEND stops the walk")
  public void testCase03() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        0,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(
      description = "fragment without FRAGEND is malformed",
      expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "Unexpected opcode: 7 at index 10"
  )
  public void testCase04() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(2);
      opts.addInt8(0);
    });

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentAttributeRecorder subject;
    subject = new FragmentAttributeRecorder(sink, valueRecorder);

    subject.record(ElementName.DIV, IntSize.BIT8, 3);
  }

  @DataProvider
  public Object[][] testCase05Provider() {
    return new Object[][] {
        {bytes(HtmlBytes.TEXT8, 0x7F), "TEXT8"},
        {bytes(HtmlBytes.TEXT16, 0x7F, 0x7F), "TEXT16"},
        {bytes(HtmlBytes.TEXT24, 0x7F, 0x7F, 0x7F), "TEXT24"},
        {bytes(HtmlBytes.RAW8, 0x7F), "RAW8"},
        {bytes(HtmlBytes.RAW16, 0x7F, 0x7F), "RAW16"},
        {bytes(HtmlBytes.RAW24, 0x7F, 0x7F, 0x7F), "RAW24"},
        {bytes(HtmlBytes.xTEXT8, 0x7F), "xTEXT8"},
        {bytes(HtmlBytes.xRAW8, 0x7F), "xRAW8"},
        {bytes(HtmlBytes.ELEMSTR8, 3, HtmlBytes.VALUES8, 1, 9), "ELEMSTR8"},
        {bytes(HtmlBytes.ELEMVOID8, 4, HtmlBytes.VALUES8, 0), "ELEMVOID8"},
        {bytes(HtmlBytes.xELEMSTR8, 3, HtmlBytes.VALUES8, 1, 9), "xELEMSTR8"},
        {bytes(HtmlBytes.THEADING8, 1, 0), "testable heading (1-byte value index)"},
        {bytes(HtmlBytes.THEADING16, 1, 0, 0), "testable heading (2-byte value index)"},
        {bytes(HtmlBytes.THEADING24, 1, 0, 0, 0), "testable heading (3-byte value index)"},
        {bytes(HtmlBytes.xTHEADING8, 1, 0), "consumed testable heading (1-byte value index)"},
        {bytes(HtmlBytes.xTHEADING16, 1, 0, 0), "consumed testable heading (2-byte value index)"},
        {bytes(HtmlBytes.xTHEADING24, 1, 0, 0, 0), "consumed testable heading (3-byte value index)"},
        {bytes(HtmlBytes.oTHEADING8, 1, 0), "testable-only heading (1-byte value index)"},
        {bytes(HtmlBytes.oTHEADING16, 1, 0, 0), "testable-only heading (2-byte value index)"},
        {bytes(HtmlBytes.oTHEADING24, 1, 0, 0, 0), "testable-only heading (3-byte value index)"},
        {bytes(HtmlBytes.TCELL8, 0), "testable cell (1-byte value index)"},
        {bytes(HtmlBytes.TCELL16, 0, 0), "testable cell (2-byte value index)"},
        {bytes(HtmlBytes.TCELL24, 0, 0, 0), "testable cell (3-byte value index)"},
        {bytes(HtmlBytes.xTCELL8, 0), "consumed testable cell (1-byte value index)"},
        {bytes(HtmlBytes.xTCELL16, 0, 0), "consumed testable cell (2-byte value index)"},
        {bytes(HtmlBytes.xTCELL24, 0, 0, 0), "consumed testable cell (3-byte value index)"},
        {bytes(HtmlBytes.oTCELL8, 0), "testable-only cell (1-byte value index)"},
        {bytes(HtmlBytes.oTCELL16, 0, 0), "testable-only cell (2-byte value index)"},
        {bytes(HtmlBytes.oTCELL24, 0, 0, 0), "testable-only cell (3-byte value index)"},
        {bytes(HtmlBytes.TITEM8, 0), "testable list item (1-byte value index)"},
        {bytes(HtmlBytes.TITEM16, 0, 0), "testable list item (2-byte value index)"},
        {bytes(HtmlBytes.TITEM24, 0, 0, 0), "testable list item (3-byte value index)"},
        {bytes(HtmlBytes.xTITEM8, 0), "consumed testable list item (1-byte value index)"},
        {bytes(HtmlBytes.xTITEM16, 0, 0), "consumed testable list item (2-byte value index)"},
        {bytes(HtmlBytes.xTITEM24, 0, 0, 0), "consumed testable list item (3-byte value index)"},
        {bytes(HtmlBytes.oTITEM8, 0), "testable-only list item (1-byte value index)"},
        {bytes(HtmlBytes.oTITEM16, 0, 0), "testable-only list item (2-byte value index)"},
        {bytes(HtmlBytes.oTITEM24, 0, 0, 0), "testable-only list item (3-byte value index)"},
        {bytes(HtmlBytes.TPROP8, 0), "testable prop (1-byte value index)"},
        {bytes(HtmlBytes.TPROP16, 0, 0), "testable prop (2-byte value index)"},
        {bytes(HtmlBytes.TPROP24, 0, 0, 0), "testable prop (3-byte value index)"},
        {bytes(HtmlBytes.xTPROP8, 0), "consumed testable prop (1-byte value index)"},
        {bytes(HtmlBytes.xTPROP16, 0, 0), "consumed testable prop (2-byte value index)"},
        {bytes(HtmlBytes.xTPROP24, 0, 0, 0), "consumed testable prop (3-byte value index)"},
        {bytes(HtmlBytes.oTPROP8, 0), "testable-only prop (1-byte value index)"},
        {bytes(HtmlBytes.oTPROP16, 0, 0), "testable-only prop (2-byte value index)"},
        {bytes(HtmlBytes.oTPROP24, 0, 0, 0), "testable-only prop (3-byte value index)"},
        {bytes(HtmlBytes.TROW), "testable row"},
        {bytes(HtmlBytes.xTROW), "consumed testable row"},
        {bytes(HtmlBytes.TLIST), "testable list"},
        {bytes(HtmlBytes.xTLIST), "consumed testable list"}
    };
  }

  @Test(dataProvider = "testCase05Provider", description = "ignored bytes")
  public void testCase05(byte[] body, String description) {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);

          for (byte b : body) {
            opts.addByte(b);
          }

          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        0,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);

          for (byte b : body) {
            opts.addByte(b);
          }

          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(description = "JSACTION88 (ValueRecorder delegation)")
  public void testCase06() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.JSACTION88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xJSACTION88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        }
    );
  }

  @Test(description = "nested fragment (unconsumed)")
  public void testCase07() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(18);
          opts.addByte(HtmlBytes.JSACTION88);
          opts.addInt8(3);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        3,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(18);
          opts.addByte(HtmlBytes.xJSACTION88);
          opts.addInt8(3);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
          opts.addInt8(14);
          opts.addInt8(18);
        }
    );
  }

  @Test(description = "nested fragment (already consumed)")
  public void testCase08() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(15);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(15);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(15);
        }
    );
  }

  @Test(description = """
  operand byte equal to the ATTRIBUTE88 opcode is not scanned as an instruction

  The name operand of the ATTRIBUTE88 instruction at index 7 is 131, the index
  of AttributeName::TYPE. As a byte, 131 equals HtmlBytes::ATTRIBUTE88. This
  reproduces TemplateTest::testCase80.
  """)
  public void testCase09() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(131);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(4);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(7);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        0,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(131);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(4);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(7);
          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(description = """
  issue #1152: testable node before an element header

  The xTHEADING8 value index is 129 (0x81), which is the ELEMSTR8 opcode when
  read as a signed byte. If the testable instruction is not skipped, the walker
  reads it as an element header and then reads the SPAN name index (59) as the
  VALUES marker.
  """)
  public void testCase10() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(0x80);
          opts.addByte(HtmlBytes.xTHEADING8);
          opts.addInt8(2);
          opts.addInt8(0x81); // == (byte) HtmlBytes.ELEMSTR8
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.SPAN.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        0,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(0x80);
          opts.addByte(HtmlBytes.xTHEADING8);
          opts.addInt8(2);
          opts.addInt8(0x81); // == (byte) HtmlBytes.ELEMSTR8
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.SPAN.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(description = "testable node does not break attribute alignment")
  public void testCase11() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xTCELL8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xTCELL8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(9);
        }
    );
  }

  @DataProvider
  public Object[][] testCase12Provider() {
    return new Object[][] {
        {bytes(HtmlBytes.VALUES8, 1), "VALUES8"},
        {bytes(HtmlBytes.DOCTYPE), "DOCTYPE"},
        {bytes(10), "unmatched byte"}
    };
  }

  @Test(
      dataProvider = "testCase12Provider",
      description = "unexpected opcode",
      expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "Unexpected opcode: -?\\d+ at index 7"
  )
  public void testCase12(byte[] body, String description) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);

      for (byte b : body) {
        opts.addByte(b);
      }

      opts.addByte(HtmlBytes.FRAGEND);
    });

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentAttributeRecorder subject;
    subject = new FragmentAttributeRecorder(sink, valueRecorder);

    subject.record(ElementName.DIV, IntSize.BIT8, 3);
  }

  private byte[] bytes(int... values) {
    final byte[] res;
    res = new byte[values.length];

    for (int i = 0; i < values.length; i++) {
      res[i] = (byte) values[i];
    }

    return res;
  }

  private void test(
      Consumer<? super HtmlSink> input,
      IntSize size, int fragValue,
      int sum,
      Consumer<? super HtmlSink> expected
  ) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(input);

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentAttributeRecorder subject;
    subject = new FragmentAttributeRecorder(sink, valueRecorder);

    final int res;
    res = subject.record(ElementName.DIV, size, fragValue);

    assertEquals(res, sum);

    final HtmlSink out;
    out = HtmlSinkY.create(expected);

    assertEquals(sink, out);
  }

}
