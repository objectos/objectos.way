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
import objectos.code.tmpl.InternalApi;
import objectos.code.tmpl.InternalApi.AtRef;
import objectos.code.tmpl.InternalApi.ClassElement;
import objectos.code.tmpl.InternalApi.ClassRef;
import objectos.code.tmpl.InternalApi.ExpressionElement;
import objectos.code.tmpl.InternalApi.ExtendsRef;
import objectos.code.tmpl.InternalApi.FinalRef;
import objectos.code.tmpl.InternalApi.IdentifierRef;
import objectos.code.tmpl.InternalApi.LiteralRef;
import objectos.code.tmpl.InternalApi.LocalVariableDeclarationRef;
import objectos.code.tmpl.InternalApi.MethodElement;
import objectos.code.tmpl.InternalApi.MethodInvocationElement;
import objectos.code.tmpl.InternalApi.MethodInvocationRef;
import objectos.code.tmpl.InternalApi.MethodRef;
import objectos.code.tmpl.InternalApi.NameRef;
import objectos.code.tmpl.InternalApi.NewLineRef;
import objectos.lang.Check;

public abstract class JavaTemplate {

  public interface Renderer {

    void annotationEnd();

    void annotationStart();

    void blockAfterLastItem();

    void blockBeforeFirstItem();

    void blockBeforeNextItem();

    void blockEnd();

    void blockStart();

    void classEnd();

    void classStart();

    void comma();

    void compilationUnitEnd();

    void compilationUnitStart();

    void identifier(String name);

    void keyword(String keyword);

    void methodEnd();

    void methodStart();

    void modifier(String name);

    void name(String name);

    void newLine();

    void packageEnd();

    void packageStart();

    void parameterListEnd();

    void parameterListStart();

    void semicolon();

    void separator(char c);

    void statementEnd();

    void statementStart();

    void stringLiteral(String s);

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

  protected final ClassRef _class(ClassElement... elements) {
    api.classDeclaration(elements.length); // implicit elements null check

    return InternalApi.REF;
  }

  protected final ExtendsRef _extends(ClassName superclass) {
    api._extends(superclass);

    return InternalApi.REF;
  }

  protected final FinalRef _final() {
    api._final();

    return InternalApi.REF;
  }

  protected final void _package(String packageName) {
    api.packageDeclaration(packageName);
  }

  protected final AtRef annotation(Class<? extends Annotation> annotationType) {
    var name = ClassName.of(annotationType); // implicit null-check

    api.className(name);

    api.annotation(1);

    return InternalApi.REF;
  }

  protected final void autoImports() {
    api.autoImports();
  }

  protected abstract void definition();

  protected final IdentifierRef id(String name) {
    api.identifier(name);

    return InternalApi.REF;
  }

  protected final MethodInvocationRef invoke(
      NameRef methodName, MethodInvocationElement... elements) {
    api.methodInvocation(elements.length + 1);

    return InternalApi.REF;
  }

  protected final MethodRef method(MethodElement... elements) {
    api.methodDeclaration(elements.length); // implicit elements null check

    return InternalApi.REF;
  }

  protected final NameRef name(String value) {
    api.name(value);

    return InternalApi.REF;
  }

  protected final NewLineRef nl() {
    api.newLine();

    return InternalApi.REF;
  }

  protected final LiteralRef s(String value) {
    api.stringLiteral(value);

    return InternalApi.REF;
  }

  protected final LocalVariableDeclarationRef var(IdentifierRef id, ExpressionElement expression) {
    api.localVariable(2);

    return InternalApi.REF;
  }

}