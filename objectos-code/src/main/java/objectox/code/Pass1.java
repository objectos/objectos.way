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

public final class Pass1 {

  private State state;

  public final void execute(State state) {
    this.state = state.startPass1();

    execute();
  }

  private void execute() {
    state.protorea(ByteProto.JMP);

    state.protorea();

    state.protopsh();

    state.protoass(ByteProto.COMPILATION_UNIT);

    executeCompilationUnit();
  }

  private int executeAnnotation() {
    int name = ByteCode.NOP;
    int pairs = ByteCode.NOP;

    var self = state.codeadd(ByteCode.ANNOTATION, name, pairs);

    var children = state.protolst();

    for (int i = 0; i < children; i++) {
      state.protorea();

      switch (state.protopsh()) {
        case ByteProto.CLASS_NAME -> name = setOrThrow(name, state.protorea());

        default -> throw state.protouoe();
      }

      state.protopop();
    }

    return state.codeset(self, name, pairs);
  }

  private int executeClass() {
    int modifiers = ByteCode.NOP;
    int name = ByteCode.NOP;
    int typeArgs = ByteCode.NOP;
    int _extends = ByteCode.NOP;
    int _implements = ByteCode.NOP;
    int _permits = ByteCode.NOP;
    int body = ByteCode.NOP;

    var self = state.codeadd(
      ByteCode.CLASS,
      modifiers, name, typeArgs, _extends, _implements, _permits,
      body
    );

    var children = state.protolst();

    for (int i = 0; i < children; i++) {
      state.protorea();

      switch (state.protopsh()) {
        case ByteProto.ANNOTATION -> modifiers = state.codelst(modifiers, executeAnnotation());

        case ByteProto.MODIFIER -> modifiers = state.codelst(modifiers, executeModifier());

        case ByteProto.IDENTIFIER -> name = setOrThrow(name, state.protorea());

        case ByteProto.EXTENDS -> _extends = setOrThrow(_extends, state.protorea());

        case ByteProto.METHOD -> body = state.codelst(body, executeMethod());

        default -> throw state.protouoe();
      }

      state.protopop();
    }

    return state.codeset(
      self,
      modifiers, name, typeArgs, _extends, _implements, _permits,
      body
    );
  }

  private void executeCompilationUnit() {
    var _package = ByteCode.NOP;
    var _import = ByteCode.NOP;
    var body = ByteCode.NOP;

    var self = state.codeadd(
      ByteCode.COMPILATION_UNIT,
      _package, _import, body
    );

    var children = state.protolst();

    for (int i = 0; i < children; i++) {
      state.protorea();

      switch (state.protopsh()) {
        case ByteProto.PACKAGE -> _package = setOrThrow(_package, executePackage());

        case ByteProto.CLASS -> body = state.codelst(body, executeClass());

        case ByteProto.LOCAL_VARIABLE -> body = state.codelst(body, executeLocalVariable());

        case ByteProto.METHOD -> body = state.codelst(body, executeMethod());

        case ByteProto.METHOD_INVOCATION -> body = state.codelst(body, executeMethodInvocation());

        default -> throw state.protouoe();
      }

      state.protopop();
    }

    if (_import != ByteCode.NOP) {
      throw new UnsupportedOperationException("Implement me :: unexpected imports");
    }

    if (state.importSet().enabled) {
      _import = executeEofImportSet();
    }

    state.codeset(
      self,
      _package, _import, body
    );
  }

  private int executeEofImportSet() {
    var self = state.codeadd();

    var sorted = state.importSet().sort();

    for (int i = 0, size = sorted.size(); i < size; i++) {
      state.codeadd(ByteCode.IMPORT, i);
    }

    state.codeadd(ByteCode.EOF);

    return self;
  }

  private int executeExpressionName() {
    var children = state.protolst();

    var self = state.codeadd(
      ByteCode.EXPRESSION_NAME, children
    );

    for (int i = 0; i < children; i++) {
      state.protorea();

      switch (state.protopsh()) {
        case ByteProto.CLASS_NAME, ByteProto.IDENTIFIER -> state.codeadd(state.protorea());

        default -> throw state.protouoe();
      }

      state.protopop();
    }

    return self;
  }

  private int executeLocalVariable() {
    var modifiers = ByteCode.NOP;
    var type = ByteCode.NOP;
    var name = ByteCode.NOP;
    var init = ByteCode.NOP;

    var self = state.codeadd(
      ByteCode.LOCAL_VARIABLE,
      modifiers, type, name, init
    );

    var children = state.protolst();

    for (int i = 0; i < children; i++) {
      state.protorea();

      switch (state.protopsh()) {
        case ByteProto.IDENTIFIER -> name = setOrThrow(name, state.protorea());

        case ByteProto.STRING_LITERAL -> init = setOrThrow(init, executeStringLiteral());

        default -> throw state.protouoe();
      }

      state.protopop();
    }

    return state.codeset(
      self,
      modifiers, type, name, init
    );
  }

  private int executeMethod() {
    int modifiers = ByteCode.NOP;
    int typeParams = ByteCode.NOP;
    int returnType = ByteCode.NOP;
    int name = ByteCode.NOP;
    int receiver = ByteCode.NOP;
    int params = ByteCode.NOP;
    int _throws = ByteCode.NOP;
    int body = ByteCode.NOP;

    var self = state.codeadd(
      ByteCode.METHOD,
      modifiers, typeParams, returnType, name, receiver, params, _throws,
      body
    );

    var children = state.protolst();

    for (int i = 0; i < children; i++) {
      state.protorea();

      switch (state.protopsh()) {
        case ByteProto.ANNOTATION -> modifiers = state.codelst(modifiers, executeAnnotation());

        case ByteProto.MODIFIER -> modifiers = state.codelst(modifiers, executeModifier());

        case ByteProto.IDENTIFIER -> name = setOrThrow(name, state.protorea());

        case ByteProto.TYPE_NAME -> returnType = setOrThrow(returnType, state.protorea());

        case ByteProto.METHOD_INVOCATION -> body = state.codelst(body, executeMethodInvocation());

        default -> throw state.protouoe();
      }

      state.protopop();
    }

    return state.codeset(
      self,
      modifiers, typeParams, returnType, name, receiver, params, _throws,
      body
    );
  }

  private int executeMethodInvocation() {
    int callee = ByteCode.NOP;
    int typeArgs = ByteCode.NOP;
    int name = ByteCode.NOP;
    int args = ByteCode.NOP;

    var self = state.codeadd(
      ByteCode.METHOD_INVOCATION,
      callee, typeArgs, name, args
    );

    var size = state.protolst();

    for (int i = 0; i < size; i++) {
      state.protorea();

      switch (state.protopsh()) {
        case ByteProto.IDENTIFIER -> name = setOrThrow(name, state.protorea());

        case ByteProto.EXPRESSION_NAME -> args = state.codelst(args, executeExpressionName());

        case ByteProto.NEW_LINE -> args = state.codelst(args, executeNewLine());

        case ByteProto.METHOD_INVOCATION -> args = state.codelst(args, executeMethodInvocation());

        case ByteProto.STRING_LITERAL -> args = state.codelst(args, executeStringLiteral());

        default -> throw state.protouoe();
      }

      state.protopop();
    }

    return state.codeset(
      self,
      callee, typeArgs, name, args
    );
  }

  private int executeModifier() {
    var proto = state.protorea();

    return state.codeadd(ByteCode.MODIFIER, proto);
  }

  private int executeNewLine() {
    return state.codeadd(ByteCode.NEW_LINE);
  }

  private int executePackage() {
    var annotations = ByteCode.NOP;
    var name = ByteCode.NOP;

    var self = state.codeadd(
      ByteCode.PACKAGE,
      annotations, name
    );

    var children = state.protolst();

    for (int i = 0; i < children; i++) {
      state.protorea();

      switch (state.protopsh()) {
        case ByteProto.PACKAGE_NAME -> name = setOrThrow(name, state.protorea());

        default -> throw state.protouoe();
      }

      state.protopop();
    }

    return state.codeset(
      self,
      annotations, name
    );
  }

  private int executeStringLiteral() {
    return state.codeadd(ByteCode.STRING_LITERAL, state.protorea());
  }

  private int setOrThrow(int index, int value) {
    if (index == ByteCode.NOP) {
      return value;
    }

    throw new UnsupportedOperationException("Implement me");
  }

}