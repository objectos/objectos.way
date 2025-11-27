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

public class CssEngineTest00ValueFormat {

  @DataProvider
  public Object[][] formatValidProvider() {
    return new Object[][] {
        // single value
        {"color: hex", "#f0f0f0", "#f0f0f0"},
        {"keyword", "block", "block"},
        {"keyword", "!important", "!important"},
        {"length", "40rem", "40rem"},
        {"num: int", "123", "123"},
        {"num: neg int", "-123", "-123"},
        {"num: dbl", "1.23", "1.23"},
        {"num: neg dbl", "-1.23", "-1.23"},
        {"num: dbl dot", ".23", ".23"},
        {"num: neg dbl dot", "-.23", "-.23"},
        {"perc", "40%", "40%"},
        {"ratio: ws", "2 / 3", "2 / 3"},
        {"ratio: no ws", "2/3", "2 / 3"},
        {"rx: int", "40rx", "calc(40 / 16 * 1rem)"},
        {"rx: neg int", "-123rx", "calc(-123 / 16 * 1rem)"},
        {"rx: dbl", "1.23rx", "calc(1.23 / 16 * 1rem)"},
        {"rx: dbl dot", ".23rx", "calc(.23 / 16 * 1rem)"},
        {"rx: neg dbl", "-4.567rx", "calc(-4.567 / 16 * 1rem)"},
        {"rx: neg dbl dot", "-.567rx", "calc(-.567 / 16 * 1rem)"},
        {"rx: zero", "0rx", "calc(0 / 16 * 1rem)"},
        {"str: sng quote", "'foo'", "'foo'"},
        {"str: sng quote (escape)", "'foo \\' bar'", "'foo \\' bar'"},
        {"str: dbl quote", "\"foo\"", "\"foo\""},
        {"str: dbl quote (escape)", "\"foo \\\" bar\"", "\"foo \\\" bar\""},
        {"u-range: single", "U+26", "U+26"},
        {"u-range: range", "U+0-7F", "U+0-7F"},
        {"u-range: range", "U+0025-00FF", "U+0025-00FF"},
        {"u-range: wildcard", "U+4??", "U+4??"},
        {"u-range: list", "U+0025-00FF,  U+4??", "U+0025-00FF, U+4??"},
        // fun
        {"calc: +", "calc(1 + 2)", "calc(1 + 2)"},
        {"calc: -", "calc(1 - 2)", "calc(1 - 2)"},
        {"calc: *", "calc(1 * 2)", "calc(1 * 2)"},
        {"calc: /", "calc(1 / 2)", "calc(1 / 2)"},
        {"calc: (", "calc((var(--a) - var(--b))/2)", "calc((var(--a) - var(--b)) / 2)"},
        {"oklch", "oklch(0.408 0.123 38.172)", "oklch(0.408 0.123 38.172)"},
        {"var: 1 arg", "var(--foo)", "var(--foo)"},
        {"var: 2 args", "var(--foo, alt)", "var(--foo, alt)"},
        {"var: nested", "var(--foo, var(--bar))", "var(--foo, var(--bar))"},
        {"var: ws", "var(\n--foo,var(--bar))", "var(--foo, var(--bar))"},
    };
  }

  @Test(dataProvider = "formatValidProvider")
  public void formatValid(
      String description,
      String src,
      String expected) {
    final CssEngine.ValueFormat value;
    value = new CssEngine.ValueFormat();

    value.set(src);

    assertEquals(value.format(), expected);
  }

}