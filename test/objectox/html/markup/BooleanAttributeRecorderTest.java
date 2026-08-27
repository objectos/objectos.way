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
import static org.testng.Assert.assertThrows;

import objectos.html.Markup;
import objectos.html.markup.BooleanAttribute;
import objectox.html.attr.AttributeName;
import org.testng.annotations.Test;

public class BooleanAttributeRecorderTest {

  @Test(description = "BOOLEAN8 (markup)")
  public void boolean8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final BooleanAttributeRecorder subject;
    subject = new BooleanAttributeRecorder(sink);

    final AttributeName name;
    name = new AttributeName(HtmlSink.MAX_INT8, "foo");

    final AttributePointer res;
    res = subject.record(name);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.BOOLEAN8);
          opts.addInt8(name.index());
        })
    );
  }

  @Test(description = "BOOLEAN8 (object)")
  public void boolean8_object() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final BooleanAttributeRecorder subject;
    subject = new BooleanAttributeRecorder(sink);

    final BooleanAttribute attribute;
    attribute = Markup.autofocus;

    final AttributePointer res;
    res = subject.record(attribute);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.BOOLEAN8);
          opts.addInt8(AttributeName.AUTOFOCUS.index());
        })
    );
  }

  @Test(description = "index too large")
  public void booleanInvalid() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final BooleanAttributeRecorder subject;
    subject = new BooleanAttributeRecorder(sink);

    final AttributeName name;
    name = new AttributeName(HtmlSink.MAX_INT8 + 1, "foo");

    assertThrows(IllegalArgumentException.class, () -> subject.record(name));
  }

  @Test(description = "returned markup")
  public void markup01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final BooleanAttributeRecorder subject;
    subject = new BooleanAttributeRecorder(sink);

    final AttributeName name;
    name = new AttributeName(HtmlSink.MAX_INT8, "foo");

    final AttributePointer res;
    res = subject.record(name);

    assertEquals(res.value(), 3);
  }

}
