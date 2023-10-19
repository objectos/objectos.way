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
package selfgen.css.util;

import java.util.ArrayList;
import java.util.List;

public final class StyleMethod {

  public record Declaration(String methodName, List<Value> values) {}

  public final SelectorKind selectorKind;

  public final String constantName;

  public final List<Declaration> declarationList = new ArrayList<>();

  StyleMethod(SelectorKind selectorKind, String constantName) {
    this.selectorKind = selectorKind;
    this.constantName = constantName;
  }

  public final void addDeclaration(String methodName, Value value) {
    List<Value> values;
    values = List.of(value);

    addDeclaration(methodName, values);
  }

  public final void addDeclaration(String methodName, List<Value> values) {
    Declaration declaration;
    declaration = new Declaration(methodName, values);

    declarationList.add(declaration);
  }

}