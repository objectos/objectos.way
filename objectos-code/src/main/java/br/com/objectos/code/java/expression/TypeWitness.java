/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.expression;

import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.type.NamedType;
import java.util.Arrays;
import objectos.util.UnmodifiableList;

public class TypeWitness extends AbstractImmutableCodeElement {

  private static final TypeWitness EMPTY = new TypeWitness(
      openAngle(), closeAngle()
  );

  private TypeWitness(CodeElement... elements) {
    super(elements);
  }

  private TypeWitness(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static TypeWitness witness0() {
    return EMPTY;
  }

  static TypeWitness witness0(Iterable<? extends NamedType> types) {
    return new TypeWitness(
        openAngle(), commaSeparated(types), closeAngle()
    );
  }

  static TypeWitness witness0(NamedType t1) {
    return witness1(t1);
  }

  static TypeWitness witness0(NamedType t1, NamedType t2) {
    return witness1(t1, t2);
  }

  static TypeWitness witness0(NamedType t1, NamedType t2, NamedType t3) {
    return witness1(t1, t2, t3);
  }

  private static TypeWitness witness1(NamedType... types) {
    return witness0(Arrays.asList(types));
  }

}
