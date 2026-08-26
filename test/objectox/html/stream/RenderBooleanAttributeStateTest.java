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

public class RenderBooleanAttributeStateTest {

  @Test(description = """
  regular elem:
  - 1 boolean attr (object) (8bit)
  - no children
  """)
  public void regularElement03() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeName.AUTOFOCUS.index());
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());

      opts.push(new ScanDocumentNodeFrame(6));
      opts.push(new ScanRegularElementAttributeFrame(7, IntSize.BIT8, 1, 1));
      opts.push(new JumpFrame(1));
    });

    final RenderBooleanAttributeState subject;
    subject = new RenderBooleanAttributeState(ctx, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanRegularElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(6));
          opts.push(new ScanRegularElementAttributeFrame(7, IntSize.BIT8, 1, 1));
          opts.result(RenderBooleanAttributeEvent.of(AttributeName.AUTOFOCUS));
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 boolean attr (object) (8bit)
  """)
  public void voidElement03() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeName.CHECKED.index());
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.push(new ScanDocumentNodeFrame(6));
      opts.push(new ScanVoidElementAttributeFrame(7, IntSize.BIT8, 1, 1));
      opts.push(new JumpFrame(1));
    });

    final RenderBooleanAttributeState subject;
    subject = new RenderBooleanAttributeState(ctx, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanVoidElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new ScanDocumentNodeFrame(6));
          opts.push(new ScanVoidElementAttributeFrame(7, IntSize.BIT8, 1, 1));
          opts.result(RenderBooleanAttributeEvent.of(AttributeName.CHECKED));
        })
    );
  }

}
