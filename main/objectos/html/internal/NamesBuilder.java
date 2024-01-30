/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

import objectos.util.map.GrowableMap;
import objectos.util.map.UnmodifiableMap;

class NamesBuilder {

  private final GrowableMap<String, StandardAttributeName> map = new GrowableMap<>();

  public final UnmodifiableMap<String, StandardAttributeName> build() {
    return map.toUnmodifiableMap();
  }

  public final NamesBuilder put(String name, StandardAttributeName value) {
    map.put(name, value);
    return this;
  }

}
