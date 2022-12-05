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

  public sealed interface AbstractModifier
      extends
      ClassDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement {}

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

      FieldDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface ArrayTypeComponent extends Markable {}

  public sealed interface ArrayTypeElement extends Markable {}

  public sealed interface AssignmentExpression
      extends Expression, ExpressionStatement {}

  public sealed interface Block extends Statement {}

  public sealed interface BlockElement extends Markable {}

  public sealed interface BlockStatement extends BlockElement {}

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

  public sealed interface ClassType
      extends
      AnyType,
      ReferenceType,
      TypeParameterBound,

      FieldDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface ConstructorDeclaration
      extends ClassDeclarationElement, EnumDeclarationElement {}

  public sealed interface ConstructorDeclarationElement extends Markable {}

  public sealed interface Ellipsis extends FormalParameterElement {}

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

  public sealed interface FormalParameterElement extends Markable {}

  public sealed interface IdentifierRef
      extends
      ClassDeclarationElement,
      EnumConstantElement,
      EnumDeclarationElement,
      FieldDeclarationElement,
      FormalParameterElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface IfStatement
      extends Statement {}

  public sealed interface Implements
      extends ClassDeclarationElement, EnumDeclarationElement {}

  public sealed interface IncludeRef
      extends
      ClassDeclarationElement,
      EnumDeclarationElement,
      FieldDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement, MethodInvocationElement {}

  public sealed interface IntegerLiteral extends Literal {}

  public sealed interface InterfaceDeclaration {}

  public sealed interface InterfaceDeclarationElement extends Markable {}

  public sealed interface LeftHandSide extends Markable {}

  public sealed interface Literal extends PrimaryExpression {}

  public sealed interface LocalVariableDeclarationRef
      extends BlockStatement {}

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
      extends BlockElement, ChainedMethodInvocationElement, MethodInvocationElement {}

  public sealed interface ParameterizedClassType
      extends
      AnyType,
      ReferenceType {}

  public sealed interface PrimaryExpression
      extends
      Expression {}

  public sealed interface PrimitiveType
      extends
      AnyType,
      ArrayTypeComponent,

      FieldDeclarationElement,
      FormalParameterElement,
      MethodDeclarationElement {}

  public sealed interface PrivateModifier extends AccessModifier {}

  public sealed interface ProtectedModifier extends AccessModifier {}

  public sealed interface PublicModifier extends AccessModifier {}

  public sealed interface QualifiedMethodInvocation
      extends ChainedMethodInvocationHead, MethodInvocation {}

  public sealed interface ReferenceType
      extends
      AnyType,
      ArrayTypeComponent,

      FieldDeclarationElement,
      FormalParameterElement,
      MethodDeclarationElement {}

  public sealed interface ReturnStatement
      extends Statement {}

  public sealed interface Statement
      extends BlockStatement, ConstructorDeclarationElement, MethodDeclarationElement {}

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

  public sealed interface TypeParameter
      extends MethodDeclarationElement {}

  public sealed interface TypeParameterBound extends Markable {}

  public sealed interface TypeVariable extends ReferenceType {}

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
      AbstractModifier,
      AnnotationInvocation,
      ArrayAccessExpression,
      ArrayDimension,
      ArrayInitializer,
      ArrayType,
      AssignmentExpression,
      Block,
      ChainedMethodInvocation,
      ClassDeclaration,
      ClassType,
      ClassInstanceCreationExpression,
      ConstructorDeclaration,
      Ellipsis,
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
      IfStatement,
      Implements,
      IntegerLiteral,
      InterfaceDeclaration,
      LocalVariableDeclarationRef,
      MethodDeclaration,
      NewLineRef,
      ParameterizedClassType,
      PrimitiveType,
      PrivateModifier,
      ProtectedModifier,
      PublicModifier,
      QualifiedMethodInvocation,
      ReturnStatement,
      StaticModifier,
      StringLiteral,
      ThisKeyword,
      TypeParameter,
      TypeVariable,
      UnqualifiedMethodInvocation,
      VoidInvocation {
    private Ref() {}

    @Override
    public final void mark(MarkerApi api) {
      api.markReference();
    }
  }

  @SuppressWarnings("exports")
  public static final Ref REF = new Ref();

  @SuppressWarnings("exports")
  public static final Include INCLUDE = new Include();

  private JavaModel() {}

  static void checkPackageName(String s) {
    checkName(s, true, "an invalid package name");
  }

  static void checkSimpleName(String s) {
    if (s.isEmpty()) {
      throw new IllegalArgumentException("A simple name must not be empty");
    }

    checkName(s, false, "an invalid simple name");
  }

  private static void checkName(String s, boolean allowDots, String message) {
    var hasDot = false;

    enum Parser {
      ID_START,

      ID_PART,

      DOT,

      HIGH_SURROGATE_START,

      HIGH_SURROGATE_PART;
    }

    var state = Parser.ID_START;
    char high = '0';

    for (int i = 0, len = s.length(); i < len; i++) {
      char c = s.charAt(i);

      state = switch (state) {
        case ID_START, DOT -> {
          if (Character.isHighSurrogate(c)) {
            high = c;

            yield Parser.HIGH_SURROGATE_START;
          }

          if (!Character.isJavaIdentifierStart(c)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            Character '%s' is an invalid java identifier start.
            """.formatted(s, message, c));
          }

          yield Parser.ID_PART;
        }

        case ID_PART -> {
          if (c == '.') {
            hasDot = true;

            yield Parser.DOT;
          }

          if (Character.isHighSurrogate(c)) {
            high = c;

            yield Parser.HIGH_SURROGATE_PART;
          }

          if (!Character.isJavaIdentifierPart(c)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            Character '%s' is an invalid java identifier part.
            """.formatted(s, message, c));
          }

          yield Parser.ID_PART;
        }

        case HIGH_SURROGATE_START -> {
          if (!Character.isLowSurrogate(c)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            The unicode high-low surrogate sequence is invalid %s %s
            """.formatted(s, message, high, c));
          }

          int cp = Character.toCodePoint(high, c);

          if (!Character.isJavaIdentifierStart(cp)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            Character '%s' is an invalid java identifier start.
            """.formatted(s, message, Character.toString(cp)));
          }

          yield Parser.ID_PART;
        }

        case HIGH_SURROGATE_PART -> {
          if (!Character.isLowSurrogate(c)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            The unicode high-low surrogate sequence is invalid %s %s
            """.formatted(s, message, high, c));
          }

          int cp = Character.toCodePoint(high, c);

          if (!Character.isJavaIdentifierPart(c)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            Character '%s' is an invalid java identifier part.
            """.formatted(s, message, Character.toString(cp)));
          }

          yield Parser.ID_PART;
        }

        default -> throw new AssertionError("Unknown state=" + state + ";string=" + s);
      };
    }

    if (state == Parser.DOT) {
      throw new IllegalArgumentException("""
      The string %s is %s:

      It must not end with a '.' character.
      """.formatted(s, message));
    }

    if (hasDot && !allowDots) {
      throw new IllegalArgumentException("""
      The string %s is %s:

      It must not contain a '.' character.
      """.formatted(s, message));
    }
  }

}