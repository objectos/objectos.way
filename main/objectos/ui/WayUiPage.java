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
package objectos.ui;

import java.util.function.Consumer;
import objectos.html.FragmentLambda;
import objectos.html.Html;
import objectos.lang.object.Check;

final class WayUiPage implements UiPage {

  private final Html.Extensible parent;

  private final Consumer<Html> headStart;

  private String lang;

  private String title;

  public WayUiPage(Html.Extensible parent, Consumer<Html> headStart) {
    this.parent = parent;

    this.headStart = headStart;
  }

  @Override
  public final void lang(String value) {
    lang = Check.notNull(value, "value == null");
  }

  @Override
  public final void title(String value) {
    title = Check.notNull(value, "value == null");
  }

  @Override
  public final void render(FragmentLambda body) {
    parent.renderFragment(html -> {
      html.doctype();

      html.html(
          lang != null ? html.lang(lang) : html.noop(),

          html.head(
              html.render(headStart),
              title != null ? html.title(title) : html.noop()
          ),

          html.body(
              html.include(body)
          )
      );
    });
  }

}