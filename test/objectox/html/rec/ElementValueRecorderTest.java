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
import objectos.html.AttributeObject;
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.HtmlInstruction;
import objectox.html.ObjectArray;
import objectox.html.attr.AttributeObjectPojo;
import org.testng.annotations.Test;

public class ElementValueRecorderTest {

  private ElementValueRecorder create(ByteArray aux, ByteArray main, ObjectArray objects) {
    final AttributeObjectRecorder attributeObjectRecorder;
    attributeObjectRecorder = new AttributeObjectRecorder(aux, objects);

    final ElementInternalRecorder instructionRecorder;
    instructionRecorder = new ElementInternalRecorder(aux, main);

    return new ElementValueRecorder(attributeObjectRecorder, instructionRecorder);
  }

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

    final ObjectArray objects;
    objects = ObjectArray.of();

    final ElementValueRecorder subject;
    subject = create(aux, main, objects);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents, HtmlInstruction.ATTRIBUTE), 0);

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

    assertEquals(
        objects,

        ObjectArray.of()
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

    final AttributeName name;
    name = AttributeName.of("foo");

    final ObjectArray objects;
    objects = ObjectArray.of(name);

    final ElementValueRecorder subject;
    subject = create(aux, main, objects);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents, HtmlInstruction.ATTRIBUTE), 0);

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

    assertEquals(
        objects,

        ObjectArray.of(name)
    );
  }

  @Test(description = "record ATTRIBUTE1")
  public final void record03() {
    final AttributeName name;
    name = AttributeName.ID;

    final Object value;
    value = "foo";

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

    final ObjectArray objects;
    objects = ObjectArray.of(value);

    final ElementValueRecorder subject;
    subject = create(aux, main, objects);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents, HtmlInstruction.ATTRIBUTE), 0);

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

    assertEquals(
        objects,

        ObjectArray.of(value)
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

    final AttributeName name;
    name = AttributeName.of("foo");

    final Object value;
    value = "bar";

    final ObjectArray objects;
    objects = ObjectArray.of(name, value);

    final ElementValueRecorder subject;
    subject = create(aux, main, objects);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents, HtmlInstruction.ATTRIBUTE), 0);

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

    assertEquals(
        objects,

        ObjectArray.of(name, value)
    );
  }

  @Test(description = "record ATTRIBUTE_EXT0")
  public void record05() {
    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of();

    final ObjectArray objects;
    objects = ObjectArray.of();

    final AttributeName name;
    name = AttributeName.XMLNS;

    final String value;
    value = null;

    final AttributeObject attr;
    attr = new AttributeObjectPojo(name, value);

    final ElementValueRecorder subject;
    subject = create(aux, main, objects);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents, attr), 0);

    assertEquals(
        aux,

        ByteArray.of(
            HtmlByteProto.ATTRIBUTE_EXT0,

            HtmlBytes.encodeInt0(name.index())
        )
    );

    assertEquals(
        main,

        ByteArray.of()
    );

    assertEquals(
        objects,

        ObjectArray.of()
    );
  }

  @Test(description = "record ATTRIBUTE_EXT1")
  public void record06() {
    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of();

    final ObjectArray objects;
    objects = ObjectArray.of();

    final AttributeName name;
    name = AttributeName.XMLNS;

    final String value;
    value = "abc";

    final AttributeObject attr;
    attr = new AttributeObjectPojo(name, value);

    final ElementValueRecorder subject;
    subject = create(aux, main, objects);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents, attr), 0);

    assertEquals(
        aux,

        ByteArray.of(
            HtmlByteProto.ATTRIBUTE_EXT1,

            HtmlBytes.encodeInt0(name.index()),

            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0)
        )
    );

    assertEquals(
        main,

        ByteArray.of()
    );

    assertEquals(
        objects,

        ObjectArray.of(value)
    );
  }

  @Test(description = "record NOOP")
  public void record07() {
    final ByteArray aux;
    aux = ByteArray.of();

    final ByteArray main;
    main = ByteArray.of();

    final ObjectArray objects;
    objects = ObjectArray.of();

    final ElementValueRecorder subject;
    subject = create(aux, main, objects);

    final int mainContents;
    mainContents = main.size();

    assertEquals(subject.record(mainContents, HtmlInstruction.NOOP), 0);

    assertEquals(
        aux,

        ByteArray.of()
    );

    assertEquals(
        main,

        ByteArray.of()
    );

    assertEquals(
        objects,

        ObjectArray.of()
    );
  }

}
