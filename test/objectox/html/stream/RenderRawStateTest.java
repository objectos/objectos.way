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
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.Test;

public class RenderRawStateTest {

  @Test(description = """
  raw node:
  - 8 bit
  - document level
  """)
  public void compute08() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addInt8(opts.addObject("Ipsum"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderRawState subject;
    subject = new RenderRawState(ctx, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndNodeState(ctx, FrameY.create(f -> {
      f.index = 1;
    })));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.result(RenderRawEvent.of(0, "Ipsum"));
        })
    );
  }

  @Test(description = """
  raw node:
  - 16 bit
  - document level
  """)
  public void compute16() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addInt16(opts.addObject("Ipsum"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderRawState subject;
    subject = new RenderRawState(ctx, IntSize.BIT16);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndNodeState(ctx, FrameY.create(f -> {
      f.index = 2;
    })));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.result(RenderRawEvent.of(0, "Ipsum"));
        })
    );
  }

  @Test(description = """
  raw node:
  - 24 bit
  - document level
  """)
  public void compute24() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addInt24(opts.addObject("Ipsum"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderRawState subject;
    subject = new RenderRawState(ctx, IntSize.BIT24);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndNodeState(ctx, FrameY.create(f -> {
      f.index = 3;
    })));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.result(RenderRawEvent.of(0, "Ipsum"));
        })
    );
  }

  @Test(description = """
  raw node:
  - 8 bit
  - element level
  """)
  public void element08() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addInt8(opts.addObject("Ipsum"));

      opts.push(FrameY.create(f -> {
        f.index = 5;

        f.regular(ElementName.DIV);

        f.values8(0, 1);
      }));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderRawState subject;
    subject = new RenderRawState(ctx, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndNodeState(ctx, FrameY.create(f -> {
      f.index = 1;
    })));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 5;

            f.regular(ElementName.DIV);

            f.values8(0, 1);
          }));

          opts.result(RenderRawEvent.of(1, "Ipsum"));
        })
    );
  }

}
