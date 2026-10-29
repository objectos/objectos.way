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
import static org.testng.Assert.assertFalse;

import java.util.function.Consumer;
import java.util.function.Function;
import org.testng.annotations.Test;

public class EndDocumentStateTest {

  @Test(description = """
  end document:
  - non-empty
  """)
  public void compute01() {
    test(
        EndDocumentState::compute,

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });
        },

        false,

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });

          opts.result(new EndDocumentEvent(false));
        },

        ctx -> new StopState(ctx)
    );
  }

  @Test(description = """
  end document:
  - empty
  """)
  public void compute02() {
    test(
        EndDocumentState::compute,

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });
        },

        true,

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });

          opts.result(new EndDocumentEvent(true));
        },

        ctx -> new StopState(ctx)
    );
  }

  @Test(description = """
  equality:
  - considers the empty flag
  """)
  public void equals01() {
    final Ctx ctx;
    ctx = CtxY.create(_ -> {});

    final EndDocumentState nonEmpty;
    nonEmpty = new EndDocumentState(ctx, false);

    final EndDocumentState empty;
    empty = new EndDocumentState(ctx, true);

    assertEquals(empty, new EndDocumentState(ctx, true));

    assertFalse(empty.equals(nonEmpty));
  }

  private void test(
      Function<EndDocumentState, State> op,
      Consumer<? super CtxY> opts, boolean empty,
      Consumer<? super CtxY> expected, Function<Ctx, State> state) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final EndDocumentState subject;
    subject = new EndDocumentState(ctx, empty);

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

}
