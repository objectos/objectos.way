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
import objectos.code.Separator;
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

  final void compile() {
    codeIndex = 0;

    rootIndex = -1;

    try {
      compilationUnit();
    } catch (RuntimeException e) {
      $codeadd(Whitespace.NEW_LINE);
      $codeadd(Whitespace.NEW_LINE);

      var collector = Collectors.joining(
        System.lineSeparator(),
        e.getMessage() + System.lineSeparator() + System.lineSeparator(),
        ""
      );

      var stackTrace = Stream.of(e.getStackTrace())
          .map(Object::toString)
          .collect(collector);

      $codeadd(ByteCode.RAW, object(stackTrace));
    }

    $codeadd(ByteCode.EOF);
  }

  private void $codeadd(Indentation indentation) {
    $codeadd(ByteCode.INDENTATION, indentation.ordinal());
  }

  private void $codeadd(int v0) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 0);

    codeArray[codeIndex++] = v0;
  }

  private void $codeadd(int v0, int v1) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
  }

  private void $codeadd(Keyword value) { $codeadd(ByteCode.KEYWORD, value.ordinal()); }

  private void $codeadd(Operator value) {
    $codeadd(ByteCode.OPERATOR, value.ordinal());
  }

  private void $codeadd(Separator value) { $codeadd(ByteCode.SEPARATOR, value.ordinal()); }

  private void $codeadd(Whitespace value) { $codeadd(ByteCode.WHITESPACE, value.ordinal()); }

  private void $element() {
    int size = $itemnxt();

    switch (size) {
      case 0 -> $element00();
      case 1 -> $element01();
      case 2 -> $element02();
      case 3 -> $element03();
      case 4 -> $element04();
      case 5 -> $element05();
      case 6 -> $element06();
      case 7 -> $element07();
      case 9 -> $element09();
      case 10 -> $element10();
      case 11 -> $element11();
      case 12 -> $element12();
      case 15 -> $element15();
      case 18 -> $element18();

      default -> throw new UnsupportedOperationException(
        "Implement me :: size=" + size);
    }
  }

  // @formatter:off
  private void $element00() {}

  private void $element01() {
    int v0 = $itemnxt();

    $item(v0);
  }

  private void $element02() {
    int v0 = $itemnxt(); int v1 = $itemnxt();

    $item(v0); $item(v1);
  }

  private void $element03() {
    int v0 = $itemnxt(); int v1 = $itemnxt(); int v2 = $itemnxt();

    $item(v0); $item(v1); $item(v2);
  }

  private void $element04() {
    int v0 = $itemnxt(); int v1 = $itemnxt(); int v2 = $itemnxt(); int v3 = $itemnxt();

    $item(v0); $item(v1); $item(v2); $item(v3);
  }

  private void $element05() {
    int v0 = $itemnxt(); int v1 = $itemnxt(); int v2 = $itemnxt(); int v3 = $itemnxt();
    int v4 = $itemnxt();

    $item(v0); $item(v1); $item(v2); $item(v3);
    $item(v4);
  }

  private void $element06() {
    int v0 = $itemnxt(); int v1 = $itemnxt(); int v2 = $itemnxt(); int v3 = $itemnxt();
    int v4 = $itemnxt(); int v5 = $itemnxt();

    $item(v0); $item(v1); $item(v2); $item(v3);
    $item(v4); $item(v5);
  }

  private void $element07() {
    int v0 = $itemnxt(); int v1 = $itemnxt(); int v2 = $itemnxt(); int v3 = $itemnxt();
    int v4 = $itemnxt(); int v5 = $itemnxt(); int v6 = $itemnxt();

    $item(v0); $item(v1); $item(v2); $item(v3);
    $item(v4); $item(v5); $item(v6);
  }

  private void $element09() {
    int v0 = $itemnxt(); int v1 = $itemnxt(); int v2 = $itemnxt(); int v3 = $itemnxt();
    int v4 = $itemnxt(); int v5 = $itemnxt(); int v6 = $itemnxt(); int v7 = $itemnxt();
    int v8 = $itemnxt();

    $item(v0); $item(v1); $item(v2); $item(v3);
    $item(v4); $item(v5); $item(v6); $item(v7);
    $item(v8);
  }

  private void $element10() {
    int v0 = $itemnxt(); int v1 = $itemnxt(); int v2 = $itemnxt(); int v3 = $itemnxt();
    int v4 = $itemnxt(); int v5 = $itemnxt(); int v6 = $itemnxt(); int v7 = $itemnxt();
    int v8 = $itemnxt(); int v9 = $itemnxt();

    $item(v0); $item(v1); $item(v2); $item(v3);
    $item(v4); $item(v5); $item(v6); $item(v7);
    $item(v8); $item(v9);
  }

  private void $element11() {
    int v00 = $itemnxt(); int v01 = $itemnxt(); int v02 = $itemnxt(); int v03 = $itemnxt();
    int v04 = $itemnxt(); int v05 = $itemnxt(); int v06 = $itemnxt(); int v07 = $itemnxt();
    int v08 = $itemnxt(); int v09 = $itemnxt(); int v10 = $itemnxt();

    $item(v00); $item(v01); $item(v02); $item(v03);
    $item(v04); $item(v05); $item(v06); $item(v07);
    $item(v08); $item(v09); $item(v10);
  }

  private void $element12() {
    int v00 = $itemnxt(); int v01 = $itemnxt(); int v02 = $itemnxt(); int v03 = $itemnxt();
    int v04 = $itemnxt(); int v05 = $itemnxt(); int v06 = $itemnxt(); int v07 = $itemnxt();
    int v08 = $itemnxt(); int v09 = $itemnxt(); int v10 = $itemnxt(); int v11 = $itemnxt();

    $item(v00); $item(v01); $item(v02); $item(v03);
    $item(v04); $item(v05); $item(v06); $item(v07);
    $item(v08); $item(v09); $item(v10); $item(v11);
  }

  private void $element15() {
    int v00 = $itemnxt(); int v01 = $itemnxt(); int v02 = $itemnxt(); int v03 = $itemnxt();
    int v04 = $itemnxt(); int v05 = $itemnxt(); int v06 = $itemnxt(); int v07 = $itemnxt();
    int v08 = $itemnxt(); int v09 = $itemnxt(); int v10 = $itemnxt(); int v11 = $itemnxt();
    int v12 = $itemnxt(); int v13 = $itemnxt(); int v14 = $itemnxt();

    $item(v00); $item(v01); $item(v02); $item(v03);
    $item(v04); $item(v05); $item(v06); $item(v07);
    $item(v08); $item(v09); $item(v10); $item(v11);
    $item(v12); $item(v13); $item(v14);
  }

  private void $element18() {
    int v00 = $itemnxt(); int v01 = $itemnxt(); int v02 = $itemnxt(); int v03 = $itemnxt();
    int v04 = $itemnxt(); int v05 = $itemnxt(); int v06 = $itemnxt(); int v07 = $itemnxt();
    int v08 = $itemnxt(); int v09 = $itemnxt(); int v10 = $itemnxt(); int v11 = $itemnxt();
    int v12 = $itemnxt(); int v13 = $itemnxt(); int v14 = $itemnxt(); int v15 = $itemnxt();
    int v16 = $itemnxt(); int v17 = $itemnxt();

    $item(v00); $item(v01); $item(v02); $item(v03);
    $item(v04); $item(v05); $item(v06); $item(v07);
    $item(v08); $item(v09); $item(v10); $item(v11);
    $item(v12); $item(v13); $item(v14); $item(v15);
    $item(v16); $item(v17);
  }
  // @formatter:on

  private void $elementpop(int parent, int elem, int state) {
    switch (elem) {
      case ByteProto.CLASS_DECLARATION -> classDeclarationPop(state);

      case ByteProto.COMPILATION_UNIT -> {}

      default -> $stubpop(parent, elem, state);
    }
  }

  private void $err(String msg) {
    $codeadd(Whitespace.NEW_LINE);
    $codeadd(ByteCode.COMMENT, object(msg));
  }

  private void $item(int location) {
    itemIndex = location;

    var child = $itemnxt();
    var parent = $statepeek();
    var state = $statepeek(1);

    switch (child) {
      case ByteProto.ANNOTATION -> annotation(child, parent, state);

      case ByteProto.ARRAY_DIMENSION -> arrayDimension(child, parent, state);

      case ByteProto.ARRAY_INITIALIZER -> arrayInitializer(child, parent, state);

      case ByteProto.ARRAY_TYPE -> arrayType(child, parent, state);

      case ByteProto.AUTO_IMPORTS -> autoImports(child, parent, state);

      case ByteProto.BODY -> body(child, parent, state);

      case ByteProto.BLOCK -> block(child, parent, state);

      case ByteProto.CLASS0 -> class0(child, parent, state);

      case ByteProto.CLASS_TYPE -> classType(child, parent, state);

      case ByteProto.EXTENDS -> extendsKeyword(child, parent, state);

      case ByteProto.IDENTIFIER -> identifier(child, parent, state);

      case ByteProto.IMPLEMENTS -> implementsKeyword(child, parent, state);

      case ByteProto.MODIFIER -> modifier(child, parent, state);

      case ByteProto.NEW_LINE -> newLine(child, parent, state);

      case ByteProto.PACKAGE -> packageKeyword(child, parent, state);

      case ByteProto.PARAMETERIZED_TYPE -> parameterizedType(child, parent, state);

      case ByteProto.PRIMITIVE_TYPE -> primitiveType(child, parent, state);

      case ByteProto.RETURN_STATEMENT -> returnStatement(child, parent, state);

      case ByteProto.VOID -> voidKeyword(child, parent, state);

      default -> expression(child, parent, state);
    }
  }

  private int $itemnxt() { return itemArray[itemIndex++]; }

  private Object $objectget(int index) {
    return objectArray[index];
  }

  private int $protonxt() { return itemArray[itemIndex++]; }

  private boolean $stateempty() { return rootIndex < 0; }

  private int $stateget(int offset) {
    return rootArray[rootIndex - offset];
  }

  private int $statepeek() { return rootArray[rootIndex]; }

  private int $statepeek(int offset) { return rootArray[rootIndex - offset]; }

  private int $statepop() { return rootArray[rootIndex--]; }

  private int $statepop(int expected) {
    int elem = $statepop();
    if (elem != expected) {
      throw new UnsupportedOperationException("Implement me");
    }
    return $statepop();
  }

  private void $statepop(int expected, int count) {
    var current = $statepeek();

    if (current != expected) {
      throw new UnsupportedOperationException("Implement me");
    }

    rootIndex -= count;
  }

  private void $statepush(int v0, int v1) {
    rootArray = IntArrays.growIfNecessary(rootArray, rootIndex + 2);

    rootArray[++rootIndex] = v0;
    rootArray[++rootIndex] = v1;
  }

  private void $statepush(int v0, int v1, int v2) {
    rootArray = IntArrays.growIfNecessary(rootArray, rootIndex + 3);

    rootArray[++rootIndex] = v0;
    rootArray[++rootIndex] = v1;
    rootArray[++rootIndex] = v2;
  }

  private void $stateset(int offset, int value) {
    rootArray[rootIndex - offset] = value;
  }

  private String $stub0(int value) {
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

  private void $stubparent(int self, int parent, int state) {
    $err(
      "Parent stub: at '%s' got parent '%s', state=%d"
          .formatted($stub0(self), $stub0(parent), state)
    );
  }

  private void $stubpop(int location, int state) {
    $err(
      "Pop stub: at '%s' unexpected state=%d"
          .formatted($stub0(location), state)
    );
  }

  private void $stubpop(int parent, int elem, int state) {
    $err(
      "Pop stub: at '%s' popped '%s', state=%d"
          .formatted($stub0(parent), $stub0(elem), state)
    );
  }

  private void $stubstate(int self, int parent, int state) {
    $err(
      "State stub: at '%s' with parent '%s', state=%d"
          .formatted($stub0(self), $stub0(parent), state)
    );
  }

  private void annotation(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.CLASS_BODY -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.EMIT);
            $stateset(1, _ANNOTATIONS);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.COMPILATION_UNIT -> {
        switch (state) {
          case _START -> {
            $stateset(1, _ANNOTATIONS);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $statepush(_START, self);
    $element();

    state = $statepop(self);
    switch (state) {
      case _TYPE -> {}

      default -> $stubpop(self, state);
    }
  }

  private void arrayDimension(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.ARRAY_TYPE -> {
        switch (state) {
          case _TYPE -> {
            $stateset(1, _DIMS);
          }

          case _DIMS -> {}

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $codeadd(Separator.LEFT_SQUARE_BRACKET);
    $codeadd(Separator.RIGHT_SQUARE_BRACKET);
  }

  private void arrayInitializer(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.CLASS_BODY -> {
        switch (state) {
          case _NAME -> {
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Operator.ASSIGNMENT);
            $codeadd(Whitespace.OPTIONAL);
            $stateset(1, _INIT);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $statepush(_START, self);
    $codeadd(Separator.LEFT_CURLY_BRACKET);
    $element();
    $codeadd(Separator.RIGHT_CURLY_BRACKET);
    $statepop(self);
  }

  private void arrayType(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.CLASS_BODY -> typeClassBody(self, parent, state);

      default -> $stubparent(self, parent, state);
    }

    $statepush(_START, self);
    $element();
    $statepop(self);
  }

  private void autoImports(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.COMPILATION_UNIT -> {
        switch (state) {
          case _START -> {
            $codeadd(ByteCode.AUTO_IMPORTS0);
            $stateset(1, _IMPORTS);
          }

          case _PACKAGE -> {
            $codeadd(ByteCode.AUTO_IMPORTS1);
            $stateset(1, _IMPORTS);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }
  }

  private void block(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.CLASS_BODY -> {
        switch (state) {
          case _MODS -> {
            $codeadd(Whitespace.OPTIONAL);
            $stateset(1, _BODY);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.METHOD_DECLARATION -> {
        switch (state) {
          case _START -> {
            $codeadd(Separator.LEFT_PARENTHESIS);
            $codeadd(Separator.RIGHT_PARENTHESIS);
            $codeadd(Whitespace.OPTIONAL);
            $statepop(parent, 2);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $statepush(_START, self);
    $codeadd(Separator.LEFT_CURLY_BRACKET);
    $codeadd(Indentation.ENTER_BLOCK);
    $element();

    state = $statepop(self);
    switch (state) {
      case _START -> {
        $codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        $codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      default -> $stubpop(self, state);
    }
  }

  private void body(int self, int parent, int state) {
    var body = ByteProto.NULL;

    switch (parent) {
      case ByteProto.CLASS_DECLARATION -> {
        switch (state) {
          case _START, _EXTENDS, _IMPLEMENTS, _IMPLEMENTS_TYPE -> {
            $codeadd(Whitespace.OPTIONAL);
            $statepop(parent, 2);
            body = ByteProto.CLASS_BODY;
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $statepush(_START, body);
    $codeadd(Separator.LEFT_CURLY_BRACKET);
    $codeadd(Indentation.ENTER_BLOCK);
    $element();

    state = $statepop(body);
    switch (state) {
      case _START -> {
        $codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _BODY -> {
        $codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _NAME, _INIT -> {
        $codeadd(Separator.SEMICOLON);
        $codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      default -> $stubpop(body, state);
    }
  }

  private void class0(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.CLASS_BODY -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.EMIT);
            $stateset(1, _BODY);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.COMPILATION_UNIT -> {
        switch (state) {
          case _START -> {
            $stateset(1, _BODY);
          }

          case _ANNOTATIONS -> {
            $codeadd(Whitespace.AFTER_ANNOTATION);
            $stateset(1, _BODY);
          }

          case _PACKAGE, _IMPORTS, _BODY -> {
            $codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            $stateset(1, _BODY);
          }

          case _MODS -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _BODY);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $statepush(_START, ByteProto.CLASS_DECLARATION);

    $codeadd(Keyword.CLASS);
    $codeadd(Whitespace.MANDATORY);
    $codeadd(ByteCode.IDENTIFIER, $protonxt());
  }

  private void classDeclarationPop(int state) {
    switch (state) {
      case _START, _EXTENDS -> {
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _LCURLY -> {
        $codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void classInstanceCreation(int self, int parent, int state) {
    $codeadd(Keyword.NEW);
    $statepush(_START, self);
    $element();
    state = $statepop(self);
    switch (state) {
      case _TYPE -> {
        $codeadd(Separator.LEFT_PARENTHESIS);
        $codeadd(Separator.RIGHT_PARENTHESIS);
      }

      case _ARGS -> {
        $codeadd(Separator.RIGHT_PARENTHESIS);
      }

      default -> $stubpop(self, state);
    }
  }

  private void classType(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.ANNOTATION -> {
        switch (state) {
          case _START -> {
            $codeadd(Separator.COMMERCIAL_AT);
            $stateset(1, _TYPE);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.ARRAY_TYPE -> typeArrayType(self, parent, state);

      case ByteProto.CLASS_BODY -> typeClassBody(self, parent, state);

      case ByteProto.CLASS_DECLARATION -> {
        switch (state) {
          case _IMPLEMENTS -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _IMPLEMENTS_TYPE);
          }

          case _IMPLEMENTS_TYPE -> {
            commaAndSpace();
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.CLASS_INSTANCE_CREATION -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _TYPE);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.EXPRESSION_NAME -> {
        switch (state) {
          case _START -> $stateset(1, _NAME);

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.EXTENDS -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _TYPE);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.METHOD_INVOCATION -> {
        switch (state) {
          case _START -> $stateset(1, _TYPE);

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.PARAMETERIZED_TYPE -> typeParameterizedType(self, parent, state);

      default -> $stubparent(self, parent, state);
    }

    var packageIndex = $protonxt();

    var packageName = (String) $objectget(packageIndex);

    autoImports.classTypePackageName(packageName);

    var count = $protonxt();

    switch (count) {
      case 1 -> {
        var n1Index = $protonxt();

        var n1 = (String) $objectget(n1Index);

        autoImports.classTypeSimpleName(n1);

        int instruction = autoImports.classTypeInstruction();

        switch (instruction) {
          case 1 -> {
            $codeadd(ByteCode.IDENTIFIER, n1Index);
          }

          default -> {
            $codeadd(ByteCode.IDENTIFIER, packageIndex);
            $codeadd(Separator.DOT);
            $codeadd(ByteCode.IDENTIFIER, n1Index);
          }
        }
      }

      default -> {
        throw new UnsupportedOperationException(
          "Implement me :: count=" + count);
      }
    }
  }

  private void commaAndSpace() {
    $codeadd(Separator.COMMA);
    $codeadd(Whitespace.BEFORE_NEXT_COMMA_SEPARATED_ITEM);
  }

  private void compilationUnit() {
    var proto = ByteProto.COMPILATION_UNIT;

    var item = $itemnxt();

    if (item != proto) {
      $err(
        "InternalCompiler error: expecting 'Compilation Unit' but found '%s'"
            .formatted($stub0(item)));

      return;
    }

    $statepush(_START, proto);

    $element();

    while (!$stateempty()) {
      var elem = $statepop();
      var state = $statepop();
      $elementpop(proto, elem, state);
    }
  }

  private void expression(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.ARRAY_INITIALIZER -> {
        switch (state) {
          case _START -> $stateset(1, _BODY);

          case _BODY -> commaAndSpace();

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.BLOCK -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.NEW_LINE);
            $codeadd(Indentation.EMIT);
            $stateset(1, _BODY);
          }

          case _BODY -> {
            $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
            $codeadd(Indentation.EMIT);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.CLASS_BODY -> {
        switch (state) {
          case _NAME -> {
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Operator.ASSIGNMENT);
            $codeadd(Whitespace.OPTIONAL);
            $stateset(1, _INIT);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.CLASS_INSTANCE_CREATION -> {
        switch (state) {
          case _TYPE -> {
            $codeadd(Separator.LEFT_PARENTHESIS);
            $stateset(1, _ARGS);
          }

          case _ARGS -> commaAndSpace();

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.METHOD_INVOCATION -> {
        switch (state) {
          case _START -> $stateset(1, _TYPE);

          case _NAME -> {
            $codeadd(Separator.LEFT_PARENTHESIS);
            $stateset(1, _ARGS);
          }

          case _ARGS -> commaAndSpace();

          case _NL -> {
            $codeadd(Indentation.EMIT);
            $stateset(1, _ARGS);
          }

          case _SLOT -> {
            var slot = $stateget(2);

            codeArray[slot + 0] = ByteCode.SEPARATOR;
            codeArray[slot + 1] = Separator.COMMA.ordinal();

            $codeadd(Indentation.EMIT);
            $stateset(1, _ARGS);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.RETURN_STATEMENT -> $codeadd(Whitespace.OPTIONAL);

      default -> $stubparent(self, parent, state);
    }

    switch (self) {
      case ByteProto.CLASS_INSTANCE_CREATION -> {
        classInstanceCreation(self, parent, state);
        semicolonIfNecessary(parent);
      }

      case ByteProto.EXPRESSION_NAME -> expressionName(self, parent, state);

      case ByteProto.METHOD_INVOCATION, ByteProto.METHOD_INVOCATION_QUALIFIED -> {
        methodInvocation(self, parent, state);
        semicolonIfNecessary(parent);
      }

      case ByteProto.PRIMITIVE_LITERAL -> $codeadd(ByteCode.PRIMITIVE_LITERAL, $itemnxt());

      case ByteProto.STRING_LITERAL -> $codeadd(ByteCode.STRING_LITERAL, $itemnxt());

      case ByteProto.THIS -> $codeadd(Keyword.THIS);

      default -> throw new UnsupportedOperationException(
        "Implement me :: self=%s parent=%s".formatted($stub0(self), $stub0(parent)));
    }
  }

  private void expressionName(int self, int parent, int state) {
    $statepush(_START, self);
    $element();
    $statepop(self);
  }

  private void extendsKeyword(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.CLASS_DECLARATION -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _EXTENDS);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $statepush(_START, self);
    $codeadd(Keyword.EXTENDS);
    $element();
    $statepop(self, 2);
  }

  private void identifier(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.CLASS_BODY -> {
        switch (state) {
          case _TYPE -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _NAME);
          }

          case _VOID -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _BODY);
            methodDeclaration();
          }

          case _NAME, _INIT -> {
            commaAndSpace();
            $stateset(1, _NAME);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.EXPRESSION_NAME -> {
        switch (state) {
          case _START -> {
            $stateset(1, _NAME);
          }

          case _NAME -> {
            $codeadd(Separator.DOT);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.METHOD_INVOCATION -> {
        switch (state) {
          case _TYPE -> {
            $codeadd(Separator.DOT);
            $stateset(1, _NAME);
          }

          case _RECV -> $stateset(1, _NAME);

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $codeadd(ByteCode.IDENTIFIER, $protonxt());
  }

  private void implementsKeyword(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.CLASS_DECLARATION -> {
        switch (state) {
          case _START, _EXTENDS -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _IMPLEMENTS);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $codeadd(Keyword.IMPLEMENTS);
  }

  private void methodDeclaration() {
    $statepush(_START, ByteProto.METHOD_DECLARATION);
  }

  private void methodInvocation(int self, int parent, int state) {
    int proto = ByteProto.METHOD_INVOCATION;
    $statepush(
      NULL, // 2 = slot
      self == ByteProto.METHOD_INVOCATION ? _RECV : _START,
      proto
    );
    $element();
    state = $statepop(proto);
    $statepop();
    switch (state) {
      case _NAME -> {
        $codeadd(Separator.LEFT_PARENTHESIS);
        $codeadd(Separator.RIGHT_PARENTHESIS);
      }

      case _ARGS -> {
        $codeadd(Separator.RIGHT_PARENTHESIS);
      }

      case _SLOT -> {
        $codeadd(Indentation.EXIT_PARENTHESIS);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_PARENTHESIS);
      }

      default -> $stubpop(proto, state);
    }
  }

  private void modifier(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.CLASS_BODY -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.EMIT);
            $stateset(1, _MODS);
          }

          case _MODS -> {
            $codeadd(Whitespace.MANDATORY);
          }

          case _NAME -> {
            $codeadd(Separator.SEMICOLON);
            $codeadd(Whitespace.BEFORE_NEXT_MEMBER);
            $codeadd(Indentation.EMIT);
            $stateset(1, _MODS);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      case ByteProto.COMPILATION_UNIT -> {
        switch (state) {
          case _START -> {
            $stateset(1, _MODS);
          }

          case _BODY -> {
            $codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            $stateset(1, _MODS);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $codeadd(ByteCode.KEYWORD, $protonxt());
  }

  private void newLine(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.METHOD_INVOCATION -> {
        switch (state) {
          case _NAME -> {
            $codeadd(Separator.LEFT_PARENTHESIS);
            $codeadd(Indentation.ENTER_PARENTHESIS);
            $stateset(1, _NL);
          }

          case _ARGS -> {
            $stateset(1, _SLOT);
            $stateset(2, nop1());
          }

          case _SLOT -> {}

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $codeadd(Whitespace.NEW_LINE);
  }

  private int nop1() {
    var result = codeIndex;

    $codeadd(ByteCode.NOP1, 0);

    return result;
  }

  private void packageKeyword(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.COMPILATION_UNIT -> {
        switch (state) {
          case _START -> {
            $stateset(1, _PACKAGE);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $codeadd(Keyword.PACKAGE);
    $codeadd(Whitespace.MANDATORY);
    $codeadd(ByteCode.IDENTIFIER, $protonxt());
    $codeadd(Separator.SEMICOLON);
  }

  private void parameterizedType(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.ARRAY_TYPE -> typeArrayType(self, parent, state);

      case ByteProto.CLASS_BODY -> typeClassBody(self, parent, state);

      default -> $stubparent(self, parent, state);
    }

    $statepush(_START, self);
    $element();
    state = $statepop(self);
    switch (state) {
      case _ARGS -> {
        $codeadd(Separator.RIGHT_ANGLE_BRACKET);
      }

      default -> $stubpop(self, state);
    }
  }

  private void primitiveType(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.ARRAY_TYPE -> typeArrayType(self, parent, state);

      case ByteProto.CLASS_BODY -> typeClassBody(self, parent, state);

      default -> $stubparent(self, parent, state);
    }

    $codeadd(ByteCode.KEYWORD, $itemnxt());
  }

  private void returnStatement(int self, int parent, int state) {
    statement(self, parent, state);

    $statepush(_START, self);
    $codeadd(Keyword.RETURN);
    $element();
    $codeadd(Separator.SEMICOLON);
    $statepop(self);
  }

  private void semicolonIfNecessary(int parent) {
    switch (parent) {
      case ByteProto.BLOCK -> $codeadd(Separator.SEMICOLON);
    }
  }

  private void statement(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.BLOCK -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.NEW_LINE);
            $codeadd(Indentation.EMIT);
            $stateset(1, _BODY);
          }

          case _BODY -> {
            $codeadd(Whitespace.BEFORE_NEXT_STATEMENT);
            $codeadd(Indentation.EMIT);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }
  }

  private void typeArrayType(int self, int parent, int state) {
    switch (state) {
      case _START -> {
        $stateset(1, _TYPE);
      }

      default -> $stubstate(self, parent, state);
    }
  }

  private void typeClassBody(int self, int parent, int state) {
    switch (state) {
      case _START -> {
        $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
        $codeadd(Indentation.EMIT);
        $stateset(1, _TYPE);
      }

      case _ANNOTATIONS -> {
        $codeadd(Whitespace.AFTER_ANNOTATION);
        $codeadd(Indentation.EMIT);
        $stateset(1, _TYPE);
      }

      case _MODS -> {
        $codeadd(Whitespace.MANDATORY);
        $stateset(1, _TYPE);
      }

      case _NAME, _INIT -> {
        $codeadd(Separator.SEMICOLON);
        $codeadd(Whitespace.BEFORE_NEXT_MEMBER);
        $codeadd(Indentation.EMIT);
        $stateset(1, _TYPE);
      }

      default -> $stubstate(self, parent, state);
    }
  }

  private void typeParameterizedType(int self, int parent, int state) {
    switch (state) {
      case _START -> {
        $stateset(1, _TYPE);
      }

      case _TYPE -> {
        $codeadd(Separator.LEFT_ANGLE_BRACKET);
        $stateset(1, _ARGS);
      }

      case _ARGS -> {
        commaAndSpace();
      }

      default -> $stubstate(self, parent, state);
    }
  }

  private void voidKeyword(int self, int parent, int state) {
    switch (parent) {
      case ByteProto.CLASS_BODY -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.EMIT);
            $stateset(1, _VOID);
          }

          case _ANNOTATIONS -> {
            $codeadd(Whitespace.AFTER_ANNOTATION);
            $codeadd(Indentation.EMIT);
            $stateset(1, _VOID);
          }

          default -> $stubstate(self, parent, state);
        }
      }

      default -> $stubparent(self, parent, state);
    }

    $codeadd(Keyword.VOID);
  }

}