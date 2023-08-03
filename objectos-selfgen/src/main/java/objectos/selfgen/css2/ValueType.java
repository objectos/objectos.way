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
package objectos.selfgen.css2;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import objectos.code.ClassTypeName;
import objectos.util.GrowableSet;
import objectos.util.UnmodifiableSet;

public final class ValueType implements ParameterType, Value {

  static final Comparator<? super ValueType> ORDER_BY_SIMPLE_NAME
      = (self, that) -> self.className.simpleName().compareTo(that.className.simpleName());

  public final ClassTypeName className;

  private GrowableSet<ClassTypeName> subTypes;

  private GrowableSet<ClassTypeName> superTypes;

  ValueType(ClassTypeName className) {
    this.className = className;
  }

  public static ValueType of(String simpleName) {
    Objects.requireNonNull(simpleName, "simpleName == null");

    ClassTypeName className;
    className = ClassTypeName.of(ThisTemplate.API, simpleName);

    return new ValueType(className);
  }

  public final void addPermitted(ClassTypeName className) {
    if (subTypes == null) {
      subTypes = new GrowableSet<>();
    }

    subTypes.add(className);
  }

  @Override
  public final void addValueType(ValueType valueType) {
    if (superTypes == null) {
      superTypes = new GrowableSet<>();
    }

    superTypes.add(valueType.className);

    // valueType.addPermitted(className);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof ValueType that && className.equals(that.className);
  }

  @Override
  public final int hashCode() {
    return className.hashCode();
  }

  public final boolean permitsStandardName() {
    return permitted().contains(ThisTemplate.STANDARD_NAME);
  }

  @Override
  public final ClassTypeName typeName() {
    return className;
  }

  final Collection<ClassTypeName> permitted() {
    return subTypes != null ? subTypes : UnmodifiableSet.of();
  }

  final Collection<ClassTypeName> superTypes() {
    return superTypes != null ? superTypes : UnmodifiableSet.of();
  }

}