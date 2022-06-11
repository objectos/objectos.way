/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.lang;

import java.util.Arrays;

/**
 * Provides {@code static} methods to format and generate a standardized
 * {@link Object#toString()} method output.
 *
 * @since 0.2
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
   */
  public static void appendIndentation(StringBuilder toString, int level) {
    var indentation = i(level);

    toString.append(indentation);
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} the object representation formed by its
   * {@code typeName} only.
   *
   * <p>
   * This method is meant to be used by implementors of the
   * {@link ToString.Formattable} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.format(sb, level, this);
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
  public static void format(
      StringBuilder toString, int level, Object typeName) {
    Check.notNull(toString, "toString == null");
    Check.notNull(typeName, "typeName == null");

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
   * {@link ToString.Formattable} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.format(
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
  public static void format(
      StringBuilder toString, int level, Object typeName,
      String name, Object value) {
    Check.notNull(toString, "toString == null");
    Check.notNull(typeName, "typeName == null");
    Check.notNull(name, "name == null");

    formatStart0(toString, typeName);

    var indentation = i(level);

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
   * {@link ToString.Formattable} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.format(
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
  public static void format(
      StringBuilder toString, int level, Object typeName,
      String name1, Object value1,
      String name2, Object value2) {
    Check.notNull(toString, "toString == null");
    Check.notNull(typeName, "typeName == null");
    Check.notNull(name1, "name1 == null");
    Check.notNull(name2, "name2 == null");

    formatStart0(toString, typeName);

    var indentation = i(level);

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
   * {@link ToString.Formattable} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.format(
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
  public static void format(
      StringBuilder toString, int level, Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3) {
    Check.notNull(toString, "toString == null");
    Check.notNull(typeName, "typeName == null");
    Check.notNull(name1, "name1 == null");
    Check.notNull(name2, "name2 == null");
    Check.notNull(name3, "name3 == null");

    formatStart0(toString, typeName);

    var indentation = i(level);

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
   * {@link ToString.Formattable} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.format(
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
  public static void format(
      StringBuilder toString, int level, Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3,
      String name4, Object value4) {
    Check.notNull(toString, "toString == null");
    Check.notNull(typeName, "typeName == null");
    Check.notNull(name1, "name1 == null");
    Check.notNull(name2, "name2 == null");
    Check.notNull(name3, "name3 == null");
    Check.notNull(name4, "name4 == null");

    formatStart0(toString, typeName);

    var indentation = i(level);

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
   * {@link ToString.Formattable} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.format(
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
  public static void format(
      StringBuilder toString, int level, Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3,
      String name4, Object value4,
      String name5, Object value5) {
    Check.notNull(toString, "toString == null");
    Check.notNull(typeName, "typeName == null");
    Check.notNull(name1, "name1 == null");
    Check.notNull(name2, "name2 == null");
    Check.notNull(name3, "name3 == null");
    Check.notNull(name4, "name4 == null");
    Check.notNull(name5, "name5 == null");

    formatStart0(toString, typeName);

    var indentation = i(level);

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
   * {@link ToString.Formattable} interface. Like so
   *
   * <pre>
   * &#64;Override
   * public final void formatToString(StringBuilder sb, int level) {
   *   ToString.format(
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
  public static void format(
      StringBuilder toString, int level, Object typeName,
      String name1, Object value1,
      String name2, Object value2,
      String name3, Object value3,
      String name4, Object value4,
      String name5, Object value5,
      String name6, Object value6) {
    Check.notNull(toString, "toString == null");
    Check.notNull(typeName, "typeName == null");
    Check.notNull(name1, "name1 == null");
    Check.notNull(name2, "name2 == null");
    Check.notNull(name3, "name3 == null");
    Check.notNull(name4, "name4 == null");
    Check.notNull(name5, "name5 == null");
    Check.notNull(name6, "name6 == null");

    formatStart0(toString, typeName);

    var indentation = i(level);

    formatFirstPair0(toString, level, indentation, name1, value1);

    formatNextPair0(toString, level, indentation, name2, value2);

    formatNextPair0(toString, level, indentation, name3, value3);

    formatNextPair0(toString, level, indentation, name4, value4);

    formatNextPair0(toString, level, indentation, name5, value5);

    formatNextPair0(toString, level, indentation, name6, value6);

    formatEnd0(toString, indentation);
  }

  /**
   * Formats and appends to the {@code toString} builder the start of an object
   * representation formed by its {@code typeName}.
   *
   * <p>
   * This is a low-level method meant to be used by implementors of the
   * {@link ToString.Formattable} interfaces.
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
    Check.notNull(toString, "toString == null");

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
   * {@link ToString.Formattable} interfaces.
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
    Check.notNull(toString, "toString == null");
    Check.notNull(name, "name == null");

    var indentation = i(level);

    formatFirstPair0(toString, level, indentation, name, value);
  }

  /**
   * Formats and appends to the {@code toString} builder the second or
   * subsequent name/value pair of an object representation.
   *
   * <p>
   * This is a low-level method meant to be used by implementors of the
   * {@link ToString.Formattable} interfaces.
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
    Check.notNull(toString, "toString == null");
    Check.notNull(name, "name == null");

    var indentation = i(level);

    formatNextPair0(toString, level, indentation, name, value);
  }

  /**
   * Formats and appends to the {@code toString} builder the start of an object
   * representation formed by its {@code typeName}.
   *
   * <p>
   * This is a low-level method meant to be used by implementors of the
   * {@link ToString.Formattable} interfaces.
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
    Check.notNull(toString, "toString == null");
    Check.notNull(typeName, "typeName == null");

    formatStart0(toString, typeName);
  }

  /**
   * Returns a string representation for an {@link ToString.Formattable}
   * instance.
   *
   * @param object
   *        an {@code ToString.Formattable} instance
   *
   * @return the string representation as defined by the object's
   *         {@link ToString.Formattable#formatToString(StringBuilder, int)}
   *         implementation
   */
  public static String of(ToString.Formattable object) {
    Check.notNull(object, "object == null");

    var out = new StringBuilder();

    object.formatToString(out, 0);

    return out.toString();
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

    if (key instanceof String s) {
      if (!s.isEmpty()) {
        sb.append(s);

        sb.append('=');
      }
    }

    else if (key instanceof ToString.Formattable o) {
      o.formatToString(sb, level + 1);

      sb.append('=');
    }

    // value

    if (value == null) {
      sb.append("null");
    }

    else if (value instanceof String s) {
      sb.append(s);
    }

    else if (value instanceof ToString.Formattable o) {
      o.formatToString(sb, level + 1);
    }

    else {
      var s = value.toString();

      sb.append(s);
    }

    sb.append('\n');
  }

  private static void formatStart0(StringBuilder sb, Object typeName) {
    if (typeName instanceof String s) {
      sb.append(s);
    }

    else {
      var type = typeName.getClass();

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

    var r = new char[2 * level + 2];

    Arrays.fill(r, ' ');

    return r;
  }

  /**
   * An object that can correctly participate in nested {@code toString}
   * implementations.
   *
   * @since 0.2
   */
  public interface Formattable {

    /**
     * Formats and appends this object's string representation to the
     * {@code toString} builder at the specified indentation level.
     *
     * @param toString
     *        the builder of a {@code toString} method
     * @param level
     *        the indentation level
     */
    void formatToString(StringBuilder toString, int level);

  }

}
