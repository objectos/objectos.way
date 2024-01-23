/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import objectos.http.HeaderName;
import objectos.util.map.GrowableMap;

public final class ObjectoxHeaderName extends HeaderName {

  public static class Builder {

    private final List<ObjectoxHeaderName> standardNames = new ArrayList<>();

    private int index;

    public final HeaderName create(String name, HeaderType type) {
      ObjectoxHeaderName result;
      result = new ObjectoxHeaderName(index++, name);

      standardNames.add(result);

      return result;
    }

    public final ObjectoxHeaderName[] buildNames() {
      return standardNames.toArray(ObjectoxHeaderName[]::new);
    }

  }

  record HeaderNameType(ObjectoxHeaderName name, HeaderType type) {}

  private final int index;

  private final String capitalized;

  @SuppressWarnings("unused")
  private final String lowerCase;

  public ObjectoxHeaderName(int index, String capitalized) {
    this.index = index;

    this.capitalized = capitalized;

    this.lowerCase = capitalized.toLowerCase(Locale.US);
  }

  public ObjectoxHeaderName(String name) {
    this(-1, name);
  }

  private static ObjectoxHeaderName[] STANDARD_NAMES;

  private static Map<String, ObjectoxHeaderName> FIND_BY_NAME;

  public static void set(Builder builder) {
    STANDARD_NAMES = builder.buildNames();

    GrowableMap<String, ObjectoxHeaderName> findByName;
    findByName = new GrowableMap<>();

    for (ObjectoxHeaderName value : STANDARD_NAMES) {
      findByName.put(value.capitalized, value);
    }

    FIND_BY_NAME = findByName.toUnmodifiableMap();
  }

  public static ObjectoxHeaderName findByName(String name) {
    return FIND_BY_NAME.get(name);
  }

  public static ObjectoxHeaderName standardName(int index) {
    return STANDARD_NAMES[index];
  }

  public static int standardNamesSize() {
    return STANDARD_NAMES.length;
  }

  @Override
  public final String capitalized() {
    return capitalized;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof ObjectoxHeaderName that
        && capitalized.equals(that.capitalized);
  }

  @Override
  public final int hashCode() {
    return capitalized.hashCode();
  }

  @Override
  public final int index() {
    return index;
  }

}
