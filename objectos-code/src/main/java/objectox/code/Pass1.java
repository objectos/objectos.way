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

import objectos.code.ClassName;
import objectos.code.NoTypeName;

public final class Pass1 extends Pass1Super {

  public final void execute(Pass0Super pass0) {
    this.autoImports = pass0.autoImports.toPass1();

    this.source = pass0.protoArray;

    this.object = pass0.objectArray;

    codeIndex = 0;

    sourceIndex = 0;

    stackIndex = 0;

    execute();
  }

  private int annotation() {
    int name = NOP;
    int pairs = NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.CLASS_NAME -> name = setOrThrow(name, typeName());

        case ByteProto.STRING_LITERAL -> pairs = listadd(pairs, stringLiteral());

        default -> throw protouoe();
      }

      protonxt();
    }

    return add(ANNOTATION, name, pairs);
  }

  private int classDeclaration() {
    int modifiers = NOP;
    int name = NOP;
    int typeParams = NOP;
    int _extends = NOP;
    int _implements = NOP;
    int _permits = NOP;
    int body = NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.ANNOTATION -> modifiers = listadd(modifiers, annotation());

        case ByteProto.EXTENDS -> _extends = setOrReplace(_extends, typeName());

        case ByteProto.IDENTIFIER -> name = setOrReplace(name, protoadv());

        case ByteProto.METHOD_DECLARATION -> body = listadd(body, methodDeclaration());

        case ByteProto.MODIFIER -> modifiers = listadd(modifiers, modifier());

        default -> throw protouoe();
      }

      protonxt();
    }

    return add(CLASS, modifiers, name, typeParams, _extends, _implements, _permits, body);
  }

  private int compilationUnit() {
    var _package = NOP;
    var _import = NOP;
    var body = NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.CLASS_DECLARATION -> body = listadd(body, classDeclaration());

        case ByteProto.METHOD_DECLARATION -> body = listadd(body, methodDeclaration());

        case ByteProto.PACKAGE_DECLARATION -> _package = setOrThrow(_package, packageDeclaration());

        default -> body = listadd(body, statement());
      }

      protonxt();
    }

    if (_import != NOP) {
      throw new UnsupportedOperationException("Implement me :: unexpected imports");
    }

    if (autoImports.enabled()) {
      _import = importDeclarations();
    }

    return add(COMPILATION_UNIT, _package, _import, body);
  }

  private void execute() {
    add(JMP, NOP);

    protoadv();

    protojmp();

    protoass(ByteProto.COMPILATION_UNIT);

    var unit = compilationUnit();

    code[1] = unit;
  }

  private int expression() {
    return switch (proto) {
      case ByteProto.METHOD_INVOCATION -> methodInvocation();

      case ByteProto.STRING_LITERAL -> stringLiteral();

      default -> throw protouoe();
    };
  }

  private int expressionName() {
    var self = add(EXPRESSION_NAME);

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.CLASS_NAME -> typeName();

        case ByteProto.IDENTIFIER -> add(IDENTIFIER, protoadv());

        default -> protouoe();
      }

      protonxt();
    }

    add(NOP);

    return self;
  }

  private int importDeclarations() {
    var self = codeIndex;

    var entries = autoImports.entrySet();

    for (var entry : entries) {
      Integer value = entry.getValue();

      add(IMPORT, value);
    }

    add(EOF);

    return self;
  }

  private int localVariableDeclaration() {
    var modifiers = NOP;
    var type = NOP;
    var name = NOP;
    var init = NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.IDENTIFIER -> name = setOrThrow(name, protoadv());

        default -> init = setOrThrow(init, expression());
      }

      protonxt();
    }

    return add(LOCAL_VARIABLE, modifiers, type, name, init);
  }

  private int methodDeclaration() {
    var modifiers = NOP;
    var typeParams = NOP;
    var returnType = NOP;
    var name = NOP;
    var receiver = NOP;
    var params = NOP;
    var _throws = NOP;
    var body = NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.ANNOTATION -> modifiers = listadd(modifiers, annotation());

        case ByteProto.IDENTIFIER -> name = setOrReplace(name, protoadv());

        case ByteProto.MODIFIER -> modifiers = listadd(modifiers, modifier());

        case ByteProto.TYPE_NAME -> returnType = setOrReplace(returnType, typeName());

        default -> body = listadd(body, statement());
      }

      protonxt();
    }

    return add(
      METHOD,
      modifiers, typeParams, returnType, name, receiver, params, _throws,
      body
    );
  }

  private int methodInvocation() {
    var callee = NOP;
    var typeArgs = NOP;
    var name = NOP;
    var args = NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.EXPRESSION_NAME -> args = listadd(args, expressionName());

        case ByteProto.IDENTIFIER -> name = setOrThrow(name, protoadv());

        case ByteProto.NEW_LINE -> args = listadd(args, newLine());

        case ByteProto.METHOD_INVOCATION -> args = listadd(args, methodInvocation());

        case ByteProto.STRING_LITERAL -> args = listadd(args, stringLiteral());

        default -> throw protouoe();
      }

      protonxt();
    }

    return add(METHOD_INVOCATION, callee, typeArgs, name, args);
  }

  private int modifier() {
    return add(MODIFIER, protoadv());
  }

  private int newLine() {
    return add(NEW_LINE);
  }

  private int packageDeclaration() {
    var annotations = NOP;
    var name = NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.PACKAGE_NAME -> name = setOrReplace(name, protoadv());

        default -> throw protouoe();
      }

      protonxt();
    }

    return add(PACKAGE, annotations, name);
  }

  private int statement() {
    return switch (proto) {
      case ByteProto.LOCAL_VARIABLE -> localVariableDeclaration();

      case ByteProto.METHOD_INVOCATION -> methodInvocation();

      default -> throw protouoe();
    };
  }

  private int stringLiteral() {
    return add(STRING_LITERAL, protoadv());
  }

  private int typeName() {
    protoadv();

    var o = objget(proto);

    var code = typeNameCode(o);

    return add(code, proto);
  }

  private int typeNameCode(Object o) {
    if (o instanceof ClassName className) {
      if (autoImports.addClassName(className, proto)) {
        return SIMPLE_NAME;
      } else {
        return QUALIFIED_NAME;
      }
    } else if (o instanceof NoTypeName noType) {
      return NO_TYPE;
    } else {
      throw new UnsupportedOperationException("Implement me :: type=" + o.getClass());
    }
  }

}