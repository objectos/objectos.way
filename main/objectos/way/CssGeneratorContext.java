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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

abstract class CssGeneratorContext extends Css.Context {

  Map<Css.MediaQuery, CssGeneratorContextOfMediaQuery> mediaQueries;

  @Override
  public final void addComponent(CssComponent component) {
    add(component);
  }

  @Override
  public final Css.Context contextOf(Css.Modifier modifier) {
    List<Css.MediaQuery> modifierQueries;
    modifierQueries = modifier.mediaQueries();

    if (modifierQueries.isEmpty()) {
      return this;
    }

    if (mediaQueries == null) {
      mediaQueries = new TreeMap<>();
    }

    Iterator<Css.MediaQuery> iterator;
    iterator = modifierQueries.iterator();

    Css.MediaQuery first;
    first = iterator.next(); // safe as list is not empty

    CssGeneratorContextOfMediaQuery result;
    result = mediaQueries.computeIfAbsent(first, CssGeneratorContextOfMediaQuery::new);

    while (iterator.hasNext()) {
      result = result.nest(iterator.next());
    }

    return result;
  }

  final void writeContents(StringBuilder out, Css.Indentation indentation) {
    int lastKind = 0;

    for (Css.Rule rule : rules) {
      int kind;
      kind = rule.kind();

      if (lastKind == 1 && kind == 2) {
        out.append(System.lineSeparator());
      }

      lastKind = kind;

      rule.writeTo(out, indentation);
    }

    if (mediaQueries != null) {
      for (Css.Context child : mediaQueries.values()) {
        if (!out.isEmpty()) {
          out.append(System.lineSeparator());
        }

        child.writeTo(out, indentation);
      }
    }
  }

}