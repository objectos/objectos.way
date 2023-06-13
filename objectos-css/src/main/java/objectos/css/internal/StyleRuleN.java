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

import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;

public final class StyleRuleN {

  private final Selector selector;

  private final StyleDeclaration[] declarations;

  public StyleRuleN(Selector selector, StyleDeclaration[] declarations) {
    this.selector = selector;
    this.declarations = declarations;
  }

  @Override
  public final String toString() {
    var nl = System.lineSeparator();

    var sb = new StringBuilder();

    sb.append(selector);

    sb.append(" {");

    for (var declaration : declarations) {
      sb.append(nl);

      sb.append("  ");

      sb.append(declaration);

      sb.append(';');
    }

    sb.append(nl);

    sb.append("}");

    return sb.toString();
  }

}