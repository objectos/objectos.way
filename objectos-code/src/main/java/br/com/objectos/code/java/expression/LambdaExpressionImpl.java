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

import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import objectos.util.UnmodifiableList;

final class LambdaExpressionImpl
    extends AbstractImmutableCodeElement
    implements LambdaExpression {

  private LambdaExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  static LambdaExpression lambda0(
      LambdaBody body) {
    return lambda1(body, Arguments.empty());
  }

  static LambdaExpression lambda0(
      LambdaBody body, Identifier single) {
    return new LambdaExpressionImpl(
        single, space(), arrow(), space(), body
    );
  }

  static LambdaExpression lambda0(
      LambdaBody body, Iterable<? extends LambdaParameter> params) {
    return lambda1(body, Arguments.of(params));
  }

  static LambdaExpression lambda0(
      LambdaBody body, LambdaParameter... params) {
    return lambda0(body, UnmodifiableList.copyOf(params));
  }

  static LambdaExpression lambda1(
      LambdaBody body, Arguments args) {
    return new LambdaExpressionImpl(
        args, space(), arrow(), space(), body
    );
  }

}
