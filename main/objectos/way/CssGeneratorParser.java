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

import java.util.List;
import java.util.Set;

abstract class CssGeneratorParser extends CssGeneratorVariants {

  final CssConfig config;

  CssGeneratorParser(CssConfig config) {
    this.config = config;
  }

  @Override
  final CssVariant getVariant(String variantName) {
    return config.getVariant(variantName);
  }

  @Override
  final CssRule onVariants(String className, List<CssVariant> variants, String value) {
    // 1) static values search
    CssStaticUtility staticFactory;
    staticFactory = config.getStatic(value);

    if (staticFactory != null) {
      return staticFactory.create(className, variants);
    }

    // 2) by prefix search

    char firstChar;
    firstChar = value.charAt(0);

    // are we dealing with a negative value
    boolean negative;
    negative = false;

    if (firstChar == '-') {
      negative = true;

      value = value.substring(1);
    }

    // maybe it is the prefix with an empty value
    // e.g. border-x

    Set<CssKey> candidates;
    candidates = config.getCandidates(value);

    String suffix;
    suffix = "";

    if (candidates == null) {

      int fromIndex;
      fromIndex = value.length();

      while (candidates == null && fromIndex > 0) {
        int lastDash;
        lastDash = value.lastIndexOf('-', fromIndex);

        if (lastDash == 0) {
          // value starts with a dash and has no other dash
          // => invalid value
          break;
        }

        fromIndex = lastDash - 1;

        String prefix;
        prefix = value;

        suffix = "";

        if (lastDash > 0) {
          prefix = value.substring(0, lastDash);

          suffix = value.substring(lastDash + 1);
        }

        candidates = config.getCandidates(prefix);
      }

    }

    if (candidates == null) {
      return CssRule.NOOP;
    }

    for (CssKey candidate : candidates) {
      CssResolver resolver;
      resolver = config.getResolver(candidate);

      CssRule rule;
      rule = resolver.resolve(className, variants, negative, suffix);

      if (rule != null) {
        return rule;
      }
    }

    return CssRule.NOOP;
  }

}