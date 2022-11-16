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

  public sealed interface AnnotationElementValue extends JavaModel.Markable {}

  public sealed interface AnnotationInvocation
      extends JavaModel.ClassDeclarationElement, JavaModel.EnumDeclarationElement,
      JavaModel.MethodDeclarationElement {}

  public sealed interface ArrayAccessExpression
      extends JavaModel.Expression, JavaModel.LeftHandSide {}

  public sealed interface ArrayDimension
      extends JavaModel.ArrayTypeElement {}

  public sealed interface ArrayTypeElement extends JavaModel.Markable {}

  public sealed interface ArrayTypeInvocation
      extends
      JavaModel.FieldDeclarationElement, JavaModel.FormalParameterType,
      JavaModel.MethodDeclarationElement {}

  public sealed interface AssignmentExpression
      extends JavaModel.Expression, JavaModel.ExpressionStatement {}

  public sealed interface ClassDeclaration {}

  public sealed interface ClassDeclarationElement extends JavaModel.Markable {}

  public sealed interface ClassNameInvocation
      extends
      JavaModel.FieldDeclarationElement, JavaModel.FormalParameterType,
      JavaModel.MethodDeclarationElement, JavaModel.MethodInvocationSubject {}

  public sealed interface ConstructorDeclaration
      extends JavaModel.ClassDeclarationElement, JavaModel.EnumDeclarationElement {}

  public sealed interface ConstructorDeclarationElement extends JavaModel.Markable {}

  public sealed interface EnumConstant
      extends JavaModel.EnumDeclarationElement {}

  public sealed interface EnumConstantElement extends JavaModel.Markable {}

  public sealed interface EnumDeclaration {}

  public sealed interface EnumDeclarationElement extends JavaModel.Markable {}

  public sealed interface Expression
      extends
      JavaModel.EnumConstantElement,
      JavaModel.FieldDeclarationElement,
      JavaModel.MethodInvocationElement {}

  public sealed interface ExpressionName
      extends JavaModel.Expression, JavaModel.LeftHandSide {}

  public sealed interface ExpressionStatement
      extends JavaModel.Statement {}

  public sealed interface ExtendsRef
      extends JavaModel.ClassDeclarationElement {}

  public sealed interface FieldAccessExpression
      extends JavaModel.LeftHandSide, JavaModel.PrimaryExpression {}

  public sealed interface FieldDeclaration {}

  public sealed interface FieldDeclarationElement extends JavaModel.Markable {}

  public sealed interface FinalModifier
      extends JavaModel.ClassDeclarationElement, JavaModel.FieldDeclarationElement,
      JavaModel.MethodDeclarationElement {}

  public sealed interface FormalParameter
      extends JavaModel.ConstructorDeclarationElement, JavaModel.MethodDeclarationElement {}

  public sealed interface FormalParameterType
      extends JavaModel.Markable {}

  public sealed interface IdentifierRef
      extends
      JavaModel.ClassDeclarationElement,
      JavaModel.EnumDeclarationElement, JavaModel.EnumConstantElement,
      JavaModel.FieldDeclarationElement,
      JavaModel.MethodDeclarationElement {}

  public sealed interface Implements
      extends JavaModel.ClassDeclarationElement, JavaModel.EnumDeclarationElement {}

  public sealed interface IncludeRef
      extends JavaModel.MethodDeclarationElement, JavaModel.MethodInvocationElement {}

  public sealed interface IntPrimitiveType
      extends JavaModel.FormalParameterType, JavaModel.MethodDeclarationElement {}

  public sealed interface LeftHandSide extends JavaModel.Markable {}

  public sealed interface LocalVariableDeclarationRef
      extends JavaModel.Statement {}

  public sealed interface Markable {
    void mark(MarkerApi api);
  }

  public interface MarkerApi {

    void markLambda();

    void markReference();

  }

  public sealed interface MethodDeclaration
      extends JavaModel.ClassDeclarationElement {}

  public sealed interface MethodDeclarationElement extends JavaModel.Markable {}

  public sealed interface MethodInvocation
      extends JavaModel.Expression, JavaModel.ExpressionStatement {}

  public sealed interface MethodInvocationElement extends JavaModel.Markable {}

  public sealed interface MethodInvocationSubject extends JavaModel.Markable {}

  public sealed interface NewLineRef
      extends JavaModel.MethodInvocationElement {}

  public sealed interface PrimaryExpression extends JavaModel.Expression {}

  public sealed interface PrivateModifier extends JavaModel.AccessModifier {}

  public sealed interface ProtectedModifier extends JavaModel.AccessModifier {}

  public sealed interface PublicModifier extends JavaModel.AccessModifier {}

  public sealed interface ReturnStatement
      extends JavaModel.Statement {}

  public sealed interface Statement
      extends JavaModel.ConstructorDeclarationElement, JavaModel.MethodDeclarationElement {}

  public sealed interface StaticModifier
      extends JavaModel.ClassDeclarationElement, JavaModel.EnumDeclarationElement,
      JavaModel.FieldDeclarationElement,
      JavaModel.MethodDeclarationElement {}

  public sealed interface StringLiteral
      extends JavaModel.AnnotationElementValue, JavaModel.Expression {}

  public sealed interface ThisKeyword
      extends JavaModel.PrimaryExpression {}

  public sealed interface VoidInvocation extends JavaModel.MethodDeclarationElement {}

  private sealed interface AccessModifier
      extends
      JavaModel.ClassDeclarationElement,
      JavaModel.ConstructorDeclarationElement,
      JavaModel.EnumDeclarationElement,
      JavaModel.FieldDeclarationElement,
      JavaModel.MethodDeclarationElement {}

  private static final class Include implements JavaModel.IncludeRef {
    private Include() {}

    @Override
    public final void mark(MarkerApi api) {
      api.markLambda();
    }
  }

  private static final class Ref
      implements
      JavaModel.AnnotationInvocation,
      JavaModel.ArrayAccessExpression,
      JavaModel.ArrayDimension,
      JavaModel.ArrayTypeInvocation,
      JavaModel.AssignmentExpression,
      JavaModel.ClassDeclaration,
      JavaModel.ClassNameInvocation,
      JavaModel.ConstructorDeclaration,
      JavaModel.EnumConstant,
      JavaModel.EnumDeclaration,
      JavaModel.ExpressionName,
      JavaModel.ExtendsRef,
      JavaModel.FieldAccessExpression,
      JavaModel.FieldDeclaration,
      JavaModel.FinalModifier,
      JavaModel.FormalParameter,
      JavaModel.IdentifierRef,
      JavaModel.Implements,
      JavaModel.IntPrimitiveType,
      JavaModel.LocalVariableDeclarationRef,
      JavaModel.MethodInvocation,
      JavaModel.MethodDeclaration,
      JavaModel.NewLineRef,
      JavaModel.PrivateModifier,
      JavaModel.ProtectedModifier,
      JavaModel.PublicModifier,
      JavaModel.ReturnStatement,
      JavaModel.StaticModifier,
      JavaModel.StringLiteral,
      JavaModel.ThisKeyword,
      JavaModel.VoidInvocation {
    private Ref() {}

    @Override
    public final void mark(MarkerApi api) {
      api.markReference();
    }
  }

  public static final JavaModel.Ref REF = new Ref();

  public static final JavaModel.Include INCLUDE = new Include();

  private JavaModel() {}

}