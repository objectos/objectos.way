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
package objectos.http.internal;

import java.nio.charset.StandardCharsets;

final class Bytes {

  public static final byte HTAB = '\t';

  public static final byte LF = '\n';

  public static final byte CR = '\r';

  public static final byte SP = ' ';

  public static final byte COLON = ':';

  private Bytes() {}

  public static boolean isOptionalWhitespace(byte value) {
    return switch (value) {
      case SP, HTAB -> true;

      default -> false;
    };
  }

  public static byte[] utf8(String value) {
    return value.getBytes(StandardCharsets.UTF_8);
  }

}