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
import java.util.Set;
import java.util.TreeSet;
import objectos.selfgen.util.JavaNames;

final class FunctionName implements FunctionOrProperty, Value {

  public final Set<String> interfaceSet = new TreeSet<>();

  private final String name;

  public MethodSignature[] signatures;

  FunctionName(String name) {
    this.name = name;
  }

  public static FunctionName of(String name) {
    return new FunctionName(name);
  }

  @Override
  public final void acceptValueType(ValueType type) {
    interfaceSet.add(type.simpleName);
  }

  @Override
  public final String addMethodName() {
    return "addFunction";
  }

  @Override
  public final String enumName() {
    var uppercase = name.replace('-', '_').toUpperCase(Locale.US);

    return JavaNames.toIdentifier(uppercase);
  }

  @Override
  public final String methodName() {
    return JavaNames.toValidMethodName(name);
  }

  @Override
  public final String multiDeclarationSimpleName() {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String getName() {
    return name;
  }

  @Override
  public final String singleDeclarationSimpleName() {
    return JavaNames.toValidClassName(name + "Function");
  }

}
