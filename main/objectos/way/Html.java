/*
 * Copyright (C) 2015-2026 Objectos Software LTDA.
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
import objectos.html.AttributeName;
import objectos.html.AttributeObject;
import objectos.html.Component;
import objectos.html.ElementName;
import objectos.html.Markup;
import objectos.internal.Check;
import objectos.script.JsAction;
import objectox.html.HtmlInstruction;
import objectox.html.MarkupPojo;
import objectox.html.attr.AttributeNamePojo;
import objectox.html.attr.AttributeOrNoOp;

/**
 * The <strong>Objectos HTML</strong> main class.
 */
public final class Html {

  /// A fragment represents a set of markup or template instructions to be
  /// lazily applied.
  ///
  /// The set of instructions MUST be of the same markup or template instance
  /// where this fragment will be included.
  public sealed interface Fragment {

    /// A fragment that takes no arguments.
    @FunctionalInterface
    non-sealed interface Of0 extends Fragment {

      /// Invokes this set of instructions.
      void invoke();

    }

    /// A fragment that takes one argument.
    ///
    /// @param <T1> the type of the argument
    @FunctionalInterface
    non-sealed interface Of1<T1> extends Fragment {

      /// Invokes this set of instructions.
      ///
      /// @param arg1 the argument
      void invoke(T1 arg1);

    }

    /// A fragment that takes two arguments.
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    @FunctionalInterface
    non-sealed interface Of2<T1, T2> extends Fragment {

      /// Invokes this set of instructions.
      ///
      /// @param arg1 the first argument
      /// @param arg2 the second argument
      void invoke(T1 arg1, T2 arg2);

    }

    /// A fragment that takes three arguments.
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param <T3> the type of the third argument
    @FunctionalInterface
    non-sealed interface Of3<T1, T2, T3> extends Fragment {

      /// Invokes this set of instructions.
      ///
      /// @param arg1 the first argument
      /// @param arg2 the second argument
      /// @param arg3 the third argument
      void invoke(T1 arg1, T2 arg2, T3 arg3);

    }

    /// A fragment that takes four arguments.
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param <T3> the type of the third argument
    /// @param <T4> the type of the fourth argument
    @FunctionalInterface
    non-sealed interface Of4<T1, T2, T3, T4> extends Fragment {

      /// Invokes this set of instructions.
      ///
      /// @param arg1 the first argument
      /// @param arg2 the second argument
      /// @param arg3 the third argument
      /// @param arg4 the fourth argument
      void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4);

    }

  }

  /**
   * Represents an instruction that generates part of the output of an HTML
   * template.
   */
  public sealed interface Instruction {

    /**
     * Class of instructions that are represented by object instances.
     *
     * <p>
     * Instances of this interface can be safely reused in multiple templates.
     */
    sealed interface AsObject extends Instruction permits AttributeObject {}

    /**
     * Class of instructions that are represented by methods of the
     * {@link Html.Template} class.
     *
     * <p>
     * Instances of this interface MUST NOT be reused in a template.
     */
    sealed interface AsMethod extends Instruction {}

    /**
     * An instruction to generate an ambiguous element in a template.
     */
    sealed interface OfAmbiguous extends OfAttribute, OfElement permits HtmlInstruction {}

    /**
     * An instruction to generate an HTML attribute in template.
     */
    sealed interface OfAttribute extends AsMethod, OfVoid permits OfAmbiguous, AttributeOrNoOp {}

    /**
     * An instruction to generate a {@code data-on-*} HTML attribute in a
     * template.
     */
    sealed interface OfDataOn extends AsMethod, OfVoid permits AttributeOrNoOp {}

    /**
     * An instruction to generate an HTML element in a template.
     */
    sealed interface OfElement extends AsMethod permits OfAmbiguous, HtmlInstruction {}

    /**
     * An instruction to include an HTML fragment to a template.
     */
    sealed interface OfFragment extends AsMethod, OfVoid permits HtmlInstruction {}

    /**
     * Class of instructions that are allowed as arguments to template
     * methods that represent void elements.
     */
    sealed interface OfVoid extends Instruction 
        permits 
        AttributeObject, 
        OfAttribute,
        OfDataOn,
        OfFragment,
        NoOp {}

    /// The no-op instruction.
    sealed interface NoOp extends AsMethod, OfVoid permits AttributeOrNoOp {}

    /// Returns the no-op instruction.
    ///
    /// @return the no-op instruction
    static NoOp noop() {
      return HtmlInstruction.NOOP;
    }

  }

  /**
   * A template in pure Java for generating HTML.
   *
   * <p>
   * This class provides methods for representing HTML code in a Java class. An
   * instance of the class can then be used to generate the represented HTML
   * code.
   */
  public static abstract class Template implements Component {

    Markup html;

    /**
     * Sole constructor.
     */
    protected Template() {}

    @Override
    public final void renderHtml(Markup m) {
      Check.state(html == null, "Concurrent evalution of a HtmlTemplate is not supported");

      try {
        html = m;

        render();
      } finally {
        html = null;
      }
    }

    /// Returns the HTML generated by this template suited to be used in JSON.
    ///
    /// @return the HTML generated by this template suited to be used in JSON
    public final String toJsonString() {
      final Markup html;
      html = new Markup.OfHtml();

      renderHtml(html);

      final MarkupPojo impl;
      impl = (MarkupPojo) html;

      return impl.toJsonString();
    }

    /**
     * Returns the HTML generated by this template.
     *
     * @return the HTML generated by this template
     */
    @Override
    public final String toString() {
      return toHtml();
    }

    /**
     * Defines the HTML code to be generated by this template.
     */
    protected abstract void render();

    private Markup $html() {
      Check.state(html != null, "html not set");

      return html;
    }

    /// Renders the specified attribute at the root of this template or
    /// fragment.
    ///
    /// @param object the attribute
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute attr(AttributeObject object) {
      return $html().attr(object);
    }

    // START generated code

    /// The `async` boolean attribute.
    protected static final AttributeObject async = Markup.async;

    /// The `autofocus` boolean attribute.
    protected static final AttributeObject autofocus = Markup.autofocus;

    /// The `checked` boolean attribute.
    protected static final AttributeObject checked = Markup.checked;

    /// The `defer` boolean attribute.
    protected static final AttributeObject defer = Markup.defer;

    /// The `disabled` boolean attribute.
    protected static final AttributeObject disabled = Markup.disabled;

    /// The `hidden` boolean attribute.
    protected static final AttributeObject hidden = Markup.hidden;

    /// The `multiple` boolean attribute.
    protected static final AttributeObject multiple = Markup.multiple;

    /// The `nomodule` boolean attribute.
    protected static final AttributeObject nomodule = Markup.nomodule;

    /// The `open` boolean attribute.
    protected static final AttributeObject open = Markup.open;

    /// The `readonly` boolean attribute.
    protected static final AttributeObject readonly = Markup.readonly;

    /// The `required` boolean attribute.
    protected static final AttributeObject required = Markup.required;

    /// The `reversed` boolean attribute.
    protected static final AttributeObject reversed = Markup.reversed;

    /// The `selected` boolean attribute.
    protected static final AttributeObject selected = Markup.selected;

    //
    // WAY
    //

    /// Renders the `class` attribute by processing the specified value.
    ///
    /// This method is designed to work with Java text blocks. It first removes
    /// any leading and trailing whitespace. Additionally, any sequence of
    /// consecutive whitespace characters is replaced by a single space
    /// character.
    ///
    /// For example, the following invocation:
    ///
    /// ```java css(""" display:inline-flex justify-content:center
    ///
    /// background-color:blue-500 """); ```
    ///
    /// Produces the same result as invoking `className("display:inline-flex
    /// justify-content:center background-color:blue-500")`.
    ///
    /// @param value the text block containing class names, possibly spread
    ///        across multiple lines
    ///
    /// @return an instruction representing this attribute.
    protected final Html.Instruction.OfAttribute css(String value) {
      return $html().css(value);
    }

    /// Renders the specified components in order as part of this document.
    ///
    /// @param components the components to be rendered as part of this document
    ///
    /// @return an instruction representing the rendered components.
    protected final Html.Instruction.OfFragment c(Component... components) {
      return $html().c(components);
    }

    /// Renders the specified components in order as part of this document.
    ///
    /// @param components the components to be rendered as part of this document
    ///
    /// @return an instruction representing the rendered components.
    protected final Html.Instruction.OfFragment c(Iterable<? extends Component> components) {
      return $html().c(components);
    }

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f0"}
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
    ///
    /// @return an instruction representing the fragment
    protected final Html.Instruction.OfFragment f(Html.Fragment.Of0 fragment) {
      return $html().f(fragment);
    }

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f1"}
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
    ///
    /// @return an instruction representing the fragment
    protected final <T1> Html.Instruction.OfFragment f(Html.Fragment.Of1<T1> fragment, T1 arg1) {
      return $html().f(fragment, arg1);
    }

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f2"}
    ///
    /// Generates the following HTML:
    ///
    /// ```html <div><button>OK</button><button>Cancel</button></div> ```
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param fragment the fragment to include
    /// @param arg1 the first argument
    /// @param arg2 the second argument
    ///
    /// @return an instruction representing the fragment
    protected final <T1, T2> Html.Instruction.OfFragment f(Html.Fragment.Of2<T1, T2> fragment, T1 arg1, T2 arg2) {
      return $html().f(fragment, arg1, arg2);
    }

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f3"}
    ///
    /// Generates the following HTML:
    ///
    /// ```html
    /// <div>
    /// <p>
    /// City<span>Tokyo</span>
    /// </p>
    /// <p>
    /// Country<span>Japan</span>
    /// </p>
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
    ///
    /// @return an instruction representing the fragment
    protected final <T1, T2, T3> Html.Instruction.OfFragment f(Html.Fragment.Of3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
      return $html().f(fragment, arg1, arg2, arg3);
    }

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
    ///
    /// @return an instruction representing the fragment
    protected final <T1, T2, T3, T4> Html.Instruction.OfFragment f(Html.Fragment.Of4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      return $html().f(fragment, arg1, arg2, arg3, arg4);
    }

    /// Flattens the specified instructions so that each of the specified
    /// instructions is individually added, in order, to a receiving element.
    ///
    /// @param contents the instructions to be flattened
    ///
    /// @return an instruction representing this flatten operation
    protected final Html.Instruction.OfElement flatten(Html.Instruction... contents) {
      return $html().flatten(contents);
    }

    /// Flattens the specified instructions so that each of the specified
    /// instructions is individually added, in order, to a receiving element.
    ///
    /// @param contents the instructions to be flattened
    ///
    /// @return an instruction representing this flatten operation
    protected final Html.Instruction.OfElement flatten(Iterable<? extends Html.Instruction> contents) {
      return $html().flatten(contents);
    }

    /// The no-op instruction.
    ///
    /// @return the no-op instruction.
    protected final Html.Instruction.NoOp noop() {
      return $html().noop();
    }

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
    protected final String testableCell(String value, int width) {
      return $html().testableCell(value, width);
    }

    /// Formats the specified name and value as a testable field.
    ///
    /// @param name the field name
    /// @param value the field value
    ///
    /// @return always the field value
    protected final String testableField(String name, String value) {
      return $html().testableField(name, value);
    }

    /// Formats the specified name as a testable field name.
    ///
    /// @param name the field name
    ///
    /// @return the specified field name
    protected final String testableFieldName(String name) {
      return $html().testableFieldName(name);
    }

    /// Formats the specified value as a testable field value.
    ///
    /// @param value the field value
    ///
    /// @return the specified field value
    protected final String testableFieldValue(String value) {
      return $html().testableFieldValue(value);
    }

    /// Formats the specified value as a testable heading level 1.
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    protected final String testableH1(String value) {
      return $html().testableH1(value);
    }

    /// Formats the specified value as a testable heading level 2.
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    protected final String testableH2(String value) {
      return $html().testableH2(value);
    }

    /// Formats the specified value as a testable heading level 3.
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    protected final String testableH3(String value) {
      return $html().testableH3(value);
    }

    /// Formats the specified value as a testable heading level 4.
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    protected final String testableH4(String value) {
      return $html().testableH4(value);
    }

    /// Formats the specified value as a testable heading level 5.
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    protected final String testableH5(String value) {
      return $html().testableH5(value);
    }

    /// Formats the specified value as a testable heading level 6.
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    protected final String testableH6(String value) {
      return $html().testableH6(value);
    }

    /// Formats a line separator at the testable output exclusively.
    ///
    /// @return a no-op instruction
    protected final Html.Instruction.NoOp testableNewLine() {
      return $html().testableNewLine();
    }

    //
    // ELEMENTS
    //

    /// Renders an HTML element with the specified name and contents.
    ///
    /// @param name the element name
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element
    protected final Html.Instruction.OfElement elem(ElementName name, Html.Instruction... contents) {
      return $html().elem(name, contents);
    }

    /// Renders an HTML element with the specified name and text.
    ///
    /// @param name the element name
    /// @param text the text value of this element
    ///
    /// @return an instruction representing the element
    protected final Html.Instruction.OfElement elem(ElementName name, String text) {
      return $html().elem(name, text);
    }

    /// Renders the `<!DOCTYPE html>` doctype.
    protected final void doctype() {
      $html().doctype();
    }

    /// Renders the `a` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement a(Html.Instruction... contents) {
      return $html().a(contents);
    }

    /// Renders the `a` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement a(String text) {
      return $html().a(text);
    }

    /// Renders the `abbr` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement abbr(Html.Instruction... contents) {
      return $html().abbr(contents);
    }

    /// Renders the `abbr` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement abbr(String text) {
      return $html().abbr(text);
    }

    /// Renders the `article` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement article(Html.Instruction... contents) {
      return $html().article(contents);
    }

    /// Renders the `article` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement article(String text) {
      return $html().article(text);
    }

    /// Renders the `aside` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement aside(Html.Instruction... contents) {
      return $html().aside(contents);
    }

    /// Renders the `aside` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement aside(String text) {
      return $html().aside(text);
    }

    /// Renders the `b` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement b(Html.Instruction... contents) {
      return $html().b(contents);
    }

    /// Renders the `b` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement b(String text) {
      return $html().b(text);
    }

    /// Renders the `blockquote` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement blockquote(Html.Instruction... contents) {
      return $html().blockquote(contents);
    }

    /// Renders the `blockquote` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement blockquote(String text) {
      return $html().blockquote(text);
    }

    /// Renders the `body` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement body(Html.Instruction... contents) {
      return $html().body(contents);
    }

    /// Renders the `body` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement body(String text) {
      return $html().body(text);
    }

    /// Renders the `br` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents) {
      return $html().br(contents);
    }

    /// Renders the `button` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement button(Html.Instruction... contents) {
      return $html().button(contents);
    }

    /// Renders the `button` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement button(String text) {
      return $html().button(text);
    }

    /// Renders the `clipPath` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement clipPath(Html.Instruction... contents) {
      return $html().clipPath(contents);
    }

    /// Renders the `code` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement code(Html.Instruction... contents) {
      return $html().code(contents);
    }

    /// Renders the `code` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement code(String text) {
      return $html().code(text);
    }

    /// Renders the `dd` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dd(Html.Instruction... contents) {
      return $html().dd(contents);
    }

    /// Renders the `dd` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dd(String text) {
      return $html().dd(text);
    }

    /// Renders the `defs` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement defs(Html.Instruction... contents) {
      return $html().defs(contents);
    }

    /// Renders the `defs` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement defs(String text) {
      return $html().defs(text);
    }

    /// Renders the `details` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement details(Html.Instruction... contents) {
      return $html().details(contents);
    }

    /// Renders the `details` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement details(String text) {
      return $html().details(text);
    }

    /// Renders the `dialog` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dialog(Html.Instruction... contents) {
      return $html().dialog(contents);
    }

    /// Renders the `dialog` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dialog(String text) {
      return $html().dialog(text);
    }

    /// Renders the `div` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement div(Html.Instruction... contents) {
      return $html().div(contents);
    }

    /// Renders the `div` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement div(String text) {
      return $html().div(text);
    }

    /// Renders the `dl` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dl(Html.Instruction... contents) {
      return $html().dl(contents);
    }

    /// Renders the `dl` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dl(String text) {
      return $html().dl(text);
    }

    /// Renders the `dt` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dt(Html.Instruction... contents) {
      return $html().dt(contents);
    }

    /// Renders the `dt` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dt(String text) {
      return $html().dt(text);
    }

    /// Renders the `em` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement em(Html.Instruction... contents) {
      return $html().em(contents);
    }

    /// Renders the `em` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement em(String text) {
      return $html().em(text);
    }

    /// Renders the `fieldset` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement fieldset(Html.Instruction... contents) {
      return $html().fieldset(contents);
    }

    /// Renders the `fieldset` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement fieldset(String text) {
      return $html().fieldset(text);
    }

    /// Renders the `figure` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement figure(Html.Instruction... contents) {
      return $html().figure(contents);
    }

    /// Renders the `figure` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement figure(String text) {
      return $html().figure(text);
    }

    /// Renders the `footer` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement footer(Html.Instruction... contents) {
      return $html().footer(contents);
    }

    /// Renders the `footer` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement footer(String text) {
      return $html().footer(text);
    }

    /// Renders the `form` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement form(Html.Instruction... contents) {
      return $html().form(contents);
    }

    /// Renders the `g` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement g(Html.Instruction... contents) {
      return $html().g(contents);
    }

    /// Renders the `g` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement g(String text) {
      return $html().g(text);
    }

    /// Renders the `h1` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h1(Html.Instruction... contents) {
      return $html().h1(contents);
    }

    /// Renders the `h1` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h1(String text) {
      return $html().h1(text);
    }

    /// Renders the `h2` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h2(Html.Instruction... contents) {
      return $html().h2(contents);
    }

    /// Renders the `h2` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h2(String text) {
      return $html().h2(text);
    }

    /// Renders the `h3` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h3(Html.Instruction... contents) {
      return $html().h3(contents);
    }

    /// Renders the `h3` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h3(String text) {
      return $html().h3(text);
    }

    /// Renders the `h4` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h4(Html.Instruction... contents) {
      return $html().h4(contents);
    }

    /// Renders the `h4` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h4(String text) {
      return $html().h4(text);
    }

    /// Renders the `h5` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h5(Html.Instruction... contents) {
      return $html().h5(contents);
    }

    /// Renders the `h5` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h5(String text) {
      return $html().h5(text);
    }

    /// Renders the `h6` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h6(Html.Instruction... contents) {
      return $html().h6(contents);
    }

    /// Renders the `h6` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h6(String text) {
      return $html().h6(text);
    }

    /// Renders the `head` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement head(Html.Instruction... contents) {
      return $html().head(contents);
    }

    /// Renders the `head` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement head(String text) {
      return $html().head(text);
    }

    /// Renders the `header` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement header(Html.Instruction... contents) {
      return $html().header(contents);
    }

    /// Renders the `header` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement header(String text) {
      return $html().header(text);
    }

    /// Renders the `hgroup` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement hgroup(Html.Instruction... contents) {
      return $html().hgroup(contents);
    }

    /// Renders the `hgroup` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement hgroup(String text) {
      return $html().hgroup(text);
    }

    /// Renders the `hr` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents) {
      return $html().hr(contents);
    }

    /// Renders the `html` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement html(Html.Instruction... contents) {
      return $html().html(contents);
    }

    /// Renders the `html` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement html(String text) {
      return $html().html(text);
    }

    /// Renders the `img` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents) {
      return $html().img(contents);
    }

    /// Renders the `input` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents) {
      return $html().input(contents);
    }

    /// Renders the `kbd` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement kbd(Html.Instruction... contents) {
      return $html().kbd(contents);
    }

    /// Renders the `kbd` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement kbd(String text) {
      return $html().kbd(text);
    }

    /// Renders the `label` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement label(Html.Instruction... contents) {
      return $html().label(contents);
    }

    /// Renders the `legend` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement legend(Html.Instruction... contents) {
      return $html().legend(contents);
    }

    /// Renders the `legend` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement legend(String text) {
      return $html().legend(text);
    }

    /// Renders the `li` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement li(Html.Instruction... contents) {
      return $html().li(contents);
    }

    /// Renders the `li` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement li(String text) {
      return $html().li(text);
    }

    /// Renders the `link` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents) {
      return $html().link(contents);
    }

    /// Renders the `main` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement main(Html.Instruction... contents) {
      return $html().main(contents);
    }

    /// Renders the `main` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement main(String text) {
      return $html().main(text);
    }

    /// Renders the `menu` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement menu(Html.Instruction... contents) {
      return $html().menu(contents);
    }

    /// Renders the `menu` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement menu(String text) {
      return $html().menu(text);
    }

    /// Renders the `meta` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents) {
      return $html().meta(contents);
    }

    /// Renders the `nav` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement nav(Html.Instruction... contents) {
      return $html().nav(contents);
    }

    /// Renders the `nav` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement nav(String text) {
      return $html().nav(text);
    }

    /// Renders the `noscript` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement noscript(Html.Instruction... contents) {
      return $html().noscript(contents);
    }

    /// Renders the `noscript` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement noscript(String text) {
      return $html().noscript(text);
    }

    /// Renders the `ol` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement ol(Html.Instruction... contents) {
      return $html().ol(contents);
    }

    /// Renders the `ol` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement ol(String text) {
      return $html().ol(text);
    }

    /// Renders the `optgroup` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement optgroup(Html.Instruction... contents) {
      return $html().optgroup(contents);
    }

    /// Renders the `optgroup` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement optgroup(String text) {
      return $html().optgroup(text);
    }

    /// Renders the `option` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement option(Html.Instruction... contents) {
      return $html().option(contents);
    }

    /// Renders the `option` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement option(String text) {
      return $html().option(text);
    }

    /// Renders the `p` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement p(Html.Instruction... contents) {
      return $html().p(contents);
    }

    /// Renders the `p` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement p(String text) {
      return $html().p(text);
    }

    /// Renders the `path` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement path(Html.Instruction... contents) {
      return $html().path(contents);
    }

    /// Renders the `path` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement path(String text) {
      return $html().path(text);
    }

    /// Renders the `pre` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement pre(Html.Instruction... contents) {
      return $html().pre(contents);
    }

    /// Renders the `pre` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement pre(String text) {
      return $html().pre(text);
    }

    /// Renders the `progress` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement progress(Html.Instruction... contents) {
      return $html().progress(contents);
    }

    /// Renders the `progress` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement progress(String text) {
      return $html().progress(text);
    }

    /// Renders the `samp` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement samp(Html.Instruction... contents) {
      return $html().samp(contents);
    }

    /// Renders the `samp` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement samp(String text) {
      return $html().samp(text);
    }

    /// Renders the `script` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement script(Html.Instruction... contents) {
      return $html().script(contents);
    }

    /// Renders the `script` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement script(String text) {
      return $html().script(text);
    }

    /// Renders the `section` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement section(Html.Instruction... contents) {
      return $html().section(contents);
    }

    /// Renders the `section` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement section(String text) {
      return $html().section(text);
    }

    /// Renders the `select` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement select(Html.Instruction... contents) {
      return $html().select(contents);
    }

    /// Renders the `select` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement select(String text) {
      return $html().select(text);
    }

    /// Renders the `small` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement small(Html.Instruction... contents) {
      return $html().small(contents);
    }

    /// Renders the `small` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement small(String text) {
      return $html().small(text);
    }

    /// Renders the `span` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement span(Html.Instruction... contents) {
      return $html().span(contents);
    }

    /// Renders the `span` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement span(String text) {
      return $html().span(text);
    }

    /// Renders the `strong` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement strong(Html.Instruction... contents) {
      return $html().strong(contents);
    }

    /// Renders the `strong` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement strong(String text) {
      return $html().strong(text);
    }

    /// Renders the `style` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement style(Html.Instruction... contents) {
      return $html().style(contents);
    }

    /// Renders the `sub` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement sub(Html.Instruction... contents) {
      return $html().sub(contents);
    }

    /// Renders the `sub` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement sub(String text) {
      return $html().sub(text);
    }

    /// Renders the `summary` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement summary(Html.Instruction... contents) {
      return $html().summary(contents);
    }

    /// Renders the `summary` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement summary(String text) {
      return $html().summary(text);
    }

    /// Renders the `sup` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement sup(Html.Instruction... contents) {
      return $html().sup(contents);
    }

    /// Renders the `sup` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement sup(String text) {
      return $html().sup(text);
    }

    /// Renders the `svg` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement svg(Html.Instruction... contents) {
      return $html().svg(contents);
    }

    /// Renders the `svg` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement svg(String text) {
      return $html().svg(text);
    }

    /// Renders the `table` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement table(Html.Instruction... contents) {
      return $html().table(contents);
    }

    /// Renders the `table` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement table(String text) {
      return $html().table(text);
    }

    /// Renders the `tbody` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement tbody(Html.Instruction... contents) {
      return $html().tbody(contents);
    }

    /// Renders the `tbody` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement tbody(String text) {
      return $html().tbody(text);
    }

    /// Renders the `td` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement td(Html.Instruction... contents) {
      return $html().td(contents);
    }

    /// Renders the `td` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement td(String text) {
      return $html().td(text);
    }

    /// Renders the `template` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement template(Html.Instruction... contents) {
      return $html().template(contents);
    }

    /// Renders the `template` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement template(String text) {
      return $html().template(text);
    }

    /// Renders the `textarea` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement textarea(Html.Instruction... contents) {
      return $html().textarea(contents);
    }

    /// Renders the `textarea` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement textarea(String text) {
      return $html().textarea(text);
    }

    /// Renders the `th` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement th(Html.Instruction... contents) {
      return $html().th(contents);
    }

    /// Renders the `th` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement th(String text) {
      return $html().th(text);
    }

    /// Renders the `thead` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement thead(Html.Instruction... contents) {
      return $html().thead(contents);
    }

    /// Renders the `thead` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement thead(String text) {
      return $html().thead(text);
    }

    /// Renders the `title` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement title(Html.Instruction... contents) {
      return $html().title(contents);
    }

    /// Renders the `tr` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement tr(Html.Instruction... contents) {
      return $html().tr(contents);
    }

    /// Renders the `tr` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement tr(String text) {
      return $html().tr(text);
    }

    /// Renders the `ul` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement ul(Html.Instruction... contents) {
      return $html().ul(contents);
    }

    /// Renders the `ul` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement ul(String text) {
      return $html().ul(text);
    }

    //
    // ATTRIBUTES
    //

    /// Renders an attribute with the specified name.
    ///
    /// @param name the attribute name
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute attr(AttributeName name) {
      return $html().attr(name);
    }

    /// Renders an attribute with the specified name and value.
    ///
    /// @param name the attribute name
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute attr(AttributeName name, String value) {
      return $html().attr(name, value);
    }

    /// Renders the `accesskey` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute accesskey(String value) {
      return $html().accesskey(value);
    }

    /// Renders the `action` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute action(String value) {
      return $html().action(value);
    }

    /// Renders the `align` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute align(String value) {
      return $html().align(value);
    }

    /// Renders the `alignment-baseline` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute alignmentBaseline(String value) {
      return $html().alignmentBaseline(value);
    }

    /// Renders the `alt` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute alt(String value) {
      return $html().alt(value);
    }

    /// Renders the `aria-current` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaCurrent(String value) {
      return $html().ariaCurrent(value);
    }

    /// Renders the `aria-disabled` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaDisabled(String value) {
      return $html().ariaDisabled(value);
    }

    /// Renders the `aria-hidden` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaHidden(String value) {
      return $html().ariaHidden(value);
    }

    /// Renders the `aria-invalid` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaInvalid(String value) {
      return $html().ariaInvalid(value);
    }

    /// Renders the `aria-label` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaLabel(String value) {
      return $html().ariaLabel(value);
    }

    /// Renders the `aria-labelledby` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaLabelledBy(String value) {
      return $html().ariaLabelledBy(value);
    }

    /// Renders the `aria-modal` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaModal(String value) {
      return $html().ariaModal(value);
    }

    /// Renders the `aria-placeholder` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaPlaceholder(String value) {
      return $html().ariaPlaceholder(value);
    }

    /// Renders the `aria-readonly` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaReadonly(String value) {
      return $html().ariaReadonly(value);
    }

    /// Renders the `aria-required` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaRequired(String value) {
      return $html().ariaRequired(value);
    }

    /// Renders the `aria-selected` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaSelected(String value) {
      return $html().ariaSelected(value);
    }

    /// Renders the `as` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute as(String value) {
      return $html().as(value);
    }

    /// Renders the `autocomplete` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute autocomplete(String value) {
      return $html().autocomplete(value);
    }

    /// Renders the `baseline-shift` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute baselineShift(String value) {
      return $html().baselineShift(value);
    }

    /// Renders the `border` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute border(String value) {
      return $html().border(value);
    }

    /// Renders the `cellpadding` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute cellpadding(String value) {
      return $html().cellpadding(value);
    }

    /// Renders the `cellspacing` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute cellspacing(String value) {
      return $html().cellspacing(value);
    }

    /// Renders the `charset` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute charset(String value) {
      return $html().charset(value);
    }

    /// Renders the `cite` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute cite(String value) {
      return $html().cite(value);
    }

    /// Renders the `class` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute className(String value) {
      return $html().className(value);
    }

    /// Renders the `clip-rule` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute clipRule(String value) {
      return $html().clipRule(value);
    }

    /// Renders the `closedby` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute closedby(String value) {
      return $html().closedby(value);
    }

    /// Renders the `color` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute color(String value) {
      return $html().color(value);
    }

    /// Renders the `color-interpolation` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute colorInterpolation(String value) {
      return $html().colorInterpolation(value);
    }

    /// Renders the `color-interpolation-filters` attribute with the specified
    /// value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute colorInterpolationFilters(String value) {
      return $html().colorInterpolationFilters(value);
    }

    /// Renders the `cols` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute cols(String value) {
      return $html().cols(value);
    }

    /// Renders the `content` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute content(String value) {
      return $html().content(value);
    }

    /// Renders the `contenteditable` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute contenteditable(String value) {
      return $html().contenteditable(value);
    }

    /// Renders the `crossorigin` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute crossorigin(String value) {
      return $html().crossorigin(value);
    }

    /// Renders the `cursor` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute cursor(String value) {
      return $html().cursor(value);
    }

    /// Renders the `d` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute d(String value) {
      return $html().d(value);
    }

    /// Renders the `dir` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute dir(String value) {
      return $html().dir(value);
    }

    /// Renders the `direction` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute direction(String value) {
      return $html().direction(value);
    }

    /// Renders the `dirname` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute dirname(String value) {
      return $html().dirname(value);
    }

    /// Renders the `display` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute display(String value) {
      return $html().display(value);
    }

    /// Renders the `dominant-baseline` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute dominantBaseline(String value) {
      return $html().dominantBaseline(value);
    }

    /// Renders the `download` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute download(String value) {
      return $html().download(value);
    }

    /// Renders the `draggable` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute draggable(String value) {
      return $html().draggable(value);
    }

    /// Renders the `enctype` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute enctype(String value) {
      return $html().enctype(value);
    }

    /// Renders the `fill` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute fill(String value) {
      return $html().fill(value);
    }

    /// Renders the `fill-opacity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute fillOpacity(String value) {
      return $html().fillOpacity(value);
    }

    /// Renders the `fill-rule` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute fillRule(String value) {
      return $html().fillRule(value);
    }

    /// Renders the `filter` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute filter(String value) {
      return $html().filter(value);
    }

    /// Renders the `flood-color` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute floodColor(String value) {
      return $html().floodColor(value);
    }

    /// Renders the `flood-opacity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute floodOpacity(String value) {
      return $html().floodOpacity(value);
    }

    /// Renders the `for` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute forId(String value) {
      return $html().forId(value);
    }

    /// Renders the `glyph-orientation-horizontal` attribute with the specified
    /// value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute glyphOrientationHorizontal(String value) {
      return $html().glyphOrientationHorizontal(value);
    }

    /// Renders the `glyph-orientation-vertical` attribute with the specified
    /// value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute glyphOrientationVertical(String value) {
      return $html().glyphOrientationVertical(value);
    }

    /// Renders the `height` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute height(String value) {
      return $html().height(value);
    }

    /// Renders the `href` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute href(String value) {
      return $html().href(value);
    }

    /// Renders the `http-equiv` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute httpEquiv(String value) {
      return $html().httpEquiv(value);
    }

    /// Renders the `id` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute id(String value) {
      return $html().id(value);
    }

    /// Renders the `image-rendering` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute imageRendering(String value) {
      return $html().imageRendering(value);
    }

    /// Renders the `integrity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute integrity(String value) {
      return $html().integrity(value);
    }

    /// Renders the `lang` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute lang(String value) {
      return $html().lang(value);
    }

    /// Renders the `letter-spacing` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute letterSpacing(String value) {
      return $html().letterSpacing(value);
    }

    /// Renders the `lighting-color` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute lightingColor(String value) {
      return $html().lightingColor(value);
    }

    /// Renders the `marker-end` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute markerEnd(String value) {
      return $html().markerEnd(value);
    }

    /// Renders the `marker-mid` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute markerMid(String value) {
      return $html().markerMid(value);
    }

    /// Renders the `marker-start` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute markerStart(String value) {
      return $html().markerStart(value);
    }

    /// Renders the `mask` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute mask(String value) {
      return $html().mask(value);
    }

    /// Renders the `mask-type` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute maskType(String value) {
      return $html().maskType(value);
    }

    /// Renders the `maxlength` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute maxlength(String value) {
      return $html().maxlength(value);
    }

    /// Renders the `media` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute media(String value) {
      return $html().media(value);
    }

    /// Renders the `method` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute method(String value) {
      return $html().method(value);
    }

    /// Renders the `minlength` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute minlength(String value) {
      return $html().minlength(value);
    }

    /// Renders the `name` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute name(String value) {
      return $html().name(value);
    }

    /// Renders the `onclick` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onclick(String value) {
      return $html().onclick(value);
    }

    /// Renders the `onclick` attribute with the specified action.
    ///
    /// @param value the action to execute
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onclick(JsAction value) {
      return $html().onclick(value);
    }

    /// Renders the `onload` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onload(String value) {
      return $html().onload(value);
    }

    /// Renders the `onload` attribute with the specified action.
    ///
    /// @param value the action to execute
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onload(JsAction value) {
      return $html().onload(value);
    }

    /// Renders the `onpopstate` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onpopstate(String value) {
      return $html().onpopstate(value);
    }

    /// Renders the `onpopstate` attribute with the specified action.
    ///
    /// @param value the action to execute
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onpopstate(JsAction value) {
      return $html().onpopstate(value);
    }

    /// Renders the `onsubmit` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onsubmit(String value) {
      return $html().onsubmit(value);
    }

    /// Renders the `onsubmit` attribute with the specified action.
    ///
    /// @param value the action to execute
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onsubmit(JsAction value) {
      return $html().onsubmit(value);
    }

    /// Renders the `opacity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute opacity(String value) {
      return $html().opacity(value);
    }

    /// Renders the `overflow` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute overflow(String value) {
      return $html().overflow(value);
    }

    /// Renders the `paint-order` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute paintOrder(String value) {
      return $html().paintOrder(value);
    }

    /// Renders the `placeholder` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute placeholder(String value) {
      return $html().placeholder(value);
    }

    /// Renders the `pointer-events` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute pointerEvents(String value) {
      return $html().pointerEvents(value);
    }

    /// Renders the `property` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute property(String value) {
      return $html().property(value);
    }

    /// Renders the `referrerpolicy` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute referrerpolicy(String value) {
      return $html().referrerpolicy(value);
    }

    /// Renders the `rel` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute rel(String value) {
      return $html().rel(value);
    }

    /// Renders the `rev` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute rev(String value) {
      return $html().rev(value);
    }

    /// Renders the `role` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute role(String value) {
      return $html().role(value);
    }

    /// Renders the `rows` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute rows(String value) {
      return $html().rows(value);
    }

    /// Renders the `shape-rendering` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute shapeRendering(String value) {
      return $html().shapeRendering(value);
    }

    /// Renders the `size` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute size(String value) {
      return $html().size(value);
    }

    /// Renders the `sizes` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute sizes(String value) {
      return $html().sizes(value);
    }

    /// Renders the `spellcheck` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute spellcheck(String value) {
      return $html().spellcheck(value);
    }

    /// Renders the `src` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute src(String value) {
      return $html().src(value);
    }

    /// Renders the `srcset` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute srcset(String value) {
      return $html().srcset(value);
    }

    /// Renders the `start` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute start(String value) {
      return $html().start(value);
    }

    /// Renders the `stop-color` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute stopColor(String value) {
      return $html().stopColor(value);
    }

    /// Renders the `stop-opacity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute stopOpacity(String value) {
      return $html().stopOpacity(value);
    }

    /// Renders the `stroke` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute stroke(String value) {
      return $html().stroke(value);
    }

    /// Renders the `stroke-dasharray` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeDasharray(String value) {
      return $html().strokeDasharray(value);
    }

    /// Renders the `stroke-dashoffset` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeDashoffset(String value) {
      return $html().strokeDashoffset(value);
    }

    /// Renders the `stroke-linecap` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeLinecap(String value) {
      return $html().strokeLinecap(value);
    }

    /// Renders the `stroke-linejoin` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeLinejoin(String value) {
      return $html().strokeLinejoin(value);
    }

    /// Renders the `stroke-miterlimit` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeMiterlimit(String value) {
      return $html().strokeMiterlimit(value);
    }

    /// Renders the `stroke-opacity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeOpacity(String value) {
      return $html().strokeOpacity(value);
    }

    /// Renders the `stroke-width` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeWidth(String value) {
      return $html().strokeWidth(value);
    }

    /// Renders the `tabindex` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute tabindex(String value) {
      return $html().tabindex(value);
    }

    /// Renders the `target` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute target(String value) {
      return $html().target(value);
    }

    /// Renders the `text-anchor` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute textAnchor(String value) {
      return $html().textAnchor(value);
    }

    /// Renders the `text-decoration` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute textDecoration(String value) {
      return $html().textDecoration(value);
    }

    /// Renders the `text-overflow` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute textOverflow(String value) {
      return $html().textOverflow(value);
    }

    /// Renders the `text-rendering` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute textRendering(String value) {
      return $html().textRendering(value);
    }

    /// Renders the `transform` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute transform(String value) {
      return $html().transform(value);
    }

    /// Renders the `transform-origin` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute transformOrigin(String value) {
      return $html().transformOrigin(value);
    }

    /// Renders the `translate` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute translate(String value) {
      return $html().translate(value);
    }

    /// Renders the `type` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute type(String value) {
      return $html().type(value);
    }

    /// Renders the `unicode-bidi` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute unicodeBidi(String value) {
      return $html().unicodeBidi(value);
    }

    /// Renders the `value` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute value(String value) {
      return $html().value(value);
    }

    /// Renders the `vector-effect` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute vectorEffect(String value) {
      return $html().vectorEffect(value);
    }

    /// Renders the `viewBox` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute viewBox(String value) {
      return $html().viewBox(value);
    }

    /// Renders the `visibility` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute visibility(String value) {
      return $html().visibility(value);
    }

    /// Renders the `white-space` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute whiteSpace(String value) {
      return $html().whiteSpace(value);
    }

    /// Renders the `width` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute width(String value) {
      return $html().width(value);
    }

    /// Renders the `word-spacing` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute wordSpacing(String value) {
      return $html().wordSpacing(value);
    }

    /// Renders the `wrap` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute wrap(String value) {
      return $html().wrap(value);
    }

    /// Renders the `writing-mode` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute writingMode(String value) {
      return $html().writingMode(value);
    }

    /// Renders the `xmlns` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute xmlns(String value) {
      return $html().xmlns(value);
    }

    //
    // AMBIGUOUS
    //

    /// Renders the `clip-path` attribute or the `clipPath` element with the
    /// specified text.
    ///
    /// @param text the attribute value or the text content of the element
    ///
    /// @return an instruction representing the attribute or the element
    protected final Html.Instruction.OfAmbiguous clipPath(String text) {
      return $html().clipPath(text);
    }

    /// Renders the `form` attribute or the `form` element with the specified
    /// text.
    ///
    /// @param text the attribute value or the text content of the element
    ///
    /// @return an instruction representing the attribute or the element
    protected final Html.Instruction.OfAmbiguous form(String text) {
      return $html().form(text);
    }

    /// Renders the `label` attribute or the `label` element with the specified
    /// text.
    ///
    /// @param text the attribute value or the text content of the element
    ///
    /// @return an instruction representing the attribute or the element
    protected final Html.Instruction.OfAmbiguous label(String text) {
      return $html().label(text);
    }

    /// Renders the `style` attribute or the `style` element with the specified
    /// text.
    ///
    /// @param text the attribute value or the text content of the element
    ///
    /// @return an instruction representing the attribute or the element
    protected final Html.Instruction.OfAmbiguous style(String text) {
      return $html().style(text);
    }

    /// Renders the `title` attribute or the `title` element with the specified
    /// text.
    ///
    /// @param text the attribute value or the text content of the element
    ///
    /// @return an instruction representing the attribute or the element
    protected final Html.Instruction.OfAmbiguous title(String text) {
      return $html().title(text);
    }

    //
    // TEXT
    //

    /// Renders the non-breaking space `&nbsp;` HTML character entity.
    ///
    /// @return an instruction representing the non-breaking space
    ///         character entity.
    protected final Html.Instruction.OfElement nbsp() {
      return $html().nbsp();
    }

    /// Renders the specified value as raw HTML.
    ///
    /// @param value the raw HTML value
    ///
    /// @return a raw HTML instruction
    protected final Html.Instruction.OfElement raw(String value) {
      return $html().raw(value);
    }

    /// Renders a text node with the specified value. The text value is escaped
    /// before being emitted to the output.
    ///
    /// @param value the text value
    ///
    /// @return an instruction representing the text node
    protected final Html.Instruction.OfElement text(String value) {
      return $html().text(value);
    }

    // END generated code

  }

  private Html() {}

  /// Formats the specified string to be used as an HTML attribute value. More
  /// specifically, this method returns a string whose value is the specified
  /// string:
  ///
  /// - With all leading and trailing [white space][Character#isWhitespace(char)]
  ///   removed. - All other [white space][Character#isWhitespace(char)]
  ///   characters are replaced by the space character (`U+0020`). - All
  ///   sequences of more than one consecutive space characters are normalized to
  ///   a single space character.
  ///
  /// @param value the string to be formatted
  ///
  /// @return the formatted string
  public static String formatAttrValue(String value) {
    Objects.requireNonNull(value, "value == null");

    return AttributeNamePojo.formatAttrValue(value, new StringBuilder());
  }

}
