/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import objectos.css.tmpl.Combinator;
import objectos.css.tmpl.Instruction.ExternalSelector;
import objectos.css.tmpl.PseudoClassSelector;
import objectos.css.tmpl.PseudoElementSelector;
import objectos.css.tmpl.TypeSelector;

abstract class GeneratedCssTemplate {

  protected static final ExternalSelector A = TypeSelector.A;

  protected static final ExternalSelector BODY = TypeSelector.BODY;

  protected static final ExternalSelector LI = TypeSelector.LI;

  protected static final ExternalSelector UL = TypeSelector.UL;

  protected static final Combinator OR = Combinator.LIST;

  protected static final Combinator SP = Combinator.DESCENDANT;

  protected static final PseudoClassSelector ACTIVE = PseudoClassSelector.ACTIVE;

  protected static final PseudoClassSelector VISITED = PseudoClassSelector.VISITED;

  protected static final PseudoElementSelector AFTER = PseudoElementSelector.AFTER;

}
