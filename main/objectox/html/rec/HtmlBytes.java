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

import objectos.html.ElementName;
import objectox.html.elem.ElementNamePojo;

public final class HtmlBytes {

  // element

  public static final byte STARTTAG8 = -128;
  public static final byte ENDTAG8 = -127;

  // attributes

  public static final byte ATTRIBUTE88 = -126;
  public static final byte ATTRIBUTE816 = -125;
  public static final byte ATTRIBUTE824 = -124;
  public static final byte BOOLEAN8 = -123;

  // ambiguous

  public static final byte AMBIGUOUS88 = -122;
  public static final byte AMBIGUOUS816 = -121;
  public static final byte AMBIGUOUS824 = -120;

  // text

  public static final byte TEXT8 = -119;
  public static final byte TEXT16 = -118;
  public static final byte TEXT24 = -117;

  // raw

  public static final byte RAW8 = -116;
  public static final byte RAW16 = -115;
  public static final byte RAW24 = -114;

  // flatten

  public static final byte STARTFLAT = -113;

  // fragment

  public static final byte STARTFRAG = -112;
  public static final byte ENDFRAG = -111;

  // contents

  public static final byte VALUES0 = -110;
  public static final byte VALUES8 = -109;
  public static final byte VALUES16 = -108;
  public static final byte VALUES24 = -107;

  // xelement

  public static final byte xSTARTTAG8 = -106;
  public static final byte xENDTAG8 = -105;

  // xattributes

  public static final byte xATTRIBUTE88 = -104;
  public static final byte xATTRIBUTE816 = -103;
  public static final byte xATTRIBUTE824 = -102;
  public static final byte xBOOLEAN8 = -101;

  // xambiguous

  public static final byte xAMBIGUOUS88 = -100;
  public static final byte xAMBIGUOUS816 = -99;
  public static final byte xAMBIGUOUS824 = -98;

  // xtext

  public static final byte xTEXT8 = -97;
  public static final byte xTEXT16 = -96;
  public static final byte xTEXT24 = -95;

  // xraw

  public static final byte xRAW8 = -94;
  public static final byte xRAW16 = -93;
  public static final byte xRAW24 = -92;

  // xflatten

  public static final byte xSTARTFLAT = -91;

  // xfragment

  public static final byte xSTARTFRAG = -90;

  //

  private static final int BYTE_MASK = 0xFF;

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

  //

  public static int decodeInt(byte b0) {
    return toInt(b0, 0);
  }

  public static int decodeInt(byte b0, byte b1) {
    int int0;
    int0 = toInt(b0, 0);

    int int1;
    int1 = toInt(b1, 8);

    return int1 | int0;
  }

  public static byte encodeInt0(int value) {
    return (byte) value;
  }

  public static byte encodeInt1(int value) {
    return (byte) (value >>> 8);
  }

  public static byte encodeInt2(int value) {
    return (byte) (value >>> 16);
  }

  public static byte encodeName(ElementName name) {
    final ElementNamePojo impl;
    impl = (ElementNamePojo) name;

    final int ordinal;
    ordinal = impl.index();

    return encodeInt0(ordinal);
  }

  private static final int VARINT_MAX1 = 0x7F;

  public static int decodeCommonEnd(byte[] buf, int startIndex, int endIndex) {
    int length;
    length = endIndex - startIndex;

    return switch (length) {
      case 1 -> buf[endIndex];

      case 2 -> decodeVarint(buf[endIndex], buf[endIndex - 1]);

      case 3 -> decodeVarint(buf[endIndex], buf[endIndex - 1], buf[endIndex - 2]);

      default -> throw new IllegalArgumentException(
          "HtmlTemplate is too large :: length=" + length
      );
    };
  }

  public static int decodeCommonEnd(ByteArray buf, int startIndex, int endIndex) {
    int length;
    length = endIndex - startIndex;

    return switch (length) {
      case 1 -> buf.get(endIndex);

      case 2 -> decodeVarint(buf.get(endIndex), buf.get(endIndex - 1));

      case 3 -> decodeVarint(buf.get(endIndex), buf.get(endIndex - 1), buf.get(endIndex - 2));

      default -> throw new IllegalArgumentException(
          "HtmlTemplate is too large :: length=" + length
      );
    };
  }

  public static int decodeOffset(byte[] buf, int startIndex, int endIndex) {
    int length;
    length = endIndex - startIndex;

    return switch (length) {
      case 1 -> buf[startIndex];

      case 2 -> decodeVarint(buf[startIndex], buf[startIndex + 1]);

      case 3 -> decodeVarint(buf[startIndex], buf[startIndex + 1], buf[startIndex + 2]);

      default -> throw new IllegalArgumentException(
          "HtmlTemplate is too large :: length=" + length
      );
    };
  }

  public static int toInt(byte b, int shift) {
    return (b & BYTE_MASK) << shift;
  }

  public static int decodeVarint(byte b0, byte b1) {
    int int0;
    int0 = decodeVarintValue(b0, 0);

    int int1;
    int1 = decodeVarintValue(b1, 7);

    return int0 | int1;
  }

  public static int decodeVarint(byte b0, byte b1, byte b2) {
    int int0;
    int0 = decodeVarintValue(b0, 0);

    int int1;
    int1 = decodeVarintValue(b1, 7);

    int int2;
    int2 = decodeVarintValue(b2, 14);

    return int0 | int1 | int2;
  }

  private static int decodeVarintValue(byte value, int shift) {
    int intValue;
    intValue = value & VARINT_MAX1;

    return intValue << shift;
  }

  static final int FIXED1_MAX = (1 << 8) - 1;

  static final int FIXED2_MAX = (1 << 16) - 1;

  public static final int FIXED3_MAX = (1 << 24) - 1;

  public static int decodeLength3(byte b0, byte b1, byte b2) {
    final int int0;
    int0 = toInt(b0, 0);

    final int int1;
    int1 = toInt(b1, 8);

    final int int2;
    int2 = toInt(b2, 16);

    return int0 | int1 | int2;
  }

}