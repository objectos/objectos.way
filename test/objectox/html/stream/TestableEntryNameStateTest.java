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

public class TestableEntryNameStateTest {

  @DataProvider
  public Object[][] testableOnlyProvider() {
    return new Object[][] {
        {TestableLevel.TOP_LEVEL},
        {TestableLevel.TEST_ONLY}
    };
  }

  @Test(dataProvider = "testableOnlyProvider", description = """
  testable entry name:
  - 8 bit
  - top level
  - test-only level
  """)
  public void compute01(TestableLevel level) {
    test(
        TestableEntryNameState::compute,

        opts -> {
          opts.addInt8(opts.addObject("foo"));

          opts.push(f -> {
            f.index = 0;
          });
        },

        level, IntSize.BIT8,

        opts -> {
          opts.result(new TestableEntryNameEvent(0, "foo"));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 1;
        }))
    );
  }

  @Test(description = """
  testable entry name:
  - 8 bit
  - regular level
  """)
  public void compute02() {
    test(
        TestableEntryNameState::compute,

        opts -> {
          opts.addInt8(opts.addObject("foo"));

          opts.push(f -> {
            f.index = 5;

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
            f.index = 5;

            f.regular(ElementName.DIV);

            f.values8(0, 1);
          });

          opts.push(f -> {
            f.index = 0;
          });

          opts.result(new RenderTextEvent(1, "foo"));
        },

        ctx -> new TestableEntryNameState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8)
    );
  }

  @Test(description = """
  testable entry name:
  - documentNode
  - top level
  """)
  public void documentNode01() {
    test(
        TestableEntryNameState::documentNode,

        opts -> {
          opts.addInt8(opts.addObject("foo"));
        },

        TestableLevel.TOP_LEVEL, IntSize.BIT8,

        _ -> {},

        ctx -> new TestableEntryNameState(ctx, TestableLevel.TOP_LEVEL, IntSize.BIT8)
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
  testable entry name:
  - documentNode
  - 8 bit
  - regular level
  - test-only level
  """)
  public void documentNode02(TestableLevel level) {
    test(
        TestableEntryNameState::documentNode,

        opts -> {
          opts.addInt8(opts.addObject("foo"));
        },

        level, IntSize.BIT8,

        _ -> {},

        ctx -> new SkipState(ctx, 1)
    );
  }

  @Test(dataProvider = "markedProvider", description = """
  testable entry name:
  - documentNode
  - 16 bit
  - regular level
  - test-only level
  """)
  public void documentNode03(TestableLevel level) {
    test(
        TestableEntryNameState::documentNode,

        opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

          opts.addInt16(opts.addObject("foo"));
        },

        level, IntSize.BIT16,

        _ -> {},

        ctx -> new SkipState(ctx, 2)
    );
  }

  @Test(dataProvider = "markedProvider", description = """
  testable entry name:
  - documentNode
  - 24 bit
  - regular level
  - test-only level
  """)
  public void documentNode04(TestableLevel level) {
    test(
        TestableEntryNameState::documentNode,

        opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

          opts.addInt24(opts.addObject("foo"));
        },

        level, IntSize.BIT24,

        _ -> {},

        ctx -> new SkipState(ctx, 3)
    );
  }

  @Test(description = """
  testable entry name:
  - elementAttribute
  - top level
  - throws
  """)
  public void elementAttribute01() {
    testError(
        TestableEntryNameState::elementAttribute,

        opts -> {
          opts.addInt8(opts.addObject("foo"));
        },

        TestableLevel.TOP_LEVEL, IntSize.BIT8,

        "Not an element attribute op"
    );
  }

  @Test(dataProvider = "markedProvider", description = """
  testable entry name:
  - elementAttribute
  - regular level
  - test-only level
  """)
  public void elementAttribute02(TestableLevel level) {
    test(
        TestableEntryNameState::elementAttribute,

        opts -> {
          opts.addInt8(opts.addObject("foo"));
        },

        level, IntSize.BIT8,

        _ -> {},

        ctx -> new TestableEntryNameState(ctx, level, IntSize.BIT8)
    );
  }

  @Test(description = """
  testable entry name:
  - elementNode
  - top level
  - throws
  """)
  public void elementNode01() {
    testError(
        TestableEntryNameState::elementNode,

        opts -> {
          opts.addInt8(opts.addObject("foo"));
        },

        TestableLevel.TOP_LEVEL, IntSize.BIT8,

        "Not an element node op"
    );
  }

  @Test(dataProvider = "markedProvider", description = """
  testable entry name:
  - elementNode
  - regular level
  - test-only level
  """)
  public void elementNode02(TestableLevel level) {
    test(
        TestableEntryNameState::elementNode,

        opts -> {
          opts.addInt8(opts.addObject("foo"));
        },

        level, IntSize.BIT8,

        _ -> {},

        ctx -> new TestableEntryNameState(ctx, level, IntSize.BIT8)
    );
  }

  private void test(
      Function<TestableEntryNameState, State> op,
      Consumer<? super CtxY> opts, TestableLevel testableLevel, IntSize size,
      Consumer<? super CtxY> expected, Function<Ctx, State> state) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final TestableEntryNameState subject;
    subject = new TestableEntryNameState(ctx, testableLevel, size);

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
      Function<TestableEntryNameState, State> op,
      Consumer<? super CtxY> opts, TestableLevel testableLevel, IntSize size,
      String expected) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final TestableEntryNameState subject;
    subject = new TestableEntryNameState(ctx, testableLevel, size);

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
