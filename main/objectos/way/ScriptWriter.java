/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class ScriptWriter implements Script {

  private final StringBuilder out = new StringBuilder();

  private boolean next;

  public ScriptWriter() {
    arrayStart();
  }

  public final Action build() {
    final String value;
    value = toString();

    return new ScriptAction(value);
  }

  // types

  // queries

  final class ElementQuery implements Script.Element {

    @Override
    public final ElementMethodInvocation getAttribute(String name) {
      Objects.requireNonNull(name, "name == null");

      return new ElementMethodInvocation("getAttribute", name);
    }

  }

  final class ElementMethodInvocation implements Script.StringQuery {

    private final String methodName;

    private final Object argumentOrList;

    public ElementMethodInvocation(String methodName, Object argumentOrList) {
      this.methodName = methodName;

      this.argumentOrList = argumentOrList;
    }

    final void write() {
      arrayStart();

      // arg[0] = instruction
      stringLiteral("element-1");

      // arg[1] = method name
      comma();
      stringLiteral(methodName);

      // arg[rest] = args
      if (argumentOrList instanceof List<?> list) {
        for (Object o : list) {
          comma();
          literal(o);
        }
      } else {
        comma();
        stringLiteral(argumentOrList.toString());
      }

      arrayEnd();
    }

  }

  private ElementQuery element;

  @Override
  public final Script.Element element() {
    if (element == null) {
      element = new ElementQuery();
    }

    return element;
  }

  // actions

  @Override
  public final void delay(int ms, Consumer<Script> callback) {
    Objects.requireNonNull(callback, "callback == null");

    actionStart();
    stringLiteral("delay-0");
    comma();
    intLiteral(ms);
    comma();
    scriptLiteral(callback);
    actionEnd();
  }

  @Override
  public final void html(Html.Template template) {
    final String value = template.toJsonString();

    actionStart();
    stringLiteral("html-0");
    comma();
    stringLiteral(value);
    actionEnd();
  }

  @Override
  public final void navigate() {
    actionStart();
    stringLiteral("navigate-0");
    actionEnd();
  }

  @Override
  public final void pushState(String url) {
    Objects.requireNonNull(url, "url == null");

    actionStart();
    stringLiteral("push-state-0");
    comma();
    stringLiteral(url);
    actionEnd();
  }

  final class RequestConfig implements Script.RequestConfig {

    private Script.Method method = Script.GET;

    private Object url;

    private Consumer<Script> onSuccess = (script) -> {};

    @Override
    public final void method(Script.Method method) {
      this.method = Objects.requireNonNull(method, "method == null");
    }

    @Override
    public final void url(String value) {
      url = Objects.requireNonNull(value, "value == null");
    }

    @Override
    public final void url(Script.StringQuery value) {
      url = Objects.requireNonNull(value, "value == null");
    }

    @Override
    public final void onSuccess(Consumer<Script> config) {
      onSuccess = Objects.requireNonNull(config, "config == null");
    }

    final void write() {
      if (url == null) {
        throw new IllegalArgumentException("URL was not set");
      }

      actionStart();

      // action id
      stringLiteral("request-0");

      // arg[0] = method
      comma();
      stringLiteral(method.name());

      // arg[1] = url
      comma();
      if (url instanceof ElementMethodInvocation like) {
        like.write();
      } else {
        stringLiteral(url.toString());
      }

      // arg[2] = onSuccess
      comma();
      scriptLiteral(onSuccess);

      actionEnd();
    }

  }

  @Override
  public final void request(Consumer<Script.RequestConfig> config) {
    Objects.requireNonNull(config, "RequestConfig> config == null");

    final RequestConfig delegate;
    delegate = new RequestConfig();

    config.accept(delegate);

    delegate.write();
  }

  @Override
  public final void setAttribute(Html.Id id, String name, String value) {
    final String _id = id.value();
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    actionStart();
    stringLiteral("set-attribute-0");
    comma();
    stringLiteral(_id);
    comma();
    stringLiteral(name);
    comma();
    stringLiteral(value);
    actionEnd();
  }

  @Override
  public final void stopPropagation() {
    actionStart();
    stringLiteral("stop-propagation-0");
    actionEnd();
  }

  @Override
  public final void submit(Html.Id id) {
    final String _id = id.value();

    actionStart();
    stringLiteral("submit-0");
    comma();
    stringLiteral(_id);
    actionEnd();
  }

  @Override
  public final void toggleClass(Html.Id id, String className) {
    final String _id = id.value();
    Objects.requireNonNull(className, "className == null");

    actionStart();
    stringLiteral("toggle-class-0");
    comma();
    stringLiteral(_id);
    comma();
    stringLiteral(className);
    actionEnd();
  }

  @Override
  public final void toggleClass(Html.Id id, String class1, String class2) {
    final String _id = id.value();
    Objects.requireNonNull(class1, "class1 == null");
    Objects.requireNonNull(class2, "class2 == null");

    actionStart();
    stringLiteral("toggle-class-0");
    comma();
    stringLiteral(_id);
    comma();
    stringLiteral(class1);
    comma();
    stringLiteral(class2);
    actionEnd();
  }

  @Override
  public final String toString() {
    arrayEnd();

    return out.toString();
  }

  private void actionStart() {
    if (next) {
      comma();
    }

    next = true;

    arrayStart();
  }

  private void actionEnd() {
    arrayEnd();
  }

  private void arrayEnd() {
    out.append(']');
  }

  private void arrayStart() {
    out.append('[');
  }

  private void comma() {
    out.append(',');
  }

  private void literal(Object o) {
    switch (o) {
      case Integer i -> intLiteral(i.intValue());

      case String s -> stringLiteral(s);

      default -> throw new IllegalArgumentException("Invalid type for literal: " + o.getClass());
    }
  }

  private void intLiteral(int value) {
    out.append(value);
  }

  private void scriptLiteral(Consumer<Script> script) {
    final boolean thisNext;
    thisNext = next;

    next = false;

    arrayStart();
    script.accept(this);
    arrayEnd();

    next = thisNext;
  }

  private void stringLiteral(String s) {
    out.append('"');
    // TODO escape json string literal
    out.append(s);
    out.append('"');
  }

}