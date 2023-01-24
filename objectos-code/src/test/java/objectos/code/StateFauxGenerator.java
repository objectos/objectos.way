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
package objectos.code;

import static java.lang.System.out;

class StateFauxGenerator {
  private int value = 1;

  public static void main(String[] args) {
    var gen = new StateFauxGenerator();
    gen.execute();
  }

  public final void execute() {
    value = 1;

    state("CU_START");
    state("ARGS");
    state("BLOCK");
    state("BODY");
    state("CLAUSE");
    state("CLAUSE_TYPE");
    state("CONSTRUCTOR");
    state("DIMS");
    state("ENUM_CONSTANTS");
    state("IMPORTS");
    state("INIT");
    state("LHS");
    state("LOCAL_VAR");
    state("METHOD");
    state("MODIFIERS");
    state("NAME");
    state("NL");
    state("PACKAGE");
    state("PRIMITIVE");
    state("RESERVED_DOT");
    state("RESERVED_SPACE");
    state("SLOT");
    state("STATEMENT");
    state("TYPE");
    state("TYPE_DECLARATION");
    state("TYPE_PARAMETER");
    statel("VAR");
  }

  private void state(String string) {
    out.format("_%s = %d,%n", string, value++);
  }

  private void statel(String string) {
    out.format("_%s = %d;%n", string, value++);
  }
}