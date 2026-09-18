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

import objectos.html.ClassName;
import objectos.html.Id;
import objectos.html.Markup;
import objectos.html.markup.BooleanAttribute;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;
import objectox.html.attr.AttributeName;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ElementAttributesRecorderTest {

  @Test(description = "ValueRecorder delegation")
  public void attribute01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addInt8(99);
    });

    final ElementAttributesRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final AttributePointer pointer;
    pointer = new AttributePointer(0);

    final int res;
    res = subject.record(elementName, size, pointer);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(99);
          opts.addInt8(0);
        })
    );
  }

  @Test(
      description = "Object attribute detected",
      expectedExceptions = ObjectAttributeDetected.class
  )
  public void attribute02() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(_ -> {});

    final ElementAttributesRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final ClassName attribute;
    attribute = ClassName.of("block");

    subject.record(elementName, size, attribute);
  }

  @Test(
      description = "Object attribute detected",
      expectedExceptions = ObjectAttributeDetected.class
  )
  public void attribute03() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(_ -> {});

    final ElementAttributesRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final Id attribute;
    attribute = Id.of("foo");

    subject.record(elementName, size, attribute);
  }

  @Test(description = "ValueRecorder delegation for an encoded boolean attribute")
  public void booleanAttribute01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addByte(HtmlBytes.BOOLEAN8);
      opts.addInt8(AttributeName.CHECKED.index());
    });

    final ElementAttributesRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final AttributePointer pointer;
    pointer = new AttributePointer(0);

    final int res;
    res = subject.record(elementName, size, pointer);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xBOOLEAN8);
          opts.addInt8(AttributeName.CHECKED.index());
          opts.addInt8(0);
        })
    );
  }

  @Test(
      description = "Object boolean attribute detected",
      expectedExceptions = ObjectAttributeDetected.class
  )
  public void booleanAttribute02() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(_ -> {});

    final ElementAttributesRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final BooleanAttribute attribute;
    attribute = Markup.autofocus;

    subject.record(elementName, size, attribute);
  }

  @DataProvider
  public Object[][] ignoredProvider() {
    return new Object[][] {
        {new ElementPointer(0)},
        {NoOpMarkupPojo.INSTANCE},
        {new RawPointer(0)},
        {new TextPointer(0)}
    };
  }

  @Test(dataProvider = "ignoredProvider", description = "values that produce no sink changes")
  public void ignored(ElementValue value) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(_ -> {});

    final ElementAttributesRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, value);

    assertEquals(res, 0);

    assertEquals(sink, HtmlSinkY.create(_ -> {}));
  }

  private ElementAttributesRecorder create(HtmlSink sink) {
    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentAttributeRecorder fragmentAttributeRecorder;
    fragmentAttributeRecorder = new FragmentAttributeRecorder(sink, valueRecorder);

    return new ElementAttributesRecorder(fragmentAttributeRecorder, valueRecorder);
  }

}
