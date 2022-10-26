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

  private void declarationClass() {
    processor.classStart();

    codeadv(); // Pass1.CLASS

    codeadv();

    if (!codenop()) {
      codepsh();
      executeModifiers(cursor);
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
      codepsh();
      executeClassExtends(cursor);
      codepop();
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
      executeClassBody(cursor);
      codepop();
    }

    processor.blockEnd();

    processor.classEnd();
  }

  private void execute0() {
    cursor = 0;

    stackCursor = 0;

    executeCompilationUnit();
  }

  private void executeAnnotation(int index) {
    processor.annotationStart();

    var code = codes[index++];

    assert code == Pass1.ANNOTATION : code;

    var nameIdx = codes[index++];

    var name = (ClassName) objects[nameIdx];

    importSet.execute(processor, name);

    var pairs = codes[index++];

    if (pairs != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.annotationEnd();
  }

  private void executeClassBody(int index) {
    var code = codes[index++];

    assert code == Pass1.LIST;

    var length = codes[index++];

    if (length > 0) {
      processor.blockBeforeFirstItem();

      executeClassBody(index, 0);

      for (int offset = 1; offset < length; offset++) {
        processor.blockBeforeNextItem();

        executeClassBody(index, offset);
      }

      processor.blockAfterLastItem();
    }
  }

  private void executeClassBody(int index, int offset) {
    var itemIndex = codes[index + offset];

    var item = codes[itemIndex];

    switch (item) {
      case Pass1.METHOD -> executeMethod(itemIndex);

      default -> throw new UnsupportedOperationException("Implement me :: item=" + item);
    }
  }

  private void executeClassExtends(int index) {
    var superclass = objects[index];

    assert superclass instanceof ClassName : superclass;

    var cn = (ClassName) superclass;

    processor.keyword("extends");

    importSet.execute(processor, cn);
  }

  private void executeCompilationUnit() {
    codeadv();

    codeass(Pass1.COMPILATION_UNIT);

    processor.compilationUnitStart();

    codeadv();

    if (!codenop()) {
      codepsh();
      executePackage();
      codepop();
    }

    codeadv();

    if (!codenop()) {
      codepsh();
      executeImports();
      codepop();
    }

    codeadv();

    if (!codenop()) {
      codepsh();
      executeCompilationUnitBody();
      codepop();
    }

    processor.compilationUnitEnd();
  }

  private void executeCompilationUnitBody() {
    codeadv();

    codeass(Pass1.LIST);

    codeadv();

    var length = code;

    for (int offset = 0; offset < length; offset++) {
      codeadv();

      codepsh();

      switch (code) {
        case Pass1.CLASS -> declarationClass();

        case Pass1.LOCAL_VARIABLE -> statementLocalVariable();

        case Pass1.METHOD_INVOCATION -> statementMethodInvocation();

        default -> throw codeuoe();
      }

      codepop();
    }
  }

  private void executeImports() {
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

  private void executeMethod(int index) {
    processor.methodStart();

    index++; // Pass1.METHOD

    boolean _abstract = false;

    var mods = codes[index++];

    if (mods != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var typeParams = codes[index++];

    if (typeParams != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var returnType = codes[index++];

    if (returnType != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    } else {
      processor.keyword("void");
    }

    var name = codes[index++];

    if (name != Pass1.NOP) {
      var methodName = (String) objects[name];

      processor.identifier(methodName);
    }

    var receiver = codes[index++];

    if (receiver != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.parameterListStart();

    var params = codes[index++];

    if (params != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.parameterListEnd();

    var _throws = codes[index++];

    if (_throws != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var body = codes[index++];

    if (_abstract) {
      processor.semicolon();
    } else {
      processor.blockStart();

      if (body != Pass1.NOP) {
        throw new UnsupportedOperationException("Implement me");
      }

      processor.blockEnd();
    }

    processor.methodEnd();
  }

  private void executeModifier(int index) {
    index++;

    var objIndex = codes[index];

    var modifier = (Modifier) objects[objIndex];

    processor.modifier(modifier.toString());
  }

  private void executeModifiers(int index) {
    var code = codes[index++];

    assert code == Pass1.LIST;

    var length = codes[index++];

    for (int offset = 0; offset < length; offset++) {
      var jmp = codes[index + offset];
      var inst = codes[jmp];

      switch (inst) {
        case Pass1.ANNOTATION -> executeAnnotation(jmp);

        case Pass1.MODIFIER -> executeModifier(jmp);

        default -> throw new UnsupportedOperationException("Implement me :: inst=" + inst);
      }
    }
  }

  private void executePackage() {
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

  private void expression(int index) {
    var code = codes[index++];

    switch (code) {
      case Pass1.STRING_LITERAL -> {
        var objIndex = codes[index];

        var s = (String) objects[objIndex];

        processor.stringLiteral(s);
      }

      default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
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
    codeadv();

    codeass(Pass1.LIST);

    codeadv();

    var length = code;

    if (length > 0) {
      codeadv();

      codepsh();
      expression(cursor);
      codepop();

      for (int offset = 1; offset < length; offset++) {
        processor.comma();

        codeadv();

        codepsh();
        expression(cursor);
        codepop();
      }
    }
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

      expression(code);
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