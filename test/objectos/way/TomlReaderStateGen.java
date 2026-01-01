/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless httpuired by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

final class TomlReaderStateGen {

  private int state = 0;

  public static void main(String[] args) {
    final TomlReaderStateGen gen;
    gen = new TomlReaderStateGen();

    gen.value("$START");

    gen.line();

    gen.value("$EXP");
    gen.value("$EXP_OPEN_BRACKET");

    gen.line();

    gen.value("$EOL");
    gen.value("$EOL_CR");

    gen.line();

    gen.value("$NAME_BARE");
    gen.value("$NAME_ARRAY_CLOSE");
    gen.value("$NAME_WS");

    gen.line();

    gen.value("$VALUE");
    gen.value("$VALUE_STRING");
    gen.value("$VALUE_STRING_DQUOTE2");
    gen.value("$VALUE_STRING_MULTI_LINE");
    gen.value("$VALUE_STRING_UNESCAPE");
    gen.value("$VALUE_STRING_LITERAL");

    gen.line();

    gen.value("$READ");
    gen.value("$READ_EOF");

    gen.line();

    gen.value("$PROCESS");
    gen.value("$RESULT");
  }

  private void line() {
    System.out.println();
  }

  private void value(String name) {
    System.out.printf("static final byte %s = %d;%n", name, state++);
  }

}