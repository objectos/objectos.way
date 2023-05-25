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
package objectos.selfgen.css.spec;

import java.util.Locale;
import objectos.lang.ToString;
import objectos.selfgen.util.JavaNames;

public final class Property
    implements Comparable<Property>, FunctionOrProperty, ToString.Formattable {

  final PropertyKind kind;

  private String methodName;

  final String name;

  MethodSignature[] signatures;

  Property(PropertyKind kind, String name) {
    this.kind = kind;
    this.name = name;
  }

  Property(PropertyKind kind, String name, String methodName) {
    this(kind, name);

    this.methodName = methodName;
  }

  @Override
  public final String addMethodName() {
    return "addDeclaration";
  }

  @Override
  public final int compareTo(Property o) {
    return name.compareTo(o.name);
  }

  @Override
  public final String enumName() {
    var uppercase = name.replace('-', '_').toUpperCase(Locale.US);

    return JavaNames.toIdentifier(uppercase);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Property)) {
      return false;
    }
    Property that = (Property) obj;
    return name.equals(that.name);
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
      toString, level, this,
      "", name
    );
  }

  public final String getName() {
    return name;
  }

  @Override
  public final int hashCode() {
    return name.hashCode();
  }

  @Override
  public final String methodName() {
    if (methodName == null) {
      methodName = JavaNames.toValidMethodName(name);
    }

    return methodName;
  }

  @Override
  public final String multiDeclarationSimpleName() {
    return JavaNames.toValidClassName(name + "MultiDeclaration");
  }

  @Override
  public final String singleDeclarationSimpleName() {
    var singleSuffix = kind.getSingleSuffix();

    return JavaNames.toValidClassName(name + singleSuffix);
  }

  @Override
  public final String toString() {
    return ToString.of(this);
  }

}
