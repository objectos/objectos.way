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

import objectos.html.TestableName;
import objectox.html.IntSize;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.Test;

public class RenderTestableTextStateTest {

  @Test(description = """
  testable text node:
  - 8 bit
  - document level
  """)
  public void compute08() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addObject(TestableName.H1);

      final int valueIndex;
      valueIndex = opts.addObject("Ipsum");

      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableTextState subject;
    subject = new RenderTestableTextState(ctx, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, TestableName.H1, "Ipsum"));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));

          opts.result(RenderTextEvent.of(0, "Ipsum"));
        })
    );
  }

  @Test(description = """
  testable text node:
  - 16 bit
  - document level
  """)
  public void compute16() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addObject(TestableName.H1);

      final int valueIndex;
      valueIndex = opts.addObject("Ipsum");

      opts.addInt16(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableTextState subject;
    subject = new RenderTestableTextState(ctx, IntSize.BIT16);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, TestableName.H1, "Ipsum"));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 2;
          }));

          opts.result(RenderTextEvent.of(0, "Ipsum"));
        })
    );
  }

  @Test(description = """
  testable text node:
  - 24 bit
  - document level
  """)
  public void compute24() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addObject(TestableName.H1);

      final int valueIndex;
      valueIndex = opts.addObject("Ipsum");

      opts.addInt24(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableTextState subject;
    subject = new RenderTestableTextState(ctx, IntSize.BIT24);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, TestableName.H1, "Ipsum"));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 3;
          }));

          opts.result(RenderTextEvent.of(0, "Ipsum"));
        })
    );
  }

}
