/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package selfgen.css.util;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import org.testng.annotations.Test;

public class PropertyEnumStepTest {

  @Test
  public void display() throws IOException {
    var spec = new CssUtilSelfGen() {
      @Override
      protected void definition() {
        var p = new StandardProperty("Display", "display");

        p.add("BLOCK", "block");
        p.add("FLEX", "flex");
        p.add("INLINE_FLEX", "inline-flex");

        add(p);
      }
    };

    var result = Util.generate(spec);

    assertEquals(
      result.get("objectos/css/util/Display.java"),

      """
      /*
       * Copyright (C) 2023 Objectos Software LTDA.
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
      package objectos.css.util;

      import objectos.html.tmpl.Api.ExternalAttribute.StyleClass;
      import objectox.css.ClassSelectorSeqId;

      /**
       * Utility classes for the {@code display} CSS property.
       */
      // Generated by selfgen.css.CssUtilSpec. Do not edit!
      public enum Display implements StyleClass {

        BLOCK("block"),

        FLEX("flex"),

        INLINE_FLEX("inline-flex");

        private final String className = ClassSelectorSeqId.next();

        private final String value;

        private Display(String value) {
          this.value = value;
        }

        /**
         * Returns the CSS class name.
         *
         * @return the CSS class name
         */
        @Override
        public final String className() {
          return className;
        }

        /**
         * Returns the CSS style rule represented by this utility class.
         *
         * @return the CSS style rule
         */
        @Override
        public final String toString() {
          return "." + className + " { display: " + value + " }";
        }

      }
      """
    );
  }

}