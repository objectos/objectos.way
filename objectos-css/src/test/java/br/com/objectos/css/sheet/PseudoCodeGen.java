/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.css.sheet;

import java.util.Set;
import java.util.TreeSet;
import org.testng.annotations.Test;

public class PseudoCodeGen {

  @Test(enabled = false)
  public void byteCode() {
    generate(
      // Declaration

      "DECLARATION_START",

      // Flow

      "FLOW_JMP",

      "FLOW_RETURN",

      // Function

      "FUNCTION_END",

      "FUNCTION_START",

      // @media

      "MEDIA_END",

      "MEDIA_START",

      "MEDIA_TYPE",

      "MULTI_DECLARATION_SEPARATOR",

      // Root

      "ROOT",

      // Rule

      "RULE_END",

      "RULE_START",

      // Selector

      "SELECTOR_ATTRIBUTE",

      "SELECTOR_ATTRIBUTE_VALUE",

      "SELECTOR_CLASS",

      "SELECTOR_COMBINATOR",

      "SELECTOR_ID",

      "SELECTOR_PSEUDO_CLASS",

      "SELECTOR_PSEUDO_ELEMENT",

      "SELECTOR_TYPE",

      "SELECTOR_UNIVERSAL",

      // Value

      "VALUE_ANGLE_DOUBLE",

      "VALUE_ANGLE_INT",

      "VALUE_COLOR_HEX",

      "VALUE_COLOR_NAME",

      "VALUE_DOUBLE",

      "VALUE_INT",

      "VALUE_KEYWORD",

      "VALUE_KEYWORD_CUSTOM",

      "VALUE_LENGTH_DOUBLE",

      "VALUE_LENGTH_INT",

      "VALUE_PERCENTAGE_DOUBLE",

      "VALUE_PERCENTAGE_INT",

      "VALUE_RGB_DOUBLE",

      "VALUE_RGB_DOUBLE_ALPHA",

      "VALUE_RGB_INT",

      "VALUE_RGB_INT_ALPHA",

      "VALUE_RGBA_DOUBLE",

      "VALUE_RGBA_INT",

      "VALUE_STRING",

      "VALUE_URI"
    );
  }

  @Test(enabled = false)
  public void byteProto() {
    generate(
      "AT_MEDIA_END",

      "AT_MEDIA_MARK",

      "AT_MEDIA_START",

      // Declaration

      "DECLARATION_END",

      "DECLARATION_MARK",

      "DECLARATION_MULTI_ELEMENT_MARK",

      "DECLARATION_MULTI_END",

      "DECLARATION_MULTI_START",

      "DECLARATION_START",

      // Function

      "FUNCTION_END",

      "FUNCTION_START",

      // Root

      "MEDIA_TYPE",

      "ROOT_END",

      "ROOT_START",

      // Rule

      "RULE_END",

      "RULE_MARK",

      // Selector

      "RULE_START",

      "SELECTOR_ATTRIBUTE",

      "SELECTOR_ATTRIBUTE_MARK",

      "SELECTOR_ATTRIBUTE_VALUE",

      "SELECTOR_ATTRIBUTE_VALUE_ELEMENT",

      "SELECTOR_ATTRIBUTE_VALUE_MARK",

      "SELECTOR_CLASS",

      "SELECTOR_CLASS_MARK",

      "SELECTOR_CLASS_OBJ",

      "SELECTOR_COMBINATOR",

      "SELECTOR_ID",

      "SELECTOR_ID_MARK",

      "SELECTOR_ID_OBJ",

      "SELECTOR_PSEUDO_CLASS_OBJ",

      "SELECTOR_PSEUDO_ELEMENT_OBJ",

      "SELECTOR_TYPE_OBJ",

      // Value

      "SELECTOR_UNIVERSAL_OBJ",

      "VALUE_ANGLE_DOUBLE",

      "VALUE_ANGLE_DOUBLE_MARK",

      "VALUE_ANGLE_INT",

      "VALUE_ANGLE_INT_MARK",

      "VALUE_COLOR_HEX",

      "VALUE_COLOR_HEX_MARK",

      "VALUE_COLOR_NAME",

      "VALUE_DOUBLE",

      "VALUE_DOUBLE_DSL",

      "VALUE_DOUBLE_MARK",

      "VALUE_FUNCTION_MARK",

      "VALUE_INT",

      "VALUE_INT_DSL",

      "VALUE_INT_MARK",

      "VALUE_KEYWORD",

      "VALUE_KEYWORD_CUSTOM",

      "VALUE_KEYWORD_CUSTOM_MARK",

      "VALUE_LENGTH_DOUBLE",

      "VALUE_LENGTH_DOUBLE_MARK",

      "VALUE_LENGTH_INT",

      "VALUE_LENGTH_INT_MARK",

      "VALUE_PERCENTAGE_DOUBLE",

      "VALUE_PERCENTAGE_DOUBLE_MARK",

      "VALUE_PERCENTAGE_INT",

      "VALUE_PERCENTAGE_INT_MARK",

      "VALUE_RGB_DOUBLE",

      "VALUE_RGB_DOUBLE_ALPHA",

      "VALUE_RGB_DOUBLE_ALPHA_MARK",

      "VALUE_RGB_DOUBLE_MARK",

      "VALUE_RGB_INT",

      "VALUE_RGB_INT_ALPHA",

      "VALUE_RGB_INT_ALPHA_MARK",

      "VALUE_RGB_INT_MARK",

      "VALUE_RGBA_DOUBLE",

      "VALUE_RGBA_DOUBLE_MARK",

      "VALUE_RGBA_INT",

      "VALUE_RGBA_INT_MARK",

      "VALUE_STRING",

      "VALUE_STRING_DSL",

      "VALUE_STRING_MARK",

      "VALUE_URI",

      "VALUE_URI_MARK"
    );
  }

  private void generate(String... names) {
    Set<String> tree;
    tree = new TreeSet<>();

    for (String name : names) {
      tree.add(name);
    }

    int counter = -1;

    for (String name : tree) {
      System.out.print("static final int ");

      System.out.print(name);

      System.out.print(" = ");

      System.out.print(counter--);

      System.out.println(';');

      System.out.println();
    }
  }

}