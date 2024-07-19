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
import objectos.way.Carbon.Component.Header;
import objectos.way.Carbon.Component.Header.CloseButton;
import objectos.way.Carbon.Component.Header.MenuButton;
import objectos.way.Carbon.Component.Header.Name;
import objectos.way.Carbon.Component.Header.Navigation;
import objectos.way.Http;

abstract class CarbonPage2 extends Carbon.Template2 {

  TopSection topSection = TopSection.HOME;

  CarbonPage2(Http.Exchange http) {
    super(http);
  }

  @Override
  protected abstract void preRender();

  @Override
  protected final void renderShell(Carbon.Shell shell) {
    final Header header;
    header = shell.addHeader();

    header.ariaLabel("Objectos Carbon");

    final MenuButton menuButton;
    menuButton = header.addMenuButton();

    menuButton.ariaLabel("Open menu");

    menuButton.title("Open");

    final CloseButton closeButton;
    closeButton = header.addCloseButton();

    closeButton.ariaLabel("Close menu");

    closeButton.title("Close");

    final Name headerName;
    headerName = header.addName();

    headerName.prefix("Objectos");

    headerName.text("Carbon");

    headerName.href("/");

    final 'Navigation navigation;
    navigation = header.addNavigation();

    navigation.ariaLabel("Objectos Carbon navigation");

    navigation.dataFrame("header-nav", topSection.name());

    navigation.addItem()
        .text("Components")
        .href("/components")
        .active(topSection == TopSection.COMPONENTS);

    navigation.addItem()
        .text("Examples")
        .href("#")
        .active(false);

    shell.render();
  }

  protected abstract void renderContent();

}