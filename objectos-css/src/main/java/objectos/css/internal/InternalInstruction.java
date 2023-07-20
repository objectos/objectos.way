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

import objectos.css.om.MediaFeatureOrStyleDeclaration;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleRule;
import objectos.css.tmpl.ColorValue;
import objectos.css.tmpl.Length;
import objectos.css.tmpl.Percentage;
import objectos.css.tmpl.StringLiteral;
import objectos.css.tmpl.Url;

public enum InternalInstruction
    implements
    ColorValue,
    Length,
    MediaFeatureOrStyleDeclaration,
    Percentage,
    Selector,
    StringLiteral,
    StyleDeclaration,
    StyleRule,
    Url {

  INSTANCE(-1),

  COLOR_HEX(3),

  LENGTH_DOUBLE(10),

  LENGTH_INT(6),

  PERCENTAGE_DOUBLE(9),

  PERCENTAGE_INT(5),

  STRING_LITERAL(3),

  URL(3);

  public final int length;

  private InternalInstruction(int length) {
    this.length = length;
  }

}