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
  public static final byte BOOLEAN16 = -117;
  public static final byte BOOLEAN24 = -116;

  // ambiguous

  public static final byte AMBIGUOUS88 = -115;
  public static final byte AMBIGUOUS816 = -114;
  public static final byte AMBIGUOUS824 = -113;

  // fragment

  public static final byte FRAGSTR = -112;
  public static final byte FRAGEND = -111;

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
  public static final byte xELEMTXT88 = -100;
  public static final byte xELEMTXT816 = -99;
  public static final byte xELEMTXT824 = -98;
  public static final byte xELEMVOID8 = -97;

  // xattributes

  public static final byte xATTRIBUTE88 = -96;
  public static final byte xATTRIBUTE816 = -95;
  public static final byte xATTRIBUTE824 = -94;
  public static final byte xBOOLEAN8 = -93;
  public static final byte xBOOLEAN16 = -92;
  public static final byte xBOOLEAN24 = -91;

  // xambiguous

  public static final byte aAMBIGUOUS88 = -90;
  public static final byte aAMBIGUOUS816 = -89;
  public static final byte aAMBIGUOUS824 = -88;
  public static final byte eAMBIGUOUS88 = -87;
  public static final byte eAMBIGUOUS816 = -86;
  public static final byte eAMBIGUOUS824 = -85;

  // xfragment

  public static final byte xFRAGSTR = -84;

  // xtext

  public static final byte xTEXT8 = -83;
  public static final byte xTEXT16 = -82;
  public static final byte xTEXT24 = -81;

  // xraw

  public static final byte xRAW8 = -80;
  public static final byte xRAW16 = -79;
  public static final byte xRAW24 = -78;

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

      case BOOLEAN16 -> xBOOLEAN16;

      case BOOLEAN24 -> xBOOLEAN24;

      case RAW8 -> xRAW8;

      case RAW16 -> xRAW16;

      case RAW24 -> xRAW24;

      case TEXT8 -> xTEXT8;

      case TEXT16 -> xTEXT16;

      case TEXT24 -> xTEXT24;

      default -> throw new IllegalArgumentException("source=" + source);
    };
  }

}