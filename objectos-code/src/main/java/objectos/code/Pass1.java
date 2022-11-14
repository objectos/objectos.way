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

import javax.lang.model.element.Modifier;

class Pass1 extends Pass0 {

  final void pass1() {
    codeIndex = 0;

    protoIndex = 0;

    stackIndex = 0;

    codeadd(ByteCode.JMP, ByteCode.NOP);

    protoadv();

    protojmp();

    protoass(ByteProto.COMPILATION_UNIT);

    var unit = compilationUnit();

    codeArray[1] = unit;
  }

  private int annotation() {
    int name = ByteCode.NOP;
    int pairs = ByteCode.NOP;

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

    return codeadd(ByteCode.ANNOTATION, name, pairs);
  }

  private int arrayAccessExpression() {
    int reference = ByteCode.NOP;
    int expressions = ByteCode.NOP;

    protoadv();

    if (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.EXPRESSION_NAME -> reference = expressionName();

        default -> throw protouoe();
      }

      protonxt();

      while (protolop()) {
        protojmp();

        expressions = listadd(expressions, expression());

        protonxt();
      }
    }

    return codeadd(ByteCode.ARRAY_ACCESS_EXPRESSION, reference, expressions);
  }

  private int classDeclaration(boolean topLevel) {
    var modifiers = ByteCode.NOP;
    var name = ByteCode.NOP;
    var typeParams = ByteCode.NOP;
    var _extends = ByteCode.NOP;
    var _implements = ByteCode.NOP;
    var _permits = ByteCode.NOP;
    var body = ByteCode.NOP;

    var publicFound = false;
    var simpleName = "Unnamed";

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.ANNOTATION -> modifiers = listadd(modifiers, annotation());

        case ByteProto.EXTENDS -> _extends = setOrReplace(_extends, typeName());

        case ByteProto.IDENTIFIER -> {
          name = setOrReplace(name, protoadv());

          simpleName = (String) objget(proto);
        }

        case ByteProto.METHOD_DECLARATION -> body = listadd(body, methodDeclaration());

        case ByteProto.MODIFIER -> {
          modifiers = listadd(modifiers, modifier());

          var modifier = objget(proto);

          publicFound = modifier == Modifier.PUBLIC;
        }

        default -> throw protouoe();
      }

      protonxt();
    }

    if (topLevel) {
      autoImports.fileName(publicFound, simpleName);
    }

    return codeadd(
      ByteCode.CLASS,
      modifiers, name, typeParams, _extends, _implements, _permits,
      body
    );
  }

  private int compilationUnit() {
    var _package = ByteCode.NOP;
    var _import = ByteCode.NOP;
    var body = ByteCode.NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.CLASS_DECLARATION -> body = listadd(body, classDeclaration(true));

        case ByteProto.ENUM_DECLARATION -> body = listadd(body, enumDeclaration(true));

        case ByteProto.FIELD_DECLARATION -> body = listadd(body, fieldDeclaration());

        case ByteProto.METHOD_DECLARATION -> body = listadd(body, methodDeclaration());

        case ByteProto.PACKAGE_DECLARATION -> _package = setOrThrow(_package, packageDeclaration());

        default -> body = listadd(body, compilationUnitBodyItem());
      }

      protonxt();
    }

    if (_import != ByteCode.NOP) {
      throw new UnsupportedOperationException("Implement me :: unexpected imports");
    }

    if (autoImports.enabled()) {
      _import = importDeclarations();
    }

    return codeadd(ByteCode.COMPILATION_UNIT, _package, _import, body);
  }

  private int compilationUnitBodyItem() {
    if (ByteProto.isExpression(proto)) {
      return expression();
    } else {
      return statement();
    }
  }

  private int declaratorFull(int name, int init) {
    return codeadd(ByteCode.DECLARATOR_FULL, name, init);
  }

  private int declaratorSimple(int name) {
    return codeadd(ByteCode.DECLARATOR_SIMPLE, name);
  }

  private int enumConstant() {
    var modifiers = ByteCode.NOP;
    var name = ByteCode.NOP;
    var arguments = ByteCode.NOP;
    var body = ByteCode.NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      if (ByteProto.isExpression(proto)) {
        arguments = listadd(arguments, expression());
      } else {
        switch (proto) {
          case ByteProto.IDENTIFIER -> name = setOrReplace(name, protoadv());

          default -> throw protouoe();
        }
      }

      protonxt();
    }

    return codeadd(ByteCode.ENUM_CONSTANT, modifiers, name, arguments, body);
  }

  private int enumDeclaration(boolean topLevel) {
    var modifiers = ByteCode.NOP;
    var name = ByteCode.NOP;
    var _implements = ByteCode.NOP;
    var constants = ByteCode.NOP;
    var body = ByteCode.NOP;

    var publicFound = false;
    var simpleName = "Unnamed";

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.ANNOTATION -> modifiers = listadd(modifiers, annotation());

        case ByteProto.ENUM_CONSTANT -> constants = listadd(constants, enumConstant());

        case ByteProto.IDENTIFIER -> {
          name = setOrReplace(name, protoadv());

          simpleName = (String) objget(proto);
        }

        case ByteProto.IMPLEMENTS -> _implements = implementsClause(_implements);

        case ByteProto.METHOD_DECLARATION -> body = listadd(body, methodDeclaration());

        case ByteProto.MODIFIER -> {
          modifiers = listadd(modifiers, modifier());

          var modifier = objget(proto);

          publicFound = modifier == Modifier.PUBLIC;
        }

        default -> throw protouoe();
      }

      protonxt();
    }

    if (topLevel) {
      autoImports.fileName(publicFound, simpleName);
    }

    return codeadd(
      ByteCode.ENUM_DECLARATION,
      modifiers, name, _implements, constants,
      body
    );
  }

  private int expression() {
    return switch (proto) {
      case ByteProto.ARRAY_ACCESS_EXPRESSION -> arrayAccessExpression();

      case ByteProto.EXPRESSION_NAME -> expressionName();

      case ByteProto.METHOD_INVOCATION -> methodInvocation();

      case ByteProto.STRING_LITERAL -> stringLiteral();

      default -> throw protouoe();
    };
  }

  private int expressionName() {
    var self = codeadd(ByteCode.EXPRESSION_NAME);

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.CLASS_NAME -> typeName();

        case ByteProto.IDENTIFIER -> codeadd(ByteCode.IDENTIFIER, protoadv());

        default -> protouoe();
      }

      protonxt();
    }

    codeadd(ByteCode.NOP);

    return self;
  }

  private int fieldDeclaration() {
    var modifiers = ByteCode.NOP;
    var type = ByteCode.NOP;
    var declarators = ByteCode.NOP;
    var name = ByteCode.NOP;
    var init = ByteCode.NOP;

    enum State {
      START,

      IDENTIFIER,

      INITIALIZE;
    }

    var state = State.START;

    protoadv();

    while (protolop()) {
      protojmp();

      if (ByteProto.isExpression(proto)) {
        state = switch (state) {
          case IDENTIFIER -> {
            init = expression();

            yield State.INITIALIZE;
          }

          default -> throw new UnsupportedOperationException(
            "Implement me :: state=" + state);
        };

        protonxt();

        continue;
      }

      switch (proto) {
        case ByteProto.IDENTIFIER -> {
          state = switch (state) {
            case START -> {
              name = protoadv();

              yield State.IDENTIFIER;
            }

            case IDENTIFIER -> {
              declarators = listadd(declarators, declaratorSimple(name));

              name = protoadv();

              yield State.IDENTIFIER;
            }

            case INITIALIZE -> {
              declarators = listadd(declarators, declaratorFull(name, init));

              name = protoadv();

              yield State.IDENTIFIER;
            }

            default -> throw new UnsupportedOperationException(
              "Implement me :: state=" + state);
          };
        }

        case ByteProto.MODIFIER -> modifiers = listadd(modifiers, modifier());

        case ByteProto.TYPE_NAME -> type = setOrReplace(type, typeName());

        default -> throw protouoe();
      }

      protonxt();
    }

    switch (state) {
      case IDENTIFIER -> declarators = listadd(declarators, declaratorSimple(name));

      case INITIALIZE -> declarators = listadd(declarators, declaratorFull(name, init));

      default -> throw new UnsupportedOperationException("Implement me");
    }

    return codeadd(ByteCode.FIELD_DECLARATION, modifiers, type, declarators);
  }

  private int formalParameter() {
    var modifiers = ByteCode.NOP;
    var type = ByteCode.NOP;
    var name = ByteCode.NOP;
    var varArity = ByteCode.NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.IDENTIFIER -> name = setOrReplace(name, protoadv());

        case ByteProto.TYPE_NAME -> type = setOrReplace(type, typeName());

        default -> throw protouoe();
      }

      protonxt();
    }

    return codeadd(ByteCode.FORMAL_PARAMETER, modifiers, type, name, varArity);
  }

  private int implementsClause(int list) {
    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.TYPE_NAME -> list = listadd(list, typeName());

        default -> throw protouoe();
      }

      protonxt();
    }

    return list;
  }

  private int importDeclarations() {
    var self = codeIndex;

    var entries = autoImports.entrySet();

    for (var entry : entries) {
      Integer value = entry.getValue();

      codeadd(ByteCode.IMPORT, value);
    }

    codeadd(ByteCode.EOF);

    return self;
  }

  private int localVariableDeclaration() {
    var modifiers = ByteCode.NOP;
    var type = ByteCode.NOP;
    var name = ByteCode.NOP;
    var init = ByteCode.NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.IDENTIFIER -> name = setOrThrow(name, protoadv());

        default -> init = setOrThrow(init, expression());
      }

      protonxt();
    }

    return codeadd(ByteCode.LOCAL_VARIABLE, modifiers, type, name, init);
  }

  private int methodDeclaration() {
    var modifiers = ByteCode.NOP;
    var typeParams = ByteCode.NOP;
    var returnType = ByteCode.NOP;
    var name = ByteCode.NOP;
    var receiver = ByteCode.NOP;
    var params = ByteCode.NOP;
    var _throws = ByteCode.NOP;
    var body = ByteCode.NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.ANNOTATION -> modifiers = listadd(modifiers, annotation());

        case ByteProto.FORMAL_PARAMETER -> params = listadd(params, formalParameter());

        case ByteProto.IDENTIFIER -> name = setOrReplace(name, protoadv());

        case ByteProto.MODIFIER -> modifiers = listadd(modifiers, modifier());

        case ByteProto.TYPE_NAME -> returnType = setOrReplace(returnType, typeName());

        default -> body = listadd(body, statement());
      }

      protonxt();
    }

    return codeadd(
      ByteCode.METHOD,
      modifiers, typeParams, returnType, name, receiver, params, _throws,
      body
    );
  }

  private int methodInvocation() {
    var subject = ByteCode.NOP;
    var typeArgs = ByteCode.NOP;
    var name = ByteCode.NOP;
    var args = ByteCode.NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.EXPRESSION_NAME -> args = listadd(args, expressionName());

        case ByteProto.IDENTIFIER -> name = setOrThrow(name, protoadv());

        case ByteProto.NEW_LINE -> args = listadd(args, newLine());

        case ByteProto.METHOD_INVOCATION -> args = listadd(args, methodInvocation());

        case ByteProto.STRING_LITERAL -> args = listadd(args, stringLiteral());

        case ByteProto.TYPE_NAME -> subject = setOrReplace(subject, typeName());

        default -> throw protouoe();
      }

      protonxt();
    }

    return codeadd(ByteCode.METHOD_INVOCATION, subject, typeArgs, name, args);
  }

  private int modifier() {
    return codeadd(ByteCode.MODIFIER, protoadv());
  }

  private int newLine() {
    return codeadd(ByteCode.NEW_LINE);
  }

  private int packageDeclaration() {
    var annotations = ByteCode.NOP;
    var name = ByteCode.NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.PACKAGE_NAME -> name = setOrReplace(name, protoadv());

        default -> throw protouoe();
      }

      protonxt();
    }

    return codeadd(ByteCode.PACKAGE, annotations, name);
  }

  private int returnStatement() {
    protoadv();

    if (!protolop()) {
      throw new UnsupportedOperationException(
        "Implement me :: invalid return statement");
    }

    protojmp();

    if (!ByteProto.isExpression(proto)) {
      throw new UnsupportedOperationException(
        "Implement me :: invalid return statement");
    }

    var expression = expression();

    protonxt();

    if (protolop()) {
      throw new UnsupportedOperationException(
        "Implement me :: invalid return statement");
    }

    return codeadd(ByteCode.RETURN_STATEMENT, expression);
  }

  private int statement() {
    if (ByteProto.isExpressionStatement(proto)) {
      return expression();
    } else {
      return switch (proto) {
        case ByteProto.LOCAL_VARIABLE -> localVariableDeclaration();

        case ByteProto.RETURN_STATEMENT -> returnStatement();

        default -> throw protouoe();
      };
    }
  }

  private int stringLiteral() {
    return codeadd(ByteCode.STRING_LITERAL, protoadv());
  }

  private int typeName() {
    protoadv();

    var o = objget(proto);

    var code = typeNameCode(o);

    return codeadd(code, proto);
  }

  private int typeNameCode(Object o) {
    if (o instanceof ClassName className) {
      if (autoImports.addClassName(className, proto)) {
        return ByteCode.SIMPLE_NAME;
      } else {
        return ByteCode.QUALIFIED_NAME;
      }
    } else if (o instanceof NoTypeName noType) {
      return ByteCode.NO_TYPE;
    } else {
      throw new UnsupportedOperationException("Implement me :: type=" + o.getClass());
    }
  }

}