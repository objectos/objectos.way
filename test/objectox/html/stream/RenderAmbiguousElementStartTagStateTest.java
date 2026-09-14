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

import objectox.html.AmbiguousName;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RenderAmbiguousElementStartTagStateTest {

  @DataProvider
  public Object[][] testCase01Provider() {
    return new Object[][] {
        {IntSize.BIT8, 0},
        {IntSize.BIT16, HtmlSink.MAX_INT8 + 1},
        {IntSize.BIT24, HtmlSink.MAX_INT16 + 1}
    };
  }

  @Test(dataProvider = "testCase01Provider", description = """
  ambiguous element start tag
  """)
  public void testCase01(IntSize valueSize, int offset) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(offset);

      opts.addInt8(AmbiguousName.TITLE.index());

      final int valueIndex;
      valueIndex = opts.addObject("Ipsum");

      switch (valueSize) {
        case BIT8 -> opts.addInt8(valueIndex);

        case BIT16 -> opts.addInt16(valueIndex);

        case BIT24 -> opts.addInt24(valueIndex);
      }

      opts.push(FrameY.create(f -> {
        f.index = 1 + valueSize.bytes();
      }));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderAmbiguousElementStartTagState subject;
    subject = new RenderAmbiguousElementStartTagState(ctx, IntSize.BIT8, valueSize);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAmbiguousElementEndStartState(ctx, ElementName.TITLE, "Ipsum"));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1 + valueSize.bytes();
          }));

          opts.push(FrameY.create(f -> {
            f.index = 1 + valueSize.bytes();
          }));

          final BeginStartTagEvent event;
          event = new BeginStartTagEvent(ElementName.TITLE);

          opts.result(event);
        })
    );
  }

}
