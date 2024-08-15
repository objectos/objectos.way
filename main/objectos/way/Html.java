/*
 * Copyright (C) 2015-2024 Objectos Software LTDA.
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

import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import objectos.lang.CharWritable;
import objectos.lang.IterableOnce;
import objectos.lang.object.Check;

/**
 * The <strong>Objectos HTML</strong> main class.
 */
public final class Html {

  /*
   * name types
   */

  /**
   * The name of an HTML attribute.
   */
  public sealed interface AttributeName permits HtmlAttributeName {

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
   * The name of an HTML element.
   */
  public sealed interface ElementName permits HtmlElementName {

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

  /*
   * pseudom types
   */

  /**
   * An attribute in a compiled {@link Html.Template}
   */
  public interface Attribute {

    String name();

    boolean booleanAttribute();

    boolean singleQuoted();

    String value();

    default boolean hasName(String name) {
      return name().equals(name);
    }

  }

  /**
   * A compiled {@link Html.Template}.
   */
  public interface Document {

    IterableOnce<Node> nodes();

  }

  public sealed interface Node permits DocumentType, Element, RawText, Text {}

  public non-sealed interface DocumentType extends Node {}

  public non-sealed interface Element extends Node {

    IterableOnce<Attribute> attributes();

    boolean isVoid();

    String name();

    IterableOnce<Node> nodes();

    default boolean hasName(String name) {
      return name().equals(name);
    }

  }

  public interface ElementComponent {

    Html.Instruction render();

  }

  public non-sealed interface RawText extends Node {

    String value();

  }

  public non-sealed interface Text extends Node {

    String value();

  }

  /**
   * Compiles an HTML template into a materialized HTML document.
   */
  public sealed interface Compiler extends CompilerAttributes, CompilerElements, CharWritable permits HtmlCompiler {

    Html.Id nextId();

    Html.Document compile();

    AttributeInstruction attribute(AttributeName name, String value);

    DataOnInstruction dataOn(AttributeName name, Script.Action value);

    /**
     * Flattens the specified instructions so that each of the specified
     * instructions is individually added, in order, to a receiving element.
     *
     * <p>
     * This is useful, for example, when creating {@link HtmlComponent}
     * instances.
     * The following Objectos HTML code:
     *
     * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
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
    ElementInstruction flatten(Instruction... contents);

    ElementInstruction flattenNonNull(Instruction... contents);

    /**
     * Includes a fragment into this template represented by the specified
     * lambda.
     *
     * <p>
     * The included fragment MUST only invoke methods this template instance. It
     * is common (but not required) for a fragment to be a method reference to
     * a private method of the template instance.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
     * "IncludeExample"}
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
    FragmentInstruction include(FragmentLambda fragment);

    <T1> FragmentInstruction include(FragmentLambda1<T1> fragment, T1 arg1);

    <T1, T2> FragmentInstruction include(FragmentLambda2<T1, T2> fragment, T1 arg1, T2 arg2);

    <T1, T2, T3> FragmentInstruction include(FragmentLambda3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3);

    <T1, T2, T3, T4> FragmentInstruction include(FragmentLambda4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4);

    /**
     * The no-op instruction.
     *
     * <p>
     * It can be used to conditionally add an attribute or element. For example,
     * the following Objectos HTML template:
     *
     * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
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
    NoOpInstruction noop();

    ElementInstruction raw(String text);

    /**
     * Generates a text node with the specified {@code text} value. The text
     * value
     * is escaped before being emitted to the output.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
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
    ElementInstruction text(String text);

  }

  /**
   * A delayed set of template instructions.
   *
   * <p>
   * The set of instructions MUST be of the same template instance where this
   * fragment will be included.
   *
   * @see Html#include(Html.FragmentLambda)
   */
  @FunctionalInterface
  public interface FragmentLambda {

    /**
     * Invokes this set of instructions.
     */
    void invoke() throws Exception;

  }

  /**
   * A delayed set of template instructions.
   *
   * <p>
   * The set of instructions MUST be of the same template instance where this
   * fragment will be included.
   *
   * @see BaseTemplateDsl#include(FragmentAction)
   */
  @FunctionalInterface
  public interface FragmentLambda1<T1> {

    /**
     * Invokes this set of instructions.
     */
    void renderMobileNavHeaderItems(T1 arg1) throws Exception;

  }

  /**
   * A delayed set of template instructions.
   *
   * <p>
   * The set of instructions MUST be of the same template instance where this
   * fragment will be included.
   *
   * @see BaseTemplateDsl#include(FragmentAction)
   */
  @FunctionalInterface
  public interface FragmentLambda2<T1, T2> {

    /**
     * Invokes this set of instructions.
     */
    void invoke(T1 arg1, T2 arg2) throws Exception;

  }

  /**
   * A delayed set of template instructions.
   *
   * <p>
   * The set of instructions MUST be of the same template instance where this
   * fragment will be included.
   *
   * @see BaseTemplateDsl#include(FragmentAction)
   */
  @FunctionalInterface
  public interface FragmentLambda3<T1, T2, T3> {

    /**
     * Invokes this set of instructions.
     */
    void invoke(T1 arg1, T2 arg2, T3 arg3) throws Exception;

  }

  /**
   * A delayed set of template instructions.
   *
   * <p>
   * The set of instructions MUST be of the same template instance where this
   * fragment will be included.
   *
   * @see BaseTemplateDsl#include(FragmentAction)
   */
  @FunctionalInterface
  public interface FragmentLambda4<T1, T2, T3, T4> {

    /**
     * Invokes this set of instructions.
     */
    void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws Exception;

  }

  /*
   * Template related classes
   */

  /**
   * Formatter for printing an HTML template.
   */
  public sealed interface Formatter permits HtmlFormatter {

    void formatTo(Html.Document document, Appendable appendable) throws IOException;

    void formatTo(Html.Template template, Appendable appendable) throws IOException;

  }

  /**
   * Allow for creating <em>components</em>, objects that can render parts of a
   * larger HTML template.
   *
   * <p>
   * A component instance must be bound to a distinct template instance. This
   * template instance is called the <em>parent</em> of the component. Once
   * bound, a component can only be used to render parts for its parent.
   *
   * <p>
   * A component instance may be used to render instructions issued from its
   * parent template.
   */
  public non-sealed static abstract class Component extends TemplateBase {

    private final TemplateBase parent;

    /**
     * Creates a new component bound to the specified {@code parent} template.
     *
     * @param parent
     *        the template instance for which this component will be bound to.
     */
    public Component(TemplateBase parent) {
      this.parent = Check.notNull(parent, "parent == null");
    }

    @Override
    final HtmlCompiler $compiler() {
      return parent.$compiler();
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
  public non-sealed static abstract class Template extends TemplateBase {

    HtmlCompiler compiler;

    /**
     * Sole constructor.
     */
    protected Template() {}

    /**
     * Returns the HTML generated by this template.
     *
     * @return the HTML generated by this template
     */
    @Override
    public final String toString() {
      try {
        HtmlCompiler compiler;
        compiler = new HtmlCompiler();

        accept(compiler);

        HtmlDocument document;
        document = compiler.compile();

        StringBuilder out;
        out = new StringBuilder();

        HtmlFormatter.STANDARD.formatTo(document, out);

        return out.toString();
      } catch (IOException e) {
        throw new AssertionError("StringBuilder does not throw IOException", e);
      }
    }

    /**
     * Invoked immediately before the {@code render} method during the
     * generation of this template. It is meant to be overridden by subclasses.
     */
    protected void preRender() throws Exception {}

    /**
     * Defines the HTML code to be generated by this template.
     */
    protected abstract void render() throws Exception;

    private void tryToRender() {
      try {
        preRender();

        render();
      } catch (Html.RenderingException e) {
        throw e;
      } catch (Exception e) {
        throw new Html.RenderingException(e);
      }
    }

    final HtmlDocument compile(HtmlCompiler html) {
      accept(html);

      return html.compile();
    }

    public final void accept(Html.Compiler instance) {
      Check.state(compiler == null, "Concurrent evalution of a HtmlTemplate is not supported");

      try {
        compiler = (HtmlCompiler) instance;

        compiler.compilationBegin();

        tryToRender();

        compiler.compilationEnd();
      } finally {
        compiler = null;
      }
    }

    final void attribute(Html.AttributeName name, String value) {
      $compiler().attribute(name, value);
    }

    @Override
    final HtmlCompiler $compiler() {
      Check.state(compiler != null, "html not set");

      return compiler;
    }

  }

  public sealed static abstract class TemplateBase extends Html.TemplateElements permits Component, Template {

    TemplateBase() {}

    public final void plugin(Consumer<Html.Compiler> plugin) {
      HtmlCompiler compiler;
      compiler = $compiler();

      plugin.accept(compiler);
    }

    /**
     * Generates the {@code class} attribute by joining the specified values
     * with
     * a space character.
     *
     * @param v0 the first value
     * @param v1 the second value
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction className(String v0, String v1) {
      Check.notNull(v0, "v0 == null");
      Check.notNull(v1, "v1 == null");

      return $attributes().className(v0 + " " + v1);
    }

    /**
     * Generates the {@code class} attribute by joining the specified values
     * with
     * space characters.
     *
     * @param v0 the first value
     * @param v1 the second value
     * @param v2 the third value
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction className(String v0, String v1, String v2) {
      Check.notNull(v0, "v0 == null");
      Check.notNull(v1, "v1 == null");
      Check.notNull(v2, "v2 == null");

      return $attributes().className(v0 + " " + v1 + " " + v2);
    }

    /**
     * Generates the {@code class} attribute by joining the specified values
     * with
     * space characters.
     *
     * @param v0 the first value
     * @param v1 the second value
     * @param v2 the third value
     * @param v3 the fourth value
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction className(String v0, String v1, String v2, String v3) {
      Check.notNull(v0, "v0 == null");
      Check.notNull(v1, "v1 == null");
      Check.notNull(v2, "v2 == null");
      Check.notNull(v3, "v3 == null");

      return $attributes().className(v0 + " " + v1 + " " + v2 + " " + v3);
    }

    /**
     * Generates the {@code class} attribute by joining the specified values
     * with
     * space characters.
     *
     * @param values the values to be joined
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction className(String... values) {
      Check.notNull(values, "values == null");

      String value;
      value = Stream.of(values).collect(Collectors.joining(" "));

      return $attributes().className(value);
    }

    /**
     * Generates the {@code class} attribute by joining all of the lines in the
     * specified text block.
     *
     * @param text the text block value
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction classText(String text) {
      String[] lines;
      lines = text.split("\n+");

      String value;
      value = String.join(" ", lines);

      return $attributes().className(value);
    }

    protected final AttributeInstruction dataFrame(String name) {
      Check.notNull(name, "name == null");

      return $compiler().attribute(HtmlAttributeName.DATA_FRAME, name);
    }

    protected final AttributeInstruction dataFrame(String name, String value) {
      Check.notNull(name, "name == null");
      Check.notNull(value, "value == null");

      return $compiler().attribute(HtmlAttributeName.DATA_FRAME, name + ":" + value);
    }

    protected final DataOnInstruction dataOnClick(Script.Action action) {
      return dataOn(HtmlAttributeName.DATA_ON_CLICK, action);
    }

    protected final DataOnInstruction dataOnClick(Script.Action... actions) {
      return dataOn(HtmlAttributeName.DATA_ON_CLICK, actions);
    }

    protected final DataOnInstruction dataOnInput(Script.Action action) {
      return dataOn(HtmlAttributeName.DATA_ON_INPUT, action);
    }

    protected final DataOnInstruction dataOnInput(Script.Action... actions) {
      return dataOn(HtmlAttributeName.DATA_ON_INPUT, actions);
    }

    private final DataOnInstruction dataOn(AttributeName name, Script.Action action) {
      Check.notNull(action, "action == null");

      return $compiler().dataOn(name, action);
    }

    private final DataOnInstruction dataOn(AttributeName name, Script.Action... actions) {
      Check.notNull(actions, "actions == null");

      Script.Action value;
      value = Script.join(actions);

      return $compiler().dataOn(name, value);
    }

    /**
     * Includes a fragment into this template represented by the specified
     * lambda.
     *
     * <p>
     * The included fragment MUST only invoke methods this template instance. It
     * is common (but not required) for a fragment to be a method reference to
     * a private method of the template instance.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
     * "IncludeExample"}
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
    protected final FragmentInstruction f(FragmentLambda fragment) {
      return $compiler().include(fragment);
    }

    protected final <T1> FragmentInstruction f(FragmentLambda1<T1> fragment, T1 arg1) {
      return $compiler().include(fragment, arg1);
    }

    protected final <T1, T2> FragmentInstruction f(FragmentLambda2<T1, T2> fragment, T1 arg1, T2 arg2) {
      return $compiler().include(fragment, arg1, arg2);
    }

    protected final <T1, T2, T3> FragmentInstruction f(FragmentLambda3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
      return $compiler().include(fragment, arg1, arg2, arg3);
    }

    protected final <T1, T2, T3, T4> FragmentInstruction f(FragmentLambda4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      return $compiler().include(fragment, arg1, arg2, arg3, arg4);
    }

    /**
     * Flattens the specified instructions so that each of the specified
     * instructions is individually added, in order, to a receiving element.
     *
     * <p>
     * This is useful, for example, when creating {@link HtmlComponent}
     * instances.
     * The following Objectos HTML code:
     *
     * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
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
    protected final ElementInstruction flatten(Instruction... contents) {
      return $compiler().flatten(contents);
    }

    protected final ElementInstruction flattenNonNull(Instruction... contents) {
      return $compiler().flattenNonNull(contents);
    }

    /**
     * Includes a fragment into this template represented by the specified
     * lambda.
     *
     * <p>
     * The included fragment MUST only invoke methods this template instance. It
     * is common (but not required) for a fragment to be a method reference to
     * a private method of the template instance.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
     * "IncludeExample"}
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
    protected final FragmentInstruction include(FragmentLambda fragment) {
      return $compiler().include(fragment);
    }

    /**
     * Includes the specified template into this template.
     *
     * @param template
     *        the template to be included
     *
     * @return an instruction representing the inclusion of the template.
     */
    protected final FragmentInstruction include(Html.Template template) {
      Check.notNull(template, "template == null");

      try {
        HtmlCompiler api;
        api = $compiler();

        int index;
        index = api.fragmentBegin();

        template.compiler = api;

        template.tryToRender();

        api.fragmentEnd(index);
      } finally {
        template.compiler = null;
      }

      return Html.FRAGMENT;
    }

    protected final ElementInstruction nbsp() {
      return raw("&nbsp;");
    }

    protected final Html.Id nextId() {
      return $compiler().nextId();
    }

    /**
     * The no-op instruction.
     *
     * <p>
     * It can be used to conditionally add an attribute or element. For example,
     * the following Objectos HTML template:
     *
     * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
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
    protected final NoOpInstruction noop() {
      return Html.NOOP;
    }

    protected final NoOpInstruction noop(String ignored) {
      return Html.NOOP;
    }

    protected final ElementInstruction raw(String text) {
      return $compiler().raw(text);
    }

    /**
     * Generates a text node with the specified {@code text} value. The text
     * value
     * is escaped before being emitted to the output.
     *
     * <p>
     * The following Objectos HTML template:
     *
     * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region =
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
    protected final ElementInstruction t(String text) {
      return $compiler().text(text);
    }

    @Override
    final CompilerAttributes $attributes() {
      return $compiler();
    }

    @Override
    final CompilerElements $elements() {
      return $compiler();
    }

    abstract HtmlCompiler $compiler();

  }

  // exception types

  /**
   * Wraps a checked exception thrown during the rendering of an HTML template.
   */
  public static final class RenderingException extends RuntimeException {

    private static final long serialVersionUID = 4587764358138247203L;

    RenderingException(Exception cause) {
      super(cause);
    }

    /**
     * Returns the cause of this exception.
     *
     * @return the {@code Exception} which is the cause of this exception.
     */
    @Override
    public final Exception getCause() {
      return (Exception) super.getCause();
    }

  }

  private Html() {}

  public static Compiler createCompiler() {
    return new HtmlCompiler();
  }

  public static ClassName className(ClassName... classNames) {
    StringBuilder sb;
    sb = new StringBuilder();

    for (int i = 0, len = classNames.length; i < len; i++) {
      if (i != 0) {
        sb.append(' ');
      }

      ClassName cn;
      cn = classNames[i];

      String value;
      value = cn.value();

      sb.append(value);
    }

    String value;
    value = sb.toString();

    return new HtmlClassName(value);
  }

  public static ClassName className(ClassName className, String text) {
    StringBuilder sb;
    sb = new StringBuilder();

    sb.append(className.value());

    String[] lines;
    lines = text.split("\n+");

    for (var line : lines) {
      sb.append(' ');

      sb.append(line);
    }

    String value;
    value = sb.toString();

    return new HtmlClassName(value);
  }

  public static ClassName className(String value) {
    return new HtmlClassName(
        Check.notNull(value, "value == null")
    );
  }

  /**
   * Creates a new {@code ClassName} instance whose value is given by joining
   * the lines of specified text block around the space character.
   *
   * @param text
   *        the text block value
   *
   * @return a newly constructed {@code ClassName} instance
   */
  public static ClassName classText(String text) {
    String[] lines;
    lines = text.split("\n+");

    String joined;
    joined = String.join(" ", lines);

    return new HtmlClassName(joined);
  }

  public static Id id(String value) {
    Check.notNull(value, "value == null");

    return new HtmlId(value);
  }

  // @formatter:off

  /**
   * Represents an instruction that generates part of the output of an HTML
   * template.
   */
  public sealed interface Instruction {}

  /**
   * Class of instructions that are represented by methods of the template class.
   *
   * <p>
   * Instances of this interface MUST NOT be reused in a template.
   */
  public sealed interface MethodInstruction extends Instruction {}

  /**
   * Class of instructions that are allowed as arguments to template
   * methods that represent void elements.
   */
  public sealed interface VoidInstruction extends Instruction {}

  /**
   * An instruction to generate an HTML attribute in template.
   */
  public sealed interface AttributeInstruction extends MethodInstruction, VoidInstruction {}

  /**
   * An instruction to generate a {@code data-on-*} HTML attribute in a template.
   */
  public sealed interface DataOnInstruction extends MethodInstruction, VoidInstruction {}

  /**
   * An instruction to generate an HTML element in a template.
   */
  public sealed interface ElementInstruction extends MethodInstruction {}

  /**
   * An instruction to include an HTML fragment to a template.
   */
  public sealed interface FragmentInstruction extends MethodInstruction, VoidInstruction {}

  /**
   * The no-op instruction.
   */
  public sealed interface NoOpInstruction extends MethodInstruction, VoidInstruction {}

  sealed interface AttributeOrNoOp extends AttributeInstruction, DataOnInstruction, NoOpInstruction {}

  private static final class InstructionImpl
       implements
       AttributeOrNoOp,
       ElementInstruction,
       FragmentInstruction {}

  static final AttributeOrNoOp ATTRIBUTE = new InstructionImpl();
  static final ElementInstruction ELEMENT = new InstructionImpl();
  static final FragmentInstruction FRAGMENT = new InstructionImpl();
  static final AttributeOrNoOp NOOP = new InstructionImpl();

  /**
   * Class of instructions that are represented by object instances.
   *
   * <p>
   * Instances of this interface can be safely reused in multiple templates.
   */
  public sealed interface ObjectInstruction extends Instruction {}

  /**
   * An instruction to render an HTML attribute and its value.
   */
  public non-sealed interface AttributeObject extends ObjectInstruction, VoidInstruction {

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

  /**
   * An instruction to render an HTML {@code class} attribute.
   */
  public interface ClassName extends AttributeObject {

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

  record HtmlClassName(String value) implements ClassName {}

  /**
   * An instruction to render an HTML {@code id} attribute.
   */
  public interface Id extends AttributeObject {

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

  record HtmlId(String value) implements Id {}

  /**
   * Provides the HTML attributes template methods.
   */
  public sealed static abstract class TemplateAttributes {

    TemplateAttributes() {}

    /**
     * Generates the {@code accesskey} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction accesskey(String value) {
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
    protected final AttributeInstruction action(String value) {
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
    protected final AttributeInstruction align(String value) {
      return $attributes().align(value);
    }

    /**
     * Generates the {@code alignment-baseline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction alignmentBaseline(String value) {
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
    protected final AttributeInstruction alt(String value) {
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
    protected final AttributeInstruction ariaHidden(String value) {
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
    protected final AttributeInstruction ariaLabel(String value) {
      return $attributes().ariaLabel(value);
    }

    /**
     * Generates the {@code async} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction async() {
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
    protected final AttributeInstruction autocomplete(String value) {
      return $attributes().autocomplete(value);
    }

    /**
     * Generates the {@code autofocus} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction autofocus() {
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
    protected final AttributeInstruction baselineShift(String value) {
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
    protected final AttributeInstruction border(String value) {
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
    protected final AttributeInstruction cellpadding(String value) {
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
    protected final AttributeInstruction cellspacing(String value) {
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
    protected final AttributeInstruction charset(String value) {
      return $attributes().charset(value);
    }

    /**
     * Generates the {@code cite} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction cite(String value) {
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
    protected final AttributeInstruction className(String value) {
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
    protected final AttributeInstruction clipRule(String value) {
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
    protected final AttributeInstruction color(String value) {
      return $attributes().color(value);
    }

    /**
     * Generates the {@code color-interpolation} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction colorInterpolation(String value) {
      return $attributes().colorInterpolation(value);
    }

    /**
     * Generates the {@code color-interpolation-filters} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction colorInterpolationFilters(String value) {
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
    protected final AttributeInstruction cols(String value) {
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
    protected final AttributeInstruction content(String value) {
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
    protected final AttributeInstruction contenteditable(String value) {
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
    protected final AttributeInstruction crossorigin(String value) {
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
    protected final AttributeInstruction cursor(String value) {
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
    protected final AttributeInstruction d(String value) {
      return $attributes().d(value);
    }

    /**
     * Generates the {@code defer} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction defer() {
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
    protected final AttributeInstruction dir(String value) {
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
    protected final AttributeInstruction direction(String value) {
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
    protected final AttributeInstruction dirname(String value) {
      return $attributes().dirname(value);
    }

    /**
     * Generates the {@code disabled} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction disabled() {
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
    protected final AttributeInstruction display(String value) {
      return $attributes().display(value);
    }

    /**
     * Generates the {@code dominant-baseline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction dominantBaseline(String value) {
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
    protected final AttributeInstruction draggable(String value) {
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
    protected final AttributeInstruction enctype(String value) {
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
    protected final AttributeInstruction fill(String value) {
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
    protected final AttributeInstruction fillOpacity(String value) {
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
    protected final AttributeInstruction fillRule(String value) {
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
    protected final AttributeInstruction filter(String value) {
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
    protected final AttributeInstruction floodColor(String value) {
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
    protected final AttributeInstruction floodOpacity(String value) {
      return $attributes().floodOpacity(value);
    }

    /**
     * Generates the {@code font-family} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction fontFamily(String value) {
      return $attributes().fontFamily(value);
    }

    /**
     * Generates the {@code font-size} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction fontSize(String value) {
      return $attributes().fontSize(value);
    }

    /**
     * Generates the {@code font-size-adjust} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction fontSizeAdjust(String value) {
      return $attributes().fontSizeAdjust(value);
    }

    /**
     * Generates the {@code font-stretch} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction fontStretch(String value) {
      return $attributes().fontStretch(value);
    }

    /**
     * Generates the {@code font-style} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction fontStyle(String value) {
      return $attributes().fontStyle(value);
    }

    /**
     * Generates the {@code font-variant} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction fontVariant(String value) {
      return $attributes().fontVariant(value);
    }

    /**
     * Generates the {@code font-weight} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction fontWeight(String value) {
      return $attributes().fontWeight(value);
    }

    /**
     * Generates the {@code for} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction forAttr(String value) {
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
    protected final AttributeInstruction forElement(String value) {
      return $attributes().forElement(value);
    }

    /**
     * Generates the {@code glyph-orientation-horizontal} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction glyphOrientationHorizontal(String value) {
      return $attributes().glyphOrientationHorizontal(value);
    }

    /**
     * Generates the {@code glyph-orientation-vertical} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction glyphOrientationVertical(String value) {
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
    protected final AttributeInstruction height(String value) {
      return $attributes().height(value);
    }

    /**
     * Generates the {@code hidden} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction hidden() {
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
    protected final AttributeInstruction href(String value) {
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
    protected final AttributeInstruction httpEquiv(String value) {
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
    protected final AttributeInstruction id(String value) {
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
    protected final AttributeInstruction imageRendering(String value) {
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
    protected final AttributeInstruction integrity(String value) {
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
    protected final AttributeInstruction lang(String value) {
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
    protected final AttributeInstruction letterSpacing(String value) {
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
    protected final AttributeInstruction lightingColor(String value) {
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
    protected final AttributeInstruction markerEnd(String value) {
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
    protected final AttributeInstruction markerMid(String value) {
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
    protected final AttributeInstruction markerStart(String value) {
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
    protected final AttributeInstruction mask(String value) {
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
    protected final AttributeInstruction maskType(String value) {
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
    protected final AttributeInstruction maxlength(String value) {
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
    protected final AttributeInstruction media(String value) {
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
    protected final AttributeInstruction method(String value) {
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
    protected final AttributeInstruction minlength(String value) {
      return $attributes().minlength(value);
    }

    /**
     * Generates the {@code multiple} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction multiple() {
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
    protected final AttributeInstruction name(String value) {
      return $attributes().name(value);
    }

    /**
     * Generates the {@code nomodule} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction nomodule() {
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
    protected final AttributeInstruction onafterprint(String value) {
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
    protected final AttributeInstruction onbeforeprint(String value) {
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
    protected final AttributeInstruction onbeforeunload(String value) {
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
    protected final AttributeInstruction onclick(String value) {
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
    protected final AttributeInstruction onhashchange(String value) {
      return $attributes().onhashchange(value);
    }

    /**
     * Generates the {@code onlanguagechange} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction onlanguagechange(String value) {
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
    protected final AttributeInstruction onmessage(String value) {
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
    protected final AttributeInstruction onoffline(String value) {
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
    protected final AttributeInstruction ononline(String value) {
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
    protected final AttributeInstruction onpagehide(String value) {
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
    protected final AttributeInstruction onpageshow(String value) {
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
    protected final AttributeInstruction onpopstate(String value) {
      return $attributes().onpopstate(value);
    }

    /**
     * Generates the {@code onrejectionhandled} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction onrejectionhandled(String value) {
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
    protected final AttributeInstruction onstorage(String value) {
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
    protected final AttributeInstruction onsubmit(String value) {
      return $attributes().onsubmit(value);
    }

    /**
     * Generates the {@code onunhandledrejection} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction onunhandledrejection(String value) {
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
    protected final AttributeInstruction onunload(String value) {
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
    protected final AttributeInstruction opacity(String value) {
      return $attributes().opacity(value);
    }

    /**
     * Generates the {@code open} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction open() {
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
    protected final AttributeInstruction overflow(String value) {
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
    protected final AttributeInstruction paintOrder(String value) {
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
    protected final AttributeInstruction placeholder(String value) {
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
    protected final AttributeInstruction pointerEvents(String value) {
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
    protected final AttributeInstruction property(String value) {
      return $attributes().property(value);
    }

    /**
     * Generates the {@code readonly} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction readonly() {
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
    protected final AttributeInstruction referrerpolicy(String value) {
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
    protected final AttributeInstruction rel(String value) {
      return $attributes().rel(value);
    }

    /**
     * Generates the {@code required} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction required() {
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
    protected final AttributeInstruction rev(String value) {
      return $attributes().rev(value);
    }

    /**
     * Generates the {@code reversed} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction reversed() {
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
    protected final AttributeInstruction role(String value) {
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
    protected final AttributeInstruction rows(String value) {
      return $attributes().rows(value);
    }

    /**
     * Generates the {@code selected} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction selected() {
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
    protected final AttributeInstruction shapeRendering(String value) {
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
    protected final AttributeInstruction size(String value) {
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
    protected final AttributeInstruction sizes(String value) {
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
    protected final AttributeInstruction spellcheck(String value) {
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
    protected final AttributeInstruction src(String value) {
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
    protected final AttributeInstruction srcset(String value) {
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
    protected final AttributeInstruction start(String value) {
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
    protected final AttributeInstruction stopColor(String value) {
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
    protected final AttributeInstruction stopOpacity(String value) {
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
    protected final AttributeInstruction stroke(String value) {
      return $attributes().stroke(value);
    }

    /**
     * Generates the {@code stroke-dasharray} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction strokeDasharray(String value) {
      return $attributes().strokeDasharray(value);
    }

    /**
     * Generates the {@code stroke-dashoffset} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction strokeDashoffset(String value) {
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
    protected final AttributeInstruction strokeLinecap(String value) {
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
    protected final AttributeInstruction strokeLinejoin(String value) {
      return $attributes().strokeLinejoin(value);
    }

    /**
     * Generates the {@code stroke-miterlimit} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction strokeMiterlimit(String value) {
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
    protected final AttributeInstruction strokeOpacity(String value) {
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
    protected final AttributeInstruction strokeWidth(String value) {
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
    protected final AttributeInstruction inlineStyle(String value) {
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
    protected final AttributeInstruction tabindex(String value) {
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
    protected final AttributeInstruction target(String value) {
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
    protected final AttributeInstruction textAnchor(String value) {
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
    protected final AttributeInstruction textDecoration(String value) {
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
    protected final AttributeInstruction textOverflow(String value) {
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
    protected final AttributeInstruction textRendering(String value) {
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
    protected final AttributeInstruction transform(String value) {
      return $attributes().transform(value);
    }

    /**
     * Generates the {@code transform-origin} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final AttributeInstruction transformOrigin(String value) {
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
    protected final AttributeInstruction translate(String value) {
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
    protected final AttributeInstruction type(String value) {
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
    protected final AttributeInstruction unicodeBidi(String value) {
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
    protected final AttributeInstruction value(String value) {
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
    protected final AttributeInstruction vectorEffect(String value) {
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
    protected final AttributeInstruction viewBox(String value) {
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
    protected final AttributeInstruction visibility(String value) {
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
    protected final AttributeInstruction whiteSpace(String value) {
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
    protected final AttributeInstruction width(String value) {
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
    protected final AttributeInstruction wordSpacing(String value) {
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
    protected final AttributeInstruction wrap(String value) {
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
    protected final AttributeInstruction writingMode(String value) {
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
    protected final AttributeInstruction xmlns(String value) {
      return $attributes().xmlns(value);
    }

    abstract CompilerAttributes $attributes();

  }

  /**
   * Provides the HTML attributes compiler methods.
   */
  public sealed interface CompilerAttributes permits Compiler {

    /**
     * Generates the {@code accesskey} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction accesskey(String value);

    /**
     * Generates the {@code action} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction action(String value);

    /**
     * Generates the {@code align} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction align(String value);

    /**
     * Generates the {@code alignment-baseline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction alignmentBaseline(String value);

    /**
     * Generates the {@code alt} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction alt(String value);

    /**
     * Generates the {@code aria-hidden} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction ariaHidden(String value);

    /**
     * Generates the {@code aria-label} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction ariaLabel(String value);

    /**
     * Generates the {@code async} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction async();

    /**
     * Generates the {@code autocomplete} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction autocomplete(String value);

    /**
     * Generates the {@code autofocus} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction autofocus();

    /**
     * Generates the {@code baseline-shift} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction baselineShift(String value);

    /**
     * Generates the {@code border} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction border(String value);

    /**
     * Generates the {@code cellpadding} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction cellpadding(String value);

    /**
     * Generates the {@code cellspacing} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction cellspacing(String value);

    /**
     * Generates the {@code charset} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction charset(String value);

    /**
     * Generates the {@code cite} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction cite(String value);

    /**
     * Generates the {@code class} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction className(String value);

    /**
     * Generates the {@code clip-rule} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction clipRule(String value);

    /**
     * Generates the {@code color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction color(String value);

    /**
     * Generates the {@code color-interpolation} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction colorInterpolation(String value);

    /**
     * Generates the {@code color-interpolation-filters} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction colorInterpolationFilters(String value);

    /**
     * Generates the {@code cols} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction cols(String value);

    /**
     * Generates the {@code content} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction content(String value);

    /**
     * Generates the {@code contenteditable} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction contenteditable(String value);

    /**
     * Generates the {@code crossorigin} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction crossorigin(String value);

    /**
     * Generates the {@code cursor} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction cursor(String value);

    /**
     * Generates the {@code d} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction d(String value);

    /**
     * Generates the {@code defer} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction defer();

    /**
     * Generates the {@code dir} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction dir(String value);

    /**
     * Generates the {@code direction} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction direction(String value);

    /**
     * Generates the {@code dirname} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction dirname(String value);

    /**
     * Generates the {@code disabled} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction disabled();

    /**
     * Generates the {@code display} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction display(String value);

    /**
     * Generates the {@code dominant-baseline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction dominantBaseline(String value);

    /**
     * Generates the {@code draggable} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction draggable(String value);

    /**
     * Generates the {@code enctype} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction enctype(String value);

    /**
     * Generates the {@code fill} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction fill(String value);

    /**
     * Generates the {@code fill-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction fillOpacity(String value);

    /**
     * Generates the {@code fill-rule} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction fillRule(String value);

    /**
     * Generates the {@code filter} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction filter(String value);

    /**
     * Generates the {@code flood-color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction floodColor(String value);

    /**
     * Generates the {@code flood-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction floodOpacity(String value);

    /**
     * Generates the {@code font-family} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction fontFamily(String value);

    /**
     * Generates the {@code font-size} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction fontSize(String value);

    /**
     * Generates the {@code font-size-adjust} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction fontSizeAdjust(String value);

    /**
     * Generates the {@code font-stretch} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction fontStretch(String value);

    /**
     * Generates the {@code font-style} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction fontStyle(String value);

    /**
     * Generates the {@code font-variant} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction fontVariant(String value);

    /**
     * Generates the {@code font-weight} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction fontWeight(String value);

    /**
     * Generates the {@code for} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction forAttr(String value);

    /**
     * Generates the {@code for} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction forElement(String value);

    /**
     * Generates the {@code glyph-orientation-horizontal} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction glyphOrientationHorizontal(String value);

    /**
     * Generates the {@code glyph-orientation-vertical} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction glyphOrientationVertical(String value);

    /**
     * Generates the {@code height} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction height(String value);

    /**
     * Generates the {@code hidden} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction hidden();

    /**
     * Generates the {@code href} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction href(String value);

    /**
     * Generates the {@code http-equiv} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction httpEquiv(String value);

    /**
     * Generates the {@code id} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction id(String value);

    /**
     * Generates the {@code image-rendering} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction imageRendering(String value);

    /**
     * Generates the {@code integrity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction integrity(String value);

    /**
     * Generates the {@code lang} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction lang(String value);

    /**
     * Generates the {@code letter-spacing} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction letterSpacing(String value);

    /**
     * Generates the {@code lighting-color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction lightingColor(String value);

    /**
     * Generates the {@code marker-end} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction markerEnd(String value);

    /**
     * Generates the {@code marker-mid} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction markerMid(String value);

    /**
     * Generates the {@code marker-start} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction markerStart(String value);

    /**
     * Generates the {@code mask} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction mask(String value);

    /**
     * Generates the {@code mask-type} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction maskType(String value);

    /**
     * Generates the {@code maxlength} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction maxlength(String value);

    /**
     * Generates the {@code media} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction media(String value);

    /**
     * Generates the {@code method} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction method(String value);

    /**
     * Generates the {@code minlength} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction minlength(String value);

    /**
     * Generates the {@code multiple} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction multiple();

    /**
     * Generates the {@code name} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction name(String value);

    /**
     * Generates the {@code nomodule} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction nomodule();

    /**
     * Generates the {@code onafterprint} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onafterprint(String value);

    /**
     * Generates the {@code onbeforeprint} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onbeforeprint(String value);

    /**
     * Generates the {@code onbeforeunload} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onbeforeunload(String value);

    /**
     * Generates the {@code onclick} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onclick(String value);

    /**
     * Generates the {@code onhashchange} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onhashchange(String value);

    /**
     * Generates the {@code onlanguagechange} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onlanguagechange(String value);

    /**
     * Generates the {@code onmessage} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onmessage(String value);

    /**
     * Generates the {@code onoffline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onoffline(String value);

    /**
     * Generates the {@code ononline} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction ononline(String value);

    /**
     * Generates the {@code onpagehide} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onpagehide(String value);

    /**
     * Generates the {@code onpageshow} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onpageshow(String value);

    /**
     * Generates the {@code onpopstate} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onpopstate(String value);

    /**
     * Generates the {@code onrejectionhandled} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onrejectionhandled(String value);

    /**
     * Generates the {@code onstorage} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onstorage(String value);

    /**
     * Generates the {@code onsubmit} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onsubmit(String value);

    /**
     * Generates the {@code onunhandledrejection} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onunhandledrejection(String value);

    /**
     * Generates the {@code onunload} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction onunload(String value);

    /**
     * Generates the {@code opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction opacity(String value);

    /**
     * Generates the {@code open} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction open();

    /**
     * Generates the {@code overflow} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction overflow(String value);

    /**
     * Generates the {@code paint-order} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction paintOrder(String value);

    /**
     * Generates the {@code placeholder} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction placeholder(String value);

    /**
     * Generates the {@code pointer-events} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction pointerEvents(String value);

    /**
     * Generates the {@code property} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction property(String value);

    /**
     * Generates the {@code readonly} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction readonly();

    /**
     * Generates the {@code referrerpolicy} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction referrerpolicy(String value);

    /**
     * Generates the {@code rel} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction rel(String value);

    /**
     * Generates the {@code required} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction required();

    /**
     * Generates the {@code rev} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction rev(String value);

    /**
     * Generates the {@code reversed} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction reversed();

    /**
     * Generates the {@code role} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction role(String value);

    /**
     * Generates the {@code rows} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction rows(String value);

    /**
     * Generates the {@code selected} boolean attribute.
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction selected();

    /**
     * Generates the {@code shape-rendering} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction shapeRendering(String value);

    /**
     * Generates the {@code size} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction size(String value);

    /**
     * Generates the {@code sizes} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction sizes(String value);

    /**
     * Generates the {@code spellcheck} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction spellcheck(String value);

    /**
     * Generates the {@code src} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction src(String value);

    /**
     * Generates the {@code srcset} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction srcset(String value);

    /**
     * Generates the {@code start} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction start(String value);

    /**
     * Generates the {@code stop-color} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction stopColor(String value);

    /**
     * Generates the {@code stop-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction stopOpacity(String value);

    /**
     * Generates the {@code stroke} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction stroke(String value);

    /**
     * Generates the {@code stroke-dasharray} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction strokeDasharray(String value);

    /**
     * Generates the {@code stroke-dashoffset} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction strokeDashoffset(String value);

    /**
     * Generates the {@code stroke-linecap} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction strokeLinecap(String value);

    /**
     * Generates the {@code stroke-linejoin} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction strokeLinejoin(String value);

    /**
     * Generates the {@code stroke-miterlimit} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction strokeMiterlimit(String value);

    /**
     * Generates the {@code stroke-opacity} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction strokeOpacity(String value);

    /**
     * Generates the {@code stroke-width} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction strokeWidth(String value);

    /**
     * Generates the {@code style} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction inlineStyle(String value);

    /**
     * Generates the {@code tabindex} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction tabindex(String value);

    /**
     * Generates the {@code target} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction target(String value);

    /**
     * Generates the {@code text-anchor} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction textAnchor(String value);

    /**
     * Generates the {@code text-decoration} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction textDecoration(String value);

    /**
     * Generates the {@code text-overflow} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction textOverflow(String value);

    /**
     * Generates the {@code text-rendering} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction textRendering(String value);

    /**
     * Generates the {@code transform} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction transform(String value);

    /**
     * Generates the {@code transform-origin} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction transformOrigin(String value);

    /**
     * Generates the {@code translate} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction translate(String value);

    /**
     * Generates the {@code type} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction type(String value);

    /**
     * Generates the {@code unicode-bidi} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction unicodeBidi(String value);

    /**
     * Generates the {@code value} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction value(String value);

    /**
     * Generates the {@code vector-effect} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction vectorEffect(String value);

    /**
     * Generates the {@code viewBox} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction viewBox(String value);

    /**
     * Generates the {@code visibility} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction visibility(String value);

    /**
     * Generates the {@code white-space} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction whiteSpace(String value);

    /**
     * Generates the {@code width} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction width(String value);

    /**
     * Generates the {@code word-spacing} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction wordSpacing(String value);

    /**
     * Generates the {@code wrap} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction wrap(String value);

    /**
     * Generates the {@code writing-mode} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction writingMode(String value);

    /**
     * Generates the {@code xmlns} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    AttributeInstruction xmlns(String value);

  }

  /**
   * Provides the HTML elements template methods.
   */
  public non-sealed static abstract class TemplateElements extends TemplateAttributes {

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
    protected final ElementInstruction a(Instruction... contents) {
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
    protected final ElementInstruction a(String text) {
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
    protected final ElementInstruction abbr(Instruction... contents) {
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
    protected final ElementInstruction abbr(String text) {
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
    protected final ElementInstruction article(Instruction... contents) {
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
    protected final ElementInstruction article(String text) {
      return $elements().article(text);
    }

    /**
     * Generates the {@code b} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    protected final ElementInstruction b(Instruction... contents) {
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
    protected final ElementInstruction b(String text) {
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
    protected final ElementInstruction blockquote(Instruction... contents) {
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
    protected final ElementInstruction blockquote(String text) {
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
    protected final ElementInstruction body(Instruction... contents) {
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
    protected final ElementInstruction body(String text) {
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
    protected final ElementInstruction br(VoidInstruction... contents) {
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
    protected final ElementInstruction button(Instruction... contents) {
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
    protected final ElementInstruction button(String text) {
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
    protected final ElementInstruction clipPath(Instruction... contents) {
      return $elements().clipPath(contents);
    }

    /**
     * Generates the {@code clipPath} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    protected final ElementInstruction clipPath(String text) {
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
    protected final ElementInstruction code(Instruction... contents) {
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
    protected final ElementInstruction code(String text) {
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
    protected final ElementInstruction dd(Instruction... contents) {
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
    protected final ElementInstruction dd(String text) {
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
    protected final ElementInstruction defs(Instruction... contents) {
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
    protected final ElementInstruction defs(String text) {
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
    protected final ElementInstruction details(Instruction... contents) {
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
    protected final ElementInstruction details(String text) {
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
    protected final ElementInstruction div(Instruction... contents) {
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
    protected final ElementInstruction div(String text) {
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
    protected final ElementInstruction dl(Instruction... contents) {
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
    protected final ElementInstruction dl(String text) {
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
    protected final ElementInstruction dt(Instruction... contents) {
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
    protected final ElementInstruction dt(String text) {
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
    protected final ElementInstruction em(Instruction... contents) {
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
    protected final ElementInstruction em(String text) {
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
    protected final ElementInstruction fieldset(Instruction... contents) {
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
    protected final ElementInstruction fieldset(String text) {
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
    protected final ElementInstruction figure(Instruction... contents) {
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
    protected final ElementInstruction figure(String text) {
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
    protected final ElementInstruction footer(Instruction... contents) {
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
    protected final ElementInstruction footer(String text) {
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
    protected final ElementInstruction form(Instruction... contents) {
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
    protected final ElementInstruction form(String text) {
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
    protected final ElementInstruction g(Instruction... contents) {
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
    protected final ElementInstruction g(String text) {
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
    protected final ElementInstruction h1(Instruction... contents) {
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
    protected final ElementInstruction h1(String text) {
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
    protected final ElementInstruction h2(Instruction... contents) {
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
    protected final ElementInstruction h2(String text) {
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
    protected final ElementInstruction h3(Instruction... contents) {
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
    protected final ElementInstruction h3(String text) {
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
    protected final ElementInstruction h4(Instruction... contents) {
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
    protected final ElementInstruction h4(String text) {
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
    protected final ElementInstruction h5(Instruction... contents) {
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
    protected final ElementInstruction h5(String text) {
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
    protected final ElementInstruction h6(Instruction... contents) {
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
    protected final ElementInstruction h6(String text) {
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
    protected final ElementInstruction head(Instruction... contents) {
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
    protected final ElementInstruction head(String text) {
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
    protected final ElementInstruction header(Instruction... contents) {
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
    protected final ElementInstruction header(String text) {
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
    protected final ElementInstruction hgroup(Instruction... contents) {
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
    protected final ElementInstruction hgroup(String text) {
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
    protected final ElementInstruction hr(VoidInstruction... contents) {
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
    protected final ElementInstruction html(Instruction... contents) {
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
    protected final ElementInstruction html(String text) {
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
    protected final ElementInstruction img(VoidInstruction... contents) {
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
    protected final ElementInstruction input(VoidInstruction... contents) {
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
    protected final ElementInstruction kbd(Instruction... contents) {
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
    protected final ElementInstruction kbd(String text) {
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
    protected final ElementInstruction label(Instruction... contents) {
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
    protected final ElementInstruction label(String text) {
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
    protected final ElementInstruction legend(Instruction... contents) {
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
    protected final ElementInstruction legend(String text) {
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
    protected final ElementInstruction li(Instruction... contents) {
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
    protected final ElementInstruction li(String text) {
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
    protected final ElementInstruction link(VoidInstruction... contents) {
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
    protected final ElementInstruction main(Instruction... contents) {
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
    protected final ElementInstruction main(String text) {
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
    protected final ElementInstruction menu(Instruction... contents) {
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
    protected final ElementInstruction menu(String text) {
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
    protected final ElementInstruction meta(VoidInstruction... contents) {
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
    protected final ElementInstruction nav(Instruction... contents) {
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
    protected final ElementInstruction nav(String text) {
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
    protected final ElementInstruction ol(Instruction... contents) {
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
    protected final ElementInstruction ol(String text) {
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
    protected final ElementInstruction optgroup(Instruction... contents) {
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
    protected final ElementInstruction optgroup(String text) {
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
    protected final ElementInstruction option(Instruction... contents) {
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
    protected final ElementInstruction option(String text) {
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
    protected final ElementInstruction p(Instruction... contents) {
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
    protected final ElementInstruction p(String text) {
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
    protected final ElementInstruction path(Instruction... contents) {
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
    protected final ElementInstruction path(String text) {
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
    protected final ElementInstruction pre(Instruction... contents) {
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
    protected final ElementInstruction pre(String text) {
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
    protected final ElementInstruction progress(Instruction... contents) {
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
    protected final ElementInstruction progress(String text) {
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
    protected final ElementInstruction samp(Instruction... contents) {
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
    protected final ElementInstruction samp(String text) {
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
    protected final ElementInstruction script(Instruction... contents) {
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
    protected final ElementInstruction script(String text) {
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
    protected final ElementInstruction section(Instruction... contents) {
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
    protected final ElementInstruction section(String text) {
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
    protected final ElementInstruction select(Instruction... contents) {
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
    protected final ElementInstruction select(String text) {
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
    protected final ElementInstruction small(Instruction... contents) {
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
    protected final ElementInstruction small(String text) {
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
    protected final ElementInstruction span(Instruction... contents) {
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
    protected final ElementInstruction span(String text) {
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
    protected final ElementInstruction strong(Instruction... contents) {
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
    protected final ElementInstruction strong(String text) {
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
    protected final ElementInstruction style(Instruction... contents) {
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
    protected final ElementInstruction style(String text) {
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
    protected final ElementInstruction sub(Instruction... contents) {
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
    protected final ElementInstruction sub(String text) {
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
    protected final ElementInstruction summary(Instruction... contents) {
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
    protected final ElementInstruction summary(String text) {
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
    protected final ElementInstruction sup(Instruction... contents) {
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
    protected final ElementInstruction sup(String text) {
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
    protected final ElementInstruction svg(Instruction... contents) {
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
    protected final ElementInstruction svg(String text) {
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
    protected final ElementInstruction table(Instruction... contents) {
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
    protected final ElementInstruction table(String text) {
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
    protected final ElementInstruction tbody(Instruction... contents) {
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
    protected final ElementInstruction tbody(String text) {
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
    protected final ElementInstruction td(Instruction... contents) {
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
    protected final ElementInstruction td(String text) {
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
    protected final ElementInstruction template(Instruction... contents) {
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
    protected final ElementInstruction template(String text) {
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
    protected final ElementInstruction textarea(Instruction... contents) {
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
    protected final ElementInstruction textarea(String text) {
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
    protected final ElementInstruction th(Instruction... contents) {
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
    protected final ElementInstruction th(String text) {
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
    protected final ElementInstruction thead(Instruction... contents) {
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
    protected final ElementInstruction thead(String text) {
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
    protected final ElementInstruction title(Instruction... contents) {
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
    protected final ElementInstruction title(String text) {
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
    protected final ElementInstruction tr(Instruction... contents) {
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
    protected final ElementInstruction tr(String text) {
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
    protected final ElementInstruction ul(Instruction... contents) {
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
    protected final ElementInstruction ul(String text) {
      return $elements().ul(text);
    }

    abstract CompilerElements $elements();

  }

  /**
   * Provides the HTML elements compiler methods.
   */
  public sealed interface CompilerElements permits Compiler {

    /**
     * Generates the {@code <!DOCTYPE html>} doctype.
     */
    void doctype();

    /**
     * Generates the {@code a} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction a(Instruction... contents);

    /**
     * Generates the {@code a} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction a(String text);

    /**
     * Generates the {@code abbr} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction abbr(Instruction... contents);

    /**
     * Generates the {@code abbr} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction abbr(String text);

    /**
     * Generates the {@code article} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction article(Instruction... contents);

    /**
     * Generates the {@code article} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction article(String text);

    /**
     * Generates the {@code b} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction b(Instruction... contents);

    /**
     * Generates the {@code b} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction b(String text);

    /**
     * Generates the {@code blockquote} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction blockquote(Instruction... contents);

    /**
     * Generates the {@code blockquote} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction blockquote(String text);

    /**
     * Generates the {@code body} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction body(Instruction... contents);

    /**
     * Generates the {@code body} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction body(String text);

    /**
     * Generates the {@code br} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction br(VoidInstruction... contents);

    /**
     * Generates the {@code button} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction button(Instruction... contents);

    /**
     * Generates the {@code button} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction button(String text);

    /**
     * Generates the {@code clipPath} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction clipPath(Instruction... contents);

    /**
     * Generates the {@code clipPath} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    ElementInstruction clipPath(String text);

    /**
     * Generates the {@code code} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction code(Instruction... contents);

    /**
     * Generates the {@code code} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction code(String text);

    /**
     * Generates the {@code dd} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction dd(Instruction... contents);

    /**
     * Generates the {@code dd} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction dd(String text);

    /**
     * Generates the {@code defs} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction defs(Instruction... contents);

    /**
     * Generates the {@code defs} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction defs(String text);

    /**
     * Generates the {@code details} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction details(Instruction... contents);

    /**
     * Generates the {@code details} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction details(String text);

    /**
     * Generates the {@code div} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction div(Instruction... contents);

    /**
     * Generates the {@code div} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction div(String text);

    /**
     * Generates the {@code dl} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction dl(Instruction... contents);

    /**
     * Generates the {@code dl} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction dl(String text);

    /**
     * Generates the {@code dt} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction dt(Instruction... contents);

    /**
     * Generates the {@code dt} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction dt(String text);

    /**
     * Generates the {@code em} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction em(Instruction... contents);

    /**
     * Generates the {@code em} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction em(String text);

    /**
     * Generates the {@code fieldset} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction fieldset(Instruction... contents);

    /**
     * Generates the {@code fieldset} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction fieldset(String text);

    /**
     * Generates the {@code figure} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction figure(Instruction... contents);

    /**
     * Generates the {@code figure} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction figure(String text);

    /**
     * Generates the {@code footer} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction footer(Instruction... contents);

    /**
     * Generates the {@code footer} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction footer(String text);

    /**
     * Generates the {@code form} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction form(Instruction... contents);

    /**
     * Generates the {@code form} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    ElementInstruction form(String text);

    /**
     * Generates the {@code g} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction g(Instruction... contents);

    /**
     * Generates the {@code g} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction g(String text);

    /**
     * Generates the {@code h1} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h1(Instruction... contents);

    /**
     * Generates the {@code h1} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h1(String text);

    /**
     * Generates the {@code h2} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h2(Instruction... contents);

    /**
     * Generates the {@code h2} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h2(String text);

    /**
     * Generates the {@code h3} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h3(Instruction... contents);

    /**
     * Generates the {@code h3} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h3(String text);

    /**
     * Generates the {@code h4} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h4(Instruction... contents);

    /**
     * Generates the {@code h4} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h4(String text);

    /**
     * Generates the {@code h5} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h5(Instruction... contents);

    /**
     * Generates the {@code h5} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h5(String text);

    /**
     * Generates the {@code h6} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h6(Instruction... contents);

    /**
     * Generates the {@code h6} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction h6(String text);

    /**
     * Generates the {@code head} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction head(Instruction... contents);

    /**
     * Generates the {@code head} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction head(String text);

    /**
     * Generates the {@code header} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction header(Instruction... contents);

    /**
     * Generates the {@code header} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction header(String text);

    /**
     * Generates the {@code hgroup} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction hgroup(Instruction... contents);

    /**
     * Generates the {@code hgroup} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction hgroup(String text);

    /**
     * Generates the {@code hr} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction hr(VoidInstruction... contents);

    /**
     * Generates the {@code html} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction html(Instruction... contents);

    /**
     * Generates the {@code html} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction html(String text);

    /**
     * Generates the {@code img} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction img(VoidInstruction... contents);

    /**
     * Generates the {@code input} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction input(VoidInstruction... contents);

    /**
     * Generates the {@code kbd} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction kbd(Instruction... contents);

    /**
     * Generates the {@code kbd} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction kbd(String text);

    /**
     * Generates the {@code label} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction label(Instruction... contents);

    /**
     * Generates the {@code label} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    ElementInstruction label(String text);

    /**
     * Generates the {@code legend} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction legend(Instruction... contents);

    /**
     * Generates the {@code legend} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction legend(String text);

    /**
     * Generates the {@code li} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction li(Instruction... contents);

    /**
     * Generates the {@code li} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction li(String text);

    /**
     * Generates the {@code link} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction link(VoidInstruction... contents);

    /**
     * Generates the {@code main} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction main(Instruction... contents);

    /**
     * Generates the {@code main} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction main(String text);

    /**
     * Generates the {@code menu} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction menu(Instruction... contents);

    /**
     * Generates the {@code menu} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction menu(String text);

    /**
     * Generates the {@code meta} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction meta(VoidInstruction... contents);

    /**
     * Generates the {@code nav} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction nav(Instruction... contents);

    /**
     * Generates the {@code nav} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction nav(String text);

    /**
     * Generates the {@code ol} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction ol(Instruction... contents);

    /**
     * Generates the {@code ol} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction ol(String text);

    /**
     * Generates the {@code optgroup} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction optgroup(Instruction... contents);

    /**
     * Generates the {@code optgroup} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction optgroup(String text);

    /**
     * Generates the {@code option} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction option(Instruction... contents);

    /**
     * Generates the {@code option} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction option(String text);

    /**
     * Generates the {@code p} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction p(Instruction... contents);

    /**
     * Generates the {@code p} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction p(String text);

    /**
     * Generates the {@code path} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction path(Instruction... contents);

    /**
     * Generates the {@code path} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction path(String text);

    /**
     * Generates the {@code pre} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction pre(Instruction... contents);

    /**
     * Generates the {@code pre} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction pre(String text);

    /**
     * Generates the {@code progress} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction progress(Instruction... contents);

    /**
     * Generates the {@code progress} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction progress(String text);

    /**
     * Generates the {@code samp} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction samp(Instruction... contents);

    /**
     * Generates the {@code samp} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction samp(String text);

    /**
     * Generates the {@code script} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction script(Instruction... contents);

    /**
     * Generates the {@code script} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction script(String text);

    /**
     * Generates the {@code section} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction section(Instruction... contents);

    /**
     * Generates the {@code section} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction section(String text);

    /**
     * Generates the {@code select} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction select(Instruction... contents);

    /**
     * Generates the {@code select} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction select(String text);

    /**
     * Generates the {@code small} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction small(Instruction... contents);

    /**
     * Generates the {@code small} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction small(String text);

    /**
     * Generates the {@code span} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction span(Instruction... contents);

    /**
     * Generates the {@code span} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction span(String text);

    /**
     * Generates the {@code strong} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction strong(Instruction... contents);

    /**
     * Generates the {@code strong} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction strong(String text);

    /**
     * Generates the {@code style} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction style(Instruction... contents);

    /**
     * Generates the {@code style} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction style(String text);

    /**
     * Generates the {@code sub} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction sub(Instruction... contents);

    /**
     * Generates the {@code sub} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction sub(String text);

    /**
     * Generates the {@code summary} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction summary(Instruction... contents);

    /**
     * Generates the {@code summary} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction summary(String text);

    /**
     * Generates the {@code sup} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction sup(Instruction... contents);

    /**
     * Generates the {@code sup} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction sup(String text);

    /**
     * Generates the {@code svg} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction svg(Instruction... contents);

    /**
     * Generates the {@code svg} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction svg(String text);

    /**
     * Generates the {@code table} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction table(Instruction... contents);

    /**
     * Generates the {@code table} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction table(String text);

    /**
     * Generates the {@code tbody} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction tbody(Instruction... contents);

    /**
     * Generates the {@code tbody} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction tbody(String text);

    /**
     * Generates the {@code td} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction td(Instruction... contents);

    /**
     * Generates the {@code td} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction td(String text);

    /**
     * Generates the {@code template} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction template(Instruction... contents);

    /**
     * Generates the {@code template} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction template(String text);

    /**
     * Generates the {@code textarea} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction textarea(Instruction... contents);

    /**
     * Generates the {@code textarea} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction textarea(String text);

    /**
     * Generates the {@code th} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction th(Instruction... contents);

    /**
     * Generates the {@code th} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction th(String text);

    /**
     * Generates the {@code thead} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction thead(Instruction... contents);

    /**
     * Generates the {@code thead} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction thead(String text);

    /**
     * Generates the {@code title} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction title(Instruction... contents);

    /**
     * Generates the {@code title} attribute or element with the specified text.
     *
     * @param text
     *        the text value of this attribute or element
     *
     * @return an instruction representing this attribute or element.
     */
    ElementInstruction title(String text);

    /**
     * Generates the {@code tr} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction tr(Instruction... contents);

    /**
     * Generates the {@code tr} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction tr(String text);

    /**
     * Generates the {@code ul} element with the specified content.
     *
     * @param contents
     *        the attributes and children of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction ul(Instruction... contents);

    /**
     * Generates the {@code ul} element with the specified text.
     *
     * @param text
     *        the text value of this element
     *
     * @return an instruction representing this element.
     */
    ElementInstruction ul(String text);

  }

  // @formatter:on

}
