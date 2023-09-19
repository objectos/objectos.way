/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code;

import java.util.function.Function;
import java.util.stream.Collectors;
import objectos.code.internal.Check;
import objectos.code.internal.CodeImpl;
import objectos.code.internal.CodeImportList;

/**
 * A {@linkplain StringTemplate.Processor string template processor} suitable
 * for generating Java source code.
 *
 * <p>
 * In particular, it provides a facility for generating an import declaration
 * list based on all of the {@link ClassName} instances it processes.
 *
 * <p>
 * The following program generates the Java source code for a {@code HelloWorld}
 * class:
 *
 * {@snippet file = "objectos/code/JavadocSnippetsTest.java" region = "Code01"}
 *
 * <p>
 * When executed, the program prints:
 *
 * <pre>{@code  package com.example;
 *
 * import java.util.List;
 *
 * public class HelloWorld {
 *   public static void main(String[] args) {
 *     List<String> lines = List.of("Hello", "Objectos Code!");
 *     for (var line : lines) {
 *       System.out.println(line);
 *     }
 *   }
 * }}</pre>
 */
public sealed interface Code
    extends StringTemplate.Processor<String, IllegalArgumentException>
    permits CodeImpl {

  /**
   * Provides the import list facility of the enclosing processor. An import
   * list instance is bound to a specific {@link Code} instance. The generated
   * list of import declarations are sorted alphabetically.
   *
   * <p>
   * This facility only supports single-type-import declarations.
   *
   * <h2>Base package</h2>
   *
   * <p>
   * An import list instance defines a base package. A {@link ClassName}
   * instance whose package name is different then the base package <em>may</em>
   * generate a single-type-import declaration.
   *
   * <p>
   * As an example, consider the following program:
   *
   * {@snippet file = "objectos/code/JavadocSnippetsTest.java" region =
   * "ImportList01"}
   *
   * <p>
   * When executed, it prints:
   *
   * <pre>{@code  package com.example;
   *
   * import java.util.List;
   * import java.time.LocalDate;
   *
   * interface Example01 {
   *   List<String> strings();
   *   LocalDate localDate();
   * }}</pre>
   *
   * <h2>{@code java.lang} types and base package types</h2>
   *
   * {@link ClassName} instances whose package name are either:
   *
   * <ul>
   * <li>{@code java.lang}; or
   * <li>the base package.
   * </ul>
   *
   * Will not generate an import declaration and, <em>usually</em>, will be
   * emitted only with its simple name.
   *
   * <p>
   * As an example, consider the following program:
   *
   * {@snippet file = "objectos/code/JavadocSnippetsTest.java" region =
   * "ImportList02"}
   *
   * <p>
   * When executed, it prints:
   *
   * <pre>{@code  package com.example;
   *
   * interface Example02 {
   *   Thread thread();
   *   Foo foo();
   * }}</pre>
   *
   * <h2>Same simple name but different package names and/or enclosing
   * types</h2>
   *
   * <p>
   * The import list facility handles conflicts that arise when different types
   * have the same simple name.
   *
   * <p>
   * To illustrate, consider the following program:
   *
   * {@snippet file = "objectos/code/JavadocSnippetsTest.java" region =
   * "ImportList03"}
   *
   * <p>
   * When executed, it prints:
   *
   * <pre>{@code  package com.example;
   *
   * import java.util.List;
   *
   * interface Example03 {
   *   List<String> list01();
   *   com.example.List<String> list02();
   *   java.awt.List<String> list03();
   * }}</pre>
   */
  public sealed interface ImportList permits CodeImportList {}

  /**
   * Returns a newly created {@code Code} instance.
   *
   * @return a new {@code Code} instance
   */
  static Code of() {
    return new CodeImpl();
  }

  /**
   * Escapes the specified string so it can be used to represent the value of a
   * Java string literal.
   *
   * <p>
   * The following program uses a text block value to generate the value of a
   * string literal:
   *
   * {@snippet file = "objectos/code/JavadocSnippetsTest.java" region =
   * "Code-escape"}
   *
   * <p>
   * When executed, the program prints:
   *
   * <pre>
   * String asStringLiteral = "An \"escape\" function\nexample!\n";</pre>
   *
   * @param str
   *        the string to be escaped
   *
   * @return the escaped string
   */
  static String escape(String str) {
    // implicit null check
    int length;
    length = str.length();

    StringBuilder sb;
    sb = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      char c = str.charAt(i);

      switch (c) {
        case '\\' -> sb.append("\\\\");

        case '\"' -> sb.append("\\\"");

        case '\b' -> sb.append("\\b");

        case '\t' -> sb.append("\\t");

        case '\n' -> sb.append("\\n");

        case '\f' -> sb.append("\\f");

        case '\r' -> sb.append("\\r");

        default -> sb.append(c);
      }
    }

    return sb.toString();
  }

  /**
   * Increases the indentation of each line of the specified string based on the
   * value of {@code n}.
   *
   * <p>
   * It behaves similarly to invoking the method {@linkplain String#indent(int)
   * String::indent} directly on the specified string {@code str} with the
   * following differences:
   *
   * <ul>
   * <li>this method rejects values of {@code n} which are non-positive;
   * <li>lines which are blank are ignored; and
   * <li>a new line character is not added to the end of the result.
   * </ul>
   *
   * @param str
   *        the string to have the indentation increases
   * @param n
   *        number of leading white space characters to add
   *
   * @return string with indentation increased
   *
   * @throws IllegalArgumentException
   *         if {@code n <= 0}
   */
  static String indent(String str, int n) {
    Check.argument(n > 0, "n must be a positive number");

    // implicit null-check
    if (str.isEmpty()) {
      return "";
    }

    final String spaces;
    spaces = " ".repeat(n);

    Function<String, String> mapper;
    mapper = s -> {
      if (!s.isBlank()) {
        s = spaces + s;
      }

      return s;
    };

    return str.lines().map(mapper).collect(Collectors.joining("\n"));
  }

  /**
   * Return the import list facility bound to this processor using the specified
   * package name as its base package.
   *
   * <p>
   * In other words, the returned import list will usually not generate an
   * import declaration for a type whose package is the same as the specified
   * {@code packageName}.
   *
   * <p>
   * Invoking this method also clears all of the previously processed
   * {@link ClassName} instances by the returned import list.
   *
   * @param packageName
   *        the name of the package for whose types an import declaration should
   *        not be generated (if possible).
   *
   * @return the import list instance bound to this processor
   *
   * @throws IllegalArgumentException
   *         if {@code packageName} is not a valid identifier
   */
  ImportList importList(String packageName);

}