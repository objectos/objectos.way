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
  private static final int _NAME = 13;

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
      case 10 -> $element10();
      case 11 -> $element11();
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

    var parent = $statepeek();
    var state = $statepeek(1);
    var child = $itemnxt();

    switch (parent) {
      case ByteProto.ANNOTATION -> annotation(state, child);

      case ByteProto.CLASS_BODY -> classBody(state, child);

      case ByteProto.CLASS_DECLARATION -> classDeclaration(state, child);

      case ByteProto.COMPILATION_UNIT -> compilationUnit(state, child);

      case ByteProto.EXTENDS -> extendsKeyword(state, child);

      case ByteProto.METHOD_DECLARATION -> methodDeclaration(state, child);

      default -> throw new UnsupportedOperationException(
        "Implement me :: parent=%s child=%s".formatted($stub0(parent), $stub0(child)));
    }
  }

  private int $itemnxt() { return itemArray[itemIndex++]; }

  private Object $objectget(int index) {
    return objectArray[index];
  }

  private int $protonxt() { return itemArray[itemIndex++]; }

  private boolean $stateempty() { return rootIndex < 0; }

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

  private void $stateset(int offset, int value) {
    rootArray[rootIndex - offset] = value;
  }

  private String $stub0(int value) {
    return switch (value) {
      case ByteProto.ANNOTATION -> "Annotation";

      case ByteProto.BLOCK -> "Block";

      case ByteProto.BODY -> "Body";

      case ByteProto.CLASS0 -> "Class0";

      case ByteProto.CLASS_BODY -> "Class Body";

      case ByteProto.CLASS_DECLARATION -> "Class Declaration";

      case ByteProto.CLASS_TYPE -> "Class Type";

      case ByteProto.COMPILATION_UNIT -> "Compilation Unit";

      case ByteProto.EXTENDS -> "Extends";

      case ByteProto.IDENTIFIER -> "Identifier";

      case ByteProto.IMPLEMENTS -> "Implements";

      case ByteProto.METHOD_DECLARATION -> "Method Decl.";

      case ByteProto.MODIFIER -> "Modifier";

      case ByteProto.PRIMITIVE_TYPE -> "Primitive Type";

      case ByteProto.VOID -> "Void";

      default -> Integer.toString(value);
    };
  }

  private void $stubchild(int location, int state, int child) {
    $err(
      "Child stub: at '%s' got child '%s'. state=%d"
          .formatted($stub0(location), $stub0(child), state)
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

  private void $stubstate(int location, int state, int child) {
    $err(
      "State stub: at '%s' with child '%s' and state=%d"
          .formatted($stub0(location), $stub0(child), state)
    );
  }

  private void annotation() {
    var proto = ByteProto.ANNOTATION;
    $statepush(_START, proto);
    $element();
    var state = $statepop(proto);
    switch (state) {
      case _TYPE -> {}

      default -> $stubpop(proto, state);
    }
  }

  private void annotation(int state, int child) {
    var proto = ByteProto.ANNOTATION;

    switch (child) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> {
            $codeadd(Separator.COMMERCIAL_AT);
            $stateset(1, _TYPE);
            classType();
          }

          default -> $stubstate(proto, state, child);
        }
      }

      default -> $stubchild(proto, state, child);
    }
  }

  private void body(int proto) {
    $statepush(_START, proto);
    $codeadd(Separator.LEFT_CURLY_BRACKET);
    $codeadd(Indentation.ENTER_BLOCK);
    $element();
    var state = $statepop(proto);
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

      case _NAME -> {
        $codeadd(Separator.SEMICOLON);
        $codeadd(Whitespace.BEFORE_NON_EMPTY_BLOCK_END);
        $codeadd(Indentation.EXIT_BLOCK);
        $codeadd(Indentation.EMIT);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      default -> $stubpop(proto, state);
    }
  }

  private void class0() {
    $statepush(_START, ByteProto.CLASS_DECLARATION);

    $codeadd(Keyword.CLASS);
    $codeadd(Whitespace.MANDATORY);
    $codeadd(ByteCode.IDENTIFIER, $protonxt());
  }

  private void classBody(int state, int child) {
    var proto = ByteProto.CLASS_BODY;

    switch (child) {
      case ByteProto.ANNOTATION -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.EMIT);
            $stateset(1, _ANNOTATIONS);
            annotation();
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.CLASS0 -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.EMIT);
            $stateset(1, _BODY);
            class0();
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.IDENTIFIER -> {
        switch (state) {
          case _TYPE -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _NAME);
            identifier();
          }

          case _VOID -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _BODY);
            methodDeclaration();
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.PRIMITIVE_TYPE -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.EMIT);
            $codeadd(ByteCode.KEYWORD, $itemnxt());
            $stateset(1, _TYPE);
          }

          case _ANNOTATIONS -> {
            $codeadd(Whitespace.AFTER_ANNOTATION);
            $codeadd(Indentation.EMIT);
            $codeadd(ByteCode.KEYWORD, $itemnxt());
            $stateset(1, _TYPE);
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.VOID -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.BEFORE_FIRST_MEMBER);
            $codeadd(Indentation.EMIT);
            $codeadd(Keyword.VOID);
            $stateset(1, _VOID);
          }

          case _ANNOTATIONS -> {
            $codeadd(Whitespace.AFTER_ANNOTATION);
            $codeadd(Indentation.EMIT);
            $codeadd(Keyword.VOID);
            $stateset(1, _VOID);
          }

          default -> $stubstate(proto, state, child);
        }
      }

      default -> $stubchild(proto, state, child);
    }
  }

  private void classDeclaration(int state, int child) {
    var proto = ByteProto.CLASS_DECLARATION;

    switch (child) {
      case ByteProto.BODY -> {
        switch (state) {
          case _START, _EXTENDS, _IMPLEMENTS, _IMPLEMENTS_TYPE -> {
            $codeadd(Whitespace.OPTIONAL);
            $statepop(proto, 2);
            body(ByteProto.CLASS_BODY);
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _IMPLEMENTS -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _IMPLEMENTS_TYPE);
            classType();
          }

          case _IMPLEMENTS_TYPE -> {
            $codeadd(Separator.COMMA);
            $codeadd(Whitespace.OPTIONAL);
            classType();
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.EXTENDS -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _EXTENDS);
            extendsKeyword(proto);
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.IMPLEMENTS -> {
        switch (state) {
          case _START, _EXTENDS -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _IMPLEMENTS);
            $codeadd(Keyword.IMPLEMENTS);
          }

          default -> $stubstate(proto, state, child);
        }
      }

      default -> $stubchild(proto, state, child);
    }
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

  private void classType() {
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

  private void compilationUnit(int state, int child) {
    var proto = ByteProto.COMPILATION_UNIT;

    switch (child) {
      case ByteProto.ANNOTATION -> {
        switch (state) {
          case _START -> {
            $stateset(1, _ANNOTATIONS);
            annotation();
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.AUTO_IMPORTS -> {
        switch (state) {
          case _START -> {
            $codeadd(ByteCode.AUTO_IMPORTS0);
            $stateset(1, _IMPORTS);
          }

          case _PACKAGE -> {
            $codeadd(ByteCode.AUTO_IMPORTS1);
            $stateset(1, _IMPORTS);
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.CLASS0 -> {
        switch (state) {
          case _START -> {
            $stateset(1, _BODY);
            class0();
          }

          case _ANNOTATIONS -> {
            $codeadd(Whitespace.AFTER_ANNOTATION);
            $stateset(1, _BODY);
            class0();
          }

          case _PACKAGE, _IMPORTS, _BODY -> {
            $codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            $stateset(1, _BODY);
            class0();
          }

          case _MODS -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _BODY);
            class0();
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.MODIFIER -> {
        switch (state) {
          case _START -> {
            $stateset(1, _MODS);
            modifier();
          }

          case _BODY -> {
            $codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            $stateset(1, _MODS);
            modifier();
          }

          default -> $stubstate(proto, state, child);
        }
      }

      case ByteProto.PACKAGE -> {
        switch (state) {
          case _START -> {
            $stateset(1, _PACKAGE);
            packageKeyword();
          }

          default -> $stubstate(proto, state, child);
        }
      }

      default -> $stubchild(proto, state, child);
    }
  }

  private void extendsKeyword(int parent) {
    var proto = ByteProto.EXTENDS;

    $codeadd(Keyword.EXTENDS);
    $statepush(_START, proto);
    $element();
    $statepop(proto, 2);
  }

  private void extendsKeyword(int state, int child) {
    var proto = ByteProto.EXTENDS;

    switch (child) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _TYPE);
            classType();
          }

          default -> $stubstate(proto, state, child);
        }
      }

      default -> $stubchild(proto, state, child);
    }
  }

  private void identifier() {
    $codeadd(ByteCode.IDENTIFIER, $protonxt());
  }

  private void methodDeclaration() {
    $statepush(_START, ByteProto.METHOD_DECLARATION);

    $codeadd(ByteCode.IDENTIFIER, $protonxt());
    $codeadd(Separator.LEFT_PARENTHESIS);
  }

  private void methodDeclaration(int state, int child) {
    var proto = ByteProto.METHOD_DECLARATION;

    switch (child) {
      case ByteProto.BLOCK -> {
        switch (state) {
          case _START -> {
            $codeadd(Separator.RIGHT_PARENTHESIS);
            $codeadd(Whitespace.OPTIONAL);
            $statepop(proto, 2);
            body(child);
          }

          default -> $stubstate(proto, state, child);
        }
      }

      default -> $stubchild(proto, state, child);
    }
  }

  private void modifier() {
    $codeadd(ByteCode.KEYWORD, $protonxt());
  }

  private void packageKeyword() {
    $codeadd(Keyword.PACKAGE);
    $codeadd(Whitespace.MANDATORY);
    $codeadd(ByteCode.IDENTIFIER, $protonxt());
    $codeadd(Separator.SEMICOLON);
  }

}