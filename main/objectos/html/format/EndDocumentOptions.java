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

import objectos.html.stream.EndDocument;
import objectox.html.format.EndDocumentFormatterBuilder;

/// Configures the formatting of the [EndDocument] event.
public sealed interface EndDocumentOptions
    permits
    EndDocumentFormatterBuilder {

  /// Emits the specified comment on an event. If the specified value is the
  /// empty string, then the comment markup is not emitted.
  ///
  /// @param value the comment, not null
  void comment(String value);

  /// Sets the new line style to the specified value. Defaults to
  /// [NewLineStyle#ifNotEmpty] if not set.
  ///
  /// @param value the new line style, not null
  void newLineStyle(NewLineStyle value);

}
