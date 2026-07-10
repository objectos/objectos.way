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
import objectox.html.HtmlInstruction;
import objectox.html.ObjectArray;
import org.testng.annotations.Test;

public class Attribute1RecorderTest {

  @Test
  public void record01() {
    final ByteArray main;
    main = new ByteArray(0);

    final ObjectArray objects;
    objects = new ObjectArray();

    final AttributeName name;
    name = AttributeName.ID;

    final Object value;
    value = "foo";

    final Attribute1Recorder subject;
    subject = new Attribute1Recorder(main, objects, name, value);

    assertEquals(subject.record(), HtmlInstruction.ATTRIBUTE);

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

  @Test
  public void record02() {
    final ByteArray main;
    main = new ByteArray(0);

    final ObjectArray objects;
    objects = new ObjectArray();

    final AttributeName name;
    name = AttributeName.of("foo");

    final Object value;
    value = "bar";

    final Attribute1Recorder subject;
    subject = new Attribute1Recorder(main, objects, name, value);

    assertEquals(subject.record(), HtmlInstruction.ATTRIBUTE);

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

  @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "value == null")
  public void record03() {
    final ByteArray main;
    main = new ByteArray(0);

    final ObjectArray objects;
    objects = new ObjectArray();

    final AttributeName name;
    name = AttributeName.of("foo");

    final Object value;
    value = null;

    new Attribute1Recorder(main, objects, name, value);
  }

}
