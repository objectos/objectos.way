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

import objectos.html.AttributeName;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import org.testng.annotations.Test;

public class FlattenEncoderTest {

  @Test
  public void encode01() {
    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.FLATTEN,
        HtmlBytes.encodeInt0(3),
        HtmlBytes.encodeInt1(3),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(3),
        HtmlByteProto.INTERNAL
    );

    final FlattenEncoder subject;
    subject = new FlattenEncoder(main);

    assertEquals(subject.encode(0), 6);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.LENGTH2,
            HtmlBytes.encodeInt0(3),
            HtmlBytes.encodeInt1(3),
            HtmlByteProto.END,
            HtmlBytes.encodeInt0(3),
            HtmlByteProto.INTERNAL
        )
    );
  }

  @Test
  public void encode02() {
    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.MARKED3,
        HtmlBytes.encodeInt0(AttributeName.READONLY.index()),
        HtmlByteProto.INTERNAL3,
        HtmlByteProto.FLATTEN,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.ATTRIBUTE0,
        HtmlBytes.encodeInt0(7),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(8),
        HtmlByteProto.INTERNAL
    );

    final FlattenEncoder subject;
    subject = new FlattenEncoder(main);

    assertEquals(subject.encode(3), 11);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.MARKED3,
            HtmlBytes.encodeInt0(AttributeName.READONLY.index()),
            HtmlByteProto.INTERNAL3,
            HtmlByteProto.LENGTH2,
            HtmlBytes.encodeInt0(5),
            HtmlBytes.encodeInt1(5),
            HtmlByteProto.ATTRIBUTE0,
            HtmlBytes.encodeInt0(7),
            HtmlByteProto.END,
            HtmlBytes.encodeInt0(8),
            HtmlByteProto.INTERNAL,
            HtmlByteProto.ATTRIBUTE0,
            HtmlBytes.encodeInt0(12)
        )
    );
  }

}
