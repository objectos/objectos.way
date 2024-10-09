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
package objectos.way;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class HttpHeaderName implements Http.HeaderName {

  private static class Builder {

    static Builder INSTANCE = new Builder();

    private final List<HttpHeaderName> standardNames = new ArrayList<>();

    private int index;

    public final Http.HeaderName create(String name, HttpHeaderType type) {
      HttpHeaderName result;
      result = new HttpHeaderName(index++, name);

      standardNames.add(result);

      return result;
    }

    public final HttpHeaderName[] buildNames() {
      return standardNames.toArray(HttpHeaderName[]::new);
    }

  }

  record HeaderNameType(HttpHeaderName name, HttpHeaderType type) {}

  private final int index;

  private final String capitalized;

  @SuppressWarnings("unused")
  private final String lowerCase;

  public HttpHeaderName(int index, String capitalized) {
    this.index = index;

    this.capitalized = capitalized;

    this.lowerCase = capitalized.toLowerCase(Locale.US);
  }

  public HttpHeaderName(String name) {
    this(-1, name);
  }

  static Http.HeaderName create(String name, HttpHeaderType type) {
    return Builder.INSTANCE.create(name, type);
  }

  static Http.HeaderName createLast(String name, HttpHeaderType type) {
    Builder builder;
    builder = Builder.INSTANCE;

    Http.HeaderName result;
    result = builder.create(name, type);

    STANDARD_NAMES = builder.buildNames();

    Util.GrowableMap<String, HttpHeaderName> findByName;
    findByName = Util.createGrowableMap();

    for (HttpHeaderName value : STANDARD_NAMES) {
      findByName.put(value.capitalized, value);
    }

    FIND_BY_NAME = findByName.toUnmodifiableMap();

    Builder.INSTANCE = null;

    return result;
  }

  private static HttpHeaderName[] STANDARD_NAMES;

  private static Map<String, HttpHeaderName> FIND_BY_NAME;

  public static HttpHeaderName findByName(String name) {
    return FIND_BY_NAME.get(name);
  }

  public static HttpHeaderName standardName(int index) {
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
    return obj == this || obj instanceof HttpHeaderName that
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
