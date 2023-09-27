/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
import java.util.Locale;
import java.util.Map;
import objectos.http.internal.HeaderName;
import objectos.http.internal.HttpMethod;
import objectos.http.internal.HttpStatus;

public final class Http {

  public static final class Header {

    private Header() {}

    public interface Name {
      String capitalized();
    }

    public interface Value {

      Value NULL = new Value() {
        @Override
        public final boolean contentEquals(CharSequence cs) { return false; }
      };

      boolean contentEquals(CharSequence cs);

    }

    public static final Name ACCEPT_ENCODING = HeaderName.ACCEPT_ENCODING;

    public static final Name CONNECTION = HeaderName.CONNECTION;

    public static final Name CONTENT_LENGTH = HeaderName.CONTENT_LENGTH;

    public static final Name CONTENT_TYPE = HeaderName.CONTENT_TYPE;

    public static final Name DATE = HeaderName.DATE;

    public static final Name HOST = HeaderName.HOST;

    public static final Name LOCATION = HeaderName.LOCATION;

    public static final Name TRANSFER_ENCODING = HeaderName.TRANSFER_ENCODING;

    public static final Name USER_AGENT = HeaderName.USER_AGENT;

  }

  public sealed interface Method permits objectos.http.internal.HttpMethod {

    Method GET = HttpMethod.GET;

    Method POST = HttpMethod.POST;

  }

  public sealed interface Status permits objectos.http.internal.HttpStatus {

    Status OK_200 = HttpStatus.OK;

    Status INTERNAL_SERVER_ERROR_500 = HttpStatus.INTERNAL_SERVER_ERROR;

    Status SEE_OTHER_303 = HttpStatus.SEE_OTHER;

    Status NOT_FOUND_404 = HttpStatus.NOT_FOUND;

    Status METHOD_NOT_ALLOWED_405 = HttpStatus.METHOD_NOT_ALLOWED;

    Status UNSUPPORTED_MEDIA_TYPE_415 = HttpStatus.UNSUPPORTED_MEDIA_TYPE;

    Status UNPROCESSABLE_CONTENT_422 = HttpStatus.UNPROCESSABLE_CONTENT;

    int code();

    String description();

  }

  private static final DateTimeFormatter IMF_FIXDATE;

  static {
    DateTimeFormatterBuilder b = new DateTimeFormatterBuilder();

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

  private Http() {}

  public static String formatDate(ZonedDateTime date) {
    ZonedDateTime normalized;
    normalized = date.withZoneSameInstant(ZoneOffset.UTC);

    return IMF_FIXDATE.format(normalized);
  }

}