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
import objectos.css.CssTemplate;
import objectos.css.StyleSheet;
import objectos.css.util.ClassSelector;
import objectos.css.util.Next;
import objectos.html.HtmlTemplate;
import objectos.lang.Check;

public final class Impl implements Carbon {

  final ClassSelector THEME_WHITE;

  private final Breakpoints breakpoints;

  private final CompGrid grid;

  private final CompGridColumn gridColumn;

  Impl(ImplBuilder b) {
    Next next;
    next = b.next;

    THEME_WHITE = next.classSelector();

    breakpoints = b.breakpoints;

    grid = b.grid();

    gridColumn = b.gridColumn();
  }

  @Override
  public final Breakpoints breakpoints() {
    return breakpoints;
  }

  @Override
  public final Grid grid(HtmlTemplate parent) {
    Check.state(grid != null, "The grid component is not enabled");

    return grid.new Component(parent);
  }

  @Override
  public final GridColumn gridColumn(HtmlTemplate parent) {
    Check.state(gridColumn != null, "The grid column component is not enabled");

    return gridColumn.new Component(parent);
  }

  @Override
  public final StyleSheet styleSheet() {
    Styles styles;
    styles = new Styles();

    return styles.compile();
  }

  @Override
  public final ClassSelector whiteTheme() {
    return THEME_WHITE;
  }

  private class Styles extends CssTemplate {
    @Override
    protected final void definition() {
      Impl outer;
      outer = Impl.this;

      install(new BaseReset());

      install(new BaseLayout());

      install(new BaseTypography());

      install(new ThemeWhite(outer));

      install(new CompButtonStyles());

      CompGrid g;
      g = outer.grid;

      if (g != null) {
        install(g.new Styles());
      }

      if (gridColumn != null) {
        install(gridColumn.new Styles());
      }

      install(new CompNotificationStyles(breakpoints));
    }
  }

}