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

import objectox.html.IntSize;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FragmentChildRecorderTest {

  @DataProvider
  public Object[][] testCase03Provider() {
    return new Object[][] {
        {HtmlBytes.ELEMSTR8, HtmlBytes.xELEMSTR8, "ELEMSTR8", new byte[] {2, HtmlBytes.VALUES8, 0}},
        {HtmlBytes.ELEMVOID8, HtmlBytes.xELEMVOID8, "ELEMVOID8", new byte[] {4, HtmlBytes.VALUES8, 0}},
        {HtmlBytes.TEXT8, HtmlBytes.xTEXT8, "TEXT8", new byte[0]},
        {HtmlBytes.TEXT16, HtmlBytes.xTEXT16, "TEXT16", new byte[0]},
        {HtmlBytes.TEXT24, HtmlBytes.xTEXT24, "TEXT24", new byte[0]},
        {HtmlBytes.RAW8, HtmlBytes.xRAW8, "RAW8", new byte[0]},
        {HtmlBytes.RAW16, HtmlBytes.xRAW16, "RAW16", new byte[0]},
        {HtmlBytes.RAW24, HtmlBytes.xRAW24, "RAW24", new byte[0]}
    };
  }

  @Test(dataProvider = "testCase03Provider", description = "ValueRecorder delegation (all widths)")
  public void testCase03(byte opcode, byte xOpcode, String description, byte[] suffix) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(opcode);

      for (byte b : suffix) {
        opts.addByte(b);
      }

      opts.addByte(HtmlBytes.FRAGEND);
    });

    final FragmentChildRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(xOpcode);

          for (byte b : suffix) {
            opts.addByte(b);
          }

          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        })
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
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(2);
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.FRAGEND);
    });

    final FragmentChildRecorder subject;
    subject = create(sink);

    final int res;
    res = subject.record(size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = "FRAGEND stops the walk")
  public void testCase05() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(HtmlBytes.FRAGEND);
    });

    final FragmentChildRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(size, 3);

    assertEquals(res, 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.FRAGEND);
        })
    );
  }

  @Test(description = "walk stops at the end of the sink")
  public void testCase06() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(2);
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
    });

    final FragmentChildRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(2);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addInt8(7);
        })
    );
  }

  @DataProvider
  public Object[][] testCase07Provider() {
    return new Object[][] {
        {new byte[] {HtmlBytes.ATTRIBUTE88, 2, 0}, "ATTRIBUTE88"},
        {new byte[] {HtmlBytes.JSACTION88, 2, 0}, "JSACTION88"},
        {new byte[] {HtmlBytes.BOOLEAN8, 2}, "BOOLEAN8"},
        {new byte[] {HtmlBytes.xATTRIBUTE88, 2, 0}, "xATTRIBUTE88"},
        {new byte[] {HtmlBytes.xELEMSTR8, 3, HtmlBytes.VALUES8, 1, 9}, "xELEMSTR8"},
        {new byte[] {HtmlBytes.xELEMVOID8, 4, HtmlBytes.VALUES8, 0}, "xELEMVOID8"},
        {new byte[] {HtmlBytes.xTEXT8, 0x7F}, "xTEXT8"},
        {new byte[] {HtmlBytes.xTEXT16, 0x7F, 0x7F}, "xTEXT16"},
        {new byte[] {HtmlBytes.xRAW8, 0x7F}, "xRAW8"},
        {new byte[] {HtmlBytes.VALUES8, 1}, "VALUES8"},
        {new byte[] {HtmlBytes.DOCTYPE}, "DOCTYPE"},
        {new byte[] {10}, "unmatched"}
    };
  }

  @Test(dataProvider = "testCase07Provider", description = "ignored bytes")
  public void testCase07(byte[] body, String description) {
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

    final FragmentChildRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(size, 3);

    assertEquals(res, 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);

          for (byte b : body) {
            opts.addByte(b);
          }

          opts.addByte(HtmlBytes.FRAGEND);
        })
    );
  }

  @Test(description = "nested fragment (unconsumed)")
  public void testCase08() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
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
    });

    final FragmentChildRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(size, 3);

    assertEquals(res, 3);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = "nested fragment (already consumed)")
  public void testCase09() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
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
    });

    final FragmentChildRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = """
  operand byte equal to the ELEMVOID8 opcode is not scanned as an instruction

  The name operand of the xATTRIBUTE88 instruction at index 7 is 130, the index
  of AttributeName::TRANSLATE. As a byte, 130 equals HtmlBytes::ELEMVOID8.
  """)
  public void testCase10() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
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
    });

    final FragmentChildRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = "fragment root testable node becomes testable-only")
  public void testCase11() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(HtmlBytes.THEADING8);
      opts.addByte((byte) 1);
      opts.addInt8(opts.addObject("Ipsum"));
      opts.addByte(HtmlBytes.FRAGEND);
    });

    final FragmentChildRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.oTHEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("Ipsum"));
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        })
    );
  }

  @Test(description = "fragment root testable new line becomes testable-only")
  public void testCase12() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(HtmlBytes.TNEWLINE);
      opts.addByte(HtmlBytes.FRAGEND);
    });

    final FragmentChildRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xTNEWLINE);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        })
    );
  }

  private FragmentChildRecorder create(HtmlSink sink) {
    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    return new FragmentChildRecorder(sink, valueRecorder);
  }

}
