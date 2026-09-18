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

  // text

  public static final byte TEXT8 = -116;
  public static final byte TEXT16 = -115;
  public static final byte TEXT24 = -114;

  // raw

  public static final byte RAW8 = -113;
  public static final byte RAW16 = -112;
  public static final byte RAW24 = -111;

  // contents

  public static final byte VALUES8 = -110;
  public static final byte VALUES16 = -109;
  public static final byte VALUES24 = -108;

  // xelement

  public static final byte xELEMSTR8 = -107;
  public static final byte xELEMVOID8 = -106;

  // xattributes

  public static final byte xATTRIBUTE88 = -105;
  public static final byte xATTRIBUTE816 = -104;
  public static final byte xATTRIBUTE824 = -103;
  public static final byte xBOOLEAN8 = -102;
  public static final byte xJSACTION88 = -101;
  public static final byte xJSACTION816 = -100;
  public static final byte xJSACTION824 = -99;

  // xfragment

  public static final byte xFRAGSTR = -98;

  // xtext

  public static final byte xTEXT8 = -97;
  public static final byte xTEXT16 = -96;
  public static final byte xTEXT24 = -95;

  // xraw

  public static final byte xRAW8 = -94;
  public static final byte xRAW16 = -93;
  public static final byte xRAW24 = -92;

  // testable

  public static final byte TEST8 = -91;
  public static final byte TEST16 = -90;
  public static final byte TEST24 = -89;
  public static final byte TESTONLY8 = -88;
  public static final byte TESTONLY16 = -87;
  public static final byte TESTONLY24 = -86;

  // xtestable

  public static final byte xTEST8 = -85;
  public static final byte xTEST16 = -84;
  public static final byte xTEST24 = -83;
  public static final byte xTESTONLY8 = -82;
  public static final byte xTESTONLY16 = -81;
  public static final byte xTESTONLY24 = -80;

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

      case TEST8 -> xTEST8;

      case TEST16 -> xTEST16;

      case TEST24 -> xTEST24;

      case TESTONLY8 -> xTESTONLY8;

      case TESTONLY16 -> xTESTONLY16;

      case TESTONLY24 -> xTESTONLY24;

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

      case xTEST8 -> xTEST8;

      case xTEST16 -> xTEST16;

      case xTEST24 -> xTEST24;

      case xTESTONLY8 -> xTESTONLY8;

      case xTESTONLY16 -> xTESTONLY16;

      case xTESTONLY24 -> xTESTONLY24;

      default -> throw new IllegalArgumentException("source=" + source);
    };
  }

}
