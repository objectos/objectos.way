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
import objectos.html.ClassName;
import objectos.html.Component;
import objectos.html.Id;
import objectos.html.Markup;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ElementChildrenRecorderTest {

  @DataProvider
  public Object[][] testCase01Provider() {
    final Component component;
    component = _ -> {};

    return new Object[][] {
        {new AttributePointer(3)},
        {Markup.checked},
        {ClassName.of("block")},
        {component},
        {Id.of("foo")},
        {NoOpMarkupPojo.INSTANCE}
    };
  }

  @Test(dataProvider = "testCase01Provider", description = "not a child; sink unchanged")
  public void testCase01(ElementValue value) {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
        },

        IntSize.BIT8, value,
        0,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
        }
    );
  }

  @Test(description = "ValueRecorder delegation (ElementPointer)")
  public void testCase02() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.HEAD.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt24(0);
        },

        IntSize.BIT8, new ElementPointer(3),
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(ElementName.HEAD.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt24(0);
          opts.addInt8(3);
        }
    );
  }

  @Test(description = "ValueRecorder delegation (RawPointer)")
  public void testCase03() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.RAW8);
          opts.addInt8(opts.addObject("ipsum"));
        },

        IntSize.BIT8, new RawPointer(3),
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xRAW8);
          opts.addInt8(opts.addObject("ipsum"));
          opts.addInt8(3);
        }
    );
  }

  @Test(description = "ValueRecorder delegation (TestableMarkupPointer)")
  public void testCase04() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("ipsum"));
        },

        IntSize.BIT8, new TestableMarkupPointer(3),
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xTHEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("ipsum"));
          opts.addInt8(3);
        }
    );
  }

  @Test(description = "ValueRecorder delegation (TextPointer)")
  public void testCase05() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("ipsum"));
        },

        IntSize.BIT8, new TextPointer(3),
        1,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(opts.addObject("ipsum"));
          opts.addInt8(3);
        }
    );
  }

  @Test(description = "FragmentChildRecorder delegation (FragmentPointer)")
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
          opts.addByte(HtmlBytes.FRAGEND);
        },

        IntSize.BIT8, new FragmentPointer(3),
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
          opts.addInt8(7);
        }
    );
  }

  @Test(description = "returns the sum of the processing of each input value")
  public void testCase07() {
    final ElementValue[] values;
    values = new ElementValue[] {
        NoOpMarkupPojo.INSTANCE,
        new TextPointer(3),
        new RawPointer(5),
        new ElementPointer(7)
    };

    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("ipsum"));
          opts.addByte(HtmlBytes.RAW8);
          opts.addInt8(opts.addObject("more"));
          opts.addByte(HtmlBytes.ELEMSTR8);
        },

        IntSize.BIT8, values,
        3,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(opts.addObject("ipsum"));
          opts.addByte(HtmlBytes.xRAW8);
          opts.addInt8(opts.addObject("more"));
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(3);
          opts.addInt8(5);
          opts.addInt8(7);
        }
    );
  }

  private void test(
      Consumer<? super HtmlSink> input,
      IntSize size, ElementValue value,
      int sum,
      Consumer<? super HtmlSink> expected
  ) {
    test(input, size, new ElementValue[] {value}, sum, expected);
  }

  private void test(
      Consumer<? super HtmlSink> input,
      IntSize size, ElementValue[] values,
      int sum,
      Consumer<? super HtmlSink> expected
  ) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(input);

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentChildRecorder fragmentChildRecorder;
    fragmentChildRecorder = new FragmentChildRecorder(sink, valueRecorder);

    final ElementChildrenRecorder subject;
    subject = new ElementChildrenRecorder(fragmentChildRecorder, valueRecorder);

    final int res;
    res = subject.record(size, values);

    assertEquals(res, sum);

    final HtmlSink out;
    out = HtmlSinkY.create(expected);

    assertEquals(sink, out);
  }

}
