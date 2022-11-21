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

abstract class InternalInterpreter2 extends InternalCompiler2 {

  protected abstract void write(char c);

  protected abstract void write(String s);

  protected abstract void writeArgumentListEnd();

  protected abstract void writeArgumentListStart();

  protected abstract void writeBeforeFirstMember();

  protected abstract void writeBeforeFirstStatement();

  protected abstract void writeBeforeNextMember();

  protected abstract void writeBeforeNextStatement();

  protected abstract void writeBlockEnd(boolean contents);

  protected abstract void writeBlockStart();

  protected abstract void writeComma();

  protected abstract void writeCompilationUnitEnd(PackageName packageName, String fileName);

  protected abstract void writeCompilationUnitSeparator();

  protected abstract void writeCompilationUnitStart(PackageName packageName, String fileName);

  protected abstract void writeNewLine();

  protected abstract void writeOperator(Operator operator);

  protected abstract void writeSemicolon();

  protected abstract void writeSeparator(char c);

  protected abstract void writeSpace();

  protected abstract void writeSpaceIf(boolean condition);

  protected abstract void writeStringLiteral(String s);

  final void pass2() {
    codeIndex = 0;

    stackIndex = -1;

    $codenxt(ByteCode.ROOT);

    if ($nextjmp()) {
      $codentr(ByteCode.COMPILATION_UNIT);
      compilationUnit();
      $codexit();
    } else {
      $coderr();
    }
  }

  private void $codeass(int expected) {
    if (code != expected) {
      throw new InvalidTemplateException("""
      Could not render template.

        Expected code=%d but found=%d
      """.formatted(expected, code));
    }
  }

  private void $codentr() {
    $stackpsh();

    codeIndex = code;

    $codenxt();
  }

  private void $codentr(int expected) {
    $stackpsh();

    codeIndex = code;

    $codenxt(expected);
  }

  private void $codenxt() {
    code = codeArray[codeIndex++];
  }

  private void $codenxt(int expected) {
    $codenxt();

    $codeass(expected);
  }

  private Object $codeobj() {
    return objectArray[code];
  }

  private void $coderr() {
    throw new InvalidTemplateException("""

    Could not render template.

    Expected a jump offset but found a NOP instruction instead.

      codeIndex=%d
    """.formatted(codeIndex - 1));
  }

  private void $codexit() {
    codeIndex = $stackpop();
  }

  private boolean $lnext() {
    if (code == ByteCode.LHEAD) {
      $codenxt();

      return true;
    }

    $codenxt();

    if (code == ByteCode.NOP) {
      return false;
    }

    codeIndex = code;

    $codenxt(ByteCode.LNEXT);

    $codenxt();

    return true;
  }

  private void $malformed() {
    throw new UnsupportedOperationException("Implement me");
  }

  private boolean $nextjmp() {
    $codenxt();

    if (code > 0) {
      return true;
    } else if (code == ByteCode.NOP) {
      return false;
    } else {
      throw new InvalidTemplateException("""

      Could not render template.

      Expected a jump offset but found an instruction instead.

        code=%d
        codeIndex=%d
      """.formatted(code, codeIndex - 1));
    }
  }

  private int $stackpop() {
    return stackArray[stackIndex--];
  }

  private void $stackpsh() {
    stackIndex++;

    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex);

    stackArray[stackIndex] = codeIndex;
  }

  private void $throwuoe() {
    throw new UnsupportedOperationException("Implement me :: code=" + code);
  }

  private void argumentList() {
    var arg = 0;
    var nl = 0;

    while ($lnext()) {
      $codentr();

      if (code == ByteCode.NEW_LINE) {
        nl++;
      } else {
        if (arg > 0) {
          writeComma();
        }

        for (int i = 0; i < nl; i++) {
          writeNewLine();
        }

        expression();

        arg++;
        nl = 0;
      }

      $codexit();
    }

    for (int i = 0; i < nl; i++) {
      writeNewLine();
    }
  }

  private void classDeclaration() {
    var prevSection = false;

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: modifiers");
    }

    write("class");

    writeSpace();

    if ($nextjmp()) {
      $codentr();
      declarationSimpleName();
      $codexit();

      prevSection = true;
    } else {
      throw new UnsupportedOperationException("Implement me");
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: tparams");
    }

    if ($nextjmp()) {
      writeSpaceIf(prevSection);

      $codentr();
      extendsSingleClause();
      $codexit();
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: implements");
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: permits");
    }

    writeBlockStart();

    var contents = false;

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: body");
    }

    writeBlockEnd(contents);
  }

  private void compilationUnit() {
    writeCompilationUnitStart(autoImports.packageName, autoImports.fileName);

    var prevSection = false;

    if ($nextjmp()) {
      $codentr(ByteCode.PACKAGE);
      packageDeclaration();
      $codexit();

      prevSection = true;
    }

    if ($nextjmp()) {
      if (prevSection) {
        writeCompilationUnitSeparator();
      }

      $codentr();
      importDeclarationList();
      $codexit();

      prevSection = true;
    }

    if ($nextjmp()) {
      if (prevSection) {
        writeCompilationUnitSeparator();
      }

      $codentr(ByteCode.LHEAD);
      compilationUnitBody();
      $codexit();
    }

    writeCompilationUnitEnd(autoImports.packageName, autoImports.fileName);
  }

  private void compilationUnitBody() {
    if ($lnext()) {
      $codentr();
      compilationUnitBodyItem();
      $codexit();

      while ($lnext()) {
        writeCompilationUnitSeparator();

        $codentr();
        compilationUnitBodyItem();
        $codexit();
      }
    }
  }

  private void compilationUnitBodyItem() {
    switch (code) {
      case ByteCode.CLASS -> classDeclaration();

      case ByteCode.METHOD_DECLARATION -> methodDeclaration();

      default -> statement();
    }
  }

  private void declarationSimpleName() {
    switch (code) {
      case ByteCode.OBJECT_STRING -> objectString();

      default -> $throwuoe();
    }
  }

  private void expression() {
    switch (code) {
      case ByteCode.EXPRESSION_NAME -> expressionName();

      case ByteCode.METHOD_INVOCATION -> methodInvocation();

      case ByteCode.STRING_LITERAL -> stringLiteral();

      default -> $throwuoe();
    }
  }

  private void expressionName() {
    if ($nextjmp()) {
      $codentr();
      typeName();
      $codexit();

      write('.');
    }

    if ($nextjmp()) {
      $codentr();
      expressionNameList();
      $codexit();
    }
  }

  private void expressionNameList() {
    if ($lnext()) {
      $codentr();
      declarationSimpleName();
      $codexit();

      while ($lnext()) {
        write('.');

        $codentr();
        declarationSimpleName();
        $codexit();
      }
    }
  }

  private void expressionStatement() {
    if ($nextjmp()) {
      $codentr();
      expression();
      $codexit();
    } else {
      throw new UnsupportedOperationException("Implement me");
    }

    writeSemicolon();
  }

  private void extendsSingleClause() {
    write("extends");

    writeSpace();

    if ($nextjmp()) {
      $codentr();
      typeName();
      $codexit();
    } else {
      $malformed();
    }
  }

  private void importDeclarationList() {
    if ($lnext()) {
      $codentr();
      importDeclarationListItem();
      $codexit();

      while ($lnext()) {
        writeBeforeNextStatement();

        $codentr();
        importDeclarationListItem();
        $codexit();
      }
    }
  }

  private void importDeclarationListItem() {
    switch (code) {
      case ByteCode.IMPORT -> {
        write("import");

        writeSpace();

        $codenxt();

        var o = objectArray[code];

        write(o.toString());

        writeSemicolon();
      }

      default -> $throwuoe();
    }
  }

  private void methodDeclaration() {
    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: method modifiers");
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: method type params");
    }

    if ($nextjmp()) {
      $codentr();
      typeName();
      $codexit();
    }

    writeSpace();

    if ($nextjmp()) {
      $codentr();
      declarationSimpleName();
      $codexit();
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: method receiver param");
    }

    write('(');

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: method formal parameters");
    }

    write(')');

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: method throws");
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: method body");
    } else {
      writeBlockStart();
      writeBlockEnd(false);
    }
  }

  private void methodInvocation() {
    if ($nextjmp()) {
      $codentr();

      if (ByteCode.isTypeName(code)) {
        typeName();
      } else {
        throw new UnsupportedOperationException("Implement me");
      }

      $codexit();

      write('.');
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: method invocation type args");
    }

    if ($nextjmp()) {
      $codentr();
      declarationSimpleName();
      $codexit();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: no method name");
    }

    writeArgumentListStart();

    if ($nextjmp()) {
      $codentr();
      argumentList();
      $codexit();
    }

    writeArgumentListEnd();
  }

  private void objectString() {
    $codenxt();

    var s = (String) objectArray[code];

    write(s);
  }

  private void packageDeclaration() {
    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: package modifiers");
    }

    write("package");

    writeSpace();

    if ($nextjmp()) {
      $codentr();
      objectString();
      $codexit();
    } else {
      throw new UnsupportedOperationException("Implement me :: no package name");
    }

    writeSemicolon();
  }

  private void statement() {
    switch (code) {
      case ByteCode.EXPRESSION_STATEMENT -> expressionStatement();

      default -> $throwuoe();
    }
  }

  private void stringLiteral() {
    $codenxt();

    var s = (String) objectArray[code];

    writeStringLiteral(s);
  }

  private void typeName() {
    switch (code) {
      case ByteCode.NO_TYPE -> write("void");

      case ByteCode.QUALIFIED_NAME -> {
        $codenxt();

        var qname = (ClassName) codeobj();

        write(qname.toString());
      }

      case ByteCode.SIMPLE_NAME -> {
        $codenxt();

        var sname = (ClassName) $codeobj();

        write(sname.simpleName);
      }

      default -> $throwuoe();
    }
  }

}