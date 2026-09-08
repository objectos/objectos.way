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

import objectox.html.AmbiguousName;
import objectox.html.IntSize;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FragmentAttributeRecorderTest {

  @DataProvider
  public Object[][] testCase01Provider() {
    return new Object[][] {
        {HtmlBytes.AMBIGUOUS88, HtmlBytes.aAMBIGUOUS88, "AMBIGUOUS88"},
        {HtmlBytes.AMBIGUOUS816, HtmlBytes.aAMBIGUOUS816, "AMBIGUOUS816"},
        {HtmlBytes.AMBIGUOUS824, HtmlBytes.aAMBIGUOUS824, "AMBIGUOUS824"}
    };
  }

  @Test(dataProvider = "testCase01Provider", description = "ambiguous name that resolves to an attribute")
  public void testCase01(byte opcode, byte marked, String description) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(opcode);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addByte(HtmlBytes.FRAGEND);
    });

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(marked);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        })
    );
  }

  @DataProvider
  public Object[][] testCase02Provider() {
    return new Object[][] {
        {HtmlBytes.AMBIGUOUS816, ElementName.HEAD, "not an attribute of the element"}
    };
  }

  @Test(dataProvider = "testCase02Provider", description = "ambiguous name that does not resolve to an attribute")
  public void testCase02(byte opcode, ElementName elementName, String description) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(opcode);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addByte(HtmlBytes.FRAGEND);
    });

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(opcode);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addByte(HtmlBytes.FRAGEND);
        })
    );
  }

  @DataProvider
  public Object[][] testCase03Provider() {
    return new Object[][] {
        {HtmlBytes.ATTRIBUTE88, HtmlBytes.xATTRIBUTE88, "ATTRIBUTE88"},
        {HtmlBytes.ATTRIBUTE816, HtmlBytes.xATTRIBUTE816, "ATTRIBUTE816"},
        {HtmlBytes.ATTRIBUTE824, HtmlBytes.xATTRIBUTE824, "ATTRIBUTE824"},
        {HtmlBytes.JSACTION816, HtmlBytes.xJSACTION816, "JSACTION816"},
        {HtmlBytes.JSACTION824, HtmlBytes.xJSACTION824, "JSACTION824"},
        {HtmlBytes.BOOLEAN8, HtmlBytes.xBOOLEAN8, "BOOLEAN8"}
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

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
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
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(2);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.FRAGEND);
    });

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
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
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(2);
      opts.addInt8(0);
    });

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addInt8(7);
        })
    );
  }

  @DataProvider
  public Object[][] testCase07Provider() {
    return new Object[][] {
        {new byte[] {HtmlBytes.TEXT8, 0x7F}, "TEXT8"},
        {new byte[] {HtmlBytes.TEXT16, 0x7F, 0x7F}, "TEXT16"},
        {new byte[] {HtmlBytes.TEXT24, 0x7F, 0x7F, 0x7F}, "TEXT24"},
        {new byte[] {HtmlBytes.RAW8, 0x7F}, "RAW8"},
        {new byte[] {HtmlBytes.RAW16, 0x7F, 0x7F}, "RAW16"},
        {new byte[] {HtmlBytes.RAW24, 0x7F, 0x7F, 0x7F}, "RAW24"},
        {new byte[] {HtmlBytes.ELEMSTR8, 3, HtmlBytes.VALUES8, 1, 9}, "ELEMSTR8"},
        {new byte[] {HtmlBytes.ELEMVOID8, 4, HtmlBytes.VALUES8, 0}, "ELEMVOID8"},
        {new byte[] {HtmlBytes.xELEMSTR8, 3, HtmlBytes.VALUES8, 1, 9}, "xELEMSTR8"},
        {new byte[] {HtmlBytes.xTEXT8, 0x7F}, "xTEXT8"},
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

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);

          for (byte b : body) {
            opts.addByte(b);
          }

          opts.addByte(HtmlBytes.FRAGEND);
        })
    );
  }

  @Test(description = "JSACTION88 (ValueRecorder delegation)")
  public void testCase08() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(0);
      opts.addByte(HtmlBytes.JSACTION88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addByte(HtmlBytes.FRAGEND);
    });

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.xJSACTION88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.FRAGEND);
          opts.addInt8(7);
        })
    );
  }

  @Test(description = "nested fragment (unconsumed)")
  public void testCase09() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
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
    });

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 3);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = "nested fragment (already consumed)")
  public void testCase10() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
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
    });

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = """
  operand byte equal to the ATTRIBUTE88 opcode is not scanned as an instruction

  The name operand of the ATTRIBUTE88 instruction at index 7 is 131, the index
  of AttributeName::TYPE. As a byte, 131 equals HtmlBytes::ATTRIBUTE88. This
  reproduces TemplateTest::testCase80.
  """)
  public void testCase11() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
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
    });

    final FragmentAttributeRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  private FragmentAttributeRecorder create(HtmlSink sink) {
    final AmbiguousAttributeRecorder ambiguousAttributeRecorder;
    ambiguousAttributeRecorder = new AmbiguousAttributeRecorder(sink);

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    return new FragmentAttributeRecorder(sink, ambiguousAttributeRecorder, valueRecorder);
  }

}
