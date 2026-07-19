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

import objectos.html.ElementName;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class EndTagStateTest {

  @Test
  public void compute01() {
    final Tape tape;
    tape = TapeY.create(opts -> {
      opts.main(
          HtmlByteProto.ROOT_ELEMENT,
          HtmlBytes.encodeInt0(5),
          HtmlBytes.encodeInt1(5),
          HtmlByteProto.STANDARD_NAME,
          (byte) ElementNamePojo.HTML.index(),
          HtmlByteProto.END
      );

      opts.stack(0);
    });

    final EndTagState subject;
    subject = new EndTagState(tape, ElementName.HTML);

    final State res;
    res = subject.compute();

    assertEquals(res, EndDocumentState.INSTANCE);
  }

}
