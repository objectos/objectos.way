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
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class RenderTestableNodeStateTest {

  @Test(description = """
  testable node:
  - heading 1
  - document level
  """)
  public void heading01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, TestableName.H1, "Ipsum");

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 0;
          }));

          opts.result(TestableHeadingEvent.of(0, 1, "Ipsum"));
        })
    );
  }

  @Test(description = """
  testable node:
  - heading 6
  - element level
  """)
  public void heading02() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 5;

        f.regular(ElementName.DIV);

        f.values8(0, 1);
      }));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, TestableName.H6, "Ipsum");

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanElementNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 5;

            f.regular(ElementName.DIV);

            f.values8(0, 1);
          }));

          opts.result(TestableHeadingEvent.of(1, 6, "Ipsum"));
        })
    );
  }

  @Test(description = """
  testable node:
  - cell
  - document level
  """)
  public void cell01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, TestableName.cell(4), "Ipsum");

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 0;
          }));

          opts.result(TestableCellEvent.of(0, 4, "Ipsum"));
        })
    );
  }

  @Test(description = """
  testable node:
  - entry name
  - document level
  """)
  public void entryName01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, TestableName.DT, "Ipsum");

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 0;
          }));

          opts.result(TestableEntryNameEvent.of(0, "Ipsum"));
        })
    );
  }

  @Test(description = """
  testable node:
  - entry value
  - document level
  """)
  public void entryValue01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, TestableName.DD, "Ipsum");

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 0;
          }));

          opts.result(TestableEntryValueEvent.of(0, "Ipsum"));
        })
    );
  }

}
