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

import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class EndNodeStateTest {

  @Test(description = """
  empty stack:
  - pushes the frame back
  - scans the document
  """)
  public void testCase00() {
    final Ctx ctx;
    ctx = CtxY.create(_ -> {
    });

    final Frame top;
    top = FrameY.create(f -> {
      f.index = 0;
    });

    final EndNodeState subject;
    subject = new EndNodeState(ctx, top);

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
        })
    );
  }

  @Test(description = """
  non-empty stack:
  - scans the element
  """)
  public void testCase01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 5;

        f.regular(ElementName.DIV);

        f.values8(0, 1);
      }));
    });

    final Frame top;
    top = FrameY.create(f -> {
      f.index = 0;
    });

    final EndNodeState subject;
    subject = new EndNodeState(ctx, top);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanElementNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.push(FrameY.create(f -> {
            f.index = 5;

            f.regular(ElementName.DIV);

            f.values8(0, 1);
          }));
        })
    );
  }

  @Test(description = """
  invalid stack state:
  - size < 0
  """)
  public void testCase02() {
    final Ctx ctx;
    ctx = new Ctx(new byte[0], new Object[0], Ctx.EMPTY_STACK, -3);

    final Frame top;
    top = FrameY.create(f -> {
      f.index = 0;
    });

    final EndNodeState subject;
    subject = new EndNodeState(ctx, top);

    try {
      subject.compute();

      fail();
    } catch (IllegalStateException e) {
      final String msg;
      msg = e.getMessage();

      assertEquals(msg, "Invalid stack state: size=-2");
    }
  }

}
