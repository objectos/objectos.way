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
import objectos.code.tmpl.IncludeTarget;
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
import objectos.code.tmpl.TemplateApi;
import objectos.lang.Check;

public abstract class JavaTemplate {

  public interface Renderer {

    void write(char c);

    void write(String s);

    void writeArgumentListEnd();

    void writeArgumentListStart();

    void writeBeforeBlockNextItem();

    void writeBeforeClassFirstMember();

    void writeBeforeCompilationUnitBody();

    void writeBlockEnd();

    void writeBlockStart();

    void writeComma();

    void writeCompilationUnitEnd();

    void writeCompilationUnitStart();

    void writeNewLine();

    void writeSemicolon();

    void writeSeparator(char c);

    void writeSpace();

    void writeSpaceIf(boolean condition);

    void writeStringLiteral(String s);

  }

  private TemplateApi api;

  /**
   * Sole constructor.
   */
  protected JavaTemplate() {}

  public final void eval(TemplateApi api) {
    Check.state(this.api == null, """
    Another evaluation is already is progress.
    """);

    this.api = Check.notNull(api, "api == null");

    try {
      definition();
    } finally {
      this.api = null;
    }
  }

  @Override
  public String toString() {
    var out = new StringBuilder();

    var sink = JavaSink.ofStringBuilder(out);

    sink.eval(this);

    return out.toString();
  }

  protected final ClassDeclarationRef _class(ClassDeclarationElement... elements) {
    return api._class(elements);
  }

  protected final ExtendsRef _extends(ClassName superclass) {
    return api._extends(superclass);
  }

  protected final FinalRef _final() {
    return api._final();
  }

  protected final void _package(String packageName) {
    api._package(packageName);
  }

  protected final VoidRef _void() {
    return api._void();
  }

  protected final AtRef annotation(Class<? extends Annotation> annotationType) {
    return api.annotation(annotationType);
  }

  protected final AtRef annotation(ClassName annotationType, AnnotationElementValue value) {
    return api.annotation(annotationType, value);
  }

  protected final void autoImports() {
    api.autoImports();
  }

  protected abstract void definition();

  protected final IdentifierRef id(String name) {
    return api.id(name);
  }

  protected final IncludeRef include(IncludeTarget target) {
    return api.include(target);
  }

  protected final MethodInvocation invoke(
      String methodName, MethodInvocationElement... elements) {
    return api.invoke(methodName, elements);
  }

  protected final MethodDeclaration method(MethodDeclarationElement... elements) {
    return api.method(elements);
  }

  protected final ExpressionNameRef n(ClassName name, String identifier) {
    return api.n(name, identifier);
  }

  protected final ExpressionNameRef n(String value) {
    return api.n(value);
  }

  protected final NewLineRef nl() {
    return api.nl();
  }

  protected final StringLiteral s(String value) {
    return api.s(value);
  }

  protected final LocalVariableDeclarationRef var(String name, Expression expression) {
    return api.var(name, expression);
  }

  final void execute(Pass0 pass0) {
    Check.state(this.api == null, """
    Another evaluation is already in progress.
    """);

    this.api = pass0;

    try {
      definition();
    } finally {
      this.api = null;
    }
  }

}