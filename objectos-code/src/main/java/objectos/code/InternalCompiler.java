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
      _ENUM_CONSTANT = 2,
      _IDENTIFIER = 3,
      _KEYWORD = 4,
      _NEW_LINE = 5,
      _SEMICOLON = 6;

  final void compile() {
    codeIndex = level = stackIndex = 0;

    // simpleName
    stackArray[0] = NULL;
    // public found
    stackArray[1] = NULL;
    // comma slot
    stackArray[2] = NULL;
    // topLevel
    stackArray[3] = NULL;

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

    if (itemTest(this::isArgumentStart)) {
      if (lastIs(_NEW_LINE)) {
        codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);
      }

      lastSet(_START);

      expression();

      while (itemTest(this::isArgumentStart)) {
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

  private void arrayAccess() {
    codeAdd(Symbol.LEFT_SQUARE_BRACKET);

    expression();

    codeAdd(Symbol.RIGHT_SQUARE_BRACKET);
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
    codeAdd(Indentation.ENTER_BLOCK);

    if (itemMore()) {
      if (lastIs(_NEW_LINE)) {
        codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);
      }

      variableInitializer();

      while (itemMore()) {
        slotComma();

        var ws = lastIs(_NEW_LINE)
            ? Whitespace.BEFORE_FIRST_LINE_CONTENT
            : Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM;

        codeAdd(ws);

        variableInitializer();
      }
    }

    codeAdd(Indentation.EXIT_BLOCK);

    if (lastIs(_NEW_LINE)) {
      codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);
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

  private void autoImports() {
    switch (last()) {
      default -> codeAdd(ByteCode.AUTO_IMPORTS0);

      case _SEMICOLON -> codeAdd(ByteCode.AUTO_IMPORTS1);
    }

    lastSet(_SEMICOLON);
  }

  private void beforeTopLevelTypeDeclaration() {
    switch (last()) {
      case _ANNOTATION -> codeAdd(Whitespace.AFTER_ANNOTATION);
      case _KEYWORD -> codeAdd(Whitespace.MANDATORY);
    }
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

    lastSet(_START);
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

      if (lastIs(_ENUM_CONSTANT)) {
        slotSemicolon();
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
    topLevel(NULL);

    var wasEnumConstant = lastIs(_ENUM_CONSTANT);

    declarationAnnotationList();

    modifierList();

    int item = itemPeek();

    if (wasEnumConstant) {
      if (item == ByteProto.ENUM_CONSTANT) {
        slotComma();
      } else {
        slotSemicolon();
      }
    }

    switch (last()) {
      case _ANNOTATION -> codeAdd(Whitespace.AFTER_ANNOTATION);

      case _KEYWORD -> codeAdd(Whitespace.MANDATORY);
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

      case ByteProto.BLOCK -> execute(this::block);

      case ByteProto.CLASS -> classDeclaration();

      case ByteProto.CONSTRUCTOR -> constructorDeclaration();

      case ByteProto.ENUM -> enumDeclaration();

      case ByteProto.ENUM_CONSTANT -> execute(this::enumConstant);

      case ByteProto.INTERFACE -> interfaceDeclaration();

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

    if (itemTest(this::isClassOrParameterizedType)) {
      codeAdd(Whitespace.MANDATORY);

      executeSwitch(this::type);
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
    typeKeyword(Keyword.CLASS);
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

  private void codeAdd(Keyword value) { codeAdd(ByteCode.KEYWORD, value.ordinal()); }

  private void codeAdd(Symbol value) { codeAdd(ByteCode.SYMBOL, value.ordinal()); }

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
           ByteProto.ENUM,
           ByteProto.INTERFACE,
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

  private void enumConstant() {
    execute(this::identifier);

    if (itemMore()) {
      argumentList();
    }

    slot();

    lastSet(_ENUM_CONSTANT);
  }

  private void enumDeclaration() {
    execute(this::enumKeyword);

    if (itemIs(ByteProto.IMPLEMENTS)) {
      implementsClause();
    }

    if (itemIs(ByteProto.BODY)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::body);
    }
  }

  private void enumKeyword() {
    typeKeyword(Keyword.ENUM);
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

    switch (part) {
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

            slot();
          }
        }
      }
    }

    if (stop()) {
      return;
    }

    while (itemTest(ByteProto::isOperator)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::operator);

      if (itemTest(ByteProto::isExpressionStart)) {
        codeAdd(Whitespace.OPTIONAL);

        expression();
      } else {
        errorRaise("expected expression after operator");
      }
    }
  }

  private void expressionBegin(int proto) {
    switch (proto) {
      case ByteProto.CLASS_INSTANCE_CREATION -> classInstanceCreation();

      case ByteProto.CLASS_TYPE -> classType();

      case ByteProto.EXPRESSION_NAME -> expressionName();

      case ByteProto.INVOKE -> invoke();

      case ByteProto.NULL_LITERAL -> nullLiteral();

      case ByteProto.PRIMITIVE_LITERAL -> primitiveLiteral();

      case ByteProto.STRING_LITERAL -> stringLiteral();

      case ByteProto.THIS -> thisKeyword();

      default -> errorRaise(
        "no-op expression part '%s'".formatted(protoName(proto))
      );
    }
  }

  private void nullLiteral() {
    codeAdd(Keyword.NULL);
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

  private void ifCondition() {
    codeAdd(Keyword.IF);

    codeAdd(Whitespace.OPTIONAL);

    codeAdd(Symbol.LEFT_PARENTHESIS);

    expression();

    if (itemMore()) {
      int proto = itemPeek();

      errorRaise(
        "expected expression end but found '%s'".formatted(protoName(proto))
      );
    }

    codeAdd(Symbol.RIGHT_PARENTHESIS);
  }

  private void ifStatement() {
    execute(this::ifCondition);

    if (itemTest(ByteProto::isStatementStart)) {
      codeAdd(Whitespace.OPTIONAL);

      statement();
    } else {
      errorRaise("no statement after if condition");
    }
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

  private void interfaceDeclaration() {
    execute(this::interfaceKeyword);

    if (itemIs(ByteProto.EXTENDS)) {
      codeAdd(Whitespace.MANDATORY);

      interfaceDeclarationExtends();
    }

    if (itemIs(ByteProto.BODY)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::body);
    }
  }

  private void interfaceDeclarationExtends() {
    execute(this::extendsKeyword);

    if (itemTest(this::isClassOrParameterizedType)) {
      codeAdd(Whitespace.MANDATORY);

      executeSwitch(this::type);

      while (itemTest(this::isClassOrParameterizedType)) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        executeSwitch(this::type);
      }
    }
  }

  private void interfaceKeyword() {
    typeKeyword(Keyword.INTERFACE);
  }

  private void invoke() {
    execute(this::identifier);

    argumentList();
  }

  private boolean isArgumentStart(int proto) {
    return ByteProto.isExpressionStart(proto)
        || proto == ByteProto.CLASS_TYPE;
  }

  private boolean isClassOrParameterizedType(int proto) {
    return proto == ByteProto.CLASS_TYPE || proto == ByteProto.PARAMETERIZED_TYPE;
  }

  private boolean isExpressionStartOrClassType(int proto) {
    return ByteProto.isExpressionStart(proto)
        || proto == ByteProto.CLASS_TYPE;
  }

  private boolean isModifierOrAnnotation(int proto) {
    return proto == ByteProto.MODIFIER || proto == ByteProto.ANNOTATION;
  }

  private boolean isVariableInitializerOrClassType(int proto) {
    if (ByteProto.isExpressionStart(proto) || proto == ByteProto.ARRAY_INITIALIZER) {
      return true;
    }

    if (proto == ByteProto.CLASS_TYPE) {
      int next = itemPeekAhead();

      return next != ByteProto.IDENTIFIER;
    }

    return false;
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

    codeAdd(Symbol.SEMICOLON);
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

    codeAdd(ByteCode.KEYWORD, proto);

    lastSet(_KEYWORD);
  }

  private void modifierList() {
    publicFound(NULL);

    if (itemIs(ByteProto.MODIFIER)) {
      if (lastIs(_ANNOTATION)) {
        codeAdd(Whitespace.AFTER_ANNOTATION);
      }

      execute(this::modifier);

      while (itemTest(this::isModifierOrAnnotation)) {
        codeAdd(Whitespace.MANDATORY);

        executeSwitch(this::modifierOrAnnotation);
      }
    }
  }

  private void modifierOrAnnotation(int proto) {
    switch (proto) {
      case ByteProto.ANNOTATION -> annotation();

      case ByteProto.MODIFIER -> modifier();
    }

    lastSet(_KEYWORD);
  }

  private void newLine() {
    codeAdd(Whitespace.NEW_LINE);

    lastSet(_NEW_LINE);
  }

  private void noop() {}

  private Object objectget(int index) {
    return objectArray[index];
  }

  private void operator() {
    codeAdd(ByteCode.SYMBOL, protoNext());
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
    codeAdd(ByteCode.KEYWORD, protoNext());
  }

  private String protoName(int proto) {
    return switch (proto) {
      case ByteProto.ARRAY_TYPE -> "Array Type";

      case ByteProto.CLASS -> "Class Keyword";

      case ByteProto.IF_CONDITION -> "If Condition";

      case ByteProto.INTERFACE -> "Interface";

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

    if (itemTest(ByteProto::isExpressionStart)) {
      codeAdd(Whitespace.MANDATORY);

      expression();

      codeAdd(Symbol.SEMICOLON);
    } else {
      errorRaise("expected start of expression");
    }
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

    codeArray[index + 0] = ByteCode.SYMBOL;

    codeArray[index + 1] = Symbol.COMMA.ordinal();
  }

  private void slotSemicolon() {
    int index = stackArray[2];

    codeArray[index + 0] = ByteCode.SYMBOL;

    codeArray[index + 1] = Symbol.SEMICOLON.ordinal();
  }

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

      case ByteProto.BLOCK -> execute(this::block);

      case ByteProto.CLASS_INSTANCE_CREATION,
           ByteProto.EXPRESSION_NAME,
           ByteProto.INVOKE,
           ByteProto.THIS -> {
        statementPrimary();

        codeAdd(Symbol.SEMICOLON);
      }

      case ByteProto.CLASS_TYPE -> {
        int next = itemPeekAhead();

        if (next != ByteProto.IDENTIFIER) {
          statementPrimary();

          codeAdd(Symbol.SEMICOLON);
        } else {
          localVariableDeclaration();
        }
      }

      case ByteProto.IF_CONDITION -> ifStatement();

      case ByteProto.RETURN -> returnStatement();

      case ByteProto.SUPER -> superInvocationWithKeyword();

      case ByteProto.SUPER_INVOCATION -> superInvocation();

      case ByteProto.THROW -> throwStatement();

      case ByteProto.VAR -> localVariableDeclaration();

      default -> errorRaise(
        "no-op statement start '%s'".formatted(protoName(start))
      );
    }
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

    codeAdd(Symbol.SEMICOLON);
  }

  private void superInvocationWithKeyword() {
    execute(this::superKeyword);

    codeAdd(Symbol.LEFT_PARENTHESIS);

    codeAdd(Symbol.RIGHT_PARENTHESIS);

    codeAdd(Symbol.SEMICOLON);
  }

  private void superKeyword() {
    codeAdd(Keyword.SUPER);
  }

  private void thisKeyword() {
    codeAdd(Keyword.THIS);
  }

  private void throwKeyword() {
    codeAdd(Keyword.THROW);
  }

  private void throwStatement() {
    execute(this::throwKeyword);

    if (itemTest(ByteProto::isExpressionStart)) {
      codeAdd(Whitespace.MANDATORY);

      expression();

      codeAdd(Symbol.SEMICOLON);
    } else {
      errorRaise("expected start of expression");
    }
  }

  private int topLevel() { return stackArray[3]; }

  private void topLevel(int value) { stackArray[3] = value; }

  private void topLevelDeclaration() {
    topLevel(1);

    declarationAnnotationList();

    modifierList();

    int next = itemPeek();

    switch (next) {
      case ByteProto.CLASS -> {
        beforeTopLevelTypeDeclaration();

        classDeclaration();
      }

      case ByteProto.ENUM -> {
        beforeTopLevelTypeDeclaration();

        enumDeclaration();
      }

      case ByteProto.INTERFACE -> {
        beforeTopLevelTypeDeclaration();

        interfaceDeclaration();
      }

      default -> errorRaise(
        "no-op top level declaration '%s'".formatted(protoName(next))
      );
    }
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

  private void typeKeyword(Keyword keyword) {
    codeAdd(keyword);

    codeAdd(Whitespace.MANDATORY);

    int proto = protoNext();

    codeAdd(ByteCode.IDENTIFIER, proto);

    simpleName(proto);

    var topLevel = topLevel() != NULL;

    if (topLevel) {
      var publicFound = publicFound() != NULL;

      var fileName = (String) objectget(proto);

      autoImports.fileName(publicFound, fileName);
    }

    lastSet(_IDENTIFIER);
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

    if (itemTest(this::isVariableInitializerOrClassType)) {
      codeAdd(Whitespace.OPTIONAL);
      codeAdd(Symbol.ASSIGNMENT);
      codeAdd(Whitespace.OPTIONAL);

      variableInitializer();
    }
  }

  private void variableInitializer() {
    if (itemTest(this::isExpressionStartOrClassType)) {
      expression();
    } else if (itemIs(ByteProto.ARRAY_INITIALIZER)) {
      execute(this::arrayInitializer);
    } else {
      errorRaise();
    }
  }

  private void varKeyword() {
    codeAdd(Keyword.VAR);
  }

  private void voidKeyword() {
    codeAdd(Keyword.VOID);
  }

}