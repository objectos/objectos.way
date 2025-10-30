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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngineTest01VariantParser {

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {{
        "attr variant",
        "&[data-foo]",
        CssEngine.simple("&[data-foo]")
    }, {
        "@media variant",
        "@media (prefers-color-scheme: dark)",
        CssEngine.simple("@media (prefers-color-scheme: dark)")
    }, {
        "@media variant (add ws)",
        "@media(prefers-color-scheme:dark)",
        CssEngine.simple("@media (prefers-color-scheme: dark)")
    }, {
        "@media variant (normalize ws)",
        "  @media    (prefers-color-scheme:\ndark)\n",
        CssEngine.simple("@media (prefers-color-scheme: dark)")
    }};
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "validProvider")
  public void valid(
      String descript,
      String input,
      CssEngine.Variant expected) {
    final CssEngine.VariantParser parser;
    parser = new CssEngine.VariantParser();

    final CssEngine.Variant result;
    result = parser.parse(input);

    assertEquals(result, expected);
  }

}