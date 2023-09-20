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
package objectos.selfgen.css;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import objectos.code.ClassName;
import objectos.selfgen.util.JavaNames;

public final class Function implements Value {

  static final Comparator<? super Function> ORDER_BY_CONSTANT_NAME
      = (self, that) -> self.constantName.compareTo(that.constantName);

  static final Comparator<? super Function> ORDER_BY_METHOD_NAME
      = (self, that) -> self.methodName.compareTo(that.methodName);

  final ClassName className;

  final String functionName;

  final String methodName;

  final String constantName;

  public final Set<ClassName> interfaces = new HashSet<>();

  private final List<Signature> signatures = new ArrayList<>();

  private Function(ClassName className,
                   String functionName,
                   String methodName,
                   String constantName) {
    this.className = className;
    this.functionName = functionName;
    this.methodName = methodName;
    this.constantName = constantName;
  }

  public static Function of(String name) {
    String functionName;
    functionName = Objects.requireNonNull(name, "name == null");

    String simpleName;
    simpleName = JavaNames.toValidClassName(functionName + "Function");

    ClassName className;
    className = ClassName.of(ThisTemplate.API, simpleName);

    String methodName;
    methodName = JavaNames.toValidMethodName(functionName);

    String constantName;
    constantName = functionName.replace('-', '_').toUpperCase(Locale.US);

    return new Function(className, functionName, methodName, constantName);
  }

  public final void addSignature(Signature signature) {
    signatures.add(signature);
  }

  @Override
  public final void addValueType(ValueType valueType) {
    interfaces.add(valueType.className);
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