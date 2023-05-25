/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package br.com.objectos.css;

import br.com.objectos.css.common.lang.Color;
import br.com.objectos.css.common.lang.Unit;

@SpecType
interface Css2_Section08_Box_model {

  @Property
  Object margin(Unit unit);

  @Property
  Object margin(Unit topBottom, Unit rightLeft);

  @Property
  Object margin(Unit top, Unit rightLeft, Unit bottom);

  @Property
  Object margin(Unit top, Unit right, Unit bottom, Unit left);
  
  @Property
  Object borderColor(Color color);

  @Property
  Object borderColor(Color topBottom, Color rightLeft);

}