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

import java.util.Map;
import org.testng.annotations.Test;

public class CssTest {

  @Test
  public void parseComponents01() {
    assertEquals(
        Css.parseComponents(""),

        Map.of()
    );
  }

  @Test
  public void parseComponents02() {
    assertEquals(
        Css.parseComponents("""
        # header
        foo bar
        baz
        """),

        Map.of("header", "foo bar baz")
    );
  }

  @Test
  public void parseComponents03() {
    assertEquals(
        Css.parseComponents("""
        # header
        foo bar
        baz

        # footer
        more stuff
        """),

        Map.of(
            "header", "foo bar baz",
            "footer", "more stuff"
        )
    );
  }

  @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Empty")
  public void parseProperties01() {
    Css.parseProperties("");
  }

  @Test
  public void parseProperties02() {
    Map<String, String> map;
    map = Css.parseProperties("background: var(--color-background, #FFF)").toMap();

    assertEquals(map, Map.of("background", "var(--color-background, #FFF)"));
  }

  @Test
  public void parseProperties03() {
    Map<String, String> map;
    map = Css.parseProperties("""
    0px: 0px
    1px: 1px
    2px: 0.125rem
    4px: 0.25rem
    """).toMap();

    assertEquals(
        map,
        Map.of(
            "0px", "0px",
            "1px", "1px",
            "2px", "0.125rem",
            "4px", "0.25rem"
        )
    );
  }

  @Test
  public void parseProperties04() {
    Map<String, String> map;
    map = Css.parseProperties("""
    inherit: inherit
    current: currentColor
    transparent: transparent

    black: #000000
    white: #ffffff
    """).toMap();

    assertEquals(
        map,
        Map.of(
            "inherit", "inherit",
            "current", "currentColor",
            "transparent", "transparent",
            "black", "#000000",
            "white", "#ffffff"
        )
    );
  }

  @Test
  public void typeOf() {
    assertEquals(Css.typeOf(""), Css.ValueType.STANDARD);
    assertEquals(Css.typeOf("[]"), Css.ValueType.STRING);
    assertEquals(Css.typeOf("block"), Css.ValueType.STANDARD);
    assertEquals(Css.typeOf("[block]"), Css.ValueType.STRING);
    assertEquals(Css.typeOf("1px"), Css.ValueType.STANDARD);
    assertEquals(Css.typeOf("[1px]"), Css.ValueType.LENGTH);
    assertEquals(Css.typeOf("0"), Css.ValueType.STANDARD);
    assertEquals(Css.typeOf("[0]"), Css.ValueType.ZERO);
    assertEquals(Css.typeOf("[1]"), Css.ValueType.INTEGER);
    assertEquals(Css.typeOf("[10]"), Css.ValueType.INTEGER);
    assertEquals(Css.typeOf("[-1px]"), Css.ValueType.LENGTH_NEGATIVE);
    assertEquals(Css.typeOf("[1.23%]"), Css.ValueType.PERCENTAGE);
  }

  @Test
  public void writeClassName() {
    assertEquals(writeClassName("foo"), ".foo");
    assertEquals(writeClassName("after:block"), ".after\\:block");
    assertEquals(
        writeClassName("focus:shadow-[inset_0_0_0_1px_var(--cds-focus),inset_0_0_0_2px_var(--cds-background)]"),
        ".focus\\:shadow-\\[inset_0_0_0_1px_var\\(--cds-focus\\)\\2c inset_0_0_0_2px_var\\(--cds-background\\)\\]"
    );
  }

  private String writeClassName(String className) {
    StringBuilder out = new StringBuilder();

    Css.writeClassName(out, className);

    return out.toString();
  }

}