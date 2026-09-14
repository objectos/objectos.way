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

import java.util.function.Consumer;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BeginStartTagStateTest {

  @DataProvider
  public Object[][] testCase01Provider() {
    return new Object[][] {
        {ctx(opts -> {
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }), ElementKind.REGULAR, ElementName.DIV, 0, IntSize.BIT8, 4},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES16);
          opts.addInt16(0);
        }), ElementKind.REGULAR, ElementName.DIV, 0, IntSize.BIT16, 5},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES24);
          opts.addInt24(0);
        }), ElementKind.REGULAR, ElementName.DIV, 0, IntSize.BIT24, 6},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(HtmlSink.MAX_INT8);
        }), ElementKind.REGULAR, ElementName.DIV, HtmlSink.MAX_INT8, IntSize.BIT8, 4},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES16);
          opts.addInt16(HtmlSink.MAX_INT16);
        }), ElementKind.REGULAR, ElementName.DIV, HtmlSink.MAX_INT16, IntSize.BIT16, 5},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES24);
          opts.addInt24(HtmlSink.MAX_INT24);
        }), ElementKind.REGULAR, ElementName.DIV, HtmlSink.MAX_INT24, IntSize.BIT24, 6},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }), ElementKind.VOID, ElementName.INPUT, 0, IntSize.BIT8, 4},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES16);
          opts.addInt16(0);
        }), ElementKind.VOID, ElementName.INPUT, 0, IntSize.BIT16, 5},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES24);
          opts.addInt24(0);
        }), ElementKind.VOID, ElementName.INPUT, 0, IntSize.BIT24, 6}
    };
  }

  @Test(dataProvider = "testCase01Provider", description = """
  root element
  """)
  public void testCase01(
      Consumer<? super CtxY> prefix,
      ElementKind elementKind,
      ElementName elementName,
      int valueLength,
      IntSize valueSize,
      int frameIndex) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      prefix.accept(opts);

      opts.push(new Frame(1));
    });

    final BeginStartTagState subject;
    subject = new BeginStartTagState(ctx, elementKind, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          final ElementFrame elem;
          elem = new ElementFrame(elementKind, elementName, valueLength, valueSize);

          final Frame frame;
          frame = new Frame(frameIndex, elem);

          opts.push(frame);

          final BeginStartTagEvent event;
          event = BeginStartTagEvent.of(0, elementName);

          opts.result(event);
        })
    );
  }

  private Consumer<? super CtxY> ctx(Consumer<? super CtxY> opts) {
    return opts;
  }

}
