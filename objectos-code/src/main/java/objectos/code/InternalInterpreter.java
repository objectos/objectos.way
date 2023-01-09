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

abstract class InternalInterpreter extends InternalCompiler {

  protected abstract void writeComment(String value);

  protected abstract void writeCompilationUnitEnd(String packageName, String fileName);

  protected abstract void writeCompilationUnitStart(String packageName, String fileName);

  protected abstract void writeIdentifier(String name);

  protected abstract void writeIndentation(Indentation value);

  protected abstract void writeLiteral(String value);

  protected abstract void writeName(String name);

  protected abstract void writeOperator(Operator2 operator);

  protected abstract void writeRaw(String value);

  protected abstract void writeReservedKeyword(Keyword value);

  protected abstract void writeSeparator(Separator value);

  protected abstract void writeStringLiteral(String value);

  protected abstract void writeWhitespace(Whitespace value);

  final void interpret() {
    codeIndex = itemIndex = localIndex = 0;

    objectIndex = -1;

    writeCompilationUnitStart(autoImports.packageName, autoImports.fileName);

    var code = 0;

    do {
      code = $loop();
    } while (code != ByteCode.EOF);

    writeCompilationUnitEnd(null, null);
  }

  final void pass2() {
    codeIndex = 0;

    localIndex = 0;

    objectIndex = -1;

    itemIndex = 0;

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
      case ByteCode.AUTO_IMPORTS0 -> autoImportsRender(false);

      case ByteCode.AUTO_IMPORTS1 -> autoImportsRender(true);

      case ByteCode.COMMENT -> comment();

      case ByteCode.CONSTRUCTOR_NAME -> {
        var name = "Constructor";

        if (objectIndex >= 0) {
          name = (String) objectArray[objectIndex];
        }

        writeIdentifier(name);

        objectIndex = -1;
      }

      case ByteCode.CONSTRUCTOR_NAME_STORE -> objectIndex = $codenxt();

      case ByteCode.EOF -> {}

      case ByteCode.IDENTIFIER -> identifier();

      case ByteCode.INDENTATION -> indentation();

      case ByteCode.NAME -> name();

      case ByteCode.NOP1 -> $codenxt();

      case ByteCode.OPERATOR -> operator();

      case ByteCode.PRIMITIVE_LITERAL -> primitiveLiteral();

      case ByteCode.RAW -> raw();

      case ByteCode.RESERVED_KEYWORD -> reservedKeyword();

      case ByteCode.SEPARATOR -> separator();

      case ByteCode.STRING_LITERAL -> stringLiteral();

      case ByteCode.WHITESPACE -> whitespace();

      default -> throw $uoe_code(code);
    }

    return code;
  }

  private UnsupportedOperationException $uoe_code(int code) {
    return new UnsupportedOperationException(
      "Implement me :: code = " + code);
  }

  private void autoImportsRender(boolean initialNewLine) {
    var types = autoImports.types();

    if (types.isEmpty()) {
      return;
    }

    if (initialNewLine) {
      writeWhitespace(Whitespace.BEFORE_NEXT_TOP_LEVEL_ITEM);
    }

    var iterator = types.iterator();

    if (iterator.hasNext()) {
      autoImportsRenderItem(iterator.next());

      while (iterator.hasNext()) {
        writeWhitespace(Whitespace.BEFORE_NEXT_STATEMENT);

        autoImportsRenderItem(iterator.next());
      }
    }
  }

  private void autoImportsRenderItem(String type) {
    writeReservedKeyword(Keyword.IMPORT);

    writeWhitespace(Whitespace.MANDATORY);

    writeIdentifier(type);

    writeSeparator(Separator.SEMICOLON);
  }

  private void comment() {
    var index = $codenxt();

    var value = (String) objectArray[index];

    writeComment(value);
  }

  private void identifier() {
    var index = $codenxt();

    var name = (String) objectArray[index];

    writeIdentifier(name);
  }

  private void indentation() {
    var index = $codenxt();

    var value = Indentation.get(index);

    writeIndentation(value);
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

  private void raw() {
    var index = $codenxt();

    var value = (String) objectArray[index];

    writeRaw(value);
  }

  private void reservedKeyword() {
    var index = $codenxt();

    var value = Keyword.get(index);

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

  private void whitespace() {
    var index = $codenxt();

    var value = Whitespace.get(index);

    writeWhitespace(value);
  }

}