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

final class CarbonHeaderName implements Carbon.HeaderName {

  static final Html.ClassName HEADER_NAME = Html.className("""
  flex h-full select-none items-center
  border-2 border-transparent
  px-16px
  body-compact-01 text-text-primary font-600 leading-20px tracking-0.1px
  outline-none
  transition-colors duration-100
  focus:border-focus
  lg:pl-16px lg:pr-32px
  """);

  static final Html.ClassName HEADER_NAME_PREFIX = Html.className("""
  font-400
  """);

  private CarbonHeaderCloseButton closeButton;

  String href;

  String prefix;

  String text;

  private final Html.TemplateBase tmpl;

  CarbonHeaderName(Html.TemplateBase tmpl) {
    this.tmpl = tmpl;
  }

  @Override
  public final Html.ElementInstruction render() {
    Script.Action closeAction;
    closeAction = closeButton != null ? closeButton.action() : null;

    return tmpl.a(
        HEADER_NAME,

        closeAction != null ? tmpl.dataOnClick(closeAction) : tmpl.noop(),
        closeAction != null ? tmpl.dataOnClick(Script.location(href)) : tmpl.noop(),

        href != null ? tmpl.href(href) : tmpl.noop(),

        prefix != null ? tmpl.span(HEADER_NAME_PREFIX, tmpl.t(prefix)) : tmpl.noop(),
        prefix != null ? tmpl.nbsp() : tmpl.noop(),

        text != null ? tmpl.t(text) : tmpl.noop()
    );
  }

  final void accept(CarbonHeaderCloseButton closeButton) {
    this.closeButton = closeButton;
  }

}