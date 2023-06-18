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

import objectos.css.om.Selector;
import objectos.css.tmpl.BoxSizingValue;
import objectos.css.tmpl.Color;
import objectos.css.tmpl.GlobalKeyword;
import objectos.css.tmpl.LineHeightValue;
import objectos.css.tmpl.LineStyle;
import objectos.css.tmpl.LineWidth;
import objectos.css.tmpl.NoneKeyword;
import objectos.css.tmpl.TextSizeAdjustValue;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
public final class NamedElement implements Selector,
    BoxSizingValue,
    Color,
    GlobalKeyword,
    LineHeightValue,
    LineStyle,
    LineWidth,
    TextSizeAdjustValue,
    NoneKeyword {
  private final String name;

  public NamedElement(String name) {
    this.name = name;
  }

  @Override
  public final String toString() {
    return name;
  }
}
