/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

public class ByteCodeFauxGenerator extends FauxGenerator {
  public static void main(String[] args) {
    var gen = new ByteCodeFauxGenerator();

    gen.execute();
  }

  @Override
  public final void execute() {
    comment("Symbols");

    value("GT");
    value("NL");
    value("NL_OPTIONAL");
    value("SPACE");
    value("TAB");
    value("TAB_BLOCK");

    comment("Tag");

    value("DOCTYPE");
    value("ATTR_NAME");
    value("ATTR_VALUE");
    value("ATTR_VALUE_START");
    value("ATTR_VALUE_END");
    value("START_TAG");
    value("END_TAG");

    comment("Nodes");

    value("RAW");
    value("TEXT");
    value("TEXT_SCRIPT");
    value("TEXT_STYLE");
  }
}