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
  public static final byte ELEMEND8 = -126;
  public static final byte ELEMTXT88 = -125;
  public static final byte ELEMTXT816 = -124;
  public static final byte ELEMTXT824 = -123;
  public static final byte ELEMVOID8 = -122;

  // attributes

  public static final byte ATTRIBUTE88 = -121;
  public static final byte ATTRIBUTE816 = -120;
  public static final byte ATTRIBUTE824 = -119;
  public static final byte BOOLEAN8 = -118;
  public static final byte JSACTION88 = -117;
  public static final byte JSACTION816 = -116;
  public static final byte JSACTION824 = -115;

  // ambiguous

  public static final byte AMBIGUOUS88 = -114;
  public static final byte AMBIGUOUS816 = -113;
  public static final byte AMBIGUOUS824 = -112;

  // fragment

  public static final byte FRAGSTR = -111;
  public static final byte FRAGEND = -110;

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
  public static final byte xELEMTXT88 = -99;
  public static final byte xELEMTXT816 = -98;
  public static final byte xELEMTXT824 = -97;
  public static final byte xELEMVOID8 = -96;

  // xattributes

  public static final byte xATTRIBUTE88 = -95;
  public static final byte xATTRIBUTE816 = -94;
  public static final byte xATTRIBUTE824 = -93;
  public static final byte xBOOLEAN8 = -92;
  public static final byte xJSACTION88 = -91;
  public static final byte xJSACTION816 = -90;
  public static final byte xJSACTION824 = -89;

  // xambiguous

  public static final byte aAMBIGUOUS88 = -88;
  public static final byte aAMBIGUOUS816 = -87;
  public static final byte aAMBIGUOUS824 = -86;
  public static final byte eAMBIGUOUS88 = -85;
  public static final byte eAMBIGUOUS816 = -84;
  public static final byte eAMBIGUOUS824 = -83;

  // xfragment

  public static final byte xFRAGSTR = -82;

  // xtext

  public static final byte xTEXT8 = -81;
  public static final byte xTEXT16 = -80;
  public static final byte xTEXT24 = -79;

  // xraw

  public static final byte xRAW8 = -78;
  public static final byte xRAW16 = -77;
  public static final byte xRAW24 = -76;

  private HtmlBytes() {}

  public static byte attr(byte source) {
    return switch (source) {
      case AMBIGUOUS88 -> aAMBIGUOUS88;

      case AMBIGUOUS816 -> aAMBIGUOUS816;

      case AMBIGUOUS824 -> aAMBIGUOUS824;

      default -> throw new IllegalArgumentException("source=" + source);
    };
  }

  public static byte consume(byte source) {
    return switch (source) {
      case ELEMSTR8 -> xELEMSTR8;

      case ELEMTXT88 -> xELEMTXT88;

      case ELEMTXT816 -> xELEMTXT816;

      case ELEMTXT824 -> xELEMTXT824;

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

      case xELEMSTR8 -> xELEMSTR8;

      case xELEMTXT88 -> xELEMTXT88;

      case xELEMTXT816 -> xELEMTXT816;

      case xELEMTXT824 -> xELEMTXT824;

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

      default -> throw new IllegalArgumentException("source=" + source);
    };
  }

}