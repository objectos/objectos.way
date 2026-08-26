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

  // ambiguous

  public static final byte AMBIGUOUS88 = -117;
  public static final byte AMBIGUOUS816 = -116;
  public static final byte AMBIGUOUS824 = -115;

  // fragment

  public static final byte FRAGSTR = -114;
  public static final byte FRAGEND = -113;

  // text

  public static final byte TEXT8 = -112;
  public static final byte TEXT16 = -111;
  public static final byte TEXT24 = -110;

  // raw

  public static final byte RAW8 = -109;
  public static final byte RAW16 = -108;
  public static final byte RAW24 = -107;

  // contents

  public static final byte VALUES8 = -106;
  public static final byte VALUES16 = -105;
  public static final byte VALUES24 = -104;

  // xelement

  public static final byte xELEMSTR8 = -103;
  public static final byte xELEMTXT88 = -102;
  public static final byte xELEMTXT816 = -101;
  public static final byte xELEMTXT824 = -100;
  public static final byte xELEMVOID8 = -99;

  // xattributes

  public static final byte xATTRIBUTE88 = -98;
  public static final byte xATTRIBUTE816 = -97;
  public static final byte xATTRIBUTE824 = -96;
  public static final byte xBOOLEAN8 = -95;

  // xambiguous

  public static final byte aAMBIGUOUS88 = -94;
  public static final byte aAMBIGUOUS816 = -93;
  public static final byte aAMBIGUOUS824 = -92;
  public static final byte eAMBIGUOUS88 = -91;
  public static final byte eAMBIGUOUS816 = -90;
  public static final byte eAMBIGUOUS824 = -89;

  // xfragment

  public static final byte xFRAGSTR = -88;

  // xtext

  public static final byte xTEXT8 = -87;
  public static final byte xTEXT16 = -86;
  public static final byte xTEXT24 = -85;

  // xraw

  public static final byte xRAW8 = -84;
  public static final byte xRAW16 = -83;
  public static final byte xRAW24 = -82;

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