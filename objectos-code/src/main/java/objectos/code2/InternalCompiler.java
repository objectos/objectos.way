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

import objectos.util.IntArrays;

class InternalCompiler extends InternalApi {

  private static final int _START = 0;
  private static final int _MODS = 1;
  private static final int _BODY = 2;
  private static final int _LCURLY = 3;

  int[] codeArray = new int[128];

  int codeIndex;

  int[] stateArray = new int[10];

  int stateIndex;

  final void compile() {
    aux = codeIndex = elemIndex = protoIndex = 0;

    stateIndex = -1;

    var elem = 0;

    do {
      elem = $elemloop();
    } while (elem != ByteProto.EOF);

    $codeadd(ByteCode.EOF);
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

  private int $elemloop() {
    var elem = $elemnxt();

    switch (elem) {
      case ByteProto.BODY -> {
        $elemloop1parent(elem);

        $elemloop2proto(elem);
      }

      case ByteProto.COMPILATION_UNIT -> {
        if (!$stateempty()) {
          throw new UnsupportedOperationException(
            "Implement me :: state=" + $statepeek());
        }

        aux = _START;

        $statepush(aux, elem);
      }

      case ByteProto.EOF -> {}

      case ByteProto.POP -> $elemloop3pop();

      case ByteProto.PROTOS -> {
        var size = $elemnxt();

        for (int i = 0; i < size; i++) {
          var proto = $protonxt();

          $elemloop1parent(proto);

          $elemloop2proto(proto);
        }
      }

      default -> {
        throw new UnsupportedOperationException(
          "Implement me :: elem=" + elem);
      }
    }

    return elem;
  }

  private void $elemloop1parent(int child) {
    if ($stateempty()) {
      throw new UnsupportedOperationException("Implement me");
    }

    var parent = $statepeek();
    var state = $statepeek(1);

    switch (parent) {
      case ByteProto.CLASS_DECLARATION -> classDeclaration(state, child);

      case ByteProto.COMPILATION_UNIT -> compilationUnit(state, child);
    }
  }

  private void $elemloop2proto(int proto) {
    switch (proto) {
      case ByteProto.CLASS0 -> {
        $codeadd(Keyword.CLASS);
        $codeadd(Whitespace.MANDATORY);
        $codeadd(ByteCode.IDENTIFIER, $protonxt());
      }

      case ByteProto.MODIFIER -> $codeadd(ByteCode.KEYWORD, $protonxt());
    }
  }

  private void $elemloop3pop() {
    var parent = $statepop();
    var state = $statepop();

    switch (parent) {
      case ByteProto.CLASS_DECLARATION -> classDeclarationPop(state);
    }
  }

  private int $elemnxt() { return elemArray[elemIndex++]; }

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

  private void classDeclaration(int state, int child) {
    switch (child) {
      case ByteProto.BODY -> {
        switch (state) {
          case _START -> {
            $codeadd(Whitespace.OPTIONAL);
            $codeadd(Separator.LEFT_CURLY_BRACKET);

            $stateset(1, _LCURLY);
          }
        }
      }
    }
  }

  private void classDeclarationPop(int state) {
    switch (state) {
      case _LCURLY -> {
        $codeadd(Whitespace.BEFORE_EMPTY_BODY_END);
        $codeadd(Separator.RIGHT_CURLY_BRACKET);
      }
    }
  }

  private void compilationUnit(int state, int child) {
    switch (child) {
      case ByteProto.CLASS0 -> {
        switch (state) {
          case _MODS -> {
            $codeadd(Whitespace.MANDATORY);

            $stateset(1, _BODY);

            $statepush(_START, ByteProto.CLASS_DECLARATION);
          }
        }
      }

      case ByteProto.MODIFIER -> {
        switch (state) {
          case _START -> {
            $stateset(1, _MODS);
          }
        }
      }
    }
  }

}