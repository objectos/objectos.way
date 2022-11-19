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

  @FunctionalInterface
  private interface Executable {
    void execute();
  }

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

    if ($codejmp(ByteCode.COMPILATION_UNIT)) {
      compilationUnit();
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

  private boolean $codejmp() {
    $codenxt();

    if (code == ByteCode.NOP) {
      return false;
    }

    if (code < 0) {
      throw new InvalidTemplateException("""

      Could not render template.

      Expected a jump offset but found an instruction instead.

        code=%d
        codeIndex=%d
      """.formatted(code, codeIndex - 1));
    }

    $stackpsh();

    codeIndex = code;

    $codenxt();

    return true;
  }

  private boolean $codejmp(int expected) {
    var success = $codejmp();

    if (success) {
      $codeass(expected);
    }

    return success;
  }

  private void $codenxt() {
    code = codeArray[codeIndex++];
  }

  private void $codenxt(int expected) {
    $codenxt();

    $codeass(expected);
  }

  private void $coderet() {
    codeIndex = $stackpop();
  }

  private void $coderr() {
    throw new InvalidTemplateException("""

    Could not render template.

    Expected a jump offset but found a NOP instruction instead.

      codeIndex=%d
    """.formatted(codeIndex - 1));
  }

  private void $execjmp(Executable executable) {
    executable.execute();

    $coderet();
  }

  private void $execlst(Executable executable) {
    $codejmp();

    executable.execute();

    $coderet();
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

  private void classDeclaration() {
    if ($codejmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: modifiers");
    }

    write("class");

    writeSpace();

    if ($codejmp()) {
      $execjmp(this::declarationSimpleName);
    } else {
      throw new UnsupportedOperationException("Implement me");
    }

    if ($codejmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: tparams");
    }

    if ($codejmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: extends");
    }

    if ($codejmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: implements");
    }

    if ($codejmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: permits");
    }

    writeBlockStart();

    var contents = false;

    if ($codejmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: body");
    }

    writeBlockEnd(contents);
  }

  private void compilationUnit() {
    writeCompilationUnitStart(autoImports.packageName, autoImports.fileName);

    var prevSection = false;

    if ($codejmp(ByteCode.LHEAD)) {
      $execjmp(this::compilationUnitPackage);

      prevSection = true;
    }

    if ($codejmp()) {
      throw new UnsupportedOperationException("Implement me");
    }

    if ($codejmp(ByteCode.LHEAD)) {
      if (prevSection) {
        writeCompilationUnitSeparator();
      }

      $execjmp(this::compilationUnitBody);
    }

    writeCompilationUnitEnd(autoImports.packageName, autoImports.fileName);
  }

  private void compilationUnitBody() {
    $execlst(this::compilationUnitBodyItem);

    while ($codejmp()) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  private void compilationUnitBodyItem() {
    switch (code) {
      case ByteCode.CLASS -> classDeclaration();

      default -> $throwuoe();
    }
  }

  private void compilationUnitPackage() {
    $execlst(this::compilationUnitPackageItem);

    while ($codejmp()) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  private void compilationUnitPackageItem() {
    switch (code) {
      case ByteCode.PACKAGE -> packageDeclaration();

      default -> $throwuoe();
    }
  }

  private void declarationSimpleName() {
    switch (code) {
      case ByteCode.OBJECT_STRING -> objectString();

      default -> $throwuoe();
    }
  }

  private void objectString() {
    $codenxt();

    var s = (String) objectArray[code];

    write(s);
  }

  private void packageDeclaration() {
    if ($codejmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: package modifiers");
    }

    write("package");

    writeSpace();

    if ($codejmp()) {
      $execjmp(this::objectString);
    } else {
      throw new UnsupportedOperationException("Implement me :: no package name");
    }

    writeSemicolon();
  }

}