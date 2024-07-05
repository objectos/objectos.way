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
import objectos.util.list.GrowableList;

final class CssProperties implements Iterable<Map.Entry<String, String>> {

  private final List<Map.Entry<String, String>> values = new GrowableList<>();

  public final void add(String key, String value) {
    Entry<String, String> entry;
    entry = Map.entry(key, value);

    values.add(entry);
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

  public final int size() {
    return values.size();
  }

}
