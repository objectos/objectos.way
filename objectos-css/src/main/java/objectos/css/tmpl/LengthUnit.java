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
package objectos.css.tmpl;

import java.util.Locale;

public enum LengthUnit {
  CH,

  CM,

  EM,

  EX,

  IN,

  MM,

  PC,

  PT,

  PX,

  Q,

  REM,

  VH,

  VMAX,

  VMIN,

  VW;

  private static final LengthUnit[] ARRAY = LengthUnit.values();

  private final String name;

  private LengthUnit() {
    this.name = name().toLowerCase(Locale.US);
  }

  public static LengthUnit getByCode(int code) {
    return ARRAY[code];
  }

  public static int size() {
    return ARRAY.length;
  }

  public final int getCode() {
    return ordinal();
  }

  public final String getName() {
    return name;
  }
}
