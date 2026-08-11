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

  private AttributeNamePojo[] array;

  private Map<String, AttributeNamePojo> table;

  private int size;

  public AttributeNameUniverse() {
    array = new AttributeNamePojo[16];
  }

  public final AttributeNamePojo get(int index) {
    return array[index];
  }

  public final AttributeNamePojo getOrCreate(String name) {
    final Map<String, AttributeNamePojo> t;
    t = table();

    final AttributeNamePojo found;
    found = t.get(name);

    if (found != null) {
      return found;
    }

    synchronized (this) {
      final AttributeNamePojo again;
      again = t.get(name);

      if (again != null) {
        return again;
      }

      final AttributeNamePojo created;
      created = create(name);

      t.put(name, created);

      return created;
    }
  }

  public final AttributeNamePojo create(String name) {
    final AttributeNameValidator validator;
    validator = new AttributeNameValidator(name);

    final String validated;
    validated = validator.validate();

    final int index;
    index = size++;

    array = Util.growIfNecessary(array, index);

    final AttributeNamePojo created;
    created = new AttributeNamePojo(false, index, validated);

    array[index] = created;

    return created;
  }

  private Map<String, AttributeNamePojo> table() {
    synchronized (this) {
      if (table == null) {
        final Map<String, AttributeNamePojo> created;
        created = new HashMap<>();

        for (int idx = 0; idx < size; idx++) {
          final AttributeNamePojo existing;
          existing = array[idx];

          created.put(existing.name(), existing);
        }

        table = created;
      }

      return table;
    }
  }

}
