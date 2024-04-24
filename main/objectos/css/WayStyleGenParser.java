/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.css;

import static objectos.css.Utility.ALIGN_ITEMS;
import static objectos.css.Utility.BACKGROUND_COLOR;
import static objectos.css.Utility.BORDER_COLLAPSE;
import static objectos.css.Utility.BOTTOM;
import static objectos.css.Utility.CONTENT;
import static objectos.css.Utility.CURSOR;
import static objectos.css.Utility.DISPLAY;
import static objectos.css.Utility.END;
import static objectos.css.Utility.FILL;
import static objectos.css.Utility.FLEX_DIRECTION;
import static objectos.css.Utility.FLEX_GROW;
import static objectos.css.Utility.FONT_SIZE1;
import static objectos.css.Utility.FONT_SIZE2;
import static objectos.css.Utility.FONT_SIZEX;
import static objectos.css.Utility.HEIGHT;
import static objectos.css.Utility.JUSTIFY_CONTENT;
import static objectos.css.Utility.LEFT;
import static objectos.css.Utility.LETTER_SPACING;
import static objectos.css.Utility.LINE_HEIGHT;
import static objectos.css.Utility.MARGIN;
import static objectos.css.Utility.MARGIN_BOTTOM;
import static objectos.css.Utility.MARGIN_LEFT;
import static objectos.css.Utility.MARGIN_RIGHT;
import static objectos.css.Utility.MARGIN_TOP;
import static objectos.css.Utility.MARGIN_X;
import static objectos.css.Utility.MARGIN_Y;
import static objectos.css.Utility.MAX_WIDTH;
import static objectos.css.Utility.OPACITY;
import static objectos.css.Utility.OUTLINE_COLOR;
import static objectos.css.Utility.OUTLINE_OFFSET;
import static objectos.css.Utility.OUTLINE_STYLE;
import static objectos.css.Utility.OUTLINE_WIDTH;
import static objectos.css.Utility.OVERFLOW;
import static objectos.css.Utility.OVERFLOW_X;
import static objectos.css.Utility.OVERFLOW_Y;
import static objectos.css.Utility.PADDING;
import static objectos.css.Utility.PADDING_BOTTOM;
import static objectos.css.Utility.PADDING_LEFT;
import static objectos.css.Utility.PADDING_RIGHT;
import static objectos.css.Utility.PADDING_TOP;
import static objectos.css.Utility.PADDING_X;
import static objectos.css.Utility.PADDING_Y;
import static objectos.css.Utility.POINTER_EVENTS;
import static objectos.css.Utility.POSITION;
import static objectos.css.Utility.RIGHT;
import static objectos.css.Utility.START;
import static objectos.css.Utility.TEXT_ALIGN;
import static objectos.css.Utility.TEXT_COLOR;
import static objectos.css.Utility.TEXT_DECORATION;
import static objectos.css.Utility.TOP;
import static objectos.css.Utility.TRANSITION_DURATION;
import static objectos.css.Utility.TRANSITION_PROPERTY;
import static objectos.css.Utility.USER_SELECT;
import static objectos.css.Utility.VERTICAL_ALIGN;
import static objectos.css.Utility.WIDTH;
import static objectos.css.Utility.Z_INDEX;

import java.util.List;
import java.util.Map;

abstract class WayStyleGenParser extends WayStyleGenVariants {

  final WayStyleGenConfig config;

  private String className;

  private boolean negative;

  private List<Variant> variants;

  WayStyleGenParser(WayStyleGenConfig config) {
    this.config = config;
  }

  @Override
  final Variant getVariant(String variantName) {
    return config.getVariant(variantName);
  }

  @Override
  final Rule onVariants(String className, List<Variant> variants, String value) {
    this.className = className;

    this.variants = variants;

    Map<String, String> utilities;
    utilities = config.utilities();

    String customRule;
    customRule = utilities.get(value);

    if (customRule != null) {
      return Utility.CUSTOM.get(className, variants, customRule);
    }

    // static hash map... (sort of)
    return switch (value) {
      // Accessibility
      case "sr-only" -> Utility.ACCESSIBILITY.get(className, variants, """
      position: absolute;
      width: 1px;
      height: 1px;
      padding: 0;
      margin: -1px;
      overflow: hidden;
      clip: rect(0, 0, 0, 0);
      white-space: nowrap;
      border-width: 0;
      """);
      case "not-sr-only" -> Utility.ACCESSIBILITY.get(className, variants, """
      position: static;
      width: auto;
      height: auto;
      padding: 0;
      margin: 0;
      overflow: visible;
      clip: auto;
      white-space: normal;
      """);

      // AlignItems
      case "items-start" -> nameValue(ALIGN_ITEMS, "flex-start");
      case "items-end" -> nameValue(ALIGN_ITEMS, "flex-end");
      case "items-center" -> nameValue(ALIGN_ITEMS, "center");
      case "items-baseline" -> nameValue(ALIGN_ITEMS, "baseline");
      case "items-stretch" -> nameValue(ALIGN_ITEMS, "stretch");

      // BorderCollapse
      case "border-collapse" -> nameValue(BORDER_COLLAPSE, "collapse");
      case "border-separate" -> nameValue(BORDER_COLLAPSE, "separate");

      // Display
      case "block",
           "inline-block",
           "inline",
           "flex",
           "inline-flex",
           "table",
           "inline-table",
           "table-caption",
           "table-cell",
           "table-column",
           "table-column-group",
           "table-footer-group",
           "table-header-group",
           "table-row-group",
           "table-row",
           "flow-root",
           "grid",
           "inline-grid",
           "contents",
           "list-item" -> nameValue(DISPLAY, value);
      case "hidden" -> nameValue(DISPLAY, "none");

      // Flex Direction
      case "flex-row" -> nameValue(FLEX_DIRECTION, "row");
      case "flex-row-reverse" -> nameValue(FLEX_DIRECTION, "row-reverse");
      case "flex-col" -> nameValue(FLEX_DIRECTION, "column");
      case "flex-col-reverse" -> nameValue(FLEX_DIRECTION, "column-reverse");

      // Justify Content
      case "justify-normal" -> nameValue(JUSTIFY_CONTENT, "normal");
      case "justify-start" -> nameValue(JUSTIFY_CONTENT, "flex-start");
      case "justify-end" -> nameValue(JUSTIFY_CONTENT, "flex-end");
      case "justify-center" -> nameValue(JUSTIFY_CONTENT, "center");
      case "justify-between" -> nameValue(JUSTIFY_CONTENT, "space-between");
      case "justify-around" -> nameValue(JUSTIFY_CONTENT, "space-around");
      case "justify-evenly" -> nameValue(JUSTIFY_CONTENT, "space-evenly");
      case "justify-stretch" -> nameValue(JUSTIFY_CONTENT, "stretch");

      // Overflow
      case "overflow-auto" -> nameValue(OVERFLOW, "auto");
      case "overflow-hidden" -> nameValue(OVERFLOW, "hidden");
      case "overflow-clip" -> nameValue(OVERFLOW, "clip");
      case "overflow-visible" -> nameValue(OVERFLOW, "visible");
      case "overflow-scroll" -> nameValue(OVERFLOW, "scroll");
      case "overflow-x-auto" -> nameValue(OVERFLOW_X, "auto");
      case "overflow-x-hidden" -> nameValue(OVERFLOW_X, "hidden");
      case "overflow-x-clip" -> nameValue(OVERFLOW_X, "clip");
      case "overflow-x-visible" -> nameValue(OVERFLOW_X, "visible");
      case "overflow-x-scroll" -> nameValue(OVERFLOW_X, "scroll");
      case "overflow-y-auto" -> nameValue(OVERFLOW_Y, "auto");
      case "overflow-y-hidden" -> nameValue(OVERFLOW_Y, "hidden");
      case "overflow-y-clip" -> nameValue(OVERFLOW_Y, "clip");
      case "overflow-y-visible" -> nameValue(OVERFLOW_Y, "visible");
      case "overflow-y-scroll" -> nameValue(OVERFLOW_Y, "scroll");

      // Pointer Events
      case "pointer-events-auto" -> nameValue(POINTER_EVENTS, "auto");
      case "pointer-events-none" -> nameValue(POINTER_EVENTS, "none");

      // Position
      case "static" -> nameValue(POSITION, "static");
      case "fixed" -> nameValue(POSITION, "fixed");
      case "absolute" -> nameValue(POSITION, "absolute");
      case "relative" -> nameValue(POSITION, "relative");
      case "sticky" -> nameValue(POSITION, "sticky");

      // Text Align
      case "text-left" -> nameValue(TEXT_ALIGN, "left");
      case "text-center" -> nameValue(TEXT_ALIGN, "center");
      case "text-right" -> nameValue(TEXT_ALIGN, "right");
      case "text-justify" -> nameValue(TEXT_ALIGN, "justify");
      case "text-start" -> nameValue(TEXT_ALIGN, "start");
      case "text-end" -> nameValue(TEXT_ALIGN, "end");

      // Text Decoration
      case "underline" -> nameValue(TEXT_DECORATION, "underline");
      case "overline" -> nameValue(TEXT_DECORATION, "overline");
      case "line-through" -> nameValue(TEXT_DECORATION, "line-through");
      case "no-underline" -> nameValue(TEXT_DECORATION, "none");

      // User Select
      case "select-none" -> nameValue(USER_SELECT, "none");
      case "select-text" -> nameValue(USER_SELECT, "text");
      case "select-all" -> nameValue(USER_SELECT, "all");
      case "select-auto" -> nameValue(USER_SELECT, "auto");

      // VerticalAlign
      case "align-baseline" -> nameValue(VERTICAL_ALIGN, "baseline");
      case "align-top" -> nameValue(VERTICAL_ALIGN, "top");
      case "align-middle" -> nameValue(VERTICAL_ALIGN, "middle");
      case "align-bottom" -> nameValue(VERTICAL_ALIGN, "bottom");
      case "align-text-top" -> nameValue(VERTICAL_ALIGN, "text-top");
      case "align-text-bottom" -> nameValue(VERTICAL_ALIGN, "text-bottom");
      case "align-sub" -> nameValue(VERTICAL_ALIGN, "sub");
      case "align-super" -> nameValue(VERTICAL_ALIGN, "super");

      // Others
      default -> prefixWord1(value);
    };
  }

  private Rule nameValue(Utility utility, String value) {
    String formatted;
    formatted = format(value);

    return utility.get(className, variants, formatted);
  }

  /**
   * Prefixes that are 1 word
   */
  private Rule prefixWord1(String value) {
    negative = false;

    int prefixStart = 0;

    String prefix, suffix;

    int dashIndex;
    dashIndex = value.indexOf('-');

    if (dashIndex == 0) {
      negative = true;

      prefixStart = 1;

      dashIndex = value.indexOf('-', 1);
    }

    switch (dashIndex) {
      case 0 -> {
        return Rule.NOOP;
      }

      case -1 -> {
        prefix = value;

        suffix = "";
      }

      default -> {
        prefix = value.substring(prefixStart, dashIndex);

        suffix = value.substring(dashIndex + 1);
      }
    }

    return switch (prefix) {
      // B
      case "bg" -> config(BACKGROUND_COLOR, config.colors(), suffix);
      case "border" -> {
        if (suffix.startsWith("spacing-")) {
          suffix = suffix.substring("spacing-".length());

          yield borderSpacing(suffix);
        } else {
          yield border(suffix);
        }
      }
      case "bottom" -> config(BOTTOM, config.inset(), suffix);

      // C
      case "content" -> config(CONTENT, config.content(), suffix);
      case "cursor" -> config(CURSOR, config.cursor(), suffix);

      // D
      case "duration" -> config(TRANSITION_DURATION, config.transitionDuration(), suffix);

      // E
      case "end" -> config(END, config.inset(), suffix);

      // F
      case "fill" -> {
        if (suffix.equals("none")) {
          yield FILL.get(className, variants, suffix);
        } else {
          yield config(FILL, config.colors(), suffix);
        }
      }
      case "font" -> font(suffix);

      // G
      case "gap" -> xy(Utility.GAP, Utility.GAP_X, Utility.GAP_Y, config.gap(), suffix);
      case "grow" -> config(FLEX_GROW, config.flexGrow(), suffix);

      // H
      case "h" -> config(HEIGHT, config.height(), suffix);

      // I
      case "inset" -> xy(Utility.INSET, Utility.INSET_X, Utility.INSET_Y, config.inset(), suffix);

      // L
      case "leading" -> config(LINE_HEIGHT, config.lineHeight(), suffix);
      case "left" -> config(LEFT, config.inset(), suffix);

      // M
      case "m" -> config(MARGIN, config.margin(), suffix);
      case "mx" -> config(MARGIN_X, config.margin(), suffix);
      case "my" -> config(MARGIN_Y, config.margin(), suffix);
      case "mt" -> config(MARGIN_TOP, config.margin(), suffix);
      case "mr" -> config(MARGIN_RIGHT, config.margin(), suffix);
      case "mb" -> config(MARGIN_BOTTOM, config.margin(), suffix);
      case "ml" -> config(MARGIN_LEFT, config.margin(), suffix);
      
      case "max" -> {
        if (suffix.startsWith("w-")) {
          suffix = suffix.substring("w-".length());

          yield config(MAX_WIDTH, config.maxWidth(), suffix);
        } else {
          yield Rule.NOOP;
        }
      }

      // O
      case "opacity" -> config(OPACITY, config.opacity(), suffix);
      case "outline" -> outline(suffix);

      // P
      case "p" -> config(PADDING, config.padding(), suffix);
      case "px" -> config(PADDING_X, config.padding(), suffix);
      case "py" -> config(PADDING_Y, config.padding(), suffix);
      case "pt" -> config(PADDING_TOP, config.padding(), suffix);
      case "pr" -> config(PADDING_RIGHT, config.padding(), suffix);
      case "pb" -> config(PADDING_BOTTOM, config.padding(), suffix);
      case "pl" -> config(PADDING_LEFT, config.padding(), suffix);

      // R
      case "right" -> config(RIGHT, config.inset(), suffix);

      // S
      case "start" -> config(START, config.inset(), suffix);

      // T
      case "text" -> text(suffix);
      case "top" -> config(TOP, config.inset(), suffix);
      case "tracking" -> config(LETTER_SPACING, config.letterSpacing(), suffix);
      case "transition" -> config(TRANSITION_PROPERTY, config.transitionProperty(), suffix);

      // W
      case "w" -> config(WIDTH, config.width(), suffix);

      // Z
      case "z" -> config(Z_INDEX, config.zIndex(), suffix);

      default -> Rule.NOOP;
    };
  }

  private Rule config(Utility utility, Map<String, String> map, String suffix) {
    String value;
    value = map.get(suffix);

    if (value != null) {
      String formatted;
      formatted = format(value);

      return utility.get(className, variants, formatted);
    }

    return Rule.NOOP;
  }

  private String format(String value) {
    String result;
    result = value;

    if (negative) {
      result = "-" + value;
    }

    return result;
  }

  private enum Side {

    ALL,

    X,

    Y,

    TOP,

    RIGHT,

    BOTTOM,

    LEFT;

  }

  private Side parseSide(String suffix) {
    intValue = suffix.indexOf('-');

    if (intValue != 1) {
      // dash was not found

      if (suffix.length() != 1) {
        // suffix is in the form '', 'xy', 'xyz', 'abcd', etc...
        return Side.ALL;
      }

      // suffix may be 'x', 'y', 't', etc...
      // we set dash index to 0;
      intValue = 0;
    }

    char first;
    first = suffix.charAt(0);

    return switch (first) {
      case 'x' -> Side.X;

      case 'y' -> Side.Y;

      case 't' -> Side.TOP;

      case 'r' -> Side.RIGHT;

      case 'b' -> Side.BOTTOM;

      case 'l' -> Side.LEFT;

      default -> Side.ALL;
    };
  }

  private String parseSideSuffix(Side side, String suffix) {
    return switch (side) {
      case ALL -> suffix;

      default -> suffix.substring(intValue + 1);
    };
  }

  private Rule border(String suffix) {
    Side side;
    side = parseSide(suffix);

    String sideSuffix;
    sideSuffix = parseSideSuffix(side, suffix);

    Map<String, String> colors;
    colors = config.colors();

    String color;
    color = colors.get(sideSuffix);

    if (color != null) {
      Utility utility;
      utility = switch (side) {
        case ALL -> Utility.BORDER_COLOR;
        case X -> Utility.BORDER_COLOR_X;
        case Y -> Utility.BORDER_COLOR_Y;
        case TOP -> Utility.BORDER_COLOR_TOP;
        case RIGHT -> Utility.BORDER_COLOR_RIGHT;
        case BOTTOM -> Utility.BORDER_COLOR_BOTTOM;
        case LEFT -> Utility.BORDER_COLOR_LEFT;
      };

      return utility.get(className, variants, color);
    }

    Map<String, String> borderWidth;
    borderWidth = config.borderWidth();

    String borderWidthValue;
    borderWidthValue = borderWidth.get(sideSuffix);

    if (borderWidthValue != null) {
      Utility utility;
      utility = switch (side) {
        case ALL -> Utility.BORDER_WIDTH;
        case X -> Utility.BORDER_WIDTH_X;
        case Y -> Utility.BORDER_WIDTH_Y;
        case TOP -> Utility.BORDER_WIDTH_TOP;
        case RIGHT -> Utility.BORDER_WIDTH_RIGHT;
        case BOTTOM -> Utility.BORDER_WIDTH_BOTTOM;
        case LEFT -> Utility.BORDER_WIDTH_LEFT;
      };

      return utility.get(className, variants, borderWidthValue);
    }

    return Rule.NOOP;
  }

  private Rule borderSpacing(String suffix) {
    if (suffix.length() == 0) {
      return Rule.NOOP;
    }

    String prefix, value;

    int dashIndex;
    dashIndex = suffix.indexOf('-');

    if (dashIndex < 0) {
      prefix = "";

      value = suffix;
    } else {
      prefix = suffix.substring(0, dashIndex);

      value = suffix.substring(dashIndex + 1);
    }

    Map<String, String> borderSpacing;
    borderSpacing = config.borderSpacing();

    String spacing;
    spacing = borderSpacing.get(value);

    if (spacing == null) {
      return Rule.NOOP;
    }

    return switch (prefix) {
      case "" -> Utility.BORDER_SPACING.get(className, variants, spacing + " " + spacing);

      case "x" -> Utility.BORDER_SPACING.get(className, variants, spacing + " 0");

      case "y" -> Utility.BORDER_SPACING.get(className, variants, "0 " + spacing);

      default -> Rule.NOOP;
    };
  }

  private Rule font(String suffix) {
    Map<String, String> fontWeight;
    fontWeight = config.fontWeight();

    String weight;
    weight = fontWeight.get(suffix);

    if (weight != null) {
      return nameValue(Utility.FONT_WEIGHT, weight);
    }

    return Rule.NOOP;
  }

  private Rule outline(String suffix) {
    switch (suffix) {
      case "none":
        return Utility.OUTLINE_STYLE_NONE.get(className, variants, "outline: 2px solid transparent; outline-offset: 2px");
      case "":
        return nameValue(OUTLINE_STYLE, "solid");
      case "dashed":
      case "dotted":
      case "double":
        return nameValue(OUTLINE_STYLE, suffix);
    }

    Map<String, String> outlineWidth;
    outlineWidth = config.outlineWidth();

    String width;
    width = outlineWidth.get(suffix);

    if (width != null) {
      return nameValue(OUTLINE_WIDTH, width);
    }

    Map<String, String> colors;
    colors = config.colors();

    String color;
    color = colors.get(suffix);

    if (color != null) {
      return nameValue(OUTLINE_COLOR, color);
    }

    if (suffix.startsWith("offset-")) {
      suffix = suffix.substring("offset-".length());

      Map<String, String> outlineOffset;
      outlineOffset = config.outlineOffset();

      String offset;
      offset = outlineOffset.get(suffix);

      if (offset != null) {
        return nameValue(OUTLINE_OFFSET, offset);
      }
    }

    return Rule.NOOP;
  }

  private Rule text(String suffix) {
    Map<String, String> fontSize;
    fontSize = config.fontSize();

    String fontSizeValue;
    fontSizeValue = fontSize.get(suffix);

    if (fontSizeValue != null) {
      return fontSize(fontSizeValue);
    }

    Map<String, String> colors;
    colors = config.colors();

    String colorValue;
    colorValue = colors.get(suffix);

    if (colorValue != null) {
      return TEXT_COLOR.get(className, variants, colorValue);
    }

    return Rule.NOOP;
  }

  private Rule fontSize(String value) {
    int slash;
    slash = value.indexOf('/');

    if (slash >= 0) {
      String fontSize;
      fontSize = value.substring(0, slash);

      String lineHeight;
      lineHeight = value.substring(slash + 1);

      return FONT_SIZE2.get(className, variants, fontSize, lineHeight);
    }

    int semicolon;
    semicolon = value.indexOf(';');

    if (semicolon >= 0) {
      return FONT_SIZEX.get(className, variants, value);
    }

    return FONT_SIZE1.get(className, variants, value);
  }
  
  private Rule xy(Utility util, Utility utilX, Utility utilY, Map<String, String> values, String suffix) {
    int dash;
    dash = suffix.indexOf('-');

    if (dash < 0) {
      return config(util, values, suffix);
    }

    if (dash == 1) {
      char first;
      first = suffix.charAt(0);

      return switch (first) {
        case 'x' -> config(utilX, values, suffix.substring(dash + 1));

        case 'y' -> config(utilY, values, suffix.substring(dash + 1));

        default -> Rule.NOOP;
      };
    }

    return Rule.NOOP;
  }

}