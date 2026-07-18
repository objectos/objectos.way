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

import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.elem.ElementNamePojo;

final class PlayY {

  private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  // TC00: empty document
  public static final byte[] T00_MAIN = {};
  public static final Object[] T00_OBJECTS = EMPTY_OBJECT_ARRAY;

  // TC01: <html></html>
  public static final byte[] T01_MAIN = {
      HtmlByteProto.ELEMENT,
      HtmlBytes.encodeInt0(5),
      HtmlBytes.encodeInt1(5),
      HtmlByteProto.STANDARD_NAME,
      (byte) ElementNamePojo.HTML.index(),
      HtmlByteProto.END,
      HtmlBytes.encodeInt0(5),
      HtmlByteProto.INTERNAL
  };
  public static final Object[] T01_OBJECTS = EMPTY_OBJECT_ARRAY;

  private PlayY() {}

}
