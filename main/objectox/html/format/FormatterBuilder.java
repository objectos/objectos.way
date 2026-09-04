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
import objectos.html.format.BeginStartTagOptions;
import objectos.html.format.EndStartTagOptions;
import objectos.html.format.Formatter;
import objectos.html.format.FormatterOptions;
import objectos.html.format.RenderAttributeOptions;
import objectos.html.format.RenderBooleanAttributeOptions;
import objectos.html.format.RenderDoctypeOptions;
import objectos.html.format.RenderEndTagOptions;
import objectos.html.format.RenderJsActionAttributeOptions;
import objectos.html.format.RenderRawOptions;
import objectos.html.format.RenderTextOptions;

public final class FormatterBuilder implements FormatterOptions {

  private final BeginStartTagFormatterBuilder beginStartTagFormatterBuilder = new BeginStartTagFormatterBuilder();

  private final EndStartTagFormatterBuilder endStartTagFormatterBuilder = new EndStartTagFormatterBuilder();

  private final RenderAttributeFormatterBuilder renderAttributeFormatterBuilder = new RenderAttributeFormatterBuilder();

  private final RenderBooleanAttributeFormatterBuilder renderBooleanAttributeFormatterBuilder = new RenderBooleanAttributeFormatterBuilder();

  private final RenderDoctypeFormatterBuilder renderDoctypeFormatterBuilder = new RenderDoctypeFormatterBuilder();

  private final RenderEndTagFormatterBuilder renderEndTagFormatterBuilder = new RenderEndTagFormatterBuilder();

  private final RenderJsActionAttributeFormatterBuilder renderJsActionAttributeFormatterBuilder = new RenderJsActionAttributeFormatterBuilder();

  private final RenderRawFormatterBuilder renderRawFormatterBuilder = new RenderRawFormatterBuilder();

  private final RenderTextFormatterBuilder renderTextFormatterBuilder = new RenderTextFormatterBuilder();

  private String indentationText = "  ";

  private final NewLine newLine = NewLine.LF;

  @Override
  public final void beginStartTag(Consumer<? super BeginStartTagOptions> opts) {
    opts.accept(beginStartTagFormatterBuilder);
  }

  public final Formatter build() {
    final Indentation indentation;
    indentation = new Indentation(indentationText);

    return new FormatterImpl(
        beginStartTagFormatterBuilder.build(indentation),

        endStartTagFormatterBuilder.build(indentation, newLine),

        renderAttributeFormatterBuilder.build(),

        renderBooleanAttributeFormatterBuilder.build(),

        renderDoctypeFormatterBuilder.build(newLine),

        renderEndTagFormatterBuilder.build(indentation, newLine),

        renderJsActionAttributeFormatterBuilder.build(),

        renderRawFormatterBuilder.build(indentation, newLine),

        renderTextFormatterBuilder.build(indentation, newLine)
    );
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

  @Override
  public final void renderAttribute(Consumer<? super RenderAttributeOptions> opts) {
    opts.accept(renderAttributeFormatterBuilder);
  }

  @Override
  public final void renderBooleanAttribute(Consumer<? super RenderBooleanAttributeOptions> opts) {
    opts.accept(renderBooleanAttributeFormatterBuilder);
  }

  @Override
  public final void renderDoctype(Consumer<? super RenderDoctypeOptions> opts) {
    opts.accept(renderDoctypeFormatterBuilder);
  }

  @Override
  public final void renderEndTag(Consumer<? super RenderEndTagOptions> opts) {
    opts.accept(renderEndTagFormatterBuilder);
  }

  @Override
  public final void renderJsActionAttribute(Consumer<? super RenderJsActionAttributeOptions> opts) {
    opts.accept(renderJsActionAttributeFormatterBuilder);
  }

  @Override
  public final void renderRaw(Consumer<? super RenderRawOptions> opts) {
    opts.accept(renderRawFormatterBuilder);
  }

  @Override
  public final void renderText(Consumer<? super RenderTextOptions> opts) {
    opts.accept(renderTextFormatterBuilder);
  }

}
