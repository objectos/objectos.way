/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import objectos.way.Carbon.Grid;
import objectos.way.Carbon.GridStyle;

final class CarbonGrid implements Grid {

  @SuppressWarnings("unused")
  private final GridStyle style;

  @SuppressWarnings("unused")
  private final Html.TemplateBase tmpl;

  CarbonGrid(Html.TemplateBase tmpl, GridStyle style, Grid.Value[] values) {
    this.tmpl = tmpl;
    this.style = style;
  }

  @Override
  public final Html.ElementInstruction render() {
    throw new UnsupportedOperationException("Implement me");
  }

}
