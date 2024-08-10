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
import objectos.way.Carbon.CarbonDescription;

final class CarbonHeader implements Carbon.Header {

  static final Html.ClassName HEADER = Html.className("""
  fixed top-0px right-0px left-0px z-header
  flex items-center h-header
  border-b border-b-border-subtle
  bg-background
  """);

  private final Html.TemplateBase tmpl;

  private String description;

  private CarbonHeaderCloseButton closeButton;

  private CarbonHeaderMenuButton menuButton;

  private CarbonHeaderName name;

  private CarbonHeaderNavigation navigation;

  private Carbon.Theme theme;

  CarbonHeader(Html.TemplateBase tmpl, Carbon.Header.Value[] values) {
    this.tmpl = tmpl;

    for (var value : values) {
      switch (value) {
        case CarbonDescription o -> description = o.value();

        case CarbonHeaderCloseButton o -> closeButton = o;

        case CarbonHeaderMenuButton o -> menuButton = o;

        case CarbonHeaderName o -> name = o;

        case CarbonHeaderNavigation o -> navigation = o;

        case Carbon.Theme o -> theme = o;
      }
    }
  }

  @Override
  public final Html.ElementInstruction render() {
    return tmpl.header(
        HEADER,

        theme != null ? theme : tmpl.noop(),

        description != null ? tmpl.ariaLabel(description) : tmpl.noop(),

        closeButton != null ? closeButton.render() : tmpl.noop(),

        menuButton != null ? menuButton.render() : tmpl.noop(),

        name != null ? name.render() : tmpl.noop(),

        navigation != null ? navigation.render() : tmpl.noop()
    );
  }

  final void accept(CarbonSideNav sideNavigation) {
    if (closeButton != null) {
      closeButton.accept(menuButton, sideNavigation);
    }

    if (menuButton != null) {
      menuButton.accept(closeButton, sideNavigation);
    }

    if (name != null) {
      name.accept(closeButton);
    }
  }

  final Script.Action closeButtonAction() {
    return closeButton != null ? closeButton.action() : null;
  }

  final List<CarbonHeaderMenuItem> navigationItems() {
    return navigation != null ? navigation.items() : null;
  }

}
