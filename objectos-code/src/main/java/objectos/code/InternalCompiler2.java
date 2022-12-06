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

import objectos.util.IntArrays;

class InternalCompiler2 extends InternalApi2 {

  private static final int _START = 0;

  final void pass1() {
    code = 0;

    codeIndex = 0;

    markIndex = -1;

    objectIndex = 0;

    protoIndex = 0;

    stackIndex = 0;

    while (code != ByteProto.EOF) {
      $loop();
    }

    $codeadd(ByteCode.EOF);
  }

  private void $codeadd(int v0) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex);

    codeArray[codeIndex++] = v0;
  }

  private void $codeadd(int v0, int v1) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
  }

  private void $codeadd(ReservedKeyword keyword) {
    $codeadd(ByteCode.RESERVED_KEYWORD, keyword.ordinal());
  }

  private void $codeadd(Separator separator) {
    $codeadd(ByteCode.SEPARATOR, separator.ordinal());
  }

  private void $loop() {
    $protonxt();

    switch (code) {
      case ByteProto.BREAK -> { breakInstruction(); $protopop(); }

      case ByteProto.CLASS_DECLARATION -> $statepsh();

      case ByteProto.COMPILATION_UNIT -> $statepsh();

      case ByteProto.IDENTIFIER -> { identifier(); $protopop(); }

      case ByteProto.JMP -> { $protonxt(); $protopsh(); }

      case ByteProto.EOF -> {}

      default -> throw $uoe_proto();
    }
  }

  private int $protonxt() { return code = protoArray[protoIndex++]; }

  private void $protopop() { protoIndex = stackArray[--stackIndex]; }

  private void $protopsh() {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex);

    stackArray[stackIndex++] = protoIndex;

    protoIndex = code;
  }

  private int $statepeek() {
    return markArray[markIndex];
  }

  private int $statepop() {
    return markArray[markIndex--];
  }

  private void $statepsh() {
    markArray = IntArrays.growIfNecessary(markArray, markIndex + 2);

    markArray[++markIndex] = _START;

    markArray[++markIndex] = code;
  }

  private UnsupportedOperationException $uoe_proto() {
    return new UnsupportedOperationException(
      "Implement me :: proto = " + code);
  }

  private UnsupportedOperationException $uoe_state(int state) {
    return new UnsupportedOperationException(
      "Implement me :: state = " + state);
  }

  private void breakInstruction() {
    var proto = $statepop();

    switch (proto) {
      case ByteProto.CLASS_DECLARATION -> {
        var state = $statepop();

        switch (state) {
          case _START -> {
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $codeadd(Separator.RIGHT_CURLY_BRACKET);
          }

          default -> throw $uoe_state(state);
        }
      }
    }
  }

  private void identifier() {
    switch ($statepeek()) {
      case ByteProto.CLASS_DECLARATION -> $codeadd(ReservedKeyword.CLASS);
    }

    $codeadd(ByteCode.IDENTIFIER, $protonxt());
  }

}