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
package objectos.code2;

abstract class InternalInterpreter extends InternalCompiler {

  protected abstract void writeComment(String value);

  protected abstract void writeCompilationUnitEnd(String packageName, String fileName);

  protected abstract void writeIdentifier(String name);

  protected abstract void writeIndentation(Indentation value);

  protected abstract void writeKeyword(Keyword value);

  protected abstract void writeRaw(String value);

  protected abstract void writeSeparator(Separator value);

  protected abstract void writeWhitespace(Whitespace value);

  final void interpret() {
    codeIndex = itemIndex = rootIndex = 0;

    objectIndex = -1;

    // writeCompilationUnitStart(autoImports.packageName, autoImports.fileName);

    var code = 0;

    do {
      code = $loop();
    } while (code != ByteCode.EOF);

    writeCompilationUnitEnd(null, null);
  }

  private int $codenxt() { return codeArray[codeIndex++]; }

  private int $loop() {
    var code = $codenxt();

    switch (code) {
      case ByteCode.AUTO_IMPORTS0 -> autoImportsRender(false);

      case ByteCode.AUTO_IMPORTS1 -> autoImportsRender(true);

      case ByteCode.COMMENT -> comment();

      case ByteCode.EOF -> {}

      case ByteCode.IDENTIFIER -> identifier();

      case ByteCode.INDENTATION -> indentation();

      case ByteCode.KEYWORD -> keyword();

      case ByteCode.RAW -> raw();

      case ByteCode.SEPARATOR -> separator();

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
    writeKeyword(Keyword.IMPORT);

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

  private void keyword() {
    var index = $codenxt();

    var value = Keyword.get(index);

    writeKeyword(value);
  }

  private void raw() {
    var index = $codenxt();

    var value = (String) objectArray[index];

    writeRaw(value);
  }

  private void separator() {
    var index = $codenxt();

    var value = Separator.get(index);

    writeSeparator(value);
  }

  private void whitespace() {
    var index = $codenxt();

    var value = Whitespace.get(index);

    writeWhitespace(value);
  }

}