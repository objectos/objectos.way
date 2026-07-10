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

public class Attribute0RecorderTest {

  @Test
  public void record01() {
    final ByteArray main;
    main = new ByteArray(0);

    final ObjectArray objects;
    objects = new ObjectArray();

    final AttributeName name;
    name = AttributeName.READONLY;

    final Attribute0Recorder subject;
    subject = new Attribute0Recorder(main, objects, name);

    assertEquals(subject.record(), HtmlInstruction.ATTRIBUTE);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.ATTRIBUTE0,

            HtmlBytes.encodeInt0(name.index()),

            HtmlByteProto.INTERNAL3
        )
    );

    assertEquals(
        objects,

        ObjectArray.of()
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

    final Attribute0Recorder subject;
    subject = new Attribute0Recorder(main, objects, name);

    assertEquals(subject.record(), HtmlInstruction.ATTRIBUTE);

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

}
