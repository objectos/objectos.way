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

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

final class SyntaxJavaConfig implements Syntax.Java.Config {

  private static final Set<String> KEYWORDS = Set.of(
      "strictfp",
      "assert",
      "enum",

      "public", "protected", "private",
      "abstract", "static", "final",
      "transient", "volatile", "synchronized",
      "native",

      // Declarations
      "class", "interface", "extends",
      "package", "throws", "implements",

      // Primitive types and void
      "boolean", "byte", "char",
      "short", "int", "long",
      "float", "double",
      "void",

      // Control flow
      "if", "else",
      "try", "catch", "finally",
      "do", "while",
      "for", "continue",
      "switch", "case", "default",
      "break", "throw", "return",

      // Other keywords
      "this", "new", "super",
      "import", "instanceof",

      // Forbidden!
      "goto", "const",

      // literals
      "null", "true", "false",

      // contextual
      "record",
      "var"
  );

  private final Map<SyntaxJavaElement, String> elements = new EnumMap<>(SyntaxJavaElement.class);

  public SyntaxJavaConfig() {}

  @Override
  public final void set(Syntax.Java.Element element, String className) {
    Objects.requireNonNull(element, "element == null");
    Objects.requireNonNull(className, "className == null");

    final SyntaxJavaElement key;
    key = (SyntaxJavaElement) element;

    elements.put(key, className);
  }

  final Syntax.Java build() {
    return new SyntaxJava(this);
  }

  final String get(SyntaxJavaElement element, String defaultValue) {
    return elements.getOrDefault(element, defaultValue);
  }

  final boolean isKeyword(String word) {
    return KEYWORDS.contains(word);
  }

}