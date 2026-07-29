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

import java.util.Collection;
import java.util.Objects;
import objectos.html.AttributeName;
import objectos.html.AttributeObject;
import objectos.html.ElementName;
import objectos.html.rec.AmbiguousInstruction;
import objectos.html.rec.AttributeInstruction;
import objectos.html.rec.ElementInstruction;
import objectos.html.rec.FlattenInstruction;
import objectos.html.rec.Fragment0;
import objectos.html.rec.Fragment1;
import objectos.html.rec.Fragment2;
import objectos.html.rec.Fragment3;
import objectos.html.rec.Fragment4;
import objectos.html.rec.FragmentInstruction;
import objectos.html.rec.Instruction;
import objectos.html.rec.NoOpInstruction;
import objectos.html.rec.RawInstruction;
import objectos.html.rec.TextInstruction;
import objectos.script.JsAction;
import objectox.html.attr.AttributeNamePojo;
import objectox.html.elem.ElementNamePojo;

sealed abstract class MarkupGenerated permits MarkupPojo {

  /// Renders the specified attribute at the root of a document or fragment.
  ///
  /// @param object the attribute
  ///
  /// @return an instruction representing the attribute
  public abstract AttributeInstruction attr(AttributeObject object);

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
public abstract AttributeInstruction css(String value);

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
public abstract FragmentInstruction f(Fragment0 fragment);

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
public abstract <T1> FragmentInstruction f(Fragment1<T1> fragment, T1 arg1);

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
public abstract <T1, T2> FragmentInstruction f(Fragment2<T1, T2> fragment, T1 arg1, T2 arg2);

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
public abstract <T1, T2, T3> FragmentInstruction f(Fragment3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3);

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
public abstract <T1, T2, T3, T4> FragmentInstruction f(Fragment4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4);

/// Flattens the specified instructions so that each of the specified
/// instructions is individually added, in order, to a receiving element.
/// @param contents the instructions to be flattened
/// @return an instruction representing this flatten operation
public abstract FlattenInstruction flatten(Instruction... contents);

/// Flattens the specified instructions so that each of the specified
/// instructions is individually added, in order, to a receiving element.
/// @param contents the instructions to be flattened
/// @return an instruction representing this flatten operation
public abstract FlattenInstruction flatten(Collection<? extends Instruction> contents);

/// The no-op instruction.
/// @return the no-op instruction.
public abstract NoOpInstruction noop();

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
  public abstract NoOpInstruction testableNewLine();

  //
  // ELEMENTS
  //

  abstract ElementInstruction elem0(ElementName name, Instruction... contents);

  abstract ElementInstruction elem0(ElementName name, String text);

  /// Renders an HTML element with the specified name and contents.
  ///
  /// @param name the element name
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element
  public final ElementInstruction elem(ElementName name, Instruction... contents) {
    Objects.requireNonNull(name, "name == null");
    return elem0(name, contents);
  }

  /// Renders an HTML element with the specified name and text.
  ///
  /// @param name the element name
  /// @param text the text value of this element
  ///
  /// @return an instruction representing the element
  public final ElementInstruction elem(ElementName name, String text) {
    Objects.requireNonNull(name, "name == null");
    return elem0(name, text);
  }

  /// Renders the `<!DOCTYPE html>` doctype.
  public abstract void doctype();

  /// Renders the `a` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction a(Instruction... contents) {
    return elem0(ElementNamePojo.A, contents);
  }

  /// Renders the `a` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction a(String text) {
    return elem0(ElementNamePojo.A, text);
  }

  /// Renders the `abbr` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction abbr(Instruction... contents) {
    return elem0(ElementNamePojo.ABBR, contents);
  }

  /// Renders the `abbr` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction abbr(String text) {
    return elem0(ElementNamePojo.ABBR, text);
  }

  /// Renders the `article` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction article(Instruction... contents) {
    return elem0(ElementNamePojo.ARTICLE, contents);
  }

  /// Renders the `article` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction article(String text) {
    return elem0(ElementNamePojo.ARTICLE, text);
  }

  /// Renders the `aside` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction aside(Instruction... contents) {
    return elem0(ElementNamePojo.ASIDE, contents);
  }

  /// Renders the `aside` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction aside(String text) {
    return elem0(ElementNamePojo.ASIDE, text);
  }

  /// Renders the `b` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction b(Instruction... contents) {
    return elem0(ElementNamePojo.B, contents);
  }

  /// Renders the `b` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction b(String text) {
    return elem0(ElementNamePojo.B, text);
  }

  /// Renders the `blockquote` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction blockquote(Instruction... contents) {
    return elem0(ElementNamePojo.BLOCKQUOTE, contents);
  }

  /// Renders the `blockquote` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction blockquote(String text) {
    return elem0(ElementNamePojo.BLOCKQUOTE, text);
  }

  /// Renders the `body` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction body(Instruction... contents) {
    return elem0(ElementNamePojo.BODY, contents);
  }

  /// Renders the `body` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction body(String text) {
    return elem0(ElementNamePojo.BODY, text);
  }

  /// Renders the `br` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction br(Instruction.OfVoid... contents) {
    return elem0(ElementNamePojo.BR, contents);
  }

  /// Renders the `button` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction button(Instruction... contents) {
    return elem0(ElementNamePojo.BUTTON, contents);
  }

  /// Renders the `button` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction button(String text) {
    return elem0(ElementNamePojo.BUTTON, text);
  }

  /// Renders the `clipPath` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction clipPath(Instruction... contents) {
    return elem0(ElementNamePojo.CLIPPATH, contents);
  }

  /// Renders the `code` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction code(Instruction... contents) {
    return elem0(ElementNamePojo.CODE, contents);
  }

  /// Renders the `code` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction code(String text) {
    return elem0(ElementNamePojo.CODE, text);
  }

  /// Renders the `dd` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction dd(Instruction... contents) {
    return elem0(ElementNamePojo.DD, contents);
  }

  /// Renders the `dd` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction dd(String text) {
    return elem0(ElementNamePojo.DD, text);
  }

  /// Renders the `defs` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction defs(Instruction... contents) {
    return elem0(ElementNamePojo.DEFS, contents);
  }

  /// Renders the `defs` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction defs(String text) {
    return elem0(ElementNamePojo.DEFS, text);
  }

  /// Renders the `details` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction details(Instruction... contents) {
    return elem0(ElementNamePojo.DETAILS, contents);
  }

  /// Renders the `details` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction details(String text) {
    return elem0(ElementNamePojo.DETAILS, text);
  }

  /// Renders the `dialog` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction dialog(Instruction... contents) {
    return elem0(ElementNamePojo.DIALOG, contents);
  }

  /// Renders the `dialog` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction dialog(String text) {
    return elem0(ElementNamePojo.DIALOG, text);
  }

  /// Renders the `div` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction div(Instruction... contents) {
    return elem0(ElementNamePojo.DIV, contents);
  }

  /// Renders the `div` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction div(String text) {
    return elem0(ElementNamePojo.DIV, text);
  }

  /// Renders the `dl` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction dl(Instruction... contents) {
    return elem0(ElementNamePojo.DL, contents);
  }

  /// Renders the `dl` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction dl(String text) {
    return elem0(ElementNamePojo.DL, text);
  }

  /// Renders the `dt` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction dt(Instruction... contents) {
    return elem0(ElementNamePojo.DT, contents);
  }

  /// Renders the `dt` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction dt(String text) {
    return elem0(ElementNamePojo.DT, text);
  }

  /// Renders the `em` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction em(Instruction... contents) {
    return elem0(ElementNamePojo.EM, contents);
  }

  /// Renders the `em` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction em(String text) {
    return elem0(ElementNamePojo.EM, text);
  }

  /// Renders the `fieldset` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction fieldset(Instruction... contents) {
    return elem0(ElementNamePojo.FIELDSET, contents);
  }

  /// Renders the `fieldset` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction fieldset(String text) {
    return elem0(ElementNamePojo.FIELDSET, text);
  }

  /// Renders the `figure` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction figure(Instruction... contents) {
    return elem0(ElementNamePojo.FIGURE, contents);
  }

  /// Renders the `figure` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction figure(String text) {
    return elem0(ElementNamePojo.FIGURE, text);
  }

  /// Renders the `footer` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction footer(Instruction... contents) {
    return elem0(ElementNamePojo.FOOTER, contents);
  }

  /// Renders the `footer` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction footer(String text) {
    return elem0(ElementNamePojo.FOOTER, text);
  }

  /// Renders the `form` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction form(Instruction... contents) {
    return elem0(ElementNamePojo.FORM, contents);
  }

  /// Renders the `g` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction g(Instruction... contents) {
    return elem0(ElementNamePojo.G, contents);
  }

  /// Renders the `g` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction g(String text) {
    return elem0(ElementNamePojo.G, text);
  }

  /// Renders the `h1` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h1(Instruction... contents) {
    return elem0(ElementNamePojo.H1, contents);
  }

  /// Renders the `h1` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h1(String text) {
    return elem0(ElementNamePojo.H1, text);
  }

  /// Renders the `h2` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h2(Instruction... contents) {
    return elem0(ElementNamePojo.H2, contents);
  }

  /// Renders the `h2` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h2(String text) {
    return elem0(ElementNamePojo.H2, text);
  }

  /// Renders the `h3` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h3(Instruction... contents) {
    return elem0(ElementNamePojo.H3, contents);
  }

  /// Renders the `h3` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h3(String text) {
    return elem0(ElementNamePojo.H3, text);
  }

  /// Renders the `h4` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h4(Instruction... contents) {
    return elem0(ElementNamePojo.H4, contents);
  }

  /// Renders the `h4` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h4(String text) {
    return elem0(ElementNamePojo.H4, text);
  }

  /// Renders the `h5` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h5(Instruction... contents) {
    return elem0(ElementNamePojo.H5, contents);
  }

  /// Renders the `h5` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h5(String text) {
    return elem0(ElementNamePojo.H5, text);
  }

  /// Renders the `h6` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h6(Instruction... contents) {
    return elem0(ElementNamePojo.H6, contents);
  }

  /// Renders the `h6` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction h6(String text) {
    return elem0(ElementNamePojo.H6, text);
  }

  /// Renders the `head` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction head(Instruction... contents) {
    return elem0(ElementNamePojo.HEAD, contents);
  }

  /// Renders the `head` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction head(String text) {
    return elem0(ElementNamePojo.HEAD, text);
  }

  /// Renders the `header` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction header(Instruction... contents) {
    return elem0(ElementNamePojo.HEADER, contents);
  }

  /// Renders the `header` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction header(String text) {
    return elem0(ElementNamePojo.HEADER, text);
  }

  /// Renders the `hgroup` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction hgroup(Instruction... contents) {
    return elem0(ElementNamePojo.HGROUP, contents);
  }

  /// Renders the `hgroup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction hgroup(String text) {
    return elem0(ElementNamePojo.HGROUP, text);
  }

  /// Renders the `hr` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction hr(Instruction.OfVoid... contents) {
    return elem0(ElementNamePojo.HR, contents);
  }

  /// Renders the `html` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction html(Instruction... contents) {
    return elem0(ElementNamePojo.HTML, contents);
  }

  /// Renders the `html` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction html(String text) {
    return elem0(ElementNamePojo.HTML, text);
  }

  /// Renders the `img` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction img(Instruction.OfVoid... contents) {
    return elem0(ElementNamePojo.IMG, contents);
  }

  /// Renders the `input` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction input(Instruction.OfVoid... contents) {
    return elem0(ElementNamePojo.INPUT, contents);
  }

  /// Renders the `kbd` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction kbd(Instruction... contents) {
    return elem0(ElementNamePojo.KBD, contents);
  }

  /// Renders the `kbd` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction kbd(String text) {
    return elem0(ElementNamePojo.KBD, text);
  }

  /// Renders the `label` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction label(Instruction... contents) {
    return elem0(ElementNamePojo.LABEL, contents);
  }

  /// Renders the `legend` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction legend(Instruction... contents) {
    return elem0(ElementNamePojo.LEGEND, contents);
  }

  /// Renders the `legend` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction legend(String text) {
    return elem0(ElementNamePojo.LEGEND, text);
  }

  /// Renders the `li` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction li(Instruction... contents) {
    return elem0(ElementNamePojo.LI, contents);
  }

  /// Renders the `li` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction li(String text) {
    return elem0(ElementNamePojo.LI, text);
  }

  /// Renders the `link` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction link(Instruction.OfVoid... contents) {
    return elem0(ElementNamePojo.LINK, contents);
  }

  /// Renders the `main` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction main(Instruction... contents) {
    return elem0(ElementNamePojo.MAIN, contents);
  }

  /// Renders the `main` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction main(String text) {
    return elem0(ElementNamePojo.MAIN, text);
  }

  /// Renders the `menu` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction menu(Instruction... contents) {
    return elem0(ElementNamePojo.MENU, contents);
  }

  /// Renders the `menu` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction menu(String text) {
    return elem0(ElementNamePojo.MENU, text);
  }

  /// Renders the `meta` element with the specified content.
  ///
  /// @param contents the attributes of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction meta(Instruction.OfVoid... contents) {
    return elem0(ElementNamePojo.META, contents);
  }

  /// Renders the `nav` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction nav(Instruction... contents) {
    return elem0(ElementNamePojo.NAV, contents);
  }

  /// Renders the `nav` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction nav(String text) {
    return elem0(ElementNamePojo.NAV, text);
  }

  /// Renders the `noscript` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction noscript(Instruction... contents) {
    return elem0(ElementNamePojo.NOSCRIPT, contents);
  }

  /// Renders the `noscript` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction noscript(String text) {
    return elem0(ElementNamePojo.NOSCRIPT, text);
  }

  /// Renders the `ol` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction ol(Instruction... contents) {
    return elem0(ElementNamePojo.OL, contents);
  }

  /// Renders the `ol` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction ol(String text) {
    return elem0(ElementNamePojo.OL, text);
  }

  /// Renders the `optgroup` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction optgroup(Instruction... contents) {
    return elem0(ElementNamePojo.OPTGROUP, contents);
  }

  /// Renders the `optgroup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction optgroup(String text) {
    return elem0(ElementNamePojo.OPTGROUP, text);
  }

  /// Renders the `option` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction option(Instruction... contents) {
    return elem0(ElementNamePojo.OPTION, contents);
  }

  /// Renders the `option` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction option(String text) {
    return elem0(ElementNamePojo.OPTION, text);
  }

  /// Renders the `p` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction p(Instruction... contents) {
    return elem0(ElementNamePojo.P, contents);
  }

  /// Renders the `p` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction p(String text) {
    return elem0(ElementNamePojo.P, text);
  }

  /// Renders the `path` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction path(Instruction... contents) {
    return elem0(ElementNamePojo.PATH, contents);
  }

  /// Renders the `path` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction path(String text) {
    return elem0(ElementNamePojo.PATH, text);
  }

  /// Renders the `pre` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction pre(Instruction... contents) {
    return elem0(ElementNamePojo.PRE, contents);
  }

  /// Renders the `pre` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction pre(String text) {
    return elem0(ElementNamePojo.PRE, text);
  }

  /// Renders the `progress` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction progress(Instruction... contents) {
    return elem0(ElementNamePojo.PROGRESS, contents);
  }

  /// Renders the `progress` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction progress(String text) {
    return elem0(ElementNamePojo.PROGRESS, text);
  }

  /// Renders the `samp` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction samp(Instruction... contents) {
    return elem0(ElementNamePojo.SAMP, contents);
  }

  /// Renders the `samp` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction samp(String text) {
    return elem0(ElementNamePojo.SAMP, text);
  }

  /// Renders the `script` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction script(Instruction... contents) {
    return elem0(ElementNamePojo.SCRIPT, contents);
  }

  /// Renders the `script` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction script(String text) {
    return elem0(ElementNamePojo.SCRIPT, text);
  }

  /// Renders the `section` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction section(Instruction... contents) {
    return elem0(ElementNamePojo.SECTION, contents);
  }

  /// Renders the `section` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction section(String text) {
    return elem0(ElementNamePojo.SECTION, text);
  }

  /// Renders the `select` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction select(Instruction... contents) {
    return elem0(ElementNamePojo.SELECT, contents);
  }

  /// Renders the `select` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction select(String text) {
    return elem0(ElementNamePojo.SELECT, text);
  }

  /// Renders the `small` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction small(Instruction... contents) {
    return elem0(ElementNamePojo.SMALL, contents);
  }

  /// Renders the `small` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction small(String text) {
    return elem0(ElementNamePojo.SMALL, text);
  }

  /// Renders the `span` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction span(Instruction... contents) {
    return elem0(ElementNamePojo.SPAN, contents);
  }

  /// Renders the `span` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction span(String text) {
    return elem0(ElementNamePojo.SPAN, text);
  }

  /// Renders the `strong` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction strong(Instruction... contents) {
    return elem0(ElementNamePojo.STRONG, contents);
  }

  /// Renders the `strong` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction strong(String text) {
    return elem0(ElementNamePojo.STRONG, text);
  }

  /// Renders the `style` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction style(Instruction... contents) {
    return elem0(ElementNamePojo.STYLE, contents);
  }

  /// Renders the `sub` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction sub(Instruction... contents) {
    return elem0(ElementNamePojo.SUB, contents);
  }

  /// Renders the `sub` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction sub(String text) {
    return elem0(ElementNamePojo.SUB, text);
  }

  /// Renders the `summary` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction summary(Instruction... contents) {
    return elem0(ElementNamePojo.SUMMARY, contents);
  }

  /// Renders the `summary` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction summary(String text) {
    return elem0(ElementNamePojo.SUMMARY, text);
  }

  /// Renders the `sup` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction sup(Instruction... contents) {
    return elem0(ElementNamePojo.SUP, contents);
  }

  /// Renders the `sup` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction sup(String text) {
    return elem0(ElementNamePojo.SUP, text);
  }

  /// Renders the `svg` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction svg(Instruction... contents) {
    return elem0(ElementNamePojo.SVG, contents);
  }

  /// Renders the `svg` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction svg(String text) {
    return elem0(ElementNamePojo.SVG, text);
  }

  /// Renders the `table` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction table(Instruction... contents) {
    return elem0(ElementNamePojo.TABLE, contents);
  }

  /// Renders the `table` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction table(String text) {
    return elem0(ElementNamePojo.TABLE, text);
  }

  /// Renders the `tbody` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction tbody(Instruction... contents) {
    return elem0(ElementNamePojo.TBODY, contents);
  }

  /// Renders the `tbody` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction tbody(String text) {
    return elem0(ElementNamePojo.TBODY, text);
  }

  /// Renders the `td` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction td(Instruction... contents) {
    return elem0(ElementNamePojo.TD, contents);
  }

  /// Renders the `td` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction td(String text) {
    return elem0(ElementNamePojo.TD, text);
  }

  /// Renders the `template` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction template(Instruction... contents) {
    return elem0(ElementNamePojo.TEMPLATE, contents);
  }

  /// Renders the `template` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction template(String text) {
    return elem0(ElementNamePojo.TEMPLATE, text);
  }

  /// Renders the `textarea` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction textarea(Instruction... contents) {
    return elem0(ElementNamePojo.TEXTAREA, contents);
  }

  /// Renders the `textarea` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction textarea(String text) {
    return elem0(ElementNamePojo.TEXTAREA, text);
  }

  /// Renders the `th` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction th(Instruction... contents) {
    return elem0(ElementNamePojo.TH, contents);
  }

  /// Renders the `th` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction th(String text) {
    return elem0(ElementNamePojo.TH, text);
  }

  /// Renders the `thead` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction thead(Instruction... contents) {
    return elem0(ElementNamePojo.THEAD, contents);
  }

  /// Renders the `thead` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction thead(String text) {
    return elem0(ElementNamePojo.THEAD, text);
  }

  /// Renders the `title` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction title(Instruction... contents) {
    return elem0(ElementNamePojo.TITLE, contents);
  }

  /// Renders the `tr` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction tr(Instruction... contents) {
    return elem0(ElementNamePojo.TR, contents);
  }

  /// Renders the `tr` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction tr(String text) {
    return elem0(ElementNamePojo.TR, text);
  }

  /// Renders the `ul` element with the specified content.
  ///
  /// @param contents the attributes and children of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction ul(Instruction... contents) {
    return elem0(ElementNamePojo.UL, contents);
  }

  /// Renders the `ul` element with the specified text.
  ///
  /// @param text the text value of the element
  ///
  /// @return an instruction representing the element.
  public final ElementInstruction ul(String text) {
    return elem0(ElementNamePojo.UL, text);
  }

  //
  // ATTRIBUTES
  //

  abstract AttributeInstruction attr0(AttributeName name);

  abstract AttributeInstruction attr0(AttributeName name, Object value);

  /// Renders an attribute with the specified name.
  ///
  /// @param name the attribute name
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction attr(AttributeName name) {
    Objects.requireNonNull(name, "name == null");
    return attr0(name);
  }

  /// Renders an attribute with the specified name and value.
  ///
  /// @param name the attribute name
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction attr(AttributeName name, String value) {
    Objects.requireNonNull(name, "name == null");
    return attr0(name, value);
  }

  /// Renders the `accesskey` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction accesskey(String value) {
    return attr0(AttributeNamePojo.ACCESSKEY, value);
  }

  /// Renders the `action` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction action(String value) {
    return attr0(AttributeNamePojo.ACTION, value);
  }

  /// Renders the `align` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction align(String value) {
    return attr0(AttributeNamePojo.ALIGN, value);
  }

  /// Renders the `alignment-baseline` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction alignmentBaseline(String value) {
    return attr0(AttributeNamePojo.ALIGNMENT_BASELINE, value);
  }

  /// Renders the `alt` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction alt(String value) {
    return attr0(AttributeNamePojo.ALT, value);
  }

  /// Renders the `aria-current` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaCurrent(String value) {
    return attr0(AttributeNamePojo.ARIA_CURRENT, value);
  }

  /// Renders the `aria-disabled` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaDisabled(String value) {
    return attr0(AttributeNamePojo.ARIA_DISABLED, value);
  }

  /// Renders the `aria-hidden` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaHidden(String value) {
    return attr0(AttributeNamePojo.ARIA_HIDDEN, value);
  }

  /// Renders the `aria-invalid` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaInvalid(String value) {
    return attr0(AttributeNamePojo.ARIA_INVALID, value);
  }

  /// Renders the `aria-label` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaLabel(String value) {
    return attr0(AttributeNamePojo.ARIA_LABEL, value);
  }

  /// Renders the `aria-labelledby` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaLabelledBy(String value) {
    return attr0(AttributeNamePojo.ARIA_LABELLED_BY, value);
  }

  /// Renders the `aria-modal` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaModal(String value) {
    return attr0(AttributeNamePojo.ARIA_MODAL, value);
  }

  /// Renders the `aria-placeholder` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaPlaceholder(String value) {
    return attr0(AttributeNamePojo.ARIA_PLACEHOLDER, value);
  }

  /// Renders the `aria-readonly` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaReadonly(String value) {
    return attr0(AttributeNamePojo.ARIA_READONLY, value);
  }

  /// Renders the `aria-required` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaRequired(String value) {
    return attr0(AttributeNamePojo.ARIA_REQUIRED, value);
  }

  /// Renders the `aria-selected` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction ariaSelected(String value) {
    return attr0(AttributeNamePojo.ARIA_SELECTED, value);
  }

  /// Renders the `as` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction as(String value) {
    return attr0(AttributeNamePojo.AS, value);
  }

  /// Renders the `autocomplete` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction autocomplete(String value) {
    return attr0(AttributeNamePojo.AUTOCOMPLETE, value);
  }

  /// Renders the `baseline-shift` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction baselineShift(String value) {
    return attr0(AttributeNamePojo.BASELINE_SHIFT, value);
  }

  /// Renders the `border` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction border(String value) {
    return attr0(AttributeNamePojo.BORDER, value);
  }

  /// Renders the `cellpadding` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction cellpadding(String value) {
    return attr0(AttributeNamePojo.CELLPADDING, value);
  }

  /// Renders the `cellspacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction cellspacing(String value) {
    return attr0(AttributeNamePojo.CELLSPACING, value);
  }

  /// Renders the `charset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction charset(String value) {
    return attr0(AttributeNamePojo.CHARSET, value);
  }

  /// Renders the `cite` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction cite(String value) {
    return attr0(AttributeNamePojo.CITE, value);
  }

  /// Renders the `class` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction className(String value) {
    return attr0(AttributeNamePojo.CLASS, value);
  }

  /// Renders the `clip-rule` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction clipRule(String value) {
    return attr0(AttributeNamePojo.CLIP_RULE, value);
  }

  /// Renders the `closedby` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction closedby(String value) {
    return attr0(AttributeNamePojo.CLOSEDBY, value);
  }

  /// Renders the `color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction color(String value) {
    return attr0(AttributeNamePojo.COLOR, value);
  }

  /// Renders the `color-interpolation` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction colorInterpolation(String value) {
    return attr0(AttributeNamePojo.COLOR_INTERPOLATION, value);
  }

  /// Renders the `color-interpolation-filters` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction colorInterpolationFilters(String value) {
    return attr0(AttributeNamePojo.COLOR_INTERPOLATION_FILTERS, value);
  }

  /// Renders the `cols` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction cols(String value) {
    return attr0(AttributeNamePojo.COLS, value);
  }

  /// Renders the `content` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction content(String value) {
    return attr0(AttributeNamePojo.CONTENT, value);
  }

  /// Renders the `contenteditable` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction contenteditable(String value) {
    return attr0(AttributeNamePojo.CONTENTEDITABLE, value);
  }

  /// Renders the `crossorigin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction crossorigin(String value) {
    return attr0(AttributeNamePojo.CROSSORIGIN, value);
  }

  /// Renders the `cursor` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction cursor(String value) {
    return attr0(AttributeNamePojo.CURSOR, value);
  }

  /// Renders the `d` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction d(String value) {
    return attr0(AttributeNamePojo.D, value);
  }

  /// Renders the `dir` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction dir(String value) {
    return attr0(AttributeNamePojo.DIR, value);
  }

  /// Renders the `direction` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction direction(String value) {
    return attr0(AttributeNamePojo.DIRECTION, value);
  }

  /// Renders the `dirname` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction dirname(String value) {
    return attr0(AttributeNamePojo.DIRNAME, value);
  }

  /// Renders the `display` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction display(String value) {
    return attr0(AttributeNamePojo.DISPLAY, value);
  }

  /// Renders the `dominant-baseline` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction dominantBaseline(String value) {
    return attr0(AttributeNamePojo.DOMINANT_BASELINE, value);
  }

  /// Renders the `download` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction download(String value) {
    return attr0(AttributeNamePojo.DOWNLOAD, value);
  }

  /// Renders the `draggable` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction draggable(String value) {
    return attr0(AttributeNamePojo.DRAGGABLE, value);
  }

  /// Renders the `enctype` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction enctype(String value) {
    return attr0(AttributeNamePojo.ENCTYPE, value);
  }

  /// Renders the `fill` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction fill(String value) {
    return attr0(AttributeNamePojo.FILL, value);
  }

  /// Renders the `fill-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction fillOpacity(String value) {
    return attr0(AttributeNamePojo.FILL_OPACITY, value);
  }

  /// Renders the `fill-rule` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction fillRule(String value) {
    return attr0(AttributeNamePojo.FILL_RULE, value);
  }

  /// Renders the `filter` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction filter(String value) {
    return attr0(AttributeNamePojo.FILTER, value);
  }

  /// Renders the `flood-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction floodColor(String value) {
    return attr0(AttributeNamePojo.FLOOD_COLOR, value);
  }

  /// Renders the `flood-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction floodOpacity(String value) {
    return attr0(AttributeNamePojo.FLOOD_OPACITY, value);
  }

  /// Renders the `for` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction forId(String value) {
    return attr0(AttributeNamePojo.FOR, value);
  }

  /// Renders the `glyph-orientation-horizontal` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction glyphOrientationHorizontal(String value) {
    return attr0(AttributeNamePojo.GLYPH_ORIENTATION_HORIZONTAL, value);
  }

  /// Renders the `glyph-orientation-vertical` attribute with the specified
  /// value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction glyphOrientationVertical(String value) {
    return attr0(AttributeNamePojo.GLYPH_ORIENTATION_VERTICAL, value);
  }

  /// Renders the `height` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction height(String value) {
    return attr0(AttributeNamePojo.HEIGHT, value);
  }

  /// Renders the `href` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction href(String value) {
    return attr0(AttributeNamePojo.HREF, value);
  }

  /// Renders the `http-equiv` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction httpEquiv(String value) {
    return attr0(AttributeNamePojo.HTTP_EQUIV, value);
  }

  /// Renders the `id` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction id(String value) {
    return attr0(AttributeNamePojo.ID, value);
  }

  /// Renders the `image-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction imageRendering(String value) {
    return attr0(AttributeNamePojo.IMAGE_RENDERING, value);
  }

  /// Renders the `integrity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction integrity(String value) {
    return attr0(AttributeNamePojo.INTEGRITY, value);
  }

  /// Renders the `lang` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction lang(String value) {
    return attr0(AttributeNamePojo.LANG, value);
  }

  /// Renders the `letter-spacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction letterSpacing(String value) {
    return attr0(AttributeNamePojo.LETTER_SPACING, value);
  }

  /// Renders the `lighting-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction lightingColor(String value) {
    return attr0(AttributeNamePojo.LIGHTING_COLOR, value);
  }

  /// Renders the `marker-end` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction markerEnd(String value) {
    return attr0(AttributeNamePojo.MARKER_END, value);
  }

  /// Renders the `marker-mid` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction markerMid(String value) {
    return attr0(AttributeNamePojo.MARKER_MID, value);
  }

  /// Renders the `marker-start` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction markerStart(String value) {
    return attr0(AttributeNamePojo.MARKER_START, value);
  }

  /// Renders the `mask` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction mask(String value) {
    return attr0(AttributeNamePojo.MASK, value);
  }

  /// Renders the `mask-type` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction maskType(String value) {
    return attr0(AttributeNamePojo.MASK_TYPE, value);
  }

  /// Renders the `maxlength` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction maxlength(String value) {
    return attr0(AttributeNamePojo.MAXLENGTH, value);
  }

  /// Renders the `media` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction media(String value) {
    return attr0(AttributeNamePojo.MEDIA, value);
  }

  /// Renders the `method` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction method(String value) {
    return attr0(AttributeNamePojo.METHOD, value);
  }

  /// Renders the `minlength` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction minlength(String value) {
    return attr0(AttributeNamePojo.MINLENGTH, value);
  }

  /// Renders the `name` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction name(String value) {
    return attr0(AttributeNamePojo.NAME, value);
  }

  /// Renders the `onclick` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction onclick(String value) {
    return attr0(AttributeNamePojo.ONCLICK, value);
  }

  /// Renders the `onclick` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction onclick(JsAction value) {
    return attr0(AttributeNamePojo.ONCLICK, value);
  }

  /// Renders the `onload` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction onload(String value) {
    return attr0(AttributeNamePojo.ONLOAD, value);
  }

  /// Renders the `onload` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction onload(JsAction value) {
    return attr0(AttributeNamePojo.ONLOAD, value);
  }

  /// Renders the `onpopstate` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction onpopstate(String value) {
    return attr0(AttributeNamePojo.ONPOPSTATE, value);
  }

  /// Renders the `onpopstate` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction onpopstate(JsAction value) {
    return attr0(AttributeNamePojo.ONPOPSTATE, value);
  }

  /// Renders the `onsubmit` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction onsubmit(String value) {
    return attr0(AttributeNamePojo.ONSUBMIT, value);
  }

  /// Renders the `onsubmit` attribute with the specified action.
  ///
  /// @param value the action to be executed
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction onsubmit(JsAction value) {
    return attr0(AttributeNamePojo.ONSUBMIT, value);
  }

  /// Renders the `opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction opacity(String value) {
    return attr0(AttributeNamePojo.OPACITY, value);
  }

  /// Renders the `overflow` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction overflow(String value) {
    return attr0(AttributeNamePojo.OVERFLOW, value);
  }

  /// Renders the `paint-order` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction paintOrder(String value) {
    return attr0(AttributeNamePojo.PAINT_ORDER, value);
  }

  /// Renders the `placeholder` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction placeholder(String value) {
    return attr0(AttributeNamePojo.PLACEHOLDER, value);
  }

  /// Renders the `pointer-events` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction pointerEvents(String value) {
    return attr0(AttributeNamePojo.POINTER_EVENTS, value);
  }

  /// Renders the `property` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction property(String value) {
    return attr0(AttributeNamePojo.PROPERTY, value);
  }

  /// Renders the `referrerpolicy` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction referrerpolicy(String value) {
    return attr0(AttributeNamePojo.REFERRERPOLICY, value);
  }

  /// Renders the `rel` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction rel(String value) {
    return attr0(AttributeNamePojo.REL, value);
  }

  /// Renders the `rev` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction rev(String value) {
    return attr0(AttributeNamePojo.REV, value);
  }

  /// Renders the `role` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction role(String value) {
    return attr0(AttributeNamePojo.ROLE, value);
  }

  /// Renders the `rows` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction rows(String value) {
    return attr0(AttributeNamePojo.ROWS, value);
  }

  /// Renders the `shape-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction shapeRendering(String value) {
    return attr0(AttributeNamePojo.SHAPE_RENDERING, value);
  }

  /// Renders the `size` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction size(String value) {
    return attr0(AttributeNamePojo.SIZE, value);
  }

  /// Renders the `sizes` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction sizes(String value) {
    return attr0(AttributeNamePojo.SIZES, value);
  }

  /// Renders the `spellcheck` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction spellcheck(String value) {
    return attr0(AttributeNamePojo.SPELLCHECK, value);
  }

  /// Renders the `src` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction src(String value) {
    return attr0(AttributeNamePojo.SRC, value);
  }

  /// Renders the `srcset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction srcset(String value) {
    return attr0(AttributeNamePojo.SRCSET, value);
  }

  /// Renders the `start` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction start(String value) {
    return attr0(AttributeNamePojo.START, value);
  }

  /// Renders the `stop-color` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction stopColor(String value) {
    return attr0(AttributeNamePojo.STOP_COLOR, value);
  }

  /// Renders the `stop-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction stopOpacity(String value) {
    return attr0(AttributeNamePojo.STOP_OPACITY, value);
  }

  /// Renders the `stroke` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction stroke(String value) {
    return attr0(AttributeNamePojo.STROKE, value);
  }

  /// Renders the `stroke-dasharray` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction strokeDasharray(String value) {
    return attr0(AttributeNamePojo.STROKE_DASHARRAY, value);
  }

  /// Renders the `stroke-dashoffset` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction strokeDashoffset(String value) {
    return attr0(AttributeNamePojo.STROKE_DASHOFFSET, value);
  }

  /// Renders the `stroke-linecap` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction strokeLinecap(String value) {
    return attr0(AttributeNamePojo.STROKE_LINECAP, value);
  }

  /// Renders the `stroke-linejoin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction strokeLinejoin(String value) {
    return attr0(AttributeNamePojo.STROKE_LINEJOIN, value);
  }

  /// Renders the `stroke-miterlimit` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction strokeMiterlimit(String value) {
    return attr0(AttributeNamePojo.STROKE_MITERLIMIT, value);
  }

  /// Renders the `stroke-opacity` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction strokeOpacity(String value) {
    return attr0(AttributeNamePojo.STROKE_OPACITY, value);
  }

  /// Renders the `stroke-width` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction strokeWidth(String value) {
    return attr0(AttributeNamePojo.STROKE_WIDTH, value);
  }

  /// Renders the `tabindex` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction tabindex(String value) {
    return attr0(AttributeNamePojo.TABINDEX, value);
  }

  /// Renders the `target` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction target(String value) {
    return attr0(AttributeNamePojo.TARGET, value);
  }

  /// Renders the `text-anchor` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction textAnchor(String value) {
    return attr0(AttributeNamePojo.TEXT_ANCHOR, value);
  }

  /// Renders the `text-decoration` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction textDecoration(String value) {
    return attr0(AttributeNamePojo.TEXT_DECORATION, value);
  }

  /// Renders the `text-overflow` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction textOverflow(String value) {
    return attr0(AttributeNamePojo.TEXT_OVERFLOW, value);
  }

  /// Renders the `text-rendering` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction textRendering(String value) {
    return attr0(AttributeNamePojo.TEXT_RENDERING, value);
  }

  /// Renders the `transform` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction transform(String value) {
    return attr0(AttributeNamePojo.TRANSFORM, value);
  }

  /// Renders the `transform-origin` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction transformOrigin(String value) {
    return attr0(AttributeNamePojo.TRANSFORM_ORIGIN, value);
  }

  /// Renders the `translate` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction translate(String value) {
    return attr0(AttributeNamePojo.TRANSLATE, value);
  }

  /// Renders the `type` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction type(String value) {
    return attr0(AttributeNamePojo.TYPE, value);
  }

  /// Renders the `unicode-bidi` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction unicodeBidi(String value) {
    return attr0(AttributeNamePojo.UNICODE_BIDI, value);
  }

  /// Renders the `value` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction value(String value) {
    return attr0(AttributeNamePojo.VALUE, value);
  }

  /// Renders the `vector-effect` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction vectorEffect(String value) {
    return attr0(AttributeNamePojo.VECTOR_EFFECT, value);
  }

  /// Renders the `viewBox` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction viewBox(String value) {
    return attr0(AttributeNamePojo.VIEWBOX, value);
  }

  /// Renders the `visibility` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction visibility(String value) {
    return attr0(AttributeNamePojo.VISIBILITY, value);
  }

  /// Renders the `white-space` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction whiteSpace(String value) {
    return attr0(AttributeNamePojo.WHITE_SPACE, value);
  }

  /// Renders the `width` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction width(String value) {
    return attr0(AttributeNamePojo.WIDTH, value);
  }

  /// Renders the `word-spacing` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction wordSpacing(String value) {
    return attr0(AttributeNamePojo.WORD_SPACING, value);
  }

  /// Renders the `wrap` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction wrap(String value) {
    return attr0(AttributeNamePojo.WRAP, value);
  }

  /// Renders the `writing-mode` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction writingMode(String value) {
    return attr0(AttributeNamePojo.WRITING_MODE, value);
  }

  /// Renders the `xmlns` attribute with the specified value.
  ///
  /// @param value the attribute value
  ///
  /// @return an instruction representing the attribute
  public final AttributeInstruction xmlns(String value) {
    return attr0(AttributeNamePojo.XMLNS, value);
  }

  //
  // AMBIGUOUS
  //

  abstract AmbiguousInstruction ambiguous(Ambiguous name, String text);

  /// Renders the `clip-path` attribute or the `clipPath` element with the
  /// specified text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an instruction representing the attribute or the element
  public final AmbiguousInstruction clipPath(String text) {
    return ambiguous(Ambiguous.CLIPPATH, text);
  }

  /// Renders the `form` attribute or the `form` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an instruction representing the attribute or the element
  public final AmbiguousInstruction form(String text) {
    return ambiguous(Ambiguous.FORM, text);
  }

  /// Renders the `label` attribute or the `label` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an instruction representing the attribute or the element
  public final AmbiguousInstruction label(String text) {
    return ambiguous(Ambiguous.LABEL, text);
  }

  /// Renders the `style` attribute or the `style` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an instruction representing the attribute or the element
  public final AmbiguousInstruction style(String text) {
    return ambiguous(Ambiguous.STYLE, text);
  }

  /// Renders the `title` attribute or the `title` element with the specified
  /// text.
  ///
  /// @param text the attribute value or the text content of the element
  ///
  /// @return an instruction representing the attribute or the element
  public final AmbiguousInstruction title(String text) {
    return ambiguous(Ambiguous.TITLE, text);
  }

  //
  // TEXT
  //

  /// Renders the non-breaking space `&nbsp;` HTML character entity.
  ///
  /// @return an instruction representing the non-breaking space
  ///         character entity.
  public abstract RawInstruction nbsp();

  /// Renders the specified value as raw HTML.
  ///
  /// @param value the raw HTML value
  ///
  /// @return a raw HTML instruction
  public abstract RawInstruction raw(String value);

  /// Renders a text node with the specified value. The text value is escaped
  /// before being emitted to the output.
  ///
  /// @param value the text value
  ///
  /// @return an instruction representing the text node
  public abstract TextInstruction text(String value);

}
