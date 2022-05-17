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
package objectos.lang;

/**
 * A state-machine for Unicode code point conversion participating in a
 * {@link StringConverter} instance.
 *
 * @since 0.2
 */
public abstract class Conversion {

  /**
   * Sole constructor.
   */
  protected Conversion() {}

  /**
   * Returns a new conversion that appends all code points to the output
   * <b>except</b> those code points that exist in the specified range.
   *
   * <p>
   * For example, if a converter {@code c} is created with a <i>single</i>
   * conversion
   *
   * <blockquote><pre>
   * Conversion.removeRange('2', '7')</pre></blockquote>
   *
   * <p>
   * Then the following would be conversion examples:
   *
   * <blockquote><pre>
   * c.convert("0123456789") returns "0189"
   * c.convert(" foo21") returns " foo1"</pre></blockquote>
   *
   * @param from
   *        the smallest code point of this range (inclusive)
   * @param to
   *        the largest code point of this range (inclusive)
   *
   * @return a new conversion that appends all code points except those existing
   *         in the specified range
   *
   * @throws IllegalArgumentException
   *         if {@code from >= to} or if any of {@code from}, or {@code to}
   *         fails the test {@link Character#isValidCodePoint(int)}
   */
  public static Conversion removeRange(int from, int to) {
    Checks.checkArgument(from < to, "from must be smaller than to");

    if (!Character.isValidCodePoint(from)) {
      throw new IllegalArgumentException(from + " is not a valid code point");
    }

    if (!Character.isValidCodePoint(to)) {
      throw new IllegalArgumentException(to + " is not a valid code point");
    }

    return new RemoveRange(from, to);
  }

  /**
   * Returns a conversion that appends to the output only those code points
   * that can form a valid Java identifier.
   *
   * <p>
   * More formally, it appends to the output the first code point that passes
   * the test {@link Character#isJavaIdentifierStart(int)}. After (or if) this
   * test passes, it then appends only those code points that passes the test
   * {@link Character#isJavaIdentifierPart(int)}.
   *
   * <p>
   * For example, if a converter {@code c} is created with this conversion
   * only then the following would be conversion examples:
   *
   * <blockquote><pre>
   * c.convert("foo") returns "foo"
   * c.convert("!foo") returns "foo"
   * c.convert("  !foo   ") returns "foo"
   * c.convert("foo-and-hyphen") returns "fooandhyphen"</pre></blockquote>
   *
   * @return a new conversion that appends only code points that can form a
   *         valid Java identifier
   */
  public static Conversion toJavaIdentifier() {
    return ToJavaIdentifier.INSTANCE;
  }

  /**
   * Returns a conversion that appends to the output only those code points
   * that can form a valid Java identifier while, at the same time, formatting
   * the output to a lower camel case style. This came case style is commonly
   * used for Java local variable and method names.
   *
   * <p>
   * More formally, it converts to lowercase and then appends to the output the
   * first code point that passes the test
   * {@link Character#isJavaIdentifierStart(int)}. After this test passes, it
   * appends code points that passes the test
   * {@link Character#isJavaIdentifierPart(int)}. At the same time, from this
   * point onwards, it converts to uppercase the first code point of a
   * fail-&gt;pass {@link Character#isJavaIdentifierPart(int)} test transition.
   *
   * <p>
   * For example, if a converter {@code c} is created with this conversion
   * only then the following would be conversion examples:
   *
   * <blockquote><pre>
   * c.convert("foo") returns "foo"
   * c.convert("!foo") returns "foo"
   * c.convert("  !foo   ") returns "foo"
   * c.convert("foo-and-hyphen") returns "fooAndHyphen"</pre></blockquote>
   *
   * @return a new conversion that appends only code points that can form a
   *         valid Java identifier and formats to the lower camel case style
   */
  public static Conversion toJavaLowerCamelCase() {
    return ToJavaCamelCase.LOWER;
  }

  /**
   * Returns a conversion that appends to the output only those code points
   * that can form a valid Java identifier while, at the same time, formatting
   * the output to a upper camel case style. This came case style is commonly
   * used for Java class names.
   *
   * <p>
   * More formally, it converts to uppercase and then appends to the output the
   * first code point that passes the test
   * {@link Character#isJavaIdentifierStart(int)}. After this test passes, it
   * appends code points that passes the test
   * {@link Character#isJavaIdentifierPart(int)}. At the same time, from this
   * point onwards, it converts to uppercase the first code point of a
   * fail-&gt;pass {@link Character#isJavaIdentifierPart(int)} test transition.
   *
   * <p>
   * For example, if a converter {@code c} is created with this conversion
   * only then the following would be conversion examples:
   *
   * <blockquote><pre>
   * c.convert("foo") returns "foo"
   * c.convert("!foo") returns "foo"
   * c.convert("  !foo   ") returns "foo"
   * c.convert("foo-and-hyphen") returns "FooAndHyphen"</pre></blockquote>
   *
   * @return a new conversion that appends only code points that can form a
   *         valid Java identifier and formats to the upper camel case style
   */
  public static Conversion toJavaUpperCamelCase() {
    return ToJavaCamelCase.UPPER;
  }

  /**
   * Returns a conversion that produces an output equivalent of the
   * {@link String#trim()} method.
   *
   * @return a conversion that behaves like the {@link String#trim()} method
   */
  public static Conversion trim() {
    return Trim.INSTANCE;
  }

  static Conversion removeCombiningDiacriticalMarks() {
    return RemoveRange.COMBINING_DIACRITICAL_MARKS;
  }

  /**
   * This method is invoked by the {@link StringConverter} after all code points
   * of a string have been processed.
   *
   * @param state
   *        the final state after the code points have been processed
   * @param builder
   *        the {@code StringBuilder}
   */
  protected void executeLastRound(Object state, StringBuilder builder) {}

  /**
   * This method is invoked by the {@link StringConverter} for each code
   * point if the previous conversion instance append a character to the output.
   *
   * <p>
   * Implementors of this method <i>must</i>
   *
   * <ul>
   * <li>handle the {@code null} state if one returns a {@code null} state;</li>
   * <li>if one needs to modify the {@code builder} instance, then the only
   * supported operation is appending a single char or code point to the
   * {@code builder}.</li>
   * </ul>
   *
   * @param state
   *        the current state
   * @param builder
   *        the {@code StringBuilder}
   * @param c
   *        the current Unicode code point
   *
   * @return the state after this transition
   */
  protected abstract Object executeOne(Object state, StringBuilder builder, int c);

  /**
   * Returns the starting state of the finite state machine.
   *
   * @return the starting state
   */
  protected abstract Object startingState();

}