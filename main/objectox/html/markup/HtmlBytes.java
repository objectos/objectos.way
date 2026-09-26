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

public final class HtmlBytes {

  // document

  public static final byte DOCTYPE = -128;

  // element

  public static final byte ELEMSTR8 = -127;
  public static final byte ELEMVOID8 = -126;

  // attributes

  public static final byte ATTRIBUTE88 = -125;
  public static final byte ATTRIBUTE816 = -124;
  public static final byte ATTRIBUTE824 = -123;
  public static final byte BOOLEAN8 = -122;
  public static final byte JSACTION88 = -121;
  public static final byte JSACTION816 = -120;
  public static final byte JSACTION824 = -119;

  // fragment

  public static final byte FRAGSTR = -118;
  public static final byte FRAGEND = -117;

  // testable

  public static final byte THEADING8 = -116;
  public static final byte THEADING16 = -115;
  public static final byte THEADING24 = -114;
  public static final byte TCELL8 = -113;
  public static final byte TCELL16 = -112;
  public static final byte TCELL24 = -111;
  public static final byte TPROP8 = -110;
  public static final byte TPROP16 = -109;
  public static final byte TPROP24 = -108;
  public static final byte TPROPKEY8 = -107;
  public static final byte TPROPKEY16 = -106;
  public static final byte TPROPKEY24 = -105;
  public static final byte TPROPVAL8 = -104;
  public static final byte TPROPVAL16 = -103;
  public static final byte TPROPVAL24 = -102;
  public static final byte TNEWLINE = -101;

  // text

  public static final byte TEXT8 = -100;
  public static final byte TEXT16 = -99;
  public static final byte TEXT24 = -98;

  // raw

  public static final byte RAW8 = -97;
  public static final byte RAW16 = -96;
  public static final byte RAW24 = -95;

  // contents

  public static final byte VALUES8 = -94;
  public static final byte VALUES16 = -93;
  public static final byte VALUES24 = -92;

  // xelement

  public static final byte xELEMSTR8 = -91;
  public static final byte xELEMVOID8 = -90;

  // xattributes

  public static final byte xATTRIBUTE88 = -89;
  public static final byte xATTRIBUTE816 = -88;
  public static final byte xATTRIBUTE824 = -87;
  public static final byte xBOOLEAN8 = -86;
  public static final byte xJSACTION88 = -85;
  public static final byte xJSACTION816 = -84;
  public static final byte xJSACTION824 = -83;

  // xfragment

  public static final byte xFRAGSTR = -82;

  // xtestable

  public static final byte xTHEADING8 = -81;
  public static final byte xTHEADING16 = -80;
  public static final byte xTHEADING24 = -79;
  public static final byte xTCELL8 = -78;
  public static final byte xTCELL16 = -77;
  public static final byte xTCELL24 = -76;
  public static final byte xTPROP8 = -75;
  public static final byte xTPROP16 = -74;
  public static final byte xTPROP24 = -73;
  public static final byte xTPROPKEY8 = -72;
  public static final byte xTPROPKEY16 = -71;
  public static final byte xTPROPKEY24 = -70;
  public static final byte xTPROPVAL8 = -69;
  public static final byte xTPROPVAL16 = -68;
  public static final byte xTPROPVAL24 = -67;
  public static final byte xTNEWLINE = -66;

  // otestable

  public static final byte oTHEADING8 = -65;
  public static final byte oTHEADING16 = -64;
  public static final byte oTHEADING24 = -63;
  public static final byte oTCELL8 = -62;
  public static final byte oTCELL16 = -61;
  public static final byte oTCELL24 = -60;
  public static final byte oTPROP8 = -59;
  public static final byte oTPROP16 = -58;
  public static final byte oTPROP24 = -57;
  public static final byte oTPROPKEY8 = -56;
  public static final byte oTPROPKEY16 = -55;
  public static final byte oTPROPKEY24 = -54;
  public static final byte oTPROPVAL8 = -53;
  public static final byte oTPROPVAL16 = -52;
  public static final byte oTPROPVAL24 = -51;

  // xtext

  public static final byte xTEXT8 = -50;
  public static final byte xTEXT16 = -49;
  public static final byte xTEXT24 = -48;

  // xraw

  public static final byte xRAW8 = -47;
  public static final byte xRAW16 = -46;
  public static final byte xRAW24 = -45;

  private HtmlBytes() {}

  public static byte consume(byte source) {
    return switch (source) {
      case ELEMSTR8 -> xELEMSTR8;

      case ELEMVOID8 -> xELEMVOID8;

      case ATTRIBUTE88 -> xATTRIBUTE88;

      case ATTRIBUTE816 -> xATTRIBUTE816;

      case ATTRIBUTE824 -> xATTRIBUTE824;

      case BOOLEAN8 -> xBOOLEAN8;

      case JSACTION88 -> xJSACTION88;

      case JSACTION816 -> xJSACTION816;

      case JSACTION824 -> xJSACTION824;

      case TEXT8 -> xTEXT8;

      case TEXT16 -> xTEXT16;

      case TEXT24 -> xTEXT24;

      case RAW8 -> xRAW8;

      case RAW16 -> xRAW16;

      case RAW24 -> xRAW24;

      case THEADING8 -> xTHEADING8;

      case THEADING16 -> xTHEADING16;

      case THEADING24 -> xTHEADING24;

      case TCELL8 -> xTCELL8;

      case TCELL16 -> xTCELL16;

      case TCELL24 -> xTCELL24;

      case TPROP8 -> xTPROP8;

      case TPROP16 -> xTPROP16;

      case TPROP24 -> xTPROP24;

      case TPROPKEY8 -> xTPROPKEY8;

      case TPROPKEY16 -> xTPROPKEY16;

      case TPROPKEY24 -> xTPROPKEY24;

      case TPROPVAL8 -> xTPROPVAL8;

      case TPROPVAL16 -> xTPROPVAL16;

      case TPROPVAL24 -> xTPROPVAL24;

      case TNEWLINE -> xTNEWLINE;

      case xELEMSTR8 -> xELEMSTR8;

      case xELEMVOID8 -> xELEMVOID8;

      case xATTRIBUTE88 -> xATTRIBUTE88;

      case xATTRIBUTE816 -> xATTRIBUTE816;

      case xATTRIBUTE824 -> xATTRIBUTE824;

      case xBOOLEAN8 -> xBOOLEAN8;

      case xJSACTION88 -> xJSACTION88;

      case xJSACTION816 -> xJSACTION816;

      case xJSACTION824 -> xJSACTION824;

      case xTEXT8 -> xTEXT8;

      case xTEXT16 -> xTEXT16;

      case xTEXT24 -> xTEXT24;

      case xRAW8 -> xRAW8;

      case xRAW16 -> xRAW16;

      case xRAW24 -> xRAW24;

      case xTHEADING8 -> xTHEADING8;

      case xTHEADING16 -> xTHEADING16;

      case xTHEADING24 -> xTHEADING24;

      case xTCELL8 -> xTCELL8;

      case xTCELL16 -> xTCELL16;

      case xTCELL24 -> xTCELL24;

      case xTPROP8 -> xTPROP8;

      case xTPROP16 -> xTPROP16;

      case xTPROP24 -> xTPROP24;

      case xTPROPKEY8 -> xTPROPKEY8;

      case xTPROPKEY16 -> xTPROPKEY16;

      case xTPROPKEY24 -> xTPROPKEY24;

      case xTPROPVAL8 -> xTPROPVAL8;

      case xTPROPVAL16 -> xTPROPVAL16;

      case xTPROPVAL24 -> xTPROPVAL24;

      case xTNEWLINE -> xTNEWLINE;

      case oTHEADING8 -> oTHEADING8;

      case oTHEADING16 -> oTHEADING16;

      case oTHEADING24 -> oTHEADING24;

      case oTCELL8 -> oTCELL8;

      case oTCELL16 -> oTCELL16;

      case oTCELL24 -> oTCELL24;

      case oTPROP8 -> oTPROP8;

      case oTPROP16 -> oTPROP16;

      case oTPROP24 -> oTPROP24;

      case oTPROPKEY8 -> oTPROPKEY8;

      case oTPROPKEY16 -> oTPROPKEY16;

      case oTPROPKEY24 -> oTPROPKEY24;

      case oTPROPVAL8 -> oTPROPVAL8;

      case oTPROPVAL16 -> oTPROPVAL16;

      case oTPROPVAL24 -> oTPROPVAL24;

      default -> throw new IllegalArgumentException("source=" + source);
    };
  }

  public static byte testableOnly(byte source) {
    return switch (source) {
      case THEADING8 -> oTHEADING8;

      case THEADING16 -> oTHEADING16;

      case THEADING24 -> oTHEADING24;

      case TCELL8 -> oTCELL8;

      case TCELL16 -> oTCELL16;

      case TCELL24 -> oTCELL24;

      case TPROP8 -> oTPROP8;

      case TPROP16 -> oTPROP16;

      case TPROP24 -> oTPROP24;

      case TPROPKEY8 -> oTPROPKEY8;

      case TPROPKEY16 -> oTPROPKEY16;

      case TPROPKEY24 -> oTPROPKEY24;

      case TPROPVAL8 -> oTPROPVAL8;

      case TPROPVAL16 -> oTPROPVAL16;

      case TPROPVAL24 -> oTPROPVAL24;

      case TNEWLINE -> xTNEWLINE;

      case xTNEWLINE -> xTNEWLINE;

      case oTHEADING8 -> oTHEADING8;

      case oTHEADING16 -> oTHEADING16;

      case oTHEADING24 -> oTHEADING24;

      case oTCELL8 -> oTCELL8;

      case oTCELL16 -> oTCELL16;

      case oTCELL24 -> oTCELL24;

      case oTPROP8 -> oTPROP8;

      case oTPROP16 -> oTPROP16;

      case oTPROP24 -> oTPROP24;

      case oTPROPKEY8 -> oTPROPKEY8;

      case oTPROPKEY16 -> oTPROPKEY16;

      case oTPROPKEY24 -> oTPROPKEY24;

      case oTPROPVAL8 -> oTPROPVAL8;

      case oTPROPVAL16 -> oTPROPVAL16;

      case oTPROPVAL24 -> oTPROPVAL24;

      default -> throw new IllegalArgumentException("source=" + source);
    };
  }

}
