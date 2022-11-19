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

abstract class InternalInterpreter2 extends InternalCompiler2 {

  private boolean abstractModifier;

  private boolean annotationLast;

  private int modifierCount;

  private String constructorName;

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

    stackIndex = 0;

    codejmp();

    codeass(ByteCode.COMPILATION_UNIT);

    compilationUnit();
  }

  private void annotation() {
    if (codenxt()) {
      codepsh();

      write('@');

      typeName();

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

  private void argumentList() {
    if (largs(false)) {
      expression();

      while (largs(true)) {
        expression();
      }
    }
  }

  private void arrayAccessExpression() {
    codeass("""
    Invalid array access expression:

    A no-operation (NOP) was found where the array reference was expected.
    """);

    codepsh();
    arrayAccessExpressionReference();
    codepop();

    codeass("""
    Invalid array access expression:

    A no-operation (NOP) was found where the array component expressions were expected.
    """);

    codepsh();

    while (lnext()) {
      write('[');

      expression();

      write(']');
    }

    codepop();
  }

  private void arrayAccessExpressionReference() {
    switch (code) {
      case ByteCode.EXPRESSION_NAME -> expressionName();

      default -> throw codeuoe();
    }
  }

  private void assignmentExpression() {
    codeass("""
    Invalid assignment expression:

    A no-operation (NOP) was found where the left hand side was expected.
    """);

    codepsh();
    expression();
    codepop();

    codeass("""
    Invalid assignment expression:

    A no-operation (NOP) was found where the operator was expected.
    """);

    writeOperator((Operator) codeobj());

    codeass("""
    Invalid assignment expression:

    A no-operation (NOP) was found where the expression was expected.
    """);

    codepsh();
    expression();
    codepop();
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

      write(simpleName());

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

      typeName();

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

    var contents = false;

    if (codenxt()) {
      codepsh();
      classDeclarationBody();
      codepop();

      contents = true;
    }

    writeBlockEnd(contents);
  }

  private void classDeclarationBody() {
    if (lnext()) {
      writeBeforeFirstMember();

      classDeclarationBodyItem();

      while (lnext()) {
        writeBeforeNextMember();

        classDeclarationBodyItem();
      }
    }
  }

  private void classDeclarationBodyItem() {
    switch (code) {
      case ByteCode.CONSTRUCTOR_DECLARATION -> constructorDeclaration();

      case ByteCode.FIELD_DECLARATION -> fieldDeclaration();

      case ByteCode.METHOD_DECLARATION -> methodDeclaration();

      default -> throw codeuoe();
    }
  }

  private void classInstanceCreationExpression() {
    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: CICE qualifier");
    }

    write("new");

    writeSpace();

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: CICE constructor type args");
    }

    codeass("""
    Invalid class instance creation expression:

    A no-operation (NOP) was found where the type to be instantiated was expected.
    """);

    codepsh();
    typeName();
    codepop();

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: CICE type args");
    }

    writeArgumentListStart();

    if (codenxt()) {
      codepsh();
      argumentList();
      codepop();
    }

    writeArgumentListEnd();

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: CICE class body");
    }
  }

  private void codeass(String message) {
    if (!codenxt()) {
      throw new AssertionError(message);
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
        writeCompilationUnitSeparator();
      }

      importDeclarationList();
      codepop();

      prevSection = true;
    }

    if (codenxt()) {
      if (prevSection) {
        writeCompilationUnitSeparator();
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
        writeCompilationUnitSeparator();

        compilationUnitBodyItem();
      }
    }
  }

  private void compilationUnitBodyItem() {
    constructorReset();

    if (ByteCode.isExpressionStatement(code)) {
      expression();

      writeSemicolon();
    } else if (ByteCode.isExpression(code)) {
      expression();
    } else {
      switch (code) {
        case ByteCode.CLASS -> classDeclaration();

        case ByteCode.CONSTRUCTOR_DECLARATION -> constructorDeclaration();

        case ByteCode.ENUM_DECLARATION -> enumDeclaration();

        case ByteCode.FIELD_DECLARATION -> fieldDeclaration();

        case ByteCode.METHOD_DECLARATION -> methodDeclaration();

        default -> statement();
      }
    }
  }

  private void constructorDeclaration() {
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
        "Implement me :: constructor type params");
    }

    newLineOrSpace(prevSection);

    write(constructorName);

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: constructor receiver param");
    }

    write('(');

    if (codenxt()) {
      codepsh();
      methodDeclarationParameterList();
      codepop();
    }

    write(')');

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: constructor throws");
    }

    if (codenxt()) {
      writeBlockStart();
      codepsh();
      methodDeclarationBody();
      codepop();
      writeBlockEnd(true);
    } else {
      writeBlockStart();
      writeBlockEnd(false);
    }
  }

  private void constructorReset() {
    constructorName = "Constructor";
  }

  private void declaratorFull() {
    declaratorSimple();

    if (codenxt()) {
      writeSeparator('=');

      codepsh();
      expression();
      codepop();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: invalid declarator?");
    }
  }

  private void declaratorList() {
    if (lnext()) {
      declaratorListItem();

      while (lnext()) {
        writeComma();

        declaratorListItem();
      }
    }

    writeSemicolon();
  }

  private void declaratorListItem() {
    switch (code) {
      case ByteCode.DECLARATOR_SIMPLE -> declaratorSimple();

      case ByteCode.DECLARATOR_FULL -> declaratorFull();

      default -> throw codeuoe();
    }
  }

  private void declaratorSimple() {
    if (codenxt()) {
      write((String) codeobj());
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: invalid declarator?");
    }
  }

  private void enumDeclaration() {
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

      write("enum");

      writeSpace();

      write(simpleName());

      prevSection = true;
    }

    if (codenxt()) {
      codepsh();
      implementsClause(prevSection);
      codepop();
    }

    writeBlockStart();

    var contents = false;

    if (codenxt()) {
      codepsh();
      enumDeclarationConstants();
      codepop();

      contents = true;
    }

    if (codenxt()) {
      codepsh();

      while (lnext()) {
        writeBeforeNextMember();

        classDeclarationBodyItem();
      }

      codepop();

      contents = true;
    }

    writeBlockEnd(contents);
  }

  private void enumDeclarationConstants() {
    if (lnext()) {
      writeBeforeFirstMember();

      enumDeclarationConstantsItem();

      while (lnext()) {
        writeComma();

        writeBeforeNextMember();

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
      argumentList();
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
      case ByteCode.ARRAY_ACCESS_EXPRESSION -> arrayAccessExpression();

      case ByteCode.ASSIGNMENT_EXPRESSION -> assignmentExpression();

      case ByteCode.CLASS_INSTANCE_CREATION -> classInstanceCreationExpression();

      case ByteCode.EXPRESSION_NAME -> expressionName();

      case ByteCode.FIELD_ACCESS_EXPRESSION0 -> fieldAccessExpression0();

      case ByteCode.METHOD_INVOCATION -> methodInvocation();

      case ByteCode.STRING_LITERAL -> stringLiteral();

      case ByteCode.THIS -> write("this");

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
      typeName();
    }
  }

  private void fieldAccessExpression0() {
    codeass("""
    Invalid field access expression:

    A no-operation (NOP) was found where a primary expression was expected.
    """);

    codepsh();
    expression();
    codepop();

    write('.');

    codeass("""
    Invalid field access expression:

    A no-operation (NOP) was found where an identifier was expected.
    """);

    write((String) codeobj());
  }

  private void fieldDeclaration() {
    var prevSection = false;

    modifierReset();

    if (codenxt()) {
      codepsh();
      modifierList();
      codepop();

      prevSection = true;
    }

    if (codenxt()) {
      codepsh();

      writeSpaceIf(prevSection);

      typeName();

      codepop();
    }

    if (codenxt()) {
      writeSpace();
      codepsh();
      declaratorList();
      codepop();
    }
  }

  private void implementsClause(boolean prevSection) {
    writeSpaceIf(prevSection);

    write("implements");

    writeSpace();

    if (lnext()) {
      typeName();

      while (lnext()) {
        writeComma();

        typeName();
      }
    }
  }

  private void importDeclarationList() {
    var eof = importDeclarationListItem(false);

    while (!eof) {
      eof = importDeclarationListItem(true);
    }
  }

  private boolean importDeclarationListItem(boolean next) {
    return switch (code) {
      case ByteCode.IMPORT -> {
        if (next) {
          writeBeforeNextStatement();
        }

        write("import");

        writeSpace();

        codeadv();

        var o = codeobj();

        write(o.toString());

        writeSemicolon();

        codeadv();

        yield false;
      }

      case ByteCode.EOF -> true;

      default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
    };
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
      typeName();
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
      codepsh();
      methodDeclarationParameterList();
      codepop();
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
      writeBlockEnd(true);
    } else if (abstractModifier) {
      writeSemicolon();
    } else {
      writeBlockStart();
      writeBlockEnd(false);
    }
  }

  private void methodDeclarationBody() {
    if (lnext()) {
      writeBeforeFirstStatement();

      statement();

      while (lnext()) {
        writeBeforeNextStatement();

        statement();
      }
    }
  }

  private void methodDeclarationParameterList() {
    if (lnext()) {
      methodDeclarationParameterListItem();

      while (lnext()) {
        writeComma();

        methodDeclarationParameterListItem();
      }
    }
  }

  private void methodDeclarationParameterListItem() {
    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: modifiers");
    }

    if (codenxt()) {
      codepsh();
      typeName();
      codepop();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: invalid parameter declaration?");
    }

    if (codenxt()) {
      writeSpace();

      write((String) codeobj());
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: invalid parameter declaration?");
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: variable arity");
    }
  }

  private void methodInvocation() {
    if (codenxt()) {
      codepsh();

      methodInvocationSubject();

      write('.');

      codepop();
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
      argumentList();
      codepop();
    }

    writeArgumentListEnd();
  }

  private void methodInvocationSubject() {
    if (ByteCode.isTypeName(code)) {
      typeName();
    } else {
      throw new UnsupportedOperationException("Implement me");
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

  private void returnStatement() {
    if (!codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: invalid return statement");
    }

    write("return");

    writeSpace();

    codepsh();
    expression();
    codepop();

    writeSemicolon();
  }

  private String simpleName() {
    return constructorName = (String) codeobj();
  }

  private void statement() {
    if (ByteCode.isExpressionStatement(code)) {
      expression();

      writeSemicolon();
    } else {
      switch (code) {
        case ByteCode.LOCAL_VARIABLE -> localVariableDeclaration();

        case ByteCode.RETURN_STATEMENT -> returnStatement();

        default -> throw codeuoe();
      }
    }
  }

  private void stringLiteral() {
    codeadv();

    var s = (String) codeobj();

    writeStringLiteral(s);
  }

  private void typeName() {
    switch (code) {
      case ByteCode.ARRAY_TYPE -> typeNameArrayType();

      case ByteCode.NO_TYPE -> typeNameNoType();

      case ByteCode.PRIMITIVE_TYPE -> typeNamePrimitiveType();

      case ByteCode.QUALIFIED_NAME -> typeNameQualifiedName();

      case ByteCode.SIMPLE_NAME -> typeNameSimpleName();

      default -> throw codeuoe();
    }
  }

  private void typeNameArrayType() {
    codeass("""
      Invalid array type:

      A no-operation (NOP) was found where the array type was expected.
      """);

    codepsh();
    typeName();
    codepop();

    codeass("""
      Invalid array type:

      A no-operation (NOP) was found where the array dimensions or annotations were expected.
      """);

    codepsh();

    while (lnext()) {
      switch (code) {
        case ByteCode.DIM -> write("[]");

        default -> throw codeuoe();
      }
    }

    codepop();
  }

  private void typeNameNoType() {
    codeadv();

    write("void");
  }

  private void typeNamePrimitiveType() {
    codeadv();

    var type = (PrimitiveType) codeobj();

    write(type.toString());
  }

  private void typeNameQualifiedName() {
    codeadv();

    var qname = (ClassName) codeobj();

    write(qname.toString());
  }

  private void typeNameSimpleName() {
    codeadv();

    var sname = (ClassName) codeobj();

    write(sname.simpleName);
  }

}