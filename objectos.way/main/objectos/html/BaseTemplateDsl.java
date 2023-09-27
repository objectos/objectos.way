/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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

import objectos.html.internal.Ambiguous;
import objectos.html.internal.AttributeName;
import objectos.html.internal.HtmlTemplateApi;
import objectos.html.internal.InternalFragment;
import objectos.html.internal.InternalInstruction;
import objectos.html.internal.InternalNoOp;
import objectos.html.internal.StandardElementName;
import objectos.html.tmpl.Api;
import objectos.html.tmpl.FragmentLambda;
import objectos.lang.Check;

/**
 * Provides utility methods for Objectos HTML templates.
 */
public sealed abstract class BaseTemplateDsl
    extends BaseElementDsl
    permits HtmlComponent, HtmlTemplate {

  BaseTemplateDsl() {}

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
  protected final Api.ElementContents flatten(Api.Instruction... contents) {
    Check.notNull(contents, "contents == null");

    HtmlTemplateApi api;
    api = api();

    api.flattenBegin();

    for (int i = 0; i < contents.length; i++) {
      Api.Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      api.elementValue(inst);
    }

    api.elementEnd();

    return InternalInstruction.INSTANCE;
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
    Check.notNull(fragment, "fragment == null");

    api().fragment(fragment);

    return InternalFragment.INSTANCE;
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
      template.api = api();

      template.definition();
    } finally {
      template.api = null;
    }

    return InternalFragment.INSTANCE;
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
    return InternalNoOp.INSTANCE;
  }

  protected final Api.ElementContents raw(String text) {
    Check.notNull(text, "text == null");

    api().raw(text);

    return InternalInstruction.INSTANCE;
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
  protected final Api.ElementContents t(String text) {
    Check.notNull(text, "text == null");

    api().text(text);

    return InternalInstruction.INSTANCE;
  }

  @Override
  final void ambiguous(Ambiguous name, String text) {
    api().ambiguous(name, text);
  }

  @Override
  final void attribute(AttributeName name) {
    api().attribute(name);
  }

  @Override
  final void attribute(AttributeName name, String value) {
    HtmlTemplateApi api;
    api = api();

    api.attribute(name, value);
  }

  @Override
  final void element(StandardElementName name, Api.Instruction[] contents) {
    HtmlTemplateApi api;
    api = api();

    api.elementBegin(name);

    for (int i = 0; i < contents.length; i++) {
      Api.Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      api.elementValue(inst);
    }

    api.elementEnd();
  }

  @Override
  final void element(StandardElementName name, String text) {
    HtmlTemplateApi api;
    api = api();

    api.text(text);

    api.elementBegin(name);
    api.elementValue(InternalInstruction.INSTANCE);
    api.elementEnd();
  }

}