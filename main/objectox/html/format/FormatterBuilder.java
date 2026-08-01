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

import java.util.Objects;
import java.util.function.Consumer;
import objectos.html.format.BeginDocumentOptions;
import objectos.html.format.BeginStartTagOptions;
import objectos.html.format.EndDocumentOptions;
import objectos.html.format.EndStartTagOptions;
import objectos.html.format.Formatter;
import objectos.html.format.FormatterOptions;

public final class FormatterBuilder implements FormatterOptions {

  private final BeginDocumentFormatterBuilder beginDocumentFormatterBuilder = new BeginDocumentFormatterBuilder();

  private final BeginStartTagFormatterBuilder beginStartTagFormatterBuilder = new BeginStartTagFormatterBuilder();

  private final EndDocumentFormatterBuilder endDocumentFormatterBuilder = new EndDocumentFormatterBuilder();

  private final EndStartTagFormatterBuilder endStartTagFormatterBuilder = new EndStartTagFormatterBuilder();

  private String indentationText;

  private final NewLine newLine = NewLine.LF;

  public final Formatter build() {
    final Indentation indentation;
    indentation = new Indentation(indentationText);

    return new FormatterPojo(
        beginDocumentFormatterBuilder.build(newLine),

        beginStartTagFormatterBuilder.build(indentation),

        endDocumentFormatterBuilder.build(newLine),

        endStartTagFormatterBuilder.build(indentation, newLine)
    );
  }

  @Override
  public final void beginDocument(Consumer<? super BeginDocumentOptions> opts) {
    opts.accept(beginDocumentFormatterBuilder);
  }

  @Override
  public final void beginStartTag(Consumer<? super BeginStartTagOptions> opts) {
    opts.accept(beginStartTagFormatterBuilder);
  }

  @Override
  public final void endDocument(Consumer<? super EndDocumentOptions> opts) {
    opts.accept(endDocumentFormatterBuilder);
  }

  @Override
  public final void endStartTag(Consumer<? super EndStartTagOptions> opts) {
    opts.accept(endStartTagFormatterBuilder);
  }

  @Override
  public final void indentation(String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    if (v.isEmpty()) {
      final String msg;
      msg = "Invalid indentation: it may not be empty";

      throw new IllegalArgumentException(msg);
    }

    final boolean allWhitespace;
    allWhitespace = v.chars().allMatch(Character::isWhitespace);

    if (!allWhitespace) {
      final String msg;
      msg = "Invalid indentation: it may onlhy contain whitespace characters";

      throw new IllegalArgumentException(msg);
    }

    indentationText = v;
  }

}
