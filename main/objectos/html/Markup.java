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
package objectos.html;

import java.util.Collection;
import objectos.html.markup.AmbiguousMarkup;
import objectos.html.markup.AttributeMarkup;
import objectos.html.markup.ElementMarkup;
import objectos.html.markup.FlattenInstruction;
import objectos.html.markup.Fragment0;
import objectos.html.markup.Fragment1;
import objectos.html.markup.Fragment2;
import objectos.html.markup.Fragment3;
import objectos.html.markup.Fragment4;
import objectos.html.markup.RegularElementValue;
import objectos.html.markup.ElementValue;
import objectos.html.markup.FragmentInstruction;
import objectos.html.markup.Instruction;
import objectos.html.markup.NoOpMarkup;
import objectos.html.markup.RawMarkup;
import objectos.html.markup.TextMarkup;
import objectos.html.markup.VoidElementValue;
import objectos.script.JsAction;
import objectox.html.MarkupPojo;

/// Declares the structure of an HTML document using pure Java.
public sealed interface Markup permits MarkupPojo {

  /// Markup implementation for generating HTML.
  non-sealed class OfHtml extends MarkupPojo {

    /// Sole constructor.
    public OfHtml() {}

  }

  /// Returns the HTML declared by this instance as a compiled artifact.
  ///
  /// @return a compiled HTML artifact
  Html compile();

  /// Renders the specified attribute at the root of a document or fragment.
  ///
  /// @param object the attribute
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup attr(Attribute object);

  // START generated code

  /// The `async` boolean attribute.
  BooleanAttribute async = BooleanAttribute.of(AttributeName.ASYNC);

  /// The `autofocus` boolean attribute.
  BooleanAttribute autofocus = BooleanAttribute.of(AttributeName.AUTOFOCUS);

  /// The `checked` boolean attribute.
  BooleanAttribute checked = BooleanAttribute.of(AttributeName.CHECKED);

  /// The `defer` boolean attribute.
  BooleanAttribute defer = BooleanAttribute.of(AttributeName.DEFER);

  /// The `disabled` boolean attribute.
  BooleanAttribute disabled = BooleanAttribute.of(AttributeName.DISABLED);

  /// The `hidden` boolean attribute.
  BooleanAttribute hidden = BooleanAttribute.of(AttributeName.HIDDEN);

  /// The `multiple` boolean attribute.
  BooleanAttribute multiple = BooleanAttribute.of(AttributeName.MULTIPLE);

  /// The `nomodule` boolean attribute.
  BooleanAttribute nomodule = BooleanAttribute.of(AttributeName.NOMODULE);

  /// The `open` boolean attribute.
  BooleanAttribute open = BooleanAttribute.of(AttributeName.OPEN);

  /// The `readonly` boolean attribute.
  BooleanAttribute readonly = BooleanAttribute.of(AttributeName.READONLY);

  /// The `required` boolean attribute.
  BooleanAttribute required = BooleanAttribute.of(AttributeName.REQUIRED);

  /// The `reversed` boolean attribute.
  BooleanAttribute reversed = BooleanAttribute.of(AttributeName.REVERSED);

  /// The `selected` boolean attribute.
  BooleanAttribute selected = BooleanAttribute.of(AttributeName.SELECTED);

  //
  // WAY
  //

  // @formatter:off

  /// Renders the `class` attribute by processing the specified value.
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
  /// @return an instruction representing this attribute.
  Attribute css(String value);

  /// Renders the specified fragment as part of this document.
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
  FragmentInstruction f(Fragment0 fragment);

  /// Renders the specified fragment as part of this document.
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
  /// @return an instruction representing the fragment
  <T1> FragmentInstruction f(Fragment1<T1> fragment, T1 arg1);

  /// Renders the specified fragment as part of this document.
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
  /// @return an instruction representing the fragment
  <T1, T2> FragmentInstruction f(Fragment2<T1, T2> fragment, T1 arg1, T2 arg2);

  /// Renders the specified fragment as part of this document.
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
  /// @return an instruction representing the fragment
  <T1, T2, T3> FragmentInstruction f(Fragment3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3);

  /// Renders the specified fragment as part of this document.
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
  /// @return an instruction representing the fragment
  <T1, T2, T3, T4> FragmentInstruction f(Fragment4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4);

  /// Flattens the specified instructions so that each of the specified
  /// instructions is individually added, in order, to a receiving element.
  /// @param contents the instructions to be flattened
  /// @return an instruction representing this flatten operation
  FlattenInstruction flatten(Instruction... contents);

  /// Flattens the specified instructions so that each of the specified
  /// instructions is individually added, in order, to a receiving element.
  /// @param contents the instructions to be flattened
  /// @return an instruction representing this flatten operation
  FlattenInstruction flatten(Collection<? extends Instruction> contents);

  /// The no-op markup.
  /// @return the no-op markup.
  NoOpMarkup noop();

  // @formatter:on

  //
  // TESTABLE
  //

  /// Formats the specified value as a testable table cell with the specified
  /// fixed width (optional operation).
  ///
  /// @param value the cell value
  /// @param width the fixed width of the cell
  ///
  /// @return always the cell value
  String testableCell(String value, int width);

  /// Formats the specified name and value as a testable field (optional
  /// operation).
  ///
  /// @param name the field name
  /// @param value the field value
  ///
  /// @return always the field value
  String testableField(String name, String value);

  /// Formats the specified name as a testable field name (optional operation).
  ///
  /// @param name the field name
  ///
  /// @return the specified field name
  String testableFieldName(String name);

  /// Formats the specified value as a testable field value (optional
  /// operation).
  ///
  /// @param value the field value
  ///
  /// @return the specified field value
  String testableFieldValue(String value);

  /// Formats the specified value as a testable heading level 1 (optional
  /// operation).
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  String testableH1(String value);

  /// Formats the specified value as a testable heading level 2 (optional
  /// operation).
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  String testableH2(String value);

  /// Formats the specified value as a testable heading level 3 (optional
  /// operation).
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  String testableH3(String value);

  /// Formats the specified value as a testable heading level 4 (optional
  /// operation).
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  String testableH4(String value);

  /// Formats the specified value as a testable heading level 5 (optional
  /// operation).
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  String testableH5(String value);

  /// Formats the specified value as a testable heading level 6 (optional
  /// operation).
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  String testableH6(String value);

  /// Formats a line separator at the testable output exclusively (optional
  /// operation).
  ///
  /// @return a no-op instruction
  NoOpMarkup testableNewLine();

  //
  // ELEMENTS
  //

  /// Encodes an HTML element with the specified name and values.
  ///
  /// @param name the element name
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup elem(ElementName name, ElementValue... values);

  /// Encodes an HTML element with the specified name and text.
  ///
  /// @param name the element name
  /// @param text the text value of this element
  ///
  /// @return an object representing the encoded element
  ElementMarkup elem(ElementName name, String text);

  /// Encodes the `<!DOCTYPE html>` doctype.
  void doctype();

  /// Encodes the `a` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup a(RegularElementValue... values);

  /// Encodes the `a` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup a(String text);

  /// Encodes the `abbr` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup abbr(RegularElementValue... values);

  /// Encodes the `abbr` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup abbr(String text);

  /// Encodes the `article` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup article(RegularElementValue... values);

  /// Encodes the `article` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup article(String text);

  /// Encodes the `aside` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup aside(RegularElementValue... values);

  /// Encodes the `aside` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup aside(String text);

  /// Encodes the `b` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup b(RegularElementValue... values);

  /// Encodes the `b` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup b(String text);

  /// Encodes the `blockquote` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup blockquote(RegularElementValue... values);

  /// Encodes the `blockquote` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup blockquote(String text);

  /// Encodes the `body` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup body(RegularElementValue... values);

  /// Encodes the `body` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup body(String text);

  /// Encodes the `br` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup br(VoidElementValue... values);

  /// Encodes the `button` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup button(RegularElementValue... values);

  /// Encodes the `button` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup button(String text);

  /// Encodes the `clipPath` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup clipPath(RegularElementValue... values);

  /// Encodes the `code` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup code(RegularElementValue... values);

  /// Encodes the `code` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup code(String text);

  /// Encodes the `dd` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup dd(RegularElementValue... values);

  /// Encodes the `dd` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup dd(String text);

  /// Encodes the `defs` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup defs(RegularElementValue... values);

  /// Encodes the `defs` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup defs(String text);

  /// Encodes the `details` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup details(RegularElementValue... values);

  /// Encodes the `details` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup details(String text);

  /// Encodes the `dialog` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup dialog(RegularElementValue... values);

  /// Encodes the `dialog` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup dialog(String text);

  /// Encodes the `div` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup div(RegularElementValue... values);

  /// Encodes the `div` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup div(String text);

  /// Encodes the `dl` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup dl(RegularElementValue... values);

  /// Encodes the `dl` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup dl(String text);

  /// Encodes the `dt` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup dt(RegularElementValue... values);

  /// Encodes the `dt` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup dt(String text);

  /// Encodes the `em` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup em(RegularElementValue... values);

  /// Encodes the `em` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup em(String text);

  /// Encodes the `fieldset` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup fieldset(RegularElementValue... values);

  /// Encodes the `fieldset` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup fieldset(String text);

  /// Encodes the `figure` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup figure(RegularElementValue... values);

  /// Encodes the `figure` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup figure(String text);

  /// Encodes the `footer` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup footer(RegularElementValue... values);

  /// Encodes the `footer` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup footer(String text);

  /// Encodes the `form` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup form(RegularElementValue... values);

  /// Encodes the `g` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup g(RegularElementValue... values);

  /// Encodes the `g` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup g(String text);

  /// Encodes the `h1` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h1(RegularElementValue... values);

  /// Encodes the `h1` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h1(String text);

  /// Encodes the `h2` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h2(RegularElementValue... values);

  /// Encodes the `h2` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h2(String text);

  /// Encodes the `h3` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h3(RegularElementValue... values);

  /// Encodes the `h3` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h3(String text);

  /// Encodes the `h4` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h4(RegularElementValue... values);

  /// Encodes the `h4` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h4(String text);

  /// Encodes the `h5` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h5(RegularElementValue... values);

  /// Encodes the `h5` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h5(String text);

  /// Encodes the `h6` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h6(RegularElementValue... values);

  /// Encodes the `h6` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup h6(String text);

  /// Encodes the `head` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup head(RegularElementValue... values);

  /// Encodes the `head` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup head(String text);

  /// Encodes the `header` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup header(RegularElementValue... values);

  /// Encodes the `header` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup header(String text);

  /// Encodes the `hgroup` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup hgroup(RegularElementValue... values);

  /// Encodes the `hgroup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup hgroup(String text);

  /// Encodes the `hr` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup hr(VoidElementValue... values);

  /// Encodes the `html` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup html(RegularElementValue... values);

  /// Encodes the `html` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup html(String text);

  /// Encodes the `img` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup img(VoidElementValue... values);

  /// Encodes the `input` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup input(VoidElementValue... values);

  /// Encodes the `kbd` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup kbd(RegularElementValue... values);

  /// Encodes the `kbd` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup kbd(String text);

  /// Encodes the `label` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup label(RegularElementValue... values);

  /// Encodes the `legend` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup legend(RegularElementValue... values);

  /// Encodes the `legend` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup legend(String text);

  /// Encodes the `li` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup li(RegularElementValue... values);

  /// Encodes the `li` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup li(String text);

  /// Encodes the `link` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup link(VoidElementValue... values);

  /// Encodes the `main` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup main(RegularElementValue... values);

  /// Encodes the `main` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup main(String text);

  /// Encodes the `menu` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup menu(RegularElementValue... values);

  /// Encodes the `menu` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup menu(String text);

  /// Encodes the `meta` element with the specified content.
  ///
  /// @param values the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup meta(VoidElementValue... values);

  /// Encodes the `nav` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup nav(RegularElementValue... values);

  /// Encodes the `nav` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup nav(String text);

  /// Encodes the `noscript` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup noscript(RegularElementValue... values);

  /// Encodes the `noscript` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup noscript(String text);

  /// Encodes the `ol` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup ol(RegularElementValue... values);

  /// Encodes the `ol` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup ol(String text);

  /// Encodes the `optgroup` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup optgroup(RegularElementValue... values);

  /// Encodes the `optgroup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup optgroup(String text);

  /// Encodes the `option` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup option(RegularElementValue... values);

  /// Encodes the `option` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup option(String text);

  /// Encodes the `p` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup p(RegularElementValue... values);

  /// Encodes the `p` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup p(String text);

  /// Encodes the `path` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup path(RegularElementValue... values);

  /// Encodes the `path` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup path(String text);

  /// Encodes the `pre` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup pre(RegularElementValue... values);

  /// Encodes the `pre` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup pre(String text);

  /// Encodes the `progress` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup progress(RegularElementValue... values);

  /// Encodes the `progress` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup progress(String text);

  /// Encodes the `samp` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup samp(RegularElementValue... values);

  /// Encodes the `samp` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup samp(String text);

  /// Encodes the `script` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup script(RegularElementValue... values);

  /// Encodes the `script` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup script(String text);

  /// Encodes the `section` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup section(RegularElementValue... values);

  /// Encodes the `section` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup section(String text);

  /// Encodes the `select` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup select(RegularElementValue... values);

  /// Encodes the `select` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup select(String text);

  /// Encodes the `small` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup small(RegularElementValue... values);

  /// Encodes the `small` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup small(String text);

  /// Encodes the `span` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup span(RegularElementValue... values);

  /// Encodes the `span` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup span(String text);

  /// Encodes the `strong` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup strong(RegularElementValue... values);

  /// Encodes the `strong` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup strong(String text);

  /// Encodes the `style` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup style(RegularElementValue... values);

  /// Encodes the `sub` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup sub(RegularElementValue... values);

  /// Encodes the `sub` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup sub(String text);

  /// Encodes the `summary` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup summary(RegularElementValue... values);

  /// Encodes the `summary` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup summary(String text);

  /// Encodes the `sup` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup sup(RegularElementValue... values);

  /// Encodes the `sup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup sup(String text);

  /// Encodes the `svg` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup svg(RegularElementValue... values);

  /// Encodes the `svg` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup svg(String text);

  /// Encodes the `table` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup table(RegularElementValue... values);

  /// Encodes the `table` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup table(String text);

  /// Encodes the `tbody` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup tbody(RegularElementValue... values);

  /// Encodes the `tbody` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup tbody(String text);

  /// Encodes the `td` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup td(RegularElementValue... values);

  /// Encodes the `td` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup td(String text);

  /// Encodes the `template` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup template(RegularElementValue... values);

  /// Encodes the `template` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup template(String text);

  /// Encodes the `textarea` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup textarea(RegularElementValue... values);

  /// Encodes the `textarea` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup textarea(String text);

  /// Encodes the `th` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup th(RegularElementValue... values);

  /// Encodes the `th` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup th(String text);

  /// Encodes the `thead` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup thead(RegularElementValue... values);

  /// Encodes the `thead` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup thead(String text);

  /// Encodes the `title` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup title(RegularElementValue... values);

  /// Encodes the `tr` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup tr(RegularElementValue... values);

  /// Encodes the `tr` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup tr(String text);

  /// Encodes the `ul` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup ul(RegularElementValue... values);

  /// Encodes the `ul` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  ElementMarkup ul(String text);

  //
  // ATTRIBUTES
  //

  /// Encodes the `accesskey` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute accesskey(String value);

  /// Encodes the `action` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute action(String value);

  /// Encodes the `align` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute align(String value);

  /// Encodes the `alignment-baseline` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute alignmentBaseline(String value);

  /// Encodes the `alt` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute alt(String value);

  /// Encodes the `aria-current` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaCurrent(String value);

  /// Encodes the `aria-disabled` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaDisabled(String value);

  /// Encodes the `aria-hidden` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaHidden(String value);

  /// Encodes the `aria-invalid` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaInvalid(String value);

  /// Encodes the `aria-label` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaLabel(String value);

  /// Encodes the `aria-labelledby` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaLabelledBy(String value);

  /// Encodes the `aria-modal` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaModal(String value);

  /// Encodes the `aria-placeholder` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaPlaceholder(String value);

  /// Encodes the `aria-readonly` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaReadonly(String value);

  /// Encodes the `aria-required` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaRequired(String value);

  /// Encodes the `aria-selected` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute ariaSelected(String value);

  /// Encodes the `as` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute as(String value);

  /// Encodes the `autocomplete` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute autocomplete(String value);

  /// Encodes the `baseline-shift` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute baselineShift(String value);

  /// Encodes the `border` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute border(String value);

  /// Encodes the `cellpadding` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute cellpadding(String value);

  /// Encodes the `cellspacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute cellspacing(String value);

  /// Encodes the `charset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute charset(String value);

  /// Encodes the `cite` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute cite(String value);

  /// Encodes the `class` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute className(String value);

  /// Encodes the `clip-rule` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute clipRule(String value);

  /// Encodes the `closedby` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute closedby(String value);

  /// Encodes the `color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute color(String value);

  /// Encodes the `color-interpolation` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute colorInterpolation(String value);

  /// Encodes the `color-interpolation-filters` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute colorInterpolationFilters(String value);

  /// Encodes the `cols` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute cols(String value);

  /// Encodes the `content` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute content(String value);

  /// Encodes the `contenteditable` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute contenteditable(String value);

  /// Encodes the `crossorigin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute crossorigin(String value);

  /// Encodes the `cursor` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute cursor(String value);

  /// Encodes the `d` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute d(String value);

  /// Encodes the `dir` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute dir(String value);

  /// Encodes the `direction` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute direction(String value);

  /// Encodes the `dirname` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute dirname(String value);

  /// Encodes the `display` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute display(String value);

  /// Encodes the `dominant-baseline` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute dominantBaseline(String value);

  /// Encodes the `download` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute download(String value);

  /// Encodes the `draggable` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute draggable(String value);

  /// Encodes the `enctype` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute enctype(String value);

  /// Encodes the `fill` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute fill(String value);

  /// Encodes the `fill-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute fillOpacity(String value);

  /// Encodes the `fill-rule` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute fillRule(String value);

  /// Encodes the `filter` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute filter(String value);

  /// Encodes the `flood-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute floodColor(String value);

  /// Encodes the `flood-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute floodOpacity(String value);

  /// Encodes the `for` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute forId(String value);

  /// Encodes the `glyph-orientation-horizontal` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute glyphOrientationHorizontal(String value);

  /// Encodes the `glyph-orientation-vertical` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute glyphOrientationVertical(String value);

  /// Encodes the `height` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute height(String value);

  /// Encodes the `href` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute href(String value);

  /// Encodes the `http-equiv` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute httpEquiv(String value);

  /// Encodes the `id` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute id(String value);

  /// Encodes the `image-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute imageRendering(String value);

  /// Encodes the `integrity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute integrity(String value);

  /// Encodes the `lang` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute lang(String value);

  /// Encodes the `letter-spacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute letterSpacing(String value);

  /// Encodes the `lighting-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute lightingColor(String value);

  /// Encodes the `marker-end` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute markerEnd(String value);

  /// Encodes the `marker-mid` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute markerMid(String value);

  /// Encodes the `marker-start` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute markerStart(String value);

  /// Encodes the `mask` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute mask(String value);

  /// Encodes the `mask-type` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute maskType(String value);

  /// Encodes the `maxlength` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute maxlength(String value);

  /// Encodes the `media` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute media(String value);

  /// Encodes the `method` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute method(String value);

  /// Encodes the `minlength` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute minlength(String value);

  /// Encodes the `name` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute name(String value);

  /// Encodes the `onclick` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute onclick(String value);

  /// Encodes the `onclick` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an object representing the encoded attribute
  Attribute onclick(JsAction value);

  /// Encodes the `onload` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute onload(String value);

  /// Encodes the `onload` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an object representing the encoded attribute
  Attribute onload(JsAction value);

  /// Encodes the `onpopstate` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute onpopstate(String value);

  /// Encodes the `onpopstate` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an object representing the encoded attribute
  Attribute onpopstate(JsAction value);

  /// Encodes the `onsubmit` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute onsubmit(String value);

  /// Encodes the `onsubmit` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an object representing the encoded attribute
  Attribute onsubmit(JsAction value);

  /// Encodes the `opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute opacity(String value);

  /// Encodes the `overflow` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute overflow(String value);

  /// Encodes the `paint-order` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute paintOrder(String value);

  /// Encodes the `placeholder` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute placeholder(String value);

  /// Encodes the `pointer-events` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute pointerEvents(String value);

  /// Encodes the `property` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute property(String value);

  /// Encodes the `referrerpolicy` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute referrerpolicy(String value);

  /// Encodes the `rel` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute rel(String value);

  /// Encodes the `rev` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute rev(String value);

  /// Encodes the `role` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute role(String value);

  /// Encodes the `rows` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute rows(String value);

  /// Encodes the `shape-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute shapeRendering(String value);

  /// Encodes the `size` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute size(String value);

  /// Encodes the `sizes` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute sizes(String value);

  /// Encodes the `spellcheck` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute spellcheck(String value);

  /// Encodes the `src` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute src(String value);

  /// Encodes the `srcset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute srcset(String value);

  /// Encodes the `start` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute start(String value);

  /// Encodes the `stop-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute stopColor(String value);

  /// Encodes the `stop-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute stopOpacity(String value);

  /// Encodes the `stroke` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute stroke(String value);

  /// Encodes the `stroke-dasharray` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute strokeDasharray(String value);

  /// Encodes the `stroke-dashoffset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute strokeDashoffset(String value);

  /// Encodes the `stroke-linecap` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute strokeLinecap(String value);

  /// Encodes the `stroke-linejoin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute strokeLinejoin(String value);

  /// Encodes the `stroke-miterlimit` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute strokeMiterlimit(String value);

  /// Encodes the `stroke-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute strokeOpacity(String value);

  /// Encodes the `stroke-width` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute strokeWidth(String value);

  /// Encodes the `tabindex` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute tabindex(String value);

  /// Encodes the `target` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute target(String value);

  /// Encodes the `text-anchor` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute textAnchor(String value);

  /// Encodes the `text-decoration` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute textDecoration(String value);

  /// Encodes the `text-overflow` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute textOverflow(String value);

  /// Encodes the `text-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute textRendering(String value);

  /// Encodes the `transform` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute transform(String value);

  /// Encodes the `transform-origin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute transformOrigin(String value);

  /// Encodes the `translate` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute translate(String value);

  /// Encodes the `type` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute type(String value);

  /// Encodes the `unicode-bidi` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute unicodeBidi(String value);

  /// Encodes the `value` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute value(String value);

  /// Encodes the `vector-effect` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute vectorEffect(String value);

  /// Encodes the `viewBox` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute viewBox(String value);

  /// Encodes the `visibility` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute visibility(String value);

  /// Encodes the `white-space` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute whiteSpace(String value);

  /// Encodes the `width` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute width(String value);

  /// Encodes the `word-spacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute wordSpacing(String value);

  /// Encodes the `wrap` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute wrap(String value);

  /// Encodes the `writing-mode` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute writingMode(String value);

  /// Encodes the `xmlns` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  Attribute xmlns(String value);

  //
  // AMBIGUOUS
  //

  /// Encodes the `clip-path` attribute or the `clipPath` element with the
  /// specified text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  AmbiguousMarkup clipPath(String text);

  /// Encodes the `form` attribute or the `form` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  AmbiguousMarkup form(String text);

  /// Encodes the `label` attribute or the `label` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  AmbiguousMarkup label(String text);

  /// Encodes the `style` attribute or the `style` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  AmbiguousMarkup style(String text);

  /// Encodes the `title` attribute or the `title` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  AmbiguousMarkup title(String text);

  //
  // TEXT
  //

  /// Renders the non-breaking space `&nbsp;` HTML character entity.
  ///
  /// @return an instruction representing the non-breaking space
  ///         character entity.
  RawMarkup nbsp();

  /// Renders the specified value as raw HTML.
  ///
  /// @param value the raw HTML value
  ///
  /// @return a raw HTML instruction
  RawMarkup raw(String value);

  /// Renders a text node with the specified value. The text value is escaped
  /// before being emitted to the output.
  ///
  /// @param value the text value
  ///
  /// @return an instruction representing the text node
  TextMarkup text(String value);

  // END generated code

}