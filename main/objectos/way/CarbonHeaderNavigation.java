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

import objectos.lang.object.Check;
import objectos.util.list.GrowableList;
import objectos.way.Carbon.CarbonHeaderMenuItem;
import objectos.way.Carbon.Header.MenuItem;
import objectos.way.Carbon.Header.Navigation;

final class CarbonHeaderNavigation implements Carbon.Header.Navigation {

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

  private static final Html.ClassName __HEADER_NAV_LINK = Html.className("""
  relative flex h-full select-none items-center
  border-2 border-transparent
  bg-background
  px-16px
  transition-colors duration-100
  active:bg-background-active active:text-text-primary
  focus:border-focus focus:outline-none
  hover:bg-background-hover hover:text-text-primary
  """);

  private static final Html.ClassName __HEADER_NAV_LINK_ACTIVE = Html.className("""
  text-text-primary
  after:absolute after:-bottom-2px after:-left-2px after:-right-2px
  after:block after:border-b-3 after:border-b-border-interactive after:content-empty
  """);

  private static final Html.ClassName __HEADER_NAV_LINK_INACTIVE = Html.className("""
  text-text-secondary
  """);

  private static final Html.ClassName __HEADER_NAV_LINK_PRODUCTIVE = Html.className("""
  text-14px leading-18px font-400 tracking-0px
  """);

  static final Html.ClassName HEADER_NAV_LINK_ACTIVE = Html.className(
      __HEADER_NAV_LINK, __HEADER_NAV_LINK_ACTIVE, __HEADER_NAV_LINK_PRODUCTIVE
  );

  static final Html.ClassName HEADER_NAV_LINK_INACTIVE = Html.className(
      __HEADER_NAV_LINK, __HEADER_NAV_LINK_INACTIVE, __HEADER_NAV_LINK_PRODUCTIVE
  );

  static final Html.ClassName HEADER_NAV_LINK_ACTIVE_EXPRESSIVE = Html.className(
      __HEADER_NAV_LINK, __HEADER_NAV_LINK_ACTIVE, CarbonClasses.BODY_COMPACT_02
  );

  static final Html.ClassName HEADER_NAV_LINK_INACTIVE_EXPRESSIVE = Html.className(
      __HEADER_NAV_LINK, __HEADER_NAV_LINK_INACTIVE, CarbonClasses.BODY_COMPACT_02
  );

  private String dataFrameName;
  private String dataFrameValue;

  private String description;

  private boolean expressive;

  private final GrowableList<CarbonHeaderMenuItem> items = new GrowableList<>();

  private final Html.TemplateBase tmpl;

  CarbonHeaderNavigation(Html.TemplateBase tmpl) {
    this.tmpl = tmpl;
  }

  @Override
  public final Navigation addItems(Iterable<MenuItem> values) {
    for (var value : values) {
      MenuItem o;
      o = Check.notNull(value, "value == null");

      CarbonHeaderMenuItem item;
      item = (CarbonHeaderMenuItem) o;

      items.add(item);
    }

    return this;
  }

  @Override
  public final Navigation dataFrame(String name, String value) {
    dataFrameName = Check.notNull(name, "name == null");
    dataFrameValue = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Navigation description(String value) {
    description = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Navigation expressive() {
    expressive = true;

    return this;
  }

  @Override
  public final Html.ElementInstruction render() {
    return tmpl.nav(
        HEADER_NAVIGATION,

        dataFrameName != null
            ? dataFrameValue != null
                ? tmpl.dataFrame(dataFrameName, dataFrameValue)
                : tmpl.dataFrame(dataFrameName)
            : tmpl.noop(),

        description != null ? tmpl.ariaLabel(description) : tmpl.noop(),

        tmpl.ul(
            HEADER_NAVIGATION_LIST,

            tmpl.include(this::renderItems)
        )
    );
  }

  private void renderItems() {
    for (var item : items) {
      tmpl.li(
          tmpl.a(
              item.active()
                  ? expressive ? HEADER_NAV_LINK_ACTIVE_EXPRESSIVE : HEADER_NAV_LINK_ACTIVE
                  : expressive ? HEADER_NAV_LINK_INACTIVE_EXPRESSIVE : HEADER_NAV_LINK_INACTIVE,

              tmpl.href(item.href()),

              tmpl.tabindex("0"),

              tmpl.span(item.text())
          )
      );
    }
  }

}