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

import java.util.ArrayList;
import java.util.List;

final class HtmlAttributeNameBuilder {

  private static HtmlAttributeNameBuilder INSTANCE = new HtmlAttributeNameBuilder();

  private final List<HtmlAttributeName> standardValues = new ArrayList<>();

  private int index;

  private HtmlAttributeNameBuilder() {}

  public static HtmlAttributeName action(String name) {
    return INSTANCE.createImpl(name, false, true, Script.Action.class);
  }

  public static HtmlAttributeName actionLast(String name) {
    HtmlAttributeName result;
    result = INSTANCE.createImpl(name, false, true, Script.Action.class);

    HtmlAttributeName.VALUES = INSTANCE.buildValuesImpl();

    HtmlAttributeNameBuilder.INSTANCE = null;

    return result;
  }

  public static HtmlAttributeName create(String name, boolean booleanAttribute) {
    return INSTANCE.createImpl(name, booleanAttribute);
  }

  private HtmlAttributeName createImpl(String name, boolean booleanAttribute) {
    return createImpl(name, booleanAttribute, false, String.class);
  }

  private HtmlAttributeName createImpl(String name, boolean booleanAttribute, boolean singleQuoted, Class<?> type) {
    HtmlAttributeName result;
    result = new HtmlAttributeName(index++, name, booleanAttribute, singleQuoted, type);

    standardValues.add(result);

    return result;
  }

  private HtmlAttributeName[] buildValuesImpl() {
    return standardValues.toArray(HtmlAttributeName[]::new);
  }

}