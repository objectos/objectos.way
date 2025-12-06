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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import objectos.way.Script.Callback;
import objectos.way.Script.StringQuery;

final class ScriptWriter {

  private final List<Object> actions = new ArrayList<>();

  public final ScriptAction build() {
    final String value;
    value = toString();

    return new ScriptAction(value);
  }

  // actions

  public final void delay(int ms, Callback callback) {
    final String call;
    call = toString(callback);

    actions.add(
        """
        ["delay-0",%d,%s]""".formatted(ms, call)
    );
  }

  public final void html(Html.Template template) {
    final String value;
    value = template.toJsonString();

    actions.add(
        """
        ["html-0","%s"]""".formatted(value)
    );
  }

  public final void navigate() {
    actions.add(
        "[navigate-0]"
    );
  }

  public final void pushState(String url) {
    actions.add(
        """
        ["push-state-0","%s"]""".formatted(url)
    );
  }

  public final void replaceState(String url) {
    actions.add(
        """
        ["replace-state-0","%s"]""".formatted(url)
    );
  }

  public final void request(ScriptRequestOptions value) {
    final String method;
    method = value.method.name();

    final Object url;
    url = value.url;

    final String tmpl;
    tmpl = url instanceof ScriptStringQuery
        ? """
        ["request-0","%s",%s,%s]"""
        : """
        ["request-0","%s","%s",%s]""";

    final String onSuccess;
    onSuccess = toString(value.onSuccess);

    actions.add(
        tmpl.formatted(method, url, onSuccess)
    );
  }

  public final void stopPropagation() {
    actions.add(
        "[stop-propagation-0]"
    );
  }

  // element

  public final void elementAction(Object locator, String action) {
    final String elem;
    elem = elementAction(locator);

    actions.add(
        """
        [%s,"%s"]""".formatted(elem, action)
    );
  }

  public final void elementAttr(Object locator, String attrName, String value) {
    final String elem;
    elem = elementMethodInvocation(locator);

    actions.add(
        """
        [%s,"setAttribute","%s","%s"]""".formatted(elem, attrName, value)
    );
  }

  public final void elementAttr(Object locator, String attrName, StringQuery value) {
    final String elem;
    elem = elementMethodInvocation(locator);

    actions.add(
        """
        [%s,"setAttribute","%s",%s]""".formatted(elem, attrName, value)
    );
  }

  public final void elementScroll(int x, int y) {
    writer.actionStart();

    methodInvocation();

    writer.comma();
    writer.stringLiteral("scroll");

    writer.comma();
    writer.intLiteral(x);

    writer.comma();
    writer.intLiteral(y);

    writer.actionEnd();
  }

  @Override
  public final String toString() {
    arrayEnd();

    return out.toString();
  }

  private String elementAction(Object locator) {
    return switch (locator) {
      case null -> "element-2";

      case String id -> "id-2,\"" + id + "\"";

      case StringQuery q -> "id-2," + q;

      default -> throw new IllegalArgumentException("Unknown locator type: " + locator.getClass());
    };
  }

  private String elementMethodInvocation(Object locator) {
    return switch (locator) {
      case null -> "element-1";

      case String id -> "id-1,\"" + id + "\"";

      case StringQuery q -> "id-1," + q;

      default -> throw new IllegalArgumentException("Unknown locator type: " + locator.getClass());
    };
  }

  private String toString(Callback callback) {
    final int startIndex;
    startIndex = actions.size();

    callback.execute();

    final int endIndex;
    endIndex = actions.size();

    if (endIndex > startIndex) {
      final StringBuilder sb;
      sb = new StringBuilder();

      sb.append('[');

      final ListIterator<Object> iter;
      iter = actions.listIterator(startIndex);

      if (iter.hasNext()) {
        sb.append(iter.next());

        iter.remove();

        while (iter.hasNext()) {
          sb.append(',');

          sb.append(iter.next());

          iter.remove();
        }
      }

      sb.append(']');

      return sb.toString();
    } else {
      return "[]";
    }
  }

  final void actionStart() {
    if (next) {
      comma();
    }

    next = true;

    arrayStart();
  }

  final void actionEnd() {
    arrayEnd();
  }

  final void arrayEnd() {
    out.append(']');
  }

  final void arrayStart() {
    out.append('[');
  }

  final void comma() {
    out.append(',');
  }

  final void literal(Object o) {
    switch (o) {
      case Integer i -> intLiteral(i.intValue());

      case String s -> stringLiteral(s);

      default -> throw new IllegalArgumentException("Invalid type for literal: " + o.getClass());
    }
  }

  final void intLiteral(int value) {
    out.append(value);
  }

  final void scriptLiteral(Callback script) {
    final boolean thisNext;
    thisNext = next;

    next = false;

    arrayStart();
    script.execute();
    arrayEnd();

    next = thisNext;
  }

  final void stringLiteral(String s) {
    out.append('"');
    // TODO escape json string literal
    out.append(s);
    out.append('"');
  }

  final void stringLiteralOrQuery(Object o) {
    switch (o) {
      case ScriptStringQuery q -> q.write();

      case String s -> stringLiteral(s);

      default -> throw new IllegalArgumentException("Invalid type: " + o.getClass());
    }
  }

  final void stringQuery(StringQuery value) {
    switch (value) {
      case ScriptStringQuery q -> q.write();
    }
  }

}