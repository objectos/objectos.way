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
package objectos.specgen.css;

import java.util.List;
import java.util.Set;
import javax.lang.model.SourceVersion;
import objectos.code.ClassTypeName;
import objectos.code.JavaTemplate;
import objectos.specgen.util.JavaNames;

class PropertyModuleTemplate extends JavaTemplate {

  private static final String BOOT = "objectos.selfgen.css";
  private static final String SPEC = "objectos.selfgen.css.spec";

  private static final ClassTypeName ABSTRACT_PROPERTY_MODULE
      = ClassTypeName.of(BOOT, "AbstractPropertyModule");

  private static final ClassTypeName SOURCE
      = ClassTypeName.of(SPEC, "Source");

  private final String globalSig = "globalSig";

  private List<Property> group;

  private Property property;

  @Override
  protected final void definition() {
    packageDeclaration(BOOT);

    autoImports();

    classDeclaration(
      FINAL, name(simpleName()), extendsClause(ABSTRACT_PROPERTY_MODULE),

      method(
        annotation(Override.class),
        FINAL, VOID, name("propertyDefinition"),

        include(this::def0keywords),

        include(this::def1MainProperty),

        include(this::def2GroupProperties)
      )
    );
  }

  final void set(Property property, List<Property> group) {
    this.property = property;

    this.group = group;
  }

  private void def0keywords() {
    for (var keyword : keywords()) {
      var id = JavaNames.toValidMethodName(keyword);

      if (SourceVersion.isKeyword(id)) {
        id = id + "Kw";
      }

      p(VAR, name(id), v("keyword"), argument(s(keyword)));
    }
  }

  private void def1MainProperty() {
    p(
      v("property"), NL,

      argument(s(property.name())), NL,

      NL,

      argument(v("formal"), include(this::formalArgs)), NL,

      argument(n(globalSig), NL)
    );
  }

  private void def2GroupProperties() {
    if (group.isEmpty()) {
      return;
    }

    var first = group.get(0);

    p(
      v("property"), NL,

      argument(v("names"), include(this::namesArgs)), NL,

      NL,

      argument(v("formal"), include(() -> formalArgs(first))), NL,

      argument(n(globalSig), NL)
    );
  }

  private void formalArgs() {
    formalArgs(property);
  }

  private void formalArgs(Property property) {
    code(NL);

    argument(SOURCE, n("MDN"));

    code(NL);

    argument(s(property.formal()), NL);

    var valueTypes = property.valueTypes();

    if (!valueTypes.isEmpty()) {
      code(NL);

      var valueType = valueTypes.get(0);

      argument(s(valueType.join()), NL);

      for (int i = 1, size = valueTypes.size(); i < size; i++) {
        valueType = valueTypes.get(0);

        argument(s(valueType.join()), NL);
      }
    }

    code(NL);
  }

  private Set<String> keywords() {
    var builder = KeywordSet.builder();

    property.acceptKeywordSetBuilder(builder);

    for (var groupProperty : group) {
      groupProperty.acceptKeywordSetBuilder(builder);
    }

    var keywordSet = builder.build();

    return keywordSet.values;
  }

  private void namesArgs() {
    for (int i = 0, size = group.size(); i < size; i++) {
      var prop = group.get(i);

      argument(s(prop.name()));
    }
  }

  private String simpleName() {
    return JavaNames.toValidClassName(
      property.name() + "PropertyModule"
    );
  }

}