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

  // text

  public static final byte TEXT8 = -110;
  public static final byte TEXT16 = -109;
  public static final byte TEXT24 = -108;

  // raw

  public static final byte RAW8 = -107;
  public static final byte RAW16 = -106;
  public static final byte RAW24 = -105;

  // contents

  public static final byte VALUES8 = -104;
  public static final byte VALUES16 = -103;
  public static final byte VALUES24 = -102;

  // xelement

  public static final byte xELEMSTR8 = -101;
  public static final byte xELEMVOID8 = -100;

  // xattributes

  public static final byte xATTRIBUTE88 = -99;
  public static final byte xATTRIBUTE816 = -98;
  public static final byte xATTRIBUTE824 = -97;
  public static final byte xBOOLEAN8 = -96;
  public static final byte xJSACTION88 = -95;
  public static final byte xJSACTION816 = -94;
  public static final byte xJSACTION824 = -93;

  // xfragment

  public static final byte xFRAGSTR = -92;

  // xtestable

  public static final byte xTHEADING8 = -91;
  public static final byte xTHEADING16 = -90;
  public static final byte xTHEADING24 = -89;
  public static final byte xTCELL8 = -88;
  public static final byte xTCELL16 = -87;
  public static final byte xTCELL24 = -86;

  // otestable

  public static final byte oTHEADING8 = -85;
  public static final byte oTHEADING16 = -84;
  public static final byte oTHEADING24 = -83;
  public static final byte oTCELL8 = -82;
  public static final byte oTCELL16 = -81;
  public static final byte oTCELL24 = -80;

  // xtext

  public static final byte xTEXT8 = -79;
  public static final byte xTEXT16 = -78;
  public static final byte xTEXT24 = -77;

  // xraw

  public static final byte xRAW8 = -76;
  public static final byte xRAW16 = -75;
  public static final byte xRAW24 = -74;

  private HtmlBytes() {}

}
