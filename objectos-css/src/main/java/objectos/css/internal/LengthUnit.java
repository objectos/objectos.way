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
package objectos.css.internal;

import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
public enum LengthUnit {
  CH("ch"),

  CM("cm"),

  EM("em"),

  EX("ex"),

  IN("in"),

  MM("mm"),

  PC("pc"),

  PT("pt"),

  PX("px"),

  Q("q"),

  REM("rem"),

  VH("vh"),

  VMAX("vmax"),

  VMIN("vmin"),

  VW("vw");

  private static final LengthUnit[] VALUES = values();

  public final String cssName;

  private LengthUnit(String cssName) {
    this.cssName = cssName;
  }

  public static LengthUnit byOrdinal(int ordinal) {
    return VALUES[ordinal];
  }

  @Override
  public final String toString() {
    return cssName;
  }
}