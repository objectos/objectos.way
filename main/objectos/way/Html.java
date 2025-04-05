/*
 * Copyright (C) 2015-2025 Objectos Software LTDA.
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

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * The <strong>Objectos HTML</strong> main class.
 */
public final class Html {

  /**
   * The name of an HTML attribute.
   */
  public sealed interface AttributeName extends HtmlAttributeNameGenerated permits HtmlAttributeName {

    static AttributeName of(String name) {
      return HtmlAttributeName.custom(name);
    }

    /**
     * Index of this attribute.
     *
     * @return index of this attribute.
     */
    int index();

    /**
     * Name of the attribute.
     *
     * @return name of the attribute
     */
    String name();

    /**
     * Indicates if this is the name of a boolean atttribute.
     *
     * @return {@code true} if this is the name of a boolean atttribute and
     *         {@code false} otherwise
     */
    boolean booleanAttribute();

    /**
     * Indicates if the value of this attribute must be formatted inside single
     * quotes.
     *
     * @return {@code true} if the value of this attribute must be formatted
     *         inside single quotes.
     */
    boolean singleQuoted();

  }

  /**
   * Represents an HTML {@code class} attribute and its value.
   */
  public sealed interface ClassName extends AttributeObject permits HtmlClassName {

    /**
     * Creates a new {@code ClassName} instance whose value is the result of
     * joining the value of each of the specified {@code ClassName} instances
     * around the space character.
     *
     * @param values
     *        the {@code ClassName} instances to be joined into a single value
     *
     * @return a newly constructed {@code ClassName} instance
     */
    static ClassName of(ClassName... values) {
      StringBuilder sb;
      sb = new StringBuilder();

      for (int i = 0, len = values.length; i < len; i++) {
        if (i != 0) {
          sb.append(' ');
        }

        ClassName cn;
        cn = values[i];

        String value;
        value = cn.value();

        sb.append(value);
      }

      String value;
      value = sb.toString();

      return new HtmlClassName(value);
    }

    /**
     * Creates a new {@code ClassName} instance with the specified value.
     *
     * @param value
     *        the value of this HTML {@code class} attribute
     *
     * @return a newly constructed {@code ClassName} instance
     */
    static ClassName of(String value) {
      Objects.requireNonNull(value, "value == null");

      return new HtmlClassName(value);
    }

    /**
     * Creates a new {@code ClassName} instance by processing the specified
     * value.
     *
     * <p>
     * This method is designed to work with Java text blocks. It first removes
     * any leading and trailing whitespace. Additionally, any sequence of
     * consecutive whitespace characters is replaced by a single space
     * character.
     *
     * <p>
     * For example, creating an instance like the following:
     *
     * <pre>{@code
     * Html.ClassName.ofText("""
     *     first \tsecond
     *       third\r
     *
     *     fourth
     *     """);
     * }</pre>
     *
     * <p>
     * Produces the same result as creating an instance with the
     * {@code "first second third fourth"} string literal.
     *
     * @param value
     *        the text block containing class names, possibly spread across
     *        multiple lines
     *
     * @return a newly constructed {@code ClassName} instance
     */
    static Html.ClassName ofText(String value) {
      String result;
      result = Html.ofText(value);

      return new HtmlClassName(result);
    }

    /**
     * The {@code class} attribute name.
     *
     * @return the {@code class} attribute name
     */
    @Override
    default AttributeName name() {
      return HtmlAttributeName.CLASS;
    }

    /**
     * The {@code class} value.
     *
     * @return the {@code class} value
     */
    @Override
    String value();

  }

  /**
   * A DOM-like view of a {@code Html.Template} which allows for a one-pass,
   * one-direction traversal of the HTML document.
   */
  public sealed interface Dom permits HtmlDom {

    /**
     * An attribute of a {@code Html.Template}.
     */
    sealed interface Attribute {

      /**
       * Returns the name of this attribute.
       *
       * @return the name of this attribute.
       */
      String name();

      /**
       * Returns {@code true} if this is a boolean attribute or {@code false}
       * otherwise.
       *
       * @return {@code true} if this is a boolean attribute or {@code false}
       *         otherwise
       */
      boolean booleanAttribute();

      /**
       * Returns {@code true} if the value of this attribute must be enclosed in
       * single quotes and {@code false} otherwise.
       *
       * @return {@code true} if the value of this attribute must be enclosed in
       *         single quotes and {@code false} otherwise
       */
      boolean singleQuoted();

      /**
       * Returns the value of this attribute.
       *
       * @return the value of this attribute.
       */
      String value();

      /**
       * Tests if the name of this attribute is equal to the specified value.
       *
       * @param value
       *        the value to compare against the name of this attribute
       *
       * @return {@code true} if the name of this attribute is equal to the
       *         specified value or {@code false} otherwise
       */
      default boolean hasName(String value) {
        return name().equals(value);
      }

    }

    /**
     * The document type of a {@code Html.Template}.
     */
    sealed interface DocumentType extends Node {}

    /**
     * An element of a {@code Html.Template}.
     */
    sealed interface Element extends Node {

      /**
       * Returns the attributes of this element.
       *
       * @return the attributes of this element.
       */
      Lang.IterableOnce<Dom.Attribute> attributes();

      /**
       * Returns {@code true} if this is a void element or {@code false}
       * otherwise.
       *
       * @return {@code true} if this is a void element or {@code false}
       *         otherwise
       */
      boolean isVoid();

      /**
       * Returns the name of this element.
       *
       * @return the name of this element
       */
      String name();

      /**
       * Return the child nodes of this element.
       *
       * @return the child nodes of this element
       */
      Lang.IterableOnce<Node> nodes();

      /**
       * Tests if the name of this element is equal to the specified value.
       *
       * @param value
       *        the value to compare against the name of this element
       *
       * @return {@code true} if the name of this element is equal to the
       *         specified value or {@code false} otherwise
       */
      default boolean hasName(String value) {
        return name().equals(value);
      }

    }

    /**
     * A node of a {@code Html.Template}.
     */
    sealed interface Node {}

    /**
     * A raw HTML node of a {@code Html.Template}.
     */
    sealed interface Raw extends Node {

      /**
       * Return the value of this raw HTML node.
       *
       * @return the value of this raw HTML node.
       */
      String value();

    }

    /**
     * A text node of a {@code Html.Template}.
     */
    sealed interface Text extends Node {

      /**
       * Return the value of this text node.
       *
       * @return the value of this text node.
       */
      String value();

    }

    /**
     * Create a DOM representing the specified HTML template.
     *
     * @param template
     *        the HTML template
     *
     * @return a newly created DOM object
     */
    static Dom create(Html.Template template) {
      HtmlMarkup html;
      html = new HtmlMarkup();

      template.renderHtml(html);

      return html.compile();
    }

    /**
     * Returns the nodes of this DOM. The returned {@code Iterable} can be
     * traversed only once.
     *
     * @return the nodes of this DOM.
     */
    Lang.IterableOnce<Node> nodes();

  }

  /**
   * The name of an HTML element.
   */
  public sealed interface ElementName extends HtmlElementNameGenerated permits HtmlElementName {

    /**
     * Index of this element name.
     *
     * @return index of this element name.
     */
    int index();

    /**
     * Name of the element.
     *
     * @return name of the element
     */
    String name();

    /**
     * Indicates if this is the name of an element that has an end tag.
     *
     * @return {@code true} if this is the name of an element that has an end
     *         tag and {@code false} otherwise
     */
    boolean endTag();

  }

  /**
   * A delayed set of template instructions.
   *
   * <p>
   * The set of instructions MUST be of the same template instance where this
   * fragment will be included.
   */
  public sealed interface Fragment {

    /**
     * A fragment that takes no arguments.
     */
    @FunctionalInterface
    non-sealed interface Of0 extends Fragment {

      /**
       * Invokes this set of instructions.
       */
      void invoke();

    }

    /**
     * A fragment that takes one argument.
     *
     * @param <T1>
     *        the type of the argument
     */
    @FunctionalInterface
    non-sealed interface Of1<T1> extends Fragment {

      /**
       * Invokes this set of instructions.
       */
      void invoke(T1 arg1);

    }

    /**
     * A fragment that takes two arguments.
     *
     * @param <T1>
     *        the type of the first argument
     * @param <T2>
     *        the type of the second argument
     */
    @FunctionalInterface
    non-sealed interface Of2<T1, T2> extends Fragment {

      /**
       * Invokes this set of instructions.
       */
      void invoke(T1 arg1, T2 arg2);

    }

    /**
     * A fragment that takes three arguments.
     *
     * @param <T1>
     *        the type of the first argument
     * @param <T2>
     *        the type of the second argument
     * @param <T3>
     *        the type of the third argument
     */
    @FunctionalInterface
    non-sealed interface Of3<T1, T2, T3> extends Fragment {

      /**
       * Invokes this set of instructions.
       */
      void invoke(T1 arg1, T2 arg2, T3 arg3);

    }

    /**
     * A fragment that takes four arguments.
     *
     * @param <T1>
     *        the type of the first argument
     * @param <T2>
     *        the type of the second argument
     * @param <T3>
     *        the type of the third argument
     * @param <T4>
     *        the type of the fourth argument
     */
    @FunctionalInterface
    non-sealed interface Of4<T1, T2, T3, T4> extends Fragment {

      /**
       * Invokes this set of instructions.
       */
      void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4);

    }

  }

  /**
   * Represents an HTML {@code id} attribute and its value.
   */
  public sealed interface Id extends AttributeObject {

    /**
     * Creates a new {@code Id} instance with the specified value.
     *
     * @param value
     *        the value of this HTML {@code id} attribute.
     *
     * @return a newly constructed {@code Id} instance
     */
    static Id of(String value) {
      Check.notNull(value, "value == null");

      return new HtmlId(value);
    }

    /**
     * The {@code id} attribute name.
     *
     * @return the {@code id} attribute name
     */
    @Override
    default AttributeName name() {
      return HtmlAttributeName.ID;
    }

    /**
     * The {@code id} value.
     *
     * @return the {@code id} value
     */
    @Override
    String value();

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
    sealed interface AsObject extends Instruction {}

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
    sealed interface OfAmbiguous extends OfAttribute, OfElement {}

    /**
     * An instruction to generate an HTML attribute in template.
     */
    sealed interface OfAttribute extends AsMethod, OfVoid {}

    /**
     * An instruction to generate a {@code data-on-*} HTML attribute in a
     * template.
     */
    sealed interface OfDataOn extends AsMethod, OfVoid {}

    /**
     * An instruction to generate an HTML element in a template.
     */
    sealed interface OfElement extends AsMethod {}

    /**
     * An instruction to include an HTML fragment to a template.
     */
    sealed interface OfFragment extends AsMethod, OfVoid {}

    /**
     * Class of instructions that are allowed as arguments to template
     * methods that represent void elements.
     */
    sealed interface OfVoid extends Instruction {}

    /**
     * The no-op instruction.
     */
    sealed interface NoOp extends AsMethod, OfVoid {

      static NoOp get() {
        return Html.NOOP;
      }

    }

  }

  sealed interface AttributeOrNoOp extends Instruction.OfAttribute, Instruction.OfDataOn, Instruction.NoOp {}

  private static final class HtmlInstruction
      implements
      AttributeOrNoOp,
      Html.Instruction.OfAmbiguous,
      Html.Instruction.OfElement,
      Html.Instruction.OfFragment {}

  static final Html.AttributeOrNoOp ATTRIBUTE = new HtmlInstruction();
  static final HtmlInstruction ELEMENT = new HtmlInstruction();
  static final Html.Instruction.OfFragment FRAGMENT = new HtmlInstruction();
  static final Html.AttributeOrNoOp NOOP = new HtmlInstruction();

  /*
   * Template related classes
   */

  /**
   * An object capable of defining the structure of an HTML document.
   */
  @FunctionalInterface
  public interface Component extends Lang.Media {

    void renderHtml(Html.Markup m);

    /**
     * Returns {@code text/html; charset=utf-8}.
     *
     * @return {@code text/html; charset=utf-8}
     */
    @Override
    default String contentType() {
      return "text/html; charset=utf-8";
    }

    @Override
    default byte[] toByteArray() {
      String html;
      html = toHtml();

      return html.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Returns the HTML generated by this component.
     *
     * @return the HTML generated by this component
     */
    default String toHtml() {
      HtmlMarkup html;
      html = new HtmlMarkup();

      html.compilationBegin();

      renderHtml(html);

      html.compilationEnd();

      return html.toString();
    }

  }

  /**
   * Allow for defining the structure of an HTML document using pure Java.
   */
  public sealed interface Markup extends MarkupAttributes, MarkupElements, MarkupTestable permits HtmlMarkup, HtmlMarkupTestable {

    /**
     * Creates a new {@code Markup} instance.
     *
     * @return a newly created {@code Markup} instance
     */
    static Markup create() {
      return new HtmlMarkup();
    }

    /**
     * Renders the {@code class} attribute by processing the specified value.
     *
     * <p>
     * This method is designed to work with Java text blocks. It first removes
     * any leading and trailing whitespace. Additionally, any sequence of
     * consecutive whitespace characters is replaced by a single space
     * character.
     *
     * <p>
     * For example, the following invocation:
     *
     * <pre>{@code
     * css("""
     *     display:inline-flex
     *     justify-content:center
     *
     *     background-color:blue-500
     *     """);
     * }</pre>
     *
     * <p>
     * Produces the same result as invoking
     * {@code className("display:inline-flex justify-content:center background-color:blue-500")}.
     *
     * @param value
     *        the text block containing class names, possibly spread across
     *        multiple lines
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute css(String value);

    Html.Instruction.OfAttribute dataFrame(String name);

    Html.Instruction.OfAttribute dataFrame(String name, String value);

    Html.Instruction.OfDataOn dataOnClick(Consumer<Script> script);

    Html.Instruction.OfDataOn dataOnInput(Consumer<Script> script);

    Html.Instruction.OfDataOn dataOnSuccess(Consumer<Script> script);

    Html.Instruction.OfElement element(Html.ElementName name, Html.Instruction... contents);

    /**
     * Flattens the specified instructions so that each of the specified
     * instructions is individually added, in order, to a receiving element.
     *
     * @param contents
     *        the instructions to be flattened
     *
     * @return an instruction representing this flatten operation
     */
    Html.Instruction.OfElement flatten(Html.Instruction... contents);

    /**
     * The non-breaking space {@code &nbsp;} HTML character entity.
     *
     * @return an instruction representing the non-breaking space character
     *         entity.
     */
    default Html.Instruction.OfElement nbsp() {
      return raw("&nbsp;");
    }

    /**
     * The no-op instruction.
     *
     * @return the no-op instruction.
     */
    Html.Instruction.NoOp noop();

    /**
     * Renders the specified value as raw HTML.
     *
     * @param value
     *        the raw HTML value
     *
     * @return a raw HTML instruction
     */
    Html.Instruction.OfElement raw(String value);

    /**
     * Renders the specified fragment as part of this template.
     *
     * <p>
     * The included fragment MUST only invoke methods of this template instance.
     * It is common (but not required) for a fragment to be a method reference
     * to a private method of the template instance.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/way/HtmlTemplateBaseJavadoc.java" region =
     * "renderFragment0"}
     *
     * <p>
     * Generates the following HTML:
     *
     * <pre>{@code
     *     <!DOCTYPE html>
     *     <html>
     *     <head>
     *     <title>Include fragment example</title>
     *     </head>
     *     <body>
     *     <h1>Objectos HTML</h1>
     *     <p>Using the include instruction</p>
     *     </body>
     *     </html>
     * }</pre>
     *
     * <p>
     * Note that the methods of included method references all return
     * {@code void}.
     *
     * @param fragment
     *        the fragment to include
     *
     * @return an instruction representing this fragment
     */
    Html.Instruction.OfFragment renderFragment(Html.Fragment.Of0 fragment);

    /**
     * Renders the specified fragment as part of this template.
     *
     * <p>
     * The included fragment MUST only invoke methods of this template instance.
     * It is common (but not required) for a fragment to be a method reference
     * to a private method of the template instance.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/way/HtmlTemplateBaseJavadoc.java" region =
     * "renderFragment1"}
     *
     * <p>
     * Generates the following HTML:
     *
     * <pre>{@code
     *     <body>
     *     <div>Foo</div>
     *     <div>Bar</div>
     *     </body>
     * }</pre>
     *
     * <p>
     * Note that the methods of included method references all return
     * {@code void}.
     *
     * @param <T1> the type of the first argument
     *
     * @param fragment
     *        the fragment to include
     * @param arg1
     *        the first argument
     *
     * @return an instruction representing this fragment
     */
    <T1> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of1<T1> fragment, T1 arg1);

    /**
     * Renders the specified fragment as part of this template.
     *
     * <p>
     * The included fragment MUST only invoke methods of this template instance.
     * It is common (but not required) for a fragment to be a method reference
     * to a private method of the template instance.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/way/HtmlTemplateBaseJavadoc.java" region =
     * "renderFragment2"}
     *
     * <p>
     * Generates the following HTML:
     *
     * <pre>{@code
     *     <body>
     *     <p>City<span>Tokyo</span></p>
     *     <p>Country<span>Japan</span></p>
     *     </body>
     * }</pre>
     *
     * <p>
     * Note that the methods of included method references all return
     * {@code void}.
     *
     * @param <T1> the type of the first argument
     * @param <T2> the type of the second argument
     *
     * @param fragment
     *        the fragment to include
     * @param arg1
     *        the first argument
     * @param arg2
     *        the second argument
     *
     * @return an instruction representing this fragment
     */
    <T1, T2> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of2<T1, T2> fragment, T1 arg1, T2 arg2);

    <T1, T2, T3> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3);

    <T1, T2, T3, T4> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4);

    /**
     * Renders the specified component as part of this HTML instance.
     *
     * @param component
     *        the component to be rendered as part of this HTML instance
     *
     * @return an instruction representing the rendered component.
     */
    Html.Instruction.OfFragment renderComponent(Html.Component component);

    /**
     * Renders a text node with the specified {@code text} value. The text
     * value is escaped before being emitted to the output.
     *
     * @param text
     *        the text value to be added
     *
     * @return an instruction representing the text node
     */
    Html.Instruction.OfElement text(String text);

  }

  /**
   * Provides methods for defining the attributes of an HTML document.
   */
  public sealed interface MarkupAttributes permits Markup, HtmlMarkupAttributes {

    /**
     * Renders an attribute with the specified name and value.
     *
     * @param name
     *        the name of the attribute
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute attr(Html.AttributeName name, String value);

    /**
     * Renders the {@code accesskey} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute accesskey(String value);

    /**
     * Renders the {@code action} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute action(String value);

    /**
     * Renders the {@code align} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute align(String value);

    /**
     * Renders the {@code alignment-baseline} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute alignmentBaseline(String value);

    /**
     * Renders the {@code alt} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute alt(String value);

    /**
     * Renders the {@code aria-hidden} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute ariaHidden(String value);

    /**
     * Renders the {@code aria-label} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute ariaLabel(String value);

    /**
     * Renders the {@code as} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute as(String value);

    /**
     * Renders the {@code async} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute async();

    /**
     * Renders the {@code autocomplete} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute autocomplete(String value);

    /**
     * Renders the {@code autofocus} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute autofocus();

    /**
     * Renders the {@code baseline-shift} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute baselineShift(String value);

    /**
     * Renders the {@code border} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute border(String value);

    /**
     * Renders the {@code cellpadding} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute cellpadding(String value);

    /**
     * Renders the {@code cellspacing} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute cellspacing(String value);

    /**
     * Renders the {@code charset} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute charset(String value);

    /**
     * Renders the {@code checked} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute checked();

    /**
     * Renders the {@code cite} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute cite(String value);

    /**
     * Renders the {@code class} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute className(String value);

    /**
     * Renders the {@code clip-rule} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute clipRule(String value);

    /**
     * Renders the {@code color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute color(String value);

    /**
     * Renders the {@code color-interpolation} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute colorInterpolation(String value);

    /**
     * Renders the {@code color-interpolation-filters} attribute with the
     * specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute colorInterpolationFilters(String value);

    /**
     * Renders the {@code cols} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute cols(String value);

    /**
     * Renders the {@code content} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute content(String value);

    /**
     * Renders the {@code contenteditable} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute contenteditable(String value);

    /**
     * Renders the {@code crossorigin} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute crossorigin(String value);

    /**
     * Renders the {@code cursor} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute cursor(String value);

    /**
     * Renders the {@code d} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute d(String value);

    /**
     * Renders the {@code defer} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute defer();

    /**
     * Renders the {@code dir} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute dir(String value);

    /**
     * Renders the {@code direction} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute direction(String value);

    /**
     * Renders the {@code dirname} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute dirname(String value);

    /**
     * Renders the {@code disabled} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute disabled();

    /**
     * Renders the {@code display} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute display(String value);

    /**
     * Renders the {@code dominant-baseline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute dominantBaseline(String value);

    /**
     * Renders the {@code draggable} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute draggable(String value);

    /**
     * Renders the {@code enctype} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute enctype(String value);

    /**
     * Renders the {@code fill} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute fill(String value);

    /**
     * Renders the {@code fill-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute fillOpacity(String value);

    /**
     * Renders the {@code fill-rule} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute fillRule(String value);

    /**
     * Renders the {@code filter} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute filter(String value);

    /**
     * Renders the {@code flood-color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute floodColor(String value);

    /**
     * Renders the {@code flood-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute floodOpacity(String value);

    /**
     * Renders the {@code for} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute forAttr(String value);

    /**
     * Renders the {@code for} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute forElement(String value);

    /**
     * Renders the {@code glyph-orientation-horizontal} attribute with the
     * specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute glyphOrientationHorizontal(String value);

    /**
     * Renders the {@code glyph-orientation-vertical} attribute with the
     * specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute glyphOrientationVertical(String value);

    /**
     * Renders the {@code height} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute height(String value);

    /**
     * Renders the {@code hidden} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute hidden();

    /**
     * Renders the {@code href} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute href(String value);

    /**
     * Renders the {@code http-equiv} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute httpEquiv(String value);

    /**
     * Renders the {@code id} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute id(String value);

    /**
     * Renders the {@code image-rendering} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute imageRendering(String value);

    /**
     * Renders the {@code integrity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute integrity(String value);

    /**
     * Renders the {@code lang} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute lang(String value);

    /**
     * Renders the {@code letter-spacing} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute letterSpacing(String value);

    /**
     * Renders the {@code lighting-color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute lightingColor(String value);

    /**
     * Renders the {@code marker-end} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute markerEnd(String value);

    /**
     * Renders the {@code marker-mid} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute markerMid(String value);

    /**
     * Renders the {@code marker-start} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute markerStart(String value);

    /**
     * Renders the {@code mask} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute mask(String value);

    /**
     * Renders the {@code mask-type} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute maskType(String value);

    /**
     * Renders the {@code maxlength} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute maxlength(String value);

    /**
     * Renders the {@code media} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute media(String value);

    /**
     * Renders the {@code method} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute method(String value);

    /**
     * Renders the {@code minlength} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute minlength(String value);

    /**
     * Renders the {@code multiple} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute multiple();

    /**
     * Renders the {@code name} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute name(String value);

    /**
     * Renders the {@code nomodule} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute nomodule();

    /**
     * Renders the {@code onafterprint} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onafterprint(String value);

    /**
     * Renders the {@code onbeforeprint} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onbeforeprint(String value);

    /**
     * Renders the {@code onbeforeunload} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onbeforeunload(String value);

    /**
     * Renders the {@code onclick} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onclick(String value);

    /**
     * Renders the {@code onhashchange} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onhashchange(String value);

    /**
     * Renders the {@code onlanguagechange} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onlanguagechange(String value);

    /**
     * Renders the {@code onmessage} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onmessage(String value);

    /**
     * Renders the {@code onoffline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onoffline(String value);

    /**
     * Renders the {@code ononline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute ononline(String value);

    /**
     * Renders the {@code onpagehide} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onpagehide(String value);

    /**
     * Renders the {@code onpageshow} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onpageshow(String value);

    /**
     * Renders the {@code onpopstate} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onpopstate(String value);

    /**
     * Renders the {@code onrejectionhandled} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onrejectionhandled(String value);

    /**
     * Renders the {@code onstorage} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onstorage(String value);

    /**
     * Renders the {@code onsubmit} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onsubmit(String value);

    /**
     * Renders the {@code onunhandledrejection} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onunhandledrejection(String value);

    /**
     * Renders the {@code onunload} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute onunload(String value);

    /**
     * Renders the {@code opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute opacity(String value);

    /**
     * Renders the {@code open} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute open();

    /**
     * Renders the {@code overflow} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute overflow(String value);

    /**
     * Renders the {@code paint-order} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute paintOrder(String value);

    /**
     * Renders the {@code placeholder} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute placeholder(String value);

    /**
     * Renders the {@code pointer-events} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute pointerEvents(String value);

    /**
     * Renders the {@code property} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute property(String value);

    /**
     * Renders the {@code readonly} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute readonly();

    /**
     * Renders the {@code referrerpolicy} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute referrerpolicy(String value);

    /**
     * Renders the {@code rel} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute rel(String value);

    /**
     * Renders the {@code required} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute required();

    /**
     * Renders the {@code rev} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute rev(String value);

    /**
     * Renders the {@code reversed} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute reversed();

    /**
     * Renders the {@code role} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute role(String value);

    /**
     * Renders the {@code rows} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute rows(String value);

    /**
     * Renders the {@code selected} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute selected();

    /**
     * Renders the {@code shape-rendering} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute shapeRendering(String value);

    /**
     * Renders the {@code size} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute size(String value);

    /**
     * Renders the {@code sizes} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute sizes(String value);

    /**
     * Renders the {@code spellcheck} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute spellcheck(String value);

    /**
     * Renders the {@code src} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute src(String value);

    /**
     * Renders the {@code srcset} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute srcset(String value);

    /**
     * Renders the {@code start} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute start(String value);

    /**
     * Renders the {@code stop-color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute stopColor(String value);

    /**
     * Renders the {@code stop-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute stopOpacity(String value);

    /**
     * Renders the {@code stroke} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute stroke(String value);

    /**
     * Renders the {@code stroke-dasharray} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute strokeDasharray(String value);

    /**
     * Renders the {@code stroke-dashoffset} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute strokeDashoffset(String value);

    /**
     * Renders the {@code stroke-linecap} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute strokeLinecap(String value);

    /**
     * Renders the {@code stroke-linejoin} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute strokeLinejoin(String value);

    /**
     * Renders the {@code stroke-miterlimit} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute strokeMiterlimit(String value);

    /**
     * Renders the {@code stroke-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute strokeOpacity(String value);

    /**
     * Renders the {@code stroke-width} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute strokeWidth(String value);

    /**
     * Renders the {@code style} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute inlineStyle(String value);

    /**
     * Renders the {@code tabindex} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute tabindex(String value);

    /**
     * Renders the {@code target} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute target(String value);

    /**
     * Renders the {@code text-anchor} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute textAnchor(String value);

    /**
     * Renders the {@code text-decoration} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute textDecoration(String value);

    /**
     * Renders the {@code text-overflow} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute textOverflow(String value);

    /**
     * Renders the {@code text-rendering} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute textRendering(String value);

    /**
     * Renders the {@code transform} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute transform(String value);

    /**
     * Renders the {@code transform-origin} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute transformOrigin(String value);

    /**
     * Renders the {@code translate} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute translate(String value);

    /**
     * Renders the {@code type} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute type(String value);

    /**
     * Renders the {@code unicode-bidi} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute unicodeBidi(String value);

    /**
     * Renders the {@code value} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute value(String value);

    /**
     * Renders the {@code vector-effect} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute vectorEffect(String value);

    /**
     * Renders the {@code viewBox} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute viewBox(String value);

    /**
     * Renders the {@code visibility} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute visibility(String value);

    /**
     * Renders the {@code white-space} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute whiteSpace(String value);

    /**
     * Renders the {@code width} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute width(String value);

    /**
     * Renders the {@code word-spacing} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute wordSpacing(String value);

    /**
     * Renders the {@code wrap} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute wrap(String value);

    /**
     * Renders the {@code writing-mode} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute writingMode(String value);

    /**
     * Renders the {@code xmlns} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    Html.Instruction.OfAttribute xmlns(String value);

  }

  /**
   * Provides methods for defining the elements of an HTML document.
   */
  public sealed interface MarkupElements permits Markup, HtmlMarkupElements {

    /**
     * Renders the {@code <!DOCTYPE html>} doctype.
     */
    void doctype();

    /**
     * Renders the {@code a} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement a(Html.Instruction... contents);

    /**
     * Renders the {@code a} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement a(String text);

    /**
     * Renders the {@code abbr} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement abbr(Html.Instruction... contents);

    /**
     * Renders the {@code abbr} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement abbr(String text);

    /**
     * Renders the {@code article} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement article(Html.Instruction... contents);

    /**
     * Renders the {@code article} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement article(String text);

    /**
     * Renders the {@code aside} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement aside(Html.Instruction... contents);

    /**
     * Renders the {@code aside} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement aside(String text);

    /**
     * Renders the {@code b} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement b(Html.Instruction... contents);

    /**
     * Renders the {@code b} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement b(String text);

    /**
     * Renders the {@code blockquote} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement blockquote(Html.Instruction... contents);

    /**
     * Renders the {@code blockquote} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement blockquote(String text);

    /**
     * Renders the {@code body} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement body(Html.Instruction... contents);

    /**
     * Renders the {@code body} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement body(String text);

    /**
     * Renders the {@code br} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents);

    /**
     * Renders the {@code button} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement button(Html.Instruction... contents);

    /**
     * Renders the {@code button} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement button(String text);

    /**
     * Renders the {@code clipPath} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement clipPath(Html.Instruction... contents);

    /**
     * Renders the {@code clipPath} attribute or element with the specified
     * text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    Html.Instruction.OfAmbiguous clipPath(String text);

    /**
     * Renders the {@code code} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement code(Html.Instruction... contents);

    /**
     * Renders the {@code code} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement code(String text);

    /**
     * Renders the {@code dd} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement dd(Html.Instruction... contents);

    /**
     * Renders the {@code dd} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement dd(String text);

    /**
     * Renders the {@code defs} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement defs(Html.Instruction... contents);

    /**
     * Renders the {@code defs} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement defs(String text);

    /**
     * Renders the {@code details} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement details(Html.Instruction... contents);

    /**
     * Renders the {@code details} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement details(String text);

    /**
     * Renders the {@code div} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement div(Html.Instruction... contents);

    /**
     * Renders the {@code div} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement div(String text);

    /**
     * Renders the {@code dl} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement dl(Html.Instruction... contents);

    /**
     * Renders the {@code dl} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement dl(String text);

    /**
     * Renders the {@code dt} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement dt(Html.Instruction... contents);

    /**
     * Renders the {@code dt} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement dt(String text);

    /**
     * Renders the {@code em} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement em(Html.Instruction... contents);

    /**
     * Renders the {@code em} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement em(String text);

    /**
     * Renders the {@code fieldset} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement fieldset(Html.Instruction... contents);

    /**
     * Renders the {@code fieldset} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement fieldset(String text);

    /**
     * Renders the {@code figure} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement figure(Html.Instruction... contents);

    /**
     * Renders the {@code figure} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement figure(String text);

    /**
     * Renders the {@code footer} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement footer(Html.Instruction... contents);

    /**
     * Renders the {@code footer} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement footer(String text);

    /**
     * Renders the {@code form} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement form(Html.Instruction... contents);

    /**
     * Renders the {@code form} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    Html.Instruction.OfAmbiguous form(String text);

    /**
     * Renders the {@code g} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement g(Html.Instruction... contents);

    /**
     * Renders the {@code g} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement g(String text);

    /**
     * Renders the {@code h1} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h1(Html.Instruction... contents);

    /**
     * Renders the {@code h1} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h1(String text);

    /**
     * Renders the {@code h2} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h2(Html.Instruction... contents);

    /**
     * Renders the {@code h2} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h2(String text);

    /**
     * Renders the {@code h3} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h3(Html.Instruction... contents);

    /**
     * Renders the {@code h3} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h3(String text);

    /**
     * Renders the {@code h4} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h4(Html.Instruction... contents);

    /**
     * Renders the {@code h4} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h4(String text);

    /**
     * Renders the {@code h5} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h5(Html.Instruction... contents);

    /**
     * Renders the {@code h5} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h5(String text);

    /**
     * Renders the {@code h6} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h6(Html.Instruction... contents);

    /**
     * Renders the {@code h6} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement h6(String text);

    /**
     * Renders the {@code head} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement head(Html.Instruction... contents);

    /**
     * Renders the {@code head} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement head(String text);

    /**
     * Renders the {@code header} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement header(Html.Instruction... contents);

    /**
     * Renders the {@code header} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement header(String text);

    /**
     * Renders the {@code hgroup} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement hgroup(Html.Instruction... contents);

    /**
     * Renders the {@code hgroup} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement hgroup(String text);

    /**
     * Renders the {@code hr} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents);

    /**
     * Renders the {@code html} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement html(Html.Instruction... contents);

    /**
     * Renders the {@code html} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement html(String text);

    /**
     * Renders the {@code img} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents);

    /**
     * Renders the {@code input} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents);

    /**
     * Renders the {@code kbd} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement kbd(Html.Instruction... contents);

    /**
     * Renders the {@code kbd} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement kbd(String text);

    /**
     * Renders the {@code label} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement label(Html.Instruction... contents);

    /**
     * Renders the {@code label} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    Html.Instruction.OfAmbiguous label(String text);

    /**
     * Renders the {@code legend} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement legend(Html.Instruction... contents);

    /**
     * Renders the {@code legend} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement legend(String text);

    /**
     * Renders the {@code li} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement li(Html.Instruction... contents);

    /**
     * Renders the {@code li} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement li(String text);

    /**
     * Renders the {@code link} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents);

    /**
     * Renders the {@code main} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement main(Html.Instruction... contents);

    /**
     * Renders the {@code main} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement main(String text);

    /**
     * Renders the {@code menu} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement menu(Html.Instruction... contents);

    /**
     * Renders the {@code menu} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement menu(String text);

    /**
     * Renders the {@code meta} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents);

    /**
     * Renders the {@code nav} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement nav(Html.Instruction... contents);

    /**
     * Renders the {@code nav} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement nav(String text);

    /**
     * Renders the {@code ol} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement ol(Html.Instruction... contents);

    /**
     * Renders the {@code ol} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement ol(String text);

    /**
     * Renders the {@code optgroup} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement optgroup(Html.Instruction... contents);

    /**
     * Renders the {@code optgroup} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement optgroup(String text);

    /**
     * Renders the {@code option} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement option(Html.Instruction... contents);

    /**
     * Renders the {@code option} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement option(String text);

    /**
     * Renders the {@code p} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement p(Html.Instruction... contents);

    /**
     * Renders the {@code p} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement p(String text);

    /**
     * Renders the {@code path} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement path(Html.Instruction... contents);

    /**
     * Renders the {@code path} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement path(String text);

    /**
     * Renders the {@code pre} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement pre(Html.Instruction... contents);

    /**
     * Renders the {@code pre} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement pre(String text);

    /**
     * Renders the {@code progress} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement progress(Html.Instruction... contents);

    /**
     * Renders the {@code progress} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement progress(String text);

    /**
     * Renders the {@code samp} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement samp(Html.Instruction... contents);

    /**
     * Renders the {@code samp} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement samp(String text);

    /**
     * Renders the {@code script} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement script(Html.Instruction... contents);

    /**
     * Renders the {@code script} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement script(String text);

    /**
     * Renders the {@code section} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement section(Html.Instruction... contents);

    /**
     * Renders the {@code section} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement section(String text);

    /**
     * Renders the {@code select} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement select(Html.Instruction... contents);

    /**
     * Renders the {@code select} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement select(String text);

    /**
     * Renders the {@code small} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement small(Html.Instruction... contents);

    /**
     * Renders the {@code small} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement small(String text);

    /**
     * Renders the {@code span} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement span(Html.Instruction... contents);

    /**
     * Renders the {@code span} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement span(String text);

    /**
     * Renders the {@code strong} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement strong(Html.Instruction... contents);

    /**
     * Renders the {@code strong} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement strong(String text);

    /**
     * Renders the {@code style} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement style(Html.Instruction... contents);

    /**
     * Renders the {@code style} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement style(String text);

    /**
     * Renders the {@code sub} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement sub(Html.Instruction... contents);

    /**
     * Renders the {@code sub} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement sub(String text);

    /**
     * Renders the {@code summary} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement summary(Html.Instruction... contents);

    /**
     * Renders the {@code summary} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement summary(String text);

    /**
     * Renders the {@code sup} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement sup(Html.Instruction... contents);

    /**
     * Renders the {@code sup} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement sup(String text);

    /**
     * Renders the {@code svg} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement svg(Html.Instruction... contents);

    /**
     * Renders the {@code svg} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement svg(String text);

    /**
     * Renders the {@code table} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement table(Html.Instruction... contents);

    /**
     * Renders the {@code table} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement table(String text);

    /**
     * Renders the {@code tbody} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement tbody(Html.Instruction... contents);

    /**
     * Renders the {@code tbody} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement tbody(String text);

    /**
     * Renders the {@code td} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement td(Html.Instruction... contents);

    /**
     * Renders the {@code td} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement td(String text);

    /**
     * Renders the {@code template} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement template(Html.Instruction... contents);

    /**
     * Renders the {@code template} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement template(String text);

    /**
     * Renders the {@code textarea} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement textarea(Html.Instruction... contents);

    /**
     * Renders the {@code textarea} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement textarea(String text);

    /**
     * Renders the {@code th} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement th(Html.Instruction... contents);

    /**
     * Renders the {@code th} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement th(String text);

    /**
     * Renders the {@code thead} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement thead(Html.Instruction... contents);

    /**
     * Renders the {@code thead} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement thead(String text);

    /**
     * Renders the {@code title} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement title(Html.Instruction... contents);

    /**
     * Renders the {@code title} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    Html.Instruction.OfAmbiguous title(String text);

    /**
     * Renders the {@code tr} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement tr(Html.Instruction... contents);

    /**
     * Renders the {@code tr} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement tr(String text);

    /**
     * Renders the {@code ul} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement ul(Html.Instruction... contents);

    /**
     * Renders the {@code ul} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    Html.Instruction.OfElement ul(String text);

  }

  /**
   * Provides methods for formatting testable nodes during the rendering of an
   * HTML document.
   */
  public sealed interface MarkupTestable permits Markup {

    /**
     * Formats the specified value as a testable table cell with the specified
     * fixed width.
     *
     * @param value
     *        the cell value
     * @param width
     *        the fixed width of the cell
     *
     * @return always the cell value
     */
    String testableCell(String value, int width);

    /**
     * Formats the specified name and value as a testable field (optional
     * operation).
     *
     * @param name
     *        the field name
     * @param value
     *        the field value
     *
     * @return always the field value
     */
    String testableField(String name, String value);

    /**
     * Formats the specified name as a testable field name (optional operation).
     *
     * @param name
     *        the field name
     *
     * @return the specified field name
     */
    String testableFieldName(String name);

    /**
     * Formats the specified value as a testable field value (optional
     * operation).
     *
     * @param value
     *        the field value
     *
     * @return the specified field value
     */
    String testableFieldValue(String value);

    /**
     * Formats the specified value as a testable heading level 1 (optional
     * operation).
     *
     * @param value
     *        the heading value
     *
     * @return the specified value
     */
    String testableH1(String value);

    /**
     * Formats the specified value as a testable heading level 2 (optional
     * operation).
     *
     * @param value
     *        the heading value
     *
     * @return the specified value
     */
    String testableH2(String value);

    /**
     * Formats the specified value as a testable heading level 3 (optional
     * operation).
     *
     * @param value
     *        the heading value
     *
     * @return the specified value
     */
    String testableH3(String value);

    /**
     * Formats the specified value as a testable heading level 4 (optional
     * operation).
     *
     * @param value
     *        the heading value
     *
     * @return the specified value
     */
    String testableH4(String value);

    /**
     * Formats the specified value as a testable heading level 5 (optional
     * operation).
     *
     * @param value
     *        the heading value
     *
     * @return the specified value
     */
    String testableH5(String value);

    /**
     * Formats the specified value as a testable heading level 6 (optional
     * operation).
     *
     * @param value
     *        the heading value
     *
     * @return the specified value
     */
    String testableH6(String value);

    /**
     * Formats a line separator at the testable output exclusively.
     */
    Html.Instruction.NoOp testableNewLine();

  }

  /**
   * A template in pure Java for generating HTML.
   *
   * <p>
   * This class provides methods for representing HTML code in a Java class. An
   * instance of the class can then be used to generate the represented HTML
   * code.
   */
  public non-sealed static abstract class Template extends TemplateBase implements Component, Testable {

    Html.Markup html;

    /**
     * Sole constructor.
     */
    protected Template() {}

    /**
     * Returns {@code text/html; charset=utf-8}.
     *
     * @return always {@code text/html; charset=utf-8}
     */
    @Override
    public final String contentType() {
      return "text/html; charset=utf-8";
    }

    @Override
    public final void formatTestable(Testable.Formatter formatter) {
      final HtmlMarkupTestable html;
      html = new HtmlMarkupTestable(formatter);

      renderHtml(html);
    }

    @Override
    public final void renderHtml(Html.Markup m) {
      Check.state(html == null, "Concurrent evalution of a HtmlTemplate is not supported");

      try {
        html = m;

        render();
      } finally {
        html = null;
      }
    }

    /**
     * Returns the HTML generated by this template suited to be used in JSON.
     *
     * @return the HTML generated by this template suited to be used in JSON
     */
    public final String toJsonString() {
      HtmlMarkup html;
      html = new HtmlMarkup();

      html.compilationBegin();

      renderHtml(html);

      html.compilationEnd();

      return html.toJsonString();
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

    @Override
    final Html.Markup $html() {
      Check.state(html != null, "html not set");

      return html;
    }

  }

  public sealed static abstract class TemplateBase extends TemplateTestable permits Template {

    /**
     * The {@code data-execute-default} boolean attribute.
     */
    protected static final Html.AttributeObject dataExecuteDefault = Html.AttributeObject.create(HtmlAttributeName.DATA_EXECUTE_DEFAULT, "");

    TemplateBase() {}

    /**
     * Renders the {@code class} attribute by processing the specified value.
     *
     * <p>
     * This method is designed to work with Java text blocks. It first removes
     * any leading and trailing whitespace. Additionally, any sequence of
     * consecutive whitespace characters is replaced by a single space
     * character.
     *
     * <p>
     * For example, the following invocation:
     *
     * <pre>{@code
     * css("""
     *     display:inline-flex
     *     justify-content:center
     *
     *     background-color:blue-500
     *     """);
     * }</pre>
     *
     * <p>
     * Produces the same result as invoking
     * {@code className("display:inline-flex justify-content:center background-color:blue-500")}.
     *
     * @param value
     *        the text block containing class names, possibly spread across
     *        multiple lines
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute css(String value) {
      return $html().css(value);
    }

    protected final Instruction.OfAttribute dataFrame(String name) {
      return $html().dataFrame(name);
    }

    protected final Instruction.OfAttribute dataFrame(String name, String value) {
      return $html().dataFrame(name, value);
    }

    protected final Html.Instruction.OfDataOn dataOnClick(Consumer<Script> script) {
      return $html().dataOnClick(script);
    }

    protected final Html.Instruction.OfDataOn dataOnInput(Consumer<Script> script) {
      return $html().dataOnInput(script);
    }

    protected final Html.Instruction.OfDataOn dataOnSuccess(Consumer<Script> script) {
      return $html().dataOnSuccess(script);
    }

    protected final Html.Instruction.OfElement element(Html.ElementName name, Html.Instruction... contents) {
      return $html().element(name, contents);
    }

    /**
     * Flattens the specified instructions so that each of the specified
     * instructions is individually added, in order, to a receiving element.
     *
     * <p>
     * The following Objectos HTML code:
     *
     * {@snippet file = "objectos/way/HtmlTemplateBaseJavadoc.java" region =
     * "flatten"}
     *
     * <p>
     * Generates the following HTML:
     *
     * <pre>{@code
     *    <body>
     *    <div class="my-component">
     *    <h1>Flatten example</h1>
     *    <p>First paragraph</p>
     *    <p>Second paragraph</p>
     *    </div>
     *    </body>
     * }</pre>
     *
     * <p>
     * The {@code div} instruction is rendered as if it was invoked with four
     * distinct instructions:
     *
     * <ul>
     * <li>the {@code class} attribute;
     * <li>the {@code h1} element;
     * <li>the first {@code p} element; and
     * <li>the second {@code p} element.
     * </ul>
     *
     * @param contents
     *        the instructions to be flattened
     *
     * @return an instruction representing this flatten operation
     */
    protected final Html.Instruction.OfElement flatten(Instruction... contents) {
      return $html().flatten(contents);
    }

    /**
     * The non-breaking space {@code &nbsp;} HTML character entity.
     *
     * @return an instruction representing the non-breaking space character
     *         entity.
     */
    protected final Html.Instruction.OfElement nbsp() {
      return $html().nbsp();
    }

    /**
     * The no-op instruction.
     *
     * <p>
     * It can be used to conditionally add an attribute or element. For example,
     * the following Objectos HTML template:
     *
     * {@snippet file = "objectos/way/HtmlTemplateBaseJavadoc.java" region =
     * "noop"}
     *
     * <p>
     * Generates the following when {@code error == false}:
     *
     * <pre>{@code
     *     <div class="alert">This is an alert!</div>
     * }</pre>
     *
     * <p>
     * And generates the following when {@code error == true}:
     *
     * <pre>{@code
     *     <div class="alert alert-error">This is an alert!</div>
     * }</pre>
     *
     * @return the no-op instruction.
     */
    protected final Html.Instruction.NoOp noop() {
      return Html.NOOP;
    }

    /**
     * Renders the specified value as raw HTML.
     *
     * @param value
     *        the raw HTML value
     *
     * @return a raw HTML instruction
     */
    protected final Html.Instruction.OfElement raw(String value) {
      return $html().raw(value);
    }

    /**
     * Renders the specified fragment as part of this template.
     *
     * <p>
     * The included fragment MUST only invoke methods of this template instance.
     * It is common (but not required) for a fragment to be a method reference
     * to a private method of the template instance.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/way/HtmlTemplateBaseJavadoc.java" region =
     * "renderFragment0"}
     *
     * <p>
     * Generates the following HTML:
     *
     * <pre>{@code
     *     <!DOCTYPE html>
     *     <html>
     *     <head>
     *     <title>Include fragment example</title>
     *     </head>
     *     <body>
     *     <h1>Objectos HTML</h1>
     *     <p>Using the include instruction</p>
     *     </body>
     *     </html>
     * }</pre>
     *
     * <p>
     * Note that the methods of included method references all return
     * {@code void}.
     *
     * @param fragment
     *        the fragment to include
     *
     * @return an instruction representing this fragment
     */
    protected final Html.Instruction.OfFragment renderFragment(Html.Fragment.Of0 fragment) {
      return $html().renderFragment(fragment);
    }

    /**
     * Renders the specified fragment as part of this template.
     *
     * <p>
     * The included fragment MUST only invoke methods of this template instance.
     * It is common (but not required) for a fragment to be a method reference
     * to a private method of the template instance.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/way/HtmlTemplateBaseJavadoc.java" region =
     * "renderFragment1"}
     *
     * <p>
     * Generates the following HTML:
     *
     * <pre>{@code
     *     <body>
     *     <div>Foo</div>
     *     <div>Bar</div>
     *     </body>
     * }</pre>
     *
     * <p>
     * Note that the methods of included method references all return
     * {@code void}.
     *
     * @param <T1> the type of the first argument
     *
     * @param fragment
     *        the fragment to include
     * @param arg1
     *        the first argument
     *
     * @return an instruction representing this fragment
     */
    protected final <T1> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of1<T1> fragment, T1 arg1) {
      return $html().renderFragment(fragment, arg1);
    }

    /**
     * Renders the specified fragment as part of this template.
     *
     * <p>
     * The included fragment MUST only invoke methods of this template instance.
     * It is common (but not required) for a fragment to be a method reference
     * to a private method of the template instance.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/way/HtmlTemplateBaseJavadoc.java" region =
     * "renderFragment2"}
     *
     * <p>
     * Generates the following HTML:
     *
     * <pre>{@code
     *     <body>
     *     <p>City<span>Tokyo</span></p>
     *     <p>Country<span>Japan</span></p>
     *     </body>
     * }</pre>
     *
     * <p>
     * Note that the methods of included method references all return
     * {@code void}.
     *
     * @param <T1> the type of the first argument
     * @param <T2> the type of the second argument
     *
     * @param fragment
     *        the fragment to include
     * @param arg1
     *        the first argument
     * @param arg2
     *        the second argument
     *
     * @return an instruction representing this fragment
     */
    protected final <T1, T2> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of2<T1, T2> fragment, T1 arg1, T2 arg2) {
      return $html().renderFragment(fragment, arg1, arg2);
    }

    protected final <T1, T2, T3> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
      return $html().renderFragment(fragment, arg1, arg2, arg3);
    }

    protected final <T1, T2, T3, T4> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      return $html().renderFragment(fragment, arg1, arg2, arg3, arg4);
    }

    /**
     * Renders the specified component as part of this template.
     *
     * @param component
     *        the component to be rendered as part of this template
     *
     * @return an instruction representing the rendered component.
     */
    protected final Html.Instruction.OfFragment renderComponent(Html.Component component) {
      return $html().renderComponent(component);
    }

    /**
     * Renders a text node with the specified {@code text} value. The text
     * value is escaped before being emitted to the output.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/way/HtmlTemplateBaseJavadoc.java" region =
     * "text"}
     *
     * <p>
     * Generates the following HTML:
     *
     * <pre>{@code
     *     <p><strong>This is in bold</strong> &amp; this is not</p>
     * }</pre>
     *
     * @param text
     *        the text value to be added
     *
     * @return an instruction representing the text node
     */
    protected final Html.Instruction.OfElement text(String text) {
      return $html().text(text);
    }

    @Override
    final MarkupAttributes $attributes() {
      return $html();
    }

    @Override
    final MarkupElements $elements() {
      return $html();
    }

    @Override
    final MarkupTestable $testable() {
      return $html();
    }

    abstract Html.Markup $html();

  }

  private Html() {}

  static String ofText(String value) {
    return ofText(value, new StringBuilder());
  }

  static String ofText(String value, StringBuilder sb) {
    enum Parser {
      START,

      TEXT,

      WS;
    }

    Parser parser;
    parser = Parser.START;

    sb.setLength(0);

    for (int idx = 0, len = value.length(); idx < len; idx++) {
      char c;
      c = value.charAt(idx);

      switch (parser) {
        case START -> {
          switch (c) {
            case ' ', '\t', '\n', '\r', '\f' -> parser = Parser.START;

            default -> { parser = Parser.TEXT; sb.append(c); }
          }
        }

        case TEXT -> {
          switch (c) {
            case ' ', '\t', '\n', '\r', '\f' -> parser = Parser.WS;

            default -> { parser = Parser.TEXT; sb.append(c); }
          }
        }

        case WS -> {
          switch (c) {
            case ' ', '\t', '\n', '\r', '\f' -> parser = Parser.WS;

            default -> { parser = Parser.TEXT; sb.append(' '); sb.append(c); }
          }
        }
      }
    }

    return sb.toString();
  }

  /**
   * An instruction to render an HTML attribute and its value.
   */
  public sealed interface AttributeObject extends Instruction.AsObject, Instruction.OfVoid {

    static AttributeObject create(AttributeName name, String value) {
      return new HtmlAttributeObject(
          Check.notNull(name, "name == null"),
          Check.notNull(value, "value == null")
      );
    }

    /**
     * The HTML attribute name.
     *
     * @return the HTML attribute name
     */
    AttributeName name();

    /**
     * The HTML attribute value.
     *
     * @return the HTML attribute value
     */
    String value();

  }

  private record HtmlAttributeObject(AttributeName name, String value) implements AttributeObject {}

  /**
   * Provides the HTML attributes template methods.
   */
  public sealed static abstract class TemplateAttributes {

    TemplateAttributes() {}

    protected final Instruction.OfAttribute attr(AttributeName name, String value) {
      return $attributes().attr(name, value);
    }

    /**
     * Generates the {@code accesskey} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute accesskey(String value) {
      return $attributes().accesskey(value);
    }

    /**
     * Generates the {@code action} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute action(String value) {
      return $attributes().action(value);
    }

    /**
     * Generates the {@code align} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute align(String value) {
      return $attributes().align(value);
    }

    /**
     * Generates the {@code alignment-baseline} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute alignmentBaseline(String value) {
      return $attributes().alignmentBaseline(value);
    }

    /**
     * Generates the {@code alt} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute alt(String value) {
      return $attributes().alt(value);
    }

    /**
     * Generates the {@code aria-hidden} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute ariaHidden(String value) {
      return $attributes().ariaHidden(value);
    }

    /**
     * Generates the {@code aria-label} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute ariaLabel(String value) {
      return $attributes().ariaLabel(value);
    }

    /**
     * Generates the {@code as} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute as(String value) {
      return $attributes().as(value);
    }

    /**
     * Generates the {@code async} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute async() {
      return $attributes().async();
    }

    /**
     * Generates the {@code autocomplete} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute autocomplete(String value) {
      return $attributes().autocomplete(value);
    }

    /**
     * Generates the {@code autofocus} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute autofocus() {
      return $attributes().autofocus();
    }

    /**
     * Generates the {@code baseline-shift} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute baselineShift(String value) {
      return $attributes().baselineShift(value);
    }

    /**
     * Generates the {@code border} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute border(String value) {
      return $attributes().border(value);
    }

    /**
     * Generates the {@code cellpadding} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute cellpadding(String value) {
      return $attributes().cellpadding(value);
    }

    /**
     * Generates the {@code cellspacing} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute cellspacing(String value) {
      return $attributes().cellspacing(value);
    }

    /**
     * Generates the {@code charset} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute charset(String value) {
      return $attributes().charset(value);
    }

    /**
     * Generates the {@code checked} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute checked() {
      return $attributes().checked();
    }

    /**
     * Generates the {@code cite} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute cite(String value) {
      return $attributes().cite(value);
    }

    /**
     * Generates the {@code class} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute className(String value) {
      return $attributes().className(value);
    }

    /**
     * Generates the {@code clip-rule} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute clipRule(String value) {
      return $attributes().clipRule(value);
    }

    /**
     * Generates the {@code color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute color(String value) {
      return $attributes().color(value);
    }

    /**
     * Generates the {@code color-interpolation} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute colorInterpolation(String value) {
      return $attributes().colorInterpolation(value);
    }

    /**
     * Generates the {@code color-interpolation-filters} attribute with the
     * specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute colorInterpolationFilters(String value) {
      return $attributes().colorInterpolationFilters(value);
    }

    /**
     * Generates the {@code cols} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute cols(String value) {
      return $attributes().cols(value);
    }

    /**
     * Generates the {@code content} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute content(String value) {
      return $attributes().content(value);
    }

    /**
     * Generates the {@code contenteditable} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute contenteditable(String value) {
      return $attributes().contenteditable(value);
    }

    /**
     * Generates the {@code crossorigin} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute crossorigin(String value) {
      return $attributes().crossorigin(value);
    }

    /**
     * Generates the {@code cursor} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute cursor(String value) {
      return $attributes().cursor(value);
    }

    /**
     * Generates the {@code d} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute d(String value) {
      return $attributes().d(value);
    }

    /**
     * Generates the {@code defer} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute defer() {
      return $attributes().defer();
    }

    /**
     * Generates the {@code dir} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute dir(String value) {
      return $attributes().dir(value);
    }

    /**
     * Generates the {@code direction} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute direction(String value) {
      return $attributes().direction(value);
    }

    /**
     * Generates the {@code dirname} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute dirname(String value) {
      return $attributes().dirname(value);
    }

    /**
     * Generates the {@code disabled} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute disabled() {
      return $attributes().disabled();
    }

    /**
     * Generates the {@code display} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute display(String value) {
      return $attributes().display(value);
    }

    /**
     * Generates the {@code dominant-baseline} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute dominantBaseline(String value) {
      return $attributes().dominantBaseline(value);
    }

    /**
     * Generates the {@code draggable} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute draggable(String value) {
      return $attributes().draggable(value);
    }

    /**
     * Generates the {@code enctype} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute enctype(String value) {
      return $attributes().enctype(value);
    }

    /**
     * Generates the {@code fill} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute fill(String value) {
      return $attributes().fill(value);
    }

    /**
     * Generates the {@code fill-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute fillOpacity(String value) {
      return $attributes().fillOpacity(value);
    }

    /**
     * Generates the {@code fill-rule} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute fillRule(String value) {
      return $attributes().fillRule(value);
    }

    /**
     * Generates the {@code filter} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute filter(String value) {
      return $attributes().filter(value);
    }

    /**
     * Generates the {@code flood-color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute floodColor(String value) {
      return $attributes().floodColor(value);
    }

    /**
     * Generates the {@code flood-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute floodOpacity(String value) {
      return $attributes().floodOpacity(value);
    }

    /**
     * Generates the {@code for} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute forAttr(String value) {
      return $attributes().forAttr(value);
    }

    /**
     * Generates the {@code for} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute forElement(String value) {
      return $attributes().forElement(value);
    }

    /**
     * Generates the {@code glyph-orientation-horizontal} attribute with the
     * specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute glyphOrientationHorizontal(String value) {
      return $attributes().glyphOrientationHorizontal(value);
    }

    /**
     * Generates the {@code glyph-orientation-vertical} attribute with the
     * specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute glyphOrientationVertical(String value) {
      return $attributes().glyphOrientationVertical(value);
    }

    /**
     * Generates the {@code height} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute height(String value) {
      return $attributes().height(value);
    }

    /**
     * Generates the {@code hidden} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute hidden() {
      return $attributes().hidden();
    }

    /**
     * Generates the {@code href} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute href(String value) {
      return $attributes().href(value);
    }

    /**
     * Generates the {@code http-equiv} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute httpEquiv(String value) {
      return $attributes().httpEquiv(value);
    }

    /**
     * Generates the {@code id} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute id(String value) {
      return $attributes().id(value);
    }

    /**
     * Generates the {@code image-rendering} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute imageRendering(String value) {
      return $attributes().imageRendering(value);
    }

    /**
     * Generates the {@code integrity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute integrity(String value) {
      return $attributes().integrity(value);
    }

    /**
     * Generates the {@code lang} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute lang(String value) {
      return $attributes().lang(value);
    }

    /**
     * Generates the {@code letter-spacing} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute letterSpacing(String value) {
      return $attributes().letterSpacing(value);
    }

    /**
     * Generates the {@code lighting-color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute lightingColor(String value) {
      return $attributes().lightingColor(value);
    }

    /**
     * Generates the {@code marker-end} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute markerEnd(String value) {
      return $attributes().markerEnd(value);
    }

    /**
     * Generates the {@code marker-mid} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute markerMid(String value) {
      return $attributes().markerMid(value);
    }

    /**
     * Generates the {@code marker-start} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute markerStart(String value) {
      return $attributes().markerStart(value);
    }

    /**
     * Generates the {@code mask} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute mask(String value) {
      return $attributes().mask(value);
    }

    /**
     * Generates the {@code mask-type} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute maskType(String value) {
      return $attributes().maskType(value);
    }

    /**
     * Generates the {@code maxlength} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute maxlength(String value) {
      return $attributes().maxlength(value);
    }

    /**
     * Generates the {@code media} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute media(String value) {
      return $attributes().media(value);
    }

    /**
     * Generates the {@code method} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute method(String value) {
      return $attributes().method(value);
    }

    /**
     * Generates the {@code minlength} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute minlength(String value) {
      return $attributes().minlength(value);
    }

    /**
     * Generates the {@code multiple} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute multiple() {
      return $attributes().multiple();
    }

    /**
     * Generates the {@code name} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute name(String value) {
      return $attributes().name(value);
    }

    /**
     * Generates the {@code nomodule} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute nomodule() {
      return $attributes().nomodule();
    }

    /**
     * Generates the {@code onafterprint} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onafterprint(String value) {
      return $attributes().onafterprint(value);
    }

    /**
     * Generates the {@code onbeforeprint} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onbeforeprint(String value) {
      return $attributes().onbeforeprint(value);
    }

    /**
     * Generates the {@code onbeforeunload} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onbeforeunload(String value) {
      return $attributes().onbeforeunload(value);
    }

    /**
     * Generates the {@code onclick} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onclick(String value) {
      return $attributes().onclick(value);
    }

    /**
     * Generates the {@code onhashchange} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onhashchange(String value) {
      return $attributes().onhashchange(value);
    }

    /**
     * Generates the {@code onlanguagechange} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onlanguagechange(String value) {
      return $attributes().onlanguagechange(value);
    }

    /**
     * Generates the {@code onmessage} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onmessage(String value) {
      return $attributes().onmessage(value);
    }

    /**
     * Generates the {@code onoffline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onoffline(String value) {
      return $attributes().onoffline(value);
    }

    /**
     * Generates the {@code ononline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute ononline(String value) {
      return $attributes().ononline(value);
    }

    /**
     * Generates the {@code onpagehide} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onpagehide(String value) {
      return $attributes().onpagehide(value);
    }

    /**
     * Generates the {@code onpageshow} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onpageshow(String value) {
      return $attributes().onpageshow(value);
    }

    /**
     * Generates the {@code onpopstate} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onpopstate(String value) {
      return $attributes().onpopstate(value);
    }

    /**
     * Generates the {@code onrejectionhandled} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onrejectionhandled(String value) {
      return $attributes().onrejectionhandled(value);
    }

    /**
     * Generates the {@code onstorage} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onstorage(String value) {
      return $attributes().onstorage(value);
    }

    /**
     * Generates the {@code onsubmit} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onsubmit(String value) {
      return $attributes().onsubmit(value);
    }

    /**
     * Generates the {@code onunhandledrejection} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onunhandledrejection(String value) {
      return $attributes().onunhandledrejection(value);
    }

    /**
     * Generates the {@code onunload} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute onunload(String value) {
      return $attributes().onunload(value);
    }

    /**
     * Generates the {@code opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute opacity(String value) {
      return $attributes().opacity(value);
    }

    /**
     * Generates the {@code open} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute open() {
      return $attributes().open();
    }

    /**
     * Generates the {@code overflow} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute overflow(String value) {
      return $attributes().overflow(value);
    }

    /**
     * Generates the {@code paint-order} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute paintOrder(String value) {
      return $attributes().paintOrder(value);
    }

    /**
     * Generates the {@code placeholder} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute placeholder(String value) {
      return $attributes().placeholder(value);
    }

    /**
     * Generates the {@code pointer-events} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute pointerEvents(String value) {
      return $attributes().pointerEvents(value);
    }

    /**
     * Generates the {@code property} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute property(String value) {
      return $attributes().property(value);
    }

    /**
     * Generates the {@code readonly} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute readonly() {
      return $attributes().readonly();
    }

    /**
     * Generates the {@code referrerpolicy} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute referrerpolicy(String value) {
      return $attributes().referrerpolicy(value);
    }

    /**
     * Generates the {@code rel} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute rel(String value) {
      return $attributes().rel(value);
    }

    /**
     * Generates the {@code required} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute required() {
      return $attributes().required();
    }

    /**
     * Generates the {@code rev} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute rev(String value) {
      return $attributes().rev(value);
    }

    /**
     * Generates the {@code reversed} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute reversed() {
      return $attributes().reversed();
    }

    /**
     * Generates the {@code role} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute role(String value) {
      return $attributes().role(value);
    }

    /**
     * Generates the {@code rows} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute rows(String value) {
      return $attributes().rows(value);
    }

    /**
     * Generates the {@code selected} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute selected() {
      return $attributes().selected();
    }

    /**
     * Generates the {@code shape-rendering} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute shapeRendering(String value) {
      return $attributes().shapeRendering(value);
    }

    /**
     * Generates the {@code size} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute size(String value) {
      return $attributes().size(value);
    }

    /**
     * Generates the {@code sizes} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute sizes(String value) {
      return $attributes().sizes(value);
    }

    /**
     * Generates the {@code spellcheck} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute spellcheck(String value) {
      return $attributes().spellcheck(value);
    }

    /**
     * Generates the {@code src} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute src(String value) {
      return $attributes().src(value);
    }

    /**
     * Generates the {@code srcset} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute srcset(String value) {
      return $attributes().srcset(value);
    }

    /**
     * Generates the {@code start} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute start(String value) {
      return $attributes().start(value);
    }

    /**
     * Generates the {@code stop-color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute stopColor(String value) {
      return $attributes().stopColor(value);
    }

    /**
     * Generates the {@code stop-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute stopOpacity(String value) {
      return $attributes().stopOpacity(value);
    }

    /**
     * Generates the {@code stroke} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute stroke(String value) {
      return $attributes().stroke(value);
    }

    /**
     * Generates the {@code stroke-dasharray} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute strokeDasharray(String value) {
      return $attributes().strokeDasharray(value);
    }

    /**
     * Generates the {@code stroke-dashoffset} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute strokeDashoffset(String value) {
      return $attributes().strokeDashoffset(value);
    }

    /**
     * Generates the {@code stroke-linecap} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute strokeLinecap(String value) {
      return $attributes().strokeLinecap(value);
    }

    /**
     * Generates the {@code stroke-linejoin} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute strokeLinejoin(String value) {
      return $attributes().strokeLinejoin(value);
    }

    /**
     * Generates the {@code stroke-miterlimit} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute strokeMiterlimit(String value) {
      return $attributes().strokeMiterlimit(value);
    }

    /**
     * Generates the {@code stroke-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute strokeOpacity(String value) {
      return $attributes().strokeOpacity(value);
    }

    /**
     * Generates the {@code stroke-width} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute strokeWidth(String value) {
      return $attributes().strokeWidth(value);
    }

    /**
     * Generates the {@code style} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute inlineStyle(String value) {
      return $attributes().inlineStyle(value);
    }

    /**
     * Generates the {@code tabindex} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute tabindex(String value) {
      return $attributes().tabindex(value);
    }

    /**
     * Generates the {@code target} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute target(String value) {
      return $attributes().target(value);
    }

    /**
     * Generates the {@code text-anchor} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute textAnchor(String value) {
      return $attributes().textAnchor(value);
    }

    /**
     * Generates the {@code text-decoration} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute textDecoration(String value) {
      return $attributes().textDecoration(value);
    }

    /**
     * Generates the {@code text-overflow} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute textOverflow(String value) {
      return $attributes().textOverflow(value);
    }

    /**
     * Generates the {@code text-rendering} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute textRendering(String value) {
      return $attributes().textRendering(value);
    }

    /**
     * Generates the {@code transform} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute transform(String value) {
      return $attributes().transform(value);
    }

    /**
     * Generates the {@code transform-origin} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute transformOrigin(String value) {
      return $attributes().transformOrigin(value);
    }

    /**
     * Generates the {@code translate} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute translate(String value) {
      return $attributes().translate(value);
    }

    /**
     * Generates the {@code type} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute type(String value) {
      return $attributes().type(value);
    }

    /**
     * Generates the {@code unicode-bidi} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute unicodeBidi(String value) {
      return $attributes().unicodeBidi(value);
    }

    /**
     * Generates the {@code value} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute value(String value) {
      return $attributes().value(value);
    }

    /**
     * Generates the {@code vector-effect} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute vectorEffect(String value) {
      return $attributes().vectorEffect(value);
    }

    /**
     * Generates the {@code viewBox} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute viewBox(String value) {
      return $attributes().viewBox(value);
    }

    /**
     * Generates the {@code visibility} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute visibility(String value) {
      return $attributes().visibility(value);
    }

    /**
     * Generates the {@code white-space} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute whiteSpace(String value) {
      return $attributes().whiteSpace(value);
    }

    /**
     * Generates the {@code width} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute width(String value) {
      return $attributes().width(value);
    }

    /**
     * Generates the {@code word-spacing} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute wordSpacing(String value) {
      return $attributes().wordSpacing(value);
    }

    /**
     * Generates the {@code wrap} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute wrap(String value) {
      return $attributes().wrap(value);
    }

    /**
     * Generates the {@code writing-mode} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute writingMode(String value) {
      return $attributes().writingMode(value);
    }

    /**
     * Generates the {@code xmlns} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute xmlns(String value) {
      return $attributes().xmlns(value);
    }

    abstract Html.MarkupAttributes $attributes();

  }

  /**
   * Provides the HTML elements template methods.
   */
  public sealed static abstract class TemplateElements extends TemplateAttributes {

    /**
     * Sole constructor.
     */
    TemplateElements() {}

    /**
     * Generates the {@code <!DOCTYPE html>} doctype.
     */
    protected final void doctype() {
      $elements().doctype();
    }

    /**
     * Generates the {@code a} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement a(Instruction... contents) {
      return $elements().a(contents);
    }

    /**
     * Generates the {@code a} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement a(String text) {
      return $elements().a(text);
    }

    /**
     * Generates the {@code abbr} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement abbr(Instruction... contents) {
      return $elements().abbr(contents);
    }

    /**
     * Generates the {@code abbr} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement abbr(String text) {
      return $elements().abbr(text);
    }

    /**
     * Generates the {@code article} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement article(Instruction... contents) {
      return $elements().article(contents);
    }

    /**
     * Generates the {@code article} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement article(String text) {
      return $elements().article(text);
    }

    /**
     * Generates the {@code aside} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement aside(Instruction... contents) {
      return $elements().aside(contents);
    }

    /**
     * Generates the {@code aside} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement aside(String text) {
      return $elements().aside(text);
    }

    /**
     * Generates the {@code b} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement b(Instruction... contents) {
      return $elements().b(contents);
    }

    /**
     * Generates the {@code b} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement b(String text) {
      return $elements().b(text);
    }

    /**
     * Generates the {@code blockquote} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement blockquote(Instruction... contents) {
      return $elements().blockquote(contents);
    }

    /**
     * Generates the {@code blockquote} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement blockquote(String text) {
      return $elements().blockquote(text);
    }

    /**
     * Generates the {@code body} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement body(Instruction... contents) {
      return $elements().body(contents);
    }

    /**
     * Generates the {@code body} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement body(String text) {
      return $elements().body(text);
    }

    /**
     * Generates the {@code br} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement br(Instruction.OfVoid... contents) {
      return $elements().br(contents);
    }

    /**
     * Generates the {@code button} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement button(Instruction... contents) {
      return $elements().button(contents);
    }

    /**
     * Generates the {@code button} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement button(String text) {
      return $elements().button(text);
    }

    /**
     * Generates the {@code clipPath} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement clipPath(Instruction... contents) {
      return $elements().clipPath(contents);
    }

    /**
     * Generates the {@code clipPath} attribute or element with the specified
     * text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    protected final Html.Instruction.OfAmbiguous clipPath(String text) {
      return $elements().clipPath(text);
    }

    /**
     * Generates the {@code code} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement code(Instruction... contents) {
      return $elements().code(contents);
    }

    /**
     * Generates the {@code code} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement code(String text) {
      return $elements().code(text);
    }

    /**
     * Generates the {@code dd} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement dd(Instruction... contents) {
      return $elements().dd(contents);
    }

    /**
     * Generates the {@code dd} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement dd(String text) {
      return $elements().dd(text);
    }

    /**
     * Generates the {@code defs} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement defs(Instruction... contents) {
      return $elements().defs(contents);
    }

    /**
     * Generates the {@code defs} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement defs(String text) {
      return $elements().defs(text);
    }

    /**
     * Generates the {@code details} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement details(Instruction... contents) {
      return $elements().details(contents);
    }

    /**
     * Generates the {@code details} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement details(String text) {
      return $elements().details(text);
    }

    /**
     * Generates the {@code div} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement div(Instruction... contents) {
      return $elements().div(contents);
    }

    /**
     * Generates the {@code div} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement div(String text) {
      return $elements().div(text);
    }

    /**
     * Generates the {@code dl} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement dl(Instruction... contents) {
      return $elements().dl(contents);
    }

    /**
     * Generates the {@code dl} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement dl(String text) {
      return $elements().dl(text);
    }

    /**
     * Generates the {@code dt} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement dt(Instruction... contents) {
      return $elements().dt(contents);
    }

    /**
     * Generates the {@code dt} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement dt(String text) {
      return $elements().dt(text);
    }

    /**
     * Generates the {@code em} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement em(Instruction... contents) {
      return $elements().em(contents);
    }

    /**
     * Generates the {@code em} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement em(String text) {
      return $elements().em(text);
    }

    /**
     * Generates the {@code fieldset} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement fieldset(Instruction... contents) {
      return $elements().fieldset(contents);
    }

    /**
     * Generates the {@code fieldset} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement fieldset(String text) {
      return $elements().fieldset(text);
    }

    /**
     * Generates the {@code figure} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement figure(Instruction... contents) {
      return $elements().figure(contents);
    }

    /**
     * Generates the {@code figure} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement figure(String text) {
      return $elements().figure(text);
    }

    /**
     * Generates the {@code footer} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement footer(Instruction... contents) {
      return $elements().footer(contents);
    }

    /**
     * Generates the {@code footer} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement footer(String text) {
      return $elements().footer(text);
    }

    /**
     * Generates the {@code form} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement form(Instruction... contents) {
      return $elements().form(contents);
    }

    /**
     * Generates the {@code form} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    protected final Html.Instruction.OfAmbiguous form(String text) {
      return $elements().form(text);
    }

    /**
     * Generates the {@code g} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement g(Instruction... contents) {
      return $elements().g(contents);
    }

    /**
     * Generates the {@code g} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement g(String text) {
      return $elements().g(text);
    }

    /**
     * Generates the {@code h1} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h1(Instruction... contents) {
      return $elements().h1(contents);
    }

    /**
     * Generates the {@code h1} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h1(String text) {
      return $elements().h1(text);
    }

    /**
     * Generates the {@code h2} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h2(Instruction... contents) {
      return $elements().h2(contents);
    }

    /**
     * Generates the {@code h2} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h2(String text) {
      return $elements().h2(text);
    }

    /**
     * Generates the {@code h3} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h3(Instruction... contents) {
      return $elements().h3(contents);
    }

    /**
     * Generates the {@code h3} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h3(String text) {
      return $elements().h3(text);
    }

    /**
     * Generates the {@code h4} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h4(Instruction... contents) {
      return $elements().h4(contents);
    }

    /**
     * Generates the {@code h4} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h4(String text) {
      return $elements().h4(text);
    }

    /**
     * Generates the {@code h5} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h5(Instruction... contents) {
      return $elements().h5(contents);
    }

    /**
     * Generates the {@code h5} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h5(String text) {
      return $elements().h5(text);
    }

    /**
     * Generates the {@code h6} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h6(Instruction... contents) {
      return $elements().h6(contents);
    }

    /**
     * Generates the {@code h6} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement h6(String text) {
      return $elements().h6(text);
    }

    /**
     * Generates the {@code head} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement head(Instruction... contents) {
      return $elements().head(contents);
    }

    /**
     * Generates the {@code head} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement head(String text) {
      return $elements().head(text);
    }

    /**
     * Generates the {@code header} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement header(Instruction... contents) {
      return $elements().header(contents);
    }

    /**
     * Generates the {@code header} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement header(String text) {
      return $elements().header(text);
    }

    /**
     * Generates the {@code hgroup} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement hgroup(Instruction... contents) {
      return $elements().hgroup(contents);
    }

    /**
     * Generates the {@code hgroup} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement hgroup(String text) {
      return $elements().hgroup(text);
    }

    /**
     * Generates the {@code hr} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement hr(Instruction.OfVoid... contents) {
      return $elements().hr(contents);
    }

    /**
     * Generates the {@code html} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement html(Instruction... contents) {
      return $elements().html(contents);
    }

    /**
     * Generates the {@code html} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement html(String text) {
      return $elements().html(text);
    }

    /**
     * Generates the {@code img} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement img(Instruction.OfVoid... contents) {
      return $elements().img(contents);
    }

    /**
     * Generates the {@code input} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement input(Instruction.OfVoid... contents) {
      return $elements().input(contents);
    }

    /**
     * Generates the {@code kbd} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement kbd(Instruction... contents) {
      return $elements().kbd(contents);
    }

    /**
     * Generates the {@code kbd} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement kbd(String text) {
      return $elements().kbd(text);
    }

    /**
     * Generates the {@code label} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement label(Instruction... contents) {
      return $elements().label(contents);
    }

    /**
     * Generates the {@code label} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    protected final Html.Instruction.OfAmbiguous label(String text) {
      return $elements().label(text);
    }

    /**
     * Generates the {@code legend} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement legend(Instruction... contents) {
      return $elements().legend(contents);
    }

    /**
     * Generates the {@code legend} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement legend(String text) {
      return $elements().legend(text);
    }

    /**
     * Generates the {@code li} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement li(Instruction... contents) {
      return $elements().li(contents);
    }

    /**
     * Generates the {@code li} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement li(String text) {
      return $elements().li(text);
    }

    /**
     * Generates the {@code link} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement link(Instruction.OfVoid... contents) {
      return $elements().link(contents);
    }

    /**
     * Generates the {@code main} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement main(Instruction... contents) {
      return $elements().main(contents);
    }

    /**
     * Generates the {@code main} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement main(String text) {
      return $elements().main(text);
    }

    /**
     * Generates the {@code menu} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement menu(Instruction... contents) {
      return $elements().menu(contents);
    }

    /**
     * Generates the {@code menu} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement menu(String text) {
      return $elements().menu(text);
    }

    /**
     * Generates the {@code meta} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement meta(Instruction.OfVoid... contents) {
      return $elements().meta(contents);
    }

    /**
     * Generates the {@code nav} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement nav(Instruction... contents) {
      return $elements().nav(contents);
    }

    /**
     * Generates the {@code nav} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement nav(String text) {
      return $elements().nav(text);
    }

    /**
     * Generates the {@code ol} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement ol(Instruction... contents) {
      return $elements().ol(contents);
    }

    /**
     * Generates the {@code ol} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement ol(String text) {
      return $elements().ol(text);
    }

    /**
     * Generates the {@code optgroup} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement optgroup(Instruction... contents) {
      return $elements().optgroup(contents);
    }

    /**
     * Generates the {@code optgroup} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement optgroup(String text) {
      return $elements().optgroup(text);
    }

    /**
     * Generates the {@code option} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement option(Instruction... contents) {
      return $elements().option(contents);
    }

    /**
     * Generates the {@code option} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement option(String text) {
      return $elements().option(text);
    }

    /**
     * Generates the {@code p} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement p(Instruction... contents) {
      return $elements().p(contents);
    }

    /**
     * Generates the {@code p} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement p(String text) {
      return $elements().p(text);
    }

    /**
     * Generates the {@code path} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement path(Instruction... contents) {
      return $elements().path(contents);
    }

    /**
     * Generates the {@code path} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement path(String text) {
      return $elements().path(text);
    }

    /**
     * Generates the {@code pre} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement pre(Instruction... contents) {
      return $elements().pre(contents);
    }

    /**
     * Generates the {@code pre} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement pre(String text) {
      return $elements().pre(text);
    }

    /**
     * Generates the {@code progress} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement progress(Instruction... contents) {
      return $elements().progress(contents);
    }

    /**
     * Generates the {@code progress} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement progress(String text) {
      return $elements().progress(text);
    }

    /**
     * Generates the {@code samp} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement samp(Instruction... contents) {
      return $elements().samp(contents);
    }

    /**
     * Generates the {@code samp} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement samp(String text) {
      return $elements().samp(text);
    }

    /**
     * Generates the {@code script} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement script(Instruction... contents) {
      return $elements().script(contents);
    }

    /**
     * Generates the {@code script} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement script(String text) {
      return $elements().script(text);
    }

    /**
     * Generates the {@code section} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement section(Instruction... contents) {
      return $elements().section(contents);
    }

    /**
     * Generates the {@code section} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement section(String text) {
      return $elements().section(text);
    }

    /**
     * Generates the {@code select} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement select(Instruction... contents) {
      return $elements().select(contents);
    }

    /**
     * Generates the {@code select} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement select(String text) {
      return $elements().select(text);
    }

    /**
     * Generates the {@code small} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement small(Instruction... contents) {
      return $elements().small(contents);
    }

    /**
     * Generates the {@code small} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement small(String text) {
      return $elements().small(text);
    }

    /**
     * Generates the {@code span} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement span(Instruction... contents) {
      return $elements().span(contents);
    }

    /**
     * Generates the {@code span} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement span(String text) {
      return $elements().span(text);
    }

    /**
     * Generates the {@code strong} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement strong(Instruction... contents) {
      return $elements().strong(contents);
    }

    /**
     * Generates the {@code strong} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement strong(String text) {
      return $elements().strong(text);
    }

    /**
     * Generates the {@code style} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement style(Instruction... contents) {
      return $elements().style(contents);
    }

    /**
     * Generates the {@code style} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement style(String text) {
      return $elements().style(text);
    }

    /**
     * Generates the {@code sub} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement sub(Instruction... contents) {
      return $elements().sub(contents);
    }

    /**
     * Generates the {@code sub} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement sub(String text) {
      return $elements().sub(text);
    }

    /**
     * Generates the {@code summary} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement summary(Instruction... contents) {
      return $elements().summary(contents);
    }

    /**
     * Generates the {@code summary} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement summary(String text) {
      return $elements().summary(text);
    }

    /**
     * Generates the {@code sup} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement sup(Instruction... contents) {
      return $elements().sup(contents);
    }

    /**
     * Generates the {@code sup} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement sup(String text) {
      return $elements().sup(text);
    }

    /**
     * Generates the {@code svg} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement svg(Instruction... contents) {
      return $elements().svg(contents);
    }

    /**
     * Generates the {@code svg} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement svg(String text) {
      return $elements().svg(text);
    }

    /**
     * Generates the {@code table} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement table(Instruction... contents) {
      return $elements().table(contents);
    }

    /**
     * Generates the {@code table} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement table(String text) {
      return $elements().table(text);
    }

    /**
     * Generates the {@code tbody} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement tbody(Instruction... contents) {
      return $elements().tbody(contents);
    }

    /**
     * Generates the {@code tbody} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement tbody(String text) {
      return $elements().tbody(text);
    }

    /**
     * Generates the {@code td} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement td(Instruction... contents) {
      return $elements().td(contents);
    }

    /**
     * Generates the {@code td} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement td(String text) {
      return $elements().td(text);
    }

    /**
     * Generates the {@code template} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement template(Instruction... contents) {
      return $elements().template(contents);
    }

    /**
     * Generates the {@code template} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement template(String text) {
      return $elements().template(text);
    }

    /**
     * Generates the {@code textarea} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement textarea(Instruction... contents) {
      return $elements().textarea(contents);
    }

    /**
     * Generates the {@code textarea} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement textarea(String text) {
      return $elements().textarea(text);
    }

    /**
     * Generates the {@code th} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement th(Instruction... contents) {
      return $elements().th(contents);
    }

    /**
     * Generates the {@code th} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement th(String text) {
      return $elements().th(text);
    }

    /**
     * Generates the {@code thead} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement thead(Instruction... contents) {
      return $elements().thead(contents);
    }

    /**
     * Generates the {@code thead} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement thead(String text) {
      return $elements().thead(text);
    }

    /**
     * Generates the {@code title} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement title(Instruction... contents) {
      return $elements().title(contents);
    }

    /**
     * Generates the {@code title} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    protected final Html.Instruction.OfAmbiguous title(String text) {
      return $elements().title(text);
    }

    /**
     * Generates the {@code tr} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement tr(Instruction... contents) {
      return $elements().tr(contents);
    }

    /**
     * Generates the {@code tr} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement tr(String text) {
      return $elements().tr(text);
    }

    /**
     * Generates the {@code ul} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement ul(Instruction... contents) {
      return $elements().ul(contents);
    }

    /**
     * Generates the {@code ul} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    protected final Html.Instruction.OfElement ul(String text) {
      return $elements().ul(text);
    }

    abstract Html.MarkupElements $elements();

  }

  /**
   * Provides methods for integrating testable nodes into the HTML template.
   */
  public sealed static abstract class TemplateTestable extends TemplateElements {

    /**
     * Sole constructor.
     */
    TemplateTestable() {}

    /**
     * Formats the specified value as a testable table cell with the specified
     * fixed width.
     *
     * @param value
     *        the cell value
     * @param width
     *        the fixed width of the cell
     *
     * @return always the cell value
     */
    protected final String testableCell(String value, int width) {
      return $testable().testableCell(value, width);
    }

    /**
     * Formats the specified name and value as a testable field (optional
     * operation).
     *
     * @param name
     *        the field name
     * @param value
     *        the field value
     *
     * @return always the field value
     */
    protected final String testableField(String name, String value) {
      return $testable().testableField(name, value);
    }

    /**
     * Formats the specified name as a testable field name (optional operation).
     *
     * @param name
     *        the field name
     *
     * @return the specified field name
     */
    protected final String testableFieldName(String name) {
      return $testable().testableFieldName(name);
    }

    /**
     * Formats the specified value as a testable field value (optional
     * operation).
     *
     * @param name
     *        the field value
     *
     * @return the specified field value
     */
    protected final String testableFieldValue(String name) {
      return $testable().testableFieldValue(name);
    }

    /**
     * Formats the specified value as a testable heading level 1 (optional
     * operation).
     *
     * @param name
     *        the heading value
     *
     * @return the specified value
     */
    protected final String testableH1(String name) {
      return $testable().testableH1(name);
    }

    /**
     * Formats the specified value as a testable heading level 2 (optional
     * operation).
     *
     * @param name
     *        the heading value
     *
     * @return the specified value
     */
    protected final String testableH2(String name) {
      return $testable().testableH2(name);
    }

    /**
     * Formats the specified value as a testable heading level 3 (optional
     * operation).
     *
     * @param name
     *        the heading value
     *
     * @return the specified value
     */
    protected final String testableH3(String name) {
      return $testable().testableH3(name);
    }

    /**
     * Formats the specified value as a testable heading level 4 (optional
     * operation).
     *
     * @param name
     *        the heading value
     *
     * @return the specified value
     */
    protected final String testableH4(String name) {
      return $testable().testableH4(name);
    }

    /**
     * Formats the specified value as a testable heading level 5 (optional
     * operation).
     *
     * @param name
     *        the heading value
     *
     * @return the specified value
     */
    protected final String testableH5(String name) {
      return $testable().testableH5(name);
    }

    /**
     * Formats the specified value as a testable heading level 6 (optional
     * operation).
     *
     * @param name
     *        the heading value
     *
     * @return the specified value
     */
    protected final String testableH6(String name) {
      return $testable().testableH6(name);
    }

    /**
     * Formats a line separator at the testable output exclusively.
     */
    protected final Html.Instruction.NoOp testableNewLine() {
      return $testable().testableNewLine();
    }

    abstract MarkupTestable $testable();

  }

}

final class HtmlDomAttribute implements Html.Dom.Attribute {

  HtmlAttributeName name;

  private final HtmlMarkup player;

  Object value;

  public HtmlDomAttribute(HtmlMarkup player) {
    this.player = player;
  }

  @Override
  public final String name() {
    return name.name();
  }

  @Override
  public final boolean booleanAttribute() {
    return name.booleanAttribute();
  }

  @Override
  public final boolean singleQuoted() {
    return name.singleQuoted();
  }

  @Override
  public final String value() {
    player.attributeValues();

    player.attributeValuesIterator();

    if (!hasNext()) {
      return "";
    }

    Object result;
    result = next();

    if (!hasNext()) {
      return String.valueOf(result);
    }

    Class<?> type;
    type = name.type();

    if (type == Script.Action.class) {

      ScriptActionJoiner joiner;
      joiner = new ScriptActionJoiner();

      joiner.add(result);

      joiner.add(next());

      while (hasNext()) {
        joiner.add(next());
      }

      return joiner.join();

    } else {

      StringBuilder value;
      value = new StringBuilder();

      value.append(result);

      value.append(' ');

      value.append(next());

      while (hasNext()) {
        value.append(' ');

        value.append(next());
      }

      return value.toString();

    }
  }

  private boolean hasNext() {
    return player.attributeValuesHasNext();
  }

  private Object next() {
    Object result;
    result = player.attributeValuesNext(value);

    value = null;

    return result;
  }

}

enum HtmlDomDocumentType implements Html.Dom.DocumentType {
  INSTANCE;
}

final class HtmlDomElement implements Html.Dom.Element, Lang.IterableOnce<Html.Dom.Node>, Iterator<Html.Dom.Node> {

  private class ThisAttributes implements Lang.IterableOnce<Html.Dom.Attribute>, Iterator<Html.Dom.Attribute> {

    @Override
    public final boolean hasNext() {
      return player.elementAttributesHasNext(name);
    }

    @Override
    public final Iterator<Html.Dom.Attribute> iterator() {
      player.elementAttributesIterator();

      return this;
    }

    @Override
    public final Html.Dom.Attribute next() {
      return player.elementAttributesNext();
    }

  }

  private ThisAttributes attributes;

  private final HtmlMarkup player;

  HtmlElementName name;

  HtmlDomElement(HtmlMarkup player) {
    this.player = player;
  }

  @Override
  public final Lang.IterableOnce<Html.Dom.Attribute> attributes() {
    player.elementAttributes();

    if (attributes == null) {
      attributes = new ThisAttributes();
    }

    return attributes;
  }

  @Override
  public final boolean hasNext() {
    return player.elementNodesHasNext();
  }

  @Override
  public final boolean isVoid() {
    return !name.endTag();
  }

  @Override
  public final Iterator<Html.Dom.Node> iterator() {
    player.elementNodesIterator();

    return this;
  }

  @Override
  public final String name() {
    return name.name();
  }

  @Override
  public final Html.Dom.Node next() {
    return player.elementNodesNext();
  }

  @Override
  public final Lang.IterableOnce<Html.Dom.Node> nodes() {
    player.elementNodes();

    return this;
  }

}

final class HtmlDomRaw implements Html.Dom.Raw {

  String value;

  @Override
  public final String value() {
    return value;
  }

}

final class HtmlDomText implements Html.Dom.Text {

  String value;

  @Override
  public final String value() {
    return value;
  }

}

record HtmlId(String value) implements Html.Id {}