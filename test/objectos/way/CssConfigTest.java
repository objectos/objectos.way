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

import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class CssConfigTest {

  @Test
  public void staticUtility01() {
    CssConfig config;
    config = new CssConfig();

    config.staticUtility(
        CssKey.ACCESSIBILITY,

        """
        sr-only     | position: absolute
                    | width: 1px
                    | height: 1px

        not-sr-only | position: static
                    | width: auto
                    | height: auto
        """
    );

    CssStaticUtility util01;
    util01 = config.getStatic("sr-only");

    CssProperties props01;
    props01 = util01.properties();

    assertEquals(
        props01.entries(),

        List.of(
            Map.entry("position", "absolute"),
            Map.entry("width", "1px"),
            Map.entry("height", "1px")
        )
    );

    CssStaticUtility util02;
    util02 = config.getStatic("not-sr-only");

    CssProperties props02;
    props02 = util02.properties();

    assertEquals(
        props02.entries(),

        List.of(
            Map.entry("position", "static"),
            Map.entry("width", "auto"),
            Map.entry("height", "auto")
        )
    );
  }

}
