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

public class NextElementTest {

  @Test(description = """
  <html></html>
  """)
  public void endIndex01() {
    final HtmlSource source;
    source = HtmlSourceY.create(opts -> {
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
      opts.addByte(HtmlBytes.VALUES0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
    });

    final NextElement subject;
    subject = new NextElement(source, 0, IntSize.BIT8);

    final int endIndex;
    endIndex = subject.endIndex();

    assertEquals(endIndex, 5);
  }

  @Test(description = """
  <html autofocus></html>
  """)
  public void endIndex02() {
    final HtmlSource source;
    source = HtmlSourceY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);

      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
    });

    final NextElement subject;
    subject = new NextElement(source, 1, IntSize.BIT8);

    final int endIndex;
    endIndex = subject.endIndex();

    assertEquals(endIndex, 8);
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void endIndex03() {
    final HtmlSource source;
    source = HtmlSourceY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);

      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HTML.index());
    });

    final NextElement subject;
    subject = new NextElement(source, 1, IntSize.BIT8);

    final int endIndex;
    endIndex = subject.endIndex();

    assertEquals(endIndex, 8);
  }

}
