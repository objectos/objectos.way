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

import objectox.html.format.TextCaseOption;

/// Configures the text case emitted during formatting.
public sealed interface TextCaseStyle
    permits
    TextCaseOption {

  /// Returns a style that emits text in lower case.
  ///
  /// @return a style that emits text in lower case
  static TextCaseStyle lowerCase() {
    return TextCaseOption.LOWER_CASE;
  }

  /// Returns a style that emits text in upper case.
  ///
  /// @return a style that emits text in upper case
  static TextCaseStyle upperCase() {
    return TextCaseOption.UPPER_CASE;
  }

}
