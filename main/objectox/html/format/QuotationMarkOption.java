/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.format;

import objectos.html.format.QuotationMarkStyle;
import objectox.html.markup.CommonEscaper;

public enum QuotationMarkOption implements QuotationMarkStyle {

  DOUBLE('\"', CommonEscaper.create(opts -> {
    opts.add('&', "&amp;");
    opts.add('<', "&lt;");
    opts.add('>', "&gt;");
    opts.add('"', "&quot;");
  })),

  SINGLE('\'', CommonEscaper.create(opts -> {
    opts.add('&', "&amp;");
    opts.add('<', "&lt;");
    opts.add('>', "&gt;");
    opts.add('\'', "&#39;");
  }));

  final char mark;

  private final CommonEscaper escaper;

  private QuotationMarkOption(char mark, CommonEscaper escaper) {
    this.mark = mark;

    this.escaper = escaper;
  }

  public final String escape(String value) {
    return escaper.escape(value);
  }

}
