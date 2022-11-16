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

public class ByteCodeFauxGenerator extends FauxGenerator {
  public static void main(String[] args) {
    var gen = new ByteCodeFauxGenerator();

    gen.execute();
  }

  @Override
  public final void execute() {
    comment("internal instructions");

    value("NOP");
    value("EOF");
    value("LHEAD");
    value("LNEXT");
    value("LNULL");
    value("JMP");
    value("NEW_LINE");

    comment("types");

    value("ARRAY_TYPE");
    value("DIM");
    value("NO_TYPE");
    value("PRIMITIVE_TYPE");
    value("QUALIFIED_NAME");
    value("SIMPLE_NAME");

    comment("declarations");

    value("COMPILATION_UNIT");
    value("IMPORT");
    value("PACKAGE");

    comment("class");

    value("CLASS");
    value("MODIFIER");
    value("IDENTIFIER");

    comment("field");

    value("FIELD_DECLARATION");
    value("DECLARATOR_SIMPLE");
    value("DECLARATOR_FULL");

    comment("method/constructor");

    value("METHOD_DECLARATION");
    value("CONSTRUCTOR_DECLARATION");
    value("FORMAL_PARAMETER");

    comment("enum");

    value("ENUM_DECLARATION");
    value("ENUM_CONSTANT");

    comment("annotation");

    value("ANNOTATION");

    comment("statements");

    value("LOCAL_VARIABLE");
    value("RETURN_STATEMENT");

    comment("expressions");

    value("ARRAY_ACCESS_EXPRESSION");
    value("ASSIGNMENT_EXPRESSION");
    value("EXPRESSION_NAME");
    value("FIELD_ACCESS_EXPRESSION0");
    value("METHOD_INVOCATION");
    value("STRING_LITERAL");
    value("THIS");
  }
}
