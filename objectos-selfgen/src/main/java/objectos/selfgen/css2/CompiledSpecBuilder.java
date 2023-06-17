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
package objectos.selfgen.css2;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

final class CompiledSpecBuilder {

  private final Map<String, SelectorName> selectors = new TreeMap<>();

  public final void addSelector(String name) {
    var selector = SelectorName.of(name);

    var key = selector.fieldName();

    if (selectors.containsKey(key)) {
      throw new UnsupportedOperationException(
        "Implement me :: duplicate selector field name=" + key
      );
    }

    selectors.put(key, selector);
  }

  public final CompiledSpec build() {
    return new CompiledSpec(
      selectors()
    );
  }

  private List<SelectorName> selectors() {
    return selectors.values().stream().toList();
  }

}