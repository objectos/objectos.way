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
package objectos.code.tmpl;

import objectos.code.ClassName;
import objectos.code.TypeName;
import objectox.code.Pass0;

public sealed interface TemplateApi extends MarkerApi permits Pass0 {

  void _extends(ClassName superclass);

  void _final();

  void annotation();

  void autoImports();

  void classDeclaration();

  void className(ClassName name);

  void expressionName();

  void identifier(String name);

  void lambdaEnd();

  void lambdaStart();

  void localVariable();

  void markStart();

  void methodDeclaration();

  void methodInvocation();

  void newLine();

  void packageDeclaration();

  void packageName(String packageName);

  void stringLiteral(String value);

  void typeName(TypeName typeName);

}