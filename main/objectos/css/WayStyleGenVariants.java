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
package objectos.css;

import java.util.Comparator;
import java.util.List;
import objectos.util.list.GrowableList;

abstract class WayStyleGenVariants extends WayStyleGenCache {

  private final GrowableList<Variant> variantsBuilder = new GrowableList<>();

  @Override
  final Rule onCacheMiss(String className) {
    int beginIndex;
    beginIndex = 0;

    int colon;
    colon = className.indexOf(':', beginIndex);

    while (colon > 0) {
      String variantName;
      variantName = className.substring(beginIndex, colon);

      Variant variant;
      variant = getVariant(variantName);

      if (variant == null) {
        return Rule.NOOP;
      }

      variantsBuilder.add(variant);

      beginIndex = colon + 1;

      colon = className.indexOf(':', beginIndex);
    }

    List<Variant> variants;
    variants = variantsBuilder.toUnmodifiableList(Comparator.naturalOrder());

    variantsBuilder.clear();

    String value;
    value = className;

    if (beginIndex > 0) {
      value = className.substring(beginIndex);
    }

    return onVariants(className, variants, value);
  }

  abstract Variant getVariant(String variantName);

  abstract Rule onVariants(String className, List<Variant> variants, String value);

}