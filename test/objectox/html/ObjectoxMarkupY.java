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
package objectox.html;

import java.util.function.Consumer;
import objectox.html.markup.HtmlSink;

public final class ObjectoxMarkupY {

  private int codeInitial;

  private int objectsInitial;

  private ObjectoxMarkupY() {}

  public static ObjectoxMarkup create(Consumer<? super ObjectoxMarkupY> opts) {
    final ObjectoxMarkupY y;
    y = new ObjectoxMarkupY();

    opts.accept(y);

    return y.build();
  }

  public final void objects(int initial) {
    objectsInitial = initial;
  }

  private ObjectoxMarkup build() {
    final byte[] code;
    code = new byte[codeInitial];

    final Object[] objects;
    objects = new Object[objectsInitial];

    final HtmlSink sink;
    sink = new HtmlSink(code, objects);

    return ObjectoxMarkup.of(sink);
  }

}
