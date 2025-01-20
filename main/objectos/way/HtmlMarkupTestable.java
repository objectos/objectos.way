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

import java.util.Objects;
import java.util.function.Consumer;

final class HtmlMarkupTestable extends HtmlMarkupElements implements Html.Markup {

  private final Testable.Formatter formatter;

  HtmlMarkupTestable() {
    this(Testable.Formatter.create());
  }

  HtmlMarkupTestable(Testable.Formatter formatter) {
    this.formatter = formatter;
  }

  @Override
  public final String testableField(String name, String value) {
    formatter.field(name, value);

    return value;
  }

  @Override
  public final String testableFieldName(String name) {
    formatter.fieldName(name);

    return name;
  }

  @Override
  public final String testableFieldValue(String value) {
    formatter.fieldValue(value);

    return value;
  }

  @Override
  public final String testableHeading1(String value) {
    formatter.heading1(value);

    return value;
  }

  @Override
  public final String toString() {
    return formatter.toString();
  }

  //
  //
  //

  @Override
  public final void doctype() {
    // noop
  }

  @Override
  public final Html.Instruction.OfAttribute css(String value) {
    return Html.ATTRIBUTE;
  }

  @Override
  public final Html.Instruction.OfAttribute dataFrame(String name) {
    return Html.ATTRIBUTE;
  }

  @Override
  public final Html.Instruction.OfAttribute dataFrame(String name, String value) {
    return Html.ATTRIBUTE;
  }

  @Override
  public final Html.Instruction.OfDataOn dataOnClick(Consumer<Script> script) {
    return Html.ATTRIBUTE;
  }

  @Override
  public final Html.Instruction.OfDataOn dataOnInput(Consumer<Script> script) {
    return Html.ATTRIBUTE;
  }

  @Override
  public final Html.Instruction.OfElement element(Html.ElementName name, Html.Instruction... contents) {
    return Html.ELEMENT;
  }

  @Override
  public final Html.Instruction.OfElement flatten(Html.Instruction... contents) {
    Objects.requireNonNull(contents, "contents == null");

    for (int i = 0; i < contents.length; i++) {
      Check.notNull(contents[i], "contents[", i, "] == null");
    }

    return Html.ELEMENT;
  }

  @Override
  public final Html.Instruction.NoOp noop() {
    return Html.NOOP;
  }

  @Override
  public final Html.Instruction.OfElement raw(String text) {
    Objects.requireNonNull(text, "text == null");

    return Html.ELEMENT;
  }

  @Override
  public final Html.Instruction.OfFragment renderFragment(Html.Fragment.Of0 fragment) {
    Objects.requireNonNull(fragment, "fragment == null");

    fragment.invoke();

    return Html.FRAGMENT;
  }

  @Override
  public final <T1> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of1<T1> fragment, T1 arg1) {
    Objects.requireNonNull(fragment, "fragment == null");

    fragment.invoke(arg1);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1, T2> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of2<T1, T2> fragment, T1 arg1, T2 arg2) {
    Objects.requireNonNull(fragment, "fragment == null");

    fragment.invoke(arg1, arg2);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1, T2, T3> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
    Objects.requireNonNull(fragment, "fragment == null");

    fragment.invoke(arg1, arg2, arg3);

    return Html.FRAGMENT;
  }

  @Override
  public final <T1, T2, T3, T4> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
    Objects.requireNonNull(fragment, "fragment == null");

    fragment.invoke(arg1, arg2, arg3, arg4);

    return Html.FRAGMENT;
  }

  @Override
  public final Html.Instruction.OfFragment renderComponent(Html.Component component) {
    Objects.requireNonNull(component, "component == null");

    component.renderHtml(this);

    return Html.FRAGMENT;
  }

  @Override
  public final Html.Instruction.OfElement text(String text) {
    return Html.ELEMENT;
  }

  @Override
  final void ambiguous(HtmlAmbiguous name, String value) {
    // noop
  }

  @Override
  final Html.Instruction.OfElement element(Html.ElementName name, String text) {
    return Html.ELEMENT;
  }

  @Override
  final Html.Instruction.OfAttribute attribute0(Html.AttributeName name) {
    return Html.ATTRIBUTE;
  }

  @Override
  final Html.AttributeOrNoOp attribute0(Html.AttributeName name, Object value) {
    return Html.ATTRIBUTE;
  }

}