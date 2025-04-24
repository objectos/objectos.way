/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.io.IOException;

final class CssWriter {

  private final Appendable css;

  private final StringBuilder sb;

  CssWriter(Appendable css) {
    this(css, new StringBuilder());
  }

  CssWriter(Appendable css, StringBuilder sb) {
    this.css = css;
    this.sb = sb;
  }

  // ##################################################################
  // # BEGIN: output writing section
  // ##################################################################

  final void indent(int level) throws IOException {
    for (int i = 0, count = level * 2; i < count; i++) {
      css.append(' ');
    }
  }

  final StringBuilder stringBuilder() {
    sb.setLength(0);

    return sb;
  }

  final void write(char c) throws IOException {
    css.append(c);
  }

  final void write(char c1, char c2) throws IOException {
    css.append(c1);
    css.append(c2);
  }

  final void write(CharSequence s) throws IOException {
    css.append(s);
  }

  final void writeln() throws IOException {
    css.append('\n');
  }

  final void writeln(char c) throws IOException {
    css.append(c);
    css.append('\n');
  }

  final void writeln(String s) throws IOException {
    css.append(s);
    css.append('\n');
  }

  // ##################################################################
  // # END: output writing section
  // ##################################################################

}