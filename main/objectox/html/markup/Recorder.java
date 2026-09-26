/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.markup;

import objectos.html.markup.AttributeMarkup;
import objectos.html.markup.BooleanAttribute;
import objectos.html.markup.Fragment0;
import objectos.html.markup.Fragment1;
import objectos.html.markup.Fragment2;
import objectos.html.markup.Fragment3;
import objectos.html.markup.Fragment4;
import objectos.html.markup.FragmentMarkup;
import objectos.html.markup.NoOpMarkup;
import objectos.html.markup.RawMarkup;
import objectos.html.markup.RegularElementValue;
import objectos.html.markup.TestableMarkup;
import objectos.html.markup.TextMarkup;
import objectos.html.markup.VoidElementValue;
import objectos.script.JsAction;
import objectox.html.HtmlImpl;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;

public final class Recorder {

  private final AttributeRecorder attributeRecorder;

  private final BooleanAttributeRecorder booleanAttributeRecorder;

  private final DoctypeRecorder doctypeRecorder;

  private final DocumentCompiler documentCompiler;

  private final FragmentRecorder fragmentRecorder;

  private final RawRecorder rawRecorder;

  private final RegularElementRecorder regularElementRecorder;

  private final TestableRecorder testableRecorder;

  private final TextRecorder textRecorder;

  private final VoidElementRecorder voidElementRecorder;

  Recorder(
      AttributeRecorder attributeRecorder,
      BooleanAttributeRecorder booleanAttributeRecorder,
      DoctypeRecorder doctypeRecorder,
      DocumentCompiler documentCompiler,
      FragmentRecorder fragmentRecorder,
      RawRecorder rawRecorder,
      RegularElementRecorder regularElementRecorder,
      TestableRecorder testableRecorder,
      TextRecorder textRecorder,
      VoidElementRecorder voidElementRecorder
  ) {
    this.attributeRecorder = attributeRecorder;
    this.booleanAttributeRecorder = booleanAttributeRecorder;
    this.doctypeRecorder = doctypeRecorder;
    this.documentCompiler = documentCompiler;
    this.fragmentRecorder = fragmentRecorder;
    this.rawRecorder = rawRecorder;
    this.regularElementRecorder = regularElementRecorder;
    this.testableRecorder = testableRecorder;
    this.textRecorder = textRecorder;
    this.voidElementRecorder = voidElementRecorder;
  }

  public static Recorder create() {
    return of(new HtmlSink());
  }

  public static Recorder of(HtmlSink sink) {
    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentAttributeRecorder fragmentAttributeRecorder;
    fragmentAttributeRecorder = new FragmentAttributeRecorder(sink, valueRecorder);

    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final FragmentRecorder fragmentRecorder;
    fragmentRecorder = new FragmentRecorder(sink);

    final ElementPreRecorder elementPreRecorder;
    elementPreRecorder = new ElementPreRecorder(attributeRecorder, booleanAttributeRecorder, fragmentRecorder);

    final ElementAttributesRecorder elementAttributesRecorder;
    elementAttributesRecorder = new ElementAttributesRecorder(fragmentAttributeRecorder, valueRecorder);

    final FragmentChildRecorder fragmentChildRecorder;
    fragmentChildRecorder = new FragmentChildRecorder(sink, valueRecorder);

    return new Recorder(
        attributeRecorder,

        booleanAttributeRecorder,

        new DoctypeRecorder(sink),

        new DocumentCompiler(sink),

        fragmentRecorder,

        new RawRecorder(sink),

        new RegularElementRecorder(
            sink,

            elementAttributesRecorder,

            new ElementChildrenRecorder(
                fragmentChildRecorder,

                valueRecorder
            ),

            elementPreRecorder
        ),

        new TestableRecorder(sink),

        new TextRecorder(sink),

        new VoidElementRecorder(sink, elementAttributesRecorder, elementPreRecorder)
    );
  }

  public final AttributePointer attribute(AttributeName name, JsAction value) {
    return attributeRecorder.record(name, value);
  }

  public final AttributePointer attribute(AttributeName name, String value) {
    return attributeRecorder.record(name, value);
  }

  public final AttributePointer attribute(String name, JsAction action) {
    return attributeRecorder.record(name, action);
  }

  public final AttributePointer attribute(String name, String value) {
    return attributeRecorder.record(name, value);
  }

  public final AttributePointer booleanAttribute(AttributeName name) {
    return booleanAttributeRecorder.record(name);
  }

  public final AttributePointer booleanAttribute(BooleanAttribute value) {
    return booleanAttributeRecorder.record(value);
  }

  public final AttributePointer booleanAttribute(String name) {
    return booleanAttributeRecorder.record(name);
  }

  public final HtmlImpl compile() {
    return documentCompiler.compile();
  }

  public final AttributeMarkup css(String value) {
    return attributeRecorder.css(value);
  }

  public final void doctype() {
    doctypeRecorder.record();
  }

  public final FragmentMarkup f(Fragment0 fragment) {
    return fragmentRecorder.record(fragment);
  }

  public final <T1> FragmentMarkup f(Fragment1<T1> fragment, T1 arg1) {
    return fragmentRecorder.record(fragment, arg1);
  }

  public final <T1, T2> FragmentMarkup f(Fragment2<T1, T2> fragment, T1 arg1, T2 arg2) {
    return fragmentRecorder.record(fragment, arg1, arg2);
  }

  public final <T1, T2, T3> FragmentMarkup f(Fragment3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
    return fragmentRecorder.record(fragment, arg1, arg2, arg3);
  }

  public final <T1, T2, T3, T4> FragmentMarkup f(Fragment4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
    return fragmentRecorder.record(fragment, arg1, arg2, arg3, arg4);
  }

  public final NoOpMarkup noop() {
    return NoOpMarkupPojo.INSTANCE;
  }

  public final RawMarkup raw(String value) {
    return rawRecorder.record(value);
  }

  public final ElementPointer regularElement(ElementName name, RegularElementValue... values) {
    return regularElementRecorder.record(name, values);
  }

  public final ElementPointer regularElement(ElementName name, String text) {
    final TextPointer pointer;
    pointer = textRecorder.record(text);

    return regularElementRecorder.record(name, pointer);
  }

  public final ElementPointer regularElement(String name, RegularElementValue... values) {
    return regularElementRecorder.record(name, values);
  }

  public final ElementPointer regularElement(String name, String text) {
    final TextPointer pointer;
    pointer = textRecorder.record(text);

    return regularElementRecorder.record(name, pointer);
  }

  public final TestableMarkup tbr() {
    return testableRecorder.newLine();
  }

  public final TestableMarkup tcell(int width, String value) {
    return testableRecorder.cell(width, value);
  }

  public final TestableMarkup th1(String value) {
    return testableRecorder.heading(1, value);
  }

  public final TestableMarkup th2(String value) {
    return testableRecorder.heading(2, value);
  }

  public final TestableMarkup th3(String value) {
    return testableRecorder.heading(3, value);
  }

  public final TestableMarkup th4(String value) {
    return testableRecorder.heading(4, value);
  }

  public final TestableMarkup th5(String value) {
    return testableRecorder.heading(5, value);
  }

  public final TestableMarkup th6(String value) {
    return testableRecorder.heading(6, value);
  }

  public final TestableMarkup tprop(String key, String value) {
    return testableRecorder.property(key, value);
  }

  public final TestableMarkup tpropkey(String value) {
    return testableRecorder.propKey(value);
  }

  public final TestableMarkup tpropval(String value) {
    return testableRecorder.propValue(value);
  }

  public final TextMarkup text(String value) {
    return textRecorder.record(value);
  }

  public final ElementPointer voidElement(ElementName name, VoidElementValue... values) {
    return voidElementRecorder.record(name, values);
  }

  public final ElementPointer voidElement(String name, VoidElementValue... values) {
    return voidElementRecorder.record(name, values);
  }

  public final HtmlSink unwrap() {
    return documentCompiler.unwrap();
  }

}
