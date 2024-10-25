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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The <strong>Objectos HTML</strong> main class.
 */
public final class Html extends HtmlRecorder {

  /*
   * name types
   */

  /**
   * The name of an HTML attribute.
   */
  public sealed interface AttributeName extends HtmlAttributeNameGenerated permits HtmlAttributeName {

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
   * A DOM-like view of a {@link Html.Template} which allows for a one-pass,
   * one-direction traversal of the HTML document.
   */
  public sealed interface Dom {

    /**
     * An attribute of a {@link Html.Template}.
     */
    sealed interface Attribute {

      String name();

      boolean booleanAttribute();

      boolean singleQuoted();

      String value();

      default boolean hasName(String name) {
        return name().equals(name);
      }

    }

    /**
     * The document type of a {@link Html.Template}.
     */
    sealed interface DocumentType extends Node {}

    /**
     * An element of a {@link Html.Template}.
     */
    sealed interface Element extends Node {

      Lang.IterableOnce<Dom.Attribute> attributes();

      boolean isVoid();

      String name();

      Lang.IterableOnce<Node> nodes();

      default boolean hasName(String name) {
        return name().equals(name);
      }

    }

    /**
     * A node of a {@link Html.Template}.
     */
    sealed interface Node {}

    /**
     * A raw node of a {@link Html.Template}.
     */
    sealed interface Raw extends Node {

      String value();

    }

    /**
     * A text node of a {@link Html.Template}.
     */
    sealed interface Text extends Node {

      /**
       * Returns the value of the testable attribute if one was defined in this
       * element or {@code null} if there was no testable attribute.
       *
       * @return the value of testable attribute
       */
      String testable();

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
      Html html;
      html = new Html();

      template.accept(html);

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

  public interface ElementComponent {

    Html.Instruction render();

  }

  /**
   * A delayed set of template instructions.
   *
   * <p>
   * The set of instructions MUST be of the same template instance where this
   * fragment will be included.
   *
   * @see Html#renderTemplate(Html.FragmentLambda)
   */
  public sealed interface Fragment {

    /**
     * A fragment that takes no arguments.
     */
    @FunctionalInterface
    non-sealed interface Of0 extends Fragment {

      /**
       * Invokes this set of instructions.
       *
       * @throws Exception
       *         if an error occurs during the execution of this fragment
       */
      void invoke() throws Exception;

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
       *
       * @throws Exception
       *         if an error occurs during the execution of this fragment
       */
      void invoke(T1 arg1) throws Exception;

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
       *
       * @throws Exception
       *         if an error occurs during the execution of this fragment
       */
      void invoke(T1 arg1, T2 arg2) throws Exception;

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
      void invoke(T1 arg1, T2 arg2, T3 arg3) throws Exception;

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
      void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws Exception;

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
    sealed interface NoOp extends AsMethod, OfVoid {}

  }

  sealed interface AttributeOrNoOp extends Instruction.OfAttribute, Instruction.OfDataOn, Instruction.NoOp {}

  private static final class HtmlInstruction
      implements
      AttributeOrNoOp,
      Html.Instruction.OfElement,
      Html.Instruction.OfFragment {}

  static final Html.AttributeOrNoOp ATTRIBUTE = new HtmlInstruction();
  static final Html.Instruction.OfElement ELEMENT = new HtmlInstruction();
  static final Html.Instruction.OfFragment FRAGMENT = new HtmlInstruction();
  static final Html.AttributeOrNoOp NOOP = new HtmlInstruction();

  /*
   * Template related classes
   */

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
    final Html $html() {
      return parent.$html();
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
  public non-sealed static abstract class Template extends TemplateBase implements Lang.CharWritable {

    Html html;

    /**
     * Sole constructor.
     */
    protected Template() {}

    public final String testableText() {
      Html html;
      html = new Html();

      accept(html);

      return html.testableText();
    }

    /**
     * Returns the HTML generated by this template.
     *
     * @return the HTML generated by this template
     */
    @Override
    public final String toString() {
      Html html;
      html = new Html();

      accept(html);

      return html.toString();
    }

    @Override
    public final void writeTo(Appendable out) throws IOException {
      Objects.requireNonNull(out, "out == null");

      Html html;
      html = new Html();

      accept(html);

      HtmlDom document;
      document = html.compile();

      HtmlFormatter.STANDARD.formatTo(document, out);
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

    final HtmlDom compile(Html html) {
      accept(html);

      return html.compile();
    }

    final void accept(Html instance) {
      Check.state(html == null, "Concurrent evalution of a HtmlTemplate is not supported");

      try {
        html = instance;

        html.compilationBegin();

        tryToRender();

        html.compilationEnd();
      } finally {
        html = null;
      }
    }

    final void attribute(Html.AttributeName name, String value) {
      $html().attribute(name, value);
    }

    @Override
    final Html $html() {
      Check.state(html != null, "html not set");

      return html;
    }

  }

  public sealed static abstract class TemplateBase extends Html.TemplateElements permits Component, Template {

    /**
     * The {@code data-execute-default} boolean attribute.
     */
    protected static final Html.AttributeObject dataExecuteDefault = Html.AttributeObject.create(HtmlAttributeName.DATA_EXECUTE_DEFAULT, "");

    TemplateBase() {}

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
    protected final Instruction.OfAttribute className(String v0, String v1) {
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
    protected final Instruction.OfAttribute className(String v0, String v1, String v2) {
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
    protected final Instruction.OfAttribute className(String v0, String v1, String v2, String v3) {
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
    protected final Instruction.OfAttribute className(String... values) {
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
    protected final Instruction.OfAttribute classText(String text) {
      String[] lines;
      lines = text.split("\n+");

      String value;
      value = String.join(" ", lines);

      return $attributes().className(value);
    }

    protected final Instruction.OfAttribute dataFrame(String name) {
      Check.notNull(name, "name == null");

      return $html().attribute(HtmlAttributeName.DATA_FRAME, name);
    }

    protected final Instruction.OfAttribute dataFrame(String name, String value) {
      Check.notNull(name, "name == null");
      Check.notNull(value, "value == null");

      return $html().attribute(HtmlAttributeName.DATA_FRAME, name + ":" + value);
    }

    protected final Html.Instruction.OfDataOn dataOnClick(Script.Action action) {
      return dataOn(HtmlAttributeName.DATA_ON_CLICK, action);
    }

    protected final Html.Instruction.OfDataOn dataOnClick(Script.Action... actions) {
      return dataOn(HtmlAttributeName.DATA_ON_CLICK, actions);
    }

    protected final Html.Instruction.OfDataOn dataOnInput(Script.Action action) {
      return dataOn(HtmlAttributeName.DATA_ON_INPUT, action);
    }

    protected final Html.Instruction.OfDataOn dataOnInput(Script.Action... actions) {
      return dataOn(HtmlAttributeName.DATA_ON_INPUT, actions);
    }

    private final Html.Instruction.OfDataOn dataOn(AttributeName name, Script.Action action) {
      Check.notNull(action, "action == null");

      return $html().dataOn(name, action);
    }

    private final Html.Instruction.OfDataOn dataOn(AttributeName name, Script.Action... actions) {
      Check.notNull(actions, "actions == null");

      Script.Action value;
      value = Script.join(actions);

      return $html().dataOn(name, value);
    }

    protected final Html.Instruction.OfElement element(Html.ElementName name, Html.Instruction... contents) {
      return $html().element(name, contents);
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
    protected final Html.Instruction.OfElement flatten(Instruction... contents) {
      return $html().flatten(contents);
    }

    protected final Html.Instruction.OfElement flattenNonNull(Instruction... contents) {
      return $html().flattenNonNull(contents);
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
    protected final Html.Instruction.OfFragment renderFragment(Html.Fragment.Of0 fragment) {
      Check.notNull(fragment, "fragment == null");

      Html html;
      html = $html();

      int index;
      index = html.fragmentBegin();

      try {
        fragment.invoke();
      } catch (Exception e) {
        throw new Html.RenderingException(e);
      }

      html.fragmentEnd(index);

      return Html.FRAGMENT;
    }

    protected final <T1> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of1<T1> fragment, T1 arg1) {
      Check.notNull(fragment, "fragment == null");

      Html html;
      html = $html();

      int index;
      index = html.fragmentBegin();

      try {
        fragment.invoke(arg1);
      } catch (Exception e) {
        throw new Html.RenderingException(e);
      }

      html.fragmentEnd(index);

      return Html.FRAGMENT;
    }

    protected final <T1, T2> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of2<T1, T2> fragment, T1 arg1, T2 arg2) {
      Check.notNull(fragment, "fragment == null");

      Html html;
      html = $html();

      int index;
      index = html.fragmentBegin();

      try {
        fragment.invoke(arg1, arg2);
      } catch (Exception e) {
        throw new Html.RenderingException(e);
      }

      html.fragmentEnd(index);

      return Html.FRAGMENT;
    }

    protected final <T1, T2, T3> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
      Check.notNull(fragment, "fragment == null");

      Html html;
      html = $html();

      int index;
      index = html.fragmentBegin();

      try {
        fragment.invoke(arg1, arg2, arg3);
      } catch (Exception e) {
        throw new Html.RenderingException(e);
      }

      html.fragmentEnd(index);

      return Html.FRAGMENT;
    }

    protected final <T1, T2, T3, T4> Html.Instruction.OfFragment renderFragment(Html.Fragment.Of4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      Check.notNull(fragment, "fragment == null");

      Html html;
      html = $html();

      int index;
      index = html.fragmentBegin();

      try {
        fragment.invoke(arg1, arg2, arg3, arg4);
      } catch (Exception e) {
        throw new Html.RenderingException(e);
      }

      html.fragmentEnd(index);

      return Html.FRAGMENT;
    }

    /**
     * Renders the specified plugin into this template.
     *
     * @param plugin
     *        the plugin to be rendered as part of this template
     *
     * @return an instruction representing the rendered plugin.
     */
    protected final Html.Instruction.OfFragment renderPlugin(Consumer<Html> plugin) {
      Check.notNull(plugin, "plugin == null");

      Html html;
      html = $html();

      int index;
      index = html.fragmentBegin();

      plugin.accept(html);

      html.fragmentEnd(index);

      return Html.FRAGMENT;
    }

    /**
     * Renders the specified template into this template.
     *
     * @param template
     *        the template to be rendered as part of this template
     *
     * @return an instruction representing the rendered template.
     */
    protected final Html.Instruction.OfFragment renderTemplate(Html.Template template) {
      Check.notNull(template, "template == null");

      try {
        Html html;
        html = $html();

        int index;
        index = html.fragmentBegin();

        template.html = html;

        template.tryToRender();

        html.fragmentEnd(index);
      } finally {
        template.html = null;
      }

      return Html.FRAGMENT;
    }

    protected final Html.Instruction.OfElement nbsp() {
      return raw("&nbsp;");
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
    protected final Html.Instruction.NoOp noop() {
      return Html.NOOP;
    }

    protected final Html.Instruction.NoOp noop(String ignored) {
      return Html.NOOP;
    }

    protected final Html.Instruction.OfElement raw(String text) {
      return $html().raw(text);
    }

    /**
     * Generates a text node with the specified {@code text} value. The text
     * value is escaped before being emitted to the output.
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
    protected final Html.Instruction.OfElement text(String text) {
      return $html().text(text);
    }

    protected final Html.Instruction.OfElement testable(String name, String value) {
      return $html().testable(name, value);
    }

    @Override
    final HtmlRecorderAttributes $attributes() {
      return $html();
    }

    @Override
    final HtmlRecorderElements $elements() {
      return $html();
    }

    abstract Html $html();

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

  public static Html create() {
    return new Html();
  }

  public final Html.Instruction.OfFragment render(Consumer<Html> fragment) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    fragment.accept(this);

    fragmentEnd(index);

    return Html.FRAGMENT;
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
   * An instruction to render an HTML {@code class} attribute.
   */
  public sealed interface ClassName extends AttributeObject {

    static ClassName className(ClassName... classNames) {
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

    static ClassName className(ClassName className, String text) {
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

    static ClassName className(String value) {
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
    static ClassName classText(String text) {
      String[] lines;
      lines = text.split("\n+");

      String joined;
      joined = String.join(" ", lines);

      return new HtmlClassName(joined);
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

  record HtmlClassName(String value) implements ClassName {}

  /**
   * An instruction to render an HTML {@code id} attribute.
   */
  public sealed interface Id extends AttributeObject {

    static Id id(String value) {
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
     * Generates the {@code font-family} attribute with the specified value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute fontFamily(String value) {
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
    protected final Instruction.OfAttribute fontSize(String value) {
      return $attributes().fontSize(value);
    }

    /**
     * Generates the {@code font-size-adjust} attribute with the specified
     * value.
     *
     * @param value
     *        the value of the attribute
     *
     * @return an instruction representing this attribute.
     */
    protected final Instruction.OfAttribute fontSizeAdjust(String value) {
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
    protected final Instruction.OfAttribute fontStretch(String value) {
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
    protected final Instruction.OfAttribute fontStyle(String value) {
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
    protected final Instruction.OfAttribute fontVariant(String value) {
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
    protected final Instruction.OfAttribute fontWeight(String value) {
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

    abstract HtmlRecorderAttributes $attributes();

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
    protected final Html.Instruction.OfElement clipPath(String text) {
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
    protected final Html.Instruction.OfElement form(String text) {
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
    protected final Html.Instruction.OfElement label(String text) {
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
    protected final Html.Instruction.OfElement title(String text) {
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

    abstract HtmlRecorderElements $elements();

  }

}

enum HtmlAmbiguous {

  CLIPPATH(HtmlAttributeName.CLIP_PATH, HtmlElementName.CLIPPATH) {
    @Override
    public final boolean isAttributeOf(Html.ElementName element) {
      return element != Html.ElementName.SVG;
    }
  },

  FORM(HtmlAttributeName.FORM, HtmlElementName.FORM) {
    @Override
    public final boolean isAttributeOf(Html.ElementName element) {
      return element == Html.ElementName.SELECT
          || element == Html.ElementName.TEXTAREA;
    }
  },

  LABEL(HtmlAttributeName.LABEL, HtmlElementName.LABEL) {
    @Override
    public final boolean isAttributeOf(Html.ElementName element) {
      return element == Html.ElementName.OPTION;
    }
  },

  TITLE(HtmlAttributeName.TITLE, HtmlElementName.TITLE) {
    @Override
    public final boolean isAttributeOf(Html.ElementName element) {
      return element != Html.ElementName.HEAD
          && element != Html.ElementName.SVG;
    }
  };

  private static final HtmlAmbiguous[] ALL = HtmlAmbiguous.values();

  private final int attributeByteCode;

  public final Html.ElementName element;

  private final int elementByteCode;

  private HtmlAmbiguous(Html.AttributeName attribute, Html.ElementName element) {
    this.attributeByteCode = attribute.index();

    this.element = element;

    this.elementByteCode = element.index();
  }

  public static HtmlAmbiguous decode(byte b0) {
    int ordinal;
    ordinal = HtmlBytes.decodeInt(b0);

    return ALL[ordinal];
  }

  public static HtmlAmbiguous get(int code) {
    return ALL[code];
  }

  public final int attributeByteCode() {
    return attributeByteCode;
  }

  public final int code() {
    return ordinal();
  }

  public final int elementByteCode() {
    return elementByteCode;
  }

  public final byte encodeAttribute() {
    return HtmlBytes.encodeInt0(attributeByteCode);
  }

  public abstract boolean isAttributeOf(Html.ElementName element);

}

final class HtmlAttributeName implements Html.AttributeName {

  /**
   * The {@code data-execute-default} attribute.
   */
  public static final Html.AttributeName DATA_EXECUTE_DEFAULT = create("data-execute-default", true);

  /**
   * The {@code data-frame} attribute.
   */
  public static final Html.AttributeName DATA_FRAME = create("data-frame", false);

  /**
   * The {@code data-on-click} attribute.
   */
  public static final Html.AttributeName DATA_ON_CLICK = action("data-on-click");

  /**
   * The {@code data-on-input} attribute.
   */
  public static final Html.AttributeName DATA_ON_INPUT = action("data-on-input");

  static final class Builder {

    static Builder INSTANCE = new Builder();

    private final List<HtmlAttributeName> standardValues = new ArrayList<>();

    private int index;

    private Builder() {}

    public final HtmlAttributeName createImpl(String name, boolean booleanAttribute) {
      return createImpl(name, booleanAttribute, false, String.class);
    }

    public final HtmlAttributeName createImpl(String name, boolean booleanAttribute, boolean singleQuoted, Class<?> type) {
      HtmlAttributeName result;
      result = new HtmlAttributeName(index++, name, booleanAttribute, singleQuoted, type);

      standardValues.add(result);

      return result;
    }

    public HtmlAttributeName[] buildValuesImpl() {
      return standardValues.toArray(HtmlAttributeName[]::new);
    }

  }

  private final int index;

  private final String name;

  private final boolean booleanAttribute;

  private final boolean singleQuoted;

  private final Class<?> type;

  public HtmlAttributeName(int index, String name, boolean booleanAttribute, boolean singleQuoted, Class<?> type) {
    this.index = index;
    this.name = name;
    this.booleanAttribute = booleanAttribute;
    this.singleQuoted = singleQuoted;
    this.type = type;
  }

  public static HtmlAttributeName action(String name) {
    return Builder.INSTANCE.createImpl(name, false, true, Script.Action.class);
  }

  public static HtmlAttributeName create(String name, boolean booleanAttribute) {
    return Builder.INSTANCE.createImpl(name, booleanAttribute);
  }

  static int size() {
    return LazyValues.VALUES.length;
  }

  public static HtmlAttributeName get(int index) {
    return LazyValues.VALUES[index];
  }

  @Override
  public final int index() {
    return index;
  }

  @Override
  public final String name() {
    return name;
  }

  @Override
  public final boolean booleanAttribute() {
    return booleanAttribute;
  }

  @Override
  public final boolean singleQuoted() {
    return singleQuoted;
  }

  public final Class<?> type() {
    return type;
  }

  private static class LazyValues {

    static HtmlAttributeName[] VALUES = create();

    private static HtmlAttributeName[] create() {
      HtmlAttributeName[] result;
      result = Builder.INSTANCE.buildValuesImpl();

      Builder.INSTANCE = null;

      return result;
    }

  }

}

final class HtmlByteProto {

  // internal instructions

  public static final byte END = -1;
  public static final byte INTERNAL = -2;
  public static final byte INTERNAL3 = -3;
  public static final byte INTERNAL4 = -4;
  public static final byte INTERNAL5 = -5;
  public static final byte INTERNAL6 = -6;
  public static final byte LENGTH2 = -7;
  public static final byte LENGTH3 = -8;
  public static final byte MARKED3 = -9;
  public static final byte MARKED4 = -10;
  public static final byte MARKED5 = -11;
  public static final byte MARKED6 = -12;
  public static final byte NULL = -13;
  public static final byte STANDARD_NAME = -14;

  // elements

  public static final byte AMBIGUOUS1 = -15;
  public static final byte DOCTYPE = -16;
  public static final byte ELEMENT = -17;
  public static final byte FLATTEN = -18;
  public static final byte FRAGMENT = -19;
  public static final byte RAW = -20;
  public static final byte TEXT = -21;
  public static final byte TESTABLE = -22;

  // attributes

  public static final byte ATTRIBUTE0 = -23;
  public static final byte ATTRIBUTE1 = -24;
  //public static final byte ATTRIBUTE_CLASS = -22;
  //public static final byte ATTRIBUTE_ID = -23;
  public static final byte ATTRIBUTE_EXT1 = -25;

  private HtmlByteProto() {}

}

final class HtmlDom implements Html.Dom, Lang.IterableOnce<Html.Dom.Node>, Iterator<Html.Dom.Node> {

  private final HtmlRecorder player;

  public HtmlDom(HtmlRecorder ctx) {
    this.player = ctx;
  }

  @Override
  public final Lang.IterableOnce<Html.Dom.Node> nodes() {
    player.documentIterable();

    return this;
  }

  @Override
  public final Iterator<Html.Dom.Node> iterator() {
    player.documentIterator();

    return this;
  }

  @Override
  public final boolean hasNext() {
    return player.documentHasNext();
  }

  @Override
  public final Html.Dom.Node next() {
    return player.documentNext();
  }

}

final class HtmlDomAttribute implements Html.Dom.Attribute {

  HtmlAttributeName name;

  private final HtmlRecorder player;

  Object value;

  public HtmlDomAttribute(HtmlRecorder player) {
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

  private final HtmlRecorder player;

  HtmlElementName name;

  HtmlDomElement(HtmlRecorder player) {
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

  String testable;

  String value;

  @Override
  public final String testable() {
    return testable;
  }

  @Override
  public final String value() {
    return value;
  }

}

final class HtmlElementName implements Html.ElementName {

  static final class Builder {

    static Builder INSTANCE = new Builder();

    private final List<HtmlElementName> standardValues = new ArrayList<>();

    private int index;

    private Builder() {}

    public HtmlElementName[] buildValuesImpl() {
      return standardValues.toArray(HtmlElementName[]::new);
    }

    public final HtmlElementName create(String name, boolean endTag) {
      HtmlElementName result;
      result = new HtmlElementName(index++, name, endTag);

      standardValues.add(result);

      return result;
    }

  }

  private final int index;

  private final String name;

  private final boolean endTag;

  private HtmlElementName(int index, String name, boolean endTag) {
    this.index = index;
    this.name = name;
    this.endTag = endTag;
  }

  public static HtmlElementName createNormal(String name) {
    return Builder.INSTANCE.create(name, true);
  }

  public static HtmlElementName createVoid(String name) {
    return Builder.INSTANCE.create(name, false);
  }

  static HtmlElementName get(int index) {
    return LazyValues.VALUES[index];
  }

  @Override
  public final int index() {
    return index;
  }

  @Override
  public final String name() {
    return name;
  }

  @Override
  public final boolean endTag() {
    return endTag;
  }

  private static class LazyValues {

    private static HtmlElementName[] VALUES = create();

    private static HtmlElementName[] create() {
      HtmlElementName[] result;
      result = Builder.INSTANCE.buildValuesImpl();

      Builder.INSTANCE = null;

      return result;
    }

  }

  static int size() {
    return LazyValues.VALUES.length;
  }

}

final class HtmlFormatter {

  static final HtmlFormatter STANDARD = new HtmlFormatter();

  private static final Set<String> PHRASING = Set.of(
      HtmlElementName.A.name(),
      HtmlElementName.ABBR.name(),
      HtmlElementName.B.name(),
      HtmlElementName.BR.name(),
      HtmlElementName.BUTTON.name(),
      HtmlElementName.CODE.name(),
      HtmlElementName.EM.name(),
      HtmlElementName.IMG.name(),
      HtmlElementName.INPUT.name(),
      HtmlElementName.KBD.name(),
      HtmlElementName.LABEL.name(),
      HtmlElementName.PROGRESS.name(),
      HtmlElementName.SAMP.name(),
      HtmlElementName.SELECT.name(),
      HtmlElementName.SMALL.name(),
      HtmlElementName.SPAN.name(),
      HtmlElementName.STRONG.name(),
      HtmlElementName.SUB.name(),
      HtmlElementName.SUP.name(),
      HtmlElementName.SVG.name(),
      HtmlElementName.TEMPLATE.name(),
      HtmlElementName.TEXTAREA.name()
  );

  private static final Set<String> TEXT_AS_RAW = Set.of(
      "script", "style"
  );

  private static final String NL = System.lineSeparator();

  private static final byte START = 1;
  private static final byte BLOCK_START = 2;
  private static final byte BLOCK_END = 3;
  private static final byte PHRASE = 4;
  private static final byte SCRIPT = 5;

  private HtmlFormatter() {}

  public final void formatTo(Html.Dom document, Appendable appendable) throws IOException {
    Check.notNull(document, "document == null");
    Check.notNull(appendable, "appendable == null");

    format(document, appendable);
  }

  public final void formatTo(Html.Template template, Appendable appendable) throws IOException {
    Check.notNull(template, "template == null");
    Check.notNull(appendable, "appendable == null");

    Html html;
    html = Html.create();

    HtmlDom document;
    document = template.compile(html);

    format(document, appendable);
  }

  private void format(Html.Dom document, Appendable out) throws IOException {
    byte state;
    state = START;

    for (Html.Dom.Node node : document.nodes()) {
      state = node(out, state, node);
    }

    if (state != START) {
      out.append(NL);
    }
  }

  private byte node(Appendable out, byte state, Html.Dom.Node node) throws IOException {
    return switch (node) {
      case HtmlDomDocumentType doctype -> doctype(out, state, doctype);

      case HtmlDomElement element -> element(out, state, element);

      case HtmlDomText text -> text(out, state, text);

      case HtmlDomRaw raw -> raw(out, state, raw);

      default -> throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass()
      );
    };
  }

  private byte doctype(Appendable out, byte state, HtmlDomDocumentType doctype) throws IOException {
    out.append("<!DOCTYPE html>");

    return BLOCK_END;
  }

  private byte element(Appendable out, byte state, HtmlDomElement element) throws IOException {
    // start tag
    String elementName;
    elementName = element.name();

    byte nextState;

    byte childState;

    if (PHRASING.contains(elementName)) {
      nextState = childState = PHRASE;

      if (state == BLOCK_END) {
        out.append(NL);
      }
    }

    else {
      if (TEXT_AS_RAW.contains(elementName)) {
        nextState = childState = SCRIPT;
      }

      else {
        nextState = BLOCK_END;

        childState = BLOCK_START;
      }

      if (state != START) {
        // we should start this element in the next line
        // except if we are at the start of the document
        out.append(NL);
      }
    }

    out.append('<');
    out.append(elementName);

    for (Html.Dom.Attribute attribute : element.attributes()) {
      attribute(out, attribute);
    }

    out.append('>');

    if (!element.isVoid()) {
      int childCount;
      childCount = 0;

      for (Html.Dom.Node node : element.nodes()) {
        childState = node(out, childState, node);

        childCount++;
      }

      // do we need a NL before the end tag?
      if (childCount > 0) {
        if (nextState == PHRASE && childState == BLOCK_END) {
          out.append(NL);
        }

        else if (nextState != PHRASE && childState != PHRASE) {
          out.append(NL);
        }
      }

      // end tag
      out.append('<');
      out.append('/');
      out.append(elementName);
      out.append('>');
    }

    return nextState;
  }

  enum Quotes {
    SINGLE('\'', "&#39;"),

    DOUBLE('\"', "&#34;");

    final char symbol;

    final String escape;

    private Quotes(char symbol, String escape) {
      this.symbol = symbol;

      this.escape = escape;
    }
  }

  private void attribute(Appendable out, Html.Dom.Attribute attribute) throws IOException {
    String name;
    name = attribute.name();

    out.append(' ');

    out.append(name);

    if (attribute.booleanAttribute()) {
      return;
    }

    Quotes quotes;
    quotes = attribute.singleQuoted() ? Quotes.SINGLE : Quotes.DOUBLE;

    out.append('=');

    out.append(quotes.symbol);

    attributeValue(out, quotes, attribute.value());

    out.append(quotes.symbol);
  }

  // visible for testing
  final void attributeValue(Appendable out, Quotes quotes, String value) throws IOException {
    int idx;
    idx = 0;

    int len;
    len = value.length();

    for (; idx < len; idx++) {
      char c;
      c = value.charAt(idx);

      if (c == quotes.symbol) {
        break;
      }

      if (c == '&') {
        break;
      }
    }

    if (idx == len) {
      out.append(value);

      return;
    }

    out.append(value, 0, idx);

    while (idx < len) {
      char c;
      c = value.charAt(idx++);

      if (c == quotes.symbol) {
        out.append(quotes.escape);
      }

      else if (c == '&') {
        ampersand(out, value, idx, len);
      }

      else {
        out.append(c);
      }
    }
  }

  private byte text(Appendable out, byte state, HtmlDomText text) throws IOException {
    String value;
    value = text.value();

    switch (state) {
      case BLOCK_END -> {
        if (!startsWithNewLine(value)) {
          out.append(NL);
        }

        writeText(out, value);
      }

      case SCRIPT -> {
        if (!startsWithNewLine(value)) {
          out.append(NL);
        }

        out.append(value);

        if (!endsWithNewLine(value)) {
          out.append(NL);
        }
      }

      default -> writeText(out, value);
    }

    return PHRASE;
  }

  // visible for testing
  final void writeText(Appendable out, String value) throws IOException {
    for (int idx = 0, len = value.length(); idx < len;) {
      char c;
      c = value.charAt(idx++);

      switch (c) {
        case '&' -> out.append("&amp;");

        case '<' -> out.append("&lt;");

        case '>' -> out.append("&gt;");

        default -> out.append(c);
      }
    }
  }

  private byte raw(Appendable out, byte state, HtmlDomRaw raw) throws IOException {
    String value;
    value = raw.value();

    out.append(value);

    return PHRASE;
  }

  private boolean startsWithNewLine(String value) {
    int length;
    length = value.length();

    if (length == 0) {
      return false;
    }

    char first;
    first = value.charAt(0);

    return isNewLine(first);
  }

  private boolean endsWithNewLine(String value) {
    int length;
    length = value.length();

    if (length == 0) {
      return false;
    }

    char last;
    last = value.charAt(length - 1);

    return isNewLine(last);
  }

  private boolean isNewLine(char c) {
    return c == '\n' || c == '\r';
  }

  // visible for testing
  final int ampersand(Appendable out, String value, int idx, int len) throws IOException {
    enum State {
      START,
      MAYBE_NAMED,
      MAYBE_NUMERIC,
      MAYBE_DECIMAL,
      MAYBE_HEX,
      ENTITY,
      TEXT;
    }

    int start;
    start = idx;

    State state;
    state = State.START;

    loop: while (idx < len) {
      char c;
      c = value.charAt(idx++);

      switch (state) {
        case START -> {
          if (c == '#') {
            state = State.MAYBE_NUMERIC;
          } else if (isAsciiAlphanumeric(c)) {
            state = State.MAYBE_NAMED;
          } else {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_NAMED -> {
          if (c == ';') {
            state = State.ENTITY;

            break loop;
          } else if (!isAsciiAlphanumeric(c)) {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_NUMERIC -> {
          if (c == 'x' || c == 'X') {
            state = State.MAYBE_HEX;
          } else if (isAsciiDigit(c)) {
            state = State.MAYBE_DECIMAL;
          } else {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_DECIMAL -> {
          if (c == ';') {
            state = State.ENTITY;

            break loop;
          } else if (!isAsciiDigit(c)) {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_HEX -> {
          if (c == ';') {
            state = State.ENTITY;

            break loop;
          } else if (!isAsciiHexDigit(c)) {
            state = State.TEXT;

            break loop;
          }
        }

        case ENTITY, TEXT -> {
          throw new AssertionError();
        }

        default -> {
          throw new UnsupportedOperationException(
              "Implement me :: state=" + state
          );
        }
      }
    }

    switch (state) {
      case START -> {
        out.append("&amp;");
      }

      case ENTITY -> {
        out.append('&');

        out.append(value, start, idx);
      }

      case TEXT -> {
        out.append("&amp;");

        idx = start;
      }

      default -> {
        throw new UnsupportedOperationException(
        );
      }
    }

    return idx;
  }

  private boolean isAsciiAlpha(char c) {
    return 'A' <= c && c <= 'Z'
        || 'a' <= c && c <= 'z';
  }

  private boolean isAsciiAlphanumeric(char c) {
    return isAsciiDigit(c) || isAsciiAlpha(c);
  }

  private boolean isAsciiDigit(char c) {
    return '0' <= c && c <= '9';
  }

  private boolean isAsciiHexDigit(char c) {
    return isAsciiDigit(c)
        || 'a' <= c && c <= 'f'
        || 'A' <= c && c <= 'F';
  }

}