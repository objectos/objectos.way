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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;

import java.util.stream.IntStream;
import objectos.internal.Util;
import objectox.html.attr.AttributeNamePojo;
import org.testng.annotations.Test;

public class AttributeRecorderTest {

  @Test(
      description = "reject null value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void record01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AttributeRecorder subject;
    subject = new AttributeRecorder(sink);

    final AttributeNamePojo name;
    name = AttributeNamePojo.ID;

    final String value;
    value = null;

    subject.record(name, value);
  }

  @Test(description = "ATTRIBUTE88")
  public void record02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AttributeRecorder subject;
    subject = new AttributeRecorder(sink);

    final AttributeNamePojo name;
    name = AttributeNamePojo.ID;

    final String value;
    value = "foo";

    final AttributeInstruction res;
    res = subject.record(name, value);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(name.index());
          opts.addInt8(opts.addObject(value));
        })
    );
  }

  @Test(description = "ATTRIBUTE816")
  public void record03() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[256]);

    final AttributeRecorder subject;
    subject = new AttributeRecorder(sink);

    final AttributeNamePojo name;
    name = AttributeNamePojo.ID;

    final String value;
    value = "foo";

    final AttributeInstruction res;
    res = subject.record(name, value);

    assertEquals(res.value(), 0);

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
  public void record04() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[1 << 16]);

    final AttributeRecorder subject;
    subject = new AttributeRecorder(sink);

    final AttributeNamePojo name;
    name = AttributeNamePojo.ID;

    final String value;
    value = "foo";

    final AttributeInstruction res;
    res = subject.record(name, value);

    assertEquals(res.value(), 0);

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

}
