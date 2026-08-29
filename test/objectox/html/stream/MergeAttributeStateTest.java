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
package objectox.html.stream;

import static org.testng.Assert.assertEquals;

import objectox.html.IntSize;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.Test;

public class MergeAttributeStateTest {

  @Test(description = """
  reg attr:
  - count: 3
  - object (class)
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute07() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c1"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c3"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c2"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(3);
      opts.addInt8(0);
      opts.addInt8(6);
      opts.addInt8(3);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());

      opts.push(new ScanDocumentNodeFrame(13));
      opts.push(new ScanRegularElementAttributeFrame(16, IntSize.BIT8, 3, 3));
    });

    final MergeAttributeState subject;
    subject = new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.CLASS, "c1 c2 c3");

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanRegularElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(RenderAttributeEvent.of(AttributeName.CLASS, "c1 c2 c3"));
          opts.push(new ScanDocumentNodeFrame(13));
          opts.push(new ScanRegularElementAttributeFrame(16, IntSize.BIT8, 3, 3));
        })
    );
  }

}
