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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;

import objectos.html.rec.Fragment0;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.HtmlInstruction;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class FragmentRecorderTest {

  private FragmentRecorder create(ByteArray main) {
    return new FragmentRecorder(
        main,

        new ForwardOffsetRecorder(main),

        new ReverseOffsetRecorder(main)
    );
  }

  @Test
  public void record01() {
    final ByteArray main;
    main = ByteArray.of();

    final FragmentRecorder subject;
    subject = create(main);

    final Fragment0 f;
    f = () -> {

    };

    assertEquals(subject.record(f), HtmlInstruction.FRAGMENT);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.FRAGMENT,
            HtmlBytes.encodeInt0(3),
            HtmlBytes.encodeInt1(3),
            HtmlBytes.encodeInt2(3),
            HtmlByteProto.END,
            HtmlBytes.encodeInt0(4),
            HtmlByteProto.INTERNAL
        )
    );
  }

  @Test
  public void record02() {
    final ByteArray main;
    main = ByteArray.of();

    final FragmentRecorder subject;
    subject = create(main);

    final Fragment0 f;
    f = () -> {
      main.add(HtmlByteProto.ELEMENT);
      main.addInt16(5);
      main.add(HtmlByteProto.STANDARD_NAME);
      main.addInt8(ElementNamePojo.HTML.index());

      main.add(HtmlByteProto.END);
      main.addVarIntLE(5);
      main.add(HtmlByteProto.INTERNAL);
    };

    assertEquals(subject.record(f), HtmlInstruction.FRAGMENT);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.FRAGMENT,
            HtmlBytes.encodeInt0(3 + 8),
            HtmlBytes.encodeInt1(3 + 8),
            HtmlBytes.encodeInt2(3 + 8),

            HtmlByteProto.ELEMENT,
            HtmlBytes.encodeInt0(5),
            HtmlBytes.encodeInt1(5),
            HtmlByteProto.STANDARD_NAME,
            (byte) ElementNamePojo.HTML.index(),
            HtmlByteProto.END,
            HtmlBytes.encodeInt0(5),
            HtmlByteProto.INTERNAL,

            HtmlByteProto.END,
            HtmlBytes.encodeInt0(4 + 8),
            HtmlByteProto.INTERNAL
        )
    );
  }

}
