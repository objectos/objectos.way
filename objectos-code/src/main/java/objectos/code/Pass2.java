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

abstract class Pass2 extends Pass1 {

  private boolean abstractModifier;

  private boolean annotationLast;

  private int modifierCount;

  protected abstract void write(char c);

  protected abstract void write(String s);

  protected abstract void writeArgumentListEnd();

  protected abstract void writeArgumentListStart();

  protected abstract void writeBeforeBlockNextItem();

  protected abstract void writeBeforeClassFirstMember();

  protected abstract void writeBeforeCompilationUnitBody();

  protected abstract void writeBlockEnd();

  protected abstract void writeBlockStart();

  protected abstract void writeComma();

  protected abstract void writeCompilationUnitEnd(PackageName packageName, String fileName);

  protected abstract void writeCompilationUnitStart(PackageName packageName, String fileName);

  protected abstract void writeNewLine();

  protected abstract void writeSemicolon();

  protected abstract void writeSeparator(char c);

  protected abstract void writeSpace();

  protected abstract void writeSpaceIf(boolean condition);

  protected abstract void writeStringLiteral(String s);

  final void pass2() {
    codeIndex = 0;

    stackIndex = 0;

    codejmp();

    codeass(ByteCode.COMPILATION_UNIT);

    compilationUnit();
  }

  private void annotation() {
    if (codenxt()) {
      codepsh();

      write('@');

      write(typeName());

      codepop();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: no annotation class name!");
    }

    if (codenxt()) {
      codepsh();

      writeArgumentListStart();

      while (lnext()) {
        annotationPairItem();
      }

      writeArgumentListEnd();

      codepop();
    }
  }

  private void annotationPairItem() {
    switch (code) {
      case ByteCode.STRING_LITERAL -> stringLiteral();

      default -> throw codeuoe();
    }
  }

  private void classDeclaration() {
    var prevSection = false;

    modifierReset();

    if (codenxt()) {
      codepsh();
      modifierList();
      codepop();

      prevSection = true;
    }

    if (codenxt()) {
      newLineOrSpace(prevSection);

      write("class");

      writeSpace();

      write((String) codeobj());

      prevSection = true;
    } else {
      throw new UnsupportedOperationException("Implement me");
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: class type args");
    }

    if (codenxt()) {
      codepsh();

      writeSpaceIf(prevSection);

      write("extends");

      writeSpace();

      write(typeName());

      codepop();
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: class implements clause");
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: class permits clause");
    }

    writeBlockStart();

    if (codenxt()) {
      codepsh();
      classDeclarationBody();
      codepop();
    }

    writeBlockEnd();
  }

  private void classDeclarationBody() {
    if (lnext()) {
      writeBeforeClassFirstMember();

      classDeclarationBodyItem();

      while (lnext()) {
        throw new UnsupportedOperationException("Implement me");
      }
    }
  }

  private void classDeclarationBodyItem() {
    switch (code) {
      case ByteCode.METHOD -> methodDeclaration();

      default -> throw codeuoe();
    }
  }

  private void compilationUnit() {
    writeCompilationUnitStart(autoImports.packageName, autoImports.fileName);

    var prevSection = false;

    if (codenxt()) {
      codepsh();
      codeass(ByteCode.PACKAGE);
      packageDeclaration();
      codepop();

      prevSection = true;
    }

    if (codenxt()) {
      codepsh();

      if (prevSection && code != ByteCode.EOF) {
        writeBeforeCompilationUnitBody();
      }

      importDeclarations();
      codepop();

      prevSection = true;
    }

    if (codenxt()) {
      if (prevSection) {
        writeBeforeCompilationUnitBody();
      }

      codepsh();
      compilationUnitBody();
      codepop();
    }

    writeCompilationUnitEnd(autoImports.packageName, autoImports.fileName);
  }

  private void compilationUnitBody() {
    if (lnext()) {
      compilationUnitBodyItem();

      while (lnext()) {
        throw new UnsupportedOperationException("Implement me");
      }
    }
  }

  private void compilationUnitBodyItem() {
    switch (code) {
      case ByteCode.CLASS -> classDeclaration();

      case ByteCode.ENUM_DECLARATION -> enumDeclaration();

      case ByteCode.METHOD -> methodDeclaration();

      default -> statement();
    }
  }

  private void enumDeclaration() {
    var prevSection = false;

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: modifiers");
    }

    if (codenxt()) {
      newLineOrSpace(prevSection);

      write("enum");

      writeSpace();

      write((String) codeobj());

      prevSection = true;
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: implements clause");
    }

    writeBlockStart();

    if (codenxt()) {
      codepsh();
      enumDeclarationConstants();
      codepop();
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: body");
    }

    writeBlockEnd();
  }

  private void enumDeclarationConstants() {
    if (lnext()) {
      writeBeforeClassFirstMember();

      enumDeclarationConstantsItem();

      while (lnext()) {
        writeComma();

        writeBeforeBlockNextItem();

        enumDeclarationConstantsItem();
      }
    }

    writeSemicolon();
  }

  private void enumDeclarationConstantsItem() {
    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: modifiers");
    }

    if (codenxt()) {
      write((String) codeobj());
    }

    if (codenxt()) {
      writeArgumentListStart();
      codepsh();
      methodInvocationArguments();
      codepop();
      writeArgumentListEnd();
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: body");
    }
  }

  private void expression() {
    switch (code) {
      case ByteCode.EXPRESSION_NAME -> expressionName();

      case ByteCode.METHOD_INVOCATION -> methodInvocation();

      case ByteCode.STRING_LITERAL -> stringLiteral();

      default -> throw codeuoe();
    }
  }

  private void expressionName() {
    if (codenxt()) {
      expressionNameItem();

      while (codenxt()) {
        write('.');

        expressionNameItem();
      }
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: malformed expression name?");
    }
  }

  private void expressionNameItem() {
    if (code == ByteCode.IDENTIFIER) {
      codeadv();

      var s = (String) codeobj();

      write(s);
    } else {
      write(typeName());
    }
  }

  private void importDeclarations() {
    while (true) {
      switch (code) {
        case ByteCode.IMPORT -> {
          write("import");

          writeSpace();

          codeadv();

          var o = codeobj();

          write(o.toString());

          writeSemicolon();

          codeadv();
        }

        case ByteCode.EOF -> { return; }

        default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
      }
    }
  }

  private boolean largs(boolean comma) {
    var result = false;

    var nl = 0;

    while (lnext()) {
      if (code != ByteCode.NEW_LINE) {
        result = true;

        break;
      }

      nl++;
    }

    if (result && comma) {
      writeComma();
    }

    for (int i = 0; i < nl; i++) {
      writeNewLine();
    }

    return result;
  }

  private void localVariableDeclaration() {
    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: local var modifiers");
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: local var type");
    } else {
      write("var");
    }

    if (codenxt()) {
      writeSpace();

      write((String) codeobj());
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: local var name not defined?");
    }

    if (codenxt()) {
      writeSeparator('=');

      codepsh();
      expression();
      codepop();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: local var unitialized");
    }

    writeSemicolon();
  }

  private void methodDeclaration() {
    var prevSection = false;

    modifierReset();

    if (codenxt()) {
      codepsh();
      modifierList();
      codepop();

      prevSection = true;
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method type params");
    }

    newLineOrSpace(prevSection);

    if (codenxt()) {
      codepsh();
      write(typeName());
      codepop();
    } else {
      write("void");
    }

    if (codenxt()) {
      writeSpace();

      write((String) codeobj());
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method receiver param");
    }

    write('(');

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method parameters");
    }

    write(')');

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method throws");
    }

    if (codenxt()) {
      writeBlockStart();
      codepsh();
      methodDeclarationBody();
      codepop();
      writeBlockEnd();
    } else if (abstractModifier) {
      writeSemicolon();
    } else {
      writeBlockStart();
      writeBlockEnd();
    }
  }

  private void methodDeclarationBody() {
    if (lnext()) {
      writeBeforeClassFirstMember();

      statement();

      while (lnext()) {
        statement();
      }
    }
  }

  private void methodInvocation() {
    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method invocation callee");
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method invocation type args");
    }

    if (codenxt()) {
      write((String) codeobj());
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: no method name");
    }

    writeArgumentListStart();

    if (codenxt()) {
      codepsh();
      methodInvocationArguments();
      codepop();
    }

    writeArgumentListEnd();
  }

  private void methodInvocationArguments() {
    if (largs(false)) {
      expression();

      while (largs(true)) {
        expression();
      }
    }
  }

  private void modifier() {
    codeadv();

    var modifier = (Modifier) codeobj();

    write(modifier.toString());
  }

  private void modifierList() {
    if (lnext()) {
      modifierListItem();

      while (lnext()) {
        newLineOrSpace(true);

        modifierListItem();
      }
    }
  }

  private void modifierListItem() {
    switch (code) {
      case ByteCode.ANNOTATION -> {
        annotation();

        annotationLast = true;
      }

      case ByteCode.MODIFIER -> {
        modifier();

        modifierCount++;
      }

      default -> throw codeuoe();
    }
  }

  private void modifierReset() {
    abstractModifier = false;

    annotationLast = false;

    modifierCount = 0;
  }

  private void newLineOrSpace(boolean prevSection) {
    if (annotationLast && modifierCount == 0) {
      writeNewLine();
    } else {
      writeSpaceIf(prevSection);
    }
  }

  private void packageDeclaration() {
    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: package modifiers");
    }

    if (codenxt()) {
      write("package");

      writeSpace();

      var name = (String) codeobj();

      write(name);
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: no package name?");
    }

    writeSemicolon();
  }

  private void statement() {
    switch (code) {
      case ByteCode.LOCAL_VARIABLE -> localVariableDeclaration();

      case ByteCode.METHOD_INVOCATION -> {
        methodInvocation();

        writeSemicolon();
      }

      default -> throw codeuoe();
    }
  }

  private void stringLiteral() {
    codeadv();

    var s = (String) codeobj();

    writeStringLiteral(s);
  }

  private String typeName() {
    var type = code;

    codeadv();

    return switch (type) {
      case ByteCode.NO_TYPE -> "void";

      case ByteCode.QUALIFIED_NAME -> {
        var qname = (ClassName) codeobj();

        yield qname.toString();
      }

      case ByteCode.SIMPLE_NAME -> {
        var sname = (ClassName) codeobj();

        yield sname.simpleName;
      }

      default -> throw codeuoe();
    };
  }

}