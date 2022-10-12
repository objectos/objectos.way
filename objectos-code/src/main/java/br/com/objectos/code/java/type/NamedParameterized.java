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

import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.MethodReference;
import br.com.objectos.code.java.expression.TypeWitness;
import br.com.objectos.code.java.io.JavaFileImportSet;
import objectos.lang.Check;
import objectos.lang.HashCode;
import objectos.util.UnmodifiableList;

public final class NamedParameterized extends NamedClassOrParameterized {

  private final UnmodifiableList<? extends NamedType> arguments;
  private final NamedClass raw;

  NamedParameterized(NamedClass raw, UnmodifiableList<? extends NamedType> arguments) {
    this.raw = raw;
    this.arguments = arguments;

    Check.argument(!arguments.isEmpty(), "arguments is empty");
  }

  public static NamedParameterized of(
      NamedClass raw, Iterable<? extends NamedType> arguments) {
    Check.notNull(raw, "raw == null");
    Check.notNull(arguments, "arguments == null");

    UnmodifiableList<? extends NamedType> list;
    list = UnmodifiableList.copyOf(arguments);

    return new NamedParameterized(raw, list);
  }

  public static NamedParameterized of(NamedClass raw, NamedType arg) {
    Check.notNull(raw, "raw == null");

    UnmodifiableList<NamedType> list;
    list = UnmodifiableList.of(arg);

    return new NamedParameterized(raw, list);
  }

  public static NamedParameterized of(NamedClass raw, NamedType... arguments) {
    Check.notNull(raw, "raw == null");

    UnmodifiableList<NamedType> list;
    list = UnmodifiableList.copyOf(arguments);

    return new NamedParameterized(raw, list);
  }

  public static NamedParameterized of(
      NamedClass raw, NamedType arg1, NamedType arg2) {
    Check.notNull(raw, "raw == null");

    UnmodifiableList<NamedType> list;
    list = UnmodifiableList.of(arg1, arg2);

    return new NamedParameterized(raw, list);
  }

  public static NamedParameterized of(
      NamedClass raw, NamedType arg1, NamedType arg2, NamedType arg3) {
    Check.notNull(raw, "raw == null");

    UnmodifiableList<NamedType> list;
    list = UnmodifiableList.of(arg1, arg2, arg3);

    return new NamedParameterized(raw, list);
  }

  @Override
  public final String acceptJavaFileImportSet(JavaFileImportSet set) {
    StringBuilder s;
    s = new StringBuilder(raw.acceptJavaFileImportSet(set));

    s.append('<');

    NamedType first;
    first = arguments.get(0);

    s.append(first.acceptJavaFileImportSet(set));

    for (int i = 1; i < arguments.size(); i++) {
      s.append(", ");

      NamedType next;
      next = arguments.get(i);

      s.append(next.acceptJavaFileImportSet(set));
    }

    s.append('>');

    return s.toString();
  }

  @Override
  public final <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p) {
    return visitor.visitNamedParameterized(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof NamedParameterized)) {
      return false;
    }
    NamedParameterized that = (NamedParameterized) obj;
    return raw.equals(that.raw)
        && arguments.equals(that.arguments);
  }

  @Override
  public final NamedType getArrayCreationExpressionName() {
    return raw;
  }

  @Override
  public final int hashCode() {
    return HashCode.of(raw, arguments);
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
  public final String toString() {
    return raw + arguments.join(", ", "<", ">");
  }

}