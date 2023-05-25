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
package objectos.specgen.css;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import objectos.specgen.css.mdn.Mdn;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PropertyModuleTemplateTest {

  private final PropertyModuleTemplate template = new PropertyModuleTemplate();

  private Spec spec;

  @BeforeClass
  public void _beforeClass() throws IOException {
    spec = Mdn.load();
  }

  @Test
  public void clear() {
    template.set(p("clear"), List.of());

    assertEquals(
      template.toString(),

      """
      package objectos.selfgen.css;

      import objectos.selfgen.css.spec.Source;

      final class ClearPropertyModule extends AbstractPropertyModule {
        @Override
        final void propertyDefinition() {
          var both = keyword("both");
          var inlineEnd = keyword("inline-end");
          var inlineStart = keyword("inline-start");
          var left = keyword("left");
          var none = keyword("none");
          var right = keyword("right");
          property(
            "clear",

            formal(
              Source.MDN,
              "none | left | right | both | inline-start | inline-end"
            ),

            globalSig
          );
        }
      }
      """
    );
  }

  @Test
  public void margin() {
    template.set(
      p("margin"),
      group("margin-top", "margin-right", "margin-bottom", "margin-left")
    );

    assertEquals(
      template.toString(),

      """
      package objectos.selfgen.css;

      import objectos.selfgen.css.spec.Source;

      final class MarginPropertyModule extends AbstractPropertyModule {
        @Override
        final void propertyDefinition() {
          var auto = keyword("auto");
          property(
            "margin",

            formal(
              Source.MDN,
              "[ <length> | <percentage> | auto ]{1,4}"
            ),

            globalSig
          );
          property(
            names("margin-top", "margin-right", "margin-bottom", "margin-left"),

            formal(
              Source.MDN,
              "<length> | <percentage> | auto"
            ),

            globalSig
          );
        }
      }
      """
    );
  }

  private List<Property> group(String... names) {
    return Stream.of(names).map(this::p).toList();
  }

  private Property p(String name) { return spec.getProperty(name); }

}
