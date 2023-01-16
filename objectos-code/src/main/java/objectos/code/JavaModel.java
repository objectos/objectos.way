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

  public sealed interface AnyType extends BodyElement, ParameterElement,
      /* to remove */
      Markable {}

  public sealed interface ArrayAccessExpression extends Expression, LeftHandSide {}

  public sealed interface ArrayInitializerElement extends Markable {}

  public sealed interface ArrayTypeElement extends
      /* to remove */
      Markable {}

  public sealed interface AssignmentExpression
      extends Expression, ExpressionStatement {}

  public sealed interface BlockStatement extends BlockElement {}

  public sealed interface ChainedMethodInvocation extends MethodInvocation {}

  public sealed interface ChainedMethodInvocationElement extends Markable {}

  public sealed interface ChainedMethodInvocationHead extends Markable {}

  public sealed interface ClassDeclaration
      extends
      ClassDeclarationElement,
      EnumDeclarationElement {}

  public sealed interface ClassDeclarationElement extends Markable {}

  public sealed interface ClassType extends BodyElement, ReferenceType,
      /* to remove */
      AnyType,
      TypeParameterBound,
      FieldDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface ConstructorDeclarationElement extends Markable {}

  public sealed interface EnumConstant
      extends EnumDeclarationElement {}

  public sealed interface EnumConstantElement extends Markable {}

  public sealed interface EnumDeclaration {}

  public sealed interface EnumDeclarationElement extends Markable {}

  public sealed interface Expression extends ExpressionElement, BlockElement,
      /* to remove */
      ArrayInitializerElement,
      EnumConstantElement,
      FieldDeclarationElement,
      MethodInvocationElement {}

  public sealed interface ExpressionElement extends Element,
      /* to remove */
      Markable {}

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

  public sealed interface FinalModifier extends BodyElement,
      /* to remove */
      ClassDeclarationElement,
      FieldDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface FormalParameter
      extends
      ConstructorDeclarationElement,
      MethodDeclarationElement {}

  public sealed interface IfStatement
      extends Statement {}

  public sealed interface InterfaceDeclaration {}

  public sealed interface InterfaceDeclarationElement extends Markable {}

  public sealed interface LeftHandSide extends Markable {}

  public sealed interface LocalVariableDeclarationStatement
      extends BlockStatement, MethodDeclarationElement {}

  public sealed interface Markable {
    void mark(MarkerApi api);
  }

  public interface MarkerApi {

    void markLambda();

    void markReference();

  }

  public sealed interface MethodDeclarationElement extends Markable {}

  public sealed interface MethodInvocationElement extends Element, Markable {}

  public sealed interface ParameterElement extends Element, Markable {}

  public sealed interface QualifiedMethodInvocation
      extends ChainedMethodInvocationHead, MethodInvocation {}

  public sealed interface Statement
      extends BlockStatement, ConstructorDeclarationElement, MethodDeclarationElement {}

  public sealed interface TypeParameterBound extends Element, Markable {}

  public sealed interface UnqualifiedMethodInvocation extends
      /* to remove */
      ChainedMethodInvocationHead,
      ChainedMethodInvocationElement,
      MethodInvocation {}

  enum _Elem
      implements
      ArrayInitializer,
      ArrayType,
      At,
      Block,
      Body,
      ClassInstanceCreationExpression,
      ConstructorDeclaration,
      ExplicitConstructorInvocation,
      ExpressionName,
      MethodDeclaration,
      MethodInvocation,
      ParameterizedType,
      TypeParameter {
    INSTANCE;

    @Override
    public final void mark(MarkerApi api) {
      throw new UnsupportedOperationException();
    }
  }

  enum _Ext {
    INSTANCE;
  }

  enum _Include implements Include {
    INSTANCE;

    @Override
    public final void mark(MarkerApi api) {
      api.markLambda();
    }
  }

  enum _Item
      implements
      AbstractModifier,
      ArrayDimension,
      AssignmentOperator,
      AutoImports,
      ClassKeyword,
      ClassType,
      Ellipsis,
      End,
      ExtendsKeyword,
      FinalModifier,
      Identifier,
      ImplementsKeyword,
      IntegerLiteral,
      InterfaceKeyword,
      NewKeyword,
      NewLine,
      PackageKeyword,
      PrimitiveType,
      PrivateModifier,
      ProtectedModifier,
      PublicModifier,
      ReturnKeyword,
      StaticModifier,
      StringLiteral,
      SuperKeyword,
      ThisKeyword,
      TypeVariable,
      VarKeyword,
      VoidKeyword {
    INSTANCE;

    @Override
    public final void mark(MarkerApi api) {
      throw new UnsupportedOperationException();
    }
  }

  sealed interface AbstractModifier extends BodyElement,
      /* to remove */
      ClassDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement {}

  sealed interface ArrayDimension extends ArrayTypeElement {}

  sealed interface ArrayInitializer extends ArrayInitializerElement,
      /* to remove */
      FieldDeclarationElement {}

  sealed interface ArrayType extends BodyElement,
      /* to remove */
      AnyType,
      ReferenceType,

      FieldDeclarationElement,
      MethodDeclarationElement {}

  sealed interface ArrayTypeComponent extends
      /* to remove */
      Markable {}

  sealed interface AssignmentOperator extends BlockElement {}

  sealed interface At extends BodyElement {}

  sealed interface AutoImports extends Element {}

  sealed interface Block extends BodyElement,
      /* to remove */
      Statement {}

  sealed interface BlockElement extends Element, Markable {}

  sealed interface Body extends BodyElement {}

  sealed interface BodyElement extends Element {}

  sealed interface ClassInstanceCreationExpression extends ExpressionStatement, PrimaryExpression {}

  sealed interface ClassKeyword extends BodyElement {}

  sealed interface ConstructorDeclaration extends BodyElement,
      /* to remove */
      ClassDeclarationElement, EnumDeclarationElement {}

  sealed interface Element {
    /**
     * Triggers implicit null check.
     */
    default Object self() { return this; }
  }

  sealed interface Ellipsis extends ParameterElement {}

  sealed interface End extends BlockElement, Element, MethodInvocationElement {}

  sealed interface ExplicitConstructorInvocation extends BlockElement,
      /* to remove */
      ConstructorDeclarationElement {}

  sealed interface ExtendsKeyword extends BodyElement {}

  sealed interface Identifier extends BodyElement, BlockElement, ParameterElement,
      /* to remove */
      ClassDeclarationElement,
      EnumConstantElement,
      EnumDeclarationElement,
      FieldDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement {}

  sealed interface ImplementsKeyword extends BodyElement,
      /* to remove */
      ClassDeclarationElement, EnumDeclarationElement {}

  sealed interface Include extends BlockElement, BodyElement,
      /* to remove */
      ClassDeclarationElement,
      EnumDeclarationElement,
      FieldDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement, MethodInvocationElement {}

  sealed interface IntegerLiteral extends Literal {}

  sealed interface InterfaceKeyword extends BodyElement {}

  sealed interface Literal extends PrimaryExpression {}

  sealed interface MethodDeclaration extends BodyElement,
      /* to remove */
      ClassDeclarationElement, EnumDeclarationElement {}

  sealed interface MethodInvocation extends Expression, ExpressionStatement {}

  sealed interface NewKeyword extends BlockElement {}

  sealed interface NewLine extends Element,
      /* to remove */
      BlockElement,
      ChainedMethodInvocationElement,
      MethodInvocationElement {}

  sealed interface PackageKeyword extends Element {}

  sealed interface ParameterizedType extends ReferenceType,
      /* to remove */
      AnyType {}

  sealed interface PrimaryExpression extends Expression {}

  sealed interface PrimitiveType extends AnyType, BodyElement,
      /* to remove */
      ArrayTypeComponent,

      FieldDeclarationElement,
      MethodDeclarationElement {}

  sealed interface PrivateModifier extends AccessModifier {}

  sealed interface ProtectedModifier extends AccessModifier {}

  sealed interface PublicModifier extends AccessModifier {}

  sealed interface ReferenceType extends ArrayTypeComponent,
      /* to remove */
      AnyType,

      FieldDeclarationElement,
      MethodDeclarationElement {}

  sealed interface ReturnKeyword extends BlockElement,
      /* to remove */
      Statement {}

  sealed interface StaticModifier extends BodyElement,
      /* to remove */
      ClassDeclarationElement,
      EnumDeclarationElement,
      FieldDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement {}

  sealed interface StringLiteral extends Literal,
      /* to remove */
      AnnotationElementValue {}

  sealed interface SuperKeyword extends BlockElement {}

  sealed interface ThisKeyword extends PrimaryExpression {}

  sealed interface TypeParameter extends BodyElement,
      /* to remove */
      MethodDeclarationElement {}

  sealed interface TypeVariable extends ReferenceType {}

  sealed interface VarKeyword extends BlockElement {}

  sealed interface VoidKeyword extends BodyElement,
      /* to remove */
      MethodDeclarationElement {}

  private sealed interface AccessModifier extends BodyElement,
      /* to remove */
      ClassDeclarationElement,
      ConstructorDeclarationElement,
      EnumDeclarationElement,
      FieldDeclarationElement,
      InterfaceDeclarationElement,
      MethodDeclarationElement {}

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
      Identifier,
      IfStatement,
      ImplementsKeyword,
      IntegerLiteral,
      InterfaceDeclaration,
      LocalVariableDeclarationStatement,
      MethodDeclaration,
      NewLine,
      ParameterizedType,
      PrimitiveType,
      PrivateModifier,
      ProtectedModifier,
      PublicModifier,
      QualifiedMethodInvocation,
      ReturnKeyword,
      StaticModifier,
      StringLiteral,
      ThisKeyword,
      TypeParameter,
      TypeVariable,
      UnqualifiedMethodInvocation,
      VoidKeyword {
    private Ref() {}

    @Override
    public final void mark(MarkerApi api) {
      api.markReference();
    }
  }

  static final _Elem ELEM = _Elem.INSTANCE;

  static final _Ext EXT = _Ext.INSTANCE;

  static final _Include INCLUDE = _Include.INSTANCE;

  static final _Item ITEM = _Item.INSTANCE;

  static final Ref REF = new Ref();

  private JavaModel() {}

  static void checkIdentifier(String s) {
    if (s.isEmpty()) {
      throw new IllegalArgumentException("Identifier must not be empty");
    }

    checkName(s, false, "an invalid identifier");
  }

  static void checkMethodName(String methodName) {
    if (methodName.isEmpty()) {
      throw new IllegalArgumentException("Method name must not be empty");
    }

    checkName(methodName, false, "an invalid method name");
  }

  static void checkPackageName(String s) {
    checkName(s, true, "an invalid package name");
  }

  static void checkSimpleName(String s) {
    if (s.isEmpty()) {
      throw new IllegalArgumentException("A simple name must not be empty");
    }

    checkName(s, false, "an invalid simple name");
  }

  static void checkVarName(String name) {
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Local variable name must not be empty");
    }

    checkName(name, false, "an invalid local variable name");
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