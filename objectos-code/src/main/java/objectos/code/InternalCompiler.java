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
  private interface JumpTarget {
    void execute();
  }

  private static final int _START = 0,
      _ANNOTATIONS = 1,
      _ARGS = 2,
      _BLOCK = 3,
      _BODY = 4,
      _CLAUSE = 5,
      _CLAUSE_TYPE = 6,
      _CONSTRUCTOR = 7,
      _DIMS = 8,
      _ENUM_CONSTANTS = 9,
      _IMPORTS = 10,
      _INIT = 11,
      _LHS = 12,
      _LOCAL_VAR = 13,
      _METHOD = 14,
      _MODIFIERS = 15,
      _NAME = 16,
      _NL = 17,
      _PACKAGE = 18,
      _PRIMITIVE = 19,
      _RESERVED_DOT = 20,
      _RESERVED_SPACE = 21,
      _SLOT = 22,
      _STATEMENT = 23,
      _TYPE = 24,
      _TYPE_DECLARATION = 25,
      _TYPE_PARAMETER = 26,
      _VAR = 27;

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
      stackpush(
        FALSE, // 2 = public found
        ByteProto.COMPILATION_UNIT,
        _START
      );
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
    codeadd(Symbol.COMMERCIAL_AT);

    stackpush(ByteProto.ANNOTATION, _START);

    element();

    int state = contextpop();

    switch (state) {
      case _TYPE -> {}

      case _VALUE -> {
        codeadd(Symbol.RIGHT_PARENTHESIS);
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
            codeadd(Symbol.LEFT_PARENTHESIS);
            stackset(_VALUE);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void arrayAccessExpression() {
    jmp(this::expression);

    int count = protonxt();

    for (int i = 0; i < count; i++) {
      codeadd(Symbol.LEFT_SQUARE_BRACKET);
      jmp(this::expression);
      codeadd(Symbol.RIGHT_SQUARE_BRACKET);
    }
  }

  private void arrayAccessExpression(int context, int state, int item) {
    switch (item) {
      case ByteProto.EXPRESSION_NAME -> {
        stackset(_ARGS);

        switch (state) {
          case _START -> stackset(_NAME);

          case _ARGS -> righAndLeftSquareBracket();

          case _NAME -> codeadd(Symbol.LEFT_SQUARE_BRACKET);

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void arrayInitializer() {
    stackpush(ByteProto.ARRAY_INITIALIZER, _START);
    codeadd(Symbol.LEFT_CURLY_BRACKET);
    element();
    codeadd(Symbol.RIGHT_CURLY_BRACKET);
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

  private void assignOperator() {
    codeadd(Whitespace.OPTIONAL);
    codeadd(Symbol.ASSIGNMENT);
    codeadd(Whitespace.OPTIONAL);
  }

  private void block() {
    codeadd(Symbol.LEFT_CURLY_BRACKET);
    codeadd(Indentation.ENTER_BLOCK);

    stackpush(
      FALSE, // 2=NL
      ByteProto.BLOCK,
      _START
    );

    element();

    int state = contextpop();
    stackpop();
    switch (state) {
      case _START -> {
        codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Symbol.RIGHT_CURLY_BRACKET);
      }

      case _BLOCK -> {
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Symbol.RIGHT_CURLY_BRACKET);
      }

      case _RESERVED_DOT -> {
        codeadd(Symbol.LEFT_PARENTHESIS);
        codeadd(Symbol.RIGHT_PARENTHESIS);
        codeadd(Symbol.SEMICOLON);
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Symbol.RIGHT_CURLY_BRACKET);
      }

      case _STATEMENT -> {
        codeadd(Symbol.SEMICOLON);
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Symbol.RIGHT_CURLY_BRACKET);
      }

      default -> stubPop(ByteProto.BLOCK, state);
    }
  }

  private void block(int context, int state, int item) {
    switch (item) {
      case ByteProto.METHOD_INVOCATION -> {
        stackset(_STATEMENT);

        switch (state) {
          case _START -> blockBeforeFirstStatement();

          case _STATEMENT -> blockEndOfStatement();

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.RETURN -> {
        stackset(_RESERVED_SPACE);

        switch (state) {
          case _START -> blockBeforeFirstStatement();

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void blockBeforeFirstStatement() {
    codeadd(Whitespace.NEW_LINE);
    codeadd(Indentation.EMIT);
  }

  private void blockBeforeNextStatement() {
    codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
    codeadd(Indentation.EMIT);
  }

  private void blockEndOfStatement() {
    codeadd(Symbol.SEMICOLON);
    blockBeforeNextStatement();
  }

  private void body() {
    codeadd(Symbol.LEFT_CURLY_BRACKET);
    codeadd(Indentation.ENTER_BLOCK);

    stackpush(
      constructorName, // 2 = constructor name
      ByteProto.BODY,
      _START
    );

    constructorName = NULL;

    element();

    int state = contextpop();
    stackpop(); // constructor name

    switch (state) {
      case _START -> {
        codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Symbol.RIGHT_CURLY_BRACKET);
      }

      case _ENUM_CONSTANTS, _INIT, _NAME -> {
        codeadd(Symbol.SEMICOLON);
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Symbol.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Symbol.RIGHT_CURLY_BRACKET);
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
            stackset(_ANNOTATIONS);
          }

          case _BODY -> bodyBeforeNextMember(_ANNOTATIONS);

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.ARRAY_INITIALIZER -> {
        switch (state) {
          case _NAME -> {
            codeadd(Whitespace.OPTIONAL);
            codeadd(Symbol.ASSIGNMENT);
            codeadd(Whitespace.OPTIONAL);
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
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stackset(_TYPE);
          }

          case _ANNOTATIONS -> {
            codeadd(Whitespace.AFTER_ANNOTATION);
            codeadd(Indentation.EMIT);
            stackset(_TYPE);
          }

          case _BODY -> {
            codeadd(Whitespace.BEFORE_NEXT_MEMBER);
            codeadd(Indentation.EMIT);
            stackset(_TYPE);
          }

          case _METHOD -> {
            codeadd(Symbol.SEMICOLON);
            codeadd(Whitespace.BEFORE_NEXT_MEMBER);
            codeadd(Indentation.EMIT);
            stackset(_TYPE);
          }

          case _MODIFIERS -> {
            codeadd(Whitespace.MANDATORY);
            stackset(_TYPE);
          }

          case _NAME, _INIT -> {
            codeadd(Symbol.SEMICOLON);
            codeadd(Whitespace.BEFORE_NEXT_MEMBER);
            codeadd(Indentation.EMIT);
            stackset(_TYPE);
          }

          case _TYPE_PARAMETER -> {
            codeadd(Symbol.RIGHT_ANGLE_BRACKET);
            codeadd(Whitespace.OPTIONAL);
            stackset(_TYPE);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.BLOCK -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stackset(_BODY);
          }

          case _CONSTRUCTOR, _METHOD, _MODIFIERS -> {
            codeadd(Whitespace.OPTIONAL);
            stackset(_BODY);
          }

          case _NAME -> {
            codeadd(Symbol.LEFT_PARENTHESIS);
            codeadd(Symbol.RIGHT_PARENTHESIS);
            codeadd(Whitespace.OPTIONAL);
            stackset(_BODY);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.BODY -> {
        switch (state) {
          case _TYPE_DECLARATION -> {
            codeadd(Whitespace.OPTIONAL);
            stackset(_BODY);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.CLASS -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stackset(_TYPE_DECLARATION);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.CONSTRUCTOR -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stackset(_CONSTRUCTOR);
          }

          case _BODY -> bodyBeforeNextMember(_CONSTRUCTOR);

          case _MODIFIERS -> {
            codeadd(Whitespace.MANDATORY);
            stackset(_CONSTRUCTOR);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.ENUM_CONSTANT -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stackset(_ENUM_CONSTANTS);
          }

          case _ENUM_CONSTANTS -> {
            codeadd(Symbol.COMMA);
            bodyBeforeNextMember(_ENUM_CONSTANTS);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.FIELD_NAME -> {
        switch (state) {
          case _TYPE -> {
            codeadd(Whitespace.MANDATORY);
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
            codeadd(Whitespace.MANDATORY);
            stackset(_METHOD);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.METHOD_INVOCATION -> {
        switch (state) {
          case _NAME -> {
            assignOperator();
            stackset(_INIT);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.MODIFIER -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stackset(_MODIFIERS);
          }

          case _ANNOTATIONS -> {
            codeadd(Whitespace.AFTER_ANNOTATION);
            codeadd(Indentation.EMIT);
            stackset(_MODIFIERS);
          }

          case _BODY -> bodyBeforeNextMember(_MODIFIERS);

          case _ENUM_CONSTANTS -> {
            codeadd(Symbol.SEMICOLON);
            bodyBeforeNextMember(_MODIFIERS);
          }

          case _MODIFIERS -> {
            codeadd(Whitespace.MANDATORY);
          }

          case _NAME -> {
            codeadd(Symbol.SEMICOLON);
            codeadd(Whitespace.BEFORE_NEXT_MEMBER);
            codeadd(Indentation.EMIT);
            stackset(_MODIFIERS);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.STRING_LITERAL -> {
        switch (state) {
          case _NAME -> {
            codeadd(Whitespace.OPTIONAL);
            codeadd(Symbol.ASSIGNMENT);
            codeadd(Whitespace.OPTIONAL);
            stackset(_INIT);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.TYPE_PARAMETER -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            codeadd(Symbol.LEFT_ANGLE_BRACKET);
            stackset(_TYPE_PARAMETER);
          }

          case _BODY -> {
            bodyBeforeNextMember(_TYPE_PARAMETER);
            codeadd(Symbol.LEFT_ANGLE_BRACKET);
            stackset(_TYPE_PARAMETER);
          }

          case _MODIFIERS -> {
            codeadd(Whitespace.OPTIONAL);
            codeadd(Symbol.LEFT_ANGLE_BRACKET);
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

  private void bodyBeforeNextMember(int nextState) {
    codeadd(Whitespace.BEFORE_NEXT_MEMBER);
    codeadd(Indentation.EMIT);
    stackset(nextState);
  }

  private void classInstanceCreation() {
    codeadd(Keyword.NEW);

    int context = ByteProto.CLASS_INSTANCE_CREATION;
    stackpush(context, _START);

    element();

    int state = contextpop();
    switch (state) {
      case _TYPE -> {
        codeadd(Symbol.LEFT_PARENTHESIS);
        codeadd(Symbol.RIGHT_PARENTHESIS);
      }

      case _ARGS -> {
        codeadd(Symbol.RIGHT_PARENTHESIS);
      }

      default -> stubPop(context, state);
    }
  }

  private void classInstanceCreation(int context, int state, int item) {
    switch (item) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.MANDATORY);
            stackset(_TYPE);
          }

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.STRING_LITERAL -> {
        switch (state) {
          case _TYPE -> {
            codeadd(Symbol.LEFT_PARENTHESIS);
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

  private void classType() {
    var packageIndex = protonxt();

    var packageName = (String) objectget(packageIndex);

    autoImports.classTypePackageName(packageName);

    var count = protonxt();

    switch (count) {
      case 1 -> {
        var n1Index = protonxt();

        var n1 = (String) objectget(n1Index);

        autoImports.classTypeSimpleName(n1);

        int instruction = autoImports.classTypeInstruction();

        switch (instruction) {
          case 1 -> {
            codeadd(ByteCode.IDENTIFIER, n1Index);
          }

          default -> {
            codeadd(ByteCode.IDENTIFIER, packageIndex);
            codeadd(Symbol.DOT);
            codeadd(ByteCode.IDENTIFIER, n1Index);
          }
        }
      }

      case 2 -> {
        var n1Index = protonxt();
        var n2Index = protonxt();

        var n1 = (String) objectget(n1Index);
        var n2 = (String) objectget(n2Index);

        autoImports.classTypeSimpleName(n1);
        autoImports.classTypeSimpleName(n2);

        int instruction = autoImports.classTypeInstruction();

        switch (instruction) {
          case 1 -> {
            codeadd(ByteCode.IDENTIFIER, n2Index);
          }

          case 2 -> {
            codeadd(ByteCode.IDENTIFIER, n1Index);
            codeadd(Symbol.DOT);
            codeadd(ByteCode.IDENTIFIER, n2Index);
          }

          default -> {
            codeadd(ByteCode.IDENTIFIER, packageIndex);
            codeadd(Symbol.DOT);
            codeadd(ByteCode.IDENTIFIER, n1Index);
            codeadd(Symbol.DOT);
            codeadd(ByteCode.IDENTIFIER, n2Index);
          }
        }
      }

      default -> {
        throw new UnsupportedOperationException(
          "Implement me :: count=" + count);
      }
    }
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

  private void codeadd(Symbol value) { codeadd(ByteCode.SEPARATOR, value.ordinal()); }

  private void codeadd(Whitespace value) { codeadd(ByteCode.WHITESPACE, value.ordinal()); }

  private void commaAndSpace() {
    $codeadd(Symbol.COMMA);
    $codeadd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);
  }

  private void compilationUnit(int context, int state, int item) {
    switch (item) {
      case ByteProto.ANNOTATION -> {
        switch (state) {
          case _START -> {
            stackset(_ANNOTATIONS);
          }

          case _BODY, _IMPORTS -> {
            codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            stackset(_ANNOTATIONS);
          }

          case _MODIFIERS -> {
            codeadd(Whitespace.MANDATORY);
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
            codeadd(Whitespace.OPTIONAL);
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
            codeadd(Whitespace.AFTER_ANNOTATION);
            stackset(_TYPE_DECLARATION);
          }

          case _PACKAGE, _IMPORTS, _BODY -> {
            codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            stackset(_TYPE_DECLARATION);
          }

          case _MODIFIERS -> {
            codeadd(Whitespace.MANDATORY);
            stackset(_TYPE_DECLARATION);
          }

          default -> stubState(context, state, item);
        }

        int publicFound = stackpeek(2);
        int index = protopeek();
        var simpleName = (String) objectget(index);
        autoImports.fileName(publicFound == TRUE, simpleName);
        stackset(2, FALSE);
      }

      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _CLAUSE -> {
            codeadd(Whitespace.MANDATORY);
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
            codeadd(Whitespace.MANDATORY);
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
            codeadd(Whitespace.AFTER_ANNOTATION);
            stackset(_MODIFIERS);
          }

          case _BODY, _IMPORTS, _PACKAGE -> {
            codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            stackset(_MODIFIERS);
          }

          case _MODIFIERS -> {
            codeadd(Whitespace.MANDATORY);
            stackset(_MODIFIERS);
          }

          default -> stubState(context, state, item);
        }

        int index = protopeek();
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

    codeadd(ByteCode.IDENTIFIER, nameIndex);

    methodDeclaration(_CLAUSE);
  }

  private void constructorInvocation(int proto, Keyword keyword) {
    codeadd(keyword);

    stackpush(
      NULL, // 2 = slot
      proto,
      _RECV
    );

    element();

    methodInvocationPop(proto);
  }

  private void context(int context, int state, int item) {
    switch (context) {
      case ByteProto.ANNOTATION -> annotation(context, state, item);

      case ByteProto.ARRAY_ACCESS_EXPRESSION -> arrayAccessExpression(context, state, item);

      case ByteProto.ARRAY_INITIALIZER -> arrayInitializer(context, state, item);

      case ByteProto.ARRAY_TYPE -> arrayType(context, state, item);

      case ByteProto.BLOCK -> block(context, state, item);

      case ByteProto.BODY -> body(context, state, item);

      case ByteProto.CLASS_INSTANCE_CREATION -> classInstanceCreation(context, state, item);

      case ByteProto.COMPILATION_UNIT -> compilationUnit(context, state, item);

      case ByteProto.ENUM_CONSTANT -> methodInvocation(context, state, item);

      case ByteProto.METHOD -> methodDeclaration(context, state, item);

      case ByteProto.METHOD_INVOCATION -> methodInvocation(context, state, item);

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType(context, state, item);

      case ByteProto.SUPER_INVOCATION -> methodInvocation(context, state, item);

      case ByteProto.TYPE_PARAMETER -> typeParameter(context, state, item);

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

  private void dot() {
    int value1 = protonxt();
    int value2 = protonxt();
    dotItem(value1);
    codeadd(Symbol.DOT);
    dotItem(value2);
  }

  private void dotItem(int value) {
    protoIndex = value;

    int item = protonxt();

    item(item);
  }

  private void element() {
    int size = protonxt();
    int start = protoIndex;
    int max = start + size;

    for (int i = start; i < max; i++) {
      int context = stackpeek(1);

      int state = stackpeek(0);

      protoIndex = protoArray[i];

      int item = protonxt();

      context(context, state, item);

      item(item);
    }
  }

  private void enumConstant() {
    int proto = ByteProto.ENUM_CONSTANT;
    stackpush(proto, _START);

    element();

    int state = contextpop();
    switch (state) {
      case _ARGS, _ASSIGNEE, _REFERENCE -> {
        codeadd(Symbol.RIGHT_PARENTHESIS);
      }

      case _RECV -> {}

      case _REFERENCE_SLOT, _SLOT -> {
        codeadd(Indentation.EXIT_PARENTHESIS);
        codeadd(Indentation.EMIT);
        codeadd(Symbol.RIGHT_PARENTHESIS);
      }

      default -> stubPop(proto, state);
    }
  }

  private void expression() {
    int proto = protonxt();

    switch (proto) {
      case ByteProto.EXPRESSION_NAME -> codeadd(ByteCode.IDENTIFIER, protonxt());

      default -> warn("no-op expression '%s'".formatted(protoname(proto)));
    }
  }

  private void item(int item) {
    switch (item) {
      case ByteProto.ANNOTATION -> annotation();

      case ByteProto.ARRAY_ACCESS_EXPRESSION -> arrayAccessExpression();

      case ByteProto.ARRAY_DIMENSION -> {
        codeadd(Symbol.LEFT_SQUARE_BRACKET);
        codeadd(Symbol.RIGHT_SQUARE_BRACKET);
      }

      case ByteProto.ARRAY_INITIALIZER -> arrayInitializer();

      case ByteProto.ARRAY_TYPE -> arrayType();

      case ByteProto.AUTO_IMPORTS -> {}

      case ByteProto.BLOCK -> block();

      case ByteProto.BODY -> body();

      case ByteProto.CLASS -> typeKeyword(Keyword.CLASS);

      case ByteProto.CLASS_INSTANCE_CREATION -> classInstanceCreation();

      case ByteProto.CLASS_TYPE -> classType();

      case ByteProto.CONSTRUCTOR -> constructor();

      case ByteProto.DOT -> dot();

      case ByteProto.ELLIPSIS -> codeadd(Symbol.ELLIPSIS);

      case ByteProto.ENUM -> typeKeyword(Keyword.ENUM);

      case ByteProto.ENUM_CONSTANT -> enumConstant();

      case ByteProto.EXPRESSION_NAME -> codeadd(ByteCode.IDENTIFIER, protonxt());

      case ByteProto.EXTENDS -> codeadd(Keyword.EXTENDS);

      case ByteProto.FIELD_NAME -> codeadd(ByteCode.IDENTIFIER, protonxt());

      case ByteProto.IDENTIFIER -> codeadd(ByteCode.IDENTIFIER, protonxt());

      case ByteProto.IMPLEMENTS -> codeadd(Keyword.IMPLEMENTS);

      case ByteProto.INTERFACE -> typeKeyword(Keyword.INTERFACE);

      case ByteProto.METHOD -> methodDeclaration(_START);

      case ByteProto.METHOD_INVOCATION -> methodInvocation(_START);

      case ByteProto.MODIFIER -> codeadd(ByteCode.RESERVED_KEYWORD, protonxt());

      case ByteProto.NEW_LINE -> codeadd(Whitespace.NEW_LINE);

      case ByteProto.PACKAGE -> packageKeyword();

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType();

      case ByteProto.PRIMITIVE_LITERAL -> codeadd(ByteCode.PRIMITIVE_LITERAL, protonxt());

      case ByteProto.PRIMITIVE_TYPE -> codeadd(ByteCode.RESERVED_KEYWORD, protonxt());

      case ByteProto.RETURN -> codeadd(Keyword.RETURN);

      case ByteProto.STRING_LITERAL -> codeadd(ByteCode.STRING_LITERAL, protonxt());

      case ByteProto.SUPER -> codeadd(Keyword.SUPER);

      case ByteProto.SUPER_INVOCATION -> constructorInvocation(item, Keyword.SUPER);

      case ByteProto.THIS -> codeadd(Keyword.THIS);

      case ByteProto.TYPE_PARAMETER -> typeParameter();

      case ByteProto.TYPE_VARIABLE -> codeadd(ByteCode.IDENTIFIER, protonxt());

      case ByteProto.VAR -> codeadd(Keyword.VAR);

      case ByteProto.VOID -> codeadd(Keyword.VOID);

      default -> warn(
        "no-op item '%s'".formatted(protoname(item))
      );
    }
  }

  private void jmp(JumpTarget target) {
    int location = protonxt();
    int returnTo = protoIndex;
    protoIndex = location;
    target.execute();
    protoIndex = returnTo;
  }

  private void methodDeclaration(int initialState) {
    int proto = ByteProto.METHOD;
    stackpush(proto, initialState);
    element();
    int state = contextpop();
    switch (state) {
      case _CLAUSE -> {
        codeadd(Symbol.LEFT_PARENTHESIS);
        codeadd(Symbol.RIGHT_PARENTHESIS);
      }

      case _NAME -> {
        codeadd(Symbol.RIGHT_PARENTHESIS);
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
            codeadd(Symbol.LEFT_PARENTHESIS);
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
            codeadd(Whitespace.MANDATORY);
            stackset(_NAME);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void methodInvocation(int initialState) {
    int proto = ByteProto.METHOD_INVOCATION;

    stackpush(
      NULL, // 2 = slot
      proto,
      initialState
    );

    element();

    methodInvocationPop(proto);
  }

  private void methodInvocation(int context, int state, int item) {
    switch (item) {
      case ByteProto.ARRAY_ACCESS_EXPRESSION,
           ByteProto.DOT,
           ByteProto.EXPRESSION_NAME -> {
        stackset(_ARGS);

        switch (state) {
          case _ARGS -> commaAndSpace();

          case _NAME -> codeadd(Symbol.LEFT_PARENTHESIS);

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.IDENTIFIER -> {
        stackset(_NAME);

        switch (state) {
          case _START -> {}

          default -> stubState(context, state, item);
        }
      }

      case ByteProto.NEW_LINE -> {
        switch (state) {
          case _ARGS -> {
            stackset(_SLOT);
            stackset(2, nop1());
          }

          case _NAME -> {
            stackset(_NL);
            codeadd(Symbol.LEFT_PARENTHESIS);
            codeadd(Indentation.ENTER_PARENTHESIS);
          }

          case _SLOT -> {}

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void methodInvocationPop(int proto) {
    var state = contextpop();
    stackpop(); // slot
    switch (state) {
      case _ARGS -> {
        codeadd(Symbol.RIGHT_PARENTHESIS);
      }

      case _NAME -> {
        codeadd(Symbol.LEFT_PARENTHESIS);
        codeadd(Symbol.RIGHT_PARENTHESIS);
      }

      case _SLOT -> {
        codeadd(Indentation.EXIT_PARENTHESIS);
        codeadd(Indentation.EMIT);
        codeadd(Symbol.RIGHT_PARENTHESIS);
      }

      default -> stubPop(proto, state);
    }
  }

  private int nop1() {
    var result = codeIndex;

    codeadd(ByteCode.NOP1, 0);

    return result;
  }

  private Object objectget(int index) {
    return objectArray[index];
  }

  private void packageKeyword() {
    codeadd(Keyword.PACKAGE);
    codeadd(Whitespace.MANDATORY);
    codeadd(ByteCode.IDENTIFIER, protonxt());
    codeadd(Symbol.SEMICOLON);
  }

  private void parameterizedType() {
    stackpush(ByteProto.PARAMETERIZED_TYPE, _START);
    element();
    var state = contextpop();
    switch (state) {
      case _ARGS -> {
        codeadd(Symbol.RIGHT_ANGLE_BRACKET);
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
            codeadd(Symbol.LEFT_ANGLE_BRACKET);
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

  private String protoname(int value) {
    return switch (value) {
      case ByteProto.ANNOTATION -> "Annotation";

      case ByteProto.ARRAY_ACCESS_EXPRESSION -> "Array Access Expression";

      case ByteProto.ARRAY_INITIALIZER -> "Array Init.";

      case ByteProto.ARRAY_TYPE -> "Array Type";

      case ByteProto.AUTO_IMPORTS -> "Auto Imports";

      case ByteProto.BLOCK -> "Block";

      case ByteProto.BODY -> "Body";

      case ByteProto.CLASS -> "Class";

      case ByteProto.CLASS_INSTANCE_CREATION -> "Class Instance Creation Expression";

      case ByteProto.CLASS_TYPE -> "Class Type";

      case ByteProto.COMPILATION_UNIT -> "Compilation Unit";

      case ByteProto.CONSTRUCTOR -> "Constructor";

      case ByteProto.DOT -> "Dot";

      case ByteProto.ELLIPSIS -> "Ellipsis";

      case ByteProto.ENUM -> "Enum";

      case ByteProto.ENUM_CONSTANT -> "Enum Constant";

      case ByteProto.EXPRESSION_NAME -> "Expression Name";

      case ByteProto.EXTENDS -> "Extends";

      case ByteProto.FIELD_NAME -> "Field Name";

      case ByteProto.IDENTIFIER -> "Identifier";

      case ByteProto.IMPLEMENTS -> "Implements";

      case ByteProto.INTERFACE -> "Interface";

      case ByteProto.METHOD -> "Method";

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

  private int protonxt() { return protoArray[protoIndex++]; }

  private int protopeek() { return protoArray[protoIndex]; }

  private void righAndLeftSquareBracket() {
    codeadd(Symbol.RIGHT_SQUARE_BRACKET);
    codeadd(Symbol.LEFT_SQUARE_BRACKET);
  }

  private void slot() {
    var slot = stackpeek(2);

    codeArray[slot + 0] = ByteCode.SEPARATOR;
    codeArray[slot + 1] = Symbol.COMMA.ordinal();

    codeadd(Indentation.EMIT);
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

  private void stackpush(int v0, int v1, int v2) {
    localArray = IntArrays.growIfNecessary(localArray, localIndex + 3);

    localArray[++localIndex] = v0;
    localArray[++localIndex] = v1;
    localArray[++localIndex] = v2;
  }

  private void stackset(int value) { localArray[localIndex] = value; }

  private void stackset(int offset, int value) { localArray[localIndex - offset] = value; }

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

  private void typeKeyword(Keyword keyword) {
    codeadd(keyword);
    codeadd(Whitespace.MANDATORY);
    int index = protonxt();
    codeadd(ByteCode.IDENTIFIER, index);
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
            codeadd(Whitespace.MANDATORY);
            codeadd(Keyword.EXTENDS);
            codeadd(Whitespace.MANDATORY);
            stackset(_TYPE);
          }

          case _TYPE -> {
            codeadd(Whitespace.OPTIONAL);
            codeadd(Symbol.AMPERSAND);
            codeadd(Whitespace.OPTIONAL);
            stackset(_TYPE);
          }

          default -> stubState(context, state, item);
        }
      }

      default -> stubItem(context, state, item);
    }
  }

  private void warn(String msg) {
    codeadd(Whitespace.NEW_LINE);
    codeadd(ByteCode.COMMENT, object(msg));
  }

}