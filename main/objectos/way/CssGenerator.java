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
import java.util.SequencedMap;
import java.util.Set;

final class CssGenerator extends CssGeneratorAdapter implements Css.Generator, Css.Repository {

  record Notes(
      Note.Ref2<String, String> keyNotFound,
      Note.Ref3<String, String, Set<Css.Key>> matchNotFound
  ) {

    static Notes get() {
      Class<?> s;
      s = Css.Generator.class;

      return new Notes(
          Note.Ref2.create(s, "Css.Key not found", Note.DEBUG),
          Note.Ref3.create(s, "Match not found", Note.INFO)
      );
    }

  }

  private final CssGeneratorAdapter adapter;

  private final CssConfig config;

  private final Note.Sink noteSink;

  private final Notes notes = Notes.get();

  private final Deque<Css.Repository> repositories = new ArrayDeque<>(4);

  private final SequencedMap<String, Css.Rule> rules = Util.createSequencedMap();

  CssGenerator(CssConfig config) {
    this.adapter = this;

    this.config = config;

    noteSink = config.noteSink();

    init();
  }

  // for testing only
  CssGenerator(CssGeneratorAdapter adapter, CssConfig config) {
    this.adapter = adapter;

    this.config = config;

    noteSink = config.noteSink();

    init();
  }

  private void init() {
    repositories.push(this);
  }

  public final String generate() {
    // 01. scan
    CssGeneratorScanner scanner;
    scanner = new CssGeneratorScanner(config.noteSink());

    for (var clazz : config.classes()) {
      scanner.scan(clazz, adapter);
    }

    for (var directory : config.directories()) {
      scanner.scanDirectory(directory, adapter);
    }

    // 02. process

    Css.Context topLevel;
    topLevel = new CssGeneratorContextOf();

    for (Css.Rule rule : rules.values()) {
      rule.accept(topLevel);
    }

    StringBuilder out;
    out = new StringBuilder();

    // 03. reset

    if (!config.skipReset) {
      out.append(CssReset.preflight());

      out.append(System.lineSeparator());
    }

    // 04. base layer

    Iterable<String> baseLayer;
    baseLayer = config.baseLayer();

    for (var styles : baseLayer) {
      out.append(styles);

      out.append(System.lineSeparator());
    }

    // 05. components + utilities

    Css.Indentation indentation;
    indentation = Css.Indentation.ROOT;

    topLevel.writeTo(out, indentation);

    return out.toString();
  }

  @Override
  final void consumeExisting(String token, Css.Rule existing) {
    Css.Repository repository;
    repository = repositories.peek();

    repository.consumeRule(token, existing);
  }

  @Override
  final Css.Rule createComponent(String className, String definition) {
    // 0) cycle detection
    for (Css.Repository repo : repositories) {
      repo.cycleCheck(className);
    }

    // 1) create component builder
    CssComponent component;
    component = new CssComponent(className);

    // 2) push component
    repositories.push(component);

    // 3) process
    processStringConstant(definition);

    // 4) pop component

    Css.Repository pop;
    pop = repositories.pop();

    assert component == pop;

    // 5) return new component

    return component;
  }

  @Override
  final Css.Rule createUtility(String className, Css.Modifier modifier, String value) {
    // 1) static values search
    Css.StaticUtility staticFactory;
    staticFactory = config.getStatic(value);

    if (staticFactory != null) {
      return staticFactory.create(className, modifier);
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

    Set<Css.Key> candidates;
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
      noteSink.send(notes.keyNotFound, sourceName, className);

      return Css.Rule.NOOP;
    }

    for (Css.Key candidate : candidates) {
      CssResolver resolver;
      resolver = config.getResolver(candidate);

      String resolved;
      resolved = resolver.resolve(suffix);

      if (resolved != null) {
        return resolver.create(className, modifier, negative, resolved);
      }
    }

    CssValueType type;
    type = CssValueType.parse(suffix);

    for (Css.Key candidate : candidates) {
      CssResolver resolver;
      resolver = config.getResolver(candidate);

      String resolved;
      resolved = resolver.resolveWithType(type, suffix);

      if (resolved != null) {
        return resolver.create(className, modifier, negative, resolved);
      }
    }

    noteSink.send(notes.matchNotFound, sourceName, className, candidates);

    return Css.Rule.NOOP;
  }

  // testing

  @Override
  final String getComponent(String className) {
    return config.getComponent(className);
  }

  @Override
  final Css.Rule getRule(String className) {
    return rules.get(className);
  }

  @Override
  final Css.Variant getVariant(String name) {
    return config.getVariant(name);
  }

  @Override
  final void store(String className, Css.Rule rule) {
    Css.Repository repository;
    repository = repositories.peek();

    repository.putRule(className, rule);
  }

  @Override
  public final void cycleCheck(String className) {
    // noop
  }

  @Override
  public final void consumeRule(String className, Css.Rule existing) {
    // noop
  }

  @Override
  public final void putRule(String className, Css.Rule rule) {
    rules.put(className, rule);
  }

}