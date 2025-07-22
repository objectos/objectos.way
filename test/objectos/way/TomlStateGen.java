/*
 * Copyright (C) 2025 Objectos Software LTDA.
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

final class TomlStateGen {

  private int state = 0;

  public static void main(String[] args) {
    final TomlStateGen gen;
    gen = new TomlStateGen();

    gen.value("$START");

    gen.line();

    gen.value("$READ");
    gen.value("$READ_LINE");
    gen.value("$READ_LINE_CR");

    gen.line();

    gen.value("$NAME");
    gen.value("$NAME_BARE");
    gen.value("$NAME_DOUBLE");
    gen.value("$NAME_SINGLE");
    gen.value("$NAME_HEADER");
    gen.value("$NAME_ARRAY");
    gen.value("$NAME_ARRAY_CLOSE");
    gen.value("$NAME_RESULT");

    gen.line();

    gen.value("$ERROR");
  }

  private void line() {
    System.out.println();
  }

  private void value(String name) {
    System.out.printf("static final byte %s = %d;%n", name, state++);
  }

}