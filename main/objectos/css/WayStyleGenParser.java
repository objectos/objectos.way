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
import static objectos.css.Utility.DISPLAY;
import static objectos.css.Utility.FLEX_DIRECTION;
import static objectos.css.Utility.FONT_SIZE;
import static objectos.css.Utility.HEIGHT;
import static objectos.css.Utility.LETTER_SPACING;
import static objectos.css.Utility.LINE_HEIGHT;
import static objectos.css.Utility.MARGIN;
import static objectos.css.Utility.MARGIN_BOTTOM;
import static objectos.css.Utility.MARGIN_LEFT;
import static objectos.css.Utility.MARGIN_RIGHT;
import static objectos.css.Utility.MARGIN_TOP;
import static objectos.css.Utility.MARGIN_X;
import static objectos.css.Utility.MARGIN_Y;
import static objectos.css.Utility.PADDING;
import static objectos.css.Utility.PADDING_BOTTOM;
import static objectos.css.Utility.PADDING_LEFT;
import static objectos.css.Utility.PADDING_RIGHT;
import static objectos.css.Utility.PADDING_TOP;
import static objectos.css.Utility.PADDING_X;
import static objectos.css.Utility.PADDING_Y;
import static objectos.css.Utility.POSITION;

import java.util.List;
import java.util.Map;

abstract class WayStyleGenParser extends WayStyleGenVariants {

  private final WayStyleGenConfig config;

  private String className;

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

    // static hash map... (sort of)
    return switch (value) {
      // AlignItems
      case "items-start" -> nameValue(ALIGN_ITEMS, "flex-start");
      case "items-end" -> nameValue(ALIGN_ITEMS, "flex-end");
      case "items-center" -> nameValue(ALIGN_ITEMS, "center");
      case "items-baseline" -> nameValue(ALIGN_ITEMS, "baseline");
      case "items-stretch" -> nameValue(ALIGN_ITEMS, "stretch");

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

      // Position
      case "static" -> nameValue(POSITION, "static");
      case "fixed" -> nameValue(POSITION, "fixed");
      case "absolute" -> nameValue(POSITION, "absolute");
      case "relative" -> nameValue(POSITION, "relative");
      case "sticky" -> nameValue(POSITION, "sticky");

      // Others
      default -> prefixWord1(value);
    };
  }

  private Rule nameValue(Utility utility, String value) {
    return utility.get(className, variants, value);
  }

  /**
   * Prefixes that are 1 word
   */
  private Rule prefixWord1(String value) {
    int dashIndex;
    dashIndex = value.indexOf('-');

    if (dashIndex < 1) {
      // the string either:
      // 1) does not have a dash; or
      // 2) immediately start with a dash
      // in any case it is an invalid value

      return Rule.NOOP;
    }

    String prefix;
    prefix = value.substring(0, dashIndex);

    String suffix;
    suffix = value.substring(dashIndex + 1);

    return switch (prefix) {
      case "bg" -> config(BACKGROUND_COLOR, config.colors(), suffix);

      case "h" -> config(HEIGHT, config.height(), suffix);

      case "tracking" -> config(LETTER_SPACING, config.letterSpacing(), suffix);
      case "leading" -> config(LINE_HEIGHT, config.lineHeight(), suffix);

      case "m" -> config(MARGIN, config.margin(), suffix);
      case "mx" -> config(MARGIN_X, config.margin(), suffix);
      case "my" -> config(MARGIN_Y, config.margin(), suffix);
      case "mt" -> config(MARGIN_TOP, config.margin(), suffix);
      case "mr" -> config(MARGIN_RIGHT, config.margin(), suffix);
      case "mb" -> config(MARGIN_BOTTOM, config.margin(), suffix);
      case "ml" -> config(MARGIN_LEFT, config.margin(), suffix);

      case "p" -> config(PADDING, config.padding(), suffix);
      case "px" -> config(PADDING_X, config.padding(), suffix);
      case "py" -> config(PADDING_Y, config.padding(), suffix);
      case "pt" -> config(PADDING_TOP, config.padding(), suffix);
      case "pr" -> config(PADDING_RIGHT, config.padding(), suffix);
      case "pb" -> config(PADDING_BOTTOM, config.padding(), suffix);
      case "pl" -> config(PADDING_LEFT, config.padding(), suffix);

      case "text" -> text(suffix);

      default -> Rule.NOOP;
    };
  }

  private Rule config(Utility utility, Map<String, String> map, String suffix) {
    String value;
    value = map.get(suffix);

    if (value != null) {
      return utility.get(className, variants, value);
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

    return Rule.NOOP;
  }

  private Rule fontSize(String value) {
    int slash;
    slash = value.indexOf('/');

    String fontSize;
    fontSize = value.substring(0, slash);

    String lineHeight;
    lineHeight = value.substring(slash + 1);

    return FONT_SIZE.get(className, variants, fontSize, lineHeight);
  }

}