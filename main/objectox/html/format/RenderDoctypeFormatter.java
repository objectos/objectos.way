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

import java.io.IOException;
import objectos.html.format.NewLineStyle;
import objectos.html.format.TextCaseStyle;
import objectos.html.stream.RenderDoctype;

final class RenderDoctypeFormatter {

  private final NewLine newLine;

  final TextCaseStyle doctypeTextCaseStyle;

  final NewLineStyle newLineStyle;

  RenderDoctypeFormatter(NewLine newLine, TextCaseStyle doctypeTextCaseStyle, NewLineStyle newLineStyle) {
    this.newLine = newLine;

    this.doctypeTextCaseStyle = doctypeTextCaseStyle;

    this.newLineStyle = newLineStyle;
  }

  public final void format(RenderDoctype event, Output out) throws IOException {
    final String value;
    value = doctype(doctypeTextCaseStyle);

    out.append(value);

    newLine.notEmpty(newLineStyle, out);
  }

  private String doctype(TextCaseStyle style) {
    return switch (style) {
      case TextCaseOption.LOWER_CASE -> "<!doctype html>";

      case TextCaseOption.UPPER_CASE -> "<!DOCTYPE html>";
    };
  }

}
