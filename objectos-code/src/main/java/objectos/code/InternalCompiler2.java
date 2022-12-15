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

import objectos.util.IntArrays;

class InternalCompiler2 extends InternalApi2 {

  private interface ArrAccess { int START = 0; int FIRST = 1; int NEXT = 2; }

  private interface Assign { int START = 0; int LHS = 1; int RHS = 2; }

  private interface At { int START = 0; int TYPE = 1; int VALUE = 2; }

  private interface ChainIvk { int START = 0; int NL = 1; int NEXT = 2; }

  private interface ExpName { int START = 0; int BASE = 1; int NAME = 2; }

  private interface FieldDecl { int START = 0; int MODS = 1; int NAME = 2; int INIT = 3; }

  private interface IfaceDecl { int START = 0; int MODS = 1; int NAME = 2; int TYPE = 3; }

  private interface LocalVar { int START = 0; int INIT = 1; }

  private interface MInvoke { int RECV = 0; int NAME = 1; int LPAR = 2; int ARG = 3; int SLOT = 4; }

  private interface NewExpr { int START = 0; int TYPE = 1; int ARG = 2; }

  private interface Param { int START = 0; int TYPE = 1; int NAME = 2; }

  private interface TypeParam { int NAME = 0; int TYPE = 1; }

  private static final int NULL = Integer.MIN_VALUE;
  private static final int FALSE = 0;
  private static final int TRUE = 1;

  private static final int _START = 0;
  private static final int _ANNOS = 1;
  private static final int _PKG = 2;
  private static final int _IMPO = 3;
  private static final int _MODS = 4;
  private static final int _TPAR = 5;
  private static final int _TYPE = 6;
  private static final int _NAME = 7;
  private static final int _EXTS = 8;
  private static final int _IMPLS = 9;
  private static final int _CTES = 10;
  private static final int _PARAM = 11;
  private static final int _ARG = 12;
  private static final int _BODY = 13;
  private static final int _LHS = 14;

  final void pass1() {
    code = 0;

    codeIndex = 0;

    markIndex = -1;

    objectIndex = 0;

    protoIndex = 0;

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

      case ByteProto.CLASS_TYPE -> classType();

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
    if (markIndex < 0) {
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

  private void $codeadd(PseudoElement element) {
    $codeadd(ByteCode.PSEUDO_ELEMENT, element.ordinal());
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

    stackArray[stackIndex++] = protoIndex;

    protoIndex = location;
  }

  private Keyword $keywordpeek() {
    var index = protoArray[protoIndex];

    return Keyword.get(index);
  }

  private Object $objectpeek() {
    var index = protoArray[protoIndex];

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
    return markArray[markIndex];
  }

  private int $parentpop() {
    return markArray[markIndex--];
  }

  private void $parentpush(int v0, int v1) {
    markArray = IntArrays.growIfNecessary(markArray, markIndex + 2);

    markArray[++markIndex] = v0;
    markArray[++markIndex] = v1;
  }

  private void $parentpush(int v0, int v1, int v2) {
    markArray = IntArrays.growIfNecessary(markArray, markIndex + 3);

    markArray[++markIndex] = v0;
    markArray[++markIndex] = v1;
    markArray[++markIndex] = v2;
  }

  private void $parentpush(int v0, int v1, int v2, int v3, int v4) {
    markArray = IntArrays.growIfNecessary(markArray, markIndex + 5);

    markArray[++markIndex] = v0;
    markArray[++markIndex] = v1;
    markArray[++markIndex] = v2;
    markArray[++markIndex] = v3;
    markArray[++markIndex] = v4;
  }

  private int $parentvalget(int offset) {
    var index = markIndex - offset;

    return markArray[index];
  }

  private void $parentvalinc(int offset) {
    var index = markIndex - offset;

    markArray[index]++;
  }

  private void $parentvalset(int offset, int value) {
    var index = markIndex - offset;

    markArray[index] = value;
  }

  private int $protonxt() { return protoArray[protoIndex++]; }

  private int $protopeek() { return protoArray[protoIndex]; }

  private void $protopop() { protoIndex = stackArray[--stackIndex]; }

  private void annotation(int child) {
    var state = $parentvalget(1);

    if (child == ByteProto.CLASS_TYPE) {
      if (state == At.START) {
        $codeadd(Separator.COMMERCIAL_AT);

        $parentvalset(1, At.TYPE);
      }

      return;
    }

    // if child != @ element name

    switch (state) {
      case At.TYPE -> {
        $codeadd(Separator.LEFT_PARENTHESIS);

        $parentvalset(1, At.VALUE);
      }
    }
  }

  private void annotationBreak(int state) {
    switch (state) {
      case At.VALUE -> {
        $codeadd(Separator.RIGHT_PARENTHESIS);
      }
    }
  }

  private void arrayAccessExpression(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case ArrAccess.START -> $parentvalset(1, ArrAccess.FIRST);

      case ArrAccess.FIRST -> {
        $codeadd(Separator.LEFT_SQUARE_BRACKET);

        $parentvalset(1, ArrAccess.NEXT);
      }

      case ArrAccess.NEXT -> {
        $codeadd(Separator.RIGHT_SQUARE_BRACKET);
        $codeadd(Separator.LEFT_SQUARE_BRACKET);
      }
    }
  }

  private void arrayAccessExpressionBreak(int state) {
    switch (state) {
      case ArrAccess.FIRST, ArrAccess.NEXT -> {
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
      Assign.START, // 1 = state
      proto
    );
  }

  private void assignmentExpression(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case Assign.START -> {
        if (child != ByteProto.ASSIGNMENT_OPERATOR) {
          $parentvalset(1, Assign.LHS);
        }
      }

      case Assign.LHS -> {
        if (child != ByteProto.ASSIGNMENT_OPERATOR) {
          $codeadd(Whitespace.OPTIONAL);

          var operatorLocation = codeIndex;

          $codeadd(ByteCode.NOP1, 0);

          $codeadd(Whitespace.OPTIONAL);

          $parentvalset(2, operatorLocation);

          $parentvalset(1, Assign.RHS);
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

  private void block(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case _START -> {
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(PseudoElement.BEFORE_NEXT_STATEMENT);
        $codeadd(Indentation.ENTER_BLOCK);
        $codeadd(Indentation.EMIT);

        $parentvalset(1, _BODY);
      }

      case _BODY -> {
        $codeadd(PseudoElement.BEFORE_NEXT_STATEMENT);
        $codeadd(Indentation.EMIT);
      }
    }
  }

  private void blockBreak(int state) {
    switch (state) {
      case _START -> {
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        $codeadd(PseudoElement.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void chainedMethodInvocation(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case ChainIvk.START -> $parentvalset(1, ChainIvk.NEXT);

      case ChainIvk.NEXT -> {
        if (child == ByteProto.NEW_LINE) {
          $parentvalset(1, ChainIvk.NL);
        } else {
          $codeadd(Separator.DOT);
        }
      }

      case ChainIvk.NL -> {
        if (child != ByteProto.NEW_LINE) {
          $codeadd(Indentation.CONTINUATION);
          $codeadd(Separator.DOT);
          $parentvalset(1, ChainIvk.NEXT);
        }
      }
    }
  }

  private void classDeclaration(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.MODIFIER -> {
        typeDeclarationModifier();

        if (state == _MODS) {
          $codeadd(Whitespace.MANDATORY);
        } else {
          $parentvalset(1, _MODS);
        }
      }

      case ByteProto.IDENTIFIER -> {
        typeDeclarationIdentifier();

        switch (state) {
          case _START -> {
            $codeadd(Keyword.CLASS);
            $codeadd(Whitespace.MANDATORY);

            $parentvalset(1, _NAME);
          }

          case _MODS -> {
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

        $parentvalset(1, _EXTS);
      }

      case ByteProto.IMPLEMENTS -> {
        switch (state) {
          case _NAME, _EXTS -> {
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
        if (state == _NAME) {
          $codeadd(Whitespace.OPTIONAL);
          $codeadd(Separator.LEFT_CURLY_BRACKET);
          $codeadd(PseudoElement.BEFORE_FIRST_MEMBER);
          $codeadd(Indentation.ENTER_BLOCK);
          $codeadd(Indentation.EMIT);

          $parentvalset(1, _BODY);
        }
      }
    }
  }

  private void classDeclarationBreak(int state) {
    typeDeclarationBreak();

    switch (state) {
      case _NAME, _EXTS, _IMPLS -> {
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        $codeadd(PseudoElement.BEFORE_NON_EMPTY_BLOCK_END);
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
        if (state != _MODS) {
          $codeadd(PseudoElement.AFTER_ANNOTATION);
        }
      }
    }
  }

  private void classInstanceCreation(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case NewExpr.START -> {
        $codeadd(Keyword.NEW);
        $codeadd(Whitespace.MANDATORY);

        if (child == ByteProto.CLASS_TYPE) {
          $parentvalset(1, NewExpr.TYPE);
        }
      }

      case NewExpr.TYPE -> {
        $codeadd(Separator.LEFT_PARENTHESIS);

        $parentvalset(1, NewExpr.ARG);
      }

      case NewExpr.ARG -> {
        commaAndSpace();

        $parentvalset(1, NewExpr.ARG);
      }
    }
  }

  private void classInstanceCreationBreak(int state) {
    if (state == NewExpr.TYPE) {
      $codeadd(Separator.LEFT_PARENTHESIS);
    }

    $codeadd(Separator.RIGHT_PARENTHESIS);

    semicolonIfNecessary();
  }

  private void classType() {
    var proto = ByteProto.CLASS_TYPE;

    $cloop1parent(proto);

    $parentpush(
      NULL, // 2 = code start
      0, // 1 = name count
      proto
    );
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

  private void classTypeBreak(int nameCount) {
    var codeStart = $parentpop();

    var instruction = autoImports.classTypeInstruction();

    if (instruction == ByteCode.NOP) {
      return;
    }

    if (instruction == nameCount) {
      // skip package name & dot

      codeArray[codeStart] = ByteCode.NOP1;
      codeArray[codeStart + 2] = ByteCode.NOP1;

      return;
    }

    throw new UnsupportedOperationException(
      "Implement me :: nameCount=" + nameCount + ";codeStart=" + codeStart + ";inst="
          + instruction);
  }

  private void commaAndSpace() {
    $codeadd(Separator.COMMA);
    $codeadd(PseudoElement.BEFORE_NEXT_COMMA_SEPARATED_ITEM);
  }

  private void compilationUnit(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.PACKAGE_DECLARATION -> {
        if (state == _START) {
          $parentvalset(1, _PKG);
        }
      }

      case ByteProto.AUTO_IMPORTS -> {
        switch (state) {
          case _START -> {
            $codeadd(ByteCode.AUTO_IMPORTS0);

            $parentvalset(1, _IMPO);
          }

          case _PKG -> {
            $codeadd(ByteCode.AUTO_IMPORTS1);

            $parentvalset(1, _IMPO);
          }
        }
      }

      default -> {
        switch (state) {
          case _START -> {
            $parentvalset(1, _BODY);
          }

          default -> {
            $codeadd(PseudoElement.BEFORE_NEXT_TOP_LEVEL_ITEM);
          }
        }
      }
    }
  }

  private void compilationUnitBreak(int state) {
    $codeadd(ByteCode.EOF);
  }

  private void constructorDeclaration(int child) {
    var state = $parentvalget(1);

    switch (child) {
      case ByteProto.MODIFIER -> {
        if (state == _MODS) {
          $codeadd(Whitespace.MANDATORY);
        } else {
          $parentvalset(1, _MODS);
        }
      }

      case ByteProto.FORMAL_PARAMETER -> {
        switch (state) {
          case _START -> {
            $codeadd(ByteCode.CONSTRUCTOR_NAME);
            $codeadd(Separator.LEFT_PARENTHESIS);

            $parentvalset(1, _PARAM);
          }

          case _MODS -> {
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
            $codeadd(ByteCode.CONSTRUCTOR_NAME);
            $codeadd(Separator.LEFT_PARENTHESIS);
            $codeadd(Separator.RIGHT_PARENTHESIS);
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $codeadd(PseudoElement.BEFORE_NEXT_STATEMENT);
            $codeadd(Indentation.ENTER_BLOCK);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _BODY);
          }

          case _MODS -> {
            $codeadd(Whitespace.MANDATORY);
            $codeadd(ByteCode.CONSTRUCTOR_NAME);
            $codeadd(Separator.LEFT_PARENTHESIS);
            $codeadd(Separator.RIGHT_PARENTHESIS);
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $codeadd(PseudoElement.BEFORE_NEXT_STATEMENT);
            $codeadd(Indentation.ENTER_BLOCK);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _BODY);
          }

          case _PARAM -> {
            $codeadd(Separator.RIGHT_PARENTHESIS);
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $codeadd(PseudoElement.BEFORE_NEXT_STATEMENT);
            $codeadd(Indentation.ENTER_BLOCK);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _BODY);
          }

          case _BODY -> {
            $codeadd(PseudoElement.BEFORE_NEXT_STATEMENT);
            $codeadd(Indentation.EMIT);
          }
        }
      }
    }
  }

  private void constructorDeclarationBreak(int state) {
    switch (state) {
      case _START -> {
        $codeadd(ByteCode.CONSTRUCTOR_NAME);
        $codeadd(Separator.LEFT_PARENTHESIS);
        $codeadd(Separator.RIGHT_PARENTHESIS);
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _MODS -> {
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
        $codeadd(PseudoElement.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
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
        if (state == _ANNOS) {
          $codeadd(PseudoElement.AFTER_ANNOTATION);
          $codeadd(Indentation.EMIT);
        } else {
          $codeadd(Indentation.EMIT);

          $parentvalset(1, _ANNOS);
        }
      }

      case ByteProto.MODIFIER -> {
        typeDeclarationModifier();

        switch (state) {
          case _START -> {
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _MODS);
          }

          case _ANNOS -> {
            $codeadd(PseudoElement.AFTER_ANNOTATION);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _MODS);
          }

          case _MODS -> {
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

          case _MODS -> {
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
            $codeadd(PseudoElement.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.ENTER_BLOCK);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _CTES);
          }

          case _CTES -> {
            $codeadd(Separator.COMMA);
            $codeadd(PseudoElement.BEFORE_NEXT_MEMBER);
            $codeadd(Indentation.EMIT);
          }
        }
      }

      default -> {
        switch (state) {
          case _CTES -> {
            $codeadd(Separator.SEMICOLON);
            $codeadd(PseudoElement.BEFORE_NEXT_MEMBER);
            $codeadd(Indentation.EMIT);

            $parentvalset(1, _BODY);
          }

          case _BODY -> {
            $codeadd(PseudoElement.BEFORE_NEXT_MEMBER);
            $codeadd(Indentation.EMIT);
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
        $codeadd(PseudoElement.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        $codeadd(PseudoElement.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void expressionName(int child) {
    if (child == ByteProto.IDENTIFIER) {
      int state = $parentvalget(1);

      if (state != ExpName.START) {
        $codeadd(Separator.DOT);
      }

      $parentvalset(1, ExpName.NAME);
    } else {
      $parentvalset(1, ExpName.BASE);
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
      if (state != FieldDecl.START) {
        $codeadd(Whitespace.MANDATORY);
      }

      $parentvalset(1, FieldDecl.MODS);

      return;
    }

    if (child == ByteProto.IDENTIFIER) {
      switch (state) {
        case FieldDecl.START, FieldDecl.MODS
             -> $codeadd(Whitespace.MANDATORY);

        default -> {
          commaAndSpace();
        }
      }

      $parentvalset(1, FieldDecl.NAME);

      return;
    }

    if (ByteProto.isType(child)) {
      if (state != FieldDecl.START) {
        $codeadd(Whitespace.MANDATORY);
      }

      return;
    }

    if (ByteProto.isExpression(child) || child == ByteProto.ARRAY_INITIALIZER) {
      if (state == FieldDecl.NAME) {
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Operator2.ASSIGNMENT);
        $codeadd(Whitespace.OPTIONAL);

        $parentvalset(1, FieldDecl.INIT);
      }

      return;
    }
  }

  private void formalParameter(int child) {
    var state = $parentvalget(1);

    if (ByteProto.isType(child)) {
      if (state == Param.START) {
        $parentvalset(1, Param.TYPE);
      }

      return;
    }

    if (child == ByteProto.IDENTIFIER) {
      if (state == Param.TYPE) {
        $codeadd(Whitespace.MANDATORY);

        $parentvalset(1, Param.NAME);
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
        if (state == IfaceDecl.MODS) {
          $codeadd(Whitespace.MANDATORY);
        }
      }

      case ByteProto.MODIFIER -> {
        typeDeclarationModifier();

        if (state != IfaceDecl.START) {
          $codeadd(Whitespace.MANDATORY);
        }

        $parentvalset(1, IfaceDecl.MODS);
      }

      case ByteProto.IDENTIFIER -> {
        typeDeclarationIdentifier();

        if (state != IfaceDecl.START) {
          $codeadd(Whitespace.MANDATORY);
        }

        $codeadd(Keyword.INTERFACE);
        $codeadd(Whitespace.MANDATORY);

        $parentvalset(1, IfaceDecl.NAME);
      }

      case ByteProto.EXTENDS_SINGLE, ByteProto.EXTENDS_MANY -> {
        if (state == IfaceDecl.TYPE) {
          code = _TYPE;
        } else {
          $codeadd(Whitespace.MANDATORY);

          code = _START;
        }

        $parentvalset(1, IfaceDecl.TYPE);
      }
    }
  }

  private void interfaceDeclarationBreak(int state) {
    typeDeclarationBreak();

    switch (state) {
      case IfaceDecl.NAME, IfaceDecl.TYPE -> {
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

        if (state != IfaceDecl.MODS) {
          $codeadd(PseudoElement.AFTER_ANNOTATION);
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

  private void localVariableDeclaration() {
    var proto = ByteProto.LOCAL_VARIABLE;

    $cloop1parent(proto);

    $parentpush(
      NULL, // 2 = name location
      LocalVar.START, // 1 = state
      proto
    );
  }

  private void localVariableDeclaration(int child) {
    var state = $parentvalget(1);

    if (ByteProto.isExpression(child)) {
      switch (state) {
        case LocalVar.START -> {
          $codeadd(Keyword.VAR);

          $codeadd(Whitespace.MANDATORY);

          var nameLocation = codeIndex;

          $codeadd(ByteCode.NOP1, 0);

          $parentvalset(2, nameLocation);

          $codeadd(Whitespace.OPTIONAL);

          $codeadd(Operator2.ASSIGNMENT);

          $codeadd(Whitespace.OPTIONAL);

          $parentvalset(1, LocalVar.INIT);
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

          $parentvalset(1, _MODS);
        }

        case _MODS -> {
          $codeadd(Whitespace.MANDATORY);
        }
      }

      return;
    }

    if (child == ByteProto.TYPE_PARAMETER) {
      switch (state) {
        case _START -> {
          $codeadd(Separator.LEFT_ANGLE_BRACKET);

          $parentvalset(1, _TPAR);
        }

        case _MODS -> {
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
        case _MODS -> $codeadd(Whitespace.MANDATORY);

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
        $codeadd(PseudoElement.BEFORE_NEXT_STATEMENT);
        $codeadd(Indentation.ENTER_BLOCK);
        $codeadd(Indentation.EMIT);

        $parentvalset(1, _BODY);
      }

      case _BODY -> {
        $codeadd(PseudoElement.BEFORE_NEXT_STATEMENT);

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
        $codeadd(PseudoElement.BEFORE_NON_EMPTY_BLOCK_END);
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

        if (state != _MODS) {
          $codeadd(PseudoElement.AFTER_ANNOTATION);
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
      MInvoke.NAME, // 1 = state
      proto
    );
  }

  private void methodInvocation(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case MInvoke.NAME -> {
        $codeadd(Separator.LEFT_PARENTHESIS);
        $codeadd(Indentation.ENTER_PARENTHESIS);

        if (child == ByteProto.NEW_LINE) {
          $parentvalinc(4);

          $parentvalset(1, MInvoke.LPAR);
        } else {
          $parentvalset(1, MInvoke.ARG);
        }
      }

      case MInvoke.LPAR -> {
        if (child != ByteProto.NEW_LINE) {
          $parentindent(4);

          $parentvalset(1, MInvoke.ARG);
        }
      }

      case MInvoke.ARG -> {
        if (child == ByteProto.NEW_LINE) {
          $parentvalinc(4);

          $parentvalset(2, nop1());

          $parentvalset(1, MInvoke.SLOT);
        } else {
          commaAndSpace();

          $parentvalset(1, MInvoke.ARG);
        }
      }

      case MInvoke.SLOT -> {
        if (child != ByteProto.NEW_LINE) {
          $parentindent(4);

          var slot = $parentvalget(2);

          codeArray[slot + 0] = ByteCode.SEPARATOR;
          codeArray[slot + 1] = Separator.COMMA.ordinal();

          $parentvalset(1, MInvoke.ARG);
        }
      }
    }
  }

  private void methodInvocationBreak(int state) {
    $parentpop(); // comma slot
    $parentpop(); // name loc
    $parentpop(); // nl

    if (state == MInvoke.NAME) {
      $codeadd(Separator.LEFT_PARENTHESIS);
      $codeadd(Indentation.ENTER_PARENTHESIS);
    }

    $codeadd(Indentation.EXIT_PARENTHESIS);
    $codeadd(Separator.RIGHT_PARENTHESIS);

    semicolonIfNecessary();
  }

  private void methodInvocationCallback(int child) {
    var state = $parentvalget(1);

    if (state == MInvoke.RECV) {
      $codeadd(Separator.DOT);

      var nameLocation = codeIndex;

      $codeadd(ByteCode.NOP1, 0);

      $parentvalset(3, nameLocation);

      $parentvalset(1, MInvoke.NAME);
    }
  }

  private void methodInvocationQualified() {
    var proto = ByteProto.METHOD_INVOCATION_QUALIFIED;

    $cloop1parent(proto);

    $parentpush(
      0, // 4 = NL
      NULL, // 3
      NULL, // 2 = comma slot
      MInvoke.RECV, // 1 = state
      proto
    );
  }

  private int nop1() {
    var result = codeIndex;

    $codeadd(ByteCode.NOP1, 0);

    return result;
  }

  private void packageDeclaration(int child) {
    switch (child) {
      case ByteProto.PACKAGE_NAME -> {
        $codeadd(Keyword.PACKAGE);

        $codeadd(Whitespace.MANDATORY);
      }
    }
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

  private void returnStatement(int child) {
    $codeadd(Keyword.RETURN);

    $codeadd(Whitespace.MANDATORY);
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
      TypeParam.NAME, // 1 = state
      proto
    );
  }

  private void typeParameter(int child) {
    var state = $parentvalget(1);

    if (ByteProto.isType(child)) {
      switch (state) {
        case TypeParam.NAME -> {
          $codeadd(Whitespace.MANDATORY);

          $codeadd(Keyword.EXTENDS);

          $codeadd(Whitespace.MANDATORY);

          $parentvalset(1, TypeParam.TYPE);
        }

        case TypeParam.TYPE -> {
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

}