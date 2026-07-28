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
package objectox.html.play;

import static org.testng.Assert.assertEquals;

import objectox.html.elem.ElementNamePojo;
import objectox.html.rec.HtmlBytes;
import org.testng.annotations.Test;

public class StartStateTest {

  @Test(description = "empty")
  public void compute00() {
    final Ctx ctx;
    ctx = CtxY.create(_ -> {});

    final StartState subject;
    subject = new StartState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanDocumentNodeState.class);
    assertEquals(ctx.hasEvent(), true);
    assertEquals(ctx.nextEvent(), BeginDocumentEvent.INSTANCE);
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

    final StartState subject;
    subject = new StartState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanDocumentNodeState.class);
    assertEquals(ctx.peekByte(), HtmlBytes.STARTTAG8);
    assertEquals(ctx.hasEvent(), true);
    assertEquals(ctx.nextEvent(), BeginDocumentEvent.INSTANCE);
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

      opts.push(ScanDocumentNodeFrame.of(0));
    });

    final StartState subject;
    subject = new StartState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), ScanDocumentNodeState.class);
    assertEquals(ctx.peekByte(), HtmlBytes.STARTTAG8);
    assertEquals(ctx.hasEvent(), true);
    assertEquals(ctx.nextEvent(), BeginDocumentEvent.INSTANCE);
  }

}
