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

import objectox.html.attr.AttributeName;
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.Test;

public class SkipFragmentStateTest {

  @Test(description = "empty fragment")
  public void testCase01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(5);
      opts.addByte(HtmlBytes.FRAGEND);
      opts.addByte(HtmlBytes.xBOOLEAN8);

      opts.push(FrameY.create(f -> {
        f.index = 1;
      }));
    });

    final SkipFragmentState subject;
    subject = new SkipFragmentState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 5;
          }));
        })
    );
  }

  @Test(description = """
  fragment:
  - one attribute
  """)
  public void testCase02() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(8);
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addByte(HtmlBytes.FRAGEND);
      opts.addByte(HtmlBytes.xBOOLEAN8);

      opts.push(FrameY.create(f -> {
        f.index = 1;
      }));
    });

    final SkipFragmentState subject;
    subject = new SkipFragmentState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 8;
          }));
        })
    );
  }

}
