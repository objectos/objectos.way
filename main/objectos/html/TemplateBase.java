/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import java.util.function.Consumer;
import java.util.function.Function;
import objectos.html.Api.GlobalAttribute;
import objectos.lang.object.Check;

public sealed abstract class TemplateBase
    extends TemplateElements
    implements Html.Extensible
    permits HtmlComponent, HtmlTemplate {

  TemplateBase() {}

  @Override
  public final GlobalAttribute renderAttribute(Function<Html, GlobalAttribute> attribute) {
    Html html;
    html = $html();

    return attribute.apply(html);
  }

  @Override
  public final void renderFragment(Consumer<Html> plugin) {
    Html html;
    html = $html();

    plugin.accept(html);
  }

  protected final Api.GlobalAttribute dataFrame(String name) {
    Check.notNull(name, "name == null");

    return $html().attribute0(AttributeName.DATA_FRAME, name);
  }

  protected final Api.GlobalAttribute dataFrame(String name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    return $html().attribute0(AttributeName.DATA_FRAME, name + ":" + value);
  }

  protected final Api.GlobalAttribute dataOnClick(Action... actions) {
    return dataOn(AttributeName.DATA_ON_CLICK, actions);
  }

  protected final Api.GlobalAttribute dataOnInput(Action... actions) {
    return dataOn(AttributeName.DATA_ON_INPUT, actions);
  }
  
  private final Api.GlobalAttribute dataOn(AttributeName name, Action... actions) {
    Check.notNull(actions, "actions == null");

    SingleQuotedValue value;
    value = Action.join(actions);

    return $html().attribute0(name, value);
  }

  protected final Api.GlobalAttribute dataWayClick(String text) {
    Check.notNull(text, "text == null");

    return $html().attribute0(AttributeName.DATA_WAY_CLICK, text);
  }

  /**
   * Includes a fragment into this template represented by the specified lambda.
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
  protected final Api.Fragment f(FragmentLambda fragment) {
    return $html().include(fragment);
  }

  protected final <E> Api.Fragment f(final Consumer<E> consumer, final E argument) {
    return f(() -> consumer.accept(argument));
  }

  /**
   * Flattens the specified instructions so that each of the specified
   * instructions is individually added, in order, to a receiving element.
   *
   * <p>
   * This is useful, for example, when creating {@link HtmlComponent} instances.
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
  protected final Api.Element flatten(Api.Instruction... contents) {
    return $html().flatten(contents);
  }

  /**
   * Includes a fragment into this template represented by the specified lambda.
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
  protected final Api.Fragment include(FragmentLambda fragment) {
    return $html().include(fragment);
  }

  /**
   * Includes the specified template into this template.
   *
   * @param template
   *        the template to be included
   *
   * @return an instruction representing the inclusion of the template.
   */
  protected final Api.Fragment include(HtmlTemplate template) {
    Check.notNull(template, "template == null");

    try {
      Html api;
      api = $html();

      int index;
      index = api.fragmentBegin();

      template.html = api;

      template.definition();

      api.fragmentEnd(index);
    } finally {
      template.html = null;
    }

    return Api.FRAGMENT;
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
  protected final Api.NoOp noop() {
    return Api.NOOP;
  }

  protected final Api.Element raw(String text) {
    return $html().raw(text);
  }

  /**
   * Generates a text node with the specified {@code text} value. The text value
   * is escaped before being emitted to the output.
   *
   * <p>
   * The following Objectos HTML template:
   *
   * {@snippet file = "objectos/html/BaseTemplateDslTest.java" region = "text"}
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
  protected final Api.Element t(String text) {
    return $html().text(text);
  }

}
