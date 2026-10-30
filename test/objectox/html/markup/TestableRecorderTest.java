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
import java.util.function.Function;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestableRecorderTest {

  @Test(
      description = "rejects a null heading value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void heading01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.heading(1, null);
  }

  @Test(description = """
  a testable heading is recorded with the THEADING8 opcode when the object index fits in 1 byte
  """)
  public void heading02() {
    test(
        _ -> {},

        subject -> subject.heading(1, "Ipsum"),
        0,

        opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("Ipsum"));
        }
    );
  }

  @Test(description = """
  a testable heading is recorded with the THEADING16 opcode when the object index fits in 2 bytes
  """)
  public void heading03() {
    test(
        opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
        },

        subject -> subject.heading(6, "Ipsum"),
        0,

        opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
          opts.addByte(HtmlBytes.THEADING16);
          opts.addByte((byte) 6);
          opts.addInt16(opts.addObject("Ipsum"));
        }
    );
  }

  @Test(description = """
  a testable heading is recorded with the THEADING24 opcode when the object index fits in 3 bytes
  """)
  public void heading04() {
    test(
        opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
        },

        subject -> subject.heading(3, "Ipsum"),
        0,

        opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
          opts.addByte(HtmlBytes.THEADING24);
          opts.addByte((byte) 3);
          opts.addInt24(opts.addObject("Ipsum"));
        }
    );
  }

  @Test(description = "the returned markup pointer is the first available index in the code array")
  public void heading05() {
    test(
        opts -> {
          opts.addInt24(0);
        },

        subject -> subject.heading(1, "Ipsum"),
        3,

        opts -> {
          opts.addInt24(0);
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("Ipsum"));
        }
    );
  }

  @Test(
      description = "rejects a null cell value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void cell01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.cell(1, null);
  }

  @Test(
      description = "rejects a cell width smaller than one",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "width must be > 0")
  public void cell02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.cell(0, "Ipsum");
  }

  @Test(
      description = "rejects a cell value longer than the width",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Formatted value length will exceed the column width of 4")
  public void cell03() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.cell(4, "Ipsum");
  }

  @Test(dataProvider = "cell04Provider", description = """
  a testable cell is recorded with the TCELL8 opcode and the value padded to the specified width
  """)
  public void cell04(String value, int width, String expected) {
    test(
        _ -> {},

        subject -> subject.cell(width, value),
        0,

        opts -> {
          opts.addByte(HtmlBytes.TCELL8);
          opts.addInt8(opts.addObject(expected));
        }
    );
  }

  @DataProvider
  public Object[][] cell04Provider() {
    return new Object[][] {
        {"abcde", 5, "abcde"},
        {"abcd", 5, "abcd "},
        {"abc", 5, "abc  "},
        {"ab", 5, "ab   "},
        {"a", 5, "a    "},
        {"", 5, "     "}
    };
  }

  @Test(description = """
  a testable cell is recorded with the TCELL16 opcode when the object index fits in 2 bytes
  """)
  public void cell05() {
    test(
        opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
        },

        subject -> subject.cell(8, "Ipsum"),
        0,

        opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
          opts.addByte(HtmlBytes.TCELL16);
          opts.addInt16(opts.addObject("Ipsum   "));
        }
    );
  }

  @Test(description = """
  a testable cell is recorded with the TCELL24 opcode when the object index fits in 3 bytes
  """)
  public void cell06() {
    test(
        opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
        },

        subject -> subject.cell(8, "Ipsum"),
        0,

        opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
          opts.addByte(HtmlBytes.TCELL24);
          opts.addInt24(opts.addObject("Ipsum   "));
        }
    );
  }

  @Test(description = "the returned markup pointer is the first available index in the code array")
  public void cell07() {
    test(
        opts -> {
          opts.addInt24(0);
        },

        subject -> subject.cell(8, "Ipsum"),
        3,

        opts -> {
          opts.addInt24(0);
          opts.addByte(HtmlBytes.TCELL8);
          opts.addInt8(opts.addObject("Ipsum   "));
        }
    );
  }

  private void test(
      Consumer<? super HtmlSink> input,
      Function<? super TestableRecorder, ? extends TestableMarkupPointer> action,
      int pointer,
      Consumer<? super HtmlSink> expected
  ) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(input);

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = action.apply(subject);

    assertEquals(res.value(), pointer);

    final HtmlSink out;
    out = HtmlSinkY.create(expected);

    assertEquals(sink, out);
  }

}
