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

final class InstructionIfaceStep extends ThisTemplate {
  @Override
  protected final void definition() {
    packageDeclaration(HTML_TMPL);

    autoImports();

    interfaceDeclaration(
      PUBLIC, SEALED, name(INSTRUCTION),

      include(this::interfaceBody)
    );
  }

  private void attributeExtends(AttributeSpec attribute) {
    for (var className : attribute.elementInstructionMap.values()) {
      extendsClause(className);
    }
  }

  private void globalAttributeExtends() {
    for (var element : spec.elements()) {
      extendsClause(element.instructionClassName);
    }
  }

  private void interfaceBody() {
    for (var element : spec.elements()) {
      interfaceDeclaration(
        SEALED, name(element.instructionClassName),

        extendsClause(INSTRUCTION)
      );
    }

    for (var attribute : spec.attributes()) {
      var className = attribute.instructionClassName;

      if (className != null) {
        interfaceDeclaration(
          SEALED, name(className),

          include(() -> attributeExtends(attribute)),

          permitsClause(INTERNAL_INSTRUCTION)
        );
      }
    }

    interfaceDeclaration(
      SEALED, name(GLOBAL_ATTRIBUTE),

      include(this::globalAttributeExtends),

      permitsClause(INTERNAL_INSTRUCTION)
    );
  }
}