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
import objectos.util.IntArrays;

abstract class InternalInterpreter2 extends InternalCompiler2 {

  private static final int _STATE_ANNOTATION = 1 << 0;

  private static final int _STATE_MODIFIER = 1 << 1;

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
    code = 0;

    codeIndex = 0;

    markIndex = 0;

    objectIndex = -1;

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

  private void $constructorclr() {
    objectIndex = -1;
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

  private void $stateclr() {
    markIndex = 0;
  }

  private boolean $stateget(int value) {
    return (markIndex & value) != 0;
  }

  private void $stateoff(int value) {
    markIndex &= ~value;
  }

  private void $stateset(int value) {
    markIndex |= value;
  }

  private void $throwuoe() {
    throw new UnsupportedOperationException("Implement me :: code=" + code);
  }

  private void annotation() {
    if ($nextjmp()) {
      $codentr();

      write('@');

      typeName();

      $codexit();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: no annotation class name!");
    }

    if ($nextjmp()) {
      $codentr();

      writeArgumentListStart();

      while ($lnext()) {
        $codentr();
        annotationPairItem();
        $codexit();
      }

      writeArgumentListEnd();

      $codexit();
    }
  }

  private void annotationPairItem() {
    switch (code) {
      case ByteCode.STRING_LITERAL -> stringLiteral();

      default -> $throwuoe();
    }
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

  private void arrayAccessExpression() {
    if ($nextjmp()) {
      $codentr();
      expression();
      $codexit();
    } else {
      $malformed();
    }

    if ($nextjmp()) {
      $codentr();

      while ($lnext()) {
        write('[');

        $codentr();
        expression();
        $codexit();

        write(']');
      }

      $codexit();
    } else {
      $malformed();
    }
  }

  private void assignmentExpression() {
    if ($nextjmp()) {
      $codentr();
      expression();
      $codexit();
    } else {
      $malformed();
    }

    $codenxt();

    writeOperator((Operator) $codeobj());

    if ($nextjmp()) {
      $codentr();
      expression();
      $codexit();
    } else {
      $malformed();
    }
  }

  private void classDeclaration() {
    var prevSection = false;

    if ($nextjmp()) {
      $codentr();
      modifierList();
      $codexit();

      prevSection = true;
    }

    newLineOrSpace(prevSection);

    write("class");

    writeSpace();

    if ($nextjmp()) {
      $codentr();
      classSimpleName();
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
      $codentr();
      classDeclarationBody();
      $codexit();

      contents = true;
    }

    writeBlockEnd(contents);
  }

  private void classDeclarationBody() {
    if ($lnext()) {
      writeBeforeFirstMember();

      $codentr();
      classDeclarationBodyItem();
      $codexit();

      while ($lnext()) {
        writeBeforeNextMember();

        $codentr();
        classDeclarationBodyItem();
        $codexit();
      }
    }
  }

  private void classDeclarationBodyItem() {
    switch (code) {
      case ByteCode.CONSTRUCTOR_DECLARATION -> constructorDeclaration();

      case ByteCode.FIELD_DECLARATION -> fieldDeclaration();

      case ByteCode.METHOD_DECLARATION -> methodDeclaration();

      default -> $throwuoe();
    }
  }

  private void classInstanceCreationExpression() {
    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: CICE qualifier");
    }

    write("new");

    writeSpace();

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: CICE constructor type args");
    }

    if ($nextjmp()) {
      $codentr();
      typeName();
      $codexit();
    } else {
      $malformed();
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: CICE type args");
    }

    writeArgumentListStart();

    if ($nextjmp()) {
      $codentr();
      argumentList();
      $codexit();
    }

    writeArgumentListEnd();

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: CICE class body");
    }
  }

  private void classSimpleName() {
    switch (code) {
      case ByteCode.OBJECT_STRING -> {
        objectString();

        objectIndex = code;
      }

      default -> $throwuoe();
    }
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
    $constructorclr();

    if (ByteCode.isExpression(code)) {
      expression();

      return;
    }

    switch (code) {
      case ByteCode.CLASS -> classDeclaration();

      case ByteCode.CONSTRUCTOR_DECLARATION -> constructorDeclaration();

      case ByteCode.ENUM_DECLARATION -> enumDeclaration();

      case ByteCode.FIELD_DECLARATION -> fieldDeclaration();

      case ByteCode.METHOD_DECLARATION -> methodDeclaration();

      default -> statement();
    }
  }

  private void constructorDeclaration() {
    var prevSection = false;

    if ($nextjmp()) {
      $codentr();
      modifierList();
      $codexit();

      prevSection = true;
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: constructor type params");
    }

    newLineOrSpace(prevSection);

    if (objectIndex < 0) {
      write("Constructor");
    } else {
      var s = (String) objectArray[objectIndex];

      write(s);
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: constructor receiver param");
    }

    write('(');

    if ($nextjmp()) {
      $codentr();
      formalParameterList();
      $codexit();
    }

    write(')');

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: constructor throws");
    }

    if ($nextjmp()) {
      writeBlockStart();

      $codentr();
      methodDeclarationBody();
      $codexit();

      writeBlockEnd(true);
    } else {
      writeBlockStart();
      writeBlockEnd(false);
    }
  }

  private void declaratorFull() {
    declaratorSimple();

    if ($nextjmp()) {
      writeSeparator('=');

      $codentr();
      expression();
      $codexit();
    } else {
      $malformed();
    }
  }

  private void declaratorList() {
    if ($lnext()) {
      $codentr();
      declaratorListItem();
      $codexit();

      while ($lnext()) {
        writeComma();

        $codentr();
        declaratorListItem();
        $codexit();
      }
    }

    writeSemicolon();
  }

  private void declaratorListItem() {
    switch (code) {
      case ByteCode.DECLARATOR_SIMPLE -> declaratorSimple();

      case ByteCode.DECLARATOR_FULL -> declaratorFull();

      default -> $throwuoe();
    }
  }

  private void declaratorSimple() {
    if ($nextjmp()) {
      $codentr();
      identifier();
      $codexit();
    } else {
      $malformed();
    }
  }

  private void enumDeclaration() {
    var prevSection = false;

    if ($nextjmp()) {
      $codentr();
      modifierList();
      $codexit();

      prevSection = true;
    }

    if ($nextjmp()) {
      newLineOrSpace(prevSection);

      write("enum");

      writeSpace();

      $codentr();
      classSimpleName();
      $codexit();

      prevSection = true;
    }

    if ($nextjmp()) {
      writeSpaceIf(prevSection);

      write("implements");

      writeSpace();

      $codentr();
      typeList();
      $codexit();
    }

    writeBlockStart();

    var contents = false;

    if ($nextjmp()) {
      $codentr();
      enumDeclarationConstants();
      $codexit();

      contents = true;
    }

    if ($nextjmp()) {
      $codentr();

      while ($lnext()) {
        writeBeforeNextMember();

        $codentr();
        classDeclarationBodyItem();
        $codexit();
      }

      $codexit();

      contents = true;
    }

    writeBlockEnd(contents);
  }

  private void enumDeclarationConstants() {
    if ($lnext()) {
      writeBeforeFirstMember();

      $codentr();
      enumDeclarationConstantsItem();
      $codexit();

      while ($lnext()) {
        writeComma();

        writeBeforeNextMember();

        $codentr();
        enumDeclarationConstantsItem();
        $codexit();
      }
    }

    writeSemicolon();
  }
  private void enumDeclarationConstantsItem() {
    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: modifiers");
    }

    if ($nextjmp()) {
      $codentr();
      identifier();
      $codexit();
    }

    if ($nextjmp()) {
      writeArgumentListStart();

      $codentr();
      argumentList();
      $codexit();

      writeArgumentListEnd();
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: body");
    }
  }

  private void expression() {
    switch (code) {
      case ByteCode.ARRAY_ACCESS_EXPRESSION -> arrayAccessExpression();

      case ByteCode.ASSIGNMENT_EXPRESSION -> assignmentExpression();

      case ByteCode.CLASS_INSTANCE_CREATION -> classInstanceCreationExpression();

      case ByteCode.EXPRESSION_NAME -> expressionName();

      case ByteCode.FIELD_ACCESS_EXPRESSION0 -> fieldAccessExpression0();

      case ByteCode.METHOD_INVOCATION -> methodInvocation();

      case ByteCode.STRING_LITERAL -> stringLiteral();

      case ByteCode.THIS -> write("this");

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
      identifier();
      $codexit();

      while ($lnext()) {
        write('.');

        $codentr();
        identifier();
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

  private void fieldAccessExpression0() {
    if ($nextjmp()) {
      $codentr();
      expression();
      $codexit();
    } else {
      $malformed();
    }

    write('.');

    if ($nextjmp()) {
      $codentr();
      identifier();
      $codexit();
    } else {
      $malformed();
    }
  }

  private void fieldDeclaration() {
    var prevSection = false;

    if ($nextjmp()) {
      $codentr();
      modifierList();
      $codexit();

      prevSection = true;
    }

    if ($nextjmp()) {
      writeSpaceIf(prevSection);

      $codentr();
      typeName();
      $codexit();
    }

    if ($nextjmp()) {
      writeSpace();

      $codentr();
      declaratorList();
      $codexit();
    }
  }

  private void formalParameterList() {
    if ($lnext()) {
      $codentr();
      formalParameterListItem();
      $codexit();

      while ($lnext()) {
        writeComma();

        $codentr();
        formalParameterListItem();
        $codexit();
      }
    }
  }

  private void formalParameterListItem() {
    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: modifiers");
    }

    if ($nextjmp()) {
      $codentr();
      typeName();
      $codexit();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: invalid parameter declaration?");
    }

    if ($nextjmp()) {
      writeSpace();

      $codentr();
      identifier();
      $codexit();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: invalid parameter declaration?");
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: variable arity");
    }
  }

  private void identifier() {
    objectString();
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

  private void localVariableDeclaration() {
    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: local var modifiers");
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: local var type");
    } else {
      write("var");
    }

    if ($nextjmp()) {
      writeSpace();

      $codentr();
      identifier();
      $codexit();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: local var name not defined?");
    }

    if ($nextjmp()) {
      writeSeparator('=');

      $codentr();
      expression();
      $codexit();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: local var unitialized");
    }

    writeSemicolon();
  }

  private void methodDeclaration() {
    var prevSection = false;

    if ($nextjmp()) {
      $codentr();
      modifierList();
      $codexit();

      prevSection = true;
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: method type params");
    }

    newLineOrSpace(prevSection);

    if ($nextjmp()) {
      $codentr();
      typeName();
      $codexit();
    } else {
      write("void");
    }

    writeSpace();

    if ($nextjmp()) {
      $codentr();
      identifier();
      $codexit();
    }

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: method receiver param");
    }

    write('(');

    if ($nextjmp()) {
      $codentr();
      formalParameterList();
      $codexit();
    }

    write(')');

    if ($nextjmp()) {
      throw new UnsupportedOperationException(
        "Implement me :: method throws");
    }

    if ($nextjmp()) {
      writeBlockStart();

      $codentr();
      methodDeclarationBody();
      $codexit();

      writeBlockEnd(true);
    } else {
      writeBlockStart();
      writeBlockEnd(false);
    }
  }

  private void methodDeclarationBody() {
    if ($lnext()) {
      writeBeforeFirstStatement();

      $codentr();
      statement();
      $codexit();

      while ($lnext()) {
        writeBeforeNextStatement();

        $codentr();
        statement();
        $codexit();
      }
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
      identifier();
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

  private void modifier() {
    $codenxt();

    var modifier = (Modifier) $codeobj();

    write(modifier.toString());
  }

  private void modifierList() {
    $stateclr();

    if ($lnext()) {
      $codentr();
      modifierListItem();
      $codexit();

      while ($lnext()) {
        newLineOrSpace(true);

        $codentr();
        modifierListItem();
        $codexit();
      }
    }
  }

  private void modifierListItem() {
    switch (code) {
      case ByteCode.ANNOTATION -> {
        annotation();

        $stateset(_STATE_ANNOTATION);
      }

      case ByteCode.MODIFIER -> {
        modifier();

        $stateoff(_STATE_ANNOTATION);
        $stateset(_STATE_MODIFIER);
      }

      default -> $throwuoe();
    }
  }

  private void newLineOrSpace(boolean prevSection) {
    if ($stateget(_STATE_ANNOTATION) && !$stateget(_STATE_MODIFIER)) {
      writeNewLine();
    } else {
      writeSpaceIf(prevSection);
    }
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

  private void returnStatement() {
    if ($nextjmp()) {
      write("return");

      writeSpace();

      $codentr();
      expression();
      $codexit();

      writeSemicolon();
    } else {
      $malformed();
    }
  }

  private void statement() {
    switch (code) {
      case ByteCode.EXPRESSION_STATEMENT -> expressionStatement();

      case ByteCode.LOCAL_VARIABLE -> localVariableDeclaration();

      case ByteCode.RETURN_STATEMENT -> returnStatement();

      default -> $throwuoe();
    }
  }

  private void stringLiteral() {
    $codenxt();

    var s = (String) objectArray[code];

    writeStringLiteral(s);
  }

  private void typeList() {
    if ($lnext()) {
      $codentr();
      typeName();
      $codexit();

      while ($lnext()) {
        writeComma();

        $codentr();
        typeName();
        $codexit();
      }
    }
  }

  private void typeName() {
    switch (code) {
      case ByteCode.ARRAY_TYPE -> {
        if ($nextjmp()) {
          $codentr();
          typeName();
          $codexit();
        } else {
          $malformed();
        }

        if ($nextjmp()) {
          $codentr();

          while ($lnext()) {
            $codentr();

            switch (code) {
              case ByteCode.DIM -> write("[]");

              default -> $throwuoe();
            }

            $codexit();
          }

          $codexit();
        } else {
          $malformed();
        }
      }

      case ByteCode.NO_TYPE -> write("void");

      case ByteCode.PRIMITIVE_TYPE -> {
        $codenxt();

        var type = (PrimitiveType) $codeobj();

        write(type.toString());
      }

      case ByteCode.QUALIFIED_NAME -> {
        $codenxt();

        var qname = (ClassName) $codeobj();

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