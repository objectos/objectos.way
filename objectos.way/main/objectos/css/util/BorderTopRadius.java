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
 * Utility classes for the {@code border-top-left-radius} and {@code border-top-right-radius} CSS properties.
 */
// Generated by selfgen.css.CssUtilSpec. Do not edit!
public enum BorderTopRadius implements StyleClass {

  PX0("0px"),

  PX2("0.125rem"),

  PX4("0.25rem"),

  PX6("0.375rem"),

  PX8("0.5rem"),

  PX12("0.75rem"),

  PX16("1rem"),

  PX24("1.5rem"),

  FULL("9999px");

  private final String className = ClassSelectorSeqId.next();

  private final String value;

  private BorderTopRadius(String value) {
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
    return "." + className + " { border-top-left-radius: " + value + "; border-top-right-radius: " + value + " }";
  }

}
