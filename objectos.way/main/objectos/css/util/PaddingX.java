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
 * Utility classes for the {@code padding-right} and {@code padding-left} CSS properties.
 */
// Generated by selfgen.css.CssUtilSpec. Do not edit!
public enum PaddingX implements StyleClass {

  PX0("0px"),

  PX1("1px"),

  PX2("0.125rem"),

  PX4("0.25rem"),

  PX6("0.375rem"),

  PX8("0.5rem"),

  PX10("0.625rem"),

  PX12("0.75rem"),

  PX14("0.875rem"),

  PX16("1rem"),

  PX20("1.25rem"),

  PX24("1.5rem"),

  PX28("1.75rem"),

  PX32("2rem"),

  PX36("2.25rem"),

  PX40("2.5rem"),

  PX44("2.75rem"),

  PX48("3rem"),

  PX56("3.5rem"),

  PX64("4rem"),

  PX80("5rem"),

  PX96("6rem"),

  PX112("7rem"),

  PX128("8rem"),

  PX144("9rem"),

  PX160("10rem"),

  PX176("11rem"),

  PX192("12rem"),

  PX208("13rem"),

  PX224("14rem"),

  PX240("15rem"),

  PX256("16rem"),

  PX288("18rem"),

  PX320("20rem"),

  PX384("24rem");

  private final String className = ClassSelectorSeqId.next();

  private final String value;

  private PaddingX(String value) {
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
    return "." + className + " { padding-right: " + value + "; padding-left: " + value + " }";
  }

}
