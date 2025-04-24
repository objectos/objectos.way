/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

final class CssUtility implements Css.Rule {

  private final Css.Key key;

  private final String className;

  private final CssModifier modifier;

  private final CssProperties properties;

  public CssUtility(Css.Key key, String className, CssModifier modifier, CssProperties.Builder properties) {
    this(key, className, modifier, properties.build());
  }

  public CssUtility(Css.Key key, String className, CssModifier modifier, CssProperties properties) {
    this.key = key;

    this.className = className;

    this.modifier = modifier;

    this.properties = properties;
  }

  @Override
  public final void accept(CssEngineContext ctx) {
    CssEngineContext context;
    context = ctx.contextOf(modifier);

    context.add(this);
  }

  @Override
  public final int compareSameKind(Css.Rule other) {
    CssUtility o;
    o = (CssUtility) other;

    int result;
    result = key.compareTo(o.key);

    if (result != 0) {
      return result;
    }

    return modifier.compareTo(o.modifier);
  }

  @Override
  public final int kind() {
    return 2;
  }

  @Override
  public String toString() {
    try {
      final StringBuilder out;
      out = new StringBuilder();

      final CssWriter w;
      w = new CssWriter(out);

      writeTo(w, 0);

      return out.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  @Override
  public final void writeTo(CssWriter w, int level) throws IOException {
    w.indent(level);

    final StringBuilder sb;
    sb = w.stringBuilder();

    modifier.writeClassName(sb, className);

    w.write(sb);

    switch (properties.size()) {
      case 0 -> w.write(" {}");

      case 1 -> {
        Entry<String, String> prop;
        prop = properties.get(0);

        writeBlockOne(w, prop);
      }

      case 2 -> {
        Entry<String, String> first;
        first = properties.get(0);

        Entry<String, String> second;
        second = properties.get(1);

        writeBlockTwo(w, first, second);
      }

      default -> {
        writeBlockMany(w, level, properties);
      }
    }

    w.writeln();
  }

  @Override
  public final void writeProps(CssWriter w, int level) throws IOException {
    for (Map.Entry<String, String> property : properties) {
      propertyMany(w, level, property);
    }
  }

  private void writeBlockOne(CssWriter w, Map.Entry<String, String> property) throws IOException {
    blockStart(w);

    property(w, property);

    blockEnd(w);
  }

  private void writeBlockTwo(CssWriter w, Map.Entry<String, String> prop1, Map.Entry<String, String> prop2) throws IOException {
    blockStart(w);

    property(w, prop1);

    nextProperty(w);

    property(w, prop2);

    blockEnd(w);
  }

  private void writeBlockMany(CssWriter w, int level, CssProperties properties) throws IOException {
    blockStartMany(w);

    final int next;
    next = level + 1;

    for (Map.Entry<String, String> property : properties) {
      propertyMany(w, next, property);
    }

    blockEndMany(w, level);
  }

  private void blockStart(CssWriter w) throws IOException {
    w.write(" { ");
  }

  private void blockStartMany(CssWriter w) throws IOException {
    w.writeln(" {");
  }

  private void blockEnd(CssWriter w) throws IOException {
    w.write(" }");
  }

  private void blockEndMany(CssWriter w, int level) throws IOException {
    w.indent(level);

    w.write('}');
  }

  private void nextProperty(CssWriter w) throws IOException {
    w.write("; ");
  }

  private void property(CssWriter w, Map.Entry<String, String> property) throws IOException {
    String name;
    name = property.getKey();

    w.write(name);

    w.write(": ");

    String value;
    value = property.getValue();

    w.write(value);
  }

  private void propertyMany(CssWriter w, int level, Entry<String, String> property) throws IOException {
    w.indent(level);

    property(w, property);

    w.writeln(';');
  }

}