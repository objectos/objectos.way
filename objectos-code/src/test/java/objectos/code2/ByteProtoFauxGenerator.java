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

public class ByteProtoFauxGenerator extends FauxGenerator {
  public static void main(String[] args) {
    var gen = new ByteProtoFauxGenerator();

    gen.execute();
  }

  @Override
  public final void execute() {
    comment("types");

    value("ARRAY_TYPE");
    value("CLASS_TYPE");
    value("PARAMETERIZED_TYPE");
    value("PRIMITIVE_TYPE");
    value("VOID");

    comment("type aux");

    value("ARRAY_INITIALIZER");
    value("ARRAY_DIMENSION");

    comment("compilation unit");

    value("COMPILATION_UNIT");
    value("PACKAGE");
    value("AUTO_IMPORTS");

    comment("declarations");

    value("ANNOTATION");
    value("BODY");
    value("CLASS0");
    value("CLASS_BODY");
    value("CLASS_DECLARATION");
    value("EXTENDS");
    value("IDENTIFIER");
    value("IMPLEMENTS");
    value("METHOD_DECLARATION");
    value("MODIFIER");

    comment("statements");

    value("BLOCK");
    value("RETURN_STATEMENT");

    comment("expressions");

    value("CLASS_INSTANCE_CREATION");
    value("METHOD_INVOCATION");
    value("METHOD_INVOCATION_QUALIFIED");
    value("PRIMITIVE_LITERAL");
    value("STRING_LITERAL");
    value("THIS");
  }
}