/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.css.util;

import objectos.html.tmpl.Api.ExternalAttribute.StyleClass;
import objectox.css.ClassSelectorSeqId;

/**
 * Utility classes for the {@code line-height} CSS property.
 */
// Generated by selfgen.css.CssUtilSpec. Do not edit!
public enum LineHeight implements StyleClass {

  NONE("1"),

  TIGHT("1.25"),

  SNUG("1.375"),

  NORMAL("1.5"),

  RELAXED("1.625"),

  LOOSE("2"),

  PX12("0.75rem"),

  PX16("1rem"),

  PX20("1.25rem"),

  PX24("1.5rem"),

  PX28("1.75rem"),

  PX32("2rem"),

  PX36("2.25rem"),

  PX40("2.5rem");

  private final String className = ClassSelectorSeqId.next();

  private final String value;

  private LineHeight(String value) {
    this.value = value;
  }

  /**
   * Returns the CSS class name.
   *
   * @return the CSS class name
   */
  @Override
  public final String className() {
    return className;
  }

  /**
   * Returns the CSS style rule represented by this utility class.
   *
   * @return the CSS style rule
   */
  @Override
  public final String toString() {
    return "." + className + " { line-height: " + value + " }";
  }

}
