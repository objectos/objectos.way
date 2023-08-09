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
package objectos.carbonated.internal;

import objectos.carbonated.Carbon;
import objectos.css.CssTemplate;
import objectos.css.StyleSheet;

public final class StyleSheetBuilderImpl extends CssTemplate implements Carbon.StyleSheetBuilder {

  @Override
  public final StyleSheet build() {
    return compile();
  }

  @Override
  protected final void definition() {
    install(new BaseReset());

    install(new BaseLayout());

    install(new BaseTypography());

    install(new ThemeWhite());

    install(new CompButtonStyles());

    install(new CompNotificationStyles());
  }

}