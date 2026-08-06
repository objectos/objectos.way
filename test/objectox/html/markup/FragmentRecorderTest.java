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

import objectos.html.markup.Fragment0;
import objectos.html.markup.Fragment1;
import objectos.html.markup.Fragment2;
import objectos.html.markup.Fragment3;
import objectos.html.markup.Fragment4;
import org.testng.annotations.Test;

public class FragmentRecorderTest {

  @Test
  public void record01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final FragmentRecorder subject;
    subject = new FragmentRecorder(sink);

    final Fragment0 f;
    f = () -> {

    };

    final FragmentPointer res;
    res = subject.record(f);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);
          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

  @Test
  public void record02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final FragmentRecorder subject;
    subject = new FragmentRecorder(sink);

    final Fragment0 f;
    f = () -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject("foo"));
    };

    final FragmentPointer res;
    res = subject.record(f);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

  @Test
  public void record03() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final FragmentRecorder subject;
    subject = new FragmentRecorder(sink);

    final Fragment1<String> f;
    f = value -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value));
    };

    final FragmentPointer res;
    res = subject.record(f, "foo");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

  @Test
  public void record04() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final FragmentRecorder subject;
    subject = new FragmentRecorder(sink);

    final Fragment2<String, String> f;
    f = (value1, value2) -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value1));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value2));
    };

    final FragmentPointer res;
    res = subject.record(f, "foo", "bar");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("bar"));

          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

  @Test
  public void record05() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final FragmentRecorder subject;
    subject = new FragmentRecorder(sink);

    final Fragment3<String, String, String> f;
    f = (value1, value2, value3) -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value1));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value2));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value3));
    };

    final FragmentPointer res;
    res = subject.record(f, "foo", "bar", "baz");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("bar"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("baz"));

          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

  @Test
  public void record06() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final FragmentRecorder subject;
    subject = new FragmentRecorder(sink);

    final Fragment4<String, String, String, String> f;
    f = (value1, value2, value3, value4) -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value1));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value2));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value3));

      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeNamePojo.ID.index());
      sink.addInt8(sink.addObject(value4));
    };

    final FragmentPointer res;
    res = subject.record(f, "foo", "bar", "baz", "qux");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFRAG);

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("bar"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("baz"));

          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("qux"));

          opts.addByte(HtmlBytes.ENDFRAG);
        })
    );
  }

}
