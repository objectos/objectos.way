/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import objectos.way.Css.Rule;

/**
 * CSS generation context.
 */
abstract class CssEngineContext {

  Map<CssVariant.OfAtRule, CssEngineContextOfAtRule> atRules;

  final List<Rule> rules = Util.createList();

  CssEngineContext() {
  }

  public final void add(Rule rule) {
    rules.add(rule);
  }

  public final CssEngineContext contextOf(CssModifier modifier) {
    List<CssVariant.OfAtRule> modifierAtRules;
    modifierAtRules = modifier.atRules();

    if (modifierAtRules.isEmpty()) {
      return this;
    }

    if (atRules == null) {
      atRules = new TreeMap<>();
    }

    Iterator<CssVariant.OfAtRule> iterator;
    iterator = modifierAtRules.iterator();

    CssVariant.OfAtRule first;
    first = iterator.next(); // safe as list is not empty

    CssEngineContextOfAtRule result;
    result = atRules.computeIfAbsent(first, CssEngineContextOfAtRule::new);

    while (iterator.hasNext()) {
      result = result.nest(iterator.next());
    }

    return result;
  }

  public final void writeTo(CssWriter w, int level) throws IOException {
    rules.sort(Comparator.naturalOrder());

    write(w, level);
  }

  abstract void write(CssWriter w, int level) throws IOException;

  final void writeContents(CssWriter w, int level) throws IOException {
    int lastKind;
    lastKind = 0;

    for (Css.Rule rule : rules) {
      int kind;
      kind = rule.kind();

      if (lastKind == 1 && kind == 2) {
        w.writeln();
      }

      lastKind = kind;

      rule.writeTo(w, level);
    }

    if (atRules != null) {
      for (CssEngineContext child : atRules.values()) {
        w.writeln();

        child.writeTo(w, level);
      }
    }
  }

}