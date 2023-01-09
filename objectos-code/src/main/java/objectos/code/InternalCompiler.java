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

import java.util.stream.Collectors;
import java.util.stream.Stream;
import objectos.code2.Indentation;
import objectos.code2.Separator;
import objectos.code2.Whitespace;
import objectos.util.IntArrays;

class InternalCompiler extends InternalApi {

  private static final int _START = 0,
      _PACKAGE = 1, _IMPORTS = 2,
      _ANNOTATIONS = 3, _MODIFIERS = 4,
      _CLAUSE = 5, _CLAUSE_TYPE = 6,
      _TYPE = 7, _NAME = 8, _INIT = 9,
      _BODY = 10,
      _EXPRESSION = 11;

  private static final int _EXTENDS = 54;
  private static final int _TPAR = 55;
  private static final int _IMPLS = 59;
  private static final int _CTES = 60;
  private static final int _PARAM = 61;
  private static final int _ARG = 62;
  private static final int _LHS = 64;
  private static final int _RHS = 65;
  private static final int _VALUE = 66;
  private static final int _BASE = 68;
  private static final int _FIRST = 69;
  private static final int _NL = 70;
  private static final int _NEXT = 71;
  private static final int _RECV = 72;
  private static final int _LPAR = 73;
  private static final int _SLOT = 74;

  private static final int NULL = Integer.MIN_VALUE;
  private static final int FALSE = 0;
  private static final int TRUE = 1;

  final void compile() {
    codeIndex = 0;

    localIndex = -1;

    try {
      stackpush(ByteProto.COMPILATION_UNIT, _START);
      element();
    } catch (RuntimeException e) {
      codeadd(Whitespace.NEW_LINE);
      codeadd(Whitespace.NEW_LINE);

      var collector = Collectors.joining(
        System.lineSeparator(),
        e.getMessage() + System.lineSeparator() + System.lineSeparator(),
        ""
      );

      var stackTrace = Stream.of(e.getStackTrace())
          .map(Object::toString)
          .collect(collector);

      codeadd(ByteCode.RAW, object(stackTrace));

      e.printStackTrace();
    }

    codeadd(ByteCode.EOF);
  }

  final void pass1() {
    code = 0;

    codeIndex = 0;

    localIndex = -1;

    objectIndex = 0;

    itemIndex = 0;

    stackIndex = 0;

    $parentpush(0, ByteProto.EOF);

    var proto = 0;

    do {
      proto = $cloop();
    } while (proto != ByteProto.EOF);
  }

  private int $cloop() {
    var proto = $protonxt();

    switch (proto) {
      case ByteProto.ASSIGNMENT_EXPRESSION -> assignmentExpression();

      case ByteProto.ASSIGNMENT_OPERATOR -> assignmentOperator();

      case ByteProto.AUTO_IMPORTS -> { $cloop1parent(proto); }

      case ByteProto.BREAK -> { $cloop2break(); $protopop(); }

      case ByteProto.CLASS_DECLARATION -> typeDeclaration(proto);

      case ByteProto.CLASS_TYPE -> classType$();

      case ByteProto.ELLIPSIS -> { $cloop1parent(proto); $codeadd(Separator.ELLIPSIS); }

      case ByteProto.EOF -> {}

      case ByteProto.ENUM_DECLARATION -> typeDeclaration(proto);

      case ByteProto.IDENTIFIER -> $cloop0add(proto, ByteCode.IDENTIFIER);

      case ByteProto.INTERFACE_DECLARATION -> typeDeclaration(proto);

      case ByteProto.INVOKE_METHOD_NAME -> invokeMethodName();

      case ByteProto.JMP -> $jump();

      case ByteProto.LOCAL_VARIABLE -> localVariableDeclaration();

      case ByteProto.MODIFIER -> $cloop0add(proto, ByteCode.RESERVED_KEYWORD);

      case ByteProto.NEW_LINE -> { $cloop1parent(proto); $codeadd(Whitespace.NEW_LINE); }

      case ByteProto.OBJECT_END -> $protopop();

      case ByteProto.PACKAGE_NAME -> $cloop0add(proto, ByteCode.IDENTIFIER);

      case ByteProto.SIMPLE_NAME -> $cloop0add(proto, ByteCode.NAME);

      case ByteProto.STRING_LITERAL -> $cloop0add(proto, ByteCode.STRING_LITERAL);

      case ByteProto.METHOD_DECLARATION -> methodDeclaration();

      case ByteProto.METHOD_INVOCATION -> methodInvocation();

      case ByteProto.METHOD_INVOCATION_QUALIFIED -> methodInvocationQualified();

      case ByteProto.NO_TYPE -> { $cloop1parent(proto); $codeadd(Keyword.VOID); }

      case ByteProto.PRIMITIVE_LITERAL -> $cloop0add(proto, ByteCode.PRIMITIVE_LITERAL);

      case ByteProto.PRIMITIVE_TYPE -> $cloop0add(proto, ByteCode.RESERVED_KEYWORD);

      case ByteProto.THIS -> { $cloop1parent(proto); $codeadd(Keyword.THIS); }

      case ByteProto.TYPE_PARAMETER -> typeParameter();

      case ByteProto.TYPE_VARIABLE -> $cloop0add(proto, ByteCode.IDENTIFIER);

      case ByteProto.VAR_NAME -> varName();

      default -> { code = _START; $cloop1parent(proto); $parentpush(code, proto); }
    }

    return proto;
  }

  private void $cloop0add(int proto, int code) {
    $cloop1parent(proto);

    $codeadd(code, $protonxt());
  }

  private void $cloop1parent(int child) {
    var parent = $parentpeek();

    switch (parent) {
      case ByteProto.ANNOTATION -> annotation(child);

      case ByteProto.ARRAY_ACCESS_EXPRESSION -> arrayAccessExpression(child);

      case ByteProto.ARRAY_INITIALIZER -> arrayInitializer(child);

      case ByteProto.ARRAY_TYPE -> arrayType(child);

      case ByteProto.ASSIGNMENT_EXPRESSION -> assignmentExpression(child);

      case ByteProto.BLOCK -> block(child);

      case ByteProto.CHAINED_METHOD_INVOCATION -> chainedMethodInvocation(child);

      case ByteProto.CLASS_DECLARATION -> classDeclaration(child);

      case ByteProto.CLASS_INSTANCE_CREATION0 -> classInstanceCreation(child);

      case ByteProto.CLASS_TYPE -> classType(child);

      case ByteProto.COMPILATION_UNIT -> compilationUnit(child);

      case ByteProto.CONSTRUCTOR_DECLARATION -> constructorDeclaration(child);

      case ByteProto.ENUM_CONSTANT -> enumConstant(child);

      case ByteProto.ENUM_DECLARATION -> enumDeclaration(child);

      case ByteProto.EOF -> root(child);

      case ByteProto.EXTENDS_MANY -> extendsMany(child);

      case ByteProto.EXTENDS_SINGLE -> extendsSingle(child);

      case ByteProto.EXPRESSION_NAME -> expressionName(child);

      case ByteProto.FIELD_ACCESS_EXPRESSION0 -> fieldAccessExpression0(child);

      case ByteProto.FIELD_DECLARATION -> fieldDeclaration(child);

      case ByteProto.FORMAL_PARAMETER -> formalParameter(child);

      case ByteProto.IMPLEMENTS -> implementsClause(child);

      case ByteProto.INTERFACE_DECLARATION -> interfaceDeclaration(child);

      case ByteProto.LOCAL_VARIABLE -> localVariableDeclaration(child);

      case ByteProto.METHOD_DECLARATION -> methodDeclaration(child);

      case ByteProto.METHOD_INVOCATION, ByteProto.METHOD_INVOCATION_QUALIFIED
           -> methodInvocation(child);

      case ByteProto.PACKAGE_DECLARATION -> packageDeclaration(child);

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType(child);

      case ByteProto.RETURN_STATEMENT -> returnStatement(child);

      case ByteProto.SUPER_INVOCATION -> superInvocation(child);

      case ByteProto.TYPE_PARAMETER -> typeParameter(child);
    }
  }

  private void $cloop2break() {
    if (localIndex < 0) {
      return;
    }

    var self = $parentpop();

    var state = $parentpop();

    switch (self) {
      case ByteProto.ANNOTATION -> annotationBreak(state);

      case ByteProto.ARRAY_ACCESS_EXPRESSION -> arrayAccessExpressionBreak(state);

      case ByteProto.ARRAY_INITIALIZER -> arrayInitializerBreak(state);

      case ByteProto.ASSIGNMENT_EXPRESSION -> assignmentExpressionBreak(state);

      case ByteProto.BLOCK -> blockBreak(state);

      case ByteProto.CHAINED_METHOD_INVOCATION -> semicolonIfNecessary();

      case ByteProto.CLASS_DECLARATION -> classDeclarationBreak(state);

      case ByteProto.CLASS_INSTANCE_CREATION0 -> classInstanceCreationBreak(state);

      case ByteProto.CLASS_TYPE -> classTypeBreak(state);

      case ByteProto.COMPILATION_UNIT -> compilationUnitBreak(state);

      case ByteProto.CONSTRUCTOR_DECLARATION -> constructorDeclarationBreak(state);

      case ByteProto.ENUM_CONSTANT -> enumConstantBreak(state);

      case ByteProto.ENUM_DECLARATION -> enumDeclarationBreak(state);

      case ByteProto.FIELD_DECLARATION -> $codeadd(Separator.SEMICOLON);

      case ByteProto.INTERFACE_DECLARATION -> interfaceDeclarationBreak(state);

      case ByteProto.LOCAL_VARIABLE -> localVariableDeclarationBreak(state);

      case ByteProto.METHOD_DECLARATION -> methodDeclarationBreak(state);

      case ByteProto.METHOD_INVOCATION, ByteProto.METHOD_INVOCATION_QUALIFIED
           -> methodInvocationBreak(state);

      case ByteProto.PACKAGE_DECLARATION -> $codeadd(Separator.SEMICOLON);

      case ByteProto.PARAMETERIZED_TYPE -> $codeadd(Separator.RIGHT_ANGLE_BRACKET);

      case ByteProto.RETURN_STATEMENT -> $codeadd(Separator.SEMICOLON);

      case ByteProto.SUPER_INVOCATION -> superInvocationBreak(state);

      case ByteProto.TYPE_PARAMETER -> typeParameterBreak(state);
    }

    switch ($parentpeek()) {
      case ByteProto.CLASS_DECLARATION -> classDeclarationCallback(self);

      case ByteProto.INTERFACE_DECLARATION -> interfaceDeclarationCallback(self);

      case ByteProto.METHOD_DECLARATION -> methodDeclarationCallback(self);

      case ByteProto.METHOD_INVOCATION_QUALIFIED -> methodInvocationCallback(self);
    }
  }

  private void $codeadd(Indentation indentation) {
    $codeadd(ByteCode.INDENTATION, indentation.ordinal());
  }

  private void $codeadd(int v0) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex);

    codeArray[codeIndex++] = v0;
  }

  private void $codeadd(int v0, int v1) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
  }

  private void $codeadd(Keyword keyword) {
    $codeadd(ByteCode.RESERVED_KEYWORD, keyword.ordinal());
  }

  private void $codeadd(Operator2 operator) {
    $codeadd(ByteCode.OPERATOR, operator.ordinal());
  }

  private void $codeadd(Separator separator) {
    $codeadd(ByteCode.SEPARATOR, separator.ordinal());
  }

  private void $codeadd(Whitespace whitespace) {
    $codeadd(ByteCode.WHITESPACE, whitespace.ordinal());
  }

  private void $jump() {
    var location = $protonxt();

    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex);

    stackArray[stackIndex++] = itemIndex;

    itemIndex = location;
  }

  private Keyword $keywordpeek() {
    var index = itemArray[itemIndex];

    return Keyword.get(index);
  }

  private Object $objectpeek() {
    var index = itemArray[itemIndex];

    return objectArray[index];
  }

  private void $parentindent(int offset) {
    var nl = $parentvalget(offset);

    if (nl > 0) {
      $codeadd(Indentation.EMIT);
    } else {
      $parentvalset(offset, 0);
    }
  }

  private int $parentpeek() {
    return localArray[localIndex];
  }

  private int $parentpop() {
    return localArray[localIndex--];
  }

  private void $parentpush(int v0, int v1) {
    localArray = IntArrays.growIfNecessary(localArray, localIndex + 2);

    localArray[++localIndex] = v0;
    localArray[++localIndex] = v1;
  }

  private void $parentpush(int v0, int v1, int v2) {
    localArray = IntArrays.growIfNecessary(localArray, localIndex + 3);

    localArray[++localIndex] = v0;
    localArray[++localIndex] = v1;
    localArray[++localIndex] = v2;
  }

  private void $parentpush(int v0, int v1, int v2, int v3, int v4) {
    localArray = IntArrays.growIfNecessary(localArray, localIndex + 5);

    localArray[++localIndex] = v0;
    localArray[++localIndex] = v1;
    localArray[++localIndex] = v2;
    localArray[++localIndex] = v3;
    localArray[++localIndex] = v4;
  }

  private int $parentvalget(int offset) {
    var index = localIndex - offset;

    return localArray[index];
  }

  private void $parentvalinc(int offset) {
    var index = localIndex - offset;

    localArray[index]++;
  }

  private void $parentvalset(int offset, int value) {
    var index = localIndex - offset;

    localArray[index] = value;
  }

  private int $protonxt() { return itemArray[itemIndex++]; }

  private int $protopeek() { return itemArray[itemIndex]; }

  private void $protopop() { itemIndex = stackArray[--stackIndex]; }

  private void annotation() {
    codeadd(Separator.COMMERCIAL_AT);

    stackpush(ByteProto.ANNOTATION, _START);

    element();

    int state = contextpop();

    switch (state) {
      case _TYPE -> {}

      default -> stubPop(ByteProto.ANNOTATION, state);
    }
  }

  private void annotation(int child) {
    var state = $parentvalget(1);

    if (child == ByteProto.CLASS_TYPE) {
      if (state == _START) {
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.COMMERCIAL_AT);

        $parentvalset(1, _TYPE);
      }

      return;
    }

    // if child != @ element name

    switch (state) {
      case _TYPE -> {
        $codeadd(Separator.LEFT_PARENTHESIS);

        $parentvalset(1, _VALUE);
      }
    }
  }

  private void annotation(int context, int state, int item) {
    switch (item) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> {
            stateset(_TYPE);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void annotationBreak(int state) {
    switch (state) {
      case _VALUE -> {
        $codeadd(Separator.RIGHT_PARENTHESIS);
      }
    }
  }

  private void arrayAccessExpression(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case _START -> $parentvalset(1, _FIRST);

      case _FIRST -> {
        $codeadd(Separator.LEFT_SQUARE_BRACKET);

        $parentvalset(1, _NEXT);
      }

      case _NEXT -> {
        $codeadd(Separator.RIGHT_SQUARE_BRACKET);
        $codeadd(Separator.LEFT_SQUARE_BRACKET);
      }
    }
  }

  private void arrayAccessExpressionBreak(int state) {
    switch (state) {
      case _FIRST, _NEXT -> {
        $codeadd(Separator.RIGHT_SQUARE_BRACKET);
      }
    }
  }

  private void arrayInitializer(int child) {
    int count = $parentvalget(1);

    if (count == 0) {
      $codeadd(Separator.LEFT_CURLY_BRACKET);
    } else {
      commaAndSpace();
    }

    $parentvalinc(1);
  }

  private void arrayInitializerBreak(int count) {
    if (count == 0) {
      $codeadd(Separator.LEFT_CURLY_BRACKET);
    }

    $codeadd(Separator.RIGHT_CURLY_BRACKET);
  }

  private void arrayType(int child) {
    switch (child) {
      case ByteProto.DIM -> {
        $codeadd(Separator.LEFT_SQUARE_BRACKET);

        $codeadd(Separator.RIGHT_SQUARE_BRACKET);
      }
    }
  }

  private void assignmentExpression() {
    var proto = ByteProto.ASSIGNMENT_EXPRESSION;

    $cloop1parent(proto);

    $parentpush(
      NULL, // 2 = operator location
      _START, // 1 = state
      proto
    );
  }

  private void assignmentExpression(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case _START -> {
        if (child != ByteProto.ASSIGNMENT_OPERATOR) {
          $parentvalset(1, _LHS);
        }
      }

      case _LHS -> {
        if (child != ByteProto.ASSIGNMENT_OPERATOR) {
          $codeadd(Whitespace.OPTIONAL);

          var operatorLocation = codeIndex;

          $codeadd(ByteCode.NOP1, 0);

          $codeadd(Whitespace.OPTIONAL);

          $parentvalset(2, operatorLocation);

          $parentvalset(1, _RHS);
        }
      }
    }
  }

  private void assignmentExpressionBreak(int state) {
    $parentpop(); // operator location

    semicolonIfNecessary();
  }

  private void assignmentOperator() {
    var parent = $parentpeek();

    switch (parent) {
      case ByteProto.ASSIGNMENT_EXPRESSION -> {
        int location = $parentvalget(2);

        codeArray[location + 0] = ByteCode.OPERATOR;

        codeArray[location + 1] = $protonxt();
      }
    }
  }

  private void block() {
    codeadd(Separator.LEFT_CURLY_BRACKET);
    codeadd(Indentation.ENTER_BLOCK);

    stackpush(ByteProto.BLOCK, _START);

    element();

    int state = contextpop();
    switch (state) {
      case _START -> {
        codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      default -> stubPop(ByteProto.BLOCK, state);
    }
  }

  private void block(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case _START -> {
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
        $codeadd(Indentation.ENTER_BLOCK);
        $codeadd(Indentation.EMIT);

        $parentvalset(1, _BODY);
      }

      case _BODY -> {
        $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
        $codeadd(Indentation.EMIT);
      }
    }
  }

  private void block(int context, int state, int item) {
    switch (item) {
      case ByteProto.RETURN -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.NEW_LINE);
            codeadd(Indentation.EMIT);
            stateset(_BODY);
          }

          case _BODY -> {
            codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
            codeadd(Indentation.EMIT);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void blockBreak(int state) {
    switch (state) {
      case _START -> {
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        $codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void body() {
    codeadd(Separator.LEFT_CURLY_BRACKET);
    codeadd(Indentation.ENTER_BLOCK);

    stackpush(ByteProto.BODY, _START);

    element();

    int state = contextpop();

    switch (state) {
      case _START -> {
        codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _NAME, _INIT -> {
        codeadd(Separator.SEMICOLON);
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      default -> stubPop(ByteProto.BODY, state);
    }
  }

  private void body(int context, int state, int item) {
    switch (item) {
      case ByteProto.ANNOTATION -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(_ANNOTATIONS);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.ARRAY_TYPE,
           ByteProto.CLASS_TYPE,
           ByteProto.PARAMETERIZED_TYPE,
           ByteProto.PRIMITIVE_TYPE -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(_TYPE);
          }

          case _ANNOTATIONS -> {
            codeadd(Whitespace.AFTER_ANNOTATION);
            codeadd(Indentation.EMIT);
            stateset(_TYPE);
          }

          case _MODIFIERS -> {
            codeadd(Whitespace.MANDATORY);
            stateset(_TYPE);
          }

          case _NAME, _INIT -> {
            codeadd(Separator.SEMICOLON);
            codeadd(Whitespace.BEFORE_NEXT_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(_TYPE);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.BLOCK -> {
        switch (state) {
          case _NAME -> {
            codeadd(Separator.LEFT_PARENTHESIS);
            codeadd(Separator.RIGHT_PARENTHESIS);
            codeadd(Whitespace.OPTIONAL);
            stateset(_BODY);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.CLASS -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(_BODY);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.IDENTIFIER -> {
        switch (state) {
          case _TYPE -> {
            codeadd(Whitespace.MANDATORY);
            stateset(_NAME);
          }

          case _NAME, _INIT -> {
            commaAndSpace();
            stateset(_NAME);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.VOID -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(_TYPE);
          }

          case _ANNOTATIONS -> {
            codeadd(Whitespace.AFTER_ANNOTATION);
            codeadd(Indentation.EMIT);
            stateset(_TYPE);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void chainedMethodInvocation(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case _START -> $parentvalset(1, _NEXT);

      case _NEXT -> {
        if (child == ByteProto.NEW_LINE) {
          $parentvalset(1, _NL);
        } else {
          $codeadd(Separator.DOT);
        }
      }

      case _NL -> {
        if (child != ByteProto.NEW_LINE) {
          $codeadd(Indentation.CONTINUATION);
          $codeadd(Separator.DOT);
          $parentvalset(1, _NEXT);
        }
      }
    }
  }

  private void classDeclaration(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.MODIFIER -> {
        typeDeclarationModifier();

        if (state == _MODIFIERS) {
          $codeadd(Whitespace.MANDATORY);
        } else {
          $codeadd(Indentation.EMIT);

          $parentvalset(1, _MODIFIERS);
        }
      }

      case ByteProto.IDENTIFIER -> {
        typeDeclarationIdentifier();

        switch (state) {
          case _START -> {
            $codeadd(Indentation.EMIT);
            $codeadd(Keyword.CLASS);
            $codeadd(Whitespace.MANDATORY);

            $parentvalset(1, _NAME);
          }

          case _MODIFIERS -> {
            $codeadd(Whitespace.MANDATORY);
            $codeadd(Keyword.CLASS);
            $codeadd(Whitespace.MANDATORY);

            $parentvalset(1, _NAME);
          }
        }
      }

      case ByteProto.EXTENDS_SINGLE -> {
        if (state == _NAME) {
          $codeadd(Whitespace.MANDATORY);
        }

        $parentvalset(1, _EXTENDS);
      }

      case ByteProto.IMPLEMENTS -> {
        switch (state) {
          case _NAME, _EXTENDS -> {
            $codeadd(Whitespace.MANDATORY);

            $parentvalset(1, _IMPLS);

            code = _START;
          }

          case _IMPLS -> {
            code = _TYPE;
          }
        }
      }

      default -> {
        switch (state) {
          case _NAME, _EXTENDS, _IMPLS -> {
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.ENTER_BLOCK);

            $parentvalset(1, _BODY);
          }

          case _BODY -> {
            $codeadd(Whitespace.BEFORE_NEXT_MEMBER);
          }
        }
      }
    }
  }

  private void classDeclaration(int ctx, int state, int item) {
    switch (item) {
      case ByteProto.BODY -> {
        switch (state) {
          case _START,
               _CLAUSE_TYPE -> {
            contextpop();
            codeadd(Whitespace.OPTIONAL);
          }

          default -> stubState(ctx, state, item);
        }
      }

      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _CLAUSE -> {
            codeadd(Whitespace.MANDATORY);
            stateset(_CLAUSE_TYPE);
          }

          case _CLAUSE_TYPE -> {
            commaAndSpace();
          }

          default -> stubState(ctx, state, item);
        }
      }

      case ByteProto.EXTENDS,
           ByteProto.IMPLEMENTS -> {
        switch (state) {
          case _START,
               _CLAUSE_TYPE -> {
            codeadd(Whitespace.MANDATORY);
            stateset(_CLAUSE);
          }

          default -> stubState(ctx, state, item);
        }
      }

      default -> stubItem(ctx, state, item);
    }
  }

  private void classDeclarationBreak(int state) {
    typeDeclarationBreak();

    switch (state) {
      case _NAME, _EXTENDS, _IMPLS -> {
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        $codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void classDeclarationCallback(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.ANNOTATION -> {
        if (state != _MODIFIERS) {
          $codeadd(Whitespace.AFTER_ANNOTATION);
        }
      }
    }
  }

  private void classInstanceCreation(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case _START -> {
        $codeadd(Keyword.NEW);
        $codeadd(Whitespace.MANDATORY);

        if (child == ByteProto.CLASS_TYPE) {
          $parentvalset(1, _TYPE);
        }
      }

      case _TYPE -> {
        $codeadd(Separator.LEFT_PARENTHESIS);

        $parentvalset(1, _ARG);
      }

      case _ARG -> {
        commaAndSpace();

        $parentvalset(1, _ARG);
      }
    }
  }

  private void classInstanceCreationBreak(int state) {
    if (state == _TYPE) {
      $codeadd(Separator.LEFT_PARENTHESIS);
    }

    $codeadd(Separator.RIGHT_PARENTHESIS);

    semicolonIfNecessary();
  }

  private void classKeyword() {
    codeadd(Keyword.CLASS);
    codeadd(Whitespace.MANDATORY);
    codeadd(ByteCode.IDENTIFIER, itemnxt());

    stackpush(ByteProto.CLASS_DECLARATION, _START);
  }

  private void classType() {
    var packageIndex = itemnxt();

    var packageName = (String) objectget(packageIndex);

    autoImports.classTypePackageName(packageName);

    var count = itemnxt();

    switch (count) {
      case 1 -> {
        var n1Index = itemnxt();

        var n1 = (String) objectget(n1Index);

        autoImports.classTypeSimpleName(n1);

        int instruction = autoImports.classTypeInstruction();

        switch (instruction) {
          case 1 -> {
            codeadd(ByteCode.IDENTIFIER, n1Index);
          }

          default -> {
            codeadd(ByteCode.IDENTIFIER, packageIndex);
            codeadd(Separator.DOT);
            codeadd(ByteCode.IDENTIFIER, n1Index);
          }
        }
      }

      default -> {
        throw new UnsupportedOperationException(
          "Implement me :: count=" + count);
      }
    }
  }

  private void classType(int child) {
    switch (child) {
      case ByteProto.PACKAGE_NAME -> {
        var name = (String) $objectpeek();

        autoImports.classTypePackageName(name);

        $parentvalset(2, codeIndex);
      }

      case ByteProto.SIMPLE_NAME -> {
        var name = (String) $objectpeek();

        autoImports.classTypeSimpleName(name);

        $codeadd(Separator.DOT);

        $parentvalinc(1);
      }
    }
  }

  private void classType$() {
    var proto = ByteProto.CLASS_TYPE;

    $cloop1parent(proto);

    $parentpush(
      NULL, // 2 = code start
      0, // 1 = name count
      proto
    );
  }

  private void classTypeBreak(int nameCount) {
    var codeStart = $parentpop();

    var instruction = autoImports.classTypeInstruction();

    if (instruction == ByteCode.NOP) {
      return;
    }

    if (instruction == 1) {
      // skip package name
      codeArray[codeStart] = ByteCode.NOP1;

      // skip DOT
      codeArray[codeStart + 2] = ByteCode.NOP1;

      var toSkip = nameCount - instruction;

      for (int i = 0; i < toSkip; i++) {
        var index = codeStart;

        index += 4;

        index += 2 * i;

        // skip DOT
        codeArray[index] = ByteCode.NOP1;

        // skip name
        codeArray[index + 2] = ByteCode.NOP1;
      }

      return;
    }

    throw new UnsupportedOperationException(
      "Implement me :: nameCount=" + nameCount + ";codeStart=" + codeStart + ";inst="
          + instruction);
  }

  private void codeadd(Indentation value) { codeadd(ByteCode.INDENTATION, value.ordinal()); }

  private void codeadd(int v0) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 0);

    codeArray[codeIndex++] = v0;
  }

  private void codeadd(int v0, int v1) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
  }

  private void codeadd(Keyword value) { codeadd(ByteCode.RESERVED_KEYWORD, value.ordinal()); }

  private void codeadd(Separator value) { codeadd(ByteCode.SEPARATOR, value.ordinal()); }

  private void codeadd(Whitespace value) { codeadd(ByteCode.WHITESPACE, value.ordinal()); }

  private void commaAndSpace() {
    $codeadd(Separator.COMMA);
    $codeadd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);
  }

  private void compilationUnit(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.PACKAGE_DECLARATION -> {
        if (state == _START) {
          $parentvalset(1, _PACKAGE);
        }
      }

      case ByteProto.AUTO_IMPORTS -> {
        switch (state) {
          case _START -> {
            $codeadd(ByteCode.AUTO_IMPORTS0);

            $parentvalset(1, _IMPORTS);
          }

          case _PACKAGE -> {
            $codeadd(ByteCode.AUTO_IMPORTS1);

            $parentvalset(1, _IMPORTS);
          }
        }
      }

      default -> {
        switch (state) {
          case _START -> {
            $parentvalset(1, _BODY);
          }

          default -> {
            $codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
          }
        }
      }
    }
  }

  private void compilationUnit(int ctx, int state, int item) {
    switch (item) {
      case ByteProto.ANNOTATION -> {
        switch (state) {
          case _START -> {
            stateset(_ANNOTATIONS);
          }

          default -> stubState(ctx, state, item);
        }
      }

      case ByteProto.AUTO_IMPORTS -> {
        switch (state) {
          case _START -> {
            codeadd(ByteCode.AUTO_IMPORTS0);
            stateset(_IMPORTS);
          }

          case _PACKAGE -> {
            codeadd(ByteCode.AUTO_IMPORTS1);
            stateset(_IMPORTS);
          }

          default -> stubState(ctx, state, item);
        }
      }

      case ByteProto.CLASS -> {
        switch (state) {
          case _START -> {
            stateset(_BODY);
          }

          case _ANNOTATIONS -> {
            codeadd(Whitespace.AFTER_ANNOTATION);
            stateset(_BODY);
          }

          case _PACKAGE, _IMPORTS, _BODY -> {
            codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            stateset(_BODY);
          }

          case _MODIFIERS -> {
            codeadd(Whitespace.MANDATORY);
            stateset(_BODY);
          }

          default -> stubState(ctx, state, item);
        }
      }

      case ByteProto.MODIFIER -> {
        switch (state) {
          case _START -> {
            stateset(_MODIFIERS);
          }

          case _BODY -> {
            codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            stateset(_MODIFIERS);
          }

          default -> stubState(ctx, state, item);
        }
      }

      case ByteProto.PACKAGE -> {
        switch (state) {
          case _START -> {
            stateset(_PACKAGE);
          }

          default -> stubState(ctx, state, item);
        }
      }

      //      default -> {
      //        switch (state) {
      //          case _START -> {
      //            $parentvalset(1, _BODY);
      //          }
      //
      //          default -> {
      //            $codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
      //          }
      //        }
      //      }

      default -> stubItem(ctx, state, item);
    }
  }

  private void compilationUnitBreak(int state) {
    $codeadd(ByteCode.EOF);
  }

  private void constructorDeclaration(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.MODIFIER -> {
        switch (state) {
          case _START -> {
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _MODIFIERS);
          }

          case _MODIFIERS -> {
            $codeadd(Whitespace.MANDATORY);
          }
        }
      }

      case ByteProto.FORMAL_PARAMETER -> {
        switch (state) {
          case _START -> {
            $codeadd(Indentation.EMIT);
            $codeadd(ByteCode.CONSTRUCTOR_NAME);
            $codeadd(Separator.LEFT_PARENTHESIS);

            $parentvalset(1, _PARAM);
          }

          case _MODIFIERS -> {
            $codeadd(Whitespace.MANDATORY);
            $codeadd(ByteCode.CONSTRUCTOR_NAME);
            $codeadd(Separator.LEFT_PARENTHESIS);

            $parentvalset(1, _PARAM);
          }

          case _PARAM -> commaAndSpace();
        }
      }

      default -> {
        switch (state) {
          case _START -> {
            $codeadd(Indentation.EMIT);
            $codeadd(ByteCode.CONSTRUCTOR_NAME);
            $codeadd(Separator.LEFT_PARENTHESIS);
            $codeadd(Separator.RIGHT_PARENTHESIS);
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
            $codeadd(Indentation.ENTER_BLOCK);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _BODY);
          }

          case _MODIFIERS -> {
            $codeadd(Whitespace.MANDATORY);
            $codeadd(ByteCode.CONSTRUCTOR_NAME);
            $codeadd(Separator.LEFT_PARENTHESIS);
            $codeadd(Separator.RIGHT_PARENTHESIS);
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
            $codeadd(Indentation.ENTER_BLOCK);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _BODY);
          }

          case _PARAM -> {
            $codeadd(Separator.RIGHT_PARENTHESIS);
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
            $codeadd(Indentation.ENTER_BLOCK);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _BODY);
          }

          case _BODY -> {
            $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
            $codeadd(Indentation.EMIT);
          }
        }
      }
    }
  }

  private void constructorDeclarationBreak(int state) {
    switch (state) {
      case _START -> {
        $codeadd(Indentation.EMIT);
        $codeadd(ByteCode.CONSTRUCTOR_NAME);
        $codeadd(Separator.LEFT_PARENTHESIS);
        $codeadd(Separator.RIGHT_PARENTHESIS);
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _MODIFIERS -> {
        $codeadd(Whitespace.MANDATORY);
        $codeadd(ByteCode.CONSTRUCTOR_NAME);
        $codeadd(Separator.LEFT_PARENTHESIS);
        $codeadd(Separator.RIGHT_PARENTHESIS);
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _PARAM -> {
        $codeadd(Separator.RIGHT_PARENTHESIS);
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        $codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void context(int context, int state, int item) {
    switch (context) {
      case ByteProto.ANNOTATION -> annotation(context, state, item);

      case ByteProto.BLOCK -> block(context, state, item);

      case ByteProto.BODY -> body(context, state, item);

      case ByteProto.CLASS_DECLARATION -> classDeclaration(context, state, item);

      case ByteProto.COMPILATION_UNIT -> compilationUnit(context, state, item);

      case ByteProto.RETURN_STATEMENT -> returnStatement(context, state, item);

      default -> warn(
        "no-op context '%s'".formatted(protoname(context))
      );
    }
  }

  private int contextpop() {
    int state = localArray[localIndex];
    localIndex -= 2;
    return state;
  }

  private void element() {
    int size = itemnxt();
    int start = itemIndex;
    int max = start + size;

    for (int i = start; i < max; i++) {
      int context = stackpeek(1);

      int state = stackpeek(0);

      itemIndex = itemArray[i];

      int item = itemnxt();

      context(context, state, item);

      item(context, state, item);

      pop(context);
    }
  }

  private void enumConstant(int child) {
    var state = $parentvalget(1);

    if (child == ByteProto.IDENTIFIER) {
      if (state == _START) {
        $parentvalset(1, _NAME);
      }
    } else if (ByteProto.isExpression(child)) {
      switch (state) {
        case _NAME -> {
          $codeadd(Separator.LEFT_PARENTHESIS);

          $parentvalset(1, _ARG);
        }

        case _ARG -> {
          commaAndSpace();
        }
      }
    }
  }

  private void enumConstantBreak(int state) {
    switch (state) {
      case _ARG -> {
        $codeadd(Separator.RIGHT_PARENTHESIS);
      }
    }
  }

  private void enumDeclaration(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.ANNOTATION -> {
        if (state == _ANNOTATIONS) {
          $codeadd(Whitespace.AFTER_ANNOTATION);
          $codeadd(Indentation.EMIT);
        } else {
          $codeadd(Indentation.EMIT);

          $parentvalset(1, _ANNOTATIONS);
        }
      }

      case ByteProto.MODIFIER -> {
        typeDeclarationModifier();

        switch (state) {
          case _START -> {
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _MODIFIERS);
          }

          case _ANNOTATIONS -> {
            $codeadd(Whitespace.AFTER_ANNOTATION);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _MODIFIERS);
          }

          case _MODIFIERS -> {
            $codeadd(Whitespace.MANDATORY);
          }
        }
      }

      case ByteProto.IDENTIFIER -> {
        typeDeclarationIdentifier();

        switch (state) {
          case _START -> {
            $codeadd(Keyword.ENUM);
            $codeadd(Whitespace.MANDATORY);

            $parentvalset(1, _NAME);
          }

          case _MODIFIERS -> {
            $codeadd(Whitespace.MANDATORY);
            $codeadd(Keyword.ENUM);
            $codeadd(Whitespace.MANDATORY);

            $parentvalset(1, _NAME);
          }
        }
      }

      case ByteProto.IMPLEMENTS -> {
        switch (state) {
          case _NAME -> {
            $codeadd(Whitespace.MANDATORY);

            $parentvalset(1, _IMPLS);

            code = _START;
          }

          case _IMPLS -> {
            code = _TYPE;
          }
        }
      }

      case ByteProto.ENUM_CONSTANT -> {
        switch (state) {
          case _NAME, _IMPLS -> {
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.ENTER_BLOCK);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _CTES);
          }

          case _CTES -> {
            $codeadd(Separator.COMMA);
            $codeadd(Whitespace.BEFORE_NEXT_MEMBER);
            $codeadd(Indentation.EMIT);
          }
        }
      }

      default -> {
        switch (state) {
          case _CTES -> {
            $codeadd(Separator.SEMICOLON);
            $codeadd(Whitespace.BEFORE_NEXT_MEMBER);

            $parentvalset(1, _BODY);
          }

          case _BODY -> {
            $codeadd(Whitespace.BEFORE_NEXT_MEMBER);
          }
        }
      }
    }
  }

  private void enumDeclarationBreak(int state) {
    typeDeclarationBreak();

    switch (state) {
      case _CTES -> {
        $codeadd(Separator.SEMICOLON);
        $codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        $codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void expressionName(int child) {
    if (child == ByteProto.IDENTIFIER) {
      int state = $parentvalget(1);

      if (state != _START) {
        $codeadd(Separator.DOT);
      }

      $parentvalset(1, _NAME);
    } else {
      $parentvalset(1, _BASE);
    }
  }

  private void extendsMany(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case _START -> {
        if (child == ByteProto.CLASS_TYPE) {
          $codeadd(Keyword.EXTENDS);
          $codeadd(Whitespace.MANDATORY);

          $parentvalset(1, _TYPE);
        }
      }

      case _TYPE -> {
        if (child == ByteProto.CLASS_TYPE) {
          commaAndSpace();
        }
      }
    }
  }

  private void extendsSingle(int child) {
    extendsMany(child);
  }

  private void fieldAccessExpression0(int child) {
    var state = $parentvalget(1);

    if (state == _START) {
      $parentvalset(1, _LHS);
    } else {
      $codeadd(Separator.DOT);
    }
  }

  private void fieldDeclaration(int child) {
    int state = $parentvalget(1);

    if (child == ByteProto.MODIFIER) {
      switch (state) {
        case _START -> {
          $codeadd(Indentation.EMIT);

          $parentvalset(1, _MODIFIERS);
        }

        case _MODIFIERS -> {
          $codeadd(Whitespace.MANDATORY);
        }
      }

      return;
    }

    if (child == ByteProto.IDENTIFIER) {
      switch (state) {
        case _START, _MODIFIERS -> {
          $codeadd(Whitespace.MANDATORY);
        }

        default -> {
          commaAndSpace();
        }
      }

      $parentvalset(1, _NAME);

      return;
    }

    if (ByteProto.isType(child)) {
      if (state != _START) {
        $codeadd(Whitespace.MANDATORY);
      } else {
        $codeadd(Indentation.EMIT);
      }

      return;
    }

    if (ByteProto.isExpression(child) || child == ByteProto.ARRAY_INITIALIZER) {
      if (state == _NAME) {
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Operator2.ASSIGNMENT);
        $codeadd(Whitespace.OPTIONAL);

        $parentvalset(1, _INIT);
      }

      return;
    }
  }

  private void formalParameter(int child) {
    var state = $parentvalget(1);

    if (ByteProto.isType(child)) {
      if (state == _START) {
        $parentvalset(1, _TYPE);
      }

      return;
    }

    if (child == ByteProto.IDENTIFIER) {
      if (state == _TYPE) {
        $codeadd(Whitespace.MANDATORY);

        $parentvalset(1, _NAME);
      }

      return;
    }
  }

  private void implementsClause(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case _START -> {
        if (ByteProto.isType(child)) {
          $codeadd(Keyword.IMPLEMENTS);
          $codeadd(Whitespace.MANDATORY);

          $parentvalset(1, _TYPE);
        }
      }

      case _TYPE -> {
        if (ByteProto.isType(child)) {
          commaAndSpace();
        }
      }
    }
  }

  private void interfaceDeclaration(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.ANNOTATION -> {
        if (state == _MODIFIERS) {
          $codeadd(Whitespace.MANDATORY);
        }
      }

      case ByteProto.MODIFIER -> {
        typeDeclarationModifier();

        if (state != _START) {
          $codeadd(Whitespace.MANDATORY);
        }

        $parentvalset(1, _MODIFIERS);
      }

      case ByteProto.IDENTIFIER -> {
        typeDeclarationIdentifier();

        if (state != _START) {
          $codeadd(Whitespace.MANDATORY);
        }

        $codeadd(Keyword.INTERFACE);
        $codeadd(Whitespace.MANDATORY);

        $parentvalset(1, _NAME);
      }

      case ByteProto.EXTENDS_SINGLE, ByteProto.EXTENDS_MANY -> {
        if (state == _TYPE) {
          code = _TYPE;
        } else {
          $codeadd(Whitespace.MANDATORY);

          code = _START;
        }

        $parentvalset(1, _TYPE);
      }
    }
  }

  private void interfaceDeclarationBreak(int state) {
    typeDeclarationBreak();

    switch (state) {
      case _NAME, _TYPE -> {
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void interfaceDeclarationCallback(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.ANNOTATION -> {

        if (state != _MODIFIERS) {
          $codeadd(Whitespace.AFTER_ANNOTATION);
        }

      }
    }
  }

  private void invokeMethodName() {
    var parent = $parentpeek();

    switch (parent) {
      case ByteProto.METHOD_INVOCATION, ByteProto.METHOD_INVOCATION_QUALIFIED -> {
        int location = $parentvalget(3);

        codeArray[location + 0] = ByteCode.IDENTIFIER;
        codeArray[location + 1] = $protonxt();
      }

      case ByteProto.TYPE_PARAMETER -> {
        int location = $parentvalget(2);

        codeArray[location + 0] = ByteCode.IDENTIFIER;
        codeArray[location + 1] = $protonxt();
      }
    }
  }

  private int isTopLevel() {
    var parent = $parentpeek();

    return parent == ByteProto.COMPILATION_UNIT ? TRUE : FALSE;
  }

  private void item(int context, int state, int item) {
    switch (item) {
      case ByteProto.ANNOTATION -> annotation();

      case ByteProto.AUTO_IMPORTS -> {}

      case ByteProto.BLOCK -> block();

      case ByteProto.BODY -> body();

      case ByteProto.CLASS -> classKeyword();

      case ByteProto.CLASS_TYPE -> classType();

      case ByteProto.EXTENDS -> codeadd(Keyword.EXTENDS);

      case ByteProto.IDENTIFIER -> codeadd(ByteCode.IDENTIFIER, itemnxt());

      case ByteProto.IMPLEMENTS -> codeadd(Keyword.IMPLEMENTS);

      case ByteProto.MODIFIER -> codeadd(ByteCode.RESERVED_KEYWORD, itemnxt());

      case ByteProto.PACKAGE -> packageKeyword();

      case ByteProto.PRIMITIVE_TYPE -> codeadd(ByteCode.RESERVED_KEYWORD, itemnxt());

      case ByteProto.RETURN -> returnStatement();

      case ByteProto.STRING_LITERAL -> codeadd(ByteCode.STRING_LITERAL, itemnxt());

      case ByteProto.VOID -> codeadd(Keyword.VOID);

      default -> warn(
        "no-op item '%s'".formatted(protoname(item))
      );
    }
  }

  private int itemnxt() { return itemArray[itemIndex++]; }

  private void localVariableDeclaration() {
    var proto = ByteProto.LOCAL_VARIABLE;

    $cloop1parent(proto);

    $parentpush(
      NULL, // 2 = name location
      _START, // 1 = state
      proto
    );
  }

  private void localVariableDeclaration(int child) {
    var state = $parentvalget(1);

    if (ByteProto.isExpression(child)) {
      switch (state) {
        case _START -> {
          $codeadd(Keyword.VAR);

          $codeadd(Whitespace.MANDATORY);

          var nameLocation = codeIndex;

          $codeadd(ByteCode.NOP1, 0);

          $parentvalset(2, nameLocation);

          $codeadd(Whitespace.OPTIONAL);

          $codeadd(Operator2.ASSIGNMENT);

          $codeadd(Whitespace.OPTIONAL);

          $parentvalset(1, _INIT);
        }
      }

      return;
    }
  }

  private void localVariableDeclarationBreak(int state) {
    $parentpop(); // name location

    $codeadd(Separator.SEMICOLON);
  }

  private void methodDeclaration() {
    var proto = ByteProto.METHOD_DECLARATION;

    $cloop1parent(proto);

    $parentpush(
      0, // 2 = is abstract?
      _START, // 1 = state
      proto
    );
  }

  private void methodDeclaration(int child) {
    var state = $parentvalget(1);

    if (child == ByteProto.MODIFIER) {
      var modifier = $keywordpeek();

      if (modifier == Keyword.ABSTRACT) {
        $parentvalinc(2);
      }

      switch (state) {
        case _START -> {
          $codeadd(Indentation.EMIT);

          $parentvalset(1, _MODIFIERS);
        }

        case _MODIFIERS -> {
          $codeadd(Whitespace.MANDATORY);
        }
      }

      return;
    }

    if (child == ByteProto.TYPE_PARAMETER) {
      switch (state) {
        case _START -> {
          $codeadd(Indentation.EMIT);
          $codeadd(Separator.LEFT_ANGLE_BRACKET);

          $parentvalset(1, _TPAR);
        }

        case _MODIFIERS -> {
          $codeadd(Whitespace.MANDATORY);
          $codeadd(Separator.LEFT_ANGLE_BRACKET);

          $parentvalset(1, _TPAR);
        }

        case _TPAR -> {
          commaAndSpace();
        }
      }

      return;
    }

    if (ByteProto.isType(child)) {
      switch (state) {
        case _START -> {
          $codeadd(Indentation.EMIT);
        }

        case _MODIFIERS -> {
          $codeadd(Whitespace.MANDATORY);
        }

        case _TPAR -> {
          $codeadd(Separator.RIGHT_ANGLE_BRACKET);
          $codeadd(Whitespace.OPTIONAL);
        }
      }

      $parentvalset(1, _TYPE);

      return;
    }

    if (child == ByteProto.IDENTIFIER) {
      if (state < _TYPE) {
        $codeadd(Indentation.EMIT);
        $codeadd(Keyword.VOID);
      }

      $codeadd(Whitespace.MANDATORY);

      $parentvalset(1, _NAME);

      return;
    }

    if (child == ByteProto.FORMAL_PARAMETER) {
      if (state != _PARAM) {
        $codeadd(Separator.LEFT_PARENTHESIS);
      } else {
        commaAndSpace();
      }

      $parentvalset(1, _PARAM);

      return;
    }

    // body

    switch (state) {
      case _NAME -> {
        $codeadd(Separator.LEFT_PARENTHESIS);
        $codeadd(Separator.RIGHT_PARENTHESIS);
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
        $codeadd(Indentation.ENTER_BLOCK);
        $codeadd(Indentation.EMIT);

        $parentvalset(1, _BODY);
      }

      case _PARAM -> {
        $codeadd(Separator.RIGHT_PARENTHESIS);
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
        $codeadd(Indentation.ENTER_BLOCK);
        $codeadd(Indentation.EMIT);

        $parentvalset(1, _BODY);
      }

      case _BODY -> {
        $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
        $codeadd(Indentation.EMIT);
      }
    }
  }

  private void methodDeclarationBreak(int state) {
    var isAbstract = $parentpop() > 0; // is abstract

    switch (state) {
      case _NAME -> {
        $codeadd(Separator.LEFT_PARENTHESIS);
        $codeadd(Separator.RIGHT_PARENTHESIS);

        if (isAbstract) {
          $codeadd(Separator.SEMICOLON);
        } else {
          $codeadd(Whitespace.OPTIONAL);
          $codeadd(Separator.LEFT_CURLY_BRACKET);
          $codeadd(Separator.RIGHT_CURLY_BRACKET);
        }
      }

      case _PARAM -> {
        $codeadd(Separator.RIGHT_PARENTHESIS);

        if (isAbstract) {
          $codeadd(Separator.SEMICOLON);
        } else {
          $codeadd(Whitespace.OPTIONAL);
          $codeadd(Separator.LEFT_CURLY_BRACKET);
          $codeadd(Separator.RIGHT_CURLY_BRACKET);
        }
      }

      case _BODY -> {
        $codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void methodDeclarationCallback(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.ANNOTATION -> {

        if (state != _MODIFIERS) {
          $codeadd(Whitespace.AFTER_ANNOTATION);
        }

      }
    }
  }

  private void methodInvocation() {
    var proto = ByteProto.METHOD_INVOCATION;

    $cloop1parent(proto);

    var nameLocation = codeIndex;

    $codeadd(ByteCode.NOP1, 0);

    $parentpush(
      0, // 4 = NL
      nameLocation, // 3
      NULL, // 2 = comma slot
      _NAME, // 1 = state
      proto
    );
  }

  private void methodInvocation(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case _NAME -> {
        $codeadd(Separator.LEFT_PARENTHESIS);
        $codeadd(Indentation.ENTER_PARENTHESIS);

        if (child == ByteProto.NEW_LINE) {
          $parentvalinc(4);

          $parentvalset(1, _LPAR);
        } else {
          $parentvalset(1, _ARG);
        }
      }

      case _LPAR -> {
        if (child != ByteProto.NEW_LINE) {
          $parentindent(4);

          $parentvalset(1, _ARG);
        }
      }

      case _ARG -> {
        if (child == ByteProto.NEW_LINE) {
          $parentvalinc(4);

          $parentvalset(2, nop1());

          $parentvalset(1, _SLOT);
        } else {
          commaAndSpace();

          $parentvalset(1, _ARG);
        }
      }

      case _SLOT -> {
        if (child != ByteProto.NEW_LINE) {
          $parentindent(4);

          var slot = $parentvalget(2);

          codeArray[slot + 0] = ByteCode.SEPARATOR;
          codeArray[slot + 1] = Separator.COMMA.ordinal();

          $parentvalset(1, _ARG);
        }
      }
    }
  }

  private void methodInvocationBreak(int state) {
    $parentpop(); // comma slot
    $parentpop(); // name loc
    int nl = $parentpop(); // nl

    if (state == _NAME) {
      $codeadd(Separator.LEFT_PARENTHESIS);
      $codeadd(Indentation.ENTER_PARENTHESIS);
    }

    $codeadd(Indentation.EXIT_PARENTHESIS);
    if (nl > 0) {
      $codeadd(Indentation.EMIT);
    }
    $codeadd(Separator.RIGHT_PARENTHESIS);

    semicolonIfNecessary();
  }

  private void methodInvocationCallback(int child) {
    var state = $parentvalget(1);

    if (state == _RECV) {
      $codeadd(Separator.DOT);

      var nameLocation = codeIndex;

      $codeadd(ByteCode.NOP1, 0);

      $parentvalset(3, nameLocation);

      $parentvalset(1, _NAME);
    }
  }

  private void methodInvocationQualified() {
    var proto = ByteProto.METHOD_INVOCATION_QUALIFIED;

    $cloop1parent(proto);

    $parentpush(
      0, // 4 = NL
      NULL, // 3
      NULL, // 2 = comma slot
      _RECV, // 1 = state
      proto
    );
  }

  private int nop1() {
    var result = codeIndex;

    $codeadd(ByteCode.NOP1, 0);

    return result;
  }

  private Object objectget(int index) {
    return objectArray[index];
  }

  private void packageDeclaration(int child) {
    switch (child) {
      case ByteProto.PACKAGE_NAME -> {
        $codeadd(Keyword.PACKAGE);

        $codeadd(Whitespace.MANDATORY);
      }
    }
  }

  private void packageKeyword() {
    codeadd(Keyword.PACKAGE);
    codeadd(Whitespace.MANDATORY);
    codeadd(ByteCode.IDENTIFIER, itemnxt());
    codeadd(Separator.SEMICOLON);
  }

  private void parameterizedType(int child) {
    int count = $parentvalget(1);

    switch (count) {
      case 0 -> {}

      case 1 -> $codeadd(Separator.LEFT_ANGLE_BRACKET);

      default -> commaAndSpace();
    }

    $parentvalinc(1);
  }

  private void pop(int context) {
    switch (context) {
      case ByteProto.RETURN_STATEMENT -> returnStatementPop(context);
    }
  }

  private String protoname(int value) {
    return switch (value) {
      case ByteProto.ANNOTATION -> "Annotation";

      case ByteProto.ARRAY_INITIALIZER -> "Array Init.";

      case ByteProto.ARRAY_TYPE -> "Array Type";

      case ByteProto.AUTO_IMPORTS -> "Auto Imports";

      case ByteProto.BLOCK -> "Block";

      case ByteProto.BODY -> "Body";

      case ByteProto.CLASS -> "Class";

      case ByteProto.CLASS_DECLARATION -> "Class Declaration";

      case ByteProto.CLASS_TYPE -> "Class Type";

      case ByteProto.COMPILATION_UNIT -> "Compilation Unit";

      case ByteProto.EXPRESSION_NAME -> "Expression Name";

      case ByteProto.EXTENDS -> "Extends";

      case ByteProto.IDENTIFIER -> "Identifier";

      case ByteProto.IMPLEMENTS -> "Implements";

      case ByteProto.METHOD_DECLARATION -> "Method Decl.";

      case ByteProto.METHOD_INVOCATION -> "Method Invocation";

      case ByteProto.MODIFIER -> "Modifier";

      case ByteProto.NEW_LINE -> "NL";

      case ByteProto.PACKAGE -> "Package";

      case ByteProto.PARAMETERIZED_TYPE -> "Parameterized Type";

      case ByteProto.PRIMITIVE_TYPE -> "Primitive Type";

      case ByteProto.RETURN -> "Return";

      case ByteProto.RETURN_STATEMENT -> "Return Stmt.";

      case ByteProto.PRIMITIVE_LITERAL -> "Primitive Literal";

      case ByteProto.STRING_LITERAL -> "String Literal";

      case ByteProto.VOID -> "Void";

      default -> Integer.toString(value);
    };
  }

  private void returnStatement() {
    codeadd(Keyword.RETURN);
    stackpush(ByteProto.RETURN_STATEMENT, _START);
  }

  private void returnStatement(int child) {
    $codeadd(Keyword.RETURN);
    $codeadd(Whitespace.MANDATORY);
  }

  private void returnStatement(int context, int state, int item) {
    switch (item) {
      case ByteProto.STRING_LITERAL -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.OPTIONAL);
            stateset(_EXPRESSION);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void returnStatementPop(int context) {
    int state = stackpeek(0);

    switch (state) {
      case _EXPRESSION -> {
        codeadd(Separator.SEMICOLON);
        contextpop();
      }

      default -> stubPop(context, state);
    }
  }

  private void root(int child) {
  }

  private void semicolonIfNecessary() {
    switch ($parentpeek()) {
      case ByteProto.BLOCK,
           ByteProto.COMPILATION_UNIT,
           ByteProto.CONSTRUCTOR_DECLARATION,
           ByteProto.METHOD_DECLARATION -> $codeadd(Separator.SEMICOLON);
    }
  }

  private int stackpeek(int offset) { return localArray[localIndex - offset]; }

  private void stackpush(int v0, int v1) {
    localArray = IntArrays.growIfNecessary(localArray, localIndex + 2);

    localArray[++localIndex] = v0;
    localArray[++localIndex] = v1;
  }

  private void stateset(int value) { localArray[localIndex] = value; }

  private void stubItem(int ctx, int state, int item) {
    warn("no-op item '%s' @ '%s' (state=%d)".formatted(
      protoname(item), protoname(ctx), state));
  }

  private void stubPop(int ctx, int state) {
    warn("no-op pop @ '%s' (state=%d)".formatted(
      protoname(ctx), state));
  }

  private void stubState(int ctx, int state, int item) {
    warn("no-op state @ '%s' (state=%d) item '%s'".formatted(
      protoname(ctx), state, protoname(item)));
  }

  private void superInvocation(int child) {
    var state = $parentvalget(1);

    if (ByteProto.isExpression(child)) {
      switch (state) {
        case _START -> {
          $codeadd(Keyword.SUPER);
          $codeadd(Separator.LEFT_PARENTHESIS);

          $parentvalset(1, _ARG);
        }

        case _ARG -> {
          commaAndSpace();
        }
      }
    }
  }

  private void superInvocationBreak(int state) {
    switch (state) {
      case _START -> {
        $codeadd(Keyword.SUPER);
        $codeadd(Separator.LEFT_PARENTHESIS);
        $codeadd(Separator.RIGHT_PARENTHESIS);
      }

      case _ARG -> {
        $codeadd(Separator.RIGHT_PARENTHESIS);
      }
    }

    $codeadd(Separator.SEMICOLON);
  }

  private void typeDeclaration(int proto) {
    $cloop1parent(proto);

    $parentpush(
      NULL, // 4 = simple name
      FALSE, // 3 = public?
      isTopLevel(), // 2 = top level?
      _START, // 1 = state
      proto
    );
  }

  private void typeDeclarationBreak() {
    var topLevel = $parentpop();
    var publicFound = $parentpop();
    var nameIndex = $parentpop();

    if (topLevel == TRUE) {
      var simpleName = "Unnamed";

      if (nameIndex >= 0) {
        simpleName = (String) objectArray[nameIndex];
      }

      autoImports.fileName(publicFound == TRUE, simpleName);
    }
  }

  private void typeDeclarationIdentifier() {
    int nameIndex = $protopeek();
    $codeadd(ByteCode.CONSTRUCTOR_NAME_STORE, nameIndex);
    $parentvalset(4, nameIndex);
  }

  private void typeDeclarationModifier() {
    var modifier = $keywordpeek();

    if (modifier == Keyword.PUBLIC) {
      $parentvalset(3, TRUE);
    }
  }

  private void typeParameter() {
    var proto = ByteProto.TYPE_PARAMETER;

    $cloop1parent(proto);

    var nameLocation = codeIndex;

    $codeadd(ByteCode.NOP1, 0);

    $parentpush(
      nameLocation, // 2 = name location
      _NAME, // 1 = state
      proto
    );
  }

  private void typeParameter(int child) {
    var state = $parentvalget(1);

    if (ByteProto.isType(child)) {
      switch (state) {
        case _NAME -> {
          $codeadd(Whitespace.MANDATORY);

          $codeadd(Keyword.EXTENDS);

          $codeadd(Whitespace.MANDATORY);

          $parentvalset(1, _TYPE);
        }

        case _TYPE -> {
          $codeadd(Whitespace.OPTIONAL);

          $codeadd(Separator.AMPERSAND);

          $codeadd(Whitespace.OPTIONAL);
        }
      }

      return;
    }
  }

  private void typeParameterBreak(int state) {
    $parentpop(); // name location
  }

  private void varName() {
    var parent = $parentpeek();

    switch (parent) {
      case ByteProto.LOCAL_VARIABLE -> {
        int location = $parentvalget(2);

        codeArray[location + 0] = ByteCode.IDENTIFIER;
        codeArray[location + 1] = $protonxt();
      }
    }
  }

  private void warn(String msg) {
    codeadd(Whitespace.NEW_LINE);
    codeadd(ByteCode.COMMENT, object(msg));
  }

}