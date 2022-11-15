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

public class ByteProtoFauxGenerator extends FauxGenerator {
  public static void main(String[] args) {
    var gen = new ByteProtoFauxGenerator();

    gen.execute();
  }

  @Override
  public final void execute() {
    comment("internal instructions");

    value("JMP");
    value("BREAK");
    value("NEW_LINE");

    comment("types");

    value("PACKAGE_NAME");
    value("ARRAY_TYPE");
    value("DIM");
    value("CLASS_NAME");
    value("TYPE_NAME");

    comment("declarations");

    value("COMPILATION_UNIT");
    value("PACKAGE_DECLARATION");

    comment("class");

    value("CLASS_DECLARATION");
    value("MODIFIER");
    value("IDENTIFIER");
    value("EXTENDS");
    value("IMPLEMENTS");

    comment("field");

    value("FIELD_DECLARATION");

    comment("method");

    value("METHOD_DECLARATION");
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
    value("EXPRESSION_NAME");
    value("METHOD_INVOCATION");
    value("STRING_LITERAL");
  }
}