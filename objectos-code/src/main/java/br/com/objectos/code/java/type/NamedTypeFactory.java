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

import br.com.objectos.code.util.SimpleTypeVisitor;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import objectos.lang.Check;

final class NamedTypeFactory extends SimpleTypeVisitor<NamedType, Void> {

  private static final NamedTypeVisitor<NamedType, Void> ARRAY_HELPER = new ArrayTypeNameHelper();

  private static final NamedTypeFactory INSTANCE = new NamedTypeFactory();

  private NamedTypeFactory() {}

  public static NamedType of(TypeMirror type) {
    Check.notNull(type, "type == null");

    return type.accept(INSTANCE, null);
  }

  @Override
  public final NamedType visitArray(ArrayType t, Void p) {
    TypeMirror componentType = t.getComponentType();
    NamedType componentTypeName = componentType.accept(INSTANCE, null);
    return componentTypeName.acceptTypeNameVisitor(ARRAY_HELPER, null);
  }

  @Override
  public final NamedType visitDeclared(DeclaredType t, Void p) {
    TypeElement typeElement;
    typeElement = (TypeElement) t.asElement();

    NamedClass rawName;
    rawName = NamedClass.of(typeElement);

    NamedClassOrParameterized result;
    result = rawName;

    List<? extends TypeMirror> typeArguments;
    typeArguments = t.getTypeArguments();

    if (!typeArguments.isEmpty()) {
      List<NamedType> argumentNames;
      argumentNames = new ArrayList<>(typeArguments.size());

      for (int i = 0; i < typeArguments.size(); i++) {
        TypeMirror typeArgument;
        typeArgument = typeArguments.get(i);

        NamedType argumentName;
        argumentName = typeArgument.accept(this, p);

        argumentNames.add(argumentName);
      }

      result = NamedParameterized.of(rawName, argumentNames);
    }

    return result;
  }

  @Override
  public final NamedType visitNoType(NoType t, Void p) {
    return NamedVoid.VOID;
  }

  @Override
  public final NamedType visitPrimitive(PrimitiveType t, Void p) {
    switch (t.getKind()) {
      case BOOLEAN:
        return NamedPrimitive._boolean();
      case BYTE:
        return NamedPrimitive._byte();
      case SHORT:
        return NamedPrimitive._short();
      case INT:
        return NamedPrimitive._int();
      case LONG:
        return NamedPrimitive._long();
      case CHAR:
        return NamedPrimitive._char();
      case FLOAT:
        return NamedPrimitive._float();
      case DOUBLE:
        return NamedPrimitive._double();
      default:
        throw new AssertionError();
    }
  }

  @Override
  public final NamedType visitTypeVariable(TypeVariable t, Void p) {
    String name;
    name = t.toString();

    return NamedTypeVariable.of(name);
  }

  @Override
  public final NamedType visitWildcard(WildcardType t, Void p) {
    TypeMirror extendsBound = t.getExtendsBound();
    if (extendsBound != null) {
      return NamedTypes.wildcardExtends(asRTN(extendsBound));
    }

    TypeMirror superBound = t.getSuperBound();
    if (superBound != null) {
      return NamedTypes.wildcardSuper(asRTN(superBound));
    }

    return NamedTypes.wildcard();
  }

  @Override
  protected final NamedType defaultAction(TypeMirror e, Void p) {
    throw new UnsupportedOperationException("Implement me: kind=" + e.getKind());
  }

  private NamedReferenceType asRTN(TypeMirror type) {
    NamedType typeName = type.accept(INSTANCE, null);
    return (NamedReferenceType) typeName;
  }

  private static class ArrayTypeNameHelper extends SimpleNamedTypeVisitor<NamedType, Void> {
    @Override
    public final NamedType visitNamedArray(NamedArray t, Void p) {
      return t.toNamedArray();
    }

    @Override
    public final NamedType visitNamedClass(NamedClass t, Void p) {
      return t.toNamedArray();
    }

    @Override
    public final NamedType visitNamedParameterized(NamedParameterized t, Void p) {
      return t.toNamedArray();
    }

    @Override
    public final NamedType visitNamedPrimitive(NamedPrimitive t, Void p) {
      return t.toNamedArray();
    }

    @Override
    public final NamedType visitNamedTypeVariable(NamedTypeVariable t, Void p) {
      return t.toNamedArray();
    }

    @Override
    protected final NamedType defaultAction(NamedType t, Void p) {
      throw new UnsupportedOperationException(
        "Cannot convert " + t.getClass() + " to ArrayTypeName.");
    }
  }

}