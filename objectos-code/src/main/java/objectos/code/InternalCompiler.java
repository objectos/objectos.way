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

  private static class CompilationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CompilationException(String message) { super(message); }
  }

  @FunctionalInterface
  private interface ElementAction {
    void execute();
  }

  @FunctionalInterface
  private interface ItemAction {
    void execute(int proto);
  }

  @FunctionalInterface
  private interface Sub {
    void execute(int proto);
  }

  private static final int _START = 0,
      _ANNOTATIONS = 1,
      _ARGS = 2,
      _BODY = 4,
      _CLAUSE = 5,
      _CLAUSE_TYPE = 6,
      _CONSTRUCTOR = 7,
      _DIMS = 8,
      _ENUM_CONSTANTS = 9,
      _IMPORTS = 10,
      _INIT = 11,
      _METHOD = 14,
      _MODIFIERS = 15,
      _NAME = 16,
      _PACKAGE = 18,
      _TYPE = 24,
      _TYPE_DECLARATION = 25,
      _TYPE_PARAMETER = 26;

  private static final int _VALUE = 66;

  private static final int NULL = Integer.MIN_VALUE;

  private static final int FALSE = 0;

  private static final int TRUE = 1;

  private int constructorName;

  final void compile() {
    codeIndex = 0;

    constructorName = NULL;

    localIndex = -1;

    try {
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

    codeadd(ByteCode.EOF);
  }

  private void $codeadd(int v0, int v1) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
  }

  private void $codeadd(Symbol separator) {
    $codeadd(ByteCode.SEPARATOR, separator.ordinal());
  }

  private void $codeadd(Whitespace whitespace) {
    $codeadd(ByteCode.WHITESPACE, whitespace.ordinal());
  }

  private void annotation() {
    codeAdd(Symbol.COMMERCIAL_AT);

    stackpush(ByteProto.ANNOTATION, _START);

    element();

    int state = contextpop();

    switch (state) {
      case _TYPE -> {}

      case _VALUE -> {
        codeAdd(Symbol.RIGHT_PARENTHESIS);
      }

      default -> stubPop(ByteProto.ANNOTATION, state);
    }
  }

  private void annotation(int context, int state, int item) {
    switch (item) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> {
            stackset(_TYPE);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.STRING_LITERAL -> {
        switch (state) {
          case _TYPE -> {
            codeAdd(Symbol.LEFT_PARENTHESIS);
            stackset(_VALUE);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void arrayAccess(int proto) {
    int count = protoNext();

    itemExecute(this::expression);

    count--;

    for (int i = 0; i < count; i++) {
      codeAdd(Symbol.LEFT_SQUARE_BRACKET);
      itemExecute(this::expression);
      codeAdd(Symbol.RIGHT_SQUARE_BRACKET);
    }
  }

  private void arrayInitializer() {
    stackpush(ByteProto.ARRAY_INITIALIZER, _START);
    codeAdd(Symbol.LEFT_CURLY_BRACKET);
    element();
    codeAdd(Symbol.RIGHT_CURLY_BRACKET);
    contextpop();
  }

  private void arrayInitializer(int context, int state, int item) {
    switch (item) {
      case ByteProto.PRIMITIVE_LITERAL -> {
        switch (state) {
          case _START -> stackset(_BODY);

          case _BODY -> commaAndSpace();

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void arrayType() {
    stackpush(ByteProto.ARRAY_TYPE, _START);
    element();
    contextpop();
  }

  private void arrayType(int context, int state, int item) {
    switch (item) {
      case ByteProto.ARRAY_DIMENSION -> {
        switch (state) {
          case _TYPE -> {
            stackset(_DIMS);
          }

          case _DIMS -> {}

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.CLASS_TYPE,
           ByteProto.PARAMETERIZED_TYPE,
           ByteProto.PRIMITIVE_TYPE -> {
        switch (state) {
          case _START -> stackset(_TYPE);

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void assignment(int proto) {
    itemExecute(this::expression);
    codeAdd(Whitespace.OPTIONAL);
    itemExecute(this::operator);
    codeAdd(Whitespace.OPTIONAL);
    itemExecute(this::expression);
  }

  private void block() {
    codeAdd(Symbol.LEFT_CURLY_BRACKET);
    codeAdd(Indentation.ENTER_BLOCK);

    int count = protoNext();

    if (count > 0) {
      codeAdd(Whitespace.NEW_LINE);
      codeAdd(Indentation.EMIT);

      itemExecute(this::statement);

      for (int i = 1; i < count; i++) {
        codeAdd(Whitespace.BEFORE_NEXT_STATEMENT);
        codeAdd(Indentation.EMIT);

        itemExecute(this::statement);
      }

      codeAdd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
      codeAdd(Indentation.EXIT_BLOCK);
      codeAdd(Indentation.EMIT);
    } else {
      codeAdd(Whitespace.BEFORE_EMPTY_BLOCK_END);
      codeAdd(Indentation.EXIT_BLOCK);
    }

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);
  }

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
    protoAssert(proto, ByteProto.BODY);

    codeAdd(Symbol.LEFT_CURLY_BRACKET);

    elemExecute(this::bodyAction);

    codeAdd(Symbol.RIGHT_CURLY_BRACKET);
  }

  private void bodyAction() {}

  private void bodyBeforeNextMember(int nextState) {
    codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
    codeAdd(Indentation.EMIT);
    stackset(nextState);
  }

  private void bodyx(int context, int state, int item) {
    switch (item) {
      case ByteProto.ANNOTATION -> {
        switch (state) {
          case _START -> {
            codeAdd(Whitespace.BEFORE_FIRST_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_ANNOTATIONS);
          }

          case _BODY -> bodyBeforeNextMember(_ANNOTATIONS);

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.ARRAY_INITIALIZER -> {
        switch (state) {
          case _NAME -> {
            codeAdd(Whitespace.OPTIONAL);
            codeAdd(Symbol.ASSIGNMENT);
            codeAdd(Whitespace.OPTIONAL);
            stackset(_INIT);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.ARRAY_TYPE,
           ByteProto.CLASS_TYPE,
           ByteProto.PARAMETERIZED_TYPE,
           ByteProto.PRIMITIVE_TYPE,
           ByteProto.TYPE_VARIABLE,
           ByteProto.VOID -> {
        switch (state) {
          case _START -> {
            codeAdd(Whitespace.BEFORE_FIRST_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_TYPE);
          }

          case _ANNOTATIONS -> {
            codeAdd(Whitespace.AFTER_ANNOTATION);
            codeAdd(Indentation.EMIT);
            stackset(_TYPE);
          }

          case _BODY -> {
            codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_TYPE);
          }

          case _METHOD -> {
            codeAdd(Symbol.SEMICOLON);
            codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_TYPE);
          }

          case _MODIFIERS -> {
            codeAdd(Whitespace.MANDATORY);
            stackset(_TYPE);
          }

          case _NAME, _INIT -> {
            codeAdd(Symbol.SEMICOLON);
            codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_TYPE);
          }

          case _TYPE_PARAMETER -> {
            codeAdd(Symbol.RIGHT_ANGLE_BRACKET);
            codeAdd(Whitespace.OPTIONAL);
            stackset(_TYPE);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.BLOCK -> {
        switch (state) {
          case _START -> {
            codeAdd(Whitespace.BEFORE_FIRST_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_BODY);
          }

          case _CONSTRUCTOR, _METHOD, _MODIFIERS -> {
            codeAdd(Whitespace.OPTIONAL);
            stackset(_BODY);
          }

          case _NAME -> {
            codeAdd(Symbol.LEFT_PARENTHESIS);
            codeAdd(Symbol.RIGHT_PARENTHESIS);
            codeAdd(Whitespace.OPTIONAL);
            stackset(_BODY);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.BODY -> {
        switch (state) {
          case _TYPE_DECLARATION -> {
            codeAdd(Whitespace.OPTIONAL);
            stackset(_BODY);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.CLASS -> {
        switch (state) {
          case _START -> {
            codeAdd(Whitespace.BEFORE_FIRST_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_TYPE_DECLARATION);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.CONSTRUCTOR -> {
        switch (state) {
          case _START -> {
            codeAdd(Whitespace.BEFORE_FIRST_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_CONSTRUCTOR);
          }

          case _BODY -> bodyBeforeNextMember(_CONSTRUCTOR);

          case _MODIFIERS -> {
            codeAdd(Whitespace.MANDATORY);
            stackset(_CONSTRUCTOR);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.ENUM_CONSTANT -> {
        switch (state) {
          case _START -> {
            codeAdd(Whitespace.BEFORE_FIRST_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_ENUM_CONSTANTS);
          }

          case _ENUM_CONSTANTS -> {
            codeAdd(Symbol.COMMA);
            bodyBeforeNextMember(_ENUM_CONSTANTS);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.FIELD_NAME -> {
        switch (state) {
          case _TYPE -> {
            codeAdd(Whitespace.MANDATORY);
            stackset(_NAME);
          }

          case _NAME, _INIT -> {
            commaAndSpace();
            stackset(_NAME);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.METHOD -> {
        switch (state) {
          case _TYPE -> {
            codeAdd(Whitespace.MANDATORY);
            stackset(_METHOD);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.MODIFIER -> {
        switch (state) {
          case _START -> {
            codeAdd(Whitespace.BEFORE_FIRST_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_MODIFIERS);
          }

          case _ANNOTATIONS -> {
            codeAdd(Whitespace.AFTER_ANNOTATION);
            codeAdd(Indentation.EMIT);
            stackset(_MODIFIERS);
          }

          case _BODY -> bodyBeforeNextMember(_MODIFIERS);

          case _ENUM_CONSTANTS -> {
            codeAdd(Symbol.SEMICOLON);
            bodyBeforeNextMember(_MODIFIERS);
          }

          case _MODIFIERS -> {
            codeAdd(Whitespace.MANDATORY);
          }

          case _NAME -> {
            codeAdd(Symbol.SEMICOLON);
            codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
            codeAdd(Indentation.EMIT);
            stackset(_MODIFIERS);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.STRING_LITERAL -> {
        switch (state) {
          case _NAME -> {
            codeAdd(Whitespace.OPTIONAL);
            codeAdd(Symbol.ASSIGNMENT);
            codeAdd(Whitespace.OPTIONAL);
            stackset(_INIT);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.TYPE_PARAMETER -> {
        switch (state) {
          case _START -> {
            codeAdd(Whitespace.BEFORE_FIRST_MEMBER);
            codeAdd(Indentation.EMIT);
            codeAdd(Symbol.LEFT_ANGLE_BRACKET);
            stackset(_TYPE_PARAMETER);
          }

          case _BODY -> {
            bodyBeforeNextMember(_TYPE_PARAMETER);
            codeAdd(Symbol.LEFT_ANGLE_BRACKET);
            stackset(_TYPE_PARAMETER);
          }

          case _MODIFIERS -> {
            codeAdd(Whitespace.OPTIONAL);
            codeAdd(Symbol.LEFT_ANGLE_BRACKET);
            stackset(_TYPE_PARAMETER);
          }

          case _TYPE_PARAMETER -> {
            commaAndSpace();
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void classDeclaration() {
    itemExecute(this::classKeyword);

    codeAdd(Whitespace.OPTIONAL);

    itemExecute(this::body);
  }

  private void classInstanceCreation(int proto) {
    int count = protoNext();

    codeAdd(Keyword.NEW);

    codeAdd(Whitespace.MANDATORY);

    itemExecute(this::classType);

    count--;

    codeAdd(Symbol.LEFT_PARENTHESIS);

    if (count > 0) {
      itemExecute(this::expression);

      for (int i = 1; i < count; i++) {
        commaAndSpace();

        itemExecute(this::expression);
      }
    }

    codeAdd(Symbol.RIGHT_PARENTHESIS);
  }

  private void classKeyword(int proto) {
    protoAssert(proto, ByteProto.CLASS);

    codeAdd(Keyword.CLASS);

    codeAdd(Whitespace.MANDATORY);

    codeAdd(ByteCode.IDENTIFIER, protoNext());
  }

  private void classType(int proto) {
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
  }

  private void codeadd(int v0) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 0);

    codeArray[codeIndex++] = v0;
  }

  private void codeAdd(Indentation value) { codeAdd(ByteCode.INDENTATION, value.ordinal()); }

  private void codeAdd(int v0, int v1) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
  }

  private void codeAdd(Keyword value) { codeAdd(ByteCode.RESERVED_KEYWORD, value.ordinal()); }

  private void codeAdd(Symbol value) { codeAdd(ByteCode.SEPARATOR, value.ordinal()); }

  private void codeAdd(Whitespace value) { codeAdd(ByteCode.WHITESPACE, value.ordinal()); }

  private void commaAndSpace() {
    $codeadd(Symbol.COMMA);
    $codeadd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);
  }

  private void compilationUnit() {
    var contents = false;

    while (elemHasNext()) {
      if (contents) {
        codeAdd(Whitespace.BEFORE_NEXT_MEMBER);
      }

      typeDeclaration();

      contents = true;
    }
  }

  private void compilationUnitx(int context, int state, int item) {
    switch (item) {
      case ByteProto.ANNOTATION -> {
        switch (state) {
          case _START -> {
            stackset(_ANNOTATIONS);
          }

          case _BODY, _IMPORTS -> {
            codeAdd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            stackset(_ANNOTATIONS);
          }

          case _MODIFIERS -> {
            codeAdd(Whitespace.MANDATORY);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.AUTO_IMPORTS -> {
        switch (state) {
          case _START -> {
            codeadd(ByteCode.AUTO_IMPORTS0);
            stackset(_IMPORTS);
          }

          case _PACKAGE -> {
            codeadd(ByteCode.AUTO_IMPORTS1);
            stackset(_IMPORTS);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.BODY -> {
        switch (state) {
          case _CLAUSE_TYPE, _TYPE_DECLARATION -> {
            codeAdd(Whitespace.OPTIONAL);
            stackset(_BODY);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.CLASS,
           ByteProto.ENUM,
           ByteProto.INTERFACE -> {
        switch (state) {
          case _START -> {
            stackset(_TYPE_DECLARATION);
          }

          case _ANNOTATIONS -> {
            codeAdd(Whitespace.AFTER_ANNOTATION);
            stackset(_TYPE_DECLARATION);
          }

          case _PACKAGE, _IMPORTS, _BODY -> {
            codeAdd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            stackset(_TYPE_DECLARATION);
          }

          case _MODIFIERS -> {
            codeAdd(Whitespace.MANDATORY);
            stackset(_TYPE_DECLARATION);
          }

          default -> stubState(context, state, item);
        }

        int publicFound = stackpeek(2);
        int index = protoPeek();
        var simpleName = (String) objectget(index);
        autoImports.fileName(publicFound == TRUE, simpleName);
        stackset(2, FALSE);
      }

      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _CLAUSE -> {
            codeAdd(Whitespace.MANDATORY);
            stackset(_CLAUSE_TYPE);
          }

          case _CLAUSE_TYPE -> {
            commaAndSpace();
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.EXTENDS,
           ByteProto.IMPLEMENTS -> {
        switch (state) {
          case _CLAUSE_TYPE, _TYPE_DECLARATION -> {
            codeAdd(Whitespace.MANDATORY);
            stackset(_CLAUSE);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.MODIFIER -> {
        switch (state) {
          case _START -> {
            stackset(_MODIFIERS);
          }

          case _ANNOTATIONS -> {
            codeAdd(Whitespace.AFTER_ANNOTATION);
            stackset(_MODIFIERS);
          }

          case _BODY, _IMPORTS, _PACKAGE -> {
            codeAdd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            stackset(_MODIFIERS);
          }

          case _MODIFIERS -> {
            codeAdd(Whitespace.MANDATORY);
            stackset(_MODIFIERS);
          }

          default -> stubState(context, state, item);
        }

        int index = protoPeek();
        Keyword keyword = Keyword.get(index);
        if (keyword == Keyword.PUBLIC) {
          stackset(2, TRUE);
        }
      }

      case ByteProto.PACKAGE -> {
        switch (state) {
          case _START -> {
            stackset(_PACKAGE);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void constructor() {
    int nameIndex = NULL;

    int parent = stackpeek(1);
    if (parent == ByteProto.BODY) {
      nameIndex = stackpeek(2);
    }

    if (nameIndex == NULL) {
      nameIndex = object("Constructor");
    }

    codeAdd(ByteCode.IDENTIFIER, nameIndex);

    methodDeclaration(_CLAUSE);
  }

  private void context(int context, int state, int item) {
    switch (context) {
      case ByteProto.ANNOTATION -> annotation(context, state, item);

      case ByteProto.ARRAY_INITIALIZER -> arrayInitializer(context, state, item);

      case ByteProto.ARRAY_TYPE -> arrayType(context, state, item);

      case ByteProto.BODY -> bodyx(context, state, item);

      case ByteProto.COMPILATION_UNIT -> compilationUnitx(context, state, item);

      case ByteProto.METHOD -> methodDeclaration(context, state, item);

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType(context, state, item);

      case ByteProto.TYPE_PARAMETER -> typeParameter(context, state, item);

      default -> warn(
        "no-op context '%s'".formatted(protoName(context))
      );
    }
  }

  private int contextpop() {
    int state = localArray[localIndex];
    localIndex -= 2;
    return state;
  }

  private void element() {
    int size = protoNext();
    int start = protoIndex;
    int max = start + size;

    for (int i = start; i < max; i++) {
      int context = stackpeek(1);

      int state = stackpeek(0);

      protoIndex = protoArray[i];

      int item = protoNext();

      context(context, state, item);

      item(item);
    }
  }

  private void elemExecute(ElementAction action) {
    if (elemStart()) {
      action.execute();

      stackpop(); // pop max
    }
  }

  private boolean elemHasNext() {
    int max = stackpeek(0);

    return protoIndex < max;
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

  private void enumConstant() {
    itemExecute(this::identifier);

    int count = protoNext();

    if (count > 0) {
      invocationArguments(count);
    }
  }

  private void expression(int proto) {
    switch (proto) {
      case ByteProto.ARRAY_ACCESS -> arrayAccess(proto);

      case ByteProto.ASSIGNMENT -> assignment(proto);

      case ByteProto.CLASS_INSTANCE_CREATION -> classInstanceCreation(proto);

      case ByteProto.EXPRESSION_NAME -> expressionName(proto);

      case ByteProto.EXPRESSION_NAME_CHAIN -> expressionNameChain(proto);

      case ByteProto.STRING_LITERAL -> codeAdd(ByteCode.STRING_LITERAL, protoNext());

      case ByteProto.THIS -> thisKeyword();

      default -> warn("no-op expression '%s'".formatted(protoName(proto)));
    }
  }

  private void expressionName(int proto) {
    codeAdd(ByteCode.IDENTIFIER, protoNext());
  }

  private void expressionNameChain(int proto) {
    itemExecute(this::expressionNameQualifier);
    codeAdd(Symbol.DOT);
    itemExecute(this::expressionName);
  }

  private void expressionNameQualifier(int proto) {
    switch (proto) {
      case ByteProto.CLASS_TYPE -> classType(proto);

      default -> expression(proto);
    }
  }

  private void identifier(int proto) {
    codeAdd(ByteCode.IDENTIFIER, protoNext());
  }

  private void invocationArguments(int count) {
    codeAdd(Symbol.LEFT_PARENTHESIS);

    if (count > 0) {
      itemExecute(this::expression);

      for (int i = 1; i < count; i++) {
        commaAndSpace();

        itemExecute(this::expression);
      }
    }

    codeAdd(Symbol.RIGHT_PARENTHESIS);
  }

  private void item(int item) {
    switch (item) {
      case ByteProto.ANNOTATION -> annotation();

      case ByteProto.ARRAY_DIMENSION -> {
        codeAdd(Symbol.LEFT_SQUARE_BRACKET);
        codeAdd(Symbol.RIGHT_SQUARE_BRACKET);
      }

      case ByteProto.ARRAY_INITIALIZER -> arrayInitializer();

      case ByteProto.ARRAY_TYPE -> arrayType();

      case ByteProto.ASSIGNMENT -> assignment(item);

      case ByteProto.AUTO_IMPORTS -> {}

      case ByteProto.BLOCK -> block();

      case ByteProto.BODY -> body();

      case ByteProto.CLASS -> typeKeyword(Keyword.CLASS);

      case ByteProto.CLASS_INSTANCE_CREATION -> classInstanceCreation(item);

      case ByteProto.CLASS_TYPE -> classType(item);

      case ByteProto.CONSTRUCTOR -> constructor();

      case ByteProto.ELLIPSIS -> codeAdd(Symbol.ELLIPSIS);

      case ByteProto.ENUM -> typeKeyword(Keyword.ENUM);

      case ByteProto.ENUM_CONSTANT -> enumConstant();

      case ByteProto.EXPRESSION_NAME -> codeAdd(ByteCode.IDENTIFIER, protoNext());

      case ByteProto.EXTENDS -> codeAdd(Keyword.EXTENDS);

      case ByteProto.FIELD_NAME -> codeAdd(ByteCode.IDENTIFIER, protoNext());

      case ByteProto.IDENTIFIER -> codeAdd(ByteCode.IDENTIFIER, protoNext());

      case ByteProto.IMPLEMENTS -> codeAdd(Keyword.IMPLEMENTS);

      case ByteProto.INTERFACE -> typeKeyword(Keyword.INTERFACE);

      case ByteProto.METHOD -> methodDeclaration(_START);

      case ByteProto.MODIFIER -> codeAdd(ByteCode.RESERVED_KEYWORD, protoNext());

      case ByteProto.NEW_LINE -> codeAdd(Whitespace.NEW_LINE);

      case ByteProto.PACKAGE -> packageKeyword();

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType();

      case ByteProto.PRIMITIVE_LITERAL -> codeAdd(ByteCode.PRIMITIVE_LITERAL, protoNext());

      case ByteProto.PRIMITIVE_TYPE -> codeAdd(ByteCode.RESERVED_KEYWORD, protoNext());

      case ByteProto.RETURN -> codeAdd(Keyword.RETURN);

      case ByteProto.STRING_LITERAL -> codeAdd(ByteCode.STRING_LITERAL, protoNext());

      case ByteProto.SUPER -> codeAdd(Keyword.SUPER);

      case ByteProto.THIS -> codeAdd(Keyword.THIS);

      case ByteProto.TYPE_PARAMETER -> typeParameter();

      case ByteProto.TYPE_VARIABLE -> codeAdd(ByteCode.IDENTIFIER, protoNext());

      case ByteProto.VAR -> codeAdd(Keyword.VAR);

      case ByteProto.VOID -> codeAdd(Keyword.VOID);

      default -> warn(
        "no-op item '%s'".formatted(protoName(item))
      );
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

  private void methodDeclaration(int initialState) {
    int proto = ByteProto.METHOD;
    stackpush(proto, initialState);
    element();
    int state = contextpop();
    switch (state) {
      case _CLAUSE -> {
        codeAdd(Symbol.LEFT_PARENTHESIS);
        codeAdd(Symbol.RIGHT_PARENTHESIS);
      }

      case _NAME -> {
        codeAdd(Symbol.RIGHT_PARENTHESIS);
      }

      default -> stubPop(proto, state);
    }
  }

  private void methodDeclaration(int context, int state, int item) {
    switch (item) {
      case ByteProto.ARRAY_TYPE,
           ByteProto.CLASS_TYPE,
           ByteProto.PARAMETERIZED_TYPE,
           ByteProto.PRIMITIVE_TYPE,
           ByteProto.TYPE_VARIABLE -> {
        switch (state) {
          case _CLAUSE -> {
            codeAdd(Symbol.LEFT_PARENTHESIS);
            stackset(_TYPE);
          }

          case _NAME -> {
            commaAndSpace();
            stackset(_TYPE);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.ELLIPSIS -> {
        switch (state) {
          case _TYPE -> {}

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.IDENTIFIER -> {
        switch (state) {
          case _START -> {
            stackset(_CLAUSE);
          }

          case _TYPE -> {
            codeAdd(Whitespace.MANDATORY);
            stackset(_NAME);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private Object objectget(int index) {
    return objectArray[index];
  }

  private void operator(int proto) {
    codeAdd(ByteCode.SEPARATOR, protoNext());
  }

  private void packageKeyword() {
    codeAdd(Keyword.PACKAGE);
    codeAdd(Whitespace.MANDATORY);
    codeAdd(ByteCode.IDENTIFIER, protoNext());
    codeAdd(Symbol.SEMICOLON);
  }

  private void parameterizedType() {
    stackpush(ByteProto.PARAMETERIZED_TYPE, _START);
    element();
    var state = contextpop();
    switch (state) {
      case _ARGS -> {
        codeAdd(Symbol.RIGHT_ANGLE_BRACKET);
      }

      default -> stubPop(ByteProto.PARAMETERIZED_TYPE, state);
    }
  }

  private void parameterizedType(int context, int state, int item) {
    switch (item) {
      case ByteProto.CLASS_TYPE,
           ByteProto.TYPE_VARIABLE -> {
        switch (state) {
          case _START -> {
            stackset(_TYPE);
          }

          case _TYPE -> {
            codeAdd(Symbol.LEFT_ANGLE_BRACKET);
            stackset(_ARGS);
          }

          case _ARGS -> {
            commaAndSpace();
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void protoAssert(int actual, int expected) throws CompilationException {
    if (actual != expected) {
      throw new CompilationException(
        "ByteProto failed assertion: found '%s' but expected '%s'.".formatted(
          protoName(actual), protoName(expected)));
    }
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

  private void returnStatement() {
    codeAdd(Keyword.RETURN);

    codeAdd(Whitespace.MANDATORY);

    itemExecute(this::expression);

    codeAdd(Symbol.SEMICOLON);
  }

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

  private void stackset(int offset, int value) { localArray[localIndex - offset] = value; }

  private void statement(int proto) {
    switch (proto) {
      case ByteProto.BLOCK -> block();

      case ByteProto.SUPER -> superKeyword();

      case ByteProto.SUPER_INVOCATION -> superInvocation();

      case ByteProto.RETURN -> returnStatement();

      default -> {
        expression(proto);
        codeAdd(Symbol.SEMICOLON);
      }
    }
  }

  private void stubItem(int ctx, int state, int item) {
    warn("no-op item '%s' @ '%s' (state=%d)".formatted(
      protoName(item), protoName(ctx), state));
  }

  private void stubPop(int ctx, int state) {
    warn("no-op pop @ '%s' (state=%d)".formatted(
      protoName(ctx), state));
  }

  private void stubState(int ctx, int state, int item) {
    warn("no-op state @ '%s' (state=%d) item '%s'".formatted(
      protoName(ctx), state, protoName(item)));
  }

  private void superInvocation() {
    codeAdd(Keyword.SUPER);

    int count = protoNext();

    invocationArguments(count);

    codeAdd(Symbol.SEMICOLON);
  }

  private void superKeyword() {
    codeAdd(Keyword.SUPER);
    codeAdd(Symbol.LEFT_PARENTHESIS);
    codeAdd(Symbol.RIGHT_PARENTHESIS);
    codeAdd(Symbol.SEMICOLON);
  }

  private void thisKeyword() {
    codeAdd(Keyword.THIS);
  }

  private void typeDeclaration() {
    if (elemHasNext(ByteProto.CLASS)) {
      classDeclaration();
    }
  }

  private void typeKeyword(Keyword keyword) {
    codeAdd(keyword);
    codeAdd(Whitespace.MANDATORY);
    int index = protoNext();
    codeAdd(ByteCode.IDENTIFIER, index);
    constructorName = index;
  }

  private void typeParameter() {
    int proto = ByteProto.TYPE_PARAMETER;
    stackpush(proto, _START);
    element();
    contextpop();
  }

  private void typeParameter(int context, int state, int item) {
    switch (item) {
      case ByteProto.IDENTIFIER -> {
        switch (state) {
          case _START -> {
            stackset(_NAME);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _NAME -> {
            codeAdd(Whitespace.MANDATORY);
            codeAdd(Keyword.EXTENDS);
            codeAdd(Whitespace.MANDATORY);
            stackset(_TYPE);
          }

          case _TYPE -> {
            codeAdd(Whitespace.OPTIONAL);
            codeAdd(Symbol.AMPERSAND);
            codeAdd(Whitespace.OPTIONAL);
            stackset(_TYPE);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void warn(String msg) {
    codeAdd(Whitespace.NEW_LINE);
    codeAdd(ByteCode.COMMENT, object(msg));
  }

}