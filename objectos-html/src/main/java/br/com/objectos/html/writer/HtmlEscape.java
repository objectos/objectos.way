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
package br.com.objectos.html.writer;

public final class HtmlEscape {

  private HtmlEscape() {}

  public static void to(String source, StringBuilder out) {
    for (int i = 0, len = source.length(); i < len; i++) {
      var c = source.charAt(i);

      switch (c) {
        case '"' -> out.append("&quot;");
        case '&' -> out.append("&amp;");
        case '<' -> out.append("&lt;");
        case '>' -> out.append("&gt;");
        case '\u00A9' -> out.append("&copy;");
        default -> out.append(c);
      }
    }
  }

}