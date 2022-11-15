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
import objectos.code.tmpl.InternalApi.AnnotationInvocation;
import objectos.code.tmpl.InternalApi.ArrayAccessExpression;
import objectos.code.tmpl.InternalApi.ArrayDimension;
import objectos.code.tmpl.InternalApi.ArrayTypeElement;
import objectos.code.tmpl.InternalApi.ArrayTypeInvocation;
import objectos.code.tmpl.InternalApi.AssignmentExpression;
import objectos.code.tmpl.InternalApi.ClassDeclaration;
import objectos.code.tmpl.InternalApi.ClassDeclarationElement;
import objectos.code.tmpl.InternalApi.ClassNameInvocation;
import objectos.code.tmpl.InternalApi.EnumConstant;
import objectos.code.tmpl.InternalApi.EnumConstantElement;
import objectos.code.tmpl.InternalApi.EnumDeclaration;
import objectos.code.tmpl.InternalApi.EnumDeclarationElement;
import objectos.code.tmpl.InternalApi.Expression;
import objectos.code.tmpl.InternalApi.ExpressionName;
import objectos.code.tmpl.InternalApi.ExtendsRef;
import objectos.code.tmpl.InternalApi.FieldDeclaration;
import objectos.code.tmpl.InternalApi.FieldDeclarationElement;
import objectos.code.tmpl.InternalApi.FinalModifier;
import objectos.code.tmpl.InternalApi.FormalParameter;
import objectos.code.tmpl.InternalApi.FormalParameterType;
import objectos.code.tmpl.InternalApi.IdentifierRef;
import objectos.code.tmpl.InternalApi.Implements;
import objectos.code.tmpl.InternalApi.IncludeRef;
import objectos.code.tmpl.InternalApi.IntPrimitiveType;
import objectos.code.tmpl.InternalApi.LeftHandSide;
import objectos.code.tmpl.InternalApi.LocalVariableDeclarationRef;
import objectos.code.tmpl.InternalApi.MethodDeclaration;
import objectos.code.tmpl.InternalApi.MethodDeclarationElement;
import objectos.code.tmpl.InternalApi.MethodInvocation;
import objectos.code.tmpl.InternalApi.MethodInvocationElement;
import objectos.code.tmpl.InternalApi.MethodInvocationSubject;
import objectos.code.tmpl.InternalApi.NewLineRef;
import objectos.code.tmpl.InternalApi.PrivateModifier;
import objectos.code.tmpl.InternalApi.PublicModifier;
import objectos.code.tmpl.InternalApi.ReturnStatement;
import objectos.code.tmpl.InternalApi.StaticModifier;
import objectos.code.tmpl.InternalApi.StringLiteral;
import objectos.code.tmpl.InternalApi.VoidInvocation;
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

  protected final ClassDeclaration _class(ClassDeclarationElement... elements) {
    return api._class(elements);
  }

  protected final EnumDeclaration _enum(EnumDeclarationElement... elements) {
    return api._enum(elements);
  }

  protected final ExtendsRef _extends(ClassName superclass) {
    return api._extends(superclass);
  }

  protected final FinalModifier _final() {
    return api._final();
  }

  protected final Implements _implements(ClassName... interfaces) {
    return api._implements(interfaces);
  }

  protected final IntPrimitiveType _int() {
    return api._int();
  }

  protected final void _package(String packageName) {
    api._package(packageName);
  }

  protected final PrivateModifier _private() {
    return api._private();
  }

  protected final PublicModifier _public() {
    return api._public();
  }

  protected final ReturnStatement _return(Expression expression) {
    return api._return(expression);
  }

  protected final StaticModifier _static() {
    return api._static();
  }

  protected final VoidInvocation _void() {
    return api._void();
  }

  protected final ArrayAccessExpression a(ExpressionName reference, Expression... expressions) {
    return api.a(reference, expressions);
  }

  protected final AnnotationInvocation annotation(Class<? extends Annotation> annotationType) {
    return api.annotation(annotationType);
  }

  protected final AnnotationInvocation annotation(ClassName annotationType,
      AnnotationElementValue value) {
    return api.annotation(annotationType, value);
  }

  protected final AssignmentExpression assign(LeftHandSide leftHandSide, Expression expression) {
    return api.assign(leftHandSide, expression);
  }

  protected final void autoImports() {
    api.autoImports();
  }

  protected abstract void definition();

  protected final ArrayDimension dim() {
    return api.dim();
  }

  protected final EnumConstant enumConstant(EnumConstantElement... elements) {
    return api.enumConstant(elements);
  }

  protected final FieldDeclaration field(FieldDeclarationElement... elements) {
    return api.field(elements);
  }

  protected final IdentifierRef id(String name) {
    return api.id(name);
  }

  protected final IncludeRef include(IncludeTarget target) {
    return api.include(target);
  }

  protected final MethodInvocation invoke(
      MethodInvocationSubject subject, String methodName, MethodInvocationElement... elements) {
    return api.invoke(subject, methodName, elements);
  }

  protected final MethodInvocation invoke(
      String methodName, MethodInvocationElement... elements) {
    return api.invoke(methodName, elements);
  }

  protected final MethodDeclaration method(MethodDeclarationElement... elements) {
    return api.method(elements);
  }

  protected final ExpressionName n(ClassName name, String identifier) {
    return api.n(name, identifier);
  }

  protected final ExpressionName n(String value) {
    return api.n(value);
  }

  protected final NewLineRef nl() {
    return api.nl();
  }

  protected final FormalParameter param(FormalParameterType type, IdentifierRef name) {
    return api.param(type, name);
  }

  protected final StringLiteral s(String value) {
    return api.s(value);
  }

  protected final ClassNameInvocation t(Class<?> value) {
    return api.t(value);
  }

  protected final ArrayTypeInvocation t(Class<?> type, ArrayTypeElement... elements) {
    return api.t(type, elements);
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