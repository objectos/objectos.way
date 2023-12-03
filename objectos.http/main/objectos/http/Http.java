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
import objectox.http.HttpStatus;
import objectox.http.StandardHeaderName;

public final class Http {

  public static final class Header {

    private Header() {}

    public static final HeaderName ACCEPT_ENCODING = StandardHeaderName.ACCEPT_ENCODING;

    public static final HeaderName CONNECTION = StandardHeaderName.CONNECTION;

    public static final HeaderName CONTENT_LENGTH = StandardHeaderName.CONTENT_LENGTH;

    public static final HeaderName CONTENT_TYPE = StandardHeaderName.CONTENT_TYPE;

    public static final HeaderName DATE = StandardHeaderName.DATE;

    public static final HeaderName HOST = StandardHeaderName.HOST;

    public static final HeaderName LOCATION = StandardHeaderName.LOCATION;

    public static final HeaderName SET_COOKIE = StandardHeaderName.SET_COOKIE;

    public static final HeaderName TRANSFER_ENCODING = StandardHeaderName.TRANSFER_ENCODING;

    public static final HeaderName USER_AGENT = StandardHeaderName.USER_AGENT;

  }

  /**
   * The HTTP methods.
   */
  public enum Method {

    /**
     * The CONNECT method.
     */
    CONNECT,

    /**
     * The DELETE method.
     */
    DELETE,

    /**
     * The GET method.
     */
    GET,

    /**
     * The HEAD method.
     */
    HEAD,

    /**
     * The OPTIONS method.
     */
    OPTIONS,

    /**
     * The PATCH method.
     */
    PATCH,

    /**
     * The POST method.
     */
    POST,

    /**
     * The PUT method.
     */
    PUT,

    /**
     * The TRACE method.
     */
    TRACE;

  }

  public sealed interface Status permits objectox.http.HttpStatus {

    Status OK_200 = HttpStatus.OK;

    Status MOVED_PERMANENTLY_301 = HttpStatus.MOVED_PERMANENTLY;

    Status FOUND_302 = HttpStatus.FOUND;

    Status SEE_OTHER_303 = HttpStatus.SEE_OTHER;

    Status NOT_FOUND_404 = HttpStatus.NOT_FOUND;

    Status METHOD_NOT_ALLOWED_405 = HttpStatus.METHOD_NOT_ALLOWED;

    Status UNSUPPORTED_MEDIA_TYPE_415 = HttpStatus.UNSUPPORTED_MEDIA_TYPE;

    Status UNPROCESSABLE_CONTENT_422 = HttpStatus.UNPROCESSABLE_CONTENT;

    Status INTERNAL_SERVER_ERROR_500 = HttpStatus.INTERNAL_SERVER_ERROR;

    int code();

    String description();

  }

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

  private Http() {}

  public static String formatDate(ZonedDateTime date) {
    ZonedDateTime normalized;
    normalized = date.withZoneSameInstant(ZoneOffset.UTC);

    return IMF_FIXDATE.format(normalized);
  }

}