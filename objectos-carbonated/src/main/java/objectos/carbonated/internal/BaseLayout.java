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

import objectos.css.CssTemplate;
import objectos.css.tmpl.Api;
import objectos.css.util.CustomProperty;

public final class BaseLayout extends CssTemplate {

  static final CustomProperty<Api.LengthValue> SIZE_HEIGHT_XS = U.nextProp();
  static final CustomProperty<Api.LengthValue> SIZE_HEIGHT_SM = U.nextProp();
  static final CustomProperty<Api.LengthValue> SIZE_HEIGHT_MD = U.nextProp();
  static final CustomProperty<Api.LengthValue> SIZE_HEIGHT_LG = U.nextProp();
  static final CustomProperty<Api.LengthValue> SIZE_HEIGHT_XL = U.nextProp();
  static final CustomProperty<Api.LengthValue> SIZE_HEIGHT_2XL = U.nextProp();

  @Override
  protected final void definition() {
    style(
      _root,

      set(SIZE_HEIGHT_XS, rem(1.5)),
      set(SIZE_HEIGHT_SM, rem(2)),
      set(SIZE_HEIGHT_MD, rem(2.5)),
      set(SIZE_HEIGHT_LG, rem(3)),
      set(SIZE_HEIGHT_XL, rem(4)),
      set(SIZE_HEIGHT_2XL, rem(5))
    );
  }

}