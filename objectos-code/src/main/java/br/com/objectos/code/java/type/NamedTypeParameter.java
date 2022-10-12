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

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.declaration.ClassCode;
import br.com.objectos.code.java.declaration.ClassCodeElement;
import br.com.objectos.code.java.declaration.MethodCode.Builder;
import br.com.objectos.code.java.declaration.MethodCodeElement;
import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.io.CanGenerateImportDeclaration;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.JavaFileImportSet;
import java.util.List;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import objectos.lang.Check;

public abstract class NamedTypeParameter extends AbstractCodeElement
    implements
    CanGenerateImportDeclaration,
    ClassCodeElement,
    MethodCodeElement {

  NamedTypeParameter() {}

  @Ignore("AggregatorGenProcessor")
  public static NamedTypeParameter named(String name) {
    Check.notNull(name, "name == null");
    return new Unbounded(name);
  }

  @Ignore("AggregatorGenProcessor")
  public static NamedTypeParameter of(TypeParameterElement element) {
    Check.notNull(element, "element == null");

    NamedTypeParameter result;
    result = named(element.getSimpleName().toString());

    List<? extends TypeMirror> bounds;
    bounds = element.getBounds();

    for (int i = 0; i < bounds.size(); i++) {
      TypeMirror bound;
      bound = bounds.get(i);

      NamedType boundName;
      boundName = NamedType.of(bound);

      if (!boundName.isJavaLangObject()) {
        result = result.addBound0(boundName);
      }
    }

    return result;
  }

  public static NamedTypeParameter typeParam(String name) {
    Check.notNull(name, "name == null");
    return new Unbounded(name);
  }

  public static NamedTypeParameter typeParam(String name, NamedClass bound) {
    Check.notNull(name, "name == null");
    Check.notNull(bound, "bound == null");
    return named(name).addBound0(bound);
  }

  public static NamedTypeParameter typeParam(
      String name, NamedClass bound1, NamedClass bound2) {
    Check.notNull(name, "name == null");
    Check.notNull(bound1, "bound1 == null");
    Check.notNull(bound2, "bound2 == null");
    return named(name).addBound0(bound1).addBound0(bound2);
  }

  public static NamedTypeParameter typeParam(
      String name, NamedClass bound1, NamedClass bound2, NamedClass bound3) {
    Check.notNull(name, "name == null");
    Check.notNull(bound1, "bound1 == null");
    Check.notNull(bound2, "bound2 == null");
    Check.notNull(bound3, "bound3 == null");
    return named(name).addBound0(bound1).addBound0(bound2).addBound0(bound3);
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.addTypeParameter(this);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writePreIndentation();
    w.writeTypeParameterName(this);
    return w;
  }

  @Override
  public final void acceptMethodCodeBuilder(Builder builder) {
    builder.addTypeParameter(this);
  }

  public final NamedTypeParameter addBound(Class<?> bound) {
    return addBound0(NamedClass.of(bound));
  }

  public final NamedTypeParameter addBound(NamedClass bound) {
    Check.notNull(bound, "bound == null");
    return addBound0(bound);
  }

  public final NamedTypeParameter addBound(NamedTypeVariable bound) {
    Check.notNull(bound, "bound == null");
    return addBound0(bound);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof NamedTypeParameter)) {
      return false;
    }
    NamedTypeParameter that = (NamedTypeParameter) obj;
    return toString().equals(that.toString());
  }

  @Override
  public final int hashCode() {
    return toString().hashCode();
  }

  @Override
  public abstract String toString();

  abstract NamedTypeParameter addBound0(NamedType typeName);

  private static class Bounded extends NamedTypeParameter {

    private final NamedType bound;
    private final NamedTypeParameter previous;
    private final String separator;

    Bounded(NamedTypeParameter previous, String separator, NamedType bound) {
      this.previous = previous;
      this.separator = separator;
      this.bound = bound;
    }

    static Bounded first(Unbounded unbounded, NamedType bound) {
      return new Bounded(unbounded, " extends ", bound);
    }

    @Override
    public final String acceptJavaFileImportSet(JavaFileImportSet set) {
      return previous.acceptJavaFileImportSet(set) + separator + bound.acceptJavaFileImportSet(set);
    }

    @Override
    public final String toString() {
      return previous.toString() + separator + bound.toString();
    }

    @Override
    final NamedTypeParameter addBound0(NamedType typeName) {
      return new Bounded(this, " & ", typeName);
    }

  }

  private static class Unbounded extends NamedTypeParameter {

    private final String name;

    Unbounded(String name) {
      this.name = name;
    }

    @Override
    public final String acceptJavaFileImportSet(JavaFileImportSet set) {
      return name;
    }

    @Override
    public final String toString() {
      return name;
    }

    @Override
    final NamedTypeParameter addBound0(NamedType typeName) {
      return Bounded.first(this, typeName);
    }

  }

}