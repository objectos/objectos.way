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
  public static final byte TROW = -110;

  // text

  public static final byte TEXT8 = -109;
  public static final byte TEXT16 = -108;
  public static final byte TEXT24 = -107;

  // raw

  public static final byte RAW8 = -106;
  public static final byte RAW16 = -105;
  public static final byte RAW24 = -104;

  // contents

  public static final byte VALUES8 = -103;
  public static final byte VALUES16 = -102;
  public static final byte VALUES24 = -101;

  // xelement

  public static final byte xELEMSTR8 = -100;
  public static final byte xELEMVOID8 = -99;

  // xattributes

  public static final byte xATTRIBUTE88 = -98;
  public static final byte xATTRIBUTE816 = -97;
  public static final byte xATTRIBUTE824 = -96;
  public static final byte xBOOLEAN8 = -95;
  public static final byte xJSACTION88 = -94;
  public static final byte xJSACTION816 = -93;
  public static final byte xJSACTION824 = -92;

  // xfragment

  public static final byte xFRAGSTR = -91;

  // xtestable

  public static final byte xTHEADING8 = -90;
  public static final byte xTHEADING16 = -89;
  public static final byte xTHEADING24 = -88;
  public static final byte xTCELL8 = -87;
  public static final byte xTCELL16 = -86;
  public static final byte xTCELL24 = -85;
  public static final byte xTROW = -84;

  // otestable

  public static final byte oTHEADING8 = -83;
  public static final byte oTHEADING16 = -82;
  public static final byte oTHEADING24 = -81;
  public static final byte oTCELL8 = -80;
  public static final byte oTCELL16 = -79;
  public static final byte oTCELL24 = -78;

  // xtext

  public static final byte xTEXT8 = -77;
  public static final byte xTEXT16 = -76;
  public static final byte xTEXT24 = -75;

  // xraw

  public static final byte xRAW8 = -74;
  public static final byte xRAW16 = -73;
  public static final byte xRAW24 = -72;

  private HtmlBytes() {}

}
