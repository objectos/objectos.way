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

  private void $codeadd(int v0) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 0);

    codeArray[codeIndex++] = v0;
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

  private void $codeadd(int v0, int v1, int v2, int v3, int v4) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 4);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;
    codeArray[codeIndex++] = v3;
    codeArray[codeIndex++] = v4;
  }

  private void $codeadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 6);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;
    codeArray[codeIndex++] = v3;
    codeArray[codeIndex++] = v4;
    codeArray[codeIndex++] = v5;
    codeArray[codeIndex++] = v6;
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

  private void $codeadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 8);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;
    codeArray[codeIndex++] = v3;
    codeArray[codeIndex++] = v4;
    codeArray[codeIndex++] = v5;
    codeArray[codeIndex++] = v6;
    codeArray[codeIndex++] = v7;
    codeArray[codeIndex++] = v8;
  }

  private int $codepop() { return markArray[markIndex--]; }

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

  private void $elemadd(int v0, int v1, int v2, int v3, int v4) {
    $codepsh();

    $codeadd(v0, v1, v2, v3, v4);
  }

  private void $elemadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6) {
    $codepsh();

    $codeadd(v0, v1, v2, v3, v4, v5, v6);
  }

  private void $elemadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    $codepsh();

    $codeadd(v0, v1, v2, v3, v4, v5, v6, v7);
  }

  private void $elemadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    $codepsh();

    $codeadd(v0, v1, v2, v3, v4, v5, v6, v7, v8);
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

    return $codepop();
  }

  private void $elemset(int offset, int value) {
    var base = markArray[markIndex];

    codeArray[base + offset] = value;
  }

  private int $protonxt() { return protoArray[protoIndex++]; }

  private boolean $prototru() { return true; }

  private UnsupportedOperationException $protouoe(int proto) {
    return new UnsupportedOperationException(
      "Implement me :: proto=" + proto);
  }

  private int $simpleadd(int v0) {
    var self = codeIndex;

    $codeadd(v0);

    $stackpop();

    return self;
  }

  private int $simpleadd(int v0, int v1) {
    var self = codeIndex;

    $codeadd(v0, v1);

    $stackpop();

    return self;
  }

  private void $stackpop() { protoIndex = stackArray[--stackIndex]; }

  private void $stackpsh() {
    var location = $protonxt();

    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex);

    stackArray[stackIndex++] = protoIndex;

    protoIndex = location;
  }

  private int arrayAccessExpression() {
    $elemadd(
      ByteCode.ARRAY_ACCESS_EXPRESSION,
      ByteCode.NOP, // reference = 1
      ByteCode.NOP /// expressions = 2
    );

    var reference = false;

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> {
          if (!reference) {
            $elemset(1, expression(proto));

            reference = true;
          } else {
            $elemlst(2, expression(proto));
          }
        }
      }
    }

    return $elempop();
  }

  private int assignmentExpression() {
    $elemadd(
      ByteCode.ASSIGNMENT_EXPRESSION,
      ByteCode.NOP, // lhs = 1
      ByteCode.NOP, // operator = 2
      ByteCode.NOP /// expression = 3
    );

    var lhs = false;

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.ASSIGNMENT_OPERATOR -> { $elemset(2, $protonxt()); $stackpop(); }

        case
            ByteProto.ARRAY_ACCESS_EXPRESSION,
            ByteProto.EXPRESSION_NAME,
            ByteProto.FIELD_ACCESS_EXPRESSION0 -> {
          if (!lhs) {
            $elemset(1, expression(proto));

            lhs = true;
          } else {
            $elemset(3, expression(proto));
          }
        }

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> $elemset(3, expression(proto));
      }
    }

    return $elempop();
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
        case ByteProto.EXTENDS -> $elemset(4, extendsClause());

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

  /*
   * new ClassOrInterfaceTypeToInstantiate ( [ArgumentList] )
   */
  private int classInstanceCreation0() {
    $elemadd(
      ByteCode.CLASS_INSTANCE_CREATION,
      ByteCode.NOP, // qualifier = 1
      ByteCode.NOP, // ctypeargs = 2
      ByteCode.NOP, // type = 3
      ByteCode.NOP, // teargs = 4
      ByteCode.NOP, // args = 5
      ByteCode.NOP // cbody = 6
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      if (ByteProto.isExpression(proto)) {
        $elemlst(5, expression(proto));

        continue;
      }

      switch (proto) {
        case ByteProto.CLASS_NAME -> $elemset(3, className());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> throw $protouoe(proto);
      }
    }

    return $elempop();
  }

  private int className() {
    var proto = $protonxt();

    var o = (ClassName) objectArray[proto];

    var code = autoImports.addClassName(o, proto)
        ? ByteCode.SIMPLE_NAME
        : ByteCode.QUALIFIED_NAME;

    return $simpleadd(code, proto);
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

        case ByteProto.METHOD_DECLARATION -> $elemlst(3, methodDeclaration());

        case ByteProto.PACKAGE_DECLARATION -> $elemset(1, packageDeclaration());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> {
          if (autoImports.enabled()) {
            importDeclarations(2);
          }

          break loop;
        }

        default -> $elemlst(3, compilationUnitBodyItem(proto));
      }
    }

    return $elempop();
  }

  private int compilationUnitBodyItem(int proto) {
    if (ByteProto.isExpressionStatement(proto)) {
      return statement(proto);
    }

    if (ByteProto.isExpression(proto)) {
      return expression(proto);
    }

    return statement(proto);
  }

  private int expression(int proto) {
    return switch (proto) {
      case ByteProto.ARRAY_ACCESS_EXPRESSION -> arrayAccessExpression();

      case ByteProto.ASSIGNMENT_EXPRESSION -> assignmentExpression();

      case ByteProto.CLASS_INSTANCE_CREATION0 -> classInstanceCreation0();

      case ByteProto.EXPRESSION_NAME -> expressionName();

      case ByteProto.FIELD_ACCESS_EXPRESSION0 -> fieldAccessExpression0();

      case ByteProto.METHOD_INVOCATION -> methodInvocation();

      case ByteProto.STRING_LITERAL -> stringLiteral();

      case ByteProto.THIS -> thisKeyword();

      default -> throw $protouoe(proto);
    };
  }

  private int expressionName() {
    $elemadd(
      ByteCode.EXPRESSION_NAME,
      ByteCode.NOP, // base = 1
      ByteCode.NOP /// list = 2
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.CLASS_NAME -> $elemset(1, className());

        case ByteProto.IDENTIFIER -> $elemlst(2, objectString());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> throw $protouoe(proto);
      }
    }

    return $elempop();
  }

  private int expressionStatement(int proto) {
    $elemadd(ByteCode.EXPRESSION_STATEMENT, expression(proto));

    return $codepop();
  }

  private int extendsClause() {
    $elemadd(
      ByteCode.EXTENDS,
      ByteCode.NOP // value = 1
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.CLASS_NAME -> $elemset(1, className());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> throw $protouoe(proto);
      }
    }

    return $elempop();
  }

  private int fieldAccessExpression0() {
    $elemadd(
      ByteCode.FIELD_ACCESS_EXPRESSION0,
      ByteCode.NOP, // primary = 1
      ByteCode.NOP /// identifier = 2
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.IDENTIFIER -> $elemset(2, objectString());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> $elemset(1, expression(proto));
      }
    }

    return $elempop();
  }

  private void importDeclarations(int offset) {
    var entries = autoImports.entrySet();

    for (var entry : entries) {
      var self = codeIndex;

      Integer value = entry.getValue();

      $codeadd(ByteCode.IMPORT, value);

      $elemlst(offset, self);
    }
  }

  private int localVariableDeclaration() {
    $elemadd(
      ByteCode.LOCAL_VARIABLE,
      ByteCode.NOP, // modifiers = 1
      ByteCode.NOP, // type = 2
      ByteCode.NOP, // name = 3
      ByteCode.NOP /// init = 4
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.IDENTIFIER -> $elemset(3, objectString());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> $elemset(4, expression(proto));
      }
    }

    return $elempop();
  }

  private int methodDeclaration() {
    $elemadd(
      ByteCode.METHOD_DECLARATION,
      ByteCode.NOP, // modifiers = 1
      ByteCode.NOP, // tparams = 2
      ByteCode.NOP, // rtype = 3,
      ByteCode.NOP, // name = 4,
      ByteCode.NOP, // receiver = 5
      ByteCode.NOP, // params = 6
      ByteCode.NOP, // throws = 7
      ByteCode.NOP /// body = 8
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.IDENTIFIER -> $elemset(4, objectString());

        case ByteProto.NO_TYPE -> $elemset(3, noType());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> $elemlst(8, statement(proto));
      }
    }

    return $elempop();
  }

  private int methodInvocation() {
    $elemadd(
      ByteCode.METHOD_INVOCATION,
      ByteCode.NOP, // subject = 1
      ByteCode.NOP, // targs = 2
      ByteCode.NOP, // name = 3
      ByteCode.NOP /// args = 4
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.CLASS_NAME -> $elemset(1, className());

        case ByteProto.IDENTIFIER -> $elemset(3, objectString());

        case ByteProto.NEW_LINE -> $elemlst(4, newLine());

        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> $elemlst(4, expression(proto));
      }
    }

    return $elempop();
  }

  private int newLine() {
    return $simpleadd(ByteCode.NEW_LINE);
  }

  private int noType() {
    return $simpleadd(ByteCode.NO_TYPE);
  }

  private int objectString() {
    return $simpleadd(ByteCode.OBJECT_STRING, $protonxt());
  }

  private int packageDeclaration() {
    $elemadd(
      ByteCode.PACKAGE,
      ByteCode.NOP, // annotations = 1
      ByteCode.NOP /// name = 2
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

  private int returnStatement() {
    $elemadd(
      ByteCode.RETURN_STATEMENT,
      ByteCode.NOP /// expression = 1
    );

    loop: while ($prototru()) {
      var proto = $protonxt();

      switch (proto) {
        case ByteProto.JMP -> $stackpsh();

        case ByteProto.BREAK -> { break loop; }

        default -> $elemset(1, expression(proto));
      }
    }

    return $elempop();
  }

  private int statement(int proto) {
    return switch (proto) {
      case ByteProto.LOCAL_VARIABLE -> localVariableDeclaration();

      case ByteProto.RETURN_STATEMENT -> returnStatement();

      default -> expressionStatement(proto);
    };
  }

  private int stringLiteral() {
    return $simpleadd(ByteCode.STRING_LITERAL, $protonxt());
  }

  private int thisKeyword() {
    return $simpleadd(ByteCode.THIS);
  }

}