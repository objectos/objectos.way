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
import objectos.html.Html;
import objectos.html.TemplateBase;
import objectos.lang.object.Check;

public class WayUi implements UiBuilder {

  private Consumer<Html> headStart = html -> {};

  public WayUi() {}

  @Override
  public final Ui create(TemplateBase parent) {
    Check.notNull(parent, "parent == null");

    return new ThisUi(parent);
  }

  @Override
  public final UiBuilder onHeadStart(Consumer<Html> value) {
    headStart = Check.notNull(value, "value == null");

    return this;
  }

  private class ThisUi implements Ui {

    private final Html.Extensible parent;

    private UiPage page;

    public ThisUi(Html.Extensible parent) {
      this.parent = parent;
    }

    @Override
    public final UiPage page() {
      if (page == null) {
        page = new WayUiPage(parent, headStart);
      }

      return page;
    }

  }

}