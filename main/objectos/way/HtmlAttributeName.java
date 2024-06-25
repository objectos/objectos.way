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

final class HtmlAttributeName extends HtmlAttributeNameGenerated implements Html.AttributeName {

  /**
   * The {@code data-click} attribute.
   */
  public static final Html.AttributeName DATA_CLICK = HtmlAttributeNameBuilder.action("data-click");

  /**
   * The {@code data-frame} attribute.
   */
  public static final Html.AttributeName DATA_FRAME = HtmlAttributeNameBuilder.create("data-frame", false);

  /**
   * The {@code data-on-click} attribute.
   */
  public static final Html.AttributeName DATA_ON_CLICK = HtmlAttributeNameBuilder.action("data-on-click");

  /**
   * The {@code data-on-input} attribute.
   */
  public static final Html.AttributeName DATA_ON_INPUT = HtmlAttributeNameBuilder.action("data-on-input");

  /**
   * The {@code data-way-click} attribute.
   */
  public static final Html.AttributeName DATA_WAY_CLICK = HtmlAttributeNameBuilder.action("data-way-click");

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

  static HtmlAttributeName[] VALUES;

  static int size() {
    return VALUES.length;
  }

  public static HtmlAttributeName get(int index) {
    return VALUES[index];
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

}