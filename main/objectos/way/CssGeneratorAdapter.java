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

class CssGeneratorAdapter implements CssGeneratorScanner.Adapter {

  private final List<Css.ClassNameFormat> classNameFormats = Util.createList();

  private final List<Css.MediaQuery> mediaQueries = Util.createList();

  String sourceName;

  @Override
  public void processStringConstant(String s) {
    String[] parts;
    parts = s.split("\\s+");

    for (String part : parts) {
      if (!part.isBlank()) {
        processToken(part);
      }
    }
  }

  @Override
  public final void sourceName(String binaryName) {
    sourceName = binaryName;
  }

  void processToken(String token) {
    Css.Rule existing;
    existing = getRule(token);

    if (existing == null) {
      Css.Rule newRule;
      newRule = createRule(token);

      store(token, newRule);
    }

    else {
      consumeExisting(token, existing);
    }
  }

  Css.Rule createRule(String className) {
    String component;
    component = getComponent(className);

    if (component != null) {
      return createComponent(className, component);
    } else {
      return createUtility(className);
    }
  }

  void consumeExisting(String className, Css.Rule existing) {
    throw new UnsupportedOperationException("Implement me");
  }

  Css.Rule createComponent(String className, String definition) {
    throw new UnsupportedOperationException("Implement me");
  }

  Css.Rule createUtility(String className) {
    classNameFormats.clear();

    mediaQueries.clear();

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

      switch (variant) {
        case Css.ClassNameFormat format -> classNameFormats.add(format);

        case Css.MediaQuery query -> mediaQueries.add(query);
      }

      beginIndex = colon + 1;

      colon = className.indexOf(':', beginIndex);
    }

    Css.Modifier modifier;

    if (mediaQueries.isEmpty() && classNameFormats.isEmpty()) {
      modifier = Css.EMPTY_MODIFIER;
    } else {
      modifier = new Css.Modifier(
          Util.toUnmodifiableList(mediaQueries),
          Util.toUnmodifiableList(classNameFormats)
      );
    }

    String value;
    value = className;

    if (beginIndex > 0) {
      value = className.substring(beginIndex);
    }

    return createUtility(className, modifier, value);
  }

  Css.Rule createUtility(String className, Css.Modifier modifier, String value) {
    throw new UnsupportedOperationException();
  }

  String getComponent(String className) {
    throw new UnsupportedOperationException();
  }

  Css.Rule getRule(String token) {
    throw new UnsupportedOperationException();
  }

  Css.Variant getVariant(String name) {
    throw new UnsupportedOperationException();
  }

  void store(String token, Css.Rule value) {
    throw new UnsupportedOperationException("Implement me");
  }

}