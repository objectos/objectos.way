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
import objectos.way.Carbon.Header;
import objectos.way.Carbon.HeaderCloseButton;
import objectos.way.Carbon.HeaderMenuButton;
import objectos.way.Carbon.HeaderName;
import objectos.way.Carbon.HeaderNavigation;

final class CarbonHeader extends CarbonContainer implements Header {

  private String ariaLabel;

  CarbonHeader(Html.Template tmpl) {
    super(tmpl);
  }

  @Override
  public final HeaderCloseButton addCloseButton() {
    return addComponent(new CarbonHeaderCloseButton(tmpl));
  }

  @Override
  public final HeaderMenuButton addMenuButton() {
    return addComponent(new CarbonHeaderMenuButton(tmpl));
  }

  @Override
  public final HeaderName addName() {
    return addComponent(new CarbonHeaderName(tmpl));
  }

  @Override
  public final HeaderNavigation addNavigation() {
    return addComponent(new CarbonHeaderNavigation(tmpl));
  }

  @Override
  public final Header ariaLabel(String value) {
    ariaLabel = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final void render() {
    tmpl.header(
        CarbonClasses.HEADER,

        ariaLabel != null ? tmpl.ariaLabel(ariaLabel) : tmpl.noop(),

        renderComponents()
    );
  }

}
