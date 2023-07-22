/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css2.util;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CssUtilSelfGen01Test {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssUtilSelfGen() {
      private Prefix prefixAll;
      private Prefix prefixLarge;

      private List<Prefix> responsive;

      @Override
      protected final void definition() {
        // breakpoints
        prefixAll = breakpoint("All", 0);
        prefixLarge = breakpoint("Large", 1024);

        responsive = List.of(
          prefixAll,
          prefixLarge
        );

        // d
        display();
      }

      private void display() {
        Names names;
        names = names(
          name("HIDDEN", k("none")),
          name("BLOCK", k("block")),
          name("FLEX", k("flex"))
        );

        for (Prefix prefix : responsive) {
          generate(
            prefix,
            simpleName("Display"),
            methods("display"),
            names
          );
        }
      }
    };

    result = Generate.generate(spec);
  }

  @Test
  public void all() {
    assertEquals(
      result.get("objectos/css/util/All.java"),

      """
      package objectos.css.util;

      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssUtilSpec")
      public final class All {
        private All() {}

        public static final class Display {
          public static final ClassSelector HIDDEN = ClassSelector.randomClassSelector(5);

          public static final ClassSelector BLOCK = ClassSelector.randomClassSelector(5);

          public static final ClassSelector FLEX = ClassSelector.randomClassSelector(5);

          private Display() {}
        }
      }
      """
    );
  }

  @Test
  public void large() {
    assertEquals(
      result.get("objectos/css/util/Large.java"),

      """
      package objectos.css.util;

      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssUtilSpec")
      public final class Large {
        private Large() {}

        public static final class Display {
          public static final ClassSelector HIDDEN = ClassSelector.randomClassSelector(5);

          public static final ClassSelector BLOCK = ClassSelector.randomClassSelector(5);

          public static final ClassSelector FLEX = ClassSelector.randomClassSelector(5);

          private Display() {}
        }
      }
      """
    );
  }

  @Test
  public void framework() {
    assertEquals(
      result.get("objectos/css/util/Framework.java"),

      """
      package objectos.css.util;

      @objectos.lang.Generated("objectos.selfgen.CssUtilSpec")
      public final class Framework extends objectos.css.CssTemplate {
        @java.lang.Override
        protected final void definition() {
          All();
          Large();
        }

        private void All() {
          AllDisplay();
        }

        private void Large() {
          LargeDisplay();
        }

        private void AllDisplay() {
          style(
            objectos.css.util.All.Display.HIDDEN,
            display(none)
      );
          style(
            objectos.css.util.All.Display.BLOCK,
            display(block)
      );
          style(
            objectos.css.util.All.Display.FLEX,
            display(flex)
      );
        }

        private void LargeDisplay() {
          media(
            minWidth(px(1024)),
            style(
              objectos.css.util.Large.Display.HIDDEN,
              display(none)
      ),
            style(
              objectos.css.util.Large.Display.BLOCK,
              display(block)
      ),
            style(
              objectos.css.util.Large.Display.FLEX,
              display(flex)
      ));
        }
      }
      """
    );
  }

}