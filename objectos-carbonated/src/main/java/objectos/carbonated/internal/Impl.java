/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.carbonated.internal;

import objectos.carbonated.Carbon;
import objectos.css.StyleSheet;
import objectos.css.util.ClassSelector;
import objectos.css.util.Next;
import objectos.html.HtmlTemplate;

public final class Impl implements Carbon {

  final ClassSelector THEME_WHITE;

  // Grid

  final ClassSelector GRID;
  final ClassSelector GRID_STD;
  final ClassSelector GRID_FULL;
  final ClassSelector GRID_NARROW;
  final ClassSelector GRID_CONDENSED;

  // GridColumn

  final ClassSelector COLUMN;

  final ClassSelector SPAN_0;
  final ClassSelector SPAN_1;
  final ClassSelector SPAN_2;
  final ClassSelector SPAN_3;
  final ClassSelector SPAN_4;
  final ClassSelector SPAN_5;
  final ClassSelector SPAN_6;
  final ClassSelector SPAN_7;
  final ClassSelector SPAN_8;
  final ClassSelector SPAN_9;
  final ClassSelector SPAN_10;
  final ClassSelector SPAN_11;
  final ClassSelector SPAN_12;
  final ClassSelector SPAN_13;
  final ClassSelector SPAN_14;
  final ClassSelector SPAN_15;
  final ClassSelector SPAN_16;

  Impl(ImplBuilder b) {
    Next next;
    next = b.next;

    THEME_WHITE = next.classSelector();

    // Grid

    GRID = next.classSelector();
    GRID_STD = next.classSelector();
    GRID_FULL = next.classSelector();
    GRID_NARROW = next.classSelector();
    GRID_CONDENSED = next.classSelector();

    // GridColumn

    COLUMN = next.classSelector();

    SPAN_0 = next.classSelector();
    SPAN_1 = next.classSelector();
    SPAN_2 = next.classSelector();
    SPAN_3 = next.classSelector();
    SPAN_4 = next.classSelector();
    SPAN_5 = next.classSelector();
    SPAN_6 = next.classSelector();
    SPAN_7 = next.classSelector();
    SPAN_8 = next.classSelector();
    SPAN_9 = next.classSelector();
    SPAN_10 = next.classSelector();
    SPAN_11 = next.classSelector();
    SPAN_12 = next.classSelector();
    SPAN_13 = next.classSelector();
    SPAN_14 = next.classSelector();
    SPAN_15 = next.classSelector();
    SPAN_16 = next.classSelector();
  }

  @Override
  public final Grid grid(HtmlTemplate parent) {
    return new CompGrid(parent, this);
  }

  @Override
  public final GridColumn gridColumn(HtmlTemplate parent) {
    return new CompGridColumn(parent, this);
  }

  @Override
  public final StyleSheet styleSheet() {
    ImplStyles styles;
    styles = new ImplStyles(this);

    return styles.compile();
  }

  @Override
  public final ClassSelector whiteTheme() {
    return THEME_WHITE;
  }

}