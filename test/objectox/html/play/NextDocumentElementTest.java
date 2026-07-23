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

import objectox.html.HtmlByteProto;
import objectox.html.elem.ElementNamePojo;
import objectox.html.rec.HtmlBytes;
import org.testng.annotations.Test;

public class NextDocumentElementTest {

  @Test
  public void compute01() {
    final Tape tape;
    tape = TapeY.create(opts -> {
      opts.main(
          HtmlByteProto.ELEMENT,
          HtmlBytes.encodeInt0(5),
          HtmlBytes.encodeInt1(5),
          HtmlByteProto.STANDARD_NAME,
          (byte) ElementNamePojo.HTML.index(),
          HtmlByteProto.END,
          HtmlBytes.encodeInt0(5),
          HtmlByteProto.INTERNAL
      );

      opts.mainIndex = 1;
    });

    final NextDocumentElement subject;
    subject = new NextDocumentElement(tape);

    final BeginStartTagState res;
    res = subject.compute();

    assertEquals(res.hasNext(), true);
    assertEquals(res.name(), "html");
    assertEquals(tape.nextByte(), HtmlByteProto.END);
    assertEquals(tape.pop(), FrameKind.ELEMENT_NODES);
    assertEquals(tape.nextByte(), HtmlByteProto.END);
    assertEquals(tape.pop(), FrameKind.DOC_ELEMENT);
    assertEquals(tape.hasByte(), false);
  }

}
