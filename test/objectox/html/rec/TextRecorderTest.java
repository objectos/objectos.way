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

import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.ObjectArray;
import org.testng.annotations.Test;

public class TextRecorderTest {

  @Test(
      description = "reject null value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void record01() {
    final ByteArray main;
    main = ByteArray.of();

    final ObjectArray objects;
    objects = ObjectArray.of();

    final TextRecorder subject;
    subject = new TextRecorder(main, objects);

    subject.record(null);
  }

  @Test
  public void record02() {
    final ByteArray main;
    main = ByteArray.of();

    final ObjectArray objects;
    objects = ObjectArray.of();

    final TextRecorder subject;
    subject = new TextRecorder(main, objects);

    subject.record("Ipsum");

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.TEXT,
            HtmlBytes.encodeInt0(0),
            HtmlBytes.encodeInt1(0),
            HtmlByteProto.INTERNAL4
        )
    );

    assertEquals(
        objects,

        ObjectArray.of("Ipsum")
    );
  }

}
