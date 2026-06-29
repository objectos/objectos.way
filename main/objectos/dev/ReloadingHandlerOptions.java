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
package objectos.dev;

import java.nio.file.Path;
import java.util.function.Predicate;
import objectos.way.Note;
import objectox.dev.ReloadingHandlerBuilder;

/// Configures the creation of a `ReloadingHandler`.
public sealed interface ReloadingHandlerOptions permits ReloadingHandlerBuilder {

  /// Reloads the module when changes are observed in the specified directory.
  ///
  /// @param value the directory to watch
  ///
  /// @throws IllegalArgumentException if the path does not represent a directory
  void directory(Path value);

  /// Sets the module to be reloaded to the one from the specified class.
  ///
  /// @param value the class whose module is to be reloaded
  void moduleOf(Class<?> value);

  /// Sets the note sink to the specified value.
  ///
  /// @param value a note sink instance
  void noteSink(Note.Sink value);

  /// Uses the specified function to recreate the HTTP handler instance after
  /// filesystem changes are processed.
  ///
  /// @param value the reloading function
  void reloadingFunction(ReloadingFunction value);

  /// Uses the specified predicate to decide if a class of a given binary name
  /// should be reloaded or not. If no predicate is specified, then it tries to
  /// reload the class of any binary name requested.
  ///
  /// @param value the predicate instance
  void withBinaryName(Predicate<? super String> value);

}
