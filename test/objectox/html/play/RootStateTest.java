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
package objectox.html.play;

import static org.testng.Assert.assertEquals;

import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.ObjectArray;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class RootStateTest {

  @Test
  public void toEndState() {
    final BytePlayer main;
    main = BytePlayer.of();

    final ObjectArray objects;
    objects = ObjectArray.of();

    final RootState subject;
    subject = new RootState(main, objects);

    final State res;
    res = subject.compute();

    assertEquals(res, EndState.INSTANCE);
  }

  @Test(description = "<html></html>")
  public void toStartTag01() {
    final BytePlayer main;
    main = BytePlayer.of(
        HtmlByteProto.ELEMENT,
        HtmlBytes.encodeInt0(5),
        HtmlBytes.encodeInt1(5),
        HtmlByteProto.STANDARD_NAME,
        ElementNamePojo.HTML.index(),
        HtmlByteProto.END,
        HtmlBytes.encodeInt0(5),
        HtmlByteProto.INTERNAL
    );

    final ObjectArray objects;
    objects = ObjectArray.of();

    final RootState subject;
    subject = new RootState(main, objects);

    final State res;
    res = subject.compute();

    final StartTagState state;
    state = (StartTagState) res;

    assertEquals(state.name(), "html");
    assertEquals(main.next(), HtmlByteProto.END);
  }

}
