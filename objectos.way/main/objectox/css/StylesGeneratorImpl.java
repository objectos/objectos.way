/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.css;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import objectos.css.util.Display;
import objectos.css.util.StylesGenerator;
import objectos.util.GrowableSet;
import objectox.lang.Check;

public final class StylesGeneratorImpl implements objectos.css.util.StylesGenerator {

  private final Set<String> binaryNames = new GrowableSet<>();

  private Set<Display> display;

  @Override
  public final StylesGenerator add(Class<?> clazz) {
    Check.notNull(clazz, "clazz == null");

    String binaryName;
    binaryName = clazz.getName();

    binaryNames.add(binaryName);

    return this;
  }

  @Override
  public final String generate() throws IOException {
    UtilityRefParser parser;
    parser = new UtilityRefParser();

    for (var name : binaryNames) {
      List<UtilityRef> refs;
      refs = parser.parse(name);

      for (var ref : refs) {
        acceptRef(ref);
      }
    }

    StringBuilder out;
    out = new StringBuilder();

    if (display != null) {
      for (var v : display) {
        out.append("""
        %s {
          display: %s;
        }
        """.formatted(v.className(), v.name().toLowerCase()));
      }
    }

    return out.toString();
  }

  private void acceptRef(UtilityRef ref) {
    String className;
    className = ref.className();

    switch (className) {
      case "objectos.css.util.Display" -> display(ref.constantName());
    }
  }

  private void display(String constantName) {
    if (display == null) {
      display = EnumSet.noneOf(Display.class);
    }

    Display value;
    value = Display.valueOf(constantName);

    display.add(value);
  }

}