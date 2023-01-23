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
import objectos.util.IntArrays;

class InternalCompiler extends InternalApi {

  @FunctionalInterface
  private interface ElementAction {
    void execute();
  }

  @FunctionalInterface
  private interface ItemAction {
    void execute(int proto);
  }

  private static final int _START = 0,
      _IDENTIFIER = 1,
      _KEYWORD = 2,
      _SEMICOLON = 3,

      _BODY = 4,
      _ENUM_CONSTANTS = 9,
      _INIT = 11,
      _NAME = 16;

  private static final int NULL = Integer.MIN_VALUE;

  private int constructorName;

  final void compile() {
    code = codeIndex = objectIndex = 0;

    constructorName = NULL;

    localIndex = -1;

    try {
      lastSet(_START);

      elemExecute(this::compilationUnit);
    } catch (RuntimeException e) {
      codeAdd(Whitespace.NEW_LINE);
      codeAdd(Whitespace.NEW_LINE);

      var collector = Collectors.joining(
        System.lineSeparator(),
        e.getMessage() + System.lineSeparator() + System.lineSeparator(),
        ""
      );

      var stackTrace = Stream.of(e.getStackTrace())
          .map(Object::toString)
          .collect(collector);

      codeAdd(ByteCode.RAW, object(stackTrace));

      e.printStackTrace();
    }

    codeAdd(ByteCode.EOF);
  }

  private void autoImports() {
    switch (last()) {
      default -> codeAdd(ByteCode.AUTO_IMPORTS0);

      case _SEMICOLON -> codeAdd(ByteCode.AUTO_IMPORTS1);
    }

    lastSet(_SEMICOLON);
  }

  @SuppressWarnings("unused")
  private void body() {
    codeAdd(Symbol.LEFT_CURLY_BRACKET);

    codeAdd(Indentation.ENTER_BLOCK);

    stackpush(constructorName /*1*/, _START);

    constructorName = NULL;

    int state = stackpop();
    stackpop(); // constructor name

    switch (state) {
      case _START -> {
        codeAdd(Whitespace.BEFORE_EMPTY_BLOCK_END);
        codeAdd(Indentation.EXIT_BLOCK);
        codeAdd(Symbol.RIGHT_CURLY_BRACKET);
      }

      case _ENUM_CONSTANTS, _INIT, _NAME -> {
        codeAdd(Symbol.SEMICOLON);
        codeAdd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeAdd(Indentation.EXIT_BLOCK);
        codeAdd(Indentation.EMIT);
        codeAdd(Symbol.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        codeAdd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeAdd(Indentation.EXIT_BLOCK);
        codeAdd(Indentation.EMIT);
        codeAdd(Symbol.RIGHT_CURLY_BRACKET);
      }

      default -> stubPop(ByteProto.BODY, state);
    }

    stackset(_BODY);
  }

  private void body(int proto) {
    codeAdd(Whitespace.OPTIONAL);

    codeAdd(Symbol.LEFT_CURLY_BRACKET);

    elemExecute(this::bodyAction);

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);
  }

  private void bodyAction() {}

  private void classDeclaration() {
    switch (last()) {
      case _SEMICOLON -> codeAdd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
    }

    itemExecute(this::classKeyword);

    if (elemHasNext(ByteProto.EXTENDS)) {
      itemExecute(this::extendsKeyword);

      itemExecute(this::classType);
    }

    itemExecute(this::body, ByteProto.BODY);
  }

  private void classKeyword(int proto) {
    codeAdd(Keyword.CLASS);

    codeAdd(Whitespace.MANDATORY);

    codeAdd(ByteCode.IDENTIFIER, protoNext());

    lastSet(_IDENTIFIER);
  }

  private void classType(int proto) {
    switch (last()) {
      case _KEYWORD -> codeAdd(Whitespace.MANDATORY);
    }

    var packageIndex = protoNext();

    var packageName = (String) objectget(packageIndex);

    autoImports.classTypePackageName(packageName);

    var count = protoNext();

    switch (count) {
      case 1 -> {
        var n1Index = protoNext();

        var n1 = (String) objectget(n1Index);

        autoImports.classTypeSimpleName(n1);

        int instruction = autoImports.classTypeInstruction();

        switch (instruction) {
          case 1 -> {
            codeAdd(ByteCode.IDENTIFIER, n1Index);
          }

          default -> {
            codeAdd(ByteCode.IDENTIFIER, packageIndex);
            codeAdd(Symbol.DOT);
            codeAdd(ByteCode.IDENTIFIER, n1Index);
          }
        }
      }

      case 2 -> {
        var n1Index = protoNext();
        var n2Index = protoNext();

        var n1 = (String) objectget(n1Index);
        var n2 = (String) objectget(n2Index);

        autoImports.classTypeSimpleName(n1);
        autoImports.classTypeSimpleName(n2);

        int instruction = autoImports.classTypeInstruction();

        switch (instruction) {
          case 1 -> {
            codeAdd(ByteCode.IDENTIFIER, n2Index);
          }

          case 2 -> {
            codeAdd(ByteCode.IDENTIFIER, n1Index);
            codeAdd(Symbol.DOT);
            codeAdd(ByteCode.IDENTIFIER, n2Index);
          }

          default -> {
            codeAdd(ByteCode.IDENTIFIER, packageIndex);
            codeAdd(Symbol.DOT);
            codeAdd(ByteCode.IDENTIFIER, n1Index);
            codeAdd(Symbol.DOT);
            codeAdd(ByteCode.IDENTIFIER, n2Index);
          }
        }
      }

      default -> {
        throw new UnsupportedOperationException(
          "Implement me :: count=" + count);
      }
    }

    lastSet(_IDENTIFIER);
  }

  private void codeAdd(Indentation value) { codeAdd(ByteCode.INDENTATION, value.ordinal()); }

  private void codeAdd(int v0) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 0);

    codeArray[codeIndex++] = v0;
  }

  private void codeAdd(int v0, int v1) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
  }

  private void codeAdd(Keyword value) { codeAdd(ByteCode.RESERVED_KEYWORD, value.ordinal()); }

  private void codeAdd(Symbol value) { codeAdd(ByteCode.SEPARATOR, value.ordinal()); }

  private void codeAdd(Whitespace value) { codeAdd(ByteCode.WHITESPACE, value.ordinal()); }

  private boolean compilationError() {
    var result = objectIndex == 1;

    objectIndex = 0;

    return result;
  }

  private void compilationErrorSet() { objectIndex = 1; }

  private void compilationUnit() {
    itemTry(this::packageDeclaration);

    itemTry(this::importDeclarationList);

    while (elemHasNext()) {
      typeDeclaration();
    }
  }

  private void elemExecute(ElementAction action) {
    if (elemStart()) {
      action.execute();

      stackpop(); // pop max
    }
  }

  private boolean elemHasNext() {
    if (!compilationError()) {
      int max = stackpeek(0);

      return protoIndex < max;
    } else {
      return false;
    }
  }

  private boolean elemHasNext(int condition) {
    if (elemHasNext()) {
      int location = protoPeek();

      int proto = protoAt(location);

      return proto == condition;
    } else {
      return false;
    }
  }

  private boolean elemStart() {
    int size = protoNext();

    if (size > 0) {
      int max = protoIndex + size;

      stackpush(max);

      return true;
    } else {
      return false;
    }
  }

  private void extendsKeyword(int proto) {
    switch (last()) {
      case _IDENTIFIER -> codeAdd(Whitespace.MANDATORY);
    }

    codeAdd(Keyword.EXTENDS);

    lastSet(_KEYWORD);
  }

  private void importDeclarationList(int proto) {
    switch (proto) {
      case ByteProto.AUTO_IMPORTS -> autoImports();

      default -> compilationErrorSet();
    }
  }

  private void itemExecute(ItemAction action) {
    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    int proto = protoNext();

    action.execute(proto);

    protoIndex = returnTo;
  }

  private void itemExecute(ItemAction action, int condition) {
    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    int proto = protoNext();

    if (proto == condition) {
      action.execute(proto);
    } else {
      compilationErrorSet();
    }

    protoIndex = returnTo;
  }

  private void itemTry(ItemAction action) {
    if (elemHasNext()) {
      int codeRollback = codeIndex;

      int protoRollback = protoIndex;

      itemExecute(action);

      if (compilationError()) {
        codeIndex = codeRollback;

        protoIndex = protoRollback;
      }
    }
  }

  private int last() { return code; }

  private void lastSet(int value) { code = value; }

  private Object objectget(int index) {
    return objectArray[index];
  }

  private void packageDeclaration(int proto) {
    switch (proto) {
      case ByteProto.PACKAGE -> packageKeyword();

      default -> compilationErrorSet();
    }
  }

  private void packageKeyword() {
    codeAdd(Keyword.PACKAGE);

    codeAdd(Whitespace.MANDATORY);

    codeAdd(ByteCode.IDENTIFIER, protoNext());

    codeAdd(Symbol.SEMICOLON);

    lastSet(_SEMICOLON);
  }

  private String protoName(int value) {
    return switch (value) {
      case ByteProto.ANNOTATION -> "Annotation";

      case ByteProto.ARRAY_ACCESS -> "Array Access";

      case ByteProto.ARRAY_INITIALIZER -> "Array Init.";

      case ByteProto.ARRAY_TYPE -> "Array Type";

      case ByteProto.ASSIGNMENT -> "Assignment";

      case ByteProto.AUTO_IMPORTS -> "Auto Imports";

      case ByteProto.BLOCK -> "Block";

      case ByteProto.BODY -> "Body";

      case ByteProto.CLASS -> "Class";

      case ByteProto.CLASS_INSTANCE_CREATION -> "Class Instance Creation";

      case ByteProto.CLASS_TYPE -> "Class Type";

      case ByteProto.COMPILATION_UNIT -> "Compilation Unit";

      case ByteProto.CONSTRUCTOR -> "Constructor";

      case ByteProto.ELLIPSIS -> "Ellipsis";

      case ByteProto.ENUM -> "Enum";

      case ByteProto.ENUM_CONSTANT -> "Enum Constant";

      case ByteProto.EXPRESSION_NAME -> "Expression Name";

      case ByteProto.EXPRESSION_NAME_CHAIN -> "Expression Name (Chain)";

      case ByteProto.EXTENDS -> "Extends";

      case ByteProto.FIELD_NAME -> "Field Name";

      case ByteProto.IDENTIFIER -> "Identifier";

      case ByteProto.IMPLEMENTS -> "Implements";

      case ByteProto.INTERFACE -> "Interface";

      case ByteProto.INVOKE -> "Invoke";

      case ByteProto.METHOD -> "Method";

      case ByteProto.MODIFIER -> "Modifier";

      case ByteProto.NEW_LINE -> "NL";

      case ByteProto.PACKAGE -> "Package";

      case ByteProto.PARAMETERIZED_TYPE -> "Parameterized Type";

      case ByteProto.PRIMITIVE_TYPE -> "Primitive Type";

      case ByteProto.RETURN -> "Return";

      case ByteProto.RETURN_STATEMENT -> "Return Stmt.";

      case ByteProto.PRIMITIVE_LITERAL -> "Primitive Literal";

      case ByteProto.STRING_LITERAL -> "String Literal";

      case ByteProto.SUPER -> "Super Keyword";

      case ByteProto.SUPER_INVOCATION -> "Super Invocation";

      case ByteProto.THIS -> "This";

      case ByteProto.TYPE_PARAMETER -> "Type Parameter";

      case ByteProto.TYPE_VARIABLE -> "Type Variable";

      case ByteProto.VAR -> "Var";

      case ByteProto.VOID -> "Void";

      default -> Integer.toString(value);
    };
  }

  private int protoNext() { return protoArray[protoIndex++]; }

  private int protoPeek() { return protoArray[protoIndex]; }

  private int stackpeek(int offset) { return localArray[localIndex - offset]; }

  private int stackpop() { return localArray[localIndex--]; }

  private void stackpush(int v0) {
    localArray = IntArrays.growIfNecessary(localArray, localIndex + 1);

    localArray[++localIndex] = v0;
  }

  private void stackpush(int v0, int v1) {
    localArray = IntArrays.growIfNecessary(localArray, localIndex + 2);

    localArray[++localIndex] = v0;
    localArray[++localIndex] = v1;
  }

  private void stackset(int value) { localArray[localIndex] = value; }

  private void stubPop(int ctx, int state) {
    warn("no-op pop @ '%s' (state=%d)".formatted(
      protoName(ctx), state));
  }

  private void typeDeclaration() {
    if (elemHasNext(ByteProto.CLASS)) {
      classDeclaration();
    } else {
      compilationErrorSet();
    }
  }

  private void warn(String msg) {
    codeAdd(Whitespace.NEW_LINE);
    codeAdd(ByteCode.COMMENT, object(msg));
  }

}