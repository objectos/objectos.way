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

import objectox.html.elem.ElementNamePojo;
import objectox.html.markup.AttributeNamePojo;
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.Test;

public class ScanDocumentNodeStateTest {

  @Test(description = "empty")
  public void compute00() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(ScanDocumentNodeFrame.of(0));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), StopState.class);
    assertEquals(ctx.hasEvent(), false);
  }

  @Test(description = "<html></html>")
  public void compute01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());

      opts.push(ScanDocumentNodeFrame.of(0));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), BeginStartTagState.class);
    assertEquals(ctx.hasEvent(), false);
  }

  @Test(description = """
  <!DOCTYPE html>
  <html></html>
  """)
  public void compute07() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.DOCTYPE);
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());

      opts.push(ScanDocumentNodeFrame.of(0));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanDocumentNodeState.class);
    assertEquals(ctx.hasEvent(), true);
    assertEquals(ctx.nextEvent(), RenderDoctypeEvent.of());
  }

  @Test(description = """
  <div class="c1 c2"></html>
  """)
  public void compute09() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeNamePojo.CLASS.index());
      opts.addInt8(opts.addObject("c1"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeNamePojo.CLASS.index());
      opts.addInt8(opts.addObject("c2"));
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(2);
      opts.addInt8(0);
      opts.addInt8(3);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.DIV.index());

      opts.push(ScanDocumentNodeFrame.of(6));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), BeginStartTagState.class);
    assertEquals(ctx.hasEvent(), false);
  }

}
