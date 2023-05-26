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
package br.com.objectos.css.select;

import static br.com.objectos.css.Css.sel;
import static br.com.objectos.css.select.FakeSelectable.named;
import static br.com.objectos.css.select.SelectorFactory.attr;
import static br.com.objectos.css.select.SelectorFactory.eq;

import org.testng.annotations.Test;

public class CompoundSelectorTest extends AbstractCssSelectTest {

  @Test
  public void matches() {
    assertMatches(
        sel(a, id("id")),
        named("a").withId("id")
    );
    assertDoesNotMatch(
        sel(a, id("id")),
        named("a").withId("foo")
    );

    assertMatches(
        sel(a, id("id"), attr("type", eq("input"))),
        named("a").withId("id").withAttribute("type", "input")
    );
    assertDoesNotMatch(
        sel(a, id("id"), attr("type", eq("input"))),
        named("a").withId("id").withAttribute("type", "x")
    );
  }

}
