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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ScanElementNodeStateTest {

  @Test(description = """
  element:
  - elem
  - x
  """)
  public void testCase01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(FrameY.create(f -> {
        f.index = 0;

        f.regular(ElementName.DIV);

        f.values8(1, 1);
      }));
    });

    final ScanElementNodeState subject;
    subject = new ScanElementNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new EndElementState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 0;

            f.regular(ElementName.DIV);

            f.values8(1, 1);
          }));

          final RenderEndTagEvent event;
          event = new RenderEndTagEvent(ElementName.DIV);

          opts.result(event);
        })
    );
  }

  @DataProvider
  public Object[][] testCase02Provider() {
    return new Object[][] {
        {HtmlBytes.xELEMSTR8, ElementKind.REGULAR, IntSize.BIT8},
        {HtmlBytes.xELEMVOID8, ElementKind.VOID, IntSize.BIT8}
    };
  }

  @Test(dataProvider = "testCase02Provider", description = """
  element:
  - element
  - element
  """)
  public void testCase02(byte proto, ElementKind elementKind, IntSize nameSize) {
    final int frameIndex = 1, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(proto);
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      }));
    });

    final ScanElementNodeState subject;
    subject = new ScanElementNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new BeginStartTagState(ctx, elementKind, nameSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.DIV);

            f.values8(2, 2);
          }));

          opts.push(FrameY.create(f -> {
            f.index = valueIndex + 1;
          }));
        })
    );
  }

  @DataProvider
  public Object[][] testCase03Provider() {
    return new Object[][] {
        {HtmlBytes.xTEXT8, IntSize.BIT8},
        {HtmlBytes.xTEXT16, IntSize.BIT16},
        {HtmlBytes.xTEXT24, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "testCase03Provider", description = """
  element:
  - element
  - text node
  """)
  public void textCase03(byte proto, IntSize textSize) {
    final int frameIndex = 1, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(proto);
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      }));
    });

    final ScanElementNodeState subject;
    subject = new ScanElementNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderTextState(ctx, textSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.DIV);

            f.values8(2, 2);
          }));

          opts.push(FrameY.create(f -> {
            f.index = valueIndex + 1;
          }));
        })
    );
  }

  @DataProvider
  public Object[][] testCase04Provider() {
    return new Object[][] {
        {HtmlBytes.eAMBIGUOUS88, IntSize.BIT8, IntSize.BIT8},
        {HtmlBytes.eAMBIGUOUS816, IntSize.BIT8, IntSize.BIT16},
        {HtmlBytes.eAMBIGUOUS824, IntSize.BIT8, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "testCase04Provider", description = """
  element:
  - element
  - ambiguous element
  """)
  public void testCase04(byte proto, IntSize nameSize, IntSize valueSize) {
    final int frameIndex = 1, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(proto);
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      }));
    });

    final ScanElementNodeState subject;
    subject = new ScanElementNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new RenderAmbiguousElementStartTagState(ctx, nameSize, valueSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.DIV);

            f.values8(2, 2);
          }));

          opts.push(FrameY.create(f -> {
            f.index = valueIndex + 1;
          }));
        })
    );
  }

}