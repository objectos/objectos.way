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

enum IntSize {

  BIT8,

  BIT16,

  BIT24;

  public static IntSize of(int value) {
    if (value < 0) {
      final String msg;
      msg = "Illegal int value: negative values (%d) are now allowed".formatted(value);

      throw new IllegalArgumentException(msg);
    }

    else if (value <= HtmlSink.MAX_INT8) {
      return BIT8;
    }

    else if (value <= HtmlSink.MAX_INT16) {
      return BIT16;
    }

    else if (value <= HtmlSink.MAX_INT24) {
      return BIT24;
    }

    else {
      final String msg;
      msg = "Illegal int value: (%d) is too large".formatted(value);

      throw new IllegalArgumentException(msg);
    }
  }

}
