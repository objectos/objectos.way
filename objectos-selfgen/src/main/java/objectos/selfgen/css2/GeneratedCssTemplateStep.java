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
package objectos.selfgen.css2;

final class GeneratedCssTemplateStep extends ThisTemplate {

  private static final SelectorName UNIVERSAL = new SelectorName("any", "*");

  @Override
  protected final void definition() {
    packageDeclaration(CSS);

    autoImports();

    classDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      ABSTRACT, name("GeneratedCssTemplate"),

      include(this::selectors),

      include(this::keywords),

      method(
        PRIVATE, STATIC, NAMED_ELEMENT, name("named"),
        parameter(STRING, name("name")),
        p(RETURN, NEW, NAMED_ELEMENT, argument(n("name")))
      ),

      include(this::properties)
    );
  }

  private void selectors() {
    spec.selectors().stream()
        .sorted(SelectorName.ORDER_BY_FIELD_NAME)
        .forEach(this::selectorField);

    selectorField(UNIVERSAL);
  }

  private void selectorField(SelectorName selector) {
    field(
      PROTECTED, STATIC, FINAL, SELECTOR, name(selector.fieldName()),
      v("named"), argument(s(selector.selectorName()))
    );
  }

  private void keywords() {
    spec.keywords().stream()
        .sorted(KeywordName.ORDER_BY_FIELD_NAME)
        .forEach(this::keywordField);
  }

  private void keywordField(KeywordName keyword) {
    var type = keyword.fieldType();

    field(
      PROTECTED, STATIC, FINAL, type, name(keyword.fieldName),
      v("named"), argument(s(keyword.keywordName))
    );
  }

  private void properties() {
    spec.properties().stream()
        .sorted(Property.ORDER_BY_METHOD_NAME)
        .forEach(this::propertyMethods);
  }

  private void propertyMethods(Property property) {
    for (var signature : property.signatures()) {
      if (signature instanceof MethodSig.Sig1 sig1) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig1.type().className(), name(sig1.name())),
          p(
            RETURN, NEW, STYLE_DECLARATION1,
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig1.name()), v("self"))
          )
        );
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: type=" + signature.getClass()
        );
      }
    }
  }

}
