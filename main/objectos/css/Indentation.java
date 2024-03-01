/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.css;

final class Indentation {

  static final Indentation ROOT = new Indentation(0);

  private final int level;

  private Indentation(int level) {
    this.level = level;
  }

  public final String indent(String value) {
    return value.indent(level * 2);
  }

  public final Indentation increase() {
    return new Indentation(level + 1);
  }

  public final void writeTo(StringBuilder out) {
    for (int i = 0, count = level * 2; i < count; i++) {
      out.append(' ');
    }
  }

}