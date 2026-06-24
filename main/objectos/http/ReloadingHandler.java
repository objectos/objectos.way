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
package objectos.http;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;
import objectox.http.handler.ReloadingHandlerBuilder;
import objectox.http.handler.ReloadingHandlerPojo;

/// An HTTP handler which reloads the classes of the configured module if
/// changes are observed in the module's location.
public sealed interface ReloadingHandler extends Closeable, Handler permits ReloadingHandlerPojo {

  /// Creates a new reloading handler with the specified options.
  ///
  /// @param opts allows for setting the options
  ///
  /// @return a newly created reloading handler
  ///
  /// @throws IOException if an I/O error occurs
  static ReloadingHandler create(Consumer<? super ReloadingHandlerOptions> opts) throws IOException {
    final ReloadingHandlerBuilder builder;
    builder = new ReloadingHandlerBuilder();

    opts.accept(builder);

    return builder.build();
  }

  /// Closes this reloading handler.
  ///
  /// @throws IOException if an I/O error occurs
  @Override
  void close() throws IOException;

}
