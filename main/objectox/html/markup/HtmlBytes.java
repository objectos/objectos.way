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

  // ambiguous

  public static final byte AMBIGUOUS88 = -118;
  public static final byte AMBIGUOUS816 = -117;
  public static final byte AMBIGUOUS824 = -116;

  // fragment

  public static final byte FRAGSTR = -115;
  public static final byte FRAGEND = -114;

  // text

  public static final byte TEXT8 = -113;
  public static final byte TEXT16 = -112;
  public static final byte TEXT24 = -111;

  // raw

  public static final byte RAW8 = -110;
  public static final byte RAW16 = -109;
  public static final byte RAW24 = -108;

  // contents

  public static final byte VALUES8 = -107;
  public static final byte VALUES16 = -106;
  public static final byte VALUES24 = -105;

  // xelement

  public static final byte xELEMSTR8 = -104;
  public static final byte xELEMVOID8 = -103;

  // xattributes

  public static final byte xATTRIBUTE88 = -102;
  public static final byte xATTRIBUTE816 = -101;
  public static final byte xATTRIBUTE824 = -100;
  public static final byte xBOOLEAN8 = -99;
  public static final byte xJSACTION88 = -98;
  public static final byte xJSACTION816 = -97;
  public static final byte xJSACTION824 = -96;

  // xambiguous

  public static final byte aAMBIGUOUS88 = -95;
  public static final byte aAMBIGUOUS816 = -94;
  public static final byte aAMBIGUOUS824 = -93;
  public static final byte eAMBIGUOUS88 = -92;
  public static final byte eAMBIGUOUS816 = -91;
  public static final byte eAMBIGUOUS824 = -90;

  // xfragment

  public static final byte xFRAGSTR = -89;

  // xtext

  public static final byte xTEXT8 = -88;
  public static final byte xTEXT16 = -87;
  public static final byte xTEXT24 = -86;

  // xraw

  public static final byte xRAW8 = -85;
  public static final byte xRAW16 = -84;
  public static final byte xRAW24 = -83;

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
