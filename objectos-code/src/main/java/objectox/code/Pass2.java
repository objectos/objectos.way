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
import objectos.code.TypeName;

public final class Pass2 extends Pass2Super {

  private boolean abstractModifier;

  private boolean annotationLast;

  private int modifierCount;

  public final void execute(
      int[] codes, Object[] objects, ImportSet importSet, Renderer processor) {
    this.codes = codes;
    this.objects = objects;
    this.importSet = importSet;
    this.processor = processor;

    execute0();
  }

  public final void execute(Pass1 pass1, Renderer renderer) {
    execute(
      pass1.code,
      pass1.object,
      pass1.importSet,
      renderer
    );
  }

  private void annotation() {
    if (codenxt()) {
      processor.write('@');

      var name = (ClassName) codeobj();

      importSet.execute(processor, name);
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: no annotation class name!");
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: annotation element-value pairs");
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
      if (annotationLast && modifierCount == 0) {
        processor.newLine();
      } else {
        processor.spaceIf(prevSection);
      }

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
      processor.spaceIf(prevSection);

      processor.write("extends");

      processor.space();

      var superclass = (ClassName) codeobj();

      importSet.execute(processor, superclass);
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
    if (lhead()) {
      processor.beforeClassFirstMember();

      classDeclarationBodyItem();

      while (lnext()) {
        throw new UnsupportedOperationException("Implement me");
      }
    }
  }

  private void classDeclarationBodyItem() {
    switch (code) {
      case Pass1.METHOD -> methodDeclaration();

      default -> throw codeuoe();
    }
  }

  private void compilationUnit() {
    processor.compilationUnitStart();

    var prevSection = false;

    if (codenxt()) {
      codepsh();
      codeass(Pass1.PACKAGE);
      packageDeclaration();
      codepop();

      prevSection = true;
    }

    if (codenxt()) {
      if (prevSection) {
        processor.beforeCompilationUnitBody();
      }

      codepsh();
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
    if (lhead()) {
      compilationUnitBodyItem();

      while (lnext()) {
        throw new UnsupportedOperationException("Implement me");
      }
    }
  }

  private void compilationUnitBodyItem() {
    switch (code) {
      case Pass1.CLASS -> classDeclaration();

      //      case Pass1.METHOD -> declarationMethod();
      //
      //      case Pass1.LOCAL_VARIABLE -> statementLocalVariable();
      //
      //      case Pass1.METHOD_INVOCATION -> statementMethodInvocation();

      default -> throw codeuoe();
    }
  }

  private void declarationMethodBody() {
    if (iternxt()) {
      processor.beforeClassFirstMember();

      declarationMethodBodyItem();

      while (iternxt()) {
        processor.beforeBlockNextItem();

        declarationMethodBodyItem();
      }
    }
  }

  private void declarationMethodBodyItem() {
    codepsh();

    switch (code) {
      case Pass1.METHOD_INVOCATION -> statementMethodInvocation();

      default -> throw codeuoe();
    }

    codepop();
  }

  private void execute0() {
    cursor = 0;

    stackCursor = 0;

    codejmp();

    codeass(Pass1.COMPILATION_UNIT);

    compilationUnit();
  }

  private void expression() {
    switch (code) {
      case Pass1.EXPRESSION_NAME -> expressionName();

      case Pass1.METHOD_INVOCATION -> expressionMethodInvocation();

      case Pass1.STRING_LITERAL -> {
        codeadv();

        codeadv();

        var s = (String) codeobj();

        processor.stringLiteral(s);
      }

      default -> throw codeuoe();
    }
  }

  private void expressionMethodInvocation() {
    codeadv();

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: method invocation callee");
    }

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: method invocation type args");
    }

    codeadv();

    if (!codenop()) {
      processor.identifier((String) codeobj());
    }

    processor.parameterListStart();

    codeadv();

    if (!codenop()) {
      codepsh();
      expressionMethodInvocationArguments();
      codepop();
    }

    processor.parameterListEnd();
  }

  private void expressionMethodInvocationArguments() {
    if (iterarg(false)) {
      expressionMethodInvocationArgumentsItem();

      while (iterarg(true)) {
        expressionMethodInvocationArgumentsItem();
      }
    }
  }

  private void expressionMethodInvocationArgumentsItem() {
    codepsh();

    expression();

    codepop();
  }

  private void expressionName() {
    codeadv();

    codeadv();

    var length = code;

    assert length > 0;

    codeadv();

    var first = codeobj();

    if (first instanceof ClassName cn) {
      importSet.execute(processor, cn);
    } else if (first instanceof String s) {
      processor.identifier(s);
    } else {
      throw new UnsupportedOperationException("Implement me :: first=" + first);
    }

    if (length == 1) {
      return;
    }

    for (int i = 1; i < length; i++) {
      processor.dot();

      codeadv();

      var next = codeobj();

      if (next instanceof ClassName cn) {
        importSet.execute(processor, cn);
      } else if (next instanceof String s2) {
        processor.identifier(s2);
      } else {
        throw new UnsupportedOperationException("Implement me :: next=" + next);
      }
    }
  }

  private void importDeclarations() {
    while (true) {
      switch (code) {
        case Pass1.IMPORT -> {
          processor.keyword("import");

          codeadv();

          var cn = importSet.sorted(code);

          processor.name(cn.toString());

          processor.semicolon();

          codeadv();
        }

        case Pass1.EOF -> { return; }

        default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
      }
    }
  }

  private boolean iterarg(boolean comma) {
    var result = false;

    var nl = 0;

    while (iternxt()) {
      var peek = codepek();

      if (peek != Pass1.NEW_LINE) {
        result = true;

        break;
      }

      nl++;
    }

    if (result && comma) {
      processor.comma();
    }

    for (int i = 0; i < nl; i++) {
      processor.newLine();
    }

    return result;
  }

  private boolean iternxt() {
    codeadv();

    if (code == Pass1.JMP) {
      codeadv();

      cursor = code;

      code = codes[cursor++];
    }

    return switch (code) {
      case Pass1.EOF -> false;

      case Pass1.LIST -> {
        codeadv(); // last cell

        codeadv(); // value

        yield true;
      }

      case Pass1.LIST_CELL -> {
        codeadv(); // value

        yield true;
      }

      default -> throw codeuoe();
    };
  }

  private boolean lhead() {
    codeass(Pass1.LHEAD);

    codeadv();

    codepsh();

    return true;
  }

  private boolean lnext() {
    codepop();

    codeadv();

    if (code == Pass1.NOP) {
      return false;
    }

    throw new UnsupportedOperationException("Implement me");
  }

  private void methodDeclaration() {
    if (codenxt()) {
      codepsh();
      modifierList();
      codepop();
    }

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method type params");
    }

    if (codenxt()) {
      var returnType = (TypeName) codeobj();

      importSet.execute(processor, returnType);
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

    processor.parameterListStart();

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method parameters");
    }

    processor.parameterListEnd();

    if (codenxt()) {
      throw new UnsupportedOperationException(
        "Implement me :: method throws");
    }

    if (codenxt()) {
      throw new UnsupportedOperationException("Implement me");
    } else if (abstractModifier) {
      processor.semicolon();
    } else {
      processor.blockStart();
      processor.blockEnd();
    }
  }

  private void modifier() {
    codeadv();

    var modifier = (Modifier) codeobj();

    processor.modifier(modifier.toString());
  }

  private void modifierList() {
    if (lhead()) {
      modifierListItem();

      while (lnext()) {
        throw new UnsupportedOperationException("Implement me");
      }
    }
  }

  private void modifierListItem() {
    switch (code) {
      case Pass1.ANNOTATION -> {
        annotation();

        annotationLast = true;
      }

      case Pass1.MODIFIER -> {
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

  private void statementLocalVariable() {
    processor.statementStart();

    codeadv(); // Pass1.LOCAL_VAR

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: local var modifiers");
    }

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: local var type");
    } else {
      processor.keyword("var");
    }

    codeadv();

    if (!codenop()) {
      processor.identifier((String) codeobj());
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: local var name not defined?");
    }

    codeadv();

    if (!codenop()) {
      processor.separator('=');

      codepsh();
      expression();
      codepop();
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: local var unitialized");
    }

    processor.statementEnd();
  }

  private void statementMethodInvocation() {
    processor.statementStart();

    expressionMethodInvocation();

    processor.statementEnd();
  }

}