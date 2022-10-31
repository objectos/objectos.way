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
package objectox.code;

import java.util.Arrays;
import objectos.util.IntArrays;

public final class Pass1 {

  int[] codes = new int[32];

  private int codesCursor;

  Object[] objects;

  private int[] protoArray;

  private int protoCursor;

  ImportSet importSet;

  private int proto;

  private int[] stack = new int[16];

  private int stackCursor;

  public final void execute(int[] source, Object[] object, ImportSet importSet) {
    this.protoArray = source;
    this.objects = object;
    this.importSet = importSet;

    codesCursor = 0;

    protoCursor = 0;

    execute();
  }

  public final void execute(State state) {
    execute(state.protos(), state.objects(), state.importSet());
  }

  final int[] toArray() {
    return Arrays.copyOf(codes, codesCursor);
  }

  private void codeadd(int v0) {
    codes = IntArrays.growIfNecessary(codes, codesCursor + 0);

    codes[codesCursor++] = v0;
  }

  private void codeadd(int v0, int v1) {
    codes = IntArrays.growIfNecessary(codes, codesCursor + 1);

    codes[codesCursor++] = v0;
    codes[codesCursor++] = v1;
  }

  private void codeadd(int v0, int v1, int v2) {
    codes = IntArrays.growIfNecessary(codes, codesCursor + 2);

    codes[codesCursor++] = v0;
    codes[codesCursor++] = v1;
    codes[codesCursor++] = v2;
  }

  private void codeadd(int v0, int v1, int v2, int v3) {
    codes = IntArrays.growIfNecessary(codes, codesCursor + 3);

    codes[codesCursor++] = v0;
    codes[codesCursor++] = v1;
    codes[codesCursor++] = v2;
    codes[codesCursor++] = v3;
  }

  private void codeadd(int v0, int v1, int v2, int v3, int v4) {
    codes = IntArrays.growIfNecessary(codes, codesCursor + 4);

    codes[codesCursor++] = v0;
    codes[codesCursor++] = v1;
    codes[codesCursor++] = v2;
    codes[codesCursor++] = v3;
    codes[codesCursor++] = v4;
  }

  private void codeadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    codes = IntArrays.growIfNecessary(codes, codesCursor + 7);

    codes[codesCursor++] = v0;
    codes[codesCursor++] = v1;
    codes[codesCursor++] = v2;
    codes[codesCursor++] = v3;
    codes[codesCursor++] = v4;
    codes[codesCursor++] = v5;
    codes[codesCursor++] = v6;
    codes[codesCursor++] = v7;
  }

  private void codeadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    codes = IntArrays.growIfNecessary(codes, codesCursor + 8);

    codes[codesCursor++] = v0;
    codes[codesCursor++] = v1;
    codes[codesCursor++] = v2;
    codes[codesCursor++] = v3;
    codes[codesCursor++] = v4;
    codes[codesCursor++] = v5;
    codes[codesCursor++] = v6;
    codes[codesCursor++] = v7;
    codes[codesCursor++] = v8;
  }

  private void codeset(
      int zero,
      int v1, int v2) {
    codes[zero + 1] = v1;
    codes[zero + 2] = v2;
  }

  private void codeset(
      int zero,
      int v1, int v2, int v3) {
    codes[zero + 1] = v1;
    codes[zero + 2] = v2;
    codes[zero + 3] = v3;
  }

  private void codeset(
      int zero,
      int v1, int v2, int v3, int v4) {
    codes[zero + 1] = v1;
    codes[zero + 2] = v2;
    codes[zero + 3] = v3;
    codes[zero + 4] = v4;
  }

  private void codeset(
      int zero,
      int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    codes[zero + 1] = v1;
    codes[zero + 2] = v2;
    codes[zero + 3] = v3;
    codes[zero + 4] = v4;
    codes[zero + 5] = v5;
    codes[zero + 6] = v6;
    codes[zero + 7] = v7;
  }

  private void codeset(
      int zero,
      int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    codes[zero + 1] = v1;
    codes[zero + 2] = v2;
    codes[zero + 3] = v3;
    codes[zero + 4] = v4;
    codes[zero + 5] = v5;
    codes[zero + 6] = v6;
    codes[zero + 7] = v7;
    codes[zero + 8] = v8;
  }

  private void execute() {
    protorea(ByteProto.JMP);

    protorea();

    protopsh();

    protoass(ByteProto.COMPILATION_UNIT);

    executeCompilationUnit();
  }

  private int executeAnnotation() {
    var self = codesCursor;

    int name = ByteCode.NOP;
    int pairs = ByteCode.NOP;

    codeadd(ByteCode.ANNOTATION, name, pairs);

    var children = protolst();

    for (int i = 0; i < children; i++) {
      protorea();

      protopsh();

      switch (proto) {
        case ByteProto.CLASS_NAME -> name = setOrThrow(name, protorea());

        default -> throw protouoe();
      }

      protopop();
    }

    codeset(self, name, pairs);

    return self;
  }

  private int executeClass() {
    int self = codesCursor;

    int modifiers = ByteCode.NOP;
    int name = ByteCode.NOP;
    int typeArgs = ByteCode.NOP;
    int _extends = ByteCode.NOP;
    int _implements = ByteCode.NOP;
    int _permits = ByteCode.NOP;
    int body = ByteCode.NOP;

    codeadd(
      ByteCode.CLASS,
      modifiers, name, typeArgs, _extends, _implements, _permits,
      body
    );

    var children = protolst();

    for (int i = 0; i < children; i++) {
      protorea();

      protopsh();

      switch (proto) {
        case ByteProto.ANNOTATION -> modifiers = listAdd(modifiers, executeAnnotation());

        case ByteProto.MODIFIER -> modifiers = listAdd(modifiers, executeModifier());

        case ByteProto.IDENTIFIER -> name = setOrThrow(name, protorea());

        case ByteProto.EXTENDS -> _extends = setOrThrow(_extends, protorea());

        case ByteProto.METHOD -> body = listAdd(body, executeMethod());

        default -> throw protouoe();
      }

      protopop();
    }

    codeset(
      self,
      modifiers, name, typeArgs, _extends, _implements, _permits,
      body
    );

    return self;
  }

  private void executeCompilationUnit() {
    var self = codesCursor;

    var _package = ByteCode.NOP;
    var _import = ByteCode.NOP;
    var body = ByteCode.NOP;

    codeadd(
      ByteCode.COMPILATION_UNIT,
      _package, _import, body
    );

    var children = protolst();

    for (int i = 0; i < children; i++) {
      protorea();

      protopsh();

      switch (proto) {
        case ByteProto.PACKAGE -> _package = setOrThrow(_package, executePackage());

        case ByteProto.CLASS -> body = listAdd(body, executeClass());

        case ByteProto.LOCAL_VARIABLE -> body = listAdd(body, executeLocalVariable());

        case ByteProto.METHOD -> body = listAdd(body, executeMethod());

        case ByteProto.METHOD_INVOCATION -> body = listAdd(body, executeMethodInvocation());

        default -> throw protouoe();
      }

      protopop();
    }

    if (_import != ByteCode.NOP) {
      throw new UnsupportedOperationException("Implement me :: unexpected imports");
    }

    if (importSet.enabled) {
      _import = executeEofImportSet();
    }

    codeset(
      self,
      _package, _import, body
    );
  }

  private int executeEofImportSet() {
    var self = codesCursor;

    var sorted = importSet.sort();

    for (int i = 0, size = sorted.size(); i < size; i++) {
      codeadd(ByteCode.IMPORT, i);
    }

    codeadd(ByteCode.EOF);

    return self;
  }

  private int executeExpressionName() {
    var self = codesCursor;

    var children = protolst();

    codeadd(ByteCode.EXPRESSION_NAME, children);

    for (int i = 0; i < children; i++) {
      protorea();

      protopsh();

      switch (proto) {
        case ByteProto.CLASS_NAME, ByteProto.IDENTIFIER -> codeadd(protorea());

        default -> throw protouoe();
      }

      protopop();
    }

    return self;
  }

  private int executeLocalVariable() {
    var self = codesCursor;

    var modifiers = ByteCode.NOP;
    var type = ByteCode.NOP;
    var name = ByteCode.NOP;
    var init = ByteCode.NOP;

    codeadd(
      ByteCode.LOCAL_VARIABLE,
      modifiers, type, name, init
    );

    var children = protolst();

    for (int i = 0; i < children; i++) {
      protorea();

      protopsh();

      switch (proto) {
        case ByteProto.IDENTIFIER -> name = setOrThrow(name, protorea());

        case ByteProto.STRING_LITERAL -> init = setOrThrow(init, executeStringLiteral());

        default -> throw protouoe();
      }

      protopop();
    }

    codeset(
      self,
      modifiers, type, name, init
    );

    return self;
  }

  private int executeMethod() {
    int self = codesCursor;

    int modifiers = ByteCode.NOP;
    int typeParams = ByteCode.NOP;
    int returnType = ByteCode.NOP;
    int name = ByteCode.NOP;
    int receiver = ByteCode.NOP;
    int params = ByteCode.NOP;
    int _throws = ByteCode.NOP;
    int body = ByteCode.NOP;

    codeadd(
      ByteCode.METHOD,
      modifiers, typeParams, returnType, name, receiver, params, _throws,
      body
    );

    var children = protolst();

    for (int i = 0; i < children; i++) {
      protorea();

      protopsh();

      switch (proto) {
        case ByteProto.ANNOTATION -> modifiers = listAdd(modifiers, executeAnnotation());

        case ByteProto.MODIFIER -> modifiers = listAdd(modifiers, executeModifier());

        case ByteProto.IDENTIFIER -> name = setOrThrow(name, protorea());

        case ByteProto.TYPE_NAME -> returnType = setOrThrow(returnType, protorea());

        case ByteProto.METHOD_INVOCATION -> body = listAdd(body, executeMethodInvocation());

        default -> throw protouoe();
      }

      protopop();
    }

    codeset(
      self,
      modifiers, typeParams, returnType, name, receiver, params, _throws,
      body
    );

    return self;
  }

  private int executeMethodInvocation() {
    var self = codesCursor;

    int callee = ByteCode.NOP;
    int typeArgs = ByteCode.NOP;
    int name = ByteCode.NOP;
    int args = ByteCode.NOP;

    codeadd(ByteCode.METHOD_INVOCATION, callee, typeArgs, name, args);

    var children = protolst();

    for (int i = 0; i < children; i++) {
      protorea();

      protopsh();

      switch (proto) {
        case ByteProto.IDENTIFIER -> name = setOrThrow(name, protorea());

        case ByteProto.EXPRESSION_NAME -> args = listAdd(args, executeExpressionName());

        case ByteProto.NEW_LINE -> args = listAdd(args, executeNewLine());

        case ByteProto.METHOD_INVOCATION -> args = listAdd(args, executeMethodInvocation());

        case ByteProto.STRING_LITERAL -> args = listAdd(args, executeStringLiteral());

        default -> throw protouoe();
      }

      protopop();
    }

    codeset(self, callee, typeArgs, name, args);

    return self;
  }

  private int executeModifier() {
    var self = codesCursor;

    protorea();

    codeadd(ByteCode.MODIFIER, proto);

    return self;
  }

  private int executeNewLine() {
    var self = codesCursor;

    codeadd(ByteCode.NEW_LINE);

    return self;
  }

  private int executePackage() {
    var self = codesCursor;

    var annotations = ByteCode.NOP;
    var name = ByteCode.NOP;

    codeadd(
      ByteCode.PACKAGE,
      annotations, name
    );

    var children = protolst();

    for (int i = 0; i < children; i++) {
      protorea();

      protopsh();

      switch (proto) {
        case ByteProto.PACKAGE_NAME -> name = setOrThrow(name, protorea());

        default -> throw protouoe();
      }

      protopop();
    }

    codeset(
      self,
      annotations, name
    );

    return self;
  }

  private int executeStringLiteral() {
    var self = codesCursor;

    codeadd(ByteCode.STRING_LITERAL, protorea());

    return self;
  }

  private int listAdd(int list, int value) {
    if (list == ByteCode.NOP) {
      list = codesCursor;

      codeadd(ByteCode.LIST, ByteCode.NOP, value, ByteCode.EOF, ByteCode.NOP);

      return list;
    }

    var newcell = codesCursor;

    codeadd(ByteCode.LIST_CELL, value, ByteCode.EOF, ByteCode.NOP);

    var lastcell = codes[list + 1];

    if (lastcell == ByteCode.NOP) {
      codes[list + 1] = newcell; // list last cell
      codes[list + 3] = ByteCode.JMP;
      codes[list + 4] = newcell;

      return list;
    }

    codes[list + 1] = newcell; // list last cell
    codes[lastcell + 2] = ByteCode.JMP;
    codes[lastcell + 3] = newcell;

    return list;
  }

  private void protoass(int value) {
    assert proto == value : value;
  }

  private int protolst() {
    protorea();

    return proto;
  }

  private void protopop() {
    protoCursor = stack[--stackCursor];
  }

  private void protopsh() {
    stack = IntArrays.growIfNecessary(stack, stackCursor);

    stack[stackCursor++] = protoCursor;

    protoCursor = proto;

    protorea();
  }

  private int protorea() {
    return proto = protoArray[protoCursor++];
  }

  private void protorea(int value) {
    proto = protoArray[protoCursor++];

    protoass(value);
  }

  private UnsupportedOperationException protouoe() {
    return new UnsupportedOperationException("Implement me :: proto=" + proto);
  }

  private int setOrThrow(int index, int value) {
    if (index == ByteCode.NOP) {
      return value;
    }

    throw new UnsupportedOperationException("Implement me");
  }

}