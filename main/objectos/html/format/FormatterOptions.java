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

import java.util.function.Consumer;
import objectos.html.stream.BeginDocument;
import objectos.html.stream.BeginStartTag;
import objectos.html.stream.EndDocument;
import objectos.html.stream.EndStartTag;
import objectox.html.format.FormatterBuilder;

/// Configures the creation of a [Formatter] instance.
public sealed interface FormatterOptions
    permits
    FormatterBuilder {

  /// Sets the [BeginDocument] options.
  ///
  /// @param opts allows for setting the options
  void beginDocument(Consumer<? super BeginDocumentOptions> opts);

  /// Sets the [BeginStartTag] options.
  ///
  /// @param opts allows for setting the options
  void beginStartTag(Consumer<? super BeginStartTagOptions> opts);

  /// Sets the [EndDocument] options.
  ///
  /// @param opts allows for setting the options
  void endDocument(Consumer<? super EndDocumentOptions> opts);

  /// Sets the [EndStartTag] options.
  ///
  /// @param opts allows for setting the options
  void endStartTag(Consumer<? super EndStartTagOptions> opts);

  /// Sets the indentation unit to the specified value. If not set, the
  /// formatter defaults to the `"\u0020\u0020"` string, i.e., two space
  /// characters.
  ///
  /// The string may only contain characters for which the method
  /// [Character#isWhitespace] returns `true`.
  ///
  /// @param value the string to use as indentation unit
  void indentation(String value);

}
