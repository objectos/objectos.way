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

import objectos.html.Attribute;
import objectos.html.BooleanAttribute;
import objectos.html.markup.AmbiguousMarkup;
import objectos.html.markup.AttributeMarkup;
import objectos.html.markup.ElementMarkup;
import objectos.html.markup.Fragment0;
import objectos.html.markup.Fragment1;
import objectos.html.markup.Fragment2;
import objectos.html.markup.Fragment3;
import objectos.html.markup.Fragment4;
import objectos.html.markup.FragmentMarkup;
import objectos.html.markup.NoOpMarkup;
import objectos.html.markup.RawMarkup;
import objectos.html.markup.RegularElementValue;
import objectos.html.markup.TextMarkup;
import objectos.html.markup.VoidElementValue;
import objectos.script.JsAction;
import objectox.html.AmbiguousName;
import objectox.html.HtmlPojo;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;

public final class Recorder {

  private final AmbiguousRecorder ambiguousRecorder;

  private final AttributeRecorder attributeRecorder;

  private final BooleanAttributeRecorder booleanAttributeRecorder;

  private final DoctypeRecorder doctypeRecorder;

  private final DocumentCompiler documentCompiler;

  private final FragmentRecorder fragmentRecorder;

  private final RawRecorder rawRecorder;

  private final RegularElementRecorder regularElementRecorder;

  private final TextElementRecorder textElementRecorder;

  private final TextRecorder textRecorder;

  private final VoidElementRecorder voidElementRecorder;

  Recorder(
      AmbiguousRecorder ambiguousRecorder,
      AttributeRecorder attributeRecorder,
      BooleanAttributeRecorder booleanAttributeRecorder,
      DoctypeRecorder doctypeRecorder,
      DocumentCompiler documentCompiler,
      FragmentRecorder fragmentRecorder,
      RawRecorder rawRecorder,
      RegularElementRecorder regularElementRecorder,
      TextElementRecorder textElementRecorder,
      TextRecorder textRecorder,
      VoidElementRecorder voidElementRecorder
  ) {
    this.ambiguousRecorder = ambiguousRecorder;
    this.attributeRecorder = attributeRecorder;
    this.booleanAttributeRecorder = booleanAttributeRecorder;
    this.doctypeRecorder = doctypeRecorder;
    this.documentCompiler = documentCompiler;
    this.fragmentRecorder = fragmentRecorder;
    this.rawRecorder = rawRecorder;
    this.regularElementRecorder = regularElementRecorder;
    this.textElementRecorder = textElementRecorder;
    this.textRecorder = textRecorder;
    this.voidElementRecorder = voidElementRecorder;
  }

  public static Recorder create() {
    return of(new HtmlSink());
  }

  public static Recorder of(HtmlSink sink) {
    final AmbiguousAttributeRecorder ambiguousAttributeRecorder;
    ambiguousAttributeRecorder = new AmbiguousAttributeRecorder(sink);

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentAttributeRecorder fragmentAttributeRecorder;
    fragmentAttributeRecorder = new FragmentAttributeRecorder(sink, ambiguousAttributeRecorder, valueRecorder);

    final ElementAttributesRecorder elementAttributesRecorder;
    elementAttributesRecorder = new ElementAttributesRecorder(ambiguousAttributeRecorder, fragmentAttributeRecorder, valueRecorder);

    final AmbiguousChildRecorder ambiguousChildRecorder;
    ambiguousChildRecorder = new AmbiguousChildRecorder(sink);

    final FragmentChildRecorder fragmentChildRecorder;
    fragmentChildRecorder = new FragmentChildRecorder(sink, ambiguousChildRecorder, valueRecorder);

    return new Recorder(
        new AmbiguousRecorder(sink),

        new AttributeRecorder(sink),

        new BooleanAttributeRecorder(sink),

        new DoctypeRecorder(sink),

        new DocumentCompiler(sink),

        new FragmentRecorder(sink),

        new RawRecorder(sink),

        new RegularElementRecorder(
            sink,

            elementAttributesRecorder,

            new ElementChildrenRecorder(
                ambiguousChildRecorder,

                fragmentChildRecorder,

                valueRecorder
            )
        ),

        new TextElementRecorder(sink),

        new TextRecorder(sink),

        new VoidElementRecorder(sink, elementAttributesRecorder)
    );
  }

  public final AmbiguousMarkup ambiguous(AmbiguousName name, String text) {
    return ambiguousRecorder.record(name, text);
  }

  public final AttributeMarkup attribute(Attribute value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final AttributePointer attribute(AttributeName name, JsAction value) {
    return attributeRecorder.record(name, value);
  }

  public final AttributePointer attribute(AttributeName name, String value) {
    return attributeRecorder.record(name, value);
  }

  public final AttributeMarkup attribute(String name, JsAction action) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final AttributeMarkup attribute(String name, String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final AttributePointer booleanAttribute(AttributeName name) {
    return booleanAttributeRecorder.record(name);
  }

  public final AttributeMarkup booleanAttribute(BooleanAttribute value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final AttributeMarkup booleanAttribute(String name) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final HtmlPojo compile() {
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

  public final ElementMarkup regularElement(ElementName name, RegularElementValue... values) {
    return regularElementRecorder.record(name, values);
  }

  public final ElementMarkup regularElement(String name, RegularElementValue... values) {
    return regularElementRecorder.record(name, values);
  }

  public final String testableCell(String value, int width) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String testableField(String name, String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String testableFieldName(String name) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String testableFieldValue(String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String testableH1(String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String testableH2(String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String testableH3(String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String testableH4(String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String testableH5(String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final String testableH6(String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final NoOpMarkup testableNewLine() {
    throw new UnsupportedOperationException("Implement me");
  }

  public final TextMarkup text(String value) {
    return textRecorder.record(value);
  }

  public final ElementMarkup textElement(ElementName name, String value) {
    return textElementRecorder.record(name, value);
  }

  public final ElementMarkup textElement(String name, String text) {
    return textElementRecorder.record(name, text);
  }

  public final ElementMarkup voidElement(ElementName name, VoidElementValue... values) {
    return voidElementRecorder.record(name, values);
  }

  public final ElementMarkup voidElement(String name, VoidElementValue... values) {
    return voidElementRecorder.record(name, values);
  }

}
