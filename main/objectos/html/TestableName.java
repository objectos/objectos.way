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
package objectos.html;

import objectox.html.TestableNameCell;
import objectox.html.TestableNameEnum;

/// Represents the name of an Objectos HTML testable node.
public sealed interface TestableName
    permits
    TestableNameCell,
    TestableNameEnum {

  /// A heading 1 testable node name.
  TestableName H1 = TestableNameEnum.H1;

  /// A heading 2 testable node name.
  TestableName H2 = TestableNameEnum.H2;

  /// A heading 3 testable node name.
  TestableName H3 = TestableNameEnum.H3;

  /// A heading 4 testable node name.
  TestableName H4 = TestableNameEnum.H4;

  /// A heading 5 testable node name.
  TestableName H5 = TestableNameEnum.H5;

  /// A heading 6 testable node name.
  TestableName H6 = TestableNameEnum.H6;

  /// A field name testable node name.
  TestableName DD = TestableNameEnum.DD;

  /// A field value testable node name.
  TestableName DT = TestableNameEnum.DT;

  /// Returns a new name for a testable cell of the specified width.
  ///
  /// @param width the fixed width of the cell, must be greater than zero
  ///
  /// @return a newly created testable cell name
  static TestableName cell(int width) {
    if (width <= 0) {
      final String msg;
      msg = "width must be > 0";

      throw new IllegalArgumentException(msg);
    }

    return new TestableNameCell(width);
  }

}
