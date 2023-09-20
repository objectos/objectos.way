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
package selfgen.css.iutil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.ClassName;
import objectos.code.Code;

final class FrameworkClassStep extends ThisTemplate {

  private Iterable<Prefix> prefixes;

  public FrameworkClassStep(CssUtilSelfGen spec) {
    super(spec);
  }

  @Override
  final String contents() {
    prefixes = spec.prefixList;

    className(
      ClassName.of(CSS_UTIL, "Framework")
    );

    return code."""
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
    package \{packageName};
    \{importList}
    \{GENERATED_MSG}
    public final class \{simpleName} extends \{CSS_TEMPLATE} {

    \{definitionMethod()}

    \{prefixes()}

    \{properties()}

    }
    """;
  }

  private String definitionMethod() {
    List<String> stmts;
    stmts = new ArrayList<>();

    for (var prefix : prefixes) {
      String methodName;
      methodName = prefix.className.simpleName();

      stmts.add(
        code."    \{methodName}();"
      );
    }

    String body;
    body = stmts.stream().collect(Collectors.joining("\n"));

    return code."""
      @Override
      protected final void definition() {
    \{body}
      }
    """;
  }

  private String prefixes() {
    List<String> result;
    result = new ArrayList<>();

    for (var prefix : prefixes) {
      String prefixName;
      prefixName = prefix.className.simpleName();

      List<PropertyClass> properties;
      properties = prefix.propertyClassList;

      List<String> stmts;
      stmts = new ArrayList<>();

      for (var property : properties) {
        String methodName;
        methodName = methodName(prefix, property);

        stmts.add(
          code."    \{methodName}();"
        );
      }

      String body;
      body = stmts.stream().collect(Collectors.joining("\n"));

      String m;
      m = code."""
        private void \{prefixName}() {
      \{body}
        }
      """;

      result.add(m);
    }

    return result.stream().collect(Collectors.joining("\n"));
  }

  private String properties() {
    List<String> result;
    result = new ArrayList<>();

    for (var prefix : prefixes) {
      List<PropertyClass> properties;
      properties = prefix.propertyClassList;

      for (var property : properties) {
        String methodName;
        methodName = methodName(prefix, property);

        String body;
        body = propertiesBody(prefix, property);

        result.add(
          code."""
            private void \{methodName}() {
          \{body}
            }
          """
        );
      }
    }

    return result.stream().collect(Collectors.joining("\n"));
  }

  private String propertiesBody(Prefix prefix, PropertyClass property) {
    List<String> styles;
    styles = propertiesStyles(prefix, property);

    if (prefix instanceof Prefix.Breakpoint breakpoint && breakpoint.length > 0) {
      String s;
      s = styles.stream().collect(Collectors.joining(",\n"));

      s = Code.indent(s, 2);

      return code."""
          media(
            minWidth(px(\{breakpoint.length})),

      \{s}
          );
      """;
    }

    else {
      return styles.stream().collect(Collectors.joining(";\n", "", ";\n"));
    }
  }

  private List<String> propertiesStyles(Prefix prefix, PropertyClass property) {
    List<String> result;
    result = new ArrayList<>();

    String pfix;
    pfix = prefix.className.simpleName();

    String ppty;
    ppty = property.className.simpleName();

    for (var method : property.styleMethodList) {
      String selector;
      selector = switch(method.selectorKind) {
        case STANDARD -> code."\{pfix}.\{ppty}.\{method.constantName}";

        case ALL_BUT_FIRST -> code."\{pfix}.\{ppty}.\{method.constantName}, GT, any, PLUS, any";

        case HOVER -> code."\{pfix}.\{ppty}.\{method.constantName}, _hover";
      };

      List<String> decls;
      decls = new ArrayList<>();

      for (var declaration : method.declarationList) {
        List<String> arguments;
        arguments = new ArrayList<>();

        for (var value : declaration.values()) {
          arguments.add(value.arg());
        }

        String args;
        args = arguments.stream().collect(Collectors.joining(", "));

        decls.add(code."      \{declaration.methodName()}(\{args})");
      }

      String styleDeclarations;
      styleDeclarations = decls.stream().collect(Collectors.joining(",\n"));

      result.add(
        code."""
            style(
              \{selector},

        \{styleDeclarations}
            )
        """
      );
    }

    return result;
  }

  private String methodName(Prefix prefix, PropertyClass property) {
    return prefix.className.simpleName() + property.className.simpleName();
  }

}