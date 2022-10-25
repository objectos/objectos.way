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

public final class Pass2 {

  private int[] codes;

  private Object[] objects;

  private ImportSet importSet;

  private Renderer processor;

  public final void execute(
      int[] codes, Object[] objects, ImportSet importSet, Renderer processor) {

    this.codes = codes;
    this.objects = objects;
    this.importSet = importSet;
    this.processor = processor;
    execute0();
  }

  private void execute0() {
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

  private void executeClass(int index) {
    processor.classStart();

    index++; // Pass1.CLASS

    var modifiers = codes[index++];

    if (modifiers != Pass1.NOP) {
      executeModifiers(modifiers);
    }

    processor.keyword("class");

    var nameIdx = codes[index++];

    var name = (String) objects[nameIdx];

    processor.identifier(name);

    var typeArgs = codes[index++];

    if (typeArgs != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var _extends = codes[index++];

    if (_extends != Pass1.NOP) {
      executeClassExtends(_extends);
    }

    var _implements = codes[index++];

    if (_implements != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var _permits = codes[index++];

    if (_permits != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.blockStart();

    var body = codes[index++];

    if (body != Pass1.NOP) {
      executeClassBody(body);
    }

    processor.blockEnd();

    processor.classEnd();
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
    var index = 0;

    var code = codes[index++];

    assert code == Pass1.COMPILATION_UNIT;

    processor.compilationUnitStart();

    var pkg = codes[index++];

    if (pkg != Pass1.NOP) {
      executePackage(pkg);
    }

    var imports = codes[index++];

    if (imports != Pass1.NOP) {
      executeImports(imports);
    }

    var body = codes[index++];

    if (body != Pass1.NOP) {
      executeCompilationUnitBody(body);
    }

    processor.compilationUnitEnd();
  }

  private void executeCompilationUnitBody(int index) {
    var code = codes[index++];

    assert code == Pass1.LIST;

    var length = codes[index++];

    for (int offset = 0; offset < length; offset++) {
      var itemIndex = codes[index + offset];

      var item = codes[itemIndex];

      switch (item) {
        case Pass1.CLASS -> executeClass(itemIndex);

        case Pass1.LOCAL_VARIABLE -> executeLocalVariable(itemIndex);

        default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
      }
    }
  }

  private void executeExpression(int index) {
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

  private void executeImports(int index) {
    while (true) {
      var code = codes[index++];

      switch (code) {
        case Pass1.IMPORT -> {
          processor.keyword("import");

          var idx = codes[index++];

          var cn = importSet.sorted(idx);

          processor.name(cn.toString());

          processor.semicolon();
        }

        case Pass1.EOF -> { return; }

        default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
      }
    }
  }

  private void executeLocalVariable(int index) {
    processor.statementStart();

    index++; // Pass1.LOCAL_VAR

    var modifiers = codes[index++];

    if (modifiers != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var type = codes[index++];

    if (type != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    } else {
      processor.keyword("var");
    }

    var name = codes[index++];

    if (name != Pass1.NOP) {
      var variableName = (String) objects[name];

      processor.identifier(variableName);
    } else {
      throw new UnsupportedOperationException("Implement me");
    }

    var init = codes[index++];

    if (init != Pass1.NOP) {
      processor.separator('=');

      executeExpression(init);
    } else {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.statementEnd();
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

  private void executePackage(int index) {
    processor.packageStart();

    index++;

    var annotations = codes[index++];

    if (annotations != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.keyword("package");

    var nameIdx = codes[index++];

    var name = (String) objects[nameIdx];

    processor.name(name);

    processor.semicolon();

    processor.packageEnd();
  }

}