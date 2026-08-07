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
import objectos.html.AttributeName;
import objectos.html.BooleanAttribute;
import objectos.html.ElementName;
import objectos.html.markup.ElementValue;
import org.testng.annotations.Test;

public class ElementAttributesRecorderTest {

  private HtmlSink record(ElementValue... contents) {
    return record(ElementName.HTML, contents);
  }

  private HtmlSink record(ElementName elementName, ElementValue... contents) {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final ElementAttributesRecorder subject;
    subject = new ElementAttributesRecorder(attributeRecorder, booleanAttributeRecorder);

    subject.record(elementName, contents);

    return sink;
  }

  @Test
  public void attribute01() {
    final AttributeName name;
    name = AttributeName.ID;

    final String value;
    value = "foo";

    final Attribute attr;
    attr = Attribute.of(name, value);

    assertEquals(
        record(attr),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(name.index());
          opts.addInt8(opts.addObject(value));
        })
    );
  }

  @Test
  public void booleanAttribute01() {
    final AttributeName name;
    name = AttributeName.AUTOFOCUS;

    final BooleanAttribute attr;
    attr = BooleanAttribute.of(name);

    assertEquals(
        record(attr),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.BOOLEAN8);
          opts.addInt8(name.index());
        })
    );
  }

}
