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
package objectox.html.elem;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import objectos.internal.Util;

public final class ElementNameUniverse {

  private ElementName[] array;

  private Map<String, ElementName> byName;

  private int size;

  public ElementNameUniverse() {
    array = new ElementName[16];
  }

  public final ElementName get(int index) {
    return array[index];
  }

  public final ElementName getOrCreate(String $name) {
    final String name;
    name = Objects.requireNonNull($name, "name == null");

    final Map<String, ElementName> byName;
    byName = byName();

    final ElementName found;
    found = byName.get(name);

    if (found != null) {
      return found;
    }

    synchronized (this) {
      final ElementName again;
      again = byName.get(name);

      if (again != null) {
        return again;
      }

      final ElementName created;
      created = create(name);

      byName.put(created.name(), created);

      return created;
    }
  }

  public final ElementName create(String name) {
    final ElementNameValidator validator;
    validator = new ElementNameValidator(name);

    final String validated;
    validated = validator.validate();

    final int index;
    index = size++;

    array = Util.growIfNecessary(array, index);

    final ElementName created;
    created = new ElementName(index, validated);

    array[index] = created;

    return created;
  }

  private Map<String, ElementName> byName() {
    synchronized (this) {
      if (byName == null) {
        final Map<String, ElementName> created;
        created = new HashMap<>();

        for (int idx = 0; idx < size; idx++) {
          final ElementName existing;
          existing = array[idx];

          created.put(existing.name(), existing);
        }

        byName = created;
      }

      return byName;
    }
  }

}
