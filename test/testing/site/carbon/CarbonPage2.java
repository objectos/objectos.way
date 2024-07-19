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
package testing.site.carbon;

import objectos.way.Carbon;
import objectos.way.Carbon.HeaderMenuItemContainer;
import objectos.way.Http;
import objectos.way.Script;

abstract class CarbonPage2 extends Carbon.Template2 {

  TopSection topSection = TopSection.HOME;

  CarbonPage2(Http.Exchange http) {
    super(http);
  }

  @Override
  protected abstract void preRender();

  @Override
  protected final void renderShell(Carbon.Shell shell) {
    final Carbon.Header header;
    header = shell.addHeader();

    header.ariaLabel("Objectos Carbon");

    final Carbon.HeaderMenuButton menuButton;
    menuButton = header.addMenuButton();

    menuButton.ariaLabel("Open menu");

    menuButton.title("Open");

    final Carbon.HeaderCloseButton closeButton;
    closeButton = header.addCloseButton();

    closeButton.ariaLabel("Close menu");

    closeButton.title("Close");

    final Carbon.HeaderName headerName;
    headerName = header.addName();

    headerName.prefix("Objectos");

    headerName.text("Carbon");

    headerName.href("/");

    final Carbon.HeaderNavigation navigation;
    navigation = header.addNavigation();

    navigation.ariaLabel("Objectos Carbon navigation");

    navigation.dataFrame("header-nav", topSection.name());

    addHeaderMenuItems(navigation);

    final Carbon.Overlay overlay;
    overlay = shell.addOverlay();

    overlay.offsetHeader();

    final Carbon.SideNav sideNav;
    sideNav = shell.addSideNav();

    sideNav.ariaLabel("Side navigation");

    sideNav.offsetHeader();

    sideNav.persistent(false);

    final Script.Action openMenu = Script.actions(
        closeButton.showAction(),
        menuButton.hideAction(),
        overlay.showAction(),
        sideNav.showAction()
    );

    final Script.Action closeMenu = Script.actions(
        closeButton.hideAction(),
        menuButton.showAction(),
        overlay.hideAction(),
        sideNav.hideAction()
    );

    menuButton.dataOnClick(openMenu);

    closeButton.dataOnClick(closeMenu);

    headerName.dataOnClick(closeMenu);

    final Carbon.HeaderSideNavItems headerSideNavItems;
    headerSideNavItems = sideNav.addHeaderSideNavItems();

    headerSideNavItems.dataOnClick(closeMenu);

    addHeaderMenuItems(headerSideNavItems);

    shell.render();
  }

  private void addHeaderMenuItems(HeaderMenuItemContainer navigation) {
    navigation.addItem()
        .text("Components")
        .href("/components")
        .active(topSection == TopSection.COMPONENTS);

    navigation.addItem()
        .text("Examples")
        .href("#")
        .active(false);
  }

  protected abstract void renderContent();

}