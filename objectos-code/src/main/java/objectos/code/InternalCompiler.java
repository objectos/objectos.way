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

  private static final int NULL = Integer.MIN_VALUE;

  private static final int _START = 0,
      _ANNOTATION = 1,
      _IDENTIFIER = 2,
      _KEYWORD = 3,
      _NEW_LINE = 4,
      _SEMICOLON = 5;

  final void compile() {
    codeIndex = level = stackIndex = 0;

    // simpleName
    stackArray[0] = NULL;
    // public found
    stackArray[1] = NULL;
    // comma slot
    stackArray[2] = NULL;

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

  private void argumentList() {
    codeAdd(Symbol.LEFT_PARENTHESIS);
    codeAdd(Indentation.ENTER_PARENTHESIS);

    if (itemTest(ByteProto::isExpressionStart)) {
      if (lastIs(_NEW_LINE)) {
        codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);
      }

      lastSet(_START);

      expression();

      while (itemTest(ByteProto::isExpressionStart)) {
        slotComma();

        var ws = lastIs(_NEW_LINE)
            ? Whitespace.BEFORE_FIRST_LINE_CONTENT
            : Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM;

        codeAdd(ws);

        lastSet(_START);

        expression();
      }
    }

    codeAdd(Indentation.EXIT_PARENTHESIS);

    if (lastIs(_NEW_LINE)) {
      codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);
    }

    codeAdd(Symbol.RIGHT_PARENTHESIS);
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

  private void arrayInitializer() {
    codeAdd(Symbol.LEFT_CURLY_BRACKET);

    if (itemMore()) {
      variableInitializer();

      while (itemMore()) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        variableInitializer();
      }
    }

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);
  }

  private void arrayType() {
    int item = itemPeek();

    switch (item) {
      case ByteProto.CLASS_TYPE -> execute(this::classType);

      case ByteProto.PRIMITIVE_TYPE -> execute(this::primitiveType);

      default -> errorRaise("'%s' invalid array type".formatted(protoName(item)));
    }

    while (itemMore()) {
      arrayDimension();
    }
  }

  private void assigmentOperator() {
    codeAdd(ByteCode.OPERATOR, protoNext());
  }

  private void autoImports() {
    switch (last()) {
      default -> codeAdd(ByteCode.AUTO_IMPORTS0);

      case _SEMICOLON -> codeAdd(ByteCode.AUTO_IMPORTS1);
    }

    lastSet(_SEMICOLON);
  }

  private void block() {
    codeAdd(Symbol.LEFT_CURLY_BRACKET);

    if (itemMore()) {
      codeAdd(Indentation.ENTER_BLOCK);
      codeAdd(Whitespace.BEFORE_NEXT_STATEMENT);

      blockStatement();

      while (itemMore()) {
        codeAdd(Whitespace.BEFORE_NEXT_STATEMENT);

        blockStatement();
      }

      codeAdd(Indentation.EXIT_BLOCK);
      codeAdd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
    } else {
      codeAdd(Whitespace.BEFORE_EMPTY_BLOCK_END);
    }

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);
  }

  private void blockStatement() {
    int start = itemPeek();
    // TODO local class
    // TODO local variable decl

    statement0(start);
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

    lastSet(_START);
  }

  private void bodyMember() {
    lastSet(_START);

    declarationAnnotationList();

    modifierList();

    int item = itemPeek();

    switch (last()) {
      case _ANNOTATION -> codeAdd(Whitespace.AFTER_ANNOTATION);

      case _START -> {}

      default -> codeAdd(Whitespace.MANDATORY);
    }

    switch (item) {
      case ByteProto.ARRAY_TYPE,
           ByteProto.CLASS_TYPE,
           ByteProto.PARAMETERIZED_TYPE,
           ByteProto.PRIMITIVE_TYPE,
           ByteProto.TYPE_VARIABLE -> {
        executeSwitch(this::type);

        fieldOrMethodDeclaration();
      }

      case ByteProto.CLASS -> classDeclaration();

      case ByteProto.CONSTRUCTOR -> constructorDeclaration();

      case ByteProto.TYPE_PARAMETER -> {
        typeParameterList();

        methodDeclarationAfterTypeParameterList();
      }

      case ByteProto.VOID -> {
        execute(this::voidKeyword);

        if (itemIs(ByteProto.METHOD)) {
          codeAdd(Whitespace.MANDATORY);

          methodDeclaration();
        } else {
          errorRaise(
            "method declarator not found"
          );
        }
      }

      default -> errorRaise(
        "invalid or no-op body member '%s'".formatted(protoName(item))
      );
    }
  }

  private void classDeclaration() {
    execute(this::classKeyword);

    if (itemIs(ByteProto.EXTENDS)) {
      classDeclarationExtends();
    }

    if (itemIs(ByteProto.IMPLEMENTS)) {
      implementsClause();
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

  private void classInstanceCreation() {
    codeAdd(Keyword.NEW);

    codeAdd(Whitespace.MANDATORY);

    executeSwitch(this::type);

    argumentList();
  }

  private void classKeyword() {
    codeAdd(Keyword.CLASS);

    codeAdd(Whitespace.MANDATORY);

    int proto = protoNext();

    codeAdd(ByteCode.IDENTIFIER, proto);

    simpleName(proto);

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
        execute(this::autoImports);

        importDeclarationList();

        topLevelDeclarationList();
      }

      case ByteProto.CLASS,
           ByteProto.MODIFIER -> topLevelDeclarationList();

      case ByteProto.END_ELEMENT -> {}

      case ByteProto.PACKAGE -> ordinaryCompilationUnit();

      default -> errorRaise(
        "compilationUnit: no-op proto '%s'".formatted(protoName(item))
      );
    }
  }

  private void constructorDeclaration() {
    execute(this::constructorDeclarator);

    if (itemIs(ByteProto.BLOCK)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::block);
    } else {
      errorRaise("Constructor without a block() declaration");
    }
  }

  private void constructorDeclarator() {
    int name = simpleName();

    if (name == NULL) {
      name = object("Constructor");
    }

    codeAdd(ByteCode.IDENTIFIER, name);

    formalParameterList();
  }

  private void consumeWs() {
    while (protoPeek() == ByteProto.NEW_LINE) {
      execute(this::newLine);
    }
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

  private void ellipsis() {
    codeAdd(Symbol.ELLIPSIS);
  }

  private boolean error() {
    var result = stackIndex == 1;

    stackIndex = 0;

    return result;
  }

  private void errorRaise() { stackIndex = 1; }

  private void errorRaise(String message) {
    errorRaise();

    codeAdd(ByteCode.COMMENT, object(message));
  }

  private int execute(Action action) {
    int proto = protoNext();

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    action.execute();

    protoIndex = returnTo;

    return proto;
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

    slot();

    if (stop()) {
      return;
    }

    expressionNext(part);

    slot();

    if (stop()) {
      return;
    }

    while (itemIs(ByteProto.ASSIGNMENT_OPERATOR)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::assigmentOperator);

      if (itemTest(ByteProto::isExpressionStart)) {
        codeAdd(Whitespace.OPTIONAL);

        expression();
      } else {
        errorRaise("expected expression after assigment operator");
      }
    }
  }

  private void expressionBegin(int proto) {
    switch (proto) {
      case ByteProto.CLASS_INSTANCE_CREATION -> classInstanceCreation();

      case ByteProto.CLASS_TYPE -> classType();

      case ByteProto.EXPRESSION_NAME -> expressionName();

      case ByteProto.INVOKE -> invoke();

      case ByteProto.PRIMITIVE_LITERAL -> primitiveLiteral();

      case ByteProto.STRING_LITERAL -> stringLiteral();

      case ByteProto.THIS -> thisKeyword();

      default -> errorRaise(
        "no-op expression part '%s'".formatted(protoName(proto))
      );
    }
  }

  private void expressionNext(int previous) {
    switch (previous) {
      case ByteProto.CLASS_INSTANCE_CREATION,
           ByteProto.CLASS_TYPE,
           ByteProto.EXPRESSION_NAME,
           ByteProto.INVOKE,
           ByteProto.STRING_LITERAL,
           ByteProto.THIS -> {
        int next = itemPeek();

        switch (next) {
          case ByteProto.EXPRESSION_NAME,
               ByteProto.INVOKE -> {
            if (lastIs(_NEW_LINE)) {
              codeAdd(Indentation.CONTINUATION);

              lastSet(_START);
            }

            codeAdd(Symbol.DOT);

            expression();
          }

          case ByteProto.ARRAY_ACCESS -> {
            execute(this::arrayAccess);

            while (itemIs(ByteProto.ARRAY_ACCESS)) {
              execute(this::arrayAccess);
            }
          }
        }
      }
    }
  }

  private void arrayAccess() {
    codeAdd(Symbol.LEFT_SQUARE_BRACKET);

    expression();

    codeAdd(Symbol.RIGHT_SQUARE_BRACKET);
  }

  private void expressionName() {
    codeAdd(ByteCode.IDENTIFIER, protoNext());
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
    int item = itemPeek();

    switch (item) {
      case ByteProto.IDENTIFIER -> {
        codeAdd(Whitespace.MANDATORY);

        fieldDeclarationVariableList();
      }

      case ByteProto.METHOD -> {
        codeAdd(Whitespace.MANDATORY);

        methodDeclaration();
      }

      default -> errorRaise(
        "found '%s' in field or method".formatted(protoName(item))
      );
    }
  }

  private void formalParameter() {
    if (itemTest(ByteProto::isType)) {
      executeSwitch(this::type);
    } else {
      errorRaise("invalid formal parameter");

      return;
    }

    if (itemIs(ByteProto.ELLIPSIS)) {
      execute(this::ellipsis);
    }

    if (itemIs(ByteProto.IDENTIFIER)) {
      codeAdd(Whitespace.MANDATORY);

      execute(this::identifier);
    } else {
      errorRaise("invalid formal parameter");

      return;
    }
  }

  private void formalParameterList() {
    codeAdd(Symbol.LEFT_PARENTHESIS);

    if (itemMore()) {
      formalParameter();

      while (itemMore()) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        formalParameter();
      }
    }

    codeAdd(Symbol.RIGHT_PARENTHESIS);
  }

  private void identifier() {
    codeAdd(ByteCode.IDENTIFIER, protoNext());
  }

  private void implementsClause() {
    codeAdd(Whitespace.MANDATORY);

    execute(this::implementsKeyword);

    lastSet(_KEYWORD);

    if (itemTest(ByteProto::isClassOrParameterizedType)) {
      codeAdd(Whitespace.MANDATORY);

      executeSwitch(this::type);

      while (itemTest(ByteProto::isClassOrParameterizedType)) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        executeSwitch(this::type);
      }
    }
  }

  private void implementsKeyword() {
    codeAdd(Keyword.IMPLEMENTS);
  }

  private void importDeclarationList() {
    if (itemIs(ByteProto.AUTO_IMPORTS)) {
      execute(this::autoImports);
    }
  }

  private void invoke() {
    execute(this::identifier);

    argumentList();
  }

  private boolean itemIs(int condition) {
    if (!error()) {
      consumeWs();

      return protoPeek() == condition;
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

  private int itemPeek() {
    consumeWs();

    return protoPeek();
  }

  private int itemPeekAhead() {
    for (int i = protoIndex + 2; i < protoArray.length; i += 2) {
      int proto = protoArray[i];

      if (ByteProto.isWhitespace(proto)) {
        continue;
      }

      return proto;
    }

    return ByteProto.NOOP;
  }

  private boolean itemTest(IntPredicate predicate) {
    consumeWs();

    int proto = protoPeek();

    return predicate.test(proto);
  }

  private int last() { return level; }

  private boolean lastIs(int value) { return last() == value; }

  private void lastSet(int value) { level = value; }

  private void localVariableDeclaration() {
    int type = itemPeek();

    if (ByteProto.isType(type)) {
      executeSwitch(this::type);
    } else if (type == ByteProto.VAR) {
      execute(this::varKeyword);
    } else {
      errorRaise(
        "invalid local var: expected var or type but found '%s'"
            .formatted(protoName(type))
      );
    }

    if (itemIs(ByteProto.IDENTIFIER)) {
      codeAdd(Whitespace.MANDATORY);

      variableDeclarator();

      while (itemIs(ByteProto.IDENTIFIER)) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        variableDeclarator();
      }
    } else {
      errorRaise("invalid local var: variable name not found");
    }
  }

  private void varKeyword() {
    codeAdd(Keyword.VAR);
  }

  private void methodDeclaration() {
    execute(this::methodDeclarator);

    if (itemIs(ByteProto.BLOCK)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::block);
    } else {
      // assume abstract
      codeAdd(Symbol.SEMICOLON);

      lastSet(_SEMICOLON);
    }
  }

  private void methodDeclarationAfterTypeParameterList() {
    int returnType = itemPeek();

    if (ByteProto.isType(returnType)) {
      codeAdd(Whitespace.MANDATORY);

      executeSwitch(this::type);
    } else if (returnType == ByteProto.VOID) {
      codeAdd(Whitespace.MANDATORY);

      execute(this::voidKeyword);
    } else {
      errorRaise(
        "Method declaration: expected 'Return Type' but found '%s'".formatted(protoName(returnType))
      );

      return;
    }

    if (itemIs(ByteProto.METHOD)) {
      codeAdd(Whitespace.MANDATORY);

      methodDeclaration();
    } else {
      int next = itemPeek();

      errorRaise(
        "Method declaration: expected 'Declarator' but found '%s'".formatted(protoName(next))
      );

      return;
    }
  }

  private void methodDeclarator() {
    execute(this::identifier);

    formalParameterList();
  }

  private void modifier() {
    int proto = protoNext();

    if (proto == Keyword.PUBLIC.ordinal()) {
      publicFound(1);
    }

    codeAdd(ByteCode.RESERVED_KEYWORD, proto);

    lastSet(_KEYWORD);
  }

  private void modifierList() {
    publicFound(NULL);

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

  private void newLine() {
    codeAdd(Whitespace.NEW_LINE);

    lastSet(_NEW_LINE);
  }

  private void noop() {}

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

  private void primitiveLiteral() {
    codeAdd(ByteCode.PRIMITIVE_LITERAL, protoNext());
  }

  private void primitiveType() {
    codeAdd(ByteCode.RESERVED_KEYWORD, protoNext());
  }

  private String protoName(int proto) {
    return switch (proto) {
      case ByteProto.ARRAY_TYPE -> "Array Type";

      case ByteProto.CLASS -> "Class Keyword";

      case ByteProto.INVOKE -> "Invoke";

      case ByteProto.MODIFIER -> "Modifier";

      case ByteProto.PRIMITIVE_LITERAL -> "Primitive Literal";

      case ByteProto.TYPE_PARAMETER -> "Type Parameter";

      default -> Integer.toString(proto);
    };
  }

  private int protoNext() { return protoArray[protoIndex++]; }

  private int protoPeek() { return protoArray[protoIndex]; }

  private int publicFound() { return stackArray[1]; }

  private void publicFound(int value) { stackArray[1] = value; }

  private void returnKeyword() {
    codeAdd(Keyword.RETURN);
  }

  private void returnStatement() {
    execute(this::returnKeyword);

    codeAdd(Whitespace.MANDATORY);

    expression();
  }

  private int simpleName() { return stackArray[0]; }

  private void simpleName(int value) { stackArray[0] = value; }

  private void slot() {
    stackArray[2] = codeIndex;

    codeAdd(ByteCode.NOP1);

    codeAdd(-1);
  }

  private void slotComma() {
    int index = stackArray[2];

    codeArray[index + 0] = ByteCode.SEPARATOR;

    codeArray[index + 1] = Symbol.COMMA.ordinal();
  }

  @SuppressWarnings("unused")
  private void statement() {
    int start = itemPeek();

    statement0(start);
  }

  private void statement0(int start) {
    switch (start) {
      case ByteProto.ARRAY_TYPE,
           ByteProto.PARAMETERIZED_TYPE,
           ByteProto.PRIMITIVE_TYPE,
           ByteProto.TYPE_VARIABLE -> localVariableDeclaration();

      case ByteProto.CLASS_INSTANCE_CREATION,
           ByteProto.EXPRESSION_NAME,
           ByteProto.INVOKE,
           ByteProto.THIS -> statementPrimary();

      case ByteProto.CLASS_TYPE -> {
        int next = itemPeekAhead();

        if (next != ByteProto.IDENTIFIER) {
          statementPrimary();
        } else {
          localVariableDeclaration();
        }
      }

      case ByteProto.RETURN -> returnStatement();

      case ByteProto.SUPER -> superInvocationWithKeyword();

      case ByteProto.SUPER_INVOCATION -> superInvocation();

      case ByteProto.VAR -> localVariableDeclaration();

      default -> errorRaise(
        "no-op statement start '%s'".formatted(protoName(start))
      );
    }

    codeAdd(Symbol.SEMICOLON);
  }

  private void statementPrimary() {
    expression();
  }

  private boolean stop() {
    if (itemIs(ByteProto.STOP)) {
      execute(this::noop);

      while (itemIs(ByteProto.STOP)) {
        execute(this::noop);
      }

      return true;
    } else {
      return false;
    }
  }

  private void stringLiteral() {
    codeAdd(ByteCode.STRING_LITERAL, protoNext());
  }

  private void superInvocation() {
    superKeyword();

    execute(this::argumentList);
  }

  private void superInvocationWithKeyword() {
    execute(this::superKeyword);

    codeAdd(Symbol.LEFT_PARENTHESIS);

    codeAdd(Symbol.RIGHT_PARENTHESIS);
  }

  private void superKeyword() {
    codeAdd(Keyword.SUPER);
  }

  private void thisKeyword() {
    codeAdd(Keyword.THIS);
  }

  private void topLevelDeclaration() {
    declarationAnnotationList();

    modifierList();

    if (itemIs(ByteProto.CLASS)) {
      switch (last()) {
        case _ANNOTATION -> codeAdd(Whitespace.AFTER_ANNOTATION);

        case _KEYWORD -> codeAdd(Whitespace.MANDATORY);
      }

      classDeclaration();
    } else {
      errorRaise();
    }

    var publicFound = publicFound() != NULL;

    var simpleNameIndex = simpleName();

    var simpleName = (String) objectget(simpleNameIndex);

    autoImports.fileName(publicFound, simpleName);
  }

  private void topLevelDeclarationList() {
    simpleName(NULL);

    if (itemMore()) {
      switch (last()) {
        case _ANNOTATION,
             _START -> {}

        default -> codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
      }

      topLevelDeclaration();

      while (itemMore()) {
        codeAdd(Whitespace.BEFORE_NEXT_MEMBER);

        topLevelDeclaration();
      }
    }
  }

  private void type(int proto) {
    switch (proto) {
      case ByteProto.ARRAY_TYPE -> arrayType();

      case ByteProto.CLASS_TYPE -> classType();

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType();

      case ByteProto.PRIMITIVE_TYPE -> primitiveType();

      case ByteProto.TYPE_VARIABLE -> typeVariable();

      default -> errorRaise(
        "no-op type '%s'".formatted(protoName(proto))
      );
    }
  }

  private void typeParameter() {
    execute(this::identifier);

    if (itemMore()) {
      codeAdd(Whitespace.MANDATORY);
      codeAdd(Keyword.EXTENDS);
      codeAdd(Whitespace.MANDATORY);

      executeSwitch(this::type);

      while (itemMore()) {
        codeAdd(Whitespace.OPTIONAL);
        codeAdd(Symbol.AMPERSAND);
        codeAdd(Whitespace.OPTIONAL);

        executeSwitch(this::type);
      }
    }
  }

  private void typeParameterList() {
    codeAdd(Symbol.LEFT_ANGLE_BRACKET);

    execute(this::typeParameter);

    while (itemIs(ByteProto.TYPE_PARAMETER)) {
      codeAdd(Symbol.COMMA);
      codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

      execute(this::typeParameter);
    }

    codeAdd(Symbol.RIGHT_ANGLE_BRACKET);
  }

  private void typeVariable() {
    codeAdd(ByteCode.IDENTIFIER, protoNext());
  }

  private void variableDeclarator() {
    execute(this::identifier);

    if (itemTest(ByteProto::isVariableInitializer)) {
      codeAdd(Whitespace.OPTIONAL);
      codeAdd(Symbol.ASSIGNMENT);
      codeAdd(Whitespace.OPTIONAL);

      variableInitializer();
    }
  }

  private void variableInitializer() {
    if (itemTest(ByteProto::isExpressionStart)) {
      expression();
    } else if (itemIs(ByteProto.ARRAY_INITIALIZER)) {
      execute(this::arrayInitializer);
    } else {
      errorRaise();
    }
  }

  private void voidKeyword() {
    codeAdd(Keyword.VOID);
  }

}