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

public class StandardPropertyNameStepTest extends AbstractCssBootSpecTest {

  @Test(description = "it should generate an enum constant for each property defined")
  public void execute() {
    execute(
      new StandardPropertyNameStep(adapter),

      new CssSpec() {
        @Override
        protected final void definition() {
          KeywordName auto = keyword("auto");
          KeywordName none = keyword("none");

          property(
            "clear",
            formal("", Source.MANUAL_ENTRY),
            sig(t("ClearValue", none), "value")
          );

          property(
            "top",
            formal("", Source.MANUAL_ENTRY),
            sig(t("TopValue", auto), "value")
          );
        }
      }
    );

    assertEquals(resultList.size(), 1);

    assertEquals(
      resultList.get(0),

      """
      package br.com.objectos.css.property;

      import objectos.util.GrowableMap;
      import objectos.util.UnmodifiableMap;

      public enum StandardPropertyName implements PropertyName {
        CLEAR("clear", "clear"),

        TOP("top", "top");

        private static final StandardPropertyName[] ARRAY = StandardPropertyName.values();

        private static final UnmodifiableMap<String, StandardPropertyName> MAP = buildMap();

        private final String javaName;

        private final String name;

        private StandardPropertyName(String javaName, String name) {
          this.javaName = javaName;
          this.name = name;
        }

        public static StandardPropertyName getByCode(int code) {
          return ARRAY[code];
        }

        public static StandardPropertyName getByName(String name) {
          return MAP.get(name);
        }

        private static UnmodifiableMap<String, StandardPropertyName> buildMap() {
          var m = new GrowableMap<String, StandardPropertyName>();
          m.put("clear", CLEAR);
          m.put("top", TOP);
          return m.toUnmodifiableMap();
        }

        public static int size() {
          return ARRAY.length;
        }

        @Override
        public final int getCode() {
          return ordinal();
        }

        public final String getJavaName() {
          return javaName;
        }

        @Override
        public final String getName() {
          return name;
        }
      }
      """
    );
  }

}
