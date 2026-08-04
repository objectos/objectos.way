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
package objectos.html.format;

import objectos.html.stream.RenderText;
import objectox.html.format.RenderTextFormatterBuilder;

/// Configures the formatting of the [RenderText] event.
public sealed interface RenderTextOptions
    permits
    RenderTextFormatterBuilder {

  /// Sets the indentation style to the specified value. Defaults to
  /// [IndentationStyle#startOfLine] if not set.
  ///
  /// @param value the indentation style value
  void indentationStyle(IndentationStyle value);

  /// Sets the new line style to the specified value. Defaults to
  /// [NewLineStyle#always] if not set.
  ///
  /// @param value the new line style
  void newLineStyle(NewLineStyle value);

}
