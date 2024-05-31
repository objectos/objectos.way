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

import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class ObjectosCssPseudoGen {
  public static void main(String[] args) {
    ObjectosCssPseudoGen gen;
    gen = new ObjectosCssPseudoGen();

    gen.classNameSingleLine(GRID_TEMPLATE_ROWS, "grid-rows-");
    gen.initVariable(GRID_TEMPLATE_ROWS, "gridTemplateRows");
  }

  private static final Map<String, String> SCREENS = seqmap(
      kv("screen-sm", "640px"),
      kv("screen-md", "768px"),
      kv("screen-lg", "1024px"),
      kv("screen-xl", "1280px"),
      kv("screen-2xl", "1536px")
  );

  private static final Map<String, String> SPACING = seqmap(
      kv("px", "1px"),
      kv("0", "0px"),
      kv("0.5", "0.125rem"),
      kv("1", "0.25rem"),
      kv("1.5", "0.375rem"),
      kv("2", "0.5rem"),
      kv("2.5", "0.625rem"),
      kv("3", "0.75rem"),
      kv("3.5", "0.875rem"),
      kv("4", "1rem"),
      kv("5", "1.25rem"),
      kv("6", "1.5rem"),
      kv("7", "1.75rem"),
      kv("8", "2rem"),
      kv("9", "2.25rem"),
      kv("10", "2.5rem"),
      kv("11", "2.75rem"),
      kv("12", "3rem"),
      kv("14", "3.5rem"),
      kv("16", "4rem"),
      kv("20", "5rem"),
      kv("24", "6rem"),
      kv("28", "7rem"),
      kv("32", "8rem"),
      kv("36", "9rem"),
      kv("40", "10rem"),
      kv("44", "11rem"),
      kv("48", "12rem"),
      kv("52", "13rem"),
      kv("56", "14rem"),
      kv("60", "15rem"),
      kv("64", "16rem"),
      kv("72", "18rem"),
      kv("80", "20rem"),
      kv("96", "24rem")
  );

  static final Map<String, String> ALIGN_ITEMS = seqmap(
      kv("items-start", "flex-start"),
      kv("items-end", "flex-end"),
      kv("items-center", "center"),
      kv("items-baseline", "baseline"),
      kv("items-stretch", "stretch")
  );

  static final Map<String, String> BORDER_WIDTH = seqmap(
      kv("", "1px"),
      kv("0", "0px"),
      kv("2", "2px"),
      kv("4", "4px"),
      kv("8", "8px")
  );

  static final Map<String, String> CURSOR = seqmap(
      kv("auto", "auto"),
      kv("default", "default"),
      kv("pointer", "pointer"),
      kv("wait", "wait"),
      kv("text", "text"),
      kv("move", "move"),
      kv("help", "help"),
      kv("not-allowed", "not-allowed"),
      kv("none", "none"),
      kv("context-menu", "context-menu"),
      kv("progress", "progress"),
      kv("cell", "cell"),
      kv("crosshair", "crosshair"),
      kv("vertical-text", "vertical-text"),
      kv("alias", "alias"),
      kv("copy", "copy"),
      kv("no-drop", "no-drop"),
      kv("grab", "grab"),
      kv("grabbing", "grabbing"),
      kv("all-scroll", "all-scroll"),
      kv("col-resize", "col-resize"),
      kv("row-resize", "row-resize"),
      kv("n-resize", "n-resize"),
      kv("e-resize", "e-resize"),
      kv("s-resize", "s-resize"),
      kv("w-resize", "w-resize"),
      kv("ne-resize", "ne-resize"),
      kv("nw-resize", "nw-resize"),
      kv("se-resize", "se-resize"),
      kv("sw-resize", "sw-resize"),
      kv("ew-resize", "ew-resize"),
      kv("ns-resize", "ns-resize"),
      kv("nesw-resize", "nesw-resize"),
      kv("nwse-resize", "nwse-resize"),
      kv("zoom-in", "zoom-in"),
      kv("zoom-out", "zoom-out")
  );

  static final Map<String, String> DISPLAY = seqmap(
      kv("block", "block"),
      kv("inline-block", "inline-block"),
      kv("inline", "inline"),
      kv("flex", "flex"),
      kv("inline-flex", "inline-flex"),
      kv("table", "table"),
      kv("inline-table", "inline-table"),
      kv("table-caption", "table-caption"),
      kv("table-cell", "table-cell"),
      kv("table-column", "table-column"),
      kv("table-column-group", "table-column-group"),
      kv("table-footer-group", "table-footer-group"),
      kv("table-header-group", "table-header-group"),
      kv("table-row-group", "table-row-group"),
      kv("table-row", "table-row"),
      kv("flow-root", "flow-root"),
      kv("grid", "grid"),
      kv("inline-grid", "inline-grid"),
      kv("contents", "contents"),
      kv("list-item", "list-item"),
      kv("hidden", "none")
  );

  static final Map<String, String> FLEX_DIRECTION = seqmap(
      kv("flex-row", "row"),
      kv("flex-row-reverse", "row-reverse"),
      kv("flex-col", "column"),
      kv("flex-col-reverse", "column-reverse")
  );

  static final Map<String, String> FONT_SIZE = seqmap(
      kv("xs", "0.75rem/1rem"),
      kv("sm", "0.875rem/1.25rem"),
      kv("base", "1rem/1.5rem"),
      kv("lg", "1.125rem/1.75rem"),
      kv("xl", "1.25rem/1.75rem"),
      kv("2xl", "1.5rem/2rem"),
      kv("3xl", "1.875rem/2.25rem"),
      kv("4xl", "2.25rem/2.5rem"),
      kv("5xl", "3rem/1"),
      kv("6xl", "3.75rem/1"),
      kv("7xl", "4.5rem/1"),
      kv("8xl", "6rem/1"),
      kv("9xl", "8rem/1")
  );

  static final Map<String, String> FONT_WEIGHT = seqmap(
      kv("thin", "100"),
      kv("extralight", "200"),
      kv("light", "300"),
      kv("normal", "400"),
      kv("medium", "500"),
      kv("semibold", "600"),
      kv("bold", "700"),
      kv("extrabold", "800"),
      kv("black", "900")
  );

  static final Map<String, String> GRID_COLUMN = seqmap(
      kv("auto", "auto"),
      kv("span-1", "span 1 / span 1"),
      kv("span-2", "span 2 / span 2"),
      kv("span-3", "span 3 / span 3"),
      kv("span-4", "span 4 / span 4"),
      kv("span-5", "span 5 / span 5"),
      kv("span-6", "span 6 / span 6"),
      kv("span-7", "span 7 / span 7"),
      kv("span-8", "span 8 / span 8"),
      kv("span-9", "span 9 / span 9"),
      kv("span-10", "span 10 / span 10"),
      kv("span-11", "span 11 / span 11"),
      kv("span-12", "span 12 / span 12"),
      kv("span-full", "1 / -1")
  );

  static final Map<String, String> GRID_COLUMN_END = seqmap(
      kv("auto", "auto"),
      kv("1", "1"),
      kv("2", "2"),
      kv("3", "3"),
      kv("4", "4"),
      kv("5", "5"),
      kv("6", "6"),
      kv("7", "7"),
      kv("8", "8"),
      kv("9", "9"),
      kv("10", "10"),
      kv("11", "11"),
      kv("12", "12"),
      kv("13", "13")
  );

  static final Map<String, String> GRID_COLUMN_START = seqmap(
      kv("auto", "auto"),
      kv("1", "1"),
      kv("2", "2"),
      kv("3", "3"),
      kv("4", "4"),
      kv("5", "5"),
      kv("6", "6"),
      kv("7", "7"),
      kv("8", "8"),
      kv("9", "9"),
      kv("10", "10"),
      kv("11", "11"),
      kv("12", "12"),
      kv("13", "13")
  );

  static final Map<String, String> GRID_TEMPLATE_COLUMNS = seqmap(
      kv("none", "none"),
      kv("subgrid", "subgrid"),
      kv("1", "repeat(1, minmax(0, 1fr))"),
      kv("2", "repeat(2, minmax(0, 1fr))"),
      kv("3", "repeat(3, minmax(0, 1fr))"),
      kv("4", "repeat(4, minmax(0, 1fr))"),
      kv("5", "repeat(5, minmax(0, 1fr))"),
      kv("6", "repeat(6, minmax(0, 1fr))"),
      kv("7", "repeat(7, minmax(0, 1fr))"),
      kv("8", "repeat(8, minmax(0, 1fr))"),
      kv("9", "repeat(9, minmax(0, 1fr))"),
      kv("10", "repeat(10, minmax(0, 1fr))"),
      kv("11", "repeat(11, minmax(0, 1fr))"),
      kv("12", "repeat(12, minmax(0, 1fr))")
  );

  static final Map<String, String> GRID_TEMPLATE_ROWS = seqmap(
      kv("none", "none"),
      kv("subgrid", "subgrid"),
      kv("1", "repeat(1, minmax(0, 1fr))"),
      kv("2", "repeat(2, minmax(0, 1fr))"),
      kv("3", "repeat(3, minmax(0, 1fr))"),
      kv("4", "repeat(4, minmax(0, 1fr))"),
      kv("5", "repeat(5, minmax(0, 1fr))"),
      kv("6", "repeat(6, minmax(0, 1fr))"),
      kv("7", "repeat(7, minmax(0, 1fr))"),
      kv("8", "repeat(8, minmax(0, 1fr))"),
      kv("9", "repeat(9, minmax(0, 1fr))"),
      kv("10", "repeat(10, minmax(0, 1fr))"),
      kv("11", "repeat(11, minmax(0, 1fr))"),
      kv("12", "repeat(12, minmax(0, 1fr))")
  );

  static final Map<String, String> HEIGHT = seqmap(
      kv("auto", "auto"),
      kv("1/2", "50%"),
      kv("1/3", "33.333333%"),
      kv("2/3", "66.666667%"),
      kv("1/4", "25%"),
      kv("2/4", "50%"),
      kv("3/4", "75%"),
      kv("1/5", "20%"),
      kv("2/5", "40%"),
      kv("3/5", "60%"),
      kv("4/5", "80%"),
      kv("1/6", "16.666667%"),
      kv("2/6", "33.333333%"),
      kv("3/6", "50%"),
      kv("4/6", "66.666667%"),
      kv("5/6", "83.333333%"),
      kv("full", "100%"),
      kv("screen", "100vh"),
      kv("svh", "100svh"),
      kv("lvh", "100lvh"),
      kv("dvh", "100dvh"),
      kv("min", "min-content"),
      kv("max", "max-content"),
      kv("fit", "fit-content")
  );

  static final Map<String, String> INSET = seqmap(
      kv("auto", "auto"),
      kv("1/2", "50%"),
      kv("1/3", "33.333333%"),
      kv("2/3", "66.666667%"),
      kv("1/4", "25%"),
      kv("2/4", "50%"),
      kv("3/4", "75%"),
      kv("full", "100%")
  );

  static final Map<String, String> JUSTIFY_CONTENT = seqmap(
      kv("normal", "normal"),
      kv("start", "flex-start"),
      kv("end", "flex-end"),
      kv("center", "center"),
      kv("between", "space-between"),
      kv("around", "space-around"),
      kv("evenly", "space-evenly"),
      kv("stretch", "stretch")
  );

  static final Map<String, String> LETTER_SPACING = seqmap(
      kv("tighter", "-0.05em"),
      kv("tight", "-0.025em"),
      kv("normal", "0em"),
      kv("wide", "0.025em"),
      kv("wider", "0.05em"),
      kv("widest", "0.1em")
  );

  static final Map<String, String> LINE_HEIGHT = seqmap(
      kv("3", "0.75rem"),
      kv("4", "1rem"),
      kv("5", "1.25rem"),
      kv("6", "1.5rem"),
      kv("7", "1.75rem"),
      kv("8", "2rem"),
      kv("9", "2.25rem"),
      kv("10", "2.5rem"),
      kv("none", "1"),
      kv("tight", "1.25"),
      kv("snug", "1.375"),
      kv("normal", "1.5"),
      kv("relaxed", "1.625"),
      kv("loose", "2")
  );

  static final Map<String, String> MARGIN = seqmap(
      kv("auto", "auto"),
      SPACING
  );

  static final Map<String, String> MAX_WIDTH = seqmap(
      SPACING,
      kv("none", "none"),
      kv("xs", "20rem"),
      kv("sm", "24rem"),
      kv("md", "28rem"),
      kv("lg", "32rem"),
      kv("xl", "36rem"),
      kv("2xl", "42rem"),
      kv("3xl", "48rem"),
      kv("4xl", "56rem"),
      kv("5xl", "64rem"),
      kv("6xl", "72rem"),
      kv("7xl", "80rem"),
      kv("full", "100%"),
      kv("min", "min-content"),
      kv("max", "max-content"),
      kv("fit", "fit-content"),
      kv("prose", "65ch"),
      SCREENS
  );

  static final Map<String, String> OPACITY = seqmap(
      kv("0", "0"),
      kv("5", "0.05"),
      kv("10", "0.1"),
      kv("15", "0.15"),
      kv("20", "0.2"),
      kv("25", "0.25"),
      kv("30", "0.3"),
      kv("35", "0.35"),
      kv("40", "0.4"),
      kv("45", "0.45"),
      kv("50", "0.5"),
      kv("55", "0.55"),
      kv("60", "0.6"),
      kv("65", "0.65"),
      kv("70", "0.7"),
      kv("75", "0.75"),
      kv("80", "0.8"),
      kv("85", "0.85"),
      kv("90", "0.9"),
      kv("95", "0.95"),
      kv("100", "1")
  );

  static final Map<String, String> OVERFLOW = seqmap(
      kv("auto", "auto"),
      kv("hidden", "hidden"),
      kv("clip", "clip"),
      kv("visible", "visible"),
      kv("scroll", "scroll")
  );

  static final Map<String, String> POSITION = seqmap(
      kv("static", "static"),
      kv("fixed", "fixed"),
      kv("absolute", "absolute"),
      kv("relative", "relative"),
      kv("sticky", "sticky")
  );

  static final Map<String, String> SIZE = seqmap(
      kv("auto", "auto"),
      kv("1/2", "50%"),
      kv("1/3", "33.333333%"),
      kv("2/3", "66.666667%"),
      kv("1/4", "25%"),
      kv("2/4", "50%"),
      kv("3/4", "75%"),
      kv("1/5", "20%"),
      kv("2/5", "40%"),
      kv("3/5", "60%"),
      kv("4/5", "80%"),
      kv("1/6", "16.666667%"),
      kv("2/6", "33.333333%"),
      kv("3/6", "50%"),
      kv("4/6", "66.666667%"),
      kv("5/6", "83.333333%"),
      kv("1/12", "8.333333%"),
      kv("2/12", "16.666667%"),
      kv("3/12", "25%"),
      kv("4/12", "33.333333%"),
      kv("5/12", "41.666667%"),
      kv("6/12", "50%"),
      kv("7/12", "58.333333%"),
      kv("8/12", "66.666667%"),
      kv("9/12", "75%"),
      kv("10/12", "83.333333%"),
      kv("11/12", "91.666667%"),
      kv("full", "100%"),
      kv("min", "min-content"),
      kv("max", "max-content"),
      kv("fit", "fit-content")
  );

  static final Map<String, String> TEXT_ALIGN = seqmap(
      kv("left", "left"),
      kv("center", "center"),
      kv("right", "right"),
      kv("justify", "justify"),
      kv("start", "start"),
      kv("end", "end")
  );

  static final Map<String, String> TEXT_DECORATION = seqmap(
      kv("underline", "underline"),
      kv("overline", "overline"),
      kv("line-through", "line-through"),
      kv("no-underline", "none")
  );

  static final Map<String, String> TRANSITION_DURATION = seqmap(
      kv("0", "0s"),
      kv("75", "75ms"),
      kv("100", "100ms"),
      kv("150", "150ms"),
      kv("200", "200ms"),
      kv("300", "300ms"),
      kv("500", "500ms"),
      kv("700", "700ms"),
      kv("1000", "1000ms")
  );

  static final Map<String, String> USER_SELECT = seqmap(
      kv("none", "none"),
      kv("text", "text"),
      kv("all", "all"),
      kv("auto", "auto")
  );

  static final Map<String, String> VERTICAL_ALIGN = seqmap(
      kv("baseline", "baseline"),
      kv("top", "top"),
      kv("middle", "middle"),
      kv("bottom", "bottom"),
      kv("text-top", "text-top"),
      kv("text-bottom", "text-bottom"),
      kv("sub", "sub"),
      kv("super", "super")
  );

  static final Map<String, String> WIDTH = seqmap(
      kv("auto", "auto"),
      kv("1/2", "50%"),
      kv("1/3", "33.333333%"),
      kv("2/3", "66.666667%"),
      kv("1/4", "25%"),
      kv("2/4", "50%"),
      kv("3/4", "75%"),
      kv("1/5", "20%"),
      kv("2/5", "40%"),
      kv("3/5", "60%"),
      kv("4/5", "80%"),
      kv("1/6", "16.666667%"),
      kv("2/6", "33.333333%"),
      kv("3/6", "50%"),
      kv("4/6", "66.666667%"),
      kv("5/6", "83.333333%"),
      kv("1/12", "8.333333%"),
      kv("2/12", "16.666667%"),
      kv("3/12", "25%"),
      kv("4/12", "33.333333%"),
      kv("5/12", "41.666667%"),
      kv("6/12", "50%"),
      kv("7/12", "58.333333%"),
      kv("8/12", "66.666667%"),
      kv("9/12", "75%"),
      kv("10/12", "83.333333%"),
      kv("11/12", "91.666667%"),
      kv("full", "100%"),
      kv("screen", "100vw"),
      kv("svw", "100svw"),
      kv("lvw", "100lvw"),
      kv("dvw", "100dvw"),
      kv("min", "min-content"),
      kv("max", "max-content"),
      kv("fit", "fit-content")
  );

  static final Map<String, String> Z_INDEX = seqmap(
      kv("0", "0"),
      kv("10", "10"),
      kv("20", "20"),
      kv("30", "30"),
      kv("40", "40"),
      kv("50", "50"),
      kv("auto", "auto")
  );

  final void cases(Map<String, String> map, String kind, String prefix) {
    for (var entry : map.entrySet()) {
      var key = entry.getKey();

      var value = entry.getValue();

      System.out.println("""
      case "%s%s" -> nameValue(%s, "%s");""".formatted(prefix, key, kind, value));
    }
  }

  final void classNames(Map<String, String> map) {
    for (var key : map.keySet()) {
      System.out.println("className(\"" + key + "\");");
    }
  }

  final void classNames(Map<String, String> map, String prefix) {
    for (var key : map.keySet()) {
      System.out.println("className(\"" + prefix + "-" + key + "\");");
    }
  }

  final void classNamesColors(String prefix) {
    Iterator<String> colors;
    colors = Palette.DEFAULT.keySet().iterator();

    classNamesColors(prefix, colors, 5);

    while (colors.hasNext()) {
      classNamesColors(prefix, colors, 11);
    }
  }

  private void classNamesColors(String prefix, Iterator<String> colors, int count) {
    PrintStream ps;
    ps = System.out;

    ps.append("className(\"");

    int index;
    index = 0;

    if (index++ < count && colors.hasNext()) {
      ps.append(prefix);
      ps.append('-');
      ps.append(colors.next());

      while (index++ < count && colors.hasNext()) {
        ps.append(' ');

        ps.append(prefix);
        ps.append('-');
        ps.append(colors.next());
      }
    }

    ps.println("\");");
  }

  final void classNameSingleLine(Map<String, String> map, String prefix) {
    String names = map.keySet().stream()
        .map(s -> {
          if (s.isEmpty()) {
            return prefix;
          } else {
            return prefix + s;
          }
        })
        .collect(Collectors.joining(" "));

    System.out.println("className(\"" + names + "\");");
  }

  final void factories(Map<String, String> map, String utility) {
    for (var entry : map.entrySet()) {
      var key = entry.getKey();

      var value = entry.getValue();

      System.out.println("""
      factories.put("%s", %s.factory("%s"));""".formatted(key, utility, value));
    }
  }

  final void initVariable(Map<String, String> map, String variable) {
    System.out.println(variable + " = new GrowableMap<>();");

    for (var entry : map.entrySet()) {
      var key = entry.getKey();

      var value = entry.getValue();

      System.out.println("""
      %s.put("%s", "%s");""".formatted(variable, key, value));
    }
  }

  final void mapOf(Map<String, String> map, String variable) {
    System.out.println(variable + " = Map.ofEntries(");

    Set<Entry<String, String>> set;
    set = map.entrySet();

    Iterator<Entry<String, String>> iterator;
    iterator = set.iterator();

    if (iterator.hasNext()) {
      Entry<String, String> e;
      e = iterator.next();

      System.out.print("    Map.entry(\"%s\", \"%s\")".formatted(e.getKey(), e.getValue()));

      while (iterator.hasNext()) {
        e = iterator.next();

        System.out.print(",\n    Map.entry(\"%s\", \"%s\")".formatted(e.getKey(), e.getValue()));
      }
    }

    System.out.println("\n);");
  }

  private static Map.Entry<String, String> kv(String key, String value) {
    return Map.entry(key, value);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static Map<String, String> seqmap(Object... objects) {
    Map<String, String> map;
    map = new LinkedHashMap<>();

    for (var obj : objects) {
      if (obj instanceof Map.Entry<?, ?> entry) {
        String key;
        key = (String) entry.getKey();

        String value;
        value = (String) entry.getValue();

        map.put(key, value);
      }

      else if (obj instanceof Map otherMap) {
        map.putAll(otherMap);
      }

      else {
        throw new UnsupportedOperationException("Implement me");
      }
    }

    return map;
  }

}