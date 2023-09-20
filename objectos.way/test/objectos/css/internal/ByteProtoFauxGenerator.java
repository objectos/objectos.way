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

public class ByteProtoFauxGenerator extends FauxGenerator {
  public static void main(String[] args) {
    var gen = new ByteProtoFauxGenerator();

    gen.execute();
  }

  @Override
  public final void execute() {
    comment("internal instructions");

    value("NULL");
    value("END");
    value("MARKED");
    value("MARKED3");
    value("MARKED4");
    value("MARKED5");
    value("MARKED6");
    value("MARKED7");
    value("MARKED9");
    value("MARKED10");
    value("MARKED11");
    value("INTERNAL");
    value("INTERNAL4");
    value("INTERNAL6");
    value("INTERNAL7");
    value("INTERNAL9");
    value("INTERNAL10");
    value("INTERNAL11");

    comment("rules");

    value("MEDIA_RULE");
    value("STYLE_RULE");

    comment("media query");

    value("MEDIA_TYPE");

    comment("selectors");

    value("SELECTOR_ATTR");
    value("SELECTOR_ATTR_VALUE");
    value("SELECTOR_CLASS");
    value("SELECTOR_COMBINATOR");
    value("SELECTOR_PSEUDO_CLASS");
    value("SELECTOR_PSEUDO_ELEMENT");
    value("SELECTOR_TYPE");

    comment("properties");

    value("DECLARATION");
    value("FUNCTION");
    value("FUNCTION_STANDARD");
    value("PROPERTY_CUSTOM");
    value("PROPERTY_STANDARD");
    value("VAR_FUNCTION");

    comment("property values");

    value("COLOR_HEX");
    value("COMMA");
    value("FR_DOUBLE");
    value("FR_INT");
    value("JAVA_DOUBLE");
    value("JAVA_INT");
    value("JAVA_STRING");
    value("LENGTH_DOUBLE");
    value("LENGTH_INT");
    value("LITERAL_DOUBLE");
    value("LITERAL_INT");
    value("LITERAL_STRING");
    value("PERCENTAGE_DOUBLE");
    value("PERCENTAGE_INT");
    value("RAW");
    value("STANDARD_NAME"); // keyword
    value("URL");
    value("ZERO");
  }
}