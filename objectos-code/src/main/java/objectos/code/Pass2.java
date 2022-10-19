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

  private int[] source;

  private Object[] objects;

  private Renderer processor;

  public final void execute(int[] source, Object[] objects, JavaTemplate.Renderer processor) {
    this.source = source;
    this.objects = objects;
    this.processor = processor;

    execute0();
  }

  private void execute0() {
    executeCompilationUnit();
  }

  private void executeClass(int index) {
    processor.classStart();

    var annotations = source[index++];

    if (annotations != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var modifiers = source[index++];

    if (modifiers != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.keyword(Keyword.CLASS);

    var nameIdx = source[index++];

    var name = (String) objects[nameIdx];

    processor.identifier(name);

    var _extends = source[index++];

    if (_extends != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var _implements = source[index++];

    if (_implements != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var _permits = source[index++];

    if (_permits != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.blockStart();

    var body = source[index++];

    if (body != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.blockEnd();

    processor.classEnd();
  }

  private void executeClassIface(int index) {
    var code = source[index++];

    switch (code) {
      case Pass1.CLASS -> executeClass(index);

      default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
    }
  }

  private void executeCompilationUnit() {
    var index = 0;

    var code = source[index++];

    assert code == Pass1.COMPILATION_UNIT;

    processor.compilationUnitStart();

    var pkg = source[index++];

    if (pkg != Pass1.NOP) {
      executePackage(pkg);
    }

    var imports = source[index++];

    if (imports != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var classIface = source[index++];

    if (classIface != Pass1.NOP) {
      executeClassIface(classIface);
    }

    var module = source[index++];

    if (module != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    var eof = source[index++];

    assert eof == Pass1.EOF;

    processor.compilationUnitEnd();
  }

  private void executePackage(int index) {
    processor.packageStart();

    index++;

    var annotations = source[index++];

    if (annotations != Pass1.NOP) {
      throw new UnsupportedOperationException("Implement me");
    }

    processor.keyword(Keyword.PACKAGE);

    var nameIdx = source[index++];

    var name = (String) objects[nameIdx];

    processor.name(name);

    processor.separator(Separator.SEMICOLON);

    processor.packageEnd();
  }

}