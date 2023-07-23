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
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CssUtilSelfGen04Test {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssUtilSelfGen() {
      @Override
      protected final void definition() {
        Prefix prefixAll;
        prefixAll = breakpoint("All", 0);

        Names spacing = names(
          name("PX", px(1)),
          name("V1", rem(0.25))
        );

        generate(
          prefixAll,
          simpleName("Height"),
          methods("height"),
          names(
            spacing,
            name("FULL", pct(100)),
            name("SCREEN", vh(100))
          )
        );
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

        public static final class Height {
          public static final ClassSelector PX = ClassSelector.randomClassSelector(5);

          public static final ClassSelector V1 = ClassSelector.randomClassSelector(5);

          public static final ClassSelector FULL = ClassSelector.randomClassSelector(5);

          public static final ClassSelector SCREEN = ClassSelector.randomClassSelector(5);

          private Height() {}
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
        }

        private void All() {
          AllHeight();
        }

        private void AllHeight() {
          style(
            objectos.css.util.All.Height.PX,
            height(px(1))
      );
          style(
            objectos.css.util.All.Height.V1,
            height(rem(0.25))
      );
          style(
            objectos.css.util.All.Height.FULL,
            height(pct(100))
      );
          style(
            objectos.css.util.All.Height.SCREEN,
            height(vh(100))
      );
        }
      }
      """
    );
  }

}