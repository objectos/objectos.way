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
package objectos.selfgen.html;

import objectos.code.ArrayTypeName;
import objectos.code.TypeVariableName;

final class GeneratedAbstractTemplateStep extends ThisTemplate {

  private static final TypeVariableName N = TypeVariableName.of("N");

  private AttributeSpec attribute;

  @Override
  protected final void definition() {
    packageDeclaration("br.com.objectos.html.tmpl");

    autoImports();

    classDeclaration(
      ABSTRACT, name("GeneratedAbstractTemplate"),
      include(this::classBody)
    );
  }

  private void attrMethod(String name) {
    method(
      PUBLIC, FINAL, attribute.className, name(name),
      include(this::attrMethodParam),
      p(
        RETURN,
        v("addStandardAttribute"),
        argument(STD_ATTR_NAME, n(attribute.constantName)),
        include(this::attrMethodValueArg)
      )
    );
  }

  private void attrMethodParam() {
    AttributeKind kind = attribute.kind();

    if (kind.isString()) {
      parameter(STRING, name("value"));
    }
  }

  private void attrMethodValueArg() {
    AttributeKind kind = attribute.kind();

    if (kind.isString()) {
      argument(n("value"));
    }
  }

  private void classBody() {
    var template = spec.template();

    for (var element : spec.elements()) {
      elementValuesMethod(element);

      if (template.shouldIncludeText(element)) {
        elementTextMethod(element);
      }
    }

    for (var attribute : spec.attributes()) {
      for (String name : attribute.methodNames()) {
        this.attribute = attribute;

        if (template.shouldIncludeAttribute(name)) {
          attrMethod(name);
        }
      }
    }

    method(
      ABSTRACT, typeParameter("N", STD_ATTR_NAME), N, name("addStandardAttribute"),
      parameter(N, name("name"))
    );

    method(
      ABSTRACT, typeParameter("N", STD_ATTR_NAME), N, name("addStandardAttribute"),
      parameter(N, name("name")),
      parameter(STRING, name("value"))
    );

    method(
      ABSTRACT, ELEMENT_NAME, name("addStandardElement"),
      parameter(STD_ELEMENT_NAME, name("name")),
      parameter(STRING, name("text"))
    );

    var valueArray = ArrayTypeName.of(VALUE);

    method(
      ABSTRACT, ELEMENT_NAME, name("addStandardElement"),
      parameter(STD_ELEMENT_NAME, name("name")),
      parameter(valueArray, name("values"))
    );
  }

  private void elementTextMethod(ElementSpec element) {
    method(
      PUBLIC, FINAL, ELEMENT_NAME, name(element.methodName()),
      parameter(STRING, name("text")),
      p(
        RETURN,
        v("addStandardElement"),
        argument(STD_ELEMENT_NAME, n(element.constantName)),
        argument(n("text"))
      )
    );
  }

  private void elementValuesMethod(ElementSpec element) {
    method(
      PUBLIC, FINAL, ELEMENT_NAME, name(element.methodName()),
      parameter(element.className, ELLIPSIS, name("values")),
      p(
        RETURN,
        v("addStandardElement"),
        argument(STD_ELEMENT_NAME, n(element.constantName)),
        argument(n("values"))
      )
    );
  }

}
