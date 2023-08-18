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

final class CarbonStyleSheet extends CssTemplate {

  private final Breakpoints breakpoints;

  CarbonStyleSheet(Breakpoints breakpoints) {
    this.breakpoints = breakpoints;
  }

  @Override
  protected final void definition() {
    install(new Reset());

    install(new Layout());

    install(new Typography.Styles());

    install(new Theme.White());

    install(new Button.Styles());

    install(new Grid.Styles(breakpoints));

    install(new Column.Styles(breakpoints));

    install(new Notification.Styles(breakpoints));
  }

}
