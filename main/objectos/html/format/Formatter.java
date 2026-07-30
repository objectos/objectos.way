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

import java.io.IOException;
import java.util.function.Consumer;
import objectos.html.stream.Event;
import objectox.html.format.FormatterBuilder;
import objectox.html.format.FormatterPojo;

/// Formats the string representation of a compiled Objectos HTML document.
public sealed interface Formatter permits FormatterPojo {

  /// Returns a new formatter instance with the specified options.
  ///
  /// @param opts allows for setting the options
  ///
  /// @return a new formatter instance with the specified options
  static Formatter create(Consumer<? super FormatterOptions> opts) {
    final FormatterBuilder builder;
    builder = new FormatterBuilder();

    opts.accept(builder);

    return builder.build();
  }

  /// Returns a new formatter instance with the default options.
  ///
  /// @return a new formatter instance with the default options
  static Formatter standard() {
    return create(_ -> {});
  }

  /// Formats an event to an `Appendable` using this formatter.
  ///
  /// @param event the event to format, not `null`
  /// @param out the appendable to format to, not `null`
  ///
  /// @throws IOException if an I/O error occurs
  void formatTo(Event event, Appendable out) throws IOException;

}
