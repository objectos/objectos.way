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

import objectos.html.Attribute;
import objectos.html.BooleanAttribute;
import objectox.html.attr.AttributeName;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ElementAttributesPreRecorderTest {

  @Test(description = "AttributeRecorder delegation")
  public void attribute01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final ElementAttributesPreRecorder subject;
    subject = new ElementAttributesPreRecorder(attributeRecorder, booleanAttributeRecorder);

    final Attribute title;
    title = Attribute.of("title", "Hello");

    final ElementValue[] values;
    values = new ElementValue[] { title };

    subject.record(values);

    assertEquals(values[0], new AttributePointer(0));

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.TITLE.index());
          opts.addInt8(opts.addObject("Hello"));
        })
    );
  }

  @Test(description = "BooleanAttributeRecorder delegation")
  public void booleanAttribute01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final ElementAttributesPreRecorder subject;
    subject = new ElementAttributesPreRecorder(attributeRecorder, booleanAttributeRecorder);

    final BooleanAttribute checked;
    checked = BooleanAttribute.of("checked");

    final ElementValue[] values;
    values = new ElementValue[] { checked };

    subject.record(values);

    assertEquals(values[0], new AttributePointer(0));

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.BOOLEAN8);
          opts.addInt8(AttributeName.CHECKED.index());
        })
    );
  }

  @DataProvider
  public Object[][] elementValueProvider() {
    return new Object[][] {
        {new AmbiguousPointer(0)},
        {new AttributePointer(0)},
        {new ElementPointer(0)},
        {new FragmentPointer(0)},
        {NoOpMarkupPojo.INSTANCE},
        {new RawPointer(0)},
        {new TextPointer(0)}
    };
  }

  @Test(dataProvider = "elementValueProvider", description = "other values leave the sink unchanged")
  public void ignored(ElementValue value) {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final ElementAttributesPreRecorder subject;
    subject = new ElementAttributesPreRecorder(attributeRecorder, booleanAttributeRecorder);

    final ElementValue[] values;
    values = new ElementValue[] { value };

    subject.record(values);

    assertEquals(values[0], value);
    assertEquals(sink, HtmlSinkY.create(_ -> {}));
  }

}
