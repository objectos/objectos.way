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
import org.testng.annotations.Test;

public class CssEngineValueTest0Parse {

  @Test(description = "colors :: just one")
  public void colors01() {
    test(
        """
        --color-stone-950: oklch(0.147 0.004 49.25);
        """,

        List.of(
            CssEngineValue.themeVar("color", "stone-950", "oklch(0.147 0.004 49.25)")
        )
    );
  }

  private void test(String value, List<CssEngineValue> expected) {
    final List<CssEngineValue> result;
    result = CssEngineValue.parse(value);

    assertEquals(result, expected);
  }

}