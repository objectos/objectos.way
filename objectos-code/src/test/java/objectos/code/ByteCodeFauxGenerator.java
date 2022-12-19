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
    comment(" TO DEPRECATE ");
    comment("internal instructions");

    value("ROOT");
    value("NOP");
    value("EOF");
    value("LHEAD");
    value("LNEXT");
    value("NEW_LINE");
    value("OBJECT_STRING");

    comment("types");

    value("ARRAY_INITIALIZER");
    value("ARRAY_TYPE");
    value("CLASS_TYPE");
    value("DIM");
    value("NO_TYPE");
    value("PACKAGE_NAME");
    value("PARAMETERIZED_TYPE");
    value("PRIMITIVE_TYPE");
    value("TYPE_VARIABLE");

    comment("declarations");

    value("COMPILATION_UNIT");
    value("IMPORT");
    value("PACKAGE");
    value("TYPE_LIST");
    value("TYPE_PARAMETER");

    comment("class");

    value("CLASS");
    value("MODIFIER");
    value("EXTENDS_SINGLE");

    comment("field");

    value("FIELD_DECLARATION");
    value("DECLARATOR_SIMPLE");
    value("DECLARATOR_FULL");

    comment("method/constructor");

    value("METHOD_DECLARATION");
    value("FORMAL_PARAMETER");
    value("CONSTRUCTOR_DECLARATION");
    value("SUPER_CONSTRUCTOR_INVOCATION");
    value("THIS_CONSTRUCTOR_INVOCATION");

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
    value("EXPRESSION_STATEMENT");
    value("LOCAL_VARIABLE");
    value("RETURN_STATEMENT");

    comment("expressions");

    value("ARRAY_ACCESS_EXPRESSION");
    value("ASSIGNMENT_EXPRESSION");
    value("CHAINED_METHOD_INVOCATION");
    value("CLASS_INSTANCE_CREATION");
    value("EXPRESSION_NAME");
    value("FIELD_ACCESS_EXPRESSION0");
    value("METHOD_INVOCATION");
    value("PRIMITIVE_LITERAL");
    value("STRING_LITERAL");
    value("THIS");

    comment("v2");

    value("AUTO_IMPORTS0"); // no package
    value("AUTO_IMPORTS1"); // package
    value("CONSTRUCTOR_NAME");
    value("CONSTRUCTOR_NAME_STORE");
    value("IDENTIFIER");
    value("INDENTATION");
    value("NAME");
    value("NOP0");
    value("NOP1");
    value("OPERATOR");
    value("PSEUDO_ELEMENT");
    value("RESERVED_KEYWORD");
    value("SEPARATOR");
    value("WHITESPACE");
  }
}
