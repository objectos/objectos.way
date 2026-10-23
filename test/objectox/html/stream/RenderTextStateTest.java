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
import static org.testng.Assert.fail;

import java.util.function.Consumer;
import java.util.function.Function;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.Test;

public class RenderTextStateTest {

  @Test(description = """
  text node:
  - 8 bit
  - document level
  """)
  public void compute01() {
    test(
        RenderTextState::compute,

        opts -> {
          opts.addInt8(opts.addObject("Ipsum"));

          opts.push(f -> {
            f.index = 0;
          });
        },

        ScanLevel.TOP_LEVEL, IntSize.BIT8,

        opts -> {
          opts.result(new RenderTextEvent(0, "Ipsum"));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 1;
        }))
    );
  }

  @Test(description = """
  text node:
  - 16 bit
  - document level
  """)
  public void compute02() {
    test(
        RenderTextState::compute,

        opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

          opts.addInt16(opts.addObject("Ipsum"));

          opts.push(f -> {
            f.index = 0;
          });
        },

        ScanLevel.TOP_LEVEL, IntSize.BIT16,

        opts -> {
          opts.result(new RenderTextEvent(0, "Ipsum"));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 2;
        }))
    );
  }

  @Test(description = """
  text node:
  - 24 bit
  - document level
  """)
  public void compute03() {
    test(
        RenderTextState::compute,

        opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

          opts.addInt24(opts.addObject("Ipsum"));

          opts.push(f -> {
            f.index = 0;
          });
        },

        ScanLevel.TOP_LEVEL, IntSize.BIT24,

        opts -> {
          opts.result(new RenderTextEvent(0, "Ipsum"));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 3;
        }))
    );
  }

  @Test(description = """
  text node:
  - 8 bit
  - element level
  """)
  public void compute04() {
    test(
        RenderTextState::compute,

        opts -> {
          opts.addInt8(opts.addObject("Ipsum"));

          opts.push(f -> {
            f.index = 5;

            f.regular(ElementName.DIV);

            f.values8(0, 1);
          });

          opts.push(f -> {
            f.index = 0;
          });
        },

        ScanLevel.NESTED, IntSize.BIT8,

        opts -> {
          opts.push(f -> {
            f.index = 5;

            f.regular(ElementName.DIV);

            f.values8(0, 1);
          });

          opts.result(new RenderTextEvent(1, "Ipsum"));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 1;
        }))
    );
  }

  @Test(description = """
  text node:
  - documentNode
  - top level
  """)
  public void documentNode01() {
    test(
        RenderTextState::documentNode,

        opts -> {
          opts.addInt8(opts.addObject("Ipsum"));
        },

        ScanLevel.TOP_LEVEL, IntSize.BIT8,

        _ -> {},

        ctx -> new RenderTextState(ctx, ScanLevel.TOP_LEVEL, IntSize.BIT8)
    );
  }

  @Test(description = """
  text node:
  - documentNode
  - 8 bit
  - nested level
  """)
  public void documentNode02() {
    test(
        RenderTextState::documentNode,

        opts -> {
          opts.addInt8(opts.addObject("Ipsum"));
        },

        ScanLevel.NESTED, IntSize.BIT8,

        _ -> {},

        ctx -> new SkipState(ctx, 1)
    );
  }

  @Test(description = """
  text node:
  - documentNode
  - 16 bit
  - nested level
  """)
  public void documentNode03() {
    test(
        RenderTextState::documentNode,

        opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

          opts.addInt16(opts.addObject("Ipsum"));
        },

        ScanLevel.NESTED, IntSize.BIT16,

        _ -> {},

        ctx -> new SkipState(ctx, 2)
    );
  }

  @Test(description = """
  text node:
  - documentNode
  - 24 bit
  - nested level
  """)
  public void documentNode04() {
    test(
        RenderTextState::documentNode,

        opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

          opts.addInt24(opts.addObject("Ipsum"));
        },

        ScanLevel.NESTED, IntSize.BIT24,

        _ -> {},

        ctx -> new SkipState(ctx, 3)
    );
  }

  @Test(description = """
  text node:
  - elementAttribute
  - top level
  - throws
  """)
  public void elementAttribute01() {
    testError(
        RenderTextState::elementAttribute,

        opts -> {
          opts.addInt8(opts.addObject("Ipsum"));
        },

        ScanLevel.TOP_LEVEL, IntSize.BIT8,

        "Not an element attribute op"
    );
  }

  @Test(description = """
  text node:
  - elementAttribute
  - nested level
  """)
  public void elementAttribute02() {
    test(
        RenderTextState::elementAttribute,

        opts -> {
          opts.addInt8(opts.addObject("Ipsum"));
        },

        ScanLevel.NESTED, IntSize.BIT8,

        _ -> {},

        ctx -> new RenderTextState(ctx, ScanLevel.NESTED, IntSize.BIT8)
    );
  }

  @Test(description = """
  text node:
  - elementNode
  - top level
  - throws
  """)
  public void elementNode01() {
    testError(
        RenderTextState::elementNode,

        opts -> {
          opts.addInt8(opts.addObject("Ipsum"));
        },

        ScanLevel.TOP_LEVEL, IntSize.BIT8,

        "Not an element node op"
    );
  }

  @Test(description = """
  text node:
  - elementNode
  - nested level
  """)
  public void elementNode02() {
    test(
        RenderTextState::elementNode,

        opts -> {
          opts.addInt8(opts.addObject("Ipsum"));
        },

        ScanLevel.NESTED, IntSize.BIT8,

        _ -> {},

        ctx -> new RenderTextState(ctx, ScanLevel.NESTED, IntSize.BIT8)
    );
  }

  private void test(
      Function<RenderTextState, State> op,
      Consumer<? super CtxY> opts, ScanLevel scanLevel, IntSize textSize,
      Consumer<? super CtxY> expected, Function<Ctx, State> state) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final RenderTextState subject;
    subject = new RenderTextState(ctx, scanLevel, textSize);

    final State res;
    res = op.apply(subject);

    final State expectedState;
    expectedState = state.apply(ctx);

    assertEquals(res, expectedState);

    assertEquals(
        ctx,

        CtxY.create(expectedOpts -> {
          expectedOpts.sink(ctx);

          expected.accept(expectedOpts);
        })
    );
  }

  private void testError(
      Function<RenderTextState, State> op,
      Consumer<? super CtxY> opts, ScanLevel scanLevel, IntSize textSize,
      String expected) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final RenderTextState subject;
    subject = new RenderTextState(ctx, scanLevel, textSize);

    try {
      op.apply(subject);

      fail();
    } catch (IllegalStateException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, expected);
    }

    assertEquals(
        ctx,

        CtxY.create(expectedOpts -> {
          expectedOpts.sink(ctx);
        })
    );
  }

}
