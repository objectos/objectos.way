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
package objectos.code2;

import static objectos.code2.ByteProto.NULL;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import objectos.util.IntArrays;

class InternalCompiler extends InternalApi {

  private static final int _START = 0;
  private static final int _ANNOTATIONS = 1;
  private static final int _PACKAGE = 2;
  private static final int _IMPORTS = 3;
  private static final int _MODS = 4;
  @SuppressWarnings("unused")
  private static final int _EXTENDS = 5, _EXTENDS_TYPE = 6;
  private static final int _IMPLEMENTS = 7, _IMPLEMENTS_TYPE = 8;
  private static final int _LCURLY = 9;
  private static final int _BODY = 10;
  private static final int _VOID = 11;
  private static final int _TYPE = 12;
  private static final int _RECV = 13;
  private static final int _NAME = 14;
  private static final int _INIT = 15;
  private static final int _DIMS = 16;
  private static final int _ARGS = 17;
  private static final int _NL = 18;
  private static final int _SLOT = 19;
  private static final int _EXPRESSION = 20;

  final void compile() {
    codeIndex = 0;

    rootIndex = -1;

    try {
      compilationUnit();
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
    }

    codeadd(ByteCode.EOF);
  }

  private void annotation(int self) {
    statepush(_START, self);
    elementx();
    var state = statepop(self);
    switch (state) {
      case _TYPE -> {}

      default -> stubpop(self, state);
    }
  }

  private void annotation(int item, int parent, int state) {
    switch (item) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> {
            codeadd(Separator.COMMERCIAL_AT);
            stateset(1, _TYPE);
          }

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void arrayInitializer(int item, int parent, int state) {
    switch (item) {
      case ByteProto.PRIMITIVE_LITERAL -> {
        switch (state) {
          case _START -> stateset(1, _BODY);

          case _BODY -> commaAndSpace();

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void arrayType(int self) {
    statepush(_START, self);
    elementx();
    statepop(self);
  }

  private void arrayType(int item, int parent, int state) {
    switch (item) {
      case ByteProto.ARRAY_DIMENSION -> {
        switch (state) {
          case _TYPE -> {
            stateset(1, _DIMS);
          }

          case _DIMS -> {}

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.CLASS_TYPE,
           ByteProto.PARAMETERIZED_TYPE,
           ByteProto.PRIMITIVE_TYPE -> {
        switch (state) {
          case _START -> stateset(1, _TYPE);

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void block(int self) {
    statepush(_START, self);
    codeadd(Separator.LEFT_CURLY_BRACKET);
    codeadd(Indentation.ENTER_BLOCK);
    elementx();
    var state = statepop(self);
    switch (state) {
      case _START -> {
        codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      default -> stubpop(self, state);
    }
  }

  private void block(int item, int parent, int state) {
    switch (item) {
      case ByteProto.CLASS_INSTANCE_CREATION,
           ByteProto.METHOD_INVOCATION,
           ByteProto.METHOD_INVOCATION_QUALIFIED -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.NEW_LINE);
            codeadd(Indentation.EMIT);
            stateset(1, _BODY);
          }

          case _BODY -> {
            codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
            codeadd(Indentation.EMIT);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.RETURN_STATEMENT -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.NEW_LINE);
            codeadd(Indentation.EMIT);
            stateset(1, _BODY);
          }

          case _BODY -> {
            codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
            codeadd(Indentation.EMIT);
          }

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void body(int self) {
    var parent = statepeek();

    var body = switch (parent) {
      case ByteProto.CLASS_DECLARATION -> {
        statepop(parent, 2);
        yield ByteProto.CLASS_BODY;
      }

      default -> {
        throw new UnsupportedOperationException("Implement me :: parent=" + warname(parent));
      }
    };

    statepush(_START, body);
    codeadd(Separator.LEFT_CURLY_BRACKET);
    codeadd(Indentation.ENTER_BLOCK);
    elementx();
    var state = statepop(body);
    switch (state) {
      case _START -> {
        codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _NAME, _INIT -> {
        codeadd(Separator.SEMICOLON);
        codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Indentation.EMIT);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      default -> stubpop(body, state);
    }
  }

  private void classBody(int item, int parent, int state) {
    switch (item) {
      case ByteProto.ANNOTATION -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(1, _ANNOTATIONS);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.ARRAY_INITIALIZER -> {
        switch (state) {
          case _NAME -> {
            codeadd(Whitespace.OPTIONAL);
            codeadd(Operator.ASSIGNMENT);
            codeadd(Whitespace.OPTIONAL);
            stateset(1, _INIT);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.ARRAY_TYPE,
           ByteProto.CLASS_TYPE,
           ByteProto.PARAMETERIZED_TYPE,
           ByteProto.PRIMITIVE_TYPE -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(1, _TYPE);
          }

          case _ANNOTATIONS -> {
            codeadd(Whitespace.AFTER_ANNOTATION);
            codeadd(Indentation.EMIT);
            stateset(1, _TYPE);
          }

          case _MODS -> {
            codeadd(Whitespace.MANDATORY);
            stateset(1, _TYPE);
          }

          case _NAME, _INIT -> {
            codeadd(Separator.SEMICOLON);
            codeadd(Whitespace.BEFORE_NEXT_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(1, _TYPE);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.BLOCK -> {
        switch (state) {
          case _MODS -> {
            codeadd(Whitespace.OPTIONAL);
            stateset(1, _BODY);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.CLASS0 -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(1, _BODY);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.IDENTIFIER -> {
        switch (state) {
          case _TYPE -> {
            codeadd(Whitespace.MANDATORY);
            stateset(1, _NAME);
          }

          case _VOID -> {
            codeadd(Whitespace.MANDATORY);
            stateset(1, _BODY);
            methodDeclaration();
          }

          case _NAME, _INIT -> {
            commaAndSpace();
            stateset(1, _NAME);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.MODIFIER -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(1, _MODS);
          }

          case _MODS -> {
            codeadd(Whitespace.MANDATORY);
          }

          case _NAME -> {
            codeadd(Separator.SEMICOLON);
            codeadd(Whitespace.BEFORE_NEXT_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(1, _MODS);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.STRING_LITERAL -> {
        switch (state) {
          case _NAME -> {
            codeadd(Whitespace.OPTIONAL);
            codeadd(Operator.ASSIGNMENT);
            codeadd(Whitespace.OPTIONAL);
            stateset(1, _INIT);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.VOID -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            codeadd(Indentation.EMIT);
            stateset(1, _VOID);
          }

          case _ANNOTATIONS -> {
            codeadd(Whitespace.AFTER_ANNOTATION);
            codeadd(Indentation.EMIT);
            stateset(1, _VOID);
          }

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void classDeclaration(int item, int parent, int state) {
    switch (item) {
      case ByteProto.BODY -> {
        switch (state) {
          case _START, _EXTENDS, _IMPLEMENTS, _IMPLEMENTS_TYPE -> {
            codeadd(Whitespace.OPTIONAL);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _IMPLEMENTS -> {
            codeadd(Whitespace.MANDATORY);
            stateset(1, _IMPLEMENTS_TYPE);
          }

          case _IMPLEMENTS_TYPE -> {
            commaAndSpace();
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.EXTENDS -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.MANDATORY);
            stateset(1, _EXTENDS);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.IMPLEMENTS -> {
        switch (state) {
          case _START, _EXTENDS -> {
            codeadd(Whitespace.MANDATORY);
            stateset(1, _IMPLEMENTS);
          }

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void classDeclarationPop(int state) {
    switch (state) {
      case _START, _EXTENDS -> {
        codeadd(Whitespace.OPTIONAL);
        codeadd(Separator.LEFT_CURLY_BRACKET);
        codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _LCURLY -> {
        codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        codeadd(Indentation.EXIT_BLOCK);
        codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void classInstanceCreation(int self) {
    codeadd(Keyword.NEW);
    statepush(_START, self);
    elementx();
    var state = statepop(self);
    switch (state) {
      case _TYPE -> {
        codeadd(Separator.LEFT_PARENTHESIS);
        codeadd(Separator.RIGHT_PARENTHESIS);
      }

      case _ARGS -> {
        codeadd(Separator.RIGHT_PARENTHESIS);
      }

      default -> stubpop(self, state);
    }
  }

  private void classInstanceCreation(int item, int parent, int state) {
    switch (item) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.MANDATORY);
            stateset(1, _TYPE);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.STRING_LITERAL -> {
        switch (state) {
          case _TYPE -> {
            codeadd(Separator.LEFT_PARENTHESIS);
            stateset(1, _ARGS);
          }

          case _ARGS -> commaAndSpace();

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void classType(int self) {
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
            codeadd(Separator.DOT);
            codeadd(ByteCode.IDENTIFIER, n1Index);
          }
        }
      }

      default -> {
        throw new UnsupportedOperationException(
          "Implement me :: count=" + count);
      }
    }
  }

  private void codeadd(Indentation indentation) {
    codeadd(ByteCode.INDENTATION, indentation.ordinal());
  }

  private void codeadd(int v0) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 0);

    codeArray[codeIndex++] = v0;
  }

  private void codeadd(int v0, int v1) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
  }

  private void codeadd(Keyword value) { codeadd(ByteCode.KEYWORD, value.ordinal()); }

  private void codeadd(Operator value) {
    codeadd(ByteCode.OPERATOR, value.ordinal());
  }

  private void codeadd(Separator value) { codeadd(ByteCode.SEPARATOR, value.ordinal()); }

  private void codeadd(Whitespace value) { codeadd(ByteCode.WHITESPACE, value.ordinal()); }

  private void commaAndSpace() {
    codeadd(Separator.COMMA);
    codeadd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);
  }

  private void compilationUnit() {
    var proto = ByteProto.COMPILATION_UNIT;

    var item = itemnxt();

    if (item != proto) {
      warn(
        "InternalCompiler error: expecting 'Compilation Unit' but found '%s'"
            .formatted(warname(item)));

      return;
    }

    statepush(_START, proto);
    elementx();
    while (!stateempty()) {
      var elem = statepop();
      var state = statepop();
      elementpop(proto, elem, state);
    }
  }

  private void compilationUnit(int item, int parent, int state) {
    switch (item) {
      case ByteProto.ANNOTATION -> {
        switch (state) {
          case _START -> {
            stateset(1, _ANNOTATIONS);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.AUTO_IMPORTS -> {
        switch (state) {
          case _START -> {
            codeadd(ByteCode.AUTO_IMPORTS0);
            stateset(1, _IMPORTS);
          }

          case _PACKAGE -> {
            codeadd(ByteCode.AUTO_IMPORTS1);
            stateset(1, _IMPORTS);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.CLASS0 -> {
        switch (state) {
          case _START -> {
            stateset(1, _BODY);
          }

          case _ANNOTATIONS -> {
            codeadd(Whitespace.AFTER_ANNOTATION);
            stateset(1, _BODY);
          }

          case _PACKAGE, _IMPORTS, _BODY -> {
            codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            stateset(1, _BODY);
          }

          case _MODS -> {
            codeadd(Whitespace.MANDATORY);
            stateset(1, _BODY);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.MODIFIER -> {
        switch (state) {
          case _START -> {
            stateset(1, _MODS);
          }

          case _BODY -> {
            codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            stateset(1, _MODS);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.PACKAGE -> {
        switch (state) {
          case _START -> {
            stateset(1, _PACKAGE);
          }

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void elementpop(int parent, int elem, int state) {
    switch (elem) {
      case ByteProto.CLASS_DECLARATION -> classDeclarationPop(state);

      case ByteProto.COMPILATION_UNIT -> {}

      default -> stubpop(parent, elem, state);
    }
  }

  private void elementx() {
    int size = itemnxt();

    switch (size) {
      case 0 -> elementx00();
      case 1 -> elementx01();
      case 2 -> elementx02();
      case 3 -> elementx03();
      case 4 -> elementx04();
      case 5 -> elementx05();
      case 6 -> elementx06();
      case 7 -> elementx07();
      case 9 -> elementx09();
      case 10 -> elementx10();
      case 11 -> elementx11();
      case 12 -> elementx12();
      case 15 -> elementx15();
      case 18 -> elementx18();

      default -> throw new UnsupportedOperationException(
        "Implement me :: size=" + size);
    }
  }

  // @formatter:off
  private void elementx00() {}

  private void elementx01() {
    int v0 = itemnxt();

    itemx(v0);
  }

  private void elementx02() {
    int v0 = itemnxt(); int v1 = itemnxt();

    itemx(v0); itemx(v1);
  }

  private void elementx03() {
    int v0 = itemnxt(); int v1 = itemnxt(); int v2 = itemnxt();

    itemx(v0); itemx(v1); itemx(v2);
  }

  private void elementx04() {
    int v0 = itemnxt(); int v1 = itemnxt(); int v2 = itemnxt(); int v3 = itemnxt();

    itemx(v0); itemx(v1); itemx(v2); itemx(v3);
  }

  private void elementx05() {
    int v0 = itemnxt(); int v1 = itemnxt(); int v2 = itemnxt(); int v3 = itemnxt();
    int v4 = itemnxt();

    itemx(v0); itemx(v1); itemx(v2); itemx(v3);
    itemx(v4);
  }

  private void elementx06() {
    int v0 = itemnxt(); int v1 = itemnxt(); int v2 = itemnxt(); int v3 = itemnxt();
    int v4 = itemnxt(); int v5 = itemnxt();

    itemx(v0); itemx(v1); itemx(v2); itemx(v3);
    itemx(v4); itemx(v5);
  }

  private void elementx07() {
    int v0 = itemnxt(); int v1 = itemnxt(); int v2 = itemnxt(); int v3 = itemnxt();
    int v4 = itemnxt(); int v5 = itemnxt(); int v6 = itemnxt();

    itemx(v0); itemx(v1); itemx(v2); itemx(v3);
    itemx(v4); itemx(v5); itemx(v6);
  }

  private void elementx09() {
    int v0 = itemnxt(); int v1 = itemnxt(); int v2 = itemnxt(); int v3 = itemnxt();
    int v4 = itemnxt(); int v5 = itemnxt(); int v6 = itemnxt(); int v7 = itemnxt();
    int v8 = itemnxt();

    itemx(v0); itemx(v1); itemx(v2); itemx(v3);
    itemx(v4); itemx(v5); itemx(v6); itemx(v7);
    itemx(v8);
  }

  private void elementx10() {
    int v0 = itemnxt(); int v1 = itemnxt(); int v2 = itemnxt(); int v3 = itemnxt();
    int v4 = itemnxt(); int v5 = itemnxt(); int v6 = itemnxt(); int v7 = itemnxt();
    int v8 = itemnxt(); int v9 = itemnxt();

    itemx(v0); itemx(v1); itemx(v2); itemx(v3);
    itemx(v4); itemx(v5); itemx(v6); itemx(v7);
    itemx(v8); itemx(v9);
  }

  private void elementx11() {
    int v00 = itemnxt(); int v01 = itemnxt(); int v02 = itemnxt(); int v03 = itemnxt();
    int v04 = itemnxt(); int v05 = itemnxt(); int v06 = itemnxt(); int v07 = itemnxt();
    int v08 = itemnxt(); int v09 = itemnxt(); int v10 = itemnxt();

    itemx(v00); itemx(v01); itemx(v02); itemx(v03);
    itemx(v04); itemx(v05); itemx(v06); itemx(v07);
    itemx(v08); itemx(v09); itemx(v10);
  }

  private void elementx12() {
    int v00 = itemnxt(); int v01 = itemnxt(); int v02 = itemnxt(); int v03 = itemnxt();
    int v04 = itemnxt(); int v05 = itemnxt(); int v06 = itemnxt(); int v07 = itemnxt();
    int v08 = itemnxt(); int v09 = itemnxt(); int v10 = itemnxt(); int v11 = itemnxt();

    itemx(v00); itemx(v01); itemx(v02); itemx(v03);
    itemx(v04); itemx(v05); itemx(v06); itemx(v07);
    itemx(v08); itemx(v09); itemx(v10); itemx(v11);
  }

  private void elementx15() {
    int v00 = itemnxt(); int v01 = itemnxt(); int v02 = itemnxt(); int v03 = itemnxt();
    int v04 = itemnxt(); int v05 = itemnxt(); int v06 = itemnxt(); int v07 = itemnxt();
    int v08 = itemnxt(); int v09 = itemnxt(); int v10 = itemnxt(); int v11 = itemnxt();
    int v12 = itemnxt(); int v13 = itemnxt(); int v14 = itemnxt();

    itemx(v00); itemx(v01); itemx(v02); itemx(v03);
    itemx(v04); itemx(v05); itemx(v06); itemx(v07);
    itemx(v08); itemx(v09); itemx(v10); itemx(v11);
    itemx(v12); itemx(v13); itemx(v14);
  }

  private void elementx18() {
    int v00 = itemnxt(); int v01 = itemnxt(); int v02 = itemnxt(); int v03 = itemnxt();
    int v04 = itemnxt(); int v05 = itemnxt(); int v06 = itemnxt(); int v07 = itemnxt();
    int v08 = itemnxt(); int v09 = itemnxt(); int v10 = itemnxt(); int v11 = itemnxt();
    int v12 = itemnxt(); int v13 = itemnxt(); int v14 = itemnxt(); int v15 = itemnxt();
    int v16 = itemnxt(); int v17 = itemnxt();

    itemx(v00); itemx(v01); itemx(v02); itemx(v03);
    itemx(v04); itemx(v05); itemx(v06); itemx(v07);
    itemx(v08); itemx(v09); itemx(v10); itemx(v11);
    itemx(v12); itemx(v13); itemx(v14); itemx(v15);
    itemx(v16); itemx(v17);
  }
  // @formatter:on

  private void expressionName(int item, int parent, int state) {
    switch (item) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> stateset(1, _NAME);

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.IDENTIFIER -> {
        switch (state) {
          case _START -> {
            stateset(1, _NAME);
          }

          case _NAME -> {
            codeadd(Separator.DOT);
          }

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void extendsClause(int item, int parent, int state) {
    switch (item) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.MANDATORY);
            stateset(1, _TYPE);
          }

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private int itemnxt() { return itemArray[itemIndex++]; }

  private void itemx(int location) {
    itemIndex = location;

    var item = itemnxt();

    itemx0parent(item);

    itemx1self(item);
  }

  private void itemx0parent(int item) {
    var parent = statepeek();
    var state = statepeek(1);

    switch (parent) {
      case ByteProto.ANNOTATION -> annotation(item, parent, state);

      case ByteProto.ARRAY_INITIALIZER -> arrayInitializer(item, parent, state);

      case ByteProto.ARRAY_TYPE -> arrayType(item, parent, state);

      case ByteProto.BLOCK -> block(item, parent, state);

      case ByteProto.CLASS_BODY -> classBody(item, parent, state);

      case ByteProto.CLASS_DECLARATION -> classDeclaration(item, parent, state);

      case ByteProto.CLASS_INSTANCE_CREATION -> classInstanceCreation(item, parent, state);

      case ByteProto.COMPILATION_UNIT -> compilationUnit(item, parent, state);

      case ByteProto.EXPRESSION_NAME -> expressionName(item, parent, state);

      case ByteProto.EXTENDS -> extendsClause(item, parent, state);

      case ByteProto.METHOD_DECLARATION -> methodDeclaration(item, parent, state);

      case ByteProto.METHOD_INVOCATION -> methodInvocation(item, parent, state);

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType(item, parent, state);

      case ByteProto.RETURN_STATEMENT -> returnStatement(item, parent, state);

      default -> warn(
        "Warning: unimplemented parent '%s' (state=%d) and item '%s'"
            .formatted(warname(parent), state, warname(item))
      );
    }
  }

  private void itemx1self(int self) {
    var parent = statepeek();
    var state = statepeek(1);

    switch (self) {
      case ByteProto.ANNOTATION -> annotation(self);

      case ByteProto.ARRAY_DIMENSION -> {
        codeadd(Separator.LEFT_SQUARE_BRACKET);
        codeadd(Separator.RIGHT_SQUARE_BRACKET);
      }

      case ByteProto.ARRAY_INITIALIZER -> {
        statepush(_START, self);
        codeadd(Separator.LEFT_CURLY_BRACKET);
        elementx();
        codeadd(Separator.RIGHT_CURLY_BRACKET);
        statepop(self);
      }

      case ByteProto.ARRAY_TYPE -> arrayType(self);

      case ByteProto.AUTO_IMPORTS -> {}

      case ByteProto.BODY -> body(self);

      case ByteProto.BLOCK -> block(self);

      case ByteProto.CLASS0 -> {
        statepush(_START, ByteProto.CLASS_DECLARATION);
        codeadd(Keyword.CLASS);
        codeadd(Whitespace.MANDATORY);
        codeadd(ByteCode.IDENTIFIER, protonxt());
      }

      case ByteProto.CLASS_INSTANCE_CREATION -> {
        classInstanceCreation(self);
        semicolonIfNecessary(parent);
      }

      case ByteProto.CLASS_TYPE -> classType(self);

      case ByteProto.EXTENDS -> {
        statepush(_START, self);
        codeadd(Keyword.EXTENDS);
        elementx();
        statepop(self, 2);
      }

      case ByteProto.IDENTIFIER -> codeadd(ByteCode.IDENTIFIER, protonxt());

      case ByteProto.EXPRESSION_NAME -> {
        statepush(_START, self);
        elementx();
        statepop(self);
      }

      case ByteProto.IMPLEMENTS -> codeadd(Keyword.IMPLEMENTS);

      case ByteProto.MODIFIER -> codeadd(ByteCode.KEYWORD, protonxt());

      case ByteProto.METHOD_INVOCATION, ByteProto.METHOD_INVOCATION_QUALIFIED -> {
        methodInvocation(self);
        semicolonIfNecessary(parent);
      }

      case ByteProto.NEW_LINE -> codeadd(Whitespace.NEW_LINE);

      case ByteProto.PACKAGE -> packageDeclaration(self);

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType(self);

      case ByteProto.PRIMITIVE_LITERAL -> codeadd(ByteCode.PRIMITIVE_LITERAL, itemnxt());

      case ByteProto.PRIMITIVE_TYPE -> codeadd(ByteCode.KEYWORD, itemnxt());

      case ByteProto.RETURN_STATEMENT -> {
        statepush(_START, self);
        codeadd(Keyword.RETURN);
        elementx();
        codeadd(Separator.SEMICOLON);
        statepop(self);
      }

      case ByteProto.STRING_LITERAL -> codeadd(ByteCode.STRING_LITERAL, itemnxt());

      case ByteProto.THIS -> codeadd(Keyword.THIS);

      case ByteProto.VOID -> codeadd(Keyword.VOID);

      default -> warn(
        "Warning: unimplemented itemxself '%' and parent '%s' (state=%d)"
            .formatted(warname(self), warname(parent), state)
      );
    }
  }

  private void methodDeclaration() {
    statepush(_START, ByteProto.METHOD_DECLARATION);
  }

  private void methodDeclaration(int item, int parent, int state) {
    switch (item) {
      case ByteProto.BLOCK -> {
        switch (state) {
          case _START -> {
            codeadd(Separator.LEFT_PARENTHESIS);
            codeadd(Separator.RIGHT_PARENTHESIS);
            codeadd(Whitespace.OPTIONAL);
            statepop(parent, 2);
          }

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void methodInvocation(int self) {
    int proto = ByteProto.METHOD_INVOCATION;
    statepush(
      NULL, // 2 = slot
      self == ByteProto.METHOD_INVOCATION ? _RECV : _START,
      proto
    );
    elementx();
    var state = statepop(proto);
    statepop();
    switch (state) {
      case _NAME -> {
        codeadd(Separator.LEFT_PARENTHESIS);
        codeadd(Separator.RIGHT_PARENTHESIS);
      }

      case _ARGS -> {
        codeadd(Separator.RIGHT_PARENTHESIS);
      }

      case _SLOT -> {
        codeadd(Indentation.EXIT_PARENTHESIS);
        codeadd(Indentation.EMIT);
        codeadd(Separator.RIGHT_PARENTHESIS);
      }

      default -> stubpop(proto, state);
    }
  }

  private void methodInvocation(int item, int parent, int state) {
    switch (item) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> stateset(1, _TYPE);

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.IDENTIFIER -> {
        switch (state) {
          case _TYPE -> {
            codeadd(Separator.DOT);
            stateset(1, _NAME);
          }

          case _RECV -> stateset(1, _NAME);

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.EXPRESSION_NAME,
           ByteProto.METHOD_INVOCATION,
           ByteProto.STRING_LITERAL -> {
        switch (state) {
          case _START -> stateset(1, _TYPE);

          case _NAME -> {
            codeadd(Separator.LEFT_PARENTHESIS);
            stateset(1, _ARGS);
          }

          case _ARGS -> commaAndSpace();

          case _NL -> {
            codeadd(Indentation.EMIT);
            stateset(1, _ARGS);
          }

          case _SLOT -> {
            var slot = stateget(2);

            codeArray[slot + 0] = ByteCode.SEPARATOR;
            codeArray[slot + 1] = Separator.COMMA.ordinal();

            codeadd(Indentation.EMIT);
            stateset(1, _ARGS);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.NEW_LINE -> {
        switch (state) {
          case _NAME -> {
            codeadd(Separator.LEFT_PARENTHESIS);
            codeadd(Indentation.ENTER_PARENTHESIS);
            stateset(1, _NL);
          }

          case _ARGS -> {
            stateset(1, _SLOT);
            stateset(2, nop1());
          }

          case _SLOT -> {}

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
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

  private void packageDeclaration(int self) {
    codeadd(Keyword.PACKAGE);
    codeadd(Whitespace.MANDATORY);
    codeadd(ByteCode.IDENTIFIER, protonxt());
    codeadd(Separator.SEMICOLON);
  }

  private void parameterizedType(int self) {
    statepush(_START, self);
    elementx();
    var state = statepop(self);
    switch (state) {
      case _ARGS -> {
        codeadd(Separator.RIGHT_ANGLE_BRACKET);
      }

      default -> stubpop(self, state);
    }
  }

  private void parameterizedType(int item, int parent, int state) {
    switch (item) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> {
            stateset(1, _TYPE);
          }

          case _TYPE -> {
            codeadd(Separator.LEFT_ANGLE_BRACKET);
            stateset(1, _ARGS);
          }

          case _ARGS -> {
            commaAndSpace();
          }

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private int protonxt() { return itemArray[itemIndex++]; }

  private void returnStatement(int item, int parent, int state) {
    switch (item) {
      case ByteProto.STRING_LITERAL -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.OPTIONAL);
            stateset(1, _EXPRESSION);
          }

          default -> stubstate(item, parent, state);
        }
      }

      case ByteProto.THIS -> {
        switch (state) {
          case _START -> {
            codeadd(Whitespace.MANDATORY);
            stateset(1, _EXPRESSION);
          }

          default -> stubstate(item, parent, state);
        }
      }

      default -> stubchild(item, parent, state);
    }
  }

  private void semicolonIfNecessary(int parent) {
    switch (parent) {
      case ByteProto.BLOCK -> codeadd(Separator.SEMICOLON);
    }
  }

  private boolean stateempty() { return rootIndex < 0; }

  private int stateget(int offset) {
    return rootArray[rootIndex - offset];
  }

  private int statepeek() { return rootArray[rootIndex]; }

  private int statepeek(int offset) { return rootArray[rootIndex - offset]; }

  private int statepop() { return rootArray[rootIndex--]; }

  private int statepop(int expected) {
    int elem = statepop();
    if (elem != expected) {
      throw new UnsupportedOperationException("Implement me");
    }
    return statepop();
  }

  private void statepop(int expected, int count) {
    var current = statepeek();

    if (current != expected) {
      throw new UnsupportedOperationException("Implement me");
    }

    rootIndex -= count;
  }

  private void statepush(int v0, int v1) {
    rootArray = IntArrays.growIfNecessary(rootArray, rootIndex + 2);

    rootArray[++rootIndex] = v0;
    rootArray[++rootIndex] = v1;
  }

  private void statepush(int v0, int v1, int v2) {
    rootArray = IntArrays.growIfNecessary(rootArray, rootIndex + 3);

    rootArray[++rootIndex] = v0;
    rootArray[++rootIndex] = v1;
    rootArray[++rootIndex] = v2;
  }

  private void stateset(int offset, int value) {
    rootArray[rootIndex - offset] = value;
  }

  private void stubchild(int item, int parent, int state) {
    warn(
      "Warning: unimplemented item '%s' at '%s' (state=%d)"
          .formatted(warname(item), warname(parent), state)
    );
  }

  private void stubpop(int location, int state) {
    warn(
      "Pop stub: at '%s' unexpected state=%d"
          .formatted(warname(location), state)
    );
  }

  private void stubpop(int parent, int elem, int state) {
    warn(
      "Pop stub: at '%s' popped '%s', state=%d"
          .formatted(warname(parent), warname(elem), state)
    );
  }

  private void stubstate(int self, int parent, int state) {
    warn(
      "Warning: unimplemented state '%s' (state=%d) with item '%s'"
          .formatted(warname(parent), state, warname(self))
    );
  }

  private void warn(String msg) {
    codeadd(Whitespace.NEW_LINE);
    codeadd(ByteCode.COMMENT, object(msg));
  }

  private String warname(int value) {
    return switch (value) {
      case ByteProto.ANNOTATION -> "Annotation";

      case ByteProto.ARRAY_INITIALIZER -> "Array Init.";

      case ByteProto.ARRAY_TYPE -> "Array Type";

      case ByteProto.BLOCK -> "Block";

      case ByteProto.BODY -> "Body";

      case ByteProto.CLASS0 -> "Class0";

      case ByteProto.CLASS_BODY -> "Class Body";

      case ByteProto.CLASS_INSTANCE_CREATION -> "Class Instance Creation";

      case ByteProto.CLASS_DECLARATION -> "Class Declaration";

      case ByteProto.CLASS_TYPE -> "Class Type";

      case ByteProto.COMPILATION_UNIT -> "Compilation Unit";

      case ByteProto.EXPRESSION_NAME -> "Expression Name";

      case ByteProto.EXTENDS -> "Extends";

      case ByteProto.IDENTIFIER -> "Identifier";

      case ByteProto.IMPLEMENTS -> "Implements";

      case ByteProto.METHOD_DECLARATION -> "Method Decl.";

      case ByteProto.METHOD_INVOCATION -> "Method Invocation";

      case ByteProto.MODIFIER -> "Modifier";

      case ByteProto.NEW_LINE -> "NL";

      case ByteProto.PACKAGE -> "Package";

      case ByteProto.PARAMETERIZED_TYPE -> "Parameterized Type";

      case ByteProto.PRIMITIVE_TYPE -> "Primitive Type";

      case ByteProto.RETURN_STATEMENT -> "Return Stmt.";

      case ByteProto.PRIMITIVE_LITERAL -> "Primitive Literal";

      case ByteProto.STRING_LITERAL -> "String Literal";

      case ByteProto.VOID -> "Void";

      default -> Integer.toString(value);
    };
  }

}