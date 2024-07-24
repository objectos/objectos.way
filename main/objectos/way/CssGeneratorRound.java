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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import objectos.util.list.GrowableList;
import objectos.util.map.GrowableSequencedMap;
import objectos.way.CssVariant.MediaQuery;

class CssGeneratorRound {

  static class Context {

    private final MediaQuery query;

    private final GrowableList<CssRule> rules = new GrowableList<>();

    private Map<MediaQuery, Context> mediaQueries;

    private Context(MediaQuery query) {
      this.query = query;
    }

    final void add(CssRule rule) {
      rules.add(rule);
    }

    final Context contextOf(MediaQuery child) {
      if (mediaQueries == null) {
        mediaQueries = new TreeMap<>();
      }

      return mediaQueries.computeIfAbsent(child, Context::new);
    }

    final void writeTo(StringBuilder out, CssIndentation indentation) {
      query.writeMediaQueryStart(out, indentation);

      rules.sort(Comparator.naturalOrder());

      CssIndentation blockIndentation;
      blockIndentation = indentation.increase();

      for (CssRule rule : rules) {
        rule.writeTo(out, blockIndentation);
      }

      if (mediaQueries != null) {
        for (Context child : mediaQueries.values()) {
          if (!out.isEmpty()) {
            out.append(System.lineSeparator());
          }

          child.writeTo(out, blockIndentation);
        }
      }

      indentation.writeTo(out);

      out.append('}');

      out.append(System.lineSeparator());
    }

  }

  private final CssConfig config;

  private final Map<String, CssRule> rules = new GrowableSequencedMap<>();

  private GrowableList<CssRule> topLevel;

  private Map<MediaQuery, Context> mediaQueries;

  // for testing only
  CssGeneratorRound() {
    config = null;
  }

  CssGeneratorRound(CssConfig config) {
    this.config = config;
  }

  public final String generate() {
    CssGeneratorScanner scanner;
    scanner = new CssGeneratorScanner(config.noteSink());

    for (var clazz : config.classes()) {
      scanner.scan(clazz, this::split);
    }

    topLevel = new GrowableList<>();

    for (CssRule rule : rules.values()) {
      rule.accept(this);
    }

    StringBuilder out;
    out = new StringBuilder();

    if (!config.skipReset()) {
      out.append(CssReset.preflight());

      out.append(System.lineSeparator());
    }

    CssIndentation indentation;
    indentation = CssIndentation.ROOT;

    Iterable<String> baseLayer;
    baseLayer = config.baseLayer();

    for (var styles : baseLayer) {
      out.append(styles);

      if (!topLevel.isEmpty()) {
        out.append(System.lineSeparator());
      }
    }

    topLevel.sort(Comparator.naturalOrder());

    for (CssRule rule : topLevel) {
      rule.writeTo(out, indentation);
    }

    if (mediaQueries != null) {
      for (Context ctx : mediaQueries.values()) {
        if (!out.isEmpty()) {
          out.append(System.lineSeparator());
        }

        ctx.writeTo(out, indentation);
      }
    }

    return out.toString();
  }

  // visible for testing
  final void split(String s) {
    int beginIndex;
    beginIndex = 0;

    int endIndex;
    endIndex = s.indexOf(' ', beginIndex);

    while (endIndex >= 0) {
      if (beginIndex < endIndex) {
        String candidate;
        candidate = s.substring(beginIndex, endIndex);

        onSplit(candidate);
      }

      beginIndex = endIndex + 1;

      endIndex = s.indexOf(' ', beginIndex);
    }

    if (beginIndex == 0) {
      onSplit(s);
    }

    else if (beginIndex < s.length() - 1) {
      onSplit(s.substring(beginIndex));
    }
  }

  void onSplit(String s) {
    CssRule existing;
    existing = rules.get(s);

    if (existing == null) {
      CssRule newRule;
      newRule = onCacheMiss(s);

      rules.put(s, newRule);
    }

    else {
      onCacheHit(existing);
    }
  }

  void onCacheHit(CssRule existing) {
    // for testing
  }

  private final GrowableList<CssVariant> variantsBuilder = new GrowableList<>();

  CssRule onCacheMiss(String className) {
    int beginIndex;
    beginIndex = 0;

    int colon;
    colon = className.indexOf(':', beginIndex);

    while (colon > 0) {
      String variantName;
      variantName = className.substring(beginIndex, colon);

      CssVariant variant;
      variant = getVariant(variantName);

      if (variant == null) {
        return CssRule.NOOP;
      }

      variantsBuilder.add(variant);

      beginIndex = colon + 1;

      colon = className.indexOf(':', beginIndex);
    }

    List<CssVariant> variants;
    variants = variantsBuilder.toUnmodifiableList(Comparator.naturalOrder());

    variantsBuilder.clear();

    String value;
    value = className;

    if (beginIndex > 0) {
      value = className.substring(beginIndex);
    }

    return onVariants(className, variants, value);
  }

  CssRule onVariants(String className, List<CssVariant> variants, String value) {
    // 1) static values search
    CssStaticUtility staticFactory;
    staticFactory = config.getStatic(value);

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
      CssResolver resolver;
      resolver = config.getResolver(candidate);

      CssRule rule;
      rule = resolver.resolve(className, variants, negative, suffix);

      if (rule != null) {
        return rule;
      }
    }

    return CssRule.NOOP;
  }

  final Context contextOf(MediaQuery query) {
    if (mediaQueries == null) {
      mediaQueries = new TreeMap<>();
    }

    return mediaQueries.computeIfAbsent(query, Context::new);
  }

  final void topLevel(CssRule rule) {
    topLevel.add(rule);
  }

  // testing

  final CssRule getRule(String className) {
    return rules.get(className);
  }

  CssVariant getVariant(String name) {
    return config.getVariant(name);
  }

  final void putRule(String className, CssRule rule) {
    rules.put(className, rule);
  }

}