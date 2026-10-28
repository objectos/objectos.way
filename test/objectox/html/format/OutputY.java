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

import java.util.function.Consumer;

public final class OutputY {

  public int bufferSize = 256;

  public int level = 0;

  public String overflow = "";

  public String text = "";

  public static Output create() {
    return create(_ -> {});
  }

  public static Output create(Consumer<? super OutputY> opts) {
    final OutputY y;
    y = new OutputY();

    opts.accept(y);

    return y.build();
  }

  private Output build() {
    final char[] buf;
    buf = new char[bufferSize];

    final Output out;
    out = new Output(buf);

    for (int i = 0; i < level; i++) {
      out.inc();
    }

    if (!overflow.isEmpty()) {
      out.overflow(overflow);
    }

    if (!text.isEmpty()) {
      out.append(text);
    }

    return out;
  }

}
