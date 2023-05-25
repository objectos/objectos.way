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

public class KeywordsClassStepTest extends AbstractCssBootSpecTest {

  @Test(description = "Keywords class must declared each keyword defined (sorted alpha)")
  public void execute() {
    execute(
      new KeywordsClassStep(adapter),

      new CssSpec() {
        @Override
        protected final void definition() {
          elementName("a");
          elementName("div");

          keyword("none");
          keyword("auto");
          keyword("double");
          keyword("div");
        }
      }
    );

    assertEquals(resultList.size(), 1);

    assertEquals(
      resultList.get(0),

      """
      package br.com.objectos.css.keyword;

      import objectos.util.GrowableMap;
      import objectos.util.UnmodifiableMap;

      public final class Keywords {
        public static final AutoKeyword auto = AutoKeyword.INSTANCE;

        public static final DivKeyword divKw = DivKeyword.INSTANCE;

        public static final DoubleKeyword doubleKw = DoubleKeyword.INSTANCE;

        public static final NoneKeyword none = NoneKeyword.INSTANCE;

        private static final StandardKeyword[] ARRAY = {
          auto,
          divKw,
          doubleKw,
          none
        };

        private static final UnmodifiableMap<String, StandardKeyword> MAP = buildMap();

        private Keywords() {}

        public static StandardKeyword getByCode(int code) {
          return ARRAY[code];
        }

        public static StandardKeyword getByName(String name) {
          var k = MAP.get(name);
          if (k == null) {
            throw new IllegalArgumentException(name);
          }
          return k;
        }

        public static boolean isKeyword(String name) {
          return MAP.containsKey(name);
        }

        private static UnmodifiableMap<String, StandardKeyword> buildMap() {
          var m = new GrowableMap<String, StandardKeyword>();
          m.put("auto", auto);
          m.put("div", divKw);
          m.put("double", doubleKw);
          m.put("none", none);
          return m.toUnmodifiableMap();
        }
      }
      """
    );
  }

}
