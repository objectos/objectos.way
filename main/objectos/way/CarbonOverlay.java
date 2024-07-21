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

import objectos.way.Carbon.Overlay;

final class CarbonOverlay extends CarbonComponent implements Overlay {

  private final Html.Id id;

  private boolean header;

  CarbonOverlay(Html.Template tmpl) {
    super(tmpl);

    id = tmpl.nextId();
  }

  @Override
  public final Overlay offsetHeader() {
    header = true;
    return this;
  }

  @Override
  public final Script.Action hideAction() {
    return Script.actions(
        Script.addClass(id, CarbonClasses.HIDDEN, CarbonClasses.OPACITY_0),
        Script.removeClass(id, CarbonClasses.OPACITY_100)
    );
  }

  @Override
  public final Script.Action showAction() {
    return Script.actions(
        Script.removeClass(id, CarbonClasses.HIDDEN, CarbonClasses.OPACITY_0),
        Script.addClass(id, CarbonClasses.OPACITY_100)
    );
  }

  @Override
  public void render() {
    tmpl.div(
        id,

        tmpl.className("fixed inset-0px block hidden z-overlay"),
        tmpl.className("bg-overlay opacity-0"),
        tmpl.className("transition-opacity duration-300"),
        tmpl.className("lg:hidden"),

        header ? tmpl.className("mt-header") : tmpl.noop()
    );
  }

}
