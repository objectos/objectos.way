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

import static objectos.css.select.Combinator.ADJACENT_SIBLING;
import static objectos.css.select.Combinator.CHILD;
import static objectos.css.select.Combinator.DESCENDANT;
import static objectos.css.select.Combinator.GENERAL_SIBLING;
import static objectos.css.select.Combinator.LIST;

import objectos.css.select.Selector.Builder;
import objectos.lang.Check;

public class SelectorFactory {

  private SelectorFactory() {}

  public static UniversalSelector any() {
    return UniversalSelector.getInstance();
  }

  public static AttributeSelector attr(String name) {
    Check.notNull(name, "name == null");
    return new AttributeSelector(name);
  }

  public static AttributeValueSelector attr(String name, AttributeValueElement element) {
    AttributeSelector previous = attr(name);
    Check.notNull(element, "element == null");
    return new AttributeValueSelector(previous, element);
  }

  public static AttributeValueSelector attr(String name, AttributeValueOperator op, String value) {
    AttributeSelector previous = attr(name);
    Check.notNull(op, "op == null");
    Check.notNull(value, "value == null");
    return new AttributeValueSelector(previous, op, value);
  }

  public static ClassSelector cn(String className) {
    Check.notNull(className, "className == null");
    return new ClassSelector(className);
  }

  public static AttributeValueElement contains(String value) {
    return AttributeValueOperator.CONTAINS.withValue(value);
  }

  public static ClassSelector dot(String className) {
    Check.notNull(className, "className == null");
    return new ClassSelector(className);
  }

  public static AttributeValueElement endsWith(String value) {
    return AttributeValueOperator.ENDS_WITH.withValue(value);
  }

  public static AttributeValueElement eq(String value) {
    return AttributeValueOperator.EQUALS.withValue(value);
  }

  public static Combinator gt() {
    return CHILD;
  }

  public static IdSelector id(String id) {
    Check.notNull(id, "id == null");
    return new IdSelector(id);
  }

  public static AttributeValueElement lang(String value) {
    return AttributeValueOperator.HYPHEN.withValue(value);
  }

  public static SelectorList list(Selector... selectors) {
    return new SelectorList(selectors);
  }

  public static Combinator or() {
    return LIST;
  }

  public static Combinator plus() {
    return ADJACENT_SIBLING;
  }

  public static Selector sel(Selector selector) {
    return Check.notNull(selector, "selector == null");
  }

  public static Selector sel(SelectorElement... elements) {
    Check.notNull(elements, "elements == null");
    Builder b = Selector.builder();

    for (int i = 0; i < elements.length; i++) {
      SelectorElement e = Check.notNull(elements[i], "elements[" + i + "] == null");
      e.acceptSelectorBuilderDsl(b);
    }

    return b.build();
  }

  public static Selector sel(
      SelectorElement e1,
      SelectorElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = Selector.builder();
    e1.acceptSelectorBuilderDsl(b);
    e2.acceptSelectorBuilderDsl(b);
    return b.build();
  }

  public static Selector sel(
      SelectorElement e1,
      SelectorElement e2,
      SelectorElement e3) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Builder b = Selector.builder();
    e1.acceptSelectorBuilderDsl(b);
    e2.acceptSelectorBuilderDsl(b);
    e3.acceptSelectorBuilderDsl(b);
    return b.build();
  }

  public static Selector sel(
      SelectorElement e1,
      SelectorElement e2,
      SelectorElement e3,
      SelectorElement e4) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Builder b = Selector.builder();
    e1.acceptSelectorBuilderDsl(b);
    e2.acceptSelectorBuilderDsl(b);
    e3.acceptSelectorBuilderDsl(b);
    e4.acceptSelectorBuilderDsl(b);
    return b.build();
  }

  public static Selector sel(
      SelectorElement e1,
      SelectorElement e2,
      SelectorElement e3,
      SelectorElement e4,
      SelectorElement e5) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Builder b = Selector.builder();
    e1.acceptSelectorBuilderDsl(b);
    e2.acceptSelectorBuilderDsl(b);
    e3.acceptSelectorBuilderDsl(b);
    e4.acceptSelectorBuilderDsl(b);
    e5.acceptSelectorBuilderDsl(b);
    return b.build();
  }

  public static Selector sel(
      SelectorElement e1,
      SelectorElement e2,
      SelectorElement e3,
      SelectorElement e4,
      SelectorElement e5,
      SelectorElement e6) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Builder b = Selector.builder();
    e1.acceptSelectorBuilderDsl(b);
    e2.acceptSelectorBuilderDsl(b);
    e3.acceptSelectorBuilderDsl(b);
    e4.acceptSelectorBuilderDsl(b);
    e5.acceptSelectorBuilderDsl(b);
    e6.acceptSelectorBuilderDsl(b);
    return b.build();
  }

  public static Selector sel(
      SelectorElement e1,
      SelectorElement e2,
      SelectorElement e3,
      SelectorElement e4,
      SelectorElement e5,
      SelectorElement e6,
      SelectorElement e7) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Builder b = Selector.builder();
    e1.acceptSelectorBuilderDsl(b);
    e2.acceptSelectorBuilderDsl(b);
    e3.acceptSelectorBuilderDsl(b);
    e4.acceptSelectorBuilderDsl(b);
    e5.acceptSelectorBuilderDsl(b);
    e6.acceptSelectorBuilderDsl(b);
    e7.acceptSelectorBuilderDsl(b);
    return b.build();
  }

  public static Selector sel(
      SelectorElement e1,
      SelectorElement e2,
      SelectorElement e3,
      SelectorElement e4,
      SelectorElement e5,
      SelectorElement e6,
      SelectorElement e7,
      SelectorElement e8) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Builder b = Selector.builder();
    e1.acceptSelectorBuilderDsl(b);
    e2.acceptSelectorBuilderDsl(b);
    e3.acceptSelectorBuilderDsl(b);
    e4.acceptSelectorBuilderDsl(b);
    e5.acceptSelectorBuilderDsl(b);
    e6.acceptSelectorBuilderDsl(b);
    e7.acceptSelectorBuilderDsl(b);
    e8.acceptSelectorBuilderDsl(b);
    return b.build();
  }

  // ClassSelector

  public static Selector sel(
      SelectorElement e1,
      SelectorElement e2,
      SelectorElement e3,
      SelectorElement e4,
      SelectorElement e5,
      SelectorElement e6,
      SelectorElement e7,
      SelectorElement e8,
      SelectorElement e9) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Check.notNull(e9, "e9 == null");
    Builder b = Selector.builder();
    e1.acceptSelectorBuilderDsl(b);
    e2.acceptSelectorBuilderDsl(b);
    e3.acceptSelectorBuilderDsl(b);
    e4.acceptSelectorBuilderDsl(b);
    e5.acceptSelectorBuilderDsl(b);
    e6.acceptSelectorBuilderDsl(b);
    e7.acceptSelectorBuilderDsl(b);
    e8.acceptSelectorBuilderDsl(b);
    e9.acceptSelectorBuilderDsl(b);
    return b.build();
  }

  public static Combinator sp() {
    return DESCENDANT;
  }

  public static UniversalSelector star() {
    return UniversalSelector.getInstance();
  }

  public static AttributeValueElement startsWith(String value) {
    return AttributeValueOperator.STARTS_WITH.withValue(value);
  }

  public static Combinator tilde() {
    return GENERAL_SIBLING;
  }

  public static AttributeValueElement wsList(String value) {
    return AttributeValueOperator.WS_LIST.withValue(value);
  }

}
