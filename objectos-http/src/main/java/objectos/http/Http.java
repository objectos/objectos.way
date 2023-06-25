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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.TimeZone;
import objectos.http.Header.ContentType;
import objectos.lang.Check;

public final class Http {

  public static final char CR = '\r';

  static final byte CR_BYTE = CR;

  public static final String CRLF = "\r\n";

  public static final Method GET = Method.GET;

  public static final char LF = '\n';

  static final byte LF_BYTE = LF;

  public static final Method POST = Method.POST;

  public static final Version V1_0 = Version.V1_0;

  public static final Version V1_1 = Version.V1_1;

  static final char COLON = ':';

  static final int DEFAULT_BUFFER_SIZE = 4096;

  static final char HTAB = '\t';

  static final char SP = ' ';

  static final byte SP_BYTE = SP;

  private Http() {}

  public static HeaderParser<ContentType> createContentTypeParser() {
    return new HeaderContentTypeImpl();
  }

  public static RequestParser createRequestParser(ByteBuffer byteBuffer, CharBuffer charBuffer) {
    return RequestParser.create(byteBuffer, charBuffer);
  }

  public static boolean isTokenChar(char c) {
    if (Character.isAlphabetic(c)) {
      return true;
    }

    if (Character.isDigit(c)) {
      return true;
    }

    switch (c) {
      case '!':
      case '#':
      case '$':
      case '%':
      case '&':
      case '\'':
      case '*':
      case '+':
      case '-':
      case '.':
      case '^':
      case '_':
      case '`':
      case '|':
      case '~':
        return true;
      default:
        return false;
    }
  }

  static int checkBufferSize(int size) {
    Check.argument(size >= 64, "bufferSize minimum value is 64 bytes");

    return size;
  }

  static DateFormat createDateFormat() {
    DateFormat format;
    format = new SimpleDateFormat("EEE, dd LLL yyyy HH:mm:ss zzz", Locale.US);

    TimeZone gmt;
    gmt = TimeZone.getTimeZone("GMT");

    format.setTimeZone(gmt);

    return format;
  }

  static <E> ArrayDeque<E> newArrayDeque(int capacity) {
    return new ArrayDeque<E>(capacity);
  }

  static int toUsAsciiDigit(char c) {
    return c - 48;
  }

}
