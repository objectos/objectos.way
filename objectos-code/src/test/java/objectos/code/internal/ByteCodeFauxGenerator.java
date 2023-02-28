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

public class ByteCodeFauxGenerator extends FauxGenerator {
  public static void main(String[] args) {
    var gen = new ByteCodeFauxGenerator();

    gen.execute();
  }

  @Override
  public final void execute() {
    value("AUTO_IMPORTS0"); // no package
    value("AUTO_IMPORTS1"); // package
    value("COMMENT");
    value("EOF");
    value("IDENTIFIER");
    value("INDENTATION");
    value("KEYWORD");
    value("NOP");
    value("NOP1");
    value("PRIMITIVE_LITERAL");
    value("RAW");
    value("STRING_LITERAL");
    value("SYMBOL");
    value("WHITESPACE");
  }
}
