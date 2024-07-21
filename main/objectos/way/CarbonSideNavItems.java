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

import objectos.way.Carbon.HeaderSideNavItems;
import objectos.way.Carbon.SideNavItems;
import objectos.way.Carbon.SideNavLink;

final class CarbonSideNavItems extends CarbonContainer implements SideNavItems {

  CarbonSideNavItems(Html.Template tmpl) {
    super(tmpl);
  }

  @Override
  public final HeaderSideNavItems addHeaderSideNavItems() {
    return addComponent(new CarbonHeaderSideNavItems(tmpl));
  }

  @Override
  public final SideNavLink addLink() {
    return addComponent(new CarbonSideNavLink(tmpl));
  }

  @Override
  public void render() {
    tmpl.ul(
        Carbon.SIDE_NAV_ITEMS,

        renderComponents()
    );
  }

}
