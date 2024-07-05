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
  public void parseProperties01() {
    Map<String, String> map;
    map = Css.parseProperties("").toMap();

    assertEquals(map, Map.of());
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

}