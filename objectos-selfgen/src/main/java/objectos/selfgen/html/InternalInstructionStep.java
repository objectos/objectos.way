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

final class InternalInstructionStep extends ThisTemplate {
  @Override
  protected final void definition() {
    packageDeclaration(HTML_INTERNAL);

    autoImports();

    enumDeclaration(
      PUBLIC, name(INTERNAL_INSTRUCTION),

      include(this::enumBody)
    );
  }

  private void enumBody() {
    for (var element : spec.elements()) {
      implementsClause(NL, element.instructionClassName);
    }

    implementsClause(NL, GLOBAL_ATTRIBUTE);

    enumConstant(name("INSTANCE"));
  }
}