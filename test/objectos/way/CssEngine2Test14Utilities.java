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

import static objectos.way.CssEngine2.rule;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngine2Test14Utilities {

  private static final CssEngine2.Simple AFTER = CssEngine2.simple("&::after");

  @DataProvider
  public Object[][] writeProvider() {
    return new Object[][] {{
        "Empty",
        List.of(),
        ""
    }, {
        "1 rule",
        List.of(
            rule(".margin\\:0", List.of(), "margin", "0")
        ),
        """
        @layer utilities {
          .margin\\:0 { margin: 0 }
        }
        """
    }, {
        "1 rule + 1 variant",
        List.of(
            rule(".after\\/padding\\:0", List.of(AFTER), "padding", "0")
        ),
        """
        @layer utilities {
          .after\\/padding\\:0 { &::after { padding: 0 } }
        }
        """
    }};
  }

  @Test(dataProvider = "writeProvider")
  public void write(
      String description,
      @SuppressWarnings("exports") List<CssEngine2.Rule> rules,
      String expected) {
    try {
      final CssEngine2.Utilities utilities;
      utilities = new CssEngine2.Utilities(rules);

      final StringBuilder out;
      out = new StringBuilder();

      utilities.write(out);

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}