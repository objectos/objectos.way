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

import br.com.objectos.code.java.declaration.PackageName;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;

public class NamedTypes {

  private NamedTypes() {}

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

  public static NamedVoid _void() {
    return NamedVoid._void();
  }

  public static NamedArray a(NamedArray type) {
    return NamedArray.of(type);
  }

  public static NamedArray a(NamedSingleDimensionArrayComponent type) {
    return NamedArray.of(type);
  }

  public static NamedClass t(Class<?> type) {
    return NamedClass.of(type);
  }

  public static NamedClassOrParameterized t(
      NamedClass raw, Iterable<? extends NamedType> arguments) {
    Check.notNull(arguments, "arguments == null");

    UnmodifiableList<? extends NamedType> list;
    list = UnmodifiableList.copyOf(arguments);

    switch (list.size()) {
      case 0:
        Check.notNull(raw, "raw == null");
        return raw;
      default:
        return new NamedParameterized(raw, list);
    }
  }

  public static NamedClassOrParameterized t(NamedClass raw, NamedType... arguments) {
    Check.notNull(arguments, "arguments == null");

    switch (arguments.length) {
      case 0:
        Check.notNull(raw, "raw == null");
        return raw;
      default:
        return NamedParameterized.of(raw, arguments);
    }
  }

  public static NamedParameterized t(NamedClass raw, NamedType arg) {
    return NamedParameterized.of(raw, arg);
  }

  public static NamedParameterized t(NamedClass raw, NamedType arg1, NamedType arg2) {
    return NamedParameterized.of(raw, arg1, arg2);
  }

  public static NamedParameterized t(
      NamedClass raw, NamedType arg1, NamedType arg2, NamedType arg3) {
    return NamedParameterized.of(raw, arg1, arg2, arg3);
  }

  public static NamedClass t(NamedClass className, String simpleName) {
    return NamedClass.of(className, simpleName);
  }

  public static NamedClass t(PackageName packageName, String simpleName) {
    return NamedClass.of(packageName, simpleName);
  }

  public static NamedTypeVariable tvar(String name) {
    return NamedTypeVariable.of(name);
  }

  public static NamedWildcard wildcard() {
    return NamedWildcard.UNBOUND;
  }

  public static NamedWildcard wildcardExtends(NamedReferenceType bound) {
    Check.notNull(bound, "bound == null");
    return NamedWildcard.extendsUnchecked(bound);
  }

  public static NamedWildcard wildcardSuper(NamedReferenceType bound) {
    Check.notNull(bound, "bound == null");
    return NamedWildcard.superUnchecked(bound);
  }

}
