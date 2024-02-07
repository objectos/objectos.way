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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectosCssPseudoGen {
  public static void main(String[] args) {
    ObjectosCssPseudoGen gen;
    gen = new ObjectosCssPseudoGen();

    gen.classNameSingleLine(MARGIN, "m-");
    gen.classNameSingleLine(MARGIN, "mx-");
    gen.classNameSingleLine(MARGIN, "my-");
    gen.classNameSingleLine(MARGIN, "mt-");
    gen.classNameSingleLine(MARGIN, "mr-");
    gen.classNameSingleLine(MARGIN, "mb-");
    gen.classNameSingleLine(MARGIN, "ml-");
  }

  private static Map.Entry<String, String> kv(String key, String value) {
    return Map.entry(key, value);
  }

  @SuppressWarnings("unchecked")
  @SafeVarargs
  private static Map<String, String> seqmap(Object... objects) {
    Map<String, String> map;
    map = new LinkedHashMap<>();

    for (var obj : objects) {
      if (obj instanceof Map.Entry entry) {
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

  final void classNames(Map<String, String> map) {
    for (var key : map.keySet()) {
      System.out.println("className(\"" + key + "\");");
    }
  }

  final void classNameSingleLine(Map<String, String> map, String prefix) {
    String names = map.keySet().stream()
        .map(s -> prefix + s)
        .collect(Collectors.joining(" "));

    System.out.println("className(\"" + names + "\");");
  }

  final void classNameSpacing(String prefix) {
    for (var key : SPACING.keySet()) {
      System.out.println("className(\"" + prefix + key + "\");");
    }
  }

  static final Map<String, String> ALIGN_ITEMS = seqmap(
      kv("items-start", "flex-start"),
      kv("items-end", "flex-end"),
      kv("items-center", "center"),
      kv("items-baseline", "baseline"),
      kv("items-stretch", "stretch")
  );

  final void cases(Map<String, String> map, String kind) {
    for (var entry : map.entrySet()) {
      var key = entry.getKey();

      var value = entry.getValue();

      System.out.println("""
      case "%s" -> UtilityKind.%s.nameValue(className, "%s");""".formatted(key, kind, value));
    }
  }

  private static final Map<String, String> HEIGHT = seqmap(
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

  final void heigthCases() {
    for (var entry : HEIGHT.entrySet()) {
      var key = entry.getKey();

      var value = entry.getValue();

      System.out.println("""
      case "%s" -> UtilityKind.HEIGHT.nameValue(className, "%s");""".formatted(key, value));
    }
  }

  private static final Map<String, String> MARGIN = seqmap(
      kv("auto", "auto"),
      SPACING
  );

}