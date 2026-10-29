/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import objectox.html.IntSize;

final class ValueRecorder {

  private final HtmlSink sink;

  ValueRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final int record(IntSize size, int value) {
    final byte opcode;
    opcode = sink.getByte(value);

    final byte recorded;
    recorded = record0(opcode);

    sink.setByte(value, recorded);

    sink.addInt(size, value);

    return 1;
  }

  public final int testableOnly(IntSize size, int value) {
    final byte opcode;
    opcode = sink.getByte(value);

    final byte recorded;
    recorded = testableOnly0(opcode);

    sink.setByte(value, recorded);

    sink.addInt(size, value);

    return 1;
  }

  private byte record0(byte opcode) {
    return switch (opcode) {
      case HtmlBytes.ELEMSTR8 -> HtmlBytes.xELEMSTR8;
      case HtmlBytes.ELEMVOID8 -> HtmlBytes.xELEMVOID8;

      case HtmlBytes.ATTRIBUTE88 -> HtmlBytes.xATTRIBUTE88;
      case HtmlBytes.ATTRIBUTE816 -> HtmlBytes.xATTRIBUTE816;
      case HtmlBytes.ATTRIBUTE824 -> HtmlBytes.xATTRIBUTE824;
      case HtmlBytes.BOOLEAN8 -> HtmlBytes.xBOOLEAN8;

      case HtmlBytes.JSACTION88 -> HtmlBytes.xJSACTION88;
      case HtmlBytes.JSACTION816 -> HtmlBytes.xJSACTION816;
      case HtmlBytes.JSACTION824 -> HtmlBytes.xJSACTION824;

      case HtmlBytes.TEXT8 -> HtmlBytes.xTEXT8;
      case HtmlBytes.TEXT16 -> HtmlBytes.xTEXT16;
      case HtmlBytes.TEXT24 -> HtmlBytes.xTEXT24;

      case HtmlBytes.RAW8 -> HtmlBytes.xRAW8;
      case HtmlBytes.RAW16 -> HtmlBytes.xRAW16;
      case HtmlBytes.RAW24 -> HtmlBytes.xRAW24;

      case HtmlBytes.THEADING8 -> HtmlBytes.xTHEADING8;
      case HtmlBytes.THEADING16 -> HtmlBytes.xTHEADING16;
      case HtmlBytes.THEADING24 -> HtmlBytes.xTHEADING24;

      case HtmlBytes.TCELL8 -> HtmlBytes.xTCELL8;
      case HtmlBytes.TCELL16 -> HtmlBytes.xTCELL16;
      case HtmlBytes.TCELL24 -> HtmlBytes.xTCELL24;

      case HtmlBytes.xELEMSTR8 -> HtmlBytes.xELEMSTR8;
      case HtmlBytes.xELEMVOID8 -> HtmlBytes.xELEMVOID8;

      case HtmlBytes.xATTRIBUTE88 -> HtmlBytes.xATTRIBUTE88;
      case HtmlBytes.xATTRIBUTE816 -> HtmlBytes.xATTRIBUTE816;
      case HtmlBytes.xATTRIBUTE824 -> HtmlBytes.xATTRIBUTE824;
      case HtmlBytes.xBOOLEAN8 -> HtmlBytes.xBOOLEAN8;

      case HtmlBytes.xJSACTION88 -> HtmlBytes.xJSACTION88;
      case HtmlBytes.xJSACTION816 -> HtmlBytes.xJSACTION816;
      case HtmlBytes.xJSACTION824 -> HtmlBytes.xJSACTION824;

      case HtmlBytes.xTEXT8 -> HtmlBytes.xTEXT8;
      case HtmlBytes.xTEXT16 -> HtmlBytes.xTEXT16;
      case HtmlBytes.xTEXT24 -> HtmlBytes.xTEXT24;

      case HtmlBytes.xRAW8 -> HtmlBytes.xRAW8;
      case HtmlBytes.xRAW16 -> HtmlBytes.xRAW16;
      case HtmlBytes.xRAW24 -> HtmlBytes.xRAW24;

      case HtmlBytes.xTHEADING8 -> HtmlBytes.xTHEADING8;
      case HtmlBytes.xTHEADING16 -> HtmlBytes.xTHEADING16;
      case HtmlBytes.xTHEADING24 -> HtmlBytes.xTHEADING24;

      case HtmlBytes.xTCELL8 -> HtmlBytes.xTCELL8;
      case HtmlBytes.xTCELL16 -> HtmlBytes.xTCELL16;
      case HtmlBytes.xTCELL24 -> HtmlBytes.xTCELL24;

      case HtmlBytes.oTHEADING8 -> HtmlBytes.oTHEADING8;
      case HtmlBytes.oTHEADING16 -> HtmlBytes.oTHEADING16;
      case HtmlBytes.oTHEADING24 -> HtmlBytes.oTHEADING24;

      case HtmlBytes.oTCELL8 -> HtmlBytes.oTCELL8;
      case HtmlBytes.oTCELL16 -> HtmlBytes.oTCELL16;
      case HtmlBytes.oTCELL24 -> HtmlBytes.oTCELL24;

      default -> throw new IllegalArgumentException("Invalid opcode: " + opcode);
    };
  }

  private byte testableOnly0(byte opcode) {
    return switch (opcode) {
      case HtmlBytes.THEADING8 -> HtmlBytes.oTHEADING8;
      case HtmlBytes.THEADING16 -> HtmlBytes.oTHEADING16;
      case HtmlBytes.THEADING24 -> HtmlBytes.oTHEADING24;

      case HtmlBytes.TCELL8 -> HtmlBytes.oTCELL8;
      case HtmlBytes.TCELL16 -> HtmlBytes.oTCELL16;
      case HtmlBytes.TCELL24 -> HtmlBytes.oTCELL24;

      default -> throw new IllegalArgumentException("Invalid opcode: " + opcode);
    };
  }

}
