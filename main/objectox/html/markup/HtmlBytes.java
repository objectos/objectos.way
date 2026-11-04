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
  public static final byte TITEM8 = -110;
  public static final byte TITEM16 = -109;
  public static final byte TITEM24 = -108;
  public static final byte TPROP8 = -107;
  public static final byte TPROP16 = -106;
  public static final byte TPROP24 = -105;
  public static final byte TROW = -104;
  public static final byte TLIST = -103;

  // text

  public static final byte TEXT8 = -102;
  public static final byte TEXT16 = -101;
  public static final byte TEXT24 = -100;

  // raw

  public static final byte RAW8 = -99;
  public static final byte RAW16 = -98;
  public static final byte RAW24 = -97;

  // contents

  public static final byte VALUES8 = -96;
  public static final byte VALUES16 = -95;
  public static final byte VALUES24 = -94;

  // xelement

  public static final byte xELEMSTR8 = -93;
  public static final byte xELEMVOID8 = -92;

  // xattributes

  public static final byte xATTRIBUTE88 = -91;
  public static final byte xATTRIBUTE816 = -90;
  public static final byte xATTRIBUTE824 = -89;
  public static final byte xBOOLEAN8 = -88;
  public static final byte xJSACTION88 = -87;
  public static final byte xJSACTION816 = -86;
  public static final byte xJSACTION824 = -85;

  // xfragment

  public static final byte xFRAGSTR = -84;

  // xtestable

  public static final byte xTHEADING8 = -83;
  public static final byte xTHEADING16 = -82;
  public static final byte xTHEADING24 = -81;
  public static final byte xTCELL8 = -80;
  public static final byte xTCELL16 = -79;
  public static final byte xTCELL24 = -78;
  public static final byte xTITEM8 = -77;
  public static final byte xTITEM16 = -76;
  public static final byte xTITEM24 = -75;
  public static final byte xTPROP8 = -74;
  public static final byte xTPROP16 = -73;
  public static final byte xTPROP24 = -72;
  public static final byte xTROW = -71;
  public static final byte xTLIST = -70;

  // otestable

  public static final byte oTHEADING8 = -69;
  public static final byte oTHEADING16 = -68;
  public static final byte oTHEADING24 = -67;
  public static final byte oTCELL8 = -66;
  public static final byte oTCELL16 = -65;
  public static final byte oTCELL24 = -64;
  public static final byte oTITEM8 = -63;
  public static final byte oTITEM16 = -62;
  public static final byte oTITEM24 = -61;
  public static final byte oTPROP8 = -60;
  public static final byte oTPROP16 = -59;
  public static final byte oTPROP24 = -58;

  // xtext

  public static final byte xTEXT8 = -57;
  public static final byte xTEXT16 = -56;
  public static final byte xTEXT24 = -55;

  // xraw

  public static final byte xRAW8 = -54;
  public static final byte xRAW16 = -53;
  public static final byte xRAW24 = -52;

  private HtmlBytes() {}

}
