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

/**
 * The symbols used as separators and operators in the Java programming
 * language.
 *
 * @since 0.4
 */
public enum Symbol {

  // Separators

  /**
   * The separator {@code (}
   */
  LEFT_PARENTHESIS("("),

  /**
   * The separator {@code )}
   */
  RIGHT_PARENTHESIS(")"),

  /**
   * The separator <code>{</code>
   */
  LEFT_CURLY_BRACKET("{"),

  /**
   * The separator <code>}</code>
   */
  RIGHT_CURLY_BRACKET("}"),

  /**
   * The separator {@code [}
   */
  LEFT_SQUARE_BRACKET("["),

  /**
   * The separator {@code ]}
   */
  RIGHT_SQUARE_BRACKET("]"),

  /**
   * The separator {@code ;}
   */
  SEMICOLON(";"),

  /**
   * The separator {@code ,}
   */
  COMMA(","),

  /**
   * The separator {@code .}
   */
  DOT("."),

  /**
   * The separator {@code ...}
   */
  ELLIPSIS("..."),

  /**
   * The separator <code>@</code>
   */
  COMMERCIAL_AT("@"),

  /**
   * The separator {@code ::}
   */
  DOUBLE_COLON("::"),

  /*

   Operators
  
   =   >   <   !   ~   ?   :   ->
   ==  >=  <=  !=  &&  ||  ++  --
   +   -   *   /   &   |   ^   %   <<   >>   >>>
   +=  -=  *=  /=  &=  |=  ^=  %=  <<=  >>=  >>>=
  
   */

  /**
   * The operator {@code &}
   */
  AMPERSAND("&"),

  /**
   * The operator {@code =}
   */
  ASSIGNMENT("="),

  /**
   * The operator {@code ==}
   *
   * @since 0.4.1
   */
  EQUAL_TO("=="),

  /**
   * The operator {@code !=}
   *
   * @since 0.4.1
   */
  NOT_EQUAL_TO("!="),

  /**
   * The operator {@code <}
   */
  LEFT_ANGLE_BRACKET("<"),

  /**
   * The operator {@code >}
   */
  RIGHT_ANGLE_BRACKET(">");

  private static final Symbol[] VALUES = values();

  private final String name;

  private Symbol(String name) { this.name = name; }

  static Symbol get(int index) {
    return VALUES[index];
  }

  /**
   * Returns the string representation of the symbol so it can be used in a Java
   * program.
   *
   * @return the symbol's string representation
   */
  @Override
  public final String toString() { return name; }

}