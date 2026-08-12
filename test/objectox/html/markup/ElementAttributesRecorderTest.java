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
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.AmbiguousName;
import objectox.html.attr.JsActionAttribute;
import objectox.html.elem.ElementName;
import objectox.html.attr.AttributeName;
import objectox.html.attr.AttributeName;
import org.testng.annotations.Test;

public class ElementAttributesRecorderTest {

  @Test(description = "ambiguous name that resolves to an attribute")
  public void ambiguous01() {
    final AttributeName name;
    name = AmbiguousName.TITLE.attribute;

    final String text;
    text = "foo";

    final AmbiguousPojo value;
    value = AmbiguousPojo.of0(AmbiguousName.TITLE, text);

    assertEquals(
        record(ElementName.HTML, value),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(name.index());
          opts.addInt8(opts.addObject(text));
        })
    );
  }

  @Test(description = "ambiguous name that does not resolve to an attribute")
  public void ambiguous02() {
    final AmbiguousPojo value;
    value = AmbiguousPojo.of0(AmbiguousName.TITLE, "foo");

    assertEquals(
        record(ElementName.HEAD, value),

        HtmlSinkY.create(_ -> {})
    );
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
  public void attributeJsAction01() {
    final AttributeName name;
    name = AttributeName.ONCLICK;

    final JsAction action;
    action = Js.submit();

    final JsActionAttribute value;
    value = JsActionAttribute.of0(name, action);

    assertEquals(
        record(value),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(name.index());
          opts.addInt8(opts.addObject(action));
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

  private HtmlSink record(ElementValue... contents) {
    return record(ElementName.HTML, contents);
  }

}
