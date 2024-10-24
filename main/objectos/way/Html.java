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
import objectos.way.Html.AttributeInstruction;
import objectos.way.Html.AttributeName;
import objectos.way.Html.ElementName;

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

    Lang.IterableOnce<Node> nodes();

  }

  public sealed interface Node permits DocumentType, Element, RawText, Text {}

  public non-sealed interface DocumentType extends Node {}

  public non-sealed interface Element extends Node {

    Lang.IterableOnce<Attribute> attributes();

    boolean isVoid();

    String name();

    Lang.IterableOnce<Node> nodes();

    default boolean hasName(String name) {
      return name().equals(name);
    }

    String testField();

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
    final Html $compiler() {
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
  public non-sealed static abstract class Template extends TemplateBase implements Lang.CharWritable {

    Html compiler;

    /**
     * Sole constructor.
     */
    protected Template() {}

    public final String testableText() {
      Html compiler;
      compiler = new Html();

      accept(compiler);

      return compiler.testableText();
    }

    /**
     * Returns the HTML generated by this template.
     *
     * @return the HTML generated by this template
     */
    @Override
    public final String toString() {
      try {
        Html compiler;
        compiler = new Html();

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

    @Override
    public final void writeTo(Appendable out) throws IOException {
      Objects.requireNonNull(out, "out == null");

      Html html;
      html = new Html();

      accept(html);

      HtmlDocument document;
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

    final HtmlDocument compile(Html html) {
      accept(html);

      return html.compile();
    }

    public final void accept(Html instance) {
      Check.state(compiler == null, "Concurrent evalution of a HtmlTemplate is not supported");

      try {
        compiler = instance;

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
    final Html $compiler() {
      Check.state(compiler != null, "html not set");

      return compiler;
    }

  }

  public sealed static abstract class TemplateBase extends Html.TemplateElements permits Component, Template {

    TemplateBase() {}

    public final void plugin(Consumer<Html> plugin) {
      Html compiler;
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

    protected final Html.ElementInstruction element(Html.ElementName name, Html.Instruction... contents) {
      return $compiler().element(name, contents);
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
        Html api;
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
    protected final ElementInstruction t(String text) {
      return $compiler().text(text);
    }

    protected final AttributeInstruction testable(String name) {
      return $compiler().testable(name);
    }

    @Override
    final HtmlRecorderAttributes $attributes() {
      return $compiler();
    }

    @Override
    final HtmlRecorderElements $elements() {
      return $compiler();
    }

    abstract Html $compiler();

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

  public final Html.FragmentInstruction render(Consumer<Html> fragment) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    fragment.accept(this);

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  /**
   * Represents an instruction that generates part of the output of an HTML
   * template.
   */
  public sealed interface Instruction {}

  /**
   * Class of instructions that are represented by methods of the template
   * class.
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
   * An instruction to generate a {@code data-on-*} HTML attribute in a
   * template.
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
  public interface ClassName extends AttributeObject {

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
  public interface Id extends AttributeObject {

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
     * Generates the {@code alignment-baseline} attribute with the specified
     * value.
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
     * Generates the {@code color-interpolation} attribute with the specified
     * value.
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
     * Generates the {@code color-interpolation-filters} attribute with the
     * specified value.
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
     * Generates the {@code dominant-baseline} attribute with the specified
     * value.
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
     * Generates the {@code font-size-adjust} attribute with the specified
     * value.
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
     * Generates the {@code glyph-orientation-horizontal} attribute with the
     * specified value.
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
     * Generates the {@code glyph-orientation-vertical} attribute with the
     * specified value.
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
     * Generates the {@code onlanguagechange} attribute with the specified
     * value.
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
     * Generates the {@code onrejectionhandled} attribute with the specified
     * value.
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
     * Generates the {@code onunhandledrejection} attribute with the specified
     * value.
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
     * Generates the {@code stroke-dasharray} attribute with the specified
     * value.
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
     * Generates the {@code stroke-dashoffset} attribute with the specified
     * value.
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
     * Generates the {@code stroke-miterlimit} attribute with the specified
     * value.
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
     * Generates the {@code transform-origin} attribute with the specified
     * value.
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
     * Generates the {@code clipPath} attribute or element with the specified
     * text.
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

    abstract HtmlRecorderElements $elements();

  }

}

enum HtmlAmbiguous {

  CLIPPATH(HtmlAttributeName.CLIP_PATH, HtmlElementName.CLIPPATH) {
    @Override
    public final boolean isAttributeOf(ElementName element) {
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

final class HtmlAttribute implements Html.Attribute {

  HtmlAttributeName name;

  private final HtmlRecorder player;

  Object value;

  public HtmlAttribute(HtmlRecorder player) {
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

final class HtmlAttributeName implements Html.AttributeName {

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
  public static final byte LENGTH2 = -6;
  public static final byte LENGTH3 = -7;
  public static final byte MARKED3 = -8;
  public static final byte MARKED4 = -9;
  public static final byte MARKED5 = -10;
  public static final byte NULL = -11;
  public static final byte STANDARD_NAME = -12;

  // elements

  public static final byte AMBIGUOUS1 = -13;
  public static final byte DOCTYPE = -14;
  public static final byte ELEMENT = -15;
  public static final byte FLATTEN = -16;
  public static final byte FRAGMENT = -17;
  public static final byte RAW = -18;
  public static final byte TEXT = -19;

  // attributes

  public static final byte ATTRIBUTE0 = -20;
  public static final byte ATTRIBUTE1 = -21;
  //public static final byte ATTRIBUTE_CLASS = -22;
  //public static final byte ATTRIBUTE_ID = -23;
  public static final byte ATTRIBUTE_EXT1 = -22;
  public static final byte TESTABLE = -23;

  private HtmlByteProto() {}

}

final class HtmlDocument implements Html.Document, Lang.IterableOnce<Html.Node>, Iterator<Html.Node> {

  private final HtmlRecorder player;

  public HtmlDocument(HtmlRecorder ctx) {
    this.player = ctx;
  }

  @Override
  public final Lang.IterableOnce<Html.Node> nodes() {
    player.documentIterable();

    return this;
  }

  @Override
  public final Iterator<Html.Node> iterator() {
    player.documentIterator();

    return this;
  }

  @Override
  public final boolean hasNext() {
    return player.documentHasNext();
  }

  @Override
  public final Html.Node next() {
    return player.documentNext();
  }

}

enum HtmlDocumentType implements Html.DocumentType {
  INSTANCE;
}

final class HtmlElement implements Html.Element, Lang.IterableOnce<Html.Node>, Iterator<Html.Node> {

  private class ThisAttributes implements Lang.IterableOnce<Html.Attribute>, Iterator<Html.Attribute> {

    @Override
    public final boolean hasNext() {
      return player.elementAttributesHasNext(name);
    }

    @Override
    public final Iterator<Html.Attribute> iterator() {
      player.elementAttributesIterator();

      return this;
    }

    @Override
    public final Html.Attribute next() {
      return player.elementAttributesNext();
    }

  }

  private ThisAttributes attributes;

  private final HtmlRecorder player;

  HtmlElementName name;

  HtmlElement(HtmlRecorder player) {
    this.player = player;
  }

  @Override
  public final Lang.IterableOnce<Html.Attribute> attributes() {
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
  public final Iterator<Html.Node> iterator() {
    player.elementNodesIterator();

    return this;
  }

  @Override
  public final String name() {
    return name.name();
  }

  @Override
  public final String testField() {
    return player.elementTestField();
  }

  @Override
  public final Html.Node next() {
    return player.elementNodesNext();
  }

  @Override
  public final Lang.IterableOnce<Html.Node> nodes() {
    player.elementNodes();

    return this;
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

  public final void formatTo(Html.Document document, Appendable appendable) throws IOException {
    Check.notNull(document, "document == null");
    Check.notNull(appendable, "appendable == null");

    format(document, appendable);
  }

  public final void formatTo(Html.Template template, Appendable appendable) throws IOException {
    Check.notNull(template, "template == null");
    Check.notNull(appendable, "appendable == null");

    Html html;
    html = Html.create();

    HtmlDocument document;
    document = template.compile(html);

    format(document, appendable);
  }

  private void format(Html.Document document, Appendable out) throws IOException {
    byte state;
    state = START;

    for (Html.Node node : document.nodes()) {
      state = node(out, state, node);
    }

    if (state != START) {
      out.append(NL);
    }
  }

  private byte node(Appendable out, byte state, Html.Node node) throws IOException {
    return switch (node) {
      case HtmlDocumentType doctype -> doctype(out, state, doctype);

      case HtmlElement element -> element(out, state, element);

      case HtmlText text -> text(out, state, text);

      case HtmlRawText raw -> raw(out, state, raw);

      default -> throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass()
      );
    };
  }

  private byte doctype(Appendable out, byte state, HtmlDocumentType doctype) throws IOException {
    out.append("<!DOCTYPE html>");

    return BLOCK_END;
  }

  private byte element(Appendable out, byte state, HtmlElement element) throws IOException {
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

    for (Html.Attribute attribute : element.attributes()) {
      attribute(out, attribute);
    }

    out.append('>');

    if (!element.isVoid()) {
      int childCount;
      childCount = 0;

      for (Html.Node node : element.nodes()) {
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

  private void attribute(Appendable out, Html.Attribute attribute) throws IOException {
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

  private byte text(Appendable out, byte state, HtmlText text) throws IOException {
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

  private byte raw(Appendable out, byte state, HtmlRawText raw) throws IOException {
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
            "Implement me :: state=" + state
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

final class HtmlRawText implements Html.RawText {

  String value;

  @Override
  public final String value() {
    return value;
  }

}

final class HtmlText implements Html.Text {

  String value;

  @Override
  public final String value() {
    return value;
  }

}

sealed class HtmlRecorder extends HtmlRecorderElements {

  static final byte _DOCUMENT_START = -1;
  static final byte _DOCUMENT_NODES_ITERABLE = -2;
  static final byte _DOCUMENT_NODES_ITERATOR = -3;
  static final byte _DOCUMENT_NODES_HAS_NEXT = -4;
  static final byte _DOCUMENT_NODES_NEXT = -5;
  static final byte _DOCUMENT_NODES_EXHAUSTED = -6;

  static final byte _ELEMENT_START = -7;
  static final byte _ELEMENT_ATTRS_ITERABLE = -8;
  static final byte _ELEMENT_ATTRS_ITERATOR = -9;
  static final byte _ELEMENT_ATTRS_HAS_NEXT = -10;
  static final byte _ELEMENT_ATTRS_NEXT = -11;
  static final byte _ELEMENT_ATTRS_EXHAUSTED = -12;
  static final byte _ELEMENT_NODES_ITERABLE = -13;
  static final byte _ELEMENT_NODES_ITERATOR = -14;
  static final byte _ELEMENT_NODES_HAS_NEXT = -15;
  static final byte _ELEMENT_NODES_NEXT = -16;
  static final byte _ELEMENT_NODES_EXHAUSTED = -17;

  static final byte _ATTRIBUTE_VALUES_ITERABLE = -18;
  static final byte _ATTRIBUTE_VALUES_ITERATOR = -19;
  static final byte _ATTRIBUTE_VALUES_HAS_NEXT = -20;
  static final byte _ATTRIBUTE_VALUES_NEXT = -21;
  static final byte _ATTRIBUTE_VALUES_EXHAUSTED = -22;

  private static final int OFFSET_ELEMENT = 0;
  private static final int OFFSET_ATTRIBUTE = 1;
  private static final int OFFSET_TEXT = 2;
  private static final int OFFSET_RAW = 3;

  private static final int OFFSET_MAX = OFFSET_RAW;

  final String testableText() {
    StringBuilder sb;
    sb = new StringBuilder();

    HtmlDocument document;
    document = compile();

    for (var node : document.nodes()) {
      switch (node) {
        case Html.Element element -> testableElement(sb, element);

        default -> {}
      }
    }

    return sb.toString();
  }

  private void testableElement(StringBuilder sb, Html.Element element) {
    String testField;
    testField = element.testField();

    if (testField != null) {
      sb.append(testField);
      sb.append(':');
      sb.append(' ');

      for (var node : element.nodes()) {
        switch (node) {
          case Html.Text text -> sb.append(text.value());

          default -> {}
        }
      }

      sb.append(System.lineSeparator());
    } else {
      for (var node : element.nodes()) {
        switch (node) {
          case Html.Element child -> testableElement(sb, child);

          default -> {}
        }
      }
    }
  }

  @Override
  public final String toString() {
    try {
      StringBuilder sb;
      sb = new StringBuilder();

      HtmlDocument document;
      document = compile();

      HtmlFormatter.STANDARD.formatTo(document, sb);

      return sb.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  public final Html.AttributeInstruction attribute(Html.AttributeName name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    return attribute0(name, value);
  }

  public final Html.DataOnInstruction dataOn(Html.AttributeName name, Script.Action value) {
    Check.notNull(name, "name == null");

    Script.Action a;
    a = Check.notNull(value, "value == null");

    if (a == Script.noop()) {
      return Html.NOOP;
    } else {
      return attribute0(name, a);
    }
  }

  /**
   * Flattens the specified instructions so that each of the specified
   * instructions is individually added, in order, to a receiving element.
   *
   * <p>
   * This is useful, for example, when creating {@link HtmlComponent}
   * instances. The following Objectos HTML code:
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
  public final Html.ElementInstruction flatten(Html.Instruction... contents) {
    Check.notNull(contents, "contents == null");

    flattenBegin();

    for (int i = 0; i < contents.length; i++) {
      Html.Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      elementValue(inst);
    }

    elementEnd();

    return Html.ELEMENT;
  }

  public final Html.ElementInstruction flattenNonNull(Html.Instruction... contents) {
    Check.notNull(contents, "contents == null");

    flattenBegin();

    for (int i = 0; i < contents.length; i++) {
      Html.Instruction inst;
      inst = contents[i];

      if (inst != null) {
        elementValue(inst);
      }
    }

    elementEnd();

    return Html.ELEMENT;
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
  public final Html.FragmentInstruction include(Html.FragmentLambda fragment) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke();
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  public final <T1> Html.FragmentInstruction include(Html.FragmentLambda1<T1> fragment, T1 arg1) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.renderMobileNavHeaderItems(arg1);
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  public final <T1, T2> Html.FragmentInstruction include(Html.FragmentLambda2<T1, T2> fragment, T1 arg1, T2 arg2) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke(arg1, arg2);
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  public final <T1, T2, T3> Html.FragmentInstruction include(
      Html.FragmentLambda3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke(arg1, arg2, arg3);
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  public final <T1, T2, T3, T4> Html.FragmentInstruction include(
      Html.FragmentLambda4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
    Check.notNull(fragment, "fragment == null");

    int index;
    index = fragmentBegin();

    try {
      fragment.invoke(arg1, arg2, arg3, arg4);
    } catch (Exception e) {
      throw new Html.RenderingException(e);
    }

    fragmentEnd(index);

    return Html.FRAGMENT;
  }

  /**
   * The no-op instruction.
   *
   * <p>
   * It can be used to conditionally add an attribute or element. For example,
   * the following Objectos HTML template:
   *
   * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region = "noop"}
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
  public final Html.NoOpInstruction noop() {
    return Html.NOOP;
  }

  public final Html.ElementInstruction raw(String text) {
    Check.notNull(text, "text == null");

    rawImpl(text);

    return Html.ELEMENT;
  }

  public final AttributeInstruction testable(String name) {
    Check.notNull(name, "name == null");

    testableImpl(name);

    return Html.ATTRIBUTE;
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
  public final Html.ElementInstruction text(String text) {
    Check.notNull(text, "text == null");

    textImpl(text);

    return Html.ELEMENT;
  }

  //

  final HtmlDocument compile() {
    // we will use the aux list to store contexts
    auxIndex = 0;

    // holds decoded length
    auxStart = 0;

    // holds maximum main index. DO NOT TOUCH!!!
    // mainIndex

    // holds the current context
    mainStart = 0;

    // we reuse objectArray reference to store our pseudo html objects
    if (objectArray == null) {
      objectArray = new Object[10];
    } else {
      objectArray = Util.growIfNecessary(objectArray, objectIndex + OFFSET_MAX);
    }

    objectArray[objectIndex + OFFSET_ELEMENT] = new HtmlElement(this);

    objectArray[objectIndex + OFFSET_ATTRIBUTE] = new HtmlAttribute(this);

    objectArray[objectIndex + OFFSET_TEXT] = new HtmlText();

    objectArray[objectIndex + OFFSET_RAW] = new HtmlRawText();

    documentCtx();

    return new HtmlDocument(this);
  }

  final void documentIterable() {
    stateCAS(_DOCUMENT_START, _DOCUMENT_NODES_ITERABLE);
  }

  final void documentIterator() {
    stateCAS(_DOCUMENT_NODES_ITERABLE, _DOCUMENT_NODES_ITERATOR);
  }

  final boolean documentHasNext() {
    // our iteration index
    int index;

    // state check
    switch (statePeek()) {
      case _DOCUMENT_NODES_ITERATOR, _DOCUMENT_NODES_NEXT -> {
        // valid state

        // restore main index from the context
        index = documentCtxMainIndexLoad();
      }

      case _ELEMENT_START, _ELEMENT_ATTRS_EXHAUSTED, _ELEMENT_NODES_EXHAUSTED -> {
        int parentIndex;
        parentIndex = elementCtxRemove();

        stateCheck(_DOCUMENT_NODES_NEXT);

        // restore main index from the context
        index = documentCtxMainIndexLoad();

        if (index != parentIndex) {
          throw new IllegalStateException(
              """
              Last consumed element was not a child of this document
              """
          );
        }
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    // has next
    byte nextState;
    nextState = _DOCUMENT_NODES_EXHAUSTED;

    loop: while (index < mainIndex) {
      byte proto;
      proto = main[index];

      switch (proto) {
        case HtmlByteProto.DOCTYPE, HtmlByteProto.ELEMENT, HtmlByteProto.TEXT -> {
          // next node found
          nextState = _DOCUMENT_NODES_HAS_NEXT;

          break loop;
        }

        case HtmlByteProto.LENGTH2 -> {
          index++;

          byte b0;
          b0 = main[index++];

          byte b1;
          b1 = main[index++];

          index += HtmlBytes.decodeInt(b0, b1);
        }

        case HtmlByteProto.LENGTH3 -> {
          index++;

          byte b0;
          b0 = main[index++];

          byte b1;
          b1 = main[index++];

          byte b2;
          b2 = main[index++];

          index += HtmlBytes.decodeLength3(b0, b1, b2);
        }

        case HtmlByteProto.MARKED3 -> index += 3;

        case HtmlByteProto.MARKED4 -> index += 4;

        case HtmlByteProto.MARKED5 -> index += 5;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    stateSet(nextState);

    documentCtxMainIndexStore(index);

    return nextState == _DOCUMENT_NODES_HAS_NEXT;
  }

  final Html.Node documentNext() {
    stateCAS(_DOCUMENT_NODES_HAS_NEXT, _DOCUMENT_NODES_NEXT);

    // restore main index from the context
    int index;
    index = documentCtxMainIndexLoad();

    // next
    byte proto;
    proto = main[index++];

    return switch (proto) {
      case HtmlByteProto.DOCTYPE -> {
        documentCtxMainIndexStore(index);

        yield HtmlDocumentType.INSTANCE;
      }

      case HtmlByteProto.ELEMENT -> {
        byte b0;
        b0 = main[index++];

        byte b1;
        b1 = main[index++];

        int length;
        length = HtmlBytes.decodeInt(b0, b1);

        int elementStartIndex;
        elementStartIndex = index;

        int parentIndex;
        parentIndex = index + length;

        documentCtxMainIndexStore(parentIndex);

        yield element(elementStartIndex, parentIndex);
      }

      case HtmlByteProto.TEXT -> {
        byte b0;
        b0 = main[index++];

        byte b1;
        b1 = main[index++];

        // skip ByteProto.INTERNAL4
        documentCtxMainIndexStore(index + 1);

        yield htmlText(b0, b1);
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };
  }

  private void documentCtx() {
    // set current context
    mainStart = auxIndex;

    // push document context
    auxAdd(
        _DOCUMENT_START,

        // main index @ start iteration from the start = 0
        HtmlBytes.encodeInt0(0),
        HtmlBytes.encodeInt1(0),
        HtmlBytes.encodeInt2(0)
    );
  }

  private int documentCtxMainIndexLoad() {
    byte b0;
    b0 = aux[mainStart + 1];

    byte b1;
    b1 = aux[mainStart + 2];

    byte b2;
    b2 = aux[mainStart + 3];

    return HtmlBytes.decodeLength3(b0, b1, b2);
  }

  private void documentCtxMainIndexStore(int value) {
    aux[mainStart + 1] = HtmlBytes.encodeInt0(value);

    aux[mainStart + 2] = HtmlBytes.encodeInt1(value);

    aux[mainStart + 3] = HtmlBytes.encodeInt2(value);
  }

  final HtmlElement element(int startIndex, int parentIndex) {
    // our iteration index
    int elementIndex;
    elementIndex = startIndex;

    HtmlElementName name;

    // first proto should be the element's name
    byte proto;
    proto = main[elementIndex++];

    switch (proto) {
      case HtmlByteProto.STANDARD_NAME -> {
        byte nameByte;
        nameByte = main[elementIndex++];

        int ordinal;
        ordinal = HtmlBytes.decodeInt(nameByte);

        name = HtmlElementName.get(ordinal);
      }

      default -> throw new IllegalArgumentException(
          "Malformed element. Expected name but found=" + proto
      );
    }

    elementCtx(startIndex, parentIndex);

    HtmlElement element;
    element = htmlElement();

    element.name = name;

    return element;
  }

  final void elementAttributes() {
    // state check
    switch (statePeek()) {
      case _ELEMENT_START -> {
        // valid state
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    stateSet(_ELEMENT_ATTRS_ITERABLE);
  }

  final void elementAttributesIterator() {
    stateCAS(_ELEMENT_ATTRS_ITERABLE, _ELEMENT_ATTRS_ITERATOR);
  }

  final boolean elementAttributesHasNext(Html.ElementName parent) {
    // state check
    switch (statePeek()) {
      case _ELEMENT_ATTRS_ITERATOR,
           _ELEMENT_ATTRS_NEXT,
           _ATTRIBUTE_VALUES_EXHAUSTED -> {
        // valid state
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    // restore index from context
    int index;
    index = elementCtxAttrsIndexLoad();

    // has next
    byte nextState;
    nextState = _ELEMENT_ATTRS_EXHAUSTED;

    loop: while (index < mainIndex) {
      // assume 'worst case'
      // in the happy path we should rollback the index
      int rollbackIndex;
      rollbackIndex = index;

      byte proto;
      proto = main[index++];

      switch (proto) {
        case HtmlByteProto.STANDARD_NAME -> index += 1;

        case HtmlByteProto.AMBIGUOUS1 -> {
          index = jmp2(index);

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          HtmlAmbiguous ambiguous;
          ambiguous = HtmlAmbiguous.decode(ordinalByte);

          if (ambiguous.isAttributeOf(parent)) {
            index = rollbackIndex;

            nextState = _ELEMENT_ATTRS_HAS_NEXT;

            break loop;
          }
        }

        case HtmlByteProto.ATTRIBUTE0,
             HtmlByteProto.ATTRIBUTE1,
             HtmlByteProto.ATTRIBUTE_EXT1 -> {
          index = rollbackIndex;

          nextState = _ELEMENT_ATTRS_HAS_NEXT;

          break loop;
        }

        case HtmlByteProto.ELEMENT,
             HtmlByteProto.RAW,
             HtmlByteProto.TEXT,
             HtmlByteProto.TESTABLE -> index = skipVarInt(index);

        case HtmlByteProto.END -> {
          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.LENGTH2 -> {
          byte len0;
          len0 = main[index++];

          byte len1;
          len1 = main[index++];

          int length;
          length = HtmlBytes.decodeInt(len0, len1);

          index += length;
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    elementCtxAttrsIndexStore(index);

    stateSet(nextState);

    return nextState == _ELEMENT_ATTRS_HAS_NEXT;
  }

  final HtmlAttribute elementAttributesNext() {
    stateCAS(_ELEMENT_ATTRS_HAS_NEXT, _ELEMENT_ATTRS_NEXT);

    // restore main index
    int index;
    index = elementCtxAttrsIndexLoad();

    // our return value
    final HtmlAttribute attribute;
    attribute = htmlAttribute();

    // values to set
    byte attr, v0 = -1, v1 = -1;

    byte proto;
    proto = main[index++];

    switch (proto) {
      case HtmlByteProto.AMBIGUOUS1 -> {
        index = jmp2(index);

        byte ordinalByte;
        ordinalByte = main[auxStart++];

        HtmlAmbiguous ambiguous;
        ambiguous = HtmlAmbiguous.decode(ordinalByte);

        attr = ambiguous.encodeAttribute();

        v0 = main[auxStart++];

        v1 = main[auxStart++];
      }

      case HtmlByteProto.ATTRIBUTE0 -> {
        index = jmp2(index);

        attr = main[auxStart++];
      }

      case HtmlByteProto.ATTRIBUTE1 -> {
        index = jmp2(index);

        attr = main[auxStart++];

        v0 = main[auxStart++];

        v1 = main[auxStart++];
      }

      case HtmlByteProto.ATTRIBUTE_EXT1 -> {
        attr = main[index++];

        v0 = main[index++];

        v1 = main[index++];
      }

      default -> {
        // the previous hasNext should have set the index in the right position
        // if we got to an invalid proto something bad must have happened
        throw new IllegalStateException();
      }
    }

    // attribute name
    int ordinal;
    ordinal = HtmlBytes.decodeInt(attr);

    attribute.name = HtmlAttributeName.get(ordinal);

    // attribute value
    Object value;
    value = null;

    if (v0 != -1 || v1 != -1) {
      value = toObject(v0, v1);
    }

    attribute.value = value;

    // store new state
    elementCtxAttrsIndexStore(index);

    stateSet(_ELEMENT_ATTRS_NEXT);

    return attribute;
  }

  private HtmlAttribute htmlAttribute() {
    return (HtmlAttribute) objectArray[objectIndex + OFFSET_ATTRIBUTE];
  }

  final void attributeValues() {
    stateCAS(_ELEMENT_ATTRS_NEXT, _ATTRIBUTE_VALUES_ITERABLE);
  }

  final void attributeValuesIterator() {
    stateCAS(_ATTRIBUTE_VALUES_ITERABLE, _ATTRIBUTE_VALUES_ITERATOR);
  }

  final boolean attributeValuesHasNext() {
    // state check
    switch (statePeek()) {
      case _ATTRIBUTE_VALUES_ITERATOR,
           _ATTRIBUTE_VALUES_NEXT -> {
        // valid state
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    HtmlAttribute attribute;
    attribute = htmlAttribute();

    if (attribute.value != null) {
      stateSet(_ATTRIBUTE_VALUES_HAS_NEXT);

      return true;
    }

    // restore index from context
    int index;
    index = elementCtxAttrsIndexLoad();

    // current attribute
    HtmlAttributeName attributeName;
    attributeName = attribute.name;

    int attributeCode;
    attributeCode = attributeName.index();

    byte currentAttr;
    currentAttr = HtmlBytes.encodeInt0(attributeCode);

    // next state
    byte nextState;
    nextState = _ATTRIBUTE_VALUES_EXHAUSTED;

    loop: while (index < mainIndex) {
      // assume 'worst case'
      // in the happy path we should rollback the index
      int rollbackIndex;
      rollbackIndex = index;

      byte proto;
      proto = main[index++];

      switch (proto) {
        case HtmlByteProto.AMBIGUOUS1 -> {
          index = jmp2(index);

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          int ordinal;
          ordinal = HtmlBytes.decodeInt(ordinalByte);

          HtmlAmbiguous ambiguous;
          ambiguous = HtmlAmbiguous.get(ordinal);

          // find out the parent
          HtmlElement element;
          element = htmlElement();

          HtmlElementName elementName;
          elementName = element.name;

          if (!ambiguous.isAttributeOf(elementName)) {
            // this is an element
            continue loop;
          }

          // find out if this is the same attribute
          byte attr;
          attr = ambiguous.encodeAttribute();

          if (currentAttr == attr) {
            // this is a new value of the same attribute
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.ATTRIBUTE0 -> {
          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.ATTRIBUTE1 -> {
          index = jmp2(index);

          byte attr;
          attr = main[auxStart++];

          if (attr == currentAttr) {
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.ATTRIBUTE_EXT1 -> {
          byte attr;
          attr = main[index++];

          if (attr == currentAttr) {
            nextState = _ATTRIBUTE_VALUES_HAS_NEXT;
          }

          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.ELEMENT,
             HtmlByteProto.RAW,
             HtmlByteProto.TEXT -> index = skipVarInt(index);

        case HtmlByteProto.END -> {
          index = rollbackIndex;

          break loop;
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    elementCtxAttrsIndexStore(index);

    stateSet(nextState);

    return nextState == _ATTRIBUTE_VALUES_HAS_NEXT;
  }

  final Object attributeValuesNext(Object maybeNext) {
    stateCAS(_ATTRIBUTE_VALUES_HAS_NEXT, _ATTRIBUTE_VALUES_NEXT);

    if (maybeNext != null) {
      return maybeNext;
    }

    // restore index
    int index;
    index = elementCtxAttrsIndexLoad();

    byte proto;
    proto = main[index++];

    return switch (proto) {
      case HtmlByteProto.AMBIGUOUS1 -> {
        index = jmp2(index);

        elementCtxAttrsIndexStore(index);

        // skip ordinal
        auxStart++;

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        yield toObject(v0, v1);
      }

      case HtmlByteProto.ATTRIBUTE1 -> {
        index = jmp2(index);

        elementCtxAttrsIndexStore(index);

        // skip ordinal
        auxStart++;

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        yield toObject(v0, v1);
      }

      case HtmlByteProto.ATTRIBUTE_EXT1 -> {
        // skip ordinal
        index++;

        byte v0;
        v0 = main[index++];

        byte v1;
        v1 = main[index++];

        elementCtxAttrsIndexStore(index);

        yield toObject(v0, v1);
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };
  }

  private Object toObject(byte v0, byte v1) {
    int objectIndex;
    objectIndex = HtmlBytes.decodeInt(v0, v1);

    return objectArray[objectIndex];
  }

  private String toObjectString(byte v0, byte v1) {
    Object o;
    o = toObject(v0, v1);

    return o.toString();
  }

  final void elementNodes() {
    // state check
    switch (statePeek()) {
      case _ELEMENT_START, _ELEMENT_ATTRS_EXHAUSTED -> {
        // valid state
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    stateSet(_ELEMENT_NODES_ITERABLE);
  }

  final void elementNodesIterator() {
    stateCAS(_ELEMENT_NODES_ITERABLE, _ELEMENT_NODES_ITERATOR);
  }

  final boolean elementNodesHasNext() {
    // iteration index
    int index;

    // state check
    switch (statePeek()) {
      case _ELEMENT_NODES_ITERATOR, _ELEMENT_NODES_NEXT -> {
        // valid state

        // restore index from context
        index = elementCtxNodesIndexLoad();
      }

      case _ELEMENT_START, _ELEMENT_ATTRS_EXHAUSTED, _ELEMENT_NODES_EXHAUSTED -> {
        // remove previous element context
        int parentIndex;
        parentIndex = elementCtxRemove();

        // restore index from context
        index = elementCtxNodesIndexLoad();

        if (index != parentIndex) {
          throw new IllegalStateException(
              """
              Last consumed element was not a child of this element
              """
          );
        }

        // restore name
        HtmlElement element;
        element = htmlElement();

        element.name = elementCtxNameLoad();
      }

      default -> throw new IllegalStateException(
          """
          %d state not allowed @ HtmlElement#nodes#hasNext
          """.formatted(statePeek())
      );
    }

    // has next
    byte nextState;
    nextState = _ELEMENT_NODES_EXHAUSTED;

    loop: while (index < mainIndex) {
      // assume 'worst case'
      // in the happy path we rollback the index
      int rollbackIndex;
      rollbackIndex = index;

      byte proto;
      proto = main[index++];

      switch (proto) {
        case HtmlByteProto.AMBIGUOUS1 -> {
          index = jmp2(index);

          byte ordinalByte;
          ordinalByte = main[auxStart++];

          int ordinal;
          ordinal = HtmlBytes.decodeInt(ordinalByte);

          HtmlAmbiguous ambiguous;
          ambiguous = HtmlAmbiguous.get(ordinal);

          // find out parent element
          HtmlElement element;
          element = htmlElement();

          HtmlElementName parent;
          parent = element.name;

          if (ambiguous.isAttributeOf(parent)) {
            continue loop;
          }

          index = rollbackIndex;

          nextState = _ELEMENT_NODES_HAS_NEXT;

          break loop;
        }

        case HtmlByteProto.ATTRIBUTE0,
             HtmlByteProto.ATTRIBUTE1,
             HtmlByteProto.TESTABLE -> index = skipVarInt(index);

        case HtmlByteProto.ATTRIBUTE_EXT1 -> index += 3;

        case HtmlByteProto.ELEMENT,
             HtmlByteProto.RAW,
             HtmlByteProto.TEXT -> {
          index = rollbackIndex;

          nextState = _ELEMENT_NODES_HAS_NEXT;

          break loop;
        }

        case HtmlByteProto.END -> {
          index = rollbackIndex;

          break loop;
        }

        case HtmlByteProto.LENGTH2 -> {
          byte len0;
          len0 = main[index++];

          byte len1;
          len1 = main[index++];

          int length;
          length = HtmlBytes.decodeInt(len0, len1);

          index += length;
        }

        case HtmlByteProto.STANDARD_NAME -> index += 1;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    }

    elementCtxNodesIndexStore(index);

    stateSet(nextState);

    return nextState == _ELEMENT_NODES_HAS_NEXT;
  }

  final Html.Node elementNodesNext() {
    stateCAS(_ELEMENT_NODES_HAS_NEXT, _ELEMENT_NODES_NEXT);

    // restore index from context
    int index;
    index = elementCtxNodesIndexLoad();

    byte proto;
    proto = main[index++];

    return switch (proto) {
      case HtmlByteProto.AMBIGUOUS1 -> {
        index = jmp2(index);

        // load ambiguous name

        byte ordinalByte;
        ordinalByte = main[auxStart++];

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        int ordinal;
        ordinal = HtmlBytes.decodeInt(ordinalByte);

        HtmlAmbiguous ambiguous;
        ambiguous = HtmlAmbiguous.get(ordinal);

        Html.ElementName element;
        element = ambiguous.element;

        main = Util.growIfNecessary(main, mainIndex + 13);

        /*00*/main[mainIndex++] = HtmlByteProto.MARKED4;
        /*01*/main[mainIndex++] = v0;
        /*02*/main[mainIndex++] = v1;
        /*03*/main[mainIndex++] = HtmlByteProto.INTERNAL4;

        /*04*/main[mainIndex++] = HtmlByteProto.LENGTH2;
        /*05*/main[mainIndex++] = HtmlBytes.encodeInt0(7);
        /*06*/main[mainIndex++] = HtmlBytes.encodeInt0(7);
        int elementStartIndex = mainIndex;
        /*07*/main[mainIndex++] = HtmlByteProto.STANDARD_NAME;
        /*08*/main[mainIndex++] = (byte) element.index();
        /*09*/main[mainIndex++] = HtmlByteProto.TEXT;
        /*10*/main[mainIndex++] = HtmlBytes.encodeInt0(10);
        /*11*/main[mainIndex++] = HtmlByteProto.END;
        /*12*/main[mainIndex++] = HtmlBytes.encodeInt0(11);
        /*13*/main[mainIndex++] = HtmlByteProto.INTERNAL;

        int parentIndex;
        parentIndex = index;

        elementCtxNodesIndexStore(parentIndex);

        yield element(elementStartIndex, parentIndex);
      }

      case HtmlByteProto.ELEMENT -> {
        index = jmp2(index);

        // skip fixed length
        auxStart += 2;

        int elementStartIndex;
        elementStartIndex = auxStart;

        int parentIndex;
        parentIndex = index;

        elementCtxNodesIndexStore(parentIndex);

        yield element(elementStartIndex, parentIndex);
      }

      case HtmlByteProto.RAW -> {
        index = jmp2(index);

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        elementCtxNodesIndexStore(index);

        // return value
        HtmlRawText raw;
        raw = (HtmlRawText) objectArray[objectIndex + OFFSET_RAW];

        // text value
        raw.value = toObjectString(v0, v1);

        yield raw;
      }

      case HtmlByteProto.TEXT -> {
        index = jmp2(index);

        byte v0;
        v0 = main[auxStart++];

        byte v1;
        v1 = main[auxStart++];

        elementCtxNodesIndexStore(index);

        yield htmlText(v0, v1);
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };
  }

  final String elementTestField() {
    // state check
    switch (statePeek()) {
      case _ELEMENT_START, _ELEMENT_ATTRS_EXHAUSTED -> {
        // valid state
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + statePeek()
      );
    }

    // we assume there's no test field
    String testField;
    testField = null;

    // restore index from context
    int index;
    index = elementCtxAttrsIndexLoad();

    loop: while (index < mainIndex) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case HtmlByteProto.TESTABLE -> {
          index = jmp2(index);

          byte v0;
          v0 = main[auxStart++];

          byte v1;
          v1 = main[auxStart++];

          testField = toObjectString(v0, v1);

          break loop;
        }

        case HtmlByteProto.ATTRIBUTE_EXT1 -> index += 2;

        case HtmlByteProto.END -> {
          break loop;
        }

        default -> index += 1;
      }
    }

    return testField;
  }

  private HtmlText htmlText(byte v0, byte v1) {
    HtmlText text;
    text = (HtmlText) objectArray[objectIndex + OFFSET_TEXT];

    // text value
    text.value = toObjectString(v0, v1);

    return text;
  }

  private void elementCtx(int startIndex, int parentIndex) {
    // current context length
    int length;
    length = auxIndex - mainStart;

    // set current context
    mainStart = auxIndex;

    // ensure aux length
    aux = Util.growIfNecessary(aux, auxIndex + 13);

    // 0
    aux[auxIndex++] = _ELEMENT_START;

    // 1-3 attrs iteration index
    aux[auxIndex++] = HtmlBytes.encodeInt0(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt1(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt2(startIndex);

    // 4-6 nodes iteration index
    aux[auxIndex++] = HtmlBytes.encodeInt0(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt1(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt2(startIndex);

    // 7-9 start index
    aux[auxIndex++] = HtmlBytes.encodeInt0(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt1(startIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt2(startIndex);

    // 10-12 parent index
    aux[auxIndex++] = HtmlBytes.encodeInt0(parentIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt1(parentIndex);
    aux[auxIndex++] = HtmlBytes.encodeInt2(parentIndex);

    // 13 parent context length
    aux[auxIndex++] = HtmlBytes.encodeInt0(length);
  }

  private int elementCtxAttrsIndexLoad() {
    byte b0;
    b0 = aux[mainStart + 1];

    byte b1;
    b1 = aux[mainStart + 2];

    byte b2;
    b2 = aux[mainStart + 3];

    return HtmlBytes.decodeLength3(b0, b1, b2);
  }

  private void elementCtxAttrsIndexStore(int value) {
    aux[mainStart + 1] = HtmlBytes.encodeInt0(value);

    aux[mainStart + 2] = HtmlBytes.encodeInt1(value);

    aux[mainStart + 3] = HtmlBytes.encodeInt2(value);
  }

  private HtmlElementName elementCtxNameLoad() {
    // restore start index
    byte b0;
    b0 = aux[mainStart + 7];

    byte b1;
    b1 = aux[mainStart + 8];

    byte b2;
    b2 = aux[mainStart + 9];

    int startIndex;
    startIndex = HtmlBytes.decodeLength3(b0, b1, b2);

    HtmlElementName name;

    // first proto should be the element's name
    byte proto;
    proto = main[startIndex++];

    switch (proto) {
      case HtmlByteProto.STANDARD_NAME -> {
        byte nameByte;
        nameByte = main[startIndex++];

        int ordinal;
        ordinal = HtmlBytes.decodeInt(nameByte);

        name = HtmlElementName.get(ordinal);
      }

      default -> throw new IllegalArgumentException(
          "Malformed element. Expected name but found=" + proto
      );
    }

    return name;
  }

  private int elementCtxNodesIndexLoad() {
    byte b0;
    b0 = aux[mainStart + 4];

    byte b1;
    b1 = aux[mainStart + 5];

    byte b2;
    b2 = aux[mainStart + 6];

    return HtmlBytes.decodeLength3(b0, b1, b2);
  }

  private void elementCtxNodesIndexStore(int value) {
    aux[mainStart + 4] = HtmlBytes.encodeInt0(value);

    aux[mainStart + 5] = HtmlBytes.encodeInt1(value);

    aux[mainStart + 6] = HtmlBytes.encodeInt2(value);
  }

  private int elementCtxRemove() {
    // restore parent index
    byte b0;
    b0 = aux[mainStart + 10];

    byte b1;
    b1 = aux[mainStart + 11];

    byte b2;
    b2 = aux[mainStart + 12];

    int parentIndex;
    parentIndex = HtmlBytes.decodeLength3(b0, b1, b2);

    // restore parent length
    byte len;
    len = aux[mainStart + 13];

    int length;
    length = HtmlBytes.decodeInt(len);

    // remove this context
    auxIndex = mainStart;

    // set parent as the current context
    mainStart = auxIndex - length;

    return parentIndex;
  }

  private HtmlElement htmlElement() {
    return (HtmlElement) objectArray[objectIndex + OFFSET_ELEMENT];
  }

  private void stateCheck(byte expected) {
    byte actual;
    actual = statePeek();

    if (actual != expected) {
      throw new IllegalStateException(
          """
          Found state '%d' but expected state '%d'
          """.formatted(actual, expected)
      );
    }
  }

  private void stateCAS(byte expected, byte next) {
    // not a real CAS
    // but it does compare and swap
    stateCheck(expected);

    aux[mainStart] = next;
  }

  private byte statePeek() {
    return aux[mainStart];
  }

  private void stateSet(byte value) {
    aux[mainStart] = value;
  }

  private int decodeLength(int index) {
    int startIndex;
    startIndex = index;

    byte maybeNeg;

    do {
      maybeNeg = main[index++];
    } while (maybeNeg < 0);

    auxStart = HtmlBytes.decodeOffset(main, startIndex, index);

    return index;
  }

  private int jmp2(int index) {
    int baseIndex;
    baseIndex = index;

    index = decodeLength(index);

    auxStart = baseIndex - auxStart;

    // skip ByteProto
    auxStart++;

    return index;
  }

  private int skipVarInt(int index) {
    byte len0;

    do {
      len0 = main[index++];
    } while (len0 < 0);

    return index;
  }

}

sealed class HtmlRecorderBase {

  byte[] aux = new byte[128];

  int auxIndex;

  int auxStart;

  byte[] main = new byte[256];

  int mainContents;

  int mainIndex;

  int mainStart;

  Object[] objectArray;

  int objectIndex;

  final void compilationBegin() {
    auxIndex = auxStart = 0;

    mainContents = mainIndex = mainStart = 0;

    objectIndex = 0;
  }

  /**
   * Generates the {@code <!DOCTYPE html>} doctype.
   */
  public final void doctype() {
    mainAdd(HtmlByteProto.DOCTYPE);
  }

  final void compilationEnd() {
    // TODO remove...
  }

  final void ambiguous(HtmlAmbiguous name, String value) {
    int ordinal;
    ordinal = name.ordinal();

    int object;
    object = objectAdd(value);

    mainAdd(
        HtmlByteProto.AMBIGUOUS1,

        // name
        HtmlBytes.encodeInt0(ordinal),

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL5
    );
  }

  final Html.AttributeInstruction attribute0(Html.AttributeName name) {
    int index;
    index = name.index();

    if (index < 0) {
      throw new UnsupportedOperationException("Custom attribute name");
    }

    mainAdd(
        HtmlByteProto.ATTRIBUTE0,

        // name
        HtmlBytes.encodeInt0(index),

        HtmlByteProto.INTERNAL3
    );

    return Html.ATTRIBUTE;
  }

  final Html.AttributeOrNoOp attribute0(Html.AttributeName name, Object value) {
    return attribute1(name, value, HtmlByteProto.ATTRIBUTE1);
  }

  private Html.AttributeOrNoOp attribute1(Html.AttributeName name, Object value, byte proto) {
    int index;
    index = name.index();

    if (index < 0) {
      throw new UnsupportedOperationException("Custom attribute name");
    }

    int object;
    object = objectAdd(value);

    mainAdd(
        proto,

        // name
        HtmlBytes.encodeInt0(index),

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL5
    );

    return Html.ATTRIBUTE;
  }

  public final Html.ElementInstruction element(Html.ElementName name, Html.Instruction... contents) {
    Check.notNull(name, "name == null");

    elementBegin(name);

    for (int i = 0; i < contents.length; i++) {
      Html.Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      elementValue(inst);
    }

    elementEnd();

    return Html.ELEMENT;
  }

  public final Html.ElementInstruction element(Html.ElementName name, String text) {
    Check.notNull(name, "name == null");
    Check.notNull(text, "text == null");

    textImpl(text);

    elementBegin(name);
    elementValue(Html.ELEMENT);
    elementEnd();

    return Html.ELEMENT;
  }

  final void elementBegin(Html.ElementName name) {
    commonBegin();

    mainAdd(
        HtmlByteProto.ELEMENT,

        // length takes 2 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL,

        HtmlByteProto.STANDARD_NAME,

        HtmlBytes.encodeName(name)
    );
  }

  final void elementValue(Html.Instruction value) {
    if (value == Html.ATTRIBUTE ||
        value == Html.ELEMENT ||
        value == Html.FRAGMENT) {
      // @ ByteProto
      mainContents--;

      byte proto;
      proto = main[mainContents--];

      switch (proto) {
        case HtmlByteProto.INTERNAL -> {
          int endIndex;
          endIndex = mainContents;

          byte maybeNeg;

          do {
            maybeNeg = main[mainContents--];
          } while (maybeNeg < 0);

          int length;
          length = HtmlBytes.decodeCommonEnd(main, mainContents, endIndex);

          mainContents -= length;
        }

        case HtmlByteProto.INTERNAL3 -> mainContents -= 3 - 2;

        case HtmlByteProto.INTERNAL4 -> mainContents -= 4 - 2;

        case HtmlByteProto.INTERNAL5 -> mainContents -= 5 - 2;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }

      auxAdd(HtmlByteProto.INTERNAL);
    }

    else if (value instanceof Html.AttributeObject ext) {
      AttributeName name;
      name = ext.name();

      int nameIndex;
      nameIndex = name.index();

      if (nameIndex < 0) {
        throw new UnsupportedOperationException("Custom attribute name");
      }

      int valueIndex;
      valueIndex = externalValue(ext.value());

      auxAdd(
          HtmlByteProto.ATTRIBUTE_EXT1,

          // name
          HtmlBytes.encodeInt0(nameIndex),

          // value
          HtmlBytes.encodeInt0(valueIndex),
          HtmlBytes.encodeInt1(valueIndex)
      );
    }

    else if (value == Html.NOOP) {
      // no-op
    }

    else {
      throw new UnsupportedOperationException(
          "Implement me :: type=" + value.getClass()
      );
    }
  }

  final void elementEnd() {
    // we iterate over each value added via elementValue(Instruction)
    int index;
    index = auxStart;

    int indexMax;
    indexMax = auxIndex;

    int contents;
    contents = mainContents;

    loop: while (index < indexMax) {
      byte mark;
      mark = aux[index++];

      switch (mark) {
        case HtmlByteProto.TEXT -> {
          mainAdd(mark, aux[index++], aux[index++]);
        }

        case HtmlByteProto.ATTRIBUTE_EXT1 -> {
          mainAdd(mark, aux[index++], aux[index++], aux[index++]);
        }

        case HtmlByteProto.INTERNAL -> {
          while (true) {
            byte proto;
            proto = main[contents];

            switch (proto) {
              case HtmlByteProto.ATTRIBUTE0 -> {
                contents = encodeInternal3(contents, proto);

                continue loop;
              }

              case HtmlByteProto.AMBIGUOUS1,
                   HtmlByteProto.ATTRIBUTE1 -> {
                contents = encodeInternal5(contents, proto);

                continue loop;
              }

              case HtmlByteProto.ELEMENT -> {
                contents = encodeElement(contents, proto);

                continue loop;
              }

              case HtmlByteProto.FLATTEN -> {
                contents = encodeFlatten(contents);

                continue loop;
              }

              case HtmlByteProto.FRAGMENT -> {
                contents = encodeFragment(contents);

                continue loop;
              }

              case HtmlByteProto.LENGTH2 -> contents = encodeLength2(contents);

              case HtmlByteProto.LENGTH3 -> contents = encodeLength3(contents);

              case HtmlByteProto.MARKED3 -> contents += 3;

              case HtmlByteProto.MARKED4 -> contents += 4;

              case HtmlByteProto.MARKED5 -> contents += 5;

              case HtmlByteProto.RAW,
                   HtmlByteProto.TEXT,
                   HtmlByteProto.TESTABLE -> {
                contents = encodeInternal4(contents, proto);

                continue loop;
              }

              default -> {
                throw new UnsupportedOperationException(
                    "Implement me :: proto=" + proto
                );
              }
            }
          }
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: mark=" + mark
        );
      }
    }

    commonEnd(mainContents, mainStart);

    // we clear the aux list
    auxIndex = auxStart;
  }

  final void flattenBegin() {
    commonBegin();

    mainAdd(
        HtmlByteProto.FLATTEN,

        // length takes 2 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL
    );
  }

  final void rawImpl(String value) {
    int object;
    object = objectAdd(value);

    mainAdd(
        HtmlByteProto.RAW,

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL4
    );
  }

  final void testableImpl(String name) {
    int object;
    object = objectAdd(name);

    mainAdd(
        HtmlByteProto.TESTABLE,

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL4
    );
  }

  final void textImpl(String value) {
    int object;
    object = objectAdd(value);

    mainAdd(
        HtmlByteProto.TEXT,

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL4
    );
  }

  final void auxAdd(byte b0) {
    aux = Util.growIfNecessary(aux, auxIndex + 0);
    aux[auxIndex++] = b0;
  }

  final void auxAdd(byte b0, byte b1) {
    aux = Util.growIfNecessary(aux, auxIndex + 1);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
  }

  final void auxAdd(byte b0, byte b1, byte b2) {
    aux = Util.growIfNecessary(aux, auxIndex + 2);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3) {
    aux = Util.growIfNecessary(aux, auxIndex + 3);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    aux = Util.growIfNecessary(aux, auxIndex + 4);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5) {
    aux = Util.growIfNecessary(aux, auxIndex + 5);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6) {
    aux = Util.growIfNecessary(aux, auxIndex + 6);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
    aux[auxIndex++] = b6;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
    aux = Util.growIfNecessary(aux, auxIndex + 7);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
    aux[auxIndex++] = b6;
    aux[auxIndex++] = b7;
  }

  private void commonBegin() {
    // we mark the start of our aux list
    auxStart = auxIndex;

    // we mark:
    // 1) the start of the contents of the current declaration
    // 2) the start of our main list
    mainContents = mainStart = mainIndex;
  }

  private void commonEnd(int contentsIndex, int startIndex) {
    // ensure main can hold 5 more elements
    // - ByteProto.END
    // - length
    // - length
    // - length
    // - ByteProto.INTERNAL
    main = Util.growIfNecessary(main, mainIndex + 4);

    // mark the end
    main[mainIndex++] = HtmlByteProto.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - contentsIndex - 1;

    mainIndex = HtmlBytes.encodeCommonEnd(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = HtmlByteProto.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1
    length -= 3;

    // we skip the first byte proto
    main[startIndex + 1] = HtmlBytes.encodeInt0(length);
    main[startIndex + 2] = HtmlBytes.encodeInt1(length);
  }

  private int encodeElement(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents++] = HtmlByteProto.LENGTH2;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    // point to next element
    int offset;
    offset = HtmlBytes.decodeInt(len0, len1);

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = Util.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeFlatten(int contents) {
    int index;
    index = contents;

    // mark this fragment
    main[index++] = HtmlByteProto.LENGTH2;

    // decode the length
    byte len0;
    len0 = main[index++];

    byte len1;
    len1 = main[index++];

    // point to next element
    int offset;
    offset = HtmlBytes.decodeInt(len0, len1);

    int maxIndex;
    maxIndex = index + offset;

    loop: while (index < maxIndex) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case HtmlByteProto.ATTRIBUTE_EXT1 -> {
          byte idx0;
          idx0 = main[index++];

          byte idx1;
          idx1 = main[index++];

          byte idx2;
          idx2 = main[index++];

          mainAdd(proto, idx0, idx1, idx2);
        }

        case HtmlByteProto.AMBIGUOUS1,
             HtmlByteProto.ATTRIBUTE0,
             HtmlByteProto.ATTRIBUTE1,
             HtmlByteProto.ELEMENT,
             HtmlByteProto.TEXT,
             HtmlByteProto.RAW -> {
          int elementIndex;
          elementIndex = index;

          do {
            len0 = main[index++];
          } while (len0 < 0);

          int len;
          len = HtmlBytes.decodeOffset(main, elementIndex, index);

          elementIndex -= len;

          // ensure main can hold least 4 elements
          // 0   - ByteProto
          // 1-3 - variable length
          main = Util.growIfNecessary(main, mainIndex + 3);

          main[mainIndex++] = proto;

          int length;
          length = mainIndex - elementIndex;

          mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);
        }

        case HtmlByteProto.END -> {
          break loop;
        }

        default -> {
          throw new UnsupportedOperationException(
              "Implement me :: proto=" + proto
          );
        }
      }
    }

    return maxIndex;
  }

  private int encodeFragment(int contents) {
    int index;
    index = contents;

    // mark this fragment
    main[index++] = HtmlByteProto.LENGTH3;

    // decode the length
    byte len0;
    len0 = main[index++];

    byte len1;
    len1 = main[index++];

    byte len2;
    len2 = main[index++];

    // point to next element
    int offset;
    offset = HtmlBytes.decodeLength3(len0, len1, len2);

    int maxIndex;
    maxIndex = index + offset;

    loop: while (index < maxIndex) {
      byte proto;
      proto = main[index];

      switch (proto) {
        case HtmlByteProto.AMBIGUOUS1 -> index = encodeInternal5(index, proto);

        case HtmlByteProto.ATTRIBUTE0 -> index = encodeInternal3(index, proto);

        case HtmlByteProto.ATTRIBUTE1 -> index = encodeInternal5(index, proto);

        case HtmlByteProto.ELEMENT -> index = encodeElement(index, proto);

        case HtmlByteProto.END -> {
          break loop;
        }

        case HtmlByteProto.FRAGMENT -> index = encodeFragment(index);

        case HtmlByteProto.LENGTH2 -> index = encodeLength2(index);

        case HtmlByteProto.LENGTH3 -> index = encodeLength3(index);

        case HtmlByteProto.MARKED3 -> index += 3;

        case HtmlByteProto.MARKED4 -> index += 4;

        case HtmlByteProto.MARKED5 -> index += 5;

        case HtmlByteProto.RAW,
             HtmlByteProto.TEXT -> index = encodeInternal4(index, proto);

        default -> {
          throw new UnsupportedOperationException(
              "Implement me :: proto=" + proto
          );
        }
      }
    }

    return maxIndex;
  }

  private int encodeInternal3(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = HtmlByteProto.MARKED3;

    // point to next
    int offset;
    offset = 3;

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = Util.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeInternal4(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = HtmlByteProto.MARKED4;

    // point to next
    int offset;
    offset = 4;

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = Util.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeInternal5(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = HtmlByteProto.MARKED5;

    // point to next
    int offset;
    offset = 5;

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = Util.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeLength2(int contents) {
    contents++;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    int length;
    length = HtmlBytes.decodeInt(len0, len1);

    // point to next element
    return contents + length;
  }

  private int encodeLength3(int contents) {
    contents++;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    byte len2;
    len2 = main[contents++];

    int length;
    length = HtmlBytes.decodeLength3(len0, len1, len2);

    // point to next element
    return contents + length;
  }

  private int externalValue(String value) {
    String result;
    result = value;

    if (value == null) {
      result = "null";
    }

    return objectAdd(result);
  }

  final int fragmentBegin() {
    // we mark:
    // 1) the start of the contents of the current declaration
    int startIndex;
    startIndex = mainIndex;

    mainAdd(
        HtmlByteProto.FRAGMENT,

        // length takes 3 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL,
        HtmlByteProto.NULL
    );

    return startIndex;
  }

  final void fragmentEnd(int startIndex) {
    // ensure main can hold 5 more elements
    // - ByteProto.END
    // - length
    // - length
    // - length
    // - ByteProto.INTERNAL
    main = Util.growIfNecessary(main, mainIndex + 4);

    // mark the end
    main[mainIndex++] = HtmlByteProto.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - startIndex - 1;

    mainIndex = HtmlBytes.encodeCommonEnd(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = HtmlByteProto.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1 + len2
    length -= 4;

    // we skip the first byte proto
    HtmlBytes.encodeLength3(main, startIndex + 1, length);
  }

  private void mainAdd(byte b0) {
    main = Util.growIfNecessary(main, mainIndex + 0);
    main[mainIndex++] = b0;
  }

  private void mainAdd(byte b0, byte b1, byte b2) {
    main = Util.growIfNecessary(main, mainIndex + 2);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3) {
    main = Util.growIfNecessary(main, mainIndex + 3);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    main = Util.growIfNecessary(main, mainIndex + 4);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
  }

  private int objectAdd(Object value) {
    int index;
    index = objectIndex++;

    if (objectArray == null) {
      objectArray = new Object[10];
    }

    objectArray = Util.growIfNecessary(objectArray, objectIndex);

    objectArray[index] = value;

    return index;
  }

}

/**
 * Provides methods for rendering HTML attributes.
 */
sealed class HtmlRecorderAttributes extends HtmlRecorderBase {

  HtmlRecorderAttributes() {}

  /**
   * Generates the {@code accesskey} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction accesskey(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ACCESSKEY, value);
  }

  /**
   * Generates the {@code action} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction action(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ACTION, value);
  }

  /**
   * Generates the {@code align} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction align(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALIGN, value);
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
  public final Html.AttributeInstruction alignmentBaseline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALIGNMENT_BASELINE, value);
  }

  /**
   * Generates the {@code alt} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction alt(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALT, value);
  }

  /**
   * Generates the {@code aria-hidden} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction ariaHidden(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ARIA_HIDDEN, value);
  }

  /**
   * Generates the {@code aria-label} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction ariaLabel(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ARIA_LABEL, value);
  }

  /**
   * Generates the {@code async} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction async() {
    return attribute0(HtmlAttributeName.ASYNC);
  }

  /**
   * Generates the {@code autocomplete} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction autocomplete(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.AUTOCOMPLETE, value);
  }

  /**
   * Generates the {@code autofocus} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction autofocus() {
    return attribute0(HtmlAttributeName.AUTOFOCUS);
  }

  /**
   * Generates the {@code baseline-shift} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction baselineShift(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.BASELINE_SHIFT, value);
  }

  /**
   * Generates the {@code border} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction border(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.BORDER, value);
  }

  /**
   * Generates the {@code cellpadding} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction cellpadding(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CELLPADDING, value);
  }

  /**
   * Generates the {@code cellspacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction cellspacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CELLSPACING, value);
  }

  /**
   * Generates the {@code charset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction charset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CHARSET, value);
  }

  /**
   * Generates the {@code cite} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction cite(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CITE, value);
  }

  /**
   * Generates the {@code class} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction className(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CLASS, value);
  }

  /**
   * Generates the {@code clip-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction clipRule(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CLIP_RULE, value);
  }

  /**
   * Generates the {@code color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction color(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR, value);
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
  public final Html.AttributeInstruction colorInterpolation(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR_INTERPOLATION, value);
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
  public final Html.AttributeInstruction colorInterpolationFilters(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR_INTERPOLATION_FILTERS, value);
  }

  /**
   * Generates the {@code cols} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction cols(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLS, value);
  }

  /**
   * Generates the {@code content} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction content(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CONTENT, value);
  }

  /**
   * Generates the {@code contenteditable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction contenteditable(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CONTENTEDITABLE, value);
  }

  /**
   * Generates the {@code crossorigin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction crossorigin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CROSSORIGIN, value);
  }

  /**
   * Generates the {@code cursor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction cursor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CURSOR, value);
  }

  /**
   * Generates the {@code d} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction d(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.D, value);
  }

  /**
   * Generates the {@code defer} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction defer() {
    return attribute0(HtmlAttributeName.DEFER);
  }

  /**
   * Generates the {@code dir} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction dir(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIR, value);
  }

  /**
   * Generates the {@code direction} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction direction(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIRECTION, value);
  }

  /**
   * Generates the {@code dirname} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction dirname(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIRNAME, value);
  }

  /**
   * Generates the {@code disabled} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction disabled() {
    return attribute0(HtmlAttributeName.DISABLED);
  }

  /**
   * Generates the {@code display} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction display(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DISPLAY, value);
  }

  /**
   * Generates the {@code dominant-baseline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction dominantBaseline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DOMINANT_BASELINE, value);
  }

  /**
   * Generates the {@code draggable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction draggable(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DRAGGABLE, value);
  }

  /**
   * Generates the {@code enctype} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction enctype(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ENCTYPE, value);
  }

  /**
   * Generates the {@code fill} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction fill(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL, value);
  }

  /**
   * Generates the {@code fill-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction fillOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL_OPACITY, value);
  }

  /**
   * Generates the {@code fill-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction fillRule(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL_RULE, value);
  }

  /**
   * Generates the {@code filter} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction filter(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILTER, value);
  }

  /**
   * Generates the {@code flood-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction floodColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FLOOD_COLOR, value);
  }

  /**
   * Generates the {@code flood-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction floodOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FLOOD_OPACITY, value);
  }

  /**
   * Generates the {@code font-family} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction fontFamily(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_FAMILY, value);
  }

  /**
   * Generates the {@code font-size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction fontSize(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_SIZE, value);
  }

  /**
   * Generates the {@code font-size-adjust} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction fontSizeAdjust(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_SIZE_ADJUST, value);
  }

  /**
   * Generates the {@code font-stretch} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction fontStretch(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_STRETCH, value);
  }

  /**
   * Generates the {@code font-style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction fontStyle(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_STYLE, value);
  }

  /**
   * Generates the {@code font-variant} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction fontVariant(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_VARIANT, value);
  }

  /**
   * Generates the {@code font-weight} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction fontWeight(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_WEIGHT, value);
  }

  /**
   * Generates the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction forAttr(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FOR, value);
  }

  /**
   * Generates the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction forElement(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FOR, value);
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
  public final Html.AttributeInstruction glyphOrientationHorizontal(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.GLYPH_ORIENTATION_HORIZONTAL, value);
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
  public final Html.AttributeInstruction glyphOrientationVertical(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.GLYPH_ORIENTATION_VERTICAL, value);
  }

  /**
   * Generates the {@code height} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction height(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HEIGHT, value);
  }

  /**
   * Generates the {@code hidden} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction hidden() {
    return attribute0(HtmlAttributeName.HIDDEN);
  }

  /**
   * Generates the {@code href} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction href(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HREF, value);
  }

  /**
   * Generates the {@code http-equiv} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction httpEquiv(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HTTP_EQUIV, value);
  }

  /**
   * Generates the {@code id} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction id(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ID, value);
  }

  /**
   * Generates the {@code image-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction imageRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.IMAGE_RENDERING, value);
  }

  /**
   * Generates the {@code integrity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction integrity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.INTEGRITY, value);
  }

  /**
   * Generates the {@code lang} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction lang(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LANG, value);
  }

  /**
   * Generates the {@code letter-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction letterSpacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LETTER_SPACING, value);
  }

  /**
   * Generates the {@code lighting-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction lightingColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LIGHTING_COLOR, value);
  }

  /**
   * Generates the {@code marker-end} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction markerEnd(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_END, value);
  }

  /**
   * Generates the {@code marker-mid} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction markerMid(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_MID, value);
  }

  /**
   * Generates the {@code marker-start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction markerStart(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_START, value);
  }

  /**
   * Generates the {@code mask} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction mask(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MASK, value);
  }

  /**
   * Generates the {@code mask-type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction maskType(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MASK_TYPE, value);
  }

  /**
   * Generates the {@code maxlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction maxlength(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MAXLENGTH, value);
  }

  /**
   * Generates the {@code media} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction media(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MEDIA, value);
  }

  /**
   * Generates the {@code method} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction method(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.METHOD, value);
  }

  /**
   * Generates the {@code minlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction minlength(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MINLENGTH, value);
  }

  /**
   * Generates the {@code multiple} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction multiple() {
    return attribute0(HtmlAttributeName.MULTIPLE);
  }

  /**
   * Generates the {@code name} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction name(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.NAME, value);
  }

  /**
   * Generates the {@code nomodule} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction nomodule() {
    return attribute0(HtmlAttributeName.NOMODULE);
  }

  /**
   * Generates the {@code onafterprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onafterprint(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONAFTERPRINT, value);
  }

  /**
   * Generates the {@code onbeforeprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onbeforeprint(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONBEFOREPRINT, value);
  }

  /**
   * Generates the {@code onbeforeunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onbeforeunload(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONBEFOREUNLOAD, value);
  }

  /**
   * Generates the {@code onclick} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onclick(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONCLICK, value);
  }

  /**
   * Generates the {@code onhashchange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onhashchange(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONHASHCHANGE, value);
  }

  /**
   * Generates the {@code onlanguagechange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onlanguagechange(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONLANGUAGECHANGE, value);
  }

  /**
   * Generates the {@code onmessage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onmessage(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONMESSAGE, value);
  }

  /**
   * Generates the {@code onoffline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onoffline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONOFFLINE, value);
  }

  /**
   * Generates the {@code ononline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction ononline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONONLINE, value);
  }

  /**
   * Generates the {@code onpagehide} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onpagehide(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPAGEHIDE, value);
  }

  /**
   * Generates the {@code onpageshow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onpageshow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPAGESHOW, value);
  }

  /**
   * Generates the {@code onpopstate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onpopstate(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPOPSTATE, value);
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
  public final Html.AttributeInstruction onrejectionhandled(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONREJECTIONHANDLED, value);
  }

  /**
   * Generates the {@code onstorage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onstorage(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONSTORAGE, value);
  }

  /**
   * Generates the {@code onsubmit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onsubmit(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONSUBMIT, value);
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
  public final Html.AttributeInstruction onunhandledrejection(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONUNHANDLEDREJECTION, value);
  }

  /**
   * Generates the {@code onunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction onunload(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONUNLOAD, value);
  }

  /**
   * Generates the {@code opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction opacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.OPACITY, value);
  }

  /**
   * Generates the {@code open} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction open() {
    return attribute0(HtmlAttributeName.OPEN);
  }

  /**
   * Generates the {@code overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction overflow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.OVERFLOW, value);
  }

  /**
   * Generates the {@code paint-order} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction paintOrder(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PAINT_ORDER, value);
  }

  /**
   * Generates the {@code placeholder} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction placeholder(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PLACEHOLDER, value);
  }

  /**
   * Generates the {@code pointer-events} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction pointerEvents(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.POINTER_EVENTS, value);
  }

  /**
   * Generates the {@code property} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction property(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PROPERTY, value);
  }

  /**
   * Generates the {@code readonly} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction readonly() {
    return attribute0(HtmlAttributeName.READONLY);
  }

  /**
   * Generates the {@code referrerpolicy} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction referrerpolicy(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REFERRERPOLICY, value);
  }

  /**
   * Generates the {@code rel} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction rel(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REL, value);
  }

  /**
   * Generates the {@code required} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction required() {
    return attribute0(HtmlAttributeName.REQUIRED);
  }

  /**
   * Generates the {@code rev} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction rev(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REV, value);
  }

  /**
   * Generates the {@code reversed} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction reversed() {
    return attribute0(HtmlAttributeName.REVERSED);
  }

  /**
   * Generates the {@code role} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction role(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ROLE, value);
  }

  /**
   * Generates the {@code rows} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction rows(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ROWS, value);
  }

  /**
   * Generates the {@code selected} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction selected() {
    return attribute0(HtmlAttributeName.SELECTED);
  }

  /**
   * Generates the {@code shape-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction shapeRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SHAPE_RENDERING, value);
  }

  /**
   * Generates the {@code size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction size(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SIZE, value);
  }

  /**
   * Generates the {@code sizes} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction sizes(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SIZES, value);
  }

  /**
   * Generates the {@code spellcheck} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction spellcheck(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SPELLCHECK, value);
  }

  /**
   * Generates the {@code src} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction src(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SRC, value);
  }

  /**
   * Generates the {@code srcset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction srcset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SRCSET, value);
  }

  /**
   * Generates the {@code start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction start(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.START, value);
  }

  /**
   * Generates the {@code stop-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction stopColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STOP_COLOR, value);
  }

  /**
   * Generates the {@code stop-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction stopOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STOP_OPACITY, value);
  }

  /**
   * Generates the {@code stroke} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction stroke(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE, value);
  }

  /**
   * Generates the {@code stroke-dasharray} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction strokeDasharray(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_DASHARRAY, value);
  }

  /**
   * Generates the {@code stroke-dashoffset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction strokeDashoffset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_DASHOFFSET, value);
  }

  /**
   * Generates the {@code stroke-linecap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction strokeLinecap(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_LINECAP, value);
  }

  /**
   * Generates the {@code stroke-linejoin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction strokeLinejoin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_LINEJOIN, value);
  }

  /**
   * Generates the {@code stroke-miterlimit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction strokeMiterlimit(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_MITERLIMIT, value);
  }

  /**
   * Generates the {@code stroke-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction strokeOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_OPACITY, value);
  }

  /**
   * Generates the {@code stroke-width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction strokeWidth(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_WIDTH, value);
  }

  /**
   * Generates the {@code style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction inlineStyle(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STYLE, value);
  }

  /**
   * Generates the {@code tabindex} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction tabindex(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TABINDEX, value);
  }

  /**
   * Generates the {@code target} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction target(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TARGET, value);
  }

  /**
   * Generates the {@code text-anchor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction textAnchor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_ANCHOR, value);
  }

  /**
   * Generates the {@code text-decoration} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction textDecoration(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_DECORATION, value);
  }

  /**
   * Generates the {@code text-overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction textOverflow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_OVERFLOW, value);
  }

  /**
   * Generates the {@code text-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction textRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_RENDERING, value);
  }

  /**
   * Generates the {@code transform} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction transform(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSFORM, value);
  }

  /**
   * Generates the {@code transform-origin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction transformOrigin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSFORM_ORIGIN, value);
  }

  /**
   * Generates the {@code translate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction translate(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSLATE, value);
  }

  /**
   * Generates the {@code type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction type(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TYPE, value);
  }

  /**
   * Generates the {@code unicode-bidi} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction unicodeBidi(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.UNICODE_BIDI, value);
  }

  /**
   * Generates the {@code value} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction value(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VALUE, value);
  }

  /**
   * Generates the {@code vector-effect} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction vectorEffect(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VECTOR_EFFECT, value);
  }

  /**
   * Generates the {@code viewBox} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction viewBox(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VIEWBOX, value);
  }

  /**
   * Generates the {@code visibility} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction visibility(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VISIBILITY, value);
  }

  /**
   * Generates the {@code white-space} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction whiteSpace(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WHITE_SPACE, value);
  }

  /**
   * Generates the {@code width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction width(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WIDTH, value);
  }

  /**
   * Generates the {@code word-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction wordSpacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WORD_SPACING, value);
  }

  /**
   * Generates the {@code wrap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction wrap(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WRAP, value);
  }

  /**
   * Generates the {@code writing-mode} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction writingMode(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WRITING_MODE, value);
  }

  /**
   * Generates the {@code xmlns} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.AttributeInstruction xmlns(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.XMLNS, value);
  }

}

/**
 * Provides methods for rendering HTML elements.
 */
sealed class HtmlRecorderElements extends HtmlRecorderAttributes permits HtmlRecorder {

  HtmlRecorderElements() {}

  /**
   * Generates the {@code a} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction a(Html.Instruction... contents) {
    return element(HtmlElementName.A, contents);
  }

  /**
   * Generates the {@code a} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction a(String text) {
    return element(HtmlElementName.A, text);
  }

  /**
   * Generates the {@code abbr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction abbr(Html.Instruction... contents) {
    return element(HtmlElementName.ABBR, contents);
  }

  /**
   * Generates the {@code abbr} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction abbr(String text) {
    return element(HtmlElementName.ABBR, text);
  }

  /**
   * Generates the {@code article} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction article(Html.Instruction... contents) {
    return element(HtmlElementName.ARTICLE, contents);
  }

  /**
   * Generates the {@code article} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction article(String text) {
    return element(HtmlElementName.ARTICLE, text);
  }

  /**
   * Generates the {@code b} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction b(Html.Instruction... contents) {
    return element(HtmlElementName.B, contents);
  }

  /**
   * Generates the {@code b} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction b(String text) {
    return element(HtmlElementName.B, text);
  }

  /**
   * Generates the {@code blockquote} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction blockquote(Html.Instruction... contents) {
    return element(HtmlElementName.BLOCKQUOTE, contents);
  }

  /**
   * Generates the {@code blockquote} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction blockquote(String text) {
    return element(HtmlElementName.BLOCKQUOTE, text);
  }

  /**
   * Generates the {@code body} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction body(Html.Instruction... contents) {
    return element(HtmlElementName.BODY, contents);
  }

  /**
   * Generates the {@code body} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction body(String text) {
    return element(HtmlElementName.BODY, text);
  }

  /**
   * Generates the {@code br} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction br(Html.VoidInstruction... contents) {
    return element(HtmlElementName.BR, contents);
  }

  /**
   * Generates the {@code button} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction button(Html.Instruction... contents) {
    return element(HtmlElementName.BUTTON, contents);
  }

  /**
   * Generates the {@code button} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction button(String text) {
    return element(HtmlElementName.BUTTON, text);
  }

  /**
   * Generates the {@code clipPath} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction clipPath(Html.Instruction... contents) {
    return element(HtmlElementName.CLIPPATH, contents);
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
  public final Html.ElementInstruction clipPath(String text) {
    ambiguous(HtmlAmbiguous.CLIPPATH, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code code} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction code(Html.Instruction... contents) {
    return element(HtmlElementName.CODE, contents);
  }

  /**
   * Generates the {@code code} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction code(String text) {
    return element(HtmlElementName.CODE, text);
  }

  /**
   * Generates the {@code dd} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction dd(Html.Instruction... contents) {
    return element(HtmlElementName.DD, contents);
  }

  /**
   * Generates the {@code dd} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction dd(String text) {
    return element(HtmlElementName.DD, text);
  }

  /**
   * Generates the {@code defs} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction defs(Html.Instruction... contents) {
    return element(HtmlElementName.DEFS, contents);
  }

  /**
   * Generates the {@code defs} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction defs(String text) {
    return element(HtmlElementName.DEFS, text);
  }

  /**
   * Generates the {@code details} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction details(Html.Instruction... contents) {
    return element(HtmlElementName.DETAILS, contents);
  }

  /**
   * Generates the {@code details} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction details(String text) {
    return element(HtmlElementName.DETAILS, text);
  }

  /**
   * Generates the {@code div} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction div(Html.Instruction... contents) {
    return element(HtmlElementName.DIV, contents);
  }

  /**
   * Generates the {@code div} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction div(String text) {
    return element(HtmlElementName.DIV, text);
  }

  /**
   * Generates the {@code dl} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction dl(Html.Instruction... contents) {
    return element(HtmlElementName.DL, contents);
  }

  /**
   * Generates the {@code dl} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction dl(String text) {
    return element(HtmlElementName.DL, text);
  }

  /**
   * Generates the {@code dt} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction dt(Html.Instruction... contents) {
    return element(HtmlElementName.DT, contents);
  }

  /**
   * Generates the {@code dt} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction dt(String text) {
    return element(HtmlElementName.DT, text);
  }

  /**
   * Generates the {@code em} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction em(Html.Instruction... contents) {
    return element(HtmlElementName.EM, contents);
  }

  /**
   * Generates the {@code em} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction em(String text) {
    return element(HtmlElementName.EM, text);
  }

  /**
   * Generates the {@code fieldset} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction fieldset(Html.Instruction... contents) {
    return element(HtmlElementName.FIELDSET, contents);
  }

  /**
   * Generates the {@code fieldset} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction fieldset(String text) {
    return element(HtmlElementName.FIELDSET, text);
  }

  /**
   * Generates the {@code figure} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction figure(Html.Instruction... contents) {
    return element(HtmlElementName.FIGURE, contents);
  }

  /**
   * Generates the {@code figure} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction figure(String text) {
    return element(HtmlElementName.FIGURE, text);
  }

  /**
   * Generates the {@code footer} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction footer(Html.Instruction... contents) {
    return element(HtmlElementName.FOOTER, contents);
  }

  /**
   * Generates the {@code footer} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction footer(String text) {
    return element(HtmlElementName.FOOTER, text);
  }

  /**
   * Generates the {@code form} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction form(Html.Instruction... contents) {
    return element(HtmlElementName.FORM, contents);
  }

  /**
   * Generates the {@code form} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.ElementInstruction form(String text) {
    ambiguous(HtmlAmbiguous.FORM, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code g} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction g(Html.Instruction... contents) {
    return element(HtmlElementName.G, contents);
  }

  /**
   * Generates the {@code g} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction g(String text) {
    return element(HtmlElementName.G, text);
  }

  /**
   * Generates the {@code h1} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h1(Html.Instruction... contents) {
    return element(HtmlElementName.H1, contents);
  }

  /**
   * Generates the {@code h1} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h1(String text) {
    return element(HtmlElementName.H1, text);
  }

  /**
   * Generates the {@code h2} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h2(Html.Instruction... contents) {
    return element(HtmlElementName.H2, contents);
  }

  /**
   * Generates the {@code h2} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h2(String text) {
    return element(HtmlElementName.H2, text);
  }

  /**
   * Generates the {@code h3} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h3(Html.Instruction... contents) {
    return element(HtmlElementName.H3, contents);
  }

  /**
   * Generates the {@code h3} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h3(String text) {
    return element(HtmlElementName.H3, text);
  }

  /**
   * Generates the {@code h4} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h4(Html.Instruction... contents) {
    return element(HtmlElementName.H4, contents);
  }

  /**
   * Generates the {@code h4} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h4(String text) {
    return element(HtmlElementName.H4, text);
  }

  /**
   * Generates the {@code h5} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h5(Html.Instruction... contents) {
    return element(HtmlElementName.H5, contents);
  }

  /**
   * Generates the {@code h5} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h5(String text) {
    return element(HtmlElementName.H5, text);
  }

  /**
   * Generates the {@code h6} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h6(Html.Instruction... contents) {
    return element(HtmlElementName.H6, contents);
  }

  /**
   * Generates the {@code h6} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction h6(String text) {
    return element(HtmlElementName.H6, text);
  }

  /**
   * Generates the {@code head} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction head(Html.Instruction... contents) {
    return element(HtmlElementName.HEAD, contents);
  }

  /**
   * Generates the {@code head} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction head(String text) {
    return element(HtmlElementName.HEAD, text);
  }

  /**
   * Generates the {@code header} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction header(Html.Instruction... contents) {
    return element(HtmlElementName.HEADER, contents);
  }

  /**
   * Generates the {@code header} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction header(String text) {
    return element(HtmlElementName.HEADER, text);
  }

  /**
   * Generates the {@code hgroup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction hgroup(Html.Instruction... contents) {
    return element(HtmlElementName.HGROUP, contents);
  }

  /**
   * Generates the {@code hgroup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction hgroup(String text) {
    return element(HtmlElementName.HGROUP, text);
  }

  /**
   * Generates the {@code hr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction hr(Html.VoidInstruction... contents) {
    return element(HtmlElementName.HR, contents);
  }

  /**
   * Generates the {@code html} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction html(Html.Instruction... contents) {
    return element(HtmlElementName.HTML, contents);
  }

  /**
   * Generates the {@code html} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction html(String text) {
    return element(HtmlElementName.HTML, text);
  }

  /**
   * Generates the {@code img} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction img(Html.VoidInstruction... contents) {
    return element(HtmlElementName.IMG, contents);
  }

  /**
   * Generates the {@code input} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction input(Html.VoidInstruction... contents) {
    return element(HtmlElementName.INPUT, contents);
  }

  /**
   * Generates the {@code kbd} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction kbd(Html.Instruction... contents) {
    return element(HtmlElementName.KBD, contents);
  }

  /**
   * Generates the {@code kbd} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction kbd(String text) {
    return element(HtmlElementName.KBD, text);
  }

  /**
   * Generates the {@code label} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction label(Html.Instruction... contents) {
    return element(HtmlElementName.LABEL, contents);
  }

  /**
   * Generates the {@code label} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.ElementInstruction label(String text) {
    ambiguous(HtmlAmbiguous.LABEL, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code legend} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction legend(Html.Instruction... contents) {
    return element(HtmlElementName.LEGEND, contents);
  }

  /**
   * Generates the {@code legend} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction legend(String text) {
    return element(HtmlElementName.LEGEND, text);
  }

  /**
   * Generates the {@code li} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction li(Html.Instruction... contents) {
    return element(HtmlElementName.LI, contents);
  }

  /**
   * Generates the {@code li} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction li(String text) {
    return element(HtmlElementName.LI, text);
  }

  /**
   * Generates the {@code link} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction link(Html.VoidInstruction... contents) {
    return element(HtmlElementName.LINK, contents);
  }

  /**
   * Generates the {@code main} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction main(Html.Instruction... contents) {
    return element(HtmlElementName.MAIN, contents);
  }

  /**
   * Generates the {@code main} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction main(String text) {
    return element(HtmlElementName.MAIN, text);
  }

  /**
   * Generates the {@code menu} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction menu(Html.Instruction... contents) {
    return element(HtmlElementName.MENU, contents);
  }

  /**
   * Generates the {@code menu} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction menu(String text) {
    return element(HtmlElementName.MENU, text);
  }

  /**
   * Generates the {@code meta} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction meta(Html.VoidInstruction... contents) {
    return element(HtmlElementName.META, contents);
  }

  /**
   * Generates the {@code nav} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction nav(Html.Instruction... contents) {
    return element(HtmlElementName.NAV, contents);
  }

  /**
   * Generates the {@code nav} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction nav(String text) {
    return element(HtmlElementName.NAV, text);
  }

  /**
   * Generates the {@code ol} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction ol(Html.Instruction... contents) {
    return element(HtmlElementName.OL, contents);
  }

  /**
   * Generates the {@code ol} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction ol(String text) {
    return element(HtmlElementName.OL, text);
  }

  /**
   * Generates the {@code optgroup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction optgroup(Html.Instruction... contents) {
    return element(HtmlElementName.OPTGROUP, contents);
  }

  /**
   * Generates the {@code optgroup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction optgroup(String text) {
    return element(HtmlElementName.OPTGROUP, text);
  }

  /**
   * Generates the {@code option} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction option(Html.Instruction... contents) {
    return element(HtmlElementName.OPTION, contents);
  }

  /**
   * Generates the {@code option} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction option(String text) {
    return element(HtmlElementName.OPTION, text);
  }

  /**
   * Generates the {@code p} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction p(Html.Instruction... contents) {
    return element(HtmlElementName.P, contents);
  }

  /**
   * Generates the {@code p} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction p(String text) {
    return element(HtmlElementName.P, text);
  }

  /**
   * Generates the {@code path} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction path(Html.Instruction... contents) {
    return element(HtmlElementName.PATH, contents);
  }

  /**
   * Generates the {@code path} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction path(String text) {
    return element(HtmlElementName.PATH, text);
  }

  /**
   * Generates the {@code pre} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction pre(Html.Instruction... contents) {
    return element(HtmlElementName.PRE, contents);
  }

  /**
   * Generates the {@code pre} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction pre(String text) {
    return element(HtmlElementName.PRE, text);
  }

  /**
   * Generates the {@code progress} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction progress(Html.Instruction... contents) {
    return element(HtmlElementName.PROGRESS, contents);
  }

  /**
   * Generates the {@code progress} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction progress(String text) {
    return element(HtmlElementName.PROGRESS, text);
  }

  /**
   * Generates the {@code samp} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction samp(Html.Instruction... contents) {
    return element(HtmlElementName.SAMP, contents);
  }

  /**
   * Generates the {@code samp} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction samp(String text) {
    return element(HtmlElementName.SAMP, text);
  }

  /**
   * Generates the {@code script} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction script(Html.Instruction... contents) {
    return element(HtmlElementName.SCRIPT, contents);
  }

  /**
   * Generates the {@code script} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction script(String text) {
    return element(HtmlElementName.SCRIPT, text);
  }

  /**
   * Generates the {@code section} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction section(Html.Instruction... contents) {
    return element(HtmlElementName.SECTION, contents);
  }

  /**
   * Generates the {@code section} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction section(String text) {
    return element(HtmlElementName.SECTION, text);
  }

  /**
   * Generates the {@code select} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction select(Html.Instruction... contents) {
    return element(HtmlElementName.SELECT, contents);
  }

  /**
   * Generates the {@code select} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction select(String text) {
    return element(HtmlElementName.SELECT, text);
  }

  /**
   * Generates the {@code small} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction small(Html.Instruction... contents) {
    return element(HtmlElementName.SMALL, contents);
  }

  /**
   * Generates the {@code small} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction small(String text) {
    return element(HtmlElementName.SMALL, text);
  }

  /**
   * Generates the {@code span} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction span(Html.Instruction... contents) {
    return element(HtmlElementName.SPAN, contents);
  }

  /**
   * Generates the {@code span} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction span(String text) {
    return element(HtmlElementName.SPAN, text);
  }

  /**
   * Generates the {@code strong} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction strong(Html.Instruction... contents) {
    return element(HtmlElementName.STRONG, contents);
  }

  /**
   * Generates the {@code strong} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction strong(String text) {
    return element(HtmlElementName.STRONG, text);
  }

  /**
   * Generates the {@code style} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction style(Html.Instruction... contents) {
    return element(HtmlElementName.STYLE, contents);
  }

  /**
   * Generates the {@code style} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction style(String text) {
    return element(HtmlElementName.STYLE, text);
  }

  /**
   * Generates the {@code sub} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction sub(Html.Instruction... contents) {
    return element(HtmlElementName.SUB, contents);
  }

  /**
   * Generates the {@code sub} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction sub(String text) {
    return element(HtmlElementName.SUB, text);
  }

  /**
   * Generates the {@code summary} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction summary(Html.Instruction... contents) {
    return element(HtmlElementName.SUMMARY, contents);
  }

  /**
   * Generates the {@code summary} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction summary(String text) {
    return element(HtmlElementName.SUMMARY, text);
  }

  /**
   * Generates the {@code sup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction sup(Html.Instruction... contents) {
    return element(HtmlElementName.SUP, contents);
  }

  /**
   * Generates the {@code sup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction sup(String text) {
    return element(HtmlElementName.SUP, text);
  }

  /**
   * Generates the {@code svg} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction svg(Html.Instruction... contents) {
    return element(HtmlElementName.SVG, contents);
  }

  /**
   * Generates the {@code svg} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction svg(String text) {
    return element(HtmlElementName.SVG, text);
  }

  /**
   * Generates the {@code table} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction table(Html.Instruction... contents) {
    return element(HtmlElementName.TABLE, contents);
  }

  /**
   * Generates the {@code table} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction table(String text) {
    return element(HtmlElementName.TABLE, text);
  }

  /**
   * Generates the {@code tbody} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction tbody(Html.Instruction... contents) {
    return element(HtmlElementName.TBODY, contents);
  }

  /**
   * Generates the {@code tbody} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction tbody(String text) {
    return element(HtmlElementName.TBODY, text);
  }

  /**
   * Generates the {@code td} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction td(Html.Instruction... contents) {
    return element(HtmlElementName.TD, contents);
  }

  /**
   * Generates the {@code td} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction td(String text) {
    return element(HtmlElementName.TD, text);
  }

  /**
   * Generates the {@code template} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction template(Html.Instruction... contents) {
    return element(HtmlElementName.TEMPLATE, contents);
  }

  /**
   * Generates the {@code template} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction template(String text) {
    return element(HtmlElementName.TEMPLATE, text);
  }

  /**
   * Generates the {@code textarea} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction textarea(Html.Instruction... contents) {
    return element(HtmlElementName.TEXTAREA, contents);
  }

  /**
   * Generates the {@code textarea} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction textarea(String text) {
    return element(HtmlElementName.TEXTAREA, text);
  }

  /**
   * Generates the {@code th} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction th(Html.Instruction... contents) {
    return element(HtmlElementName.TH, contents);
  }

  /**
   * Generates the {@code th} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction th(String text) {
    return element(HtmlElementName.TH, text);
  }

  /**
   * Generates the {@code thead} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction thead(Html.Instruction... contents) {
    return element(HtmlElementName.THEAD, contents);
  }

  /**
   * Generates the {@code thead} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction thead(String text) {
    return element(HtmlElementName.THEAD, text);
  }

  /**
   * Generates the {@code title} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction title(Html.Instruction... contents) {
    return element(HtmlElementName.TITLE, contents);
  }

  /**
   * Generates the {@code title} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.ElementInstruction title(String text) {
    ambiguous(HtmlAmbiguous.TITLE, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code tr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction tr(Html.Instruction... contents) {
    return element(HtmlElementName.TR, contents);
  }

  /**
   * Generates the {@code tr} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction tr(String text) {
    return element(HtmlElementName.TR, text);
  }

  /**
   * Generates the {@code ul} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction ul(Html.Instruction... contents) {
    return element(HtmlElementName.UL, contents);
  }

  /**
   * Generates the {@code ul} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.ElementInstruction ul(String text) {
    return element(HtmlElementName.UL, text);
  }

}
