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

import objectos.html.stream.EndStartTagKind;
import objectox.html.IntSize;
import objectox.html.elem.ElementNamePojo;
import objectox.html.markup.AttributeNamePojo;
import objectox.html.markup.HtmlBytes;
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
    assertEquals(ctx.nextEvent(), EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
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
  <html lang="pt-BR"></html>
  """)
  public void compute02b() {
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
      opts.push(new ScanElementAttributeFrame(8, IntSize.BIT8, 1, 1));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanElementNodeState.class);
    assertEquals(ctx.hasEvent(), true);
    assertEquals(ctx.nextEvent(), EndStartTagEvent.of(EndStartTagKind.EMPTY_ELEMENT));
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

  @Test(description = "<html><head></head></html>")
  public void compute04b() {
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

      final ScanElementAttributeFrame f;
      f = new ScanElementAttributeFrame(11, IntSize.BIT8, 1, 1);

      f.childElement();

      opts.push(f);
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanElementNodeState.class);
    assertEquals(ctx.hasEvent(), true);
    assertEquals(ctx.nextEvent(), EndStartTagEvent.of(EndStartTagKind.ELEMENT));
  }

  @Test(description = """
  <head><meta charset="UTF-8"></head>
  """)
  public void compute05() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeNamePojo.CHARSET.index());
      opts.addInt8(opts.addObject("UTF-8"));
      opts.addByte(HtmlBytes.xSTARTTAG8);
      opts.addInt8(ElementNamePojo.META.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.VOIDELEM);
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HEAD.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(3);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HEAD.index());

      opts.push(new ScanElementNodeFrame(13, IntSize.BIT8, 1));
      opts.push(new ScanElementAttributeFrame(13, IntSize.BIT8, 1));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanElementAttributeState.class);
    assertEquals(ctx.hasEvent(), false);
  }

  @Test(description = """
  <head><meta charset="UTF-8"></head>
  """)
  public void compute05b() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(AttributeNamePojo.CHARSET.index());
      opts.addInt8(opts.addObject("UTF-8"));
      opts.addByte(HtmlBytes.xSTARTTAG8);
      opts.addInt8(ElementNamePojo.META.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.VOIDELEM);

      opts.push(new ScanElementNodeFrame(7, IntSize.BIT8, 1));
      opts.push(new ScanElementAttributeFrame(8, IntSize.BIT8, 1, 1));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanElementNodeState.class);
    assertEquals(ctx.hasEvent(), true);
    assertEquals(ctx.nextEvent(), EndStartTagEvent.of(EndStartTagKind.VOID_ELEMENT));
  }

  @Test(description = """
  <span>ipsum</span>
  """)
  public void compute08() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xTEXT8);
      opts.addInt8(opts.addObject("ipsum"));
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.SPAN.index());

      opts.push(new ScanElementNodeFrame(6, IntSize.BIT8, 1));
      opts.push(new ScanElementAttributeFrame(6, IntSize.BIT8, 1));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanElementAttributeState.class);
    assertEquals(ctx.hasEvent(), false);
  }

  @Test(description = """
  <span>ipsum</span>
  """)
  public void compute08b() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xTEXT8);
      opts.addInt8(opts.addObject("ipsum"));
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.SPAN.index());

      opts.push(new ScanElementNodeFrame(6, IntSize.BIT8, 1));

      final ScanElementAttributeFrame f;
      f = new ScanElementAttributeFrame(7, IntSize.BIT8, 1, 1);

      f.childText();

      opts.push(f);
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanElementNodeState.class);
    assertEquals(ctx.hasEvent(), true);
    assertEquals(ctx.nextEvent(), EndStartTagEvent.of(EndStartTagKind.ELEMENT));
  }

}
