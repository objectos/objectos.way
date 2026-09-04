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

import objectos.html.format.IndentationStyle;
import objectos.html.format.NewLineStyle;
import objectos.html.stream.RenderText;
import objectox.html.markup.CommonEscaper;

final class RenderTextFormatter {

  private static final CommonEscaper ESCAPER = CommonEscaper.create(opts -> {
    opts.add('&', "&amp;");
    opts.add('<', "&lt;");
    opts.add('>', "&gt;");
  });

  private final Indentation indentation;

  final IndentationStyle indentationStyle;

  private final NewLine newLine;

  final NewLineStyle newLineStyle;

  RenderTextFormatter(Indentation indentation, IndentationStyle indentationStyle, NewLine newLine, NewLineStyle newLineStyle) {
    this.indentation = indentation;

    this.indentationStyle = indentationStyle;

    this.newLine = newLine;

    this.newLineStyle = newLineStyle;
  }

  public final void format(RenderText event, Output out) {
    indentation.format(indentationStyle, out);

    final String value;
    value = event.value();

    final String escaped;
    escaped = ESCAPER.escape(value);

    out.append(escaped);

    newLine.notEmpty(newLineStyle, out);
  }

}
