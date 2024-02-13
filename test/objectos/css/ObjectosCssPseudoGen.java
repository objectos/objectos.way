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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectosCssPseudoGen {
  public static void main(String[] args) {
    ObjectosCssPseudoGen gen;
    gen = new ObjectosCssPseudoGen();

    gen.classNameSingleLine(BORDER_WIDTH, "border");
    gen.classNameSingleLine(BORDER_WIDTH, "border-x");
    gen.classNameSingleLine(BORDER_WIDTH, "border-y");
    gen.classNameSingleLine(BORDER_WIDTH, "border-t");
    gen.classNameSingleLine(BORDER_WIDTH, "border-r");
    gen.classNameSingleLine(BORDER_WIDTH, "border-b");
    gen.classNameSingleLine(BORDER_WIDTH, "border-l");
  }

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

  static final Map<String, String> POSITION = seqmap(
      kv("static", "static"),
      kv("fixed", "fixed"),
      kv("absolute", "absolute"),
      kv("relative", "relative"),
      kv("sticky", "sticky")
  );

  final void cases(Map<String, String> map, String kind) {
    for (var entry : map.entrySet()) {
      var key = entry.getKey();

      var value = entry.getValue();

      System.out.println("""
      case "%s" -> nameValue(%s, "%s");""".formatted(key, kind, value));
    }
  }

  final void classNames(Map<String, String> map) {
    for (var key : map.keySet()) {
      System.out.println("className(\"" + key + "\");");
    }
  }

  final void classNames(Map<String, String> map, String prefix) {
    for (var key : map.keySet()) {
      System.out.println("className(\"" + prefix + key + "\");");
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
      ps.append(colors.next());

      while (index++ < count && colors.hasNext()) {
        ps.append(' ');

        ps.append(prefix);
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
            return prefix + '-' + s;
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

    return Collections.unmodifiableMap(map);
  }

}