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

import objectos.carbonated.internal.CompGrid;
import objectos.carbonated.internal.CompGridColumn;
import objectos.carbonated.internal.Impl;
import objectos.carbonated.internal.ImplBuilder;
import objectos.css.StyleSheet;
import objectos.css.util.ClassSelector;
import objectos.css.util.Next;
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.Instruction.ElementContents;

/**
 * @since 0.7.1
 */
public sealed interface Carbon permits Impl {

  sealed interface Builder permits ImplBuilder {

    Carbon build();

    Builder next(Next value);

  }

  /**
   * The grid component.
   */
  sealed interface Grid permits CompGrid {

    HtmlTemplate render(ElementContents... contents);

  }

  /**
   * A column of the grid.
   */
  sealed interface GridColumn permits CompGridColumn {

    HtmlTemplate render(ElementContents... contents);

  }

  static Builder builder() {
    return new ImplBuilder();
  }

  /**
   * Creates a new grid component instance.
   */
  Grid grid();

  /**
   * Creates a new column instance.
   */
  GridColumn gridColumn();

  /**
   * The white theme selector.
   */
  ClassSelector whiteTheme();

  StyleSheet styleSheet();

}