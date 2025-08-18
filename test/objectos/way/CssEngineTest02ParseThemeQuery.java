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

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

public class CssEngineTest02ParseThemeQuery {

  @Test(description = "single entry")
  public void query01() {
    test(
        "@media (prefers-color-scheme: dark)",

        """
        --color-background: var(--color-gray-800);
        """,

        """
        --color-background: var(--color-gray-800);
        """
    );
  }

  @Test(description = "multiple entries")
  public void query02() {
    test(
        "@media (prefers-color-scheme: dark)",

        """
        --color-background: var(--color-gray-800);

        --color-foreground: var(--color-gray-100);
        --color-border: var(--color-gray-600);
        """,

        """
        --color-background: var(--color-gray-800);
        --color-foreground: var(--color-gray-100);
        --color-border: var(--color-gray-600);
        """
    );
  }

  private void test(String query, String theme, String expected) {
    final CssEngineBuilder builder;
    builder = new CssEngineBuilder(true);

    builder.noteSink(Y.noteSink());

    builder.theme(query, theme);

    final Css.Query key;
    key = Css.Query.of(query);

    List<Css.ThemeQueryEntry> list;
    list = builder.testThemeQueryEntries(key);

    assertEquals(
        list.stream()
            .map(Object::toString)
            .collect(Collectors.joining("\n", "", "\n")),

        expected
    );
  }

}