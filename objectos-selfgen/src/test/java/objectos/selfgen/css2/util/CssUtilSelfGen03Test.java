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

public class CssUtilSelfGen03Test {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssUtilSelfGen() {
      @Override
      protected final void definition() {
        Prefix prefixAll;
        prefixAll = breakpoint("All", 0);

        generate(
          prefixAll,
          simpleName("Padding"),
          methods("padding"),
          names(
            name("PX", px(1)),
            name("V1", rem(0.25))
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

        public static final class Padding {
          public static final ClassSelector PX = ClassSelector.randomClassSelector(5);

          public static final ClassSelector V1 = ClassSelector.randomClassSelector(5);

          private Padding() {}
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
          AllPadding();
        }

        private void AllPadding() {
          style(
            objectos.css.util.All.Padding.PX,
            padding(px(1))
      );
          style(
            objectos.css.util.All.Padding.V1,
            padding(rem(0.25))
      );
        }
      }
      """
    );
  }

}