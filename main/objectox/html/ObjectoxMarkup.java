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
package objectox.html;

import objectos.html.Html;
import objectos.html.Markup;
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
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlSink;
import objectox.html.markup.Recorder;

public final class ObjectoxMarkup implements Markup {

  private final Recorder recorder;

  private ObjectoxMarkup(Recorder recorder) {
    this.recorder = recorder;
  }

  public static ObjectoxMarkup create() {
    final Recorder recorder;
    recorder = Recorder.create();

    return new ObjectoxMarkup(recorder);
  }

  public static ObjectoxMarkup of(HtmlSink sink) {
    final Recorder recorder;
    recorder = Recorder.of(sink);

    return new ObjectoxMarkup(recorder);
  }

  /// Encodes a boolean attribute with the specified name.
  ///
  /// @param name the attribute name
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup attr(String name) {
    return recorder.booleanAttribute(name);
  }

  /// Encodes an attribute with the specified name and action.
  ///
  /// @param name the attribute name
  /// @param action the script action
  ///
  /// @return an object representing the encoded attribute
  public final AttributeMarkup attr(String name, JsAction action) {
    return recorder.attribute(name, action);
  }

  /// Encodes an attribute with the specified name and value.
  ///
  /// @param name the attribute name
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup attr(String name, String value) {
    return recorder.attribute(name, value);
  }

  /// Encodes an HTML element with the specified name and values.
  ///
  /// @param name the element name
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup elem(String name, RegularElementValue... values) {
    return recorder.regularElement(name, values);
  }

  /// Encodes an HTML element with the specified name and text.
  ///
  /// @param name the element name
  /// @param text the text value of this element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup elem(String name, String text) {
    return recorder.textElement(name, text);
  }

  /// Encodes an HTML void element with the specified name and values.
  ///
  /// @param name the element name
  /// @param values the encoded attributes
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup voidElem(String name, VoidElementValue... values) {
    return recorder.voidElement(name, values);
  }

  /// Encodes the `<!DOCTYPE html>` doctype.
  @Override
  public final void doctype() {
    recorder.doctype();
  }

  @Override
  public final Html compile() {
    return recorder.compile();
  }

  final HtmlSink unwrap() {
    return recorder.unwrap();
  }

  // BEGIN generated code

  //
  // WAY
  //

  // @formatter:off

  /// Encodes the `class` attribute by processing the specified value.
  ///
  /// This method is designed to work with Java text blocks. It first removes
  /// any leading and trailing whitespace. Additionally, any sequence of
  /// consecutive whitespace characters is replaced by a single space
  /// character.
  ///
  /// For example, the following invocation:
  ///
  /// ```java
  /// css("""
  ///     display:inline-flex
  ///     justify-content:center
  ///
  ///     background-color:blue-500
  ///     """);
  /// ```
  ///
  /// Produces the same result as invoking
  /// `className("display:inline-flex justify-content:center background-color:blue-500")`.
  ///
  /// @param value the text block containing class names, possibly spread across multiple lines
  /// @return an object representing the encoded attribute.
  @Override
  public final AttributeMarkup css(String value) {
    return recorder.css(value);
  }

  /// Encodes the specified fragment as part of this document.
  ///
  /// The following Objectos HTML component:
  ///
  /// {@snippet file = "objectos/html/MarkupJavadoc.java" region = "f0"}
  ///
  /// Generates the following HTML:
  ///
  /// ```html
  /// <ul>
  /// <li>Mon</li>
  /// <li>Wed</li>
  /// <li>Fri</li>
  /// </ul>
  /// ```
  ///
  /// @param fragment the fragment to include
  /// @return an instruction representing the fragment
  @Override
  public final FragmentMarkup f(Fragment0 fragment){
    return recorder.f(fragment);
  }

  /// Encodes the specified fragment as part of this document.
  ///
  /// The following Objectos HTML component:
  ///
  /// {@snippet file = "objectos/html/MarkupJavadoc.java" region = "f1"}
  ///
  /// Generates the following HTML:
  ///
  /// ```html
  /// <ul>
  /// <li>Mon</li>
  /// <li>Wed</li>
  /// <li>Fri</li>
  /// </ul>
  /// ```
  ///
  /// @param <T1> the type of the first argument
  /// @param fragment the fragment to include
  /// @param arg1 the first argument
  /// @return an object representing the encoded fragment
  @Override
  public final <T1> FragmentMarkup f(Fragment1<T1> fragment, T1 arg1){
    return recorder.f(fragment, arg1);
  }

  /// Encodes the specified fragment as part of this document.
  ///
  /// The following Objectos HTML component:
  ///
  /// {@snippet file = "objectos/html/MarkupJavadoc.java" region = "f2"}
  ///
  /// Generates the following HTML:
  ///
  /// ```html
  /// <div><button>OK</button><button>Cancel</button></div>
  /// ```
  ///
  /// @param <T1> the type of the first argument
  /// @param <T2> the type of the second argument
  /// @param fragment the fragment to include
  /// @param arg1 the first argument
  /// @param arg2 the second argument
  /// @return an object representing the encoded fragment
  @Override
  public final <T1, T2> FragmentMarkup f(Fragment2<T1, T2> fragment, T1 arg1, T2 arg2){
    return recorder.f(fragment, arg1, arg2);
  }

  /// Encodes the specified fragment as part of this document.
  ///
  /// The following Objectos HTML component:
  ///
  /// {@snippet file = "objectos/html/MarkupJavadoc.java" region = "f3"}
  ///
  /// Generates the following HTML:
  ///
  /// ```html
  /// <div>
  /// <p>City<span>Tokyo</span></p>
  /// <p>Country<span>Japan</span></p>
  /// </div>
  /// ```
  ///
  /// @param <T1> the type of the first argument
  /// @param <T2> the type of the second argument
  /// @param <T3> the type of the third argument
  /// @param fragment the fragment to include
  /// @param arg1 the first argument
  /// @param arg2 the second argument
  /// @param arg3 the third argument
  /// @return an object representing the encoded fragment
  @Override
  public final <T1, T2, T3> FragmentMarkup f(Fragment3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3){
    return recorder.f(fragment, arg1, arg2, arg3);
  }

  /// Encodes the specified fragment as part of this document.
  ///
  /// @param <T1> the type of the first argument
  /// @param <T2> the type of the second argument
  /// @param <T3> the type of the third argument
  /// @param <T4> the type of the fourth argument
  /// @param fragment the fragment to include
  /// @param arg1 the first argument
  /// @param arg2 the second argument
  /// @param arg3 the third argument
  /// @param arg4 the fourth argument
  /// @return an object representing the encoded fragment
  @Override
  public final <T1, T2, T3, T4> FragmentMarkup f(Fragment4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4){
    return recorder.f(fragment, arg1, arg2, arg3, arg4);
  }

  /// The no-op markup.
  /// @return the no-op markup.
  @Override
  public final NoOpMarkup noop(){
    return recorder.noop();
  }

  // @formatter:on

  //
  // TESTABLE
  //

  /// Formats the specified value as a testable table cell with the specified
  /// fixed width.
  ///
  /// @param value the cell value
  /// @param width the fixed width of the cell
  ///
  /// @return always the cell value
  @Override
  public final String testableCell(String value, int width) {
    return recorder.testableCell(value, width);
  }

  /// Formats the specified name and value as a testable field.
  ///
  /// @param name the field name
  /// @param value the field value
  ///
  /// @return always the field value
  @Override
  public final String testableField(String name, String value) {
    return recorder.testableField(name, value);
  }

  /// Formats the specified name as a testable field name.
  ///
  /// @param name the field name
  ///
  /// @return the specified field name
  @Override
  public final String testableFieldName(String name) {
    return recorder.testableFieldName(name);
  }

  /// Formats the specified value as a testable field value.
  ///
  /// @param value the field value
  ///
  /// @return the specified field value
  @Override
  public final String testableFieldValue(String value) {
    return recorder.testableFieldValue(value);
  }

  /// Formats the specified value as a testable heading level 1.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  @Override
  public final String testableH1(String value) {
    return recorder.testableH1(value);
  }

  /// Formats the specified value as a testable heading level 2.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  @Override
  public final String testableH2(String value) {
    return recorder.testableH2(value);
  }

  /// Formats the specified value as a testable heading level 3.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  @Override
  public final String testableH3(String value) {
    return recorder.testableH3(value);
  }

  /// Formats the specified value as a testable heading level 4.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  @Override
  public final String testableH4(String value) {
    return recorder.testableH4(value);
  }

  /// Formats the specified value as a testable heading level 5.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  @Override
  public final String testableH5(String value) {
    return recorder.testableH5(value);
  }

  /// Formats the specified value as a testable heading level 6.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  @Override
  public final String testableH6(String value) {
    return recorder.testableH6(value);
  }

  /// Formats a line separator at the testable output exclusively.
  ///
  /// @return a no-op instruction
  @Override
  public final NoOpMarkup testableNewLine() {
    return recorder.testableNewLine();
  }

  //
  // ELEMENTS
  //

  /// Encodes the `a` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup a(RegularElementValue... values) {
    return recorder.regularElement(ElementName.A, values);
  }

  /// Encodes the `a` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup a(String text) {
    return recorder.textElement(ElementName.A, text);
  }

  /// Encodes the `abbr` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup abbr(RegularElementValue... values) {
    return recorder.regularElement(ElementName.ABBR, values);
  }

  /// Encodes the `abbr` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup abbr(String text) {
    return recorder.textElement(ElementName.ABBR, text);
  }

  /// Encodes the `article` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup article(RegularElementValue... values) {
    return recorder.regularElement(ElementName.ARTICLE, values);
  }

  /// Encodes the `article` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup article(String text) {
    return recorder.textElement(ElementName.ARTICLE, text);
  }

  /// Encodes the `aside` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup aside(RegularElementValue... values) {
    return recorder.regularElement(ElementName.ASIDE, values);
  }

  /// Encodes the `aside` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup aside(String text) {
    return recorder.textElement(ElementName.ASIDE, text);
  }

  /// Encodes the `b` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup b(RegularElementValue... values) {
    return recorder.regularElement(ElementName.B, values);
  }

  /// Encodes the `b` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup b(String text) {
    return recorder.textElement(ElementName.B, text);
  }

  /// Encodes the `blockquote` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup blockquote(RegularElementValue... values) {
    return recorder.regularElement(ElementName.BLOCKQUOTE, values);
  }

  /// Encodes the `blockquote` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup blockquote(String text) {
    return recorder.textElement(ElementName.BLOCKQUOTE, text);
  }

  /// Encodes the `body` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup body(RegularElementValue... values) {
    return recorder.regularElement(ElementName.BODY, values);
  }

  /// Encodes the `body` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup body(String text) {
    return recorder.textElement(ElementName.BODY, text);
  }

  /// Encodes the `br` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup br(VoidElementValue... values) {
    return recorder.voidElement(ElementName.BR, values);
  }

  /// Encodes the `button` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup button(RegularElementValue... values) {
    return recorder.regularElement(ElementName.BUTTON, values);
  }

  /// Encodes the `button` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup button(String text) {
    return recorder.textElement(ElementName.BUTTON, text);
  }

  /// Encodes the `clipPath` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup clipPath(RegularElementValue... values) {
    return recorder.regularElement(ElementName.CLIPPATH, values);
  }

  /// Encodes the `code` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup code(RegularElementValue... values) {
    return recorder.regularElement(ElementName.CODE, values);
  }

  /// Encodes the `code` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup code(String text) {
    return recorder.textElement(ElementName.CODE, text);
  }

  /// Encodes the `dd` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup dd(RegularElementValue... values) {
    return recorder.regularElement(ElementName.DD, values);
  }

  /// Encodes the `dd` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup dd(String text) {
    return recorder.textElement(ElementName.DD, text);
  }

  /// Encodes the `defs` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup defs(RegularElementValue... values) {
    return recorder.regularElement(ElementName.DEFS, values);
  }

  /// Encodes the `defs` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup defs(String text) {
    return recorder.textElement(ElementName.DEFS, text);
  }

  /// Encodes the `details` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup details(RegularElementValue... values) {
    return recorder.regularElement(ElementName.DETAILS, values);
  }

  /// Encodes the `details` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup details(String text) {
    return recorder.textElement(ElementName.DETAILS, text);
  }

  /// Encodes the `dialog` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup dialog(RegularElementValue... values) {
    return recorder.regularElement(ElementName.DIALOG, values);
  }

  /// Encodes the `dialog` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup dialog(String text) {
    return recorder.textElement(ElementName.DIALOG, text);
  }

  /// Encodes the `div` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup div(RegularElementValue... values) {
    return recorder.regularElement(ElementName.DIV, values);
  }

  /// Encodes the `div` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup div(String text) {
    return recorder.textElement(ElementName.DIV, text);
  }

  /// Encodes the `dl` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup dl(RegularElementValue... values) {
    return recorder.regularElement(ElementName.DL, values);
  }

  /// Encodes the `dl` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup dl(String text) {
    return recorder.textElement(ElementName.DL, text);
  }

  /// Encodes the `dt` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup dt(RegularElementValue... values) {
    return recorder.regularElement(ElementName.DT, values);
  }

  /// Encodes the `dt` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup dt(String text) {
    return recorder.textElement(ElementName.DT, text);
  }

  /// Encodes the `em` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup em(RegularElementValue... values) {
    return recorder.regularElement(ElementName.EM, values);
  }

  /// Encodes the `em` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup em(String text) {
    return recorder.textElement(ElementName.EM, text);
  }

  /// Encodes the `fieldset` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup fieldset(RegularElementValue... values) {
    return recorder.regularElement(ElementName.FIELDSET, values);
  }

  /// Encodes the `fieldset` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup fieldset(String text) {
    return recorder.textElement(ElementName.FIELDSET, text);
  }

  /// Encodes the `figure` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup figure(RegularElementValue... values) {
    return recorder.regularElement(ElementName.FIGURE, values);
  }

  /// Encodes the `figure` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup figure(String text) {
    return recorder.textElement(ElementName.FIGURE, text);
  }

  /// Encodes the `footer` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup footer(RegularElementValue... values) {
    return recorder.regularElement(ElementName.FOOTER, values);
  }

  /// Encodes the `footer` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup footer(String text) {
    return recorder.textElement(ElementName.FOOTER, text);
  }

  /// Encodes the `form` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup form(RegularElementValue... values) {
    return recorder.regularElement(ElementName.FORM, values);
  }

  /// Encodes the `g` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup g(RegularElementValue... values) {
    return recorder.regularElement(ElementName.G, values);
  }

  /// Encodes the `g` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup g(String text) {
    return recorder.textElement(ElementName.G, text);
  }

  /// Encodes the `h1` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h1(RegularElementValue... values) {
    return recorder.regularElement(ElementName.H1, values);
  }

  /// Encodes the `h1` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h1(String text) {
    return recorder.textElement(ElementName.H1, text);
  }

  /// Encodes the `h2` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h2(RegularElementValue... values) {
    return recorder.regularElement(ElementName.H2, values);
  }

  /// Encodes the `h2` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h2(String text) {
    return recorder.textElement(ElementName.H2, text);
  }

  /// Encodes the `h3` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h3(RegularElementValue... values) {
    return recorder.regularElement(ElementName.H3, values);
  }

  /// Encodes the `h3` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h3(String text) {
    return recorder.textElement(ElementName.H3, text);
  }

  /// Encodes the `h4` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h4(RegularElementValue... values) {
    return recorder.regularElement(ElementName.H4, values);
  }

  /// Encodes the `h4` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h4(String text) {
    return recorder.textElement(ElementName.H4, text);
  }

  /// Encodes the `h5` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h5(RegularElementValue... values) {
    return recorder.regularElement(ElementName.H5, values);
  }

  /// Encodes the `h5` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h5(String text) {
    return recorder.textElement(ElementName.H5, text);
  }

  /// Encodes the `h6` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h6(RegularElementValue... values) {
    return recorder.regularElement(ElementName.H6, values);
  }

  /// Encodes the `h6` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup h6(String text) {
    return recorder.textElement(ElementName.H6, text);
  }

  /// Encodes the `head` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup head(RegularElementValue... values) {
    return recorder.regularElement(ElementName.HEAD, values);
  }

  /// Encodes the `head` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup head(String text) {
    return recorder.textElement(ElementName.HEAD, text);
  }

  /// Encodes the `header` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup header(RegularElementValue... values) {
    return recorder.regularElement(ElementName.HEADER, values);
  }

  /// Encodes the `header` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup header(String text) {
    return recorder.textElement(ElementName.HEADER, text);
  }

  /// Encodes the `hgroup` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup hgroup(RegularElementValue... values) {
    return recorder.regularElement(ElementName.HGROUP, values);
  }

  /// Encodes the `hgroup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup hgroup(String text) {
    return recorder.textElement(ElementName.HGROUP, text);
  }

  /// Encodes the `hr` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup hr(VoidElementValue... values) {
    return recorder.voidElement(ElementName.HR, values);
  }

  /// Encodes the `html` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup html(RegularElementValue... values) {
    return recorder.regularElement(ElementName.HTML, values);
  }

  /// Encodes the `html` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup html(String text) {
    return recorder.textElement(ElementName.HTML, text);
  }

  /// Encodes the `img` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup img(VoidElementValue... values) {
    return recorder.voidElement(ElementName.IMG, values);
  }

  /// Encodes the `input` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup input(VoidElementValue... values) {
    return recorder.voidElement(ElementName.INPUT, values);
  }

  /// Encodes the `kbd` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup kbd(RegularElementValue... values) {
    return recorder.regularElement(ElementName.KBD, values);
  }

  /// Encodes the `kbd` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup kbd(String text) {
    return recorder.textElement(ElementName.KBD, text);
  }

  /// Encodes the `label` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup label(RegularElementValue... values) {
    return recorder.regularElement(ElementName.LABEL, values);
  }

  /// Encodes the `legend` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup legend(RegularElementValue... values) {
    return recorder.regularElement(ElementName.LEGEND, values);
  }

  /// Encodes the `legend` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup legend(String text) {
    return recorder.textElement(ElementName.LEGEND, text);
  }

  /// Encodes the `li` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup li(RegularElementValue... values) {
    return recorder.regularElement(ElementName.LI, values);
  }

  /// Encodes the `li` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup li(String text) {
    return recorder.textElement(ElementName.LI, text);
  }

  /// Encodes the `link` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup link(VoidElementValue... values) {
    return recorder.voidElement(ElementName.LINK, values);
  }

  /// Encodes the `main` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup main(RegularElementValue... values) {
    return recorder.regularElement(ElementName.MAIN, values);
  }

  /// Encodes the `main` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup main(String text) {
    return recorder.textElement(ElementName.MAIN, text);
  }

  /// Encodes the `menu` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup menu(RegularElementValue... values) {
    return recorder.regularElement(ElementName.MENU, values);
  }

  /// Encodes the `menu` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup menu(String text) {
    return recorder.textElement(ElementName.MENU, text);
  }

  /// Encodes the `meta` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup meta(VoidElementValue... values) {
    return recorder.voidElement(ElementName.META, values);
  }

  /// Encodes the `nav` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup nav(RegularElementValue... values) {
    return recorder.regularElement(ElementName.NAV, values);
  }

  /// Encodes the `nav` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup nav(String text) {
    return recorder.textElement(ElementName.NAV, text);
  }

  /// Encodes the `noscript` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup noscript(RegularElementValue... values) {
    return recorder.regularElement(ElementName.NOSCRIPT, values);
  }

  /// Encodes the `noscript` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup noscript(String text) {
    return recorder.textElement(ElementName.NOSCRIPT, text);
  }

  /// Encodes the `ol` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup ol(RegularElementValue... values) {
    return recorder.regularElement(ElementName.OL, values);
  }

  /// Encodes the `ol` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup ol(String text) {
    return recorder.textElement(ElementName.OL, text);
  }

  /// Encodes the `optgroup` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup optgroup(RegularElementValue... values) {
    return recorder.regularElement(ElementName.OPTGROUP, values);
  }

  /// Encodes the `optgroup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup optgroup(String text) {
    return recorder.textElement(ElementName.OPTGROUP, text);
  }

  /// Encodes the `option` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup option(RegularElementValue... values) {
    return recorder.regularElement(ElementName.OPTION, values);
  }

  /// Encodes the `option` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup option(String text) {
    return recorder.textElement(ElementName.OPTION, text);
  }

  /// Encodes the `p` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup p(RegularElementValue... values) {
    return recorder.regularElement(ElementName.P, values);
  }

  /// Encodes the `p` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup p(String text) {
    return recorder.textElement(ElementName.P, text);
  }

  /// Encodes the `path` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup path(RegularElementValue... values) {
    return recorder.regularElement(ElementName.PATH, values);
  }

  /// Encodes the `path` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup path(String text) {
    return recorder.textElement(ElementName.PATH, text);
  }

  /// Encodes the `pre` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup pre(RegularElementValue... values) {
    return recorder.regularElement(ElementName.PRE, values);
  }

  /// Encodes the `pre` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup pre(String text) {
    return recorder.textElement(ElementName.PRE, text);
  }

  /// Encodes the `progress` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup progress(RegularElementValue... values) {
    return recorder.regularElement(ElementName.PROGRESS, values);
  }

  /// Encodes the `progress` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup progress(String text) {
    return recorder.textElement(ElementName.PROGRESS, text);
  }

  /// Encodes the `samp` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup samp(RegularElementValue... values) {
    return recorder.regularElement(ElementName.SAMP, values);
  }

  /// Encodes the `samp` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup samp(String text) {
    return recorder.textElement(ElementName.SAMP, text);
  }

  /// Encodes the `script` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup script(RegularElementValue... values) {
    return recorder.regularElement(ElementName.SCRIPT, values);
  }

  /// Encodes the `script` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup script(String text) {
    return recorder.textElement(ElementName.SCRIPT, text);
  }

  /// Encodes the `section` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup section(RegularElementValue... values) {
    return recorder.regularElement(ElementName.SECTION, values);
  }

  /// Encodes the `section` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup section(String text) {
    return recorder.textElement(ElementName.SECTION, text);
  }

  /// Encodes the `select` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup select(RegularElementValue... values) {
    return recorder.regularElement(ElementName.SELECT, values);
  }

  /// Encodes the `select` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup select(String text) {
    return recorder.textElement(ElementName.SELECT, text);
  }

  /// Encodes the `small` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup small(RegularElementValue... values) {
    return recorder.regularElement(ElementName.SMALL, values);
  }

  /// Encodes the `small` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup small(String text) {
    return recorder.textElement(ElementName.SMALL, text);
  }

  /// Encodes the `span` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup span(RegularElementValue... values) {
    return recorder.regularElement(ElementName.SPAN, values);
  }

  /// Encodes the `span` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup span(String text) {
    return recorder.textElement(ElementName.SPAN, text);
  }

  /// Encodes the `strong` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup strong(RegularElementValue... values) {
    return recorder.regularElement(ElementName.STRONG, values);
  }

  /// Encodes the `strong` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup strong(String text) {
    return recorder.textElement(ElementName.STRONG, text);
  }

  /// Encodes the `style` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup style(RegularElementValue... values) {
    return recorder.regularElement(ElementName.STYLE, values);
  }

  /// Encodes the `sub` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup sub(RegularElementValue... values) {
    return recorder.regularElement(ElementName.SUB, values);
  }

  /// Encodes the `sub` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup sub(String text) {
    return recorder.textElement(ElementName.SUB, text);
  }

  /// Encodes the `summary` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup summary(RegularElementValue... values) {
    return recorder.regularElement(ElementName.SUMMARY, values);
  }

  /// Encodes the `summary` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup summary(String text) {
    return recorder.textElement(ElementName.SUMMARY, text);
  }

  /// Encodes the `sup` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup sup(RegularElementValue... values) {
    return recorder.regularElement(ElementName.SUP, values);
  }

  /// Encodes the `sup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup sup(String text) {
    return recorder.textElement(ElementName.SUP, text);
  }

  /// Encodes the `svg` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup svg(RegularElementValue... values) {
    return recorder.regularElement(ElementName.SVG, values);
  }

  /// Encodes the `svg` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup svg(String text) {
    return recorder.textElement(ElementName.SVG, text);
  }

  /// Encodes the `table` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup table(RegularElementValue... values) {
    return recorder.regularElement(ElementName.TABLE, values);
  }

  /// Encodes the `table` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup table(String text) {
    return recorder.textElement(ElementName.TABLE, text);
  }

  /// Encodes the `tbody` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup tbody(RegularElementValue... values) {
    return recorder.regularElement(ElementName.TBODY, values);
  }

  /// Encodes the `tbody` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup tbody(String text) {
    return recorder.textElement(ElementName.TBODY, text);
  }

  /// Encodes the `td` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup td(RegularElementValue... values) {
    return recorder.regularElement(ElementName.TD, values);
  }

  /// Encodes the `td` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup td(String text) {
    return recorder.textElement(ElementName.TD, text);
  }

  /// Encodes the `template` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup template(RegularElementValue... values) {
    return recorder.regularElement(ElementName.TEMPLATE, values);
  }

  /// Encodes the `template` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup template(String text) {
    return recorder.textElement(ElementName.TEMPLATE, text);
  }

  /// Encodes the `textarea` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup textarea(RegularElementValue... values) {
    return recorder.regularElement(ElementName.TEXTAREA, values);
  }

  /// Encodes the `textarea` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup textarea(String text) {
    return recorder.textElement(ElementName.TEXTAREA, text);
  }

  /// Encodes the `th` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup th(RegularElementValue... values) {
    return recorder.regularElement(ElementName.TH, values);
  }

  /// Encodes the `th` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup th(String text) {
    return recorder.textElement(ElementName.TH, text);
  }

  /// Encodes the `thead` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup thead(RegularElementValue... values) {
    return recorder.regularElement(ElementName.THEAD, values);
  }

  /// Encodes the `thead` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup thead(String text) {
    return recorder.textElement(ElementName.THEAD, text);
  }

  /// Encodes the `title` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup title(RegularElementValue... values) {
    return recorder.regularElement(ElementName.TITLE, values);
  }

  /// Encodes the `tr` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup tr(RegularElementValue... values) {
    return recorder.regularElement(ElementName.TR, values);
  }

  /// Encodes the `tr` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup tr(String text) {
    return recorder.textElement(ElementName.TR, text);
  }

  /// Encodes the `ul` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup ul(RegularElementValue... values) {
    return recorder.regularElement(ElementName.UL, values);
  }

  /// Encodes the `ul` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  @Override
  public final ElementMarkup ul(String text) {
    return recorder.textElement(ElementName.UL, text);
  }

  //
  // ATTRIBUTES
  //

  /// Encodes the `accesskey` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup accesskey(String value) {
    return recorder.attribute(AttributeName.ACCESSKEY, value);
  }

  /// Encodes the `action` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup action(String value) {
    return recorder.attribute(AttributeName.ACTION, value);
  }

  /// Encodes the `align` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup align(String value) {
    return recorder.attribute(AttributeName.ALIGN, value);
  }

  /// Encodes the `alignment-baseline` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup alignmentBaseline(String value) {
    return recorder.attribute(AttributeName.ALIGNMENT_BASELINE, value);
  }

  /// Encodes the `alt` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup alt(String value) {
    return recorder.attribute(AttributeName.ALT, value);
  }

  /// Encodes the `aria-current` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaCurrent(String value) {
    return recorder.attribute(AttributeName.ARIA_CURRENT, value);
  }

  /// Encodes the `aria-disabled` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaDisabled(String value) {
    return recorder.attribute(AttributeName.ARIA_DISABLED, value);
  }

  /// Encodes the `aria-hidden` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaHidden(String value) {
    return recorder.attribute(AttributeName.ARIA_HIDDEN, value);
  }

  /// Encodes the `aria-invalid` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaInvalid(String value) {
    return recorder.attribute(AttributeName.ARIA_INVALID, value);
  }

  /// Encodes the `aria-label` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaLabel(String value) {
    return recorder.attribute(AttributeName.ARIA_LABEL, value);
  }

  /// Encodes the `aria-labelledby` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaLabelledBy(String value) {
    return recorder.attribute(AttributeName.ARIA_LABELLED_BY, value);
  }

  /// Encodes the `aria-modal` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaModal(String value) {
    return recorder.attribute(AttributeName.ARIA_MODAL, value);
  }

  /// Encodes the `aria-placeholder` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaPlaceholder(String value) {
    return recorder.attribute(AttributeName.ARIA_PLACEHOLDER, value);
  }

  /// Encodes the `aria-readonly` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaReadonly(String value) {
    return recorder.attribute(AttributeName.ARIA_READONLY, value);
  }

  /// Encodes the `aria-required` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaRequired(String value) {
    return recorder.attribute(AttributeName.ARIA_REQUIRED, value);
  }

  /// Encodes the `aria-selected` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup ariaSelected(String value) {
    return recorder.attribute(AttributeName.ARIA_SELECTED, value);
  }

  /// Encodes the `as` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup as(String value) {
    return recorder.attribute(AttributeName.AS, value);
  }

  /// Encodes the `autocomplete` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup autocomplete(String value) {
    return recorder.attribute(AttributeName.AUTOCOMPLETE, value);
  }

  /// Encodes the `baseline-shift` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup baselineShift(String value) {
    return recorder.attribute(AttributeName.BASELINE_SHIFT, value);
  }

  /// Encodes the `border` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup border(String value) {
    return recorder.attribute(AttributeName.BORDER, value);
  }

  /// Encodes the `cellpadding` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup cellpadding(String value) {
    return recorder.attribute(AttributeName.CELLPADDING, value);
  }

  /// Encodes the `cellspacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup cellspacing(String value) {
    return recorder.attribute(AttributeName.CELLSPACING, value);
  }

  /// Encodes the `charset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup charset(String value) {
    return recorder.attribute(AttributeName.CHARSET, value);
  }

  /// Encodes the `cite` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup cite(String value) {
    return recorder.attribute(AttributeName.CITE, value);
  }

  /// Encodes the `class` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup className(String value) {
    return recorder.attribute(AttributeName.CLASS, value);
  }

  /// Encodes the `clip-rule` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup clipRule(String value) {
    return recorder.attribute(AttributeName.CLIP_RULE, value);
  }

  /// Encodes the `closedby` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup closedby(String value) {
    return recorder.attribute(AttributeName.CLOSEDBY, value);
  }

  /// Encodes the `color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup color(String value) {
    return recorder.attribute(AttributeName.COLOR, value);
  }

  /// Encodes the `color-interpolation` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup colorInterpolation(String value) {
    return recorder.attribute(AttributeName.COLOR_INTERPOLATION, value);
  }

  /// Encodes the `color-interpolation-filters` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup colorInterpolationFilters(String value) {
    return recorder.attribute(AttributeName.COLOR_INTERPOLATION_FILTERS, value);
  }

  /// Encodes the `cols` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup cols(String value) {
    return recorder.attribute(AttributeName.COLS, value);
  }

  /// Encodes the `content` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup content(String value) {
    return recorder.attribute(AttributeName.CONTENT, value);
  }

  /// Encodes the `contenteditable` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup contenteditable(String value) {
    return recorder.attribute(AttributeName.CONTENTEDITABLE, value);
  }

  /// Encodes the `crossorigin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup crossorigin(String value) {
    return recorder.attribute(AttributeName.CROSSORIGIN, value);
  }

  /// Encodes the `cursor` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup cursor(String value) {
    return recorder.attribute(AttributeName.CURSOR, value);
  }

  /// Encodes the `d` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup d(String value) {
    return recorder.attribute(AttributeName.D, value);
  }

  /// Encodes the `dir` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup dir(String value) {
    return recorder.attribute(AttributeName.DIR, value);
  }

  /// Encodes the `direction` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup direction(String value) {
    return recorder.attribute(AttributeName.DIRECTION, value);
  }

  /// Encodes the `dirname` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup dirname(String value) {
    return recorder.attribute(AttributeName.DIRNAME, value);
  }

  /// Encodes the `display` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup display(String value) {
    return recorder.attribute(AttributeName.DISPLAY, value);
  }

  /// Encodes the `dominant-baseline` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup dominantBaseline(String value) {
    return recorder.attribute(AttributeName.DOMINANT_BASELINE, value);
  }

  /// Encodes the `download` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup download(String value) {
    return recorder.attribute(AttributeName.DOWNLOAD, value);
  }

  /// Encodes the `draggable` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup draggable(String value) {
    return recorder.attribute(AttributeName.DRAGGABLE, value);
  }

  /// Encodes the `enctype` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup enctype(String value) {
    return recorder.attribute(AttributeName.ENCTYPE, value);
  }

  /// Encodes the `fill` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup fill(String value) {
    return recorder.attribute(AttributeName.FILL, value);
  }

  /// Encodes the `fill-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup fillOpacity(String value) {
    return recorder.attribute(AttributeName.FILL_OPACITY, value);
  }

  /// Encodes the `fill-rule` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup fillRule(String value) {
    return recorder.attribute(AttributeName.FILL_RULE, value);
  }

  /// Encodes the `filter` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup filter(String value) {
    return recorder.attribute(AttributeName.FILTER, value);
  }

  /// Encodes the `flood-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup floodColor(String value) {
    return recorder.attribute(AttributeName.FLOOD_COLOR, value);
  }

  /// Encodes the `flood-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup floodOpacity(String value) {
    return recorder.attribute(AttributeName.FLOOD_OPACITY, value);
  }

  /// Encodes the `for` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup forId(String value) {
    return recorder.attribute(AttributeName.FOR, value);
  }

  /// Encodes the `glyph-orientation-horizontal` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup glyphOrientationHorizontal(String value) {
    return recorder.attribute(AttributeName.GLYPH_ORIENTATION_HORIZONTAL, value);
  }

  /// Encodes the `glyph-orientation-vertical` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup glyphOrientationVertical(String value) {
    return recorder.attribute(AttributeName.GLYPH_ORIENTATION_VERTICAL, value);
  }

  /// Encodes the `height` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup height(String value) {
    return recorder.attribute(AttributeName.HEIGHT, value);
  }

  /// Encodes the `href` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup href(String value) {
    return recorder.attribute(AttributeName.HREF, value);
  }

  /// Encodes the `http-equiv` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup httpEquiv(String value) {
    return recorder.attribute(AttributeName.HTTP_EQUIV, value);
  }

  /// Encodes the `id` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup id(String value) {
    return recorder.attribute(AttributeName.ID, value);
  }

  /// Encodes the `image-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup imageRendering(String value) {
    return recorder.attribute(AttributeName.IMAGE_RENDERING, value);
  }

  /// Encodes the `integrity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup integrity(String value) {
    return recorder.attribute(AttributeName.INTEGRITY, value);
  }

  /// Encodes the `lang` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup lang(String value) {
    return recorder.attribute(AttributeName.LANG, value);
  }

  /// Encodes the `letter-spacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup letterSpacing(String value) {
    return recorder.attribute(AttributeName.LETTER_SPACING, value);
  }

  /// Encodes the `lighting-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup lightingColor(String value) {
    return recorder.attribute(AttributeName.LIGHTING_COLOR, value);
  }

  /// Encodes the `marker-end` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup markerEnd(String value) {
    return recorder.attribute(AttributeName.MARKER_END, value);
  }

  /// Encodes the `marker-mid` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup markerMid(String value) {
    return recorder.attribute(AttributeName.MARKER_MID, value);
  }

  /// Encodes the `marker-start` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup markerStart(String value) {
    return recorder.attribute(AttributeName.MARKER_START, value);
  }

  /// Encodes the `mask` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup mask(String value) {
    return recorder.attribute(AttributeName.MASK, value);
  }

  /// Encodes the `mask-type` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup maskType(String value) {
    return recorder.attribute(AttributeName.MASK_TYPE, value);
  }

  /// Encodes the `maxlength` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup maxlength(String value) {
    return recorder.attribute(AttributeName.MAXLENGTH, value);
  }

  /// Encodes the `media` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup media(String value) {
    return recorder.attribute(AttributeName.MEDIA, value);
  }

  /// Encodes the `method` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup method(String value) {
    return recorder.attribute(AttributeName.METHOD, value);
  }

  /// Encodes the `minlength` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup minlength(String value) {
    return recorder.attribute(AttributeName.MINLENGTH, value);
  }

  /// Encodes the `name` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup name(String value) {
    return recorder.attribute(AttributeName.NAME, value);
  }

  /// Encodes the `onclick` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup onclick(String value) {
    return recorder.attribute(AttributeName.ONCLICK, value);
  }

  /// Encodes the `onclick` attribute with the specified action.
  ///
  /// @param action the action to be executed
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup onclick(JsAction action) {
    return recorder.attribute(AttributeName.ONCLICK, action);
  }

  /// Encodes the `onload` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup onload(String value) {
    return recorder.attribute(AttributeName.ONLOAD, value);
  }

  /// Encodes the `onload` attribute with the specified action.
  ///
  /// @param action the action to be executed
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup onload(JsAction action) {
    return recorder.attribute(AttributeName.ONLOAD, action);
  }

  /// Encodes the `onpopstate` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup onpopstate(String value) {
    return recorder.attribute(AttributeName.ONPOPSTATE, value);
  }

  /// Encodes the `onpopstate` attribute with the specified action.
  ///
  /// @param action the action to be executed
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup onpopstate(JsAction action) {
    return recorder.attribute(AttributeName.ONPOPSTATE, action);
  }

  /// Encodes the `onsubmit` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup onsubmit(String value) {
    return recorder.attribute(AttributeName.ONSUBMIT, value);
  }

  /// Encodes the `onsubmit` attribute with the specified action.
  ///
  /// @param action the action to be executed
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup onsubmit(JsAction action) {
    return recorder.attribute(AttributeName.ONSUBMIT, action);
  }

  /// Encodes the `opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup opacity(String value) {
    return recorder.attribute(AttributeName.OPACITY, value);
  }

  /// Encodes the `overflow` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup overflow(String value) {
    return recorder.attribute(AttributeName.OVERFLOW, value);
  }

  /// Encodes the `paint-order` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup paintOrder(String value) {
    return recorder.attribute(AttributeName.PAINT_ORDER, value);
  }

  /// Encodes the `placeholder` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup placeholder(String value) {
    return recorder.attribute(AttributeName.PLACEHOLDER, value);
  }

  /// Encodes the `pointer-events` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup pointerEvents(String value) {
    return recorder.attribute(AttributeName.POINTER_EVENTS, value);
  }

  /// Encodes the `property` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup property(String value) {
    return recorder.attribute(AttributeName.PROPERTY, value);
  }

  /// Encodes the `referrerpolicy` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup referrerpolicy(String value) {
    return recorder.attribute(AttributeName.REFERRERPOLICY, value);
  }

  /// Encodes the `rel` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup rel(String value) {
    return recorder.attribute(AttributeName.REL, value);
  }

  /// Encodes the `rev` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup rev(String value) {
    return recorder.attribute(AttributeName.REV, value);
  }

  /// Encodes the `role` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup role(String value) {
    return recorder.attribute(AttributeName.ROLE, value);
  }

  /// Encodes the `rows` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup rows(String value) {
    return recorder.attribute(AttributeName.ROWS, value);
  }

  /// Encodes the `shape-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup shapeRendering(String value) {
    return recorder.attribute(AttributeName.SHAPE_RENDERING, value);
  }

  /// Encodes the `size` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup size(String value) {
    return recorder.attribute(AttributeName.SIZE, value);
  }

  /// Encodes the `sizes` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup sizes(String value) {
    return recorder.attribute(AttributeName.SIZES, value);
  }

  /// Encodes the `spellcheck` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup spellcheck(String value) {
    return recorder.attribute(AttributeName.SPELLCHECK, value);
  }

  /// Encodes the `src` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup src(String value) {
    return recorder.attribute(AttributeName.SRC, value);
  }

  /// Encodes the `srcset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup srcset(String value) {
    return recorder.attribute(AttributeName.SRCSET, value);
  }

  /// Encodes the `start` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup start(String value) {
    return recorder.attribute(AttributeName.START, value);
  }

  /// Encodes the `stop-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup stopColor(String value) {
    return recorder.attribute(AttributeName.STOP_COLOR, value);
  }

  /// Encodes the `stop-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup stopOpacity(String value) {
    return recorder.attribute(AttributeName.STOP_OPACITY, value);
  }

  /// Encodes the `stroke` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup stroke(String value) {
    return recorder.attribute(AttributeName.STROKE, value);
  }

  /// Encodes the `stroke-dasharray` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup strokeDasharray(String value) {
    return recorder.attribute(AttributeName.STROKE_DASHARRAY, value);
  }

  /// Encodes the `stroke-dashoffset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup strokeDashoffset(String value) {
    return recorder.attribute(AttributeName.STROKE_DASHOFFSET, value);
  }

  /// Encodes the `stroke-linecap` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup strokeLinecap(String value) {
    return recorder.attribute(AttributeName.STROKE_LINECAP, value);
  }

  /// Encodes the `stroke-linejoin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup strokeLinejoin(String value) {
    return recorder.attribute(AttributeName.STROKE_LINEJOIN, value);
  }

  /// Encodes the `stroke-miterlimit` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup strokeMiterlimit(String value) {
    return recorder.attribute(AttributeName.STROKE_MITERLIMIT, value);
  }

  /// Encodes the `stroke-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup strokeOpacity(String value) {
    return recorder.attribute(AttributeName.STROKE_OPACITY, value);
  }

  /// Encodes the `stroke-width` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup strokeWidth(String value) {
    return recorder.attribute(AttributeName.STROKE_WIDTH, value);
  }

  /// Encodes the `tabindex` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup tabindex(String value) {
    return recorder.attribute(AttributeName.TABINDEX, value);
  }

  /// Encodes the `target` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup target(String value) {
    return recorder.attribute(AttributeName.TARGET, value);
  }

  /// Encodes the `text-anchor` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup textAnchor(String value) {
    return recorder.attribute(AttributeName.TEXT_ANCHOR, value);
  }

  /// Encodes the `text-decoration` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup textDecoration(String value) {
    return recorder.attribute(AttributeName.TEXT_DECORATION, value);
  }

  /// Encodes the `text-overflow` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup textOverflow(String value) {
    return recorder.attribute(AttributeName.TEXT_OVERFLOW, value);
  }

  /// Encodes the `text-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup textRendering(String value) {
    return recorder.attribute(AttributeName.TEXT_RENDERING, value);
  }

  /// Encodes the `transform` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup transform(String value) {
    return recorder.attribute(AttributeName.TRANSFORM, value);
  }

  /// Encodes the `transform-origin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup transformOrigin(String value) {
    return recorder.attribute(AttributeName.TRANSFORM_ORIGIN, value);
  }

  /// Encodes the `translate` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup translate(String value) {
    return recorder.attribute(AttributeName.TRANSLATE, value);
  }

  /// Encodes the `type` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup type(String value) {
    return recorder.attribute(AttributeName.TYPE, value);
  }

  /// Encodes the `unicode-bidi` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup unicodeBidi(String value) {
    return recorder.attribute(AttributeName.UNICODE_BIDI, value);
  }

  /// Encodes the `value` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup value(String value) {
    return recorder.attribute(AttributeName.VALUE, value);
  }

  /// Encodes the `vector-effect` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup vectorEffect(String value) {
    return recorder.attribute(AttributeName.VECTOR_EFFECT, value);
  }

  /// Encodes the `viewBox` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup viewBox(String value) {
    return recorder.attribute(AttributeName.VIEWBOX, value);
  }

  /// Encodes the `visibility` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup visibility(String value) {
    return recorder.attribute(AttributeName.VISIBILITY, value);
  }

  /// Encodes the `white-space` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup whiteSpace(String value) {
    return recorder.attribute(AttributeName.WHITE_SPACE, value);
  }

  /// Encodes the `width` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup width(String value) {
    return recorder.attribute(AttributeName.WIDTH, value);
  }

  /// Encodes the `word-spacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup wordSpacing(String value) {
    return recorder.attribute(AttributeName.WORD_SPACING, value);
  }

  /// Encodes the `wrap` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup wrap(String value) {
    return recorder.attribute(AttributeName.WRAP, value);
  }

  /// Encodes the `writing-mode` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup writingMode(String value) {
    return recorder.attribute(AttributeName.WRITING_MODE, value);
  }

  /// Encodes the `xmlns` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  @Override
  public final AttributeMarkup xmlns(String value) {
    return recorder.attribute(AttributeName.XMLNS, value);
  }

  //
  // AMBIGUOUS
  //

  /// Encodes the `clip-path` attribute or the `clipPath` element with the
  /// specified text.
  ///
  /// @param value the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  @Override
  public final AmbiguousMarkup clipPath(String value) {
    return recorder.ambiguous(AmbiguousName.CLIPPATH, value);
  }

  /// Encodes the `form` attribute or the `form` element with the specified
  /// text.
  ///
  /// @param value the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  @Override
  public final AmbiguousMarkup form(String value) {
    return recorder.ambiguous(AmbiguousName.FORM, value);
  }

  /// Encodes the `label` attribute or the `label` element with the specified
  /// text.
  ///
  /// @param value the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  @Override
  public final AmbiguousMarkup label(String value) {
    return recorder.ambiguous(AmbiguousName.LABEL, value);
  }

  /// Encodes the `style` attribute or the `style` element with the specified
  /// text.
  ///
  /// @param value the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  @Override
  public final AmbiguousMarkup style(String value) {
    return recorder.ambiguous(AmbiguousName.STYLE, value);
  }

  /// Encodes the `title` attribute or the `title` element with the specified
  /// text.
  ///
  /// @param value the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  @Override
  public final AmbiguousMarkup title(String value) {
    return recorder.ambiguous(AmbiguousName.TITLE, value);
  }

  //
  // TEXT
  //

  /// Encodes the non-breaking space `&nbsp;` HTML character entity.
  ///
  /// @return an object representing the encoded character entity.
  @Override
  public final RawMarkup nbsp() {
    return recorder.raw("&nbsp;");
  }

  /// Encodes the specified value as raw HTML.
  ///
  /// @param value the raw HTML value
  ///
  /// @return an object representing the encoded raw HTML
  @Override
  public final RawMarkup raw(String value) {
    return recorder.raw(value);
  }

  /// Encodes a text node with the specified value.
  ///
  /// @param value the text value
  ///
  /// @return an object representing the encoded text node
  @Override
  public final TextMarkup text(String value) {
    return recorder.text(value);
  }

  // END generated code

}