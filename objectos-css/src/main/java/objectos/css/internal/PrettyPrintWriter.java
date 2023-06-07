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

import objectos.css.pseudom.PDeclaration;
import objectos.css.pseudom.PPropertyValue;
import objectos.css.pseudom.PPropertyValue.PDoubleValue;
import objectos.css.pseudom.PPropertyValue.PIntValue;
import objectos.css.pseudom.PPropertyValue.PKeyword;
import objectos.css.pseudom.PPropertyValue.PStringValue;
import objectos.css.pseudom.PRule;
import objectos.css.pseudom.PRule.PStyleRule;
import objectos.css.pseudom.PSelector;
import objectos.css.pseudom.PSelectorElement.PClassSelector;
import objectos.css.pseudom.PSelectorElement.PIdSelector;
import objectos.css.pseudom.PSelectorElement.PPseudoClassSelector;
import objectos.css.pseudom.PSelectorElement.PPseudoElementSelector;
import objectos.css.pseudom.PSelectorElement.PTypeSelector;
import objectos.css.pseudom.PSelectorElement.PUniversalSelector;
import objectos.css.pseudom.PStyleSheet;

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
      write(NL);

      while (iter.hasNext()) {
        write("  ");
        declaration(iter.next());
        write(NL);
      }

      write('}');
    } else {
      write(" {}");
    }
  }

  private void declaration(PDeclaration declaration) {
    var property = declaration.property();

    write(property.propertyName());
    write(':');

    var values = declaration.values().iterator();

    while (values.hasNext()) {
      write(' ');
      propertyValue(values.next());
    }

    write(';');
  }

  private void propertyValue(PPropertyValue value) {
    if (value instanceof PDoubleValue impl) {
      double d = impl.doubleValue();

      var s = Double.toString(d);

      write(s);
    } else if (value instanceof PIntValue impl) {
      int intValue = impl.intValue();

      var s = Integer.toString(intValue);

      write(s);
    } else if (value instanceof PKeyword keyword) {
      write(keyword.keywordName());
    } else if (value instanceof PStringValue impl) {
      var s = impl.value();
      write('"');
      write(s);
      write('"');
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + value.getClass()
      );
    }
  }

  private void selector(PSelector selector) {
    for (var element : selector.elements()) {
      if (element instanceof PAttributeSelectorImpl impl) {
        write('[');
        write(impl.attributeName());
        write(']');
      } else if (element instanceof PAttributeValueSelectorImpl impl) {
        write('[');
        write(impl.attributeName());
        write(impl.operator().symbol());
        write('"');
        write(impl.value());
        write('"');
        write(']');
      } else if (element instanceof PClassSelector classSelector) {
        write('.');
        write(classSelector.className());
      } else if (element instanceof Combinator combinator) {
        switch (combinator) {
          case DESCENDANT -> write(' ');

          case LIST -> write(", ");

          default -> {
            write(' ');
            write(combinator.symbol);
            write(' ');
          }
        }
      } else if (element instanceof PIdSelector idSelector) {
        write('#');
        write(idSelector.id());
      } else if (element instanceof PPseudoClassSelector pseudo) {
        write(pseudo.toString());
      } else if (element instanceof PPseudoElementSelector pseudo) {
        write(pseudo.toString());
      } else if (element instanceof PTypeSelector typeSelector) {
        write(typeSelector.toString());
      } else if (element instanceof PUniversalSelector) {
        write('*');
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: type=" + element.getClass()
        );
      }
    }
  }

}