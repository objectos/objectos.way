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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class CssProperties implements Iterable<Map.Entry<String, String>> {

  static final class Builder {

    private final List<Map.Entry<String, String>> values = Util.createList();

    public final void add(String key, String value) {
      Entry<String, String> entry;
      entry = Map.entry(key, value);

      values.add(entry);
    }

    public final CssProperties build() {
      if (values.isEmpty()) {
        throw new IllegalArgumentException("Empty");
      }

      return new CssProperties(
          Util.toUnmodifiableList(values)
      );
    }

  }

  public static final CssProperties NOOP = new CssProperties(List.of());

  private final List<Map.Entry<String, String>> values;

  private CssProperties(List<Entry<String, String>> values) {
    this.values = values;
  }

  public static CssProperties of(String key, String value) {
    Map.Entry<String, String> property;
    property = Map.entry(key, value);

    List<Entry<String, String>> values;
    values = List.of(property);

    return new CssProperties(values);
  }

  public final Iterable<Map.Entry<String, String>> entries() {
    return values;
  }

  public final Map.Entry<String, String> get(int index) {
    return values.get(index);
  }

  @Override
  public final Iterator<Entry<String, String>> iterator() {
    return values.iterator();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public final Map<String, String> toMap() {
    Map.Entry[] entries;
    entries = values.toArray(Map.Entry[]::new);

    return Map.ofEntries(entries);
  }

  public final Map<String, String> toMap(Map<String, String> more) {
    Util.GrowableMap<String, String> map;
    map = Util.createGrowableMap();

    for (Map.Entry<String, String> entry : values) {
      map.put(
          entry.getKey(),

          entry.getValue()
      );
    }

    map.putAll(more);

    return map.toUnmodifiableMap();
  }

  public final Map<String, String> toMap(CssProperties extension) {
    Util.GrowableMap<String, String> map;
    map = Util.createGrowableMap();

    for (Map.Entry<String, String> entry : values) {
      map.put(
          entry.getKey(),

          entry.getValue()
      );
    }

    for (Map.Entry<String, String> entry : extension.values) {
      map.put(
          entry.getKey(),

          entry.getValue()
      );
    }

    return map.toUnmodifiableMap();
  }

  public final int size() {
    return values.size();
  }

}
