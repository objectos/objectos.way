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
import objectos.carbonated.Carbon.GridColumn;

final class GridPage extends AbstractPage {

  GridPage(Carbon carbon) {
    super(carbon);
  }

  public static void main(String[] args) throws IOException {
    TestingCarbon.write("grid.html", GridPage::new);
  }

  @Override
  final void body0() {
    Carbon.Grid grid;
    grid = carbon.grid(this);

    GridColumn col;
    col = carbon.gridColumn(this);

    h1("Grid");

    grid.render(
      col.render(t("A")),
      col.render(t("B")),
      col.render(t("C"))
    );
  }

  @Override
  final void head0() {
    super.head0();
  }

}