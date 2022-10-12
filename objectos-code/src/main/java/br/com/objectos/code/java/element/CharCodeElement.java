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
package br.com.objectos.code.java.element;

import br.com.objectos.code.java.io.CodeWriter;

final class CharCodeElement implements CodeElement {

  static final CodeElement BITWISE_AND = new CharCodeElement('&');

  static final CodeElement BITWISE_XOR = new CharCodeElement('^');

  static final CodeElement BITWISE_OR = new CharCodeElement('|');

  static final CodeElement CLOSE_ANGLE = new CharCodeElement('>');

  static final CodeElement CLOSE_BRACKET = new CharCodeElement(']');

  static final CodeElement CLOSE_PARENS = new CharCodeElement(')');

  static final CodeElement COLON = new CharCodeElement(':');

  static final CodeElement COMMA = new CharCodeElement(',');

  static final CodeElement DOT = new CharCodeElement('.');

  static final CodeElement OPEN_ANGLE = new CharCodeElement('<');

  static final CodeElement OPEN_BRACE = new CharCodeElement('{');

  static final CodeElement OPEN_BRACKET = new CharCodeElement('[');

  static final CodeElement OPEN_PARENS = new CharCodeElement('(');

  static final CodeElement QUESTION_MARK = new CharCodeElement('?');

  static final CodeElement QUOTE = new CharCodeElement('"');

  static final CodeElement SEMICOLON = new CharCodeElement(';');

  private final char value;

  CharCodeElement(char value) {
    this.value = value;
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    return w.write(value);
  }

}
