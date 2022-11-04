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

import java.lang.annotation.Annotation;
import objectos.code.ClassName;
import objectos.code.tmpl.InternalApi.AnnotationElementValue;
import objectos.code.tmpl.InternalApi.AtRef;
import objectos.code.tmpl.InternalApi.ClassDeclarationElement;
import objectos.code.tmpl.InternalApi.ClassDeclarationRef;
import objectos.code.tmpl.InternalApi.Expression;
import objectos.code.tmpl.InternalApi.ExpressionNameRef;
import objectos.code.tmpl.InternalApi.ExtendsRef;
import objectos.code.tmpl.InternalApi.FinalRef;
import objectos.code.tmpl.InternalApi.IdentifierRef;
import objectos.code.tmpl.InternalApi.IncludeRef;
import objectos.code.tmpl.InternalApi.LocalVariableDeclarationRef;
import objectos.code.tmpl.InternalApi.MethodDeclaration;
import objectos.code.tmpl.InternalApi.MethodDeclarationElement;
import objectos.code.tmpl.InternalApi.MethodInvocation;
import objectos.code.tmpl.InternalApi.MethodInvocationElement;
import objectos.code.tmpl.InternalApi.NewLineRef;
import objectos.code.tmpl.InternalApi.StringLiteral;
import objectos.code.tmpl.InternalApi.VoidRef;

public interface TemplateApi extends MarkerApi {

  ClassDeclarationRef _class(ClassDeclarationElement[] elements);

  ExtendsRef _extends(ClassName superclass);

  FinalRef _final();

  void _package(String packageName);

  VoidRef _void();

  AtRef annotation(Class<? extends Annotation> annotationType);

  AtRef annotation(ClassName annotationType, AnnotationElementValue value);

  void autoImports();

  IdentifierRef id(String name);

  IncludeRef include(IncludeTarget target);

  MethodInvocation invoke(String methodName, MethodInvocationElement[] elements);

  MethodDeclaration method(MethodDeclarationElement[] elements);

  ExpressionNameRef n(ClassName name, String identifier);

  ExpressionNameRef n(String value);

  NewLineRef nl();

  StringLiteral s(String value);

  LocalVariableDeclarationRef var(String name, Expression expression);

}