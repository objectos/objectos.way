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
package objectos.notes.base;

final class Logging {

  private Logging() {}

  public static void abbreviate(StringBuilder out, String source, int length) {
    String result;
    result = source;

    int resultLength;
    resultLength = result.length();

    if (resultLength > length) {
      int start;
      start = resultLength - length;

      result = result.substring(start, resultLength);
    }

    out.append(result);

    int pad;
    pad = length - result.length();

    for (int i = 0; i < pad; i++) {
      out.append(' ');
    }
  }

  public static void pad(StringBuilder out, String source, int length) {
    String result;
    result = source;

    if (result.length() > length) {
      result = result.substring(0, length);
    }

    out.append(result);

    int pad;
    pad = length - result.length();

    for (int i = 0; i < pad; i++) {
      out.append(' ');
    }
  }

}