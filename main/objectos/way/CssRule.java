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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import objectos.way.CssGeneratorRound.Context;
import objectos.way.CssVariant.MediaQuery;

final class CssRule implements Comparable<CssRule> {

  public static final CssRule NOOP = new CssRule(CssKey.$NOOP, "", List.of(), CssProperties.NOOP);

  private final CssKey key;

  private final String className;

  private final List<CssVariant> variants;

  private final CssProperties properties;

  public CssRule(CssKey key, String className, List<CssVariant> variants, CssProperties.Builder properties) {
    this(key, className, variants, properties.build());
  }

  public CssRule(CssKey key, String className, List<CssVariant> variants, CssProperties properties) {
    this.key = key;

    this.className = className;

    this.variants = variants;

    this.properties = properties;
  }

  public final void accept(CssGeneratorRound gen) {
    Context context;
    context = null;

    for (int i = 0, size = variants.size(); i < size; i++) {
      CssVariant variant;
      variant = variants.get(i);

      if (!(variant instanceof MediaQuery query)) {
        break;
      }

      if (context == null) {
        context = gen.contextOf(query);
      } else {
        context = context.contextOf(query);
      }
    }

    if (context != null) {
      context.add(this);
    } else {
      gen.topLevel(this);
    }
  }

  @Override
  public final int compareTo(CssRule o) {
    int result;
    result = key.compareTo(o.key);

    if (result != 0) {
      return result;
    }

    CssVariant thisMax;
    thisMax = max();

    CssVariant thatMax;
    thatMax = o.max();

    if (thisMax == null && thatMax == null) {
      return result;
    }

    if (thisMax == null) {
      return -1;
    }

    if (thatMax == null) {
      return 1;
    }

    return thisMax.compareTo(thatMax);
  }

  private CssVariant max() {
    return switch (variants.size()) {
      case 0 -> null;

      case 1 -> variants.get(0);

      default -> Collections.max(variants);
    };
  }

  @Override
  public String toString() {
    StringBuilder out;
    out = new StringBuilder();

    writeTo(out, CssIndentation.ROOT);

    return out.toString();
  }

  public final void writeTo(StringBuilder out, CssIndentation indentation) {
    indentation.writeTo(out);

    int startIndex;
    startIndex = out.length();

    writeClassName(out);

    for (CssVariant variant : variants) {
      if (variant instanceof CssVariant.ClassNameVariant cnv) {
        cnv.writeClassName(out, startIndex);
      }
    }

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

  private void writeClassName(StringBuilder out) {
    int length;
    length = className.length();

    if (length == 0) {
      return;
    }

    out.append('.');

    int index;
    index = 0;

    boolean escaped;
    escaped = false;

    char first;
    first = className.charAt(index);

    if (0x30 <= first && first <= 0x39) {
      out.append("\\3");
      out.append(first);

      index++;

      escaped = true;
    }

    for (; index < length; index++) {
      char c;
      c = className.charAt(index);

      switch (c) {
        case ' ', ',', '.', '/', ':', '@', '[', ']', '*' -> {
          out.append("\\");

          out.append(c);

          escaped = false;
        }

        case 'a', 'b', 'c', 'd', 'e', 'f',
             'A', 'B', 'C', 'D', 'E', 'F',
             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
          if (escaped) {
            out.append(' ');
          }

          out.append(c);

          escaped = false;
        }

        default -> out.append(c);
      }
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