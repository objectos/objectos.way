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

import java.util.Objects;
import objectos.html.Attribute;
import objectos.html.ElementName;
import objectos.html.markup.AmbiguousMarkup;
import objectos.html.markup.ElementMarkup;
import objectos.html.markup.ElementValue;
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
import objectox.html.attr.AttributeJsAction;
import objectox.html.attr.AttributeNamePojo;
import objectox.html.attr.AttributePojo;
import objectox.html.elem.ElementNamePojo;
import objectox.html.markup.AmbiguousPojo;
import objectox.html.markup.RawNode;
import objectox.html.markup.TextNode;

sealed abstract class MarkupGenerated permits MarkupPojo {

  // START generated code

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
public abstract Attribute css(String value);

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
public abstract FragmentMarkup f(Fragment0 fragment);

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
public abstract <T1> FragmentMarkup f(Fragment1<T1> fragment, T1 arg1);

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
public abstract <T1, T2> FragmentMarkup f(Fragment2<T1, T2> fragment, T1 arg1, T2 arg2);

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
public abstract <T1, T2, T3> FragmentMarkup f(Fragment3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3);

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
public abstract <T1, T2, T3, T4> FragmentMarkup f(Fragment4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4);

/// The no-op markup.
/// @return the no-op markup.
public abstract NoOpMarkup noop();

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
  public abstract String testableCell(String value, int width);

  /// Formats the specified name and value as a testable field.
  ///
  /// @param name the field name
  /// @param value the field value
  ///
  /// @return always the field value
  public abstract String testableField(String name, String value);

  /// Formats the specified name as a testable field name.
  ///
  /// @param name the field name
  ///
  /// @return the specified field name
  public abstract String testableFieldName(String name);

  /// Formats the specified value as a testable field value.
  ///
  /// @param value the field value
  ///
  /// @return the specified field value
  public abstract String testableFieldValue(String value);

  /// Formats the specified value as a testable heading level 1.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  public abstract String testableH1(String value);

  /// Formats the specified value as a testable heading level 2.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  public abstract String testableH2(String value);

  /// Formats the specified value as a testable heading level 3.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  public abstract String testableH3(String value);

  /// Formats the specified value as a testable heading level 4.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  public abstract String testableH4(String value);

  /// Formats the specified value as a testable heading level 5.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  public abstract String testableH5(String value);

  /// Formats the specified value as a testable heading level 6.
  ///
  /// @param value the heading value
  ///
  /// @return the specified value
  public abstract String testableH6(String value);

  /// Formats a line separator at the testable output exclusively.
  ///
  /// @return a no-op instruction
  public abstract NoOpMarkup testableNewLine();

  //
  // ELEMENTS
  //

  abstract ElementMarkup elem0(ElementName name, ElementValue... values);

  abstract ElementMarkup elem0(ElementName name, String text);

  /// Encodes an HTML element with the specified name and values.
  ///
  /// @param name the element name
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup elem(ElementName name, ElementValue... values) {
    Objects.requireNonNull(name, "name == null");
    return elem0(name, values);
  }

  /// Encodes an HTML element with the specified name and text.
  ///
  /// @param name the element name
  /// @param text the text value of this element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup elem(ElementName name, String text) {
    Objects.requireNonNull(name, "name == null");
    return elem0(name, text);
  }

  /// Encodes the `<!DOCTYPE html>` doctype.
  public abstract void doctype();

  /// Encodes the `a` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup a(RegularElementValue... values) {
    return elem0(ElementNamePojo.A, values);
  }

  /// Encodes the `a` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup a(String text) {
    return elem0(ElementNamePojo.A, text);
  }

  /// Encodes the `abbr` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup abbr(RegularElementValue... values) {
    return elem0(ElementNamePojo.ABBR, values);
  }

  /// Encodes the `abbr` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup abbr(String text) {
    return elem0(ElementNamePojo.ABBR, text);
  }

  /// Encodes the `article` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup article(RegularElementValue... values) {
    return elem0(ElementNamePojo.ARTICLE, values);
  }

  /// Encodes the `article` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup article(String text) {
    return elem0(ElementNamePojo.ARTICLE, text);
  }

  /// Encodes the `aside` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup aside(RegularElementValue... values) {
    return elem0(ElementNamePojo.ASIDE, values);
  }

  /// Encodes the `aside` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup aside(String text) {
    return elem0(ElementNamePojo.ASIDE, text);
  }

  /// Encodes the `b` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup b(RegularElementValue... values) {
    return elem0(ElementNamePojo.B, values);
  }

  /// Encodes the `b` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup b(String text) {
    return elem0(ElementNamePojo.B, text);
  }

  /// Encodes the `blockquote` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup blockquote(RegularElementValue... values) {
    return elem0(ElementNamePojo.BLOCKQUOTE, values);
  }

  /// Encodes the `blockquote` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup blockquote(String text) {
    return elem0(ElementNamePojo.BLOCKQUOTE, text);
  }

  /// Encodes the `body` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup body(RegularElementValue... values) {
    return elem0(ElementNamePojo.BODY, values);
  }

  /// Encodes the `body` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup body(String text) {
    return elem0(ElementNamePojo.BODY, text);
  }

  /// Encodes the `br` element with the specified content.
  ///
  /// @param contents the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup br(VoidElementValue... contents) {
    return elem0(ElementNamePojo.BR, contents);
  }

  /// Encodes the `button` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup button(RegularElementValue... values) {
    return elem0(ElementNamePojo.BUTTON, values);
  }

  /// Encodes the `button` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup button(String text) {
    return elem0(ElementNamePojo.BUTTON, text);
  }

  /// Encodes the `clipPath` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup clipPath(RegularElementValue... values) {
    return elem0(ElementNamePojo.CLIPPATH, values);
  }

  /// Encodes the `code` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup code(RegularElementValue... values) {
    return elem0(ElementNamePojo.CODE, values);
  }

  /// Encodes the `code` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup code(String text) {
    return elem0(ElementNamePojo.CODE, text);
  }

  /// Encodes the `dd` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup dd(RegularElementValue... values) {
    return elem0(ElementNamePojo.DD, values);
  }

  /// Encodes the `dd` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup dd(String text) {
    return elem0(ElementNamePojo.DD, text);
  }

  /// Encodes the `defs` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup defs(RegularElementValue... values) {
    return elem0(ElementNamePojo.DEFS, values);
  }

  /// Encodes the `defs` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup defs(String text) {
    return elem0(ElementNamePojo.DEFS, text);
  }

  /// Encodes the `details` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup details(RegularElementValue... values) {
    return elem0(ElementNamePojo.DETAILS, values);
  }

  /// Encodes the `details` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup details(String text) {
    return elem0(ElementNamePojo.DETAILS, text);
  }

  /// Encodes the `dialog` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup dialog(RegularElementValue... values) {
    return elem0(ElementNamePojo.DIALOG, values);
  }

  /// Encodes the `dialog` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup dialog(String text) {
    return elem0(ElementNamePojo.DIALOG, text);
  }

  /// Encodes the `div` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup div(RegularElementValue... values) {
    return elem0(ElementNamePojo.DIV, values);
  }

  /// Encodes the `div` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup div(String text) {
    return elem0(ElementNamePojo.DIV, text);
  }

  /// Encodes the `dl` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup dl(RegularElementValue... values) {
    return elem0(ElementNamePojo.DL, values);
  }

  /// Encodes the `dl` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup dl(String text) {
    return elem0(ElementNamePojo.DL, text);
  }

  /// Encodes the `dt` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup dt(RegularElementValue... values) {
    return elem0(ElementNamePojo.DT, values);
  }

  /// Encodes the `dt` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup dt(String text) {
    return elem0(ElementNamePojo.DT, text);
  }

  /// Encodes the `em` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup em(RegularElementValue... values) {
    return elem0(ElementNamePojo.EM, values);
  }

  /// Encodes the `em` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup em(String text) {
    return elem0(ElementNamePojo.EM, text);
  }

  /// Encodes the `fieldset` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup fieldset(RegularElementValue... values) {
    return elem0(ElementNamePojo.FIELDSET, values);
  }

  /// Encodes the `fieldset` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup fieldset(String text) {
    return elem0(ElementNamePojo.FIELDSET, text);
  }

  /// Encodes the `figure` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup figure(RegularElementValue... values) {
    return elem0(ElementNamePojo.FIGURE, values);
  }

  /// Encodes the `figure` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup figure(String text) {
    return elem0(ElementNamePojo.FIGURE, text);
  }

  /// Encodes the `footer` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup footer(RegularElementValue... values) {
    return elem0(ElementNamePojo.FOOTER, values);
  }

  /// Encodes the `footer` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup footer(String text) {
    return elem0(ElementNamePojo.FOOTER, text);
  }

  /// Encodes the `form` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup form(RegularElementValue... values) {
    return elem0(ElementNamePojo.FORM, values);
  }

  /// Encodes the `g` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup g(RegularElementValue... values) {
    return elem0(ElementNamePojo.G, values);
  }

  /// Encodes the `g` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup g(String text) {
    return elem0(ElementNamePojo.G, text);
  }

  /// Encodes the `h1` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h1(RegularElementValue... values) {
    return elem0(ElementNamePojo.H1, values);
  }

  /// Encodes the `h1` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h1(String text) {
    return elem0(ElementNamePojo.H1, text);
  }

  /// Encodes the `h2` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h2(RegularElementValue... values) {
    return elem0(ElementNamePojo.H2, values);
  }

  /// Encodes the `h2` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h2(String text) {
    return elem0(ElementNamePojo.H2, text);
  }

  /// Encodes the `h3` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h3(RegularElementValue... values) {
    return elem0(ElementNamePojo.H3, values);
  }

  /// Encodes the `h3` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h3(String text) {
    return elem0(ElementNamePojo.H3, text);
  }

  /// Encodes the `h4` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h4(RegularElementValue... values) {
    return elem0(ElementNamePojo.H4, values);
  }

  /// Encodes the `h4` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h4(String text) {
    return elem0(ElementNamePojo.H4, text);
  }

  /// Encodes the `h5` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h5(RegularElementValue... values) {
    return elem0(ElementNamePojo.H5, values);
  }

  /// Encodes the `h5` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h5(String text) {
    return elem0(ElementNamePojo.H5, text);
  }

  /// Encodes the `h6` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h6(RegularElementValue... values) {
    return elem0(ElementNamePojo.H6, values);
  }

  /// Encodes the `h6` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup h6(String text) {
    return elem0(ElementNamePojo.H6, text);
  }

  /// Encodes the `head` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup head(RegularElementValue... values) {
    return elem0(ElementNamePojo.HEAD, values);
  }

  /// Encodes the `head` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup head(String text) {
    return elem0(ElementNamePojo.HEAD, text);
  }

  /// Encodes the `header` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup header(RegularElementValue... values) {
    return elem0(ElementNamePojo.HEADER, values);
  }

  /// Encodes the `header` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup header(String text) {
    return elem0(ElementNamePojo.HEADER, text);
  }

  /// Encodes the `hgroup` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup hgroup(RegularElementValue... values) {
    return elem0(ElementNamePojo.HGROUP, values);
  }

  /// Encodes the `hgroup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup hgroup(String text) {
    return elem0(ElementNamePojo.HGROUP, text);
  }

  /// Encodes the `hr` element with the specified content.
  ///
  /// @param contents the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup hr(VoidElementValue... contents) {
    return elem0(ElementNamePojo.HR, contents);
  }

  /// Encodes the `html` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup html(RegularElementValue... values) {
    return elem0(ElementNamePojo.HTML, values);
  }

  /// Encodes the `html` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup html(String text) {
    return elem0(ElementNamePojo.HTML, text);
  }

  /// Encodes the `img` element with the specified content.
  ///
  /// @param contents the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup img(VoidElementValue... contents) {
    return elem0(ElementNamePojo.IMG, contents);
  }

  /// Encodes the `input` element with the specified content.
  ///
  /// @param contents the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup input(VoidElementValue... contents) {
    return elem0(ElementNamePojo.INPUT, contents);
  }

  /// Encodes the `kbd` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup kbd(RegularElementValue... values) {
    return elem0(ElementNamePojo.KBD, values);
  }

  /// Encodes the `kbd` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup kbd(String text) {
    return elem0(ElementNamePojo.KBD, text);
  }

  /// Encodes the `label` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup label(RegularElementValue... values) {
    return elem0(ElementNamePojo.LABEL, values);
  }

  /// Encodes the `legend` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup legend(RegularElementValue... values) {
    return elem0(ElementNamePojo.LEGEND, values);
  }

  /// Encodes the `legend` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup legend(String text) {
    return elem0(ElementNamePojo.LEGEND, text);
  }

  /// Encodes the `li` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup li(RegularElementValue... values) {
    return elem0(ElementNamePojo.LI, values);
  }

  /// Encodes the `li` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup li(String text) {
    return elem0(ElementNamePojo.LI, text);
  }

  /// Encodes the `link` element with the specified content.
  ///
  /// @param contents the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup link(VoidElementValue... contents) {
    return elem0(ElementNamePojo.LINK, contents);
  }

  /// Encodes the `main` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup main(RegularElementValue... values) {
    return elem0(ElementNamePojo.MAIN, values);
  }

  /// Encodes the `main` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup main(String text) {
    return elem0(ElementNamePojo.MAIN, text);
  }

  /// Encodes the `menu` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup menu(RegularElementValue... values) {
    return elem0(ElementNamePojo.MENU, values);
  }

  /// Encodes the `menu` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup menu(String text) {
    return elem0(ElementNamePojo.MENU, text);
  }

  /// Encodes the `meta` element with the specified content.
  ///
  /// @param contents the encoded attributes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup meta(VoidElementValue... contents) {
    return elem0(ElementNamePojo.META, contents);
  }

  /// Encodes the `nav` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup nav(RegularElementValue... values) {
    return elem0(ElementNamePojo.NAV, values);
  }

  /// Encodes the `nav` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup nav(String text) {
    return elem0(ElementNamePojo.NAV, text);
  }

  /// Encodes the `noscript` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup noscript(RegularElementValue... values) {
    return elem0(ElementNamePojo.NOSCRIPT, values);
  }

  /// Encodes the `noscript` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup noscript(String text) {
    return elem0(ElementNamePojo.NOSCRIPT, text);
  }

  /// Encodes the `ol` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup ol(RegularElementValue... values) {
    return elem0(ElementNamePojo.OL, values);
  }

  /// Encodes the `ol` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup ol(String text) {
    return elem0(ElementNamePojo.OL, text);
  }

  /// Encodes the `optgroup` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup optgroup(RegularElementValue... values) {
    return elem0(ElementNamePojo.OPTGROUP, values);
  }

  /// Encodes the `optgroup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup optgroup(String text) {
    return elem0(ElementNamePojo.OPTGROUP, text);
  }

  /// Encodes the `option` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup option(RegularElementValue... values) {
    return elem0(ElementNamePojo.OPTION, values);
  }

  /// Encodes the `option` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup option(String text) {
    return elem0(ElementNamePojo.OPTION, text);
  }

  /// Encodes the `p` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup p(RegularElementValue... values) {
    return elem0(ElementNamePojo.P, values);
  }

  /// Encodes the `p` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup p(String text) {
    return elem0(ElementNamePojo.P, text);
  }

  /// Encodes the `path` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup path(RegularElementValue... values) {
    return elem0(ElementNamePojo.PATH, values);
  }

  /// Encodes the `path` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup path(String text) {
    return elem0(ElementNamePojo.PATH, text);
  }

  /// Encodes the `pre` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup pre(RegularElementValue... values) {
    return elem0(ElementNamePojo.PRE, values);
  }

  /// Encodes the `pre` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup pre(String text) {
    return elem0(ElementNamePojo.PRE, text);
  }

  /// Encodes the `progress` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup progress(RegularElementValue... values) {
    return elem0(ElementNamePojo.PROGRESS, values);
  }

  /// Encodes the `progress` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup progress(String text) {
    return elem0(ElementNamePojo.PROGRESS, text);
  }

  /// Encodes the `samp` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup samp(RegularElementValue... values) {
    return elem0(ElementNamePojo.SAMP, values);
  }

  /// Encodes the `samp` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup samp(String text) {
    return elem0(ElementNamePojo.SAMP, text);
  }

  /// Encodes the `script` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup script(RegularElementValue... values) {
    return elem0(ElementNamePojo.SCRIPT, values);
  }

  /// Encodes the `script` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup script(String text) {
    return elem0(ElementNamePojo.SCRIPT, text);
  }

  /// Encodes the `section` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup section(RegularElementValue... values) {
    return elem0(ElementNamePojo.SECTION, values);
  }

  /// Encodes the `section` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup section(String text) {
    return elem0(ElementNamePojo.SECTION, text);
  }

  /// Encodes the `select` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup select(RegularElementValue... values) {
    return elem0(ElementNamePojo.SELECT, values);
  }

  /// Encodes the `select` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup select(String text) {
    return elem0(ElementNamePojo.SELECT, text);
  }

  /// Encodes the `small` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup small(RegularElementValue... values) {
    return elem0(ElementNamePojo.SMALL, values);
  }

  /// Encodes the `small` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup small(String text) {
    return elem0(ElementNamePojo.SMALL, text);
  }

  /// Encodes the `span` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup span(RegularElementValue... values) {
    return elem0(ElementNamePojo.SPAN, values);
  }

  /// Encodes the `span` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup span(String text) {
    return elem0(ElementNamePojo.SPAN, text);
  }

  /// Encodes the `strong` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup strong(RegularElementValue... values) {
    return elem0(ElementNamePojo.STRONG, values);
  }

  /// Encodes the `strong` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup strong(String text) {
    return elem0(ElementNamePojo.STRONG, text);
  }

  /// Encodes the `style` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup style(RegularElementValue... values) {
    return elem0(ElementNamePojo.STYLE, values);
  }

  /// Encodes the `sub` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup sub(RegularElementValue... values) {
    return elem0(ElementNamePojo.SUB, values);
  }

  /// Encodes the `sub` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup sub(String text) {
    return elem0(ElementNamePojo.SUB, text);
  }

  /// Encodes the `summary` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup summary(RegularElementValue... values) {
    return elem0(ElementNamePojo.SUMMARY, values);
  }

  /// Encodes the `summary` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup summary(String text) {
    return elem0(ElementNamePojo.SUMMARY, text);
  }

  /// Encodes the `sup` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup sup(RegularElementValue... values) {
    return elem0(ElementNamePojo.SUP, values);
  }

  /// Encodes the `sup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup sup(String text) {
    return elem0(ElementNamePojo.SUP, text);
  }

  /// Encodes the `svg` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup svg(RegularElementValue... values) {
    return elem0(ElementNamePojo.SVG, values);
  }

  /// Encodes the `svg` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup svg(String text) {
    return elem0(ElementNamePojo.SVG, text);
  }

  /// Encodes the `table` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup table(RegularElementValue... values) {
    return elem0(ElementNamePojo.TABLE, values);
  }

  /// Encodes the `table` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup table(String text) {
    return elem0(ElementNamePojo.TABLE, text);
  }

  /// Encodes the `tbody` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup tbody(RegularElementValue... values) {
    return elem0(ElementNamePojo.TBODY, values);
  }

  /// Encodes the `tbody` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup tbody(String text) {
    return elem0(ElementNamePojo.TBODY, text);
  }

  /// Encodes the `td` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup td(RegularElementValue... values) {
    return elem0(ElementNamePojo.TD, values);
  }

  /// Encodes the `td` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup td(String text) {
    return elem0(ElementNamePojo.TD, text);
  }

  /// Encodes the `template` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup template(RegularElementValue... values) {
    return elem0(ElementNamePojo.TEMPLATE, values);
  }

  /// Encodes the `template` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup template(String text) {
    return elem0(ElementNamePojo.TEMPLATE, text);
  }

  /// Encodes the `textarea` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup textarea(RegularElementValue... values) {
    return elem0(ElementNamePojo.TEXTAREA, values);
  }

  /// Encodes the `textarea` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup textarea(String text) {
    return elem0(ElementNamePojo.TEXTAREA, text);
  }

  /// Encodes the `th` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup th(RegularElementValue... values) {
    return elem0(ElementNamePojo.TH, values);
  }

  /// Encodes the `th` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup th(String text) {
    return elem0(ElementNamePojo.TH, text);
  }

  /// Encodes the `thead` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup thead(RegularElementValue... values) {
    return elem0(ElementNamePojo.THEAD, values);
  }

  /// Encodes the `thead` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup thead(String text) {
    return elem0(ElementNamePojo.THEAD, text);
  }

  /// Encodes the `title` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup title(RegularElementValue... values) {
    return elem0(ElementNamePojo.TITLE, values);
  }

  /// Encodes the `tr` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup tr(RegularElementValue... values) {
    return elem0(ElementNamePojo.TR, values);
  }

  /// Encodes the `tr` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup tr(String text) {
    return elem0(ElementNamePojo.TR, text);
  }

  /// Encodes the `ul` element with the specified values.
  ///
  /// @param values the encoded attributes and encoded child nodes of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup ul(RegularElementValue... values) {
    return elem0(ElementNamePojo.UL, values);
  }

  /// Encodes the `ul` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an object representing the encoded element
  public final ElementMarkup ul(String text) {
    return elem0(ElementNamePojo.UL, text);
  }

  //
  // ATTRIBUTES
  //

  /// Encodes the `accesskey` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute accesskey(String value) {
    return AttributePojo.of0(AttributeNamePojo.ACCESSKEY, value);
  }

  /// Encodes the `action` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute action(String value) {
    return AttributePojo.of0(AttributeNamePojo.ACTION, value);
  }

  /// Encodes the `align` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute align(String value) {
    return AttributePojo.of0(AttributeNamePojo.ALIGN, value);
  }

  /// Encodes the `alignment-baseline` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute alignmentBaseline(String value) {
    return AttributePojo.of0(AttributeNamePojo.ALIGNMENT_BASELINE, value);
  }

  /// Encodes the `alt` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute alt(String value) {
    return AttributePojo.of0(AttributeNamePojo.ALT, value);
  }

  /// Encodes the `aria-current` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaCurrent(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_CURRENT, value);
  }

  /// Encodes the `aria-disabled` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaDisabled(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_DISABLED, value);
  }

  /// Encodes the `aria-hidden` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaHidden(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_HIDDEN, value);
  }

  /// Encodes the `aria-invalid` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaInvalid(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_INVALID, value);
  }

  /// Encodes the `aria-label` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaLabel(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_LABEL, value);
  }

  /// Encodes the `aria-labelledby` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaLabelledBy(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_LABELLED_BY, value);
  }

  /// Encodes the `aria-modal` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaModal(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_MODAL, value);
  }

  /// Encodes the `aria-placeholder` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaPlaceholder(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_PLACEHOLDER, value);
  }

  /// Encodes the `aria-readonly` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaReadonly(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_READONLY, value);
  }

  /// Encodes the `aria-required` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaRequired(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_REQUIRED, value);
  }

  /// Encodes the `aria-selected` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute ariaSelected(String value) {
    return AttributePojo.of0(AttributeNamePojo.ARIA_SELECTED, value);
  }

  /// Encodes the `as` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute as(String value) {
    return AttributePojo.of0(AttributeNamePojo.AS, value);
  }

  /// Encodes the `autocomplete` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute autocomplete(String value) {
    return AttributePojo.of0(AttributeNamePojo.AUTOCOMPLETE, value);
  }

  /// Encodes the `baseline-shift` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute baselineShift(String value) {
    return AttributePojo.of0(AttributeNamePojo.BASELINE_SHIFT, value);
  }

  /// Encodes the `border` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute border(String value) {
    return AttributePojo.of0(AttributeNamePojo.BORDER, value);
  }

  /// Encodes the `cellpadding` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute cellpadding(String value) {
    return AttributePojo.of0(AttributeNamePojo.CELLPADDING, value);
  }

  /// Encodes the `cellspacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute cellspacing(String value) {
    return AttributePojo.of0(AttributeNamePojo.CELLSPACING, value);
  }

  /// Encodes the `charset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute charset(String value) {
    return AttributePojo.of0(AttributeNamePojo.CHARSET, value);
  }

  /// Encodes the `cite` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute cite(String value) {
    return AttributePojo.of0(AttributeNamePojo.CITE, value);
  }

  /// Encodes the `class` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute className(String value) {
    return AttributePojo.of0(AttributeNamePojo.CLASS, value);
  }

  /// Encodes the `clip-rule` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute clipRule(String value) {
    return AttributePojo.of0(AttributeNamePojo.CLIP_RULE, value);
  }

  /// Encodes the `closedby` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute closedby(String value) {
    return AttributePojo.of0(AttributeNamePojo.CLOSEDBY, value);
  }

  /// Encodes the `color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute color(String value) {
    return AttributePojo.of0(AttributeNamePojo.COLOR, value);
  }

  /// Encodes the `color-interpolation` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute colorInterpolation(String value) {
    return AttributePojo.of0(AttributeNamePojo.COLOR_INTERPOLATION, value);
  }

  /// Encodes the `color-interpolation-filters` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute colorInterpolationFilters(String value) {
    return AttributePojo.of0(AttributeNamePojo.COLOR_INTERPOLATION_FILTERS, value);
  }

  /// Encodes the `cols` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute cols(String value) {
    return AttributePojo.of0(AttributeNamePojo.COLS, value);
  }

  /// Encodes the `content` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute content(String value) {
    return AttributePojo.of0(AttributeNamePojo.CONTENT, value);
  }

  /// Encodes the `contenteditable` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute contenteditable(String value) {
    return AttributePojo.of0(AttributeNamePojo.CONTENTEDITABLE, value);
  }

  /// Encodes the `crossorigin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute crossorigin(String value) {
    return AttributePojo.of0(AttributeNamePojo.CROSSORIGIN, value);
  }

  /// Encodes the `cursor` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute cursor(String value) {
    return AttributePojo.of0(AttributeNamePojo.CURSOR, value);
  }

  /// Encodes the `d` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute d(String value) {
    return AttributePojo.of0(AttributeNamePojo.D, value);
  }

  /// Encodes the `dir` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute dir(String value) {
    return AttributePojo.of0(AttributeNamePojo.DIR, value);
  }

  /// Encodes the `direction` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute direction(String value) {
    return AttributePojo.of0(AttributeNamePojo.DIRECTION, value);
  }

  /// Encodes the `dirname` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute dirname(String value) {
    return AttributePojo.of0(AttributeNamePojo.DIRNAME, value);
  }

  /// Encodes the `display` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute display(String value) {
    return AttributePojo.of0(AttributeNamePojo.DISPLAY, value);
  }

  /// Encodes the `dominant-baseline` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute dominantBaseline(String value) {
    return AttributePojo.of0(AttributeNamePojo.DOMINANT_BASELINE, value);
  }

  /// Encodes the `download` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute download(String value) {
    return AttributePojo.of0(AttributeNamePojo.DOWNLOAD, value);
  }

  /// Encodes the `draggable` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute draggable(String value) {
    return AttributePojo.of0(AttributeNamePojo.DRAGGABLE, value);
  }

  /// Encodes the `enctype` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute enctype(String value) {
    return AttributePojo.of0(AttributeNamePojo.ENCTYPE, value);
  }

  /// Encodes the `fill` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute fill(String value) {
    return AttributePojo.of0(AttributeNamePojo.FILL, value);
  }

  /// Encodes the `fill-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute fillOpacity(String value) {
    return AttributePojo.of0(AttributeNamePojo.FILL_OPACITY, value);
  }

  /// Encodes the `fill-rule` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute fillRule(String value) {
    return AttributePojo.of0(AttributeNamePojo.FILL_RULE, value);
  }

  /// Encodes the `filter` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute filter(String value) {
    return AttributePojo.of0(AttributeNamePojo.FILTER, value);
  }

  /// Encodes the `flood-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute floodColor(String value) {
    return AttributePojo.of0(AttributeNamePojo.FLOOD_COLOR, value);
  }

  /// Encodes the `flood-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute floodOpacity(String value) {
    return AttributePojo.of0(AttributeNamePojo.FLOOD_OPACITY, value);
  }

  /// Encodes the `for` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute forId(String value) {
    return AttributePojo.of0(AttributeNamePojo.FOR, value);
  }

  /// Encodes the `glyph-orientation-horizontal` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute glyphOrientationHorizontal(String value) {
    return AttributePojo.of0(AttributeNamePojo.GLYPH_ORIENTATION_HORIZONTAL, value);
  }

  /// Encodes the `glyph-orientation-vertical` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute glyphOrientationVertical(String value) {
    return AttributePojo.of0(AttributeNamePojo.GLYPH_ORIENTATION_VERTICAL, value);
  }

  /// Encodes the `height` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute height(String value) {
    return AttributePojo.of0(AttributeNamePojo.HEIGHT, value);
  }

  /// Encodes the `href` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute href(String value) {
    return AttributePojo.of0(AttributeNamePojo.HREF, value);
  }

  /// Encodes the `http-equiv` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute httpEquiv(String value) {
    return AttributePojo.of0(AttributeNamePojo.HTTP_EQUIV, value);
  }

  /// Encodes the `id` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute id(String value) {
    return AttributePojo.of0(AttributeNamePojo.ID, value);
  }

  /// Encodes the `image-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute imageRendering(String value) {
    return AttributePojo.of0(AttributeNamePojo.IMAGE_RENDERING, value);
  }

  /// Encodes the `integrity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute integrity(String value) {
    return AttributePojo.of0(AttributeNamePojo.INTEGRITY, value);
  }

  /// Encodes the `lang` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute lang(String value) {
    return AttributePojo.of0(AttributeNamePojo.LANG, value);
  }

  /// Encodes the `letter-spacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute letterSpacing(String value) {
    return AttributePojo.of0(AttributeNamePojo.LETTER_SPACING, value);
  }

  /// Encodes the `lighting-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute lightingColor(String value) {
    return AttributePojo.of0(AttributeNamePojo.LIGHTING_COLOR, value);
  }

  /// Encodes the `marker-end` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute markerEnd(String value) {
    return AttributePojo.of0(AttributeNamePojo.MARKER_END, value);
  }

  /// Encodes the `marker-mid` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute markerMid(String value) {
    return AttributePojo.of0(AttributeNamePojo.MARKER_MID, value);
  }

  /// Encodes the `marker-start` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute markerStart(String value) {
    return AttributePojo.of0(AttributeNamePojo.MARKER_START, value);
  }

  /// Encodes the `mask` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute mask(String value) {
    return AttributePojo.of0(AttributeNamePojo.MASK, value);
  }

  /// Encodes the `mask-type` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute maskType(String value) {
    return AttributePojo.of0(AttributeNamePojo.MASK_TYPE, value);
  }

  /// Encodes the `maxlength` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute maxlength(String value) {
    return AttributePojo.of0(AttributeNamePojo.MAXLENGTH, value);
  }

  /// Encodes the `media` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute media(String value) {
    return AttributePojo.of0(AttributeNamePojo.MEDIA, value);
  }

  /// Encodes the `method` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute method(String value) {
    return AttributePojo.of0(AttributeNamePojo.METHOD, value);
  }

  /// Encodes the `minlength` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute minlength(String value) {
    return AttributePojo.of0(AttributeNamePojo.MINLENGTH, value);
  }

  /// Encodes the `name` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute name(String value) {
    return AttributePojo.of0(AttributeNamePojo.NAME, value);
  }

  /// Encodes the `onclick` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute onclick(String value) {
    return AttributePojo.of0(AttributeNamePojo.ONCLICK, value);
  }

  /// Encodes the `onclick` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an object representing the encoded attribute
  public final Attribute onclick(JsAction value) {
    return AttributeJsAction.of0(AttributeNamePojo.ONCLICK, value);
  }

  /// Encodes the `onload` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute onload(String value) {
    return AttributePojo.of0(AttributeNamePojo.ONLOAD, value);
  }

  /// Encodes the `onload` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an object representing the encoded attribute
  public final Attribute onload(JsAction value) {
    return AttributeJsAction.of0(AttributeNamePojo.ONLOAD, value);
  }

  /// Encodes the `onpopstate` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute onpopstate(String value) {
    return AttributePojo.of0(AttributeNamePojo.ONPOPSTATE, value);
  }

  /// Encodes the `onpopstate` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an object representing the encoded attribute
  public final Attribute onpopstate(JsAction value) {
    return AttributeJsAction.of0(AttributeNamePojo.ONPOPSTATE, value);
  }

  /// Encodes the `onsubmit` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute onsubmit(String value) {
    return AttributePojo.of0(AttributeNamePojo.ONSUBMIT, value);
  }

  /// Encodes the `onsubmit` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an object representing the encoded attribute
  public final Attribute onsubmit(JsAction value) {
    return AttributeJsAction.of0(AttributeNamePojo.ONSUBMIT, value);
  }

  /// Encodes the `opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute opacity(String value) {
    return AttributePojo.of0(AttributeNamePojo.OPACITY, value);
  }

  /// Encodes the `overflow` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute overflow(String value) {
    return AttributePojo.of0(AttributeNamePojo.OVERFLOW, value);
  }

  /// Encodes the `paint-order` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute paintOrder(String value) {
    return AttributePojo.of0(AttributeNamePojo.PAINT_ORDER, value);
  }

  /// Encodes the `placeholder` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute placeholder(String value) {
    return AttributePojo.of0(AttributeNamePojo.PLACEHOLDER, value);
  }

  /// Encodes the `pointer-events` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute pointerEvents(String value) {
    return AttributePojo.of0(AttributeNamePojo.POINTER_EVENTS, value);
  }

  /// Encodes the `property` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute property(String value) {
    return AttributePojo.of0(AttributeNamePojo.PROPERTY, value);
  }

  /// Encodes the `referrerpolicy` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute referrerpolicy(String value) {
    return AttributePojo.of0(AttributeNamePojo.REFERRERPOLICY, value);
  }

  /// Encodes the `rel` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute rel(String value) {
    return AttributePojo.of0(AttributeNamePojo.REL, value);
  }

  /// Encodes the `rev` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute rev(String value) {
    return AttributePojo.of0(AttributeNamePojo.REV, value);
  }

  /// Encodes the `role` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute role(String value) {
    return AttributePojo.of0(AttributeNamePojo.ROLE, value);
  }

  /// Encodes the `rows` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute rows(String value) {
    return AttributePojo.of0(AttributeNamePojo.ROWS, value);
  }

  /// Encodes the `shape-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute shapeRendering(String value) {
    return AttributePojo.of0(AttributeNamePojo.SHAPE_RENDERING, value);
  }

  /// Encodes the `size` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute size(String value) {
    return AttributePojo.of0(AttributeNamePojo.SIZE, value);
  }

  /// Encodes the `sizes` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute sizes(String value) {
    return AttributePojo.of0(AttributeNamePojo.SIZES, value);
  }

  /// Encodes the `spellcheck` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute spellcheck(String value) {
    return AttributePojo.of0(AttributeNamePojo.SPELLCHECK, value);
  }

  /// Encodes the `src` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute src(String value) {
    return AttributePojo.of0(AttributeNamePojo.SRC, value);
  }

  /// Encodes the `srcset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute srcset(String value) {
    return AttributePojo.of0(AttributeNamePojo.SRCSET, value);
  }

  /// Encodes the `start` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute start(String value) {
    return AttributePojo.of0(AttributeNamePojo.START, value);
  }

  /// Encodes the `stop-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute stopColor(String value) {
    return AttributePojo.of0(AttributeNamePojo.STOP_COLOR, value);
  }

  /// Encodes the `stop-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute stopOpacity(String value) {
    return AttributePojo.of0(AttributeNamePojo.STOP_OPACITY, value);
  }

  /// Encodes the `stroke` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute stroke(String value) {
    return AttributePojo.of0(AttributeNamePojo.STROKE, value);
  }

  /// Encodes the `stroke-dasharray` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute strokeDasharray(String value) {
    return AttributePojo.of0(AttributeNamePojo.STROKE_DASHARRAY, value);
  }

  /// Encodes the `stroke-dashoffset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute strokeDashoffset(String value) {
    return AttributePojo.of0(AttributeNamePojo.STROKE_DASHOFFSET, value);
  }

  /// Encodes the `stroke-linecap` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute strokeLinecap(String value) {
    return AttributePojo.of0(AttributeNamePojo.STROKE_LINECAP, value);
  }

  /// Encodes the `stroke-linejoin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute strokeLinejoin(String value) {
    return AttributePojo.of0(AttributeNamePojo.STROKE_LINEJOIN, value);
  }

  /// Encodes the `stroke-miterlimit` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute strokeMiterlimit(String value) {
    return AttributePojo.of0(AttributeNamePojo.STROKE_MITERLIMIT, value);
  }

  /// Encodes the `stroke-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute strokeOpacity(String value) {
    return AttributePojo.of0(AttributeNamePojo.STROKE_OPACITY, value);
  }

  /// Encodes the `stroke-width` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute strokeWidth(String value) {
    return AttributePojo.of0(AttributeNamePojo.STROKE_WIDTH, value);
  }

  /// Encodes the `tabindex` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute tabindex(String value) {
    return AttributePojo.of0(AttributeNamePojo.TABINDEX, value);
  }

  /// Encodes the `target` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute target(String value) {
    return AttributePojo.of0(AttributeNamePojo.TARGET, value);
  }

  /// Encodes the `text-anchor` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute textAnchor(String value) {
    return AttributePojo.of0(AttributeNamePojo.TEXT_ANCHOR, value);
  }

  /// Encodes the `text-decoration` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute textDecoration(String value) {
    return AttributePojo.of0(AttributeNamePojo.TEXT_DECORATION, value);
  }

  /// Encodes the `text-overflow` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute textOverflow(String value) {
    return AttributePojo.of0(AttributeNamePojo.TEXT_OVERFLOW, value);
  }

  /// Encodes the `text-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute textRendering(String value) {
    return AttributePojo.of0(AttributeNamePojo.TEXT_RENDERING, value);
  }

  /// Encodes the `transform` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute transform(String value) {
    return AttributePojo.of0(AttributeNamePojo.TRANSFORM, value);
  }

  /// Encodes the `transform-origin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute transformOrigin(String value) {
    return AttributePojo.of0(AttributeNamePojo.TRANSFORM_ORIGIN, value);
  }

  /// Encodes the `translate` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute translate(String value) {
    return AttributePojo.of0(AttributeNamePojo.TRANSLATE, value);
  }

  /// Encodes the `type` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute type(String value) {
    return AttributePojo.of0(AttributeNamePojo.TYPE, value);
  }

  /// Encodes the `unicode-bidi` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute unicodeBidi(String value) {
    return AttributePojo.of0(AttributeNamePojo.UNICODE_BIDI, value);
  }

  /// Encodes the `value` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute value(String value) {
    return AttributePojo.of0(AttributeNamePojo.VALUE, value);
  }

  /// Encodes the `vector-effect` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute vectorEffect(String value) {
    return AttributePojo.of0(AttributeNamePojo.VECTOR_EFFECT, value);
  }

  /// Encodes the `viewBox` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute viewBox(String value) {
    return AttributePojo.of0(AttributeNamePojo.VIEWBOX, value);
  }

  /// Encodes the `visibility` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute visibility(String value) {
    return AttributePojo.of0(AttributeNamePojo.VISIBILITY, value);
  }

  /// Encodes the `white-space` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute whiteSpace(String value) {
    return AttributePojo.of0(AttributeNamePojo.WHITE_SPACE, value);
  }

  /// Encodes the `width` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute width(String value) {
    return AttributePojo.of0(AttributeNamePojo.WIDTH, value);
  }

  /// Encodes the `word-spacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute wordSpacing(String value) {
    return AttributePojo.of0(AttributeNamePojo.WORD_SPACING, value);
  }

  /// Encodes the `wrap` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute wrap(String value) {
    return AttributePojo.of0(AttributeNamePojo.WRAP, value);
  }

  /// Encodes the `writing-mode` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute writingMode(String value) {
    return AttributePojo.of0(AttributeNamePojo.WRITING_MODE, value);
  }

  /// Encodes the `xmlns` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an object representing the encoded attribute
  public final Attribute xmlns(String value) {
    return AttributePojo.of0(AttributeNamePojo.XMLNS, value);
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
  public final AmbiguousMarkup clipPath(String value) {
    return AmbiguousPojo.of0(AmbiguousName.CLIPPATH, value);
  }

  /// Encodes the `form` attribute or the `form` element with the specified
  /// text.
  ///
  /// @param value the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  public final AmbiguousMarkup form(String value) {
    return AmbiguousPojo.of0(AmbiguousName.FORM, value);
  }

  /// Encodes the `label` attribute or the `label` element with the specified
  /// text.
  ///
  /// @param value the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  public final AmbiguousMarkup label(String value) {
    return AmbiguousPojo.of0(AmbiguousName.LABEL, value);
  }

  /// Encodes the `style` attribute or the `style` element with the specified
  /// text.
  ///
  /// @param value the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  public final AmbiguousMarkup style(String value) {
    return AmbiguousPojo.of0(AmbiguousName.STYLE, value);
  }

  /// Encodes the `title` attribute or the `title` element with the specified
  /// text.
  ///
  /// @param value the attribute value or the text content of the element
  ///
  /// @return an object representing the encoded attribute or element
  public final AmbiguousMarkup title(String value) {
    return AmbiguousPojo.of0(AmbiguousName.TITLE, value);
  }

  //
  // TEXT
  //

  /// Renders the non-breaking space `&nbsp;` HTML character entity.
  ///
  /// @return an instruction representing the non-breaking space
  ///         character entity.
  public final RawMarkup nbsp() {
    return new RawNode("&nbsp;");
  }

  /// Renders the specified value as raw HTML.
  ///
  /// @param value the raw HTML value
  ///
  /// @return a raw HTML instruction
  public final RawMarkup raw(String value) {
    return RawNode.of(value);
  }

  /// Renders a text node with the specified value. The text value is escaped
  /// before being emitted to the output.
  ///
  /// @param value the text value
  ///
  /// @return an instruction representing the text node
  public final TextMarkup text(String value) {
    return TextNode.of(value);
  }

  // END generated code

}
