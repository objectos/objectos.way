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

    value("ELEMSTR8");
    value("ELEMVOID8");

    comment("attributes");

    value("ATTRIBUTE88");
    value("ATTRIBUTE816");
    value("ATTRIBUTE824");
    value("BOOLEAN8");
    value("JSACTION88");
    value("JSACTION816");
    value("JSACTION824");

    comment("fragment");

    value("FRAGSTR");
    value("FRAGEND");

    comment("testable");

    value("THEADING8");
    value("THEADING16");
    value("THEADING24");
    value("TCELL8");
    value("TCELL16");
    value("TCELL24");
    value("TPROP8");
    value("TPROP16");
    value("TPROP24");
    value("TPROPKEY8");
    value("TPROPKEY16");
    value("TPROPKEY24");
    value("TPROPVAL8");
    value("TPROPVAL16");
    value("TPROPVAL24");
    value("TNEWLINE");

    comment("text");

    value("TEXT8");
    value("TEXT16");
    value("TEXT24");

    comment("raw");

    value("RAW8");
    value("RAW16");
    value("RAW24");

    comment("contents");

    value("VALUES8");
    value("VALUES16");
    value("VALUES24");

    comment("xelement");

    value("xELEMSTR8");
    value("xELEMVOID8");

    comment("xattributes");

    value("xATTRIBUTE88");
    value("xATTRIBUTE816");
    value("xATTRIBUTE824");
    value("xBOOLEAN8");
    value("xJSACTION88");
    value("xJSACTION816");
    value("xJSACTION824");

    comment("xfragment");

    value("xFRAGSTR");

    comment("xtestable");

    value("xTHEADING8");
    value("xTHEADING16");
    value("xTHEADING24");
    value("xTCELL8");
    value("xTCELL16");
    value("xTCELL24");
    value("xTPROP8");
    value("xTPROP16");
    value("xTPROP24");
    value("xTPROPKEY8");
    value("xTPROPKEY16");
    value("xTPROPKEY24");
    value("xTPROPVAL8");
    value("xTPROPVAL16");
    value("xTPROPVAL24");
    value("xTNEWLINE");

    comment("otestable");

    value("oTHEADING8");
    value("oTHEADING16");
    value("oTHEADING24");
    value("oTCELL8");
    value("oTCELL16");
    value("oTCELL24");
    value("oTPROP8");
    value("oTPROP16");
    value("oTPROP24");
    value("oTPROPKEY8");
    value("oTPROPKEY16");
    value("oTPROPKEY24");
    value("oTPROPVAL8");
    value("oTPROPVAL16");
    value("oTPROPVAL24");

    comment("xtext");

    value("xTEXT8");
    value("xTEXT16");
    value("xTEXT24");

    comment("xraw");

    value("xRAW8");
    value("xRAW16");
    value("xRAW24");
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