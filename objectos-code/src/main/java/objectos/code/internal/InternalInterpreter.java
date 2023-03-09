/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

public abstract class InternalInterpreter extends InternalCompiler {

  protected final void interpret() {
    codeIndex = protoIndex = stackIndex = 0;

    objectIndex = -1;

    writeCompilationUnitStart(autoImports.packageName, autoImports.fileName);

    var code = 0;

    do {
      code = loop();
    } while (code != ByteCode.EOF);

    writeCompilationUnitEnd(autoImports.packageName, autoImports.fileName);
  }

  protected final int level() { return protoIndex; }

  protected final void levelDecrease() { protoIndex--; }

  protected final void levelIncrease() { protoIndex++; }

  protected final void levelDecrease(int count) { protoIndex -= count; }

  protected final void levelIncrease(int count) { protoIndex += count; }

  protected abstract void writeComment(String value);

  protected abstract void writeCompilationUnitEnd(String packageName, String fileName);

  protected abstract void writeCompilationUnitStart(String packageName, String fileName);

  protected abstract void writeIdentifier(String name);

  protected abstract void writeIndentation(Indentation value);

  protected abstract void writeKeyword(Keyword value);

  protected abstract void writeLiteral(String value);

  protected abstract void writeName(String name);

  protected abstract void writeRaw(String value);

  protected abstract void writeStringLiteral(String value);

  protected abstract void writeSymbol(Symbol value);

  protected abstract void writeWhitespace(Whitespace value);

  private UnsupportedOperationException $uoe_code(int code) {
    return new UnsupportedOperationException(
      "Implement me :: code = " + code + System.lineSeparator() + toString());
  }

  private void autoImportsRender(boolean initialNewLine) {
    var types = autoImports.types();

    if (types.isEmpty()) {
      return;
    }

    if (initialNewLine) {
      writeWhitespace(Whitespace.BEFORE_NEXT_MEMBER);
    }

    var iterator = types.iterator();

    if (iterator.hasNext()) {
      autoImportsRenderItem(iterator.next());

      while (iterator.hasNext()) {
        writeWhitespace(Whitespace.BEFORE_NEXT_STATEMENT);

        autoImportsRenderItem(iterator.next());
      }
    }

    if (topLevel() != NULL && !initialNewLine) {
      writeWhitespace(Whitespace.BEFORE_NEXT_MEMBER);
    }
  }

  private void autoImportsRenderItem(String type) {
    writeKeyword(Keyword.IMPORT);

    writeWhitespace(Whitespace.MANDATORY);

    writeIdentifier(type);

    writeSymbol(Symbol.SEMICOLON);
  }

  private int codeNext() { return codeArray[codeIndex++]; }

  private void comment() {
    var index = codeNext();

    var value = (String) objectArray[index];

    writeComment(value);
  }

  private void identifier() {
    var index = codeNext();

    var name = (String) objectArray[index];

    writeIdentifier(name);
  }

  private void indentation() {
    var index = codeNext();

    var value = Indentation.get(index);

    writeIndentation(value);
  }

  private void keyword() {
    var index = codeNext();

    var value = Keyword.get(index);

    writeKeyword(value);
  }

  private int loop() {
    var code = codeNext();

    switch (code) {
      case ByteCode.AUTO_IMPORTS0 -> autoImportsRender(false);

      case ByteCode.AUTO_IMPORTS1 -> autoImportsRender(true);

      case ByteCode.COMMENT -> comment();

      case ByteCode.EOF -> {}

      case ByteCode.IDENTIFIER -> identifier();

      case ByteCode.INDENTATION -> indentation();

      case ByteCode.KEYWORD -> keyword();

      case ByteCode.NOP1 -> codeNext();

      case ByteCode.PRIMITIVE_LITERAL -> primitiveLiteral();

      case ByteCode.RAW -> raw();

      case ByteCode.STRING_LITERAL -> stringLiteral();

      case ByteCode.SYMBOL -> symbol();

      case ByteCode.WHITESPACE -> whitespace();

      default -> throw $uoe_code(code);
    }

    return code;
  }

  private void primitiveLiteral() {
    var index = codeNext();

    var value = (String) objectArray[index];

    writeLiteral(value);
  }

  private void raw() {
    var index = codeNext();

    var value = (String) objectArray[index];

    writeRaw(value);
  }

  private void stringLiteral() {
    var index = codeNext();

    var value = (String) objectArray[index];

    writeStringLiteral(value);
  }

  private void symbol() {
    var index = codeNext();

    var value = Symbol.get(index);

    writeSymbol(value);
  }

  private void whitespace() {
    var index = codeNext();

    var value = Whitespace.get(index);

    writeWhitespace(value);
  }

}