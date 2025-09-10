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

import java.util.Iterator;

public class HtmlMarkupOfTestableGen {
  private String methods;

  public static void main(String[] args) {
    System.out.println(new HtmlMarkupOfTestableGen());
  }

  @Override
  public final String toString() {
    prepare();

    return """
    /// Declares the structure of an HTML document using pure Java (testable nodes).
    public sealed static abstract class MarkupOfTestable extends MarkupOfElement {

      MarkupOfTestable() {}
    %s
    }
    """.formatted(methods);
  }

  private void prepare() {
    final StringBuilder methodsBuilder;
    methodsBuilder = new StringBuilder();

    for (HtmlSpec.TestableSpec spec : HtmlSpec.testableNodes()) {
      methodsBuilder.append('\n');

      final String javadocs;
      javadocs = spec.javadocs();

      final Iterator<String> lines;
      lines = javadocs.lines().iterator();

      final String first;
      first = lines.next();

      methodsBuilder.append("  /// ");

      methodsBuilder.append(first.substring(0, first.length() - 1));

      methodsBuilder.append(" (optional operation).");

      while (lines.hasNext()) {
        methodsBuilder.append('\n');

        methodsBuilder.append("  /// ");

        methodsBuilder.append(lines.next());
      }

      methodsBuilder.append('\n');

      methodsBuilder.append("  public abstract ");

      methodsBuilder.append(spec.sig());

      methodsBuilder.append(';');

      methodsBuilder.append('\n');
    }

    methods = methodsBuilder.toString();
  }
}
