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
import objectox.html.attr.AttributeNamePojo;
import objectox.html.elem.ElementNamePojo;
import objectox.html.rec.HtmlBytes;
import org.testng.annotations.Test;

public class ScanElementAttributeStateTest {

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

      opts.push(new ScanElementNodeFrame(4, IntSize.BIT8, 0));
      opts.push(new ScanElementAttributeFrame(4, IntSize.BIT8, 0));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanElementNodeState.class);
    assertEquals(ctx.hasEvent(), true);
    assertEquals(ctx.nextEvent(), EndStartTagEvent.INSTANCE);
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void compute02() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeNamePojo.LANG.index());
      opts.addInt8(opts.addObject("pt-BR"));
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());

      opts.push(new ScanElementNodeFrame(7, IntSize.BIT8, 1));
      opts.push(new ScanElementAttributeFrame(7, IntSize.BIT8, 1));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), RenderAttributeState.class);
    assertEquals(ctx.hasEvent(), false);
    assertEquals(ctx.nextInt(IntSize.BIT8), AttributeNamePojo.LANG.index());
  }

  @Test(description = """
  <html autofocus></html>
  """)
  public void compute03() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeNamePojo.AUTOFOCUS.index());
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());

      opts.push(new ScanElementNodeFrame(6, IntSize.BIT8, 1));
      opts.push(new ScanElementAttributeFrame(6, IntSize.BIT8, 1));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), RenderBooleanAttributeState.class);
    assertEquals(ctx.hasEvent(), false);
    assertEquals(ctx.nextInt(IntSize.BIT8), AttributeNamePojo.AUTOFOCUS.index());
  }

  @Test(description = "<html><head></head></html>")
  public void compute04() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xSTARTTAG8);
      opts.addInt8(ElementNamePojo.HEAD.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HEAD.index());
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());

      opts.push(new ScanElementNodeFrame(10, IntSize.BIT8, 1));
      opts.push(new ScanElementAttributeFrame(10, IntSize.BIT8, 1));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanElementAttributeState.class);
    assertEquals(ctx.hasEvent(), false);
  }

}
