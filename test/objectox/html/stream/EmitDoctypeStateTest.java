/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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

import objectox.html.markup.HtmlBytes;
import org.testng.annotations.Test;

public class EmitDoctypeStateTest {

  @Test(description = """
  - doctype event
  """)
  public void compute() {
    final Ctx ctx;
    ctx = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.DOCTYPE);

      opts.push(f -> {
        f.index = 1;
      });
    });

    final EmitDoctypeState subject;
    subject = new EmitDoctypeState(ctx);

    final State res;
    res = subject.compute();

    assertEquals(res, new ScanDocumentNodeState(ctx));

    assertEquals(
        ctx,

        CtxY.create(opts -> {
          opts.sink(ctx);

          opts.result(new EmitDoctypeEvent(0));

          opts.push(f -> {
            f.index = 1;
          });
        })
    );
  }

}
