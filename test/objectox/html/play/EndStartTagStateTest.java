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

public class EndStartTagStateTest {

  @Test(enabled = false)
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

      opts.mainIndex = 5;
    });

    final EndStartTagState subject;
    subject = new EndStartTagState(tape, ElementNamePojo.HTML);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), EndTagState.class);
  }

  @Test(enabled = false)
  public void compute04() {
    final Tape tape;
    tape = TapeY.create(opts -> {
      opts.main(
          HtmlByteProto.LENGTH2,
          HtmlBytes.encodeInt0(5),
          HtmlBytes.encodeInt1(5),
          HtmlByteProto.STANDARD_NAME,
          (byte) ElementNamePojo.HEAD.index(),
          HtmlByteProto.END,
          HtmlBytes.encodeInt0(5),
          HtmlByteProto.INTERNAL,

          HtmlByteProto.ELEMENT,
          HtmlBytes.encodeInt0(7),
          HtmlBytes.encodeInt1(7),
          HtmlByteProto.STANDARD_NAME,
          (byte) ElementNamePojo.HTML.index(),
          HtmlByteProto.ELEMENT,
          HtmlBytes.encodeInt0(14),
          HtmlByteProto.END,
          HtmlBytes.encodeInt0(15),
          HtmlByteProto.INTERNAL
      );

      opts.mainIndex = 13;
    });

    final EndStartTagState subject;
    subject = new EndStartTagState(tape, ElementNamePojo.HTML);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), BeginStartTagState.class);
    assertEquals(tape.nextByte(), HtmlByteProto.END);
    assertEquals(tape.pop(), FrameKind.ELEMENT_NODES);
    assertEquals(tape.nextByte(), HtmlByteProto.END);
    assertEquals(tape.pop(), FrameKind.ELEMENT_CHILD);
    assertEquals(tape.nextByte(), HtmlByteProto.END);
  }

}
