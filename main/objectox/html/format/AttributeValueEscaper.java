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

import objectox.html.markup.CommonEscaper;

public final class AttributeValueEscaper {

  private static final CommonEscaper ESCAPER = CommonEscaper.create(opts -> {
    opts.add('&', "&amp;");
    opts.add('<', "&lt;");
    opts.add('>', "&gt;");
    opts.add('"', "&quot;");
    opts.add('\'', "&#39;");
  });

  private final String value;

  public AttributeValueEscaper(String value) {
    this.value = value;
  }

  public final String escape() {
    return ESCAPER.escape(value);
  }

}
