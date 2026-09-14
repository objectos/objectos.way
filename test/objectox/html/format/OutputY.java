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
package objectox.html.format;

import java.io.IOException;
import java.util.function.Consumer;

final class OutputY {

  int bufferSize = 256;

  int level = 0;

  String text = "";

  public static Output create() throws IOException {
    return create(_ -> {});
  }

  public static Output create(Consumer<? super OutputY> opts) throws IOException {
    final OutputY y;
    y = new OutputY();

    opts.accept(y);

    return y.build();
  }

  private Output build() throws IOException {
    final char[] buf;
    buf = new char[bufferSize];

    final Output out;
    out = new Output(buf);

    for (int i = 0; i < level; i++) {
      out.inc();
    }

    if (!text.isEmpty()) {
      out.append(text);
    }

    return out;
  }

}
