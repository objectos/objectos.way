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
    value("MARKED");
    value("MARKED4");
    value("MARKED5");
    value("MARKED6");
    value("MARKED7");
    value("MARKED9");
    value("MARKED10");
    value("INTERNAL");
    value("INTERNAL4");
    value("INTERNAL7");
    value("INTERNAL9");

    comment("rules");

    value("STYLE_RULE");
    value("STYLE_RULE_END");

    comment("selectors");

    value("SELECTOR_ATTR");
    value("SELECTOR_ATTR_VALUE");
    value("SELECTOR_SEL");
    value("SELECTOR_SEL_END");
    value("SELECTOR_TYPE");

    comment("properties");

    value("DECLARATION");
    value("DECLARATION_END");

    comment("property values");

    value("JAVA_DOUBLE");
    value("JAVA_INT");
    value("JAVA_STRING");
    value("LENGTH_DOUBLE");
    value("LENGTH_INT");
    value("PERCENTAGE_DOUBLE");
    value("PERCENTAGE_INT");
    value("STANDARD_NAME"); // keyword
    value("ZERO");
  }
}