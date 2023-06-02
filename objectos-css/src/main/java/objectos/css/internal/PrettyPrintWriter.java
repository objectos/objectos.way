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

import objectos.css.pseudom.PRule;
import objectos.css.pseudom.PRule.PStyleRule;
import objectos.css.pseudom.PSelector;
import objectos.css.pseudom.PStyleSheet;
import objectos.css.tmpl.TypeSelector;

public final class PrettyPrintWriter extends Writer {

  private static final String NL = System.lineSeparator();

  @Override
  public final void process(PStyleSheet sheet) {
    var rules = sheet.rules();

    var rulesIter = rules.iterator();

    if (rulesIter.hasNext()) {
      rule(rulesIter.next());

      write(NL);
    }
  }

  private void rule(PRule rule) {
    if (rule instanceof PStyleRule styleRule) {
      styleRule(styleRule);
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + rule.getClass()
      );
    }
  }

  private void styleRule(PStyleRule rule) {
    selector(rule.selector());

    var declarations = rule.declarations();

    var iter = declarations.iterator();

    if (iter.hasNext()) {
      write(" {");

      throw new UnsupportedOperationException("Implement me");
    } else {
      write(" {}");
    }
  }

  private void selector(PSelector selector) {
    for (var element : selector.elements()) {
      if (element instanceof TypeSelector typeSelector) {
        write(typeSelector.toString());
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: type=" + element.getClass()
        );
      }
    }
  }

}