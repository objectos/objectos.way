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
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import org.testng.annotations.Test;

public class ElementInternalRecorderTest {

  @Test(description = "record ATTRIBUTE0")
  public final void record01() {
    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.ATTRIBUTE0,

        HtmlBytes.encodeInt0(AttributeName.READONLY.index()),

        HtmlByteProto.INTERNAL3
    );

    final ElementInternalRecorder subject;
    subject = new ElementInternalRecorder(aux, main);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents), 0);

    assertEquals(
        aux,

        ByteArray.of(HtmlByteProto.INTERNAL)
    );

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.ATTRIBUTE0,

            HtmlBytes.encodeInt0(AttributeName.READONLY.index()),

            HtmlByteProto.INTERNAL3
        )
    );
  }

  @Test(description = "record CUSTOM_ATTR0")
  public final void record02() {
    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.CUSTOM_ATTR0,

        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),

        HtmlByteProto.INTERNAL4
    );

    final ElementInternalRecorder subject;
    subject = new ElementInternalRecorder(aux, main);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents), 0);

    assertEquals(
        aux,

        ByteArray.of(HtmlByteProto.INTERNAL)
    );

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.CUSTOM_ATTR0,

            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),

            HtmlByteProto.INTERNAL4
        )
    );
  }

  @Test(description = "record ATTRIBUTE1")
  public final void record03() {
    final AttributeName name;
    name = AttributeName.ID;

    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.ATTRIBUTE1,

        HtmlBytes.encodeInt0(name.index()),

        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),

        HtmlByteProto.INTERNAL5
    );

    final ElementInternalRecorder subject;
    subject = new ElementInternalRecorder(aux, main);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents), 0);

    assertEquals(
        aux,

        ByteArray.of(HtmlByteProto.INTERNAL)
    );

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.ATTRIBUTE1,

            HtmlBytes.encodeInt0(name.index()),

            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),

            HtmlByteProto.INTERNAL5
        )
    );
  }

  @Test(description = "record CUSTOM_ATTR1")
  public final void record04() {
    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.CUSTOM_ATTR1,

        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),

        HtmlBytes.encodeInt0(1),
        HtmlBytes.encodeInt1(1),

        HtmlByteProto.INTERNAL6
    );

    final ElementInternalRecorder subject;
    subject = new ElementInternalRecorder(aux, main);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents), 0);

    assertEquals(
        aux,

        ByteArray.of(HtmlByteProto.INTERNAL)
    );

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.CUSTOM_ATTR1,

            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),

            HtmlBytes.encodeInt0(1),
            HtmlBytes.encodeInt1(1),

            HtmlByteProto.INTERNAL6
        )
    );
  }

  @Test(description = "record TEXT")
  public final void record05() {
    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.TEXT,
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL4
    );

    final ElementInternalRecorder subject;
    subject = new ElementInternalRecorder(aux, main);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents), 0);

    assertEquals(
        aux,

        ByteArray.of(HtmlByteProto.INTERNAL)
    );

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.TEXT,
            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),
            HtmlByteProto.INTERNAL4
        )
    );
  }

  @Test(description = "record RAW")
  public final void record06() {
    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.RAW,
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlByteProto.INTERNAL4
    );

    final ElementInternalRecorder subject;
    subject = new ElementInternalRecorder(aux, main);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents), 0);

    assertEquals(
        aux,

        ByteArray.of(HtmlByteProto.INTERNAL)
    );

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.RAW,
            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),
            HtmlByteProto.INTERNAL4
        )
    );
  }

}
