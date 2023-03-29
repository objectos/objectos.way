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

final class GeneratedHtmlTemplateStep extends ThisTemplate {

  private AttributeSpec attribute;

  @Override
  protected final void definition() {
    packageDeclaration("objectos.html.internal");

    autoImports();

    classDeclaration(
      ABSTRACT, name("GeneratedHtmlTemplate"),
      include(this::body)
    );
  }

  private void body() {
    var template = spec.template();

    for (var element : spec.elements()) {
      body0elementMethod(element);

      if (template.shouldIncludeText(element)) {
        body1elementMethodText(element);
      }
    }

    for (var attribute : spec.attributes()) {
      for (String name : attribute.methodNames()) {
        this.attribute = attribute;

        if (template.shouldIncludeAttribute(name)) {
          body2attrMethod(name);
        }
      }
    }

    method(
      ABSTRACT, VOID, name("attribute"),
      parameter(STD_ATTR_NAME, name("name"))
    );

    method(
      ABSTRACT, VOID, name("attribute"),
      parameter(STD_ATTR_NAME, name("name")),
      parameter(STRING, name("value"))
    );

    method(
      ABSTRACT, VOID, name("element"),
      parameter(STD_ELEMENT_NAME, name("name")),
      parameter(STRING, name("text"))
    );

    var array = ArrayTypeName.of(INSTRUCTION);

    method(
      ABSTRACT, VOID, name("element"),
      parameter(STD_ELEMENT_NAME, name("name")),
      parameter(array, name("contents"))
    );
  }

  private void body0elementMethod(ElementSpec element) {
    method(
      PUBLIC, FINAL, ELEMENT_CONTENTS, name(element.methodName()),
      parameter(element.instructionClassName, ELLIPSIS, name("contents")),

      p(
        v("element"),
        argument(STD_ELEMENT_NAME, n(element.constantName)),
        argument(n("contents"))
      ),
      p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
    );
  }

  private void body1elementMethodText(ElementSpec element) {
    method(
      PUBLIC, FINAL, ELEMENT_CONTENTS, name(element.methodName()),
      parameter(STRING, name("text")),
      p(
        v("element"),
        argument(STD_ELEMENT_NAME, n(element.constantName)),
        argument(n("text"))
      ),
      p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
    );
  }

  private void body2attrMethod(String name) {
    var returnType = attribute.instructionClassName;

    if (returnType == null) {
      if (attribute.global()) {
        returnType = GLOBAL_ATTRIBUTE;
      } else {
        returnType = attribute.elementInstructionMap
            .values()
            .iterator()
            .next();
      }
    }

    method(
      PUBLIC, FINAL, returnType, name(name),
      include(this::body2attrMethodParam),
      p(
        v("attribute"),
        argument(STD_ATTR_NAME, n(attribute.constantName)),
        include(this::body2attrMethodValueArg)
      ),
      p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
    );
  }

  private void body2attrMethodParam() {
    AttributeKind kind = attribute.kind();

    if (kind.isString()) {
      parameter(STRING, name("value"));
    }
  }

  private void body2attrMethodValueArg() {
    AttributeKind kind = attribute.kind();

    if (kind.isString()) {
      argument(n("value"));
    }
  }

}
