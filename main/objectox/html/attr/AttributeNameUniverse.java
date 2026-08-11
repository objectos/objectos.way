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
package objectox.html.attr;

import java.util.HashMap;
import java.util.Map;
import objectos.internal.Util;

public final class AttributeNameUniverse {

  private AttributeName[] array;

  private Map<String, AttributeName> byName;

  private int size;

  public AttributeNameUniverse() {
    array = new AttributeName[16];
  }

  public final AttributeName get(int index) {
    return array[index];
  }

  public final AttributeName getOrCreate(String name) {
    final Map<String, AttributeName> byName;
    byName = byName();

    final AttributeName found;
    found = byName.get(name);

    if (found != null) {
      return found;
    }

    synchronized (this) {
      final AttributeName again;
      again = byName.get(name);

      if (again != null) {
        return again;
      }

      final AttributeName created;
      created = create(name);

      byName.put(created.name(), created);

      return created;
    }
  }

  public final AttributeName create(String name) {
    final AttributeNameValidator validator;
    validator = new AttributeNameValidator(name);

    final String validated;
    validated = validator.validate();

    final int index;
    index = size++;

    array = Util.growIfNecessary(array, index);

    final AttributeName created;
    created = new AttributeName(index, validated);

    array[index] = created;

    return created;
  }

  private Map<String, AttributeName> byName() {
    synchronized (this) {
      if (byName == null) {
        final Map<String, AttributeName> created;
        created = new HashMap<>();

        for (int idx = 0; idx < size; idx++) {
          final AttributeName existing;
          existing = array[idx];

          created.put(existing.name(), existing);
        }

        byName = created;
      }

      return byName;
    }
  }

}
