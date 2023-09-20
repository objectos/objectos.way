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

import objectos.css.internal.InternalColor;
import objectos.css.tmpl.Api;

/**
 * @since 0.7
 */
public sealed abstract class Color
    extends GeneratedColor
    implements Api.ColorValue
    permits InternalColor {

  protected Color() {}

  public static Color named(String name) {
    return new InternalColor(name.toString());
  }

  public static Color ofHex(String hex) {
    return new InternalColor(hex.toString());
  }

}