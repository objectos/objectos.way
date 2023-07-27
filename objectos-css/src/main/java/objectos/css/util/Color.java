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

import objectos.css.tmpl.PropertyValue.ColorValue;

/**
 * @since 0.7
 */
public final class Color extends GeneratedColor implements ColorValue {

  private final String value;

  private Color(String value) {
    this.value = value;
  }

  public static Color named(String name) {
    return new Color(name.toString());
  }

  public static Color ofHex(String hex) {
    return new Color(hex.toString());
  }

  @Override
  public final String toString() {
    return value;
  }

}