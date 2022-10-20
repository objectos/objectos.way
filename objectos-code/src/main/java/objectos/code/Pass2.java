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

import objectos.code.JavaTemplate.Renderer;

final class Pass2 {

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

  private void executeClass(int index) {
    processor.classStart();

    var annotations = codes[index++];

    if (annotations != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var modifiers = codes[index++];

    if (modifiers != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.keyword(Keyword.CLASS);

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
      throw new UnsupportedOperationException("Implement me");
    }

    processor.blockEnd();

    processor.classEnd();
  }

  private void executeClassExtends(int index) {
    var superclass = objects[index];

    assert superclass instanceof ClassName : superclass;

    var cn = (ClassName) superclass;

    processor.keyword(Keyword.EXTENDS);

    cn.execute(processor, importSet);
  }

  private void executeClassIface(int index) {
    var code = codes[index++];

    switch (code) {
      case Pass1.CLASS -> executeClass(index);

      default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
    }
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

    var classIface = codes[index++];

    if (classIface != Pass1.NOP) {
      executeClassIface(classIface);
    }

    var module = codes[index++];

    if (module != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var eof = codes[index++];

    assert eof == Pass1.EOF;

    processor.compilationUnitEnd();
  }

  private void executeImports(int index) {
    while (true) {
      var code = codes[index++];

      switch (code) {
        case Pass1.IMPORT -> {
          processor.keyword(Keyword.IMPORT);

          var idx = codes[index++];

          var cn = importSet.sorted(idx);

          processor.name(cn.toString());

          processor.separator(Separator.SEMICOLON);
        }

        case Pass1.EOF -> { return; }

        default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
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

    processor.keyword(Keyword.PACKAGE);

    var nameIdx = codes[index++];

    var name = (String) objects[nameIdx];

    processor.name(name);

    processor.separator(Separator.SEMICOLON);

    processor.packageEnd();
  }

}