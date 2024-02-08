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

import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class WayStyleGenParserTest {

  @Test
  public void responsive() {
    WayStyleGenParser parser;
    parser = new WayStyleGenParser();

    parser.parse("sm:block");

    Map<String, Rule> rules;
    rules = parser.rules;

    assertEquals(rules.size(), 1);

    Rule rule;
    rule = rules.values().iterator().next();

    List<Variant> variants;
    variants = rule.variants;

    assertEquals(variants.size(), 1);
    assertEquals(variants.get(0), new Breakpoint(0, "sm", "640px"));
  }

  @Test
  public void responsive_multiple() {
    WayStyleGenParser parser;
    parser = new WayStyleGenParser();

    parser.parse("sm:block md:m-0");

    Map<String, Rule> rules;
    rules = parser.rules;

    assertEquals(rules.size(), 2);
  }

}