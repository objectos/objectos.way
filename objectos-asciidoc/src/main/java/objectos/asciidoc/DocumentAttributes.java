/*
 * Copyright (C) 2021-2026 Objectos Software LTDA.
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
package objectos.asciidoc;

import objectos.asciidoc.DocumentAttributes.Empty;
import objectos.asciidoc.DocumentAttributes.Standard;
import objectos.util.UnmodifiableMap;

public sealed abstract class DocumentAttributes permits Empty, Standard {

  static final class Empty extends DocumentAttributes {
    @Override
    public String getOrDefault(String key, String defaultValue) { return defaultValue; }
  }

  static final class Standard extends DocumentAttributes {
    private final UnmodifiableMap<String, String> map;

    public Standard(UnmodifiableMap<String, String> map) {
      this.map = map;
    }

    @Override
    public String getOrDefault(String key, String defaultValue) {
      return map.getOrDefault(key, defaultValue);
    }
  }

  static final DocumentAttributes EMPTY = new Empty();

  DocumentAttributes() {}

  static DocumentAttributes wrap(UnmodifiableMap<String, String> map) {
    return new Standard(map);
  }

  public abstract String getOrDefault(String key, String defaultValue);

}