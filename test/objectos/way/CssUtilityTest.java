/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;

public class CssUtilityTest {

  @Test(description = "order by utility first")
  public void ordering01() {
    List<CssVariant.OfClassName> empty = List.of();
    List<CssVariant.OfClassName> hover = List.of(new CssVariant.Suffix(":hover"));
    List<CssVariant.OfClassName> active = List.of(new CssVariant.Suffix(":active"));

    List<Css.Rule> rules = new ArrayList<>();

    Css.Rule a1 = rule(Css.Key.APPEARANCE, "a-1", empty);
    rules.add(a1);

    Css.Rule a2 = rule(Css.Key.APPEARANCE, "a-2", empty);
    rules.add(a2);

    Css.Rule a1Active = rule(Css.Key.APPEARANCE, "a-2:active", active);
    rules.add(a1Active);

    Css.Rule a1Hover = rule(Css.Key.APPEARANCE, "a-1:hover", hover);
    rules.add(a1Hover);

    Css.Rule b1 = rule(Css.Key.DISPLAY, "b-1", empty);
    rules.add(b1);

    Css.Rule b2 = rule(Css.Key.DISPLAY, "b-2", empty);
    rules.add(b2);

    Collections.sort(rules);

    assertSame(rules.get(0), a1);
    assertSame(rules.get(1), a2);
    assertSame(rules.get(2), a1Hover);
    assertSame(rules.get(3), a1Active);
    assertSame(rules.get(4), b1);
    assertSame(rules.get(5), b2);
  }

  @Test
  public void writeClassName() {
    testClassName("m-0", ".m-0 {}\n");
    testClassName("sm:m-1", ".sm\\:m-1 {}\n");
    testClassName("2xl:m-2", ".\\32 xl\\:m-2 {}\n");
  }

  private Css.Rule rule(Css.Key key, String className, List<CssVariant.OfClassName> formats) {
    CssModifier modifier;
    modifier = new CssModifier(List.of(), formats);

    return new CssUtility(key, className, modifier, CssProperties.NOOP);
  }

  private void testClassName(String className, String expected) {
    StringBuilder out;
    out = new StringBuilder();

    Css.Rule rule;
    rule = rule(Css.Key.COLOR, className, List.of());

    rule.writeTo(out, CssIndentation.ROOT);

    assertEquals(out.toString(), expected);
  }

}
