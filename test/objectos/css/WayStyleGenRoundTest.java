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
package objectos.css;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class WayStyleGenRoundTest {

  @Test
  public void testCase01() {
    WayStyleGenConfig config;
    config = config();

    WayStyleGenRound round;
    round = new WayStyleGenRound(config);

    round.rules.put("bg-black", Utility.BACKGROUND_COLOR.get("bg-black", List.of(), "black"));

    assertEquals(
        round.generate(),

        """
        .bg-black { background-color: black }
        """
    );
  }

  private WayStyleGenConfig config() {
    WayStyleGen gen;
    gen = new WayStyleGen();

    gen.overrideColors(
        Map.entry("inherit", "inherit"),
        Map.entry("current", "currentColor"),
        Map.entry("transparent", "transparent"),

        Map.entry("black", "#000000"),
        Map.entry("white", "#ffffff")
    );
    
    gen.skipReset();

    return gen;
  }

}