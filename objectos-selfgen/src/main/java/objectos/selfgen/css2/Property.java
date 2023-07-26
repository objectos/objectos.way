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

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import objectos.code.ClassTypeName;
import objectos.selfgen.util.JavaNames;
import objectos.util.GrowableList;

final class Property {

  static final Comparator<? super Property> ORDER_BY_CONSTANT_NAME
      = (self, that) -> self.constantName.compareTo(that.constantName);

  static final Comparator<? super Property> ORDER_BY_METHOD_NAME
      = (self, that) -> self.methodName.compareTo(that.methodName);

  final String propertyName;

  final String methodName;

  final String constantName;

  ClassTypeName declarationClassName = ThisTemplate.STYLE_DECLARATION;

  ClassTypeName hashClassName;

  private final List<Signature> signatures = new GrowableList<>();

  private Property(String propertyName, String methodName, String constantName) {
    this.propertyName = propertyName;
    this.methodName = methodName;
    this.constantName = constantName;
  }

  public static Property of(String name) {
    String propertyName;
    propertyName = Objects.requireNonNull(name, "name == null");

    String methodName;
    methodName = JavaNames.toValidMethodName(propertyName);

    String constantName;
    constantName = propertyName.replace('-', '_').toUpperCase(Locale.US);

    return new Property(propertyName, methodName, constantName);
  }

  public final Property asHashProperty() {
    String simpleName;
    simpleName = JavaNames.toValidClassName(propertyName);

    declarationClassName = ClassTypeName.of(
      ThisTemplate.CSS_TMPL, simpleName + "Declaration"
    );

    hashClassName = ClassTypeName.of(
      ThisTemplate.CSS_TMPL, simpleName + "HashDeclaration"
    );

    return this;
  }

  final void addSignature(Signature signature) {
    signatures.add(signature);
  }

  final boolean isHash() {
    return hashClassName != null;
  }

  final Iterable<Signature> signatures() {
    return signatures;
  }

  final void value(ParameterType parameterType) {
    signatures.add(
      new Signature1(parameterType.typeName(), "value")
    );
  }

}