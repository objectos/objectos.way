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

import javax.lang.model.element.Modifier;
import objectos.code.ClassName;
import objectos.code.JavaTemplate.Renderer;

public final class Pass2 extends Pass1 {

  private boolean abstractModifier;

  private boolean annotationLast;

  private int modifierCount;

  public final void executePass2(Renderer renderer) {
    this.processor = renderer;

    execute0();
  }

  private void annotation() {
    if (codenxt()) {
      codepsh();
      processor.write('@');

      processor.write(typeName());
      codepop();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: no annotation class name!");
    }

    if (codenxt()) {
      codepsh();

      processor.argumentListStart();

      while (lnext()) {
        annotationPairItem();
      }

      processor.argumentListEnd();

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

      processor.write("class");

      processor.space();

      processor.write((String) codeobj());

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
      processor.spaceIf(prevSection);

      processor.write("extends");

      processor.space();

      processor.write(typeName());
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

    processor.blockStart();

    if (codenxt()) {
      codepsh();
      classDeclarationBody();
      codepop();
    }

    processor.blockEnd();
  }

  private void classDeclarationBody() {
    if (lnext()) {
      processor.beforeClassFirstMember();

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
    processor.compilationUnitStart();

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
        processor.beforeCompilationUnitBody();
      }

      importDeclarations();
      codepop();

      prevSection = true;
    }

    if (codenxt()) {
      if (prevSection) {
        processor.beforeCompilationUnitBody();
      }

      codepsh();
      compilationUnitBody();
      codepop();
    }

    processor.compilationUnitEnd();
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

      case ByteCode.METHOD -> methodDeclaration();

      default -> statement();
    }
  }

  private void execute0() {
    codeIndex = 0;

    stackIndex = 0;

    codejmp();

    codeass(ByteCode.COMPILATION_UNIT);

    compilationUnit();
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
        processor.write('.');

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

      processor.write(s);
    } else {
      processor.write(typeName());
    }
  }

  private void importDeclarations() {
    while (true) {
      switch (code) {
        case ByteCode.IMPORT -> {
          processor.write("import");

          processor.space();

          codeadv();

          var o = codeobj();

          processor.write(o.toString());

          processor.semicolon();

          codeadv();
        }

        case ByteCode.EOF -> { return; }

        default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
      }
    }
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
      processor.write("var");
    }

    if (codenxt()) {
      processor.space();

      processor.write((String) codeobj());
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: local var name not defined?");
    }

    if (codenxt()) {
      processor.separator('=');

      codepsh();
      expression();
      codepop();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: local var unitialized");
    }

    processor.semicolon();
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
      processor.write(typeName());
      codepop();
    } else {
      processor.write("void");
    }

    if (codenxt()) {
      processor.space();

      processor.write((String) codeobj());
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method receiver param");
    }

    processor.write('(');

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method parameters");
    }

    processor.write(')');

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method throws");
    }

    if (codenxt()) {
      processor.blockStart();
      codepsh();
      methodDeclarationBody();
      codepop();
      processor.blockEnd();
    } else if (abstractModifier) {
      processor.semicolon();
    } else {
      processor.blockStart();
      processor.blockEnd();
    }
  }

  private void methodDeclarationBody() {
    if (lnext()) {
      processor.beforeClassFirstMember();

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
      processor.write((String) codeobj());
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: no method name");
    }

    processor.argumentListStart();

    if (codenxt()) {
      codepsh();
      methodInvocationArguments();
      codepop();
    }

    processor.argumentListEnd();
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

    processor.write(modifier.toString());
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
      processor.newLine();
    } else {
      processor.spaceIf(prevSection);
    }
  }

  private void packageDeclaration() {
    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: package modifiers");
    }

    if (codenxt()) {
      processor.write("package");

      processor.space();

      var name = (String) codeobj();

      processor.write(name);
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: no package name?");
    }

    processor.semicolon();
  }

  private void statement() {
    switch (code) {
      case ByteCode.LOCAL_VARIABLE -> localVariableDeclaration();

      case ByteCode.METHOD_INVOCATION -> {
        methodInvocation();

        processor.semicolon();
      }

      default -> throw codeuoe();
    }
  }

  private void stringLiteral() {
    codeadv();

    var s = (String) codeobj();

    processor.stringLiteral(s);
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