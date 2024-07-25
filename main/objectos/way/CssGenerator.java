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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import objectos.util.map.GrowableSequencedMap;

final class CssGenerator extends CssGeneratorAdapter implements Css.Generator, CssRepository {

  private static abstract class ThisContext extends Css.Context {

    Map<Css.MediaQuery, Css.Context> mediaQueries;

    @Override
    public final Css.Context contextOf(Css.Variant variant) {
      if (variant instanceof Css.MediaQuery query) {
        if (mediaQueries == null) {
          mediaQueries = new TreeMap<>();
        }

        return mediaQueries.computeIfAbsent(query, MediaQueryContext::new);
      } else {
        return this;
      }
    }

    final void writeContents(StringBuilder out, CssIndentation indentation) {
      for (CssRule rule : rules) {
        rule.writeTo(out, indentation);
      }

      if (mediaQueries != null) {
        for (Css.Context child : mediaQueries.values()) {
          if (!out.isEmpty()) {
            out.append(System.lineSeparator());
          }

          child.writeTo(out, indentation);
        }
      }
    }

  }

  private static final class TopLevelContext extends ThisContext {

    @Override
    final void write(StringBuilder out, CssIndentation indentation) {
      writeContents(out, indentation);
    }

  }

  private static final class MediaQueryContext extends ThisContext {

    private final Css.MediaQuery query;

    MediaQueryContext(Css.MediaQuery query) {
      this.query = query;
    }

    @Override
    final void write(StringBuilder out, CssIndentation indentation) {
      query.writeMediaQueryStart(out, indentation);

      CssIndentation blockIndentation;
      blockIndentation = indentation.increase();

      writeContents(out, blockIndentation);

      indentation.writeTo(out);

      out.append('}');

      out.append(System.lineSeparator());
    }

  }

  private final CssGeneratorAdapter adapter;

  private final CssConfig config;

  private final Deque<CssRepository> repositories = new ArrayDeque<>(4);

  private final Map<String, CssRule> rules = new GrowableSequencedMap<>();

  CssGenerator(CssConfig config) {
    this.adapter = this;

    this.config = config;

    init();
  }

  // for testing only
  CssGenerator(CssGeneratorAdapter adapter, CssConfig config) {
    this.adapter = adapter;

    this.config = config;

    init();
  }

  private void init() {
    repositories.push(this);
  }

  public final String generate() {
    CssGeneratorScanner scanner;
    scanner = new CssGeneratorScanner(config.noteSink());

    for (var clazz : config.classes()) {
      scanner.scan(clazz, adapter::processRawString);
    }

    Css.Context topLevel;
    topLevel = new TopLevelContext();

    for (CssRule rule : rules.values()) {
      rule.accept(topLevel);
    }

    StringBuilder out;
    out = new StringBuilder();

    if (!config.skipReset()) {
      out.append(CssReset.preflight());

      out.append(System.lineSeparator());
    }

    Iterable<String> baseLayer;
    baseLayer = config.baseLayer();

    for (var styles : baseLayer) {
      out.append(styles);

      out.append(System.lineSeparator());
    }

    CssIndentation indentation;
    indentation = CssIndentation.ROOT;

    topLevel.writeTo(out, indentation);

    return out.toString();
  }

  @Override
  final void consumeExisting(CssRule existing) {
    CssRepository repository;
    repository = repositories.peek();

    repository.consumeRule(existing);
  }

  @Override
  final CssRule createComponent(String className, String definition) {
    // 0) cycle detection
    for (CssRepository repo : repositories) {
      repo.cycleCheck(className);
    }

    // 1) create component builder
    Css.Component component;
    component = new Css.Component(className);

    // 2) push component
    repositories.push(component);

    // 3) process
    processRawString(definition);

    // 4) pop component

    CssRepository pop;
    pop = repositories.pop();

    assert component == pop;

    // 5) return new component

    return component;
  }

  @Override
  final CssRule createUtility(String className, List<Css.Variant> variants, String value) {
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

  // testing

  @Override
  final String getComponent(String className) {
    return config.getComponent(className);
  }

  @Override
  final CssRule getFragment(String className) {
    return rules.get(className);
  }

  @Override
  final Css.Variant getVariant(String name) {
    return config.getVariant(name);
  }

  @Override
  final void store(String className, CssRule rule) {
    CssRepository repository;
    repository = repositories.peek();

    repository.putRule(className, rule);
  }

  @Override
  public final void cycleCheck(String className) {
    // noop
  }

  @Override
  public final void consumeRule(CssRule existing) {
    // noop
  }

  @Override
  public final void putRule(String className, CssRule rule) {
    rules.put(className, rule);
  }

}