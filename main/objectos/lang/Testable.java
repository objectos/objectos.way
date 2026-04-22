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
package objectos.lang;

/// An object that produces a string representation suitable for testing.
public interface Testable {

  /// Formats this testable instance using the specified formatter instance.
  ///
  /// @param formatter the formatter to use
  void formatTestable(TestableFormatter formatter);

  /// Returns the formatted string representation of this testable instance.
  ///
  /// @return the formatted string representation of this testable instance
  default String toTestableText() {
    final TestableFormatter f;
    f = TestableFormatter.create();

    formatTestable(f);

    return f.toString();
  }

}