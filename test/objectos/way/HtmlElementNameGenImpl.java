/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import objectos.way.HtmlSpec.ElementSpec;

public class HtmlElementNameGenImpl {

  private final StringBuilder fields = new StringBuilder();

  private final StringBuilder values = new StringBuilder();

  private int counter;

  public static void main(String[] args) {
    System.out.println(new HtmlElementNameGenImpl());
  }

  @Override
  public final String toString() {
    prepare();

    return """
    %s

      private static final HtmlElementName[] VALUES = {
    %s
      };
    """.formatted(fields, values);
  }

  private void prepare() {
    for (ElementSpec spec : HtmlSpec.elements()) {
      spec(spec);
    }
  }

  private void spec(ElementSpec spec) {
    final int index;
    index = counter++;

    fields.append("""
      static final HtmlElementName %s = new HtmlElementName(%s, %d, "%s");
    """.formatted(spec.javaName(), spec.endTag(), index, spec.htmlName()));

    if (index != 0) {
      values.append(',');
      values.append('\n');
    }

    values.append("    ");

    values.append(spec.javaName());
  }

}
