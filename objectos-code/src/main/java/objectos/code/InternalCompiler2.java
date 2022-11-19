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

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.COMPILATION_UNIT -> compilationUnit();

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> throw $protouoe(proto);
      }
    }
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

      $codeadd(ByteCode.LHEAD, value, ByteCode.LNULL, ByteCode.LNULL);

      codeArray[listIndex] = list;
    } else {
      var head = codeArray[list];

      assert head == ByteCode.LHEAD : head;

      var next = codeIndex;

      $codeadd(ByteCode.LNEXT, value, ByteCode.LNULL);

      var last = codeArray[list + 3];

      var target = last != ByteCode.LNULL ? last : list;

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

  private int assignmentExpression() {
    var lhs = ByteCode.NOP;
    var operator = ByteCode.NOP;
    var expression = ByteCode.NOP;

    protoadv();

    while (protolop()) {
      protojmp();

      switch (proto) {
        case ByteProto.ASSIGNMENT_OPERATOR -> operator = setOrThrow(operator, protoadv());

        case ByteProto.ARRAY_ACCESS_EXPRESSION,
            ByteProto.EXPRESSION_NAME,
            ByteProto.FIELD_ACCESS_EXPRESSION0 -> {
          if (lhs == ByteCode.NOP) {
            lhs = setOrThrow(lhs, expression());
          } else {
            expression = setOrThrow(expression, expression());
          }
        }

        default -> expression = setOrThrow(expression, expression());
      }

      protonxt();
    }

    return codeadd(ByteCode.ASSIGNMENT_EXPRESSION, lhs, operator, expression);
  }

  private int classDeclaration(boolean topLevel) {
    $elemadd(
      ByteCode.CLASS,
      ByteCode.NOP, /* modifiers = 1 */
      ByteCode.NOP, /* name = 2 */
      ByteCode.NOP, /* tparams = 3 */
      ByteCode.NOP, /* extends = 4 */
      ByteCode.NOP, /* implements = 5 */
      ByteCode.NOP, /* permits = 6 */
      ByteCode.NOP /* body = 7 */
    );

    var publicFound = false;
    var simpleName = "Unnamed";

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.IDENTIFIER -> $elemset(2, identifier());

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

  /*
   * new ClassOrInterfaceTypeToInstantiate ( [ArgumentList] )
   */
  private int classInstanceCreation0() {
    var qualifier = ByteCode.NOP;
    var ctypeargs = ByteCode.NOP;
    var type = ByteCode.NOP;
    var typeargs = ByteCode.NOP;
    var args = ByteCode.NOP;
    var cbody = ByteCode.NOP;

    protoadv();

    protobrk("""
    Invalid class instance creation (0) expression:

    Found 'BREAK' but expected 'type to be instantiated' instead.
    """);

    protojmp();
    type = typeName();
    protonxt();

    while (protolop()) {
      protojmp();

      if (ByteProto.isExpression(proto)) {
        args = listadd(args, expression());
      } else {
        throw new UnsupportedOperationException("Implement me");
      }

      protonxt();
    }

    return codeadd(
      ByteCode.CLASS_INSTANCE_CREATION,
      qualifier, ctypeargs, type, typeargs, args, cbody
    );
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

  private int expression() {
    return switch (proto) {
      case ByteProto.ARRAY_ACCESS_EXPRESSION -> arrayAccessExpression();

      case ByteProto.ASSIGNMENT_EXPRESSION -> assignmentExpression();

      case ByteProto.CLASS_INSTANCE_CREATION0 -> classInstanceCreation0();

      case ByteProto.EXPRESSION_NAME -> expressionName();

      case ByteProto.FIELD_ACCESS_EXPRESSION0 -> fieldAccessExpression0();

      case ByteProto.METHOD_INVOCATION -> methodInvocation();

      case ByteProto.STRING_LITERAL -> stringLiteral();

      case ByteProto.THIS -> thisKeyword();

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

  private int fieldAccessExpression0() {
    protoadv();

    protobrk("""
    Invalid field access expression:

    Found BREAK but expected a primary expression
    """);

    protojmp();
    var primary = expression();
    protonxt();

    protojmp();

    protoass(ByteProto.IDENTIFIER, """
    Invalid field access expression:

    Expected an identifier but found proto=%d
    """);

    var identifier = protoadv();
    protonxt();

    return codeadd(ByteCode.FIELD_ACCESS_EXPRESSION0, primary, identifier);
  }

  private int identifier() {
    int value = $protonxt();

    $stackpop();

    return value;
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

  private int newLine() {
    return codeadd(ByteCode.NEW_LINE);
  }

  private int stringLiteral() {
    return codeadd(ByteCode.STRING_LITERAL, protoadv());
  }

  private int thisKeyword() {
    protoadv();

    return codeadd(ByteCode.THIS);
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