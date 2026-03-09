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

/// An object that contributes to the configuration of a
/// [style sheet][StyleSheet] generation.
@FunctionalInterface
public interface CssLibrary {

  /// Contributes to the configuration of a `StyleSheet` generation.
  sealed interface Options permits StyleSheet.Options {

    /// Accepts the specified values as CSS property names.
    ///
    /// @param values the property names
    void cssPropertyNames(String... values);

    /// Scans the class file of the specified class (if found) during the CSS
    /// generation process.
    ///
    /// @param value the class to scan for CSS utilities
    void scanClass(Class<?> value);

    /// Scans the class files of the specified classes (if found) during the CSS
    /// generation process.
    ///
    /// @param values the classes to scan for CSS utilities
    void scanClasses(Class<?>... values);

    //
    // THEME
    //

    /// Adds the specified CSS to the `theme` layer.
    ///
    /// @param value the CSS
    void theme(String value);

    //
    // COMPONENTS
    //

    /// Adds the specified CSS to the `components` layer.
    ///
    /// @param value the CSS
    void components(String value);

    //
    // VARIANT
    //

    /// Creates the specified custom variants.
    ///
    /// Custom variants are sorted in the generated CSS in the same order as they
    /// are declared in the configuration.
    ///
    /// @param value the variants definition
    void variants(String value);

  }

  /// Sets the configuration of this `Library` instance.
  ///
  /// @param options allows for setting the options
  void configure(Options options);

}