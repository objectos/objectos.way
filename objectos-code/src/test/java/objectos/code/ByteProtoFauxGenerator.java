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

public class ByteProtoFauxGenerator extends FauxGenerator {
  public static void main(String[] args) {
    var gen = new ByteProtoFauxGenerator();

    gen.execute();
  }

  @Override
  public final void execute() {
    comment("internal instructions");

    value("AUTO_IMPORTS");
    value("END");
    value("END_ELEMENT");
    value("NOOP");

    comment("types");

    value("ARRAY_TYPE");
    value("CLASS_TYPE");
    value("NO_TYPE");
    value("PARAMETERIZED_TYPE");
    value("PRIMITIVE_TYPE");
    value("TYPE_VARIABLE");
    value("VOID");

    comment("type aux");

    value("ARRAY_DIMENSION");
    value("ARRAY_INITIALIZER");
    value("ELLIPSIS");

    comment("declarations");

    value("ANNOTATION");
    value("BODY");
    value("CLASS");
    value("COMPILATION_UNIT");
    value("CONSTRUCTOR");
    value("ENUM");
    value("ENUM_CONSTANT");
    value("EXTENDS");
    value("IDENTIFIER");
    value("IMPLEMENTS");
    value("INTERFACE");
    value("METHOD");
    value("MODIFIER");
    value("PACKAGE");
    value("TYPE_PARAMETER");

    comment("statements");

    value("BLOCK");
    value("LOCAL_VARIABLE");
    value("SUPER_INVOCATION");

    comment("expression start");

    value("EXPRESSION_NAME");
    value("INVOKE");
    value("PRIMITIVE_LITERAL");
    value("STRING_LITERAL");
    value("THIS");

    comment("expressions");

    value("ASSIGNMENT");
    value("CLASS_INSTANCE_CREATION");
    value("FIELD_ACCESS");

    comment("stmt/exp");

    value("ARRAY_ACCESS");
    value("NEW");
    value("NEW_LINE");
    value("OPERATOR");
    value("RETURN");
    value("SUPER");
    value("VAR");
  }
}