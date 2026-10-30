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
import objectox.html.attr.AttributeName;
import org.testng.annotations.Test;

public class EmitAttributeStateTest {

  @Test
  public void testCase01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addInt8(AttributeName.CONTENTEDITABLE.index());
      opts.addInt8(opts.addObject("false"));

      opts.push(f -> {
        f.index = 2;
      });

      opts.push(f -> {
        f.index = 0;
      });
    });

    final EmitAttributeState subject;
    subject = new EmitAttributeState(ctx, IntSize.BIT8, IntSize.BIT8);

    final State res;
    res = subject.compute();

    assertEquals(res, new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.CONTENTEDITABLE, "false"));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(f -> {
            f.index = 2;
          });
        })
    );
  }

}
