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
import objectos.way.Carbon.Component.Header.Name;

final class CarbonHeaderName implements Carbon.Component.Header.Name {

  private final Html.Template tmpl;

  private String href;

  private String prefix;

  private String text;

  private Script.Action onClick;

  CarbonHeaderName(Html.Template tmpl) {
    this.tmpl = tmpl;
  }

  @Override
  public final Name href(String value) {
    href = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final Name prefix(String value) {
    prefix = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final Name text(String value) {
    text = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final Name dataOnClick(Script.Action value) {
    onClick = Carbon.joinIf(onClick, value);
    return this;
  }

  @Override
  public final Html.ElementInstruction render() {
    return tmpl.a(
        Carbon.HEADER_NAME,

        href != null ? tmpl.href(href) : tmpl.noop(),

        onClick != null ? tmpl.dataOnClick(onClick) : tmpl.noop(),

        href != null && onClick != null ? tmpl.dataOnClick(Script.location(href)) : tmpl.noop(),

        prefix != null ? tmpl.span("Objectos") : tmpl.noop(),

        prefix != null && text != null ? tmpl.nbsp() : tmpl.noop(),

        text != null ? tmpl.t("Carbon") : tmpl.noop()
    );
  }

}