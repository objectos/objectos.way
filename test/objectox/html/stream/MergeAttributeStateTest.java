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

import objectox.html.AmbiguousName;
import objectox.html.IntSize;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.Test;

public class MergeAttributeStateTest {

  @Test(description = """
  single value
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

    final MergeAttributeState subject;
    subject = new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.CLASS, "c1");

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = 0;

            f.regular(ElementName.DIV);

            f.values8(1, 1);
          }));

          opts.result(RenderAttributeEvent.of(AttributeName.CLASS, "c1"));
        })
    );
  }

  @Test(description = """
  another attribute but with different name
  """)
  public void testCase02() {
    final int frameIndex = 3, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.HREF.index());
      opts.addInt8(opts.addObject("#"));
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      }));
    });

    final MergeAttributeState subject;
    subject = new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.CLASS, "c1");

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex;

            f.regular(ElementName.DIV);

            f.values8(1, 2);
          }));

          opts.result(RenderAttributeEvent.of(AttributeName.CLASS, "c1"));
        })
    );
  }

  @Test(description = """
  another attribute but with same name
  """)
  public void testCase03() {
    final int frameIndex = 3, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c2"));
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      }));
    });

    final MergeAttributeState subject;
    subject = new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.CLASS, "c1");

    final State res;
    res = subject.compute();

    assertEquals(res, new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.CLASS, "c1 c2"));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.DIV);

            f.values8(2, 2);
          }));
        })
    );
  }

  @Test(description = """
  ambiguous attribute but with different name
  """)
  public void testCase04() {
    final int frameIndex = 3, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.aAMBIGUOUS88);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addInt8(opts.addObject("#"));
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      }));
    });

    final MergeAttributeState subject;
    subject = new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.STYLE, "s1");

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanElementAttributeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex;

            f.regular(ElementName.DIV);

            f.values8(1, 2);
          }));

          opts.result(RenderAttributeEvent.of(AttributeName.STYLE, "s1"));
        })
    );
  }

  @Test(description = """
  ambiguous attribute but with same name
  """)
  public void testCase05() {
    final int frameIndex = 3, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.aAMBIGUOUS88);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addInt8(opts.addObject("t2"));
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      }));
    });

    final MergeAttributeState subject;
    subject = new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.TITLE, "t1");

    final State res;
    res = subject.compute();

    assertEquals(res, new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.TITLE, "t1 t2"));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.DIV);

            f.values8(2, 2);
          }));
        })
    );
  }

  @Test(description = """
  ambiguous attribute 8/16 but with same name
  """)
  public void testCase06() {
    final int frameIndex = 4, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
      opts.addByte(HtmlBytes.aAMBIGUOUS816);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addInt16(opts.addObject("t2"));
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      }));
    });

    final MergeAttributeState subject;
    subject = new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.TITLE, "t1");

    final State res;
    res = subject.compute();

    assertEquals(res, new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.TITLE, "t1 t2"));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.DIV);

            f.values8(2, 2);
          }));
        })
    );
  }

  @Test(description = """
  ambiguous attribute 8/24 but with same name
  """)
  public void testCase07() {
    final int frameIndex = 5, valueIndex = 0;

    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
      opts.addByte(HtmlBytes.aAMBIGUOUS824);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addInt24(opts.addObject("t2"));
      opts.addInt8(valueIndex);

      opts.push(FrameY.create(f -> {
        f.index = frameIndex;

        f.regular(ElementName.DIV);

        f.values8(1, 2);
      }));
    });

    final MergeAttributeState subject;
    subject = new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.TITLE, "t1");

    final State res;
    res = subject.compute();

    assertEquals(res, new MergeAttributeState(ctx, IntSize.BIT8, AttributeName.TITLE, "t1 t2"));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(FrameY.create(f -> {
            f.index = frameIndex + 1;

            f.regular(ElementName.DIV);

            f.values8(2, 2);
          }));
        })
    );
  }

}
