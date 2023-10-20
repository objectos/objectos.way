/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package selfgen.css.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import objectos.code.ClassName;
import objectos.code.Code;
import objectos.lang.Check;

public enum Prefix {

  ALL("All"),

  SMALL("Small"),

  MEDIUM("Medium"),

  LARGE("Large"),

  EXTRA("Extra"),

  MAX("Max"),

  HOVER("Hover", SelectorKind.HOVER);

  public static final Set<Prefix> RESPONSIVE = EnumSet.range(ALL, MAX);

  final String simpleName;

  final SelectorKind selector;

  final List<PropertyClass> propertyClassList = new ArrayList<>();

  Prefix(String simpleName) {
    this(simpleName, SelectorKind.STANDARD);
  }

  Prefix(String simpleName, SelectorKind selector) {
    this.simpleName = simpleName;

    this.selector = selector;
  }

  public final void add(PropertyClass property) {
    propertyClassList.add(property);
  }

  final String generate(Code code) {
    return code."""
    public final class \{simpleName} {

      private \{simpleName}() {}

    \{generateProperties(code)}

    }""";
  }

  private String generateProperties(Code code) {
    List<String> result;
    result = new ArrayList<>();

    for (var property : propertyClassList) {
      String s;
      s = property.generate(code, selector);

      s = property.javadoc(code) + "\n" + s;

      s = Code.indent(s, 2);

      result.add(s);
    }

    Stream<String> stream;
    stream = result.stream();

    return stream.collect(Collectors.joining("\n\n"));
  }

}