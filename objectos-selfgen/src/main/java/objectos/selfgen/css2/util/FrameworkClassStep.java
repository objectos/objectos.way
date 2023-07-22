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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import objectos.selfgen.css2.util.Prefix.Breakpoint;
import objectos.selfgen.css2.util.Value.Keyword;

final class FrameworkClassStep extends ThisTemplate {

  private Set<Entry<Prefix, List<Property>>> entries;

  @Override
  protected final void definition() {
    Map<Prefix, List<Property>> map;
    map = spec.properties;

    entries = map.entrySet();

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
    for (var entry : entries) {
      Prefix prefix;
      prefix = entry.getKey();

      p(v(prefix.className.simpleName()));
    }
  }

  private void prefixes() {
    for (var entry : entries) {
      Prefix prefix;
      prefix = entry.getKey();

      String prefixName;
      prefixName = prefix.className.simpleName();

      List<Property> properties;
      properties = entry.getValue();

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
    for (var entry : entries) {
      Prefix prefix;
      prefix = entry.getKey();

      List<Property> properties;
      properties = entry.getValue();

      for (var property : properties) {
        properties0(prefix, property);
      }
    }
  }

  private void properties0(Prefix prefix, Property property) {
    method(
      PRIVATE, VOID, name(methodName(prefix, property)),
      include(() -> properties1(prefix, property))
    );
  }

  private void properties1(Prefix prefix, Property property) {
    if (prefix instanceof Breakpoint breakpoint) {

      if (breakpoint.length == 0) {
        for (var name : property.names) {
          p(
            v("style"),
            argument(NL, property.className, n(name.constantName), NL),
            include(() -> {
              for (var methodName : property.methodNames) {
                argument(
                  v(methodName),
                  include(() -> {
                    for (var value : name.values) {
                      propertyValue(value);
                    }
                  }),
                  NL
                );
              }
            })
          );
        }
      }

      else {
        p(
          v("media"),

          argument(NL, v("minWidth"), argument(v("px"), argument(i(breakpoint.length)))),

          include(() -> {
            for (var name : property.names) {
              argument(
                NL,
                v("style"),
                argument(NL, property.className, n(name.constantName), NL),
                include(() -> {
                  for (var methodName : property.methodNames) {
                    argument(
                      v(methodName),
                      include(() -> {
                        for (var value : name.values) {
                          propertyValue(value);
                        }
                      }),
                      NL
                    );
                  }
                })
              );
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

  private void propertyValue(Value value) {
    if (value instanceof Keyword kw) {
      argument(n(kw.fieldName()));
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type = " + value.getClass()
      );
    }
  }

  private String methodName(Prefix prefix, Property property) {
    return prefix.className.simpleName() + property.className.simpleName();
  }

}