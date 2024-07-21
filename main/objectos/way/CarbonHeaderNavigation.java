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
import objectos.way.Carbon.HeaderMenuItem;
import objectos.way.Carbon.HeaderNavigation;

final class CarbonHeaderNavigation extends CarbonContainer implements HeaderNavigation {

  private String ariaLabel;

  private String frameName;

  private String frameValue;

  CarbonHeaderNavigation(Html.Template tmpl) {
    super(tmpl);
  }

  @Override
  public final HeaderNavigation ariaLabel(String value) {
    ariaLabel = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final HeaderNavigation dataFrame(String name, String value) {
    frameName = Check.notNull(name, "name == null");
    frameValue = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final HeaderMenuItem addItem() {
    return addComponent(new CarbonHeaderMenuItem(tmpl));
  }

  @Override
  public void render() {
    tmpl.nav(
        tmpl.className("relative hidden h-full pl-16px"),
        tmpl.className("lg:flex lg:items-center"),
        tmpl.className("lg:before:relative lg:before:-left-16px lg:before:z-header lg:before:block"),
        tmpl.className("lg:before:h-1/2 lg:before:w-1px"),
        tmpl.className("lg:before:border-l lg:before:border-l-subtle"),
        tmpl.className("lg:before:content-empty"),

        ariaLabel != null ? tmpl.ariaLabel(ariaLabel) : tmpl.noop(),

        frameName != null ? tmpl.dataFrame(frameName, frameValue) : tmpl.noop(),

        tmpl.ul(
            tmpl.className("h-full flex text-secondary"),

            renderComponents()
        )
    );
  }

}