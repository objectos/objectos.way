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
package objectos.selfgen.css2;

import static objectos.selfgen.css2.Util.generate;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import org.testng.annotations.Test;

public class CssSelfGen00Test {

  @Test
  public void propertyBorderWidth() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        var length = length("em", "px");

        var lineWidth = t(
          "LineWidth",

          length,
          k("thin"),
          k("medium"),
          k("thick")
        );

        property(
          "border-width",

          sig(lineWidth, "all"),
          sig(lineWidth, "vertical", lineWidth, "horizontal"),
          sig(lineWidth, "top", lineWidth, "vertical", lineWidth, "bottom"),
          sig(lineWidth, "top", lineWidth, "right", lineWidth, "bottom", lineWidth, "left")
        );
      }
    };

    var result = generate(spec);

    assertEquals(result.size(), 10);

    assertEquals(
      result.get("objectos/css/internal/Property.java"),
      """
      package objectos.css.internal;

      import objectos.css.om.PropertyName;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public enum Property implements PropertyName {
        BORDER_WIDTH("border-width");

        private static final Property[] VALUES = values();

        public final String cssName;

        private Property(String cssName) {
          this.cssName = cssName;
        }

        public static Property byOrdinal(int ordinal) {
          return VALUES[ordinal];
        }

        @Override
        public final String toString() {
          return cssName;
        }
      }
      """
    );

    assertEquals(
      result.get("objectos/css/tmpl/LengthValue.java"),
      """
      package objectos.css.tmpl;

      import objectos.css.internal.InternalInstruction;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface LengthValue extends
          LineWidth permits InternalInstruction, Zero {}
      """
    );

    assertEquals(
      result.get("objectos/css/tmpl/LineWidth.java"),
      """
      package objectos.css.tmpl;

      import objectos.css.internal.StandardName;
      import objectos.css.om.PropertyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface LineWidth extends PropertyValue permits LengthValue, StandardName {}
      """
    );

    assertEquals(
      result.get("objectos/css/tmpl/Zero.java"),
      """
      package objectos.css.tmpl;

      import objectos.css.internal.InternalZero;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface Zero extends LengthValue permits InternalZero {}
      """
    );
  }

  @Test
  public void selectors01() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        selectors(
          // type selectors
          "a",
          "pre",

          // pseudo elements
          "::after", "::before"
        );
      }
    };

    var result = generate(spec);

    assertEquals(result.size(), 6);

    assertEquals(
      result.get("objectos/css/internal/Property.java"),
      """
      package objectos.css.internal;

      import objectos.css.om.PropertyName;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public enum Property implements PropertyName {
        private static final Property[] VALUES = values();

        public final String cssName;

        private Property(String cssName) {
          this.cssName = cssName;
        }

        public static Property byOrdinal(int ordinal) {
          return VALUES[ordinal];
        }

        @Override
        public final String toString() {
          return cssName;
        }
      }
      """
    );
  }

}