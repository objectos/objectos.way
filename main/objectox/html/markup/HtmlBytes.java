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

  public static final byte STARTTAG8 = -127;
  public static final byte ENDTAG8 = -126;
  public static final byte VOIDELEM = -125;

  // attributes

  public static final byte ATTRIBUTE88 = -124;
  public static final byte ATTRIBUTE816 = -123;
  public static final byte ATTRIBUTE824 = -122;
  public static final byte BOOLEAN8 = -121;
  public static final byte BOOLEAN16 = -120;
  public static final byte BOOLEAN24 = -119;

  // child

  public static final byte ELEM8 = -118;
  public static final byte ELEM16 = -117;
  public static final byte ELEM24 = -116;
  public static final byte ELTXT88 = -115;
  public static final byte ELTXT816 = -114;
  public static final byte ELTXT824 = -113;

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

  // xelement

  public static final byte xSTARTTAG8 = -103;

  // control flow

  public static final byte JMP24 = -102;

  private HtmlBytes() {}

  public static byte consume(byte source) {
    return switch (source) {
      case STARTTAG8, xSTARTTAG8 -> xSTARTTAG8;

      default -> throw new IllegalArgumentException("source=" + source);
    };
  }

}