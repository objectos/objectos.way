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

  public sealed interface AnnotationElementValue extends Markable {}

  public sealed interface AnnotationInvocation
      extends ClassDeclarationElement, EnumDeclarationElement, MethodDeclarationElement {}

  public sealed interface ArrayAccessExpression
      extends Expression, LeftHandSide {}

  public sealed interface ArrayDimension
      extends ArrayTypeElement {}

  public sealed interface ArrayTypeElement extends Markable {}

  public sealed interface ArrayTypeInvocation
      extends
      FieldDeclarationElement, FormalParameterType,
      MethodDeclarationElement {}

  public sealed interface AssignmentExpression extends Expression {}

  public sealed interface ClassDeclaration {}

  public sealed interface ClassDeclarationElement extends Markable {}

  public sealed interface ClassNameInvocation
      extends
      FieldDeclarationElement, FormalParameterType,
      MethodDeclarationElement, MethodInvocationSubject {}

  public sealed interface EnumConstant
      extends EnumDeclarationElement {}

  public sealed interface EnumConstantElement extends Markable {}

  public sealed interface EnumDeclaration {}

  public sealed interface EnumDeclarationElement extends Markable {}

  public sealed interface Expression
      extends
      EnumConstantElement,
      FieldDeclarationElement,
      MethodInvocationElement {}

  public sealed interface ExpressionName
      extends Expression, LeftHandSide {}

  public sealed interface ExpressionStatement
      extends Statement {}

  public sealed interface ExtendsRef
      extends ClassDeclarationElement {}

  public sealed interface FieldAccessExpression
      extends LeftHandSide, PrimaryExpression {}

  public sealed interface FieldDeclaration {}

  public sealed interface FieldDeclarationElement extends Markable {}

  public sealed interface FinalModifier
      extends ClassDeclarationElement, FieldDeclarationElement, MethodDeclarationElement {}

  public sealed interface FormalParameter
      extends MethodDeclarationElement {}

  public sealed interface FormalParameterType
      extends Markable {}

  public sealed interface IdentifierRef
      extends
      ClassDeclarationElement,
      EnumDeclarationElement, EnumConstantElement,
      FieldDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface Implements
      extends ClassDeclarationElement, EnumDeclarationElement {}

  public sealed interface IncludeRef
      extends MethodDeclarationElement, MethodInvocationElement {}

  public sealed interface IntPrimitiveType extends MethodDeclarationElement {}

  public sealed interface LeftHandSide extends Markable {}

  public sealed interface LocalVariableDeclarationRef
      extends Statement {}

  public sealed interface Markable {
    void mark(MarkerApi api);
  }

  public sealed interface MethodDeclaration
      extends ClassDeclarationElement {}

  public sealed interface MethodDeclarationElement extends Markable {}

  public sealed interface MethodInvocation
      extends Expression, ExpressionStatement {}

  public sealed interface MethodInvocationElement extends Markable {}

  public sealed interface MethodInvocationSubject extends Markable {}

  public sealed interface NewLineRef
      extends MethodInvocationElement {}

  public sealed interface PrimaryExpression extends Expression {}

  public sealed interface PrivateModifier
      extends ClassDeclarationElement, EnumDeclarationElement, FieldDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface PublicModifier
      extends ClassDeclarationElement, EnumDeclarationElement, FieldDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface ReturnStatement
      extends Statement {}

  public sealed interface Statement
      extends MethodDeclarationElement {}

  public sealed interface StaticModifier
      extends ClassDeclarationElement, EnumDeclarationElement, FieldDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface StringLiteral
      extends AnnotationElementValue, Expression {}

  public sealed interface ThisKeyword
      extends PrimaryExpression {}

  public sealed interface VoidInvocation extends MethodDeclarationElement {}

  private static final class Include implements IncludeRef {
    private Include() {}

    @Override
    public final void mark(MarkerApi api) {
      api.markLambda();
    }
  }

  private static final class Ref
      implements
      AnnotationInvocation,
      ArrayAccessExpression,
      ArrayDimension,
      ArrayTypeInvocation,
      AssignmentExpression,
      ClassDeclaration,
      ClassNameInvocation,
      EnumConstant,
      EnumDeclaration,
      ExpressionName,
      ExtendsRef,
      FieldAccessExpression,
      FieldDeclaration,
      FinalModifier,
      FormalParameter,
      IdentifierRef,
      Implements,
      IntPrimitiveType,
      LocalVariableDeclarationRef,
      MethodInvocation,
      MethodDeclaration,
      NewLineRef,
      PrivateModifier,
      PublicModifier,
      ReturnStatement,
      StaticModifier,
      StringLiteral,
      ThisKeyword,
      VoidInvocation {
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