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
import objectox.html.Ambiguous;
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import org.testng.annotations.Test;

public class ElementInternalEncoderTest {

  @Test(description = "encode ATTRIBUTE0")
  public final void encode01() {
    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.ATTRIBUTE0,

        HtmlBytes.encodeInt0(AttributeName.READONLY.index()),

        HtmlByteProto.INTERNAL3
    );

    final ElementInternalEncoder subject;
    subject = new ElementInternalEncoder(main);

    assertEquals(subject.encode(0), 3);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.MARKED3,

            HtmlBytes.encodeInt0(AttributeName.READONLY.index()),

            HtmlByteProto.INTERNAL3,

            HtmlByteProto.ATTRIBUTE0,

            HtmlBytes.encodeInt0(4)
        )
    );
  }

  @Test(description = "encode CUSTOM_ATTR0")
  public final void encode02() {
    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.CUSTOM_ATTR0,

        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),

        HtmlByteProto.INTERNAL4
    );

    final ElementInternalEncoder subject;
    subject = new ElementInternalEncoder(main);

    assertEquals(subject.encode(0), 4);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.MARKED4,

            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),

            HtmlByteProto.INTERNAL4,

            HtmlByteProto.CUSTOM_ATTR0,

            HtmlBytes.encodeInt0(5)
        )
    );
  }

  @Test(description = "encode ATTRIBUTE1")
  public final void encode03() {
    final AttributeName name;
    name = AttributeName.ID;

    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.ATTRIBUTE1,

        HtmlBytes.encodeInt0(name.index()),

        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),

        HtmlByteProto.INTERNAL5
    );

    final ElementInternalEncoder subject;
    subject = new ElementInternalEncoder(main);

    assertEquals(subject.encode(0), 5);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.MARKED5,

            HtmlBytes.encodeInt0(name.index()),

            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),

            HtmlByteProto.INTERNAL5,

            HtmlByteProto.ATTRIBUTE1,

            HtmlBytes.encodeInt0(6)
        )
    );
  }

  @Test(description = "encode CUSTOM_ATTR1")
  public final void encode04() {
    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.CUSTOM_ATTR1,

        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),

        HtmlBytes.encodeInt0(1),
        HtmlBytes.encodeInt1(1),

        HtmlByteProto.INTERNAL6
    );

    final ElementInternalEncoder subject;
    subject = new ElementInternalEncoder(main);

    assertEquals(subject.encode(0), 6);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.MARKED6,

            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),

            HtmlBytes.encodeInt0(1),
            HtmlBytes.encodeInt1(1),

            HtmlByteProto.INTERNAL6,

            HtmlByteProto.CUSTOM_ATTR1,

            HtmlBytes.encodeInt0(7)
        )
    );
  }

  @Test(description = "encode TEXT")
  public final void encode05() {
    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.TEXT,
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL4
    );

    final ElementInternalEncoder subject;
    subject = new ElementInternalEncoder(main);

    assertEquals(subject.encode(0), 4);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.MARKED4,
            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),
            HtmlByteProto.INTERNAL4,

            HtmlByteProto.TEXT,
            HtmlBytes.encodeInt0(5)
        )
    );
  }

  @Test(description = "encode RAW")
  public final void encode06() {
    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.RAW,
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL4
    );

    final ElementInternalEncoder subject;
    subject = new ElementInternalEncoder(main);

    assertEquals(subject.encode(0), 4);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.MARKED4,
            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),
            HtmlByteProto.INTERNAL4,

            HtmlByteProto.RAW,
            HtmlBytes.encodeInt0(5)
        )
    );
  }

  @Test(description = "encode AMBIGUOUS1")
  public final void encode07() {
    final Ambiguous name;
    name = Ambiguous.TITLE;

    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.AMBIGUOUS1,
        HtmlBytes.encodeInt0(name.ordinal()),
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL5
    );

    final ElementInternalEncoder subject;
    subject = new ElementInternalEncoder(main);

    assertEquals(subject.encode(0), 5);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.MARKED5,
            HtmlBytes.encodeInt0(name.ordinal()),
            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),
            HtmlByteProto.INTERNAL5,

            HtmlByteProto.AMBIGUOUS1,
            HtmlBytes.encodeInt0(6)
        )
    );
  }

}
