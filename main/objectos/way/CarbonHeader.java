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

final class CarbonHeader implements Carbon.Header {

  static final Html.ClassName HEADER = Html.className("""
  fixed top-0px right-0px left-0px z-header
  flex items-center h-header
  border-b border-b-border-subtle
  bg-background
  """);

  private final Html.TemplateBase tmpl;

  private String description;

  private CloseButton closeButton;

  private MenuButton menuButton;

  private Name name;

  private Navigation navigation;

  private Carbon.Theme theme;

  CarbonHeader(Html.TemplateBase tmpl) {
    this.tmpl = tmpl;
  }

  @Override
  public final Carbon.Header description(String value) {
    description = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Carbon.Header closeButton(CloseButton value) {
    closeButton = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Carbon.Header menuButton(MenuButton value) {
    menuButton = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Carbon.Header name(Name value) {
    name = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Carbon.Header navigation(Navigation value) {
    navigation = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Carbon.Header theme(Carbon.Theme value) {
    theme = Check.notNull(value, "value == null");

    return this;
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

}
