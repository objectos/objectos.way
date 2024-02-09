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
import static objectos.css.Utility.HEIGHT;
import static objectos.css.Utility.LETTER_SPACING;

import java.util.List;
import java.util.Map;
import objectos.util.list.GrowableList;
import objectos.util.map.GrowableSequencedMap;

class WayStyleGenParser extends WayStyleGenScanner {

  private static final Map<String, String> DEFAULT_SPACING = Map.ofEntries(
      Map.entry("px", "1px"),
      Map.entry("0", "0px"),
      Map.entry("0.5", "0.125rem"),
      Map.entry("1", "0.25rem"),
      Map.entry("1.5", "0.375rem"),
      Map.entry("2", "0.5rem"),
      Map.entry("2.5", "0.625rem"),
      Map.entry("3", "0.75rem"),
      Map.entry("3.5", "0.875rem"),
      Map.entry("4", "1rem"),
      Map.entry("5", "1.25rem"),
      Map.entry("6", "1.5rem"),
      Map.entry("7", "1.75rem"),
      Map.entry("8", "2rem"),
      Map.entry("9", "2.25rem"),
      Map.entry("10", "2.5rem"),
      Map.entry("11", "2.75rem"),
      Map.entry("12", "3rem"),
      Map.entry("14", "3.5rem"),
      Map.entry("16", "4rem"),
      Map.entry("20", "5rem"),
      Map.entry("24", "6rem"),
      Map.entry("28", "7rem"),
      Map.entry("32", "8rem"),
      Map.entry("36", "9rem"),
      Map.entry("40", "10rem"),
      Map.entry("44", "11rem"),
      Map.entry("48", "12rem"),
      Map.entry("52", "13rem"),
      Map.entry("56", "14rem"),
      Map.entry("60", "15rem"),
      Map.entry("64", "16rem"),
      Map.entry("72", "18rem"),
      Map.entry("80", "20rem"),
      Map.entry("96", "24rem")
  );

  private final Map<String, Variant> configuredVariants = Map.of(
      "sm", new Breakpoint(0, "sm", "640px"),
      "md", new Breakpoint(1, "md", "768px"),
      "lg", new Breakpoint(2, "lg", "1024px"),
      "xl", new Breakpoint(3, "xl", "1280px"),
      "2xl", new Breakpoint(4, "xl", "1536px")
  );

  final Map<String, String> letterSpacing = Map.of(
      "tighter", "-0.05em",
      "tight", "-0.025em",
      "normal", "0em",
      "wide", "0.025em",
      "wider", "0.05em",
      "widest", "0.1em"
  );

  final Palette palette = Palette.defaultPalette();

  Map<String, Rule> rules = new GrowableSequencedMap<>();

  private final Map<String, String> spacing = DEFAULT_SPACING;

  private List<Variant> variants;

  private final GrowableList<Variant> variantsBuilder = new GrowableList<>();

  // parsing

  @Override
  final void parse(String s) {
    int beginIndex;
    beginIndex = 0;

    int endIndex;
    endIndex = s.indexOf(' ', beginIndex);

    while (endIndex >= 0) {
      String candidate;
      candidate = s.substring(beginIndex, endIndex);

      process(candidate);

      beginIndex = endIndex + 1;

      endIndex = s.indexOf(' ', beginIndex);
    }

    if (beginIndex == 0) {
      process(s);
    } else {
      process(s.substring(beginIndex));
    }
  }

  private void process(String candidate) {
    if (rules.containsKey(candidate)) {
      return;
    }

    name = candidate;

    Rule rule;
    rule = rule();

    rules.put(candidate, rule);
  }

  private Rule rule() {
    int beginIndex;
    beginIndex = 0;

    int colon;
    colon = name.indexOf(':', beginIndex);

    while (colon > 0) {
      String variantName;
      variantName = name.substring(beginIndex, colon);

      Variant variant;
      variant = configuredVariants.get(variantName);

      if (variant == null) {
        return Rule.NOOP;
      }

      variantsBuilder.add(variant);

      beginIndex = colon + 1;

      colon = name.indexOf(':', beginIndex);
    }

    variants = variantsBuilder.toUnmodifiableList();

    variantsBuilder.clear();

    String value;
    value = name;

    if (beginIndex > 0) {
      value = name.substring(beginIndex);
    }

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

      // Others
      default -> rule0(value);
    };
  }

  private Rule nameValue(Utility utility, String value) {
    return utility.nameValue(variants, name, value);
  }

  private Rule rule0(String value) {
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

    String nextValue = value.substring(dashIndex + 1);

    return switch (prefix) {
      case "bg" -> backgroundColor(nextValue);

      case "h" -> height(nextValue);

      case "m" -> margin(nextValue, Utility.MARGIN);
      case "mx" -> margin(nextValue, Utility.MARGIN_X);
      case "my" -> margin(nextValue, Utility.MARGIN_Y);
      case "mt" -> margin(nextValue, Utility.MARGIN_TOP);
      case "mr" -> margin(nextValue, Utility.MARGIN_RIGHT);
      case "mb" -> margin(nextValue, Utility.MARGIN_BOTTOM);
      case "ml" -> margin(nextValue, Utility.MARGIN_LEFT);

      case "p" -> padding(nextValue, Utility.PADDING);
      case "px" -> padding(nextValue, Utility.PADDING_X);
      case "py" -> padding(nextValue, Utility.PADDING_Y);
      case "pt" -> padding(nextValue, Utility.PADDING_TOP);
      case "pr" -> padding(nextValue, Utility.PADDING_RIGHT);
      case "pb" -> padding(nextValue, Utility.PADDING_BOTTOM);
      case "pl" -> padding(nextValue, Utility.PADDING_LEFT);

      case "tracking" -> letterSpacing(nextValue);

      default -> Rule.NOOP;
    };
  }

  private Rule backgroundColor(String value) {
    String color;
    color = palette.get(value);

    if (color == null) {
      return Rule.NOOP;
    } else {
      return nameValue(BACKGROUND_COLOR, color);
    }
  }

  private Rule height(String value) {
    return switch (value) {
      case "auto" -> nameValue(HEIGHT, "auto");
      case "1/2" -> nameValue(HEIGHT, "50%");
      case "1/3" -> nameValue(HEIGHT, "33.333333%");
      case "2/3" -> nameValue(HEIGHT, "66.666667%");
      case "1/4" -> nameValue(HEIGHT, "25%");
      case "2/4" -> nameValue(HEIGHT, "50%");
      case "3/4" -> nameValue(HEIGHT, "75%");
      case "1/5" -> nameValue(HEIGHT, "20%");
      case "2/5" -> nameValue(HEIGHT, "40%");
      case "3/5" -> nameValue(HEIGHT, "60%");
      case "4/5" -> nameValue(HEIGHT, "80%");
      case "1/6" -> nameValue(HEIGHT, "16.666667%");
      case "2/6" -> nameValue(HEIGHT, "33.333333%");
      case "3/6" -> nameValue(HEIGHT, "50%");
      case "4/6" -> nameValue(HEIGHT, "66.666667%");
      case "5/6" -> nameValue(HEIGHT, "83.333333%");
      case "full" -> nameValue(HEIGHT, "100%");
      case "screen" -> nameValue(HEIGHT, "100vh");
      case "svh" -> nameValue(HEIGHT, "100svh");
      case "lvh" -> nameValue(HEIGHT, "100lvh");
      case "dvh" -> nameValue(HEIGHT, "100dvh");
      case "min" -> nameValue(HEIGHT, "min-content");
      case "max" -> nameValue(HEIGHT, "max-content");
      case "fit" -> nameValue(HEIGHT, "fit-content");
      default -> spacing(name, value, Utility.HEIGHT);
    };
  }

  private Rule letterSpacing(String value) {
    String maybe;
    maybe = letterSpacing.get(value);

    if (maybe != null) {
      return LETTER_SPACING.nameValue(variants, name, maybe);
    } else {
      return Rule.NOOP;
    }
  }

  private Rule margin(String value, Utility utility) {
    return switch (value) {
      case "auto" -> utility.nameValue(variants, name, "auto");
      default -> spacing(name, value, utility);
    };
  }

  private Rule padding(String value, Utility utility) {
    return spacing(name, value, utility);
  }

  private Rule spacing(String className, String value, Utility kind) {
    String maybe;
    maybe = spacing.get(value);

    if (maybe != null) {
      return kind.nameValue(variants, className, maybe);
    } else {
      return Rule.NOOP;
    }
  }

}