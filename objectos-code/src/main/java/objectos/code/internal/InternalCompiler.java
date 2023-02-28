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
package objectos.code.internal;

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
      _BLOCK = 2,
      _COMMA = 3,
      _ENUM_CONSTANT = 4,
      _IDENTIFIER = 5,
      _KEYWORD = 6,
      _NEW_LINE = 7,
      _PRIMARY = 8,
      _SYMBOL = 9,
      _TYPE = 10;

  protected final void compile() {
    codeIndex = 0;

    // simpleName
    stackArray[0] = NULL;
    // public found
    stackArray[1] = NULL;
    // comma slot
    stackArray[2] = NULL;
    // topLevel
    stackArray[3] = NULL;
    // error
    stackArray[4] = 0;
    // abstract found
    stackArray[5] = NULL;
    // last
    stackArray[6] = NULL;

    // do not change
    // - objectIndex
    // - stackIndex

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

  final void noopState(String element, String state) {
    int proto = protoNext();

    errorRaise(
      "no-op item '%s' at '%s' (State '%s')".formatted(protoName(proto), element, state)
    );
  }

  private boolean abstractFound() { return stackArray[5] != NULL; }

  private void abstractFound(int value) {
    stackArray[5] = value;
  }

  private void annotation() {
    codeAdd(Symbol.COMMERCIAL_AT);

    execute(this::classType);

    if (elemMore()) {
      codeAdd(Symbol.LEFT_PARENTHESIS);

      last(_START);

      execute(this::annotationValue);

      while (elemMore()) {
        comma();

        execute(this::annotationValue);
      }

      codeAdd(Symbol.RIGHT_PARENTHESIS);
    }

    last(_ANNOTATION);
  }

  private void annotationValue() {
    // TODO name

    lang();
  }

  private void argument() {
    lang();
  }

  private void argumentComma() {
    int nl = 0;

    int index = codeIndex;

    while (index >= 0) {
      int value = codeArray[--index];

      if (value != Whitespace.NEW_LINE.ordinal()) {
        break;
      }

      int code = codeArray[--index];

      if (code != ByteCode.WHITESPACE) {
        break;
      }

      nl++;

      codeIndex = index;
    }

    codeAdd(Symbol.COMMA);

    last(_COMMA);

    for (int i = 0; i < nl; i++) {
      codeAdd(Whitespace.NEW_LINE);

      last(_NEW_LINE);
    }
  }

  private void argumentEnd() {
    codeAdd(Indentation.EXIT_PARENTHESIS);

    if (lastIs(_NEW_LINE)) {
      codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);
    }

    codeAdd(Symbol.RIGHT_PARENTHESIS);
  }

  private void argumentList() {
    argumentStart();

    consumeWs();

    if (protoIs(ByteProto.ARGUMENT)) {
      execute(this::argument);

      consumeWs();

      while (protoIs(ByteProto.ARGUMENT)) {
        argumentComma();

        execute(this::argument);

        consumeWs();
      }
    }

    argumentEnd();
  }

  private void argumentStart() {
    codeAdd(Symbol.LEFT_PARENTHESIS);

    codeAdd(Indentation.ENTER_PARENTHESIS);

    last(_START);
  }

  private void arrayAccess() {
    codeAdd(Symbol.LEFT_SQUARE_BRACKET);

    last(_START);

    lang();

    codeAdd(Symbol.RIGHT_SQUARE_BRACKET);

    last(_PRIMARY);
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
    switch (last()) {
      case _SYMBOL -> codeAdd(Whitespace.OPTIONAL);
    }
  }

  private void arrayInitializerValueList() {
    codeAdd(Symbol.LEFT_CURLY_BRACKET);
    codeAdd(Indentation.ENTER_BLOCK);
    last(_START);

    consumeWs();

    if (protoIs(ByteProto.VALUE)) {
      execute(this::argument);

      consumeWs();

      while (protoIs(ByteProto.VALUE)) {
        argumentComma();

        execute(this::argument);

        consumeWs();
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

    last(_TYPE);
  }

  private void autoImports() {
    switch (last()) {
      default -> codeAdd(ByteCode.AUTO_IMPORTS0);

      case _SYMBOL -> codeAdd(ByteCode.AUTO_IMPORTS1);
    }

    last(_SYMBOL);
  }

  private void beforeTopLevelTypeDeclaration() {
    switch (last()) {
      case _ANNOTATION -> codeAdd(Whitespace.AFTER_ANNOTATION);

      case _KEYWORD -> codeAdd(Whitespace.MANDATORY);
    }
  }

  private void block() {
    switch (last()) {
      case _KEYWORD, _SYMBOL -> codeAdd(Whitespace.OPTIONAL);
    }

    codeAdd(Symbol.LEFT_CURLY_BRACKET);

    last(_START);

    if (protoIs(ByteProto.STATEMENT)) {
      codeAdd(Indentation.ENTER_BLOCK);

      execute(this::blockStatement);

      while (elemMore()) {
        execute(this::blockStatement);
      }

      codeAdd(Indentation.EXIT_BLOCK);
      codeAdd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
    } else {
      codeAdd(Whitespace.BEFORE_EMPTY_BLOCK_END);
    }

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);

    last(_BLOCK);
  }

  private void blockStatement() {
    codeAdd(Whitespace.BEFORE_NEXT_STATEMENT);

    last(_START);

    lang();

    if (!lastIs(_BLOCK)) {
      codeAdd(Symbol.SEMICOLON);

      last(_SYMBOL);
    }
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

    last(_START);
  }

  private void body(int enumConstants, int members) {
    preSymbol();

    codeAdd(Symbol.LEFT_CURLY_BRACKET);

    last(_START);

    var empty = true;

    if (enumConstants != NULL) {
      empty = false;

      codeAdd(Indentation.ENTER_BLOCK);

      listExecute(enumConstants, this::enumConstant);

      semicolon();
    }

    if (members != NULL) {
      if (empty) {
        empty = false;

        codeAdd(Indentation.ENTER_BLOCK);
      }

      listSwitch(members, this::bodyMember);
    }

    if (!empty) {
      codeAdd(Indentation.EXIT_BLOCK);

      codeAdd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
    } else {
      codeAdd(Whitespace.BEFORE_EMPTY_BLOCK_END);
    }

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);

    last(_SYMBOL);
  }

  private void bodyMember() {
    topLevel(NULL);

    if (itemIs(ByteProto.FIELD_DECLARATION)) {
      execute(this::fieldDeclaration);

      return;
    }

    if (itemIs(ByteProto.CONSTRUCTOR_DECLARATION)) {
      execute(this::constructorDeclaration);

      return;
    }

    if (itemIs(ByteProto.METHOD_DECLARATION)) {
      execute(this::methodDeclaration);

      return;
    }

    var wasEnumConstant = lastIs(_ENUM_CONSTANT);

    last(_START);

    oldDeclarationAnnotationList();

    oldModifierList();

    int item = itemPeek();

    if (wasEnumConstant) {
      if (item == ByteProto.ENUM_CONSTANT) {
        slotComma();
      } else {
        slotSemicolon();
      }
    }

    switch (item) {
      case ByteProto.ARRAY_TYPE,
           ByteProto.CLASS_TYPE,
           ByteProto.PARAMETERIZED_TYPE,
           ByteProto.PRIMITIVE_TYPE,
           ByteProto.TYPE_VARIABLE -> {
        executeSwitch(this::oldType);

        fieldOrMethodDeclaration();
      }

      case ByteProto.BLOCK -> {
        if (lastIs(_KEYWORD)) {
          codeAdd(Whitespace.MANDATORY);
        }

        execute(this::oldBlock);
      }

      case ByteProto.CLASS -> {
        if (lastIs(_KEYWORD)) {
          codeAdd(Whitespace.MANDATORY);
        }

        oldClassDeclaration();
      }

      case ByteProto.CONSTRUCTOR -> oldConstructorDeclaration();

      case ByteProto.ENUM -> oldEnumDeclaration();

      case ByteProto.ENUM_CONSTANT -> execute(this::oldEnumConstant);

      case ByteProto.INTERFACE -> {
        if (lastIs(_KEYWORD)) {
          codeAdd(Whitespace.MANDATORY);
        }

        oldInterfaceDeclaration();
      }

      case ByteProto.TYPE_PARAMETER -> {
        oldTypeParameterList();

        oldMethodDeclarationAfterTypeParameterList();
      }

      case ByteProto.VOID -> {
        execute(this::voidKeyword);

        if (itemIs(ByteProto.METHOD)) {
          oldMethodDeclaration();
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

  private void bodyMember(int proto) {
    topLevel(NULL);

    switch (last()) {
      case _START -> codeAdd(Whitespace.BEFORE_FIRST_MEMBER);

      case _SYMBOL -> codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
    }

    switch (proto) {
      case ByteProto.CLASS_DECLARATION -> classDeclaration();

      case ByteProto.CONSTRUCTOR_DECLARATION -> constructorDeclaration();

      case ByteProto.ENUM_DECLARATION -> enumDeclaration();

      case ByteProto.FIELD_DECLARATION -> fieldDeclaration();

      case ByteProto.INTERFACE_DECLARATION -> interfaceDeclaration();

      case ByteProto.METHOD_DECLARATION -> methodDeclaration();

      default -> errorRaise(
        "no-op body member '%s'".formatted(protoName(proto))
      );
    }
  }

  private void classDeclaration() {
    typeDeclaration(Keyword.CLASS);
  }

  private void classDeclarationExtends() {
    execute(this::extendsKeyword);

    if (itemTest(this::isClassOrParameterizedType)) {
      executeSwitch(this::oldType);
    } else {
      error();
    }
  }

  private void classType() {
    preType();

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

    last(_TYPE);
  }

  private void clause(Keyword keyword) {
    if (elemMore()) {
      switch (last()) {
        case _IDENTIFIER -> keyword(keyword);

        case _TYPE -> comma();
      }

      executeSwitch(this::type);

      while (elemMore()) {
        comma();

        executeSwitch(this::type);
      }
    }
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

  private void comma() {
    codeAdd(Symbol.COMMA);

    last(_COMMA);
  }

  private void compilationUnit() {
    last(_START);

    while (elemMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto.ANNOTATION -> {
          oldDeclarationAnnotationList();

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

        case ByteProto.CLASS_DECLARATION -> {
          preTopLevel();

          execute(this::classDeclaration);
        }

        case ByteProto.END_ELEMENT -> {}

        case ByteProto.ENUM_DECLARATION -> {
          preTopLevel();

          execute(this::enumDeclaration);
        }

        case ByteProto.INTERFACE_DECLARATION -> {
          preTopLevel();

          execute(this::interfaceDeclaration);
        }

        case ByteProto.PACKAGE -> ordinaryCompilationUnit();

        case ByteProto.PACKAGE_DECLARATION -> {
          execute(this::packageDeclaration);
        }

        default -> errorRaise(
          "no-op item @ compilation unit '%s'".formatted(protoName(proto))
        );
      }
    }
  }

  private void preTopLevel() {
    switch (last()) {
      case _SYMBOL -> codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
    }

    simpleName(NULL);

    topLevel(1);
  }

  private void packageDeclaration() {
    execute(this::packageKeyword);

    last(_SYMBOL);
  }

  private void constructorDeclaration() {
    int start = stackIndex;

    abstractFound(NULL);

    last(_START);

    int annotations = NULL,
        modifiers = NULL,
        typeParameters = NULL,
        //receiverParameter = NULL,
        parameters = NULL,
        statements = NULL;

    while (protoMore()) {
      int proto = protoPeek();

      if (proto == ByteProto.END_ELEMENT) {
        break;
      }

      switch (proto) {
        case ByteProto.ANNOTATION -> annotations = listAdd(annotations);

        case ByteProto.MODIFIER,
             ByteProto.MODIFIERS -> modifiers = listAdd(modifiers);

        case ByteProto.PARAMETER_SHORT -> parameters = listAdd(parameters);

        case ByteProto.STATEMENT -> statements = listAdd(statements);

        case ByteProto.TYPE_PARAMETER -> typeParameters = listAdd(typeParameters);

        default -> {
          throw new UnsupportedOperationException(
            "Implement me :: " + protoName(proto)
          );
        }
      }
    }

    if (annotations != NULL) {
      listExecute(annotations, this::annotation);
    }

    if (modifiers != NULL) {
      listSwitch(modifiers, this::modifierSwitcher);
    }

    if (typeParameters != NULL) {
      listExecute(typeParameters, this::typeParameter);

      typeParameterListEnd();
    }

    int name = simpleName();
    if (name == NULL) {
      name = object("Constructor");
    }
    preIdentifier();
    codeAdd(ByteCode.IDENTIFIER, name);

    codeAdd(Symbol.LEFT_PARENTHESIS);

    if (parameters != NULL) {
      last(_START);

      listSwitch(parameters, this::parameterSwitcher);
    }

    executableBody(statements);

    stackIndex = start;
  }

  private void constructorDeclarator() {
    int name = simpleName();

    if (name == NULL) {
      name = object("Constructor");
    }

    preIdentifier();

    codeAdd(ByteCode.IDENTIFIER, name);

    oldFormalParameterList();
  }

  private void consumeWs() {
    while (protoMore() && protoPeek() == ByteProto.NEW_LINE) {
      execute(this::newLine);
    }
  }

  private void declarationName() {
    preIdentifier();

    codeAdd(ByteCode.IDENTIFIER, protoNext());

    last(_IDENTIFIER);
  }

  private boolean elemMore() {
    if (protoMore()) {
      int proto = protoPeek();

      return proto != ByteProto.END_ELEMENT;
    } else {
      return false;
    }
  }

  private void ellipsis() {
    codeAdd(Symbol.ELLIPSIS);

    last(_SYMBOL);
  }

  private void elseKeyword() {
    preKeyword();

    codeAdd(Keyword.ELSE);

    last(_KEYWORD);
  }

  private void enumConstant() {
    switch (last()) {
      case _ENUM_CONSTANT -> {
        codeAdd(Symbol.COMMA);

        codeAdd(Whitespace.BEFORE_NEXT_MEMBER);

        last(_START);
      }

      case _START -> codeAdd(Whitespace.BEFORE_FIRST_MEMBER);

      case _SYMBOL -> codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
    }

    execute(this::declarationName);

    if (elemMore()) {
      argumentList();
    }

    last(_ENUM_CONSTANT);
  }

  private void enumDeclaration() {
    typeDeclaration(Keyword.ENUM);
  }

  private void enumKeyword() {
    typeKeyword(Keyword.ENUM);
  }

  private boolean error() {
    var result = stackArray[4] == 1;

    stackArray[4] = 0;

    return result;
  }

  private void errorRaise() { stackArray[4] = 1; }

  private void errorRaise(String message) {
    errorRaise();

    codeAdd(ByteCode.COMMENT, object(message));
  }

  private void executableBody(int statements) {
    codeAdd(Symbol.RIGHT_PARENTHESIS);

    if (!abstractFound()) {
      codeAdd(Whitespace.OPTIONAL);

      codeAdd(Symbol.LEFT_CURLY_BRACKET);

      if (statements != NULL) {
        codeAdd(Indentation.ENTER_BLOCK);

        listExecute(statements, this::blockStatement);

        codeAdd(Indentation.EXIT_BLOCK);

        codeAdd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
      } else {
        codeAdd(Whitespace.BEFORE_EMPTY_BLOCK_END);
      }

      codeAdd(Symbol.RIGHT_CURLY_BRACKET);

      last(_SYMBOL);
    } else {
      semicolon();
    }
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

  private void expressionName() {
    preDot();

    codeAdd(ByteCode.IDENTIFIER, protoNext());

    last(_IDENTIFIER);
  }

  private void extendsClause() {
    clause(Keyword.EXTENDS);
  }

  private void extendsKeyword() {
    preKeyword();

    codeAdd(Keyword.EXTENDS);

    last(_KEYWORD);
  }

  private void fieldDeclaration() {
    int start = stackIndex;

    last(_START);

    int annotations = NULL,
        modifiers = NULL,
        type = NULL,
        declarators = NULL;

    loop: while (protoMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto.ANNOTATION -> annotations = listAdd(annotations);

        case ByteProto.ARRAY_TYPE,
             ByteProto.CLASS_TYPE,
             ByteProto.PARAMETERIZED_TYPE,
             ByteProto.PRIMITIVE_TYPE,
             ByteProto.TYPE_VARIABLE -> type = singleSet(type);

        case ByteProto.MODIFIER,
             ByteProto.MODIFIERS -> modifiers = listAdd(modifiers);

        default -> {
          if (proto != ByteProto.END_ELEMENT) {
            declarators = protoIndex;
          }

          break loop;
        }
      }
    }

    if (annotations != NULL) {
      listExecute(annotations, this::annotation);
    }

    if (modifiers != NULL) {
      listSwitch(modifiers, this::modifierSwitcher);
    }

    if (type != NULL) {
      singleSwitch(type, this::type);
    }

    if (declarators != NULL) {
      protoIndex = declarators;

      fieldDeclarationVariableList();
    }

    semicolon();

    stackIndex = start;
  }

  private void fieldDeclarationVariableItem() {
    if (protoIs(ByteProto.DECLARATION_NAME)) {
      execute(this::declarationName);
    } else {
      unnamed();
    }

    int proto = protoPeek();

    if (proto == ByteProto.END_ELEMENT) {
      return;
    }

    if (proto == ByteProto.DECLARATION_NAME) {
      return;
    }

    preSymbol();
    codeAdd(Symbol.ASSIGNMENT);
    last(_SYMBOL);

    langItem(proto);

    while (elemMore()) {
      proto = protoPeek();

      if (proto == ByteProto.DECLARATION_NAME) {
        return;
      }

      langItem(proto);
    }
  }

  private void fieldDeclarationVariableList() {
    if (elemMore()) {
      fieldDeclarationVariableItem();

      while (elemMore()) {
        comma();

        fieldDeclarationVariableItem();
      }
    }
  }

  private void fieldOrMethodDeclaration() {
    int item = itemPeek();

    switch (item) {
      case ByteProto.IDENTIFIER -> {
        oldFieldDeclarationVariableList();
      }

      case ByteProto.METHOD -> {
        oldMethodDeclaration();
      }

      default -> errorRaise(
        "found '%s' in field or method".formatted(protoName(item))
      );
    }
  }

  private void identifier() {
    preIdentifier();

    codeAdd(ByteCode.IDENTIFIER, protoNext());

    last(_IDENTIFIER);
  }

  private void ifKeyword() {
    preKeyword();

    codeAdd(Keyword.IF);

    last(_KEYWORD);
  }

  private void ifStatement() {
    execute(this::oldIfCondition);

    if (itemTest(ByteProto::isStatementStart)) {
      codeAdd(Whitespace.OPTIONAL);

      oldStatement();
    } else {
      errorRaise("no statement after if condition");
    }

    if (itemIs(ByteProto.ELSE)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::elseKeyword);

      if (itemTest(ByteProto::isStatementStart)) {
        codeAdd(Whitespace.MANDATORY);

        oldStatement();
      } else {
        errorRaise("no statement after the `else` keyword");
      }
    }
  }

  private void implementsClause() {
    clause(Keyword.IMPLEMENTS);
  }

  private void implementsKeyword() {
    preKeyword();

    codeAdd(Keyword.IMPLEMENTS);

    last(_KEYWORD);
  }

  private void importDeclarationList() {
    if (itemIs(ByteProto.AUTO_IMPORTS)) {
      execute(this::autoImports);
    }
  }

  private void interfaceDeclaration() {
    typeDeclaration(Keyword.INTERFACE);
  }

  private void interfaceDeclarationExtends() {
    execute(this::extendsKeyword);

    if (itemTest(this::isClassOrParameterizedType)) {
      executeSwitch(this::oldType);

      while (itemTest(this::isClassOrParameterizedType)) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        executeSwitch(this::oldType);
      }
    }
  }

  private void interfaceKeyword() {
    typeKeyword(Keyword.INTERFACE);
  }

  private void invoke() {
    preDot();

    last(_START);

    execute(this::identifier);

    oldArgumentList();

    last(_PRIMARY);
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

  private void keyword(Keyword value) {
    preKeyword();

    codeAdd(value);

    last(_KEYWORD);
  }

  private void lang() {
    while (elemMore()) {
      int proto = protoPeek();

      langItem(proto);
    }
  }

  private void langItem(int proto) {
    switch (proto) {
      case ByteProto.ARRAY_ACCESS -> execute(this::arrayAccess);

      case ByteProto.ARRAY_INITIALIZER -> {
        execute(this::arrayInitializer);
        arrayInitializerValueList();
      }

      case ByteProto.ARGUMENT -> execute(this::argument);

      case ByteProto.ASSIGNMENT_OPERATOR -> execute(this::operator);

      case ByteProto.BLOCK -> execute(this::block);

      case ByteProto.CLASS_TYPE -> {
        execute(this::classType);
        maybeLocalVariable();
      }

      case ByteProto.DECLARATION_NAME -> execute(this::declarationName);

      case ByteProto.ELSE -> execute(this::elseKeyword);

      case ByteProto.EQUALITY_OPERATOR -> execute(this::operator);

      case ByteProto.EXPRESSION_NAME -> execute(this::expressionName);

      case ByteProto.IF -> {
        execute(this::ifKeyword);
        consumeWs();

        if (protoIs(ByteProto.ARGUMENT)) {
          codeAdd(Whitespace.OPTIONAL);
          argumentStart();
          consumeWs();
          execute(this::argument);
          consumeWs();
          argumentEnd();
          last(_SYMBOL);
        }
      }

      case ByteProto.NEW -> {
        execute(this::newKeyword);
        consumeWs();
        if (protoTest(ByteProto::isClassOrParameterizedType)) {
          executeSwitch(this::type);
          argumentList();
          last(_PRIMARY);
        }
      }

      case ByteProto.NEW_LINE -> execute(this::newLine);

      case ByteProto.NULL_LITERAL -> execute(this::nullLiteral);

      case ByteProto.PARAMETERIZED_TYPE -> execute(this::parameterizedType);

      case ByteProto.PRIMITIVE_LITERAL -> execute(this::primitiveLiteral);

      case ByteProto.PRIMITIVE_TYPE -> {
        execute(this::primitiveType);
        maybeLocalVariable();
      }

      case ByteProto.RETURN -> execute(this::returnKeyword);

      case ByteProto.STRING_LITERAL -> execute(this::stringLiteral);

      case ByteProto.SUPER -> {
        execute(this::superKeyword);

        consumeWs();

        if (protoIs(ByteProto.END_ELEMENT)) {
          codeAdd(Symbol.LEFT_PARENTHESIS);
          codeAdd(Symbol.RIGHT_PARENTHESIS);
        } else if (protoIs(ByteProto.ARGUMENT)) {
          argumentList();
        }
      }

      case ByteProto.THIS -> execute(this::thisKeyword);

      case ByteProto.THROW -> execute(this::throwKeyword);

      case ByteProto.V -> {
        execute(this::v);
        argumentList();
        last(_PRIMARY);
      }

      case ByteProto.VAR -> {
        execute(this::varKeyword);
        maybeLocalVariable();
      }

      default -> errorRaise(
        "no-op statement part '%s'".formatted(protoName(proto))
      );
    }
  }

  private int last() { return stackArray[6]; }

  private void last(int value) { stackArray[6] = value; }

  private boolean lastIs(int value) { return last() == value; }

  private int listAdd(int list) {
    if (list == NULL) {
      list = stackIndex;
      protoArray = IntArrays.growIfNecessary(protoArray, stackIndex + 3);
      protoArray[stackIndex++] = NULL;
    } else {
      int jmpLocation = protoArray[list];
      protoArray[jmpLocation] = stackIndex;
      protoArray = IntArrays.growIfNecessary(protoArray, stackIndex + 2);
    }

    int proto = protoNext();
    int value = protoNext();

    protoArray[stackIndex++] = proto;
    protoArray[stackIndex++] = value;
    int tail = stackIndex;
    protoArray[stackIndex++] = NULL;
    protoArray[list] = tail;

    return list;
  }

  private void listExecute(Action action) {
    protoNext();

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    action.execute();

    protoIndex = returnTo;

    int maybeNext = protoArray[protoIndex];

    if (maybeNext != NULL) {
      protoIndex = maybeNext;
    }
  }

  private void listExecute(int offset, Action action) {
    protoIndex = offset + 1;

    while (listMore()) {
      listExecute(action);
    }
  }

  private boolean listMore() {
    return protoArray[protoIndex] != NULL;
  }

  private void listSwitch(int offset, SwitchAction action) {
    protoIndex = offset + 1;

    while (listMore()) {
      listSwitch(action);
    }
  }

  private void listSwitch(SwitchAction action) {
    int proto = protoNext();

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    action.execute(proto);

    protoIndex = returnTo;

    int maybeNext = protoArray[protoIndex];

    if (maybeNext != NULL) {
      protoIndex = maybeNext;
    }
  }

  private void localVariableDeclaration() {
    int type = itemPeek();

    last(_START);

    if (ByteProto.isType(type)) {
      executeSwitch(this::oldType);
    } else if (type == ByteProto.VAR) {
      execute(this::varKeyword);
    } else {
      errorRaise(
        "invalid local var: expected var or type but found '%s'"
            .formatted(protoName(type))
      );
    }

    if (itemIs(ByteProto.IDENTIFIER)) {
      oldVariableDeclarator();

      while (itemIs(ByteProto.IDENTIFIER)) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        oldVariableDeclarator();
      }
    } else {
      errorRaise("invalid local var: variable name not found");
    }

    codeAdd(Symbol.SEMICOLON);
  }

  private void maybeLocalVariable() {
    if (protoIs(ByteProto.DECLARATION_NAME)) {
      execute(this::declarationName);

      codeAdd(Whitespace.OPTIONAL);
      codeAdd(Symbol.ASSIGNMENT);
      last(_SYMBOL);
    }
  }

  private void methodDeclaration() {
    int start = stackIndex;

    abstractFound(NULL);

    last(_START);

    int annotations = NULL,
        modifiers = NULL,
        typeParameters = NULL,
        result = NULL,
        name = NULL,
        //receiverParameter = NULL,
        parameters = NULL,
        statements = NULL;

    while (protoMore()) {
      int proto = protoPeek();

      if (proto == ByteProto.END_ELEMENT) {
        break;
      }

      switch (proto) {
        case ByteProto.ANNOTATION -> annotations = listAdd(annotations);

        case ByteProto.ARRAY_TYPE,
             ByteProto.CLASS_TYPE,
             ByteProto.PARAMETERIZED_TYPE,
             ByteProto.PRIMITIVE_TYPE,
             ByteProto.TYPE_VARIABLE -> result = singleSet(result);

        case ByteProto.DECLARATION_NAME -> name = singleSet(name);

        case ByteProto.MODIFIER,
             ByteProto.MODIFIERS -> modifiers = listAdd(modifiers);

        case ByteProto.PARAMETER_SHORT -> parameters = listAdd(parameters);

        case ByteProto.RETURN_TYPE -> result = singleSet(result);

        case ByteProto.STATEMENT -> statements = listAdd(statements);

        case ByteProto.TYPE_PARAMETER -> typeParameters = listAdd(typeParameters);

        case ByteProto.VOID -> result = singleSet(result);

        default -> {
          throw new UnsupportedOperationException(
            "Implement me :: " + protoName(proto)
          );
        }
      }
    }

    if (annotations != NULL) {
      listExecute(annotations, this::annotation);
    }

    if (modifiers != NULL) {
      listSwitch(modifiers, this::modifierSwitcher);
    }

    if (typeParameters != NULL) {
      listExecute(typeParameters, this::typeParameter);

      typeParameterListEnd();
    }

    if (result != NULL) {
      singleSwitch(result, this::methodResult);
    } else {
      voidKeyword();
    }

    if (name != NULL) {
      singleExecute(name, this::declarationName);
    } else {
      unnamed();
    }

    codeAdd(Symbol.LEFT_PARENTHESIS);

    if (parameters != NULL) {
      last(_START);

      listSwitch(parameters, this::parameterSwitcher);
    }

    executableBody(statements);

    stackIndex = start;
  }

  private void methodResult(int proto) {
    switch (proto) {
      case ByteProto.ARRAY_TYPE -> arrayType();

      case ByteProto.CLASS_TYPE -> classType();

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType();

      case ByteProto.PRIMITIVE_TYPE -> primitiveType();

      case ByteProto.RETURN_TYPE -> returnType();

      case ByteProto.TYPE_VARIABLE -> typeVariable();

      case ByteProto.VOID -> voidKeyword();

      default -> errorRaise(
        "no-op method result '%s'".formatted(protoName(proto))
      );
    }
  }

  private void modifier() {
    switch (last()) {
      case _ANNOTATION -> codeAdd(Whitespace.AFTER_ANNOTATION);

      case _KEYWORD -> codeAdd(Whitespace.MANDATORY);
    }

    int proto = protoNext();

    var keyword = Keyword.get(proto);

    switch (keyword) {
      case ABSTRACT -> abstractFound(1);

      case PUBLIC -> publicFound(1);

      default -> {}
    }

    codeAdd(ByteCode.KEYWORD, proto);

    last(_KEYWORD);
  }

  private void modifiers() {
    int size = protoNext();

    for (int i = 0; i < size; i++) {
      modifier();
    }
  }

  private void modifierSwitcher(int proto) {
    switch (proto) {
      case ByteProto.MODIFIER -> modifier();

      case ByteProto.MODIFIERS -> modifiers();
    }
  }

  private void newKeyword() {
    preKeyword();

    codeAdd(Keyword.NEW);

    last(_KEYWORD);
  }

  private void newLine() {
    codeAdd(Whitespace.NEW_LINE);

    last(_NEW_LINE);
  }

  private void noop() {}

  private void nullLiteral() {
    preKeyword();

    codeAdd(Keyword.NULL);

    last(_KEYWORD);
  }

  private Object objectget(int index) {
    return objectArray[index];
  }

  private void oldAnnotation() {
    codeAdd(Symbol.COMMERCIAL_AT);

    last(_START);

    execute(this::classType);

    if (itemMore()) {
      codeAdd(Symbol.LEFT_PARENTHESIS);

      last(_START);

      oldAnnotationValuePair();

      while (itemMore()) {
        codeAdd(Symbol.COMMA);
        codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

        oldAnnotationValuePair();
      }

      codeAdd(Symbol.RIGHT_PARENTHESIS);
    }

    last(_ANNOTATION);
  }

  private void oldAnnotationValuePair() {
    // check for value name

    oldExpression();
  }

  private void oldArgumentList() {
    codeAdd(Symbol.LEFT_PARENTHESIS);
    codeAdd(Indentation.ENTER_PARENTHESIS);

    if (itemTest(this::isArgumentStart)) {
      if (lastIs(_NEW_LINE)) {
        codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);
      }

      last(_START);

      oldExpression();

      while (itemTest(this::isArgumentStart)) {
        slotComma();

        var ws = lastIs(_NEW_LINE)
            ? Whitespace.BEFORE_FIRST_LINE_CONTENT
            : Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM;

        codeAdd(ws);

        last(_START);

        oldExpression();
      }
    }

    argumentEnd();
  }

  private void oldArrayAccess() {
    codeAdd(Symbol.LEFT_SQUARE_BRACKET);

    last(_START);

    oldExpression();

    codeAdd(Symbol.RIGHT_SQUARE_BRACKET);
  }

  private void oldArrayInitializer() {
    if (lastIs(_SYMBOL)) {
      codeAdd(Whitespace.OPTIONAL);
    }

    codeAdd(Symbol.LEFT_CURLY_BRACKET);
    codeAdd(Indentation.ENTER_BLOCK);

    last(_START);

    if (itemMore()) {
      oldVariableInitializer();

      while (itemMore()) {
        slotComma();

        oldVariableInitializer();
      }
    }

    codeAdd(Indentation.EXIT_BLOCK);

    if (lastIs(_NEW_LINE)) {
      codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);
    }

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);
  }

  private void oldBlock() {
    codeAdd(Symbol.LEFT_CURLY_BRACKET);

    if (itemMore()) {
      codeAdd(Indentation.ENTER_BLOCK);
      codeAdd(Whitespace.BEFORE_NEXT_STATEMENT);

      oldBlockStatement();

      while (itemMore()) {
        codeAdd(Whitespace.BEFORE_NEXT_STATEMENT);

        oldBlockStatement();
      }

      codeAdd(Indentation.EXIT_BLOCK);
      codeAdd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
    } else {
      codeAdd(Whitespace.BEFORE_EMPTY_BLOCK_END);
    }

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);

    last(_START);
  }

  private void oldBlockStatement() {
    int start = itemPeek();
    // TODO local class
    // TODO local variable decl

    oldStatement0(start);
  }

  private void oldClassDeclaration() {
    execute(this::oldClassKeyword);

    if (itemIs(ByteProto.EXTENDS)) {
      classDeclarationExtends();
    }

    if (itemIs(ByteProto.IMPLEMENTS)) {
      oldImplementsClause();
    }

    if (itemIs(ByteProto.BODY)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::body);
    }
  }

  private void oldClassInstanceCreation() {
    preKeyword();

    codeAdd(Keyword.NEW);

    last(_KEYWORD);

    executeSwitch(this::oldType);

    oldArgumentList();
  }

  private void oldClassKeyword() {
    typeKeyword(Keyword.CLASS);
  }

  private void oldConstructorDeclaration() {
    execute(this::constructorDeclarator);

    if (itemIs(ByteProto.BLOCK)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::oldBlock);
    } else {
      errorRaise("Constructor without a block() declaration");
    }
  }

  private void oldDeclarationAnnotationList() {
    if (itemIs(ByteProto.ANNOTATION)) {
      execute(this::oldAnnotation);

      while (itemIs(ByteProto.ANNOTATION)) {
        codeAdd(Whitespace.AFTER_ANNOTATION);

        execute(this::oldAnnotation);
      }
    }
  }

  private void oldEnumConstant() {
    last(_START);

    execute(this::identifier);

    if (itemMore()) {
      oldArgumentList();
    }

    slot();

    last(_ENUM_CONSTANT);
  }

  private void oldEnumDeclaration() {
    execute(this::enumKeyword);

    if (itemIs(ByteProto.IMPLEMENTS)) {
      oldImplementsClause();
    }

    if (itemIs(ByteProto.BODY)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::body);
    }
  }

  private void oldExpression() {
    int part = executeSwitch(this::oldExpressionBegin);

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

              last(_START);
            }

            oldExpression();
          }

          case ByteProto.ARRAY_ACCESS -> {
            execute(this::oldArrayAccess);

            while (itemIs(ByteProto.ARRAY_ACCESS)) {
              execute(this::oldArrayAccess);
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
      execute(this::operator);

      if (itemTest(ByteProto::isExpressionStart)) {
        oldExpression();
      } else {
        errorRaise("expected expression after operator");
      }
    }
  }

  private void oldExpressionBegin(int proto) {
    switch (proto) {
      case ByteProto.ARRAY_ACCESS -> oldArrayAccess();

      case ByteProto.CLASS_INSTANCE_CREATION -> oldClassInstanceCreation();

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

  private void oldFieldDeclarationVariableList() {
    oldVariableDeclarator();

    while (itemIs(ByteProto.IDENTIFIER)) {
      codeAdd(Symbol.COMMA);
      last(_COMMA);

      oldVariableDeclarator();
    }

    codeAdd(Symbol.SEMICOLON);

    last(_SYMBOL);
  }

  private void oldFormalParameter() {
    if (itemTest(ByteProto::isType)) {
      executeSwitch(this::oldType);
    } else {
      errorRaise("invalid formal parameter");

      return;
    }

    if (itemIs(ByteProto.ELLIPSIS)) {
      execute(this::ellipsis);
    }

    if (itemIs(ByteProto.IDENTIFIER)) {
      execute(this::identifier);
    } else {
      errorRaise("invalid formal parameter");

      return;
    }
  }

  private void oldFormalParameterList() {
    codeAdd(Symbol.LEFT_PARENTHESIS);

    last(_START);

    if (itemMore()) {
      oldFormalParameter();

      while (itemMore()) {
        codeAdd(Symbol.COMMA);
        last(_COMMA);

        oldFormalParameter();
      }
    }

    codeAdd(Symbol.RIGHT_PARENTHESIS);
  }

  private void oldIfCondition() {
    codeAdd(Keyword.IF);

    codeAdd(Whitespace.OPTIONAL);

    codeAdd(Symbol.LEFT_PARENTHESIS);

    oldExpression();

    if (itemMore()) {
      int proto = itemPeek();

      errorRaise(
        "expected expression end but found '%s'".formatted(protoName(proto))
      );
    }

    codeAdd(Symbol.RIGHT_PARENTHESIS);
  }

  private void oldImplementsClause() {
    execute(this::implementsKeyword);

    if (itemTest(ByteProto::isClassOrParameterizedType)) {
      executeSwitch(this::oldType);

      while (itemTest(ByteProto::isClassOrParameterizedType)) {
        codeAdd(Symbol.COMMA);
        last(_COMMA);

        executeSwitch(this::oldType);
      }
    }
  }

  private void oldInterfaceDeclaration() {
    execute(this::interfaceKeyword);

    if (itemIs(ByteProto.EXTENDS)) {
      interfaceDeclarationExtends();
    }

    if (itemIs(ByteProto.BODY)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::body);
    }
  }

  private void oldMethodDeclaration() {
    execute(this::oldMethodDeclarator);

    if (itemIs(ByteProto.BLOCK)) {
      codeAdd(Whitespace.OPTIONAL);

      execute(this::oldBlock);
    } else {
      // assume abstract
      codeAdd(Symbol.SEMICOLON);

      last(_SYMBOL);
    }
  }

  private void oldMethodDeclarationAfterTypeParameterList() {
    int returnType = itemPeek();

    if (ByteProto.isType(returnType)) {
      executeSwitch(this::oldType);
    } else if (returnType == ByteProto.VOID) {
      execute(this::voidKeyword);
    } else {
      errorRaise(
        "Method declaration: expected 'Return Type' but found '%s'".formatted(protoName(returnType))
      );

      return;
    }

    if (itemIs(ByteProto.METHOD)) {
      oldMethodDeclaration();
    } else {
      int next = itemPeek();

      errorRaise(
        "Method declaration: expected 'Declarator' but found '%s'".formatted(protoName(next))
      );

      return;
    }
  }

  private void oldMethodDeclarator() {
    execute(this::identifier);

    oldFormalParameterList();
  }

  private void oldModifier() {
    int proto = protoNext();

    var keyword = Keyword.get(proto);

    switch (keyword) {
      case ABSTRACT -> abstractFound(1);

      case PUBLIC -> publicFound(1);

      default -> {}
    }

    codeAdd(ByteCode.KEYWORD, proto);

    last(_KEYWORD);
  }

  private void oldModifierList() {
    publicFound(NULL);

    if (itemIs(ByteProto.MODIFIER)) {
      if (lastIs(_ANNOTATION)) {
        codeAdd(Whitespace.AFTER_ANNOTATION);
      }

      execute(this::oldModifier);

      while (itemTest(this::isModifierOrAnnotation)) {
        codeAdd(Whitespace.MANDATORY);

        executeSwitch(this::oldModifierOrAnnotation);
      }
    }
  }

  private void oldModifierOrAnnotation(int proto) {
    switch (proto) {
      case ByteProto.ANNOTATION -> oldAnnotation();

      case ByteProto.MODIFIER -> oldModifier();
    }

    last(_KEYWORD);
  }

  private void oldReturnStatement() {
    execute(this::returnKeyword);

    if (itemTest(ByteProto::isExpressionStart)) {
      oldExpression();

      codeAdd(Symbol.SEMICOLON);
    } else {
      errorRaise("expected start of expression");
    }
  }

  private void oldStatement() {
    int start = itemPeek();

    oldStatement0(start);
  }

  private void oldStatement0(int start) {
    last(_START);

    switch (start) {
      case ByteProto.ARRAY_TYPE,
           ByteProto.PARAMETERIZED_TYPE,
           ByteProto.PRIMITIVE_TYPE,
           ByteProto.TYPE_VARIABLE -> localVariableDeclaration();

      case ByteProto.BLOCK -> execute(this::oldBlock);

      case ByteProto.CLASS_INSTANCE_CREATION,
           ByteProto.EXPRESSION_NAME,
           ByteProto.INVOKE,
           ByteProto.THIS -> {
        oldStatementPrimary();

        codeAdd(Symbol.SEMICOLON);
      }

      case ByteProto.CLASS_TYPE -> {
        int next = itemPeekAhead();

        if (next != ByteProto.IDENTIFIER) {
          oldStatementPrimary();

          codeAdd(Symbol.SEMICOLON);
        } else {
          localVariableDeclaration();
        }
      }

      case ByteProto.IF_CONDITION -> ifStatement();

      case ByteProto.RETURN -> oldReturnStatement();

      case ByteProto.SUPER -> superInvocationWithKeyword();

      case ByteProto.SUPER_INVOCATION -> superInvocation();

      case ByteProto.THROW -> throwStatement();

      case ByteProto.VAR -> localVariableDeclaration();

      default -> errorRaise(
        "no-op statement start '%s'".formatted(protoName(start))
      );
    }
  }

  private void oldStatementPrimary() {
    oldExpression();
  }

  private void oldType(int proto) {
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

  private void oldTypeParameter() {
    execute(this::identifier);

    if (itemMore()) {
      codeAdd(Whitespace.MANDATORY);
      codeAdd(Keyword.EXTENDS);
      codeAdd(Whitespace.MANDATORY);

      executeSwitch(this::oldType);

      while (itemMore()) {
        codeAdd(Whitespace.OPTIONAL);
        codeAdd(Symbol.AMPERSAND);
        codeAdd(Whitespace.OPTIONAL);

        executeSwitch(this::oldType);
      }
    }
  }

  private void oldTypeParameterList() {
    if (lastIs(_KEYWORD)) {
      codeAdd(Whitespace.OPTIONAL);
    }

    last(_START);

    codeAdd(Symbol.LEFT_ANGLE_BRACKET);

    execute(this::oldTypeParameter);

    while (itemIs(ByteProto.TYPE_PARAMETER)) {
      codeAdd(Symbol.COMMA);
      last(_COMMA);

      execute(this::oldTypeParameter);
    }

    codeAdd(Symbol.RIGHT_ANGLE_BRACKET);

    last(_SYMBOL);
  }

  private void oldVariableDeclarator() {
    execute(this::identifier);

    if (itemTest(this::isVariableInitializerOrClassType)) {
      preSymbol();
      codeAdd(Symbol.ASSIGNMENT);
      last(_SYMBOL);

      oldVariableInitializer();
    }
  }

  private void oldVariableInitializer() {
    if (itemTest(this::isExpressionStartOrClassType)) {
      oldExpression();
    } else if (itemIs(ByteProto.ARRAY_INITIALIZER)) {
      execute(this::oldArrayInitializer);
    } else {
      errorRaise();
    }
  }

  private void operator() {
    preSymbol();

    codeAdd(ByteCode.SYMBOL, protoNext());

    last(_SYMBOL);
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

    last(_SYMBOL);
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

    last(_TYPE);
  }

  private void parameterShort() {
    executeSwitch(this::parameterType);

    if (protoIs(ByteProto.ELLIPSIS)) {
      execute(this::ellipsis);
    }

    execute(this::identifier);
  }

  private void parameterSwitcher(int proto) {
    switch (last()) {
      case _IDENTIFIER -> comma();
    }

    switch (proto) {
      case ByteProto.PARAMETER_SHORT -> parameterShort();

      default -> errorRaise(
        "no-op formal parameter '%s'".formatted(protoName(proto))
      );
    }
  }

  private void parameterType(int proto) {
    type(proto);

    last(_KEYWORD);
  }

  private void preDot() {
    switch (last()) {
      case _COMMA -> codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

      case _IDENTIFIER,
           _PRIMARY,
           _TYPE -> codeAdd(Symbol.DOT);

      case _KEYWORD -> codeAdd(Whitespace.MANDATORY);

      case _NEW_LINE -> codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);

      case _SYMBOL -> codeAdd(Whitespace.OPTIONAL);
    }
  }

  private void preIdentifier() {
    switch (last()) {
      case _COMMA -> codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

      case _IDENTIFIER,
           _KEYWORD,
           _TYPE -> codeAdd(Whitespace.MANDATORY);

      case _NEW_LINE -> codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);

      case _SYMBOL -> codeAdd(Whitespace.OPTIONAL);
    }
  }

  private void preKeyword() {
    switch (last()) {
      case _ANNOTATION -> codeAdd(Whitespace.AFTER_ANNOTATION);

      case _BLOCK -> codeAdd(Whitespace.OPTIONAL);

      case _IDENTIFIER,
           _KEYWORD,
           _PRIMARY,
           _TYPE -> codeAdd(Whitespace.MANDATORY);

      case _SYMBOL -> codeAdd(Whitespace.OPTIONAL);
    }
  }

  private void preSymbol() {
    switch (last()) {
      case _IDENTIFIER,
           _KEYWORD,
           _PRIMARY,
           _TYPE -> codeAdd(Whitespace.OPTIONAL);
    }
  }

  private void preType() {
    switch (last()) {
      case _ANNOTATION -> codeAdd(Whitespace.AFTER_ANNOTATION);

      case _COMMA -> codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

      case _KEYWORD -> codeAdd(Whitespace.MANDATORY);

      case _NEW_LINE -> codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);

      case _SYMBOL -> codeAdd(Whitespace.OPTIONAL);
    }
  }

  private void primitiveLiteral() {
    switch (last()) {
      case _COMMA -> codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

      case _KEYWORD -> codeAdd(Whitespace.MANDATORY);

      case _NEW_LINE -> codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);

      case _SYMBOL -> codeAdd(Whitespace.OPTIONAL);
    }

    codeAdd(ByteCode.PRIMITIVE_LITERAL, protoNext());
  }

  private void primitiveType() {
    preType();

    codeAdd(ByteCode.KEYWORD, protoNext());

    last(_TYPE);
  }

  private boolean protoIs(int value) {
    if (protoMore()) {
      int proto = protoPeek();

      return proto == value;
    } else {
      return false;
    }
  }

  private boolean protoMore() {
    if (!error()) {
      return protoIndex < protoArray.length;
    } else {
      return false;
    }
  }

  private String protoName(int proto) {
    return switch (proto) {
      case ByteProto.ARRAY_TYPE -> "Array Type";

      case ByteProto.CLASS -> "Class Keyword";

      case ByteProto.DECLARATION_NAME -> "Declaration Name";

      case ByteProto.IF_CONDITION -> "If Condition";

      case ByteProto.INTERFACE -> "Interface";

      case ByteProto.INVOKE -> "Method invocation";

      case ByteProto.MODIFIER -> "Modifier";

      case ByteProto.PARAMETER -> "Formal Parameter";

      case ByteProto.PRIMITIVE_LITERAL -> "Primitive Literal";

      case ByteProto.TYPE_PARAMETER -> "Type Parameter";

      case ByteProto.V -> "V";

      default -> Integer.toString(proto);
    };
  }

  private int protoNext() { return protoArray[protoIndex++]; }

  private int protoPeek() {
    return protoArray[protoIndex];
  }

  private boolean protoTest(IntPredicate predicate) {
    if (protoMore()) {
      int proto = protoPeek();

      return predicate.test(proto);
    } else {
      return false;
    }
  }

  private int publicFound() { return stackArray[1]; }

  private void publicFound(int value) { stackArray[1] = value; }

  private void returnKeyword() {
    preKeyword();

    codeAdd(Keyword.RETURN);

    last(_KEYWORD);
  }

  private void returnType() {
    executeSwitch(this::type);
  }

  private void semicolon() {
    codeAdd(Symbol.SEMICOLON);

    last(_SYMBOL);
  }

  private int simpleName() { return stackArray[0]; }

  private void simpleName(int value) { stackArray[0] = value; }

  private void singleExecute(int offset, Action action) {
    int location = protoArray[offset + 1];

    int returnTo = protoIndex;

    protoIndex = location;

    action.execute();

    protoIndex = returnTo;
  }

  private int singleSet(int property) {
    int proto = protoNext();
    int value = protoNext();

    if (property == NULL) {
      property = stackIndex;
      protoArray = IntArrays.growIfNecessary(protoArray, stackIndex + 1);
      protoArray[stackIndex++] = proto;
      protoArray[stackIndex++] = value;
    } else {
      protoArray[property + 0] = proto;
      protoArray[property + 1] = value;
    }

    return property;
  }

  private void singleSwitch(int offset, SwitchAction action) {
    protoIndex = offset;

    int proto = protoArray[protoIndex++];

    int location = protoArray[protoIndex++];

    int returnTo = protoIndex;

    protoIndex = location;

    action.execute(proto);

    protoIndex = returnTo;
  }

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
    switch (last()) {
      case _COMMA -> codeAdd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);

      case _KEYWORD -> codeAdd(Whitespace.OPTIONAL);

      case _NEW_LINE -> codeAdd(Whitespace.BEFORE_FIRST_LINE_CONTENT);

      case _SYMBOL -> codeAdd(Whitespace.OPTIONAL);
    }

    codeAdd(ByteCode.STRING_LITERAL, protoNext());

    last(_PRIMARY);
  }

  private void superInvocation() {
    superKeyword();

    execute(this::oldArgumentList);

    codeAdd(Symbol.SEMICOLON);
  }

  private void superInvocationWithKeyword() {
    execute(this::superKeyword);

    codeAdd(Symbol.LEFT_PARENTHESIS);

    codeAdd(Symbol.RIGHT_PARENTHESIS);

    codeAdd(Symbol.SEMICOLON);
  }

  private void superKeyword() {
    preKeyword();

    codeAdd(Keyword.SUPER);

    last(_KEYWORD);
  }

  private void symbol(Symbol symbol) {
    preSymbol();

    codeAdd(symbol);

    last(_SYMBOL);
  }

  private void thisKeyword() {
    preKeyword();

    codeAdd(Keyword.THIS);

    last(_PRIMARY);
  }

  private void throwKeyword() {
    preKeyword();

    codeAdd(Keyword.THROW);

    last(_KEYWORD);
  }

  private void throwStatement() {
    execute(this::throwKeyword);

    if (itemTest(ByteProto::isExpressionStart)) {
      oldExpression();

      codeAdd(Symbol.SEMICOLON);
    } else {
      errorRaise("expected start of expression");
    }
  }

  private int topLevel() { return stackArray[3]; }

  private void topLevel(int value) { stackArray[3] = value; }

  private void topLevelDeclaration() {
    topLevel(1);

    oldDeclarationAnnotationList();

    oldModifierList();

    int next = itemPeek();

    switch (next) {
      case ByteProto.CLASS -> {
        beforeTopLevelTypeDeclaration();

        oldClassDeclaration();
      }

      case ByteProto.CLASS_DECLARATION -> execute(this::classDeclaration);

      case ByteProto.ENUM -> {
        beforeTopLevelTypeDeclaration();

        oldEnumDeclaration();
      }

      case ByteProto.ENUM_DECLARATION -> execute(this::enumDeclaration);

      case ByteProto.INTERFACE -> {
        beforeTopLevelTypeDeclaration();

        oldInterfaceDeclaration();
      }

      case ByteProto.INTERFACE_DECLARATION -> execute(this::interfaceDeclaration);

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

  private void typeDeclaration(Keyword keyword) {
    int start = stackIndex;

    last(_START);

    int annotations = NULL,
        modifiers = NULL,
        typeParameters = NULL,
        name = NULL,
        extendsClause = NULL,
        implementsClause = NULL,
        enumConstants = NULL,
        body = NULL;

    while (protoMore()) {
      int proto = protoPeek();

      if (proto == ByteProto.END_ELEMENT) {
        break;
      }

      switch (proto) {
        case ByteProto.ANNOTATION -> annotations = listAdd(annotations);

        case ByteProto.CLASS_DECLARATION -> body = listAdd(body);

        case ByteProto.CONSTRUCTOR_DECLARATION -> body = listAdd(body);

        case ByteProto.DECLARATION_NAME -> name = singleSet(name);

        case ByteProto.ENUM_CONSTANT -> enumConstants = listAdd(enumConstants);

        case ByteProto.ENUM_DECLARATION -> body = listAdd(body);

        case ByteProto.EXTENDS_CLAUSE -> extendsClause = listAdd(extendsClause);

        case ByteProto.FIELD_DECLARATION -> body = listAdd(body);

        case ByteProto.IMPLEMENTS_CLAUSE -> implementsClause = listAdd(implementsClause);

        case ByteProto.INTERFACE_DECLARATION -> body = listAdd(body);

        case ByteProto.METHOD_DECLARATION -> body = listAdd(body);

        case ByteProto.MODIFIER,
             ByteProto.MODIFIERS -> modifiers = listAdd(modifiers);

        case ByteProto.TYPE_PARAMETER -> typeParameters = listAdd(typeParameters);

        default -> {
          throw new UnsupportedOperationException(
            "Implement me :: " + protoName(proto)
          );
        }
      }
    }

    if (annotations != NULL) {
      listExecute(annotations, this::annotation);
    }

    if (modifiers != NULL) {
      listSwitch(modifiers, this::modifierSwitcher);
    }

    keyword(keyword);

    if (name != NULL) {
      singleExecute(name, this::typeDeclarationName);
    } else {
      unnamedType();
    }

    if (extendsClause != NULL) {
      last(_IDENTIFIER);

      listExecute(extendsClause, this::extendsClause);
    }

    if (implementsClause != NULL) {
      last(_IDENTIFIER);

      listExecute(implementsClause, this::implementsClause);
    }

    body(enumConstants, body);

    stackIndex = start;
  }

  private void typeDeclarationName() {
    int proto = protoNext();

    typeDeclarationNameValue(proto);

    preIdentifier();

    codeAdd(ByteCode.IDENTIFIER, proto);

    last(_IDENTIFIER);
  }

  private void typeDeclarationNameValue(int proto) {
    simpleName(proto);

    var topLevel = topLevel() != NULL;

    if (topLevel) {
      var publicFound = publicFound() != NULL;

      var fileName = (String) objectget(proto);

      autoImports.fileName(publicFound, fileName);
    }
  }

  private void typeKeyword(Keyword keyword) {
    codeAdd(keyword);

    codeAdd(Whitespace.MANDATORY);

    int proto = protoNext();

    codeAdd(ByteCode.IDENTIFIER, proto);

    typeDeclarationNameValue(proto);

    last(_IDENTIFIER);
  }

  private void typeParameter() {
    switch (last()) {
      case _KEYWORD -> {
        codeAdd(Whitespace.OPTIONAL);
        codeAdd(Symbol.LEFT_ANGLE_BRACKET);
        last(_START);
      }

      case _IDENTIFIER -> comma();

      case _START -> {
        codeAdd(Symbol.LEFT_ANGLE_BRACKET);
        last(_START);
      }
    }

    execute(this::identifier);

    if (elemMore()) {
      extendsKeyword();

      executeSwitch(this::type);

      while (elemMore()) {
        symbol(Symbol.AMPERSAND);

        executeSwitch(this::type);
      }
    }
  }

  private void typeParameterListEnd() {
    codeAdd(Symbol.RIGHT_ANGLE_BRACKET);

    last(_SYMBOL);
  }

  private void typeVariable() {
    preType();

    codeAdd(ByteCode.IDENTIFIER, protoNext());

    last(_TYPE);
  }

  private void unnamed() {
    if (lastIs(_KEYWORD)) {
      codeAdd(Whitespace.MANDATORY);
    }

    codeAdd(ByteCode.IDENTIFIER, object("unnamed"));
  }

  private void unnamedType() {
    if (lastIs(_KEYWORD)) {
      codeAdd(Whitespace.MANDATORY);
    }

    int proto = object("Unnamed");

    typeDeclarationNameValue(proto);

    codeAdd(ByteCode.IDENTIFIER, proto);
  }

  private void v() {
    preDot();

    codeAdd(ByteCode.IDENTIFIER, protoNext());
  }

  private void varKeyword() {
    preKeyword();

    codeAdd(Keyword.VAR);

    last(_KEYWORD);
  }

  private void voidKeyword() {
    preType();

    codeAdd(Keyword.VOID);

    last(_KEYWORD);
  }

}