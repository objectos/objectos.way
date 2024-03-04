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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import objectos.css.Variant.AppendTo;
import org.testng.annotations.Test;

public class RuleTest {

  @Test(description = "order by utility first")
  public void ordering01() {
    List<Variant> empty = List.of();
    List<Variant> hover = List.of(new AppendTo(1, ":hover"));
    List<Variant> active = List.of(new AppendTo(2, ":active"));

    List<Rule> rules = new ArrayList<>();

    Rule a1 = new Rule(0, "a-1", empty);
    rules.add(a1);

    Rule a2 = new Rule(0, "a-2", empty);
    rules.add(a2);

    Rule a1Active = new Rule(0, "a-2:active", active);
    rules.add(a1Active);

    Rule a1Hover = new Rule(0, "a-1:hover", hover);
    rules.add(a1Hover);

    Rule b1 = new Rule(1, "b-1", empty);
    rules.add(b1);

    Rule b2 = new Rule(1, "b-2", empty);
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
    testClassName("m-0", ".m-0 {  }\n");
    testClassName("sm:m-1", ".sm\\:m-1 {  }\n");
    testClassName("2xl:m-2", ".\\32xl\\:m-2 {  }\n");
  }

  private void testClassName(String className, String expected) {
    StringBuilder out;
    out = new StringBuilder();

    Rule rule;
    rule = new Rule(0, className, List.of());

    rule.writeTo(out, Indentation.ROOT);

    assertEquals(out.toString(), expected);
  }

}
