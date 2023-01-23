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
      _ANNOTATION = 1,
      _IDENTIFIER = 2,
      _KEYWORD = 3,
      _RIGHT_CURLY_BRACKET = 4,
      _SEMICOLON = 5;

  final void compile() {
    code = codeIndex = objectIndex = 0;

    stackIndex = -1;

    try {
      lastSet(_START);

      elemExecute(this::compilationUnit, ByteProto.COMPILATION_UNIT);
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

  private void annotation(int proto) {
    switch (last()) {
      case _SEMICOLON -> codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
    }

    elemExecute(this::annotationAction, proto);

    lastSet(_ANNOTATION);
  }

  private void annotationAction() {
    codeAdd(Symbol.COMMERCIAL_AT);

    itemExecute(this::classType);
  }

  private void autoImports() {
    switch (last()) {
      default -> codeAdd(ByteCode.AUTO_IMPORTS0);

      case _SEMICOLON -> codeAdd(ByteCode.AUTO_IMPORTS1);
    }

    lastSet(_SEMICOLON);
  }

  private void body(int proto) {
    codeAdd(Whitespace.OPTIONAL);

    codeAdd(Symbol.LEFT_CURLY_BRACKET);

    elemExecute(this::bodyAction, ByteProto.BODY);

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);

    lastSet(_RIGHT_CURLY_BRACKET);
  }

  private void bodyAction() {}

  private void classDeclaration() {
    switch (last()) {
      case _ANNOTATION -> {
        codeAdd(Whitespace.AFTER_ANNOTATION);
        codeAdd(Indentation.EMIT);
      }

      case _KEYWORD -> codeAdd(Whitespace.MANDATORY);

      case _RIGHT_CURLY_BRACKET,
           _SEMICOLON -> {
        codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
        codeAdd(Indentation.EMIT);
      }
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

  private void elemExecute(ElementAction action, int self) {
    stackpush(self);

    if (elemStart()) {
      action.execute();

      stackpop(); // pop max
    }

    stackpop(); // self
  }

  private boolean elemHasNext() {
    if (!compilationError()) {
      int max = stackPeek(0);

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

  private void modifier(int proto) {
    switch (last()) {
      case _SEMICOLON -> {
        codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
        codeAdd(Indentation.EMIT);
      }
    }

    codeAdd(ByteCode.RESERVED_KEYWORD, protoNext());

    lastSet(_KEYWORD);
  }

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

  private int protoNext() { return protoArray[protoIndex++]; }

  private int protoPeek() { return protoArray[protoIndex]; }

  private int stackPeek(int offset) { return stackArray[stackIndex - offset]; }

  private int stackpop() { return stackArray[stackIndex--]; }

  private void stackpush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);

    stackArray[++stackIndex] = v0;
  }

  private void typeDeclaration() {
    while (elemHasNext(ByteProto.ANNOTATION)) {
      itemExecute(this::annotation);
    }

    while (elemHasNext(ByteProto.MODIFIER)) {
      itemExecute(this::modifier);
    }

    if (elemHasNext(ByteProto.CLASS)) {
      classDeclaration();
    } else {
      compilationErrorSet();
    }
  }

}