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
  public static final byte ELEMTXT88 = -126;
  public static final byte ELEMTXT816 = -125;
  public static final byte ELEMTXT824 = -124;
  public static final byte ELEMEND8 = -123;
  public static final byte ELEMVOID = -122;

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

  public static final byte FRAG8 = -112;
  public static final byte FRAG16 = -111;
  public static final byte FRAG24 = -110;

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

  // xtext

  public static final byte xTEXT8 = -84;
  public static final byte xTEXT16 = -83;
  public static final byte xTEXT24 = -82;

  // xraw

  public static final byte xRAW8 = -81;
  public static final byte xRAW16 = -80;
  public static final byte xRAW24 = -79;

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