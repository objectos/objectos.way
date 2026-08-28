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

import objectos.html.Component;

/**
 * The <strong>Objectos Syntax</strong> main class, part of Objectos HTML.
 */
public final class Syntax {

  public sealed interface Language permits SyntaxLanguage {}

  public static final String DATA_LINE = "data-line";

  public static final String DATA_HIGH = "data-high";

  // languages

  public static final Language JAVA = SyntaxLanguage.JAVA;

  private Syntax() {}

  public static Component highlight(Language language, String source) {
    return switch (language) {
      case SyntaxLanguage.JAVA -> new SyntaxJava(source);
    };
  }

}