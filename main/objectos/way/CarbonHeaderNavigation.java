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
import objectos.way.Carbon.Header.MenuItem;
import objectos.way.Carbon.Header.Navigation;

final class CarbonHeaderNavigation extends CarbonContainer implements Navigation {

  private String ariaLabel;

  private String frameName;

  private String frameValue;

  CarbonHeaderNavigation(Html.Template tmpl) {
    super(tmpl);
  }

  @Override
  public final Navigation ariaLabel(String value) {
    ariaLabel = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final Navigation dataFrame(String name, String value) {
    frameName = Check.notNull(name, "name == null");
    frameValue = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final MenuItem addItem() {
    return addComponent(new CarbonHeaderMenuItem(tmpl));
  }

  @Override
  public Html.ElementInstruction render() {
    return tmpl.nav(
        CarbonClasses.HEADER_NAV,

        ariaLabel != null ? tmpl.ariaLabel(ariaLabel) : tmpl.noop(),

        frameName != null ? tmpl.dataFrame(frameName, frameValue) : tmpl.noop(),

        tmpl.ul(
            CarbonClasses.HEADER_NAV_LIST,

            renderComponents()
        )
    );
  }

}