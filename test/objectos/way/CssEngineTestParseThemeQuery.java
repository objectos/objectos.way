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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.List;
import org.testng.annotations.Test;

public class CssEngineTestParseThemeQuery {

  @Test(description = "single entry")
  public void query01() {
    List<CssEngine.ThemeQueryEntry> entries;
    entries = test("@media (prefers-color-scheme: dark)", """
    --color-background: var(--color-gray-800);
    """);

    assertEquals(entries.size(), 1);

    CssEngine.ThemeQueryEntry entry0;
    entry0 = entries.get(0);

    assertEquals(entry0.name(), "--color-background");
    assertEquals(entry0.value(), "var(--color-gray-800)");
    assertEquals(entry0.toString(), "--color-background: var(--color-gray-800);");
  }

  private List<CssEngine.ThemeQueryEntry> test(String query, String theme) {
    CssEngine engine;
    engine = new CssEngine();

    engine.theme(query, theme);

    return engine.testThemeQueryEntries(query);
  }

}