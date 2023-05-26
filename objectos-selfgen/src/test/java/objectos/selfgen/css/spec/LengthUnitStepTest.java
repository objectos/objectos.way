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

public class LengthUnitStepTest extends AbstractCssBootSpecTest {

  @Test(description = "it should generate an enum constant for each unit.")
  public void execute() {
    execute(
      new LengthUnitStep(adapter),

      new CssSpec() {
        @Override
        protected final void definition() {
          lengthUnits("px", "em");
        }
      }
    );

    assertEquals(resultList.size(), 1);

    assertEquals(
      resultList.get(0),

      """
      package objectos.css.type;

      import java.util.Locale;

      public enum LengthUnit {
        EM,

        PX;

        private static final LengthUnit[] ARRAY = LengthUnit.values();

        private final String name;

        private LengthUnit() {
          this.name = name().toLowerCase(Locale.US);
        }

        public static LengthUnit getByCode(int code) {
          return ARRAY[code];
        }

        public static int size() {
          return ARRAY.length;
        }

        public final int getCode() {
          return ordinal();
        }

        public final String getName() {
          return name;
        }
      }
      """
    );
  }

}
