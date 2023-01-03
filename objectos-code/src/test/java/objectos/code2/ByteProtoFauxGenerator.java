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

    proto("ARRAY_TYPE");
    proto("CLASS_TYPE");
    proto("PARAMETERIZED_TYPE");
    proto("PRIMITIVE_TYPE");
    proto("VOID");

    comment("type aux");

    proto("ARRAY_DIMENSION");
    proto("ARRAY_INITIALIZER");

    comment("compilation unit");

    proto("AUTO_IMPORTS");
    proto("EOF");
    proto("PACKAGE");

    comment("declarations");

    proto("ANNOTATION");
    proto("BODY"); // remove
    proto("CLASS");
    proto("EXTENDS");
    proto("IDENTIFIER");
    proto("IMPLEMENTS");
    proto("METHOD_DECLARATION");
    proto("MODIFIER");
    proto("NEW_LINE");
    proto("PERMITS");

    comment("stmt builder");

    proto("RETURN");
    proto("STATEMENT");

    comment("statements");

    proto("BLOCK");
    proto("RETURN_STATEMENT");

    comment("expressions");

    proto("CLASS_INSTANCE_CREATION");
    proto("EXPRESSION_NAME");
    proto("METHOD_INVOCATION");
    proto("METHOD_INVOCATION_QUALIFIED");
    proto("PRIMITIVE_LITERAL");
    proto("STRING_LITERAL");
    proto("THIS");
  }
}