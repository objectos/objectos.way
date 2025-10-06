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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class CssEngineValue {

  private final Value value;

  private CssEngineValue(Value value) {
    this.value = value;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof CssEngineValue that
        && Objects.equals(value, that.value);
  }

  @Override
  public final String toString() {
    return "CssEngineValue[value=" + value + "]";
  }

  // ##################################################################
  // # BEGIN: Value
  // ##################################################################

  private sealed interface Value {}

  private record CustomProp(String name, String value) implements Value {}

  static CssEngineValue customProp(String name, String value) {
    return new CssEngineValue(
        new CustomProp(name, value)
    );
  }

  private record ThemeSkip(String ns) implements Value {}

  static CssEngineValue themeSkip(String ns) {
    return new CssEngineValue(
        new ThemeSkip(ns)
    );
  }

  private record ThemeVar(String ns, String id, String value) implements Value {}

  static CssEngineValue themeVar(String ns, String id, String value) {
    return new CssEngineValue(
        new ThemeVar(ns, id, value)
    );
  }

  // ##################################################################
  // # END: Value
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse
  // ##################################################################

  public static List<CssEngineValue> parse(String text) {
    final List<CssEngineValue> values;
    values = new ArrayList<>();

    parse(text, values);

    return values;
  }

  public static void parse(String text, List<CssEngineValue> values) {
    final CssEngineValueParser parser;
    parser = new CssEngineValueParser(text, values);

    parser.parse();
  }

  // ##################################################################
  // # END: Parse
  // ##################################################################

}
