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
package objectos.code;

import java.lang.annotation.Annotation;
import objectos.code.tmpl.AtRef;
import objectos.code.tmpl.ClassDeclarationElement;
import objectos.code.tmpl.ClassDeclarationRef;
import objectos.code.tmpl.ExpressionElement;
import objectos.code.tmpl.ExpressionNameRef;
import objectos.code.tmpl.ExtendsRef;
import objectos.code.tmpl.FinalRef;
import objectos.code.tmpl.IdentifierRef;
import objectos.code.tmpl.IncludeRef;
import objectos.code.tmpl.InternalApi;
import objectos.code.tmpl.LiteralRef;
import objectos.code.tmpl.LocalVariableDeclarationRef;
import objectos.code.tmpl.MethodDeclarationElement;
import objectos.code.tmpl.MethodInvocationElement;
import objectos.code.tmpl.MethodInvocationRef;
import objectos.code.tmpl.MethodRef;
import objectos.code.tmpl.NewLineRef;
import objectos.code.tmpl.VoidRef;
import objectos.lang.Check;
import objectox.code.Pass0;

public abstract class JavaTemplate {

  public interface Renderer {

    void beforeBlockNextItem();

    void beforeClassFirstMember();

    void beforeCompilationUnitBody();

    void blockEnd();

    void blockStart();

    void comma();

    void compilationUnitEnd();

    void compilationUnitStart();

    void dot();

    void identifier(String name);

    void keyword(String keyword);

    void modifier(String name);

    void name(String name);

    void newLine();

    void parameterListEnd();

    void parameterListStart();

    void semicolon();

    void separator(char c);

    void space();

    void spaceIf(boolean condition);

    void statementEnd();

    void statementStart();

    void stringLiteral(String s);

    void write(char c);

    void write(String s);

  }

  @FunctionalInterface
  protected interface IncludeTarget {
    void def1MainProperty();
  }

  private InternalApi api;

  /**
   * Sole constructor.
   */
  protected JavaTemplate() {}

  public final void acceptJavaGenerator(JavaGenerator generator) {
    Check.state(this.api == null, """
    Another code generation is already is progress.
    """);
    Check.notNull(generator, "generator == null");

    api = generator.pass0;

    try {
      definition();
    } finally {
      this.api = null;
    }
  }

  public final void eval(InternalApi api) {
    Check.state(this.api == null, """
    Another evaluation is already is progress.
    """);

    this.api = Check.notNull(api, "api == null");

    definition();
  }

  protected final ClassDeclarationRef _class(ClassDeclarationElement... elements) {
    api.classDeclaration(elements.length); // implicit elements null check

    return Pass0.REF;
  }

  protected final ExtendsRef _extends(ClassName superclass) {
    api._extends(superclass);

    return Pass0.REF;
  }

  protected final FinalRef _final() {
    api._final();

    return Pass0.REF;
  }

  protected final void _package(String packageName) {
    api.packageName(packageName);

    api.packageDeclaration(1);
  }

  protected final VoidRef _void() {
    api.typeName(TypeName.VOID);

    return Pass0.REF;
  }

  protected final AtRef annotation(Class<? extends Annotation> annotationType) {
    var name = ClassName.of(annotationType); // implicit null-check

    api.className(name);

    api.annotation(1);

    return Pass0.REF;
  }

  protected final void autoImports() {
    api.autoImports();
  }

  protected abstract void definition();

  protected final IdentifierRef id(String name) {
    api.identifier(name);

    return Pass0.REF;
  }

  protected final IncludeRef include(IncludeTarget target) {
    target.def1MainProperty();

    return Pass0.REF;
  }

  protected final MethodInvocationRef invoke(
      String methodName, MethodInvocationElement... elements) {
    api.identifier(methodName);

    api.methodInvocation(elements.length + 1); // implicit elements null check

    return Pass0.REF;
  }

  protected final MethodRef method(MethodDeclarationElement... elements) {
    api.methodDeclaration(elements.length); // implicit elements null check

    return Pass0.REF;
  }

  protected final ExpressionNameRef n(ClassName name, String identifier) {
    api.className(name);

    api.identifier(identifier);

    api.expressionName(2);

    return Pass0.REF;
  }

  protected final ExpressionNameRef n(String value) {
    api.identifier(value);

    api.expressionName(1);

    return Pass0.REF;
  }

  protected final NewLineRef nl() {
    api.newLine();

    return Pass0.REF;
  }

  protected final LiteralRef s(String value) {
    api.stringLiteral(value);

    return Pass0.REF;
  }

  protected final LocalVariableDeclarationRef var(String name, ExpressionElement expression) {
    api.identifier(name);

    api.localVariable(2);

    return Pass0.REF;
  }

}