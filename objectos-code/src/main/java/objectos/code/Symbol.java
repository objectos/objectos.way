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

public enum Symbol {

  // Separators

  LEFT_PARENTHESIS("("),

  RIGHT_PARENTHESIS(")"),

  LEFT_CURLY_BRACKET("{"),

  RIGHT_CURLY_BRACKET("}"),

  LEFT_SQUARE_BRACKET("["),

  RIGHT_SQUARE_BRACKET("]"),

  SEMICOLON(";"),

  COMMA(","),

  DOT("."),

  ELLIPSIS("..."),

  COMMERCIAL_AT("@"),

  DOUBLE_COLON("::"),

  /*
  
   Operators

   =   >   <   !   ~   ?   :   ->
   ==  >=  <=  !=  &&  ||  ++  --
   +   -   *   /   &   |   ^   %   <<   >>   >>>
   +=  -=  *=  /=  &=  |=  ^=  %=  <<=  >>=  >>>=

   */

  AMPERSAND("&"),

  ASSIGNMENT("="),

  LEFT_ANGLE_BRACKET("<"),

  RIGHT_ANGLE_BRACKET(">");

  private static final Symbol[] VALUES = values();

  private final String name;

  private Symbol(String name) { this.name = name; }

  static Symbol get(int index) {
    return VALUES[index];
  }

  @Override
  public final String toString() { return name; }

}