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

import static objectos.way.CssEngine2.Configuring.Flag.SKIP_SYSTEM_THEME;
import static objectos.way.CssEngine2.Configuring.Flag.SKIP_SYSTEM_VARIANTS;
import static org.testng.Assert.assertEquals;

import java.util.Map;
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class CssEngine2Test01Config {

  @Test(description = """
  breakpoint
  - it should create a keyword
  - it should create a variant
  """)
  public void breakpoint01() {
    test(
        """
        --breakpoint-sm: 40rem;
        """,

        c -> c.flags(SKIP_SYSTEM_THEME, SKIP_SYSTEM_VARIANTS),

        c -> {
          assertEquals(c.keywords(), Map.of(
              "screen-sm", CssEngineValue.themeVar("breakpoint", "sm", "40rem")
          ));
          assertEquals(c.variants(), Map.of(
              "sm", CssVariant.atRule("@media (min-width: 40rem)")
          ));
        }
    );
  }

  @Test(description = """
  color
  - it should create a keyword
  """)
  public void colors01() {
    test(
        """
        --color-test: #cafeba;
        """,

        c -> c.flags(SKIP_SYSTEM_THEME, SKIP_SYSTEM_VARIANTS),

        c -> {
          assertEquals(c.keywords(), Map.of(
              "test", CssEngineValue.themeVar("color", "test", "#cafeba")
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test(description = """
  font
  - it should create a keyword
  """)
  public void font01() {
    test(
        """
        --font-test: 'Comic Sans';
        """,

        c -> c.flags(SKIP_SYSTEM_THEME, SKIP_SYSTEM_VARIANTS),

        c -> {
          assertEquals(c.keywords(), Map.of(
              "test", CssEngineValue.themeVar("font", "test", "'Comic Sans'")
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  private void test(String theme, Consumer<? super CssEngine2.Configuring> flags, Consumer<? super CssEngine2.Config> test) {
    final CssEngine2 engine;
    engine = new CssEngine2();

    engine.theme(theme);

    final CssEngine2.Configuring configuring;
    configuring = engine.configuring();

    flags.accept(configuring);

    final CssEngine2.Config config;
    config = configuring.configure();

    test.accept(config);
  }

}