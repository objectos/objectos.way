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
import objectox.html.HtmlInstruction;
import org.testng.annotations.Test;

public class FlattenRecorderTest {

  private FlattenRecorder create(ByteArray aux, ByteArray main, ObjectArray objects) {
    return new FlattenRecorder(
        main,

        new ElementValueEncoder(
            aux,

            main,

            new Encoder(
                main,

                new FlattenEncoder(main),

                new ElementEncoder(main)
            )
        ),

        new ElementValueRecorder(
            new AttributeObjectRecorder(aux, objects),

            new ElementInternalRecorder(aux, main)
        ),

        new ForwardOffsetRecorder(main),

        new ReverseOffsetRecorder(main)
    );
  }

  @Test
  public void record01() {
    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of();

    final ObjectArray objects;
    objects = ObjectArray.of();

    final FlattenRecorder subject;
    subject = create(aux, main, objects);

    assertEquals(subject.record(), HtmlInstruction.ELEMENT);

    assertEquals(
        aux,

        ByteArray.of()
    );

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.FLATTEN,
            HtmlBytes.encodeInt0(3),
            HtmlBytes.encodeInt1(3),
            HtmlByteProto.END,
            HtmlBytes.encodeInt0(3),
            HtmlByteProto.INTERNAL
        )
    );

    assertEquals(
        objects,

        ObjectArray.of()
    );
  }

  @Test
  public void record02() {
    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.ATTRIBUTE0,
        HtmlBytes.encodeInt0(AttributeName.READONLY.index()),
        HtmlByteProto.INTERNAL3
    );

    final ObjectArray objects;
    objects = ObjectArray.of();

    final FlattenRecorder subject;
    subject = create(aux, main, objects);

    assertEquals(subject.record(HtmlInstruction.ATTRIBUTE), HtmlInstruction.ELEMENT);

    assertEquals(
        aux,

        ByteArray.of(HtmlByteProto.INTERNAL)
    );

    assertEquals(
        main,

        ByteArray.of(
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
        )
    );

    assertEquals(
        objects,

        ObjectArray.of()
    );
  }

}
