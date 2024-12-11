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
package objectos.way;

import java.util.Map;

record CssThemeEntryOfVariable(int index, String name, String value, String id) implements CssThemeEntry {

  @Override
  public final void acceptMappings(Map<String, String> mappings) {
    String maybeExisting;
    maybeExisting = mappings.put(id, "var(" + name + ")");

    if (maybeExisting != null) {
      throw new IllegalArgumentException("Duplicate mapping for " + name + ": " + value);
    }
  }

  @Override
  public final Object key() {
    return name;
  }

  @Override
  public final String toString() {
    StringBuilder out;
    out = new StringBuilder();

    writeTo(out);

    return out.toString();
  }

  @Override
  public final void writeTo(StringBuilder out) {
    out.append(name);
    out.append(": ");
    out.append(value);
    out.append(';');
  }

}