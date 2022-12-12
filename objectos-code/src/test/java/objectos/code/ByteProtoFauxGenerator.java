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
    value("BREAK"); // rename 2 ELEMENT_END
    value("OBJECT_END");
    value("NEW_LINE");
    value("EOF");
    value("INVOKE_METHOD_NAME");
    value("VAR_NAME");

    comment("types");

    value("ARRAY_TYPE");
    value("CLASS_TYPE");
    value("NO_TYPE");
    value("PARAMETERIZED_TYPE");
    value("PRIMITIVE_TYPE");
    value("TYPE_VARIABLE");

    comment("type aux");

    value("ARRAY_INITIALIZER");
    value("DIM");
    value("PACKAGE_NAME");
    value("SIMPLE_NAME");

    comment("declarations");

    value("COMPILATION_UNIT");
    value("PACKAGE_DECLARATION");
    value("TYPE_PARAMETER");

    comment("class");

    value("CLASS_DECLARATION");
    value("MODIFIER");
    value("IDENTIFIER");
    value("EXTENDS_SINGLE");
    value("IMPLEMENTS");

    comment("field");

    value("FIELD_DECLARATION");

    comment("method/constructor");

    value("METHOD_DECLARATION");
    value("FORMAL_PARAMETER");
    value("ELLIPSIS");
    value("CONSTRUCTOR_DECLARATION");
    value("THIS_INVOCATION");
    value("SUPER_INVOCATION");
    value("QUALIFIED_SUPER_INVOCATION");

    comment("enum");

    value("ENUM_DECLARATION");
    value("ENUM_CONSTANT");

    comment("interface");

    value("INTERFACE_DECLARATION");
    value("EXTENDS_MANY");

    comment("annotation");

    value("ANNOTATION");

    comment("statements");

    value("BLOCK");
    value("LOCAL_VARIABLE");
    value("RETURN_STATEMENT");

    comment("expressions");

    value("ARRAY_ACCESS_EXPRESSION");
    value("ASSIGNMENT_EXPRESSION");
    value("ASSIGNMENT_OPERATOR");
    value("CHAINED_METHOD_INVOCATION");
    value("CLASS_INSTANCE_CREATION0");
    value("EXPRESSION_NAME");
    value("FIELD_ACCESS_EXPRESSION0");
    value("METHOD_INVOCATION");
    value("METHOD_INVOCATION_QUALIFIED");
    value("PRIMITIVE_LITERAL");
    value("STRING_LITERAL");
    value("THIS");
  }
}