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

import java.util.Comparator;
import java.util.List;
import objectos.util.list.GrowableList;

class CssGeneratorAdapter {

  private final GrowableList<Css.Variant> variantsBuilder = new GrowableList<>();

  void processRawString(String s) {
    String[] parts;
    parts = s.split("\\s+");

    for (String part : parts) {
      if (!part.isBlank()) {
        processToken(part);
      }
    }
  }

  void processToken(String token) {
    Css.Rule existing;
    existing = getFragment(token);

    if (existing == null) {
      Css.Rule newRule;
      newRule = createFragment(token);

      store(token, newRule);
    }

    else {
      consumeExisting(existing);
    }
  }

  Css.Rule createFragment(String className) {
    String component;
    component = getComponent(className);

    if (component != null) {
      return createComponent(className, component);
    } else {
      return createUtility(className);
    }
  }

  void consumeExisting(Css.Rule existing) {
    throw new UnsupportedOperationException("Implement me");
  }

  Css.Rule createComponent(String className, String definition) {
    throw new UnsupportedOperationException("Implement me");
  }

  Css.Rule createUtility(String className) {
    int beginIndex;
    beginIndex = 0;

    int colon;
    colon = className.indexOf(':', beginIndex);

    while (colon > 0) {
      String variantName;
      variantName = className.substring(beginIndex, colon);

      Css.Variant variant;
      variant = getVariant(variantName);

      if (variant == null) {
        return Css.Rule.NOOP;
      }

      variantsBuilder.add(variant);

      beginIndex = colon + 1;

      colon = className.indexOf(':', beginIndex);
    }

    List<Css.Variant> variants;
    variants = variantsBuilder.toUnmodifiableList(Comparator.naturalOrder());

    variantsBuilder.clear();

    String value;
    value = className;

    if (beginIndex > 0) {
      value = className.substring(beginIndex);
    }

    return createUtility(className, variants, value);
  }

  Css.Rule createUtility(String className, List<Css.Variant> variants, String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  String getComponent(String className) {
    throw new UnsupportedOperationException("Implement me");
  }

  Css.Rule getFragment(String token) {
    throw new UnsupportedOperationException("Implement me");
  }

  Css.Variant getVariant(String name) {
    throw new UnsupportedOperationException("Implement me");
  }

  void store(String token, Css.Rule value) {
    throw new UnsupportedOperationException("Implement me");
  }

}