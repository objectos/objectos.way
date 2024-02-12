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
package objectos.css;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.List;
import org.testng.annotations.Test;

public class WayStyleGenParserTest {

  private final RuleFactory block = new RuleFactory(Utility.DISPLAY, "block");

  @Test(description = "it should search factory by value, not className")
  public void testCase01() {
    var parser = new ThisImpl();

    Rule rule;
    rule = parser.onVariants("sm:foo", List.of(), "foo");

    assertSame(rule, Rule.NOOP);

    assertEquals(parser.findValue, "foo");
  }

  @Test(description = "it should return rule from factory")
  public void testCase02() {
    var parser = new ThisImpl();

    Rule rule;
    rule = parser.onVariants("sm:block", List.of(), "block");

    assertEquals(rule.utility, Utility.DISPLAY);

    assertEquals(parser.findValue, "block");
  }

  private class ThisImpl extends WayStyleGenParser {

    String findValue;

    @Override
    final RuleFactory findFactory(String value) {
      this.findValue = value;

      if (value.equals("block")) {
        return block;
      }

      else {
        return null;
      }
    }

    @Override
    final Variant getVariant(String variantName) {
      throw new UnsupportedOperationException();
    }

  }

}