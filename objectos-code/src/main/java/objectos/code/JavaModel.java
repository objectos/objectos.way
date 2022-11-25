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

/**
 * Holds the hierarchy of interfaces representing elements and types of the
 * Java language.
 *
 * While this class and its members must be visible to subclasses of
 * {@link JavaTemplate}, they are meant for internal use only.
 */
public final class JavaModel {

  public sealed interface AnnotationElementValue extends Markable {}

  public sealed interface AnnotationInvocation
      extends
      ClassDeclarationElement,
      EnumDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface AnyType extends Markable {}

  public sealed interface ArrayAccessExpression extends Expression, LeftHandSide {}

  public sealed interface ArrayDimension extends ArrayTypeElement {}

  public sealed interface ArrayInitializer
      extends ArrayInitializerElement, FieldDeclarationElement {}

  public sealed interface ArrayInitializerElement extends Markable {}

  public sealed interface ArrayType
      extends
      AnyType,
      ReferenceType,

      FieldDeclarationElement, FormalParameterType,
      MethodDeclarationElement {}

  public sealed interface ArrayTypeComponent extends Markable {}

  public sealed interface ArrayTypeElement extends Markable {}

  public sealed interface AssignmentExpression
      extends Expression, ExpressionStatement {}

  public sealed interface ChainedMethodInvocation extends MethodInvocation {}

  public sealed interface ChainedMethodInvocationElement extends Markable {}

  public sealed interface ChainedMethodInvocationHead extends Markable {}

  public sealed interface ClassDeclaration
      extends
      ClassDeclarationElement,
      EnumDeclarationElement {}

  public sealed interface ClassDeclarationElement extends Markable {}

  public sealed interface ClassInstanceCreationExpression
      extends
      ExpressionStatement,
      PrimaryExpression {}

  public sealed interface ClassOrInterfaceType
      extends
      AnyType,
      ReferenceType,

      FieldDeclarationElement, FormalParameterType,
      MethodDeclarationElement {}

  public sealed interface ConstructorDeclaration
      extends ClassDeclarationElement, EnumDeclarationElement {}

  public sealed interface ConstructorDeclarationElement extends Markable {}

  public sealed interface EnumConstant
      extends EnumDeclarationElement {}

  public sealed interface EnumConstantElement extends Markable {}

  public sealed interface EnumDeclaration {}

  public sealed interface EnumDeclarationElement extends Markable {}

  public sealed interface ExplicitConstructorInvocation
      extends
      ConstructorDeclarationElement {}

  public sealed interface Expression
      extends
      ArrayInitializerElement,
      EnumConstantElement,
      FieldDeclarationElement,
      MethodInvocationElement {}

  public sealed interface ExpressionName extends Expression, LeftHandSide {}

  public sealed interface ExpressionStatement
      extends Statement {}

  public sealed interface ExtendsMany extends InterfaceDeclarationElement {}

  public sealed interface ExtendsSingle
      extends ClassDeclarationElement, InterfaceDeclarationElement {}

  public sealed interface FieldAccessExpression
      extends LeftHandSide, PrimaryExpression {}

  public sealed interface FieldDeclaration
      extends ClassDeclarationElement, EnumDeclarationElement {}

  public sealed interface FieldDeclarationElement extends Markable {}

  public sealed interface FinalModifier
      extends
      ClassDeclarationElement,
      FieldDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface FormalParameter
      extends
      ConstructorDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface FormalParameterType
      extends Markable {}

  public sealed interface IdentifierRef
      extends
      ClassDeclarationElement,
      EnumDeclarationElement, EnumConstantElement,
      FieldDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface Implements
      extends ClassDeclarationElement, EnumDeclarationElement {}

  public sealed interface IncludeRef
      extends
      ClassDeclarationElement,
      EnumDeclarationElement,
      MethodDeclarationElement, MethodInvocationElement {}

  public sealed interface IntegerLiteral extends Literal {}

  public sealed interface InterfaceDeclaration {}

  public sealed interface InterfaceDeclarationElement extends Markable {}

  public sealed interface LeftHandSide extends Markable {}

  public sealed interface Literal extends PrimaryExpression {}

  public sealed interface LocalVariableDeclarationRef
      extends Statement {}

  public sealed interface Markable {
    void mark(MarkerApi api);
  }

  public interface MarkerApi {

    void markLambda();

    void markReference();

  }

  public sealed interface MethodDeclaration
      extends ClassDeclarationElement, EnumDeclarationElement {}

  public sealed interface MethodDeclarationElement extends Markable {}

  public sealed interface MethodInvocation extends Expression, ExpressionStatement {}

  public sealed interface MethodInvocationElement extends Markable {}

  public sealed interface NewLineRef
      extends ChainedMethodInvocationElement, MethodInvocationElement {}

  public sealed interface ParameterizedTypeInvocation
      extends
      AnyType,
      FieldDeclarationElement, FormalParameterType,
      MethodDeclarationElement {}

  public sealed interface PrimaryExpression
      extends
      Expression {}

  public sealed interface PrimitiveType
      extends
      AnyType,
      ArrayTypeComponent,

      FieldDeclarationElement, FormalParameterType,
      MethodDeclarationElement {}

  public sealed interface PrivateModifier extends AccessModifier {}

  public sealed interface ProtectedModifier extends AccessModifier {}

  public sealed interface PublicModifier extends AccessModifier {}

  public sealed interface QualifiedMethodInvocation
      extends ChainedMethodInvocationHead, MethodInvocation {}

  public sealed interface ReferenceType
      extends
      ArrayTypeComponent {}

  public sealed interface ReturnStatement
      extends Statement {}

  public sealed interface Statement
      extends ConstructorDeclarationElement, MethodDeclarationElement {}

  public sealed interface StaticModifier
      extends
      ClassDeclarationElement,
      EnumDeclarationElement,
      FieldDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface StringLiteral
      extends AnnotationElementValue, Literal {}

  public sealed interface ThisKeyword
      extends PrimaryExpression {}

  public sealed interface UnqualifiedMethodInvocation
      extends ChainedMethodInvocationHead, ChainedMethodInvocationElement, MethodInvocation {}

  public sealed interface VoidInvocation extends MethodDeclarationElement {}

  private sealed interface AccessModifier
      extends
      ClassDeclarationElement,
      ConstructorDeclarationElement,
      EnumDeclarationElement,
      FieldDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement {}

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
      ArrayInitializer,
      ArrayType,
      AssignmentExpression,
      ChainedMethodInvocation,
      ClassDeclaration,
      ClassOrInterfaceType,
      ClassInstanceCreationExpression,
      ConstructorDeclaration,
      EnumConstant,
      EnumDeclaration,
      ExplicitConstructorInvocation,
      ExpressionName,
      ExtendsMany,
      ExtendsSingle,
      FieldAccessExpression,
      FieldDeclaration,
      FinalModifier,
      FormalParameter,
      IdentifierRef,
      Implements,
      IntegerLiteral,
      InterfaceDeclaration,
      LocalVariableDeclarationRef,
      MethodDeclaration,
      NewLineRef,
      ParameterizedTypeInvocation,
      PrimitiveType,
      PrivateModifier,
      ProtectedModifier,
      PublicModifier,
      QualifiedMethodInvocation,
      ReturnStatement,
      StaticModifier,
      StringLiteral,
      ThisKeyword,
      UnqualifiedMethodInvocation,
      VoidInvocation {
    private Ref() {}

    @Override
    public final void mark(MarkerApi api) {
      api.markReference();
    }
  }

  public static final Ref REF = new Ref();

  public static final Include INCLUDE = new Include();

  private JavaModel() {}

}