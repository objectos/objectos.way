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
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.Test;

public class ScanRegularElementNodeStateTest {

  @Test(description = """
  regular elem:
  - no attrs
  - reg elem (no attrs / no children)
  """)
  public void regularElement02() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());

      opts.push(new ScanDocumentNodeFrame(10));
      opts.push(new ScanRegularElementNodeFrame(11, IntSize.BIT8, 1, 1));
    });

    final ScanElementNodeState subject;
    subject = new ScanElementNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.push(new ScanDocumentNodeFrame(11));
        })
    );
  }

  @Test(description = """
  regular elem:
  - no attrs
  - void elem (no attrs)
  """)
  public void regularElement03() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xELEMVOID8);
      opts.addInt8(ElementName.HR.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.ARTICLE.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.ARTICLE.index());

      opts.push(new ScanDocumentNodeFrame(8));
      opts.push(new ScanRegularElementNodeFrame(9, IntSize.BIT8, 1, 1));
    });

    final ScanElementNodeState subject;
    subject = new ScanElementNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.push(new ScanDocumentNodeFrame(9));
        })
    );
  }

  @Test(description = """
  regular elem:
  - no attrs
  - reg elem (no attrs / containing a void elem)
  - void elem (no attrs)
  """)
  public void regularElement04() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xELEMVOID8);
      opts.addInt8(ElementName.META.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.HEAD.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.HEAD.index());
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(4);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.HTML.index());

      opts.push(new ScanDocumentNodeFrame(15));
      opts.push(new ScanRegularElementNodeFrame(16, IntSize.BIT8, 1, 1));
      opts.push(new JumpFrame(8));
      opts.push(new ScanRegularElementNodeFrame(9, IntSize.BIT8, 1, 1));
    });

    final ScanElementNodeState subject;
    subject = new ScanElementNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderEndTagState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.push(new ScanDocumentNodeFrame(15));
          opts.push(new ScanRegularElementNodeFrame(16, IntSize.BIT8, 1, 1));
          opts.push(new JumpFrame(9));
        })
    );
  }

}
