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

  final void pass1() {
    codeIndex = 0;

    protoIndex = 0;

    stackIndex = 0;

    $elemadd(
      ByteCode.ROOT,
      ByteCode.NOP // compilation unit = 1
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.COMPILATION_UNIT -> $elemset(1, compilationUnit());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> throw $protouoe(proto);
      }
    }
  }

  private void $codeadd(int v0, int v1) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
  }

  private void $codeadd(int v0, int v1, int v2) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 2);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;
  }

  private void $codeadd(int v0, int v1, int v2, int v3) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 3);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;
    codeArray[codeIndex++] = v3;
  }

  private void $codeadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 7);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;
    codeArray[codeIndex++] = v3;
    codeArray[codeIndex++] = v4;
    codeArray[codeIndex++] = v5;
    codeArray[codeIndex++] = v6;
    codeArray[codeIndex++] = v7;
  }

  private void $codepsh() {
    markIndex++;

    markArray = IntArrays.growIfNecessary(markArray, markIndex);

    markArray[markIndex] = codeIndex;
  }

  private void $elemadd(int v0, int v1) {
    $codepsh();

    $codeadd(v0, v1);
  }

  private void $elemadd(int v0, int v1, int v2) {
    $codepsh();

    $codeadd(v0, v1, v2);
  }

  private void $elemadd(int v0, int v1, int v2, int v3) {
    $codepsh();

    $codeadd(v0, v1, v2, v3);
  }

  private void $elemadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    $codepsh();

    $codeadd(v0, v1, v2, v3, v4, v5, v6, v7);
  }

  private void $elemlst(int offset, int value) {
    var base = markArray[markIndex];

    var listIndex = base + offset;

    var list = codeArray[listIndex];

    if (list == ByteCode.NOP) {
      list = codeIndex;

      $codeadd(ByteCode.LHEAD, value, ByteCode.NOP, ByteCode.NOP);

      codeArray[listIndex] = list;
    } else {
      var head = codeArray[list];

      assert head == ByteCode.LHEAD : head;

      var next = codeIndex;

      $codeadd(ByteCode.LNEXT, value, ByteCode.NOP);

      var last = codeArray[list + 3];

      var target = last != ByteCode.NOP ? last : list;

      codeArray[target + 2] = next;

      codeArray[list + 3] = next;
    }
  }

  private int $elempop() {
    $stackpop();

    return markArray[markIndex--];
  }

  private void $elemset(int offset, int value) {
    var base = markArray[markIndex];

    codeArray[base + offset] = value;
  }

  private int $protonxt() {
    return protoArray[protoIndex++];
  }

  private boolean $prototru() {
    return true;
  }

  private UnsupportedOperationException $protouoe(int proto) {
    return new UnsupportedOperationException(
      "Implement me :: proto=" + proto);
  }

  private void $stackpop() {
    protoIndex = stackArray[--stackIndex];
  }

  private void $stackpsh() {
    var location = $protonxt();

    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex);

    stackArray[stackIndex++] = protoIndex;

    protoIndex = location;
  }

  private int classDeclaration(boolean topLevel) {
    $elemadd(
      ByteCode.CLASS,
      ByteCode.NOP, // modifiers = 1
      ByteCode.NOP, // name = 2
      ByteCode.NOP, // tparams = 3
      ByteCode.NOP, // extends = 4
      ByteCode.NOP, // implements = 5
      ByteCode.NOP, // permits = 6
      ByteCode.NOP /// body = 7
    );

    var publicFound = false;
    var simpleName = "Unnamed";

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.IDENTIFIER -> $elemset(2, objectString());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> throw $protouoe(proto);
      }
    }

    if (topLevel) {
      autoImports.fileName(publicFound, simpleName);
    }

    return $elempop();
  }

  private int compilationUnit() {
    $elemadd(
      ByteCode.COMPILATION_UNIT,
      ByteCode.NOP, /* package = 1 */
      ByteCode.NOP, /* imports = 2 */
      ByteCode.NOP /* body = 3 */
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.CLASS_DECLARATION -> $elemlst(3, classDeclaration(true));

        case ByteProto.PACKAGE_DECLARATION -> $elemlst(1, packageDeclaration());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> {
          if (autoImports.enabled()) {
            throw new UnsupportedOperationException("Implement me");
          }

          break loop;
        }

        default -> throw $protouoe(proto);
      }
    }

    return $elempop();
  }

  private int objectString() {
    int value = $protonxt();

    $elemadd(
      ByteCode.OBJECT_STRING,
      value
    );

    return $elempop();
  }

  private int packageDeclaration() {
    $elemadd(
      ByteCode.PACKAGE,
      ByteCode.NOP, /* annotations = 1 */
      ByteCode.NOP /* name = 2 */
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.PACKAGE_NAME -> $elemset(2, objectString());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> throw $protouoe(proto);
      }
    }

    return $elempop();
  }

}