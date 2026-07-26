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
package objectox.html.play;

import static org.testng.Assert.assertEquals;

import objectox.html.IntSize;
import objectox.html.elem.ElementNamePojo;
import objectox.html.rec.HtmlBytes;
import org.testng.annotations.Test;

public class BeginStartTagStateTest {

  @Test(description = "<html></html>")
  public void compute01() {
    final HtmlSource source;
    source = CtxY.create(opts -> {
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
    });

    final BeginStartTagState subject;
    subject = new BeginStartTagState(
        source,
        Stack.of(6),
        ElementNamePojo.HTML,
        Values.of(4, IntSize.BIT8, 0)
    );

    final EndStartTagState res;
    res = (EndStartTagState) subject.compute();

    assertEquals(res.elementName, ElementNamePojo.HTML);
    assertEquals(res.stack, Stack.of(6));
  }

}
