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
package objectos.css.internal;

public class ByteCodeFauxGenerator extends FauxGenerator {
  public static void main(String[] args) {
    var gen = new ByteCodeFauxGenerator();

    gen.execute();
  }

  @Override
  public final void execute() {
    comment("symbol");

    value("BLOCK_START");
    value("BLOCK_END");
    value("BLOCK_EMPTY");
    value("COLON");
    value("COMMA");
    value("NEXT_RULE");
    value("PARENS_OPEN");
    value("PARENS_CLOSE");
    value("SEMICOLON");
    value("SEMICOLON_OPTIONAL");
    value("SPACE");
    value("SPACE_OPTIONAL");
    value("TAB");

    comment("media query");

    value("AT_MEDIA");
    value("MEDIA_QUERY");

    comment("selectors");

    value("SELECTOR");
    value("SELECTOR_ATTR");
    value("SELECTOR_ATTR_VALUE");
    value("SELECTOR_CLASS");
    value("SELECTOR_COMBINATOR");
    value("SELECTOR_PSEUDO_CLASS");
    value("SELECTOR_PSEUDO_ELEMENT");
    value("SELECTOR_TYPE");

    comment("property");

    value("FUNCTION_STANDARD");
    value("PROPERTY_CUSTOM");
    value("PROPERTY_STANDARD");

    comment("property values");

    value("COLOR_HEX");
    value("FR_DOUBLE");
    value("FR_INT");
    value("KEYWORD");
    value("LENGTH_DOUBLE");
    value("LENGTH_INT");
    value("LITERAL_DOUBLE");
    value("LITERAL_INT");
    value("LITERAL_STRING");
    value("PERCENTAGE_DOUBLE");
    value("PERCENTAGE_INT");
    value("RAW");
    value("STRING_QUOTES_OPTIONAL");
    value("URL");
    value("VAR");
    value("ZERO");
  }
}