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

import java.util.Map;
import java.util.function.Consumer;
import objectos.way.CssEngine2.Config;
import org.testng.annotations.Test;

public class CssEngine2Test01Config {

  @Test(description = """
  breakpoint
  - it should create a variant
  """)
  public void breakpoint01() {
    test(
        """
        --breakpoint-sm: 40rem;
        """,

        c -> {
          final Map<String, CssVariant> variants;
          variants = c.variants();

          assertEquals(variants.get("sm"), CssVariant.atRule("@media (min-width: 40rem)"));
        }
    );
  }

  private void test(String theme, Consumer<? super CssEngine2.Config> test) {
    final CssEngine2 engine;
    engine = new CssEngine2();

    engine.theme("--*: initial;");

    engine.theme(theme);

    final Config config;
    config = engine.configure();

    test.accept(config);
  }

}