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

// Generated by selfgen.css.CssUtilSpec. Do not edit!
public final class Max {

  private Max() {}

  /**
   * Utility classes for the {@code align-content} CSS property.
   */
  public enum AlignContent implements StyleClass {

    NORMAL("normal"),

    CENTER("center"),

    START("flex-start"),

    END("flex-end"),

    BETWEEN("space-between"),

    AROUND("space-around"),

    EVENLY("space-evenly"),

    BASELINE("baseline"),

    STRETCH("stretch");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private AlignContent(String value) {
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
      return "." + className + " { align-content: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code align-items} CSS property.
   */
  public enum AlignItems implements StyleClass {

    START("flex-start"),

    END("flex-end"),

    CENTER("center"),

    BASELINE("baseline"),

    STRETCH("stretch");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private AlignItems(String value) {
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
      return "." + className + " { align-items: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code align-self} CSS property.
   */
  public enum AlignSelf implements StyleClass {

    AUTO("auto"),

    START("flex-start"),

    END("flex-end"),

    CENTER("center"),

    STRETCH("stretch"),

    BASELINE("baseline");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private AlignSelf(String value) {
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
      return "." + className + " { align-self: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code display} CSS property.
   */
  public enum Display implements StyleClass {

    NONE("none"),

    BLOCK("block"),

    FLOW_ROOT("flow-root"),

    INLINE_BLOCK("inline-block"),

    INLINE("inline"),

    FLEX("flex"),

    INLINE_FLEX("inline-flex"),

    GRID("grid"),

    INLINE_GRID("inline-grid"),

    TABLE("table"),

    TABLE_CAPTION("table-caption"),

    TABLE_CELL("table-cell"),

    TABLE_COLUMN("table-column"),

    TABLE_COLUMN_GROUP("table-column-group"),

    TABLE_FOOTER_GROUP("table-footer-group"),

    TABLE_HEADER_GROUP("table-header-group"),

    TABLE_ROW_GROUP("table-row-group"),

    TABLE_ROW("table-row");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Display(String value) {
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
      return "." + className + " { display: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code flex} CSS property.
   */
  public enum Flex implements StyleClass {

    ONE("1 1 0%"),

    AUTO("1 1 auto"),

    INITIAL("0 1 auto"),

    NONE("none");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Flex(String value) {
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
      return "." + className + " { flex: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code flex-direction} CSS property.
   */
  public enum FlexDirection implements StyleClass {

    ROW("row"),

    ROW_REVERSE("row-reverse"),

    COLUMN("column"),

    COLUMN_REVERSE("column-reverse");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private FlexDirection(String value) {
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
      return "." + className + " { flex-direction: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code flex-grow} CSS property.
   */
  public enum FlexGrow implements StyleClass {

    V1("1"),

    V0("0");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private FlexGrow(String value) {
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
      return "." + className + " { flex-grow: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code height} CSS property.
   */
  public enum Height implements StyleClass {

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

    PX384("24rem"),

    AUTO("auto"),

    HALF("50%"),

    THIRD1("33.333333%"),

    THIRD2("66.666667%"),

    QUARTER1("25%"),

    QUARTER2("50%"),

    QUARTER3("75%"),

    FIFTH1("20%"),

    FIFTH2("40%"),

    FIFTH3("60%"),

    FIFTH4("80%"),

    SIXTH1("16.666667%"),

    SIXTH2("33.333333%"),

    SIXTH3("50%"),

    SIXTH4("66.666667%"),

    SIXTH5("83.333333%"),

    FULL("100%"),

    SCREEN("100vh"),

    MIN("min-content"),

    MAX("max-content"),

    FIT("fit-content");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Height(String value) {
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
      return "." + className + " { height: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code top} CSS property.
   */
  public enum Top implements StyleClass {

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

    PX384("24rem"),

    AUTO("auto"),

    HALF("50%"),

    THIRD1("33.333333%"),

    THIRD2("66.666667%"),

    QUARTER1("25%"),

    QUARTER2("50%"),

    QUARTER3("75%"),

    FULL("100%");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Top(String value) {
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
      return "." + className + " { top: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code right} CSS property.
   */
  public enum Right implements StyleClass {

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

    PX384("24rem"),

    AUTO("auto"),

    HALF("50%"),

    THIRD1("33.333333%"),

    THIRD2("66.666667%"),

    QUARTER1("25%"),

    QUARTER2("50%"),

    QUARTER3("75%"),

    FULL("100%");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Right(String value) {
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
      return "." + className + " { right: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code bottom} CSS property.
   */
  public enum Bottom implements StyleClass {

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

    PX384("24rem"),

    AUTO("auto"),

    HALF("50%"),

    THIRD1("33.333333%"),

    THIRD2("66.666667%"),

    QUARTER1("25%"),

    QUARTER2("50%"),

    QUARTER3("75%"),

    FULL("100%");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Bottom(String value) {
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
      return "." + className + " { bottom: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code left} CSS property.
   */
  public enum Left implements StyleClass {

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

    PX384("24rem"),

    AUTO("auto"),

    HALF("50%"),

    THIRD1("33.333333%"),

    THIRD2("66.666667%"),

    QUARTER1("25%"),

    QUARTER2("50%"),

    QUARTER3("75%"),

    FULL("100%");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Left(String value) {
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
      return "." + className + " { left: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code inset} CSS property.
   */
  public enum Inset implements StyleClass {

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

    PX384("24rem"),

    AUTO("auto"),

    HALF("50%"),

    THIRD1("33.333333%"),

    THIRD2("66.666667%"),

    QUARTER1("25%"),

    QUARTER2("50%"),

    QUARTER3("75%"),

    FULL("100%");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Inset(String value) {
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
      return "." + className + " { inset: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code left} and {@code right} CSS properties.
   */
  public enum InsetX implements StyleClass {

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

    PX384("24rem"),

    AUTO("auto"),

    HALF("50%"),

    THIRD1("33.333333%"),

    THIRD2("66.666667%"),

    QUARTER1("25%"),

    QUARTER2("50%"),

    QUARTER3("75%"),

    FULL("100%");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private InsetX(String value) {
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
      return "." + className + " { left: " + value + "; right: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code top} and {@code bottom} CSS properties.
   */
  public enum InsetY implements StyleClass {

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

    PX384("24rem"),

    AUTO("auto"),

    HALF("50%"),

    THIRD1("33.333333%"),

    THIRD2("66.666667%"),

    QUARTER1("25%"),

    QUARTER2("50%"),

    QUARTER3("75%"),

    FULL("100%");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private InsetY(String value) {
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
      return "." + className + " { top: " + value + "; bottom: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code justify-content} CSS property.
   */
  public enum JustifyContent implements StyleClass {

    NORMAL("normal"),

    START("flex-start"),

    END("flex-end"),

    CENTER("center"),

    BETWEEN("space-between"),

    AROUND("space-around"),

    EVENLY("space-evenly"),

    STRETCH("stretch");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private JustifyContent(String value) {
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
      return "." + className + " { justify-content: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code padding} CSS property.
   */
  public enum Padding implements StyleClass {

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

    private Padding(String value) {
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
      return "." + className + " { padding: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code padding-top} CSS property.
   */
  public enum PaddingTop implements StyleClass {

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

    private PaddingTop(String value) {
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
      return "." + className + " { padding-top: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code padding-right} CSS property.
   */
  public enum PaddingRight implements StyleClass {

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

    private PaddingRight(String value) {
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
      return "." + className + " { padding-right: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code padding-bottom} CSS property.
   */
  public enum PaddingBottom implements StyleClass {

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

    private PaddingBottom(String value) {
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
      return "." + className + " { padding-bottom: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code padding-left} CSS property.
   */
  public enum PaddingLeft implements StyleClass {

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

    private PaddingLeft(String value) {
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
      return "." + className + " { padding-left: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code padding-right} and {@code padding-left} CSS properties.
   */
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

  /**
   * Utility classes for the {@code padding-top} and {@code padding-bottom} CSS properties.
   */
  public enum PaddingY implements StyleClass {

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

    private PaddingY(String value) {
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
      return "." + className + " { padding-top: " + value + "; padding-bottom: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code position} CSS property.
   */
  public enum Position implements StyleClass {

    STATIC("static"),

    FIXED("fixed"),

    ABSOLUTE("absolute"),

    RELATIVE("relative"),

    STICKY("sticky");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Position(String value) {
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
      return "." + className + " { position: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code width} CSS property.
   */
  public enum Width implements StyleClass {

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

    PX384("24rem"),

    AUTO("auto"),

    HALF("50%"),

    THIRD1("33.333333%"),

    THIRD2("66.666667%"),

    QUARTER1("25%"),

    QUARTER2("50%"),

    QUARTER3("75%"),

    FIFTH1("20%"),

    FIFTH2("40%"),

    FIFTH3("60%"),

    FIFTH4("80%"),

    SIXTH1("16.666667%"),

    SIXTH2("33.333333%"),

    SIXTH3("50%"),

    SIXTH4("66.666667%"),

    SIXTH5("83.333333%"),

    TWELFTH1("8.333333%"),

    TWELFTH2("16.666667%"),

    TWELFTH3("25%"),

    TWELFTH4("33.333333%"),

    TWELFTH5("41.666667%"),

    TWELFTH6("50%"),

    TWELFTH7("58.333333%"),

    TWELFTH8("66.666667%"),

    TWELFTH9("75%"),

    TWELFTH10("83.333333%"),

    TWELFTH11("91.666667%"),

    FULL("100%"),

    SCREEN("100vh"),

    MIN("min-content"),

    MAX("max-content"),

    FIT("fit-content");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Width(String value) {
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
      return "." + className + " { width: " + value + " }";
    }

  }

  /**
   * Utility classes for the {@code z-index} CSS property.
   */
  public enum ZIndex implements StyleClass {

    V0("0"),

    V10("10"),

    V20("20"),

    V30("30"),

    V40("40"),

    V50("50"),

    AUTO("auto");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private ZIndex(String value) {
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
      return "." + className + " { z-index: " + value + " }";
    }

  }

}
