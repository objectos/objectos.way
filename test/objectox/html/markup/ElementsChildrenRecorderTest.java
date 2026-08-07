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

import objectos.html.ElementName;
import objectos.html.markup.ElementValue;
import org.testng.annotations.Test;

public class ElementsChildrenRecorderTest {

  private HtmlSink record(ElementValue... contents) {
    return record(ElementName.HTML, contents);
  }

  private HtmlSink record(ElementName elementName, ElementValue... contents) {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ChildElementRecorder childElementRecorder;
    childElementRecorder = new ChildElementRecorder(sink);

    final ElementsChildrenRecorder subject;
    subject = new ElementsChildrenRecorder(childElementRecorder);

    subject.record(elementName, contents);

    return sink;
  }

  @Test
  public void element01() {
    final ElementPointer child;
    child = new ElementPointer(32);

    assertEquals(
        record(child),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ELEM8);
          opts.addInt8(32);
        })
    );
  }

}
