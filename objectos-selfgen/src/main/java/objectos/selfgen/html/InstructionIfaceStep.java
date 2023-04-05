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

import java.util.Iterator;
import objectos.code.ClassTypeName;
import objectos.code.ParameterizedTypeName;

final class InstructionIfaceStep extends ThisTemplate {
  private static final ClassTypeName HTML_TEMPLATE
      = ClassTypeName.of("objectos.html", "HtmlTemplate");

  private static final ClassTypeName ITERATOR
      = ClassTypeName.of(Iterator.class);

  @Override
  protected final void definition() {
    packageDeclaration(HTML_TMPL);

    autoImports();

    interfaceDeclaration(
      PUBLIC, SEALED, name(INSTRUCTION),

      include(this::interfaceBody)
    );
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

          include(() -> interfaceBody0Attr(attribute)),

          permitsClause(INTERNAL_INSTRUCTION)
        );
      }
    }

    interfaceDeclaration(
      SEALED, name(AMBIGUOUS_INST),

      include(this::interfaceBodyExtendsAll),

      permitsClause(INTERNAL_INSTRUCTION)
    );

    interfaceDeclaration(
      SEALED, name(GLOBAL_ATTRIBUTE),

      include(this::interfaceBodyExtendsAll),

      permitsClause(EXTERNAL_ATTRIBUTE, INTERNAL_INSTRUCTION)
    );

    interfaceDeclaration(
      SEALED, name(EXTERNAL_ATTRIBUTE),
      extendsClause(GLOBAL_ATTRIBUTE),

      include(this::interfaceBody2External)
    );

    interfaceDeclaration(
      SEALED, name(ELEMENT_CONTENTS),

      include(this::interfaceBody3Contents),

      permitsClause(HTML_TEMPLATE, INTERNAL_INSTRUCTION)
    );

    interfaceDeclaration(
      SEALED, name("Fragment"),

      include(this::interfaceBodyExtendsAll),

      permitsClause(
        ClassTypeName.of(HTML_INTERNAL, "InternalFragment")
      )
    );

    interfaceDeclaration(
      SEALED, name("NoOp"),

      include(this::interfaceBodyExtendsAll),

      permitsClause(
        ClassTypeName.of(HTML_INTERNAL, "InternalNoOp")
      )
    );
  }

  private void interfaceBody0Attr(AttributeSpec attribute) {
    for (var className : attribute.elementInstructionMap.values()) {
      extendsClause(className);
    }
  }

  private void interfaceBody2External() {
    interfaceDeclaration(
      NON_SEALED, name("Id"),
      extendsClause(EXTERNAL_ATTRIBUTE),

      method(
        STRING, name("value")
      )
    );

    interfaceDeclaration(
      NON_SEALED, name("StyleClass"),
      extendsClause(EXTERNAL_ATTRIBUTE),

      method(
        STRING, name("value")
      )
    );

    interfaceDeclaration(
      NON_SEALED, name("StyleClassSet"),
      extendsClause(EXTERNAL_ATTRIBUTE),

      method(
        ParameterizedTypeName.of(ITERATOR, STRING), name("value")
      )
    );
  }

  private void interfaceBody3Contents() {
    for (var element : spec.elements()) {
      if (element.hasEndTag()) {
        extendsClause(NL, element.instructionClassName);
      }
    }
  }

  private void interfaceBodyExtendsAll() {
    for (var element : spec.elements()) {
      extendsClause(NL, element.instructionClassName);
    }
  }
}