/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectos.core.object;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

final class SimpleToStringMap implements ToString.Formattable {

  private final Map<String, Object> map = new LinkedHashMap<String, Object>();

  SimpleToStringMap() {}

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.formatStart(sb, this);

    Set<Entry<String, Object>> entrySet;
    entrySet = map.entrySet();

    Iterator<Entry<String, Object>> iterator;
    iterator = entrySet.iterator();

    if (iterator.hasNext()) {
      Entry<String, Object> entry;
      entry = iterator.next();

      String key;
      key = entry.getKey();

      Object value;
      value = entry.getValue();

      ToString.formatFirstPair(sb, depth, key, value);

      while (iterator.hasNext()) {
        entry = iterator.next();

        key = entry.getKey();

        value = entry.getValue();

        ToString.formatNextPair(sb, depth, key, value);
      }
    }

    ToString.formatEnd(sb, depth);
  }

  public final void put(String key, Object value) {
    map.put(key, value);
  }

  @Override
  public final String toString() {
    return ToString.of(this);
  }

}