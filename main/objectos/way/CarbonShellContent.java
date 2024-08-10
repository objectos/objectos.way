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

import objectos.way.Carbon.CarbonDataFrame;

final class CarbonShellContent implements Carbon.ShellContent {

  private CarbonContent content;

  private CarbonDataFrame dataFrame;

  private final Html.TemplateBase tmpl;

  private CarbonHeader header;

  private CarbonSideNav sideNavigation;

  CarbonShellContent(Html.TemplateBase tmpl, Carbon.ShellContent.Value[] values) {
    this.tmpl = tmpl;

    for (var value : values) {
      switch (value) {
        case CarbonContent o -> content = o;

        case CarbonDataFrame o -> dataFrame = o;
      }
    }
  }

  @Override
  public final Html.ElementInstruction render() {
    return tmpl.main(
        header != null ? tmpl.className("mt-header") : tmpl.noop(),

        sideNavigation != null ? tmpl.className("lg:ml-side-nav") : tmpl.noop(),

        dataFrame != null ? dataFrame.render(tmpl) : tmpl.noop(),

        content != null ? content.render() : tmpl.noop()
    );
  }

  final void accept(CarbonHeader header, CarbonSideNav sideNavigation) {
    this.header = header;

    this.sideNavigation = sideNavigation;
  }

}