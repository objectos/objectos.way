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

import java.util.function.Consumer;
import java.util.function.Function;
import org.testng.annotations.Test;

public class StopStateTest {

  @Test(description = """
  stop:
  - produces no event
  """)
  public void compute01() {
    test(
        StopState::compute,

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });
        },

        opts -> {
          opts.push(f -> {
            f.index = 0;
          });
        },

        ctx -> new StopState(ctx)
    );
  }

  private void test(
      Function<StopState, State> op,
      Consumer<? super CtxY> opts,
      Consumer<? super CtxY> expected, Function<Ctx, State> state) {
    final Ctx ctx;
    ctx = CtxY.create(opts);

    final StopState subject;
    subject = new StopState(ctx);

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
