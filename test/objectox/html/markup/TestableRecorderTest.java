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
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT8);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.heading(1, "Ipsum");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = """
  a testable heading is recorded with the THEADING16 opcode when the object index fits in 2 bytes
  """)
  public void heading03() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT16);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.heading(6, "Ipsum");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
          opts.addByte(HtmlBytes.THEADING16);
          opts.addByte((byte) 6);
          opts.addInt16(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = """
  a testable heading is recorded with the THEADING24 opcode when the object index fits in 3 bytes
  """)
  public void heading04() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT24);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.heading(3, "Ipsum");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
          opts.addByte(HtmlBytes.THEADING24);
          opts.addByte((byte) 3);
          opts.addInt24(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = "the returned markup pointer is the first available index in the code array")
  public void heading05() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.heading(1, "Ipsum");

    assertEquals(res.value(), 3);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(0);
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("Ipsum"));
        })
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

  @Test(description = """
  a testable cell is recorded with the TCELL8 opcode when both the width and the object index fit in 1 byte
  """)
  public void cell03() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.cell(3, "Ipsum");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TCELL8);
          opts.addInt8(3);
          opts.addInt8(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = """
  a testable cell is recorded with the TCELL16 opcode when the width does not fit in 1 byte
  """)
  public void cell04() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.cell(256, "Ipsum");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TCELL16);
          opts.addInt16(256);
          opts.addInt16(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = """
  a testable cell is recorded with the TCELL16 opcode when the object index does not fit in 1 byte
  """)
  public void cell05() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT16);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.cell(1, "Ipsum");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
          opts.addByte(HtmlBytes.TCELL16);
          opts.addInt16(1);
          opts.addInt16(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = """
  a testable cell is recorded with the TCELL24 opcode when the width does not fit in 2 bytes
  """)
  public void cell06() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.cell(65536, "Ipsum");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TCELL24);
          opts.addInt24(65536);
          opts.addInt24(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = """
  a testable cell is recorded with the TCELL24 opcode when the object index does not fit in 2 bytes
  """)
  public void cell07() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT24);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.cell(1, "Ipsum");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
          opts.addByte(HtmlBytes.TCELL24);
          opts.addInt24(1);
          opts.addInt24(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = "the returned markup pointer is the first available index in the code array")
  public void cell08() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.cell(3, "Ipsum");

    assertEquals(res.value(), 3);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(0);
          opts.addByte(HtmlBytes.TCELL8);
          opts.addInt8(3);
          opts.addInt8(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(
      description = "rejects a null property key",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "key == null")
  public void property01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.property(null, "bar");
  }

  @Test(
      description = "rejects a null property value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void property02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.property("foo", null);
  }

  @Test(description = """
  a testable property is recorded with the TPROP8 opcode when both object indices fit in 1 byte
  """)
  public void property03() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.property("foo", "bar");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TPROP8);
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(opts.addObject("bar"));
        })
    );
  }

  @Test(description = """
  a testable property is recorded with the TPROP16 opcode when the object indices fit in 2 bytes
  """)
  public void property04() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT16);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.property("foo", "bar");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
          opts.addByte(HtmlBytes.TPROP16);
          opts.addInt16(opts.addObject("foo"));
          opts.addInt16(opts.addObject("bar"));
        })
    );
  }

  @Test(description = """
  a testable property is recorded with the TPROP24 opcode when the object indices fit in 3 bytes
  """)
  public void property05() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT24);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.property("foo", "bar");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
          opts.addByte(HtmlBytes.TPROP24);
          opts.addInt24(opts.addObject("foo"));
          opts.addInt24(opts.addObject("bar"));
        })
    );
  }

  @Test(description = "the returned markup pointer is the first available index in the code array")
  public void property06() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.property("foo", "bar");

    assertEquals(res.value(), 3);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(0);
          opts.addByte(HtmlBytes.TPROP8);
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(opts.addObject("bar"));
        })
    );
  }

  @Test(
      description = "rejects a null property key",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "key == null")
  public void propKey01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.propKey(null);
  }

  @Test(description = """
  a testable property key is recorded with the TPROPKEY8 opcode when the object index fits in 1 byte
  """)
  public void propKey02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.propKey("foo");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TPROPKEY8);
          opts.addInt8(opts.addObject("foo"));
        })
    );
  }

  @Test(description = """
  a testable property key is recorded with the TPROPKEY16 opcode when the object index fits in 2 bytes
  """)
  public void propKey03() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT16);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.propKey("foo");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
          opts.addByte(HtmlBytes.TPROPKEY16);
          opts.addInt16(opts.addObject("foo"));
        })
    );
  }

  @Test(description = """
  a testable property key is recorded with the TPROPKEY24 opcode when the object index fits in 3 bytes
  """)
  public void propKey04() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT24);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.propKey("foo");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
          opts.addByte(HtmlBytes.TPROPKEY24);
          opts.addInt24(opts.addObject("foo"));
        })
    );
  }

  @Test(description = "the returned markup pointer is the first available index in the code array")
  public void propKey05() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.propKey("foo");

    assertEquals(res.value(), 3);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(0);
          opts.addByte(HtmlBytes.TPROPKEY8);
          opts.addInt8(opts.addObject("foo"));
        })
    );
  }

  @Test(
      description = "rejects a null property value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void propValue01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.propValue(null);
  }

  @Test(description = """
  a testable property value is recorded with the TPROPVAL8 opcode when the object index fits in 1 byte
  """)
  public void propValue02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.propValue("bar");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TPROPVAL8);
          opts.addInt8(opts.addObject("bar"));
        })
    );
  }

  @Test(description = """
  a testable property value is recorded with the TPROPVAL16 opcode when the object index fits in 2 bytes
  """)
  public void propValue03() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT16);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.propValue("bar");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
          opts.addByte(HtmlBytes.TPROPVAL16);
          opts.addInt16(opts.addObject("bar"));
        })
    );
  }

  @Test(description = """
  a testable property value is recorded with the TPROPVAL24 opcode when the object index fits in 3 bytes
  """)
  public void propValue04() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT24);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.propValue("bar");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
          opts.addByte(HtmlBytes.TPROPVAL24);
          opts.addInt24(opts.addObject("bar"));
        })
    );
  }

  @Test(description = "the returned markup pointer is the first available index in the code array")
  public void propValue05() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.propValue("bar");

    assertEquals(res.value(), 3);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(0);
          opts.addByte(HtmlBytes.TPROPVAL8);
          opts.addInt8(opts.addObject("bar"));
        })
    );
  }

  @Test(description = """
  a testable newline is recorded with the TNEWLINE opcode
  """)
  public void newLine01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.newLine();

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TNEWLINE);
        })
    );
  }

  @Test(description = "the returned markup pointer is the first available index in the code array")
  public void newLine02() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.newLine();

    assertEquals(res.value(), 3);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(0);
          opts.addByte(HtmlBytes.TNEWLINE);
        })
    );
  }

}
