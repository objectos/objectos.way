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

import static objectos.way.CssEngine.block;
import static objectos.way.CssEngine.decl;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngineTest13Components {

  @DataProvider
  public Object[][] writeProvider() {
    return new Object[][] {{
        "1 component",

        List.of(
            block("[data-theme=g90]", decl("--color-background", "#262626"))
        ),

        """
        @layer components {
          [data-theme=g90] {
            --color-background: #262626;
          }
        }
        """
    }};
  }

  @Test(dataProvider = "writeProvider")
  public void write(
      String description,
      @SuppressWarnings("exports") List<CssEngine.Block> components,
      String expected) {
    try {
      final CssEngine.Components writer;
      writer = new CssEngine.Components(components);

      final StringBuilder out;
      out = new StringBuilder();

      writer.write(out);

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}