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
package br.com.objectos.code.java.expression;

import br.com.objectos.code.java.declaration.ClassCodeElement;
import br.com.objectos.code.java.declaration.EnumCodeElement;
import br.com.objectos.code.java.declaration.EnumConstantCodeElement;
import br.com.objectos.code.java.declaration.FieldCodeElement;
import br.com.objectos.code.java.declaration.InterfaceCodeElement;
import br.com.objectos.code.java.declaration.MethodCodeElement;

public interface Identifier
    extends
    Argument,
    Callee,
    ClassCodeElement,
    EnumCodeElement,
    EnumConstantCodeElement,
    Expression,
    ExpressionName,
    FieldCodeElement,
    InterfaceCodeElement,
    LambdaParameter,
    LeftHandSide,
    MethodCodeElement,
    ThrowableExpression {

  String name();

  @Override
  Identifier nl();

}