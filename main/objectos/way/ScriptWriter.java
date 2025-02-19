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

  private enum ElementQueryKind {

    ELEMENT,

    BY_ID;

  }

  final class ElementQuery implements Script.Element {

    private final ElementQueryKind kind;

    private final Object value;

    ElementQuery(ElementQueryKind kind) {
      this.kind = kind;

      this.value = null;
    }

    ElementQuery(ElementQueryKind kind, String value) {
      this.kind = kind;

      this.value = value;
    }

    ElementQuery(ElementQueryKind kind, StringQuery value) {
      this.kind = kind;

      this.value = value;
    }

    @Override
    public final ElementMethodInvocation attr(Html.AttributeName name) {
      final String _name = name.name();

      return new ElementMethodInvocation(this, "getAttribute", _name);
    }

    @Override
    public final void attr(Html.AttributeName name, StringQuery value) {
      String _name = name.name();
      Objects.requireNonNull(value, "value == null");

      actionStart();

      elementAction();

      comma();
      stringLiteral("attr-0");

      comma();
      stringLiteral(_name);

      comma();
      stringQuery(value);

      actionEnd();
    }

    @Override
    public final void attr(Html.AttributeName name, String value) {
      String _name = name.name();
      Objects.requireNonNull(value, "value == null");

      actionStart();

      methodInvocation();

      comma();
      stringLiteral("setAttribute");

      comma();
      stringLiteral(_name);

      comma();
      stringLiteral(value);

      actionEnd();
    }

    @Override
    public final void toggleClass(String classes) {
      Check.argument(!classes.isBlank(), "Classes to toggle must not be blank");

      final String[] parts;
      parts = classes.split(" ");

      actionStart();

      elementAction();

      comma();
      stringLiteral("toggle-class-0");

      for (var part : parts) {
        comma();
        stringLiteral(part);
      }

      actionEnd();
    }

    @Override
    public final void scroll(int x, int y) {
      actionStart();

      methodInvocation();

      comma();
      stringLiteral("scroll");

      comma();
      intLiteral(x);

      comma();
      intLiteral(y);

      actionEnd();
    }

    @Override
    public final void submit() {
      actionStart();

      elementAction();

      comma();
      stringLiteral("submit-0");

      actionEnd();
    }

    final void elementAction() {
      switch (kind) {
        case ELEMENT -> stringLiteral("element-2");

        case BY_ID -> {
          stringLiteral("id-2");
          comma();
          stringLiteralOrQuery(value);
        }
      }
    }

    final void methodInvocation() {
      switch (kind) {
        case ELEMENT -> stringLiteral("element-1");

        case BY_ID -> {
          stringLiteral("id-1");
          comma();
          stringLiteralOrQuery(value);
        }
      }
    }

  }

  final class ElementMethodInvocation implements Script.StringQuery {

    private final ElementQuery query;

    private final String methodName;

    private final Object argumentOrList;

    public ElementMethodInvocation(ElementQuery query, String methodName, Object argumentOrList) {
      this.query = query;

      this.methodName = methodName;

      this.argumentOrList = argumentOrList;
    }

    final void write() {
      arrayStart();

      // instruction
      query.methodInvocation();

      // method name
      comma();
      stringLiteral(methodName);

      // args
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
      element = new ElementQuery(ElementQueryKind.ELEMENT);
    }

    return element;
  }

  @Override
  public final Element elementById(Html.Id id) {
    final String _id = id.value();

    return new ElementQuery(ElementQueryKind.BY_ID, _id);
  }

  @Override
  public final Element elementById(StringQuery id) {
    Objects.requireNonNull(id, "id == null");

    return new ElementQuery(ElementQueryKind.BY_ID, id);
  }

  // actions

  @Override
  public final void delay(int ms, Callback callback) {
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

  @Override
  public final void replaceState(String url) {
    Objects.requireNonNull(url, "url == null");

    actionStart();
    stringLiteral("replace-state-0");
    comma();
    stringLiteral(url);
    actionEnd();
  }

  final class RequestConfig implements Script.RequestConfig {

    private Script.Method method = Script.GET;

    private Object url;

    private Callback onSuccess = () -> {};

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
    public final void onSuccess(Callback callback) {
      onSuccess = Objects.requireNonNull(callback, "callback == null");
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
  public final void stopPropagation() {
    actionStart();
    stringLiteral("stop-propagation-0");
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

  private void scriptLiteral(Callback script) {
    final boolean thisNext;
    thisNext = next;

    next = false;

    arrayStart();
    script.execute();
    arrayEnd();

    next = thisNext;
  }

  private void stringLiteral(String s) {
    out.append('"');
    // TODO escape json string literal
    out.append(s);
    out.append('"');
  }

  private void stringLiteralOrQuery(Object o) {
    switch (o) {
      case ElementMethodInvocation invocation -> invocation.write();

      case String s -> stringLiteral(s);

      default -> throw new IllegalArgumentException("Invalid type: " + o.getClass());
    }
  }

  private void stringQuery(StringQuery value) {
    switch (value) {
      case ElementMethodInvocation invocation -> invocation.write();
    }
  }

}