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
package objectos.html;

public class ByteProtoFauxGenerator extends FauxGenerator {
  public static void main(String[] args) {
    var gen = new ByteProtoFauxGenerator();

    gen.execute();
  }

  @Override
  public final void execute() {
    comment("internal instructions");

    value("END");
    value("INTERNAL");
    value("INTERNAL3");
    value("INTERNAL4");
    value("INTERNAL5");
    value("LENGTH2");
    value("LENGTH3");
    value("MARKED3");
    value("MARKED4");
    value("MARKED5");
    value("NULL");
    value("STANDARD_NAME");

    comment("elements");

    value("AMBIGUOUS1");
    value("DOCTYPE");
    value("ELEMENT");
    value("FLATTEN");
    value("FRAGMENT");
    value("RAW");
    value("TEXT");

    comment("attributes");

    value("ATTRIBUTE0");
    value("ATTRIBUTE1");
    value("ATTRIBUTE1_SINGLE");
    value("ATTRIBUTE_CLASS");
    value("ATTRIBUTE_ID");
  }
}