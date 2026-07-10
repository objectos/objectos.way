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
import objectox.html.ObjectArray;
import objectox.html.attr.AttributeObjectPojo;
import org.testng.annotations.Test;

public class AttributeObjectRecorderTest {

  @Test(
      description = "reject null attribute name",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "Attribute object provided a null attribute name: AttributeObjectPojo\\[attrName=null, attrValue=\\]")
  public void record01() {
    final ByteArray aux;
    aux = new ByteArray(0);

    final ObjectArray objects;
    objects = new ObjectArray();

    final AttributeObject attr;
    attr = new AttributeObjectPojo(null, "");

    final AttributeObjectRecorder subject;
    subject = new AttributeObjectRecorder(aux, objects);

    subject.record(attr);
  }

  @Test
  public void record02() {
    final ByteArray aux;
    aux = new ByteArray(0);

    final ObjectArray objects;
    objects = new ObjectArray();

    final AttributeName name;
    name = AttributeName.XMLNS;

    final String value;
    value = null;

    final AttributeObject attr;
    attr = new AttributeObjectPojo(name, value);

    final AttributeObjectRecorder subject;
    subject = new AttributeObjectRecorder(aux, objects);

    subject.record(attr);

    assertEquals(
        aux,

        ByteArray.of(
            HtmlByteProto.ATTRIBUTE_EXT0,

            HtmlBytes.encodeInt0(name.index())
        )
    );

    assertEquals(
        objects,

        ObjectArray.of()
    );
  }

  @Test
  public void record03() {
    final ByteArray aux;
    aux = new ByteArray(0);

    final ObjectArray objects;
    objects = new ObjectArray();

    final AttributeName name;
    name = AttributeName.XMLNS;

    final String value;
    value = "abc";

    final AttributeObject attr;
    attr = new AttributeObjectPojo(name, value);

    final AttributeObjectRecorder subject;
    subject = new AttributeObjectRecorder(aux, objects);

    subject.record(attr);

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
        objects,

        ObjectArray.of(value)
    );
  }

}
