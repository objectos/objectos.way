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
import objectos.util.IntArrays;

public final class Pass2 {

  private int[] codes;

  private int cursor;

  private Object[] objects;

  private ImportSet importSet;

  private Renderer processor;

  private int[] stack = new int[16];

  private int stackCursor;

  private int code;

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

  private void codeadv() {
    code = codes[cursor++];
  }

  private void codeass(int value) {
    assert code == value : value;
  }

  private boolean codenop() {
    return code == Pass1.NOP;
  }

  private Object codeobj() {
    return objects[code];
  }

  private int codepek() {
    return codes[code];
  }

  private void codepop() {
    cursor = stack[--stackCursor];
  }

  private void codepsh() {
    stack = IntArrays.growIfNecessary(stack, stackCursor);

    stack[stackCursor++] = cursor;

    cursor = code;

    code = codes[cursor];
  }

  private UnsupportedOperationException codeuoe() {
    return new UnsupportedOperationException("Implement me :: code=" + code);
  }

  private void compilationUnit() {
    codeadv();

    codeass(Pass1.COMPILATION_UNIT);

    processor.compilationUnitStart();

    codeadv();

    if (!codenop()) {
      codepsh();
      declarationPackage();
      codepop();
    }

    codeadv();

    if (!codenop()) {
      codepsh();
      declarationImports();
      codepop();
    }

    codeadv();

    if (!codenop()) {
      codepsh();
      compilationUnitBody();
      codepop();
    }

    processor.compilationUnitEnd();
  }

  private void compilationUnitBody() {
    while (iternxt()) {
      codepsh();

      switch (code) {
        case Pass1.CLASS -> declarationClass();

        case Pass1.METHOD -> declarationMethod();

        case Pass1.LOCAL_VARIABLE -> statementLocalVariable();

        case Pass1.METHOD_INVOCATION -> statementMethodInvocation();

        default -> throw codeuoe();
      }

      codepop();
    }
  }

  private void declarationAnnotation() {
    processor.annotationStart();

    codeadv();

    codeass(Pass1.ANNOTATION);

    codeadv();

    var name = (ClassName) codeobj();

    importSet.execute(processor, name);

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: annotation element-value pairs");
    }

    processor.annotationEnd();
  }

  private void declarationClass() {
    processor.classStart();

    codeadv(); // Pass1.CLASS

    codeadv();

    if (!codenop()) {
      codepsh();
      declarationModifierList();
      codepop();
    }

    codeadv();

    processor.keyword("class");

    processor.identifier((String) codeobj());

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: class type args");
    }

    codeadv();

    if (!codenop()) {
      var superclass = (ClassName) codeobj();

      processor.keyword("extends");

      importSet.execute(processor, superclass);
    }

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: class implements clause");
    }

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: class permits clause");
    }

    processor.blockStart();

    codeadv();

    if (!codenop()) {
      codepsh();
      declarationClassBody();
      codepop();
    }

    processor.blockEnd();

    processor.classEnd();
  }

  private void declarationClassBody() {
    if (iternxt()) {
      processor.blockBeforeFirstItem();

      declarationClassBodyItem();

      while (iternxt()) {
        processor.blockBeforeNextItem();

        declarationClassBodyItem();
      }

      processor.blockAfterLastItem();
    }
  }

  private void declarationClassBodyItem() {
    codepsh();

    switch (code) {
      case Pass1.METHOD -> declarationMethod();

      default -> throw codeuoe();
    }

    codepop();
  }

  private void declarationImports() {
    while (true) {
      codeadv();

      switch (code) {
        case Pass1.IMPORT -> {
          processor.keyword("import");

          codeadv();

          var cn = importSet.sorted(code);

          processor.name(cn.toString());

          processor.semicolon();
        }

        case Pass1.EOF -> { return; }

        default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
      }
    }
  }

  private void declarationMethod() {
    processor.methodStart();

    codeadv(); // Pass1.METHOD

    codeadv();

    boolean _abstract = false;

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: method modifiers");
    }

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: method type params");
    }

    codeadv();

    if (!codenop()) {
      var returnType = (TypeName) codeobj();

      importSet.execute(processor, returnType);
    } else {
      processor.keyword("void");
    }

    codeadv();

    if (!codenop()) {
      processor.identifier((String) codeobj());
    }

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: method receiver param");
    }

    codeadv();

    processor.parameterListStart();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: method parameters");
    }

    processor.parameterListEnd();

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: method throws");
    }

    codeadv();

    if (_abstract) {
      processor.semicolon();
    } else {
      processor.blockStart();

      if (!codenop()) {
        codepsh();
        declarationMethodBody();
        codepop();
      }

      processor.blockEnd();
    }

    processor.methodEnd();
  }

  private void declarationMethodBody() {
    if (iternxt()) {
      processor.blockBeforeFirstItem();

      declarationMethodBodyItem();

      while (iternxt()) {
        processor.blockBeforeNextItem();

        declarationMethodBodyItem();
      }

      processor.blockAfterLastItem();
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

  private void declarationModifier() {
    codeadv();

    codeadv();

    var modifier = (Modifier) codeobj();

    processor.modifier(modifier.toString());
  }

  private void declarationModifierList() {
    while (iternxt()) {
      codepsh();

      switch (code) {
        case Pass1.ANNOTATION -> declarationAnnotation();

        case Pass1.MODIFIER -> declarationModifier();

        default -> throw codeuoe();
      }

      codepop();
    }
  }

  private void declarationPackage() {
    processor.packageStart();

    codeadv(); // Pass1.PACKAGE

    codeadv();

    if (!codenop()) {
      throw new UnsupportedOperationException(
        "Implement me :: package modifiers");
    }

    processor.keyword("package");

    codeadv();

    var name = (String) codeobj();

    processor.name(name);

    processor.semicolon();

    processor.packageEnd();
  }

  private void execute0() {
    cursor = 0;

    stackCursor = 0;

    compilationUnit();
  }

  private void expression() {
    switch (code) {
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