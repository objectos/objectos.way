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

public class SkipVoidElementTest {

  @Test(description = """
  void elem:
  - no attrs
  """)
  public void voidElement01() {
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

      opts.push(new Frame(0));
    });

    final SkipVoidElement subject;
    subject = new SkipVoidElement(ctx, IntSize.BIT8);

    subject.execute();

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.push(new Frame(4));
        })
    );
  }

}
