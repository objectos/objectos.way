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
package objectos.selfgen.css.spec;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class PseudoClassSelectorsGenTest extends AbstractCssBootSpecTest {

  @Test
  public void generateJavaFile() {
    execute(
      new PseudoClassSelectorsGen(adapter),

      new CssSpec() {
        @Override
        protected final void definition() {
          pseudoClasses(
            "checked",
            "-moz-focusring"
          );
        }
      }
    );

    assertEquals(resultList.size(), 1);

    assertEquals(
      resultList.get(0),

      """
      package objectos.css.select;

      import objectos.util.GrowableMap;
      import objectos.util.UnmodifiableMap;

      public final class PseudoClassSelectors {
        public static final PseudoClassSelector CHECKED = new PseudoClassSelector(0, "checked");

        public static final PseudoClassSelector _MOZ_FOCUSRING = new PseudoClassSelector(1, "-moz-focusring");

        private static final PseudoClassSelector[] ARRAY = {
          CHECKED,
          _MOZ_FOCUSRING
        };

        private static final UnmodifiableMap<String, PseudoClassSelector> MAP = buildMap();

        private PseudoClassSelectors() {}

        public static PseudoClassSelector getByCode(int code) {
          return ARRAY[code];
        }

        public static PseudoClassSelector getByName(String name) {
          return MAP.get(name);
        }

        private static UnmodifiableMap<String, PseudoClassSelector> buildMap() {
          var m = new GrowableMap<String, PseudoClassSelector>();
          m.put("checked", CHECKED);
          m.put("-moz-focusring", _MOZ_FOCUSRING);
          return m.toUnmodifiableMap();
        }
      }
      """
    );
  }

}
