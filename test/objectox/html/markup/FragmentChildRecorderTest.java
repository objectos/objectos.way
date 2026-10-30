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
import java.util.stream.IntStream;
import objectox.html.IntSize;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FragmentChildRecorderTest {

  @Test(description = "ValueRecorder delegation (ELEMSTR8)")
  public void testCase01() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(2);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3, 1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(2);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        }
    );
  }

  @Test(description = "ValueRecorder delegation (ELEMVOID8)")
  public void testCase02() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(4);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3, 1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xELEMVOID8);
          opts.addInt8(4);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        }
    );
  }

  @DataProvider
  public Object[][] testCase03Provider() {
    return new Object[][] {
        {HtmlBytes.TEXT8, HtmlBytes.xTEXT8, "text (1-byte value index)"},
        {HtmlBytes.TEXT16, HtmlBytes.xTEXT16, "text (2-byte value index)"},
        {HtmlBytes.TEXT24, HtmlBytes.xTEXT24, "text (3-byte value index)"},
        {HtmlBytes.RAW8, HtmlBytes.xRAW8, "raw text (1-byte value index)"},
        {HtmlBytes.RAW16, HtmlBytes.xRAW16, "raw text (2-byte value index)"},
        {HtmlBytes.RAW24, HtmlBytes.xRAW24, "raw text (3-byte value index)"}
    };
  }

  @Test(dataProvider = "testCase03Provider", description = "ValueRecorder delegation (all widths)")
  public void testCase03(byte opcode, byte xOpcode, String description) {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(opcode);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3, 1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(xOpcode);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        }
    );
  }

  @DataProvider
  public Object[][] testCase04Provider() {
    return new Object[][] {
        {IntSize.BIT8},
        {IntSize.BIT16},
        {IntSize.BIT24}
    };
  }

  @Test(dataProvider = "testCase04Provider", description = "ValueRecorder delegation (all value sizes)")
  public void testCase04(IntSize size) {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(2);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        size, 3,
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(2);
          opts.addByte(HtmlBytes.VALUES8);
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
  public void testCase05() {
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
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(description = "walk stops at the end of the sink")
  public void testCase06() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(2);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        },

        IntSize.BIT8, 3,
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(2);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addInt8(7);
        }
    );
  }

  @DataProvider
  public Object[][] testCase07Provider() {
    return new Object[][] {
        {bytes(HtmlBytes.xFRAGSTR, 0, 0, 11), "unconsumed fragment (3-byte jump operand)"},
        {bytes(HtmlBytes.xELEMSTR8, 3, HtmlBytes.VALUES8, 1, 9), "unconsumed regular element (1 value)"},
        {bytes(HtmlBytes.xELEMVOID8, 4, HtmlBytes.VALUES8, 0), "unconsumed void element (no values)"},
        {bytes(HtmlBytes.xTHEADING8, 1, 0), "unconsumed testable heading (1-byte value index)"},
        {bytes(HtmlBytes.xTHEADING16, 1, 0, 0), "unconsumed testable heading (2-byte value index)"},
        {bytes(HtmlBytes.xTHEADING24, 1, 0, 0, 0), "unconsumed testable heading (3-byte value index)"},
        {bytes(HtmlBytes.xTCELL8, 0), "unconsumed testable cell (1-byte value index)"},
        {bytes(HtmlBytes.xTCELL16, 0, 0), "unconsumed testable cell (2-byte value index)"},
        {bytes(HtmlBytes.xTCELL24, 0, 0, 0), "unconsumed testable cell (3-byte value index)"},
        {bytes(HtmlBytes.xTEXT8, 0x7F), "unconsumed text (1-byte value index)"},
        {bytes(HtmlBytes.xTEXT16, 0x7F, 0x7F), "unconsumed text (2-byte value index)"},
        {bytes(HtmlBytes.xTEXT24, 0x7F, 0x7F, 0x7F), "unconsumed text (3-byte value index)"},
        {bytes(HtmlBytes.xRAW8, 0x7F), "unconsumed raw text (1-byte value index)"},
        {bytes(HtmlBytes.xRAW16, 0x7F, 0x7F), "unconsumed raw text (2-byte value index)"},
        {bytes(HtmlBytes.xRAW24, 0x7F, 0x7F, 0x7F), "unconsumed raw text (3-byte value index)"},
        {bytes(HtmlBytes.ATTRIBUTE88, 2, 0), "attribute (1-byte name and value indexes)"},
        {bytes(HtmlBytes.xATTRIBUTE88, 2, 0), "consumed attribute (1-byte name and value indexes)"},
        {bytes(HtmlBytes.JSACTION88, 2, 0), "JS action attribute (1-byte name and value indexes)"},
        {bytes(HtmlBytes.xJSACTION88, 2, 0), "consumed JS action attribute (1-byte name and value indexes)"},
        {bytes(HtmlBytes.ATTRIBUTE816, 2, 0, 0), "attribute (1-byte name, 2-byte value indexes)"},
        {bytes(HtmlBytes.xATTRIBUTE816, 2, 0, 0), "consumed attribute (1-byte name, 2-byte value indexes)"},
        {bytes(HtmlBytes.JSACTION816, 2, 0, 0), "JS action attribute (1-byte name, 2-byte value indexes)"},
        {bytes(HtmlBytes.xJSACTION816, 2, 0, 0), "consumed JS action attribute (1-byte name, 2-byte value indexes)"},
        {bytes(HtmlBytes.ATTRIBUTE824, 2, 0, 0, 0), "attribute (1-byte name, 3-byte value indexes)"},
        {bytes(HtmlBytes.xATTRIBUTE824, 2, 0, 0, 0), "consumed attribute (1-byte name, 3-byte value indexes)"},
        {bytes(HtmlBytes.JSACTION824, 2, 0, 0, 0), "JS action attribute (1-byte name, 3-byte value indexes)"},
        {bytes(HtmlBytes.xJSACTION824, 2, 0, 0, 0), "consumed JS action attribute (1-byte name, 3-byte value indexes)"},
        {bytes(HtmlBytes.BOOLEAN8, 2), "boolean attribute (1-byte value index)"},
        {bytes(HtmlBytes.xBOOLEAN8, 2), "consumed boolean attribute (1-byte value index)"},
        {bytes(HtmlBytes.VALUES8, 1), "unmatched byte (VALUES8 marker)"},
        {bytes(HtmlBytes.DOCTYPE), "unmatched byte (DOCTYPE)"},
        {bytes(10), "unmatched byte (10)"}
    };
  }

  @Test(dataProvider = "testCase07Provider", description = "ignored bytes")
  public void testCase07(byte[] body, String description) {
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
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);

          for (byte b : body) {
            opts.addByte(b);
          }

          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(description = "nested fragment (unconsumed)")
  public void testCase08() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(2);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(18);
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(4);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        3,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(2);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(18);
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addByte(HtmlBytes.xELEMVOID8);
          opts.addInt8(4);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
          opts.addInt8(15);
          opts.addInt8(18);
        }
    );
  }

  @Test(description = "nested fragment (already consumed)")
  public void testCase09() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(14);
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(3);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(14);
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(3);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(14);
        }
    );
  }

  @Test(description = """
  operand byte equal to the ELEMVOID8 opcode is not scanned as an instruction

  The name operand of the xATTRIBUTE88 instruction at index 7 is 130, the index
  of AttributeName::TRANSLATE. As a byte, 130 equals HtmlBytes::ELEMVOID8.
  """)
  public void testCase10() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(130);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(4);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(7);
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(130);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.xELEMVOID8);
          opts.addInt8(4);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(7);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(10);
        }
    );
  }

  @DataProvider
  public Object[][] testCase11Provider() {
    return new Object[][] {
        {HtmlBytes.THEADING8, HtmlBytes.oTHEADING8, 2, "testable heading (2 operand bytes)"},
        {HtmlBytes.THEADING16, HtmlBytes.oTHEADING16, 3, "testable heading (3 operand bytes)"},
        {HtmlBytes.THEADING24, HtmlBytes.oTHEADING24, 4, "testable heading (4 operand bytes)"},
        {HtmlBytes.TCELL8, HtmlBytes.oTCELL8, 1, "testable cell (1 operand byte)"},
        {HtmlBytes.TCELL16, HtmlBytes.oTCELL16, 2, "testable cell (2 operand bytes)"},
        {HtmlBytes.TCELL24, HtmlBytes.oTCELL24, 3, "testable cell (3 operand bytes)"}
    };
  }

  @Test(dataProvider = "testCase11Provider", description = "fragment root testable node becomes o-prefixed")
  public void testCase11(byte opcode, byte oOpcode, int operands, String description) {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(opcode);
          IntStream.range(0, operands).forEach(i -> opts.addByte((byte) i));
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, 3,
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(oOpcode);
          IntStream.range(0, operands).forEach(i -> opts.addByte((byte) i));
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        }
    );
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

    final FragmentChildRecorder subject;
    subject = new FragmentChildRecorder(sink, valueRecorder);

    final int res;
    res = subject.record(size, fragValue);

    assertEquals(res, sum);

    final HtmlSink out;
    out = HtmlSinkY.create(expected);

    assertEquals(sink, out);
  }

}
