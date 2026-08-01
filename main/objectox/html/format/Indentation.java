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

import objectos.html.format.IndentationStyle;

final class Indentation {

  private int level;

  private final String text;

  Indentation(String text) {
    this.text = text;
  }

  public final void inc() {
    level++;
  }

  public final void dec() {
    if (level == 0) {
      final String msg;
      msg = "Cannot decrement indentation level below zero";

      throw new IllegalStateException(msg);
    }

    level--;
  }

  public final void format(IndentationStyle style, Output out) {
    switch (style) {
      case IndentationOption.NEVER -> {}

      case IndentationOption.START_OF_LINE -> {
        if (out.startOfLine()) {
          out.append(toString());
        }
      }
    }
  }

  @Override
  public final String toString() {
    return text.repeat(level);
  }

}
