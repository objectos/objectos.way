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
import objectos.way.Html.ElementInstruction;

final class CarbonHeader extends CarbonContainer implements Carbon.Component.Header {

  private String ariaLabel;

  CarbonHeader(Html.Template tmpl) {
    super(tmpl);
  }

  @Override
  public final CloseButton addCloseButton() {
    return addComponent(new HeaderCloseButton());
  }

  @Override
  public final MenuButton addMenuButton() {
    return addComponent(new HeaderMenuButton());
  }

  @Override
  public final Name addName() {
    return addComponent(new CarbonHeaderName(tmpl));
  }

  @Override
  public final Navigation addNavigation() {
    return addComponent(new CarbonHeaderNavigation(tmpl));
  }

  @Override
  public final Carbon.Component.Header ariaLabel(String value) {
    ariaLabel = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final Html.ElementInstruction render() {
    return tmpl.header(
        CarbonClasses.HEADER,

        ariaLabel != null ? tmpl.ariaLabel(ariaLabel) : tmpl.noop(),

        renderComponents()
    );
  }

  final class HeaderCloseButton implements Carbon.Component.Header.CloseButton {

    private final Html.Id id = tmpl.nextId();

    private String ariaLabel;

    private Script.Action onClick;

    private String title;

    @Override
    public final CloseButton ariaLabel(String value) {
      ariaLabel = Check.notNull(value, "value == null");
      return this;
    }

    @Override
    public final CloseButton dataOnClick(Script.Action value) {
      Script.Action a;
      a = Check.notNull(value, "value");

      if (onClick == null) {
        onClick = a;
      } else {
        onClick = Script.join(onClick, a);
      }

      return this;
    }

    @Override
    public final CloseButton title(String value) {
      title = Check.notNull(value, "value == null");
      return this;
    }

    @Override
    public final ElementInstruction render() {
      return tmpl.button(
          id, Carbon.HEADER_CLOSE_BUTTON,

          ariaLabel != null ? tmpl.ariaLabel(ariaLabel) : tmpl.noop(),

          title != null ? tmpl.title(title) : tmpl.noop(),

          tmpl.type("button"),

          onClick != null ? tmpl.dataOnClick(onClick) : tmpl.noop(),

          icon20(Carbon.Icon.CLOSE)
      );
    }

  }

  final class HeaderMenuButton implements Carbon.Component.Header.MenuButton {

    private final Html.Id id = tmpl.nextId();

    private String ariaLabel;

    private Script.Action onClick;

    private String title;

    @Override
    public final MenuButton ariaLabel(String value) {
      ariaLabel = Check.notNull(value, "value == null");
      return this;
    }

    @Override
    public final MenuButton dataOnClick(Script.Action value) {
      Script.Action a;
      a = Check.notNull(value, "value");

      if (onClick == null) {
        onClick = a;
      } else {
        onClick = Script.join(onClick, a);
      }

      return this;
    }

    @Override
    public final MenuButton title(String value) {
      title = Check.notNull(value, "value == null");
      return this;
    }

    @Override
    public final ElementInstruction render() {
      return tmpl.button(
          id, Carbon.HEADER_MENU_BUTTON,

          ariaLabel != null ? tmpl.ariaLabel(ariaLabel) : tmpl.noop(),

          title != null ? tmpl.title(title) : tmpl.noop(),

          tmpl.type("button"),

          onClick != null ? tmpl.dataOnClick(onClick) : tmpl.noop(),

          icon20(Carbon.Icon.MENU)
      );
    }

  }

}
