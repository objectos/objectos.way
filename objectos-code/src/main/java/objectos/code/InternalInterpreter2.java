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

abstract class InternalInterpreter2 extends InternalCompiler2 {

  protected abstract void writeCompilationUnitEnd(String packageName, String fileName);

  protected abstract void writeCompilationUnitStart(String packageName, String fileName);

  protected abstract void writeIdentifier(String name);

  protected abstract void writeLiteral(String value);

  protected abstract void writeName(String name);

  protected abstract void writeOperator(Operator2 operator);

  protected abstract void writePseudoElement(PseudoElement value);

  protected abstract void writeReservedKeyword(ReservedKeyword value);

  protected abstract void writeSeparator(Separator value);

  protected abstract void writeStringLiteral(String value);

  final void pass2() {
    codeIndex = 0;

    markIndex = 0;

    objectIndex = 0;

    writeCompilationUnitStart(autoImports.packageName, autoImports.fileName);

    var code = 0;

    do {
      code = $loop();
    } while (code != ByteCode.EOF);

    writeCompilationUnitEnd(autoImports.packageName, autoImports.fileName);
  }

  private int $codenxt() { return codeArray[codeIndex++]; }

  private int $loop() {
    var code = $codenxt();

    switch (code) {
      case ByteCode.AUTO_IMPORTS -> autoImportsRender();

      case ByteCode.EOF -> {}

      case ByteCode.IDENTIFIER -> identifier();

      case ByteCode.NAME -> name();

      case ByteCode.NOP1 -> $codenxt();

      case ByteCode.OPERATOR -> operator();

      case ByteCode.PRIMITIVE_LITERAL -> primitiveLiteral();

      case ByteCode.PSEUDO_ELEMENT -> pseudoElement();

      case ByteCode.RESERVED_KEYWORD -> reservedKeyword();

      case ByteCode.SEPARATOR -> separator();

      case ByteCode.STRING_LITERAL -> stringLiteral();

      default -> throw $uoe_code(code);
    }

    return code;
  }

  private UnsupportedOperationException $uoe_code(int code) {
    return new UnsupportedOperationException(
      "Implement me :: code = " + code);
  }

  private void autoImportsRender() {
    var types = autoImports.types();

    if (types.isEmpty()) {
      return;
    }

    var iterator = types.iterator();

    if (iterator.hasNext()) {
      autoImportsRenderItem(iterator.next());

      while (iterator.hasNext()) {
        writePseudoElement(PseudoElement.BEFORE_NEXT_STATEMENT);

        autoImportsRenderItem(iterator.next());
      }
    }

    writePseudoElement(PseudoElement.BEFORE_NEXT_TOP_LEVEL_ITEM);
  }

  private void autoImportsRenderItem(String type) {
    writeReservedKeyword(ReservedKeyword.IMPORT);

    writePseudoElement(PseudoElement.MANDATORY_WHITESPACE);

    writeIdentifier(type);

    writeSeparator(Separator.SEMICOLON);
  }

  private void identifier() {
    var index = $codenxt();

    var name = (String) objectArray[index];

    writeIdentifier(name);
  }

  private void name() {
    var index = $codenxt();

    var name = (String) objectArray[index];

    writeName(name);
  }

  private void operator() {
    var index = $codenxt();

    var value = Operator2.get(index);

    writeOperator(value);
  }

  private void primitiveLiteral() {
    var index = $codenxt();

    var value = (String) objectArray[index];

    writeLiteral(value);
  }

  private void pseudoElement() {
    var index = $codenxt();

    var value = PseudoElement.get(index);

    writePseudoElement(value);
  }

  private void reservedKeyword() {
    var index = $codenxt();

    var value = ReservedKeyword.get(index);

    writeReservedKeyword(value);
  }

  private void separator() {
    var index = $codenxt();

    var value = Separator.get(index);

    writeSeparator(value);
  }

  private void stringLiteral() {
    var index = $codenxt();

    var value = (String) objectArray[index];

    writeStringLiteral(value);
  }

}