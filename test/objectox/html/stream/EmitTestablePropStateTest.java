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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EmitTestablePropStateTest {

  @DataProvider
  public Object[][] testableOnlyProvider() {
    return new Object[][] {
        {TestableLevel.TOP_LEVEL},
        {TestableLevel.TEST_ONLY}
    };
  }

  @Test(dataProvider = "testableOnlyProvider", description = """
  testable prop:
  - 8 bit
  - top level
  - test-only level
  """)
  public void compute01(TestableLevel level) {
    test(
        EmitTestablePropState::compute,

        opts -> {
          opts.addObject("Name");
          opts.addInt8(opts.addObject("Value"));

          opts.push(f -> {
            f.index = 0;
          });
        },

        level, IntSize.BIT8,

        opts -> {
          opts.result(new EmitTestablePropEvent(0, "Name", "Value"));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 1;
        }))
    );
  }

  @Test(description = """
  testable prop:
  - 8 bit
  - regular level
  """)
  public void compute02() {
    test(
        EmitTestablePropState::compute,

        opts -> {
          opts.addObject("Name");
          opts.addInt8(opts.addObject("Value"));

          opts.push(f -> {
            f.index = 6;

            f.regular(ElementName.DIV);

            f.values8(0, 1);
          });

          opts.push(f -> {
            f.index = 0;
          });
        },

        TestableLevel.REGULAR, IntSize.BIT8,

        opts -> {
          opts.push(f -> {
            f.index = 6;

            f.regular(ElementName.DIV);

            f.values8(0, 1);
          });

          opts.push(f -> {
            f.index = 0;
          });

          opts.result(new EmitTextEvent(1, "Value"));
        },

        ctx -> new EmitTestablePropState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8)
    );
  }

  @Test(description = """
  testable prop:
  - 16 bit
  - name index inferred at the object index boundary
  """)
  public void compute03() {
    test(
        EmitTestablePropState::compute,

        opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8);

          opts.addObject("Name");
          opts.addInt16(opts.addObject("Value"));

          opts.push(f -> {
            f.index = 0;
          });
        },

        TestableLevel.TOP_LEVEL, IntSize.BIT16,

        opts -> {
          opts.result(new EmitTestablePropEvent(0, "Name", "Value"));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 2;
        }))
    );
  }

  @Test(description = """
  testable prop:
  - documentNode
  - top level
  """)
  public void documentNode01() {    test(
        EmitTestablePropState::documentNode,

        opts -> {
          opts.addObject("Name");
          opts.addInt8(opts.addObject("Value"));
        },

        TestableLevel.TOP_LEVEL, IntSize.BIT8,

        _ -> {},

        ctx -> new EmitTestablePropState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8)
    );
  }

  @DataProvider
  public Object[][] markedProvider() {
    return new Object[][] {
        {TestableLevel.REGULAR},
        {TestableLevel.TEST_ONLY}
    };
  }

  @Test(dataProvider = "markedProvider", description = """
  testable prop:
  - documentNode
  - 8 bit
  - regular level
  - test-only level
  """)
  public void documentNode02(TestableLevel level) {
    test(
        EmitTestablePropState::documentNode,

        opts -> {
          opts.addObject("Name");
          opts.addInt8(opts.addObject("Value"));
        },

        level, IntSize.BIT8,

        _ -> {},

        ctx -> new SkipState(ctx, 1)
    );
  }

  @Test(dataProvider = "markedProvider", description = """
  testable prop:
  - documentNode
  - 16 bit
  - regular level
  - test-only level
  """)
  public void documentNode03(TestableLevel level) {
    test(
        EmitTestablePropState::documentNode,

        opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

          opts.addObject("Name");
          opts.addInt16(opts.addObject("Value"));
        },

        level, IntSize.BIT16,

        _ -> {},

        ctx -> new SkipState(ctx, 2)
    );
  }

  @Test(dataProvider = "markedProvider", description = """
  testable prop:
  - documentNode
  - 24 bit
  - regular level
  - test-only level
  """)
  public void documentNode04(TestableLevel level) {
    test(
        EmitTestablePropState::documentNode,

        opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

          opts.addObject("Name");
          opts.addInt24(opts.addObject("Value"));
        },

        level, IntSize.BIT24,

        _ -> {},

        ctx -> new SkipState(ctx, 3)
    );
  }

  @Test(description = """
  testable prop:
  - elementAttribute
  - top level
  - throws
  """)
  public void elementAttribute01() {
    testError(
        EmitTestablePropState::elementAttribute,

        opts -> {
          opts.addObject("Name");
          opts.addInt8(opts.addObject("Value"));
        },

        TestableLevel.TOP_LEVEL, IntSize.BIT8,

        "Not an element attribute op"
    );
  }

  @Test(dataProvider = "markedProvider", description = """
  testable prop:
  - elementAttribute
  - regular level
  - test-only level
  """)
  public void elementAttribute02(TestableLevel level) {
    test(
        EmitTestablePropState::elementAttribute,

        opts -> {
          opts.addObject("Name");
          opts.addInt8(opts.addObject("Value"));
        },

        level, IntSize.BIT8,

        _ -> {},

        ctx -> new EmitTestablePropState(ctx, level, IntSize.BIT8)
    );
  }

  @Test(description = """
  testable prop:
  - elementNode
  - top level
  - throws
  """)
  public void elementNode01() {
    testError(
        EmitTestablePropState::elementNode,

        opts -> {
          opts.addObject("Name");
          opts.addInt8(opts.addObject("Value"));
        },

        TestableLevel.TOP_LEVEL, IntSize.BIT8,

        "Not an element node op"
    );
  }

  @Test(dataProvider = "markedProvider", description = """
  testable prop:
  - elementNode
  - regular level
  - test-only level
  """)
  public void elementNode02(TestableLevel level) {
    test(
        EmitTestablePropState::elementNode,

        opts -> {
          opts.addObject("Name");
          opts.addInt8(opts.addObject("Value"));
        },

        level, IntSize.BIT8,

        _ -> {},

        ctx -> new EmitTestablePropState(ctx, level, IntSize.BIT8)
    );
  }

  private void test(
      Function<EmitTestablePropState, State> op,
      Consumer<? super CtxY> opts, TestableLevel testableLevel, IntSize size,
      Consumer<? super CtxY> expected, Function<Ctx, State> state) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final EmitTestablePropState subject;
    subject = new EmitTestablePropState(ctx, testableLevel, size);

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
      Function<EmitTestablePropState, State> op,
      Consumer<? super CtxY> opts, TestableLevel testableLevel, IntSize size,
      String expected) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final EmitTestablePropState subject;
    subject = new EmitTestablePropState(ctx, testableLevel, size);

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
