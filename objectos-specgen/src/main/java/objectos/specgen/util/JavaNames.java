/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.specgen.util;

/**
 * This class provides utility methods to produce valid Java names.
 *
 * @since 2
 */
public final class JavaNames {

  private static final StringConverter IDENTIFIER = StringConverter.create(
    StringConversion.toJavaIdentifier()
  );

  private static final StringConverter LOWER_CAMEL_CASE = StringConverter.create(
    StringConversion.toJavaLowerCamelCase()
  );

  private static final StringConverter UPPER_CAMEL_CASE = StringConverter.create(
    StringConversion.toJavaUpperCamelCase()
  );

  private JavaNames() {}

  public static String toIdentifier(String s) {
    return IDENTIFIER.convert(s);
  }

  /**
   * Returns a valid Java identifier suitable for using as class name.
   *
   * @param s
   *        a string
   *
   * @return a valid Java class name
   */
  public static String toValidClassName(String s) {
    return UPPER_CAMEL_CASE.convert(s);
  }

  /**
   * Returns a valid Java identifier suitable for using as method name.
   *
   * @param s
   *        a string
   *
   * @return a valid Java method name
   */
  public static String toValidMethodName(String s) {
    return LOWER_CAMEL_CASE.convert(s);
  }

}