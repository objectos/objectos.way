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

import java.util.List;
import objectos.util.list.GrowableList;
import objectos.way.Carbon.CarbonDataFrame;
import objectos.way.Carbon.CarbonDescription;

final class CarbonHeaderNavigation implements Carbon.HeaderNavigation {

  private CarbonDataFrame dataFrame;

  private String description;

  private boolean expressive;

  private final GrowableList<CarbonHeaderMenuItem> items = new GrowableList<>();

  private final Html.TemplateBase tmpl;

  CarbonHeaderNavigation(Html.TemplateBase tmpl, Carbon.HeaderNavigation.Value[] values) {
    this.tmpl = tmpl;

    for (var value : values) {
      switch (value) {
        case CarbonDataFrame o -> dataFrame = o;

        case CarbonDescription o -> description = o.value();

        case CarbonHeaderMenuItem o -> items.add(o);
      }
    }
  }

  static final Html.ClassName HEADER_NAVIGATION = Html.className("""
  relative hidden h-full pl-16px

  lg:flex lg:items-center

  lg:before:relative lg:before:-left-16px lg:before:z-header lg:before:block
  lg:before:h-1/2 lg:before:w-1px
  lg:before:border-l lg:before:border-l-border-subtle
  lg:before:content-empty
  """);

  static final Html.ClassName HEADER_NAVIGATION_LIST = Html.className("""
  flex h-full
  text-text-secondary
  """);

  @Override
  public final Html.ElementInstruction render() {
    return tmpl.nav(
        HEADER_NAVIGATION,

        dataFrame != null ? dataFrame.render(tmpl) : tmpl.noop(),

        description != null ? tmpl.ariaLabel(description) : tmpl.noop(),

        tmpl.ul(
            HEADER_NAVIGATION_LIST,

            tmpl.include(this::renderItems)
        )
    );
  }

  private void renderItems() {
    for (var item : items) {
      item.renderHeader(expressive);
    }
  }

  final List<CarbonHeaderMenuItem> items() {
    return items;
  }

}