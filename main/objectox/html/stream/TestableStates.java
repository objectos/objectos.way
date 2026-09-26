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
package objectox.html.stream;

import objectox.html.IntSize;
import objectox.html.markup.HtmlBytes;

final class TestableStates {

  private TestableStates() {}

  static boolean isOnly(byte code) {
    return switch (code) {
      case HtmlBytes.THEADING8,
           HtmlBytes.THEADING16,
           HtmlBytes.THEADING24,
           HtmlBytes.TCELL8,
           HtmlBytes.TCELL16,
           HtmlBytes.TCELL24,
           HtmlBytes.TPROP8,
           HtmlBytes.TPROP16,
           HtmlBytes.TPROP24,
           HtmlBytes.TPROPKEY8,
           HtmlBytes.TPROPKEY16,
           HtmlBytes.TPROPKEY24,
           HtmlBytes.TPROPVAL8,
           HtmlBytes.TPROPVAL16,
           HtmlBytes.TPROPVAL24,
           HtmlBytes.TNEWLINE,
           HtmlBytes.oTHEADING8,
           HtmlBytes.oTHEADING16,
           HtmlBytes.oTHEADING24,
           HtmlBytes.oTCELL8,
           HtmlBytes.oTCELL16,
           HtmlBytes.oTCELL24,
           HtmlBytes.oTPROP8,
           HtmlBytes.oTPROP16,
           HtmlBytes.oTPROP24,
           HtmlBytes.oTPROPKEY8,
           HtmlBytes.oTPROPKEY16,
           HtmlBytes.oTPROPKEY24,
           HtmlBytes.oTPROPVAL8,
           HtmlBytes.oTPROPVAL16,
           HtmlBytes.oTPROPVAL24 -> true;

      default -> false;
    };
  }

  static boolean isRegular(byte code) {
    return switch (code) {
      case HtmlBytes.xTHEADING8,
           HtmlBytes.xTHEADING16,
           HtmlBytes.xTHEADING24,
           HtmlBytes.xTCELL8,
           HtmlBytes.xTCELL16,
           HtmlBytes.xTCELL24,
           HtmlBytes.xTPROP8,
           HtmlBytes.xTPROP16,
           HtmlBytes.xTPROP24,
           HtmlBytes.xTPROPKEY8,
           HtmlBytes.xTPROPKEY16,
           HtmlBytes.xTPROPKEY24,
           HtmlBytes.xTPROPVAL8,
           HtmlBytes.xTPROPVAL16,
           HtmlBytes.xTPROPVAL24,
           HtmlBytes.xTNEWLINE -> true;

      default -> false;
    };
  }

  static State only(Ctx ctx, byte code) {
    return switch (code) {
      case HtmlBytes.THEADING8,
           HtmlBytes.oTHEADING8 -> new RenderTestableOnlyState(ctx, TestableKind.HEADING, IntSize.BIT8);

      case HtmlBytes.THEADING16,
           HtmlBytes.oTHEADING16 -> new RenderTestableOnlyState(ctx, TestableKind.HEADING, IntSize.BIT16);

      case HtmlBytes.THEADING24,
           HtmlBytes.oTHEADING24 -> new RenderTestableOnlyState(ctx, TestableKind.HEADING, IntSize.BIT24);

      case HtmlBytes.TCELL8,
           HtmlBytes.oTCELL8 -> new RenderTestableOnlyState(ctx, TestableKind.CELL, IntSize.BIT8);

      case HtmlBytes.TCELL16,
           HtmlBytes.oTCELL16 -> new RenderTestableOnlyState(ctx, TestableKind.CELL, IntSize.BIT16);

      case HtmlBytes.TCELL24,
           HtmlBytes.oTCELL24 -> new RenderTestableOnlyState(ctx, TestableKind.CELL, IntSize.BIT24);

      case HtmlBytes.TPROP8,
           HtmlBytes.oTPROP8 -> new RenderTestableOnlyState(ctx, TestableKind.PROP, IntSize.BIT8);

      case HtmlBytes.TPROP16,
           HtmlBytes.oTPROP16 -> new RenderTestableOnlyState(ctx, TestableKind.PROP, IntSize.BIT16);

      case HtmlBytes.TPROP24,
           HtmlBytes.oTPROP24 -> new RenderTestableOnlyState(ctx, TestableKind.PROP, IntSize.BIT24);

      case HtmlBytes.TPROPKEY8,
           HtmlBytes.oTPROPKEY8 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_KEY, IntSize.BIT8);

      case HtmlBytes.TPROPKEY16,
           HtmlBytes.oTPROPKEY16 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_KEY, IntSize.BIT16);

      case HtmlBytes.TPROPKEY24,
           HtmlBytes.oTPROPKEY24 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_KEY, IntSize.BIT24);

      case HtmlBytes.TPROPVAL8,
           HtmlBytes.oTPROPVAL8 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_VALUE, IntSize.BIT8);

      case HtmlBytes.TPROPVAL16,
           HtmlBytes.oTPROPVAL16 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_VALUE, IntSize.BIT16);

      case HtmlBytes.TPROPVAL24,
           HtmlBytes.oTPROPVAL24 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_VALUE, IntSize.BIT24);

      case HtmlBytes.TNEWLINE,
           HtmlBytes.xTNEWLINE -> new RenderTestableOnlyState(ctx, TestableKind.NEWLINE, IntSize.BIT8);

      default -> throw State.malformed();
    };
  }

  static int skip(byte code) {
    return switch (code) {
      case HtmlBytes.xTHEADING8 -> 2;

      case HtmlBytes.xTHEADING16 -> 3;

      case HtmlBytes.xTHEADING24 -> 4;

      case HtmlBytes.xTCELL8 -> 2;

      case HtmlBytes.xTCELL16 -> 4;

      case HtmlBytes.xTCELL24 -> 6;

      case HtmlBytes.xTPROP8 -> 2;

      case HtmlBytes.xTPROP16 -> 4;

      case HtmlBytes.xTPROP24 -> 6;

      case HtmlBytes.xTPROPKEY8 -> 1;

      case HtmlBytes.xTPROPKEY16 -> 2;

      case HtmlBytes.xTPROPKEY24 -> 3;

      case HtmlBytes.xTPROPVAL8 -> 1;

      case HtmlBytes.xTPROPVAL16 -> 2;

      case HtmlBytes.xTPROPVAL24 -> 3;

      case HtmlBytes.xTNEWLINE -> 0;

      default -> throw State.malformed();
    };
  }

  static State text(Ctx ctx, byte code) {
    return switch (code) {
      case HtmlBytes.xTHEADING8 -> new RenderTestableTextState(ctx, TestableKind.HEADING, IntSize.BIT8);

      case HtmlBytes.xTHEADING16 -> new RenderTestableTextState(ctx, TestableKind.HEADING, IntSize.BIT16);

      case HtmlBytes.xTHEADING24 -> new RenderTestableTextState(ctx, TestableKind.HEADING, IntSize.BIT24);

      case HtmlBytes.xTCELL8 -> new RenderTestableTextState(ctx, TestableKind.CELL, IntSize.BIT8);

      case HtmlBytes.xTCELL16 -> new RenderTestableTextState(ctx, TestableKind.CELL, IntSize.BIT16);

      case HtmlBytes.xTCELL24 -> new RenderTestableTextState(ctx, TestableKind.CELL, IntSize.BIT24);

      case HtmlBytes.xTPROP8 -> new RenderTestableTextState(ctx, TestableKind.PROP, IntSize.BIT8);

      case HtmlBytes.xTPROP16 -> new RenderTestableTextState(ctx, TestableKind.PROP, IntSize.BIT16);

      case HtmlBytes.xTPROP24 -> new RenderTestableTextState(ctx, TestableKind.PROP, IntSize.BIT24);

      case HtmlBytes.xTPROPKEY8 -> new RenderTestableTextState(ctx, TestableKind.PROP_KEY, IntSize.BIT8);

      case HtmlBytes.xTPROPKEY16 -> new RenderTestableTextState(ctx, TestableKind.PROP_KEY, IntSize.BIT16);

      case HtmlBytes.xTPROPKEY24 -> new RenderTestableTextState(ctx, TestableKind.PROP_KEY, IntSize.BIT24);

      case HtmlBytes.xTPROPVAL8 -> new RenderTestableTextState(ctx, TestableKind.PROP_VALUE, IntSize.BIT8);

      case HtmlBytes.xTPROPVAL16 -> new RenderTestableTextState(ctx, TestableKind.PROP_VALUE, IntSize.BIT16);

      case HtmlBytes.xTPROPVAL24 -> new RenderTestableTextState(ctx, TestableKind.PROP_VALUE, IntSize.BIT24);

      case HtmlBytes.xTNEWLINE -> new RenderTestableOnlyState(ctx, TestableKind.NEWLINE, IntSize.BIT8);

      default -> throw State.malformed();
    };
  }

}
