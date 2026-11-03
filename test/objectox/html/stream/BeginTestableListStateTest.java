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

public class BeginTestableListStateTest {

  @Test(description = """
  testable list:
  - top level
  """)
  public void compute01() {
    test(
        BeginTestableListState::compute,

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });
        },

        ScanLevel.TOP_LEVEL,

        opts -> {
          opts.result(new BeginTestableListEvent(0));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 0;
        }))
    );
  }

  @Test(description = """
  testable list:
  - nested level
  """)
  public void compute02() {
    test(
        BeginTestableListState::compute,

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

          opts.result(new BeginTestableListEvent(1));
        },

        ctx -> new EndNodeState(ctx, FrameY.create(f -> {
          f.index = 0;
        }))
    );
  }

  @Test(description = """
  testable list:
  - documentNode
  - top level
  """)
  public void documentNode01() {
    test(
        BeginTestableListState::documentNode,

        _ -> {},

        ScanLevel.TOP_LEVEL,

        _ -> {},

        ctx -> new BeginTestableListState(ctx, ScanLevel.TOP_LEVEL)
    );
  }

  @Test(description = """
  testable list:
  - documentNode
  - nested level
  """)
  public void documentNode02() {
    test(
        BeginTestableListState::documentNode,

        _ -> {},

        ScanLevel.NESTED,

        _ -> {},

        ctx -> new ScanDocumentNodeState(ctx)
    );
  }

  @Test(description = """
  testable list:
  - elementAttribute
  - top level
  - throws
  """)
  public void elementAttribute01() {
    testError(
        BeginTestableListState::elementAttribute,

        _ -> {},

        ScanLevel.TOP_LEVEL,

        "Not an element attribute op"
    );
  }

  @Test(description = """
  testable list:
  - elementAttribute
  - nested level
  """)
  public void elementAttribute02() {
    test(
        BeginTestableListState::elementAttribute,

        _ -> {},

        ScanLevel.NESTED,

        _ -> {},

        ctx -> new BeginTestableListState(ctx, ScanLevel.NESTED)
    );
  }

  @Test(description = """
  testable list:
  - elementNode
  - top level
  - throws
  """)
  public void elementNode01() {
    testError(
        BeginTestableListState::elementNode,

        _ -> {},

        ScanLevel.TOP_LEVEL,

        "Not an element node op"
    );
  }

  @Test(description = """
  testable list:
  - elementNode
  - nested level
  """)
  public void elementNode02() {
    test(
        BeginTestableListState::elementNode,

        _ -> {},

        ScanLevel.NESTED,

        _ -> {},

        ctx -> new BeginTestableListState(ctx, ScanLevel.NESTED)
    );
  }

  private void test(
      Function<BeginTestableListState, State> op,
      Consumer<? super CtxY> opts, ScanLevel scanLevel,
      Consumer<? super CtxY> expected, Function<Ctx, State> state) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final BeginTestableListState subject;
    subject = new BeginTestableListState(ctx, scanLevel);

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
      Function<BeginTestableListState, State> op,
      Consumer<? super CtxY> opts, ScanLevel scanLevel,
      String expected) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final BeginTestableListState subject;
    subject = new BeginTestableListState(ctx, scanLevel);

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
