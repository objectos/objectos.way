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
import objectos.html.markup.AmbiguousInstruction;
import objectos.html.markup.AttributeMarkup;
import objectos.html.markup.ElementMarkup;
import objectos.html.markup.FlattenInstruction;
import objectos.html.markup.Fragment0;
import objectos.html.markup.Fragment1;
import objectos.html.markup.Fragment2;
import objectos.html.markup.Fragment3;
import objectos.html.markup.Fragment4;
import objectos.html.markup.RegularElementContent;
import objectos.html.markup.ElementContent;
import objectos.html.markup.FragmentInstruction;
import objectos.html.markup.Instruction;
import objectos.html.markup.NoOpInstruction;
import objectos.html.markup.RawInstruction;
import objectos.html.markup.TextInstruction;
import objectos.html.markup.VoidElementContent;
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
  Attribute async = Attribute.of(AttributeName.ASYNC);

  /// The `autofocus` boolean attribute.
  Attribute autofocus = Attribute.of(AttributeName.AUTOFOCUS);

  /// The `checked` boolean attribute.
  Attribute checked = Attribute.of(AttributeName.CHECKED);

  /// The `defer` boolean attribute.
  Attribute defer = Attribute.of(AttributeName.DEFER);

  /// The `disabled` boolean attribute.
  Attribute disabled = Attribute.of(AttributeName.DISABLED);

  /// The `hidden` boolean attribute.
  Attribute hidden = Attribute.of(AttributeName.HIDDEN);

  /// The `multiple` boolean attribute.
  Attribute multiple = Attribute.of(AttributeName.MULTIPLE);

  /// The `nomodule` boolean attribute.
  Attribute nomodule = Attribute.of(AttributeName.NOMODULE);

  /// The `open` boolean attribute.
  Attribute open = Attribute.of(AttributeName.OPEN);

  /// The `readonly` boolean attribute.
  Attribute readonly = Attribute.of(AttributeName.READONLY);

  /// The `required` boolean attribute.
  Attribute required = Attribute.of(AttributeName.REQUIRED);

  /// The `reversed` boolean attribute.
  Attribute reversed = Attribute.of(AttributeName.REVERSED);

  /// The `selected` boolean attribute.
  Attribute selected = Attribute.of(AttributeName.SELECTED);

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
  AttributeMarkup css(String value);

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

  /// The no-op instruction.
  /// @return the no-op instruction.
  NoOpInstruction noop();

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
  NoOpInstruction testableNewLine();

  //
  // ELEMENTS
  //

  /// Renders an HTML element with the specified name and contents.
  ///
  /// @param name the element name
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element
  ElementMarkup elem(ElementName name, ElementContent... contents);

  /// Renders an HTML element with the specified name and text.
  ///
  /// @param name the element name
  /// @param text the text value of this element
  ///
  /// @return an instruction representing the element
  ElementMarkup elem(ElementName name, String text);

  /// Renders the `<!DOCTYPE html>` doctype.
  void doctype();

  /// Renders the `a` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup a(RegularElementContent... contents);

  /// Renders the `a` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup a(String text);

  /// Renders the `abbr` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup abbr(RegularElementContent... contents);

  /// Renders the `abbr` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup abbr(String text);

  /// Renders the `article` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup article(RegularElementContent... contents);

  /// Renders the `article` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup article(String text);

  /// Renders the `aside` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup aside(RegularElementContent... contents);

  /// Renders the `aside` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup aside(String text);

  /// Renders the `b` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup b(RegularElementContent... contents);

  /// Renders the `b` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup b(String text);

  /// Renders the `blockquote` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup blockquote(RegularElementContent... contents);

  /// Renders the `blockquote` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup blockquote(String text);

  /// Renders the `body` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup body(RegularElementContent... contents);

  /// Renders the `body` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup body(String text);

  /// Renders the `br` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup br(VoidElementContent... contents);

  /// Renders the `button` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup button(RegularElementContent... contents);

  /// Renders the `button` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup button(String text);

  /// Renders the `clipPath` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup clipPath(RegularElementContent... contents);

  /// Renders the `code` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup code(RegularElementContent... contents);

  /// Renders the `code` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup code(String text);

  /// Renders the `dd` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup dd(RegularElementContent... contents);

  /// Renders the `dd` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup dd(String text);

  /// Renders the `defs` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup defs(RegularElementContent... contents);

  /// Renders the `defs` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup defs(String text);

  /// Renders the `details` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup details(RegularElementContent... contents);

  /// Renders the `details` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup details(String text);

  /// Renders the `dialog` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup dialog(RegularElementContent... contents);

  /// Renders the `dialog` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup dialog(String text);

  /// Renders the `div` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup div(RegularElementContent... contents);

  /// Renders the `div` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup div(String text);

  /// Renders the `dl` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup dl(RegularElementContent... contents);

  /// Renders the `dl` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup dl(String text);

  /// Renders the `dt` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup dt(RegularElementContent... contents);

  /// Renders the `dt` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup dt(String text);

  /// Renders the `em` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup em(RegularElementContent... contents);

  /// Renders the `em` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup em(String text);

  /// Renders the `fieldset` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup fieldset(RegularElementContent... contents);

  /// Renders the `fieldset` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup fieldset(String text);

  /// Renders the `figure` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup figure(RegularElementContent... contents);

  /// Renders the `figure` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup figure(String text);

  /// Renders the `footer` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup footer(RegularElementContent... contents);

  /// Renders the `footer` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup footer(String text);

  /// Renders the `form` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup form(RegularElementContent... contents);

  /// Renders the `g` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup g(RegularElementContent... contents);

  /// Renders the `g` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup g(String text);

  /// Renders the `h1` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h1(RegularElementContent... contents);

  /// Renders the `h1` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h1(String text);

  /// Renders the `h2` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h2(RegularElementContent... contents);

  /// Renders the `h2` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h2(String text);

  /// Renders the `h3` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h3(RegularElementContent... contents);

  /// Renders the `h3` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h3(String text);

  /// Renders the `h4` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h4(RegularElementContent... contents);

  /// Renders the `h4` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h4(String text);

  /// Renders the `h5` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h5(RegularElementContent... contents);

  /// Renders the `h5` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h5(String text);

  /// Renders the `h6` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h6(RegularElementContent... contents);

  /// Renders the `h6` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup h6(String text);

  /// Renders the `head` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup head(RegularElementContent... contents);

  /// Renders the `head` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup head(String text);

  /// Renders the `header` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup header(RegularElementContent... contents);

  /// Renders the `header` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup header(String text);

  /// Renders the `hgroup` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup hgroup(RegularElementContent... contents);

  /// Renders the `hgroup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup hgroup(String text);

  /// Renders the `hr` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup hr(VoidElementContent... contents);

  /// Renders the `html` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup html(RegularElementContent... contents);

  /// Renders the `html` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup html(String text);

  /// Renders the `img` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup img(VoidElementContent... contents);

  /// Renders the `input` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup input(VoidElementContent... contents);

  /// Renders the `kbd` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup kbd(RegularElementContent... contents);

  /// Renders the `kbd` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup kbd(String text);

  /// Renders the `label` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup label(RegularElementContent... contents);

  /// Renders the `legend` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup legend(RegularElementContent... contents);

  /// Renders the `legend` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup legend(String text);

  /// Renders the `li` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup li(RegularElementContent... contents);

  /// Renders the `li` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup li(String text);

  /// Renders the `link` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup link(VoidElementContent... contents);

  /// Renders the `main` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup main(RegularElementContent... contents);

  /// Renders the `main` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup main(String text);

  /// Renders the `menu` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup menu(RegularElementContent... contents);

  /// Renders the `menu` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup menu(String text);

  /// Renders the `meta` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup meta(VoidElementContent... contents);

  /// Renders the `nav` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup nav(RegularElementContent... contents);

  /// Renders the `nav` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup nav(String text);

  /// Renders the `noscript` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup noscript(RegularElementContent... contents);

  /// Renders the `noscript` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup noscript(String text);

  /// Renders the `ol` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup ol(RegularElementContent... contents);

  /// Renders the `ol` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup ol(String text);

  /// Renders the `optgroup` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup optgroup(RegularElementContent... contents);

  /// Renders the `optgroup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup optgroup(String text);

  /// Renders the `option` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup option(RegularElementContent... contents);

  /// Renders the `option` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup option(String text);

  /// Renders the `p` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup p(RegularElementContent... contents);

  /// Renders the `p` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup p(String text);

  /// Renders the `path` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup path(RegularElementContent... contents);

  /// Renders the `path` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup path(String text);

  /// Renders the `pre` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup pre(RegularElementContent... contents);

  /// Renders the `pre` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup pre(String text);

  /// Renders the `progress` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup progress(RegularElementContent... contents);

  /// Renders the `progress` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup progress(String text);

  /// Renders the `samp` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup samp(RegularElementContent... contents);

  /// Renders the `samp` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup samp(String text);

  /// Renders the `script` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup script(RegularElementContent... contents);

  /// Renders the `script` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup script(String text);

  /// Renders the `section` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup section(RegularElementContent... contents);

  /// Renders the `section` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup section(String text);

  /// Renders the `select` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup select(RegularElementContent... contents);

  /// Renders the `select` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup select(String text);

  /// Renders the `small` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup small(RegularElementContent... contents);

  /// Renders the `small` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup small(String text);

  /// Renders the `span` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup span(RegularElementContent... contents);

  /// Renders the `span` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup span(String text);

  /// Renders the `strong` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup strong(RegularElementContent... contents);

  /// Renders the `strong` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup strong(String text);

  /// Renders the `style` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup style(RegularElementContent... contents);

  /// Renders the `sub` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup sub(RegularElementContent... contents);

  /// Renders the `sub` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup sub(String text);

  /// Renders the `summary` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup summary(RegularElementContent... contents);

  /// Renders the `summary` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup summary(String text);

  /// Renders the `sup` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup sup(RegularElementContent... contents);

  /// Renders the `sup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup sup(String text);

  /// Renders the `svg` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup svg(RegularElementContent... contents);

  /// Renders the `svg` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup svg(String text);

  /// Renders the `table` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup table(RegularElementContent... contents);

  /// Renders the `table` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup table(String text);

  /// Renders the `tbody` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup tbody(RegularElementContent... contents);

  /// Renders the `tbody` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup tbody(String text);

  /// Renders the `td` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup td(RegularElementContent... contents);

  /// Renders the `td` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup td(String text);

  /// Renders the `template` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup template(RegularElementContent... contents);

  /// Renders the `template` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup template(String text);

  /// Renders the `textarea` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup textarea(RegularElementContent... contents);

  /// Renders the `textarea` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup textarea(String text);

  /// Renders the `th` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup th(RegularElementContent... contents);

  /// Renders the `th` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup th(String text);

  /// Renders the `thead` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup thead(RegularElementContent... contents);

  /// Renders the `thead` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup thead(String text);

  /// Renders the `title` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup title(RegularElementContent... contents);

  /// Renders the `tr` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup tr(RegularElementContent... contents);

  /// Renders the `tr` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup tr(String text);

  /// Renders the `ul` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup ul(RegularElementContent... contents);

  /// Renders the `ul` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  ElementMarkup ul(String text);

  //
  // ATTRIBUTES
  //

  /// Renders an attribute with the specified name.
  ///
  /// @param name the attribute name
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup attr(AttributeName name);

  /// Renders an attribute with the specified name and value.
  ///
  /// @param name the attribute name
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup attr(AttributeName name, String value);

  /// Renders the `accesskey` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup accesskey(String value);

  /// Renders the `action` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup action(String value);

  /// Renders the `align` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup align(String value);

  /// Renders the `alignment-baseline` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup alignmentBaseline(String value);

  /// Renders the `alt` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup alt(String value);

  /// Renders the `aria-current` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaCurrent(String value);

  /// Renders the `aria-disabled` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaDisabled(String value);

  /// Renders the `aria-hidden` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaHidden(String value);

  /// Renders the `aria-invalid` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaInvalid(String value);

  /// Renders the `aria-label` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaLabel(String value);

  /// Renders the `aria-labelledby` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaLabelledBy(String value);

  /// Renders the `aria-modal` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaModal(String value);

  /// Renders the `aria-placeholder` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaPlaceholder(String value);

  /// Renders the `aria-readonly` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaReadonly(String value);

  /// Renders the `aria-required` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaRequired(String value);

  /// Renders the `aria-selected` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup ariaSelected(String value);

  /// Renders the `as` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup as(String value);

  /// Renders the `autocomplete` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup autocomplete(String value);

  /// Renders the `baseline-shift` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup baselineShift(String value);

  /// Renders the `border` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup border(String value);

  /// Renders the `cellpadding` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup cellpadding(String value);

  /// Renders the `cellspacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup cellspacing(String value);

  /// Renders the `charset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup charset(String value);

  /// Renders the `cite` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup cite(String value);

  /// Renders the `class` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup className(String value);

  /// Renders the `clip-rule` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup clipRule(String value);

  /// Renders the `closedby` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup closedby(String value);

  /// Renders the `color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup color(String value);

  /// Renders the `color-interpolation` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup colorInterpolation(String value);

  /// Renders the `color-interpolation-filters` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup colorInterpolationFilters(String value);

  /// Renders the `cols` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup cols(String value);

  /// Renders the `content` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup content(String value);

  /// Renders the `contenteditable` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup contenteditable(String value);

  /// Renders the `crossorigin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup crossorigin(String value);

  /// Renders the `cursor` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup cursor(String value);

  /// Renders the `d` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup d(String value);

  /// Renders the `dir` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup dir(String value);

  /// Renders the `direction` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup direction(String value);

  /// Renders the `dirname` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup dirname(String value);

  /// Renders the `display` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup display(String value);

  /// Renders the `dominant-baseline` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup dominantBaseline(String value);

  /// Renders the `download` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup download(String value);

  /// Renders the `draggable` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup draggable(String value);

  /// Renders the `enctype` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup enctype(String value);

  /// Renders the `fill` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup fill(String value);

  /// Renders the `fill-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup fillOpacity(String value);

  /// Renders the `fill-rule` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup fillRule(String value);

  /// Renders the `filter` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup filter(String value);

  /// Renders the `flood-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup floodColor(String value);

  /// Renders the `flood-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup floodOpacity(String value);

  /// Renders the `for` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup forId(String value);

  /// Renders the `glyph-orientation-horizontal` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup glyphOrientationHorizontal(String value);

  /// Renders the `glyph-orientation-vertical` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup glyphOrientationVertical(String value);

  /// Renders the `height` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup height(String value);

  /// Renders the `href` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup href(String value);

  /// Renders the `http-equiv` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup httpEquiv(String value);

  /// Renders the `id` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup id(String value);

  /// Renders the `image-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup imageRendering(String value);

  /// Renders the `integrity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup integrity(String value);

  /// Renders the `lang` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup lang(String value);

  /// Renders the `letter-spacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup letterSpacing(String value);

  /// Renders the `lighting-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup lightingColor(String value);

  /// Renders the `marker-end` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup markerEnd(String value);

  /// Renders the `marker-mid` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup markerMid(String value);

  /// Renders the `marker-start` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup markerStart(String value);

  /// Renders the `mask` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup mask(String value);

  /// Renders the `mask-type` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup maskType(String value);

  /// Renders the `maxlength` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup maxlength(String value);

  /// Renders the `media` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup media(String value);

  /// Renders the `method` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup method(String value);

  /// Renders the `minlength` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup minlength(String value);

  /// Renders the `name` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup name(String value);

  /// Renders the `onclick` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup onclick(String value);

  /// Renders the `onclick` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup onclick(JsAction value);

  /// Renders the `onload` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup onload(String value);

  /// Renders the `onload` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup onload(JsAction value);

  /// Renders the `onpopstate` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup onpopstate(String value);

  /// Renders the `onpopstate` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup onpopstate(JsAction value);

  /// Renders the `onsubmit` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup onsubmit(String value);

  /// Renders the `onsubmit` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup onsubmit(JsAction value);

  /// Renders the `opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup opacity(String value);

  /// Renders the `overflow` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup overflow(String value);

  /// Renders the `paint-order` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup paintOrder(String value);

  /// Renders the `placeholder` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup placeholder(String value);

  /// Renders the `pointer-events` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup pointerEvents(String value);

  /// Renders the `property` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup property(String value);

  /// Renders the `referrerpolicy` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup referrerpolicy(String value);

  /// Renders the `rel` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup rel(String value);

  /// Renders the `rev` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup rev(String value);

  /// Renders the `role` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup role(String value);

  /// Renders the `rows` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup rows(String value);

  /// Renders the `shape-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup shapeRendering(String value);

  /// Renders the `size` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup size(String value);

  /// Renders the `sizes` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup sizes(String value);

  /// Renders the `spellcheck` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup spellcheck(String value);

  /// Renders the `src` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup src(String value);

  /// Renders the `srcset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup srcset(String value);

  /// Renders the `start` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup start(String value);

  /// Renders the `stop-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup stopColor(String value);

  /// Renders the `stop-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup stopOpacity(String value);

  /// Renders the `stroke` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup stroke(String value);

  /// Renders the `stroke-dasharray` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup strokeDasharray(String value);

  /// Renders the `stroke-dashoffset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup strokeDashoffset(String value);

  /// Renders the `stroke-linecap` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup strokeLinecap(String value);

  /// Renders the `stroke-linejoin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup strokeLinejoin(String value);

  /// Renders the `stroke-miterlimit` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup strokeMiterlimit(String value);

  /// Renders the `stroke-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup strokeOpacity(String value);

  /// Renders the `stroke-width` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup strokeWidth(String value);

  /// Renders the `tabindex` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup tabindex(String value);

  /// Renders the `target` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup target(String value);

  /// Renders the `text-anchor` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup textAnchor(String value);

  /// Renders the `text-decoration` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup textDecoration(String value);

  /// Renders the `text-overflow` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup textOverflow(String value);

  /// Renders the `text-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup textRendering(String value);

  /// Renders the `transform` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup transform(String value);

  /// Renders the `transform-origin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup transformOrigin(String value);

  /// Renders the `translate` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup translate(String value);

  /// Renders the `type` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup type(String value);

  /// Renders the `unicode-bidi` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup unicodeBidi(String value);

  /// Renders the `value` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup value(String value);

  /// Renders the `vector-effect` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup vectorEffect(String value);

  /// Renders the `viewBox` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup viewBox(String value);

  /// Renders the `visibility` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup visibility(String value);

  /// Renders the `white-space` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup whiteSpace(String value);

  /// Renders the `width` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup width(String value);

  /// Renders the `word-spacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup wordSpacing(String value);

  /// Renders the `wrap` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup wrap(String value);

  /// Renders the `writing-mode` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup writingMode(String value);

  /// Renders the `xmlns` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  AttributeMarkup xmlns(String value);

  //
  // AMBIGUOUS
  //

  /// Renders the `clip-path` attribute or the `clipPath` element with the
  /// specified text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an instruction representing the attribute or the element
  AmbiguousInstruction clipPath(String text);

  /// Renders the `form` attribute or the `form` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an instruction representing the attribute or the element
  AmbiguousInstruction form(String text);

  /// Renders the `label` attribute or the `label` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an instruction representing the attribute or the element
  AmbiguousInstruction label(String text);

  /// Renders the `style` attribute or the `style` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an instruction representing the attribute or the element
  AmbiguousInstruction style(String text);

  /// Renders the `title` attribute or the `title` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an instruction representing the attribute or the element
  AmbiguousInstruction title(String text);

  //
  // TEXT
  //

  /// Renders the non-breaking space `&nbsp;` HTML character entity.
  ///
  /// @return an instruction representing the non-breaking space
  ///         character entity.
  RawInstruction nbsp();

  /// Renders the specified value as raw HTML.
  ///
  /// @param value the raw HTML value
  ///
  /// @return a raw HTML instruction
  RawInstruction raw(String value);

  /// Renders a text node with the specified value. The text value is escaped
  /// before being emitted to the output.
  ///
  /// @param value the text value
  ///
  /// @return an instruction representing the text node
  TextInstruction text(String value);

  // END generated code

}