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
package objectos.selfgen.css2.util;

import java.util.List;
import objectos.selfgen.css2.util.Prefix.Breakpoint;

final class FrameworkClassStep extends ThisTemplate {

  private Iterable<Prefix> prefixes;

  @Override
  protected final void definition() {
    prefixes = spec.prefixList;

    packageDeclaration(CSS_UTIL);

    classDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, FINAL, name("Framework"),
      extendsClause(CSS_TEMPLATE),

      method(
        annotation(OVERRIDE),
        PROTECTED, FINAL, VOID, name("definition"),
        include(this::definitionMethod)
      ),

      include(this::prefixes),

      include(this::properties)
    );
  }

  private void definitionMethod() {
    for (var prefix : prefixes) {
      p(v(prefix.className.simpleName()));
    }
  }

  private void prefixes() {
    for (var prefix : prefixes) {
      String prefixName;
      prefixName = prefix.className.simpleName();

      List<PropertyClass> properties;
      properties = prefix.propertyClassList;

      method(
        PRIVATE, VOID, name(prefixName),
        include(() -> {
          for (var property : properties) {
            p(v(methodName(prefix, property)));
          }
        })
      );
    }
  }

  private void properties() {
    for (var prefix : prefixes) {
      List<PropertyClass> properties;
      properties = prefix.propertyClassList;

      for (var property : properties) {
        properties0(prefix, property);
      }
    }
  }

  private void properties0(Prefix prefix, PropertyClass property) {
    method(
      PRIVATE, VOID, name(methodName(prefix, property)),
      include(() -> properties1(prefix, property))
    );
  }

  private void properties1(Prefix prefix, PropertyClass property) {
    if (prefix instanceof Breakpoint breakpoint) {

      if (breakpoint.length == 0) {
        for (var method : property.styleMethodList) {
          p(include(() -> properties2(method, property)));
        }
      }

      else {
        p(
          v("media"),
          argument(NL, v("minWidth"), argument(v("px"), argument(i(breakpoint.length)))),
          include(() -> {
            for (var method : property.styleMethodList) {
              argument(NL, include(() -> properties2(method, property)));
            }
          })
        );
      }

    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type = " + prefix.getClass()
      );
    }
  }

  private void properties2(StyleMethod styleMethod, PropertyClass property) {
    v("style");

    switch (styleMethod.selectorKind) {
      case STANDARD -> {
        argument(NL, property.className, n(styleMethod.constantName), NL);
      }

      case ALL_BUT_FIRST -> {
        argument(
          NL,
          v("sel"),
          argument(property.className, n(styleMethod.constantName)),
          argument(n("CHILD")),
          argument(n("any")),
          argument(n("SIBLING")),
          argument(n("any")),
          NL
        );
      }
    }

    for (var declaration : styleMethod.declarationList) {
      argument(
        v(declaration.methodName()),
        include(() -> {
          for (var value : declaration.values()) {
            propertyValue(value);
          }
        }),
        NL
      );
    }
  }

  private void propertyValue(Value value) {
    if (value instanceof Value.ExpressionName expression) {
      argument(n(expression.fieldName()));
    }

    else if (value instanceof Value.LiteralDouble literal) {
      argument(l(literal.value()));
    }

    else if (value instanceof Value.LiteralInt literal) {
      argument(i(literal.value()));
    }

    else if (value instanceof Value.MethodDouble method) {
      argument(v(method.methodName()), argument(l(method.value())));
    }

    else if (value instanceof Value.MethodInt method) {
      argument(v(method.methodName()), argument(i(method.value())));
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type = " + value.getClass()
      );
    }
  }

  private String methodName(Prefix prefix, PropertyClass property) {
    return prefix.className.simpleName() + property.className.simpleName();
  }

}