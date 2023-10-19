/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.css.util;

import objectos.css.util.Extra;
import objectos.css.util.Hover;
import objectos.css.util.Large;
import objectos.css.util.Max;
import objectos.css.util.Medium;
import objectos.css.util.Small;
import objectos.css.util.TextColor;
import objectos.html.HtmlTemplate;

public final class Utility04 extends HtmlTemplate {

  @Override
  protected void definition() {
    div(
      TextColor.SLATE_100,
      Small.TextColor.SLATE_200,
      Medium.TextColor.SLATE_300,
      Large.TextColor.SLATE_400,
      Extra.TextColor.SLATE_500,
      Max.TextColor.SLATE_600,
      Hover.TextColor.SLATE_700
    );
  }

}