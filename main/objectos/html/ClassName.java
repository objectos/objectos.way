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

import java.util.Objects;
import objectos.way.Html;
import objectox.html.attr.AttributeNamePojo;
import objectox.html.attr.ClassNamePojo;

/// Represents an HTML {@code class} attribute and its value.
public sealed interface ClassName extends AttributeObject permits ClassNamePojo {

  /**
   * Creates a new {@code ClassName} instance whose value is the result of
   * joining the value of each of the specified {@code ClassName} instances
   * around the space character.
   *
   * @param values
   *        the {@code ClassName} instances to be joined into a single value
   *
   * @return a newly constructed {@code ClassName} instance
   */
  static ClassName of(ClassName... values) {
    StringBuilder sb;
    sb = new StringBuilder();

    for (int i = 0, len = values.length; i < len; i++) {
      if (i != 0) {
        sb.append(' ');
      }

      ClassName cn;
      cn = values[i];

      String value;
      value = cn.attrValue();

      sb.append(value);
    }

    String value;
    value = sb.toString();

    return new ClassNamePojo(value);
  }

  /**
   * Creates a new {@code ClassName} instance with the specified value.
   *
   * @param value
   *        the value of this HTML {@code class} attribute
   *
   * @return a newly constructed {@code ClassName} instance
   */
  static ClassName of(String value) {
    Objects.requireNonNull(value, "value == null");

    return new ClassNamePojo(value);
  }

  /**
   * Creates a new {@code ClassName} instance by processing the specified
   * value.
   *
   * <p>
   * This method is designed to work with Java text blocks. It first removes
   * any leading and trailing whitespace. Additionally, any sequence of
   * consecutive whitespace characters is replaced by a single space
   * character.
   *
   * <p>
   * For example, creating an instance like the following:
   *
   * <pre>{@code
   * Html.ClassName.ofText("""
   *     first \tsecond
   *       third\r
   *
   *     fourth
   *     """);
   * }</pre>
   *
   * <p>
   * Produces the same result as creating an instance with the
   * {@code "first second third fourth"} string literal.
   *
   * @param value
   *        the text block containing class names, possibly spread across
   *        multiple lines
   *
   * @return a newly constructed {@code ClassName} instance
   */
  static ClassName ofText(String value) {
    String result;
    result = Html.formatAttrValue(value);

    return new ClassNamePojo(result);
  }

  /**
   * The {@code class} attribute name.
   *
   * @return the {@code class} attribute name
   */
  @Override
  default AttributeName attrName() {
    return AttributeNamePojo.CLASS;
  }

  /**
   * The {@code class} value.
   *
   * @return the {@code class} value
   */
  @Override
  String attrValue();

}