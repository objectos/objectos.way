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

import java.util.Map;
import java.util.TreeMap;
import objectos.lang.object.Check;
import objectos.notes.NoteSink;

public class WayStyleGen extends WayStyleGenParser implements StyleGen {

  private StringBuilder out;

  private Map<Variant, StringBuilder> mediaQueries;

  public WayStyleGen() {}

  public final WayStyleGen noteSink(NoteSink noteSink) {
    this.noteSink = Check.notNull(noteSink, "noteSink == null");

    return this;
  }

  @Override
  public final String generate() {
    out = new StringBuilder();

    rules.values().stream()
        .filter(o -> o != Rule.NOOP)
        .sorted()
        .forEach(rule -> rule.accept(this));

    if (mediaQueries != null) {

      for (StringBuilder query : mediaQueries.values()) {
        if (!out.isEmpty()) {
          out.append(System.lineSeparator());
        }

        out.append(query);
        out.append("}");
        out.append(System.lineSeparator());
      }

    }

    return out.toString();
  }

  final StringBuilder mediaQuery(Variant variant) {
    if (mediaQueries == null) {
      mediaQueries = new TreeMap<>();
    }

    StringBuilder out;
    out = mediaQueries.get(variant);

    if (out == null) {
      out = new StringBuilder();

      variant.writeMediaQueryStart(out);

      mediaQueries.put(variant, out);
    }

    return out;
  }

  final StringBuilder topLevel() {
    return out;
  }

}