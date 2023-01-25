/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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

import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import objectos.util.IntArrays;

class InternalCompiler extends InternalApi {

  @FunctionalInterface
  private interface Action {
    void execute();
  }

  @FunctionalInterface
  private interface SwitchAction {
    void execute(int proto);
  }

  private static final int _START = 0,
      _ANNOTATION = 1,
      _IDENTIFIER = 2,
      _KEYWORD = 3,
      _SEMICOLON = 4;

  final void compile() {
    code = codeIndex = stackIndex = 0;

    try {
      compilationUnit();
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

  private void annotation() {
    codeAdd(Symbol.COMMERCIAL_AT);

    protoConsume(ByteProto.CLASS_TYPE);

    execute(this::classType);

    if (itemMore()) {
      codeAdd(Symbol.LEFT_PARENTHESIS);

      lastSet(_START);

      annotationValuePair();

      while (itemMore()) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        annotationValuePair();
      }

      codeAdd(Symbol.RIGHT_PARENTHESIS);
    }

    lastSet(_ANNOTATION);
  }

  private void annotationValuePair() {
    // check for value name

    expression();
  }

  private void arrayDimension() {
    if (itemIs(ByteProto.ARRAY_DIMENSION)) {
      execute(this::arrayDimensionAction);
    } else {
      errorRaise("invalid array dimension");
    }
  }

  private void arrayDimensionAction() {
    codeAdd(Symbol.LEFT_SQUARE_BRACKET);
    codeAdd(Symbol.RIGHT_SQUARE_BRACKET);
  }

  private void arrayType() {
    int item = itemNext();

    switch (item) {
      case ByteProto.CLASS_TYPE -> execute(this::classType);

      default -> errorRaise("'%s' invalid array type".formatted(protoName(item)));
    }

    while (itemMore()) {
      arrayDimension();
    }
  }

  private void autoImports() {
    switch (last()) {
      default -> codeAdd(ByteCode.AUTO_IMPORTS0);

      case _SEMICOLON -> codeAdd(ByteCode.AUTO_IMPORTS1);
    }

    lastSet(_SEMICOLON);
  }

  private void body() {
    codeAdd(Symbol.LEFT_CURLY_BRACKET);

    if (itemMore()) {
      codeAdd(Indentation.ENTER_BLOCK);
      codeAdd(Whitespace.BEFORE_FIRST_MEMBER);

      bodyMember();

      while (itemMore()) {
        codeAdd(Whitespace.BEFORE_NEXT_MEMBER);

        bodyMember();
      }

      codeAdd(Indentation.EXIT_BLOCK);
      codeAdd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
    } else {
      codeAdd(Whitespace.BEFORE_EMPTY_BLOCK_END);
    }

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);
  }

  private void bodyMember() {
    lastSet(_START);

    modifierList();

    if (itemTest(ByteProto::isType)) {
      if (lastNot(_START)) {
        codeAdd(Whitespace.MANDATORY);
      }

      fieldOrMethodDeclaration();
    } else {
      errorSet();
    }
  }

  private void classDeclaration() {
    execute(this::classKeyword);

    if (itemIs(ByteProto.EXTENDS)) {
      classDeclarationExtends();
    }

    if (itemIs(ByteProto.BODY)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::body);
    }
  }

  private void classDeclarationExtends() {
    codeAdd(Whitespace.MANDATORY);

    execute(this::extendsKeyword);

    if (itemIs(ByteProto.CLASS_TYPE)) {
      codeAdd(Whitespace.MANDATORY);

      execute(this::classType);
    } else {
      error();
    }
  }

  private void classKeyword() {
    codeAdd(Keyword.CLASS);

    codeAdd(Whitespace.MANDATORY);

    codeAdd(ByteCode.IDENTIFIER, protoNext());

    lastSet(_IDENTIFIER);
  }

  private void classType() {
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

  private void compilationUnit() {
    lastSet(_START);

    int item = itemPeek();

    switch (item) {
      case ByteProto.ANNOTATION -> {
        declarationAnnotationList();

        if (itemIs(ByteProto.PACKAGE)) {
          ordinaryCompilationUnit();
        } else {
          topLevelDeclarationList();
        }
      }

      case ByteProto.AUTO_IMPORTS -> {
        protoConsume();

        execute(this::autoImports);

        importDeclarationList();

        topLevelDeclarationList();
      }

      case ByteProto.CLASS -> topLevelDeclarationList();

      case ByteProto.END_ELEMENT -> {}

      case ByteProto.PACKAGE -> { protoConsume(); ordinaryCompilationUnit(); }

      default -> System.err.println(
        "compilationUnit: no-op proto '%s'".formatted(protoName(item)));
    }
  }

  private void consumeWs() {
    // stub impl. for now...
  }

  private void declarationAnnotationList() {
    if (itemIs(ByteProto.ANNOTATION)) {
      execute(this::annotation);

      while (itemIs(ByteProto.ANNOTATION)) {
        codeAdd(Whitespace.AFTER_ANNOTATION);

        execute(this::annotation);
      }
    }
  }

  private boolean error() {
    var result = stackIndex == 1;

    stackIndex = 0;

    return result;
  }

  private void errorRaise(String message) {
    errorSet();

    codeAdd(ByteCode.COMMENT, object(message));
  }

  private void errorSet() { stackIndex = 1; }

  private void execute(Action action) {
    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    action.execute();

    protoIndex = returnTo;
  }

  private int executeSwitch(SwitchAction action) {
    int proto = protoNext();

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    action.execute(proto);

    protoIndex = returnTo;

    return proto;
  }

  private void expression() {
    int part = executeSwitch(this::expressionBegin);

    expressionDot(part);
  }

  private void expressionBegin(int proto) {
    switch (proto) {
      case ByteProto.INVOKE -> invoke();

      case ByteProto.STRING_LITERAL -> stringLiteral();

      default -> errorRaise(
        "no-op expression part '%s'".formatted(protoName(proto))
      );
    }
  }

  private void expressionDot(int previous) {
    switch (previous) {
      case ByteProto.INVOKE -> {
        if (itemTest(ByteProto::primaryDot)) {
          codeAdd(Symbol.DOT);
          expression();
        }
      }
    }
  }

  private void extendsKeyword() {
    codeAdd(Keyword.EXTENDS);

    lastSet(_KEYWORD);
  }

  private void fieldDeclarationVariableList() {
    variableDeclarator();

    while (itemIs(ByteProto.IDENTIFIER)) {
      codeAdd(Symbol.COMMA);
      codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

      variableDeclarator();
    }

    codeAdd(Symbol.SEMICOLON);

    lastSet(_SEMICOLON);
  }

  private void fieldOrMethodDeclaration() {
    executeSwitch(this::type);

    if (itemIs(ByteProto.IDENTIFIER)) {
      codeAdd(Whitespace.MANDATORY);

      fieldDeclarationVariableList();
    }
  }

  private void identifier() {
    codeAdd(ByteCode.IDENTIFIER, protoNext());
  }

  private void importDeclarationList() {
    if (itemIs(ByteProto.AUTO_IMPORTS)) {
      execute(this::autoImports);
    }
  }

  private void invoke() {
    protoConsume(ByteProto.IDENTIFIER);

    execute(this::identifier);

    codeAdd(Symbol.LEFT_PARENTHESIS);

    if (itemTest(ByteProto::isExpressionStart)) {
      expression();

      while (itemTest(ByteProto::isExpressionStart)) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        expression();
      }
    }

    codeAdd(Symbol.RIGHT_PARENTHESIS);
  }

  private boolean itemIs(int condition) {
    consumeWs();

    int proto = protoPeek();

    if (proto == condition) {
      protoIndex++;

      return true;
    } else {
      return false;
    }
  }

  private boolean itemMore() {
    if (!error()) {
      consumeWs();

      return protoPeek() != ByteProto.END_ELEMENT;
    } else {
      return false;
    }
  }

  private int itemNext() {
    consumeWs();

    return protoNext();
  }

  private int itemPeek() {
    consumeWs();
    return protoPeek();
  }

  private boolean itemTest(IntPredicate predicate) {
    consumeWs();

    int proto = protoPeek();

    return predicate.test(proto);
  }

  private int last() { return code; }

  private boolean lastIs(int value) { return last() == value; }

  private boolean lastNot(int value) { return last() != value; }

  private void lastSet(int value) { code = value; }

  private void modifier() {
    codeAdd(ByteCode.RESERVED_KEYWORD, protoNext());

    lastSet(_KEYWORD);
  }

  private void modifierList() {
    if (itemIs(ByteProto.MODIFIER)) {
      if (lastIs(_ANNOTATION)) {
        codeAdd(Whitespace.AFTER_ANNOTATION);
      }

      execute(this::modifier);

      while (itemIs(ByteProto.MODIFIER)) {
        codeAdd(Whitespace.MANDATORY);

        execute(this::modifier);
      }
    }
  }

  private Object objectget(int index) {
    return objectArray[index];
  }

  private void ordinaryCompilationUnit() {
    execute(this::packageKeyword);

    importDeclarationList();

    topLevelDeclarationList();
  }

  private void packageKeyword() {
    codeAdd(Keyword.PACKAGE);

    codeAdd(Whitespace.MANDATORY);

    codeAdd(ByteCode.IDENTIFIER, protoNext());

    codeAdd(Symbol.SEMICOLON);

    lastSet(_SEMICOLON);
  }

  private void parameterizedType() {
    protoConsume(ByteProto.CLASS_TYPE);

    execute(this::classType);

    codeAdd(Symbol.LEFT_ANGLE_BRACKET);

    if (itemMore()) {
      executeSwitch(this::type);

      while (itemMore()) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        executeSwitch(this::type);
      }
    }

    codeAdd(Symbol.RIGHT_ANGLE_BRACKET);
  }

  private void protoConsume() { protoIndex++; }

  private void protoConsume(int expected) {
    int proto = protoNext();

    assert proto == expected;
  }

  private String protoName(int proto) {
    return switch (proto) {
      case ByteProto.ARRAY_TYPE -> "Array Type";

      case ByteProto.CLASS -> "Class Keyword";

      case ByteProto.INVOKE -> "Invoke";

      default -> Integer.toString(proto);
    };
  }

  private int protoNext() { return protoArray[protoIndex++]; }

  private int protoPeek() { return protoArray[protoIndex]; }

  private void stringLiteral() {
    codeAdd(ByteCode.STRING_LITERAL, protoNext());
  }

  private void topLevelDeclarationList() {
    if (itemMore()) {
      switch (last()) {
        case _ANNOTATION,
             _START -> {}

        default -> codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
      }

      typeDeclaration();

      while (itemMore()) {
        codeAdd(Whitespace.BEFORE_NEXT_MEMBER);

        typeDeclaration();
      }
    }
  }

  private void type(int proto) {
    switch (proto) {
      case ByteProto.ARRAY_TYPE -> arrayType();

      case ByteProto.CLASS_TYPE -> classType();

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType();

      default -> errorRaise(
        "no-op type '%s'".formatted(protoName(proto))
      );
    }
  }

  private void typeDeclaration() {
    declarationAnnotationList();

    modifierList();

    if (itemIs(ByteProto.CLASS)) {
      switch (last()) {
        case _ANNOTATION -> codeAdd(Whitespace.AFTER_ANNOTATION);

        case _KEYWORD -> codeAdd(Whitespace.MANDATORY);
      }

      classDeclaration();
    } else {
      errorSet();
    }
  }

  private void variableDeclarator() {
    execute(this::identifier);

    if (itemTest(ByteProto::isExpressionStart)) {
      codeAdd(Whitespace.OPTIONAL);
      codeAdd(Symbol.ASSIGNMENT);
      codeAdd(Whitespace.OPTIONAL);

      expression();
    }
  }

}