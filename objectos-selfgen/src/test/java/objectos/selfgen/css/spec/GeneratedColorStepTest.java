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

public class GeneratedColorStepTest extends AbstractCssBootSpecTest {

  @Test
  public void execute() {
    execute(
      new GeneratedColorStep(adapter),

      new CssSpec() {
        @Override
        protected final void definition() {
          namedColors("transparent", "ButtonText");
        }
      }
    );

    assertEquals(resultList.size(), 1);

    assertEquals(
      resultList.get(0),

      """
      package br.com.objectos.css.type;

      import objectos.util.GrowableMap;
      import objectos.util.UnmodifiableMap;

      abstract class GeneratedColor {
        public static final ColorName ButtonText = new ColorName(0, "ButtonText");

        public static final ColorName transparent = new ColorName(1, "transparent");

        private static final ColorName[] ARRAY = {
          ButtonText,
          transparent
        };

        private static final UnmodifiableMap<String, ColorName> MAP = buildMap();

        public static ColorName getByCode(int code) {
          return ARRAY[code];
        }

        public static ColorName getByName(String name) {
          var c = MAP.get(name);
          if (c == null) {
            throw new IllegalArgumentException(name);
          }
          return c;
        }

        public static boolean isColor(String name) {
          return MAP.containsKey(name);
        }

        private static UnmodifiableMap<String, ColorName> buildMap() {
          var m = new GrowableMap<String, ColorName>();
          m.put("ButtonText", ButtonText);
          m.put("transparent", transparent);
          return m.toUnmodifiableMap();
        }
      }
      """
    );
  }

}