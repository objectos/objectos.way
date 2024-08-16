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

final class HtmlAttributeName implements Html.AttributeName {

  /**
   * The {@code data-frame} attribute.
   */
  public static final Html.AttributeName DATA_FRAME = create("data-frame", false);

  /**
   * The {@code data-on-click} attribute.
   */
  public static final Html.AttributeName DATA_ON_CLICK = action("data-on-click");

  /**
   * The {@code data-on-input} attribute.
   */
  public static final Html.AttributeName DATA_ON_INPUT = action("data-on-input");

  static final class Builder {

    static Builder INSTANCE = new Builder();

    private final List<HtmlAttributeName> standardValues = new ArrayList<>();

    private int index;

    private Builder() {}

    public final HtmlAttributeName createImpl(String name, boolean booleanAttribute) {
      return createImpl(name, booleanAttribute, false, String.class);
    }

    public final HtmlAttributeName createImpl(String name, boolean booleanAttribute, boolean singleQuoted, Class<?> type) {
      HtmlAttributeName result;
      result = new HtmlAttributeName(index++, name, booleanAttribute, singleQuoted, type);

      standardValues.add(result);

      return result;
    }

    public HtmlAttributeName[] buildValuesImpl() {
      return standardValues.toArray(HtmlAttributeName[]::new);
    }

  }

  private final int index;

  private final String name;

  private final boolean booleanAttribute;

  private final boolean singleQuoted;

  private final Class<?> type;

  public HtmlAttributeName(int index, String name, boolean booleanAttribute, boolean singleQuoted, Class<?> type) {
    this.index = index;
    this.name = name;
    this.booleanAttribute = booleanAttribute;
    this.singleQuoted = singleQuoted;
    this.type = type;
  }

  public static HtmlAttributeName action(String name) {
    return Builder.INSTANCE.createImpl(name, false, true, Script.Action.class);
  }

  public static HtmlAttributeName create(String name, boolean booleanAttribute) {
    return Builder.INSTANCE.createImpl(name, booleanAttribute);
  }

  static int size() {
    return LazyValues.VALUES.length;
  }

  public static HtmlAttributeName get(int index) {
    return LazyValues.VALUES[index];
  }

  @Override
  public final int index() {
    return index;
  }

  @Override
  public final String name() {
    return name;
  }

  @Override
  public final boolean booleanAttribute() {
    return booleanAttribute;
  }

  @Override
  public final boolean singleQuoted() {
    return singleQuoted;
  }

  public final Class<?> type() {
    return type;
  }

  private static class LazyValues {

    static HtmlAttributeName[] VALUES = create();

    private static HtmlAttributeName[] create() {
      HtmlAttributeName[] result;
      result = Builder.INSTANCE.buildValuesImpl();

      Builder.INSTANCE = null;

      return result;
    }

  }

}