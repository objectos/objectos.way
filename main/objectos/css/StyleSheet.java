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
package objectos.css;

import module java.base;
import module objectos.way;

/// A style sheet whose content is generated on demand by scanning Java class
/// files for CSS utilities.
public sealed interface StyleSheet extends Media.Text permits CssEngine {

  /// Configures the creation of a `StyleSheet` instance.
  sealed interface Options extends CssLibrary.Options permits CssEngine.Configuring {

    /// Includes the specified library. In other words, uses the configuration
    /// contributed by the specified library in this generation.
    ///
    /// @param value the library whose configuration is to be included
    void include(CssLibrary value);

    /// Uses the specified note sink during generation.
    ///
    /// @param value the note sink to use
    void noteSink(Note.Sink value);

    /// Scans the specified directory recursively for
    /// [`Source`][CssSource] annotated Java class files during the CSS
    /// generation process.
    ///
    /// @param value the directory to be scanned
    void scanDirectory(Path value);

    /// Scans the JAR file of the specified class (if found) for
    /// [`Source`][CssSource] annotated Java class files during the CSS
    /// generation process.
    ///
    /// @param value the class whose JAR file will be scanned
    void scanJarFileOf(Class<?> value);

    //
    // THEME
    //

    /// Replaces the system `theme` layer contents with the specified CSS.
    ///
    /// @param value the CSS
    void systemTheme(String value);

    //
    // BASE
    //

    /// Replaces the system `base` layer contents with the specified CSS.
    ///
    /// @param value the CSS
    void systemBase(String value);

    //
    // VARIANT
    //

    /// Replaces the system variants definition with the specified value.
    ///
    /// @param value the system variants definition
    void systemVariants(String value);

  }

  /// Creates a new `StyleSheet` instance with the specified options.
  ///
  /// @param options allows for setting the options
  ///
  /// @return a new `StyleSheet` instance
  static StyleSheet create(Consumer<? super Options> options) {
    return CssEngine.create0(options);
  }

  /// Returns `text/css; charset=utf-8`.
  ///
  /// @return `@code text/css; charset=utf-8`
  @Override
  String contentType();

  /// Returns `StandardCharsets.UTF_8`.
  ///
  /// @return `StandardCharsets.UTF_8`
  @Override
  Charset charset();

  /**
   * Scans the configured Java class files for CSS utilities and returns the
   * generated CSS.
   *
   * @return the generated CSS
   */
  String generate();

  /**
   * Scans the configured Java class files for CSS utilities and writes the
   * resulting CSS to the specified {@code Appendable}.
   *
   * @param out
   *        where to append the generated CSS
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  void writeTo(Appendable out) throws IOException;

}