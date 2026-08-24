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
package objectox.html.stream;

import java.util.function.Consumer;
import objectox.html.markup.HtmlSink;

public final class PlayerY {

  private PlayerY() {}

  public static Player create(Consumer<? super HtmlSink> opts) {
    final HtmlSink sink;
    sink = new HtmlSink();

    opts.accept(sink);

    final byte[] code;
    code = sink.code();

    final Object[] objects;
    objects = sink.objects();

    return Player.of(code, objects);
  }

}
