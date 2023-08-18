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
package objectos.carbonated;

import objectos.css.CssTemplate;
import objectos.css.util.ClassSelector;
import objectos.html.HtmlComponent;
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.Instruction;
import objectos.html.tmpl.Instruction.ElementContents;

public final class Grid extends HtmlComponent {

  private static final ClassSelector GRID = U.cs("grid");
  private static final ClassSelector GRID_STD = U.cs("grid-std");
  private static final ClassSelector GRID_FULL = U.cs("grid-full");
  @SuppressWarnings("unused")
  private static final ClassSelector GRID_NARROW = U.cs("grid-narrow");
  @SuppressWarnings("unused")
  private static final ClassSelector GRID_CONDENSED = U.cs("grid-condensed");

  public Grid(HtmlTemplate parent) {
    super(parent);
  }

  public final ElementContents render(Instruction... columns) {
    ElementContents div;
    div = div(
      GRID,
      GRID_STD,

      flatten(columns)
    );

    reset();

    return div;
  }

  private void reset() {}

  static final class Styles extends CssTemplate {
    private final Breakpoints breakpoints;

    Styles(Breakpoints breakpoints) {
      this.breakpoints = breakpoints;
    }

    @Override
    protected final void definition() {
      style(
        GRID,

        display(grid),
        gridTemplateColumns(repeat(l(4), minmax($0, fr(1)))),
        marginLeft(auto),
        marginRight(auto),
        paddingLeft($0),
        paddingRight($0),
        width(pct(100))
      );

      style(
        GRID_STD,

        maxWidth(breakpoints.max())
      );

      style(
        GRID_FULL,

        maxWidth(pct(100))
      );

      media(
        minWidth(breakpoints.medium()),

        style(
          GRID,

          gridTemplateColumns(repeat(l(8), minmax($0, fr(1)))),
          paddingLeft(rem(1)),
          paddingRight(rem(1))
        )
      );

      media(
        minWidth(breakpoints.large()),

        style(
          GRID,

          gridTemplateColumns(repeat(l(16), minmax($0, fr(1))))
        )
      );

      media(
        minWidth(breakpoints.max()),

        style(
          GRID,

          paddingLeft(rem(1.5)),
          paddingRight(rem(1.5))
        )
      );
    }
  }

}