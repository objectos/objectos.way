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

import br.com.objectos.code.java.element.CodeElement;

public interface Callee extends CodeElement {

  MethodInvocation invoke(String methodName);

  MethodInvocation invoke(String methodName,
      ArgumentsElement a1);

  MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2);

  MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3);

  MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4);

  MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5);

  MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6);

  MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7);

  MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7,
      ArgumentsElement a8);

  MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7,
      ArgumentsElement a8,
      ArgumentsElement a9);

  MethodInvocation invoke(String methodName, ArgumentsElement... args);

  MethodInvocation invoke(String methodName, Iterable<? extends ArgumentsElement> args);

  MethodInvocation invoke(
      TypeWitness witness, String methodName);

  MethodInvocation invoke(
      TypeWitness witness, String methodName,
      ArgumentsElement a1);

  MethodInvocation invoke(
      TypeWitness witness, String methodName,
      ArgumentsElement a1, ArgumentsElement a2);

  MethodInvocation invoke(
      TypeWitness witness, String methodName,
      ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3);

  MethodInvocation invoke(
      TypeWitness witness, String methodName,
      ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4);

  MethodInvocation invoke(
      TypeWitness witness, String methodName,
      Iterable<? extends ArgumentsElement> args);

}