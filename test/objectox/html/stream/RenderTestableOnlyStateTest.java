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
import objectox.html.markup.HtmlSink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RenderTestableOnlyStateTest {

  @Test(description = """
  testable-only heading:
  - 8 bit
  - document level
  """)
  public void heading08() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte((byte) 1);

      opts.addInt8(opts.addObject("Ipsum"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableOnlyState subject;
    subject = new RenderTestableOnlyState(ctx, ScanOpKind.REGULAR, TestableKind.HEADING, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, new TestableNode(TestableKind.HEADING, 1, 0, null, "Ipsum")));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 2;
          }));
        })
    );
  }

  @Test(description = """
  testable-only heading:
  - 16 bit
  - document level
  """)
  public void heading16() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addByte((byte) 2);

      opts.addInt16(opts.addObject("Ipsum"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableOnlyState subject;
    subject = new RenderTestableOnlyState(ctx, ScanOpKind.REGULAR, TestableKind.HEADING, IntSize.BIT16);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, new TestableNode(TestableKind.HEADING, 2, 0, null, "Ipsum")));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 3;
          }));
        })
    );
  }

  @Test(description = """
  testable-only heading:
  - 24 bit
  - document level
  """)
  public void heading24() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addByte((byte) 3);

      opts.addInt24(opts.addObject("Ipsum"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableOnlyState subject;
    subject = new RenderTestableOnlyState(ctx, ScanOpKind.REGULAR, TestableKind.HEADING, IntSize.BIT24);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, new TestableNode(TestableKind.HEADING, 3, 0, null, "Ipsum")));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 4;
          }));
        })
    );
  }

  @Test(description = """
  testable-only cell:
  - 8 bit
  - document level
  """)
  public void cell08() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addInt8(4);

      opts.addInt8(opts.addObject("Ipsum"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableOnlyState subject;
    subject = new RenderTestableOnlyState(ctx, ScanOpKind.REGULAR, TestableKind.CELL, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, new TestableNode(TestableKind.CELL, 0, 4, null, "Ipsum")));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 2;
          }));
        })
    );
  }

  @Test(description = """
  testable-only property:
  - 8 bit
  - document level
  """)
  public void prop08() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addInt8(opts.addObject("foo"));

      opts.addInt8(opts.addObject("bar"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableOnlyState subject;
    subject = new RenderTestableOnlyState(ctx, ScanOpKind.REGULAR, TestableKind.PROP, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, new TestableNode(TestableKind.PROP, 0, 0, "foo", "bar")));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 2;
          }));
        })
    );
  }

  @Test(description = """
  testable-only property key:
  - 8 bit
  - document level
  """)
  public void propKey08() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addInt8(opts.addObject("foo"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableOnlyState subject;
    subject = new RenderTestableOnlyState(ctx, ScanOpKind.REGULAR, TestableKind.PROP_KEY, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, new TestableNode(TestableKind.PROP_KEY, 0, 0, "foo", null)));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));
        })
    );
  }

  @Test(description = """
  testable-only property value:
  - 8 bit
  - document level
  """)
  public void propValue08() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addInt8(opts.addObject("bar"));

      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableOnlyState subject;
    subject = new RenderTestableOnlyState(ctx, ScanOpKind.REGULAR, TestableKind.PROP_VALUE, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, new TestableNode(TestableKind.PROP_VALUE, 0, 0, null, "bar")));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 1;
          }));
        })
    );
  }

  @Test(description = """
  testable-only new line:
  - document level
  """)
  public void newLine01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;
      }));
    });

    final RenderTestableOnlyState subject;
    subject = new RenderTestableOnlyState(ctx, ScanOpKind.REGULAR, TestableKind.NEWLINE, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTestableNodeState(ctx, new TestableNode(TestableKind.NEWLINE, 0, 0, null, null)));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 0;
          }));
        })
    );
  }

  @DataProvider
  public Object[][] documentNodeProvider() {
    return new Object[][] {
        {ScanOpKind.REGULAR, TestableKind.HEADING, "regular heading"},
        {ScanOpKind.REGULAR, TestableKind.NEWLINE, "regular new line"},
        {ScanOpKind.X, TestableKind.HEADING, "recorded heading"},
        {ScanOpKind.X, TestableKind.CELL, "recorded cell"},
        {ScanOpKind.X, TestableKind.PROP, "recorded prop"},
        {ScanOpKind.X, TestableKind.PROP_KEY, "recorded prop key"},
        {ScanOpKind.X, TestableKind.PROP_VALUE, "recorded prop value"}
    };
  }

  @Test(dataProvider = "documentNodeProvider", description = """
  documentNode:
  - renders at the document level
  """)
  public void documentNode(ScanOpKind opKind, TestableKind kind, String description) {
    final Ctx ctx;
    ctx = CtxY.create(_ -> {});

    final RenderTestableOnlyState subject;
    subject = new RenderTestableOnlyState(ctx, opKind, kind, IntSize.BIT8);

    final State res;
    res = subject.documentNode();

    assertEquals(res, subject);
  }

  @Test(description = """
  documentNode:
  - recorded new line
  - continues scanning
  """)
  public void documentNodeNewLine() {
    final Ctx ctx;
    ctx = CtxY.create(_ -> {});

    final RenderTestableOnlyState subject;
    subject = new RenderTestableOnlyState(ctx, ScanOpKind.X, TestableKind.NEWLINE, IntSize.BIT8);

    final State res;
    res = subject.documentNode();

    assertEquals(res, new ScanDocumentNodeState(ctx));
  }

}
