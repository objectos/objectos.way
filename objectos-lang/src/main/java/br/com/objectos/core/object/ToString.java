/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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
package br.com.objectos.core.object;

import java.util.Arrays;

/**
 * Provides {@code static} methods to format and generate a standardized
 * {@link Object#toString()} method output.
 */
public final class ToString {

  private static final char[][] INDENTATION = new char[][] {
      "  ".toCharArray(),
      "    ".toCharArray(),
      "      ".toCharArray(),
      "        ".toCharArray()
  };

  private ToString() {}

  /**
   * Appends to the {@code toString} builder an indentation suitable for the
   * specified level.
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level
   *
   * @throws IllegalArgumentException
   *         if {@code level} is negative
   *
   * @since 3
   */
  public static void appendIndentation(StringBuilder toString, int level) {
    char[] indentation;
    indentation = i(level);

    toString.append(indentation);
  }

  /**
   * Formats and appends to the {@code toString} builder the start of an object
   * representation formed by its {@code typeName}.
   *
   * <p>
   * This is a low-level method meant to be used by implementors of the
   * {@link ToStringObject} interfaces.
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   *
   * @throws IllegalArgumentException
   *         if {@code level} is negative
   * @throws NullPointerException
   *         if {@code toString} is null
   */
  public static void formatEnd(StringBuilder toString, int level) {
    Checks.checkNotNull(toString, "toString == null");

    char[] indentation;
    indentation = i(level);

    formatEnd0(toString, indentation);
  }

  /**
   * Formats and appends to the {@code toString} builder the first name/value
   * pair of an object representation.
   *
   * <p>
   * This is a low-level method meant to be used by implementors of the
   * {@link ToStringObject} interfaces.
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   * @param name
   *        the name of the pair
   * @param value
   *        the value of the pair
   *
   * @throws IllegalArgumentException
   *         if {@code level} is negative
   * @throws NullPointerException
   *         if any of the {@code toString}, {@code typeName} or {@code name} is
   *         null
   */
  public static void formatFirstPair(StringBuilder toString, int level, String name, Object value) {
    Checks.checkNotNull(toString, "toString == null");
    Checks.checkNotNull(name, "name == null");

    char[] indentation;
    indentation = i(level);

    formatFirstPair0(toString, level, indentation, name, value);
  }

  /**
   * Formats and appends to the {@code toString} builder the second or
   * subsequent name/value pair of an object representation.
   *
   * <p>
   * This is a low-level method meant to be used by implementors of the
   * {@link ToStringObject} interfaces.
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   * @param name
   *        the name of the pair
   * @param value
   *        the value of the pair
   *
   * @throws IllegalArgumentException
   *         if {@code level} is negative
   * @throws NullPointerException
   *         if any of the {@code toString}, {@code typeName} or {@code name} is
   *         null
   */
  public static void formatNextPair(StringBuilder toString, int level, String name, Object value) {
    Checks.checkNotNull(toString, "toString == null");
    Checks.checkNotNull(name, "name == null");

    char[] indentation;
    indentation = i(level);

    formatNextPair0(toString, level, indentation, name, value);
  }

  /**
   * Formats and appends to the {@code toString} builder the start of an object
   * representation formed by its {@code typeName}.
   *
   * <p>
   * This is a low-level method meant to be used by implementors of the
   * {@link ToStringObject} interfaces.
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation. If this is an instance of {@link java.lang.String},
   *        it is cast and used directly. If not, the simple name of this
   *        object's class is used instead
   *
   * @throws NullPointerException
   *         if any of the {@code toString} or {@code typeName} is null
   */
  public static void formatStart(StringBuilder toString, Object typeName) {
    Checks.checkNotNull(toString, "toString == null");
    Checks.checkNotNull(typeName, "typeName == null");

    formatStart0(toString, typeName);
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} the object representation formed by its
   * {@code typeName} only.
   *
   * <p>
   * This method is meant to be used by implementors of the
   * {@link ToStringObject} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.formatToString(sb, level, this);
   * }</pre>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation. If this is an instance of {@link java.lang.String},
   *        it is cast and used directly. If not, the simple name of this
   *        object's class is used instead
   *
   * @throws NullPointerException
   *         if any of {@code toString} or {@code typeName} is null
   */
  public static void formatToString(
      StringBuilder toString, int level, Object typeName) {
    Checks.checkNotNull(toString, "toString == null");
    Checks.checkNotNull(typeName, "typeName == null");

    formatStart0(toString, typeName);

    toString.append(']');
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} the object representation formed by its
   * {@code typeName} and a single name/value pair.
   *
   * <p>
   * This method is meant to be used by implementors of the
   * {@link ToStringObject} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.formatToString(
   *       sb, level, this,
   *       "a", a
   *   );
   * }</pre>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation. If this is an instance of {@link java.lang.String},
   *        it is cast and used directly. If not, the simple name of this
   *        object's class is used instead
   * @param name
   *        the name of the pair
   * @param value
   *        the value of the pair
   *
   * @throws IllegalArgumentException
   *         if {@code level} is negative
   * @throws NullPointerException
   *         if any of the {@code toString}, {@code typeName} or {@code name} is
   *         null
   */
  public static void formatToString(
      StringBuilder toString, int level, Object typeName,
      String name, Object value) {
    Checks.checkNotNull(toString, "toString == null");
    Checks.checkNotNull(typeName, "typeName == null");
    Checks.checkNotNull(name, "name == null");

    formatStart0(toString, typeName);

    char[] indentation;
    indentation = i(level);

    formatFirstPair0(toString, level, indentation, name, value);

    formatEnd0(toString, indentation);
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} the object representation formed by its
   * {@code typeName} and two name/value pairs.
   *
   * <p>
   * This method is meant to be used by implementors of the
   * {@link ToStringObject} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.formatToString(
   *       sb, level, this,
   *       "a", a,
   *       "b", b
   *   );
   * }</pre>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation. If this is an instance of {@link java.lang.String},
   *        it is cast and used directly. If not, the simple name of this
   *        object's class is used instead
   * @param name1
   *        the name of the first pair
   * @param value1
   *        the value of the first pair
   * @param name2
   *        the name of the second pair
   * @param value2
   *        the value of the second pair
   *
   * @throws IllegalArgumentException
   *         if {@code level} is negative
   * @throws NullPointerException
   *         if any of the {@code toString}, {@code typeName}, {@code name1} or
   *         {@code name2} is null
   */
  public static void formatToString(
      StringBuilder toString, int level, Object typeName,
      String name1, Object value1,
      String name2, Object value2) {
    Checks.checkNotNull(toString, "toString == null");
    Checks.checkNotNull(typeName, "typeName == null");
    Checks.checkNotNull(name1, "name1 == null");
    Checks.checkNotNull(name2, "name2 == null");

    formatStart0(toString, typeName);

    char[] indentation;
    indentation = i(level);

    formatFirstPair0(toString, level, indentation, name1, value1);

    formatNextPair0(toString, level, indentation, name2, value2);

    formatEnd0(toString, indentation);
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} the object representation formed by its
   * {@code typeName} and three name/value pairs.
   *
   * <p>
   * This method is meant to be used by implementors of the
   * {@link ToStringObject} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.formatToString(
   *       sb, level, this,
   *       "a", a,
   *       "b", b,
   *       "c", c
   *   );
   * }</pre>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation. If this is an instance of {@link java.lang.String},
   *        it is cast and used directly. If not, the simple name of this
   *        object's class is used instead
   * @param name1
   *        the name of the first pair
   * @param value1
   *        the value of the first pair
   * @param name2
   *        the name of the second pair
   * @param value2
   *        the value of the second pair
   * @param name3
   *        the name of the third pair
   * @param value3
   *        the value of the third pair
   *
   * @throws IllegalArgumentException
   *         if {@code level} is negative
   * @throws NullPointerException
   *         if any of the {@code toString}, {@code typeName}, {@code name1},
   *         {@code name2} or {@code name3} is null
   */
  public static void formatToString(
      StringBuilder toString, int level, Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3) {
    Checks.checkNotNull(toString, "toString == null");
    Checks.checkNotNull(typeName, "typeName == null");
    Checks.checkNotNull(name1, "name1 == null");
    Checks.checkNotNull(name2, "name2 == null");
    Checks.checkNotNull(name3, "name3 == null");

    formatStart0(toString, typeName);

    char[] indentation;
    indentation = i(level);

    formatFirstPair0(toString, level, indentation, name1, value1);

    formatNextPair0(toString, level, indentation, name2, value2);

    formatNextPair0(toString, level, indentation, name3, value3);

    formatEnd0(toString, indentation);
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} the object representation formed by its
   * {@code typeName} and four name/value pairs.
   *
   * <p>
   * This method is meant to be used by implementors of the
   * {@link ToStringObject} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.formatToString(
   *       sb, level, this,
   *       "a", a,
   *       "b", b,
   *       "c", c,
   *       "d", d
   *   );
   * }</pre>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation. If this is an instance of {@link java.lang.String},
   *        it is cast and used directly. If not, the simple name of this
   *        object's class is used instead
   * @param name1
   *        the name of the first pair
   * @param value1
   *        the value of the first pair
   * @param name2
   *        the name of the second pair
   * @param value2
   *        the value of the second pair
   * @param name3
   *        the name of the third pair
   * @param value3
   *        the value of the third pair
   * @param name4
   *        the name of the fourth pair
   * @param value4
   *        the value of the fourth pair
   *
   * @throws IllegalArgumentException
   *         if {@code level} is negative
   * @throws NullPointerException
   *         if any of the {@code toString}, {@code typeName}, {@code name1},
   *         {@code name2}, {@code name3} or {@code name4} is null
   */
  public static void formatToString(
      StringBuilder toString, int level, Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3,
      String name4, Object value4) {
    Checks.checkNotNull(toString, "toString == null");
    Checks.checkNotNull(typeName, "typeName == null");
    Checks.checkNotNull(name1, "name1 == null");
    Checks.checkNotNull(name2, "name2 == null");
    Checks.checkNotNull(name3, "name3 == null");
    Checks.checkNotNull(name4, "name4 == null");

    formatStart0(toString, typeName);

    char[] indentation;
    indentation = i(level);

    formatFirstPair0(toString, level, indentation, name1, value1);

    formatNextPair0(toString, level, indentation, name2, value2);

    formatNextPair0(toString, level, indentation, name3, value3);

    formatNextPair0(toString, level, indentation, name4, value4);

    formatEnd0(toString, indentation);
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} the object representation formed by its
   * {@code typeName} and five name/value pairs.
   *
   * <p>
   * This method is meant to be used by implementors of the
   * {@link ToStringObject} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.formatToString(
   *       sb, level, this,
   *       "a", a,
   *       "b", b,
   *       "c", c,
   *       "d", d,
   *       "e", e
   *   );
   * }</pre>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation. If this is an instance of {@link java.lang.String},
   *        it is cast and used directly. If not, the simple name of this
   *        object's class is used instead
   * @param name1
   *        the name of the first pair
   * @param value1
   *        the value of the first pair
   * @param name2
   *        the name of the second pair
   * @param value2
   *        the value of the second pair
   * @param name3
   *        the name of the third pair
   * @param value3
   *        the value of the third pair
   * @param name4
   *        the name of the fourth pair
   * @param value4
   *        the value of the fourth pair
   * @param name5
   *        the name of the fifth pair
   * @param value5
   *        the value of the fifth pair
   *
   * @throws IllegalArgumentException
   *         if {@code level} is negative
   * @throws NullPointerException
   *         if any of the {@code toString}, {@code typeName}, {@code name1},
   *         {@code name2}, {@code name3} or {@code name4} is null
   */
  public static void formatToString(
      StringBuilder toString, int level, Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3,
      String name4, Object value4,
      String name5, Object value5) {
    Checks.checkNotNull(toString, "toString == null");
    Checks.checkNotNull(typeName, "typeName == null");
    Checks.checkNotNull(name1, "name1 == null");
    Checks.checkNotNull(name2, "name2 == null");
    Checks.checkNotNull(name3, "name3 == null");
    Checks.checkNotNull(name4, "name4 == null");
    Checks.checkNotNull(name5, "name5 == null");

    formatStart0(toString, typeName);

    char[] indentation;
    indentation = i(level);

    formatFirstPair0(toString, level, indentation, name1, value1);

    formatNextPair0(toString, level, indentation, name2, value2);

    formatNextPair0(toString, level, indentation, name3, value3);

    formatNextPair0(toString, level, indentation, name4, value4);

    formatNextPair0(toString, level, indentation, name5, value5);

    formatEnd0(toString, indentation);
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} the object representation formed by its
   * {@code typeName} and six name/value pairs.
   *
   * <p>
   * This method is meant to be used by implementors of the
   * {@link ToStringObject} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.formatToString(
   *       sb, level, this,
   *       "a", a,
   *       "b", b,
   *       "c", c,
   *       "d", d,
   *       "e", e,
   *       "f", f
   *   );
   * }</pre>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation. If this is an instance of {@link java.lang.String},
   *        it is cast and used directly. If not, the simple name of this
   *        object's class is used instead
   * @param name1
   *        the name of the first pair
   * @param value1
   *        the value of the first pair
   * @param name2
   *        the name of the second pair
   * @param value2
   *        the value of the second pair
   * @param name3
   *        the name of the third pair
   * @param value3
   *        the value of the third pair
   * @param name4
   *        the name of the fourth pair
   * @param value4
   *        the value of the fourth pair
   * @param name5
   *        the name of the fifth pair
   * @param value5
   *        the value of the fifth pair
   * @param name6
   *        the name of the sixth pair
   * @param value6
   *        the value of the sixth pair
   *
   * @throws IllegalArgumentException
   *         if {@code level} is negative
   * @throws NullPointerException
   *         if any of the {@code toString}, {@code typeName}, {@code name1},
   *         {@code name2}, {@code name3} or {@code name4} is null
   */
  public static void formatToString(
      StringBuilder toString, int level, Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3,
      String name4, Object value4,
      String name5, Object value5,
      String name6, Object value6) {
    Checks.checkNotNull(toString, "toString == null");
    Checks.checkNotNull(typeName, "typeName == null");
    Checks.checkNotNull(name1, "name1 == null");
    Checks.checkNotNull(name2, "name2 == null");
    Checks.checkNotNull(name3, "name3 == null");
    Checks.checkNotNull(name4, "name4 == null");
    Checks.checkNotNull(name5, "name5 == null");
    Checks.checkNotNull(name6, "name6 == null");

    formatStart0(toString, typeName);

    char[] indentation;
    indentation = i(level);

    formatFirstPair0(toString, level, indentation, name1, value1);

    formatNextPair0(toString, level, indentation, name2, value2);

    formatNextPair0(toString, level, indentation, name3, value3);

    formatNextPair0(toString, level, indentation, name4, value4);

    formatNextPair0(toString, level, indentation, name5, value5);

    formatNextPair0(toString, level, indentation, name6, value6);

    formatEnd0(toString, indentation);
  }

  /**
   * Returns a string representation of the {@code int} value.
   *
   * @param value
   *        an {@code int} value
   *
   * @return a string representation of the {@code int} value
   */
  public static String toString(int value) {
    return Integer.toString(value);
  }

  /**
   * Returns a string representation for an object represented by its
   * {@code typeName} only. The returned string is formatted by invoking the
   * {@link #formatToString(StringBuilder, int, Object)} method with a zero
   * identation {@code level}.
   *
   * <p>
   * This method is useful for implementing {@link Object#toString()} methods
   * like so
   *
   * <pre>
   * &#64;Override
   * public final void toString() {
   *   return ToString.toString(this);
   * }</pre>
   *
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation.
   *
   * @return a string representation containing only the specified class name
   *
   * @see #formatToString(StringBuilder, int, Object)
   */
  public static String toString(
      Object typeName) {
    StringBuilder toString;
    toString = new StringBuilder();

    formatToString(
        toString, 0, typeName
    );

    return toString.toString();
  }

  /**
   * Returns a string representation for an object represented by its
   * {@code typeName} a single name/value pair. The returned string is formatted
   * by invoking the
   * {@link #formatToString(StringBuilder, int, Object, String, Object)}
   * method with a zero identation {@code level}.
   *
   * <p>
   * This method is useful for implementing {@link Object#toString()} methods
   * like so
   *
   * <pre>
   * &#64;Override
   * public final void toString() {
   *   return ToString.toString(
   *       this,
   *       "a", a
   *   );
   * }</pre>
   *
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation.
   * @param name
   *        the name of the pair
   * @param value
   *        the value of the pair
   *
   * @return a string representation containing the specified class name and
   *         the name/value pair
   *
   * @see #formatToString(StringBuilder, int, Object, String, Object)
   */
  public static String toString(
      Object typeName,
      String name, Object value) {
    StringBuilder toString;
    toString = new StringBuilder();

    formatToString(
        toString, 0, typeName,
        name, value
    );

    return toString.toString();
  }

  /**
   * Returns a string representation for an object represented by its
   * {@code typeName} a two name/value pairs. The returned string is formatted
   * by invoking the
   * {@link #formatToString(StringBuilder, int, Object, String, Object, String, Object)}
   * method with a zero identation {@code level}.
   *
   * <p>
   * This method is useful for implementing {@link Object#toString()} methods
   * like so
   *
   * <pre>
   * &#64;Override
   * public final void toString() {
   *   return ToString.toString(
   *       this,
   *       "a", a,
   *       "b", b
   *   );
   * }</pre>
   *
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation.
   * @param name1
   *        the name of the first pair
   * @param value1
   *        the value of the first pair
   * @param name2
   *        the name of the second pair
   * @param value2
   *        the value of the second pair
   *
   * @return a string representation containing the specified class name and
   *         the name/value pairs
   *
   * @see #formatToString(StringBuilder, int, Object, String, Object, String,
   *      Object)
   */
  public static String toString(
      Object typeName,
      String name1, Object value1,
      String name2, Object value2) {
    StringBuilder toString;
    toString = new StringBuilder();

    formatToString(
        toString, 0, typeName,
        name1, value1,
        name2, value2
    );

    return toString.toString();
  }

  /**
   * Returns a string representation for an object represented by its
   * {@code typeName} a three name/value pairs. The returned string is formatted
   * by invoking the
   * {@link #formatToString(StringBuilder, int, Object, String, Object, String, Object, String, Object)}
   * method with a zero identation {@code level}.
   *
   * <p>
   * This method is useful for implementing {@link Object#toString()} methods
   * like so
   *
   * <pre>
   * &#64;Override
   * public final void toString() {
   *   return ToString.toString(
   *       this,
   *       "a", a,
   *       "b", b,
   *       "c", c
   *   );
   * }</pre>
   *
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation.
   * @param name1
   *        the name of the first pair
   * @param value1
   *        the value of the first pair
   * @param name2
   *        the name of the second pair
   * @param value2
   *        the value of the second pair
   * @param name3
   *        the name of the third pair
   * @param value3
   *        the value of the third pair
   *
   * @return a string representation containing the specified class name and
   *         the name/value pairs
   *
   * @see #formatToString(StringBuilder, int, Object, String, Object, String,
   *      Object, String, Object)
   */
  public static String toString(
      Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3) {
    StringBuilder toString;
    toString = new StringBuilder();

    formatToString(
        toString, 0, typeName,
        name1, value1,
        name2, value2,
        name3, value3
    );

    return toString.toString();
  }

  /**
   * Returns a string representation for an object represented by its
   * {@code typeName} a four name/value pairs. The returned string is formatted
   * by invoking the
   * {@link #formatToString(StringBuilder, int, Object, String, Object, String, Object, String, Object, String, Object)}
   * method with a zero identation {@code level}.
   *
   * <p>
   * This method is useful for implementing {@link Object#toString()} methods
   * like so
   *
   * <pre>
   * &#64;Override
   * public final void toString() {
   *   return ToString.toString(
   *       this,
   *       "a", a,
   *       "b", b,
   *       "c", c,
   *       "d", d
   *   );
   * }</pre>
   *
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation.
   * @param name1
   *        the name of the first pair
   * @param value1
   *        the value of the first pair
   * @param name2
   *        the name of the second pair
   * @param value2
   *        the value of the second pair
   * @param name3
   *        the name of the third pair
   * @param value3
   *        the value of the third pair
   * @param name4
   *        the name of the fourth pair
   * @param value4
   *        the value of the fourth pair
   *
   * @return a string representation containing the specified class name and
   *         the name/value pairs
   *
   * @see #formatToString(StringBuilder, int, Object, String, Object, String,
   *      Object, String, Object, String, Object)
   */
  public static String toString(
      Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3,
      String name4, Object value4) {
    StringBuilder toString;
    toString = new StringBuilder();

    formatToString(
        toString, 0, typeName,
        name1, value1,
        name2, value2,
        name3, value3,
        name4, value4
    );

    return toString.toString();
  }

  /**
   * Returns a string representation for an object represented by its
   * {@code typeName} a five name/value pairs. The returned string is formatted
   * by invoking the
   * {@link #formatToString(StringBuilder, int, Object, String, Object, String, Object, String, Object, String, Object, String, Object)}
   * method with a zero identation {@code level}.
   *
   * <p>
   * This method is useful for implementing {@link Object#toString()} methods
   * like so
   *
   * <pre>
   * &#64;Override
   * public final void toString() {
   *   return ToString.toString(
   *       this,
   *       "a", a,
   *       "b", b,
   *       "c", c,
   *       "d", d,
   *       "e", e
   *   );
   * }</pre>
   *
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation.
   * @param name1
   *        the name of the first pair
   * @param value1
   *        the value of the first pair
   * @param name2
   *        the name of the second pair
   * @param value2
   *        the value of the second pair
   * @param name3
   *        the name of the third pair
   * @param value3
   *        the value of the third pair
   * @param name4
   *        the name of the fourth pair
   * @param value4
   *        the value of the fourth pair
   * @param name5
   *        the name of the fifth pair
   * @param value5
   *        the value of the fifth pair
   *
   * @return a string representation containing the specified class name and
   *         the name/value pairs
   *
   * @see #formatToString(StringBuilder, int, Object, String, Object, String,
   *      Object, String, Object, String, Object, String, Object)
   */
  public static String toString(
      Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3,
      String name4, Object value4,
      String name5, Object value5) {
    StringBuilder toString;
    toString = new StringBuilder();

    formatToString(
        toString, 0, typeName,
        name1, value1,
        name2, value2,
        name3, value3,
        name4, value4,
        name5, value5
    );

    return toString.toString();
  }

  /**
   * Returns a string representation for an object represented by its
   * {@code typeName} a five name/value pairs. The returned string is formatted
   * by invoking the
   * {@link #formatToString(StringBuilder, int, Object, String, Object, String, Object, String, Object, String, Object, String, Object, String, Object)}
   * method with a zero identation {@code level}.
   *
   * <p>
   * This method is useful for implementing {@link Object#toString()} methods
   * like so
   *
   * <pre>
   * &#64;Override
   * public final void toString() {
   *   return ToString.toString(
   *       this,
   *       "a", a,
   *       "b", b,
   *       "c", c,
   *       "d", d,
   *       "e", e,
   *       "f", f
   *   );
   * }</pre>
   *
   * @param typeName
   *        the value to use as type name for the {@code toString}
   *        representation.
   * @param name1
   *        the name of the first pair
   * @param value1
   *        the value of the first pair
   * @param name2
   *        the name of the second pair
   * @param value2
   *        the value of the second pair
   * @param name3
   *        the name of the third pair
   * @param value3
   *        the value of the third pair
   * @param name4
   *        the name of the fourth pair
   * @param value4
   *        the value of the fourth pair
   * @param name5
   *        the name of the fifth pair
   * @param value5
   *        the value of the fifth pair
   * @param name6
   *        the name of the sixth pair
   * @param value6
   *        the value of the sixth pair
   *
   * @return a string representation containing the specified class name and
   *         the name/value pairs
   *
   * @see #formatToString(StringBuilder, int, Object, String, Object, String,
   *      Object, String, Object, String, Object, String, Object, String,
   *      Object)
   */
  public static String toString(
      Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3,
      String name4, Object value4,
      String name5, Object value5,
      String name6, Object value6) {
    StringBuilder toString;
    toString = new StringBuilder();

    formatToString(
        toString, 0, typeName,
        name1, value1,
        name2, value2,
        name3, value3,
        name4, value4,
        name5, value5,
        name6, value6
    );

    return toString.toString();
  }

  /**
   * Returns a string representation for an {@link ToStringObject} instance.
   *
   * @param object
   *        an {@code ToStringObject} instance
   *
   * @return the string representation as defined by the object's
   *         {@link ToStringObject#formatToString(StringBuilder, int)}
   *         implementation
   */
  public static String toString(ToStringObject object) {
    Checks.checkNotNull(object, "object == null");

    StringBuilder out;
    out = new StringBuilder();

    object.formatToString(out, 0);

    return out.toString();
  }

  private static void formatEnd0(StringBuilder sb, char[] indentation) {
    sb.append(indentation, 0, indentation.length - 2);

    sb.append(']');
  }

  private static void formatFirstPair0(
      StringBuilder sb, int level, char[] indentation, Object key, Object value) {
    sb.append('\n');

    formatNextPair0(sb, level, indentation, key, value);
  }

  private static void formatNextPair0(
      StringBuilder sb, int level, char[] indentation, Object key, Object value) {
    sb.append(indentation);

    // key

    if (key instanceof String) {
      String s;
      s = (String) key;

      if (!s.isEmpty()) {
        sb.append(s);

        sb.append('=');
      }
    }

    else if (key instanceof ToStringObject) {
      ToStringObject o;
      o = (ToStringObject) key;

      o.formatToString(sb, level + 1);

      sb.append('=');
    }

    // value

    if (value == null) {
      sb.append("null");
    }

    else if (value instanceof String) {
      String s;
      s = (String) value;

      sb.append(s);
    }

    else if (value instanceof ToStringObject) {
      ToStringObject o;
      o = (ToStringObject) value;

      o.formatToString(sb, level + 1);
    }

    else {
      String s;
      s = value.toString();

      sb.append(s);
    }

    sb.append('\n');
  }

  private static void formatStart0(StringBuilder sb, Object typeName) {
    if (typeName instanceof String) {
      String s;
      s = (String) typeName;

      sb.append(s);
    }

    else {
      Class<? extends Object> type;
      type = typeName.getClass();

      sb.append(type.getSimpleName());
    }

    sb.append('[');
  }

  private static char[] i(int level) {
    if (level < 0) {
      throw new IllegalArgumentException("level=" + level + " < 0");
    }

    if (level < INDENTATION.length) {
      return INDENTATION[level];
    }

    char[] r;
    r = new char[2 * level + 2];

    Arrays.fill(r, ' ');

    return r;
  }

}
