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

  private interface CompilationUnit {
    int CONTENTS = 1 << 0;

    int AUTO_IMPORTS = 1 << 1;
  }

  private static final int NULL = Integer.MIN_VALUE;

  private static final int _START = 0;

  final void pass1() {
    code = 0;

    codeIndex = 0;

    markIndex = -1;

    objectIndex = 0;

    protoIndex = 0;

    stackIndex = 0;

    $parentpush(0, ByteProto.EOF);

    var proto = 0;

    do {
      proto = $loop();
    } while (proto != ByteProto.EOF);
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

  private void $codeadd(PseudoElement element) {
    $codeadd(ByteCode.PSEUDO_ELEMENT, element.ordinal());
  }

  private void $codeadd(ReservedKeyword keyword) {
    $codeadd(ByteCode.RESERVED_KEYWORD, keyword.ordinal());
  }

  private void $codeadd(Separator separator) {
    $codeadd(ByteCode.SEPARATOR, separator.ordinal());
  }

  private void $jump() {
    var location = $protonxt();

    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex);

    stackArray[stackIndex++] = protoIndex;

    protoIndex = location;
  }

  private int $loop() {
    var proto = $protonxt();

    switch (proto) {
      case ByteProto.BREAK -> { breakInstruction(); $protopop(); }

      case ByteProto.CLASS_TYPE -> {
        $loopparent(proto);

        $parentpush(
          NULL, // 2 = code start
          0, // 1 = name count
          proto
        );
      }

      case ByteProto.EOF -> {}

      case ByteProto.IDENTIFIER -> { $loopparent(proto); identifier(); }

      case ByteProto.JMP -> $jump();

      case ByteProto.OBJECT_END -> $protopop();

      case ByteProto.PACKAGE_NAME -> { $loopparent(proto); packageName(); }

      case ByteProto.SIMPLE_NAME -> { $loopparent(proto); simpleName(); }

      default -> { $loopparent(proto); $parentpush(0, proto); }
    }

    return proto;
  }

  private void $loopparent(int child) {
    var parent = $parentpeek();

    switch (parent) {
      case ByteProto.CLASS_DECLARATION -> classDeclaration(child);

      case ByteProto.CLASS_TYPE -> classType(child);

      case ByteProto.COMPILATION_UNIT -> compilationUnit(child);

      case ByteProto.EOF -> root(child);

      case ByteProto.EXTENDS_SINGLE -> extendsSingle(child);

      case ByteProto.PACKAGE_DECLARATION -> packageDeclaration(child);
    }
  }

  private Object $objectpeek() {
    var index = protoArray[protoIndex];

    return objectArray[index];
  }

  private boolean $parentbitget(int offset, int value) {
    var index = markIndex - offset;

    return (markArray[index] & value) != 0;
  }

  private void $parentbitset(int offset, int value) {
    var index = markIndex - offset;

    markArray[index] |= value;
  }

  private int $parentpeek() {
    return markArray[markIndex];
  }

  private int $parentpop() {
    return markArray[markIndex--];
  }

  private void $parentpush(int v0, int v1) {
    markArray = IntArrays.growIfNecessary(markArray, markIndex + 2);

    markArray[++markIndex] = v0;
    markArray[++markIndex] = v1;
  }

  private void $parentpush(int v0, int v1, int v2) {
    markArray = IntArrays.growIfNecessary(markArray, markIndex + 3);

    markArray[++markIndex] = v0;
    markArray[++markIndex] = v1;
    markArray[++markIndex] = v2;
  }

  private void $parentvalinc(int offset) {
    var index = markIndex - offset;

    markArray[index]++;
  }

  private void $parentvalset(int offset, int value) {
    var index = markIndex - offset;

    markArray[index] = value;
  }

  private int $protonxt() { return protoArray[protoIndex++]; }

  private void $protopop() { protoIndex = stackArray[--stackIndex]; }

  private UnsupportedOperationException $uoe_state(int state) {
    return new UnsupportedOperationException(
      "Implement me :: state = " + state);
  }

  private void breakInstruction() {
    if (markIndex < 0) {
      return;
    }

    var proto = $parentpop();

    var state = $parentpop();

    switch (proto) {
      case ByteProto.CLASS_DECLARATION -> {
        switch (state) {
          case _START -> {
            $codeadd(Separator.LEFT_CURLY_BRACKET);
            $codeadd(Separator.RIGHT_CURLY_BRACKET);
          }

          default -> throw $uoe_state(state);
        }
      }

      case ByteProto.CLASS_TYPE -> {
        var codeStart = $parentpop();

        var nameCount = state;

        var instruction = autoImports.classTypeInstruction();

        if (instruction == ByteCode.NOP) {
          break;
        } else if (instruction == nameCount) {
          // skip package name & dot

          codeArray[codeStart] = ByteCode.NOP1;
          codeArray[codeStart + 2] = ByteCode.NOP1;

        } else {
          throw new UnsupportedOperationException(
            "Implement me :: nameCount=" + nameCount + ";codeStart=" + codeStart + ";inst="
                + instruction);
        }
      }

      case ByteProto.COMPILATION_UNIT -> $codeadd(ByteCode.EOF);

      case ByteProto.PACKAGE_DECLARATION -> $codeadd(Separator.SEMICOLON);
    }
  }

  private void classDeclaration(int child) {
    switch (child) {
      case ByteProto.IDENTIFIER -> $codeadd(ReservedKeyword.CLASS);
    }
  }

  private void classType(int child) {
    switch (child) {
      case ByteProto.PACKAGE_NAME -> {
        var name = (String) $objectpeek();

        autoImports.classTypePackageName(name);

        $parentvalset(2, codeIndex);
      }

      case ByteProto.SIMPLE_NAME -> {
        var name = (String) $objectpeek();

        autoImports.classTypeSimpleName(name);

        $codeadd(Separator.DOT);

        $parentvalinc(1);
      }
    }
  }

  private void compilationUnit(int child) {
    if (child != ByteProto.PACKAGE_DECLARATION &&
        !$parentbitget(1, CompilationUnit.AUTO_IMPORTS) &&
        autoImports.enabled()) {
      $codeadd(ByteCode.AUTO_IMPORTS);

      $parentbitset(1, CompilationUnit.AUTO_IMPORTS);
    }

    if ($parentbitget(1, CompilationUnit.CONTENTS)) {
      $codeadd(PseudoElement.BEFORE_NEXT_TOP_LEVEL_ITEM);
    }

    $parentbitset(1, CompilationUnit.CONTENTS);
  }

  private void extendsSingle(int child) {
    switch (child) {
      case ByteProto.CLASS_TYPE -> $codeadd(ReservedKeyword.EXTENDS);
    }
  }

  private void identifier() {
    $codeadd(ByteCode.IDENTIFIER, $protonxt());
  }

  private void packageDeclaration(int child) {
    switch (child) {
      case ByteProto.PACKAGE_NAME -> $codeadd(ReservedKeyword.PACKAGE);
    }
  }

  private void packageName() {
    $codeadd(ByteCode.IDENTIFIER, $protonxt());
  }

  private void root(int child) {
  }

  private void simpleName() {
    $codeadd(ByteCode.IDENTIFIER, $protonxt());
  }

}