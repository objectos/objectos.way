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
package br.com.objectos.css;

import static br.com.objectos.css.NestableSymbol.DESCENDANT;
import static br.com.objectos.css.NestableSymbol.PARENT;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class SelectorTest {

  @Test
  public void nest() {
    assertEquals(selector(".parent")
        .nest(SimpleNestable.of(PARENT, DESCENDANT, new Ident(".child"))),
      selector(".parent .child"));
  }

  @Test
  public void nest_parent_selector() {
    assertEquals(selector(".parent")
        .nest(SimpleNestable.of(PARENT, new Ident(".child"))),
      selector(".parent.child"));
  }

  @Test
  public void nest_parent_is_list() {
    assertEquals(list(selector("#a"), selector("#b"))
        .nest(SimpleNestable.of(PARENT, DESCENDANT, new Ident(".c"))),
      list(selector("#a .c"), selector("#b .c")));
  }

  @Test
  public void nest_child_is_list() {
    SimpleNestable b = SimpleNestable.of(PARENT, DESCENDANT, new Ident(".b"));
    SimpleNestable c = SimpleNestable.of(PARENT, DESCENDANT, new Ident(".c"));
    assertEquals(selector("#a").nest(ListNestable.of(b, c)),
      list(selector("#a .b"), selector("#a .c")));
  }

  @Test
  public void nest_child_is_list_withparent() {
    SimpleNestable b = SimpleNestable.of(PARENT, new Ident("::after"));
    SimpleNestable c = SimpleNestable.of(PARENT, new Ident("::before"));
    assertEquals(selector("#a").nest(ListNestable.of(b, c)),
      list(selector("#a::after"), selector("#a::before")));
  }

  @Test
  public void nest_parent_is_descendant_selector() {
    SimpleNestable after = SimpleNestable.of(PARENT, new Ident("::after"));
    assertEquals(selector("#a #b").nest(after), selector("#a #b::after"));
  }

  @Test
  public void nest_parent_and_child_are_lists() {
    SimpleNestable after = SimpleNestable.of(PARENT, new Ident("::after"));
    SimpleNestable before = SimpleNestable.of(PARENT, new Ident("::before"));
    assertEquals(
      list(selector("#a"), selector("#b")).nest(ListNestable.of(after, before)),
      list(list(selector("#a::after"), selector("#b::after")),
        list(selector("#a::before"), selector("#b::before")))
    );
  }

  @Test
  public void nest_child_is_combinator() {
    assertEquals(selector(".parent")
        .nest(SimpleNestable.of(PARENT, Combinator.CHILD, new Ident(".child"))),
      selector(".parent > .child"));
  }

  private ListSelector list(Selector first, Selector second) {
    return new ListSelector(first, second);
  }

  private SimpleSelector selector(String value) {
    return new SimpleSelector(value);
  }

}