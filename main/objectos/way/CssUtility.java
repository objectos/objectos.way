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
    StringBuilder out;
    out = new StringBuilder();

    writeTo(out, CssIndentation.ROOT);

    return out.toString();
  }

  @Override
  public final void writeTo(StringBuilder out, CssIndentation indentation) {
    indentation.writeTo(out);

    modifier.writeClassName(out, className);

    switch (properties.size()) {
      case 0 -> out.append(" {}");

      case 1 -> {
        Entry<String, String> prop;
        prop = properties.get(0);

        writeBlockOne(out, prop);
      }

      case 2 -> {
        Entry<String, String> first;
        first = properties.get(0);

        Entry<String, String> second;
        second = properties.get(1);

        writeBlockTwo(out, first, second);
      }

      default -> {
        writeBlockMany(out, indentation, properties);
      }
    }

    out.append(System.lineSeparator());
  }

  @Override
  public final void writeProps(StringBuilder out, CssIndentation indentation) {
    for (Map.Entry<String, String> property : properties) {
      propertyMany(out, indentation, property);
    }
  }

  private void writeBlockOne(StringBuilder out, Entry<String, String> property) {
    blockStart(out);

    property(out, property);

    blockEnd(out);
  }

  private void writeBlockTwo(StringBuilder out, Entry<String, String> prop1, Entry<String, String> prop2) {
    blockStart(out);

    property(out, prop1);

    nextProperty(out);

    property(out, prop2);

    blockEnd(out);
  }

  private void writeBlockMany(
      StringBuilder out, CssIndentation indentation, CssProperties properties) {
    blockStartMany(out);

    CssIndentation next;
    next = indentation.increase();

    for (Map.Entry<String, String> property : properties) {
      propertyMany(out, next, property);
    }

    blockEndMany(out, indentation);
  }

  private void blockStart(StringBuilder out) {
    out.append(" { ");
  }

  private void blockStartMany(StringBuilder out) {
    out.append(" {");
    out.append(System.lineSeparator());
  }

  private void blockEnd(StringBuilder out) {
    out.append(" }");
  }

  private void blockEndMany(StringBuilder out, CssIndentation indentation) {
    indentation.writeTo(out);

    out.append('}');
  }

  private void nextProperty(StringBuilder out) {
    out.append("; ");
  }

  private void property(StringBuilder out, Entry<String, String> property) {
    String name;
    name = property.getKey();

    out.append(name);

    out.append(": ");

    String value;
    value = property.getValue();

    out.append(value);
  }

  private void propertyMany(StringBuilder out, CssIndentation indentation, Entry<String, String> property) {
    indentation.writeTo(out);

    property(out, property);

    out.append(';');
    out.append(System.lineSeparator());
  }

}