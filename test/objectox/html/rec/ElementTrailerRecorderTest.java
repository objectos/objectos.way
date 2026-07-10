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
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class ElementTrailerRecorderTest {

  @Test
  public void record01() {
    final ByteArray main;
    main = ByteArray.of(
        HtmlByteProto.ELEMENT,
        HtmlByteProto.NULL,
        HtmlByteProto.NULL,
        HtmlByteProto.STANDARD_NAME,
        ElementNamePojo.DIV.index()
    );

    final ElementTrailerRecorder subject;
    subject = new ElementTrailerRecorder(main);

    final int startIndex;
    startIndex = 0;

    final int contentsIndex;
    contentsIndex = 0;

    subject.record(startIndex, contentsIndex);

    assertEquals(
        main,

        ByteArray.of(
            HtmlByteProto.ELEMENT,
            HtmlBytes.encodeInt0(5),
            HtmlBytes.encodeInt1(5),
            HtmlByteProto.STANDARD_NAME,
            ElementNamePojo.DIV.index(),

            HtmlByteProto.END,
            HtmlBytes.encodeInt0(5),
            HtmlByteProto.INTERNAL
        )
    );
  }

}
