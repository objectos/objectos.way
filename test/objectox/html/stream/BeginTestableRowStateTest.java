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
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class BeginTestableRowStateTest {

  @Test(description = """
  testable row:
  - top level
  """)
  public void compute01() {
    test(
        BeginTestableRowState::compute,

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });
        },

        ScanLevel.TOP_LEVEL,

        opts -> {
          opts.result(new BeginTestableRowEvent(0));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 0;
        }))
    );
  }

  @Test(description = """
  testable row:
  - nested level
  """)
  public void compute02() {
    test(
        BeginTestableRowState::compute,

        opts -> {
          opts.push(f -> {
            f.index = 0;

            f.regular(ElementName.DIV);

            f.values8(0, 1);
          });

          opts.push(f -> {
            f.index = 0;
          });
        },

        ScanLevel.NESTED,

        opts -> {
          opts.push(f -> {
            f.index = 0;

            f.regular(ElementName.DIV);

            f.values8(0, 1);
          });

          opts.result(new BeginTestableRowEvent(1));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 0;
        }))
    );
  }

  @Test(description = """
  testable row:
  - documentNode
  - top level
  """)
  public void documentNode01() {
    test(
        BeginTestableRowState::documentNode,

        _ -> {},

        ScanLevel.TOP_LEVEL,

        _ -> {},

        ctx -> new BeginTestableRowState(ctx, ScanLevel.TOP_LEVEL)
    );
  }

  @Test(description = """
  testable row:
  - documentNode
  - nested level
  """)
  public void documentNode02() {
    test(
        BeginTestableRowState::documentNode,

        _ -> {},

        ScanLevel.NESTED,

        _ -> {},

        ctx -> new ScanDocumentNodeState(ctx)
    );
  }

  @Test(description = """
  testable row:
  - elementAttribute
  - top level
  - throws
  """)
  public void elementAttribute01() {
    testError(
        BeginTestableRowState::elementAttribute,

        _ -> {},

        ScanLevel.TOP_LEVEL,

        "Not an element attribute op"
    );
  }

  @Test(description = """
  testable row:
  - elementAttribute
  - nested level
  """)
  public void elementAttribute02() {
    test(
        BeginTestableRowState::elementAttribute,

        _ -> {},

        ScanLevel.NESTED,

        _ -> {},

        ctx -> new BeginTestableRowState(ctx, ScanLevel.NESTED)
    );
  }

  @Test(description = """
  testable row:
  - elementNode
  - top level
  - throws
  """)
  public void elementNode01() {
    testError(
        BeginTestableRowState::elementNode,

        _ -> {},

        ScanLevel.TOP_LEVEL,

        "Not an element node op"
    );
  }

  @Test(description = """
  testable row:
  - elementNode
  - nested level
  """)
  public void elementNode02() {
    test(
        BeginTestableRowState::elementNode,

        _ -> {},

        ScanLevel.NESTED,

        _ -> {},

        ctx -> new BeginTestableRowState(ctx, ScanLevel.NESTED)
    );
  }

  private void test(
      Function<BeginTestableRowState, State> op,
      Consumer<? super CtxY> opts, ScanLevel scanLevel,
      Consumer<? super CtxY> expected, Function<Ctx, State> state) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final BeginTestableRowState subject;
    subject = new BeginTestableRowState(ctx, scanLevel);

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
      Function<BeginTestableRowState, State> op,
      Consumer<? super CtxY> opts, ScanLevel scanLevel,
      String expected) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final BeginTestableRowState subject;
    subject = new BeginTestableRowState(ctx, scanLevel);

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
