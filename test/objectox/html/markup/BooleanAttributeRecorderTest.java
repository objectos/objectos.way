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

import objectox.html.attr.AttributeName;
import objectox.html.attr.BooleanAttributePojo;
import org.testng.annotations.Test;

public class BooleanAttributeRecorderTest {

  @Test(
      description = "reject null name",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "Attribute provided a null name")
  public void reject01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final BooleanAttributeRecorder subject;
    subject = new BooleanAttributeRecorder(sink);

    final BooleanAttributePojo attribute;
    attribute = new BooleanAttributePojo(null);

    subject.record(attribute);
  }

  @Test(description = "BOOLEAN8")
  public void boolean8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final BooleanAttributeRecorder subject;
    subject = new BooleanAttributeRecorder(sink);

    final AttributeName name;
    name = AttributeName.AUTOFOCUS;

    final BooleanAttributePojo attribute;
    attribute = new BooleanAttributePojo(name);

    subject.record(attribute);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.BOOLEAN8);
          opts.addInt8(name.index());
        })
    );
  }

}
