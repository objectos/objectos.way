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

/**
 * Holds the hierarchy of interfaces representing elements and types of the
 * Java language.
 *
 * While this class and its members must be visible to subclasses, they are
 * meant for internal use only.
 */
public final class InternalApi {

  public sealed interface AnnotationElementValue {
    void mark(MarkerApi api);
  }

  public sealed interface AtRef
      extends ClassDeclarationElement, EnumDeclarationElement, MethodDeclarationElement {}

  public sealed interface ClassDeclaration {}

  public sealed interface ClassDeclarationElement {
    void mark(MarkerApi api);
  }

  public sealed interface EnumConstant
      extends EnumDeclarationElement {}

  public sealed interface EnumConstantElement {
    void mark(MarkerApi api);
  }

  public sealed interface EnumDeclaration {}

  public sealed interface EnumDeclarationElement {
    void mark(MarkerApi api);
  }

  public sealed interface Expression
      extends EnumConstantElement, MethodInvocationElement {}

  public sealed interface ExpressionNameRef
      extends Expression {}

  public sealed interface ExpressionStatement
      extends Statement {}

  public sealed interface ExtendsRef
      extends ClassDeclarationElement {}

  public sealed interface FinalModifier
      extends ClassDeclarationElement, MethodDeclarationElement {}

  public sealed interface IdentifierRef
      extends
      ClassDeclarationElement,
      EnumDeclarationElement, EnumConstantElement,
      MethodDeclarationElement {}

  public sealed interface Implements
      extends ClassDeclarationElement, EnumDeclarationElement {}

  public sealed interface IncludeRef
      extends MethodDeclarationElement, MethodInvocationElement {}

  public sealed interface LocalVariableDeclarationRef
      extends Statement {}

  public sealed interface MethodDeclaration
      extends ClassDeclarationElement {}

  public sealed interface MethodDeclarationElement {
    void mark(MarkerApi api);
  }

  public sealed interface MethodInvocation
      extends Expression, ExpressionStatement {}

  public sealed interface MethodInvocationElement {
    void mark(MarkerApi api);
  }

  public sealed interface NewLineRef
      extends MethodInvocationElement {}

  public sealed interface PublicModifier
      extends ClassDeclarationElement, EnumDeclarationElement, MethodDeclarationElement {}

  public sealed interface Statement
      extends MethodDeclarationElement {}

  public sealed interface StringLiteral
      extends AnnotationElementValue, Expression {}

  public sealed interface VoidRef extends MethodDeclarationElement {}

  private static final class Include implements IncludeRef {
    private Include() {}

    @Override
    public final void mark(MarkerApi api) {
      api.markLambda();
    }
  }

  private static final class Ref
      implements
      AtRef,
      ClassDeclaration,
      EnumConstant,
      EnumDeclaration,
      ExpressionNameRef,
      ExtendsRef,
      FinalModifier,
      IdentifierRef,
      Implements,
      LocalVariableDeclarationRef,
      MethodInvocation,
      MethodDeclaration,
      NewLineRef,
      PublicModifier,
      StringLiteral,
      VoidRef {
    private Ref() {}

    @Override
    public final void mark(MarkerApi api) {
      api.markReference();
    }
  }

  public static final Ref REF = new Ref();

  public static final Include INCLUDE = new Include();

  private InternalApi() {}

}