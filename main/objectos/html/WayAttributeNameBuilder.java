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
package objectos.html;

import java.util.ArrayList;
import java.util.List;

final class WayAttributeNameBuilder {

  private static WayAttributeNameBuilder BUILDER = new WayAttributeNameBuilder();

  private final List<WayAttributeName> standardValues = new ArrayList<>();

  private int index;

  private WayAttributeNameBuilder() {}

  public static AttributeName create(String name, boolean booleanAttribute) {
    return BUILDER.createImpl(name, booleanAttribute);
  }

  public static AttributeName action(String name) {
    return BUILDER.createImpl(name, false, true, Action.class);
  }

  public static WayAttributeName[] build() {
    WayAttributeName[] result;
    result = BUILDER.buildValuesImpl();

    BUILDER = null;

    return result;
  }

  private WayAttributeName createImpl(String name, boolean booleanAttribute) {
    return createImpl(name, booleanAttribute, false, String.class);
  }

  private WayAttributeName createImpl(String name, boolean booleanAttribute, boolean singleQuoted, Class<?> type) {
    WayAttributeName result;
    result = new WayAttributeName(index++, name, booleanAttribute, singleQuoted, type);

    standardValues.add(result);

    return result;
  }

  private WayAttributeName[] buildValuesImpl() {
    return standardValues.toArray(WayAttributeName[]::new);
  }

}