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

  private interface ChainIvk {
    int START = 0;
    int NL = 1;
    int NEXT = 2;
  }

  private interface CUnit {
    int CONTENTS = 1 << 0;
    int AUTO_IMPORTS = 1 << 1;
  }

  private interface ExpName {
    int START = 0;
    int BASE = 1;
    int NAME = 2;
  }

  private interface FieldDecl {
    int START = 0;
    int MODIFIERS = 1;
    int IDENTIFIER = 2;
    int INITIALIZE = 3;
  }

  private interface LocalVar {
    int START = 0;
    int INIT = 1;
  }

  private interface MInvoke {
    int SUBJECT = 0;
    int METHOD_NAME = 1;
    int LEFT_PAR = 2;
    int ARG = 3;
    int SLOT = 4;
  }

  private interface NewExpr {
    int START = 0;
    int TYPE = 1;
    int ARG = 2;
  }

  private static final int NULL = Integer.MIN_VALUE;

  private static final int _START = 0;

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
      proto = loop();
    } while (proto != ByteProto.EOF);
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

  private Object $objectpeek() {
    var index = protoArray[protoIndex];

    return objectArray[index];
  }

  private boolean $parentbitget(int offset, int value) {
    var index = markIndex - offset;

    return (markArray[index] & value) != 0;
  }

  private void $parentbitset(int offset, int value) {
    var index = markIndex - offset;

    markArray[index] |= value;
  }

  private void $parentindent(int offset) {
    var nl = $parentvalget(offset);

    if (nl > 0) {
      $codeadd(PseudoElement.INDENTATION);
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

  private void $protopop() { protoIndex = stackArray[--stackIndex]; }

  private UnsupportedOperationException $uoe_state(int state) {
    return new UnsupportedOperationException(
      "Implement me :: state = " + state);
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
          $codeadd(PseudoElement.CONTINUATION_INDENTATION);

          $codeadd(Separator.DOT);

          $parentvalset(1, ChainIvk.NEXT);
        }
      }
    }
  }

  private void classDeclaration(int child) {
    switch (child) {
      case ByteProto.IDENTIFIER -> {
        $codeadd(Keyword.CLASS);

        $codeadd(Whitespace.MANDATORY);
      }
    }
  }

  private void classDeclarationBreak(int state) {
    switch (state) {
      case _START -> {
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      default -> throw $uoe_state(state);
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

    loop1Parent(proto);

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
    if (child != ByteProto.PACKAGE_DECLARATION &&
        !$parentbitget(1, CUnit.AUTO_IMPORTS) &&
        autoImports.enabled()) {
      $codeadd(ByteCode.AUTO_IMPORTS);

      $parentbitset(1, CUnit.AUTO_IMPORTS);
    }

    if ($parentbitget(1, CUnit.CONTENTS)) {
      $codeadd(PseudoElement.BEFORE_NEXT_TOP_LEVEL_ITEM);
    }

    $parentbitset(1, CUnit.CONTENTS);
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

  private void extendsSingle(int child) {
    if (child == ByteProto.CLASS_TYPE) {
      $codeadd(Whitespace.MANDATORY);

      $codeadd(Keyword.EXTENDS);

      $codeadd(Whitespace.MANDATORY);
    }
  }

  private void fieldDeclaration(int child) {
    int state = $parentvalget(1);

    if (child == ByteProto.MODIFIER) {
      if (state != FieldDecl.START) {
        $codeadd(Whitespace.MANDATORY);
      }

      $parentvalset(1, FieldDecl.MODIFIERS);

      return;
    }

    if (child == ByteProto.IDENTIFIER) {
      switch (state) {
        case FieldDecl.START, FieldDecl.MODIFIERS
            -> $codeadd(Whitespace.MANDATORY);

        default -> {
          commaAndSpace();
        }
      }

      $parentvalset(1, FieldDecl.IDENTIFIER);

      return;
    }

    if (ByteProto.isType(child)) {
      if (state != FieldDecl.START) {
        $codeadd(Whitespace.MANDATORY);
      }

      return;
    }

    if (ByteProto.isExpression(child) || child == ByteProto.ARRAY_INITIALIZER) {
      if (state == FieldDecl.IDENTIFIER) {
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Operator2.ASSIGNMENT);
        $codeadd(Whitespace.OPTIONAL);

        $parentvalset(1, FieldDecl.INITIALIZE);
      }

      return;
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
    }
  }

  private void localVariableDeclaration() {
    var proto = ByteProto.LOCAL_VARIABLE;

    loop1Parent(proto);

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

  private int loop() {
    var proto = $protonxt();

    switch (proto) {
      case ByteProto.BREAK -> { loop2Break(); $protopop(); }

      case ByteProto.CLASS_TYPE -> classType();

      case ByteProto.EOF -> {}

      case ByteProto.IDENTIFIER -> loop0(proto, ByteCode.IDENTIFIER);

      case ByteProto.INVOKE_METHOD_NAME -> invokeMethodName();

      case ByteProto.JMP -> $jump();

      case ByteProto.LOCAL_VARIABLE -> localVariableDeclaration();

      case ByteProto.MODIFIER -> loop0(proto, ByteCode.RESERVED_KEYWORD);

      case ByteProto.NEW_LINE -> { loop1Parent(proto); $codeadd(Whitespace.NEW_LINE); }

      case ByteProto.OBJECT_END -> $protopop();

      case ByteProto.PACKAGE_NAME -> loop0(proto, ByteCode.IDENTIFIER);

      case ByteProto.SIMPLE_NAME -> loop0(proto, ByteCode.NAME);

      case ByteProto.STRING_LITERAL -> loop0(proto, ByteCode.STRING_LITERAL);

      case ByteProto.METHOD_INVOCATION -> methodInvocation();

      case ByteProto.METHOD_INVOCATION_QUALIFIED -> methodInvocationQualified();

      case ByteProto.PRIMITIVE_LITERAL -> loop0(proto, ByteCode.PRIMITIVE_LITERAL);

      case ByteProto.PRIMITIVE_TYPE -> loop0(proto, ByteCode.RESERVED_KEYWORD);

      case ByteProto.THIS -> { loop1Parent(proto); $codeadd(Keyword.THIS); }

      case ByteProto.VAR_NAME -> varName();

      default -> { loop1Parent(proto); $parentpush(0, proto); }
    }

    return proto;
  }

  private void loop0(int proto, int code) {
    loop1Parent(proto);

    $codeadd(code, $protonxt());
  }

  private void loop1Parent(int child) {
    var parent = $parentpeek();

    switch (parent) {
      case ByteProto.ARRAY_INITIALIZER -> arrayInitializer(child);

      case ByteProto.ARRAY_TYPE -> arrayType(child);

      case ByteProto.CHAINED_METHOD_INVOCATION -> chainedMethodInvocation(child);

      case ByteProto.CLASS_DECLARATION -> classDeclaration(child);

      case ByteProto.CLASS_INSTANCE_CREATION0 -> classInstanceCreation(child);

      case ByteProto.CLASS_TYPE -> classType(child);

      case ByteProto.COMPILATION_UNIT -> compilationUnit(child);

      case ByteProto.EOF -> root(child);

      case ByteProto.EXTENDS_SINGLE -> extendsSingle(child);

      case ByteProto.EXPRESSION_NAME -> expressionName(child);

      case ByteProto.FIELD_DECLARATION -> fieldDeclaration(child);

      case ByteProto.LOCAL_VARIABLE -> localVariableDeclaration(child);

      case ByteProto.METHOD_INVOCATION, ByteProto.METHOD_INVOCATION_QUALIFIED
          -> methodInvocation(child);

      case ByteProto.PACKAGE_DECLARATION -> packageDeclaration(child);

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType(child);

      case ByteProto.RETURN_STATEMENT -> returnStatement(child);
    }
  }

  private void loop2Break() {
    if (markIndex < 0) {
      return;
    }

    var self = $parentpop();

    var state = $parentpop();

    switch (self) {
      case ByteProto.ARRAY_INITIALIZER -> arrayInitializerBreak(state);

      case ByteProto.CHAINED_METHOD_INVOCATION -> semicolonIfNecessary();

      case ByteProto.CLASS_DECLARATION -> classDeclarationBreak(state);

      case ByteProto.CLASS_INSTANCE_CREATION0 -> classInstanceCreationBreak(state);

      case ByteProto.CLASS_TYPE -> classTypeBreak(state);

      case ByteProto.COMPILATION_UNIT -> $codeadd(ByteCode.EOF);

      case ByteProto.FIELD_DECLARATION -> $codeadd(Separator.SEMICOLON);

      case ByteProto.LOCAL_VARIABLE -> localVariableDeclarationBreak(state);

      case ByteProto.METHOD_INVOCATION, ByteProto.METHOD_INVOCATION_QUALIFIED
          -> methodInvocationBreak(state);

      case ByteProto.PACKAGE_DECLARATION -> $codeadd(Separator.SEMICOLON);

      case ByteProto.PARAMETERIZED_TYPE -> $codeadd(Separator.GREATER_THAN_SIGN);

      case ByteProto.RETURN_STATEMENT -> $codeadd(Separator.SEMICOLON);
    }

    switch ($parentpeek()) {
      case ByteProto.METHOD_INVOCATION_QUALIFIED -> methodInvocationCallback(self);
    }
  }

  private void methodInvocation() {
    var proto = ByteProto.METHOD_INVOCATION;

    loop1Parent(proto);

    var nameLocation = codeIndex;

    $codeadd(ByteCode.NOP1, 0);

    $parentpush(
      0, // 4 = NL
      nameLocation, // 3
      NULL, // 2 = comma slot
      MInvoke.METHOD_NAME, // 1 = state
      proto
    );
  }

  private void methodInvocation(int child) {
    var state = $parentvalget(1);

    switch (state) {
      case MInvoke.METHOD_NAME -> {
        $codeadd(Separator.LEFT_PARENTHESIS);

        if (child == ByteProto.NEW_LINE) {
          $parentvalinc(4);

          $parentvalset(1, MInvoke.LEFT_PAR);
        } else {
          $parentvalset(1, MInvoke.ARG);
        }
      }

      case MInvoke.LEFT_PAR -> {
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

    if (state == MInvoke.METHOD_NAME) {
      $codeadd(Separator.LEFT_PARENTHESIS);
    }

    $codeadd(Separator.RIGHT_PARENTHESIS);

    semicolonIfNecessary();
  }

  private void methodInvocationCallback(int child) {
    var state = $parentvalget(1);

    if (state == MInvoke.SUBJECT) {
      $codeadd(Separator.DOT);

      var nameLocation = codeIndex;

      $codeadd(ByteCode.NOP1, 0);

      $parentvalset(3, nameLocation);

      $parentvalset(1, MInvoke.METHOD_NAME);
    }
  }

  private void methodInvocationQualified() {
    var proto = ByteProto.METHOD_INVOCATION_QUALIFIED;

    loop1Parent(proto);

    $parentpush(
      0, // 4 = NL
      NULL, // 3
      NULL, // 2 = comma slot
      MInvoke.SUBJECT, // 1 = state
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

      case 1 -> $codeadd(Separator.LESS_THAN_SIGN);

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
      case ByteProto.COMPILATION_UNIT -> $codeadd(Separator.SEMICOLON);

      case ByteProto.METHOD_DECLARATION -> $codeadd(Separator.SEMICOLON);
    }
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