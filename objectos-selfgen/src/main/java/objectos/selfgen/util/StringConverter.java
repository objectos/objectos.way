/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.util;

import objectos.lang.Check;

/**
 * A class for converting strings by applying a series of user defined
 * conversions.
 *
 * @since 0.2
 */
final class StringConverter {

  private final StringConversion[] conversions;

  StringConverter(StringConversion[] conversions) {
    this.conversions = conversions;
  }

  /**
   * Creates a new string converter with a single conversion.
   *
   * @param conversion
   *        the single conversion of this converter
   *
   * @return a newly constructed string converter with a single conversion
   *
   * @throws NullPointerException
   *         if {@code conversion} is null
   */
  public static StringConverter create(StringConversion conversion) {
    Check.notNull(conversion, "conversion == null");

    return new StringConverter(
      new StringConversion[] {conversion}
    );
  }

  /**
   * Creates a new string converter with one or more conversions. The conversion
   * instances are added to the converter in their <i>declared order</i>.
   *
   * @param first
   *        the first conversion of this converter
   * @param rest
   *        the remaining conversions of this converter
   *
   * @return a newly constructed string converter with one or more conversions
   *         in their declared order
   *
   * @throws NullPointerException
   *         if {@code first} or {@code rest} is null or if any of the elements
   *         on {@code rest} are null
   */
  public static StringConverter create(StringConversion first, StringConversion... rest) {
    Check.notNull(first, "first == null");
    Check.notNull(rest, "rest == null");

    StringConversion[] conversions;
    conversions = new StringConversion[rest.length + 1];

    conversions[0] = first;

    for (int i = 0; i < rest.length; i++) {
      StringConversion r;
      r = rest[i];

      conversions[i + 1] = Check.notNull(r, "rest[", i, "] == null");
    }

    return new StringConverter(conversions);
  }

  /**
   * Converts the specified string {@code s} and appends the result to the
   * specified {@code builder}.
   *
   * <p>
   * If the string {@code s} is empty then nothing is done and the
   * {@code builder} is left unchanged.
   *
   * <p>
   * If the string {@code s} is non-empty then all of its Unicode code points
   * are processed, in order, one at a time. For each code point, the defined
   * conversions are considered for application in their declared order. If the
   * first {@code Conversion} instance provides a converted code point then this
   * code point is supplied to the next {@code Conversion}. If the second
   * {@code Conversion} instance provides a converted code point then it is
   * supplied to the next {@code Conversion}, and so on. This continues until
   *
   * <ol>
   * <li>there are no more {@code Conversion} instances; or</li>
   * <li>a {@code Conversion} provides no converted code point.</li>
   * </ol>
   *
   * <p>
   * The final converted code point is appended to {@code builder} in the first
   * scenario and the code point is discarded in the second one.
   *
   * <p>
   * This is repeated until all of the code points have been processed
   * processed. The {@code Conversion} instances are then allowed to do a last
   * round of processing. In their declared order, for each {@code Conversion}
   * instance, the
   * {@link StringConversion#executeLastRound(Object, StringBuilder)}
   * method is invoked.
   *
   * @param s
   *        a string to convert
   * @param builder
   *        the {@code StringBuilder} where the result of the conversion will be
   *        appended to
   */
  public final void apply(String s, StringBuilder builder) {
    Check.notNull(s, "s == null");
    Check.notNull(builder, "builder == null");

    if (s.isEmpty()) {
      return;
    }

    Object[] states;
    states = new Object[conversions.length];

    for (int i = 0; i < conversions.length; i++) {
      StringConversion conversion;
      conversion = conversions[i];

      states[i] = conversion.startingState();
    }

    for (int codePointIndex = 0, strLength = s.length(); codePointIndex < strLength;) {
      int codePoint;
      codePoint = s.codePointAt(codePointIndex);

      int startingLength;
      startingLength = builder.length();

      int conversionIndex;
      conversionIndex = 0;

      for (; conversionIndex < conversions.length; conversionIndex++) {
        Object state;
        state = states[conversionIndex];

        StringConversion conversion;
        conversion = conversions[conversionIndex];

        state = conversion.executeOne(state, builder, codePoint);

        states[conversionIndex] = state;

        int currentLength;
        currentLength = builder.length();

        if (currentLength == startingLength) {
          break;
        }

        if (currentLength < startingLength) {
          throw new UnsupportedOperationException(
            "Conversions that remove code points are not supported"
          );
        }

        codePoint = builder.codePointAt(startingLength);

        int codePointLength;
        codePointLength = Character.charCount(codePoint);

        int supportedLength;
        supportedLength = startingLength + codePointLength;

        if (currentLength != supportedLength) {
          throw new UnsupportedOperationException(
            "Conversions that add more than one code point are not supported"
          );
        }

        builder.setLength(startingLength);
      }

      if (conversionIndex == conversions.length) {
        builder.appendCodePoint(codePoint);
      }

      codePointIndex += Character.charCount(codePoint);
    }

    for (int i = 0; i < conversions.length; i++) {
      Object state;
      state = states[i];

      StringConversion conversion;
      conversion = conversions[i];

      conversion.executeLastRound(state, builder);
    }
  }

  /**
   * Returns a new string resulting from applying all conversions from this
   * converter to the specified string {@code s}.
   *
   * @param s
   *        a string to be converted
   *
   * @return a new string containing the result of the application of all
   *         conversions from this converter
   *
   * @see #apply(String, StringBuilder)
   */
  public final String convert(String s) {
    StringBuilder builder;
    builder = new StringBuilder();

    apply(s, builder);

    return builder.toString();
  }

}