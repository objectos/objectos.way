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
package objectos.css.select;

import static objectos.css.select.FakeSelectable.named;
import static objectos.css.select.SelectorAssertion.assertThat;
import static objectos.css.select.SelectorFactory.contains;
import static objectos.css.select.SelectorFactory.endsWith;
import static objectos.css.select.SelectorFactory.eq;
import static objectos.css.select.SelectorFactory.lang;
import static objectos.css.select.SelectorFactory.startsWith;
import static objectos.css.select.SelectorFactory.wsList;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class AttributeSelectorTest extends AbstractCssSelectTest {

  @Test
  public void matches() {
    assertThat(attribute("foo"))
        .matches(named("x").withAttribute("foo", ""))
        .matches(named("x").withAttribute("foo", "abc"))
        .doesNotMatch(named("foo").withAttribute("bar", "foo"));
  }

  @Test
  public void matches_EQUALS() {
    assertThat(attribute("foo", eq("bar")))
        .matches(named("x").withAttribute("foo", "bar"))
        .doesNotMatch(named("foo").withAttribute("foo", "xbar"))
        .doesNotMatch(named("foo").withAttribute("foo", "bar oi"));
  }

  @Test
  public void toStringTest() {
    assertEquals(attribute("foo").toString(), "[foo]");
  }

  @Test
  public void toStringTest_withValue() {
    assertEquals(attribute("foo", eq("bar")).toString(), "[foo=bar]");
    assertEquals(attribute("foo", contains("bar")).toString(), "[foo*=bar]");
    assertEquals(attribute("foo", endsWith("bar")).toString(), "[foo$=bar]");
    assertEquals(attribute("foo", lang("pt")).toString(), "[foo|=pt]");
    assertEquals(attribute("foo", startsWith("bar")).toString(), "[foo^=bar]");
    assertEquals(attribute("foo", wsList("bar")).toString(), "[foo~=bar]");
  }

}