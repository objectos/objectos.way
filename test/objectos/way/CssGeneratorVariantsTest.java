/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.List;
import org.testng.annotations.Test;

public class CssGeneratorVariantsTest {

  private final CssVariant hover = new CssVariant.ClassNameFormat("", ":hover");

  @Test(description = "single variant")
  public void testCase01() {
    Impl impl;
    impl = new Impl();

    CssRule rule;
    rule = impl.onCacheMiss("hover:block");

    assertEquals(
        rule.toString(),

        """
        .hover\\:block:hover { display: block }
        """
    );
  }

  @Test(description = "it should not process class name with unknown prefix")
  public void testCase02() {
    Impl impl;
    impl = new Impl();

    CssRule rule;
    rule = impl.onCacheMiss("xyz:block");

    assertSame(rule, CssRule.NOOP);
  }

  private class Impl extends CssGeneratorVariants {

    @Override
    final CssVariant getVariant(String variantName) {
      return switch (variantName) {
        case "hover" -> hover;

        default -> null;
      };
    }

    @Override
    final CssRule onVariants(String className, List<CssVariant> variants, String value) {
      return new CssUtility(
          CssKey.DISPLAY,
          className, variants,
          CssProperties.of("display", value)
      );
    }

  }

}