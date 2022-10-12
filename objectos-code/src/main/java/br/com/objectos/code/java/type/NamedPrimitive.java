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

import br.com.objectos.code.java.io.JavaFileImportSet;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.lang.model.type.TypeKind;

public final class NamedPrimitive extends NamedType
    implements
    NamedSingleDimensionArrayComponent {

  public static final NamedPrimitive BOOLEAN = new NamedPrimitive("boolean", Boolean.class);

  public static final NamedPrimitive BYTE = new NamedPrimitive("byte", Byte.class);

  public static final NamedPrimitive CHAR = new NamedPrimitive("char", Character.class);

  public static final NamedPrimitive DOUBLE = new NamedPrimitive("double", Double.class);

  public static final NamedPrimitive FLOAT = new NamedPrimitive("float", Float.class);

  public static final NamedPrimitive INT = new NamedPrimitive("int", Integer.class);

  public static final NamedPrimitive LONG = new NamedPrimitive("long", Long.class);

  public static final NamedPrimitive SHORT = new NamedPrimitive("short", Short.class);

  private final String name;
  private final NamedClass wrapperClass;

  private NamedPrimitive(String name, Class<?> wrapperClass) {
    this.name = name;
    this.wrapperClass = NamedClass.of(wrapperClass);
  }

  public static NamedPrimitive _boolean() {
    return NamedPrimitive.BOOLEAN;
  }

  public static NamedPrimitive _byte() {
    return NamedPrimitive.BYTE;
  }

  public static NamedPrimitive _char() {
    return NamedPrimitive.CHAR;
  }

  public static NamedPrimitive _double() {
    return NamedPrimitive.DOUBLE;
  }

  public static NamedPrimitive _float() {
    return NamedPrimitive.FLOAT;
  }

  public static NamedPrimitive _int() {
    return NamedPrimitive.INT;
  }

  public static NamedPrimitive _long() {
    return NamedPrimitive.LONG;
  }

  public static NamedPrimitive _short() {
    return NamedPrimitive.SHORT;
  }

  public static NamedPrimitive of(TypeKind kind) {
    return JavaTypeKinds.get(kind);
  }

  @Override
  public final String acceptJavaFileImportSet(JavaFileImportSet set) {
    return name;
  }

  @Override
  public final <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p) {
    return visitor.visitNamedPrimitive(this, p);
  }

  @Override
  public final NamedType getArrayCreationExpressionName() {
    return this;
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

  public final NamedClass toWrapperClass() {
    return wrapperClass;
  }

  private static class JavaTypeKinds {

    static final EnumMap<TypeKind, NamedPrimitive> MAP = buildMap();

    static EnumMap<TypeKind, NamedPrimitive> buildMap() {
      Map<TypeKind, NamedPrimitive> map = new HashMap<>();

      map.put(TypeKind.BOOLEAN, BOOLEAN);
      map.put(TypeKind.BYTE, BYTE);
      map.put(TypeKind.CHAR, CHAR);
      map.put(TypeKind.DOUBLE, DOUBLE);
      map.put(TypeKind.FLOAT, FLOAT);
      map.put(TypeKind.INT, INT);
      map.put(TypeKind.LONG, LONG);
      map.put(TypeKind.SHORT, SHORT);

      return new EnumMap<TypeKind, NamedPrimitive>(map);
    }

    static NamedPrimitive get(TypeKind kind) {
      if (!MAP.containsKey(kind)) {
        throw new IllegalArgumentException("kind is not of a valid primitive type");
      } else {
        return MAP.get(kind);
      }
    }

  }

}