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
import static org.testng.Assert.assertSame;

import objectos.html.AttributeObject;
import objectos.html.rec.Instruction;
import objectox.html.attr.AttributeNamePojo;
import org.testng.annotations.Test;

public class ContentsPrecorderTest {

  @Test(description = "returns original array when no AttributeObject components")
  public void precord01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final ContentsPrecorder subject;
    subject = new ContentsPrecorder(attributeRecorder, booleanAttributeRecorder);

    final Instruction[] input;
    input = new Instruction[] {
        new AttributePointer(0),
        new AttributePointer(1),
        new AttributePointer(2)
    };

    final Instruction[] result;
    result = subject.precord(input);

    assertSame(result, input);
  }

  @Test(description = "records AttributeObject with non-null value via AttributeRecorder")
  public void precord02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final ContentsPrecorder subject;
    subject = new ContentsPrecorder(attributeRecorder, booleanAttributeRecorder);

    final Instruction[] input;
    input = new Instruction[] {
        new ElementPointer(10),
        AttributeObject.of(AttributeNamePojo.ID, "foo"),
        new ElementPointer(20)
    };

    final Instruction[] result;
    result = subject.precord(input);

    assertEquals(
        result,

        new Instruction[] {
            new ElementPointer(10),
            new AttributePointer(0),
            new ElementPointer(20)
        }
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeNamePojo.ID.index());
          opts.addInt8(opts.addObject("foo"));
        })
    );
  }

  @Test(description = "records AttributeObject with null value via BooleanAttributeRecorder")
  public void precord03() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final ContentsPrecorder subject;
    subject = new ContentsPrecorder(attributeRecorder, booleanAttributeRecorder);

    final Instruction[] input;
    input = new Instruction[] {
        new ElementPointer(10),
        AttributeObject.of(AttributeNamePojo.AUTOFOCUS),
        new ElementPointer(20)
    };

    final Instruction[] result;
    result = subject.precord(input);

    assertEquals(
        result,

        new Instruction[] {
            new ElementPointer(10),
            new AttributePointer(0),
            new ElementPointer(20)
        }
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.BOOLEAN8);
          opts.addInt8(AttributeNamePojo.AUTOFOCUS.index());
        })
    );
  }

}
