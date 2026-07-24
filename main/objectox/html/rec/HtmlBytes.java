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
package objectox.html.rec;

public final class HtmlBytes {

  // document

  public static final byte DOCTYPE = -128;

  // element

  public static final byte STARTTAG8 = -127;
  public static final byte ENDTAG8 = -126;

  // attributes

  public static final byte ATTRIBUTE88 = -125;
  public static final byte ATTRIBUTE816 = -124;
  public static final byte ATTRIBUTE824 = -123;
  public static final byte BOOLEAN8 = -122;

  // ambiguous

  public static final byte AMBIGUOUS88 = -121;
  public static final byte AMBIGUOUS816 = -120;
  public static final byte AMBIGUOUS824 = -119;

  // text

  public static final byte TEXT8 = -118;
  public static final byte TEXT16 = -117;
  public static final byte TEXT24 = -116;

  // raw

  public static final byte RAW8 = -115;
  public static final byte RAW16 = -114;
  public static final byte RAW24 = -113;

  // flatten

  public static final byte STARTFLAT = -112;

  // fragment

  public static final byte STARTFRAG = -111;
  public static final byte ENDFRAG = -110;

  // contents

  public static final byte VALUES0 = -109;
  public static final byte VALUES8 = -108;
  public static final byte VALUES16 = -107;
  public static final byte VALUES24 = -106;

  // xelement

  public static final byte xSTARTTAG8 = -105;
  public static final byte xENDTAG8 = -104;

  // xattributes

  public static final byte xATTRIBUTE88 = -103;
  public static final byte xATTRIBUTE816 = -102;
  public static final byte xATTRIBUTE824 = -101;
  public static final byte xBOOLEAN8 = -100;

  // xambiguous

  public static final byte xAMBIGUOUS88 = -99;
  public static final byte xAMBIGUOUS816 = -98;
  public static final byte xAMBIGUOUS824 = -97;

  // xtext

  public static final byte xTEXT8 = -96;
  public static final byte xTEXT16 = -95;
  public static final byte xTEXT24 = -94;

  // xraw

  public static final byte xRAW8 = -93;
  public static final byte xRAW16 = -92;
  public static final byte xRAW24 = -91;

  // xflatten

  public static final byte xSTARTFLAT = -90;

  // xfragment

  public static final byte xSTARTFRAG = -89;

  private HtmlBytes() {}

  public static byte consume(byte source) {
    return switch (source) {
      case STARTTAG8, xSTARTTAG8 -> xSTARTTAG8;

      case ATTRIBUTE88, xATTRIBUTE88 -> xATTRIBUTE88;

      case ATTRIBUTE816, xATTRIBUTE816 -> xATTRIBUTE816;

      case ATTRIBUTE824, xATTRIBUTE824 -> xATTRIBUTE824;

      case BOOLEAN8, xBOOLEAN8 -> xBOOLEAN8;

      case AMBIGUOUS88, xAMBIGUOUS88 -> xAMBIGUOUS88;

      case AMBIGUOUS816, xAMBIGUOUS816 -> xAMBIGUOUS816;

      case AMBIGUOUS824, xAMBIGUOUS824 -> xAMBIGUOUS824;

      case TEXT8, xTEXT8 -> xTEXT8;

      case TEXT16, xTEXT16 -> xTEXT16;

      case TEXT24, xTEXT24 -> xTEXT24;

      case RAW8, xRAW8 -> xRAW8;

      case RAW16, xRAW16 -> xRAW16;

      case RAW24, xRAW24 -> xRAW24;

      case STARTFLAT, xSTARTFLAT -> xSTARTFLAT;

      case STARTFRAG, xSTARTFRAG -> xSTARTFRAG;

      default -> throw new IllegalArgumentException("source=" + source);
    };
  }

}