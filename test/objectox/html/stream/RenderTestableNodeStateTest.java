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

    final TestableNode node;
    node = new TestableNode(TestableKind.HEADING, 1, 0, null, "Ipsum");

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, node);

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

    final TestableNode node;
    node = new TestableNode(TestableKind.HEADING, 6, 0, null, "Ipsum");

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, node);

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

    final TestableNode node;
    node = new TestableNode(TestableKind.CELL, 0, 4, null, "Ipsum");

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, node);

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
  - property
  - document level
  """)
  public void prop01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final TestableNode node;
    node = new TestableNode(TestableKind.PROP, 0, 0, "foo", "bar");

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, node);

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

          opts.result(TestablePropEvent.of(0, "foo", "bar"));
        })
    );
  }

  @Test(description = """
  testable node:
  - property key
  - document level
  """)
  public void propKey01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final TestableNode node;
    node = new TestableNode(TestableKind.PROP_KEY, 0, 0, "foo", null);

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, node);

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

          opts.result(TestableEntryNameEvent.of(0, "foo"));
        })
    );
  }

  @Test(description = """
  testable node:
  - property value
  - document level
  """)
  public void propValue01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final TestableNode node;
    node = new TestableNode(TestableKind.PROP_VALUE, 0, 0, null, "bar");

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, node);

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

          opts.result(TestableEntryValueEvent.of(0, "bar"));
        })
    );
  }

  @Test(description = """
  testable node:
  - new line
  - document level
  """)
  public void newLine01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final TestableNode node;
    node = new TestableNode(TestableKind.NEWLINE, 0, 0, null, null);

    final RenderTestableNodeState subject;
    subject = new RenderTestableNodeState(ctx, node);

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

          opts.result(TestableNewLineEvent.of(0));
        })
    );
  }

}
