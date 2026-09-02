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
import static org.testng.Assert.assertSame;
import java.util.function.Consumer;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.AmbiguousName;
import objectox.html.IntSize;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ScanDocumentNodeStateTest {

  @Test(description = """
  empty document
  """)
  public void testCase00() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new StopState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.push(new Frame(0));
        })
    );
  }

  @Test(description = """
  - doctype
  """)
  public void testCase01() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      opts.addByte(HtmlBytes.DOCTYPE);
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertSame(res, subject);

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.result(RenderDoctypeEvent.of());
          opts.push(new Frame(1));
        })
    );
  }

  @DataProvider
  public Object[][] testCase02Provider() {
    return new Object[][] {
        {HtmlBytes.ELEMSTR8, ElementName.DIV, ElementKind.REGULAR, IntSize.BIT8},
        {HtmlBytes.ELEMVOID8, ElementName.HR, ElementKind.VOID, IntSize.BIT8}
    };
  }

  @Test(dataProvider = "testCase02Provider", description = """
  StartState -> BeginStartTagState
  """)
  public void testCase02(byte proto, ElementName elementName, ElementKind elementKind, IntSize nameSize) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      opts.addByte(proto);
      opts.addInt8(elementName.index());
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new BeginStartTagState(ctx, elementKind, nameSize));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new Frame(1));
        })
    );
  }

  @DataProvider
  public Object[][] testCase03Provider() {
    final JsAction action;
    action = Js.noop();

    return new Object[][] {
        {ctx(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CONTENTEDITABLE.index());
          opts.addInt8(opts.addObject("false"));
        }), 4, "attribute 8/8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xATTRIBUTE816);
          opts.addInt8(AttributeName.CONTENTEDITABLE.index());
          opts.addInt16(opts.addObject("false"));
        }), 5, "attribute 8/16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xATTRIBUTE824);
          opts.addInt8(AttributeName.CONTENTEDITABLE.index());
          opts.addInt24(opts.addObject("false"));
        }), 6, "attribute 8/24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xBOOLEAN8);
          opts.addInt8(AttributeName.AUTOFOCUS.index());
        }), 3, "boolean 8"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xJSACTION88);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt8(opts.addObject(action));
        }), 4, "jsaction 8/8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xJSACTION816);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt16(opts.addObject(action));
        }), 5, "jsaction 8/16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xJSACTION824);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt24(opts.addObject(action));
        }), 6, "jsaction 8/24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.aAMBIGUOUS88);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt8(opts.addObject("foo"));
        }), 4, "ambiguous attribute 8/8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.aAMBIGUOUS816);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt16(opts.addObject("foo"));
        }), 5, "ambiguous attribute 8/16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.aAMBIGUOUS824);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt24(opts.addObject("foo"));
        }), 6, "ambiguous attribute 8/24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.eAMBIGUOUS88);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt8(opts.addObject("foo"));
        }), 4, "ambiguous element 8/8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.eAMBIGUOUS816);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt16(opts.addObject("foo"));
        }), 5, "ambiguous element 8/16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.eAMBIGUOUS824);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt24(opts.addObject("foo"));
        }), 6, "ambiguous element 8/24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(ElementName.SPAN.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }), 5, "consumed element regular"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xELEMVOID8);
          opts.addInt8(ElementName.HR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }), 5, "consumed element void"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(opts.addObject("Ipsum"));
        }), 3, "text 8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xTEXT16);
          opts.addInt16(opts.addObject("Ipsum"));
        }), 4, "text 16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xTEXT24);
          opts.addInt24(opts.addObject("Ipsum"));
        }), 5, "text 24"},

        {ctx(opts -> {
          opts.addByte(HtmlBytes.xRAW8);
          opts.addInt8(opts.addObject("Ipsum"));
        }), 3, "raw 8"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);
          opts.addByte(HtmlBytes.xRAW16);
          opts.addInt16(opts.addObject("Ipsum"));
        }), 4, "raw 16"},

        {ctx(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);
          opts.addByte(HtmlBytes.xRAW24);
          opts.addInt24(opts.addObject("Ipsum"));
        }), 5, "raw 24"}
    };
  }

  @Test(dataProvider = "testCase03Provider", description = """
  Skip to element start
  """)
  public void testCase03(Consumer<CtxY> prefix, int expectedIndex, String description) {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.push(new Frame(0));

      prefix.accept(opts);

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    final ScanDocumentNodeState subject;
    subject = new ScanDocumentNodeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new BeginStartTagState(ctx, ElementKind.REGULAR, IntSize.BIT8));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);
          opts.push(new Frame(expectedIndex));
        })
    );
  }

  @Test(description = """
  factory:
  - pushes a scan document node frame
  """)
  public void of() {
    final Ctx ctx;
    ctx = CtxY.create(_ -> {});

    final ScanDocumentNodeState res;
    res = ScanDocumentNodeState.of(ctx);

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.push(new Frame(0));
        })
    );
  }

  private Consumer<? super CtxY> ctx(Consumer<? super CtxY> opts) {
    return opts;
  }

}
