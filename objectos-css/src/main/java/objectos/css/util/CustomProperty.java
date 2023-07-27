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
package objectos.css.util;

import objectos.css.om.PropertyValue;

public class CustomProperty<T extends PropertyValue> {

  public final String cssName;

  private CustomProperty(String cssName) {
    this.cssName = cssName;
  }

  public static <T extends PropertyValue> CustomProperty<T> named(String name) {
    int length;
    length = name.length();

    if (length < 3) {
      throw invalidName(name);
    }

    if (name.charAt(0) != '-') {
      throw invalidName(name);
    }

    if (name.charAt(1) != '-') {
      throw invalidName(name);
    }

    for (int index = 2; index < length; index++) {
      char c;
      c = name.charAt(index);

      if (c == '-') {
        continue;
      }

      if (c < '0') {
        throw invalidName(name);
      }

      if (c <= '9') {
        continue;
      }

      if (c < 'A') {
        throw invalidName(name);
      }

      if (c <= 'Z') {
        continue;
      }

      if (c == '_') {
        continue;
      }

      if (c < 'a') {
        throw invalidName(name);
      }

      if (c <= 'z') {
        continue;
      }

      throw invalidName(name);
    }

    return new CustomProperty<>(name);
  }

  private static IllegalArgumentException invalidName(String name) {
    return new IllegalArgumentException(name + " is not a valid CSS custom property name");
  }

  @Override
  public final String toString() {
    return cssName;
  }

}