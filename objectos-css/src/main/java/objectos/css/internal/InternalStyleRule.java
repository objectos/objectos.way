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

import java.io.IOException;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleRule;
import objectos.util.UnmodifiableIterator;
import objectos.util.UnmodifiableList;

public record InternalStyleRule(UnmodifiableList<Selector> selectors,
                                UnmodifiableList<StyleDeclaration> declarations)
    implements StyleRule, TopLevelElement {

  @Override
  public final void writeTo(Appendable dest) throws IOException {
    UnmodifiableIterator<Selector> selIterator;
    selIterator = selectors.iterator();

    if (selIterator.hasNext()) {
      Selector selector;
      selector = selIterator.next();

      selector.writeTo(dest);

      while (selIterator.hasNext()) {
        dest.append(", ");

        selector = selIterator.next();

        selector.writeTo(dest);
      }
    }

    dest.append(" {");

    String nl;
    nl = System.lineSeparator();

    for (StyleDeclaration declaration : declarations) {
      dest.append(nl);

      dest.append("  ");

      declaration.writeTo(dest);

      dest.append(';');
    }

    if (!declarations.isEmpty()) {
      dest.append(nl);
    }

    dest.append('}');
  }

}