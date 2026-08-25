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
import objectox.html.elem.ElementNamePojo;
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.Test;

public class RenderBooleanAttributeStateTest {

  @Test(description = """
  <html autofocus></html>
  """)
  public void compute03() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeName.AUTOFOCUS.index());
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());

      opts.push(new ScanElementNodeFrame(6, IntSize.BIT8, 1));
      opts.push(new ScanElementAttributeFrame(7, IntSize.BIT8, 1));
      opts.push(new JumpFrame(1));
    });

    final RenderBooleanAttributeState subject;
    subject = new RenderBooleanAttributeState(ctx, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanRegularElementAttributeState.class);
    assertEquals(ctx.hasEvent(), true);
    assertEquals(ctx.nextEvent(), RenderBooleanAttributeEvent.of(AttributeName.AUTOFOCUS));
  }

}
