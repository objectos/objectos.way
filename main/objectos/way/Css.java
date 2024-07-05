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

import java.util.Map;
import java.util.Set;
import objectos.lang.object.Check;
import objectos.notes.NoteSink;
import objectos.util.map.GrowableSequencedMap;

/**
 * The <strong>Objectos CSS</strong> main class.
 */
public final class Css {

  /**
   * Generates a style sheet by scanning Java class files for predefined CSS
   * utility class names.
   */
  public sealed interface Generator permits CssGeneratorSpec {

    /**
     * The set of classes to scan.
     */
    public sealed interface Classes permits CssGeneratorOption {}

    /**
     * A style sheet generation option.
     */
    public sealed interface Option permits CssGeneratorOption {}

  }

  /**
   * A CSS style sheet.
   */
  public sealed interface StyleSheet permits CssStyleSheet {

    default String contentType() {
      return "text/css; charset=utf-8";
    }

    String css();

    byte[] toByteArray();

  }

  // private types

  private Css() {}

  public static String generateCss(Generator.Classes classes, Generator.Option... options) {
    int len;
    len = options.length; // implicit null-check

    CssGenerator config;
    config = new CssGenerator();

    CssGeneratorOption classesOption;
    classesOption = CssGeneratorOption.cast(classes);

    classesOption.acceptCssGenerator(config);

    for (int i = 0; i < len; i++) {
      Generator.Option o;
      o = Check.notNull(options[i], "options[", i, "] == null");

      CssGeneratorOption option;
      option = CssGeneratorOption.cast(o);

      option.acceptCssGenerator(config);
    }

    CssGeneratorSpec spec;
    spec = new CssGeneratorSpec(config);

    return spec.generate();
  }

  public static StyleSheet generateStyleSheet(Generator.Classes classes, Generator.Option... options) {
    String css;
    css = generateCss(classes, options);

    return new CssStyleSheet(css);
  }

  // options

  public static Generator.Classes classes(Class<?>... values) {
    Set<Class<?>> set;
    set = Set.of(values);

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        config.classes(set);
      }
    };
  }

  public static Generator.Option noteSink(NoteSink noteSink) {
    Check.notNull(noteSink, "noteSink == null");

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        config.noteSink(noteSink);
      }
    };
  }

  public static Generator.Option overrideBackgroundColor(String text) {
    CssProperties properties;
    properties = parseProperties(text);

    return new CssGeneratorOption.OverrideKey(CssKey.BACKGROUND_COLOR, properties);
  }

  public static Generator.Option overrideColors(String text) {
    Map<String, String> map;
    map = parseProperties(text).toMap();

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        config.overrideColors(map);
      }
    };
  }

  public static Generator.Option overrideContent(String text) {
    Map<String, String> map;
    map = parseProperties(text).toMap();

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        config.overrideContent(map);
      }
    };
  }

  public static Generator.Option overrideFontSize(String text) {
    Map<String, String> map;
    map = parseProperties(text).toMap();

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        config.overrideFontSize(map);
      }
    };
  }

  public static Generator.Option overrideGridTemplateRows(String text) {
    Map<String, String> map;
    map = parseProperties(text).toMap();

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        config.overrideGridTemplateRows(map);
      }
    };
  }

  public static Generator.Option overrideSpacing(String text) {
    Map<String, String> map;
    map = parseProperties(text).toMap();

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        config.overrideSpacing(map);
      }
    };
  }

  public static Generator.Option rule(String selector, String contents) {
    Check.notNull(selector, "selector == null");
    Check.notNull(contents, "contents == null");

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        config.addRule(selector, contents);
      }
    };
  }

  public static Generator.Option skipReset() {
    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        config.skipReset();
      }
    };
  }

  public static Generator.Option utility(String className, String rule) {
    Check.notNull(className, "className == null");
    Check.notNull(rule, "rule == null");

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        config.addUtility(className, rule);
      }
    };
  }

  public static Generator.Option variants(String text) {
    CssProperties props;
    props = parseProperties(text);

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssGenerator config) {
        for (Map.Entry<String, String> entry : props.entries()) {
          String variantName;
          variantName = entry.getKey();

          String formatString;
          formatString = entry.getValue();

          config.addVariant(variantName, formatString);
        }
      }
    };
  }

  // private stuff

  static CssProperties parseProperties(String text) {
    CssProperties props;
    props = new CssProperties();

    String[] lines;
    lines = text.split("\n");

    for (String line : lines) {
      if (line.isBlank()) {
        continue;
      }

      int colon;
      colon = line.indexOf(':');

      if (colon < 0) {
        throw new IllegalArgumentException(
            "The colon character ':' was not found in the line listed below:\n\n" + line + "\n"
        );
      }

      String key;
      key = line.substring(0, colon);

      String value;
      value = line.substring(colon + 1);

      props.add(key.trim(), value.trim());
    }

    return props;
  }

  private static final int UNSIGNED = 0xFF;

  static int toUnsignedInt(byte b) {
    return b & UNSIGNED;
  }

  static int toBigEndianInt(byte b0, byte b1) {
    int v0;
    v0 = toInt(b0, 8);

    int v1;
    v1 = toInt(b1, 0);

    return v1 | v0;
  }

  static int toBigEndianInt(byte b0, byte b1, byte b2, byte b3) {
    int v0;
    v0 = toInt(b0, 24);

    int v1;
    v1 = toInt(b1, 16);

    int v2;
    v2 = toInt(b2, 8);

    int v3;
    v3 = toInt(b3, 0);

    return v3 | v2 | v1 | v0;
  }

  private static int toInt(byte b, int shift) {
    return toUnsignedInt(b) << shift;
  }

  static Map<String, String> properties(String k1, String v1, String k2, String v2) {
    GrowableSequencedMap<String, String> map;
    map = new GrowableSequencedMap<>();

    map.put(k1, v1);
    map.put(k2, v2);

    return map.toUnmodifiableMap();
  }

}