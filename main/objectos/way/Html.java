/*
 * Copyright (C) 2015-2026 Objectos Software LTDA.
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
package objectos.way;

import java.util.Objects;
import objectox.html.attr.AttributeNamePojo;

/**
 * The <strong>Objectos HTML</strong> main class.
 */
public final class Html {

  private Html() {}

  /// Formats the specified string to be used as an HTML attribute value. More
  /// specifically, this method returns a string whose value is the specified
  /// string:
  ///
  /// - With all leading and trailing [white space][Character#isWhitespace(char)]
  ///   removed. - All other [white space][Character#isWhitespace(char)]
  ///   characters are replaced by the space character (`U+0020`). - All
  ///   sequences of more than one consecutive space characters are normalized to
  ///   a single space character.
  ///
  /// @param value the string to be formatted
  ///
  /// @return the formatted string
  public static String formatAttrValue(String value) {
    Objects.requireNonNull(value, "value == null");

    return AttributeNamePojo.formatAttrValue(value, new StringBuilder());
  }

}
