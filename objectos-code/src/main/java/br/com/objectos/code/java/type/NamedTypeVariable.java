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
package br.com.objectos.code.java.type;

import br.com.objectos.code.java.declaration.ThrowsElement;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.MethodReference;
import br.com.objectos.code.java.expression.TypeWitness;
import br.com.objectos.code.java.io.JavaFileImportSet;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeParameterElement;
import objectos.lang.Check;

public class NamedTypeVariable extends NamedReferenceType
    implements
    NamedSingleDimensionArrayComponent,
    ThrowsElement {

  private final String name;

  private NamedTypeVariable(String name) {
    this.name = name;
  }

  public static NamedTypeVariable of(String name) {
    Check.notNull(name, "name == null");

    return new NamedTypeVariable(name);
  }

  public static NamedTypeVariable of(TypeParameterElement element) {
    Check.notNull(element, "element == null");

    Name simpleName;
    simpleName = element.getSimpleName();

    String name;
    name = simpleName.toString();

    return new NamedTypeVariable(name);
  }

  @Override
  public final String acceptJavaFileImportSet(JavaFileImportSet set) {
    return name;
  }

  @Override
  public final void acceptThrowsElementConsumer(Consumer consumer) {
    consumer.addThrownType(this);
  }

  @Override
  public final <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p) {
    return visitor.visitNamedTypeVariable(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof NamedTypeVariable)) {
      return false;
    }

    NamedTypeVariable that;
    that = (NamedTypeVariable) obj;

    return name.equals(that.name);
  }

  @Override
  public final NamedType getArrayCreationExpressionName() {
    return NamedClass.object();
  }

  @Override
  public final int hashCode() {
    return name.hashCode();
  }

  @Override
  public final MethodReference ref(String methodName) {
    return Expressions.ref(this, methodName);
  }

  @Override
  public final MethodReference ref(TypeWitness witness, String methodName) {
    return Expressions.ref(this, witness, methodName);
  }

  @Override
  public final NamedArray toNamedArray() {
    return NamedArray.uncheckedSingleDimension(this);
  }

  @Override
  public final NamedType toNamedType() {
    return this;
  }

  @Override
  public final String toString() {
    return name;
  }

}