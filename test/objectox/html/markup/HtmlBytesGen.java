/*
 * Copyright (C) 2015-2026 Objectos Software LTDA.
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
package objectox.html.markup;

import static java.lang.System.out;

public class HtmlBytesGen {
  public static void main(String[] args) {
    var gen = new HtmlBytesGen();

    gen.execute();
  }

  public final void execute() {
    comment("document");

    value("DOCTYPE");

    comment("element");

    value("STARTTAG8");
    value("ENDTAG8");
    value("VOIDELEM");

    comment("attributes");

    value("ATTRIBUTE88");
    value("ATTRIBUTE816");
    value("ATTRIBUTE824");
    value("BOOLEAN8");
    value("BOOLEAN16");
    value("BOOLEAN24");

    comment("ambiguous");

    value("AMBIGUOUS88");
    value("AMBIGUOUS816");
    value("AMBIGUOUS824");

    comment("child");

    value("ELEM8");
    value("ELEM16");
    value("ELEM24");
    value("ELTXT88");
    value("ELTXT816");
    value("ELTXT824");

    comment("fragment");

    value("FRAG8");
    value("FRAG16");
    value("FRAG24");

    comment("text");

    value("TEXT8");
    value("TEXT16");
    value("TEXT24");

    comment("raw");

    value("RAW8");
    value("RAW16");
    value("RAW24");

    comment("xelement");

    value("xSTARTTAG8");

    comment("control flow");

    value("JMP24");
  }

  protected int value = -128;

  private void comment(String string) {
    out.println();
    out.println("// " + string);
    out.println();
  }

  private void value(String string) {
    out.println("public static final byte " + string + " = " + value++ + ";");
  }

}