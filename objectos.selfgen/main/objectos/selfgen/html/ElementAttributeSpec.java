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

import objectos.code.ClassName;

class ElementAttributeSpec extends AttributeSpec {

  ElementAttributeSpec(String name) {
    super(name);
  }

  @Override
  final ElementAttributeSpec toElementAttributeSpec(ElementSpec parent) {
    var className = parent.className;

    interfaceMap.put(className.simpleName(), className);

    var parentClassName = parent.instructionClassName;

    elementInstructionMap.put(parentClassName.simpleName(), parent.instructionClassName);

    if (elementInstructionMap.size() > 1) {
      instructionClassName = ClassName.of(ThisTemplate.API, simpleName() + "Attribute");
    }

    return this;
  }

}