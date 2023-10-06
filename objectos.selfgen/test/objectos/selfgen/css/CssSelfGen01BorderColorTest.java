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
package objectos.selfgen.css;

import static objectos.selfgen.css.Util.generate;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CssSelfGen01BorderColorTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        // global keywords
        var globalKeyword = t("GlobalKeyword",
          k("inherit"), k("initial"), k("unset")
        );

        // color
        var color = color(
          "currentcolor",
          "transparent"
        );

        // B
        property(
          "border-color",

          sig(globalKeyword, "value"),
          sig(color, "all"),
          sig(color, "vertical", color, "horizontal"),
          sig(color, "top", color, "horizontal", color, "bottom"),
          sig(color, "top", color, "right", color, "bottom", color, "left")
        );
      }
    };

    result = generate(spec);
  }

  @Test
  public void generatedColor() {
    assertEquals(
      result.get("objectos/css/util/GeneratedColor.java"),

      """
      /*
       * Copyright (C) 2016-2023 Objectos Software LTDA.
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

      // Generated by selfgen.css.CssSpec. Do not edit!
      abstract class GeneratedColor {
        public static final Color CURRENTCOLOR = Color.named("currentcolor");

        public static final Color TRANSPARENT = Color.named("transparent");
      }
      """
    );
  }

  @Test
  public void generatedCssTemplate() throws IOException {
    assertEquals(
      result.get("objectos/css/internal/GeneratedCssTemplate.java"),

      """
      /*
       * Copyright (C) 2016-2023 Objectos Software LTDA.
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
      package objectos.css.internal;

      import objectos.css.tmpl.Api.ColorValue;
      import objectos.css.tmpl.Api.GlobalKeyword;
      import objectos.css.tmpl.Api.PropertyValue;
      import objectos.css.tmpl.Api.Selector;
      import objectos.css.tmpl.Api.StyleDeclaration;
      import objectox.lang.Check;

      // Generated by selfgen.css.CssSpec. Do not edit!
      abstract class GeneratedCssTemplate {

        protected static final Selector any = StandardName.any;

        protected static final ColorValue currentcolor = StandardName.currentcolor;

        protected static final ColorValue transparent = StandardName.transparent;

        protected static final GlobalKeyword inherit = StandardName.inherit;

        protected static final GlobalKeyword initial = StandardName.initial;

        protected static final GlobalKeyword unset = StandardName.unset;

        protected final StyleDeclaration borderColor(GlobalKeyword value) {
          Check.notNull(value, "value == null");
          declaration(Property.BORDER_COLOR, value);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration borderColor(ColorValue all) {
          Check.notNull(all, "all == null");
          declaration(Property.BORDER_COLOR, all);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration borderColor(ColorValue vertical, ColorValue horizontal) {
          Check.notNull(vertical, "vertical == null");
          Check.notNull(horizontal, "horizontal == null");
          declaration(Property.BORDER_COLOR, vertical, horizontal);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration borderColor(ColorValue top, ColorValue horizontal, ColorValue bottom) {
          Check.notNull(top, "top == null");
          Check.notNull(horizontal, "horizontal == null");
          Check.notNull(bottom, "bottom == null");
          declaration(Property.BORDER_COLOR, top, horizontal, bottom);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration borderColor(ColorValue top, ColorValue right, ColorValue bottom, ColorValue left) {
          Check.notNull(top, "top == null");
          Check.notNull(right, "right == null");
          Check.notNull(bottom, "bottom == null");
          Check.notNull(left, "left == null");
          declaration(Property.BORDER_COLOR, top, right, bottom, left);
          return InternalInstruction.INSTANCE;
        }

        abstract void declaration(Property name, PropertyValue value);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4);

      }
      """
    );
  }

  @Test
  public void property() {
    assertEquals(
      result.get("objectos/css/internal/Property.java"),

      """
      /*
       * Copyright (C) 2016-2023 Objectos Software LTDA.
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
      package objectos.css.internal;

      // Generated by selfgen.css.CssSpec. Do not edit!
      public enum Property {
        BORDER_COLOR("border-color");

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