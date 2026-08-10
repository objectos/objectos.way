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

import java.util.stream.IntStream;
import objectos.html.AttributeName;
import objectos.internal.Util;
import org.testng.annotations.Test;

public class AttributeRecorderTest {

  private HtmlSink record(AttributeName name, String value) {
    final HtmlSink sink;
    sink = new HtmlSink();

    record(sink, name, value);

    return sink;
  }

  private void record(HtmlSink sink, AttributeName name, String value) {
    final AttributeRecorder subject;
    subject = new AttributeRecorder(sink);

    subject.record(name, value);
  }

  @Test(
      description = "reject null name",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "Attribute provided a null name")
  public void reject01() {
    final AttributeNamePojo name;
    name = null;

    final String value;
    value = "foo";

    record(name, value);
  }

  @Test(
      description = "reject null value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "Attribute provided a null value")
  public void reject02() {
    final AttributeNamePojo name;
    name = AttributeNamePojo.ID;

    final String value;
    value = null;

    record(name, value);
  }

  @Test(description = "ATTRIBUTE88")
  public void attribute88() {
    final AttributeNamePojo name;
    name = AttributeNamePojo.ID;

    final String value;
    value = "foo";

    assertEquals(
        record(name, value),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(name.index());
          opts.addInt8(opts.addObject(value));
        })
    );
  }

  @Test(description = "ATTRIBUTE816")
  public void attribute816() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[256]);

    final AttributeNamePojo name;
    name = AttributeNamePojo.ID;

    final String value;
    value = "foo";

    record(sink, name, value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 256).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.ATTRIBUTE816);
          opts.addInt8(name.index());
          opts.addInt16(opts.addObject("foo"));
        })
    );
  }

  @Test(description = "ATTRIBUTE824")
  public void attribute824() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[1 << 16]);

    final AttributeNamePojo name;
    name = AttributeNamePojo.ID;

    final String value;
    value = "foo";

    record(sink, name, value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 1 << 16).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.ATTRIBUTE824);
          opts.addInt8(name.index());
          opts.addInt24(opts.addObject("foo"));
        })
    );
  }

  @Test(description = "value should be escaped")
  public void valueEscaper() {
    final AttributeNamePojo name;
    name = AttributeNamePojo.ID;

    final String value;
    value = "A&B";

    final String escaped;
    escaped = "A&amp;B";

    assertEquals(
        record(name, value),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(name.index());
          opts.addInt8(opts.addObject(escaped));
        })
    );
  }

}
