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

import java.util.function.Consumer;

/**
 * The <strong>Objectos Syntax</strong> main class, part of Objectos HTML.
 */
public final class Syntax {

  public sealed interface Highlighter {

    Html.Component highlight(String value);

  }

  public sealed interface Java extends Highlighter permits SyntaxJava {

    public sealed interface Config permits SyntaxJavaConfig {

      void set(Element element, String className);

    }

    public sealed interface Element permits SyntaxJavaElement {}

    Element ANNOTATION = SyntaxJavaElement.ANNOTATION;

    Element COMMENT = SyntaxJavaElement.COMMENT;

    Element KEYWORD = SyntaxJavaElement.KEYWORD;

    Element STRING_LITERAL = SyntaxJavaElement.STRING_LITERAL;

    static Java create(Consumer<Config> config) {
      final SyntaxJavaConfig builder;
      builder = new SyntaxJavaConfig();

      config.accept(builder);

      return builder.build();
    }

  }

}