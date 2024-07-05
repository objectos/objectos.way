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
package objectos.way;

import static objectos.way.CssUtility.ACCESSIBILITY;
import static objectos.way.CssUtility.ALIGN_ITEMS;
import static objectos.way.CssUtility.BACKGROUND_COLOR;
import static objectos.way.CssUtility.BORDER_COLLAPSE;
import static objectos.way.CssUtility.BOTTOM;
import static objectos.way.CssUtility.CONTENT;
import static objectos.way.CssUtility.CURSOR;
import static objectos.way.CssUtility.DISPLAY;
import static objectos.way.CssUtility.END;
import static objectos.way.CssUtility.FILL;
import static objectos.way.CssUtility.FLEX_DIRECTION;
import static objectos.way.CssUtility.FLEX_GROW;
import static objectos.way.CssUtility.FONT_SIZE1;
import static objectos.way.CssUtility.FONT_SIZE2;
import static objectos.way.CssUtility.FONT_SIZE3;
import static objectos.way.CssUtility.FONT_SIZE4;
import static objectos.way.CssUtility.GRID_COLUMN;
import static objectos.way.CssUtility.GRID_COLUMN_END;
import static objectos.way.CssUtility.GRID_COLUMN_START;
import static objectos.way.CssUtility.GRID_TEMPLATE_COLUMNS;
import static objectos.way.CssUtility.GRID_TEMPLATE_ROWS;
import static objectos.way.CssUtility.HEIGHT;
import static objectos.way.CssUtility.JUSTIFY_CONTENT;
import static objectos.way.CssUtility.LEFT;
import static objectos.way.CssUtility.LETTER_SPACING;
import static objectos.way.CssUtility.LINE_HEIGHT;
import static objectos.way.CssUtility.MARGIN;
import static objectos.way.CssUtility.MARGIN_BOTTOM;
import static objectos.way.CssUtility.MARGIN_LEFT;
import static objectos.way.CssUtility.MARGIN_RIGHT;
import static objectos.way.CssUtility.MARGIN_TOP;
import static objectos.way.CssUtility.MARGIN_X;
import static objectos.way.CssUtility.MARGIN_Y;
import static objectos.way.CssUtility.MAX_WIDTH;
import static objectos.way.CssUtility.MIN_WIDTH;
import static objectos.way.CssUtility.OPACITY;
import static objectos.way.CssUtility.OUTLINE_COLOR;
import static objectos.way.CssUtility.OUTLINE_OFFSET;
import static objectos.way.CssUtility.OUTLINE_STYLE;
import static objectos.way.CssUtility.OUTLINE_WIDTH;
import static objectos.way.CssUtility.OVERFLOW;
import static objectos.way.CssUtility.OVERFLOW_X;
import static objectos.way.CssUtility.OVERFLOW_Y;
import static objectos.way.CssUtility.PADDING;
import static objectos.way.CssUtility.PADDING_BOTTOM;
import static objectos.way.CssUtility.PADDING_LEFT;
import static objectos.way.CssUtility.PADDING_RIGHT;
import static objectos.way.CssUtility.PADDING_TOP;
import static objectos.way.CssUtility.PADDING_X;
import static objectos.way.CssUtility.PADDING_Y;
import static objectos.way.CssUtility.POINTER_EVENTS;
import static objectos.way.CssUtility.POSITION;
import static objectos.way.CssUtility.RIGHT;
import static objectos.way.CssUtility.START;
import static objectos.way.CssUtility.STROKE;
import static objectos.way.CssUtility.TABLE_LAYOUT;
import static objectos.way.CssUtility.TEXT_ALIGN;
import static objectos.way.CssUtility.TEXT_COLOR;
import static objectos.way.CssUtility.TEXT_DECORATION;
import static objectos.way.CssUtility.TEXT_WRAP;
import static objectos.way.CssUtility.TOP;
import static objectos.way.CssUtility.TRANSITION_DURATION;
import static objectos.way.CssUtility.TRANSITION_PROPERTY;
import static objectos.way.CssUtility.USER_SELECT;
import static objectos.way.CssUtility.VERTICAL_ALIGN;
import static objectos.way.CssUtility.VISIBILITY;
import static objectos.way.CssUtility.WIDTH;
import static objectos.way.CssUtility.Z_INDEX;

import java.util.List;
import java.util.Map;
import java.util.Set;
import objectos.way.CssVariant.Breakpoint;

abstract class CssGeneratorParser extends CssGeneratorVariants {

  final CssGeneratorConfig config;

  private String className;

  private boolean negative;

  private List<CssVariant> variants;

  CssGeneratorParser(CssGeneratorConfig config) {
    this.config = config;
  }

  abstract Object execute(CssKey key, CssAction action, Object arg);

  @Override
  final CssVariant getVariant(String variantName) {
    return config.getVariant(variantName);
  }

  @Override
  final CssRule onVariants(String className, List<CssVariant> variants, String value) {
    return onVariantsOld(className, variants, value);
  }

  final CssRule onVariantsNew(String className, List<CssVariant> variants, String value) {
    // 1) static values search
    Object staticTableObject;
    staticTableObject = config.get(CssKey._STATIC_TABLE);

    CssStaticTable staticTable;
    staticTable = (CssStaticTable) staticTableObject;

    CssRuleFactory staticFactory;
    staticFactory = staticTable.get(value);

    if (staticFactory != null) {
      return staticFactory.create(className, variants);
    }

    // 2) by prefix search

    char firstChar;
    firstChar = value.charAt(0);

    // are we dealing with a negative value
    boolean negative;
    negative = false;

    if (firstChar == '-') {
      negative = true;

      value = value.substring(1);
    }

    // maybe it is the prefix with an empty value
    // e.g. border-x

    Set<CssKey> candidates;
    candidates = config.getCandidates(value);

    String suffix;
    suffix = "";

    if (candidates == null) {

      int fromIndex;
      fromIndex = value.length();

      while (candidates == null && fromIndex > 0) {
        int lastDash;
        lastDash = value.lastIndexOf('-', fromIndex);

        if (lastDash == 0) {
          // value starts with a dash and has no other dash
          // => invalid value
          break;
        }

        fromIndex = lastDash - 1;

        String prefix;
        prefix = value;

        suffix = "";

        if (lastDash > 0) {
          prefix = value.substring(0, lastDash);

          suffix = value.substring(lastDash + 1);
        }

        candidates = config.getCandidates(prefix);
      }

    }

    if (candidates == null) {
      return CssRule.NOOP;
    }

    for (CssKey candidate : candidates) {
      Object resolverObject;
      resolverObject = config.getOrCompute(candidate, this::createResolver);

      CssRuleResolver resolver;
      resolver = (CssRuleResolver) resolverObject;

      CssRule rule;
      rule = resolver.resolve(className, variants, negative, suffix);

      if (rule != null) {
        return rule;
      }
    }

    return CssRule.NOOP;
  }

  private CssRuleResolver createResolver(CssKey key) {
    return (CssRuleResolver) execute(key, CssAction.RESOLVER, null);
  }

  final CssRule onVariantsOld(String className, List<CssVariant> variants, String value) {
    Map<String, String> utilities;
    utilities = config.utilities();

    String customRule;
    customRule = utilities.get(value);

    if (customRule != null) {
      return CssUtility.CUSTOM.get(className, variants, customRule);
    }

    this.className = className;

    negative = false;

    this.variants = variants;

    // static hash map... (sort of)
    return switch (value) {
      // Accessibility
      case "sr-only" -> ACCESSIBILITY.get(className, variants, """
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
      case "not-sr-only" -> ACCESSIBILITY.get(className, variants, """
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

      // Container
      case "container" -> new CssRule(0, className, variants) {

        final List<Breakpoint> breakpoints = config.breakpoints();

        @Override
        final void writeBlock(StringBuilder out, CssIndentation indentation) {
          out.append(" { width: 100% }");

          CssIndentation next;
          next = indentation.increase();

          for (Breakpoint breakpoint : breakpoints) {
            out.append(System.lineSeparator());

            breakpoint.writeMediaQueryStart(out, indentation);

            next.writeTo(out);

            writeClassName(out);

            out.append(" { max-width: ");

            out.append(breakpoint.value());

            out.append(" }");

            out.append(System.lineSeparator());

            indentation.writeTo(out);

            out.append("}");
          }
        }

      };

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

      // Table Layout
      case "table-auto" -> nameValue(TABLE_LAYOUT, "auto");
      case "table-fixed" -> nameValue(TABLE_LAYOUT, "fixed");

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

      // Text Wrap
      case "text-wrap" -> nameValue(TEXT_WRAP, "wrap");
      case "text-nowrap" -> nameValue(TEXT_WRAP, "nowrap");
      case "text-balance" -> nameValue(TEXT_WRAP, "balance");
      case "text-pretty" -> nameValue(TEXT_WRAP, "pretty");

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

      // Visibility
      case "visible" -> nameValue(VISIBILITY, "visible");
      case "invisible" -> nameValue(VISIBILITY, "hidden");
      case "collapse" -> nameValue(VISIBILITY, "collapse");

      // Others
      default -> prefixWord1(value);
    };
  }

  private CssRule nameValue(CssUtility utility, String value) {
    String formatted;
    formatted = format(value);

    return utility.get(className, variants, formatted);
  }

  private CssRule prefixWord1(String value) {
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
        return CssRule.NOOP;
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
      case "col" -> gridColumn(suffix);
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
      case "gap" -> xy(CssUtility.GAP, CssUtility.GAP_X, CssUtility.GAP_Y, config.gap(), suffix);
      case "grid" -> grid(suffix);
      case "grow" -> config(FLEX_GROW, config.flexGrow(), suffix);

      // H
      case "h" -> config(HEIGHT, config.height(), suffix);

      // I
      case "inset" -> xy(CssUtility.INSET, CssUtility.INSET_X, CssUtility.INSET_Y, config.inset(), suffix);

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
          yield CssRule.NOOP;
        }
      }
      case "min" -> {
        if (suffix.startsWith("w-")) {
          suffix = suffix.substring("w-".length());

          yield config(MIN_WIDTH, config.minWidth(), suffix);
        } else {
          yield CssRule.NOOP;
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
      case "rounded" -> borderRadius(suffix);

      // S
      case "size" -> config(CssUtility.SIZE, config.size(), suffix);
      case "start" -> config(START, config.inset(), suffix);
      case "stroke" -> stroke(suffix);

      // T
      case "text" -> text(suffix);
      case "top" -> config(TOP, config.inset(), suffix);
      case "tracking" -> config(LETTER_SPACING, config.letterSpacing(), suffix);
      case "transition" -> config(TRANSITION_PROPERTY, config.transitionProperty(), suffix);

      // W
      case "w" -> config(WIDTH, config.width(), suffix);

      // Z
      case "z" -> config(Z_INDEX, config.zIndex(), suffix);

      default -> CssRule.NOOP;
    };
  }

  private CssRule config(CssUtility utility, Map<String, String> map, String suffix) {
    String value;
    value = map.get(suffix);

    if (value != null) {
      String formatted;
      formatted = format(value);

      return utility.get(className, variants, formatted);
    }

    return CssRule.NOOP;
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

  private CssRule border(String suffix) {
    Side side;
    side = parseSide(suffix);

    String sideSuffix;
    sideSuffix = parseSideSuffix(side, suffix);

    Map<String, String> colors;
    colors = config.colors();

    String color;
    color = colors.get(sideSuffix);

    if (color != null) {
      CssUtility utility;
      utility = switch (side) {
        case ALL -> CssUtility.BORDER_COLOR;
        case X -> CssUtility.BORDER_COLOR_X;
        case Y -> CssUtility.BORDER_COLOR_Y;
        case TOP -> CssUtility.BORDER_COLOR_TOP;
        case RIGHT -> CssUtility.BORDER_COLOR_RIGHT;
        case BOTTOM -> CssUtility.BORDER_COLOR_BOTTOM;
        case LEFT -> CssUtility.BORDER_COLOR_LEFT;
      };

      return utility.get(className, variants, color);
    }

    Map<String, String> borderWidth;
    borderWidth = config.borderWidth();

    String borderWidthValue;
    borderWidthValue = borderWidth.get(sideSuffix);

    if (borderWidthValue != null) {
      CssUtility utility;
      utility = switch (side) {
        case ALL -> CssUtility.BORDER_WIDTH;
        case X -> CssUtility.BORDER_WIDTH_X;
        case Y -> CssUtility.BORDER_WIDTH_Y;
        case TOP -> CssUtility.BORDER_WIDTH_TOP;
        case RIGHT -> CssUtility.BORDER_WIDTH_RIGHT;
        case BOTTOM -> CssUtility.BORDER_WIDTH_BOTTOM;
        case LEFT -> CssUtility.BORDER_WIDTH_LEFT;
      };

      return utility.get(className, variants, borderWidthValue);
    }

    return CssRule.NOOP;
  }

  private static final Map<String, CssUtility> BORDER_RADIUS_PREFIXES = Map.ofEntries(
      Map.entry("s", CssUtility.BORDER_RADIUS_S),
      Map.entry("e", CssUtility.BORDER_RADIUS_E),
      Map.entry("t", CssUtility.BORDER_RADIUS_T),
      Map.entry("r", CssUtility.BORDER_RADIUS_R),
      Map.entry("b", CssUtility.BORDER_RADIUS_B),
      Map.entry("l", CssUtility.BORDER_RADIUS_L),
      Map.entry("se", CssUtility.BORDER_RADIUS_SE),
      Map.entry("ee", CssUtility.BORDER_RADIUS_EE),
      Map.entry("es", CssUtility.BORDER_RADIUS_ES),
      Map.entry("tl", CssUtility.BORDER_RADIUS_TL),
      Map.entry("tr", CssUtility.BORDER_RADIUS_TR),
      Map.entry("br", CssUtility.BORDER_RADIUS_BR),
      Map.entry("bl", CssUtility.BORDER_RADIUS_BL)
  );

  private CssRule borderRadius(String suffix) {
    String prefix, value;

    int dashIndex;
    dashIndex = suffix.indexOf('-');

    if (dashIndex < 0) {
      if (BORDER_RADIUS_PREFIXES.containsKey(suffix)) {
        prefix = suffix;

        value = "";
      } else {
        prefix = "";

        value = suffix;
      }
    } else {
      prefix = suffix.substring(0, dashIndex);

      value = suffix.substring(dashIndex + 1);
    }

    Map<String, String> borderRadius;
    borderRadius = config.borderRadius();

    String radius;
    radius = borderRadius.get(value);

    if (radius == null) {
      return CssRule.NOOP;
    }

    CssUtility utility = switch (prefix) {
      case "" -> CssUtility.BORDER_RADIUS;

      default -> BORDER_RADIUS_PREFIXES.get(prefix);
    };

    return utility != null
        ? utility.get(className, variants, radius)
        : CssRule.NOOP;
  }

  private CssRule borderSpacing(String suffix) {
    if (suffix.length() == 0) {
      return CssRule.NOOP;
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
      return CssRule.NOOP;
    }

    return switch (prefix) {
      case "" -> CssUtility.BORDER_SPACING.get(className, variants, spacing + " " + spacing);

      case "x" -> CssUtility.BORDER_SPACING.get(className, variants, spacing + " 0");

      case "y" -> CssUtility.BORDER_SPACING.get(className, variants, "0 " + spacing);

      default -> CssRule.NOOP;
    };
  }

  private CssRule font(String suffix) {
    Map<String, String> fontWeight;
    fontWeight = config.fontWeight();

    String weight;
    weight = fontWeight.get(suffix);

    if (weight != null) {
      return nameValue(CssUtility.FONT_WEIGHT, weight);
    }

    return CssRule.NOOP;
  }

  private CssRule grid(String suffix) {
    int dashIndex;
    dashIndex = suffix.indexOf('-');

    if (dashIndex <= 0) {
      return CssRule.NOOP;
    }

    String prefix;
    prefix = suffix.substring(0, dashIndex);

    suffix = suffix.substring(dashIndex + 1);

    return switch (prefix) {
      case "cols" -> config(GRID_TEMPLATE_COLUMNS, config.gridTemplateColumns(), suffix);

      case "rows" -> config(GRID_TEMPLATE_ROWS, config.gridTemplateRows(), suffix);

      default -> CssRule.NOOP;
    };
  }

  private CssRule gridColumn(String suffix) {
    Map<String, String> gridColumn;
    gridColumn = config.gridColumn();

    String col;
    col = gridColumn.get(suffix);

    if (col != null) {
      return nameValue(GRID_COLUMN, col);
    }

    int dashIndex;
    dashIndex = suffix.indexOf('-');

    if (dashIndex <= 0) {
      return CssRule.NOOP;
    }

    String prefix;
    prefix = suffix.substring(0, dashIndex);

    suffix = suffix.substring(dashIndex + 1);

    return switch (prefix) {
      case "end" -> config(GRID_COLUMN_END, config.gridColumnEnd(), suffix);

      case "start" -> config(GRID_COLUMN_START, config.gridColumnStart(), suffix);

      default -> CssRule.NOOP;
    };
  }

  private CssRule outline(String suffix) {
    switch (suffix) {
      case "none":
        return CssUtility.OUTLINE_STYLE_NONE.get(className, variants, "outline: 2px solid transparent; outline-offset: 2px");
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

    return CssRule.NOOP;
  }

  private CssRule stroke(String suffix) {
    Map<String, String> stroke;
    stroke = config.stroke();

    String color;
    color = stroke.get(suffix);

    if (color != null) {
      return nameValue(STROKE, color);
    }

    Map<String, String> strokeWidth;
    strokeWidth = config.strokeWidth();

    String width;
    width = strokeWidth.get(suffix);

    if (width != null) {
      return nameValue(CssUtility.STROKE_WIDTH, width);
    }

    return CssRule.NOOP;
  }

  private CssRule text(String suffix) {
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

    return CssRule.NOOP;
  }

  private CssRule fontSize(String value) {
    int slash;
    slash = value.indexOf('/');

    if (slash < 0) {
      return FONT_SIZE1.get(className, variants, value);
    }

    String fontSize;
    fontSize = value.substring(0, slash);

    String lineHeight;
    lineHeight = value.substring(slash + 1);

    slash = lineHeight.indexOf('/');

    if (slash < 0) {
      return FONT_SIZE2.get(className, variants, fontSize, lineHeight);
    }

    value = lineHeight;

    lineHeight = value.substring(0, slash);

    String letterSpacing;
    letterSpacing = value.substring(slash + 1);

    slash = letterSpacing.indexOf('/');

    if (slash < 0) {
      return FONT_SIZE3.get(className, variants, fontSize, lineHeight, letterSpacing);
    }

    value = letterSpacing;

    letterSpacing = value.substring(0, slash);

    String fontWeight;
    fontWeight = value.substring(slash + 1);

    return FONT_SIZE4.get(className, variants, fontSize, lineHeight, letterSpacing, fontWeight);
  }

  private CssRule xy(CssUtility util, CssUtility utilX, CssUtility utilY, Map<String, String> values, String suffix) {
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

        default -> CssRule.NOOP;
      };
    }

    return CssRule.NOOP;
  }

}