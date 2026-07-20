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

import objectox.html.HtmlBytes;

final class DocumentRecorder {

  final ByteArray main;

  final ObjectArray objects;

  DocumentRecorder(ByteArray main, ObjectArray objects) {
    this.main = main;

    this.objects = objects;
  }

  public static DocumentRecorder create() {
    final ByteArray main;
    main = new ByteArray(256);

    final ObjectArray objects;
    objects = new ObjectArray();

    return new DocumentRecorder(main, objects);
  }

  public final void startDocument() {
    main.add(HtmlBytes.START_DOCUMENT);
  }

  public final void endDocument() {
    main.add(HtmlBytes.END_DOCUMENT);
  }

}
