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
import objectox.html.attr.AttributeNamePojo;
import objectox.html.elem.ElementNamePojo;
import objectox.html.rec.HtmlBytes;
import org.testng.annotations.Test;

public class AttributeStateTest {

  @Test
  public void compute02() {
    final Tape tape;
    tape = TapeY.create(opts -> {
      opts.main(
          HtmlByteProto.MARKED5,
          (byte) AttributeNamePojo.LANG.index(),
          HtmlBytes.encodeInt0(0),
          HtmlBytes.encodeInt1(0),
          HtmlByteProto.INTERNAL5,

          HtmlByteProto.ELEMENT,
          HtmlBytes.encodeInt0(7),
          HtmlBytes.encodeInt1(7),
          HtmlByteProto.STANDARD_NAME,
          (byte) ElementNamePojo.HTML.index(),
          HtmlByteProto.ATTRIBUTE1,
          HtmlBytes.encodeInt0(11),
          HtmlByteProto.END,
          HtmlBytes.encodeInt0(12),
          HtmlByteProto.INTERNAL
      );

      opts.mainIndex = 12;

      opts.frame(FrameKind.ELEMENT_NODES, 10);
    });

    final AttributeState subject;
    subject = new AttributeState(tape, ElementNamePojo.HTML, null);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), EndStartTagState.class);
  }

}
