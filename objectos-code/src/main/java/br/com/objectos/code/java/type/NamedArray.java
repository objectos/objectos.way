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

import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.element.Symbols;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.MethodReference;
import br.com.objectos.code.java.expression.TypeWitness;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.JavaFileImportSet;
import objectos.lang.Check;

public abstract class NamedArray extends NamedReferenceType {

  NamedArray() {}

  public static NamedArray of(NamedArray type) {
    Check.notNull(type, "type == null");

    return new MultiDimension(type);
  }

  public static NamedArray of(NamedSingleDimensionArrayComponent type) {
    Check.notNull(type, "type == null");

    return new SingleDimension(type);
  }

  static NamedArray uncheckedSingleDimension(NamedSingleDimensionArrayComponent type) {
    return new SingleDimension(type);
  }

  @Override
  public final String acceptJavaFileImportSet(JavaFileImportSet set) {
    return getComponent().acceptJavaFileImportSet(set) + "[]";
  }

  @Override
  public final <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p) {
    return visitor.visitNamedArray(this, p);
  }

  public abstract int dimension();

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof NamedArray)) {
      return false;
    }
    return toString().equals(obj.toString());
  }

  @Override
  public final NamedType getArrayCreationExpressionName() {
    return getComponent().getArrayCreationExpressionName();
  }

  public abstract NamedArrayComponent getComponent();

  public abstract NamedArrayComponent getDeepComponent();

  @Override
  public final int hashCode() {
    return toString().hashCode();
  }

  public abstract CodeElement printVarArgsSymbol();

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
    return new MultiDimension(this);
  }

  @Override
  public final String toString() {
    return getComponent() + "[]";
  }

  private static class MultiDimension extends NamedArray {

    private final NamedArray componentTypeName;

    MultiDimension(NamedArray componentTypeName) {
      this.componentTypeName = componentTypeName;
    }

    @Override
    public final int dimension() {
      return componentTypeName.dimension() + 1;
    }

    @Override
    public final NamedArrayComponent getComponent() {
      return componentTypeName;
    }

    @Override
    public final NamedArrayComponent getDeepComponent() {
      return componentTypeName.getDeepComponent();
    }

    @Override
    public final CodeElement printVarArgsSymbol() {
      return new VarArgsHelper(componentTypeName.printVarArgsSymbol());
    }

  }

  private static class SingleDimension extends NamedArray {

    private final NamedSingleDimensionArrayComponent componentTypeName;

    SingleDimension(NamedSingleDimensionArrayComponent componentTypeName) {
      this.componentTypeName = componentTypeName;
    }

    @Override
    public final int dimension() {
      return 1;
    }

    @Override
    public final NamedArrayComponent getComponent() {
      return componentTypeName;
    }

    @Override
    public final NamedArrayComponent getDeepComponent() {
      return componentTypeName;
    }

    @Override
    public final CodeElement printVarArgsSymbol() {
      return Symbols.ellipsis();
    }

  }

  private static class VarArgsHelper extends AbstractCodeElement {

    private final CodeElement element;

    VarArgsHelper(CodeElement element) {
      this.element = element;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      w.writePreIndentation();
      w.write("[]");
      w.writeCodeElement(element);
      return w;
    }

  }

}
