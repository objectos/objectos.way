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
  public Object[][] testCase01Provider() {
    return new Object[][] {
        {HtmlBytes.AMBIGUOUS88, HtmlBytes.eAMBIGUOUS88, "AMBIGUOUS88"},
        {HtmlBytes.AMBIGUOUS816, HtmlBytes.eAMBIGUOUS816, "AMBIGUOUS816"},
        {HtmlBytes.AMBIGUOUS824, HtmlBytes.eAMBIGUOUS824, "AMBIGUOUS824"}
    };
  }

  @Test(dataProvider = "testCase01Provider", description = "AmbiguousChildRecorder delegation")
  public void testCase01(byte opcode, byte marked, String description) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(opcode);
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
          opts.addByte(marked);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        })
    );
  }

  @DataProvider
  public Object[][] testCase02Provider() {
    return new Object[][] {
        {HtmlBytes.aAMBIGUOUS88, "aAMBIGUOUS88"},
        {HtmlBytes.aAMBIGUOUS816, "aAMBIGUOUS816"},
        {HtmlBytes.aAMBIGUOUS824, "aAMBIGUOUS824"}
    };
  }

  @Test(dataProvider = "testCase02Provider", description = "ambiguous already consumed as attribute")
  public void testCase02(byte opcode, String description) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(opcode);
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
          opts.addByte(opcode);
          opts.addByte(HtmlBytes.FRAGEND);
        })
    );
  }

  @DataProvider
  public Object[][] testCase03Provider() {
    return new Object[][] {
        {HtmlBytes.ELEMSTR8, HtmlBytes.xELEMSTR8, "ELEMSTR8"},
        {HtmlBytes.ELEMVOID8, HtmlBytes.xELEMVOID8, "ELEMVOID8"},
        {HtmlBytes.TEXT8, HtmlBytes.xTEXT8, "TEXT8"},
        {HtmlBytes.TEXT16, HtmlBytes.xTEXT16, "TEXT16"},
        {HtmlBytes.TEXT24, HtmlBytes.xTEXT24, "TEXT24"},
        {HtmlBytes.RAW8, HtmlBytes.xRAW8, "RAW8"},
        {HtmlBytes.RAW16, HtmlBytes.xRAW16, "RAW16"},
        {HtmlBytes.RAW24, HtmlBytes.xRAW24, "RAW24"}
    };
  }

  @Test(dataProvider = "testCase03Provider", description = "ValueRecorder delegation (all widths)")
  public void testCase03(byte opcode, byte xOpcode, String description) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(opcode);
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
        {new byte[] {HtmlBytes.xELEMVOID8, 4}, "xELEMVOID8"},
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
      opts.addInt8(0);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(17);
      opts.addByte(HtmlBytes.TEXT8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.FRAGEND);
      opts.addByte(HtmlBytes.ELEMVOID8);
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
          opts.addInt8(0);
          opts.addByte(HtmlBytes.xFRAGSTR);
          opts.addInt24(17);
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addByte(HtmlBytes.xELEMVOID8);
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
          opts.addInt8(14);
          opts.addInt8(17);
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

  private FragmentChildRecorder create(HtmlSink sink) {
    final AmbiguousChildRecorder ambiguousChildRecorder;
    ambiguousChildRecorder = new AmbiguousChildRecorder(sink);

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    return new FragmentChildRecorder(sink, ambiguousChildRecorder, valueRecorder);
  }

}
