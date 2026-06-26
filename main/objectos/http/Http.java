/*
 * Copyright (C) 2016-2026 Objectos Software LTDA.
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
package objectos.http;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/// Provides helpers to declaring routes.
final class Http {

  static final String LINE_TERM = "Invalid line terminator.\n";

  static final String REQ_LINE = "Invalid request line.\n";

  // exception types

  /**
   * Thrown to indicate that a content type is not supported.
   */
  static final class UnsupportedMediaTypeException extends RuntimeException {

    private static final long serialVersionUID = -6412173093510319276L;

    private final String unsupportedMediaType;

    private final List<String> supportedMediaTypes;

    /**
     * Creates a new {@code UnsupportedMediaTypeException}.
     *
     * @param unsupportedMediaType
     *        the name of the media type such as {@code application/pdf} or
     *        {@code image/gif} that caused this exception to be thrown
     *
     * @param supportedMediaTypes
     *        the names of the supported media types such as
     *        {@code application/json}
     */
    public UnsupportedMediaTypeException(String unsupportedMediaType, String... supportedMediaTypes) {
      super((String) null);

      this.unsupportedMediaType = unsupportedMediaType;

      if (supportedMediaTypes.length == 0) {
        throw new IllegalArgumentException("At least one media type is required");
      }

      this.supportedMediaTypes = List.of(supportedMediaTypes);
    }

    @Override
    public final String getMessage() {
      if (unsupportedMediaType != null) {
        return "Supports " + supportedMediaTypes.stream().collect(Collectors.joining(", ")) + " but got " + unsupportedMediaType;
      } else {
        return "Supports " + supportedMediaTypes.stream().collect(Collectors.joining(", ")) + " but Content-Type was not specified";
      }
    }

    /**
     * Returns the name of the unsupported media type.
     *
     * @return the name of the unsupported media type.
     */
    public final String unsupportedMediaType() {
      return unsupportedMediaType;
    }

    /**
     * Returns the names of the supported media types.
     *
     * @return the names of the supported media types.
     */
    public final List<String> supportedMediaTypes() {
      return supportedMediaTypes;
    }

  }

  private Http() {}

  /**
   * Formats a date so it can be used as the value of a {@code Date} HTTP
   * header.
   *
   * @param date
   *        the date to be formatted
   *
   * @return the formatted date
   */
  static String formatDate(ZonedDateTime date) {
    ZonedDateTime normalized;
    normalized = date.withZoneSameInstant(ZoneOffset.UTC);

    return IMF_FIXDATE.format(normalized);
  }

  // utils

  private static final DateTimeFormatter IMF_FIXDATE;

  static {
    DateTimeFormatterBuilder b;
    b = new DateTimeFormatterBuilder();

    Map<Long, String> dow;
    dow = new HashMap<>();

    dow.put(1L, "Mon");
    dow.put(2L, "Tue");
    dow.put(3L, "Wed");
    dow.put(4L, "Thu");
    dow.put(5L, "Fri");
    dow.put(6L, "Sat");
    dow.put(7L, "Sun");

    b.appendText(ChronoField.DAY_OF_WEEK, dow);

    b.appendLiteral(", ");

    b.appendValue(ChronoField.DAY_OF_MONTH, 2);

    b.appendLiteral(' ');

    Map<Long, String> moy;
    moy = new HashMap<>();

    moy.put(1L, "Jan");
    moy.put(2L, "Feb");
    moy.put(3L, "Mar");
    moy.put(4L, "Apr");
    moy.put(5L, "May");
    moy.put(6L, "Jun");
    moy.put(7L, "Jul");
    moy.put(8L, "Aug");
    moy.put(9L, "Sep");
    moy.put(10L, "Oct");
    moy.put(11L, "Nov");
    moy.put(12L, "Dec");

    b.appendText(ChronoField.MONTH_OF_YEAR, moy);

    b.appendLiteral(' ');

    b.appendValue(ChronoField.YEAR, 4);

    b.appendLiteral(' ');

    b.appendValue(ChronoField.HOUR_OF_DAY, 2);

    b.appendLiteral(':');

    b.appendValue(ChronoField.MINUTE_OF_HOUR, 2);

    b.appendLiteral(':');

    b.appendValue(ChronoField.SECOND_OF_MINUTE, 2);

    b.appendLiteral(' ');

    b.appendOffset("+HHMM", "GMT");

    IMF_FIXDATE = b.toFormatter(Locale.US);
  }

  static int parseHexDigit(byte value) {
    return parseHexDigit(value);
  }

  static int parseHexDigit(int value) {
    return switch (value) {
      case '0' -> 0;
      case '1' -> 1;
      case '2' -> 2;
      case '3' -> 3;
      case '4' -> 4;
      case '5' -> 5;
      case '6' -> 6;
      case '7' -> 7;
      case '8' -> 8;
      case '9' -> 9;
      case 'a', 'A' -> 10;
      case 'b', 'B' -> 11;
      case 'c', 'C' -> 12;
      case 'd', 'D' -> 13;
      case 'e', 'E' -> 14;
      case 'f', 'F' -> 15;

      default -> throw new IllegalArgumentException(
          "Illegal hex char= " + (char) value
      );
    };
  }

  @SuppressWarnings("unchecked")
  static String queryParamsToString(Map<String, Object> params, Function<String, String> processor) {
    StringBuilder builder;
    builder = new StringBuilder();

    int count;
    count = 0;

    for (String key : params.keySet()) {
      if (count++ > 0) {
        builder.append('&');
      }

      queryParamsToStringAppend(builder, processor, key);

      builder.append('=');

      Object existing;
      existing = params.get(key);

      if (existing instanceof String s) {
        queryParamsToStringAppend(builder, processor, s);
      }

      else {
        List<String> list;
        list = (List<String>) existing;

        String value;
        value = list.get(0);

        queryParamsToStringAppend(builder, processor, value);

        for (int i = 1; i < list.size(); i++) {
          builder.append('&');

          queryParamsToStringAppend(builder, processor, key);

          builder.append('=');

          value = list.get(i);

          queryParamsToStringAppend(builder, processor, value);
        }
      }
    }

    return builder.toString();
  }

  private static void queryParamsToStringAppend(StringBuilder builder, Function<String, String> processor, String value) {
    String processed;
    processed = processor.apply(value);

    builder.append(processed);
  }

  static String pathDelim() {
    return "/-.";
  }

}
