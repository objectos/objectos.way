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
import org.testng.annotations.Test;

public class CssEngine2Test10Theme {

  @Test
  public void testCase01() {
    test(
        List.of(),

        ""
    );
  }

  @Test
  public void testCase02() {
    test(
        List.of(s(
            List.of(),

            CssEngine2.decl("--color-red-50", "oklch(97.1% 0.013 17.38)")
        )),

        """
        @layer theme {
          :root {
            --color-red-50: oklch(97.1% 0.013 17.38);
          }
        }
        """
    );
  }

  private CssEngine2.Section s(List<String> selector, CssEngine2.Decl... values) {
    return new CssEngine2.Section(
        selector,
        List.of(values)
    );
  }

  private void test(List<CssEngine2.Section> sections, String expected) {
    try {
      final CssEngine2.Theme theme;
      theme = new CssEngine2.Theme(sections);

      final StringBuilder out;
      out = new StringBuilder();

      theme.write(out);

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}