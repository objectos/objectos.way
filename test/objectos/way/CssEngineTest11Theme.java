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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngineTest11Theme {

  private static final String DARK = "@media (prefers-color-scheme: dark)";

  @DataProvider
  public Object[][] writeProvider() {
    return new Object[][] {{
        "Empty",
        List.of(),
        ""
    }, {
        ":root + 1 decl",

        List.of(s(
            List.of(":root"),

            CssEngine.decl("--color-red-50", "oklch(97.1% 0.013 17.38)")
        )),

        """
        @layer theme {
          :root {
            --color-red-50: oklch(97.1% 0.013 17.38);
          }
        }
        """
    }, {
        ":root + @media",

        List.of(s(
            List.of(":root"),

            CssEngine.decl("--color-primary", "#f0f0f0")
        ), s(
            List.of(":root", DARK),

            CssEngine.decl("--color-primary", "#1e1e1e")
        )),

        """
        @layer theme {
          :root {
            --color-primary: #f0f0f0;
          }
          :root {
            @media (prefers-color-scheme: dark) {
              --color-primary: #1e1e1e;
            }
          }
        }
        """
    }};
  }

  @Test(dataProvider = "writeProvider")
  public void write(
      String description,
      @SuppressWarnings("exports") List<CssEngine.Section> sections,
      String expected) {
    try {
      final CssEngine.Theme theme;
      theme = new CssEngine.Theme(sections);

      final StringBuilder out;
      out = new StringBuilder();

      theme.write(out);

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private CssEngine.Section s(List<String> selector, CssEngine.Decl... values) {
    return CssEngine.section(
        selector,
        List.of(values)
    );
  }

}