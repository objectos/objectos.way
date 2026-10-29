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
import static org.testng.Assert.fail;

import objectox.html.IntSize;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ValueRecorderTest {

  @DataProvider
  public Object[][] recordValidProvider() {
    return new Object[][] {
        {HtmlBytes.ELEMSTR8, HtmlBytes.xELEMSTR8},
        {HtmlBytes.ELEMVOID8, HtmlBytes.xELEMVOID8},

        {HtmlBytes.ATTRIBUTE88, HtmlBytes.xATTRIBUTE88},
        {HtmlBytes.ATTRIBUTE816, HtmlBytes.xATTRIBUTE816},
        {HtmlBytes.ATTRIBUTE824, HtmlBytes.xATTRIBUTE824},
        {HtmlBytes.BOOLEAN8, HtmlBytes.xBOOLEAN8},

        {HtmlBytes.JSACTION88, HtmlBytes.xJSACTION88},
        {HtmlBytes.JSACTION816, HtmlBytes.xJSACTION816},
        {HtmlBytes.JSACTION824, HtmlBytes.xJSACTION824},

        {HtmlBytes.TEXT8, HtmlBytes.xTEXT8},
        {HtmlBytes.TEXT16, HtmlBytes.xTEXT16},
        {HtmlBytes.TEXT24, HtmlBytes.xTEXT24},

        {HtmlBytes.RAW8, HtmlBytes.xRAW8},
        {HtmlBytes.RAW16, HtmlBytes.xRAW16},
        {HtmlBytes.RAW24, HtmlBytes.xRAW24},

        {HtmlBytes.THEADING8, HtmlBytes.xTHEADING8},
        {HtmlBytes.THEADING16, HtmlBytes.xTHEADING16},
        {HtmlBytes.THEADING24, HtmlBytes.xTHEADING24},
        {HtmlBytes.TCELL8, HtmlBytes.xTCELL8},
        {HtmlBytes.TCELL16, HtmlBytes.xTCELL16},
        {HtmlBytes.TCELL24, HtmlBytes.xTCELL24},

        {HtmlBytes.xELEMSTR8, HtmlBytes.xELEMSTR8},
        {HtmlBytes.xELEMVOID8, HtmlBytes.xELEMVOID8},

        {HtmlBytes.xATTRIBUTE88, HtmlBytes.xATTRIBUTE88},
        {HtmlBytes.xATTRIBUTE816, HtmlBytes.xATTRIBUTE816},
        {HtmlBytes.xATTRIBUTE824, HtmlBytes.xATTRIBUTE824},
        {HtmlBytes.xBOOLEAN8, HtmlBytes.xBOOLEAN8},

        {HtmlBytes.xJSACTION88, HtmlBytes.xJSACTION88},
        {HtmlBytes.xJSACTION816, HtmlBytes.xJSACTION816},
        {HtmlBytes.xJSACTION824, HtmlBytes.xJSACTION824},

        {HtmlBytes.xTEXT8, HtmlBytes.xTEXT8},
        {HtmlBytes.xTEXT16, HtmlBytes.xTEXT16},
        {HtmlBytes.xTEXT24, HtmlBytes.xTEXT24},

        {HtmlBytes.xRAW8, HtmlBytes.xRAW8},
        {HtmlBytes.xRAW16, HtmlBytes.xRAW16},
        {HtmlBytes.xRAW24, HtmlBytes.xRAW24},

        {HtmlBytes.xTHEADING8, HtmlBytes.xTHEADING8},
        {HtmlBytes.xTHEADING16, HtmlBytes.xTHEADING16},
        {HtmlBytes.xTHEADING24, HtmlBytes.xTHEADING24},
        {HtmlBytes.xTCELL8, HtmlBytes.xTCELL8},
        {HtmlBytes.xTCELL16, HtmlBytes.xTCELL16},
        {HtmlBytes.xTCELL24, HtmlBytes.xTCELL24},

        {HtmlBytes.oTHEADING8, HtmlBytes.oTHEADING8},
        {HtmlBytes.oTHEADING16, HtmlBytes.oTHEADING16},
        {HtmlBytes.oTHEADING24, HtmlBytes.oTHEADING24},
        {HtmlBytes.oTCELL8, HtmlBytes.oTCELL8},
        {HtmlBytes.oTCELL16, HtmlBytes.oTCELL16},
        {HtmlBytes.oTCELL24, HtmlBytes.oTCELL24}
    };
  }

  @Test(dataProvider = "recordValidProvider")
  public void record01(byte opcode, byte xOpcode) {
    final HtmlSink in;
    in = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(opcode);
    });

    final HtmlSink out;
    out = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(xOpcode);
      opts.addInt8(3);
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(in);

    final int res;
    res = subject.record(IntSize.BIT8, 3);

    assertEquals(res, 1);

    assertEquals(in, out);
  }

  @Test(dataProvider = "recordValidProvider")
  public void record02(byte opcode, byte xOpcode) {
    final HtmlSink in;
    in = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(opcode);
    });

    final HtmlSink out;
    out = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(xOpcode);
      opts.addInt16(3);
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(in);

    final int res;
    res = subject.record(IntSize.BIT16, 3);

    assertEquals(res, 1);

    assertEquals(in, out);
  }

  @Test(dataProvider = "recordValidProvider")
  public void record03(byte opcode, byte xOpcode) {
    final HtmlSink in;
    in = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(opcode);
    });

    final HtmlSink out;
    out = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(xOpcode);
      opts.addInt24(3);
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(in);

    final int res;
    res = subject.record(IntSize.BIT24, 3);

    assertEquals(res, 1);

    assertEquals(in, out);
  }

  @DataProvider
  public Object[][] recordInvalidProvider() {
    return new Object[][] {
        {HtmlBytes.DOCTYPE},

        {HtmlBytes.FRAGSTR},
        {HtmlBytes.FRAGEND},

        {HtmlBytes.VALUES8},
        {HtmlBytes.VALUES16},
        {HtmlBytes.VALUES24},

        {HtmlBytes.xFRAGSTR}
    };
  }

  @Test(dataProvider = "recordInvalidProvider")
  public void recordInvalid(byte opcode) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(opcode);
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(sink);

    try {
      subject.record(IntSize.BIT8, 3);

      fail();
    } catch (IllegalArgumentException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Invalid opcode: " + opcode);
    }
  }

  @DataProvider
  public Object[][] testableOnlyValidProvider() {
    return new Object[][] {
        {HtmlBytes.THEADING8, HtmlBytes.oTHEADING8},
        {HtmlBytes.THEADING16, HtmlBytes.oTHEADING16},
        {HtmlBytes.THEADING24, HtmlBytes.oTHEADING24},
        {HtmlBytes.TCELL8, HtmlBytes.oTCELL8},
        {HtmlBytes.TCELL16, HtmlBytes.oTCELL16},
        {HtmlBytes.TCELL24, HtmlBytes.oTCELL24}
    };
  }

  @Test(dataProvider = "testableOnlyValidProvider")
  public void testableOnly01(byte opcode, byte oOpcode) {
    final HtmlSink in;
    in = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(opcode);
    });

    final HtmlSink out;
    out = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(oOpcode);
      opts.addInt8(3);
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(in);

    final int res;
    res = subject.testableOnly(IntSize.BIT8, 3);

    assertEquals(res, 1);

    assertEquals(in, out);
  }

  @Test(dataProvider = "testableOnlyValidProvider")
  public void testableOnly02(byte opcode, byte oOpcode) {
    final HtmlSink in;
    in = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(opcode);
    });

    final HtmlSink out;
    out = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(oOpcode);
      opts.addInt16(3);
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(in);

    final int res;
    res = subject.testableOnly(IntSize.BIT16, 3);

    assertEquals(res, 1);

    assertEquals(in, out);
  }

  @Test(dataProvider = "testableOnlyValidProvider")
  public void testableOnly03(byte opcode, byte oOpcode) {
    final HtmlSink in;
    in = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(opcode);
    });

    final HtmlSink out;
    out = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(oOpcode);
      opts.addInt24(3);
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(in);

    final int res;
    res = subject.testableOnly(IntSize.BIT24, 3);

    assertEquals(res, 1);

    assertEquals(in, out);
  }

  @DataProvider
  public Object[][] testableOnlyInvalidProvider() {
    return new Object[][] {
        {HtmlBytes.DOCTYPE},

        {HtmlBytes.ELEMSTR8},
        {HtmlBytes.ELEMVOID8},

        {HtmlBytes.ATTRIBUTE88},
        {HtmlBytes.ATTRIBUTE816},
        {HtmlBytes.ATTRIBUTE824},
        {HtmlBytes.BOOLEAN8},
        {HtmlBytes.JSACTION88},
        {HtmlBytes.JSACTION816},
        {HtmlBytes.JSACTION824},

        {HtmlBytes.FRAGSTR},
        {HtmlBytes.FRAGEND},

        {HtmlBytes.TEXT8},
        {HtmlBytes.TEXT16},
        {HtmlBytes.TEXT24},

        {HtmlBytes.RAW8},
        {HtmlBytes.RAW16},
        {HtmlBytes.RAW24},

        {HtmlBytes.VALUES8},
        {HtmlBytes.VALUES16},
        {HtmlBytes.VALUES24},

        {HtmlBytes.xELEMSTR8},
        {HtmlBytes.xELEMVOID8},

        {HtmlBytes.xATTRIBUTE88},
        {HtmlBytes.xATTRIBUTE816},
        {HtmlBytes.xATTRIBUTE824},
        {HtmlBytes.xBOOLEAN8},
        {HtmlBytes.xJSACTION88},
        {HtmlBytes.xJSACTION816},
        {HtmlBytes.xJSACTION824},

        {HtmlBytes.xFRAGSTR},

        {HtmlBytes.xTHEADING8},
        {HtmlBytes.xTHEADING16},
        {HtmlBytes.xTHEADING24},
        {HtmlBytes.xTCELL8},
        {HtmlBytes.xTCELL16},
        {HtmlBytes.xTCELL24},

        {HtmlBytes.oTHEADING8},
        {HtmlBytes.oTHEADING16},
        {HtmlBytes.oTHEADING24},
        {HtmlBytes.oTCELL8},
        {HtmlBytes.oTCELL16},
        {HtmlBytes.oTCELL24},

        {HtmlBytes.xTEXT8},
        {HtmlBytes.xTEXT16},
        {HtmlBytes.xTEXT24},

        {HtmlBytes.xRAW8},
        {HtmlBytes.xRAW16},
        {HtmlBytes.xRAW24}
    };
  }

  @Test(dataProvider = "testableOnlyInvalidProvider")
  public void testableOnlyInvalid(byte opcode) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(opcode);
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(sink);

    try {
      subject.testableOnly(IntSize.BIT8, 3);

      fail();
    } catch (IllegalArgumentException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Invalid opcode: " + opcode);
    }
  }

}
