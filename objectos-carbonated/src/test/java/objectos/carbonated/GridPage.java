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

import java.io.IOException;
import objectos.css.CssTemplate;
import objectos.css.util.ClassSelector;
import objectos.css.util.Next;

final class GridPage extends AbstractPage {

  private final Grid grid = new Grid(this);

  private final Column col = new Column(this);

  private final ClassSelector gridStyle;

  private final ClassSelector colStyle;

  GridPage() {
    Next next = Next.builder()
        .nameLength(10)
        .build();

    gridStyle = next.classSelector();

    colStyle = next.classSelector();
  }

  public static void main(String[] args) throws IOException {
    TestingCarbon.write("grid.html", GridPage::new);
  }

  @Override
  final void body0() {
    h1("Grid");

    grid.render(
      gridStyle,

      col.sm(2).render(
        colStyle,

        t("A")
      ),

      col.sm(4).md(2).render(
        colStyle,

        t("B")
      )
    );
  }

  @Override
  final void head0() {
    super.head0();

    Styles styles;
    styles = new Styles();

    style(styles.toString());
  }

  private class Styles extends CssTemplate {
    @Override
    protected void definition() {
      style(
        gridStyle,

        backgroundColor(Palette.BLUE_20),
        outline(px(1), dashed, Palette.BLUE_20_HOVER)
      );

      style(
        colStyle,

        backgroundColor(Palette.WHITE),
        boxShadow($0, $0, $0, px(1), Palette.BLUE_20_HOVER),
        minHeight(rem(4))
      );
    }
  }

}