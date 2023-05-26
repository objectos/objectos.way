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

import static br.com.objectos.css.Css.AFTER;
import static br.com.objectos.css.Css.BEFORE;
import static br.com.objectos.css.Css.HOVER;
import static br.com.objectos.css.Css.a;
import static br.com.objectos.css.Css.b;
import static br.com.objectos.css.Css.body;
import static br.com.objectos.css.Css.input;
import static br.com.objectos.css.Css.p;
import static br.com.objectos.css.Css.strong;
import static br.com.objectos.css.select.Combinator.CHILD;
import static br.com.objectos.css.select.SelectorFactory.any;
import static br.com.objectos.css.select.SelectorFactory.attr;
import static br.com.objectos.css.select.SelectorFactory.cn;
import static br.com.objectos.css.select.SelectorFactory.contains;
import static br.com.objectos.css.select.SelectorFactory.endsWith;
import static br.com.objectos.css.select.SelectorFactory.eq;
import static br.com.objectos.css.select.SelectorFactory.id;
import static br.com.objectos.css.select.SelectorFactory.lang;
import static br.com.objectos.css.select.SelectorFactory.or;
import static br.com.objectos.css.select.SelectorFactory.sel;
import static br.com.objectos.css.select.SelectorFactory.sp;
import static br.com.objectos.css.select.SelectorFactory.startsWith;
import static br.com.objectos.css.select.SelectorFactory.wsList;
import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SelectorFactoryTest {

  @Test
  public void attributeSelector() {
    test(sel(attr("foo")), "[foo]");
    test(sel(attr("foo"), attr("bar")), "[foo][bar]");
    test(sel(attr("foo"), attr("bar", eq("baz"))), "[foo][bar=baz]");
    test(sel(attr("foo"), cn("bar")), "[foo].bar");
    test(sel(attr("foo"), HOVER), "[foo]:hover");
    test(sel(attr("foo"), AFTER), "[foo]::after");
  }

  @Test
  public void attributeValueSelector() {
    test(attr("foo", eq("bar")), "[foo=bar]");
    test(attr("foo", contains("bar")), "[foo*=bar]");
    test(attr("foo", endsWith("bar")), "[foo$=bar]");
    test(attr("foo", lang("pt")), "[foo|=pt]");
    test(attr("foo", startsWith("bar")), "[foo^=bar]");
    test(attr("foo", wsList("bar")), "[foo~=bar]");

    test(sel(attr("a", eq("b")), id("bar")), "[a=b]#bar");
    test(sel(attr("a", eq("b")), attr("bar")), "[a=b][bar]");
    test(sel(attr("a", eq("b")), attr("bar", eq("baz"))), "[a=b][bar=baz]");
    test(sel(attr("a", eq("b")), cn("bar")), "[a=b].bar");
    test(sel(attr("a", eq("b")), HOVER), "[a=b]:hover");
    test(sel(attr("a", eq("b")), AFTER), "[a=b]::after");
  }

  @Test
  public void childSelector() {
    test(
        sel(id("a"), CHILD, id("b")),
        "#a > #b"
    );
    test(
        sel(id("a"), CHILD, id("b"), CHILD, id("c")),
        "#a > #b > #c"
    );
    test(
        sel(id("a"), CHILD, id("b"), CHILD, id("c"), CHILD, id("d")),
        "#a > #b > #c > #d"
    );
  }

  @Test
  public void classSelector() {
    test(sel(cn("foo")), ".foo");
    test(sel(cn("foo"), attr("bar")), ".foo[bar]");
    test(sel(cn("foo"), attr("bar", eq("baz"))), ".foo[bar=baz]");
    test(sel(cn("foo"), cn("bar")), ".foo.bar");
    test(sel(cn("foo"), HOVER), ".foo:hover");
    test(sel(cn("foo"), AFTER), ".foo::after");
    test(sel(id("a"), cn("foo"), AFTER), "#a.foo::after");
  }

  @Test
  public void descendantSelector() {
    test(
        sel(id("a"), sp(), id("b")),
        "#a #b"
    );
    test(
        sel(id("a"), sp(), id("b"), sp(), id("c")),
        "#a #b #c"
    );
    test(
        sel(id("a"), sp(), id("b"), sp(), id("c"), sp(), id("d")),
        "#a #b #c #d"
    );
  }

  @Test
  public void idSelector() {
    test(sel(id("foo")), "#foo");
    test(sel(id("foo"), attr("bar")), "#foo[bar]");
    test(sel(id("foo"), attr("bar", eq("baz"))), "#foo[bar=baz]");
    test(sel(id("foo"), cn("bar")), "#foo.bar");
    test(sel(id("foo"), HOVER), "#foo:hover");
    test(sel(id("foo"), AFTER), "#foo::after");
    test(sel(id("foo"), attr("bar", eq("baz"))), "#foo[bar=baz]");
    test(sel(id("foo"), cn("active"), attr("bar", startsWith("baz"))), "#foo.active[bar^=baz]");
  }

  @Test
  public void invalidSelectors() {
    testInvalid(
        "Cannot append type selector 'p' to another type selector 'a'",
        a, p
    );
    testInvalid(
        "Cannot append type selector 'p' to another type selector 'a'",
        body, or(), a, p
    );
    testInvalid(
        "Cannot append type selector 'a' to the universal selector '*'",
        any(), a
    );
  }

  @Test
  public void selectorList() {
    test(sel(b, or(), strong), "b, strong");
  }

  @Test
  public void typeSelector() {
    test(sel(input), "input");
    test(sel(input, id("foo")), "input#foo");
    test(sel(input, cn("foo")), "input.foo");
    test(sel(input, attr("hidden")), "input[hidden]");
    test(sel(input, attr("type", eq("text"))), "input[type=text]");
    test(sel(input, HOVER), "input:hover");
    test(sel(input, AFTER), "input::after");
  }

  @Test
  public void universalSelector() {
    test(sel(any()), "*");
    test(sel(any(), or(), AFTER), "*, ::after");
    test(sel(any(), or(), any(), AFTER), "*, *::after");
    test(sel(any(), or(), AFTER, or(), BEFORE), "*, ::after, ::before");
  }

  private void test(Selector selector, String expected) {
    assertEquals(selector.toString(), expected);
  }

  private void testInvalid(String message, SelectorElement... elements) {
    try {
      sel(elements);
      Assert.fail();
    } catch (InvalidSelectorException expected) {
      assertEquals(expected.getMessage(), message);
    }
  }

}
