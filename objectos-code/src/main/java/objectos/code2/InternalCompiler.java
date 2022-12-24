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
  private static final int _PKG = 1;
  private static final int _IMPO = 2;
  private static final int _MODS = 3;
  private static final int _EXTS = 4;
  private static final int _LCURLY = 5;
  private static final int _BODY = 6;

  int[] codeArray = new int[128];

  int codeIndex;

  int[] stateArray = new int[10];

  int stateIndex;

  final void compile() {
    aux = codeIndex = elemIndex = protoIndex = 0;

    stateIndex = -1;

    var elem = 0;

    try {
      do {
        elem = $cloop();
      } while (elem != ByteProto.EOF);
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

      $codeadd(ByteCode.RAW, $objectadd(stackTrace));
    }

    $codeadd(ByteCode.EOF);
  }

  private int $cloop() {
    var elem = $elemnxt();

    switch (elem) {
      case ByteProto.BODY, ByteProto.EXTENDS -> {
        $cloop1parent(elem);
      }

      case ByteProto.COMPILATION_UNIT -> compilationUnit();

      case ByteProto.EOF -> {}

      case ByteProto.POP -> pop();

      case ByteProto.PROTOS -> {
        var size = $elemnxt();

        for (int i = 0; i < size; i++) {
          var proto = $protonxt();

          $cloop1parent(proto);
        }
      }

      default -> {
        throw new UnsupportedOperationException(
          "Implement me :: elem=" + elem);
      }
    }

    return elem;
  }

  private void $cloop1parent(int child) {
    if ($stateempty()) {
      throw new UnsupportedOperationException("Implement me");
    }

    var parent = $statepeek();
    var state = $statepeek(1);

    switch (parent) {
      case ByteProto.CLASS_DECLARATION -> classDeclaration(state, child);

      case ByteProto.COMPILATION_UNIT -> compilationUnit(state, child);

      case ByteProto.EXTENDS -> extendsKeyword(state, child);
    }
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

  private int $elemnxt() { return elemArray[elemIndex++]; }

  private Object $objectget(int index) {
    return objectArray[index];
  }

  private int $protonxt() { return protoArray[protoIndex++]; }

  private boolean $stateempty() { return stateIndex < 0; }

  private int $statepeek() { return stateArray[stateIndex]; }

  private int $statepeek(int offset) { return stateArray[stateIndex - offset]; }

  private int $statepop() { return stateArray[stateIndex--]; }

  private void $statepush(int v0, int v1) {
    stateArray = IntArrays.growIfNecessary(stateArray, stateIndex + 2);

    stateArray[++stateIndex] = v0;
    stateArray[++stateIndex] = v1;
  }

  private void $stateset(int offset, int value) {
    stateArray[stateIndex - offset] = value;
  }

  private void class0() {
    $statepush(_START, ByteProto.CLASS_DECLARATION);

    $codeadd(Keyword.CLASS);
    $codeadd(Whitespace.MANDATORY);
    $codeadd(ByteCode.IDENTIFIER, $protonxt());
  }

  private void classDeclaration(int state, int child) {
    var proto = ByteProto.CLASS_DECLARATION;

    switch (child) {
      case ByteProto.BODY -> {
        switch (state) {
          case _START, _EXTS -> {
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $stateset(1, _LCURLY);
          }

          default -> stubState(proto, state, child);
        }
      }

      case ByteProto.EXTENDS -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _EXTS);
            extendsKeyword();
          }

          default -> stubState(proto, state, child);
        }
      }

      default -> stubErr(proto, state, child);
    }
  }

  private void classDeclarationPop(int state) {
    switch (state) {
      case _START, _EXTS -> {
        $codeadd(Whitespace.OPTIONAL);
        $codeadd(Separator.LEFT_CURLY_BRACKET);
        $codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }

      case _LCURLY -> {
        $codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
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
    if (!$stateempty()) {
      throw new UnsupportedOperationException(
        "Implement me :: state=" + $statepeek());
    }

    aux = _START;

    $statepush(_START, ByteProto.COMPILATION_UNIT);
  }

  private void compilationUnit(int state, int child) {
    var proto = ByteProto.COMPILATION_UNIT;

    switch (child) {
      case ByteProto.AUTO_IMPORTS -> {
        switch (state) {
          case _START -> {
            $codeadd(ByteCode.AUTO_IMPORTS0);
            $stateset(1, _IMPO);
          }

          case _PKG -> {
            $codeadd(ByteCode.AUTO_IMPORTS1);
            $stateset(1, _IMPO);
          }

          default -> stubState(proto, state, child);
        }
      }

      case ByteProto.CLASS0 -> {
        switch (state) {
          case _START -> {
            class0();
          }

          case _PKG, _IMPO, _BODY -> {
            $codeadd(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
            $stateset(1, _BODY);
            class0();
          }

          case _MODS -> {
            $codeadd(Whitespace.MANDATORY);
            $stateset(1, _BODY);
            class0();
          }

          default -> stubState(proto, state, child);
        }
      }

      case ByteProto.MODIFIER -> {
        switch (state) {
          case _START -> {
            $stateset(1, _MODS);
            modifier();
          }

          default -> stubState(proto, state, child);
        }
      }

      case ByteProto.PACKAGE -> {
        switch (state) {
          case _START -> {
            $stateset(1, _PKG);
            packageKeyword();
          }

          default -> stubState(proto, state, child);
        }
      }

      default -> stubErr(proto, state, child);
    }
  }

  private void extendsKeyword() {
    $codeadd(Keyword.EXTENDS);

    $statepush(_START, ByteProto.EXTENDS);
  }

  private void extendsKeyword(int state, int child) {
    switch (child) {
      case ByteProto.CLASS_TYPE -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.MANDATORY);
            classType();
          }
        }
      }
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

  private void pop() {
    var parent = $statepop();
    var state = $statepop();

    switch (parent) {
      case ByteProto.CLASS_DECLARATION -> classDeclarationPop(state);

      case ByteProto.COMPILATION_UNIT -> {}

      case ByteProto.EXTENDS -> {}

      default -> stubPop(parent, state);
    }
  }

  private String stub0(int value) {
    return switch (value) {
      case ByteProto.BODY -> "BODY";

      case ByteProto.CLASS0 -> "CLASS0";

      case ByteProto.CLASS_DECLARATION -> "Class Declaration";

      case ByteProto.COMPILATION_UNIT -> "Compilation Unit";

      case ByteProto.EXTENDS -> "EXTENDS";

      default -> Integer.toString(value);
    };
  }

  private void stubErr(int location, int state, int child) {
    var msg = "Stub error: at %s got child %s. state=%d".formatted(
      stub0(location), stub0(child), state);

    $codeadd(Whitespace.NEW_LINE);
    $codeadd(ByteCode.COMMENT, $objectadd(msg));
  }

  private void stubPop(int parent, int state) {
    var msg = "Pop stub: popped %s, state=%d".formatted(
      stub0(parent), state);

    $codeadd(Whitespace.NEW_LINE);
    $codeadd(ByteCode.COMMENT, $objectadd(msg));
  }

  private void stubState(int location, int state, int child) {
    var msg = "State stub: at %s with child %s and state=%d".formatted(
      stub0(location), stub0(child), state);

    $codeadd(Whitespace.NEW_LINE);
    $codeadd(ByteCode.COMMENT, $objectadd(msg));
  }

}