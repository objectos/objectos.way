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
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.Test;

public class ScanElementAttributeStateTest {

  @Test(description = """
  element w/ 0 values
  """)
  public void regular01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());

      opts.push(FrameY.create(f -> {
        f.index = 0;

        f.regular(ElementName.SPAN);

        f.values8(0, 0);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx, ElementName.SPAN));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT));
          opts.push(FrameY.create(f -> {
            f.index = 0;

            f.regular(ElementName.SPAN);

            f.values8(0, 0);
          }));
        })
    );
  }

  @Test(description = """
  element w/ 0 values
  """)
  public void void01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;

        f.voidElem(ElementName.INPUT);

        f.values8(0, 0);
      }));
    });

    final ScanElementAttributeState subject;
    subject = new ScanElementAttributeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndElementState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT));
          opts.push(FrameY.create(f -> {
            f.index = 0;

            f.voidElem(ElementName.INPUT);

            f.values8(0, 0);
          }));
        })
    );
  }

}