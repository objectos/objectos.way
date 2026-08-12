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

  public static final byte RELEMSTR8 = -127;
  public static final byte RELEMEND8 = -126;
  public static final byte RELEMTXT88 = -125;
  public static final byte RELEMTXT816 = -124;
  public static final byte RELEMTXT824 = -123;
  public static final byte VELEMSTR8 = -122;

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

  public static final byte xRELEMSTR8 = -100;
  public static final byte xRELEMEND8 = -99;
  public static final byte xRELEMTXT88 = -98;
  public static final byte xRELEMTXT816 = -97;
  public static final byte xRELEMTXT824 = -96;
  public static final byte xVELEMSTR8 = -95;

  // xattributes

  public static final byte xATTRIBUTE88 = -94;
  public static final byte xATTRIBUTE816 = -93;
  public static final byte xATTRIBUTE824 = -92;
  public static final byte xBOOLEAN8 = -91;
  public static final byte xBOOLEAN16 = -90;
  public static final byte xBOOLEAN24 = -89;

  // xambiguous

  public static final byte xAMBIGUOUS88 = -88;
  public static final byte xAMBIGUOUS816 = -87;
  public static final byte xAMBIGUOUS824 = -86;

  private HtmlBytes() {}

  public static byte consume(byte source) {
    return switch (source) {
      case RELEMSTR8, xRELEMSTR8 -> xRELEMSTR8;

      default -> throw new IllegalArgumentException("source=" + source);
    };
  }

}